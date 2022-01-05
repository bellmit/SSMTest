/**
 * 清除缓存
 * 并可以打印输出添加标记后的地图 以及添加动态标绘 并结合分析 进行扩展
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "mustache",
    "map/core/BaseWidget",
    "layer",
    "slimScroll",
    'esri/toolbars/draw',
    "esri/layers/GraphicsLayer",
    "esri/Color",
    "map/manager/LayoutManager",
    "esri/graphic",
    "esri/tasks/GeometryService",
    "esri/tasks/BufferParameters",
    "esri/SpatialReference",
    "map/core/JsonConverters",
    "esri/geometry/Point",
    "esri/symbols/TextSymbol",
    "map/component/MapInfoWindow",
    "map/core/EsriSymbolsCreator",
    "static/js/cfg/core/SerializeForm",
    "static/js/UUIDUtils",
    "static/thirdparty/icheck/icheck",
    "ko",
    "text!map/template/mark/mark-info-form.html",
    "text!map/template/mark/mark-add.html"], function (declare, lang, Mustache, BaseWidget, layer,
                                                       slimScroll, Draw, GraphicsLayer, Color, LayoutManager,
                                                       Graphic, GeometryService, BufferParameters, SpatialReference, JsonConverters, Point, TextSymbol, MapInfoWindow,
                                                       EsriSymbolsCreator, SerializeForm, uuid, icheck, ko, MarkInformation, MarkAddTpl) {
    var __map;
    var graphicsLayer = new GraphicsLayer({id: 'markPoints'});

    var viewModel; //ko view-model

    var me = declare([BaseWidget], {
        /***
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            _init();
        },
        /***
         *
         */
        onOpen: function () {
            _init();
        },
        /***
         *
         */
        onPause: function () {

        },
        /**
         *
         */
        onDestroy: function () {

        }

    });

    /***
     * init
     * @private
     */
    function _init() {

        //添加查询事件
        $('#clearServiceBtn').on('click', function () {
            var url=root+"/map/"+tpl+'/clearServiceCache';
            if (url != undefined) {
                layer.msg("执行中...", {time: 3000, shade: 0.35, shadeClose: !1});
                $.ajax({
                    url: url,
                    complete: function (evt) {
                        var r = $.parseJSON(evt.responseText);
                        if (r === true) {
                            layer.msg("缓存已清除!", {time: 1000});
                        }
                    }
                });
            }
        });
    }

    return me;
});