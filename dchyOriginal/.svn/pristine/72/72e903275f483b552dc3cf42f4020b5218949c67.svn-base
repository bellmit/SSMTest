/**
 * 监测图斑分析
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/6/30 12:23
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "dojox/uuid/generateRandomUuid",
    "map/core/BaseAnalysis",
    "map/utils/MapUtils",
    "map/core/GeoDataStore",
    "esri/lang",
    "layer",
    "handlebars",
    "esri/graphic",
    "text!static/js/map/template/analysis/analysis-basic-tpl.html",
    "text!static/js/map/template/analysis/analysis-list-item.html",
    "esri/tasks/FeatureSet",
    "map/core/GeometryIO"], function (declare, lang, arrayUtil, topic, BaseWidget, JsonConverters, RandomUuid,
                                                                               BaseAnalysis, MapUtils, GeoDataStore, EsriLang, layer, Handlebars, Graphic, BaseTpl, ListItemTpl, FeatureSet, GeometryIO) {
    var _map, jctbAnalysis, _label, _analysisConfig;
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
            _label = this.getLabel();
            _analysisConfig = this.getConfig();
            _init();
            _addListener();
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
            jctbAnalysis.clear();
        }
    });

    //监测图斑分析自定义选项
    var analysisOption = {};
    var jctbAnalysisPanel, $optContainer, $clearBtn, $listContainer;

    // 查询条件
    var queryConditions = [], outFieldsStr = "", titleField = "", queryResult = [];
    var geoDataStore = GeoDataStore.getInstance();
    var shareResult = [];
    var subscribe;
    var exportSelTpl;
    var geometryIO = new GeometryIO();

    /***
     * init
     * @private
     */
    function _init() {

        //监听共享数据变化
        geoDataStore.on("onChange", lang.hitch(this, onShareData));
        geoDataStore.fetch(GeoDataStore.SK_LOC).then(lang.hitch(this, onShareData));

        jctbAnalysisPanel = $("#jctbAnalysisPanel");
        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });
        //根据methodType判断是一般的监测图斑分析还是地方版本（南通，马鞍山）定制版本
        if (_analysisConfig.hasOwnProperty("methodType")) {
            jctbAnalysis = new BaseAnalysis(_analysisConfig.methodType, _map);
            jctbAnalysis.setMethodType(_analysisConfig.methodType);
        } else {
            jctbAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_JCTB, _map);
        }
        if (!EsriLang.isDefined(_analysisConfig.analysis)) {
            layer.msg("分析图层尚未配置!", {icon: 0});
            return;
        }
        var analysisPart = _analysisConfig.analysis;
        var queryPart = _analysisConfig.query;
        jctbAnalysis.setDataSource(analysisPart.dataSource);
        jctbAnalysis.setAnalysisLyrs(analysisPart.layers);
        jctbAnalysis.setTdlyxzArray(analysisPart.tdlyxz);
        if (_analysisConfig.hasOwnProperty("methodType")) {
            analysisOption.methodType = _analysisConfig.methodType;
        }

        if (lang.isArray(queryPart.layers)) {
            jctbAnalysisPanel.append(renderTpl($("#jctbQueryTpl").html(), queryPart));
        }
        if (lang.isArray(analysisPart.tdlyxz)) {
            jctbAnalysisPanel.append(renderTpl($("#tdlyxzTpl").html(), {years: analysisPart.tdlyxz}));
        }
        if (lang.isArray(queryPart.queryFields)) {
            jctbAnalysisPanel.append(renderTpl($("#jctbFieldsQueryTpl").html(), {fields: queryPart.queryFields}));
            arrayUtil.forEach(queryPart.queryFields, function (item) {
                var condition = {
                    name: item.name,
                    title: item.alias,
                    value: item.defaultValue || '',
                    operator: item.operator || 'like',
                    type: item.type || 'string'
                };
                queryConditions.push(condition);
            });

            $.each(queryPart.outFields, function (i1, n1) {
                if (i1 == queryPart.outFields.length - 1)
                    outFieldsStr = outFieldsStr.concat(n1.name);
                else
                    outFieldsStr = outFieldsStr.concat(n1.name).concat(",");
            });
            titleField = queryPart.titleField;
        }

        lang.mixin(analysisOption, {unit: analysisPart.unit});
        updateAnalysisOption();

        //根据配置控制页面显示内容
        var option = {listId: "jctbAnalysisList", id: jctbAnalysis.getId()};
        lang.mixin(option, _analysisConfig);
        $("#jctbAnalysisTitle").html(_label);//设置分析功能标题
        jctbAnalysisPanel.append(renderTpl(BaseTpl, option));
        $optContainer = $('#' + jctbAnalysis.getId());
        $clearBtn = $optContainer.find('a[data-opt="clear"]');
        $listContainer = $("#jctbAnalysisList");
        //selectContainer = $('#select_'+jctbAnalysis.getId());
        if (lang.isArray(queryPart.queryFields)) {
            var queryHtml = "<a id=\"qSearchBtn\" class=\"btn btn-primary btn-sm\" data-loading-text=\"查询中 ...\"><i class=\"iconfont\">&#xe602;</i>查询</a>";
            $("#" + jctbAnalysis.getId()).append(queryHtml);
        }
        exportSelTpl = $("#export-select-tpl").html();
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
     * 获取共享数据
     * @param data
     */
    function onShareData(data) {
        if (data != undefined) {
            var fs = data.features;
            if (lang.isArray(fs)) {
                arrayUtil.forEach(fs, function (item) {
                    var attr = item.attributes;
                    var tmp = {};
                    tmp.id = RandomUuid();
                    tmp.type = "jctb";
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
                    jctbAnalysis.draw('polygon', true).then(function (obj) {
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
                    jctbAnalysis.setAnalysisGeometry(null);
                    try {
                        updateAnalysisOption();

                        if (queryResult.length > 0) {
                            var features = [];
                            for (var i = 0; i < queryResult.length; i++) {
                                features.push(queryResult[i].graphic);
                            }
                            if (features.length > 0)
                                jctbAnalysis.setAnalysisGeometry(JSON.stringify(JsonConverters.toGeoJson({features: features})));
                        }

                        jctbAnalysis.doAnalysis(analysisOption);
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'clear':
                    jctbAnalysis.clear().then(lang.hitch(this, function () {
                        _map.graphics.clear();
                        queryResult = [];
                        $clearBtn.hide();
                        $listContainer.empty();
                    }));
                    break;
                default :
                    break;
            }
        });
        $("#qSearchBtn").on("click", function () {
            var where = "1=1";
            $.each($("#layerForm .querySelector"), function (index, layerFormItem) {
                var prefix = "%";
                var operator = ' LIKE ';
                if (layerFormItem.nodeName.toLowerCase() == 'input' || layerFormItem.nodeName.toLowerCase() == 'select') {
                    $.each(queryConditions, function (index, item) {
                        if (layerFormItem.name == item.name) {
                            operator = item.operator;
                            return false;
                        }
                    });
                    if (layerFormItem.value != undefined && layerFormItem.value != '') {
                        where += "AND";
                        if (operator == "like" || operator == "LIKE") {
                            where += " " + layerFormItem.name + " " + operator + " '" + prefix + layerFormItem.value + prefix + "'";
                        } else {
                            where += " " + layerFormItem.name + " " + operator + " '" + layerFormItem.value + "'";
                        }
                    }
                }
            });

            if (where.length == 0) {
                where = null;
                $("#qSearchBtn").html('<i class="iconfont">&#xe602;</i>查询');
                return false;
            }
            var year = $("#jctbYearSelector option:selected").val();
            var url = "";
            $.each(_analysisConfig.query.layers, function (index, item) {
                if (item.year == year) {
                    // url = item.url+"/0/query?";
                    url = item.url;
                }
            });
            if (where === " AND JCBH = '1'") {
                where = "JCBH = '1'";
            }
            var data = {
                layerUrl: url,
                where: where,
                returnFields: outFieldsStr
            };

            $.ajax({
                url: "/omp/map/query",
                data: data,
                success: function (r) { 
                    var result = JSON.parse(r);
                    $.each(result.features, function (i, feature) {
                        var g = result.features[i];
                        var graphic = new Graphic(g);
                        graphic.geometry.spatialReference = _map.spatialReference;
                        jctbAnalysis.graphicsLyr.add(graphic);
                        var o = {};
                        o.id = RandomUuid();
                        if (g.attributes.hasOwnProperty(titleField)) {
                            o.src = "查询";
                            o.title = g.attributes[titleField];
                        } else {
                            o.src = "查询";
                            o.title = "监测图斑" + (i + 1);
                        }
                        o.graphic = graphic;
                        o.style = 'teal';
                        o.type = this.type;
                        queryResult.push(o);
                        _appendList(o);
                    })
                }
            });
            where = null;
            return false;

        });

        //导入事件监听
        listenerFileInput();
    }

    /**
     *
     */
    function listenerFileInput() {
        $('#analysis-file-input-' + jctbAnalysis.getId()).off('change');
        $('#analysis-file-input-' + jctbAnalysis.getId()).on('change', function () {
            jctbAnalysis.importFile($('#analysis-file-input-' + jctbAnalysis.getId()));
        });
        setTimeout(function () {
            listenerFileInput();
        }, 2000);
    }

    /**
     * 更新分析参数
     */
    function updateAnalysisOption() {
        //更新year参数的值
        lang.mixin(analysisOption, {year: $("#jctbYearSelector option:selected").val()});
        //根据选择的现状年度 更新analysisLayers里的现状图层
        var xzSelected = $("#tdlyxzSelector option:selected");
        var xzdwLyr = xzSelected.data("xzdw");
        var dltbLyr = xzSelected.data("dltb");

        var lyrs = jctbAnalysis.getAnalysisLyrs();
        arrayUtil.forEach(lyrs, function (item) {
            if (item.fid === "xzdw") {
                item.layerName = xzdwLyr;
            } else if (item.fid === "dltb") {
                item.layerName = dltbLyr;
            }
        });
        jctbAnalysis.setAnalysisLyrs(lyrs);
    }

    /**
     * 添加要素
     * @param obj
     * @private
     */
    function _appendList(obj) {
        $listContainer.append(renderTpl(ListItemTpl, obj));
        if(_analysisConfig.exportType!==undefined){
            var btn=$("#multipleList").find("#exportBtn");
            btn.show()
        }
        var scrollHeight = $(window).height() - 400;
        $listContainer.slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        $clearBtn.show();
        $("#jctbAnalysisList .a-r-btn").unbind();
        $("#jctbAnalysisList .a-r-btn").on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            var isContain = false;
            var gra;
            jctbAnalysis.findGraById(id).then(function (g) {
                isContain = true;
                gra = g;
            });
            if (!isContain) {
                $.each(queryResult, function (idx, item) {
                    if (item.id === id)
                        gra = item.graphic;
                });
            }
            switch (t) {
                case 'location':
                    _map.setExtent(gra.geometry.getExtent().expand(2.5));
                    MapUtils.highlightFeatures([gra], false);
                    break;
                case 'analysis':
                    var geometry = JsonConverters.toGeoJson(gra);
                    jctbAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                    try {
                        updateAnalysisOption();
                        jctbAnalysis.doAnalysis(analysisOption);
                    } catch (e) {
                        alert(e.message);
                    }
                    break;
                case 'export':
                    var attr = [];
                    attr.push(gra);
                    _export(attr);
                    break;
            }
        });
    }
    function _export(attr) {
        var featureSet = new FeatureSet();
        featureSet.features = attr;
        var template = Handlebars.compile(exportSelTpl);
        var content = template({types: parseTypesArray});

        layer.open({
            title: '选择导出格式',
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
        var expType = _analysisConfig.exportType;
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

    function _pause() {
        if (subscribe != null && subscribe != undefined)
            subscribe.remove();
        topic.publish('clearAnalysisResult', _map);
    }

    function _resume() {
        subscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        })
    }

    return me;
});
