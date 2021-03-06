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
    "css!static/js/map/widgets/Analysis/Style.css"], function (declare, lang,arrayUtil, topic, RandomUuid, EsriSymbolsCreator, Handlebars, Color,
                                                                               Graphic,  Geometry, Polygon, Extent, GraphicsLayer,MapUtils,
                                                                               BaseWidget, JsonConverters, BaseAnalysis, GeoDataStore, slimScroll, bootstrap,
                                                                               baseTpl, listItem) {
    var _map,customAnalysis,_label,_analysisConfig,_id;
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
            customAnalysis.clear();
        }
    });

    var $optContainer,$selectContainer,$listContainer,$clearBtn;

    var geoDataStore = GeoDataStore.getInstance();
    /***
     * init
     * @private
     */
    function _init() {

        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>ζ₯ζΊ:&nbsp;" + context + "</div>");
        });
        customAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_CUSTOM_COMMON,_map);
        customAnalysis.setAppConfig(appConfig);
        customAnalysis.setDataSource(_analysisConfig.dataSource);
        customAnalysis.setAnalysisLyrs(_analysisConfig.analysislayers);
        customAnalysis.setAnalysisMethod(_analysisConfig.analysisMethod);
        customAnalysis.setAreaUnit(_analysisConfig.areaUnit);
        //ζ Ήζ?ιη½?ζ§εΆι‘΅ι’ζΎη€Ίεε?Ή
        var option = {
            listId: "customList".concat(customAnalysis.getId()),
            id: customAnalysis.getId(),
            queryModeOn: lang.isArray(_analysisConfig.scopeLayers)
        };
        lang.mixin(option,_analysisConfig);
        $("#dzAnalysisTitle").append(_label);
        $("#dzAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + customAnalysis.getId());
        $selectContainer = $('#select_' + customAnalysis.getId());
        $listContainer = $("#customList".concat(customAnalysis.getId()));
        $clearBtn = $optContainer.find('a[data-opt="clear"]');

		 //ηε¬ε±δΊ«ζ°ζ?εε
        geoDataStore.on("onChange", lang.hitch(this, onShareData));
        geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));
    }

	   /**
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
                            tmp.type = "custom";
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
                            customAnalysis.addGraphic(tmp);
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
    function _addListener(){
        $optContainer.on('click', 'a', function () {
            var o = $(this).data("opt");
            switch (o) {
                case 'draw':
                    customAnalysis.draw('polygon', true).then(function (obj) {
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
                    customAnalysis.setAnalysisGeometry();
                    try {
                        customAnalysis.doAnalysis();
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'exp':
                    customAnalysis.exportFeature(null);
                    break;
                case 'clear':
                    customAnalysis.clear().then(lang.hitch(this,function(){
                        $clearBtn.hide();
                        $listContainer.empty();
                    }));
                    _map.graphics.clear();
                    var fileInput = document.getElementById('analysis-file-input-' + customAnalysis.getId());
                    //ζΈι€fileInputεε?Ή
                    fileInput.outerHTML=fileInput.outerHTML;
                    listenerFileInput();
                    break;
            }
        });

        //ζ₯θ―’δΊδ»Άηε¬
        $selectContainer.on('click', 'a', function () {
            var drawType = $(this).data("type");
            customAnalysis.draw(drawType,false).then(function(r){
                customAnalysis.queryGeo(r);
            });
        });
        //ε―Όε₯δΊδ»Άηε¬
        listenerFileInput();
    }

    function listenerFileInput(){
        $('#analysis-file-input-' + customAnalysis.getId()).off('change');
        $('#analysis-file-input-' + customAnalysis.getId()).on('change',function(){
            customAnalysis.importFile( $('#analysis-file-input-' + customAnalysis.getId()));
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
            customAnalysis.findGraById(id).then(function(g){
                switch (t) {
                    case 'location':
                        MapUtils.highlightFeatures([new Graphic(g.geometry)], false);
                        MapUtils.locateFeatures([g]);
                        break;
                    case 'analysis':
                        var geometry = JsonConverters.toGeoJson(g);
                        customAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        try {
                            customAnalysis.doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }
                        break;
                    case "export":
                        customAnalysis.exportFeature(g);
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
        var lyr=_map.getLayer(customAnalysis.getGraphicsLyr().id);
        if(lyr!=null){
            _map.removeLayer(lyr);
        }
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
        var lyr=_map.getLayer(customAnalysis.getGraphicsLyr().id);
        if(lyr==null){
            _map.addLayer(customAnalysis.getGraphicsLyr());
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
    function addResultHandlerListener(_map, excelData, excelList){
        var ENUM_UTIL = {
            "EXPORT_EXCEL_GROUP": 0,
            "EXPORT_EXCEL_LIST": 1,
            EXPORT_EXCEL_URL: "../geometryService/export/excel"
        };

        //ζ·»ε slimScroll
        $("#customAnalysisResultContainer").slimScroll({
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

        //ε―ΌεΊηε¬
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
            var graphicId = $(this).data("id");
            var graphics = _map.getLayer("graphicsLayer_from_result").graphics;
            $.each(graphics, function(index, graphic){
                if (graphic.attributes.id == graphicId){
                    _map.setExtent(graphic.geometry.getExtent().expand(6));
                }
            });

        });


    }

    return me;
});