/**
 *  鹤岗属性识别
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/12 16:50
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/on",
        "dojox/uuid/generateRandomUuid",
        "esri/lang",
        "esri/graphic",
        "esri/tasks/IdentifyTask",
        "esri/tasks/FeatureSet",
        "esri/tasks/IdentifyParameters",
        "esri/geometry/Point",
        "esri/graphicsUtils",
        "esri/toolbars/draw",
        "map/core/BaseWidget",
        "map/core/GeometryIO",
        "slimScroll",
        "mustache",
        "layer",
        "handlebars",
        "map/utils/MapUtils",
        "map/component/MapPopup",
        "map/component/ListDataRenderer",
        "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
        "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters,
              Point, graphicsUtils, Draw, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer) {

        var _map, _identifyConfig, _identifyCount;
        var _identifyLayers = [];
        var drawTool, drawHandler;
        var linkUrl;

        var me = declare([BaseWidget], {

            onCreate: function () {
                _map = this.getMap().map();
                _identifyConfig = this.getConfig();
                // _layer = _identifyConfig.layers[0];
                _init();
            },
            onOpen: function () {
                _map = this.getMap().map();
                _identifyLayers = [];
                _init();
                _activated = true;
                $switchBtn.addClass("omp-switch-on");
                $switchBtn.attr('title', '关闭功能');
                _prepare();
            },
            onPause: function () {
                _activated = false;
                _clear();
            },
            onDestroy: function () {
                _clear();
            }
        });


        //初始化组件
        function _init() {
            if (drawTool != undefined) {
                drawTool.deactivate();
            }
            if (drawHandler != undefined) {
                drawHandler.remove();
            }
            layer.config();
            if (_identifyConfig) {
                //跳转url
                linkUrl = _identifyConfig.linkUrl;
                var lyrs = _identifyConfig.layers;
                try {
                    $.each(lyrs, function (i, item) {
                        var _lyrs = _map.getLayer(item.serviceId);
                        if (_lyrs != undefined) {
                            _identifyLayers.push(lang.mixin(item, {url: _lyrs.url}));
                        }
                    });

                } catch (e) {
                    console.log(e.message);
                }
            }

            drawTool = drawTool ? drawTool : new Draw(_map);
            drawHandler = on(drawTool, "draw-end", lang.hitch(this, doSpatialQuery));
            drawTool.activate(Draw.POLYGON);
        }

        function drawInit() {
            if (drawTool != undefined) {
                drawTool.deactivate();
            }
            if (drawHandler != undefined) {
                drawHandler.remove();
            }
            drawTool = drawTool ? drawTool : new Draw(_map);
            drawHandler = on(drawTool, "draw-end", lang.hitch(this, doSpatialQuery));
            drawTool.activate(Draw.POLYGON);
        }

        /**
         * 执行空间查询
         * @param evt
         */
        function doSpatialQuery(evt) {
            resultTotal = [];
            if (_identifyLayers.length == 0) {
                if (_identifyConfig.layers.length == 0) {
                    layer.alter('未配置任何属性识别图层！', {btn: [], shadeClose: true});
                } else {
                    layer.msg('配置图层未加载到地图中！', {icon: 0});
                }
                return;
            }
            _identifyCount = _identifyLayers.length;
            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
            identifyParams.width = _map.width;
            identifyParams.height = _map.height;
            identifyParams.geometry = evt.geometry;
            identifyParams.mapExtent = _map.extent;
            for (var i in _identifyLayers) {
                var tmp = _identifyLayers[i];
                if (_map.getLayer(tmp.serviceId) == undefined) {
                    _identifyCount -= 1;
                    continue;
                }
                var identifyTask = new IdentifyTask(_map.getLayer(tmp.serviceId).url);
                on(identifyTask, 'error', _identifyResultError);
                identifyTask.execute(identifyParams, lang.hitch(this, _parseQueryResult, tmp));
            }

        }

        var resultTotal = [];

        function _parseQueryResult(token, result) {
            if (_identifyCount > 1) {
                resultTotal = resultTotal.concat(result);
                _identifyCount -= 1;
            } else if(_identifyCount == 1){
                resultTotal = resultTotal.concat(result);
                if (resultTotal.length > 0) {
                    if (resultTotal.length > 1) {
                        layer.msg("只能选择一个点进行识别");
                        drawInit();
                        return;
                    }
                    if (resultTotal.length == 1) {
                        var id = resultTotal[0].feature.attributes.地块id;
                        var layerName = resultTotal[0].layerName;
                        var year = layerName.substr(layerName.length - 4);
                        var url = linkUrl.concat("?id=").concat(id).concat("&year=").concat(year);// var url = esriLang.substitute(attr, lyr.link.url);
                        layer.open({
                            type: 2,
                            title: "地块详情",
                            area: ['800px', "600px"],
                            shade: 0,
                            content: [url],
                            end: function () {
                                drawInit();
                            }
                        });
                        return;
                    }
                }else {
                        layer.msg("未选择到点进行识别！");
                        drawInit();
                        return;
                }
            }


        }


        /***
         * 处理图层url
         * @param lyrUrl
         * @returns {string}
         */
        function getLayerUrl(lyrUrl) {
            var sr = _map.spatialReference.wkid;
            var realUrl;
            if (lyrUrl.startWith("http://")) {
                realUrl = sr != undefined ? lyrUrl.concat("/query").concat("?outSR=" + sr) : lyrUrl.concat("/query");
            } else {
                realUrl = sr != undefined ? lyrUrl.replace("/oms", omsUrl).concat("/query").concat("?outSR=" + sr) : lyrUrl.replace("/oms", omsUrl).concat("/query");
            }
            return encodeURI(realUrl);
        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            drawTool.deactivate();
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            __map.setMapCursor('default');
            layer.closeAll();
            if (mapPopup.isShowing) mapPopup.closePopup();
        }


        /**
         *
         * @param error
         * @private
         */
        function _identifyResultError(error) {
            console.error(error.message);
            _map.setMapCursor('default');
        }


        return me;
    });