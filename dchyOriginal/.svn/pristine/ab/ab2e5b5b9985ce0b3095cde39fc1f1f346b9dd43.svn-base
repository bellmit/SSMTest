/*!
 * 地图采集功能（常州订制）
 * - 采集点/线/面
 * - 进行属性识别（图层配置）
 * - post 到配置的 url
 * widget config:
 * {
 *   "drawTypes": "point,polygon",
 *   "postUrl": "http://xxx",
 *   "layers": [{"layerUrl": "", "fields": "XZQDM,DKMC,PRONAME", "enable": true}]
 * }
 * Author: yingxiufeng
 * Date:   2018/3/7
 * Version:v1.0 (c) Copyright gtmap Corp.2018
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/on",
    "esri/toolbars/draw",
    "esri/graphic",
    "esri/lang",
    "map/core/QueryTask",
    "esri/tasks/FeatureSet",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "map/core/JsonConverters",
    "map/core/EsriSymbolsCreator",
    "layer",
    "hbarsUtils",
    "map/component/ListDataRenderer",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/Color",
    "map/core/BaseWidget"], function (declare, arrayUtil, lang, on, Draw, Graphic, esriLang, QueryTask, FeatureSet, MapUtils, MapPopup,
                                      JsonConverters, EsriSymbolsCreator, layer, HbarsUtils, ListDataRenderer, SimpleMarkerSymbol, Color, BaseWidget) {

    var _map, _config;
    var mapPopup = MapPopup.getInstance();

    var me = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {
        },
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            _config = this.getConfig();
            _init();
        },
        onOpen: function () {

        },
        onPause: function () {
            if (mapPopup.isShowing) mapPopup.closePopup();
            _map.graphics.clear();
        },
        onDestroy: function () {
            if (mapPopup.isShowing) mapPopup.closePopup();
        }

    });
    var drawTool, drawHandler;
    var returnGeometry;
    var returnFieldsData = [];
    var $collectResultPanel, $collectDrawPanel, $collectOptPanel;

    var STYLE_MARKER_CONSTANT = {
        STYLE_CIRCLE: "circle",
        STYLE_SQUARE: "square",
        STYLE_CROSS: "cross",
        STYLE_X: "x",
        STYLE_DIAMOND: "diamond",
        STYLE_TARGET: "target"
    };

    // 可提供的绘制类型
    var drawTypes = [{
        name: 'point',
        alias: '绘点'
    }, {
        name: 'polyline',
        alias: '绘线'
    }, {
        name: 'polygon',
        alias: '绘面'
    }];

    var drawTypeTpl = '<div class="col-sm-{{colLen}} col-xs-{{colLen}} text-center">' +
        '<div class="{{name}}-choose spatial-geo-icon" data-type="{{name}}" title="{{alias}}" style="height: 47px;width: 47px"></div></div>';

    /**
     * 初始化参数等
     * @private
     */
    function _init() {
        layer.config();
        Date.prototype.toLocaleString = function () {
            return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 ";
        };
        //初始化jq变量
        $collectDrawPanel = $("#collectDrawPanel");
        $collectResultPanel = $("#collectResultPanel");
        $collectOptPanel = $("#collectOptPanel");
        //根据配置初始化界面绘制类型
        var _drawTypes = _config.drawTypes;
        arrayUtil.forEach(drawTypes, function (item) {
            if (_drawTypes != undefined && _drawTypes.indexOf(item.name) > -1) {
                // render
                $collectDrawPanel.append(HbarsUtils.renderTpl(drawTypeTpl, lang.mixin(item, {colLen: 12 / (_drawTypes.split(',').length)})));
            }
        });
        // 增加按钮监听事件
        $collectDrawPanel.find('.spatial-geo-icon').on('click', function () {
            spatialClickHandler($(this).data("type"));
        });
        //取消按钮点击事件
        $collectOptPanel.find('.btn-default').on('click', function () {
            clear();
            $collectResultPanel.empty();
            returnFieldsData = [];
        });
        //提交按钮点击事件
        $collectOptPanel.find('.btn-primary').on('click', function () {
            submitData();
        });
    }

    /**
     * 提交数据
     */
    function submitData() {
        // var returnData = [];
        // returnData.push(returnFieldsData);
        // returnData.push(JsonConverters.toGeoJson(returnGeometry));

        var checkValues = [];

        $('#collectResultPanel input:checked').each(function (index, el) {
            var value = $(el).val();
            value = value.replace('图幅号:', '');
            checkValues.push({
                MAP: value
            });
        });

        if (checkValues.length == 0) {
            checkValues = returnFieldsData;
        }

        var postData = {
            geo: {type: 'Feature', geometry: JsonConverters.toGeoJson(returnGeometry)},
            attrs: checkValues,
            proid: getUrlParams().proid
        };
        $.ajax({
            url: _config.postUrl
            , data: {postData: JSON.stringify(postData)}
            , success: function (r) {
                if (r) {
                    layer.msg("提交成功！");
                } else {
                    layer.msg("提交失败！");
                }
            }
        });
    }

    /**
     * 空间查询Handler
     * @param type
     */
    function spatialClickHandler(type) {
        if (drawTool != undefined) drawTool.deactivate();
        if (drawHandler != undefined) drawHandler.remove();
        drawTool = drawTool ? drawTool : new Draw(_map);
        drawHandler = on(drawTool, "draw-end", lang.hitch(this, onDrawEnd));
        highLight(type);
        switch (type) {
            case "point":
                drawTool.activate(Draw.POINT);
                break;
            case "polyline":
                drawTool.activate(Draw.POLYLINE);
                break;
            case "polygon":
                drawTool.activate(Draw.POLYGON);
                break;
            default:
                console.error(type + "is unsupported yet!");
                break;
        }
    }

    /**
     * 高亮要素
     * @param type
     */
    function highLight(type) {
        $collectDrawPanel.find('.spatial-geo-icon').removeClass("highLight");
        $collectDrawPanel.find("." + type + "-choose").addClass("highLight");
    }

    /**
     * 执行空间查询
     * @param evt
     */
    function onDrawEnd(evt) {
        drawHandler.remove();
        drawTool.deactivate();
        var geo = evt.geometry;
        returnGeometry = evt.geometry;
        // 显示绘制图形
        _map.graphics.add(new Graphic(evt.geometry, getSymbol(geo.type)));
        // 获取配置图层
        var layers = _config.layers;
        var agsGeoStr = JSON.stringify(geo);
        arrayUtil.forEach(layers, function (item) {
            if (item.enable === true) {
                var returnFieldsStr = "";
                arrayUtil.forEach(item.fields, function (n1, i1) {
                    if (i1 === item.fields.length - 1) {
                        returnFieldsStr = returnFieldsStr.concat(n1.name);
                    } else {
                        returnFieldsStr = returnFieldsStr.concat(n1.name).concat(",");
                    }
                });
                var data = {
                    layerUrl: item.layerUrl
                    , geometry: agsGeoStr
                    , returnFields: returnFieldsStr
                    , extraParams: JSON.stringify({
                        inSR: geo.spatialReference.wkid,
                        spatialRel:'esriSpatialRelIntersects',
                        returnGeometry:false,
                        returnIdsOnly:false,
                        returnCountOnly:false,
                        returnZ:false,
                        returnM:false
                    })
                };
                $.ajax({
                    url: "/omp/map/query"
                    , data: data
                    , type: 'POST'
                    , success: function (r) {
                        var ret = $.parseJSON(r);
                        if (ret.hasOwnProperty('msg')) {
                            layer.msg(ret.msg, {icon: 0});
                            return;
                        }
                        if (ret.features.length > 0) {
                            parseQueryResult(ret);
                        }
                    }
                });

            }
        });
    }

    /**
     * 解析空间查询结果
     * @param r
     * @param data
     */
    function parseQueryResult(r) {
        {
            var attrs = [];//存放要进行展示的属性集合
            var layerObj = null; //选定的图层对象
            arrayUtil.forEach(_config.layers, function (layerItem, n) {
                layerObj = layerItem;
                //一级标题
                var tf = layerItem.titleField;
                //二级标题
                var sf = getSubTitleField(layerItem);
                var rf = layerItem.returnFields;
                var graphics = [];
                arrayUtil.forEach(r.features, function (feature, i) {
                    //返回属性数据
                    returnFieldsData.push(feature.attributes);
                    arrayUtil.forEach(rf, function (r) {
                        if (r.type == "DATA" && feature.attributes[r.name]) {
                            feature.attributes[r.name] = new Date(feature.attributes[r.name]).toLocaleString();
                        }
                    });
                    var titleText = esriLang.substitute({
                        alias: tf.alias
                        , value: feature.attributes[tf.name] || '空'
                    }, '${alias}:${value}');
                    var subTitleText = esriLang.isDefined(sf) ? esriLang.substitute({
                        alias: sf.alias
                        , value: feature.attributes[sf.name] || '空'
                    }, '${alias}:${value}') : '';
                    var g = r.features[i];
                    var geo = g.geometry;
                    if(typeof(geo) != 'undefined'){
                        if (!geo.spatialReference) {
                            geo.spatialReference = _map.spatialReference;
                        }
                    }
                    var graphic = new Graphic(g);
                    graphics.push(graphic);
                    attrs.push({
                        title: titleText
                        , subtitle: subTitleText
                        , graphic: graphic
                    });
                });
            });
            //渲染模板显示结果
            // console.log(attrs);
            var listDataRenderer = new ListDataRenderer({
                renderTo: $('#collectResultPanel'),
                type: "checkbox",
                map: _map,
                renderData: attrs
            });
            listDataRenderer.on('location', function (data) {
                doLocation(data.graphic);
            });

            listDataRenderer.render();

        }
    }

    /***
     * 定位图形
     * @param g
     */
    function doLocation(g) {
        var geo = g.geometry;
        var geoType = geo.type;
        var geoCenter;
        switch (geoType) {
            case 'point':
                geoCenter = geo;
                break;
            case 'polyline':
            case 'polygon':
                geoCenter = geo.getExtent().getCenter();
                break;
        }
        geoCenter = lang.mixin(geoCenter, {spatialReference: _map.spatialReference});
        var graphic = new Graphic(lang.mixin(geo, {spatialReference: _map.spatialReference}));
        graphic.setAttributes(g.attributes);
        MapUtils.setMap(_map);
        MapUtils.highlightFeatures([graphic], false, 1.0);

    }

    /**
     * get symbol by geo type
     * @param type
     * @returns {*}
     */
    function getSymbol(type) {
        switch (type) {
            case 'point':
                return new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 12, null, new Color([255, 0, 0, 1]));
                break;
            case 'polyline':
                return EsriSymbolsCreator.defaultLineSymbol;
                break;
            default:
                return EsriSymbolsCreator.defaultFillSymbol;
        }
    }


    /**
     * 设置二级标题字段
     * 非一级标题字段
     * @param lyr
     * @returns {undefined}
     */
    function getSubTitleField(lyr) {
        var tf = lyr.titleField;
        var rf = lyr.returnFields;
        arrayUtil.forEach(rf, function (item) {
            if (esriLang.isDefined(item) && item.name != tf.name) {
                return item;
            }
        });
        return undefined;
    }

    /**
     * 清除
     */
    function clear() {
        _map.graphics.clear();
    }

    return me;
});