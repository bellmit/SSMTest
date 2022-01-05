/**
 * 通透镜以及地图卷帘功能
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/25 13:38
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "map/core/BaseWidget",
    "map/component/SpyTool",
    "map/component/SwipeTool",
    "mustache",
    "layer",
    "map/core/support/Environment",
    "icheck",
    "static/thirdparty/semantic-ui/checkbox/checkbox",
    "css!static/thirdparty/icheck/icheck.css",
    "css!static/thirdparty/semantic-ui/checkbox/checkbox.css"], function (declare, lang, arrayUtil, BaseWidget, SpyTool, SwipeTool, Mustache, layer,Environment) {
    var _map;
    var spyTool, swipeTool;
    var me = declare([BaseWidget], {

        onCreate: function () {
            _init();
        },
        onOpen: function () {
            _addCheckListener();
        },
        onPause: function () {
            _pause();
        },
        /**
         *
         */
        onDestroy:function(){
            spyTool.deactivate();
            swipeTool.deactivate();
        }

    });
    var spyData = [];//用于存储选择的图层数据结构： [{id:'sss',name:'test',layer:{}}]
    var tmpl_lyr = "<option value='{{id}}'>{{alias}}</option>";
    var $layersSelect;

    /***
     *
     * @private
     */
    function _init() {
        _map = map.map();
        layer.config();
        spyTool = new SpyTool({map: _map});
        swipeTool = new SwipeTool({map: _map});
        $layersSelect=$("#layers_select");
        //初始化select控件
        refreshSpyLyrs();
        _addCheckListener();
        //监听地图图层变化
        _map.on('layer-add', function () {
            refreshSpyLyrs();
        });
        _map.on('layer-remove', function () {
            refreshSpyLyrs();
        });
    }

    /**
     * 刷新图层列表
     */
    function refreshSpyLyrs(){
        $layersSelect.empty();
        var configLayers = appConfig.map.operationalLayers;
        spyData = arrayUtil.map(_map.layerIds, function (item, idx) {
            var l = _map.getLayer(item);
            var s = arrayUtil.filter(configLayers, function (s) {
                return s.id === item;
            })[0];
            if (s != undefined) {
                return {id: item, alias: s.alias, layer: l, index: idx, type: s.type};
            }
        });
        if (spyData.length > 0) {
            arrayUtil.forEach(spyData, function (d) {
                Mustache.parse(tmpl_lyr);
                $layersSelect.append(Mustache.render(tmpl_lyr, d));
            });
            spyTool.setLayer(spyData[0]);
            swipeTool.setLayer(spyData[0]);
            swipeTool.setDirection("horizontal");  //水平： horizontal 竖直：vertical 同时：both
            $layersSelect.on('change', function () {
                if($('.swipeToolDiv')) $('.swipeToolDiv').remove();
                var id = this.value;
                var obj = arrayUtil.filter(spyData, function (item) {
                    return item.id === id;
                })[0];
                spyTool.setLayer(obj);
                swipeTool.setLayer(obj);
                swipeTool.setDirection("horizontal");//水平： horizontal 竖直：vertical 同时：both
            });
        }
    }
    /***
     * listener checkbox
     * @private
     */
    function _addCheckListener() {
        if (Environment.isWebkit()) {
            $(".checkbox-for-spy").checkbox({
                onChecked: function () {
                    var name = $(this)[0].name;
                    if (name === 'spy') {
                        $(".checkbox-for-spy").eq(1).checkbox('uncheck');
                        if (swipeTool.isActive() === true)
                            swipeTool.deactivate();
                        spyTool.activate();
                    }
                    else {
                        $(".checkbox-for-spy").eq(0).checkbox('uncheck');
                        if (spyTool.isActive() === true)
                            spyTool.deactivate();
                        swipeTool.activate();
                    }
                },
                onUnchecked: function () {
                    var t = $(this)[0].name;
                    if (t === 'spy') {
                        spyTool.deactivate();
                    } else {
                        swipeTool.deactivate();
                    }
                }
            });
        } else {
            $(".opera-container div").each(function () {
                $(this).removeClass("ui slider");
                $(this).removeClass("col-sm-offset-1");
                $(this).addClass("pull-left");
            });
            $(".opera-container input").iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%' // optional
            });
            $(".checkbox-for-spy").css('width', '108px');

            $(".opera-container input").on('ifChecked', function () {
                var name = $(this)[0].name;
                if (name === 'spy') {
                    $(".checkbox-for-spy").eq(1).iCheck('uncheck');
                    if (swipeTool.isActive() === true)
                        swipeTool.deactivate();
                    spyTool.activate();
                }
                else {
                    $(".checkbox-for-spy").eq(0).iCheck('uncheck');
                    if (spyTool.isActive() === true)
                        spyTool.deactivate();
                    swipeTool.activate();
                }
            });

            $(".opera-container input").on('ifUnchecked', function () {
                var t = $(this)[0].name;
                if (t === 'spy') {
                    spyTool.deactivate();
                } else {
                    swipeTool.deactivate();
                }
            });

        }
    }
    /***
     *
     * @private
     */
    function _pause() {
        if (spyTool.isActive()) {
            spyTool.deactivate();
            $(".checkbox").eq(0).checkbox('uncheck');
            if ($(".checkbox-for-spy") != undefined) {
                $(".checkbox-for-spy").eq(0).iCheck('uncheck');
            }
        }
        if (swipeTool.isActive()) {
            swipeTool.deactivate();
            $(".checkbox").eq(1).checkbox('uncheck');
            if ($(".checkbox-for-spy") != undefined) {
                $(".checkbox-for-spy").eq(1).iCheck('uncheck');
            }
        }
    }

    lang.mixin({
        SPY: 'spy',
        SWIPE: 'swipe'
    });

    return me;
});