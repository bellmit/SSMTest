/**
 * 一键出图
 * @author hanguanghui
 * @Description 一键出图 支持导入图形 并实现多不同年份土地利用现状数据变化数据制表并输出
 * @version V1.0, 2017/4/26
 * @project onemap-parent
 */
/*!
用法
 * {
 "titleSize": "20",
 "textSize": "12",
 "templateName": "Land,现状",
 "jsonParams": [
 {
 "dataSource": "sde",
 "layerName": "DLTB_2009",
 "id": "compareLayer"
 },
 {
 "dataSource": "sde",
 "layerName": "DLTB_2015",
 "id": "targetLayer"
 }
 ],
 "author": "作者",
 "copyright": "版权",
 "customTextItems": [
 {
 "key": "1",
 "name": "1",
 "value": "1"
 }
 ],
 "title": "打印标题",
 "url": "http://192.168.90.7:6080/arcgis/rest/services/ExportWebMapNew/GPServer/Export%20Web%20Map"
 }
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/json",
    "dojo/topic",
    "dojo/_base/Color",
    "dojox/uuid/generateRandomUuid",
    "esri/request",
    "esri/config",
    "esri/graphic",
    "esri/geometry/Polygon",
    "esri/geometry/Point",
    "esri/geometry/Extent",
    "esri/layers/GraphicsLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/symbols/TextSymbol",
    "esri/symbols/Font",
    "mustache",
    "layer",
    "handlebars",
    "map/core/BaseWidget",
    "map/core/GeometryIO",
    "map/core/JsonConverters",
    "map/core/EsriSymbolsCreator",
    "map/core/support/Environment",
    "map/utils/MapUtils",
    "css!static/js/map/template/analysis/analysis.css",
    "css!static/thirdparty/semantic-ui/card/card.css"], function (declare, lang, arrayUtils,Json,topic,Color,RandomUuid, esriRequest, esriConfig,Graphic,Polygon,Point,Extent,GraphicsLayer,ArcGISTiledMapServiceLayer,
                                                         ArcGISDynamicMapServiceLayer, TextSymbol,Font,Mustache,layer,Handlebars, BaseWidget,GeometryIO,JsonConverters,EsriSymbolsCreator,Environment,MapUtils) {

    var _map,_printConfig;
    var me = declare([BaseWidget], {

        onCreate: function () {
            _printConfig=this.getConfig();
            _init();
        },
        onOpen: function () {

        },
        onPause: function () {
        }

    });

    var printUrl,msgHandler;
    var templateNames, printFormates, authorText, copyrightText, titleText;
    var root = "/" + window.location.pathname.split("/")[1];

    //图形导入相关变量
    var report = undefined;//电子报件信息
    var geometryIO;
    var graphicsList = [];
    var analysisFeatures=[];
    var printLayer;

    //对比分析
    var titleSize;
    var commonSize;
    var templateName;
    var pJsonParams;
    /***
     * 作为 JSON 的 Web 地图:
     * @type {{mapOptions: {}, operationalLayers: Array, baseMap: {title: string, baseMapLayers: Array}, exportOptions: {}, layoutOptions: {}}}
     */
    var web_map_as_json = {
        mapOptions:{},
        operationalLayers:[],
        baseMap:{
            title:"omp_base_map",
            baseMapLayers:[]
        },
        exportOptions:{},
        layoutOptions:{}
    };
    var format,layout_template;

    /***
     * 初始化页面
     * @private
     */
    function _init() {
        _map=map.map();
        layer.config();
        esriConfig.defaults.io.proxyUrl = "/omp/proxy.jsp";
        if (_printConfig) {
            printUrl=_printConfig.url;
            var printInfo = new esriRequest({
                "url": _printConfig.url,
                "content": { "f": "json" }
            });
            printInfo.then(handlePrintInfo, handleError);

            //获取配置中默认的各项参数值
            authorText = _printConfig.author;
            titleText = _printConfig.title;
            copyrightText = _printConfig.copyright;
            if(_printConfig.hasOwnProperty("titleSize"))titleSize=_printConfig.titleSize||20;
            if(_printConfig.hasOwnProperty("textSize"))commonSize = _printConfig.textSize||12;
            if(_printConfig.hasOwnProperty("templateName"))templateName=_printConfig.templateName.split(",");
            if(_printConfig.hasOwnProperty("jsonParams"))pJsonParams = _printConfig.jsonParams;
        }

        printLayer = new GraphicsLayer({id:"plan_graphicsLayer"});
        _map.addLayer(printLayer);
        geometryIO = new GeometryIO();

        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });
    }

    /**
     * 解析打印服务信息
     * @param resp
     */
    function handlePrintInfo(resp) {
        var layoutTemplate, formats, mapOnlyIndex;
        layoutTemplate = arrayUtils.filter(resp.parameters, function (param, idx) {
            return param.name === "Layout_Template";
        });
        formats = arrayUtils.filter(resp.parameters, function (param, idx) {
            return param.name === "Format";
        });
        if (layoutTemplate.length == 0) {
            console.log("print service parameters name for templates must be \"Layout_Template\"");
            return;
        }
        if (formats.length == 0) {
            console.log("print service parameters name for formates must be \"Format\"");
            return;
        }
        templateNames = layoutTemplate[0].choiceList;
        mapOnlyIndex = arrayUtils.indexOf(templateNames, "MAP_ONLY"); // 把MAP_ONLY加到templateNames的底部
        if (mapOnlyIndex > -1) {
            var mapOnly = templateNames.splice(mapOnlyIndex, mapOnlyIndex + 1)[0];
            templateNames.push(mapOnly);
        }
        printFormates = formats[0].choiceList;
        renderComponents();
    }

    /***
     * 渲染页面
     */
    function renderComponents() {
        var tmpl = $("#printFormTpl").html();
        Mustache.parse(tmpl);
        var form = Mustache.render(tmpl, {templates: templateNames, formats: printFormates, author: authorText, copyright: copyrightText, title: titleText});
        $("#printPanel").append(form);
        addListeners();
    }

    /**
     * 添加事件监听
     */
    function addListeners() {
        $("#printBtn").off("click");
        $("#printBtn").on('click', function () {
            checkImpGra();
        });
        $("#impBtn").off("click");
        $("#impBtn").on('click',function(){
            $("#analysis-file-input-print").click();
            addListeners();
        });
        $("#analysis-file-input-print").off("change");
        $("#analysis-file-input-print").on("change",function(){
            importFile( $("#analysis-file-input-print"));
        });

    }

    /**
     * 检测是否存在导入图形 并进行分析
     */
    function checkImpGra(){
        var isAnalysis =false;
        var arr = $("#printSetForm").serializeArray();
        var template=arr[0].value;

        arrayUtils.forEach(templateName,function(item){
            if(template.indexOf(item)>0){
                isAnalysis=true;
            }
        });
        if(isAnalysis&&graphicsList.length>0){
            var analysisGeometry;
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
                analysisGeometry = JSON.stringify(JsonConverters.toGeoJson({features: features}));
            else
            {
                layer.msg("分析图形为空!");
                return;
            }

            var index = layer.confirm("检测到有导入图形，时候进行土地利用现状对比分析?", lang.hitch(this, function () {
                layer.close(index);
                var loadingIndex = layer.msg("分析中…",{timeout:500});
                var data = {
                    jsonParams: JSON.stringify(pJsonParams),
                    geometry: analysisGeometry,
                }
                $.ajax({
                    url: '/omp/geometryService/rest/analysis/compareXz',
                    data: data,
                    method:"POST",
                    async: false, //关闭异步
                    success: function (r) {
                        layer.close(loadingIndex);
                        parseAnaResult(r.result);
                    }
                });
            }), lang.hitch(this, function (index) {
                layer.close(index);
                getActualAGSServices();
            }));
        }
    }

    function parseAnaResult(data) {
        var font = new Font();
        font.setSize(commonSize || "12pt");
        font.setFamily("宋体");

        var syms = [];
        var graExtent = null;
        var features = JSON.parse(data.result).features;
        var index = 1;

        var changeTxt = "";
        var changeInfo = [];
        arrayUtils.forEach(features, function (item) {
            var attr = item.properties;
            var f = null;
            arrayUtils.forEach(syms, function (symbol) {
                if (symbol.id == attr.before + " " + attr.after) {
                    f = symbol.f;
                    arrayUtils.forEach(changeInfo, function (info) {
                        if (info.id == attr.before + " " + attr.after) info.area += attr.area;
                    });
                }
            });
            if (f == null) {
                f = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_NULL, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, getRandomColor(), 2), new Color([0, 0, 0, 0.25]))
                syms.f = f;
                syms.id = attr.before + " " + attr.after;
                syms.index = index;

                var info = {};
                info.id = attr.before + " " + attr.after;
                info.index = index;
                info.before = attr.before;
                info.after = attr.after;
                info.area = attr.area;
                changeInfo.push(info);
                index++;
            }
            var polygonJson = {"rings": item.geometry.coordinates, "spatialReference": _map.spatialReference};
            var polygon = new Polygon(polygonJson);

            if (graExtent == null) {
                graExtent = polygon.getExtent() == null ? _map.extent : polygon.getExtent();
            } else {
                var extent = polygon.getExtent();
                //重新获取合并后的extent范围，extent自带的union合并不成功
                graExtent = new Extent(graExtent.xmin < extent.xmin ? graExtent.xmin : extent.xmin, graExtent.ymin < extent.ymin ? graExtent.ymin : extent.ymin, graExtent.xmax > extent.xmax ? graExtent.xmax : extent.xmax, graExtent.ymax > extent.ymax ? graExtent.ymax : extent.ymax, extent.spatialReference);
            }
            var gra = new Graphic(polygon, f, item.properties);
            printLayer.add(gra);

            //图形编号 相同图形标号相同
            var label = index - 1;
            var txtSym = new TextSymbol(""+label+"",font,Color.fromHex("#000"));
            var pt = polygon.getExtent().getCenter();
            var txtGra = new Graphic(pt, txtSym,null,null);
            printLayer.add(txtGra);
        });
        changeTxt = "图编号:|原地类：" + "|" + "现地类：" + "|" + "变化面积：";
        var i = 1;
        arrayUtils.forEach(changeInfo, function (info) {
            var text = info.index + "    |  " + info.before + "    |  " + info.after + "   |  " + info.area.toFixed(4);
            var txtSym = new TextSymbol(text,font,Color.fromHex("#000"));
            var pt = new Point(graExtent.getCenter().x + 100, graExtent.getCenter().y - i * 8, _map.spatialReference);
            addSplit(new Point(graExtent.getCenter().x + 100, pt.y + 6, _map.spatialReference));
            var txtGra = new Graphic(pt, txtSym);
            printLayer.add(txtGra);
            i++;
        });

        var txtSym = new TextSymbol(changeTxt,font,Color.fromHex("#000"));
        var pt = new Point(graExtent.getCenter().x + 100, graExtent.getCenter().y, _map.spatialReference);
        var txtGra = new Graphic(pt, txtSym);
        printLayer.add(txtGra);
        this.map.setExtent(graExtent.expand(2.5));

        getActualAGSServices();
    }
    function addSplit(pt){
        var tx="_______________________________________";
        var font = new Font();
        font.setSize(commonSize || "12pt");
        font.setFamily("宋体");
        var txtSym = new TextSymbol(tx,font,Color.fromHex("#000"));
        var txtGra = new Graphic(pt,txtSym);
        printLayer.add(txtGra);
    }
    /**
     * 导入
     * @param file
     */
    function importFile(file) {
        clear();
        var f =  EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_NULL,EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, "#ff0000", 2), new Color([0, 0, 0, 0.25]));
        report = undefined;
        geometryIO.impFromFile(file, _map.spatialReference, this.xy).then(lang.hitch(this, function (data) {
            var featureSet = data.fs;
            var info= data.info;

            report = data.report;
            topic.publish("report/info",report);
            topic.publish("bjTitleInfo",info);

            //定义用于图形定位的extent
            var graExtent = _map.extent;
            if (featureSet != undefined && featureSet.hasOwnProperty("features")) {
                for (var i = 0; i < featureSet.features.length; i++) {
                    var feature = featureSet.features[i];
                    var polygonTmp = new Polygon(feature.geometry);
                    var gra = new Graphic(polygonTmp, f, feature.attributes);
                    gra.geometry.spatialReference = _map.extent.spatialReference;

                    printLayer.add(gra);
                    var obj = {};
                    obj.id = RandomUuid();
                    obj.src = '导入';
                    obj.graphic = gra;
                    //若属性中带有name/title则取此属性值
                    var attr = gra.attributes;
                    if (attr != undefined && (attr.hasOwnProperty("TITLE") || attr.hasOwnProperty("NAME") || attr.hasOwnProperty("XMMC") || attr.hasOwnProperty("xmmc"))) {
                        obj.title = attr["NAME"] || attr["TITLE"] || attr["XMMC"] || attr["xmmc"];
                    } else if (data.fileName)
                        obj.title = data.fileName+"-"+ (graphicsList.length + 1).toString();
                    else
                        obj.title = "导入图形" + (graphicsList.length + 1).toString();
                    obj.style = 'teal';
                    obj.type = "imp"; //记录当前分析的类型（type），便于清除操作
                    graphicsList.push(obj);
                    if (i === 0) {
                        graExtent = gra.geometry.getExtent() == null ?_map.extent : gra.geometry.getExtent();
                    } else {
                        var extent = gra.geometry.getExtent();
                        //重新获取合并后的extent范围，extent自带的union合并不成功
                        graExtent = new Extent(graExtent.xmin < extent.xmin ? graExtent.xmin : extent.xmin, graExtent.ymin < extent.ymin ? graExtent.ymin : extent.ymin, graExtent.xmax > extent.xmax ? graExtent.xmax : extent.xmax, graExtent.ymax > extent.ymax ? graExtent.ymax : extent.ymax, extent.spatialReference);
                    }
                    //topic.publish(m.EVENT_QUERY_RESULT, obj);
                }
            } else if (featureSet != undefined && featureSet.hasOwnProperty("geometry")) { //单个图形
                var feature = featureSet;
                var polygonTmp = new Polygon(feature.geometry);
                var gra = new Graphic(polygonTmp, f);
                gra.geometry.spatialReference = _map.extent.spatialReference;
                printLayer.add(gra);
                var obj = {};
                obj.id = RandomUuid();
                obj.src = '导入';
                obj.graphic = gra;
                obj.title = data.fileName ||("导入图形" + (graphicsList.length + 1).toString());
                obj.style = 'teal';
                obj.type = "imp";
                graphicsList.push(obj);
                graExtent = gra.geometry.getExtent() == null ? _map.extent : gra.geometry.getExtent();
                //topic.publish(m.EVENT_QUERY_RESULT, obj);
            }
            //增加图形定位
            this.map.setExtent(graExtent.expand(2.5));
            layer.msg("图形导入成功!",{time:1500});
            appendLst(graphicsList);

            $("#iBtnContainer").append("<button type='button' class='btn btn-primary ' id='pringClearBtn'><i class='iconfont'>&#xe605;</i>&nbsp;清&nbsp;&nbsp;除</button>");
            $("#pringClearBtn").on("click",function(){
                clear();
            });
        }));
    }

    function clear(){
        printLayer.clear();
        $("#pringClearBtn").remove();
        $(".impDataContainer").empty();
        graphicsList =[];
    }

    /**
     *
     * @param lst
     */
    function appendLst(lst){
        arrayUtils.forEach(lst,function(obj){
            var listItem = $("#printImpLstTpl").html();
            var templ = Handlebars.compile(listItem);
            var html =templ(obj);
            $(".impDataContainer").append(html);
        });
        var scrollHeight = $(window).height() - 420;
        $(".impDataContainer").slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        $(".locationBtn").off("click");
        $(".locationBtn").on("click",function(){
            var id = $(this).data("id");
            var locGras=[];
            arrayUtils.forEach(graphicsList,function(item){
                if(item.id== id) {
                    locGras.push(item.graphic);
                }
            });
            MapUtils.locateFeatures(locGras);
        });
    }

    /***
     *
     */
    function getActualAGSServices() {
        var l = _map.layerIds.length;
        var lyrs = [];
        for (var i = 0; i < l; i++) {
            var tmp = _map.getLayer(_map.layerIds[i]);
            if (tmp.url) {
                lyrs.push(tmp.id);
            }
        }
        var request = new esriRequest({
            "url": root + '/map/agsurl',
            "content": { "layers": Json.stringify(lyrs) }
        });

        request.then(function result(resp) {
            print(resp);
        }, handleError);
    }

    /**
     * 创建随机数
     * @returns {string}
     */
    function getRandomColor(){
        return  '#' +
            (function(color){
                return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
                && (color.length == 6) ?  color : arguments.callee(color);
            })('');
    }

    /**
     * 输出
     */
    function print(agsurl){
        web_map_as_json.operationalLayers=[];
        //组织map参数
        web_map_as_json.mapOptions.extent=_map.extent;
        web_map_as_json.mapOptions.spatialReference=_map.spatialReference;
        web_map_as_json.mapOptions.scale=_map.getScale();
        web_map_as_json.mapOptions.showAttribution=true;

        var arr = $("#printSetForm").serializeArray();
        format=arr[1].value;
        layout_template=arr[0].value;

        //组织layout参数
        web_map_as_json.layoutOptions.titleText= arr[2].value;
        web_map_as_json.layoutOptions.authorText=arr[3].value;
        web_map_as_json.layoutOptions.copyrightText=arr[4].value;
        web_map_as_json.layoutOptions.scaleBarOptions={};
        web_map_as_json.layoutOptions.legendOptions={operationalLayers:[]};
        //组织export参数
        web_map_as_json.exportOptions.dpi=96;
        web_map_as_json.exportOptions.outputSize=[800,1100];

        //组织operationalLayers参数 注意替换图层的url为实际的ags的url
        for(var i=0;i<_map.layerIds.length;i++){
            var id=_map.layerIds[i];
            var currentLayer = _map.getLayer(id);

            if (currentLayer instanceof ArcGISTiledMapServiceLayer ) {
                web_map_as_json.baseMap.baseMapLayers.push({type:'tiled',url:agsurl.hasOwnProperty(id)?agsurl[id]:currentLayer.url});
            }else if(currentLayer instanceof  ArcGISDynamicMapServiceLayer){
               web_map_as_json.operationalLayers.push({
                   id:id,
                   title:id,
                   opacity:currentLayer.opacity,
                   minScale:currentLayer.minScale,
                   maxScale:currentLayer.maxScale,
                   url:agsurl.hasOwnProperty(id)?agsurl[id]:currentLayer.url,
                   layers: window.definitionExpression?window.definitionExpression[id]:null
               });
            }
        }

        var map_graphics_layer = {
            id:"",
            featureCollection: {
                layers:[
                    {
                        layerDefinition:{},
                        featureSet:{
                            geometryType:"",
                            features:[]
                        }
                    }
                ]
            }};
        var map_graphics_layer_all_points = {
            id:"map_graphics_layer_all_points",
            featureCollection:{
                layers:[
                    {
                        layerDefinition: {
                            name: "pointLayer",
                            geometryType: "esriGeometryPoint"
                        },

                        featureSet: {
                            geometryType: "esriGeometryPoint",
                            features: []
                        }
                    }
                ]
            }
        };

// map_graphics_layer_all_ploylines 存放当前map的graphics_layer的ploylines
        var map_graphics_layer_all_ploylines = {
            id:"map_graphics_layer_all_ploylines",
            featureCollection:{
                layers:[
                    {
                        layerDefinition: {
                            name: "polylineLayer",
                            geometryType: "esriGeometryPolyline"
                        },
                        featureSet: {
                            geometryType: "esriGeometryPolyline",
                            features: [ ]
                        }
                    }
                ]
            }
        };

// map_graphics_layer_all_ploygons 存放当前map的graphics_layer的ploygons
        var map_graphics_layer_all_ploygons = {
            id:"map_graphics_layer_all_ploygons",
            featureCollection:{
                layers:[
                    {
                        layerDefinition: {
                            name: "polygonLayer",
                            geometryType: "esriGeometryPolygon"
                        },

                        featureSet: {
                            geometryType: "esriGeometryPolygon",
                            features: [
                            ]
                        }
                    }
                ]
            }
        };
        //组织map的graphics参数
        for (var j=0, layers_length = _map.graphicsLayerIds.length; j<layers_length; j++) {
            var currentLayer = _map.getLayer(_map.graphicsLayerIds[j]);
            //只添加显示的图层 隐藏的不考虑
            if(currentLayer.visible==true){
                map_graphics_layer.id  = currentLayer.id;
                for (var n=0, graphics_length = currentLayer.graphics.length; n<graphics_length; n++)
                {
                    var graphic = new Graphic(currentLayer.graphics[n].geometry, currentLayer.graphics[n].symbol);
                    if (graphic.geometry.type == "point" ){
                        map_graphics_layer_all_points.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polyline" ){
                        map_graphics_layer_all_ploylines.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polygon" ){
                        map_graphics_layer_all_ploygons.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                    }
                }
            }
        }
        // 注意图层push顺序，直接决定打印显示结果，遵循面——线——点的先后顺序，防止要素压盖
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploygons);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploylines);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_points);

        console.log(JSON.stringify(web_map_as_json));
        $.ajax({
            type: "POST",
            url: root + "/map/proxy?requestUrl=" + printUrl.concat("/execute"),
            data: {
                Web_Map_as_JSON: Json.stringify(web_map_as_json),
                Format: format,
                Layout_Template: layout_template,
                method: 'post',
                f: 'json'
            },
            beforeSend :function(){
                msgHandler = layer.msg('处理中...', {time: 15000});
                $("#printBtn").addClass('disabled');
            },
            success: function (evt) {
                var result = $.parseJSON(evt);
                var target = result.results[0];
                if($("#formatSelect").val()=="PDF") {  //pdf预览
                    if (target != undefined && isAcrobatPluginInstall()) {
                        var val = target.value.url;
                        if (val != undefined)window.open(val, "_blank");
                    } else if (target != undefined && !isAcrobatPluginInstall()) {
                        layer.confirm('检测到您的浏览器没有Adobe Reader插件,为了方便预览PDF文档，请选择是否安装？', {
                            btn: ['安装Adobe Reader', ' 直接下载PDF '] //按钮
                        }, function () {
                            //location = 'http://ardownload.adobe.com/pub/adobe/reader/win/9.x/9.3/chs/AdbeRdr930_zh_CN.exe';
                            location = 'http://ardownload.adobe.com/pub/adobe/reader/win/11.x/11.0.00/zh_CN/AdbeRdr11000_zh_CN.exe';
                        }, function () {
                            location = target.value.url;
                        });
                    }
                }else{
                    if (target != undefined) {
                        var val = target.value.url;
                        if (val != undefined)window.open(val, "_blank");
                    }
                }
            },
            error:function(e){
                console.error("打印失败！"+e);
            },
            complete :function(evt){
                if (msgHandler != undefined)layer.close(msgHandler);
                $("#printBtn").removeClass('disabled');
            }

        });
    }
    /***
     * 调用gp服务成功
     * @param evt
     */
    function onPrintCompleteHandler(evt){
        layer.close(msgHandler);
        if(evt!=undefined&&evt.hasOwnProperty("url")){
            window.open(evt.url,'_blank');
        }
    }

    /***
     * 调用gp服务异常
     * @param e
     */
    function onPrintError(e){
        layer.close(msgHandler);
        console.error("print task error:"+ e.error.message);
    }

    /***
     * 异常
     * @param err
     */
    function handleError(err) {
        console.error("Something error: ", err);
    }

    /**
     * 检测浏览器 adboe pdf 插件是否安装
     * @returns {boolean}
     */
    function isAcrobatPluginInstall() {
        var flag = false;

        //chrome 支持pdf预览
        if(Environment.isChrome()){
            flag = true;
        }
        // 如果是firefox浏览器
        if (navigator.plugins && navigator.plugins.length) {
            for (var x = 0; x < navigator.plugins.length; x++) {
                if (navigator.plugins[x].name == 'Adobe Acrobat')
                    flag = true;
            }
        }
        // 下面代码都是处理IE浏览器的情况
        else if (window.ActiveXObject) {
            for (var x = 2; x < 10; x++) {
                try {
                    var oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
                    if (oAcro) {
                        flag = true;
                    }
                } catch (e) {
                    flag = false;
                }
            }
            try {
                var oAcro4 = new ActiveXObject('PDF.PdfCtrl.1');
                if (oAcro4)
                    flag = true;
            } catch (e) {
                flag = false;
            }
            try {
                var oAcro7 = new ActiveXObject('AcroPDF.PDF.1');
                if (oAcro7)
                    flag = true;
            } catch (e) {
                flag = false;
            }
        }
        return flag;
    }

    return me;
});