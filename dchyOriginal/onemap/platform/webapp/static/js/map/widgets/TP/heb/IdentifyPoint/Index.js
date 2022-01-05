/**
 *  坐标查询（界址点属性识别）
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
        "map/core/BaseWidget",
        "map/core/GeometryIO",
        "slimScroll",
        "mustache",
        "layer",
        "handlebars",
        "map/utils/MapUtils",
        "map/component/MapPopup",
        "map/component/ListDataRenderer",
        "esri/symbols/SimpleMarkerSymbol",
        "esri/Color",
        "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
        "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, graphicsUtils, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer, SimpleMarkerSymbol, Color) {

        var STYLE_MARKER_CONSTANT = {
            STYLE_CIRCLE: "circle",
            STYLE_SQUARE: "square",
            STYLE_CROSS: "cross",
            STYLE_X: "x",
            STYLE_DIAMOND: "diamond",
            STYLE_TARGET: "target"
        };

        var __pointMap, _identifyConfig;
        var mapPopup = MapPopup.getInstance();


        var me = declare([BaseWidget], {

            onCreate: function () {
                __pointMap = this.getMap().map();
                _identifyConfig = this.getConfig();
                _init();
                _addListeners();
            },
            onOpen: function () {
                __pointMap = this.getMap().map();
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

        //设置属性识别后信息弹出窗的样式
        var POPUP_OPTION_INFO = "infoWindow";
        var POPUP_OPTION_MODAL = "modal";

        var _activated = false;
        //存放配置的属性识别图层
        var _identifyLayers = [];

        //默认是infowindow样式
        var popUpStyle = POPUP_OPTION_INFO;

        var _mapClickHanlder = null;
        var _identifyResults = [];
        var _identifyCount = 0;
        var _listData = [];//页面展示的数据集合
        var _lastClick;
        var _resultObj = {};
        var identifyLayerData = {};//属性识别数据

        var $clearBtn, $descSpan, $switchBtn;

        //是否显示底部按钮
        var showArchiveBtn = false;
        var exportData = false;
        var exportTypes = "";
        var geometryIO;

        /**
         *  初始化组件
         */
        function _init() {

            //读取弹出层方式
            if (_identifyConfig.popUpStyle && _identifyConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            if (_identifyConfig.hasOwnProperty("showFooter")) {
                showArchiveBtn = _identifyConfig.showFooter;
            }

            layer.config();
            if (_identifyConfig) {
                exportTypes = _identifyConfig.exportTypes;
                exportData = _identifyConfig.exportData;
                var lyrs = _identifyConfig.layers;
                //组织url等参数
                try {
                    $.each(lyrs, function (i, item) {
                        var _lyr = __pointMap.getLayer(item.serviceId);
                        if (_lyr != undefined) {
                            _identifyLayers.push(lang.mixin(item, {url: _lyr.url}));
                        }
                    });
                } catch (e) {
                    console.log(e.message);
                }
            }
            //初始化jq变量
            $clearBtn = $("#iClearPointBtn");
            $descSpan = $("#iDescPointSpan");
            $switchBtn = $("#iSwitchPointBtn");
            geometryIO = new GeometryIO();
        }

        /**
         * DOM事件监听
         */
        function _addListeners() {
            $clearBtn.on("click", function () {
                $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                $("#layerFoldlPoint").addClass("hidden").empty();
                $clearBtn.addClass("hidden");
                __pointMap.graphics.clear();
                layer.closeAll();
                if (mapPopup.isShowing) mapPopup.closePopup();
            });
            $switchBtn.on('click', function () {
                var $this = $(this);
                if ($this.hasClass("omp-switch-on")) {
                    $this.removeClass("omp-switch-on");
                    $this.find('em').text("关");
                    $this.attr('title', '打开功能');
                    if (_mapClickHanlder) _mapClickHanlder.remove();
                    __pointMap.setMapCursor('default');
                } else {
                    $this.addClass("omp-switch-on");
                    $this.find('em').text("开");
                    $this.attr('title', '关闭功能');
                    _prepare();
                }
            });
        }

        /**
         * 添加地图监听
         * @private
         */
        function _prepare() {
            _mapClickHanlder = __pointMap.on('click', doIdentify);
            __pointMap.setMapCursor('crosshair');
            MapUtils.setMap(__pointMap);
        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __pointMap.graphics.clear();
            layer.closeAll();
            if (mapPopup.isShowing) mapPopup.closePopup();
        }

        /**
         * do identify
         * @param evt
         */
        function doIdentify(event) {
            if (_identifyLayers.length == 0) {
                if (_identifyConfig.layers.length === 0) {
                    layer.alert('未配置任何查询图层!', {btn: [], shadeClose: true});
                } else {
                    layer.msg("待查询的服务尚未加载到地图中！", {icon: 0});
                }
                return;
            }
            _lastClick = event.mapPoint;
            _identifyCount = _identifyLayers.length;
            layer.closeAll();
            layer.msg('坐标查询中..', {time: 10000});
            _identifyResults = [];
            _listData = [];
            _resultObj = [];
            __pointMap.graphics.clear();
            __pointMap.infoWindow.hide();

            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
            identifyParams.width = __pointMap.width;
            identifyParams.height = __pointMap.height;
            identifyParams.geometry = event.mapPoint;
            identifyParams.mapExtent = __pointMap.extent;

            for (var i in _identifyLayers) {
                var tmp = _identifyLayers[i];
                if (__pointMap.getLayer(tmp.serviceId) == undefined) {
                    _identifyCount -= 1;
                    continue;
                }
                var identifyTask = new IdentifyTask(__pointMap.getLayer(tmp.serviceId).url);
                on(identifyTask, 'error', _identifyResultError);
                identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult, tmp));
            }
        }

        /**
         * 处理识别结果
         * @param event
         * @param result
         * @private
         */
        function _handleIdentifyResult(token, result) {
            if ((result != null && result.length > 0) && _identifyCount > 0) {
                if (_identifyResults.length > 0) {
                    _identifyResults = [];
                }
                _identifyResults = _identifyResults.concat(result);
                arrayUtil.forEach(_identifyResults, function (item) {
                    if (token.layerName === item.layerName) {    //过滤图层
                        var ringSize = item.feature.geometry.rings.length;
                        var pointList = [];
                        for (var ri = 0; ri < ringSize; ri++) {
                            var ring = item.feature.geometry.rings[ri];
                            var xh = 1;
                            arrayUtil.forEach(ring, function (ringItem) {
                                var tmp = {};
                                var data = {};
                                data.xh = "J".concat(xh);
                                data.x = ringItem[0];
                                data.y = ringItem[1];
                                var geometry = {};
                                geometry.type = "point";
                                geometry.spatialReference = item.feature.geometry.spatialReference;
                                geometry.x = ringItem[0];
                                geometry.y = ringItem[1];
                                var feature = {};
                                feature.geometry = geometry;
                                tmp.feature = feature;
                                tmp.data = data;
                                pointList.push(tmp);
                                xh++;
                            });
                        }
                        if (item.layerName in _resultObj) {
                            arrayUtil.forEach(pointList, function (pointItem) {
                                _resultObj[item.layerName].push(pointItem);
                            });
                        } else {
                            _resultObj[item.layerName] = [];
                            arrayUtil.forEach(pointList, function (pointItem) {
                                _resultObj[item.layerName].push(pointItem);
                            });
                        }
                        pointList = null;
                    }
                });
            }
            _identifyCount -= 1;
            if (_identifyCount == 0) {
                layer.closeAll();
                if (!isEmpty(_resultObj)) {
                    //清空dom
                    $("#layerFoldlPoint").empty();
                    $descSpan.empty();
                    $("#resultPointDiv ul").removeClass("hidden");
                    $clearBtn.removeClass("hidden");
                    var options = '';
                    identifyLayerData = {};

                    for (var name in _resultObj) { //组织识别信息
                        var data = {layerName: name, info: []};
                        for (var i in _resultObj[name]) {
                            var g = _resultObj[name][i].feature;
                            var tmp = {
                                "geometry": {
                                    "x": g.geometry.x,
                                    "y": g.geometry.y,
                                    "spatialReference": __pointMap.spatialReference
                                }, "type": "esriSFS"
                            };
                            var graphic = new Graphic(tmp);
                            var info = _resultObj[name][i].data;
                            data.info.push({
                                title: info.xh,
                                subtitle: "X:".concat(info.x).concat(" ").concat("Y:").concat(info.y),
                                graphic: graphic,
                                x: info.x,
                                y: info.y,
                                layerName: name
                            });
                        }
                        identifyLayerData[name] = data.info;
                        options += '<option value="' + name + '">' + name + '</option>';
                    }

                    var layerSelect = '<select id="identifyPointLayerSelect" style="margin-left: 15px;" class="form-control input-sm">' + options + '</select>';
                    $descSpan.removeClass("text-muted").addClass("text-primary").html(layerSelect);

                    $('#identifyPointLayerSelect').on("change", function () {
                        $('#layerFoldlPoint').empty();
                        var name = $(this).val();
                        var listDataRenderer;

                        listDataRenderer = new ListDataRenderer({
                            renderTo: $('#layerFoldlPoint'),
                            type: "loc",
                            map: map.map(),
                            renderData: identifyLayerData[name]
                        });

                        //自动定位当前
                        arrayUtil.forEach(identifyLayerData[name], function (item) {
                            var currObj = {};
                            currObj.feature = item.graphic;
                            if (currObj != null) {
                                locGraphic(currObj);
                            }
                        });

                        //进行定位展示详细信息
                        listDataRenderer.on('location', function (data) {
                            var currObj = {};
                            currObj.feature = data.graphic;
                            if (currObj != null) {
                                locGraphic(currObj);
                            }
                        });

                        listDataRenderer.render();
                        var scrollHeight = $(window).height() - 220;
                        $("#layerFoldlPoint").slimScroll({
                            height: scrollHeight
                        });
                    });
                    $('#identifyPointLayerSelect').trigger('change');

                } else {
                    $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                    $("#resultPointDiv p").removeClass("hidden");
                    $("#resultPointDiv ul").addClass("hidden");
                    $clearBtn.addClass("hidden");
                }
            }
        }

        /***
         *
         * @returns {Array}
         */
        function parseTypesArray() {
            var types = exportTypes != "" ? exportTypes.split(",") : "txt,xls,cad,shp,bj".split(",");
            var r = [];
            arrayUtil.forEach(types, function (item) {
                if (item != 'bj') {
                    r.push({alias: getAlias(item), value: item});
                }
            });
            return r;
        }

        /***
         * 获取别名
         * @param name
         * @returns {*}
         */
        function getAlias(name) {
            switch (name) {
                case 'xls':
                    return 'excel文件(*.xls)';
                case 'txt':
                    return 'txt文件(*.txt)';
                case 'dwg':
                    return 'cad文件(*.dwg)';
                case 'bj':
                    return '电子报件包(*.zip)';
                case 'shp':
                    return 'shapfile压缩包(*.zip)';
                default:
                    return name;
            }
        }

        /**
         * 获取url中的参数
         * @param name
         * @returns {*}
         */
        function getArgumentFormUrl(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        };


        /***
         * 定位graphic
         * @param obj
         */
        function locGraphic(obj) {
            obj.feature.symbol= new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 10, null, new Color("#FF0000"));
                //高亮显示定位
            MapUtils.highlightFeatures([obj.feature], false, _identifyConfig.locateOpacity);

        }

        /**
         *
         * @param error
         * @private
         */
        function _identifyResultError(error) {
            console.error(error.message);
            __pointMap.setMapCursor('default');
        }

        /**
         *
         * @param obj
         * @param returnFields
         * @returns {*}
         */
        function getInfoContent(obj, returnFields) {
            var data = [];
            var tmpl = $("#infoContentTpl").html();
            var showData = obj.feature.attributes;
            for (var i in showData) {
                if ($.inArray(i, returnFields) != -1)
                    data.push({key: i, value: showData[i]});
            }
            return Mustache.render(tmpl, {data: data});
        }

        /***
         *
         * @param geometry
         * @returns {*}
         */
        function getInfoPosition(geometry) {
            switch (geometry.type) {
                case 'point':
                    return geometry;
                    break;
                case 'polyline':
                    break;
                case 'polygon':
                    var rightPiont = getPostion_(geometry);
                    var point_P = new Point(rightPiont);
                    return point_P;
                    break;

            }
            return null;
        }

        /**
         * 获取点
         * @param geometry
         * @returns {*}
         * @private
         */
        function getPostion_(geometry) {
            var arr = [];
            var rings = geometry.rings;
            rings = rings[0];
            var xMax = geometry.getExtent().xmax;

            for (var i = 0; i < rings.length; i++) {
                var x_ = rings[i];
                if (x_[0] == xMax) {
                    arr.push(x_);
                    if (arr.length != 1) {
                        return arr[arr.length];
                    } else {
                        return arr[0];
                    }

                }
            }
        }

        /**
         *
         * @param id
         * @returns {*}
         */
        function findFeatureByOid(id) {
            for (var i in _listData) {
                var obj = _listData[i];
                if (obj.objectid == id) {
                    return obj;
                }
            }
            return null;
        }

        /**
         *
         * @param layerId
         * @param oId
         * @returns {*}
         */
        function findFeatureByLayerIdAndOid(layerId, oId) {
            for (var i in _resultObj[layerId]) {
                var obj = _resultObj[layerId][i];
                if (obj.objectid == oId) {
                    return obj;
                }
            }
            return null;
        }

        /**
         * 检测对象是否为没有任何属性对象
         * 不检测继承原型链继承的属性
         * @returns {boolean}
         */
        function isEmpty(obj) {
            for (var name in obj) {
                if (obj.hasOwnProperty(name)) {
                    return false;
                }
            }
            return true;
        }

        /***
         * 添加滚动条
         * @param selector  需要添加滚动条的$元素
         * @param height    滚动条设置高度
         */
        function addSlimScroll(selector, height) {
            selector.slimScroll({
                height: height,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        }

        return me;
    });