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
        "map/core/GeoDataStore",
        "esri/layers/GraphicsLayer",
        "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
        "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, graphicsUtils, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer, SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol, Color, Draw, GeoDataStore, GraphicsLayer) {


        var exportData = false;
        var geoDataStore = GeoDataStore.getInstance();


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

        var __map, _insertConfig, _selectLayer, highlightFeatureOpacity;
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
        var $inserDataResultPanel;

        //监听事件
        EventBus.listener("InsertData_TZ", function () {
            query()
            //地图缩放，以实现地图新增地块刷新效果
            var extent = __map.extent;
            if (extent != null && extent != undefined) {
                extent = lang.mixin(extent, {spatialReference: __map.spatialReference});
                __map.setExtent(extent.expand(2));
                var center = extent.getCenter();
                if (center) __map.centerAt(center);
            }
        });

        /**
         *  初始化组件
         */
        function _init() {
            $inserDataResultPanel = $("#inserDataResultPanel");

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
                    var layerUrl = item.layerUrl;
                    var locateOpacity = item.locateOpacity;
                    var titleField = item.titleField;
                    options += '<option value="' + alias + '">' + alias + '</option>';
                    var tmp = {};
                    tmp["alias"] = alias;
                    tmp["layerName"] = layerName;
                    tmp["returnFields"] = returnFields;
                    tmp["drawMethod"] = drawMethod;
                    tmp["dataSource"] = dataSource;
                    tmp["layerUrl"] = layerUrl;
                    tmp["titleField"] = titleField;
                    tmp["locateOpacity"]=locateOpacity;
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
            //执行初始化查询
            query();
        }


        //查询出图层所有数据
        function query() {
            var selectLayerAlias = $("#insertDataLayerSelect").val();
            _selectLayer = config[selectLayerAlias];
            highlightFeatureOpacity = _selectLayer.locateOpacity;
            var url = _selectLayer.layerUrl;
            var outFieldsStr = "";
            var allData1 = {
                layerUrl: getLayerUrl(url),
                where: "1=1",
                geometry: null,
                returnFields: outFieldsStr,
                page: 0
            };

            $.ajax({
                url: "/omp/map/query",
                data: allData1,
                success: function (t) {
                    parseQueryResult(t, allData1);
                }
            });
        }


        /***
         * 处理图层url
         * @param lyrUrl
         * @returns {string}
         */
        function getLayerUrl(lyrUrl) {
            var sr = __map.spatialReference.wkid;
            var realUrl;
            if (lyrUrl.startWith("http://")) {
                realUrl = sr != undefined ? lyrUrl.concat("/query").concat("?outSR=" + sr) : lyrUrl.concat("/query");
            } else {
                realUrl = sr != undefined ? lyrUrl.replace("/oms", omsUrl).concat("/query").concat("?outSR=" + sr) : lyrUrl.replace("/oms", omsUrl).concat("/query");
            }
            return encodeURI(realUrl);
        }

        /**
         *  图层查询后回调函数处理结果
         */
        function parseQueryResult(r, data, isAttrQuery) {
            var attrs = [];//存放要进行展示的属性集合
            var layerObj = null; //选定的图层对象

            var rp = {};
            rp = JSON.parse(r);
            var totalSize = rp.features.length;

            layerObj = _selectLayer;
            var tf = _selectLayer.titleField;
            var sf = getSubTitleField(_selectLayer);
            var rf = _selectLayer.returnFields;
            var graphics = [];
            $.each(rp.features, function (i, feature) {
                for (var j in rf) {
                    if (rf[j].type == "DATE" && feature.attributes[rf[j].name]) {
                        feature.attributes[rf[j].name] = new Date(feature.attributes[rf[j].name]).toLocaleString();
                    }
                }
                var titleText = esriLang.substitute({
                    alias: tf.alias,
                    value: feature.attributes[tf.name] || '空'
                }, '${alias}:${value}');
                var subTitleText = esriLang.isDefined(sf) ? esriLang.substitute({
                    alias: sf.alias,
                    value: feature.attributes[sf.name] || '空'
                }, '${alias}:${value}') : '';
                var g = rp.features[i];
                var geo = g.geometry;
                if (!geo.spatialReference) {
                    geo.spatialReference = __map.spatialReference;
                }
                var graphic = new Graphic(g);
                graphics.push(graphic);
                attrs.push({
                    title: titleText,
                    subtitle: subTitleText,
                    graphic: graphic
                });
            });
            //渲染模板显示结果

            var tpl = $("#insertAttrResultTpl").html();
            var listDataRenderer;

            $inserDataResultPanel.empty();
            $inserDataResultPanel.append(Mustache.render(tpl, {
                result: attrs,
                size: totalSize,
                exportData: exportData
            }));
            listDataRenderer = new ListDataRenderer({
                renderTo: $('#insertAttr_result'),
                // type: exportData ? "export" : "loc",
                type: "locAndDelTpl",
                map: map.map(),
                renderData: attrs
            });

            //定位
            listDataRenderer.on('location', function (data) {
                doLocation(data.graphic, layerObj);
            });
            //删除
            listDataRenderer.on('delete', function (data) {
                showMsg('删除中...', false);
                var layerName = _selectLayer.layerName
                var dataSource = _selectLayer.dataSource
                var pk = data.graphic.attributes.OBJECTID
                $.ajax({
                    type: 'post'
                    , url: root + '/geometryService/rest/delete'
                    , data: {layerName: layerName, primaryKey: pk, dataSource: dataSource}
                    , cache: false
                    , async: true
                    , success: function (ret) {
                        layer.closeAll();
                        if (ret.result === true) {
                            layer.msg("删除成功！");
                            refresh()
                        } else {
                            layer.msg("删除失败！");
                        }
                    }
                });
            });


            listDataRenderer.render();
            //结果界面可滚动
            $("#insertAttr_result").slimScroll({
                height: $(window).height() - 270,
                width: 300,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
            $("#qExportBtn").on('click', function () {
                data.page = -1;
                $.ajax({
                    url: "/omp/map/query",
                    data: data,
                    success: function (r) {
                        var features = $.parseJSON(r).features;
                        var data = [];
                        for (var key in features[0].attributes) {
                            keys.push(key);
                        }
                        data.push(keys);
                        arrayUtil.forEach(features, function (item) {
                            var att = item.attributes;
                            var str = [];
                            arrayUtil.forEach(keys, function (k) {
                                str.push(att[k]);
                            });
                            data.push(str);
                        });
                        geometryIO._exportAttInfo(data);
                    }
                });
            })

        }

        // 操作结束后刷新
        function refresh() {
            __map.graphics.clear();
            var graphicsLayer = __map.getLayer("graphics4Print");
            if (graphicsLayer != undefined) {
                graphicsLayer.clear();
            }
            if (mapPopup.isShowing) {
                mapPopup.closePopup()
            }
            query();
            //通过更改展示范围刷新图层
            /*      var lyr = new GraphicsLayer({id: '0170a457e1a14028da9f70a4570c0003'});
                  var lyr1 = __map.getLayer("0170a457e1a14028da9f70a4570c0003")
                  __map.removeLayer(lyr1)
                  __map.addLayer(lyr1)
                  lyr.redraw()
                  var center = __map.extent.getCenter()
                  __map.resize(true)
                  __map.reposition()
                  __map.centerAt(center)*/
            var extent = __map.extent;
            if (extent != null && extent != undefined) {
                extent = lang.mixin(extent, {spatialReference: __map.spatialReference});
                __map.setExtent(extent.expand(2));
                var center = extent.getCenter();
                if (center) __map.centerAt(center);
            }

        }


        /***
         * 定位图形
         * @param g
         */
        function doLocation(g, lyr) {
            clearHighLayer();
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
            geoCenter = lang.mixin(geoCenter, {spatialReference: __map.spatialReference});
            var graphic = new Graphic(lang.mixin(geo, {spatialReference: __map.spatialReference}));
            graphic.setAttributes(g.attributes);
            MapUtils.setMap(__map);
            //高亮
            if (_selectLayer.type == "noColor") {
                MapUtils.highlightFeatures([graphic], false, 0, "graphics4Print", _selectLayer.type);
            } else {
                MapUtils.highlightFeatures([graphic], false, highlightFeatureOpacity, "graphics4Print");
            }
            //定位
            MapUtils.locateFeatures([graphic]);
            //弹框
            popupWindow(geo, g.attributes)

        }

        /**
         * 设置二级标题字段
         * 选取非标题字段的第一个字段
         * @param lyr
         */
        function getSubTitleField(lyr) {
            var tf = lyr.titleField;
            var rf = lyr.returnFields;
            for (var i = 0, l = rf.length; i < l; i++) {
                var item = rf[i];
                if (esriLang.isDefined(item) && item.name != tf.name) {
                    return item;
                }
            }
            return undefined;
        }

        /**
         * 事件监听
         *
         * @private
         */
        function _addListeners() {

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
            clearHighLayer();
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
            popupWindow(obj.feature.geometry);


        }

        /**
         * 弹框设置
         * @param evt
         */
        function popupWindow(obj, attributes) {
            var selectLayerAlias = $("#insertDataLayerSelect").val();
            _selectLayer = config[selectLayerAlias];
            var insertDataInfo = {};
            insertDataInfo["dataSource"] = _selectLayer.dataSource;
            insertDataInfo["layerName"] = _selectLayer.layerName;
            insertDataInfo["geometry"] = obj;
            if (mapPopup.isShowing) {
                mapPopup.closePopup();
            }
            var data = {};
            var fields = [];
            arrayUtil.forEach(_selectLayer.returnFields, function (item) {
                var name = item.name;
                if (item.name != "OBJECTID") {
                    data[name] = "";
                    fields.push(item);
                }
            });
            if (attributes != null) {
                mapPopup.setData(attributes);
            } else {
                mapPopup.setData(data);
            }
            mapPopup.setFields(fields);
            mapPopup.setTitle(selectLayerAlias);
            mapPopup.setIsEdit(true);
            mapPopup.setInsertDataInfo(insertDataInfo);
            mapPopup.setSource("InsertData_TZ");
            mapPopup.openPopup(getInfoPosition(obj));
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
                } else {
                    obj.feature.symbol = new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 10, null, new Color([51, 136, 255, 1]));
                }
            }
            if (obj.feature.geometry.type == "polygon") {
                obj.feature.symbol = new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new Color([51, 136, 255, 1]), 2), new Color([51, 136, 255, 0.2]));
            }
            MapUtils.highlightFeatures([obj.feature], false, _insertConfig.locateOpacity);

        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            drawTool.deactivate();
            layer.closeAll();
            clearHighLayer()

        }

        function clearHighLayer() {
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            if (mapPopup.isShowing) mapPopup.closePopup();
            var graphicsLayer = __map.getLayer("graphics4Print");
            if (graphicsLayer != undefined) {
                graphicsLayer.clear();
            }
        }


        /**
         * show msg
         * @param m
         * @param time
         */
        function showMsg(m, time, icon, shift) {
            return layer.msg(m || '加载中..', {
                icon: icon || 16,
                shade: 0.5,
                time: time,
                shift: shift || 0
            });
        }


        return me;
    });