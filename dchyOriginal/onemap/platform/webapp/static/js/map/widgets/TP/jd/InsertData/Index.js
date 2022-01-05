/**
 * 数据录入
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
        "esri/symbols/SimpleLineSymbol",
        "esri/symbols/SimpleFillSymbol",
        "esri/Color",
        "esri/toolbars/draw",
        "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
        "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, graphicsUtils, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer, SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol, Color, Draw) {

        var STYLE_MARKER_CONSTANT = {
            STYLE_CIRCLE: "circle",
            STYLE_SQUARE: "square",
            STYLE_CROSS: "cross",
            STYLE_X: "x",
            STYLE_DIAMOND: "diamond",
            STYLE_TARGET: "target"
        };

        var STYLE_FILL_CONSTANT = {
            STYLE_SOLID: "solid",
            STYLE_NULL: "none",
            STYLE_HORIZONTAL: "horizontal",
            STYLE_VERTICAL: "vertical",
            STYLE_FORWARD_DIAGONAL: "forwarddiagonal",
            STYLE_BACKWARD_DIAGONAL: "backwarddiagonal",
            STYLE_CROSS: "cross",
            STYLE_DIAGONAL_CROSS: "diagonalcross",
            STYLE_FORWARDDIAGONAL: "forwarddiagonal",
            STYLE_BACKWARDDIAGONAL: "backwarddiagonal",
            STYLE_DIAGONALCROSS: "diagonalcross"
        };

        var __map, _insertConfig;
        var mapPopup = MapPopup.getInstance();


        var me = declare([BaseWidget], {

            onCreate: function () {
                __map = this.getMap().map();
                _insertConfig = this.getConfig();
                _init();
                _addListeners();
            },
            onOpen: function () {
                __map = this.getMap().map();
                _identifyLayers = [];
                _init();
                _activated = true;
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

        var $descSpan, $insertBtn, $clearBtn, $drawDiv, $pointChoose, $polygonChoose;
        var xCood, yCood;

        //是否显示底部按钮
        var showArchiveBtn = false;
        var geometryIO;
        var config = {};
        var drawTool, drawHandler;


        /**
         *  初始化组件
         */
        function _init() {

            //读取弹出层方式
            if (_insertConfig.popUpStyle && _insertConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            if (_insertConfig.hasOwnProperty("showFooter")) {
                showArchiveBtn = _insertConfig.showFooter;
            }
            var options = '';
            if (_insertConfig.layers.length > 0) {
                for (var i = 0; i < _insertConfig.layers.length; i++) {
                    var item = _insertConfig.layers[i];
                    var alias = item.layerAlias;
                    var layerName = item.layerName;
                    var returnFields = item.returnFields;
                    var drawMethod = item.drawMethod;
                    var dataSource = item.dataSource;
                    options += '<option value="' + alias + '">' + alias + '</option>';
                    var tmp = {};
                    tmp["alias"] = alias;
                    tmp["layerName"] = layerName;
                    tmp["returnFields"] = returnFields;
                    tmp["drawMethod"] = drawMethod;
                    tmp["dataSource"] = dataSource;
                    config[alias] = tmp;
                }
            }
            layer.config();
            //初始化jq变量
            $descSpan = $("#iDescInsertSpan");
            $insertBtn = $("#insertIdentifyBtn");
            $clearBtn = $("#insertClearBtn");
            $drawDiv = $("#drawDiv");
            $pointChoose = $(".pointDiv");
            $polygonChoose = $(".polygonDiv");
            geometryIO = new GeometryIO();
            var layerSelect = '<select id="insertDataLayerSelect" style="margin-left: 15px;" class="form-control input-sm">' + options + '</select>';
            $descSpan.removeClass("text-muted").addClass("text-primary").html(layerSelect);

            /**
             * 下拉框监听
             */
            $("#insertDataLayerSelect").on("change", function () {
                var alias = $(this).val();
                $pointChoose.addClass("display");
                $polygonChoose.addClass("display");
                if (config[alias] != null) {
                    var drawMethod = config[alias]["drawMethod"];
                    var drawMethodDiv = '';
                    if (drawMethod == "point") {
                        $pointChoose.removeClass("display");
                    }
                    if (drawMethod == "polygon") {
                        $polygonChoose.removeClass("display");
                    }
                }
            });

            $('#insertDataLayerSelect').trigger('change');
        }


        /**
         * 事件监听
         *
         * @private
         */
        function _addListeners() {
            /**
             * 定位坐标
             */
            $insertBtn.on("click", function () {
                xCood = $("#xCood").val();
                yCood = $("#yCood").val();
                var coordinateSystem = $("#coordinateSystem").val();
                if (coordinateSystem === "project") {
                    var tmp = {
                        "geometry": {
                            "x": xCood,
                            "y": yCood,
                            "spatialReference": __map.spatialReference
                        }, "type": "esriSFS"
                    };
                    var graphic = new Graphic(tmp);
                    var currObj = {};
                    currObj.feature = graphic;
                    if (currObj != null) {
                        var flag = "1";
                        locGraphic(currObj, flag);
                    }
                } else {
                    var data = {
                        x: xCood,
                        y: yCood,
                        inSRStr: _insertConfig.inSR
                    };
                    $.ajax({
                        type: 'post'
                        , url: root + '/geometryService/jdGeoToProject'
                        , data: {param: JSON.stringify(data)}
                        , success: function (ret) {
                            if (ret != null) {
                                var x = ret[0];
                                var y = ret[1];
                                var tmp = {
                                    "geometry": {
                                        "x": x,
                                        "y": y,
                                        "spatialReference": __map.spatialReference
                                    }, "type": "esriSFS"
                                };
                                var graphic = new Graphic(tmp);
                                var currObj = {};
                                currObj.feature = graphic;
                                if (currObj != null) {
                                    var flag = "1";
                                    locGraphic(currObj, flag);
                                }
                            } else {
                                alert("定位失败！");
                            }
                        }
                    });
                }


            });

            /**
             * 清除
             */
            $clearBtn.on("click", function () {
                $("#xCood").val("");
                $("#yCood").val("");
                __map.graphics.clear();
                layer.closeAll();
                if (mapPopup.isShowing) mapPopup.closePopup();
            });


            /**
             * 绘制按钮监听
             */
            $drawDiv.find('.spatial-geo-icon').on('click', function () {
                spatialClickHandler($(this).data("type"));
            });
        }

        /***
         * 绘制图形
         * @param evt
         */
        function spatialClickHandler(type) {
            if (drawTool != undefined) drawTool.deactivate();
            if (drawHandler != undefined) drawHandler.remove();
            drawTool = drawTool ? drawTool : new Draw(__map);
            drawHandler = on(drawTool, "draw-end", lang.hitch(this, drawEndEvent));
            switch (type) {
                case "point":
                    drawTool.activate(Draw.POINT);
                    break;
                case "polygon":
                    drawTool.activate(Draw.POLYGON);
                    break;
                default :
                    console.error(type + " is unsupported yet!");
                    break;
            }
        }


        function drawEndEvent(evt) {
            var graphic = new Graphic(evt.geometry);
            var obj = {};
            obj.feature = graphic;
            locGraphic(obj);
            popupWindow(obj);


        }

        /**
         * 弹框设置
         * @param evt
         */
        function popupWindow(obj) {
            var selectLayerAlias = $("#insertDataLayerSelect").val();
            var selectLayer = config[selectLayerAlias];
            var insertDataInfo = {};
            insertDataInfo["dataSource"] = selectLayer.dataSource;
            insertDataInfo["layerName"] = selectLayer.layerName;
            insertDataInfo["geometry"] = obj.feature.geometry;
            if (mapPopup.isShowing) {
                mapPopup.closePopup();
            }
            var data = {};
            var fields = [];
            arrayUtil.forEach(selectLayer.returnFields, function (item) {
                var name = item.name;
                if (item.name != "OBJECTID") {
                    data[name] = name;
                    fields.push(item);
                }
            });
            mapPopup.setFields(fields);
            mapPopup.setData(data);
            mapPopup.setTitle(selectLayerAlias);
            mapPopup.setIsEdit(true);
            mapPopup.setInsertDataInfo(insertDataInfo);
            mapPopup.openPopup(getInfoPosition(obj.feature.geometry));
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


        /***
         * 定位graphic
         * @param obj
         */
        function locGraphic(obj, flag) {
            //高亮显示定位
            if (obj.feature.geometry.type == "point") {
                if (flag == "1") {
                    MapUtils.locatePoint(obj.feature);
                    obj.feature.symbol = new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 10, null, new Color("#FF0000"));
                }
                else {
                    obj.feature.symbol = new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 10, null, new Color([51, 136, 255, 1]));
                }
            }
            if (obj.feature.geometry.type == "polygon") {
                obj.feature.symbol = new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new Color([51, 136, 255, 1]), 2), new Color([51, 136, 255, 0.2]));
            }
            MapUtils.highlightFeatures([obj.feature], false, _insertConfig.locateOpacity);

        }


        /**
         * 添加地图监听
         * @private
         */
        function _prepare() {
            _mapClickHanlder = __map.on('click', doIdentify);
            __map.setMapCursor('crosshair');
            MapUtils.setMap(__map);
        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            drawTool.deactivate();
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            layer.closeAll();
            if (mapPopup.isShowing) mapPopup.closePopup();
        }


        return me;
    });