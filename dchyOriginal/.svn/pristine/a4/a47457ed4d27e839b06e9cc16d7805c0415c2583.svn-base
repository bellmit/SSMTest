/**
 * sector  extends arcgis  polygon
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/26
 * Version: v1.0
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/has",
    "esri/kernel",
    "esri/lang",
    "esri/geometry/Geometry",
    "esri/geometry/Point",
    "esri/geometry/Polygon",
    "esri/geometry/geodesicUtils",
    "esri/geometry/webMercatorUtils",
    "esri/units",
    "esri/WKIDUnitConversion"], function (declare, lang,q,r,esriLang, Geometry, Point, Polygon, geodesicUtils, webMercatorUtils, units, WKIDUnitConversion) {

    var s = {type: "sector"};
    var h= declare([Polygon], {
        _unitToMeters: {
            esriCentimeters: 0.01,
            esriDecimeters: 0.1,
            esriFeet: 0.3048,
            esriInches: 0.0254,
            esriKilometers: 1E3,
            esriMeters: 1,
            esriMiles: 1609.34,
            esriMillimeters: 0.0010,
            esriNauticalMiles: 1852,
            esriYards: 0.9144,
            esriDecimalDegrees: 111320
        },
        constructor: function (a, c) {
            lang.mixin(this, s);
            var b;
            a.center ? b = a : (b = c || {}, b.center = a);
            this.center = lang.isArray(b.center) ? new Point(b.center[0], b.center[1]) : b.center;
            this.radius = b.radius || 1E3;
            this.radiusUnit = b.radiusUnit || units.METERS;
            this.geodesic = !0 === b.geodesic ? !0 : !1;
            this.numberOfPoints = b.numberOfPoints || 60;
            this.startAngle = b.startAngle || 0;
            this.endAngle = b.endAngle || 60;
            this._init();
        },
        _init: function () {
            this.arcCenter=[]; //扇形弧的中心点
            this.rings = [];
            this._ring = 0;
            var a = this.radius * this._unitToMeters[this.radiusUnit], c = this._srType(this.center.spatialReference);
            if (this.geodesic) {
                var b;
                switch (c) {
                    case "webMercator":
                        b = webMercatorUtils.webMercatorToGeographic(this.center);
                        break;
                    case "projected":
                        console.error("Creating a geodesic circle requires the center to be specified in web mercator or geographic coordinate system");
                        break;
                    case "geographic":
                        b = this.center
                }
                a = this._createGeodesicSector(b, a, this.startAngle, this.endAngle, this.numberOfPoints);
                "webMercator" === c && (a = webMercatorUtils.geographicToWebMercator(a))
            } else {
                var d;
                "webMercator" === c || "projected" === c ? d = a / this._convert2Meters(1, this.center.spatialReference) : "geographic" === c && (d = a / this._unitToMeters.esriDecimalDegrees);
                a = this._createPlanarSector(this.center, d, this.startAngle, this.endAngle, this.numberOfPoints)
            }
            this.spatialReference = a.spatialReference;
            this.addRing(a.rings[0]);
            this.verifySR();
        },

        /***
         *
         */
        getPolygon:function(){
            var a = new Polygon(this.spatialReference);
            a.addRing(this.rings[0]);
            return a;
        },
        toJson: function () {
            return this.inherited(arguments)
        },
        /***
         *
         * @returns {{}|*}
         */
        getArcCenter:function(){
            return this.arcCenter;
        },
        /***
         *
         * @param c
         * @param r
         * @param s
         * @param e
         * @param n
         * @private
         */
        _createGeodesicSector: function (c, r, s, e, n) {
            var f = this._getPoints(c, r, s, e, n);
            var a = new Polygon(c.spatialReference);
            a.addRing(f);
            return a;
        },
        /***
         *
         * @param c
         * @param r
         * @param s
         * @param e
         * @param n
         * @private
         */
        _createPlanarSector: function (c, r, s, e, n) {
            var f = this._getPoints(c, r, s, e, n);
            var a = new Polygon(c.spatialReference);
            a.addRing(f);
            return a;
        },

        _srType: function (a) {
            return a.isWebMercator() ? "webMercator" : esriLang.isDefined(WKIDUnitConversion[a.wkid]) || a.wkt && 0 === a.wkt.indexOf("PROJCS") ? "projected" : "geographic"
        },

        _convert2Meters: function (a, c) {
            var b;
            if (esriLang.isDefined(WKIDUnitConversion[c.wkid]))b = WKIDUnitConversion.values[WKIDUnitConversion[c.wkid]]; else {
                b = c.wkt;
                var d = b.lastIndexOf(",") + 1, e = b.lastIndexOf("]]");
                b = parseFloat(b.substring(d, e))
            }
            return a * b
        },
        /***
         *
         * @param c
         * @param r
         * @param s  start angle
         * @param e   end angle
         * @param e
         * @param n
         * @returns {Array}
         * @private
         */
        _getPoints: function (c, r, s, e, n) {
            var sin, cos, x, y, angle;
            var pnts = new Array();
            pnts.push([c.x, c.y]);
            var cx = c.x+ r * Math.sin((s+(e-s)*0.5) * Math.PI / 180);
            var cy = c.y+ r * Math.cos((s+(e-s)*0.5) * Math.PI / 180);
            this.arcCenter = [[c.x, c.y],[cx,cy]];
            for (var i = 0; i <= n; i++) {
                angle = s + (e - s) * i / n;
                sin = Math.sin(angle * Math.PI / 180);
                cos = Math.cos(angle * Math.PI / 180);
                x = c.x + r * sin;
                y = c.y + r * cos;
                pnts[i] = [x, y];
            }
            var point = pnts;
            point.push([c.x, c.y]);
            return point;
        }

    });

    q("extend-esri") && lang.setObject("geometry.Sector", h, r);
    return h;

});