/**
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/11 10:08
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "dojo/on",
    "dojo/dom",
    "mustache",
    "layer",
    "handlebars",
    "esri/dijit/Legend",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/FeatureLayer",
    "esri/layers/LayerDrawingOptions",
    "esri/renderers/ClassBreaksRenderer",
    "esri/renderers/UniqueValueRenderer",
    "esri/symbols/SimpleLineSymbol",
    'esri/symbols/SimpleFillSymbol',
    "esri/Color",
    "esri/layers/ImageParameters",
    "map/manager/ConfigManager",
    "map/core/BaseWidget"], function (declare, lang, arrayUtil, Deferred, on,dom, Mustache, layer, Handlebars, Legend, ArcGISDynamicMapServiceLayer, FeatureLayer, LayerDrawingOptions, ClassBreaksRenderer, UniqueValueRenderer,
                                      SimpleLineSymbol, SimpleFillSymbol, Color, ImageParameters, ConfigManager, BaseWidget) {

    var _rendererConfig, _map, _dynamicLayer, configManager = ConfigManager.getInstance();

    var renderer = declare([BaseWidget], {
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
            _rendererConfig = this.getConfig();
            _init();
        },
        onDestroy: function () {

        }
    });

    var $rendererPanel, allFields;

    //renderer params
    var renderLayer, renderType, renderField, renderFieldAlias, renderLevel;
    //renderer fields unique value
    var uniqueValue = [];

    function _init() {
        esri.config.defaults.io.proxyUrl = root + "/map/proxy";
        esri.config.defaults.io.alwaysUseProxy = false;
        //esri.config.defaults.io.corsDetection=false;
        //初始化 renderertpl
        //初始化 renderertpl
        $rendererPanel = $("#rendererPanel");
        //初始化默认模板
        $rendererPanel.append(Mustache.render($("#layerRendererTpl").html(), {}));

        //根据配置增加渲染图层和渲染类型
        $.each(_rendererConfig.rendererTypes, function (i, type) {
            $("#rendererTypeSelect").append('<option value="' + type.type + '">' + type.alias + '</option>');
        });
        $.each(_rendererConfig.layers, function (i, lyr) {
            if (i == 0)
                allFields = lyr.returnFields;
            var serviceID = lyr.serviceId;
            var l = _getService(serviceID);
            $("#rendererLayerSelect").append('<option value="' + lyr.serviceId + '">' + l.alias + '</option>');
        });
        //默认添加所有字段进行唯一值渲染
        $.each(allFields, function (i, field) {
            $("#rendererFieldSelect").append('<option value="' + field.name + '">' + field.alias + '</option>');
        });
        //增加分类级别 并隐藏
        var tpl = $("#rendererLevel").html();
        $("#rendererField").after(Mustache.render(tpl, {}));
        $("#rendererLevel").addClass("hidden");
        //增加渲染级别，从三开始
        for (var i = 3; i < 10; i++) {
            $("#rendererLevelSelect").append('<option value="' + i + '">' + i + '</option>');
        }
        addListener();
    }


    /**
     * add listener
     */
    function addListener() {
        $("#rendererBtn").on("click", function () {
            layer.msg("图层渲染中..", {time: 20000});
            //清除dynamic layer
            arrayUtil.forEach(_rendererConfig.layers, function (layer) {
                var layerids = _map.layerIds;
                var lyr = arrayUtil.filter(layerids, function (item) {
                    return layer.serviceId == item;
                });
                if (lyr.length > 0) {
                    var l = _map.getLayer(layer.serviceId);
                    _map.removeLayer(l);
                }
            });

            renderLayer = $("#rendererLayerSelect").val();

            renderType = $("#rendererTypeSelect").val();
            renderField = $("#rendererFieldSelect").val();

            var field = arrayUtil.filter(allFields, function (item) {
                return renderField == item.name;
            });
            renderFieldAlias = field[0].alias;
            //获取渲染字段唯一值
            // uniqueRender();
            getUniqueValue().then(function () {
                if (renderType == "class") {
                    renderLevel = $("#rendererLevelSelect").val();
                    classRender();
                } else {
                    uniqueRender();
                }
            });
        });
        //图层选择改变事件
        $("#rendererLayerSelect").on("change", function () {
            var serviceId = $("#rendererLayerSelect").val();
            var lyr = arrayUtil.filter(_rendererConfig.layers, function (lyr) {
                return serviceId === lyr.serviceId;
            });
            allFields = lyr[0].returnFields;
            $("#rendererFieldSelect").empty();
            $.each(allFields, function (i, field) {
                $("#rendererFieldSelect").append('<option value="' + field.name + '">' + field.alias + '</option>');
            });
            filterField();
        });

        //渲染类型改变
        $("#rendererTypeSelect").on("change", function () {
            filterField();
        });
    }

    /**
     * filter field by renderType
     */
    function filterField() {
        var type = $("#rendererTypeSelect").val();
        var fields = [];
        if (type == "class") {
            fields = arrayUtil.filter(allFields, function (field) {
                return field.type == type;
            });
            $("#rendererLevel").removeClass("hidden");
        } else {
            if (!$("#rendererLevel").hasOwnProperty("hidden"))
                $("#rendererLevel").addClass("hidden");
            fields = allFields;
        }
        //默认添加所有字段进行唯一值渲染
        $("#rendererFieldSelect").empty();
        $.each(fields, function (i, field) {
            $("#rendererFieldSelect").append('<option value="' + field.name + '">' + field.alias + '</option>');
        });
    }

    /**
     * get unique value by renderer field from the server
     * @returns {Deferred}
     */
    function getUniqueValue() {
        var d = new Deferred();
        var layerurl = _getService(renderLayer).url + "/0";
        var data = {layerUrl: encodeURI(layerurl), where: "1=1", returnFields: renderField};
        $.ajax({
            url: "/omp/map/query",
            data: data,
            success: function (r) {
                var rp = JSON.parse(r);
                uniqueValue = [];
                arrayUtil.forEach(rp.features, function (feature) {
                    var value = feature.attributes[renderField];
                    if (value != null && $.inArray(value, uniqueValue) == -1) {
                        uniqueValue.push(value);
                    }
                });
                d.callback(true);
            }
        });
        return d;
    }

    /**
     * class renderer
     */
    function classRender() {
        bubbleSort();
        var min;
        for (var i = 0; i < uniqueValue.length; i++) {
            if (null != uniqueValue[i] && " " != uniqueValue[i]) {
                min = uniqueValue[i];
                break;
            }
        }
        //計算步長
        var step = (uniqueValue[uniqueValue.length - 1] - min) / renderLevel;

        var ldo = new LayerDrawingOptions();
        ldo.layerId = renderLayer;

        var symbol;
        var symbolAyy = [];

        var colors = generateSymbol(renderLevel);
        var defaultSymbol = createSymbol(colors[colors.length - 1]);
        var renderer = new ClassBreaksRenderer(defaultSymbol, renderField);
        $.each(colors, function (i, item) {
            var mins = min + step * i;
            var maxs;
            if (i == renderLevel - 1) {
                maxs = uniqueValue[uniqueValue.length - 1];
            } else {
                maxs = min + step * (i + 1);
            }
            renderer.addBreak(mins, maxs, createSymbol(item));
        });
        ldo.renderer = renderer;
        var lyr = _getService(renderLayer);
        _dynamicLayer = new ArcGISDynamicMapServiceLayer(lyr.url, []);
        _dynamicLayer.id = renderLayer;
        _dynamicLayer.layerDrawingOptions = [ldo];
        _dynamicLayer.visible = true;
        _map.addLayer(_dynamicLayer);
        createLegend();
        layer.closeAll();

        layer.msg("地图分级渲染成功！",{icon:1});
    }

    /**
     * unique renderer
     */
    function uniqueRender() {
        var ldo = new LayerDrawingOptions();
        ldo.layerId = renderLayer;

        var defaultSymbol = new SimpleFillSymbol().setColor(GetRandomNum());
        defaultSymbol.outline.setStyle(SimpleLineSymbol.STYLE_NULL);

        var renderer = new UniqueValueRenderer(defaultSymbol, renderField);
        arrayUtil.forEach(uniqueValue, function (item) {
            if (" " != item)
                renderer.addValue({value: item, symbol: createSymbol(GetRandomNum()), label: item});
        });
        ldo.renderer = renderer;
        var lyr = _getService(renderLayer);
        _dynamicLayer = new ArcGISDynamicMapServiceLayer(lyr.url, []);
        _dynamicLayer.id = renderLayer;
        _dynamicLayer.layerDrawingOptions = [ldo];
        _dynamicLayer.visible = true;
        _map.addLayer(_dynamicLayer);

        createLegend();
        layer.closeAll();

        layer.msg("地图唯一值渲染成功！",{icon:1});
    }

    /**
     * create legend
     */
    function createLegend() {
        var legend = new Legend({
            map : _map,
            layerInfos : [ {
                layer : _map.getLayer(renderLayer),
                title : renderFieldAlias
            } ]
        }, dom.byId("legendDiv"));
        legend.startup();
    }

    /**
     * show legend
     * @param url
     * @param lyr
     */
    function showLegend(url, lyr) {
        var layerObj = {};

        if ($("#legendDiv"))
            $("#legendDiv").empty();
        var legendUrl;
        if (url.startWith(window.location.protocol))
            legendUrl = root + "/proxy.jsp?" + encodeURI(encodeURI(url)) + "/legend?f=json" + "&dynamicLayers=" + JSON.stringify([lyr.dynamicLayerInfos]);
        else
            legendUrl = root + "/proxy.jsp?" + window.location.origin + encodeURI(encodeURI(url)) + "/legend?f=json" + "&dynamicLayers=" + JSON.stringify([lyr]);
        $.ajax({
            url: legendUrl,
            async: false,
            success: function (res) {
                var r = $.parseJSON(res);
                if (r.hasOwnProperty("layers")) {
                    layerObj = r;
                    var legends = [];
                    arrayUtil.forEach(layerObj.layers, function (lyr) {
                        arrayUtil.forEach(lyr.legend, function (item) {
                            item.layerName = lyr.layerName;
                            legends.push(item);
                        });
                    });
                    var tpl = $("#renderLegend").html();
                    var template = Handlebars.compile(tpl);
                    $("#legendDiv").append(template({legend: legends}));
                } else
                    console.log("get none legend layers!");
            }
        });
    }

    /**
     * bubbleSort the unique value
     */
    function bubbleSort() {
        for (var i = 0; i < uniqueValue.length; i++) {
            for (var j = i; j < uniqueValue.length; j++) {
                if (uniqueValue[i] > uniqueValue[j]) {
                    var tmp = uniqueValue[i];
                    uniqueValue[i] = uniqueValue[j];
                    uniqueValue[j] = tmp;
                }
            }
        }
    }

    /**
     * create symbol
     * @param color
     * @returns {*}
     */
    function createSymbol(color) {
        return new SimpleFillSymbol()
            .setColor(color)
            .setOutline(
            new SimpleLineSymbol().setColor(color).setWidth(0.5)
        );
    }

    /**
     * create random color
     * @returns {Color}
     * @constructor
     */
    function GetRandomNum() {
        var r = Math.floor(Math.random() * 255);
        var g = Math.floor(Math.random() * 255);
        var b = Math.floor(Math.random() * 255);

        var color = new Color([r, g, b, 0.8]);
        return color;
    }

    /**
     * create series color by renderLevel
     * @param color1
     * @param color2
     * @param steps
     * @returns {Array}
     */
    function generateSymbol(steps) {
        var color1RGB = [255, 0, 0];
        var color2RGB = [255, 220, 220];

        var rDiff = (color2RGB[0] - color1RGB[0]) / (steps - 1);
        var gDiff = (color2RGB[1] - color1RGB[1]) / (steps - 1);
        var bDiff = (color2RGB[2] - color1RGB[2]) / (steps - 1);

        var colors = [];
        colors.push(new Color([color2RGB[0], color2RGB[1], color2RGB[2], 0.8]));
        for (var i = 1; i < steps - 1; i++) {
            var color = new Color([Math.floor(color1RGB[0] + rDiff * (steps - 1 - i)), Math.floor(color1RGB[1] + gDiff * (steps - i - 1)), Math.floor(color1RGB[2] + bDiff * (steps - i - 1)), 0.8]);
            colors.push(color);
        }
        colors.push(new Color([color1RGB[0], color1RGB[1], color1RGB[2], 0.8]));
        return colors;
    }

    /**
     * get service for config
     * @param id
     * @returns {*}
     * @private
     */
    function _getService(id) {
        var operaLayers = configManager.getAppConfig().map.operationalLayers;
        for (var i in operaLayers) {
            var s = operaLayers[i];
            if (s.id === id) {
                return s;
            }
        }
    }

    return renderer;
});
