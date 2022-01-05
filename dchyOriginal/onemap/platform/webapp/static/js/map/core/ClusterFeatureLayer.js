/**
 * 数据聚合工具类
 * @author hanguanghui
 * @Description 数据聚合方式展示摄像头
 * @version V1.0, 2017/4/14
 * @project onemap-parent
 */
define(["dojo/on",
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/geometry/Multipoint",
    "esri/geometry/Point",
    "esri/geometry/Extent",
    "map/core/EsriSymbolsCreator",
    "esri/symbols/TextSymbol",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleLineSymbol",
    "dojo/_base/Color",
    "esri/geometry/screenUtils",
    "esri/geometry/webMercatorUtils"], function (on, declare, lang,arrayUtils, GraphicsLayer, Graphic, Multipoint, Point,Extent,EsriSymbolsCreator, TextSymbol, SimpleMarkerSymbol, SimpleLineSymbol, Color, screenUtils, webMercatorUtils) {

    // global Graphic the ClusterGraphic collection
    var globalGraphics =[];

    var ClusterGraphic = declare(Graphic, {
        /*
         * construct Graphic
         */
        constructor: function (map, graphicslayer, params) {
            console.info("初始化ClusterGraphic");
            this._map = map;
            this._graphicsLayer = graphicslayer;
            //该聚合里包括的graphic
            this._graphics = [];
            this.graFieldList=[];
            //该聚合的几何中心
            this._clusterCenter = null;
            //该聚合的几何范围
            this._clusterExtent = null;
            //该聚合的网格范围(凡是在该范围的graphic都应该被聚合)
            this._gridExtent = null;
            this.lableField = graphicslayer.hasOwnProperty("lableField")?graphicslayer.lableField:"name";
            this.uniqueField = graphicslayer.hasOwnProperty("uniqueField")?graphicslayer.uniqueField:"indexCode";
            //是否含有聚合
            this.isCluster = false;
            //该聚合格网范围的像素值
            this._gridPixel = graphicslayer.hasOwnProperty("gridPixel")?graphicslayer.gridPixel:200;
            this._pushGraphic(params.graphic);
            this._updateGridExtent();
            lang.mixin(this, params.graphic);
        },
        /**
         * 添加到ClusterGraphic
         */
        addGraphic: function (graphic) {
            this._pushGraphic(graphic);
            this._updateGridExtent();
            this.isCluster = true;
            this.setInfoTemplate(null);
        },
        /**
         * 把添加的graphic加入内部的graphic数组和，几何点数组
         */
        _pushGraphic: function (graphic) {
            this.graFieldList.push(graphic.attributes[this.uniqueField]);
            if (!this._multiGeo)//该聚合里所含的graphic的geometry,用来得到该聚合的中心点位
                this._multiGeo = new Multipoint(graphic.geometry.spatialReference);
            this._graphics.push(graphic);
            this._multiGeo.addPoint(graphic.geometry);
            this._updateClusterCenter();
        },
        /**
         * 更新聚合的几何中心
         */
        _updateClusterCenter: function () {
            this._clusterExtent = this._multiGeo.getExtent();
            this._clusterCenter = this._clusterExtent.getCenter();
            this.setGeometry(this._clusterCenter);
        },
        /**
         * 更新该聚合的聚合范围
         */
        _updateGridExtent: function () {
            var scpoint = screenUtils.toScreenGeometry(this._map.extent, this._map.width, this._map.height, this._clusterCenter);
            var xmax = scpoint.x + this._gridPixel / 2;
            var xmin = scpoint.x - this._gridPixel / 2;
            var ymax = scpoint.y + this._gridPixel / 2;
            var ymin = scpoint.y - this._gridPixel / 2;
            this._gridExtent = new Extent(xmin, ymin, xmax, ymax);
        },
        /**
         * 判断该graphic是否在该聚合的聚合范围内
         */
        isGraphicInGridExtent: function (graphic) {
            var scpoint = screenUtils.toScreenGeometry(this._map.extent, this._map.width, this._map.height, graphic.geometry);
            return this._gridExtent.contains(scpoint);
        }

    });

    var ClusterFeatureLayer = declare(GraphicsLayer, {
        constructor: function (params) {
            this.id="clusterFeatureLayer";
            lang.mixin(this, params);
            this.esriGraphics =this._parseEsriGraphics(params);
            this._classifySymols = this._defaultClassifySymols();

            on(this.map, "zoom-start", lang.hitch(this, this._handleMapZoomStart));
            on(this.map, "extent-change", lang.hitch(this, this._handleMapExtentChange));
            on(this, "load", lang.hitch(this, this._handleLayerLoaded));
            this._initLayer();
        },


        /**
         * 将配置文件夹转换为Graphic
         * @param inGraphics
         * @private
         */
        _parseEsriGraphics:function(params){
            var esriGraphics=[];
            arrayUtils.forEach(params.config,function(item){
                var pt = new Point(item.x,item.y,params.map.spatialReference);
                var g = new Graphic(pt,null,item,null);
                esriGraphics.push(g);
            });
            return esriGraphics;
        },

        /**
         * 绘制聚合
         */
        _makeClusters: function () {
            this.clear();
            globalGraphics=[];
            for (var i = 0; i < this.esriGraphics.length; i++) {
                var g = this.esriGraphics[i];
                var mapwkid = this.map.spatialReference.wkid;
                var gwkid = g.geometry.spatialReference.wkid;
                if (mapwkid == gwkid) {
                    this._isGraphicInCurrentMapExtent(g) && this._addGraphicToCluster(g);
                } else {
                    if (mapwkid == 102100)
                        g.geometry = webMercatorUtils.geographicToWebMercator(g.geometry);
                    else if (mapwkid == 4326)
                        g.geometry = webMercatorUtils.webMercatorToGeographic(g.geometry);
                    else
                        throw "请使用102100, 4326 空间参考";
                    this._isGraphicInCurrentMapExtent(g) && this._addGraphicToCluster(g);
                }
            }
            this._add2Map();
        },
        /**
         *  添加聚合到地图
         * @private
         */
        _add2Map:function(){
            var cluLyr = this;
            arrayUtils.forEach(globalGraphics,function(item){
                if(item.isCluster){
                    var att = {isCluster:item.isCluster,graIds:item.graFieldList};
                    var symbol = cluLyr._getClassifySymol(item._graphics.length,cluLyr);
                    var gra = new Graphic(item.geometry,symbol,att,null);
                    cluLyr.add(gra);

                    var textSbl = new TextSymbol(2);
                    textSbl.setColor("#ffffff");
                    textSbl.setOffset(0, -5);
                    textSbl.setText(item._graphics.length);
                    var textGra = new Graphic(item._clusterCenter, textSbl, att, null);
                    cluLyr.add(textGra);
                } else {
                    cluLyr._addGraphic(item,cluLyr);
                }
            });
        },
        /**
         * 添加单个摄像头 Graphic
         * @param gra
         * @param cluLyr
         * @private
         */
        _addGraphic:function(gra,cluLyr){
            //vedio
            gra.attributes.isCluster = false;
            gra.setSymbol(cluLyr.vedioSymbol);
            cluLyr.add(gra);
            //text
            var txtSymbolColor = cluLyr.hasOwnProperty("labelColor")?cluLyr.labelColor:"#32bfef";
            var txtSymbolFontSize = cluLyr.hasOwnProperty("labelFont")?cluLyr.labelFont:12;
            var offset = cluLyr.hasOwnProperty("labelOffset")?cluLyr.labelOffset:10;
            var textSbl = EsriSymbolsCreator.createTextSymbol(gra.attributes[cluLyr.lableField],txtSymbolColor,txtSymbolFontSize,true);
            //浏览器显示矢量图标的问题，增大偏移量
            textSbl.setOffset(0,10);
            var textGra = new Graphic(gra.geometry,textSbl,gra.attributes,null);
            cluLyr.add(textGra);
        },

        /**
         * 过滤聚合Graphic 并添加到地图
         * @param gras
         * @param clusterLayer
         * @private
         */
        _addGraphics:function(gras,clusterLayer){
            arrayUtils.forEach(gras,function(item){
                arrayUtils.forEach(clusterLayer.esriGraphics,function(graItem){
                    var indexCode = graItem.attributes[clusterLayer.uniqueField];
                    if(indexCode == item){
                        clusterLayer._addGraphic(graItem,clusterLayer);
                    }
                });
            });
        },
        /**
         * 更新聚合的点状符号
         */
        _getClassifySymol: function (count,cluLyr) {
            if (count > 1 && count <= 10)
                return cluLyr._classifySymols.less10;
            if (count > 10 && count <= 25 )
                return cluLyr._classifySymols.less25;
            if (count > 25 && count <= 50)
                return cluLyr._classifySymols.less50;
            if (count > 50)
                return cluLyr._classifySymols.over50;
        },
        /**
         * 添加graphic到聚合
         * @param graphic
         * @private
         */
        _addGraphicToCluster: function (graphic) {
            var closestCluster = null;
            for (var i = 0; i < globalGraphics.length; i++) {
                var c = globalGraphics[i];
                if (typeof c.isCluster === "undefined") {
                    continue;
                }
                if (c.isGraphicInGridExtent(graphic)) {
                    closestCluster = c;
                    break;
                }
            }
            if (closestCluster) {
                closestCluster.addGraphic(graphic);
            } else {
                graphic.attributes.type=1;
                var clusterGraphic = new ClusterGraphic(this.map, this, {
                    graphic: graphic
                });
                if (!clusterGraphic.symbol) {
                    clusterGraphic.setSymbol(this._defaultMarkerSymbol());
                }
                globalGraphics.push(clusterGraphic);
            }
        },

        /**
         * 判断graphic是否在当前地图范围内
         * @param graphic
         * @returns {*}
         * @private
         */
        _isGraphicInCurrentMapExtent: function (graphic) {
            return this.map.extent.contains(graphic.geometry);
        },
        /**
         *  添加graphic
         */
        addGraphic: function (graphic) {
            this.esriGraphics.push(graphic);
            this._addGraphicToCluster(graphic);
        },
        /**
         * 图层加载完毕 事件
         */
        _handleLayerLoaded: function (layer) {
            globalGraphics=[];
            this._makeClusters();
        },
        /**
         * 地图缩放
         */
        _handleMapZoomStart: function () {
            this.clear();
        },
        /**
         * 地图范围改变 事件
         */
        _handleMapExtentChange: function () {
            globalGraphics=[];
            this._makeClusters();
        },
       /**
        *  Graphic鼠标单击响应
         */
        _handleMouseClick: function (graphic,clusterLayer) {
            var att = graphic.attributes.graIds;
            this._addGraphics(att,clusterLayer);
        },
        /*
         * graphic默认符号
         */
        _defaultMarkerSymbol: function () {
            return new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 10, /*new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([255, 250, 250, 1]), 1)*/null, new Color([46, 223, 163, 0.5]));
        },
        /*
         * 默认的分级符号
         */
        _defaultClassifySymols: function () {
            return {
                less10: new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 25, null, new Color([46, 223, 163, 0.8])),
                less25: new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 35, null, new Color([0, 191, 255, 0.8])),
                less50: new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 35, null, new Color([30, 144, 255, 0.8])),
                over50: new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 50, null, new Color([255, 0, 0, 0.8]))
            };
        }
    });

    return {
        ClusterGraphic: ClusterGraphic,
        ClusterFeatureLayer: ClusterFeatureLayer
    };
});