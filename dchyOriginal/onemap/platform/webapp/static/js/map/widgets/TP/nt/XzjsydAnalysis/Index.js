/**
 *
 * 南通订制 新增建设用地分析功能
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "map/core/BaseWidget",
    "layer",
    "map/core/GeoDataStore",
    "text!static/js/map/template/analysis/analysis-list-item.html",
    "dojo/_base/array",
    "dojox/uuid/generateRandomUuid",
    "map/utils/MapUtils",
    "esri/graphic",
    "esri/Color",
    "text!static/js/map/template/analysis/analysis-basic-tpl.html",
    "map/core/BaseAnalysis",
    "handlebars",
    "map/core/EsriSymbolsCreator",
    "map/core/JsonConverters",
    "esri/lang",
    "dojo/json",
    "static/thirdparty/laydate/laydate"
], function (declare, lang, BaseWidget, layer, GeoDataStore, listItem, arrayUtil, RandomUuid, MapUtils, Graphic, Color, BasicTpl, BaseAnalysis, Handlebars, EsriSymbolsCreator, JsonConverters, esriLang, dojoJSON) {
    var geoDataStore = GeoDataStore.getInstance();
    var $xzjsydList, $xzjsydAnalysisPanel, $xzJsydOptContainer;
    var _xzjsydConfig; //配置信息
    var xzJsydAnalysis, $xzJsydAnalysis, _map;
    var geometryData = [];
    var me = declare([BaseWidget], {

        onCreate: function () {
            _xzjsydConfig = this.getConfig();
            _map = this.getMap().map();
            _init();
            _addListener();

        },

        onPause: function () {
            $('.analysisBtnPanel').show();
        },

        onOpen: function () {
            $('.analysisBtnPanel').hide();
        },
        /**
         *
         */
        onDestroy: function () {
            xzJsydAnalysis.clear();
        }
    });

    function _init() {

        layer.config();
        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined && context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });

        //初始化变量
        $xzjsydAnalysisPanel = $("#xzjsydAnalysisPanel");
        $xzJsydAnalysis = $("#xzJsydAnalysisBtn");
        xzJsydAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_XZJSYD_NT, _map);
        xzJsydAnalysis.setDataSource(_xzjsydConfig.dataSource);
        xzJsydAnalysis.setLinkUrl(_xzjsydConfig.linkUrl);
        xzJsydAnalysis.setAnalysisLyr(_xzjsydConfig.gzqLayerName);

        //根据配置控制页面显示内容
        var option = {
            listId: "xzjsydList",
            id: xzJsydAnalysis.getId(),
            queryModeOn: lang.isArray(_xzjsydConfig.scopeLayers) ? true : false
        };
        lang.mixin(option, _xzjsydConfig);
        $xzjsydAnalysisPanel.append(renderTpl(BasicTpl, option));
        $('.analysisBtnPanel').hide();

        $xzjsydList = $("#xzjsydList");
        $xzJsydOptContainer = $('#' + xzJsydAnalysis.getId());
        geoDataStore.on("onChange", lang.hitch(this, onShareData));
        geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

    }

    function _addListener() {
        $xzJsydAnalysis.on('click', function () {
            try {
                xzJsydAnalysis.doAnalysis();
            } catch (e) {
                alert(e.message);
            }

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
                            tmp.type = "xzJsyd";
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
                            xzJsydAnalysis.addGraphic(tmp);
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
        $xzjsydList.append(renderTpl(listItem, obj));
        var scrollHeight = $(window).height() - 300;
        $xzjsydList.slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        $("#xzjsydList .a-r-btn").unbind();
        $("#xzjsydList .a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            xzJsydAnalysis.findGraById(id).then(function (g) {
                switch (t) {
                    case 'location':
                        var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.solid, "#eaee27", 2),
                            new Color([0, 0, 0, 0]));
                        var gras = new Graphic(g.geometry);
                        gras.setSymbol(fillSymbol);
                        MapUtils.highlightFeatures([gras], false);
                        MapUtils.locateFeatures([g]);
                        break;
                    case 'analysis':
                        var geometry = JsonConverters.toGeoJson(g);
                        xzJsydAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                        xzJsydAnalysis.doAnalysis();
                        break;
                }
            });

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
});