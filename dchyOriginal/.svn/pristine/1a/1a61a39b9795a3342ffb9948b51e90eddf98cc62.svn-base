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
    var _map,ecoAnalysis,_ecoConfig;
    var ecoSubscribe;
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            _map = this.getMap().map();
            _ecoConfig = this.getConfig();
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
        ecoAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_ECO, _map);
        ecoAnalysis.setAppConfig(appConfig);
        ecoAnalysis.setDataSource(_ecoConfig.dataSource);
        ecoAnalysis.setAnalysisLyr(_ecoConfig.layerName);
        if (_ecoConfig.hasOwnProperty("fillColor")) {
            fillColor=_ecoConfig.fillColor;
        }
        if (_ecoConfig.hasOwnProperty("locFillColor")) {
            locFillColor=_ecoConfig.locFillColor;
        }
        if (_ecoConfig.hasOwnProperty("lineColor")) {
            lineColor=_ecoConfig.lineColor;
        }
        if (_ecoConfig.hasOwnProperty("duration")) {
            duration=_ecoConfig.duration;
        }

        //根据配置控制页面显示内容
        var option = {
            listId: "ecoList",
            id: ecoAnalysis.getId(),
            queryModeOn: lang.isArray(_ecoConfig.scopeLayers) && _ecoConfig.scopeLayers.length > 0
        };
        lang.mixin(option, _ecoConfig);
        $("#ecoAnalysisPanel").append(renderTpl(baseTpl, option));
        $optContainer = $('#' + ecoAnalysis.getId());
        $clearBtn = $optContainer.find('a[data-opt="clear"]');
        $selectContainer = $('#select_' + ecoAnalysis.getId());
        $listContainer = $("#ecoList");

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
                    if(_ecoConfig.custom===true){
                        var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                            new Color(fillColor));
                        ecoAnalysis.draw('polygon', true,reSymbol).then(function (obj) {
                            //将对象渲染到页面列表中
                            if (obj != undefined) {
                                _appendList(obj);
                                $clearBtn.show();
                            }
                        })}else {
                        ecoAnalysis.draw('polygon', true).then(function (obj) {
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
                    ecoAnalysis.setAnalysisGeometry(null);
                    try {
                        ecoAnalysis.doAnalysis();
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'exp':
                    ecoAnalysis.exportFeature(null);
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
        $('#analysis-file-input-' + ecoAnalysis.getId()).off('change');
        $('#analysis-file-input-' + ecoAnalysis.getId()).on('change', function () {
            if(_ecoConfig.custom===true){
                var reSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                    EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(lineColor), 2),
                    new Color(fillColor));
                ecoAnalysis.importFile($('#analysis-file-input-' + ecoAnalysis.getId()),reSymbol);
            }
            else {
                ecoAnalysis.importFile($('#analysis-file-input-' + ecoAnalysis.getId()));
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
        $("#ecoList .a-r-btn").unbind();
        $("#ecoList .a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            ecoAnalysis.findGraById(id).then(function (g) {
                switch (t) {
                    case 'location':
                        if(_ecoConfig.custom===true){
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
                        ecoAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        try {
                            ecoAnalysis.doAnalysis();
                        } catch (e) {
                            alert(e.message);
                        }
                        break;
                    case "export":
                        ecoAnalysis.exportFeature(g);
                        break;

                }
            });
        });
    }
    function clearHandle() {
        ecoAnalysis.clear().then(lang.hitch(this, function () {
            $clearBtn.hide();
            $listContainer.empty();
        }));
        _map.graphics.clear();
        var fileInput = document.getElementById('analysis-file-input-' + ecoAnalysis.getId());
        //清除fileInput内容
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
    }

    function _pause() {
        if (ecoSubscribe != null && ecoSubscribe != undefined)
            ecoSubscribe.remove();
        // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
        // if(lyr!=null){
        //     _map.removeLayer(lyr);
        // }
    }

    function _resume() {
        ecoSubscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        });
        // var lyr=_map.getLayer(multipleAnalysis.getGraphicsLyr().id);
        // if(lyr==null){
        //     _map.addLayer(multipleAnalysis.getGraphicsLyr());
        // }
    }
    return me;
});
