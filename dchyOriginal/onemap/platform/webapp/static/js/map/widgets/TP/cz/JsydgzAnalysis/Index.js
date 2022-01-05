/**
 * 建设用地管制区分析
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "dojox/uuid/generateRandomUuid",
    "handlebars",
    "esri/graphic",
    "esri/geometry/Polygon",
    "map/utils/MapUtils",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/BaseAnalysis",
    "map/core/GeoDataStore",
    "map/core/GeometryIO",
    "map/component/MapPopup",
    "text!static/js/map/template/analysis/analysis-basic-tpl.html",
    "text!static/js/map/template/analysis/analysis-list-item.html",
    "text!static/js/map/template/analysis/ghAnalysis-result-tpl.html",
    "css!static/js/map/template/analysis/analysis.css"], function (declare, lang, arrayUtil, topic, RandomUuid, Handlebars, Graphic, Polygon, MapUtils, BaseWidget,
                                                                   JsonConverters, BaseAnalysis, GeoDataStore, GeometryIO, MapPopup, baseTpl, listItem, resultTpl) {
    var _map, ghscAnalysis, _ghscConfig;
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
            _ghscConfig = this.getConfig();
            _init();
            _addListener();
            MapUtils.setMap(_map);

        },
        /**
         *
         */
        onPause: function () {
            if (mapPopup.isShowing) mapPopup.closePopup();
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
            if (mapPopup.isShowing) mapPopup.closePopup();
            ghscAnalysis.clear();
        }
    });

    var $optContainer, $clearBtn, $selectContainer, $listContainer;
    var geoDataStore = GeoDataStore.getInstance();
    //建设用地管制区总面积
    var totalArea = 0;
    //符合规划图形
    var suitGras = [];

    var infoAC = [];
    var detailAC = [];
    var groupedAc = [];
    var ghscObj = {};
    var ghscResult = [];

    var info, title, blArea;

    var geometryIO;

    var POPUP_OPTION_INFO = "infoWindow";
    //默认是infowindow样式
    var popUpStyle = POPUP_OPTION_INFO;

    /***
     * init
     * @private
     */
    function _init() {

        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined && context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });

        ghscAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_JSYDGZQ_CZ, _map);
        ghscAnalysis.setAppConfig(appConfig);
        ghscAnalysis.setDataSource(_ghscConfig.dataSource);
        ghscAnalysis.setModuleConfig(_ghscConfig);
        ghscAnalysis.setAreaUnit(_ghscConfig.areaUnit || BaseAnalysis.UNIT_SQUARE_METERS);
        ghscAnalysis.setScopeLayers(_ghscConfig.scopeLayers);
        if (_ghscConfig.hasOwnProperty("responseType")) ghscAnalysis.setResponseType(_ghscConfig.responseType);
        if (_ghscConfig.hasOwnProperty("yx")) {
            ghscAnalysis.setYx(_ghscConfig.yx);
        }
        if (_ghscConfig.hasOwnProperty("linkUrl")) {
            ghscAnalysis.setLinkUrl(_ghscConfig.linkUrl);
        }
        ghscAnalysis.setLocalVersion(_ghscConfig.localVersion);
        //根据配置控制页面显示内容
        var option = {
            listId: "ghscList",
            id: ghscAnalysis.getId(),
            queryModeOn: lang.isArray(_ghscConfig.scopeLayers) && _ghscConfig.scopeLayers.length > 0
        };
        lang.mixin(option, _ghscConfig);
        $("#jsydgzCzAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + ghscAnalysis.getId());
        $clearBtn = $optContainer.find('a[data-opt="clear"]');
        $selectContainer = $('#select_' + ghscAnalysis.getId());
        $listContainer = $("#ghscList");


        //判断是否配置定位查询选择功能，若配置则显示不配置则监听定位数据变化
        if (_ghscConfig.locQuery == true) {
            $(".div-dataSourceTop-gh").click(function () {
                $(this).find("em").removeClass("animation-top2").addClass("animation-top").parents(".div-dataSource-gh").siblings().children(".div-dataSourceTop-gh").find("em").removeClass("animation-top").addClass(".animation-top2");
                $(this).next(".ul-dataSource-gh").toggle().parents(".div-dataSource-gh").siblings().find(".ul-dataSource-gh").hide();
            });
            $("#dataGhTypeUl>li").click(function () {
                var selva = $(this).text();
                var value = $(this).attr("code")
                $(this).parents("#dataGhTypeUl").siblings(".div-dataSourceTop-gh").find("span").text(selva);
                $(this).parents("#dataGhTypeUl").siblings(".div-dataSourceTop-gh").find("span").attr("code", value);
                $(this).parent("ul").hide();
                $(this).parents(".div-dataSource-gh").children(".div-dataSourceTop-gh").find("em").addClass("animation-top2");
                dataType = value;
            });
        } else {
            $("#hideGhDiv").hide();
            //监听共享数据变化
            geoDataStore.on("onChange", lang.hitch(this, onShareData));
            geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
        }
        //监听共享数据变化
        // geoDataStore.on("onChange", lang.hitch(this, onShareData));
        // geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

        //监听地图上的绘制要素清除事件
        topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
            clearHandle();
        });

        geometryIO = new GeometryIO();
    }

    var xzSubscribe;

    /***
     *
     * @private
     */
    function _addListener() {
        $optContainer.on('click', 'a', function () {
            var opt = $(this).data("opt");
            switch (opt) {
                case 'draw':
                    ghscAnalysis.draw('polygon', true).then(function (obj) {
                        //将对象渲染到页面列表中
                        if (obj != undefined) {
                            _appendList(obj);
                            $clearBtn.show();
                        }
                    });
                    break;
                case 'imp':
                    $('#analysis-file-input').click();
                    break;
                case 'analysis':
                    ghscAnalysis.setAnalysisGeometry(null);
                    try {
                        ghscAnalysis.doAnalysis();
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'exp':
                    ghscAnalysis.exportFeature(null);
                    break;
                case 'clear':
                    clearHandle();
                    break;
            }
        });


        //报件、shapefile等title信息
        topic.subscribe("bjTitleInfo", function (data) {
            info = data;
        });
        //分析完成
        topic.subscribe("ghAnalysisHandler", function (data) {
            parseRestResult(data);
        });
        //查询事件监听
        $selectContainer.on('click', 'a', function () {
            var drawType = $(this).data("type");
            ghscAnalysis.draw(drawType, false).then(function (r) {
                ghscAnalysis.queryGeo(r);
            });
        });
        //导入事件监听
        listenerFileInput();

        //监听数据来源
        $('#dataGhTypeUl').on("click", function () {
            if (dataType == "SK_LOC") {
                geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
            }
            else if (dataType == "SK_QUERY") {
                geoDataStore.fetch(GeoDataStore.SK_QUERY).then(lang.hitch(this, onShareData));
            }
        });
    }

    /***
     * clear handle
     */
    function clearHandle() {
        ghscAnalysis.clear().then(lang.hitch(this, function () {
            $clearBtn.hide();
            $listContainer.empty();
        }));
        _map.graphics.clear();
        var fileInput = document.getElementById('analysis-file-input-' + ghscAnalysis.getId());
        //清除fileInput内容
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
    }

    /***
     *
     */
    function listenerFileInput() {
        $('#analysis-file-input-' + ghscAnalysis.getId()).off('change');
        $('#analysis-file-input-' + ghscAnalysis.getId()).on('change', function () {
            ghscAnalysis.importFile($('#analysis-file-input-' + ghscAnalysis.getId()));
        });

        setTimeout(function () {
            listenerFileInput();
        }, 2000);
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
                            tmp.type = "ghsc";
                            tmp.style = "teal";
                            tmp.title = "";
                            tmp.src = "定位";
                            tmp.graphic = item;
                            for (k in attr) {
                                if (attr[k] != undefined) {
                                    tmp.title = attr[k];
                                    break;
                                }
                            }
                            ghscAnalysis.addGraphic(tmp);
                            _appendList(tmp);
                        });
                    }
                    break;
                }

                case GeoDataStore.SK_QUERY: {
                    var fs = data.features;
                    if (lang.isArray(fs)) {
                        arrayUtil.forEach(fs, function (item) {
                            var attr = item.attributes;
                            var tmp = {};
                            tmp.id = RandomUuid();
                            tmp.type = "ghsc";
                            tmp.style = "teal";
                            tmp.src = "查询";
                            tmp.title = "";
                            tmp.graphic = item;
                            for (var k in attr) {
                                if (attr[k] != undefined && k != 'OBJECTID') {
                                    tmp.title = attr[k];
                                    break;
                                }
                            }
                            // shareResult.push(tmp);
                            ghscAnalysis.addGraphic(item);
                            _appendList(tmp);
                        });
                    }
                    break;
                }
            }
        }
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
        $("#ghscList .a-r-btn").unbind();
        $("#ghscList .a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            ghscAnalysis.findGraById(id).then(function (g) {
                switch (t) {
                    case 'location':
                        MapUtils.highlightFeatures([new Graphic(g.geometry)], false);
                        MapUtils.locateFeatures([g]);
                        break;
                    case 'analysis':
                        var geometry = JsonConverters.toGeoJson(g);
                        ghscAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        try {
                            ghscAnalysis.doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }
                        break;
                    case "export":
                        ghscAnalysis.exportFeature(g);
                        break;

                }
            });

        });
    }

    /**
     * 用于处理rest方式 数据
     * @param data
     */
    function parseRestResult(data) {
        ghscObj = {};
        ghscResult = [];
        totalArea = 0;

        arrayUtil.forEach(ghscAC, function (item) {
            var label = item.label;
            var field = item.field;
            var name = item.name;

            var featureJson = JSON.parse(data[label]);
            var features = featureJson.features;

            //对象格式:{info:ArrayCollection,detail:ArrayCollection}
            infoAC = [];
            detailAC = [];
            groupedAc = [];

            arrayUtil.forEach(features, function (feature) {
                var temp = {};
                temp = lang.clone(feature.properties);
                temp.geometry = feature.geometry;
                temp.attributes = feature.attributes;
                temp.layerName = label;
                temp.origin = feature;
                temp.geo = JSON.stringify(feature);
                groupedAc.push(temp);
            });
            groupedAc = parseGroup(groupedAc, field);
            switch (name) {
                case "JSYDGZQ":
                    //todo ...
                    suitGras = [];
                    arrayUtil.forEach(JSYDGZQ_LXMC, function (item) {
                        parseDetail(item, 0);
                    });
                    break;
                case "TDYTQ":
                    arrayUtil.forEach(TDYTQ_LXMC, function (item) {
                        parseDetail(item, 1);
                    });
                    break;
                case "GHJBNTTZ":
                    arrayUtil.forEach(GHJBNTTZ_LXMC, function (item) {
                        parseDetail(item, 2);
                    });
                    break;
                case "MZZDJSXM":
                    arrayUtil.forEach(MZZDJSXM_LXMC, function (item) {
                        parseDetail(item, 3);
                    });
                    break;
            }
            arrayUtil.forEach(infoAC, function (item) {
                if (item.area == 0)
                    item.per = 0;
                else {
                    item.per = (item.area / totalArea * 100).toFixed(4);
                    item.area = (item.area / 10000).toFixed(4);
                }
            });
            ghscObj = {info: infoAC, detail: detailAC};
            var tmp = {};
            tmp.key = label;
            tmp.value = ghscObj;
            ghscResult.push(tmp);
        });
        renderResultPage();
        addChangeListener()
    }

    function addChangeListener() {
        //详细、概要切换按钮
        $("#summary").on("click", function () {
            $("#firstTab li").each(function (i, item) {
                if ($(item).hasClass("active")) {
                    var id = $(item).data("id");
                    $("#" + id + "summary").css("display", "block");
                    $("#" + id + "detail").css("display", "none");
                    if (!$("#summary").hasClass("active")) {
                        $("#summary").addClass("active");
                    }
                    $("#detail").removeClass("active");
                }
            });
        });
        $("#detail").on("click", function () {
            $("#infoContainer ul").find("li").each(function () {
                if ($(this).hasClass("active")) {
                    var id = $(this).data("id");
                    $("#" + id + "detail").css("display", "block");
                    $("#" + id + "summary").css("display", "none");

                    if (!$("#detail").hasClass("active")) {
                        $("#detail").addClass("active");
                    }
                    $("#summary").removeClass("active");
                }
            });
        });
        //导出符合规划图形
        $("#exportSuit").on("click", function () {
            var geometry = JSON.stringify(suitGras);
            var data = {geometries: geometry};
            var result;
            $.ajax({
                url: root + "/geometryService/union",
                data: data,
                async: false, //关闭异步
                success: function (r) {
                    result = r.result;
                    _export(result);
                }
            });
        });

    }


    /**
     * 导出
     * @param geo
     * @private
     */
    function _export(geo) {
        var expType = "cad,shp";
        var types = expType.split(",");
        var r = [];
        arrayUtil.forEach(types, function (item) {
            if (item != 'bj') {
                r.push({alias: getAlias(item), value: item});
            }
        });

        var exportSelTpl = $("#export-select-tpl").html();
        var template = Handlebars.compile(exportSelTpl);
        var content = template({types: r});

        var urlVar = {};
        urlVar.geometry = JSON.stringify({
            "type": "FeatureCollection",
            "features": [{"type": "feature", "geometry": JSON.parse(geo), "properties": {}}]
        });

        var exportHandler = layer.open({
            title: '选择导出格式',
            content: content,
            area: '300px',
            yes: function (index, layero) {
                var type = $(layero).find('select').val();
                if (type == "shp") {
                    exportFile(urlVar, root + "/geometryService/export/shp");
                    layer.close(exportHandler);
                    return;
                } else if (type == "cad") {
                    $.post(root + "/geometryService/rest/export/shp", urlVar, function (str_response) {
                        var id = str_response;
                        var data = {};
                        var shpUrl = "http://" + window.location.hostname + ":" + window.location.port + root + "/file/download/" + id;
                        //全局dwgurl
                        var gpUrl = dwgExpUrl;
                        data.shpUrl = shpUrl;
                        data.gpUrl = gpUrl;
                        $.post(root + "/geometryService/rest/export/dwg", data, function (str_response) {
                            var urlstr = str_response;
                            window.location = urlstr.result;
                            layer.close(exportHandler);
                        });
                    });
                }
            }
        });
    }


    /**
     * 渲染展示页面
     */
    function renderResultPage() {
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

        $("#result-container").css("display", "block");
        if ($("#result-container").hasClass("shrinked")) {
            $('.expand').find("a").trigger("click");
        }

        if (info != undefined) {
            if (info.hasOwnProperty("title"))
                title = "项目名称：" + info.title;
            if (info.hasOwnProperty("area"))
                blArea = "批次总面积:" + info.area;
        }
        //绑定数据
        var data = {proname: title, pcArea: blArea, dkArea: (totalArea / 10000).toFixed(4), info: ghscResult};

        var template = Handlebars.compile(resultTpl);
        var html = template(data);

        $("#result-container .content").empty();
        $("#result-container .content").append(html);

        $('#firstTab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });

        $("#tabContainer").slimScroll({
            height: '185px',
            width: '100%',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        $('.dropdown-toggle').dropdown();
        $(".location-gra").on("click", function () {
            var popGeo;
            var geoJson = $(this).data("geo");
            var jsonObj = geoJson.geometry.coordinates;
            var attr = geoJson.properties;

            var locGra = [];
            arrayUtil.forEach(jsonObj, function (item, index) {
                var polygonJson = {"rings": item, "spatialReference": {"wkid": _map.spatialReference}};
                var polygon = new Polygon(polygonJson);
                if (index == 0) {
                    popGeo = polygon;
                }
                locGra.push(new Graphic(polygon));
            });
            MapUtils.locateFeatures(locGra);
            MapUtils.highlightFeatures(locGra, false);

            var returnFields = [];

            $.each(attr, function (key, values) {
                var obj = {};
                obj.alias = key;
                obj.name = key;
                obj.type = "STRING";
                returnFields.push(obj);
            });

            var geoType = popGeo.type;
            var geoCenter;
            switch (geoType) {
                case 'point':
                    geoCenter = popGeo;
                    break;
                case 'polyline':
                case 'polygon':
                    geoCenter = popGeo.getExtent().getCenter();
                    break;
            }
            geoCenter = lang.mixin(geoCenter, {spatialReference: _map.spatialReference});
            var graphic = new Graphic(lang.mixin(popGeo, {spatialReference: _map.spatialReference}));

            if (popUpStyle == POPUP_OPTION_INFO) {
                if (mapPopup.isShowing) mapPopup.closePopup();
                mapPopup.setData(attr);
                mapPopup.setTitleField(returnFields[1].name);
                mapPopup.setFields(returnFields);
                mapPopup.openPopup(geoCenter).then(function () {
                    if (geoType == 'point')
                        MapUtils.locatePoint(graphic);
                    else
                        MapUtils.locateFeatures([graphic]);
                });
            }
        });
    }

    /**
     * 处理详细
     * @param item
     */
    function parseDetail(item, index) {
        var infoObj = {};
        var lxmc = item.value;
        var area = 0;
        var isSuit = false;
        if (item.name == "010" && item.name != "基本农田保护区") isSuit = true;
        arrayUtil.forEach(groupedAc, function (groupedItem) {
            var lxdm = groupedItem["GroupLabel"];
            if (lxdm == item.name) {
                area = getShapeArea(groupedItem.children);
                if (index == 0) {
                    totalArea += area;
                }
                arrayUtil.forEach(groupedItem.children, function (child) {
                    if (isSuit) {
                        suitGras.push(child.origin);
                    }
                    var tmp = lang.clone(child);
                    tmp.OG_SHAPE_AREA = (tmp.OG_SHAPE_AREA / 10000).toFixed(4);
                    tmp.SHAPE_AREA = (tmp.SHAPE_AREA / 10000).toFixed(4);
                    tmp.lxmc = lxmc;
                    detailAC.push(tmp);
                });
            }
        });
        infoObj = {lxmc: lxmc, area: area, per: 0};
        infoAC.push(infoObj);
    }

    function getShapeArea(arrc) {
        var area = 0;
        arrayUtil.forEach(arrc, function (item) {
            area += Number(item.SHAPE_AREA);
        });
        return area;
    }

    /**
     * 根据字段组织排序
     * @param data
     * @param field
     * @returns {Array}
     */
    function parseGroup(data, field) {
        var result = [];
        arrayUtil.forEach(data, function (goroup) {
            var con = arrayUtil.filter(result, function (item) {
                return goroup[field] == item["GroupLabel"];
            });
            if (con.length > 0) {
                obj = con[0];
                result.splice($.inArray(obj, result), 1);

                obj.children.push(goroup);
                result.push(obj);
            } else {
                var obj = {};
                obj.GroupLabel = goroup[field];
                obj.children = [goroup];
                result.push(obj);
            }
        });
        return result;
    }

    /***
     * 切换当前widget后 移除监听等
     * @private
     */
    function _pause() {
        if (xzSubscribe != null && xzSubscribe != undefined)
            xzSubscribe.remove();
    }

    /***
     *
     * @private
     */
    function _resume() {
        xzSubscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        });
    }

    function exportFile(data, url) {
        if (data == "") {
            console.error("无数据!");
            return;
        }
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;
        for (var key in data) {
            var hideInput = document.createElement("input");
            hideInput.type = "hidden";
            hideInput.name = key;
            hideInput.value = data[key];
            tempForm.appendChild(hideInput);
        }
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
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

    var ghscAC = [
        {
            "name": "JSYDGZQ",
            "label": "建设用地管制区",
            "field": "GZQLXDM"
        },
        {
            "name": "TDYTQ",
            "label": "土地用途分区",
            "field": "TDYTQLXDM"
        },
        {
            "name": "GHJBNTTZ",
            "label": "规划基本农田调整",
            "field": "TZLXDM"
        },
        {
            "name": "MZZDJSXM",
            "label": "重点建设项目",
            "field": "XMLXDM"
        }
    ];

    /**
     * 建设用地管制区类型集合
     * @type {{name: string, value: string}[]}
     */
    var JSYDGZQ_LXMC = [
        {
            "name": "010",
            "value": "允许建设用地区"
        },
        {
            "name": "020",
            "value": "有条件建设用地区"
        },
        {
            "name": "030",
            "value": "限制建设用地区"
        },
        {
            "name": "040",
            "value": "禁止建设用地区"
        }
    ];
    /**
     * 土地用途区类型名称
     * @type {{name: string, value: string}[]}
     */
    var TDYTQ_LXMC = [
        {
            "name": "010",
            "value": "基本农田保护区"
        },
        {
            "name": "020",
            "value": "一般农地区"
        },
        {
            "name": "030",
            "value": "城镇建设用地区"
        },
        {
            "name": "040",
            "value": "村镇建设用地区"
        },
        {
            "name": "050",
            "value": "独立工矿用地区"
        },
        {
            "name": "060",
            "value": "风景旅游用地区"
        },
        {
            "name": "070",
            "value": "生态环境安全控制区"
        },
        {
            "name": "080",
            "value": "自然与文化遗产保护区"
        },
        {
            "name": "090",
            "value": "林业用地区"
        },
        {
            "name": "100",
            "value": "牧业用地区"
        },
        {
            "name": "990",
            "value": "其他用地区"
        }
    ];

    /**
     * 规划基本农田项目类型
     * @type {{name: string, value: string}[]}
     */
    var GHJBNTTZ_LXMC = [
        {
            "name": "00",
            "value": "保留基本农田"
        },
        {
            "name": "01",
            "value": "调入基本农田"
        },
        {
            "name": "02",
            "value": "调出基本农田"
        }
    ];

    /**
     * 重点建设项目类型集合
     * @type {{name: string, value: string}[]}
     */
    var MZZDJSXM_LXMC = [
        {
            "name": "01",
            "value": "能源"
        },
        {
            "name": "02",
            "value": "交通"
        },
        {
            "name": "03",
            "value": "水利"
        },
        {
            "name": "04",
            "value": "电力"
        },
        {
            "name": "05",
            "value": "环保"
        },
        {
            "name": "99",
            "value": "其他"
        }
    ]
    return me;
});