/**
 *
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:59
 * File:    JsonConverters
 * (c) Copyright gtmap Corp.2015
 */
define(function () {
    "use strict";

    var root;
    root = typeof exports !== "undefined" && exports !== null ? exports : this;

    /**determine if polygon ring coordinates are clockwise. clockwise signifies outer ring, counter-clockwise an inner ring
     or hole. this logic was found at http://stackoverflow.com/questions/1165647/how-to-determine-if-a-list-of-polygon-
     points-are-in-clockwise-order*/
    function ringIsClockwise(ringToTest) {
        var total = 0, i = 0,
            rLength = ringToTest.length,
            pt1 = ringToTest[i],
            pt2;
        for (i; i < rLength - 1; i++) {
            pt2 = ringToTest[i + 1];
            total += (pt2[0] - pt1[0]) * (pt2[1] + pt1[1]);
            pt1 = pt2;
        }
        return (total >= 0);
    }

    /************************************
     * ESRI Rest to GeoJSON Converter
     ************************************/
    function esriConverter() {
        var esriCon = {};

        /*Converts ESRI Rest Geometry to GeoJSON Geometry
         Input is ESRI Rest Geometry Object*/
        function esriGeometryToGcGeometry(esriGeom) {
            var gcGeom,
                i,
                coordinates,
                geomType,
                geomParts,
                polyArray,
                ring;

            //check for x, points, paths, or rings to determine geometry type.
            if (esriGeom) {
                //gcGeom = {};
                if (((esriGeom.x && esriGeom.x !== "NaN") || esriGeom.x === 0) &&
                    ((esriGeom.y && esriGeom.y !== "NaN") || esriGeom.y === 0)) {
                    geomType = "Point";
                    coordinates = [esriGeom.x, esriGeom.y];
                } else if (esriGeom.points && esriGeom.points.length) {
                    geomType = "MultiPoint";
                    coordinates = esriGeom.points;
                } else if (esriGeom.paths && esriGeom.paths.length) {
                    geomParts = esriGeom.paths;
                    if (geomParts.length === 1) {
                        geomType = "LineString";
                        coordinates = geomParts[0];
                    } else {
                        geomType = "MultiLineString";
                        coordinates = geomParts;
                    }
                } else if (esriGeom.rings && esriGeom.rings.length) {
                    //array to hold the individual polygons. A polygon is an outer ring with one or more inner rings
                    //the conversion logic assumes that the Esri json is in the format of an outer ring (clockwise)
                    //followed by inner rings (counter-clockwise) with a clockwise ring signalling the start of a new polygon
                    polyArray = [];
                    geomParts = esriGeom.rings;
                    for (i = 0; i < geomParts.length; i++) {
                        ring = geomParts[i];
                        if (ringIsClockwise(ring)) {
                            //outer ring so new polygon. Add to poly array
                            polyArray.push([ring]);
                        } else if (polyArray.length > 0) {
                            //inner ring. Add as part of last polygon in poly array
                            polyArray[polyArray.length - 1].push(ring);
                        }
                    }
                    if (polyArray.length > 1) {
                        //MultiPolygon. Leave coordinates wrapped in outer array
                        coordinates = polyArray;
                        geomType = "MultiPolygon";
                    } else {
                        //Polygon. Remove outer array wrapper.
                        coordinates = polyArray.pop();
                        geomType = "Polygon";
                    }
                }
                gcGeom = (coordinates && geomType) ? {type: geomType, coordinates: coordinates} : null;
                return gcGeom;
                //gcGeom.coordinates = coordinates;
            }
            return gcGeom;
        }

        /*
         * Converts GeoJSON feature to ESRI REST Feature.
         * Input parameter is an ESRI Rest Feature object
         */
        function esriFeatureToGcFeature(esriFeature) {
            var gcFeat = null,
                prop,
                gcProps,
                attr;
            if (esriFeature) {
                gcFeat = {
                    type: "Feature"
                };
                if (esriFeature.geometry) {
                    gcFeat.geometry = esriGeometryToGcGeometry(esriFeature.geometry);
                    if (esriFeature.geometry.spatialReference) {
                        gcFeat.crs = CreateSR(esriFeature.geometry.spatialReference);
                    }
                }
                if (esriFeature.attributes) {
                    gcProps = {};
                    attr = esriFeature.attributes;
                    for (prop in attr) {
                        gcProps[prop] = attr[prop];
                    }
                    gcFeat.properties = gcProps;
                }
            }
            return gcFeat;
        }


        function CreateSR(sr) {
            var crs = {};
            var crsName;
            if (sr.wkid) {
                crsName = "EPSG:" + sr.wkid;
                crs.type = "name";
                crs.properties = {"name": crsName};
            }
            else {
                crsName = sr.wkt;
                crs.type = "value";
                crs.properties = {"value": trimCommas(crsName, "\"")};
            }
            return crs;
        }

        function trimCommas(value, commaStr) {
            if (!commaStr) {
                commaStr = ",";
            }
            //IE8 trim异常获取
            try{
            value = value.trim();
            }catch(e){

            }
            while (value.length > 0 && value.charAt(0) == commaStr) {
                value = value.substring(1, value.length);
            }
            while (value.length > 0 && value.charAt(value.length - 1) == commaStr) {
                value = value.substring(0, value.length - 1);
            }
            return value;
        }

        /***
         * Converts ESRI Rest Featureset, Feature, or Geometry
         * to GeoJSON FeatureCollection, Feature, or Geometry
         * @param esriObject
         * @returns {*}
         */
        esriCon.toGeoJson = function (esriObject) {
            var outObj, i, esriFeats, gcFeat;
            if (esriObject) {
                if (esriObject.features) {
                    outObj = {
                        type: "FeatureCollection",
                        features: []
                    };
                    esriFeats = esriObject.features;
                    for (i = 0; i < esriFeats.length; i++) {
                        gcFeat = esriFeatureToGcFeature(esriFeats[i]);
                        if (gcFeat) {
                            outObj.features.push(gcFeat);
                        }
                    }
                } else if (esriObject.geometry) {
                    outObj = esriFeatureToGcFeature(esriObject);
                } else {
                    outObj = esriGeometryToGcGeometry(esriObject);
                }
            }
            return outObj;
        };

        return esriCon;
    }

    /************************************************
     * GeoJSON to ESRI Rest Converter
     ************************************************/
    function geoJsonConverter() {
        var gCon = {};
        var defaultWkid = 4610;

        /***
         * compares a GeoJSON geometry type and ESRI geometry type to see if they can be safely
         * put together in a single ESRI feature. ESRI features must only have one
         * geometry type, point, line, polygon
         * @param esriGeomType
         * @param gcGeomType
         * @returns {boolean}
         */
        function isCompatible(esriGeomType, gcGeomType) {
            var compatible = false;
            if ((esriGeomType === "esriGeometryPoint" || esriGeomType === "esriGeometryMultipoint") && (gcGeomType === "Point" || gcGeomType === "MultiPoint")) {
                compatible = true;
            } else if (esriGeomType === "esriGeometryPolyline" && (gcGeomType === "LineString" || gcGeomType === "MultiLineString")) {
                compatible = true;
            } else if (esriGeomType === "esriGeometryPolygon" && (gcGeomType === "Polygon" || gcGeomType === "MultiPolygon")) {
                compatible = true;
            }
            return compatible;
        }

        /***
         * Take a GeoJSON geometry type and make an object that has information about
         * what the ESRI geometry should hold. Includes the ESRI geometry type and the name
         * of the member that holds coordinate information
         * @param gcType
         * @returns {{type: *, geomHolder: *}}
         */
        function gcGeomTypeToEsriGeomInfo(gcType) {
            var esriType,
                geomHolderId;
            if (gcType === "Point") {
                esriType = "esriGeometryPoint";
            } else if (gcType === "MultiPoint") {
                esriType = "esriGeometryMultipoint";
                geomHolderId = "points";
            } else if (gcType === "LineString" || gcType === "MultiLineString") {
                esriType = "esriGeometryPolyline";
                geomHolderId = "paths";
            } else if (gcType === "Polygon" || gcType === "MultiPolygon") {
                esriType = "esriGeometryPolygon";
                geomHolderId = "rings";
            }
            return {
                type: esriType,
                geomHolder: geomHolderId
            };
        }

        /***
         * Convert GeoJSON polygon coordinates to ESRI polygon coordinates.
         * GeoJSON rings are listed starting with a singular outer ring. ESRI
         * rings can be listed in any order, but unlike GeoJSON, the ordering of
         * vertices determines whether it's an outer or inner ring. Clockwise
         * vertices indicate outer ring and counter-clockwise vertices indicate
         * inner ring
         * @param gcCoords
         * @returns {Array}
         */
        function gcPolygonCoordinatesToEsriPolygonCoordinates(gcCoords) {
            var i,
                len,
                esriCoords = [],
                ring;
            for (i = 0, len = gcCoords.length; i < len; i++) {
                ring = gcCoords[i];
                // Exclusive OR.
                if ((i == 0) != ringIsClockwise(ring)) {
                    ring = ring.reverse();
                }
                esriCoords.push(ring);
            }
            return esriCoords;
        }

        /***
         * 转换geojson坐标至esri坐标 不支持geometryCollection
         * @param gcGeom  a GeoJSON geometry object
         * @returns {*}
         */
        function gcCoordinatesToEsriCoordinates(gcGeom) {
            var i,
                len,
                esriCoords;
            if (gcGeom.type === "MultiPoint" || gcGeom.type === "MultiLineString") {
                esriCoords = gcGeom.coordinates;
            } else if (gcGeom.type === "Point" || gcGeom.type === "LineString") {
                esriCoords = [gcGeom.coordinates];
            } else if (gcGeom.type === "Polygon") {
                esriCoords = gcPolygonCoordinatesToEsriPolygonCoordinates(gcGeom.coordinates);
            } else if (gcGeom.type === "MultiPolygon") {
                esriCoords = [];
                for (i = 0, len = gcGeom.coordinates.length; i < len; i++) {
                    var length = gcPolygonCoordinatesToEsriPolygonCoordinates(gcGeom.coordinates[i]).length;
                    if (length > 1) {
                        for (var j = 0; j < length; j++) {
                            esriCoords.push(gcPolygonCoordinatesToEsriPolygonCoordinates(gcGeom.coordinates[i])[j]);
                        }
                    } else {
                        esriCoords.push(gcPolygonCoordinatesToEsriPolygonCoordinates(gcGeom.coordinates[i])[0]);
                    }
                }
            }
            return esriCoords;
        }

        /***
         * Converts GeoJSON geometry to ESRI geometry. The ESRI geometry is
         * only allowed to contain one type of geometry, so if the GeoJSON
         * geometry is a GeometryCollection, then only geometries compatible
         * with the first geometry type in the collection are added to the ESRI geometry
         *
         * @param gcGeom  a GeoJSON geometry object
         * @param sr
         * @returns {{spatialReference: {wkid: number}}|*}
         */
        function gcGeometryToEsriGeometry(gcGeom, sr) {
            var esriGeometry,
                esriGeomInfo,
                gcGeometriesToConvert,
                i,
                g,
                coords;

            //if geometry collection, get info about first geometry in collection
            if (gcGeom.type === "GeometryCollection") {
                gcGeometriesToConvert = [gcGeom.geometries.shift()];
                esriGeomInfo = gcGeomTypeToEsriGeomInfo(gcGeometriesToConvert[0].type);

                //loop through collection and only add compatible geometries to the array
                //of geometries that will be converted
                for (i = 0; i < gcGeom.geometries.length; i++) {
                    if (isCompatible(esriGeomInfo.type, gcGeom.geometries[i].type)) {
                        gcGeometriesToConvert.push(gcGeom.geometries[i]);
                    }
                }
            } else {
                esriGeomInfo = gcGeomTypeToEsriGeomInfo(gcGeom.type);
                gcGeometriesToConvert = [gcGeom];
            }
            //if a collection contained multiple points, change the ESRI geometry
            //type to MultiPoint
            if (esriGeomInfo.type === "esriGeometryPoint" && gcGeometriesToConvert.length > 1) {
                esriGeomInfo = gcGeomTypeToEsriGeomInfo("MultiPoint");
            }
            //处理空间参考
            var wkid = undefined;
            if (sr) {
                wkid = sr.wkid;
            } else {
                if (gcGeom.coordinates.length == 2 && !(gcGeom.coordinates[0] instanceof Array))
                    wkid = getWkidByXcoord(gcGeom.coordinates[0]);
                else
                    wkid = getWkidByXcoord(gcGeom.coordinates[0][0][0]);
            }
            if (wkid === undefined) {
                wkid = defaultWkid;
                console.log("set default wkid to " + defaultWkid);
            }

            //make new empty ESRI geometry object
            esriGeometry = {
                //type: esriGeomInfo.type,
                spatialReference: {
                    wkid: wkid
                }
            };

            //perform conversion
            if (esriGeomInfo.type === "esriGeometryPoint") {
                if (gcGeometriesToConvert[0].coordinates.length === 0) {
                    esriGeometry.x = null;
                    esriGeometry.y = null;
                } else {
                    esriGeometry.x = gcGeometriesToConvert[0].coordinates[0];
                    esriGeometry.y = gcGeometriesToConvert[0].coordinates[1];
                }
            } else {
                esriGeometry[esriGeomInfo.geomHolder] = [];
                for (i = 0; i < gcGeometriesToConvert.length; i++) {
                    coords = gcCoordinatesToEsriCoordinates(gcGeometriesToConvert[i]);
                    for (g = 0; g < coords.length; g++) {
                        esriGeometry[esriGeomInfo.geomHolder].push(coords[g]);
                    }
                }
            }
            return esriGeometry;
        }

        /***
         * 根据坐标点的x坐标值粗略判断wkid
         * @param x
         */
        function getWkidByXcoord(x) {
            if (x != undefined) {
                if (typeof  x === 'object' && x.length > 0)
                    x = x[0];
                var s = Math.round(x).toString();
                switch (s.length) {
                    case 8: {
                        var prefix = s.substr(0, 2);
                        switch (prefix) {
                            case '40':
                                return 2364;
                            case '39':
                                return 2363;
                        }
                    }
                    case 3:
                        return defaultWkid;
                }
            }
            return undefined;
        }

        /***
         * Converts GeoJSON feature to ESRI REST Feature.
         * @param gcFeature  is a GeoJSON Feature object
         * @param sr         optional
         * @returns {*}
         */
        function gcFeatureToEsriFeature(gcFeature, sr) {
            var esriFeat,
                prop,
                esriAttribs;
            if (gcFeature) {
                esriFeat = {};
                if (gcFeature.geometry) {
                    esriFeat.geometry = gcGeometryToEsriGeometry(gcFeature.geometry, sr);
                }
                if (gcFeature.properties) {
                    esriAttribs = {};
                    for (prop in gcFeature.properties) {
                        esriAttribs[prop] = gcFeature.properties[prop];
                    }
                    esriFeat.attributes = esriAttribs;
                }
            }
            return esriFeat;
        }

        /***
         * Converts GeoJSON FeatureCollection, Feature, or Geometry
         * to ESRI Rest Featureset, Feature, or Geometry
         * @param geoJsonObject
         * @param sr
         * @returns {*}
         */
        gCon.toEsri = function (geoJsonObject, sr) {
            var outObj,
                i,
                gcFeats,
                esriFeat;
            if (geoJsonObject) {
                if (geoJsonObject.type === "FeatureCollection") {
                    outObj = {
                        features: []
                    };
                    gcFeats = geoJsonObject.features;
                    for (i = 0; i < gcFeats.length; i++) {
                        esriFeat = gcFeatureToEsriFeature(gcFeats[i], sr);
                        if (esriFeat) {
                            outObj.features.push(esriFeat);
                        }
                    }
                }
                else if (geoJsonObject.type === "Feature") {
                    outObj = gcFeatureToEsriFeature(geoJsonObject, sr);
                }
                else {
                    outObj = gcGeometryToEsriGeometry(geoJsonObject, sr);
                }
            }
            return outObj;
        };
        return gCon;
    }

    return {
        toEsri: function (geoJsonObject, sr) {
            return geoJsonConverter().toEsri(geoJsonObject, sr === undefined ? null : sr);
        },
        toGeoJson: function (esriObject) {
            return esriConverter().toGeoJson(esriObject);
        },
    };
});