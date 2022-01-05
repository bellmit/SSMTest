define(["dojo/_base/declare",
     "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "map/core/BaseWidget",
    "esri/graphic",
    "esri/geometry/Polygon",
    "esri/Color",
    "map/utils/MapUtils",
    "map/core/BaseAnalysis",
    "map/core/EsriSymbolsCreator",
    "text!static/js/map/template/analysis/analysis-basic-tpl.html",
    "text!static/js/map/template/analysis/analysis-list-item.html",
    "map/core/GeometryIO",
    "map/core/JsonConverters",
    "handlebars"
], function (declare,lang,arrayUtil,topic,BaseWidget,Graphic, Polygon,Color,MapUtils,BaseAnalysis,EsriSymbolsCreator,baseTpl,listItem,GeometryIO,
             JsonConverters,Handlebars) {
    var _map,ghjcAnalysis,_ghjcConfig;
    var ghjcSubscribe;
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            _map = this.getMap().map();
            _ghjcConfig = this.getConfig();
            _init();
            _addListener();
            MapUtils.setMap(_map);
        },

        onPause: function () {
            _pause()
        },

        onOpen: function () {
            _resume();
        }
    });

    var $optContainer, $clearBtn, $selectContainer, $listContainer;
    var geometryIO;
    var fillColor=[0,0,0,0],lineColor=[255,0,0,1],locFillColor=[0,0,0,0];
    var duration=300000;

    function _init() {
        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined && context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });
        ghjcAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_GHJC, _map);
        ghjcAnalysis.setAppConfig(appConfig);
        ghjcAnalysis.setModuleConfig(_ghjcConfig);
        ghjcAnalysis.setDataSource(_ghjcConfig.dataSource);
        if (_ghjcConfig.hasOwnProperty("fillColor")) {
            fillColor=_ghjcConfig.fillColor;
        }
        if (_ghjcConfig.hasOwnProperty("locFillColor")) {
            locFillColor=_ghjcConfig.locFillColor;
        }
        if (_ghjcConfig.hasOwnProperty("lineColor")) {
            lineColor=_ghjcConfig.lineColor;
        }
        if (_ghjcConfig.hasOwnProperty("duration")) {
            duration=_ghjcConfig.duration;
        }

        //根据配置控制页面显示内容
        var option = {
            listId: "ghjcList",
            id: ghjcAnalysis.getId(),
            queryModeOn: lang.isArray(_ghjcConfig.scopeLayers) && _ghjcConfig.scopeLayers.length > 0
        };
        lang.mixin(option, _ghjcConfig);
        $("#ghjcAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + ghjcAnalysis.getId());
        $clearBtn = $optContainer.find('a[data-opt="clear"]');
        $selectContainer = $('#select_' + ghjcAnalysis.getId());
        $listContainer = $("#ghjcList");

        geometryIO = new GeometryIO();

        topic.subscribe(EventBus.MAIN_MAP_GRAPHICS_REMOVED, function () {
            clearHandle();
        });
    }
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }

    function _addListener() {
        $optContainer.on('click', 'a', function () {
            var opt = $(this).data("opt");
            switch (opt) {
                case 'draw':
                    if(_ghjcConfig.custom===true){
                        var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                            new Color(fillColor));
                        ghjcAnalysis.draw('polygon', true,reSymbol).then(function (obj) {
                            //将对象渲染到页面列表中
                            if (obj != undefined) {
                                _appendList(obj);
                                $clearBtn.show();
                            }
                        })}else {
                        ghjcAnalysis.draw('polygon', true).then(function (obj) {
                            //将对象渲染到页面列表中
                            if (obj != undefined) {
                                _appendList(obj);
                                $clearBtn.show();
                            }
                        })}
                    break;
                case 'imp':
                    $('#analysis-file-input').click();
                    break;
                case 'analysis':
                    ghjcAnalysis.setAnalysisGeometry(null);
                    try {
                        ghjcAnalysis.doAnalysis();
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'exp':
                    ghjcAnalysis.exportFeature(null);
                    break;
                case 'clear':
                    clearHandle();
                    break;
            }
        });
        //导入事件监听
        listenerFileInput();
    }

    function listenerFileInput() {
        $('#analysis-file-input-' + ghjcAnalysis.getId()).off('change');
        $('#analysis-file-input-' + ghjcAnalysis.getId()).on('change', function () {
            if(_ghjcConfig.custom===true){
                var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                    EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                    new Color(fillColor));
                ghjcAnalysis.importFile($('#analysis-file-input-' + ghjcAnalysis.getId()),reSymbol);
            }
            else {
                ghjcAnalysis.importFile($('#analysis-file-input-' + ghjcAnalysis.getId()));
            }
        });
        setTimeout(function () {
            listenerFileInput();
        }, 2000);
    }

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
        $("#ghjcList .a-r-btn").unbind();
        $("#ghjcList .a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            ghjcAnalysis.findGraById(id).then(function (g) {
                switch (t) {
                    case 'location':
                        if(_ghjcConfig.custom===true){
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
                        ghjcAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        try {
                            ghjcAnalysis.doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }
                        break;
                    case "export":
                        ghjcAnalysis.exportFeature(g);
                        break;

                }
            });
        });
    }
    function clearHandle() {
        ghjcAnalysis.clear().then(lang.hitch(this, function () {
            $clearBtn.hide();
            $listContainer.empty();
        }));
        _map.graphics.clear();
        var fileInput = document.getElementById('analysis-file-input-' + ghjcAnalysis.getId());
        //清除fileInput内容
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
    }

    function _pause() {
        if (ghjcSubscribe != null && ghjcSubscribe != undefined)
            ghjcSubscribe.remove();
        // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
        // if(lyr!=null){
        //     _map.removeLayer(lyr);
        // }
    }

    function _resume() {
        ghjcSubscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        });
        // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
        // if(lyr==null){
        //     _map.addLayer(multipleAnalysis.getGraphicsLyr());
        // }
    }
    return me;
});
