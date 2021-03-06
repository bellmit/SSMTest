/**
 * η»Όεεζ
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/14 13:40
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/topic",
        "dojox/uuid/generateRandomUuid",
        "handlebars",
        "esri/graphic",
        "esri/Color",
        "layer",
        "map/core/BaseWidget",
        "map/utils/MapUtils",
        "map/core/JsonConverters",
        "map/core/BaseAnalysis",
        "map/core/GeoDataStore",
        "map/core/EsriSymbolsCreator",
        "ko",
        "./AnalysisLevels",
        "text!static/js/map/template/analysis/analysis-basic-tpl.html",
        "text!static/js/map/template/analysis/analysis-list-item.html",
        "esri/tasks/FeatureSet",
        "map/core/GeometryIO",
        "colorPicker",
        "css!static/thirdparty/bootstrap-colorpicker/css/bootstrap-colorpicker.css"
        ],
    function (declare, lang, arrayUtil, topic, RandomUuid, Handlebars, Graphic, Color, layer, BaseWidget, MapUtils,
              JsonConverters, BaseAnalysis, GeoDataStore, EsriSymbolsCreator, ko, AnalysisLevels, baseTpl, listItem, FeatureSet, GeometryIO) {

        var _map, _multiConfig, multipleAnalysis;
        var multipleSubscribe, vm;

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
                _multiConfig = this.getConfig();
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
                multipleAnalysis.clear();
            }
        });

        var $optContainer, $listContainer, $clearBtn;
        var drawBtnHandler;
        var geoDataStore = GeoDataStore.getInstance();

        //yearChooseζ§εΆη»ΌεεζδΈ­η°ηΆεζζ―ε¦θΏθ‘εΉ΄εΊ¦ιζ©
        var yearChoose = false;
        var xzYear = [];
        var xzParam;
        var bpParam;
        var locationWithoutHeight = false;
        var exportSelTpl;
        var geometryIO = new GeometryIO();
        var color;
        var fillColor=[0,0,0,0],lineColor=[255,0,0,1],locFillColor=[0,0,0,0];
        var duration=300000;

        /***
         * init
         * @private
         */
        function _init() {
            layer.config();
            Handlebars.registerHelper('existSrc', function (context, options) {
                if (context != undefined && context != "")
                    return new Handlebars.SafeString("<div class='meta'>ζ₯ζΊ:&nbsp;" + context + "</div>");
            });

            multipleAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_MULTIPLE, _map);
            multipleAnalysis.setAppConfig(appConfig);
            multipleAnalysis.setJsonParams(JSON.stringify(_multiConfig.jsonParams));
            multipleAnalysis.setLevel(_multiConfig.level);
            multipleAnalysis.setLink(_multiConfig.link);
            multipleAnalysis.setDecimal(_multiConfig.decimal);
            multipleAnalysis.setScopeLayers(_multiConfig.scopeLayers);
            if (_multiConfig.hasOwnProperty("yx")) {
                multipleAnalysis.setYx(_multiConfig.yx);
            }
            if (_multiConfig.hasOwnProperty("fillColor")) {
                fillColor=_multiConfig.fillColor;
            }
            if (_multiConfig.hasOwnProperty("locFillColor")) {
                locFillColor=_multiConfig.locFillColor;
            }
            if (_multiConfig.hasOwnProperty("lineColor")) {
                lineColor=_multiConfig.lineColor;
            }
            if (_multiConfig.hasOwnProperty("duration")) {
                duration=_multiConfig.duration;
            }


            //DPFζ·»ε 
            var arr = [];
            // var yearsArr;
            //
            // var yearsArr = arrayUtil.filter(_multiConfig.jsonParams, function (item) {
            //     if(item.funid==='xz'){
            //         yearsArr=item.
            //     }
            //     return item.funid === 'xz';
            // });
            //

            exportSelTpl = $("#export-select-tpl").html();
            // var xzYear = yearsArr[0].year;
            getXzParam();
            getGhParam();
            if (xzYear.length !== 0 && typeof(xzYear) != "undefined") {
                //δΈζη§ιη½?
                arr = [];
            } else {
                for (var i = 0; i < xzYear.length; i++) {
                    var val = xzYear[i].year;
                    arr.push(val);
                }
            }
            //init vm
            if (yearChoose == true) {
                vm = new ViewModel(_multiConfig.hasOwnProperty("tdlyxzYear") ? _multiConfig.tdlyxzYear : []);
            } else {
                vm = new ViewModel(_multiConfig.hasOwnProperty("tdlyxzYear") ? arr : []);
            }
            ko.applyBindings(vm, document.getElementById('xzYearKo'));
            //ζ Ήζ?ιη½?ζ§εΆι‘΅ι’ζΎη€Ίεε?Ή
            var option = {
                listId: "multipleList",
                id: multipleAnalysis.getId(),
                queryModeOn: lang.isArray(_multiConfig.scopeLayers)
            };
            lang.mixin(option, _multiConfig);
            $("#multipleAnalysisPanel").append(renderTpl(baseTpl, option));
            if(_multiConfig.color===true){
                $("#colorPicker").removeAttr("style");
            }

            $('#colorPicker').colorpicker().on('changeColor', function(ev){
                color =ev.color.toRGB();
                var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                    EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.solid, color, 2),
                    new Color([0,0,0,0]));
                multipleAnalysis.setFillSymbol(fillSymbol);
                var grasArr=multipleAnalysis.graphicsLyr.graphics;
                $.each(grasArr,function (index,item) {
                    item.setSymbol(EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.solid, color, 2));
                });
            });
            $optContainer = $('#' + multipleAnalysis.getId());
            $clearBtn = $optContainer.find('a[data-opt="clear"]');
            $listContainer = $("#multipleList");



            //ε€ζ­ζ―ε¦ιη½?ε?δ½ζ₯θ―’ιζ©εθ½οΌθ₯ιη½?εζΎη€ΊδΈιη½?εηε¬ε?δ½ζ°ζ?εε
            if (_multiConfig.locQuery == true) {
                $(".div-dataSourceTop-mu").click(function () {
                    $(this).find("em").removeClass("animation-top2").addClass("animation-top").parents(".div-dataSource-mu").siblings().children(".div-dataSourceTop-mu").find("em").removeClass("animation-top").addClass(".animation-top2");
                    $(this).next(".ul-dataSource-mu").toggle().parents(".div-dataSource-mu").siblings().find(".ul-dataSource-mu").hide();
                });
                $("#dataMuTypeUl>li").click(function () {
                    var selva = $(this).text();
                    var value = $(this).attr("code")
                    $(this).parents("#dataMuTypeUl").siblings(".div-dataSourceTop-mu").find("span").text(selva);
                    $(this).parents("#dataMuTypeUl").siblings(".div-dataSourceTop-mu").find("span").attr("code", value);
                    $(this).parent("ul").hide();
                    $(this).parents(".div-dataSource-mu").children(".div-dataSourceTop-mu").find("em").addClass("animation-top2");
                    dataType = value;
                });
            } else {
                $("#hideMuDiv").hide();
                //ηε¬ε±δΊ«ζ°ζ?εε
                geoDataStore.on("onChange", lang.hitch(this, onShareData));
                geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
            }


            //ηε¬ε±δΊ«ζ°ζ?εε
            // geoDataStore.on("onChange", lang.hitch(this, onShareData));
            // geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

            //ηε¬ε°εΎδΈηη»εΆθ¦η΄ ζΈι€δΊδ»Ά
            topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
                clearHandle();
            });
        }

        /**
         * θ·ειη½?ζδ»Άζ―ε¦ιη½?yearChooseεtdlyxzYearεζ°
         * yearChoose  ζ§εΆη»ΌεεζδΈ­η°ηΆεζζ―ε¦θΏθ‘εΉ΄εΊ¦ιζ©
         * tdlyxzYear  θΏθ‘ιζ©ηεΉ΄εΊ¦
         *
         */
        function getXzParam() {
            arrayUtil.forEach(_multiConfig.jsonParams, function (param) {
                if (param.funid === "xz") {
                    yearChoose = param.hasOwnProperty("yearChoose") ? param.yearChoose : false;
                    xzYear = _multiConfig.hasOwnProperty("tdlyxzYear") ? _multiConfig.tdlyxzYear : [];
                }
            });
        }

        function getGhParam() {
            arrayUtil.forEach(_multiConfig.jsonParams, function (param) {
                if (param.funid === "bp") {
                    yearChoose = param.hasOwnProperty("yearChoose") ? param.yearChoose : false;
                    xzYear = _multiConfig.hasOwnProperty("tdlyxzYear") ? _multiConfig.tdlyxzYear : [];
                }
            });
        }

        /***
         * vm
         * @param marks
         * @constructor
         */
        function ViewModel(years) {
            var self = this;
            self.xzYears = ko.observableArray(years);
            if (yearChoose === true && years.length > 0) {
                var year = years[0];
                if(_multiConfig.level=="tc"){
                    BpParamTc(year)
                }
                yearXzParam(year);
            }
            self.changeXzYear = function (vm, evt) {
                console.info($(evt.currentTarget).val());
                var year = $(evt.currentTarget).val();
                if(_multiConfig.level=="tc"){
                    BpParamTc(year)
                }
                yearXzParam(year);
            }
        }


        /**
         * ζ Ήζ?εε°ιζ©εΉ΄εΊ¦θ°ζ΄εε°ε©η¨η°ηΆεζ°
         * @param year
         */
        function yearXzParam(year) {
            var tmp;
            tmp = arrayUtil.filter(_multiConfig.jsonParams, function (item) {
                return item.funid === 'xz';
            });
            if (tmp.length > 0) {
                if (year !== null) {
                    var dltbStr = tmp[0].dltb.substring(0, tmp[0].dltb.length - 4);
                    var xzdwStr = tmp[0].xzdw.substring(0, tmp[0].dltb.length - 4);
                    tmp[0].dltb = dltbStr.concat(year);
                    tmp[0].xzdw = xzdwStr.concat(year);
                    tmp[0].year = year;
                    xzParam = tmp[0];
                }
            }
        }

        function BpParamTc(year) {
            var tmp;
            tmp = arrayUtil.filter(_multiConfig.jsonParams, function (item) {
                return item.funid === 'bp';
            });
            if (tmp.length > 0) {
                if (year !== null) {
                    tmp[0].year = year;
                    bpParam = tmp[0];
                }
            }
        }

        /***
         * θ·εε±δΊ«ζ°ζ?
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
                                tmp.type = "multiple";
                                tmp.style = "teal";
                                tmp.title = "";
                                tmp.graphic = item;
                                tmp.src = "ε?δ½";
                                for (var k in attr) {
                                    if (attr[k] != undefined) {
                                        tmp.title = attr[k];
                                        break;
                                    }
                                }
                                multipleAnalysis.addGraphic(tmp);
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
                                tmp.type = "multiple";
                                tmp.style = "teal";
                                tmp.src = "ζ₯θ―’";
                                tmp.title = "";
                                tmp.graphic = item;
                                for (var k in attr) {
                                    if (attr[k] != undefined && k != 'OBJECTID') {
                                        tmp.title = attr[k];
                                        break;
                                    }
                                }
                                multipleAnalysis.addGraphic(item);
                                _appendList(tmp);
                            });
                        }
                        break;
                    }
                }
            }
        }

        /***
         *
         * @private
         */
        function _addListener() {
            $optContainer.on('click', 'a', function () {
                var opt = $(this).data("opt");
                switch (opt) {
                    case 'draw':
                        if(_multiConfig.custom===true){
                            var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                                EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                                new Color(fillColor));
                            multipleAnalysis.draw('polygon', true,reSymbol).then(function (obj) {
                                //ε°ε―Ήθ±‘ζΈ²ζε°ι‘΅ι’εθ‘¨δΈ­
                                if (obj != undefined) {
                                    _appendList(obj);
                                    $clearBtn.show();
                                }
                            })}else {
                            multipleAnalysis.draw('polygon', true).then(function (obj) {
                                //ε°ε―Ήθ±‘ζΈ²ζε°ι‘΅ι’εθ‘¨δΈ­
                                if (obj != undefined) {
                                    _appendList(obj);
                                    $clearBtn.show();
                                }
                            })
                        }
                        break;
                    case 'imp':
                        $('#analysis-file-input').click();
                        break;
                    case 'analysis':
                        beforeAnalyze(null);
                        break;
                    case 'exp':
                        multipleAnalysis.exportFeature(null);
                        break;
                    case 'clear':
                        clearHandle();
                        break;
                }
            });
            //ζ₯θ―’δΊδ»Άηε¬
            drawBtnHandler = $(".a-draw-content").on('click', 'a', function () {
                var drawType = $(this).data("type");
                multipleAnalysis.draw(drawType, false).then(function (r) {
                    multipleAnalysis.queryGeo(r);
                });
            });
            //ε―Όε₯δΊδ»Άηε¬
            listenerFileInput();

            //ηε¬ζ°ζ?ζ₯ζΊ
            $('#dataMuTypeUl').on("click", function () {
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
            multipleAnalysis.clear().then(lang.hitch(this, function () {
                $clearBtn.hide();
                $listContainer.empty();
            }));
            _map.graphics.clear();
            var fileInput = document.getElementById('analysis-file-input-' + multipleAnalysis.getId());
            //ζΈι€fileInputεε?Ή
            fileInput.outerHTML = fileInput.outerHTML;
            listenerFileInput();
        }


        /***
         * εζθΏθ‘δΉε
         * ιζ©εͺδΊεζη±»η?εδΈζ­€ζ¬‘εζ
         */
        function beforeAnalyze(geo) {
            if (yearChoose === true) {
                arrayUtil.forEach(_multiConfig.jsonParams, function (param) {
                    if (param.funid === "xz") {
                        param = xzParam;
                    }else if (param.funid==="bp") {
                        param=bpParam
                    }
                })
            }
            lyrs = _multiConfig.jsonParams;
            layer.open({
                type: 1,
                content: renderTpl($("#lyrsChosenTpl").html(), {layers: lyrs}),
                shadeClose: false,
                shade: 0.6,
                area: '460px',
                title: 'εζιζ©',
                btn: ['η‘?ε?'],
                btnAlign: 'c',
                success: function (layero, index) {
                    $(".omp-checkbox").on('click', function () {
                        var funid = $(this).data("funid");
                        var tmp = arrayUtil.filter(lyrs, function (item) {
                            return item.funid === funid;
                        });
                        if ($(this).hasClass('omp-checked')) {
                            $(this).removeClass('omp-checked');
                            if (tmp.length > 0) {
                                tmp[0].visible = false;
                            }
                        } else {
                            $(this).addClass('omp-checked');
                            if (tmp.length > 0) {
                                tmp[0].visible = true;
                            }
                        }
                    });
                    //ηε¬ ε¨ι/ει
                    $(".btn-lyr-sel").on('click', function () {
                        var opt = $(this).data("opt");
                        switch (opt) {
                            case 'all':
                                var $target = $(".omp-checkbox").not($(".omp-checkbox[class*='omp-checked']"));
                                if ($target.length > 0) {
                                    $target.trigger('click');
                                }
                                break;
                            case 'reverse':
                                $(".omp-checkbox").trigger('click');
                                break;
                        }
                    });
                },
                yes: function (index) {
                    layer.close(index);
                    //εͺδΌ ιιζ©ηεζη±»η? ζͺιδΈ­ηεδΈδΌ ι
                    var selArr = arrayUtil.filter(_multiConfig.jsonParams, function (item) {
                        return item.visible === true || item.visible === 'true';
                    });
                    if (selArr.length > 0) {
                        multipleAnalysis.setJsonParams(JSON.stringify(selArr));
                        multipleAnalysis.setAnalysisGeometry(geo);
                        try {
                            if (_multiConfig.level === AnalysisLevels.XINYI || _multiConfig.level === AnalysisLevels.CHANGZHOU || _multiConfig.level === AnalysisLevels.BOTTOM || _multiConfig.level === AnalysisLevels.HAERBIN) {
                                showResultPanel();
                                multipleAnalysis.setTargetName("resultIframe");
                            }
                            multipleAnalysis.doAnalysis();
                        } catch (e) {
                            console.error(e.message);
                        }
                    } else {
                        layer.msg('ζͺιζ©δ»»δ½εζ!');
                        return false;
                    }
                }
            });
        }

        /**
         *
         */
        function listenerFileInput() {
            $('#analysis-file-input-' + multipleAnalysis.getId()).off('change');
            $('#analysis-file-input-' + multipleAnalysis.getId()).on('change', function () {
                if(_multiConfig.custom===true){
                    var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                        EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                        new Color(fillColor));
                multipleAnalysis.importFile($('#analysis-file-input-' + multipleAnalysis.getId()),reSymbol);
                }
                else {
                    multipleAnalysis.importFile($('#analysis-file-input-' + multipleAnalysis.getId()));
                }
            });
            setTimeout(function () {
                listenerFileInput();
            }, 2000);
        }

        /***
         * ζ·»ε θ¦η΄ εθ‘¨
         * @param obj
         * @private
         */
        function _appendList(obj) {
            $listContainer.append(renderTpl(listItem, obj));
            if(_multiConfig.exportType!==undefined){
                var btn=$("#multipleList").find("#exportBtn");
                btn.show()
            }
            var scrollHeight = $(window).height() - 300;
            $listContainer.slimScroll({
                height: scrollHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
            $clearBtn.show();
            $("#multipleList .a-r-btn").unbind();
            $("#multipleList .a-r-btn").on('click', function () {
                var t = $(this).data("type");
                var id = $(this).data("id");
                multipleAnalysis.findGraById(id).then(function (g) {
                    switch (t) {
                        case 'location':
                            if(_multiConfig.custom===true){
                                var locGras=[new Graphic(g.geometry)];
                                var locSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                                    EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH, new Color(lineColor), 2),
                                    new Color(locFillColor));
                                arrayUtil.forEach(locGras, function (item) {
                                    item.setSymbol(locSymbol)
                                });
                                MapUtils.highlightFeatures_TZ(locGras, false);
                                MapUtils.locateFeatures([g]);
                                MapUtils.flashFeatures([g],500,null,duration);
                            }else{
                                MapUtils.highlightFeatures([new Graphic(g.geometry)], false);
                                MapUtils.locateFeatures([g]);
                            }
                            break;
                        case 'analysis':
                            var geometry = JsonConverters.toGeoJson(g);
                            beforeAnalyze(JSON.stringify(geometry));
                            break;
                        case "export":
                            var attr = [];
                            attr.push(g);
                            _export(attr);
                            multipleAnalysis.exportFeature(g);
                            break;
                    }
                });

            });
        }

        /***
         * εζ’ε½εwidgetε η§»ι€ηε¬η­
         * @private
         */
        function _export(attr) {
            var featureSet = new FeatureSet();
            featureSet.features = attr;
            var template = Handlebars.compile(exportSelTpl);
            var content = template({types: parseTypesArray});

            layer.open({
                title: 'ιζ©ε―ΌεΊζ ΌεΌ',
                content: content,
                area: '300px',
                yes: function (index, layero) {
                    var type = $(layero).find('select').val();
                    layer.close(index);
                    geometryIO.expToFile(featureSet, type);
                }
            });
        }
        function parseTypesArray() {
            var expType = _multiConfig.exportType;
            var types = expType.split(",");
            var r = [];
            arrayUtil.forEach(types, function (item) {
                if (item != 'bj') {
                    r.push({alias: getAlias(item), value: item});
                }
            });
            return r;
        }
        function getAlias(name) {
            switch (name) {
                case 'xls':
                    return 'excelζδ»Ά(*.xls)';
                case 'txt':
                    return 'txtζδ»Ά(*.txt)';
                case 'dwg':
                    return 'cadζδ»Ά(*.dwg)';
                case 'bj':
                    return 'η΅ε­ζ₯δ»Άε(*.zip)';
                case 'shp':
                    return 'shapfileεηΌ©ε(*.zip)';
                default:
                    return name;
            }
        }
        function _pause() {
            if (multipleSubscribe != null && multipleSubscribe != undefined)
                multipleSubscribe.remove();
            // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
            // if(lyr!=null){
            //     _map.removeLayer(lyr);
            // }
        }

        function _resume() {
            multipleSubscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
                _appendList(data);
            });
            // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
            // if(lyr==null){
            //     _map.addLayer(multipleAnalysis.getGraphicsLyr());
            // }
        }


        /**
         * εζη»ζζΎη€Ίε¨ ε°εΎδΈζΉηη»ζζ‘δΈ­
         * ε©η¨ iframe ζΎη€Ί
         * @return iframe εη§°
         */
        function showResultPanel() {

            $("#result-container").show();
            if ($("#result-container").hasClass("shrinked")) {
                $('.expand').find("a").trigger("click");
            }
            // η»ε?ζ°ζ?
            $("#result-container .content").empty();
            $("#result-container .content").append(renderTpl($("#resultPanelTpl").html(), {}));

            var oFrm = $("#resultIframe")[0];
            oFrm.onload = oFrm.onreadystatechange = function () {
                if (this.readyState && this.readyState !== 'complete') return;
                else {
                    $("#iframeLoading").remove();
                }
            };
            var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH, "#4cae4c", 2), //#4cae4c  #ffc555
                EsriSymbolsCreator.colorFromHex('#5cb85c'));  // #5cb85c ff6686
            var gras = [];
            var heightLightSaveFeature = [];
            if (!window.doXinyiLoc) {
                window.doXinyiLoc = function (fc) {
                    if (fc !== undefined) {
                        if (fc === '') {
                            layer.msg('ζͺθ·εε°εΎε½’δΏ‘ζ―');
                            return false;
                        }
                        if (typeof fc === 'string') {
                            fc = $.parseJSON(fc);
                        }
                        var featureObj = JsonConverters.toEsri(fc, _map.spatialReference);
                        var gras = [];
                        if (featureObj.hasOwnProperty("features") && lang.isArray(featureObj.features)) {
                            arrayUtil.forEach(featureObj.features, function (feature) {
                                var g = new Graphic(feature);
                                g.setSymbol(fillSymbol);
                                gras.push(g);
                                _map.graphics.add(g);
                            });
                            if (gras.length > 0) {
                                MapUtils.locateFeatures(gras, 3);
                                MapUtils.flashFeatures(gras, 600, function () {
                                    setTimeout(function () {
                                        arrayUtil.forEach(gras, function (f) {
                                            _map.graphics.remove(f);
                                        });
                                    }, 2000);
                                });
                            }
                        } else {
                            if (featureObj.hasOwnProperty('rings')) {
                                var g = new Graphic({geometry: featureObj});
                                g.setSymbol(fillSymbol);
                                gras.push(g);
                                _map.graphics.add(g);
                                if (gras.length > 0) {
                                    MapUtils.locateFeatures(gras, 3);
                                    MapUtils.flashFeatures(gras, 600, function () {
                                        setTimeout(function () {
                                            arrayUtil.forEach(gras, function (f) {
                                                _map.graphics.remove(f);
                                            });
                                        }, 2000);
                                    });
                                }
                            }
                        }
                    }
                }
            }
            if (!window.doBottomLoc) {
                window.doBottomLoc = function (fc) {
                    if (fc !== undefined) {
                        if (fc === '') {
                            layer.msg('ζͺθ·εε°εΎε½’δΏ‘ζ―');
                            return false;
                        }
                        if (typeof fc === 'string') {
                            fc = $.parseJSON(fc);
                        }
                        var featureObj = JsonConverters.toEsri(fc, _map.spatialReference);
                        var gras = [];
                        if (featureObj.hasOwnProperty("features") && lang.isArray(featureObj.features)) {
                            arrayUtil.forEach(featureObj.features, function (feature) {
                                arrayUtil.forEach(heightLightSaveFeature, function (f) {
                                    _map.graphics.remove(f);
                                    heightLightSaveFeature = [];
                                });
                                var g = new Graphic(feature);
                                g.setSymbol(fillSymbol);
                                gras.push(g);
                                _map.graphics.add(g);
                            });
                            if (gras.length > 0) {
                                MapUtils.locateFeatures(gras, 3);
                                MapUtils.flashFeatures(gras, 600);
                            }
                        } else {
                            if (featureObj.hasOwnProperty('rings')) {
                                arrayUtil.forEach(heightLightSaveFeature, function (f) {
                                    _map.graphics.remove(f);
                                    heightLightSaveFeature = [];
                                });
                                var g = new Graphic({geometry: featureObj});
                                g.setSymbol(fillSymbol);
                                gras.push(g);
                                heightLightSaveFeature.push(g);
                                _map.graphics.add(g);
                                if (gras.length > 0) {
                                    MapUtils.locateFeatures(gras, 3);
                                    MapUtils.flashFeatures(gras, 600);
                                }
                            }
                        }
                    }
                }
            }
            if(!window.heightLightFeature){
                window.heightLightFeature = function (fcs) {
                    if (fcs !== undefined) {
                        locationWithoutHeight = true;
                        if (fcs === '') {
                            layer.msg('ζͺθ·εε°εΎε½’δΏ‘ζ―');
                            return false;
                        }
                        if (typeof fcs === 'string') {
                            fcs = $.parseJSON(fcs);
                        }
                        arrayUtil.forEach(fcs,function (fc) {
                            fc.features.forEach(function (feature) {
                                var featureObj = JsonConverters.toEsri(feature.geometry, _map.spatialReference);
                                var styleHeight={
                                    lineStyle: "solid",
                                    lineColor: "#ff0000",
                                    width: "5px",
                                    fillStyle: "solid",
                                    fillColor: "#3f8599"
                                };
                                var highlightFeatureOpacit = 0.5;
                                if (featureObj.hasOwnProperty('rings')) {
                                    var g = new Graphic({geometry: featureObj});
                                    gras.push(g);
                                    MapUtils.highlightFeaturesWithStyle(gras, styleHeight, highlightFeatureOpacit);
                                    _map.graphics.add(g);
                                }
                            })
                        })
                    }
                };
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

        return me;
    });
