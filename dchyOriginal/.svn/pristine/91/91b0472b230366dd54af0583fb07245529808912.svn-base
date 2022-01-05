/**
 * 响水违法用地分析
 *
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "dojox/uuid/generateRandomUuid",
    "map/core/EsriSymbolsCreator",
    "handlebars",
    "dojo/_base/Color",
    "esri/graphic",
    "esri/geometry/Geometry",
    "esri/geometry/Polygon",
    "esri/geometry/Extent",
    "esri/layers/GraphicsLayer",
    "map/utils/MapUtils",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/BaseAnalysis",
    "map/core/GeoDataStore",
    "slimScroll",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "text!static/js/map/template/analysis/analysis-basic-tpl.html",
    "text!static/js/map/template/analysis/analysis-list-item.html",
    "text!static/js/map/template/analysis/commonAnalysis-result-tpl.html",
    "map/core/GeometryIO",
    "css!static/js/map/widgets/Analysis/Style.css"], function (declare, lang, arrayUtil, topic, RandomUuid, EsriSymbolsCreator, Handlebars, Color,
                                                               Graphic, Geometry, Polygon, Extent, GraphicsLayer, MapUtils,
                                                               BaseWidget, JsonConverters, BaseAnalysis, GeoDataStore, slimScroll, bootstrap,
                                                               baseTpl, listItem, resultTpl, GeometryIO) {
    var _map, commonAnalysis, _label, _analysisConfig, _id;
    var subscribe;

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
            _id = this.id;
            _label = this.getLabel();
            _analysisConfig = this.getConfig();
            _init();
            _addListener();
            MapUtils.setMap(_map);

        },
        /**
         *
         */
        onPause: function () {
            _pause();
        },
        /**
         *
         */
        onOpen: function () {
            _resume();
        },
        /**
         *
         */
        onDestroy: function () {
            commonAnalysis.clear();
        }
    });

    var $optContainer, $selectContainer, $listContainer, $clearBtn;
    var geoDataStore = GeoDataStore.getInstance();
    var shareResult = [];

    /***
     * init
     * @private
     */
    function _init() {
        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });
        commonAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_ILLEGAL_XS, _map);
        commonAnalysis.setAppConfig(appConfig);
        commonAnalysis.setModuleConfig(_analysisConfig);
        commonAnalysis.setScopeLayers(_analysisConfig.scopeLayers);

        commonAnalysis.setFieldAlias(_getFieldAlias());
        if (_analysisConfig.hasOwnProperty("link")) {
            var _link = _analysisConfig.link;
            if (_link.hasOwnProperty("url") && _link.url != "") {
                commonAnalysis.setFreeParams({iframeUrl: _link.url});
            }
        }
        if (_analysisConfig.responseType) {
            commonAnalysis.setResponseType(_analysisConfig.responseType);
            //同时订阅一个anaResultHandler事件处理通过rest接口得到的分析结果
            topic.subscribe("anaResultHandler", function (data) {
                Handlebars.registerHelper("fieldsTitles", function (data, options) {
                    var titles = ["序号"];
                    $.each(data[0], function (index, item) {
                        if (item.name.toUpperCase() == "SHAPE") {
                            titles.push("图形");
                        } else if (item.alias) {
                            titles.push(item.alias);
                        } else {
                            titles.push(item.name);
                        }
                    });
                    var tpl = "{{#each this}}<th>{{this}}</th>{{/each}}";
                    var temp = Handlebars.compile(tpl);
                    return temp(titles);

                });

                Handlebars.registerHelper("addOne", function (index, options) {
                    return parseInt(index) + 1;
                });

                Handlebars.registerHelper("enableActive", function (value, type, options) {
                    if (value == 0) {
                        if (type == 1) {
                            return "in active";
                        }
                        return "active";
                    } else {
                        return "";
                    }
                });
                Handlebars.registerHelper("formatValue", function (data, options) {
                    if (data.name.toUpperCase() == "SHAPE") {
                        var geometry = JsonConverters.toEsri(JSON.parse(data.value));
                        if (geometry.type == undefined) geometry = new Polygon(geometry);
                        var graphicId = "graphic-" + RandomUuid();
                        var attr = {id: graphicId};
                        var graphic = new Graphic(geometry, fillSymbol, attr);
                        graphicsLayer.add(graphic);
                        return '<a class="locationBtn" onclick="javascript:;" data-id="' + graphicId + '"><i title="点击定位" class="fa fa-map-marker fa-2x "></i></a>';
                    } else {
                        return data.value;
                    }
                });

                //订阅分析结果清除事件
                topic.subscribe('clearAnalysisResult', function (_map) {
                    var graphicsLayer = _map.getLayer("graphicsLayer_from_result");
                    if (graphicsLayer) {
                        graphicsLayer.clear();
                        _map.removeLayer(graphicsLayer);
                    }
                    var $content = $("#result-container .content");
                    if ($content) {
                        $content.html('');
                    }
                });

                var result = JSON.parse(data.responseText);
                var info = result.info;
                var html = "";
                if (info.length > 0) {
                    var lineSymbol = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([255, 0, 0]), 2);
                    var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID, lineSymbol, new Color([0, 0, 0, 0.25]));
                    var graphicsLayer = new GraphicsLayer({id: 'graphicsLayer_from_result'});
                    var template = Handlebars.compile(resultTpl);
                    html = template(result);

                } else {
                    html = "<h4>当前分析无结果！</h4>";
                }
                $("#result-container").css({"display": "block"});
                topic.publish('clearAnalysisResult', _map);
                $("#result-container .content").append(html);
                if (graphicsLayer) _map.addLayer(graphicsLayer);
                addResultHandlerListener(_map, result.excelData, result.excelList, graphicsLayer);
            });
        }
        if (_analysisConfig.hasOwnProperty("yx")) {
            commonAnalysis.setYx(_analysisConfig.yx);
        }
        //根据配置控制页面显示内容
        var option = {
            listId: "commonList".concat(commonAnalysis.getId()),
            id: commonAnalysis.getId(),
            queryModeOn: lang.isArray(_analysisConfig.scopeLayers)
        };
        lang.mixin(option, _analysisConfig);
        $("#illegalAnalysisTitle").append(_label);
        $("#illegalAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + commonAnalysis.getId());
        $selectContainer = $('#select_' + commonAnalysis.getId());
        $listContainer = $("#commonList".concat(commonAnalysis.getId()));
        $clearBtn = $optContainer.find('a[data-opt="clear"]');

        //监听共享数据变化
        geoDataStore.on("onChange", lang.hitch(this, onShareData));
        geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

        //监听地图上的绘制要素清除事件
        topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
            clearHandle();
        });
    }

    /***
     * 组织字段别名与名称映射对象
     * @private
     */
    function _getFieldAlias() {
        var r = {};
        arrayUtil.forEach(_analysisConfig.returnFields, function (item) {
            r[item.name] = item.alias;
        });
        return r;
    }

    /***
     *
     * @private
     */
    function _addListener() {
        $optContainer.on('click', 'a', function () {
            var o = $(this).data("opt");
            switch (o) {
                case 'draw':
                    commonAnalysis.draw('polygon', true).then(function (obj) {
                        //将对象渲染到页面列表中
                        if (obj != undefined) {
                            _appendList(obj);
                        }
                        $clearBtn.show();
                    });
                    break;
                case 'imp':
                    $optContainer.find("input[type='file']").click();
                    break;
                case 'analysis':
                    commonAnalysis.setAnalysisGeometry();
                    try {
                        commonAnalysis.doAnalysis();
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'exp':
                    commonAnalysis.exportFeature(null);
                    break;
                case 'clear':
                    clearHandle();
                    break;
            }
        });

        //查询事件监听
        $selectContainer.on('click', 'a', function () {
            var drawType = $(this).data("type");
            commonAnalysis.draw(drawType, false).then(function (r) {
                commonAnalysis.queryGeo(r);
            });
        });
        //导入事件监听
        listenerFileInput();
    }

    /***
     * clear handle
     */
    function clearHandle() {
        commonAnalysis.clear().then(lang.hitch(this, function () {
            $clearBtn.hide();
            $listContainer.empty();
        }));
        _map.graphics.clear();
        var fileInput = document.getElementById('analysis-file-input-' + commonAnalysis.getId());
        //清除fileInput内容
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
    }

    /**
     *
     */
    function listenerFileInput() {
        $('#analysis-file-input-' + commonAnalysis.getId()).off('change');
        $('#analysis-file-input-' + commonAnalysis.getId()).on('change', function () {
            commonAnalysis.importFile($('#analysis-file-input-' + commonAnalysis.getId()));
        });
        setTimeout(function () {
            listenerFileInput();
        }, 2000);
    }

    /***
     * 添加要素列表
     * @param obj
     * @private
     */
    function _appendList(obj) {
        $listContainer.append(renderTpl(listItem, obj));
        var scrollHeight = $(window).height() - 300;
        $listContainer.slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        $clearBtn.show();
        $listContainer.find(".a-r-btn").unbind();
        $listContainer.find(".a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            commonAnalysis.findGraById(id).then(function (g) {
                switch (t) {
                    case 'location':
                        MapUtils.highlightFeatures([new Graphic(g.geometry)], false);
                        MapUtils.locateFeatures([g]);
                        break;
                    case 'analysis':
                        var geometry = JsonConverters.toGeoJson(g);
                        commonAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        try {
                            commonAnalysis.doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }
                        break;
                    case "export":
                        commonAnalysis.exportFeature(g);
                        break;
                }
            });

        });
    }

    /***
     * 切换当前widget后 移除监听等
     * @private
     */
    function _pause() {
        if (subscribe != null && subscribe != undefined)
            subscribe.remove();
        topic.publish('clearAnalysisResult', _map);
    }

    /***
     *
     * @private
     */
    function _resume() {
        subscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        });
    }

    /**
     * 获取共享数据
     * @param data
     */
    function onShareData(data) {
        if (data != undefined) {
            var type = data.type;
            switch (type) {
                case GeoDataStore.SK_LOC: {
                    var fs = data.features;
                    if (lang.isArray(fs)) {
                        arrayUtil.forEach(fs, function (item) {
                            var attr = item.attributes;
                            var tmp = {};
                            tmp.id = RandomUuid();
                            tmp.type = "wftb";
                            tmp.style = "teal";
                            tmp.title = "";
                            tmp.src = "定位";
                            tmp.graphic = item;
                            for (var k in attr) {
                                if (attr[k] != undefined && k != 'OBJECTID') {
                                    tmp.title = attr[k];
                                    break;
                                }
                            }
                            shareResult.push(tmp);
                            commonAnalysis.addGraphic(tmp);
                            _appendList(tmp);
                        });
                    }
                    break;
                }
            }
        }
    }

    /***
     * render tpl
     * @param tpl
     * @param data
     */
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }

    /**
     * rest接口返回结果处理监听事件
     * @param _map
     * @param excelData
     * @param excelList
     */
    function addResultHandlerListener(_map, excelData, excelList, graphics) {
        var ENUM_UTIL = {
            "EXPORT_EXCEL_GROUP": 0,
            "EXPORT_EXCEL_LIST": 1,
            EXPORT_EXCEL_URL: "../geometryService/export/excel"
        };

        //添加slimScroll
        $("#commonAnalysisResultContainer").slimScroll({
            height: '200px',
            width: '100%',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        //dropdown
        $('.dropdown-toggle').dropdown()

        //tab
        $('#infoContainer a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })

        //清除分析结果按钮监听
        $('#clearResultBtn').on('click', function () {
            topic.publish('clearAnalysisResult', _map);
        });

        //导出Excel监听
        $("#expExcelGroupBtn").unbind();
        $("#expExcelListBtn").unbind();
        $("#expExcelGroupBtn").on('click', function () {
            if (excelData) {
                exportExcel(0, excelData);
            } else {
                console.log("导出分析结果失败:不存在分组Excel数据");
            }
        });
        $("#expExcelListBtn").on('click', function () {
            if (excelList) {
                exportExcel(1, excelList);
            } else {
                console.log("导出分析结果失败:不存在列表Excel数据");
            }
        });

        //导出Shp监听
        $("#exportShpBtn").unbind();
        $("#exportShpBtn").on('click', function () {
            var geometryIO = new GeometryIO();
            if (graphics) {
                geometryIO.expToFile(graphics, 'shp');
            } else {
                console.log("导出分析结果失败:不存在shp数据");
            }
        });

        function exportExcel(type, data) {
            switch (type) {
                case ENUM_UTIL.EXPORT_EXCEL_GROUP: {
                    openPostWindow(ENUM_UTIL.EXPORT_EXCEL_URL, data, null);
                    break;
                }
                case ENUM_UTIL.EXPORT_EXCEL_LIST: {
                    openPostWindow(ENUM_UTIL.EXPORT_EXCEL_URL, data, null);
                    break;
                }
            }
        }

        function openPostWindow(url, data, fileName) {
            if (data == "") {
                alert("无导出数据!");
                return;
            }
            var tempForm = document.createElement("form");
            tempForm.method = "post";
            tempForm.action = url;
            var hideInput1 = document.createElement("input");
            hideInput1.type = "hidden";
            hideInput1.name = "data"
            hideInput1.value = data;
            var hideInput2 = document.createElement("input");
            hideInput2.type = "hidden";
            hideInput2.name = "fileName"
            hideInput2.value = fileName;
            tempForm.appendChild(hideInput1);
            if (fileName != null && fileName != "null" && fileName != "")
                tempForm.appendChild(hideInput2);
            document.body.appendChild(tempForm);
            tempForm.submit();
            document.body.removeChild(tempForm);
        }

        //图形定位
        $(".locationBtn").on("click", function () {
            var style = {
                lineStyle: "solid",
                lineColor: "#f0ad4e",
                fillStyle: "solid",
                fillColor: "#ff0000"
            };
            var highlightFeatureOpacity = 0.5;
            var graphicId = $(this).data("id");
            var graphics = _map.getLayer("graphicsLayer_from_result").graphics;
            $.each(graphics, function (index, graphic) {
                if (graphic.attributes.id == graphicId) {
                    MapUtils.highlightFeaturesWithStyle([new Graphic(graphic.geometry)], style, highlightFeatureOpacity);
                    MapUtils.locateFeatures([graphic]);
                }
            });

        });
    }

    return me;
});