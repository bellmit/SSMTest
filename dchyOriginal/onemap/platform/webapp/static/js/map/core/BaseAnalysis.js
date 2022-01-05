/**
 * 分析功能的核心基类
 * 所有分析功能都需要继承此类
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/23
 * Version: v1.0
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Color",
    "dojo/_base/Deferred",
    "dojo/topic",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/SpatialReference",
    "esri/toolbars/draw",
    "esri/tasks/FeatureSet",
    "esri/geometry/Geometry",
    "esri/geometry/Polygon",
    "esri/geometry/Point",
    "esri/geometry/Extent",
    "esri/lang",
    "map/core/QueryTask",
    "map/core/GeometryIO",
    "map/core/JsonConverters",
    "map/core/EsriSymbolsCreator",
    "./GeometryServiceTask",
    "layer",
    "handlebars",
    "css!static/js/map/template/analysis/analysis.css"], function (declare, lang, arrayUtil, Color, Deferred, topic, RandomUuid, GraphicsLayer, Graphic,
                                                                   SpatialReference, Draw, FeatureSet, Geometry, Polygon, Point, Extent, esriLang, QueryTask,
                                                                   GeometryIO, JsonConverters, EsriSymbolsCreator, GeometryServiceTask, layer, Handlebars) {
    var drawTool, drawHandler;
    var graphicsList = [];
    var geometryIO;
    var report = undefined;//电子报件信息
    var reportNt = undefined;//电子报件信息(南通导入报件分析)
    var m = declare("BaseAnalysis", null, {

        /**
         *
         */
        id: '',
        /***
         *
         */
        graphicsLyr: null,
        /***
         *
         */
        jsonParams: null,
        /**
         *
         */
        level: null,
        /**
         *
         */
        link: null,
        /**
         *功能配置config
         */
        moduleConfig: null,
        /**
         * 自定义参数，该部分参数会被传递到后端(由各自方法进行处理)
         */
        freeParams: null,
        /**
         * txt文件中xy坐标的顺序
         * x在前y在后为false(即前7后8)，否则为true
         */
        xy: false,
        /**
         * 分析类型
         * default 一般类型
         */
        type: this.TYPE_COMMON,
        /**
         * 分析图层
         */
        analysisLyr: null,
        /***
         * 分析图层组（监测图斑分析）
         */
        analysisLyrs: [],
        /***
         * 土地利用现状图层组（监测图斑分析）
         */
        tdlyxzArray: [],
        /***
         * 分析的geojson图形
         */
        analysisGeometry: null,
        /**
         * 分析数据源名称
         */
        dataSource: null,
        /**
         * 分析返回字段数组
         */
        outFields: null,
        /***
         *
         */
        titleField: null,
        /***
         *
         */
        scopeLayers: [],
        /**
         * 地类图斑图层名
         */
        dltbLyr: null,
        /**
         * 现状地物图层名
         */
        xzdwLyr: null,
        /**
         * 零星地物图层名
         */
        lxdwLyr: null,
        /**
         * 多年份数据
         */
        yearList: [],
        /**
         * 方法类型，南通版本tdlyxznt
         */
        methodType: null,
        /***
         *
         */
        map: undefined,
        /**
         * 关联的地图模板 default tpl
         */
        mapTpl: tpl,
        /**
         * 是否限制分析范围大小
         */
        areaLimit: !0,
        /**
         * 限制的最大可分析范围
         * 单位公顷
         */
        areaMax: 500,
        /**
         * 面积结果显示单位 平方米/公顷/亩
         */
        areaUnit: this.UNIT_SQUARE_METERS,

        /***
         * 是否允许导出分析结果图形
         * 默认 false
         */
        enableExportResult: false,
        /**
         *点样式
         */
        markerSymbol: null,
        /**
         * 线样式
         */
        lineSymbol: null,
        /**
         * 面样式
         */
        fillSymbol: null,

        /**
         *name/alias
         */
        fieldAlias: {},
        /**
         * linkurl for xzAnalysis
         */
        linkUrl: null,
        /**
         * 模板配置
         */
        appConfig: undefined,
        /**
         * 保留小数位
         */
        decimal: null,

        /**
         * 结果返回类型
         *支持类型 html: RESPONSE_HTML; json: RESPONSE_JSON: json
         */
        responseType: "html",

        /**
         * 定制分析的分析方法 由ANALYSIS_CUSTOM_BASE_URL 追加
         */
        analysisMethod: null,

        /**
         * 地方定制分析请求url， 用于土地利用现状分析等
         */
        requestUrl: null,

        /**
         * 分析结果页面的目标 name 默认是打开新页面
         * 可传 iframe 名称
         */
        targetName: '_blank',
        /**
         * 地方定制要求导入查询数据
         *
         */
        locQuery: null,

        /**
         * 哈尔滨版本 是否显示土地利用现状分析备注
         */
        tdlyxzfxBz: null,

        /**
         * 导入报件面积单位
         */
        bjZipImportUnit: null,

        /**
         * 地方版本
         */
        localVersion: null,

        /**
         * 获取面积信息接口
         */
        reportInfoUrl: null,

        /**
         * 土地利用现状分析展示字典表行政区名称
         */
        showXzqmc: false,

        showDetail: false,

        /**
         * 土地利用现状分析分组，zldwdm、qsdwdm
         */
        groupBy: null,

        constructor: function (_type, _map, _option) {
            graphicsList = [];
            layer.config();
            this.type = _type;
            this.map = _map;
            lang.mixin(this, _option);
            this.id = RandomUuid();

            this.graphicsLyr = new GraphicsLayer({id: 'analysis_' + this.id});

            this.map.addLayer(this.graphicsLyr);

            this.lineSymbol = EsriSymbolsCreator.defaultLineSymbol;

            this.markerSymbol = EsriSymbolsCreator.createSimpleMarkerSymbol(EsriSymbolsCreator.markerStyle.STYLE_SOLID, 10,
                this.lineSymbol, new Color([0, 0, 0, 0.25]));

            this.fillSymbol = EsriSymbolsCreator.defaultFillSymbol;

            geometryIO = new GeometryIO();
        },
        setJsonParams: function (c) {
            this.jsonParams = c;
        },
        setLink: function (c) {
            this.link = c;
        },
        setLevel: function (c) {
            this.level = c;
        },
        setModuleConfig: function (c) {
            this.moduleConfig = c;
        },
        setYx: function (c) {
            this.xy = c;
        },
        setType: function (c) {
            this.type = c;
        },
        setAnalysisLyr: function (c) {
            this.analysisLyr = c;
        },
        setAnalysisLyrs: function (lyrs) {
            this.analysisLyrs = lyrs;
        },
        getAnalysisLyrs: function () {
            return this.analysisLyrs;
        },
        setTdlyxzArray: function (val) {
            this.tdlyxzArray = val;
        },
        setDataSource: function (c) {
            this.dataSource = c;
        },
        setMarkerSymbol: function (c) {
            this.markerSymbol = c;
        },
        setLineSymbol: function (c) {
            this.lineSymbol = c;
        },
        setFillSymbol: function (c) {
            this.fillSymbol = c;
        },
        setOutFields: function (val) {
            if (lang.isArray(val)) {
                var arr = arrayUtil.map(val, function (item) {
                    return item.name;
                });
                this.outFields = arr.join(",");
            } else if (lang.isString(val))
                this.outFields = val;
        },
        setTitleField: function (val) {
            this.titleField = val;
        },
        setMapTpl: function (c) {
            this.mapTpl = c;
        },
        setMap: function (c) {
            this.map = c;
        },
        setAreaMax: function (c) {
            this.areaMax = c;
        },
        setDecimal: function (c) {
            this.decimal = c;
        },
        setAreaUnit: function (c) {
            this.areaUnit = c;
        },
        setExportResult: function (c) {
            this.enableExportResult = c;
        },
        setDltbLyr: function (c) {
            this.dltbLyr = c;
        },
        setLocQuery: function (c) {
            this.locQuery = c;
        },
        setXzdwLyr: function (c) {
            this.xzdwLyr = c;
        },
        setLxdwLyr: function (c) {
            this.lxdwLyr = c;
        },
        setMethodType: function (c) {
            this.methodType = c;
        },
        setYearList: function (val) {
            this.yearList = val;
        },
        setScopeLayers: function (c) {
            this.scopeLayers = c;
        },
        setAnalysisGeometry: function (c) {
            this.analysisGeometry = c;
        },
        getGraphicsLyr: function () {
            return this.graphicsLyr;
        },
        setFieldAlias: function (val) {
            this.fieldAlias = val;
        },
        getId: function () {
            return this.id;
        },
        setFreeParams: function (val) {
            this.freeParams = val;
        },
        addGraphic: function (obj) {
            if (graphicsList != null && graphicsList.length > 0) {
                var isContain = false;
                arrayUtil.forEach(graphicsList, function (item) {
                    if (item.graphic == obj.graphic) {
                        isContain = true;
                    }
                });
                if (!isContain) {
                    graphicsList.push(obj);
                }
            } else {
                graphicsList.push(obj);
            }
        },
        setLinkUrl: function (val) {
            this.linkUrl = val;
        },
        setAppConfig: function (val) {
            this.appConfig = val;
        },
        setReport: function (val) {
            report = val;
        },
        setResponseType: function (val) {
            this.responseType = val;
        },
        setAnalysisMethod: function (val) {
            this.analysisMethod = val;
        },
        setRequestUrl: function (val) {
            this.requestUrl = val;
        },
        getRequestUrl: function (val) {
            return this.requestUrl;
        },
        setTargetName: function (val) {
            this.targetName = val;
        },
        setTdlyxzfxBz: function (val) {
            this.tdlyxzfxBz = val;
        },
        setBjZipImportUnit: function (val) {
            this.bjZipImportUnit = val;
        },
        setLocalVersion: function (val) {
            this.localVersion = val;
        },
        setReportInfoUrl: function (val) {
            this.reportInfoUrl = val;
        },
        setShowXzqmc: function (val) {
            this.showXzqmc = val
        },
        setShowDetail: function (val) {
            this.showDetail = val
        },
        setGroupBy: function (val) {
            this.groupBy = val
        },
        /***
         * 地图上绘制图形
         * @param a  draw 类型 eg.Draw.POLYGON
         * @param c  是否添加绘制的图形 true---手绘功能 false---则进行后续的查询
         * @private
         */
        draw: function (a, c, x) {
            var d = new Deferred();
            var f;
            if (x !== null && x !== undefined) {
                f = x;
            } else {
                f = this.fillSymbol
            }
            var n = c;
            if (drawTool != undefined) drawTool.deactivate();
            if (drawHandler != undefined) drawHandler.remove();
            drawTool = drawTool ? drawTool : new Draw(this.map);
            drawHandler = drawTool.on("draw-end", lang.hitch(this, function (evt) {
                drawTool.deactivate();
                if (n) {
                    if (evt.geometry != null) {
                        var gra = new Graphic(evt.geometry, f, {});
                        this.graphicsLyr.add(gra);
                        var o = {};
                        o.id = RandomUuid();
                        o.src = '手绘';
                        o.graphic = gra;
                        o.title = m.GEO_DRAW_PREFIX + (graphicsList.length + 1).toString();
                        o.style = 'teal';
                        o.type = this.type;  //记录当前分析的类型（type），便于清除操作
                        graphicsList.push(o);
                        d.callback(o);
                    }
                } else {
                    // this.graphicsLyr.clear();  不对原有手绘等图形进行清除操作
                    d.callback(evt.geometry);
                }
            }));
            drawTool.activate(a);
            return d;
        },
        /***
         * 导入图形
         * @param file input(type=file)
         * @private
         */
        importFile: function (file, x) {
            var f;
            if (x !== null && x !== undefined) {
                f = x;
            } else {
                f = this.fillSymbol
            }

            report = undefined;
            reportNt = undefined;
            geometryIO.impFromFile(file, this.map.spatialReference, this.xy).then(lang.hitch(this, function (data) {
                var featureSet = data.fs;
                var info = data.info;

                report = data.report;
                reportNt = data.report;
                topic.publish("report/info", report);
                topic.publish("bjTitleInfo", info);

                //定义用于图形定位的extent
                var graExtent = this.map.extent;
                if (featureSet != undefined && featureSet.hasOwnProperty("features")) {
                    for (var i = 0; i < featureSet.features.length; i++) {
                        var feature = featureSet.features[i];
                        var polygonTmp = new Polygon(feature.geometry);
                        var gra = new Graphic(polygonTmp, f, feature.attributes);
                        gra.geometry.spatialReference = this.map.extent.spatialReference;
                        this.graphicsLyr.add(gra);
                        var obj = {};
                        obj.id = RandomUuid();
                        obj.src = '导入';
                        obj.graphic = gra;
                        //若属性中带有name/title则取此属性值
                        var attr = gra.attributes;
                        if (attr != undefined && (attr.hasOwnProperty("TITLE") || attr.hasOwnProperty("NAME") || attr.hasOwnProperty("XMMC") || attr.hasOwnProperty("xmmc"))) {
                            obj.title = attr["NAME"] || attr["TITLE"] || attr["XMMC"] || attr["xmmc"];
                        } else if (data.fileName)
                            obj.title = data.fileName + "-" + (graphicsList.length + 1).toString();
                        else
                            obj.title = m.GEO_IMP_PREFIX + (graphicsList.length + 1).toString();
                        obj.style = 'teal';
                        obj.type = this.type; //记录当前分析的类型（type），便于清除操作
                        graphicsList.push(obj);
                        if (i === 0) {
                            graExtent = gra.geometry.getExtent() == null ? this.map.extent : gra.geometry.getExtent();
                        } else {
                            var extent = gra.geometry.getExtent();
                            //重新获取合并后的extent范围，extent自带的union合并不成功
                            graExtent = new Extent(graExtent.xmin < extent.xmin ? graExtent.xmin : extent.xmin, graExtent.ymin < extent.ymin ? graExtent.ymin : extent.ymin, graExtent.xmax > extent.xmax ? graExtent.xmax : extent.xmax, graExtent.ymax > extent.ymax ? graExtent.ymax : extent.ymax, extent.spatialReference);
                        }
                        topic.publish(m.EVENT_QUERY_RESULT, obj);
                    }
                } else if (featureSet != undefined && featureSet.hasOwnProperty("geometry")) { //单个图形
                    var feature = featureSet;
                    var polygonTmp = new Polygon(feature.geometry);
                    var gra = new Graphic(polygonTmp, f);
                    gra.geometry.spatialReference = this.map.extent.spatialReference;
                    this.graphicsLyr.add(gra);
                    var obj = {};
                    obj.id = RandomUuid();
                    obj.src = '导入';
                    obj.graphic = gra;
                    obj.title = data.fileName || (m.GEO_IMP_PREFIX + (graphicsList.length + 1).toString());
                    obj.style = 'teal';
                    obj.type = this.type;
                    graphicsList.push(obj);
                    graExtent = gra.geometry.getExtent() == null ? this.map.extent : gra.geometry.getExtent();
                    topic.publish(m.EVENT_QUERY_RESULT, obj);
                }
                //增加图形定位
                this.map.setExtent(graExtent.expand(2.5));
                layer.msg("图形导入成功!", {time: 5000});
            }), function (err) {
                console.error(err);
            });
        },
        exportFeature: function (fs) {
            geometryIO.expToFile(fs, "xls");
        },
        /***
         *  查询范围图层
         * @param geo
         * @private
         */
        queryGeo: function (geo) {
            var availableLayers = arrayUtil.filter(this.scopeLayers, lang.hitch(this, this._filterScopeLayers));
            if (availableLayers.length > 0) {
                for (var i = 0; i < availableLayers.length; i++) {
                    var lyr = availableLayers[i];
                    QueryTask.deferredQuery(lyr.layerUrl, null, geo, null, this.map).then(lang.hitch(this, this._parseQueryResult));
                }
            } else
                warn("范围图层尚未加载!无法进行查询!");
        },
        /**
         * 过滤地图中尚未加载/不可见的范围图层
         * @private
         */
        _filterScopeLayers: function (lyr) {
            if (esriLang.isDefined(this.appConfig)) {
                var _url = lyr.layerUrl;
                var operaLayers = this.appConfig.map.operationalLayers;
                var hitLyrs = arrayUtil.filter(operaLayers, function (item) {
                    return _url.split("/oms")[1].indexOf(item.url.split("/oms")[1]) > -1;
                });
                if (esriLang.isDefined(hitLyrs[0])) {
                    var mapLyr = this.map.getLayer(hitLyrs[0].id);
                    if (esriLang.isDefined(mapLyr) && mapLyr.visible == true)
                        return true;
                }
                return false;
            }
            return true;
        },
        /***
         *
         * @param id
         * @returns {*}
         */
        findGraById: function (id) {
            var d = new Deferred();
            $.each(graphicsList, function (idx, item) {
                if (item.id === id)
                    d.callback(item.graphic);
            });
            return d;
        },

        /***
         * 处理查询的结果
         * @param a
         * @private
         */
        _parseQueryResult: function (a) { 
            var lyr = _findScopeLayer(a.url, this.scopeLayers);
            var tf = _findScopeTitleField(a.url, this.scopeLayers);
            var fs = a.featureSet;
            if (tf == undefined) tf = fs.displayFieldName;
            for (var k = 0; k < fs.features.length; k++) {
                var feature = fs.features[k];
                var g = new Graphic(feature.geometry, this.fillSymbol, feature.attributes);
                this.graphicsLyr.add(g);
                var obj = {};
                obj.title = feature.attributes[tf];
                obj.graphic = g;
                obj.id = RandomUuid();
                obj.src = lyr.layerName;
                obj.style = 'olive';
                obj.type = this.type;
                graphicsList.push(obj);
                topic.publish(m.EVENT_QUERY_RESULT, obj);
            }
        },

        /***
         * 分析
         * @param c
         */
        doAnalysis: function (option) {
            var tempthis = this;
            $.ajax({
                type: 'post'
                , url: root + '/geometryService/getenv'
                , data: {envName: "analysis.saveLog"}
                , success: function (ret) {
                    if (ret == "true") {
                        layer.open({
                            type: 1
                            , content: renderTpl("<div class=\"layui-form-item layui-form-text\">\n" +
                                "    <div style=\"margin:20px;    min-height: 36px;\">\n" +
                                "      <textarea id=\"purpose\" placeholder=\"请输入分析目的（必填）\" class=\"layui-textarea\"></textarea>\n" +
                                "    </div>\n" +
                                "  </div>")
                            , area: '460px'
                            , title: '分析目的'
                            , shade: 0.6
                            , btn: ['确定', '取消']
                            , btnAlign: 'c'
                            , btn1: function () {
                                var purpose = $("#purpose").val();
                                if (purpose === "") {
                                    layer.msg("请填写分析目的")
                                } else {
                                    tempthis.saveLog(option, purpose)
                                }
                            }
                        })
                    } else {
                        tempthis.doAnalysis2(option)
                    }
                }
            })
        },
        saveLog: function (option, purpose) {
            var tempthis = this;
            var tempType = null;
            var g = m;
            switch (this.type) {
                case g.TYPE_ECO: {
                    tempType = "生态红线分析";
                    break;
                }
                case g.TYPE_COMMON: {
                    tempType = "一般分析";
                    break;
                }
                case g.TYPE_MULTIPLE: {
                    tempType = "综合分析";
                    break;
                }
                case g.TYPE_TDLYXZ: {
                    tempType = "土地利用现状分析";
                    break;
                }
                case g.TYPE_GHSC: {
                    tempType = "规划审查分析";
                    break;
                }
                case g.TYPE_JCTB: {
                    tempType = "监测图斑分析";
                    break;
                }
                case g.TYPE_JCTBNT: {
                    tempType = "监测图斑分析";
                    break;
                }
                case g.TYPE_JCTBMAS: {
                    tempType = "监测图斑分析";
                    break;
                }
                case g.TYPE_CUSTOM: {
                    tempType = "一般分析";
                    break;
                }
                case g.TYPE_CUSTOM_COMMON: {
                    tempType = "一般分析";
                    break;
                }
                case g.TYPE_CZ_GD: {
                    tempType = "耕地分析";
                    break;
                }
                case g.TYPE_JSYDGZQ_CZ: {
                    tempType = "建设用地管制区分析";
                    break;
                }
                case g.TYPE_XZJSYD_NT: {
                    tempType = "新增建设用地分析";
                    break;
                }
                case g.TYPE_XZJSYD: {
                    tempType = "新增建设用地分析";
                    break;
                }
            }

            $.ajax({
                type: 'post'
                , url: root + '/geometryService/log/save'
                , data: {purpose: purpose, type: tempType}
                , success: function () {
                    tempthis.doAnalysis2(option)
                }
            })

        },


        /***
         * 分析
         * @param c
         */
        doAnalysis2: function (option) {
            var g = m;
            lang.mixin(this, option);
            if (this.analysisGeometry == null) {
                var features = [];
                for (var i = 0; i < graphicsList.length; i++) {
                    //判断手绘图形是否自相交
                    if (graphicsList[i].src === "手绘") {
                        var polygonTmp = new Polygon(graphicsList[i].graphic.geometry);
                        var isSelfIntersect = polygonTmp.isSelfIntersecting(polygonTmp);
                        if (isSelfIntersect) {
                            layer.msg("所绘制图形自相交 请重新绘制!");
                            return;
                        } else {
                            features.push(graphicsList[i].graphic);
                        }
                    } else {
                        features.push(graphicsList[i].graphic);
                    }
                }

                if (features.length > 0)
                    this.analysisGeometry = JSON.stringify(JsonConverters.toGeoJson({features: features}));
                else {
                    layer.msg("分析图形为空!");
                    return;
                }
            } else {
                //判断图形是否自相交
                var featureSet = JsonConverters.toEsri(JSON.parse(this.analysisGeometry), this.map.spatialReference);
                var polygonTmp = new Polygon(featureSet.geometry);
                var isSelfIntersect = polygonTmp.isSelfIntersecting(polygonTmp);
                if (isSelfIntersect) {
                    layer.msg("所绘制图形自相交 请重新绘制!");
                    return;
                }
            }
            //请求后台计算分析图形面积
            if (this.areaLimit) {
                var msg = layer.msg('验证分析面积...', {time: 8000});
                $.ajax({
                    type: 'post',
                    url: root + '/geometryService/geo/area',
                    data: {geometry: this.analysisGeometry, crs: this.map.spatialReference.wkid},
                    success: lang.hitch(this, this._processArea),
                    complete: function () {
                        if (msg != undefined) layer.close(msg);
                    }
                });
            }
        },


        /***
         * clear graphics
         */
        clear: function () {
            return this._clear();
        },

        /***
         *
         * @param r
         * @private
         */
        _processArea: function (r) {
            var self = this;
            layer.closeAll();
            if (r.hasOwnProperty("success") && r.success == false) {
                console.error(r.msg);
                return;
            }
            var area = r;
            //分析面积超过area max时 给出提示 是否继续分析
            if ((area * .00001) >= this.areaMax) {
                layer.confirm("分析面积超过设定的最大面积范围。继续?", lang.hitch(this, function (index) {
                    layer.close(index);
                    this._prepare(self);
                }), lang.hitch(this, function (index) {
                    layer.close(index);
                }));
            } else
                this._prepare(self);
        },
        /***
         * prepare to execute
         * 确定分析类型、检查分析参数是否合法等
         * @param a
         * @param b
         * @param c
         * @private
         */
        _prepare: function (c) {
            var g = m;
            var restRet = null;
            var restClause = undefined, proid = undefined;
            //处理预先rest分析结果（南通反填等功能）
            if (this.linkUrl) {
                if (urlParams.params) {
                    var locParams = $.parseJSON(urlParams.params);
                    if (urlParams && urlParams.hasOwnProperty("action") && urlParams.action == "location") {
                        var p = locParams.params;
                        proid = urlParams.proid;
                        restClause = p.where;
                    }
                }
            }
            switch (this.type) {
                case g.TYPE_COMMON: {
                    if (this.analysisLyr == null) throw new Error("analysis layer cannot be null!");
                    var data = {
                        titleField: this.titleField,
                        layerName: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        outFields: this.outFields,
                        fieldAlias: JSON.stringify(this.fieldAlias),
                        dataSource: this.dataSource
                    };
                    if (this.freeParams) {
                        lang.mixin(data, this.freeParams);
                    }
                    if (this.responseType == g.RESPONSE_TYPE_JSON) {
                        $.ajax({
                            url: g.ANALYSIS_REST_COMMON_URL,
                            data: data,
                            dataType: 'json',
                            async: false,
                            complete: lang.hitch(this, this._restAnalysisResultHandler, "anaResultHandler"),
                            error: function (err, status, e) {
                                console.error(e);
                            }
                        });
                    } else if (this.responseType == g.RESPONSE_TYPE_HTML) {
                        this._execute(g.ANALYSIS_COMMON_URL, data);
                    }
                    break;
                }
                case g.TYPE_MULTIPLE: {
                    if (this.jsonParams == null) throw new Error("jsonParams is null!");
                    if (this.mapTpl == null) throw new Error("mapTpl is null!");
                    this._execute(g.ANALYSIS_MULTIPLE_URL, {
                        geometry: this.analysisGeometry,
                        jsonParams: this.jsonParams,
                        level: this.level,
                        link: this.link,
                        tpl: this.mapTpl,
                        //下面是新加的小数点
                        decimal: this.decimal
                    });
                    break;
                }
                case g.TYPE_TDLYXZ: {
                    var option = {
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        unit: this.areaUnit,
                        tpl: this.mapTpl,
                        exportable: this.enableExportResult,
                        //下面是新加的小数点
                        decimal: this.decimal,
                        methodType: this.methodType,
                        tdlyxzfxBz: this.tdlyxzfxBz,
                        bjZipImportUnit: this.bjZipImportUnit,
                        showXzqmc: this.showXzqmc,
                        showDetail: this.showDetail,
                        groupBy: this.groupBy
                    };
                    if (esriLang.isDefined(report)) {
                        if (this.areaUnit != undefined) {
                            lang.mixin(option, {
                                unit: this.areaUnit,
                                report: encodeURI(report, "utf-8")
                            });
                        } else {
                            lang.mixin(option, {
                                unit: "SQUARE",
                                report: encodeURI(report)
                            });
                        }

                    }
                    if (this.yearList.length > 0) {
                        lang.mixin(option, {yearList: JSON.stringify(this.yearList)});
                    } else {
                        if (esriLang.isDefined(this.dltbLyr) && esriLang.isDefined(this.xzdwLyr)) {
                            lang.mixin(option, {
                                dltb: this.dltbLyr,
                                xzdw: this.xzdwLyr
                            });
                        }
                        if (esriLang.isDefined(this.lxdwLyr)) {
                            lang.mixin(options, {
                                lxdw: this.lxdwLyr
                            });
                        }
                        locQuery:this.locQuery
                    }
                    if (!esriLang.isDefined(option.yearList) && !esriLang.isDefined(option.dltb)) {
                        throw new Error("现状分析参数有误!");
                    }
                    if (esriLang.isDefined(this.linkUrl) && this.linkUrl !== '') {
                        var data = {};
                        if (this.yearList.length > 0) {
                            data = lang.mixin(option, {
                                xzdw: this.yearList[0].xzdw,
                                dltb: this.yearList[0].dltb
                            });
                        }
                        $.ajax({
                            url: g.ANALYSIS_REST_TDLYXZ_URL,
                            data: data,
                            method: "POST",
                            dataType: 'json',
                            async: false, //关闭异步
                            success: function (r) {
                                restRet = r.result;
                            }
                        });
                        var url = this.linkUrl.concat("?proid=").concat(proid).concat("&data=").concat(JSON.stringify(restRet)).concat("&where=").concat(restClause);
                        $.post(root + "/map/proxy", {requestUrl: url, dataType: "text"});
                    }
                    lang.mixin(option, {yearList: JSON.stringify(this.yearList), xzdw: "", dltb: ""});
                    if (this.methodType == g.TYPE_TDLYXZ_NT) {
                        if (esriLang.isDefined(reportNt)) {
                            if (this.areaUnit != undefined) {
                                lang.mixin(option, {
                                    unit: this.areaUnit,
                                    report: encodeURI(reportNt, "utf-8")
                                });
                            } else {
                                lang.mixin(option, {
                                    unit: "SQUARE",
                                    report: encodeURI(reportNt)
                                });
                            }
                        }
                        var geo = JSON.parse(this.analysisGeometry);
                        var dkid;
                        if (geo.hasOwnProperty('properties') && geo.properties.hasOwnProperty('DKID')) {
                            dkid = geo.properties.DKID;
                        } else if (typeof (dkid) == "undefined") {
                            dkid = urlParams.proid;
                        }
                        if (typeof (dkid) != "undefined") {
                            $.ajax({
                                url: this.reportInfoUrl + "?dkid=" + dkid,
                                method: "POST",
                                dataType: 'json',
                                async: false, //关闭异步
                                success: function (res) {
                                    if (res != null && res != "") {
                                        report = JSON.stringify(res);
                                        lang.mixin(option, {
                                            report: report
                                        });
                                    }
                                }
                            });
                        }
                        this._execute(this.requestUrl ? this.requestUrl : g.ANALYSIS_TDLYXZ_NT_URL, option);
                    } else {
                        this._execute(this.requestUrl ? this.requestUrl : g.ANALYSIS_TDLYXZ_URL, option);
                    }
                    break;
                }
                case g.TYPE_GHSC: {
                    if (this.linkUrl || this.responseType == g.RESPONSE_TYPE_JSON) {
                        var data = {
                            layerType: this.moduleConfig.layerType,
                            year: this.moduleConfig.year,
                            geometry: this.analysisGeometry,
                            outFields: this.outFields,
                            dataSource: this.dataSource
                        };
                        $.ajax({
                            url: g.ANALYSI_REST_GHSC_URL,
                            data: data,
                            method: "POST",
                            async: false, //关闭异步
                            success: function (r) {
                                restRet = r.result;
                            }
                        });
                    }
                    if (this.linkUrl) {
                        var url = this.linkUrl.concat("?proid=").concat(proid).concat("&data=").concat(JSON.stringify(postPre(restRet))).concat("&where=").concat(restClause);
                        $.post(root + "/map/proxy", {requestUrl: url, dataType: "text"});
                    }
                    if (this.responseType == g.RESPONSE_TYPE_JSON) {
                        this._restAnalysisResultHandler("ghAnalysisHandler", restRet);
                    } else {
                        this._execute(g.ANALYSIS_GHSC_URL, {
                            layerType: this.moduleConfig.layerType,
                            //下面是新加的小数点
                            decimal: this.moduleConfig.decimal,
                            year: this.moduleConfig.year,
                            geometry: this.analysisGeometry,
                            outFields: this.outFields,
                            dataSource: this.dataSource,
                            unit: this.areaUnit == "ACRE" ? "ACRES" : this.areaUnit,
                            localVersion: this.localVersion,
                            //新增底部显示
                            level: this.level
                        });
                    }
                    break;
                }
                case g.TYPE_JCTB: {
                    this._execute(g.ANALYSIS_JCTB_URL, {
                        dataSource: c.dataSource,
                        geometry: this.analysisGeometry,
                        year: c.year,
                        unit: JSON.stringify(c.unit),
                        analysisLayers: JSON.stringify(c.analysisLyrs)
                    });
                    break;
                }
                case g.TYPE_JCTBNT: {
                    this._execute(g.ANALYSIS_JCTBNT_URL, {
                        dataSource: this.dataSource,
                        geometry: this.analysisGeometry,
                        year: c.year,
                        unit: JSON.stringify(c.unit),
                        analysisLayers: JSON.stringify(c.analysisLyrs),
                        template: c.template,
                        methodType: c.methodType
                    });
                    break;
                }
                case g.TYPE_JCTBMAS: {
                    this._execute(g.ANALYSIS_JCTB_URL, {
                        dataSource: c.dataSource,
                        geometry: this.analysisGeometry,
                        year: c.year,
                        unit: JSON.stringify(c.unit),
                        analysisLayers: JSON.stringify(c.analysisLyrs),
                        template: "result-jctb-mas",
                        methodType: this.methodType
                    });
                    break;
                }
                case g.TYPE_CUSTOM: {
                    this._execute(g.ANALYSIS_GHSC_URL, lang.mixin(c, {
                        geometry: this.analysisGeometry,
                        outFields: this.outFields,
                        dataSource: this.dataSource
                    }));
                    break;
                }
                case g.TYPE_CUSTOM_COMMON: {
                    var url = g.ANALYSIS_CUSTOM_COMMON_BASE_URL.concat(this.analysisMethod);
                    var data = {
                        analysisLayers: JSON.stringify(this.analysisLyrs),
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        unit: this.areaUnit
                    };
                    this._execute(url, data);
                    break;
                }
                case g.TYPE_CZ_GD: {
                    var url = g.ANALYSIS_CZ_GD_BASE_URL.concat(this.analysisMethod);
                    var data = {
                        analysisLayers: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        unit: this.areaUnit
                    };
                    this._execute(url, data);
                    break;
                }
                case g.TYPE_ILLEGAL_XS: {
                    var url = g.ANALYSIS_ILLEGAL_XS_URL;
                    var data = {
                        geometry: this.analysisGeometry
                        , param: JSON.stringify(this.moduleConfig)
                    };
                    this._execute(url, data);
                    break;
                }
                case g.TYPE_TDDJ: {
                    var url = g.ANALYSIS_TDDJ_URL;
                    var data = {
                        analysisLayers: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        unit: this.areaUnit,
                        geometry: this.analysisGeometry,
                    };
                    this._execute(url, data);
                    break;
                }
                case g.TYPE_JSYDGZQ_CZ: {
                    this._execute(g.ANALYSIS_JSYDGZQ_CZ_URL, {
                        layerType: this.moduleConfig.layerType,
                        //下面是新加的小数点
                        decimal: this.moduleConfig.decimal,
                        year: this.moduleConfig.year,
                        geometry: this.analysisGeometry,
                        outFields: this.outFields,
                        dataSource: this.dataSource,
                        unit: this.areaUnit == "ACRE" ? "ACRES" : this.areaUnit,
                        localVersion: this.localVersion
                    });
                    break;
                }
                case g.TYPE_XZJSYD_NT: {
                    if (this.analysisLyr == null) throw new Error("analysisLyr is null!");
                    if (this.analysisGeometry == null) throw new Error("analysisGeometry is null!");
                    if (this.dataSource == null) throw new Error("dataSource is null!");
                    if (this.linkUrl == null) throw new Error("linkUrl is null!");
                    this._execute(g.ANALYSIS_XZJSYD_NT_URL, {
                        layerName: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        linkUrl: this.linkUrl
                    });
                    break;
                }
                case g.TYPE_XZJSYD: {
                    if (this.analysisLyr == null) throw new Error("analysisLyr is null!");
                    if (this.analysisGeometry == null) throw new Error("analysisGeometry is null!");
                    if (this.dataSource == null) throw new Error("dataSource is null!");
                    if (this.linkUrl == null) throw new Error("linkUrl is null!");
                    this._execute(g.ANALYSIS_XZJSYDFX_URL, {
                        layerName: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        linkUrl: this.linkUrl
                    });
                    break;
                }
                case g.TYPE_BB_CXGH: {
                    if (this.analysisLyr == null) throw new Error("analysis layer cannot be null!");
                    var data = {
                        layerName: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource
                    };
                    this._execute(g.ANALYSIS_BB_CXGH_URL, data);
                    break;
                }
                case g.TYPE_ECO: {
                    if (this.analysisLyr == null) throw new Error("analysis layer cannot be null!");
                    var data = {
                        SJHXT: this.analysisLyr.SJHX,
                        GJHXT: this.analysisLyr.GJHX,
                        geometry: this.analysisGeometry,
                        outFields: this.outFields,
                        dataSource: this.dataSource
                    };
                    this._execute(g.ANALYSIS_ECO_URL, data);
                    break;
                }
                case g.TYPE_CENTER: {
                    var data= {
                        layerName: this.analysisLyr,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource
                    };
                    this._execute(g.ANALYSIS_CENTER_URL, data);
                    break;
                }
                case g.TYPE_GHJC: {
                    var data={
                        foreverNT:this.moduleConfig.yjnt,
                        year: this.moduleConfig.year,
                        geometry: this.analysisGeometry,
                        dataSource: this.dataSource,
                        dataSourceNT:this.moduleConfig.dataSourceNT
                    };
                    this._execute(g.ANALYSIS_GHJC_URL, data);
                    break;
                }
                case g.TYPE_NEW_JSYD: {
                    var data={
                        bpLayer: this.moduleConfig.bpLayer,
                        bpDataSource: this.moduleConfig.bpDataSource,
                        xzLayer: this.moduleConfig.xzLayer,
                        xzDataSource: this.moduleConfig.xzDataSource,
                        linkUrl: this.linkUrl,
                        geometry: this.analysisGeometry
                    };
                    this._execute(g.ANALYSIS_NEW_JSYD_URL, data);
                    break;
                }
            }
        },

        /***
         * 执行分析 发送请求
         * @param a
         * @param b
         * @private
         */
        _execute: function (a, b) {
            var form = $("<form method='post' style='display:none;' target='" + this.targetName + "'></form>"),
                input;
            form.attr({"action": a});
            $.each(b, function (key, value) {
                input = $("<input type='hidden'>");
                input.attr({"name": key});
                input.val(value);
                form.append(input);
            });
            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
        },
        /****
         * clear
         * @private
         */
        _clear: function () {
            var d = new Deferred();
            if (graphicsList.length > 0) {
                for (var i = 0; i < graphicsList.length;) {
                    var gra = graphicsList[i];
                    //根据type和当前analysis的type值进行对比 如果类型一致则移除，保留其他分析中的图形
                    if (gra.type == this.type) {
                        graphicsList.splice(i, 1);
                    } else {
                        i++;
                    }
                }
            }
            this.graphicsLyr.clear();
            topic.publish('clearAnalysisResult', this.map);
            d.callback();
            return d;
        },

        /***
         * 处理rest方式分析结果
         * @param data
         * @private
         */
        _restAnalysisResultHandler: function (handler, data) {
            //publish一个anaResultHandler事件进行让具体的分析模块进行处理
            topic.publish(handler, data);

        }

    });

    function postPre(r) {
        var res = {};
        for (var i in r) {
            var item = JSON.parse(r[i]);
            var features = item.features;
            var feaRes = [];
            if (features !== undefined) {
                $.each(features, function (i, fea) {
                    var pro = fea.properties;
                    feaRes.push(pro);
                });
            }
            var featuresRes = {};
            featuresRes.detail = feaRes;
            res[i] = featuresRes;
        }
        return res;
    }


    /***
     *
     * @param a
     * @param b
     * @returns {*}
     * @private
     */
    function _findScopeTitleField(b, c) {
        for (var i = 0; i < c.length; i++) {
            var l = c[i];
            var f = l.returnFields;
            if (l.layerUrl === b) {
                return f[0].name;
            }
        }
        return undefined;
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
     *
     * @param u
     * @param s
     * @returns {*}
     * @private
     */
    function _findScopeLayer(u, s) {
        for (var i = 0; i < s.length; i++) {
            var l = s[i];
            if (l.layerUrl === u) {
                return l;
            }
        }
        return undefined;
    }

    lang.mixin(m, {
        TYPE_NEW_JSYD: 'new_jsyd',
        TYPE_GHJC: 'GHJC',
        TYPE_CENTER: 'center',
        TYPE_ECO: 'eco',
        TYPE_COMMON: 'common',
        TYPE_TDLYXZ: 'tdlyxz',
        TYPE_GHSC: 'ghsc',
        TYPE_JCTB: 'jctb',
        TYPE_JCTBNT: 'jctbnt',
        TYPE_JCTBMAS: 'jctbmas',
        TYPE_MULTIPLE: 'multiple',
        TYPE_CUSTOM: 'custom',
        TYPE_CUSTOM_COMMON: 'custom_common',
        TYPE_CZ_GD: 'czgd',
        TYPE_BB_CXGH: 'bb_cxgh',
        TYPE_TDLYXZ_NT: 'tdlyxznt',
        TYPE_XZJSYD_NT: 'xzjsydnt',
        TYPE_XZJSYD: 'xzjsyd',
        TYPE_TDDJ: 'tddj',
        TYPE_JSYDGZQ_CZ: "czjsydgzq",
        TYPE_ILLEGAL_XS: 'illegal_xs',
        UNIT_SQUARE_METERS: 'square',
        UNIT_HECTARE: 'hectare',
        UNIT_ACRE: 'acre',
        ANALYSIS_GHJC_URL:'/omp/geometryService/analysis/GHJC',
        ANALYSIS_CENTER_URL:'/omp/geometryService/analysis/center',
        ANALYSIS_ECO_URL: '/omp/geometryService/analysis/eco',
        ANALYSIS_CUSTOM_COMMON_BASE_URL: '/omp/geometryService/analysis/',
        ANALYSIS_CZ_GD_BASE_URL: '/omp/geometryService/analysis/',
        ANALYSIS_MULTIPLE_URL: '/omp/geometryService/analysis/multiple',
        ANALYSIS_COMMON_URL: '/omp/geometryService/analysis/common',
        ANALYSIS_TDLYXZ_URL: '/omp/geometryService/analysis/tdlyxz',
        ANALYSIS_TDLYXZ_NT_URL: '/omp/geometryService/analysis/tdlyxznt',
        ANALYSIS_REST_TDLYXZ_URL: '/omp/geometryService/rest/analysis/tdlyxz',
        ANALYSIS_GHSC_URL: '/omp/geometryService/analysis/tdghsc',
        ANALYSI_REST_GHSC_URL: '/omp/geometryService/rest/analysis/tdghsc',
        ANALYSIS_JCTB_URL: '/omp/geometryService/analysis/jctb',
        ANALYSIS_JCTBNT_URL: '/omp/geometryService/analysis/jctbnt',
        ANALYSIS_REST_COMMON_URL: '/omp/geometryService/rest/analysis/common',
        ANALYSIS_ILLEGAL_XS_URL: '/omp/geometryService/analysis/illegalXs',
        ANALYSIS_TDDJ_URL: '/omp/geometryService/analysis/tddj',
        ANALYSIS_JSYDGZQ_CZ_URL: '/omp/geometryService/analysis/jsydgzq/cz',
        ANALYSIS_XZJSYD_NT_URL: '/omp/geometryService/analysis/xzjsyd/nt',
        ANALYSIS_XZJSYDFX_URL: '/omp/geometryService/analysis/xzjsydfx',
        ANALYSIS_BB_CXGH_URL: '/omp/geometryService/analysis/bb/cxgh',
        ANALYSIS_NEW_JSYD_URL: '/omp/geometryService/analysis/newjsyd',
        GEO_DRAW_PREFIX: '手绘图形',
        GEO_IMP_PREFIX: '导入图形',
        EVENT_QUERY_RESULT: "queryResult",
        RESPONSE_TYPE_HTML: 'html',
        RESPONSE_TYPE_JSON: 'json'
    });
    return m;
});

