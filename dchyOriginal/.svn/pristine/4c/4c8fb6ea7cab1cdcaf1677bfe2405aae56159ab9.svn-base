/**
 * ogs wmts service for supermap
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/8/10 14:32
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "esri/map",
    "esri/layers/TiledMapServiceLayer",
    "esri/geometry/Extent",
    "esri/SpatialReference",
    "esri/layers/TileInfo"], function (declare, lang, Map, TiledMapServiceLayer, Extent, SpatialReference, TileInfo) {

    var wmtsLayer = declare('SWMTSLayer', [TiledMapServiceLayer], {

        tileUrl: null,
        version: '1.0.0',
        constructor: function (url, option) {
            this.tileUrl = url;
            this.identifier = option.layer || "";
            this.imageFormat = option.imageFormat || "image/png";
            this.tileMatrixSet = option.tileMatrixSet || "";
            this.spatialReference = new SpatialReference(option.wkt ? {wkt: option.wkt} : {wkid: option.wkid || 2364});

            this.initialExtent = option.fullExtent;
            this.fullExtent = option.fullExtent;

            this.tileInfo = new TileInfo({
                "rows": 256,
                "cols": 256,
                "dpi": option.dpi || 96,
                "format": option.imageFormat || "image/png",
                "compressionQuality": 0,
                "origin": option.origin || {
                    "x": 40456629.299342358,
                    "y": 3564390.728841688
                },
                "spatialReference": option.wkt ? {"wkt": option.wkt} : {"wkid": option.wkid || 2364},
                "lods": option.lods || [
                    {
                        "level": 0,
                        "scale": 288895.85,
                        "resolution": 76.43717985352637
                    },
                    {
                        "level": 1,
                        "scale": 144447.93,
                        "resolution": 38.2185912496825
                    },
                    {
                        "level": 2,
                        "scale": 72223.96,
                        "resolution": 19.10929430192194
                    },
                    {
                        "level": 3,
                        "scale": 36111.98,
                        "resolution": 9.55464715096097
                    },
                    {
                        "level": 4,
                        "scale": 18055.99,
                        "resolution": 4.777323575480485
                    },
                    {
                        "level": 5,
                        "scale": 9028,
                        "resolution": 2.3886631106595546
                    },
                    {
                        "level": 6,
                        "scale": 4514,
                        "resolution": 1.1943315553297773
                    },
                    {
                        "level": 7,
                        "scale": 2257,
                        "resolution": 0.5971657776648887
                    },
                    {
                        "level": 8,
                        "scale": 1128.5,
                        "resolution": 0.2985828888324443
                    }
                ]
            });
            this.loaded = true;
            this.onLoad(this);
        },
        getTileUrl: function (level, row, col) {
            var urlTemplate = this.tileUrl +
                "?SERVICE=WMTS&VERSION=" + this.version +
                "&REQUEST=GetTile" +
                "&LAYER=" + this.identifier +
                "&STYLE=default" +
                "&FORMAT=" + this.imageFormat +
                "&TILEMATRIXSET=" + this.tileMatrixSet +
                "&TILEMATRIX=" +
                level + "&TILEROW=" + row + "&TILECOL=" + col;
            return urlTemplate;
        }
    });
    return wmtsLayer;
});