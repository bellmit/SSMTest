/*! 地图图例功能
 *  v1.0:
 *  - 显示地图服务图例(ArcServer 10.1以上) or 用户自定义图例(v1.0 仅支持色块展示,优先级更高)
 *  - 配置说明：
 *      respectCurrentMapScale: 图例是否随着地图比例尺范围的变化而变化 默认true;
 *      autoUpdate: 地图服务变化后，是否自动更新图例,默认true，false时，模块会提供刷新按钮，用户可手动刷新
 *      excludes: 排除某些地图服务 eg "xxx,yyy,zzz" (服务的id)
 *   Todo(v1.1):
 *   - 支持同时显示服务图例与自定义图例
 *   - 自定义图例支持更多样式定制
 *
 *  配置示例:
 *      {
 *                "map": {
 *                  "enable": true,
 *                  "respectCurrentMapScale": true,
 *                  "autoUpdate": true
 *                },
 *                "custom": {
 *                  "enable": false,
 *                  "data": [
 *                    {
 *                      "label": "水田",
 *                      "color": "#0092da"
 *                    },
 *                    {
 *                      "label": "道路",
 *                      "color": "#1E90FF"
 *                    }
 *                  ]
 *                }
 *        }
 *
 * Author: yingxiufeng
 * Date:   2017/6/9
 * Version:v1.0 (c) Copyright gtmap Corp.2017
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/Stateful",
    "esri/lang",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/layer",
    "hbarsUtils",
    "map/core/BaseWidget",
    "layer"], function (declare, lang, arrayUtil, Stateful, esriLang, ArcGISDynamicMapServiceLayer,
                        ArcGISTiledMapServiceLayer, Layer, HbarsUtils, BaseWidget, layer) {

    var _map, _widgetData;

    var legend = declare([BaseWidget], {

        constructor: function () {

        },

        onCreate: function () {
            _map = this.getMap().map();
            _widgetData = this.getConfig();
            _init();
            _addListeners();
            mapLayers=_map.layerIds.toString();
        },

        onPause: function () {

        },

        onDestroy: function () {

        }

    });
    /**
     * 初始化
     * @private
     */
    function _init() {
       $legendBtn=$("#legendBtn");
       $legendShow=$("#legendShow");
        EventBus.listener(EventBus.WIDGETS_LOADED, function () {
            setTimeout(function () {
                _initComponent();
                _update();
            }, 200);
        });
    }

    var legendMode = 'map';// map/custom/mixed
    var $legendContext, $legendTools;
    var mapLegendConf, customLegendConf;
    var stateObj;
    var loadHandler;
    var $legendBtn,$legendShow;
    var mapLayers;


    /**
     * 根据配置初始化组件页面
     * @private
     */
    function _initComponent() {
        mapLegendConf = _widgetData.map;
        customLegendConf = _widgetData.custom;
        legendMode = customLegendConf.enable === true ? 'custom' : 'map';

        $legendContext = $(".legend-content");
        $legendTools = $(".legend-tools");

        stateObj = new Stateful();
        stateObj.watch("legends", function () {
            $legendContext.empty();
            if (this.legends.length > 0) {
                $legendContext.append(HbarsUtils.renderTpl(legendMode === 'map' ? $("#mapLegendTpl").html() : $("#customLegendTpl").html(), {legends: this.get("legends")}));
            }
        });
        stateObj.set("legends", legendMode === 'map' ? [] : customLegendConf.data);

        if (legendMode === 'map') {
            if (!mapLegendConf.autoUpdate) {
                $legendTools.removeClass("hidden");
                $("#legendRefreshBtn").on('click', function () {
                    _update();
                });
            } else {
                //监听地图变化,自动更新图例
                _map.on('layers-reordered',function () {
                    if(_map.layerIds.toString()!==mapLayers){
                        _update();
                        mapLayers=_map.layerIds.toString();
                    }
                })
            }

        }

    }

    /***
     * 更新图例
     * @private
     */
    function _update() {
        switch (legendMode) {
            case 'map':
                stateObj.set("legends", []);
                if (_map.layerIds.length > 0) {
                    loadHandler = layer.load(2, {
                        offset: getOffset(),
                        time: _map.layerIds.length * 1000
                    });
                    var ids = _map.layerIds;
                    var ex = _widgetData.map.excludes;
                    if(ex != undefined) {
                       ids = arrayUtil.filter(ids, function (item) {
                            return ex.indexOf(item) < 0;
                        });
                    }
                    // arrayUtil.forEach(ids, function (sId) {

                    var temp=ids[ids.length-1];
                        var lyr = _map.getLayer(temp);
                        var legendUrl = lyr.url + "/legend?f=json";
                        if (!legendUrl.startWith("http://")) {
                            legendUrl = window.location.origin + legendUrl;
                        }
                        $.ajax({
                            url: '/omp/map/proxy',
                            data: {requestUrl: legendUrl},
                            success: function (res) {
                                var r = $.parseJSON(res);
                                if (r.hasOwnProperty("layers")) {
                                    var lyrLegends = [];
                                    arrayUtil.forEach(r.layers, function (lyr) {
                                        arrayUtil.forEach(lyr.legend, function (item) {
                                            // 排除相同的 避免重复添加
                                            var arr = arrayUtil.filter(stateObj.get("legends"), function (legend) {
                                                return legend.url === item.url;
                                            });

                                            if (arr.length === 0) {
                                                if(item.label === '') item.label = lyr.layerName;
                                                lyrLegends.push(item);
                                            }

                                        });
                                    });
                                    stateObj.set("legends", lyrLegends.concat(stateObj.get("legends")));
                                }
                            },
                            error: function (ex) {
                                layer.close(loadHandler);
                                console.error(ex);
                            }
                        });
                    // });
                }
                break;

        }
    }

    function getOffset() {
        var offset = $("#Legend").offset();
        return [offset.top + 20, offset.left + 80];
    }

    function _addListeners() {
        $legendBtn.on('click', function () {
            var $this = $(this);
            if ($this.hasClass("omp-switch-on")) {
                $this.removeClass("omp-switch-on");
                $this.find('em').text("关");
                $this.attr('title', '打开功能');
                $legendShow.attr("style","display:none;");
            } else {
                $this.addClass("omp-switch-on");
                $this.find('em').text("开");
                $this.attr('title', '关闭功能');
                $legendShow.attr("style","display:block;");
            }
        });
    }

    return legend;
});
