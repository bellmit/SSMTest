/**
 * 地图打印模块
 * {
 *      "author": "作者",
 *      "copyright": "版权",
 *      "title": "打印标题",
 *      "customTextItems": [
 *        {
 *          "key": "1",
 *          "name": "1",
 *         "value": "1"
 *       }
 *     ],
 *     "url": "http://xxx:gpurl",
 *     "allowMapAnnotation": true
 *  }
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:03
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/json",
    "esri/request",
    "esri/config",
    "esri/graphic",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/toolbars/draw",
    "mustache",
    "layer",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/core/EsriSymbolsCreator"], function (declare, lang, arrayUtils, Json, esriRequest, esriConfig, Graphic, ArcGISTiledMapServiceLayer,
                                     ArcGISDynamicMapServiceLayer, Draw, Mustache, layer, BaseWidget, Environment, EsriSymbolsCreator) {

    var _map, _printConfig;
    var me = declare([BaseWidget], {

        onCreate: function () {
            _printConfig = this.getConfig();
            _init();
        },
        onOpen: function () {
            if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").show();
        },
        onPause: function () {
            if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").hide();
        },
        onDestroy: function () {
            if (_map.getLayer("graphics4Print")) _map.removeLayer(_map.getLayer("graphics4Print"));
        }

    });
    var drawAnnotationTool;
    var printUrl, msgHandler;
    var templateNames, printFormates, authorText, copyrightText, titleText;
    var root = "/" + window.location.pathname.split("/")[1];
    /***
     * 作为 JSON 的 Web 地图:
     * @type {{mapOptions: {}, operationalLayers: Array, baseMap: {title: string, baseMapLayers: Array}, exportOptions: {}, layoutOptions: {}}}
     */
    var web_map_as_json = {
        mapOptions: {},
        operationalLayers: [],
        baseMap: {
            title: "omp_base_map",
            baseMapLayers: []
        },
        exportOptions: {},
        layoutOptions: {}
    };
    var format, layout_template;

    /***
     * 初始化页面
     * @private
     */
    function _init() {
        _map = map.map();
        layer.config();
        esriConfig.defaults.io.proxyUrl = "/omp/proxy.jsp";
        if (_printConfig) {
            printUrl = _printConfig.url;
            var printInfo = new esriRequest({
                "url": _printConfig.url,
                "content": {"f": "json"}
            });
            printInfo.then(handlePrintInfo, handleError);

            //获取配置中默认的各项参数值
            authorText = _printConfig.author;
            titleText = _printConfig.title;
            copyrightText = _printConfig.copyright;
        }
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
        var data = {
            templates: templateNames,
            formats: printFormates,
            author: authorText,
            copyright: copyrightText,
            title: titleText
        };
        if (_printConfig.allowMapAnnotation === true) {
            data = lang.mixin(data, {allowMapAnnotation: true});
        }
        var form = Mustache.render(tmpl, data);
        $("#printPanel").append(form);
        addListeners();
    }

    var toh, annoHandler;

    /**
     * 添加事件监听
     */
    function addListeners() {
        $("#printBtn").on('click', function () {
            getActualAGSServices();
        });

        if ($("#annotationBtn").length > 0) {
            $("#annotationBtn").on('click', function () {
                console.log('----add annotation-----');
                drawAnnotationTool = drawAnnotationTool ? drawAnnotationTool : new Draw(_map);
                annoHandler = drawAnnotationTool.on("draw-end", lang.hitch(_map, addAnnotation));
                esri.bundle.toolbars.draw.addPoint = "单击添加注记";
                drawAnnotationTool.activate(Draw.POINT);
                var mapClickHandler = _map.on('click', function () {
                    window.clearTimeout(toh);
                    mapClickHandler.remove();
                });
                toh = window.setTimeout(function () {
                    drawAnnotationTool.deactivate();
                }, 5000);
            });
        }
    }

    /**
     * add annotation to map
     * @param pnt
     */
    function addAnnotation(geo) {
        _map.setScale(20000);
        _map.centerAt(geo.geometry);
        layer.closeAll();
        drawAnnotationTool.deactivate();
        annoHandler.remove();
        layer.open({
            type: 1,
            title: '添加注记',
            area: '240px',
            shade: 0,
            content: '<textarea style="width: 100%;height: 156px;" id="annoText"></textarea>',
            btn: ['确认','放弃'],
            btnAlign: 'r',
            yes: function(index, layero){
                addAnno2Map($.trim($("#annoText").val()), geo.geometry);
                layer.close(index);
            },
            btn2: function (index) {
                $("#annoText").html("");
                layer.close(index);
            }

        });
    }

    /**
     * 添加注记到 地图
     * @param text
     * @param pnt
     */
    function addAnno2Map(text,pnt) {
        var txtSymbol = EsriSymbolsCreator.createTextSymbol(text, '#7F7F7F', 16);
        txtSymbol.setAlign('middle');
        txtSymbol.setVerticalAlignment("top");
        txtSymbol.setHorizontalAlignment("left");
        txtSymbol.setOffset(24, 0);
        var txtGra = new Graphic(pnt, txtSymbol);
        _map.graphics.add(txtGra);
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
                var bId = getRealBaseLyrId(tmp);
                lyrs.push(bId === undefined ? tmp.id : bId);
            }
        }
        var request = new esriRequest({
            "url": root + '/map/agsurl',
            "content": {"layers": Json.stringify(lyrs)}
        });

        request.then(function result(resp) {
            print(resp);
        }, handleError);
    }

    /**
     * get real base layer id
     */
    function getRealBaseLyrId(mapLyr) {
        var ret = undefined;
        if (appConfig.map.baseLayers.length > 0) {
            var tmpArr = arrayUtils.filter(appConfig.map.baseLayers, function (lyr) {
                return lyr.url === mapLyr.url;
            });
            if (tmpArr.length > 0) {
                ret = tmpArr[0].id;
            }
        }
        return ret;
    }

    /**
     * 输出
     */
    function print(agsurl) {
        web_map_as_json.operationalLayers = [];
        //组织map参数
        web_map_as_json.mapOptions.extent = _map.extent;
        web_map_as_json.mapOptions.spatialReference = _map.spatialReference;
        web_map_as_json.mapOptions.scale = _map.getScale();
        web_map_as_json.mapOptions.showAttribution = true;

        var arr = $("#printSetForm").serializeArray();
        format = arr[1].value;
        layout_template = arr[0].value;

        //组织layout参数
        web_map_as_json.layoutOptions.titleText = arr[2].value;
        web_map_as_json.layoutOptions.authorText = arr[3].value;
        web_map_as_json.layoutOptions.copyrightText = arr[4].value;
        web_map_as_json.layoutOptions.scaleBarOptions = {};
        web_map_as_json.layoutOptions.legendOptions = {operationalLayers: []};
        //组织export参数
        web_map_as_json.exportOptions.dpi = 300;
        web_map_as_json.exportOptions.outputSize = [800, 1100];

        //组织operationalLayers参数 注意替换图层的url为实际的ags的url
        for (var i = 0; i < _map.layerIds.length; i++) {
            var id = _map.layerIds[i];
            var currentLayer = _map.getLayer(id);

            if (currentLayer instanceof ArcGISTiledMapServiceLayer) {
                if (getRealBaseLyrId(currentLayer) != undefined) {
                    id = getRealBaseLyrId(currentLayer);
                }
                web_map_as_json.baseMap.baseMapLayers.push({
                    type: 'tiled',
                    url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url
                });
            } else if (currentLayer instanceof ArcGISDynamicMapServiceLayer) {
                web_map_as_json.operationalLayers.push({
                    id: id,
                    title: id,
                    opacity: currentLayer.opacity,
                    minScale: currentLayer.minScale,
                    maxScale: currentLayer.maxScale,
                    url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url,
                    layers: window.definitionExpression ? window.definitionExpression[id] : null
                });
            }
        }

        var map_graphics_layer = {
            id: "",
            featureCollection: {
                layers: [
                    {
                        layerDefinition: {},
                        featureSet: {
                            geometryType: "",
                            features: []
                        }
                    }
                ]
            }
        };
        var map_graphics_layer_all_points = {
            id: "map_graphics_layer_all_points",
            featureCollection: {
                layers: [
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
            id: "map_graphics_layer_all_ploylines",
            featureCollection: {
                layers: [
                    {
                        layerDefinition: {
                            name: "polylineLayer",
                            geometryType: "esriGeometryPolyline"
                        },
                        featureSet: {
                            geometryType: "esriGeometryPolyline",
                            features: []
                        }
                    }
                ]
            }
        };

// map_graphics_layer_all_ploygons 存放当前map的graphics_layer的ploygons
        var map_graphics_layer_all_ploygons = {
            id: "map_graphics_layer_all_ploygons",
            featureCollection: {
                layers: [
                    {
                        layerDefinition: {
                            name: "polygonLayer",
                            geometryType: "esriGeometryPolygon"
                        },

                        featureSet: {
                            geometryType: "esriGeometryPolygon",
                            features: []
                        }
                    }
                ]
            }
        };
        var graphicsIds = [_map.graphics.id].concat(_map.graphicsLayerIds);
        //组织map的graphics参数
        for (var j = 0, layers_length = graphicsIds.length; j < layers_length; j++) {
            var currentLayer = _map.getLayer(graphicsIds[j]);
            //只添加显示的图层 隐藏的不考虑
            if (currentLayer != undefined && currentLayer.visible == true) {
                map_graphics_layer.id = currentLayer.id;
                for (var n = 0, graphics_length = currentLayer.graphics.length; n < graphics_length; n++) {
                    var graphic = new Graphic(currentLayer.graphics[n].geometry, currentLayer.graphics[n].symbol);
                    if (graphic.geometry.type == "point") {
                        if(graphic.geometry.x == 0 && graphic.geometry.y == 0){
                            continue;
                        }

                        map_graphics_layer_all_points.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                        // map_graphics_layer_all_points.featureCollection.layers[0].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polyline") {
                        // var len = map_graphics_layer_all_ploylines.featureCollection.layers.length;
                        // map_graphics_layer_all_ploylines.featureCollection.layers[1 + len].featureSet.features.push(graphic.toJson());
                        map_graphics_layer_all_ploylines.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polygon") {
                        // var len = map_graphics_layer_all_ploygons.featureCollection.layers.length;
                        // map_graphics_layer_all_ploygons.featureCollection.layers[len - 1].featureSet.features.push(graphic.toJson());
                        // map_graphics_layer_all_ploygons.featureCollection.layers[j-1].featureSet.features.push(graphic.toJson());
                        map_graphics_layer_all_ploygons.featureCollection.layers[0].featureSet.features.push(graphic.toJson());
                    }
                }
            }
        }
        // 注意图层push顺序，直接决定打印显示结果，遵循面——线——点的先后顺序，防止要素压盖
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploygons);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploylines);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_points);

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
            beforeSend: function () {
                msgHandler = layer.msg('处理中...', {time: 15000});
                $("#printBtn").addClass('disabled');
            },
            success: function (evt) {
                console.log(evt.toString());
                var result = $.parseJSON(evt);
                if(typeof result != 'undefined' && result.error && result.error.code && result.error.code == 500){
                    layer.msg('系统错误：' + result.error.message);
                    return;
                }
                var target = result.results[0];
                if ($("#formatSelect").val() == "PDF") {  //pdf预览
                    if (target != undefined && isAcrobatPluginInstall()) {
                        var val = target.value.url;
                        if (val != undefined) window.open(val, "_blank");
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
                } else {
                    if (target != undefined) {
                        var val = target.value.url;
                        if (val != undefined) window.open(val, "_blank");
                    }
                }
            },
            error: function (e) {
                console.error("打印失败！" + e);
            },
            complete: function (evt) {
                if (msgHandler != undefined) layer.close(msgHandler);
                $("#printBtn").removeClass('disabled');
            }

        });
    }

    /***
     * 调用gp服务成功
     * @param evt
     */
    function onPrintCompleteHandler(evt) {
        layer.close(msgHandler);
        if (evt != undefined && evt.hasOwnProperty("url")) {
            window.open(evt.url, '_blank');
        }
    }

    /***
     * 调用gp服务异常
     * @param e
     */
    function onPrintError(e) {
        layer.close(msgHandler);
        console.error("print task error:" + e.error.message);
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
        if (Environment.isChrome()) {
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
