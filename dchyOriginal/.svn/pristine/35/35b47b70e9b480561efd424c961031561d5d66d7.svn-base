/**
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/8 8:23
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "esri/geometry/Polygon",
    "esri/geometry/Polyline"
    ], function (declare, Polygon,Polyline) {
    function parseTxtCoords() {
        var txtCon = {};
        txtCon.COORDS_START_TAG = "@";

        /**
         * 处理指标txt文件(其中包含 @标记符号 不支持解析空间参考,坐标串首尾需闭合)
         * @param startRow
         * @param data
         * @param xy x在前y在后为false(即前7后8)，否则为true
         * @returns {Array}
         */
        txtCon.txtBjToPolygons = function (startRow, data, xy) {
            var polygons = [];
            var coords = [];
            try {
                for (startRow; startRow < data.length; startRow++) {
                    var s = data[startRow].trim();
                    if (s.length > 1) {
                        if (s.lastIndexOf(txtCon.COORDS_START_TAG) + 1 == s.length) {
                            polygons = polygons.concat(txtCon.toPolygons(startRow + 1, data, xy));
                            break;
                        }
                        var _arr = s.split(",");
                        var tmp = _arr2point(_arr, 2, xy);
                        var coord = {x: tmp[0], y: tmp[1]};
                        coord.ring = _arr[1];
                        var sIndex = _arr[0];
                        sIndex = sIndex.substr(1, sIndex.length - 1);
                        coord.index = parseInt(sIndex);
                        coords.push(coord);
                    }
                }

                var rings = _groupByField(coords, "ring");
                var polygon = [];
                for (var i in rings) {
                    var ring = rings[i];
                    var r = [];
                    var rs = [];

                    for (var j in ring) {
                        var _c = ring[j];
                        r.push(_c.x);
                        r.push(_c.y);
                    }
                    rs.push(r);
                    polygon.push(rs);
                }
                var p = _parseCoordsString2Polygon(polygon);
                if (p)
                    polygons.push(p);
            }
            catch (error) {
                console.log(error, "错误");
            }
            return polygons;
        }

        /**
         * 处理标准格式的坐标txt文件
         * @param upStr 导入的坐标串
         * @param xy x在前y在后为false(即前7后8)，否则为true
         */
        txtCon.txtStandardToPolygons = function (upStr, xy) {
            var dataArr = upStr.split("\n");
            var ringsArr = [];
            var ring = [];
            var iFirstLine = -1;
            var iRingIndex;
            var geoNumArr = [];
            var resultStr = "";
            var polygonArr = [];
            var obj = {};

            var simpleRingIndex = 1;
            for (var i = 1; i < dataArr.length; i++) {
                var linePreArr = dataArr[i - 1].toString().replace("\r", "").split(",");
                var lineCurArr = dataArr[i].toString().replace("\r", "").split(",");
                if (lineCurArr.length == 3) {
                    // 1,40591224.49962412,3528670.156312702 简单格式
                    linePreArr.push('NULL');
                    lineCurArr.push('NULL');
                }
                if (lineCurArr.length == 4) {
                    // 1,40591224.49962412,3528670.156312702,NULL 简单格式变种
                    linePreArr = [simpleRingIndex.toString()].concat(linePreArr);
                    if (lineCurArr[0] === '1') {
                        simpleRingIndex++;
                    }
                    lineCurArr = [simpleRingIndex.toString()].concat(lineCurArr);

                }
                if (lineCurArr.length != 5) {
                    continue;
                }

                var currPnt = _arr2point(lineCurArr, 2, xy);
                if (i == 1) {
                    var prePnt = _arr2point(linePreArr, 2, xy);
                    var ring1 = [];
                    ring1.push(_arr2point(linePreArr, 2, xy));
                    ring1.push(_arr2point(lineCurArr, 2, xy));
                    var polygon1 = [];
                    polygon1.push(ring1);
                    geoNumArr.push(polygon1);
                }
                else {
                    if (lineCurArr[0].toString().replace("\r", "") == linePreArr[0].toString().replace("\r", "")) {
                        var curPnt = _arr2point(lineCurArr, 2, xy);
                        var nring = geoNumArr[geoNumArr.length - 1];
                        var ringIndex = nring.length - 1;
                        if (lineCurArr[4].toString().replace("\r", "") == linePreArr[4].toString().replace("\r", "")) {
                            (geoNumArr[geoNumArr.length - 1][ringIndex]).push(curPnt);
                        }
                        else {
                            var iring = nring[ringIndex];
                            if ((iring[0]).x == (iring[iring.length - 1]).x && (iring[0]).y == (iring[iring.length - 1]).y) {
                                var aring = [];
                                aring.push(curPnt);
                                (geoNumArr[geoNumArr.length - 1]).push(aring);
                            }
                            else {
                                console.error("请保持坐标串中闭合环首尾坐标一致");
                                return null;
                            }
                        }
                    }
                    else {
                        var ring2 = [];
                        ring2.push(_arr2point(lineCurArr, 2, xy));
                        var polygon2 = [];
                        polygon2.push(ring2);
                        geoNumArr.push(polygon2);
                    }
                }
            }
            if (geoNumArr.length < 1) {
                console.error("坐标串格式有误，请按照标准格式来组织");
                return null;
            }
            for (var j = 0; j < geoNumArr.length; j++) {
                var polygon = new Polygon(geoNumArr[j]);
                polygonArr.push(polygon);
                var polyString = "";
                for (var n = 0; n < polygon.rings.length; n++) {
                    var tmpArr = polygon.rings[n];
                    if ((tmpArr[0]).x != (tmpArr[tmpArr.length - 1]).x || (tmpArr[0]).y != (tmpArr[tmpArr.length - 1]).y) {
                        console.error("请保持坐标串中闭合环首尾坐标一致");
                        return null;
                    }
                    else {
                        polyString += "(";
                        for (var tmp = 0; tmp < polygon.rings.length; tmp++) {
                            var tmpPnt = polygon.rings[tmp];
                            polyString += tmpPnt.x + " " + tmpPnt.y + ",";
                        }
                        polyString = polyString.substring(0, polyString.length - 1);
                        polyString += "),";
                    }
                }
                polyString = polyString.substring(0, polyString.length - 1);
                resultStr += polyString + ",";

            }
            if (resultStr != "") {
                resultStr = resultStr.substring(0, resultStr.length - 1);
                if (polygonArr.length == 1)
                    resultStr = "POLYGON" + resultStr;
                else if (polygonArr.length > 1)
                    resultStr = "MULTIPOLYGON(" + resultStr + ")";
            }

            obj.polygonString = resultStr;
            obj.polygonArray = polygonArr;

            return obj;
        }


        /**
         * 数组转成点数组
         * @param arr 包含一个点坐标的数组
         * @param start 坐标开始的索引
         * @param xy 坐标在数组中的顺序是x,y
         **/
        function _arr2point(arr, start, xy) {
            return xy ? [Number(arr[start]), Number(arr[start + 1])] : [Number(arr[start + 1]), Number(arr[start])];
        }

        /**
         * 简要字段分组
         * @param data
         * @param field
         * @returns {*}
         * @private
         */
        function _groupByField(data, field) {
            if (!data) return null;
            var rings = [];

            for (var i in data) {
                var coord = data[i];
                if (rings[coord[field]] == null) {
                    rings[coord[field]] = [];
                }
                rings[coord[field]].push(coord);
            }
            return rings;
        }

        /**
         * 解析SeShape coords字符串为Polygon
         * @param wkt [[[x,y,x,y,x,y...]]]
         * @return
         *
         */
        function _parseCoordsString2Polygon(xyArray) {
            if (xyArray && xyArray.length > 0) {
                var polygon;
                try {
                    var o = xyArray;
                    if (o.length > 0) {
                        //解析单一多边形，不涉及多边形合集
                        var p = o[0];
                        var rings = [];
                        var innerRing;
                        for (var i in p) {
                            var ip = p[i];
                            innerRing = [];
                            for (var i = 0; i < ip.length - 1; i += 2) {
                                var pnt = [ip[i], ip[i + 1]];
                                innerRing.push(pnt);
                            }
                            rings.push(innerRing);
                        }
                        if (rings.length == 1) {
                            rings = rings[0];
                        }
                        polygon = new Polygon();
                        polygon.addRing(rings);
                    }
                } catch (e) {
                    console.log("解析坐标串异常，" + e.message);
                }
            }
            return polygon;
        }

        /**
         * 将ring转换成array格式（[["J1",1,3566787.884,40519885.724],[点序号,环号,y坐标,x坐标]]）,
         * @param ring
         * @param ringNo
         * @return
         *
         */
        txtCon.ring2Array = function (ring, ringNo, wkid) {
            var result = [];
            for (var i = 0; i < ring.length; i++) {
                var _mp = {};
                _mp.x = ring[i][1];
                _mp.y = ring[i][0];
                var _arr = [];
                var xh = i == ring.length - 1 ? "J1" : "J".concat(i + 1);
                _arr.push(xh);
                _arr.push(ringNo);
                if (wkid == "4490") {
                    _arr.push(_mp.x);
                    _arr.push(_mp.y);
                } else {
                    _arr.push(_mp.x.toFixed(3));
                    _arr.push(_mp.y.toFixed(3));
                }
                result.push(_arr);
            }
            return result;
        }
        //定制格式组织
        txtCon.ring2Array1 = function (num,ring, ringNo, wkid, dkmc) {
            var result = [];
            var length;
            var num=0;
            for (var i = 0; i < ring.length; i++) {
                if(i===ring.length-1){
                    length='0';
                }else {
                    var polyline = new Polyline();
                    polyline.spatialReference={"wkid": 4526, "latestWkid": 4526};
                    polyline.addPath([ring[i], ring[i+1]]);
                    var geo = esri.geometry.webMercatorToGeographic(polyline);
                    length = esri.geometry.geodesicLengths([geo], esri.Units.METERS);
                    length=length[0];
                    length=length.toFixed(1)
                }
                var _mp = {};
                _mp.x = ring[i][1];
                _mp.y = ring[i][0];
                var _arr = [];
                var xh = i == ring.length - 1 ? "J1" : "J".concat(i + 1);
                num=i+1;
                _arr.push(num);
                _arr.push(xh);
                if (wkid == "4490") {
                    _arr.push(_mp.x);
                    _arr.push(_mp.y);
                } else {
                    _arr.push(_mp.x.toFixed(3));
                    _arr.push(_mp.y.toFixed(3));
                }
                _arr.push(length);
                _arr.push(dkmc);
                _arr.push(ringNo);
                result.push(_arr);
            }
            return result;
        };
        return txtCon;
    }

    return {
        txtBjToPolygons: function (startRow, data, xy) {
            return parseTxtCoords().txtBjToPolygons(startRow, data, xy);
        },
        coordsStartTag: parseTxtCoords().COORDS_START_TAG,
        txtStandardToPolygons: function (upStr, xy) {
            return parseTxtCoords().txtStandardToPolygons(upStr, xy);
        },
        ring2Array: function (ring, ringNo,wkid) {
            return parseTxtCoords().ring2Array(ring, ringNo,wkid);
        },
        ring2Array1: function (num,ring, ringNo,wkid,dkmc) {
            return parseTxtCoords().ring2Array1(num,ring, ringNo,wkid,dkmc);
        }
    };
});
