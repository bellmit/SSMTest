/**
 * Created by user on 2016-06-19.
 */
define(["esri/graphic",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/topic",
        "dojo/json",
        "dojox/uuid/generateRandomUuid",
        "map/core/BaseWidget",
        "map/core/BaseAnalysis",
        "map/core/JsonConverters",
        "map/core/GeoDataStore",
        "handlebars",
        "esri/lang",
        "map/utils/MapUtils",
        "text!static/js/map/template/analysis/analysis-basic-tpl.html",
        "text!static/js/map/template/analysis/analysis-list-item.html",
        "static/thirdparty/multi-select/multiple-select.min",
        "css!static/thirdparty/multi-select/multiple-select.css"],
    function (Graphic, declare, lang, arrayUtil, topic, dojoJSON, RandomUuid, BaseWidget, BaseAnalysis, JsonConverters, GeoDataStore, Handlebars,
              esriLang, MapUtils, BasicTpl, ListItem) {

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
                _xzConfig = this.getConfig();
                _init();
                _addListeners();
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
                xzAnalysis.clear();
            }
        });

        var _map, _xzConfig, xzAnalysis;
        var $xzAnalysisPanel, $optContainer, $selectContainer, $clearBtn, $template, $listContainer;
        var xzSubscribe;
        var geoDataStore = GeoDataStore.getInstance();
        var shareResult = [], bjInfo;
        var baseReport, exportSelTpl;

        /***
         * init
         * @private
         */
        function _init() {
            Handlebars.registerHelper('existSrc', function (context, options) {
                if (context != undefined && context != "")
                    return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
            });
            //初始化对象
            xzAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_XZJSYD, _map);
            xzAnalysis.setDataSource(_xzConfig.dataSource);
            xzAnalysis.setLinkUrl(_xzConfig.linkUrl);
            xzAnalysis.setAnalysisLyr(_xzConfig.gzqLayerName);
            //初始化变量
            $xzAnalysisPanel = $("#xzjsydAnalysisPanel");
            //根据配置控制页面显示内容
            var option = {
                listId: "xzList",
                id: xzAnalysis.getId(),
                queryModeOn: lang.isArray(_xzConfig.scopeLayers) ? true : false
            };
            lang.mixin(option, _xzConfig);

            if (lang.isArray(_xzConfig.year) && _xzConfig.year.length > 0) {
                //默认选中最新的年份数据
                var temp = _xzConfig.year.reverse();
                xzAnalysis.setYearList([temp[0]]);
                if (_xzConfig.methodType == 'tdlyxznt') {
                    for (var i = 0; i < temp.length; i++) {
                        if (temp[i].year == '2009') {
                            temp[i].selected = 'selected';
                        }
                    }
                } else {
                    temp[0].selected = 'selected';
                }
                //默认选中最新的年份数据
                $xzAnalysisPanel.append(renderTpl($("#xzjsydYearSelectTpl").html(), {years: _xzConfig.year}));
                //多选
                $("#yearSelector").multipleSelect({
                    selectAll: false,
                    placeholder: '选择年份',
                    allSelected: '所有年份',
                    minimumCountSelected: 5,
                    delimiter: '|'
                });

            }

            $xzAnalysisPanel.append(renderTpl(BasicTpl, option));

            $optContainer = $('#' + xzAnalysis.getId());
            $selectContainer = $('#select_' + xzAnalysis.getId());
            $clearBtn = $optContainer.find('a[data-opt="clear"]');
            $template = $optContainer.find('a[data-opt="template"]');
            exportSelTpl = $("#export-select-xzjsyd-tpl").html();
            if (_xzConfig.showTemplate == true) {
                $template.show();
            }
            $listContainer = $("#xzList");
            //判断是否配置定位查询选择功能，若配置则显示不配置则监听定位数据变化
            if (xzAnalysis.locQuery == true) {
                $(".div-dataSource-top").click(function () {
                    $(this).find("em").removeClass("animation-top2").addClass("animation-top").parents(".div-data-source").siblings().children(".div-dataSource-top").find("em").removeClass("animation-top").addClass(".animation-top2");
                    $(this).next(".ul-dataSource").toggle().parents(".div-data-source").siblings().find(".ul-dataSource").hide();
                });
                $("#xzjsydDataTypeUl>li").click(function () {
                    var selva = $(this).text();
                    var value = $(this).attr("code")
                    $(this).parents("#xzjsydDataTypeUl").siblings(".div-dataSource-top").find("span").text(selva);
                    $(this).parents("#xzjsydDataTypeUl").siblings(".div-dataSource-top").find("span").attr("code", value);
                    $(this).parent("ul").hide();
                    $(this).parents(".div-data-source").children(".div-dataSource-top").find("em").addClass("animation-top2");
                    dataType = value;
                    // postChart();
                });
            } else {
                $("#hideXzjsydDiv").hide();
                // 监听共享数据变化
                geoDataStore.on("onChange", lang.hitch(this, onShareData));
                geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
            }

            //监听地图上的绘制要素清除事件
            topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
                clearHandle();
            });
        }

        /**
         * 获取共享数据
         * @param data
         */
        function onShareData(data) {
            if (data !== undefined) {
                var type = data.type;
                switch (type) {
                    case GeoDataStore.SK_LOC: {
                        var fs = data.features;
                        if (lang.isArray(fs)) {
                            arrayUtil.forEach(fs, function (item) {
                                var tmp = {};
                                var attr = item.attributes;
                                tmp.id = RandomUuid();
                                tmp.type = "tdlyxz";
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
                                tmp.type = "tdlyxz";
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
                                shareResult.push(tmp);
                                _appendList(tmp);
                            });
                        }
                        break;
                    }
                }
                for (var i = 0; i < shareResult.length; i++) {
                    var item = shareResult[i];
                    if (_xzConfig.hasOwnProperty("reportInfoUrl")) {
                        requestBjInfo(item);
                    }
                    xzAnalysis.addGraphic(item);
                }
            }
        }

        /***
         *
         * @param graItem
         */
        function requestBjInfo(graItem) {
            /**若存在reportInfoUrl 则请求其地址并赋值到bjInfo **/
            var url = _xzConfig.reportInfoUrl;
            //get key
            var arr = [];
            var attr = graItem.graphic.attributes;
            for (var key in attr) {
                if (attr.hasOwnProperty(key)) {
                    arr.push(key);
                }
            }
            if (arr && arr.length > 0) {
                url = esriLang.substitute(attr, url);
            }
            if (url != "") {
                $.getJSON("/omp/map/proxy?requestUrl=" + url, null, function (r) {
                    try {
                        if (typeof r === 'string') {
                            r = $.parseJSON(r);
                        }
                        var baseReport = {};
                        $.each(r, function (k, v) {
                            if (k != 'dkList') {
                                baseReport[k] = v || 0;
                            }
                        });
                        console.log("baseReport" + baseReport);
                        bjInfo = {};
                        if (r.hasOwnProperty("dkList") && r.dkList) {
                            updateQueryResult(graItem, r.dkList, "DKID,地块ID,地块id".split(","));
                        }
                    } catch (err) {
                        alert("this is a question")
                    }
                });
            }
        }

        /***
         *
         * @param item
         * @param array
         * @param fields
         */
        var flag = [];
        var item_;

        function updateQueryResult(item, array, fields) {
            item_ = item;
            var attr = item.graphic.attributes;
            flag = array;
            if (attr == null) attr = {};
            var idValue = undefined;
            arrayUtil.forEach(fields, function (f) {
                if (idValue != undefined) {
                    idValue = attr[f];
                }
            });
            var tmpArr = arrayUtil.filter(array, function (item) {
                return item.dkid === idValue;
            });
            if (tmpArr.length > 0) {
                var tmp = tmpArr[0];
                var _b = {};
                _b.area = Number(tmp.area);
                _b.nydArea = Number(tmp.nydArea);
                _b.gdArea = Number(tmp.gdArea);
                _b.jsydArea = Number(tmp.jsydArea);
                _b.wlydArea = Number(tmp.wlydArea);
                //DPF添加
                _b.jtArea = Number(tmp.jtArea);
                _b.gyArea = Number(tmp.gyArea);

                attr["INFO"] = _b;
                item.graphic.attributes = attr;
                xzAnalysis.addGraphic(item);
            }
        }

        /***
         * add listeners
         * @private
         */
        function _addListeners() {
            $optContainer.on('click', 'a', function () {
                var opt = $(this).data("opt");
                switch (opt) {
                    case 'draw':
                        xzAnalysis.draw('polygon', true).then(function (obj) {
                            if (obj != undefined) {
                                _appendList(obj);
                            }
                            $clearBtn.show();
                        });
                        break;
                    case 'imp':
                        break;
                    case 'analysis':
                        if (flag.length == 1) {
                            var g = item_.graphic;
                            var geometry = JsonConverters.toGeoJson(g);
                            xzAnalysis.setReport(lang.isString(g.attributes.INFO) ? g.attributes.INFO : dojoJSON.stringify(g.attributes.INFO));
                            xzAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        } else {
                            xzAnalysis.setAnalysisGeometry(null);
                            xzAnalysis.setReport(typeof baseReport === 'string' ? baseReport : dojoJSON.stringify(baseReport));
                        }
                        try {
                            doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }

                        break;
                    case 'template':
                        var template = Handlebars.compile(exportSelTpl);
                        var content = template({types: parseTypesArray});
                        layer.open({
                            title: '选择下载模板格式',
                            content: content,
                            area: '300px',
                            yes: function (index, layero) {
                                var type = $(layero).find('select').val();
                                switch (type) {
                                    case 'xls':
                                        var url = root + "/static/template/模板.xls";
                                        break;
                                    case 'txt':
                                        var url = root + "/static/template/模板.txt";
                                        break;
                                    case 'cad':
                                        var url = root + "/static/template/模板.dwg";
                                        break;
                                    case  'shp':
                                        var url = root + "/static/template/模板.zip";
                                        break;
                                }
                                window.open(url);
                                layer.close(index);
                            }
                        });
                        break;
                    case 'clear': {
                        clearHandle();
                        break;
                    }
                }
            });
            $selectContainer.on('click', 'a', function () {
                var drawType = $(this).data("type");
                xzAnalysis.draw(drawType, false).then(function (r) {
                    xzAnalysis.queryGeo(r);
                });
            });
            //导入事件监听
            listenerFileInput();
            $('#yearSelector').on("change", function () {
                var selArr = $(this).multipleSelect('getSelects');
                if (selArr.length > 0) {
                    var years = _xzConfig.year;
                    var data = arrayUtil.filter(years, function (item) {
                        return arrayUtil.indexOf(selArr, item.year) > -1;
                    });
                    xzAnalysis.setYearList(data);
                }
            });
            $('#xzjsydDataTypeUl').on("click", function () {
                if (dataType == "SK_LOC") {
                    geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
                }
                else if (dataType == "SK_QUERY") {
                    geoDataStore.fetch(GeoDataStore.SK_QUERY).then(lang.hitch(this, onShareData));
                }
            });
        }

        /***
         *
         * @returns {Array}
         */
        function parseTypesArray() {
            var expType = _xzConfig.importType;
            var types = expType.split(",");
            var r = [];
            arrayUtil.forEach(types, function (item) {
                r.push({alias: getAlias(item), value: item});
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
                case 'cad':
                    return 'cad文件(*.dwg)';
                case 'bj':
                    return '电子报件包(*.zip)';
                case 'shp':
                    return 'shapfile压缩包(*.zip)';
                default:
                    return name;
            }
        }

        function getQueryVariable(variable) {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] == variable) {
                    return pair[1];
                }
            }
            return (false);
        }

        function doAnalysis() {
            //判断是否需要电子报件
            if (!_xzConfig.dzbjUrl) {
                xzAnalysis.doAnalysis();
            } else {
                var proid = getQueryVariable("proid");
                $.ajax({
                    url: _xzConfig.dzbjUrl + "?dkid=" + proid,
                    success: function (data) {
                        data = JSON.parse(data);
                        if (data["dkList"]) {
                            for (var i = 0; i < data["dkList"].length; i++) {
                                var item = data["dkList"][i];
                                if (item["dkid"] && item["dkid"] == proid) {
                                    //omp系统默认发挥为公顷
                                    var result = JSON.stringify(item);
                                    xzAnalysis.setReport(result);
                                }
                            }
                        }
                        try {
                            xzAnalysis.doAnalysis();
                        } catch (er) {
                            console.log(er);
                        }
                    },
                    error: function (er) {
                        console.log(er);
                        xzAnalysis.doAnalysis();
                    }
                });
            }
        }

        /***
         * 清除handle
         */
        function clearHandle() {
            xzAnalysis.clear().then(lang.hitch(this, function () {
                $clearBtn.hide();
                $listContainer.empty();
            }));
            _map.graphics.clear();
            var fileInput = document.getElementById('analysis-file-input-' + xzAnalysis.getId());
            //清除fileInput内容
            fileInput.outerHTML = fileInput.outerHTML;
            listenerFileInput();
        }

        /***
         * 上传文件监听
         */
        function listenerFileInput() {
            $('#analysis-file-input-' + xzAnalysis.getId()).off('change');
            $('#analysis-file-input-' + xzAnalysis.getId()).on('change', function () {
                xzAnalysis.importFile($('#analysis-file-input-' + xzAnalysis.getId()));
                timer = setTimeout(function () {
                    listenerFileInput();
                }, 2000);
            });
        }

        /***
         * 添加要素列表
         * @param obj
         * @private
         */
        function _appendList(obj) {
            $listContainer.append(renderTpl(ListItem, obj));
            var scrollHeight = $(window).height() - 300;
            $listContainer.slimScroll({
                height: scrollHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
            $clearBtn.show();
            $("#xzList .a-r-btn").unbind();
            $("#xzList .a-r-btn").on('click', function () {
                var t = $(this).data("type");
                var id = $(this).data("id");

                xzAnalysis.findGraById(id).then(function (g) {
                    switch (t) {
                        case 'location':
                            MapUtils.highlightFeatures([new Graphic(g.geometry)], false);
                            MapUtils.locateFeatures([g]);
                            break;
                        case 'analysis':
                            var geometry = JsonConverters.toGeoJson(g);
                            if (esriLang.isDefined(g.attributes) && esriLang.isDefined(g.attributes.INFO)) {
                                xzAnalysis.setReport(lang.isString(g.attributes.INFO) ? g.attributes.INFO : dojoJSON.stringify(g.attributes.INFO));
                            }
                            xzAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                            doAnalysis();
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
            if (xzSubscribe != null && xzSubscribe != undefined)
                xzSubscribe.remove();
        }


        /***
         *
         * @private
         */
        function _resume() {
            topic.subscribe("report/info", function (data) {
                if (esriLang.isDefined(data))
                    baseReport = data;
            });
            xzSubscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
                _appendList(data);
            });
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

        return me;
    }
);