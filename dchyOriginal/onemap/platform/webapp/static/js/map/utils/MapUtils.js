/**
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/8 8:23
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/on",
    "esri/graphic",
    "esri/lang",
    "esri/Color",
    "esri/graphicsUtils",
    "esri/geometry/scaleUtils",
    "esri/SpatialReference",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/FeatureLayer",
    "esri/layers/GraphicsLayer",
    "map/core/EsriSymbolsCreator"
], function (arrayUtil, lang, on, Graphic, esriLang, Color, graphicsUtils, scaleUtils, SpatialReference,
             ArcGISTiledMapServiceLayer, ArcGISDynamicMapServiceLayer, FeatureLayer, GraphicsLayer, EsriSymbolsCreator) {

    var map,timer,flashFeature;
    var defFillSymbol = EsriSymbolsCreator.defaultFillSymbol;
    var defLineSymbol = EsriSymbolsCreator.defaultLineSymbol;
    var defMarkerSymbol = EsriSymbolsCreator.defaultMarkerSymbol;
    var defaultLocateFillSymbol = EsriSymbolsCreator.defaultLocateFillSymbol;
    var ghLocateFillSymbol = EsriSymbolsCreator.ghLocateFillSymbol;
    var defaultNoColorSymbol = EsriSymbolsCreator.defaultNoColorSymbol;
    /**
     *
     * @param value
     */
    var setMap = function (value) {
        map = value;
    };
    /**
     * 刷新地图
     */
    var refreshMap = function () {
        for (var i in map.layerIds) {
            var layerId = map.layerIds[i];
            var layer = map.getLayer(layerId);
            if ('refresh' in layer)
                layer.refresh();
        }
    };

    var timeout = 0;
    /**
     * 高亮features
     * @param feature
     * @param autoClear
     * @param opacity
     * @private
     */
    var _highlightFeature = function (feature, autoClear, opacity, graphicsLayer, type) {
        var geo = feature.geometry;
        var symbol = feature.symbol;
        if (geo != undefined) {
            if (symbol == undefined) {
                switch (geo.type) {
                    case "point":
                        symbol = defMarkerSymbol;
                        break;
                    case "polyline":
                        symbol = defLineSymbol;
                        break;
                    case "polygon":
                        //symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([44, 103, 193]), 2), new Color([247, 151, 9, opacity!=undefined?opacity:1]));
                        if (esriLang.isDefined(opacity)) {
                            if (type == "gh") {
                                symbol = lang.clone(ghLocateFillSymbol);
                            } else if (type == "noColor") {
                                symbol = lang.clone(defaultNoColorSymbol);
                            } else {
                                symbol = lang.clone(defaultLocateFillSymbol);
                            }
                            var c = symbol.color;
                            var rgba = c.toRgba();
                            if (rgba.length === 4) {
                                rgba[3] = opacity;
                            } else {
                                rgba.push(opacity);
                            }
                            c.setColor(rgba);
                            symbol.setColor(c);
                        } else {
                            symbol = defaultLocateFillSymbol;
                        }
                        break;
                }
            }
            var graphic = new Graphic(feature.geometry, symbol, feature.attributes, feature.infoTemplate);

            var graphicsLyr = graphicsLayer ? graphicsLayer : map.graphics;
            graphicsLyr.add(graphic);
            graphicsLyr.show();
            if (type == "noColor") {
                flashFeatures(graphicsLyr.graphics, "fast", null, 9000);
            }
            if (autoClear === true) {
                clearTimeout(timeout);
                timeout = setTimeout(function () {
                    graphicsLyr.clear();
                }, 3000);
            }
        }
    };

    /**
     * 地图上高亮图形要素
     * @param features   高亮的要素
     * @param autoClear  是否自动清除
     * @param opacity  填充透明度
     * @param layerName graphicsLayer Name 如果不存在则使用默认的graphicsLayer
     */
    var highlightFeatures = function (features, autoClear, opacity, layerName, type) {
        var graphicsLyr = null;
        if (layerName !== undefined && layerName != null) {
            var graphicsLayer = map.getLayer(layerName);
            if (graphicsLayer !== undefined) {
                graphicsLyr = graphicsLayer;
            } else {
                graphicsLyr = new GraphicsLayer({id: layerName});
                map.addLayer(graphicsLyr);
            }
        } else {
            graphicsLyr = map.graphics;
        }

        graphicsLyr.clear();
        for (var i in features) {
            _highlightFeature(features[i], autoClear, opacity, graphicsLyr, type);
        }
    };

    /**
     *
     * @param features
     * @param style
     * @param opacity
     * @param autoClear
     */
    var highlightFeaturesWithStyle = function (features, style, opacity, autoClear, type) {
        if (esriLang.isDefined(style)) {
            if (!style.width) {
                defaultLocateFillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(style.fillStyle,
                    EsriSymbolsCreator.createSimpleLineSymbol(style.lineStyle, style.lineColor), style.fillColor);
            } else {
                defaultLocateFillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(style.fillStyle,
                    EsriSymbolsCreator.createSimpleLineSymbol(style.lineStyle, style.lineColor, style.width), style.fillColor);
            }
        }
        highlightFeatures(features, autoClear || false, opacity, null, type);
    };

    /**
     * 高亮图形带自定义样式
     * @param geoType
     * @param features
     * @param symbol
     * @param opacity
     * @param autoClear
     */
    var highlightFeaturesWithSymbol = function (geoType, features, symbol, opacity, autoClear) {
        if (esriLang.isDefined(symbol)) {
            if (geoType === 'point') {
                defMarkerSymbol = symbol
            } else if (geoType === 'polyline') {
                defLineSymbol = symbol;
            } else {
                defFillSymbol = symbol;
            }
            highlightFeatures(features, autoClear || false, opacity);
        }
    };

    /**
     * 定位图形
     * @param features
     */
    var locateFeatures = function (features, expandNum) {
        var extent = graphicsUtils.graphicsExtent(features);
        //为点设置比例尺
        if (features[0].geometry.type === "point") {
            map.setScale(10000);
        }
        if (extent != null && extent != undefined) {
            extent = lang.mixin(extent, {spatialReference: map.spatialReference});
            map.setExtent(extent.expand((expandNum == undefined || expandNum == null) ? 4 : expandNum));
            var center = extent.getCenter();
            if (center) map.centerAt(center);
        }
    };
    /**
     * 常州定制定位
     * @param features
     * @param expandNum
     */
    var locateFeatures_cz = function (features, expandNum) {
        var extent = graphicsUtils.graphicsExtent(features);
        if (extent != null && extent != undefined) {
            extent = lang.mixin(extent, {spatialReference: map.spatialReference});
            map.setExtent(extent.expand((expandNum == undefined || expandNum == null) ? 5 : expandNum + 2));
            var center = extent.getCenter();
            //定位中心点坐标像东南角偏移
            center.x = center.x + 0.0011;
            center.y = center.y - 0.0009222;
            if (center) map.centerAt(center);
        }
    };


    var highlightFeatures_TZ = function (features, autoClear, opacity, layerName, type) {
        var graphicsLyr = null;
        // // timer= setInterval(function () {
        //     arrayUtil.forEach(features, function (g) {
        //             g.show();
        //     });
        // }, frequency);
        if (flashFeature!==null&& flashFeature!==undefined){
            clearInterval(timer);
            flashFeature[0].show();
        }
        if (layerName !== undefined && layerName != null) {
            var graphicsLayer = map.getLayer(layerName);
            if (graphicsLayer !== undefined) {
                graphicsLyr = graphicsLayer;
            } else {
                graphicsLyr = new GraphicsLayer({id: layerName});
                map.addLayer(graphicsLyr);
            }
        } else {
            graphicsLyr = map.graphics;
        }

        // graphicsLyr.clear();
        for (var i in features) {
            _highlightFeature(features[i], autoClear, opacity, graphicsLyr, type);
        }
    };


    /**
     * 定位点
     * @param feature
     */
    var locatePoint = function (feature) {
        if (feature != undefined) {
            map.centerAt(feature.geometry);
        }
    };

    /***
     * 居中要素
     * @param features
     */
    var centerFeatures = function (features) {
        var extent = graphicsUtils.graphicsExtent(features);
        if (extent != null && extent != undefined) {
            var center = extent.getCenter();
            if (center) map.centerAt(center);
        }
    };

    /***
     * 闪烁要素
     * @param features  graphics
     * @param speed     闪烁速度(fast/slow/毫秒)
     * @param completed 完成后回调方法
     */
    var flashFeatures = function (features, speed, completed, duration) {
        flashFeature=null;
        //设置闪烁间隔
        var frequency = 500;
        if ("fast" === speed)
            frequency = 300;
        else if ("slow" === speed)
            frequency = 700;
        else if (!isNaN(speed))
            frequency = Number(speed);
        //设置停止时间
        if (duration == null) {
            duration = frequency * 6;
        }
        timer = setInterval(function () {
            arrayUtil.forEach(features, function (g) {
                if (g.visible === true) {
                    g.hide();
                } else {
                    g.show();
                }
            });
        }, frequency);
        setTimeout(function () {
            clearInterval(timer);
            if (lang.isFunction(completed)) {
                completed.apply();
            }
        }, duration);
        flashFeature=features;
    };

    /**
     *
     */
    var clearMapGraphics = function () {
        map.graphics.clear();
        if (map.infoWindow.isShowing)
            map.infoWindow.hide();
    };

    /**
     *
     * @param title
     * @param content
     * @param pos
     */
    var showMapInfoWindow = function (title, content, pos) {
        if (map.infoWindow.isShowing)
            map.infoWindow.hide();
        map.infoWindow.setTitle(title);
        map.infoWindow.setContent(content);
        map.infoWindow.show(pos);
    };

    var hideMapInfoWindow = function () {
        if (map != undefined && map.infoWindow) {
            map.infoWindow.hide();
        }
    };

    /**
     *
     * @param id
     * @param visible
     */
    var setLayerVisible = function (id, visible) {
        var tmpLayer = map.getLayer(id);
        tmpLayer.setVisibility(visible);
    };
    /**
     *
     * @param srid
     * @param mapLayersConfig
     */
    var reloadMapLayers = function (srid, mapLayersConfig) {
        map.removeAllLayers();
        if (srid != null)
            map.spatialReference = new SpatialReference(srid);
        for (var i in mapLayersConfig) {
            var tmpLayerConfig = mapLayersConfig[i];
            var tmpLayer = null;
            if ("tiled" === tmpLayerConfig.type) {
                tmpLayer = new ArcGISTiledMapServiceLayer(tmpLayerConfig.url, {
                    id: tmpLayerConfig.id
                });
            } else if ("dynamic" === tmpLayerConfig.type) {
                tmpLayer = new ArcGISDynamicMapServiceLayer(tmpLayerConfig.url, {
                    id: tmpLayerConfig.id
                });
            } else if ("feature" == tmpLayerConfig.type) {
                tmpLayer = new FeatureLayer(tmpLayerConfig.url, {
                    mode: FeatureLayer.MODE_SNAPSHOT
                });

            }

            if (tmpLayer != null) {
                tmpLayer.alias = tmpLayerConfig.name;
                tmpLayer.visible = tmpLayerConfig.visible;
                map.addLayer(tmpLayer);
            }
            if (i == 0 && tmpLayer != null) {
                on(tmpLayer, 'load', function (event) {
                    var layer = event.layer;
                    map.setExtent(layer.fullExtent);
                })
            }
        }
    };
    /**
     * 根据id 获取图层
     * @param layerId
     * @returns {*}
     */
    var getLayer = function (layerId) {
        return map.getLayer(layerId);
    };

    /***
     * 根据图层别名获取图层的url地址
     * @param layerAlias
     */
    var getLayerUrlByAlias = function (layerAlias) {
        var url = null;
        arrayUtil.forEach(map.layerIds, function (item) {
            var lyr = map.getLayer(item);
            var layerInfos = lyr.layerInfos;
            if (layerInfos) {
                for (var i = 0; i < layerInfos.length; i++) {
                    var lyrInfo = layerInfos[i];
                    if (lyrInfo.name == layerAlias)
                        url = lyr.url + "/" + lyrInfo.id;
                }
            }
        });
        return url;
    };

    /***
     * remove graohics from map default graphcisLayer
     * @private
     */
    var removeFeatures = function () {
        var fs;
        if (lang.isArray(arguments[0]))
            fs = arguments[0];
        else
            fs = [arguments[0]];
        arrayUtil.forEach(fs, function (item) {
            map.graphics.remove(item);
        });
    };

    /*设置cookie*/
    var setCookie = function (name, value, Days) {
        if (Days == null || Days == '') {
            Days = 300;
        }
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + encodeURIComponent(value) + "; path=/;expires=" + exp.toUTCString();
    };
    /*获取cookie*/
    var getCookie = function (name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return decodeURIComponent(arr[2]);
        else
            return null;
    };
    /***
     * 获取特定比例尺下的地图范围
     * @param scale
     * @private
     */
    var _getExtentForScale = function (scale) {
        return scaleUtils.getExtentForScale(map, scale);
    };
    /***
     * 比较2个extent的大小
     * @param extent1
     * @param extent2
     * @private
     */
    var _compareExtent = function (extent1, extent2) {
        if (esriLang.isDefined(extent1) && esriLang.isDefined(extent2)) {
            var h1 = extent1.getHeight();
            var w1 = extent1.getWidth();
            var h2 = extent2.getHeight();
            var w2 = extent2.getWidth();
            return (h1 - h2) * (w1 - w2);
        }
    };
    return {
        setMap: setMap,
        getLayer: getLayer,
        getLayerUrlByAlias: getLayerUrlByAlias,
        locateFeatures: locateFeatures,
        locateFeatures_cz: locateFeatures_cz,
        locatePoint: locatePoint,
        centerFeatures: centerFeatures,
        highlightFeatures: highlightFeatures,
        highlightFeatures_TZ: highlightFeatures_TZ,
        highlightFeature: _highlightFeature,
        highlightFeaturesWithStyle: highlightFeaturesWithStyle,
        highlightFeaturesWithSymbol: highlightFeaturesWithSymbol,
        removeFeatures: removeFeatures,
        clearMapGraphics: clearMapGraphics,
        showMapInfoWindow: showMapInfoWindow,
        hideMapInfoWindow: hideMapInfoWindow,
        reloadMapLayers: reloadMapLayers,
        setLayerVisible: setLayerVisible,
        refreshMap: refreshMap,
        flashFeatures: flashFeatures,
        getExtentForScale: _getExtentForScale,
        compareExtent: _compareExtent,
        getCookie: getCookie,
        setCookie: setCookie
    };
});
