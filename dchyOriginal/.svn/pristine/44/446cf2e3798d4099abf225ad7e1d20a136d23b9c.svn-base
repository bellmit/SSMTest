/**
 * wmts service for ogc
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/8/10 17:30
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "esri/map",
    "esri/layers/TiledMapServiceLayer",
    "esri/geometry/Extent",
    "esri/SpatialReference",
    "esri/layers/TileInfo"],function (declare, lang, Map, TiledMapServiceLayer, Extent, SpatialReference, TileInfo) {
    var ogcwmtsLayer=declare('ogc.WMTSLayer',[TiledMapServiceLayer],{
        tileUrl:null,
        constructor: function (url, opt) {

            this.template=opt.template;
            this.tileUrl = url;
            this.layer=opt.layer;
            this.tileMatrixSet = opt.tileMatrixSet||'default028mm';
            this.spatialReference = new SpatialReference({wkid: opt.wkid});
            this.initialExtent = new Extent(opt.extent[0], opt.extent[1], opt.extent[2], opt.extent[3], this.spatialReference);
            this.fullExtent = new Extent(opt.extent[0], opt.extent[1], opt.extent[2], opt.extent[3], this.spatialReference);

            this.tileInfo = new TileInfo({
                "rows": 256,
                "cols": 256,
                "dpi": 96,
                "compressionQuality": 0,
                "origin":{
                    "x": -400,
                    "y": 399.9999999999998
                },
                "spatialReference": {
                    "wkid": opt.wkid
                },
                "lods": opt.lods
            });

            this.loaded = true;
            this.onLoad(this);
        },
        getTileUrl: function (level, row, col) {
            var urlTemplate = (this.tileUrl + "/tile/1.0.0/" + this.layer + "/default/" + this.tileMatrixSet + "/{0}/{1}/{2}").format(level, row, col) + ".png";
            return urlTemplate;
        }

    });
    return ogcwmtsLayer;
});