/**
 * 地图对比地图
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/18 10:17
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "esri/map",
    "esri/layers/layer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/geometry/Extent",
    "esri/SpatialReference",
    "map/core/SWMTSLayer",
    "dojo/domReady!"], function (declare, lang, arrayUtil, topic, Map, Layer, ArcGISDynamicMapServiceLayer, ArcGISTiledMapServiceLayer,
                                 Extent, SpatialReference, SWMTSLayer) {

    return declare(null, {
        id: 'mini-map-' + Math.floor(Math.random() * 10000),
        map: undefined,
        desc: '',
        config: {},
        loaded: false,
        /**
         * new mini map
         *
         * @param map_div
         * @param option
         * @param callback
         */
        constructor: function (map_div, option, callback) {
            this.init(map_div, option, callback);
        },
        /**
         * init map
         *
         * @param map_div
         * @param option
         * @param callback
         */
        init: function (map_div, option, callback) {
            this.map = new Map(map_div, lang.mixin({
                logo: false,
                slider: false,
                autoResize: true
            }, option));

            _addEventListener(this.map);
            if (callback) callback(this);

            function _addEventListener(map) {
                var cent = false;
                if (map) {
                    map.on('loaded', function () {
                        this.loaded = true;
                    });
                    map.on('extent-change', function (evt) {
                        if(!cent) {
                            cent = true;
                            return;
                        }
                        topic.publish('map-extent-changed', {id: map.id, extent: evt.extent});
                    })
                }
                topic.subscribe('map-extent-changed', function (evt) {
                    if(evt.id == map.id) return ;
                    cent = false;
                    map.setExtent(evt.extent);
                });
            }
        },
        /**
         * get map
         *
         * @returns {*}
         */
        getMap: function () {
            return this.map;
        },
        /**
         * add layer
         *
         * @param layer
         * @param index
         */
        addLayer: function (layer, index) {
            if (!this.map) throw " map isn't init ";
            index = index || -1;
            if (layer == null) return;
            else if (layer instanceof Layer) {
                this.map.addLayer(layer, index);
                return;
            }
            _addSelfLayer(this.map, layer, index);

            function _addSelfLayer(map, layer, index) {
                var newLayer, opt = layer;
                switch (layer.type) {
                    case "dynamic":
                        newLayer = new ArcGISDynamicMapServiceLayer(layer.url, opt);
                        map.addLayer(newLayer, index);
                        break;
                    case "tiled":
                        newLayer = new ArcGISTiledMapServiceLayer(layer.url, opt);
                        map.addLayer(newLayer, index);
                        break;
                    case "swmts":
                        opt.fullExtent = new Extent(opt.xMinExtent, opt.yMinExtent, opt.xMaxExtent, opt.yMaxExtent, new SpatialReference({wkid: 2364}));
                        opt.origin = {x: opt.xMinExtent, y: opt.yMaxExtent};
                        newLayer = new SWMTSLayer(layer.url, opt);
                        map.addLayer(newLayer, index);
                        break;
                    default :
                        console.log(" current layer type " + layer.type + " not supported ! ");
                }
            }
        },
        /**
         * add layers
         *
         * @param layers
         */
        addLayers: function (layers) {
            for (var i = 0; i < layers.length; i++) {
                this.addLayer(layers[i]);
            }
        },

        /**
         * remove layer by id
         *
         * @param layerId
         */
        removeLayerById: function (layerId) {
            this.map.removeLayer(this.map.getLayer(layerId));
        },
        /**
         * remove all
         *
         */
        removeAllLayers: function () {
            this.map.removeAllLayers();
        },
        /**
         * resize map
         *
         */
        resize: function () {
            this.map.resize(true);
            this.map.reposition();
        },
        /**
         * set scale
         * @param scale
         */
        setScale: function (scale) {
            this.map.setScale(scale);
        }

    });
});