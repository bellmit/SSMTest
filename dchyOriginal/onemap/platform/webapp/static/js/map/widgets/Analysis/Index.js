/**
 * δΈθ¬εζ
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/14 13:40
 * Version: v1.0 (c) Copyright gtmap Corp.2016
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
    "css!static/js/map/widgets/Analysis/Style.css"], function (declare, lang,arrayUtil, topic, RandomUuid, EsriSymbolsCreator, Handlebars, Color,
                                                                               Graphic,  Geometry, Polygon, Extent, GraphicsLayer,MapUtils,
                                                                               BaseWidget, JsonConverters, BaseAnalysis, GeoDataStore, slimScroll, bootstrap,
                                                                               baseTpl, listItem, resultTpl, GeometryIO) {
    var _map,commonAnalysis,_label,_analysisConfig,_id;
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
            _id=this.id;
            _label=this.getLabel();
            _analysisConfig=this.getConfig();
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
        onDestroy:function(){
            commonAnalysis.clear();
        }
    });

    var $optContainer,$selectContainer,$listContainer,$clearBtn;
    var geoDataStore = GeoDataStore.getInstance();
    var shareResult = [];

    /***
     * init
     * @private
     */
    function _init() {

        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>ζ₯ζΊ:&nbsp;" + context + "</div>");
        });
        commonAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_COMMON, _map);
        commonAnalysis.setAppConfig(appConfig);
        commonAnalysis.setDataSource(_analysisConfig.dataSource);
        commonAnalysis.setAnalysisLyr(_analysisConfig.layerName);
        commonAnalysis.setOutFields(_analysisConfig.returnFields);
        commonAnalysis.setTitleField(_analysisConfig.titleField);
        commonAnalysis.setFieldAlias(_getFieldAlias());
        commonAnalysis.setScopeLayers(_analysisConfig.scopeLayers);
        if (_analysisConfig.hasOwnProperty("link")) {
            var _link = _analysisConfig.link;
            if (_link.hasOwnProperty("url") && _link.url != "") {
                commonAnalysis.setFreeParams({iframeUrl: _link.url});
            }
        }
        if(_analysisConfig.responseType){
            commonAnalysis.setResponseType(_analysisConfig.responseType);
            //εζΆθ?’ιδΈδΈͺanaResultHandlerδΊδ»Άε€ηιθΏrestζ₯ε£εΎε°ηεζη»ζ
            topic.subscribe("anaResultHandler", function(data){
                Handlebars.registerHelper("fieldsTitles", function(data, options){
                    var titles = ["εΊε·"];
                    $.each(data[0], function(index, item){
                        if (item.name.toUpperCase() == "SHAPE"){
                            titles.push("εΎε½’");
                        }else if (item.alias){
                            titles.push(item.alias);
                        }else{
                            titles.push(item.name);
                        }});
                    var tpl = "{{#each this}}<th>{{this}}</th>{{/each}}";
                    var temp = Handlebars.compile(tpl);
                    return temp(titles);

                });

                Handlebars.registerHelper("addOne", function(index, options){
                    return parseInt(index) + 1;
                });

                Handlebars.registerHelper("enableActive", function(value, type, options){
                    if (value == 0){
                        if (type == 1){
                            return "in active";
                        }
                        return "active";
                    }else{
                        return "";
                    }
                });
                Handlebars.registerHelper("formatValue", function(data, options){
                    if (data.name.toUpperCase() =="SHAPE"){
                        var geometry = JsonConverters.toEsri(JSON.parse(data.value));
                        if (geometry.type == undefined)geometry = new Polygon(geometry);
                        var graphicId= "graphic-" + RandomUuid();
                        var attr = {id: graphicId};
                        var graphic = new Graphic(geometry, fillSymbol, attr);
                        graphicsLayer.add(graphic);
                        return '<a class="locationBtn" onclick="javascript:;" data-id="' + graphicId +'"><i title="ηΉε»ε?δ½" class="fa fa-map-marker fa-2x "></i></a>';
                    }else{
                        return data.value;
                    }
                });

                //θ?’ιεζη»ζζΈι€δΊδ»Ά
                topic.subscribe('clearAnalysisResult', function(_map){
                    var graphicsLayer = _map.getLayer("graphicsLayer_from_result");
                    if(graphicsLayer){
                        graphicsLayer.clear();
                        _map.removeLayer(graphicsLayer);
                    }
                    var $content = $("#result-container .content");
                    if($content){
                        $content.html('');
                    }
                });

                var result = JSON.parse(data.responseText);
                var info = result.info;
                var html = "";
                if(info.length > 0){
                    var lineSymbol=EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([255, 0, 0]), 2);
                    var fillSymbol =EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,lineSymbol, new Color([0, 0, 0, 0.25]));
                    var graphicsLayer = new GraphicsLayer({id: 'graphicsLayer_from_result'});
                    var template = Handlebars.compile(resultTpl);
                    html = template(result);

                }else{
                    html = "<h4>ε½εεζζ η»ζοΌ</h4>";
                }
                $("#result-container").css({"display": "block"});
                topic.publish('clearAnalysisResult', _map);
                $("#result-container .content").append(html);
                if (graphicsLayer) _map.addLayer(graphicsLayer);
                addResultHandlerListener(_map, result.excelData, result.excelList, graphicsLayer);
            });
        }
        if(_analysisConfig.hasOwnProperty("yx")){
            commonAnalysis.setYx(_analysisConfig.yx);
        }
        //ζ Ήζ?ιη½?ζ§εΆι‘΅ι’ζΎη€Ίεε?Ή
        var option = {
            listId: "commonList".concat(commonAnalysis.getId()),
            id: commonAnalysis.getId(),
            queryModeOn: lang.isArray(_analysisConfig.scopeLayers)
        };
        lang.mixin(option,_analysisConfig);
        $("#commonAnalysisTitle").append(_label);
        $("#commonAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + commonAnalysis.getId());
        $selectContainer = $('#select_' + commonAnalysis.getId());
        $listContainer = $("#commonList".concat(commonAnalysis.getId()));
        $clearBtn = $optContainer.find('a[data-opt="clear"]');

        //ηε¬ε±δΊ«ζ°ζ?εε
        geoDataStore.on("onChange", lang.hitch(this, onShareData));
        geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

        //ηε¬ε°εΎδΈηη»εΆθ¦η΄ ζΈι€δΊδ»Ά
        topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED,function(){
            clearHandle();
        });
    }

    /***
     * η»η»ε­ζ?΅ε«εδΈεη§°ζ ε°ε―Ήθ±‘
     * @private
     */
    function _getFieldAlias(){
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
    function _addListener(){
        $optContainer.on('click', 'a', function () {
            var o = $(this).data("opt");
            switch (o) {
                case 'draw':
                    commonAnalysis.draw('polygon', true).then(function (obj) {
                        //ε°ε―Ήθ±‘ζΈ²ζε°ι‘΅ι’εθ‘¨δΈ­
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

        //ζ₯θ―’δΊδ»Άηε¬
        $selectContainer.on('click', 'a', function () {
            var drawType = $(this).data("type");
            commonAnalysis.draw(drawType,false).then(function(r){
                commonAnalysis.queryGeo(r);
            });
        });
        //ε―Όε₯δΊδ»Άηε¬
        listenerFileInput();
    }

    /***
     * clear handle
     */
    function clearHandle() {
        commonAnalysis.clear().then(lang.hitch(this,function(){
            $clearBtn.hide();
            $listContainer.empty();
        }));
        _map.graphics.clear();
        var fileInput = document.getElementById('analysis-file-input-' + commonAnalysis.getId());
        //ζΈι€fileInputεε?Ή
        fileInput.outerHTML=fileInput.outerHTML;
        listenerFileInput();
    }

    /**
     *
     */
    function listenerFileInput(){
        $('#analysis-file-input-' + commonAnalysis.getId()).off('change');
        $('#analysis-file-input-' + commonAnalysis.getId()).on('change',function(){
            commonAnalysis.importFile( $('#analysis-file-input-' + commonAnalysis.getId()));
        });
        setTimeout(function(){
            listenerFileInput();
        },2000);
    }

    /***
     * ζ·»ε θ¦η΄ εθ‘¨
     * @param obj
     * @private
     */
    function _appendList(obj){
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
            var id=$(this).data("id");
            commonAnalysis.findGraById(id).then(function(g){
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
     * εζ’ε½εwidgetε η§»ι€ηε¬η­
     * @private
     */
    function _pause(){
        if(subscribe!=null && subscribe!=undefined)
            subscribe.remove();
        topic.publish('clearAnalysisResult', _map);
    }

    /***
     *
     * @private
     */
    function _resume(){
        subscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT,function(data){
            _appendList(data);
        });
    }

    /**
     * θ·εε±δΊ«ζ°ζ?
     * @param data
     */
    function onShareData(data) {
        if (data != undefined) {
            var type = data.type;
            switch (type) {
                case GeoDataStore.SK_LOC:
                {
                    var fs = data.features;
                    if (lang.isArray(fs)) {
                        arrayUtil.forEach(fs, function (item) {
                            var attr = item.attributes;
                            var tmp = {};
                            tmp.id = RandomUuid();
                            tmp.type = "wftb";
                            tmp.style = "teal";
                            tmp.title = "";
                            tmp.src = "ε?δ½";
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
     * restζ₯ε£θΏεη»ζε€ηηε¬δΊδ»Ά
     * @param _map
     * @param excelData
     * @param excelList
     */
    function addResultHandlerListener(_map, excelData, excelList, graphics){
        var ENUM_UTIL = {
            "EXPORT_EXCEL_GROUP": 0,
            "EXPORT_EXCEL_LIST": 1,
            EXPORT_EXCEL_URL: "../geometryService/export/excel"
        };

        //ζ·»ε slimScroll
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

        //ζΈι€εζη»ζζι?ηε¬
        $('#clearResultBtn').on('click', function(){
            topic.publish('clearAnalysisResult', _map);
        });

        //ε―ΌεΊExcelηε¬
        $("#expExcelGroupBtn").unbind();
        $("#expExcelListBtn").unbind();
        $("#expExcelGroupBtn").on('click', function(){
            if (excelData){
                exportExcel(0, excelData);
            }else{
                console.log("ε―ΌεΊεζη»ζε€±θ΄₯:δΈε­ε¨εη»Excelζ°ζ?");
            }
        });
        $("#expExcelListBtn").on('click', function(){
            if (excelList){
                exportExcel(1, excelList);
            }else{
                console.log("ε―ΌεΊεζη»ζε€±θ΄₯:δΈε­ε¨εθ‘¨Excelζ°ζ?");
            }
        });

        //ε―ΌεΊShpηε¬
        $("#exportShpBtn").unbind();
        $("#exportShpBtn").on('click',function(){
            var geometryIO = new GeometryIO();
            if(graphics){
                geometryIO.expToFile(graphics, 'shp');
            }else{
                console.log("ε―ΌεΊεζη»ζε€±θ΄₯:δΈε­ε¨shpζ°ζ?");
            }
        });

        function exportExcel(type, data){
            switch (type){
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
                alert("ζ ε―ΌεΊζ°ζ?!");
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

        //εΎε½’ε?δ½
        $(".locationBtn").on("click", function(){
            var style={
                lineStyle: "solid",
                lineColor: "#f0ad4e",
                width: "5px",
                fillStyle: "solid",
                fillColor: "#f1b2db"
            };
            var highlightFeatureOpacity = 0.5;
            var graphicId = $(this).data("id");
            var graphics = _map.getLayer("graphicsLayer_from_result").graphics;
            $.each(graphics, function(index, graphic){
                if (graphic.attributes.id == graphicId){
                    MapUtils.highlightFeaturesWithStyle([new Graphic(graphic.geometry)], style,highlightFeatureOpacity);
                    MapUtils.flashFeatures([graphic],"fast");
                    MapUtils.locateFeatures([graphic]);
                }
            });

        });
    }

    return me;
});