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
                    return new Handlebars.SafeString("<div class='meta'>??????:&nbsp;" + context + "</div>");
            });
            //???????????????
            xzAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_XZJSYD, _map);
            xzAnalysis.setDataSource(_xzConfig.dataSource);
            xzAnalysis.setLinkUrl(_xzConfig.linkUrl);
            xzAnalysis.setAnalysisLyr(_xzConfig.gzqLayerName);
            //???????????????
            $xzAnalysisPanel = $("#xzjsydAnalysisPanel");
            //????????????????????????????????????
            var option = {
                listId: "xzList",
                id: xzAnalysis.getId(),
                queryModeOn: lang.isArray(_xzConfig.scopeLayers) ? true : false
            };
            lang.mixin(option, _xzConfig);

            if (lang.isArray(_xzConfig.year) && _xzConfig.year.length > 0) {
                //?????????????????????????????????
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
                //?????????????????????????????????
                $xzAnalysisPanel.append(renderTpl($("#xzjsydYearSelectTpl").html(), {years: _xzConfig.year}));
                //??????
                $("#yearSelector").multipleSelect({
                    selectAll: false,
                    placeholder: '????????????',
                    allSelected: '????????????',
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
            //???????????????????????????????????????????????????????????????????????????????????????????????????
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
                // ????????????????????????
                geoDataStore.on("onChange", lang.hitch(this, onShareData));
                geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
            }

            //??????????????????????????????????????????
            topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
                clearHandle();
            });
        }

        /**
         * ??????????????????
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
                                tmp.src = "??????";
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
                                tmp.src = "??????";
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
            /**?????????reportInfoUrl ??????????????????????????????bjInfo **/
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
                            updateQueryResult(graItem, r.dkList, "DKID,??????ID,??????id".split(","));
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
                //DPF??????
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
                            title: '????????????????????????',
                            content: content,
                            area: '300px',
                            yes: function (index, layero) {
                                var type = $(layero).find('select').val();
                                switch (type) {
                                    case 'xls':
                                        var url = root + "/static/template/??????.xls";
                                        break;
                                    case 'txt':
                                        var url = root + "/static/template/??????.txt";
                                        break;
                                    case 'cad':
                                        var url = root + "/static/template/??????.dwg";
                                        break;
                                    case  'shp':
                                        var url = root + "/static/template/??????.zip";
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
            //??????????????????
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
         * ????????????
         * @param name
         * @returns {*}
         */
        function getAlias(name) {
            switch (name) {
                case 'xls':
                    return 'excel??????(*.xls)';
                case 'txt':
                    return 'txt??????(*.txt)';
                case 'dwg':
                    return 'cad??????(*.dwg)';
                case 'cad':
                    return 'cad??????(*.dwg)';
                case 'bj':
                    return '???????????????(*.zip)';
                case 'shp':
                    return 'shapfile?????????(*.zip)';
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
            //??????????????????????????????
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
                                    //omp???????????????????????????
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
         * ??????handle
         */
        function clearHandle() {
            xzAnalysis.clear().then(lang.hitch(this, function () {
                $clearBtn.hide();
                $listContainer.empty();
            }));
            _map.graphics.clear();
            var fileInput = document.getElementById('analysis-file-input-' + xzAnalysis.getId());
            //??????fileInput??????
            fileInput.outerHTML = fileInput.outerHTML;
            listenerFileInput();
        }

        /***
         * ??????????????????
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
         * ??????????????????
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
         * ????????????widget??? ???????????????
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