/**
 * 图形导入导出核心类
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/23
 * Version: v1.0
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/Deferred",
    "dojo/_base/json",
    "map/core/JsonConverters",
    "esri/tasks/Geoprocessor",
    "map/utils/GeometryUtils",
    "esri/tasks/FeatureSet",
    "esri/lang",
    "esri/graphic",
    "esri/geometry/Geometry",
    "map/core/GeoDataStore",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/GraphicsLayer",
    "layer",
    "static/thirdparty/jquery/jquery.ajaxfileupload"], function (declare, lang, Deferred, dojoJSON, JsonConverters, Geoprocessor, GeometryUtils, FeatureSet,
                                                                 esriLang, Graphic, Geometry, GeoDataStore, RandomUuid, GraphicsLayer) {

    var msgHandler;
    var geoDataStore = GeoDataStore.getInstance();
    var g = declare("GeometryIO", null, {
        expToFile: function (featureSet, expType) {
            switch (expType.toLowerCase()) {
                case g.FORMAT_XLS: {
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet.geometry);
                    }
                    this._expToXls(geometries);
                    break;
                }
                case g.FORMAT_DWG: {
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet.geometry);
                    }
                    this._expToCad(geometries);
                    break;
                }
                case g.FORMAT_SHP: {
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof GraphicsLayer) {
                        geometries = featureSet.graphics;
                    }
                    this._expToShp(geometries);
                    break;
                }
                case g.FORMAT_TXT_BJ: {
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet.geometry);
                    }
                    this._expToBj(geometries);
                    break;
                }
                case g.FORMAT_TXT: {
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet.geometry);
                    }
                    this._expToTxt(geometries);
                    break;
                }
                case g.FORMAT_XLS_JZ:{
                    var geometries = [];
                    if (featureSet instanceof FeatureSet) {
                        for (var i = 0; i < featureSet.features.length; i++) {
                            geometries.push(featureSet.features[i]);
                        }
                    }
                    else if (featureSet instanceof Geometry) {
                        geometries.push(featureSet);
                    }
                    else if (featureSet instanceof Graphic) {
                        geometries.push(featureSet.geometry);
                    }
                    this._expToXls1(geometries);
                    break;
                }
                default: {
                    console.log(expType + " not supported! ");
                    break;
                }
            }
        },

        /***
         *
         * @param geo
         * @private
         */
        //导出excel定制格式
        _expToXls1: function (geo) {
            //地块名称
            if (geo.attributes) {

            }
            var data = [];
            var wkt = null;
            var wkid = null;
            var temprings = [];
            var num;
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                if (geometry) {
                    var sr = geometry.spatialReference;
                    if (isNaN(sr.wkid) && sr.wkt) {
                        wkt = sr.wkid;
                    }
                    wkid = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        temprings.push(rings);
                    }

                }
            }

            var coordinate0 = [];
            var ring0 = temprings[0];
            for (var j = 0; j < rings.length; j++) {
                var ring = ring0[j];
                if (wkid == "4490") {
                    coordinate0 = coordinate0.concat(GeometryUtils.ring2Array1(num,ring, j + 1, wkid));
                } else {
                    coordinate0 = coordinate0.concat(GeometryUtils.ring2Array1(num,ring, j + 1));
                }
            }

            var dateTimePattern = "yyyy-MM-dd";
            data.push(["[属性描述]", "", "", "", "", "", "", "", "", ""]);
            if (wkt) {
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", (new Date()).Format(dateTimePattern), "坐标系", wkt, "几度分带", "3"]);
            } else {
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", (new Date()).Format(dateTimePattern), "坐标系", "2000国家大地坐标系", "几度分带", "3"]);
            }
            if (wkid == "4490") {
                data.push(["投影类型", "高斯克吕格", "计量单位", "米", "带号", "", "精度", "", "转换参数", "0,0,0,0,0,0,0"]);
            } else {
                data.push(["投影类型", "高斯克吕格", "计量单位", "米", "带号", coordinate0[0][3].toString().substr(0, 2), "精度", "0.001", "转换参数", "0,0,0,0,0,0,0"]);
            }
            data.push(["[地块坐标]", "", "", "", "", "", "", "", "", ""]);

            for (var i = 0; i < geo.length; i++) {
                var coordinates = [];
                var rings = temprings[i];
                var dkmc = '';
                if (geo[i].attributes) {
                    dkmc = geo[i].attributes['DKMC'] || geo[i].attributes['地块名称'] || geo[i].attributes['dkmc'] || geo[i].attributes['DKNAME'] ||geo[i].attributes.title|| '';
                }
                num = i;
                for (var j = 0; j < rings.length; j++) {
                    var ring = rings[j];
                    coordinates = coordinates.concat(GeometryUtils.ring2Array1(num,ring, j + 1, wkid, dkmc));
                }
                // data.push([coordinates.length.toString().concat(", , , " + dkmc + ", , , , ,@")]);
                data = data.concat(coordinates);
            }

            var urlVar = {};
            //转json
            urlVar.data = JSON.stringify({"sheet1": data});
            urlVar.fileName = "coords.xls";
            exportFile(urlVar, root + "/geometryService/export/excel");
        },

        _expToXls: function (geo) {
            //地块名称
            if (geo.attributes) {

            }
            var data = [];
            var wkt = null;
            var wkid = null;
            var temprings = [];
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                if (geometry) {
                    var sr = geometry.spatialReference;
                    if (isNaN(sr.wkid) && sr.wkt) {
                        wkt = sr.wkid;
                    }
                    wkid = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        temprings.push(rings);
                    }

                }
            }

            var coordinate0 = [];
            var ring0 = temprings[0];
            for (var j = 0; j < rings.length; j++) {
                var ring = ring0[j];
                if (wkid == "4490") {
                    coordinate0 = coordinate0.concat(GeometryUtils.ring2Array(ring, j + 1, wkid));
                } else {
                    coordinate0 = coordinate0.concat(GeometryUtils.ring2Array(ring, j + 1));
                }
            }

            var dateTimePattern = "yyyy-MM-dd";
            data.push(["[属性描述]", "", "", "", "", "", "", "", "", ""]);
            if (wkt) {
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", (new Date()).Format(dateTimePattern), "坐标系", wkt, "几度分带", "3"]);
            } else {
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", (new Date()).Format(dateTimePattern), "坐标系", "2000国家大地坐标系", "几度分带", "3"]);
            }
            if (wkid == "4490") {
                data.push(["投影类型", "高斯克吕格", "计量单位", "米", "带号", "", "精度", "", "转换参数", "0,0,0,0,0,0,0"]);
            } else {
                data.push(["投影类型", "高斯克吕格", "计量单位", "米", "带号", coordinate0[0][3].toString().substr(0, 2), "精度", "0.001", "转换参数", "0,0,0,0,0,0,0"]);
            }
            data.push(["[地块坐标]", "", "", "", "", "", "", "", "", ""]);

            for (var i = 0; i < geo.length; i++) {
                var coordinates = [];
                var rings = temprings[i];
                for (var j = 0; j < rings.length; j++) {
                    var ring = rings[j];
                    coordinates = coordinates.concat(GeometryUtils.ring2Array(ring, j + 1, wkid));
                }
                var dkmc = '';
                if (geo[i].attributes) {
                    dkmc = geo[i].attributes['DKMC'] || geo[i].attributes['地块名称'] || geo[i].attributes['dkmc'] || geo[i].attributes['DKNAME'] || '';
                }
                data.push([coordinates.length.toString().concat(", , , " + dkmc + ", , , , ,@")]);
                data = data.concat(coordinates);
            }

            var urlVar = {};
            //转json
            urlVar.data = JSON.stringify({"sheet1": data});
            urlVar.fileName = "coords.xls";
            exportFile(urlVar, root + "/geometryService/export/excel");
        },

        /**
         * 导出成txt
         * @param geo
         * @private
         */
        _expToTxt: function (geo) {
            var coords = [];
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                if (geometry) {
                    var wkt = null;
                    var sr = geometry.spatialReference;
                    if (isNaN(sr.wkid) && sr.wkt)
                        wkt = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        var ring = rings[j];
                        coords.push(ring);
                    }
                }
            }
            var urlVar = {};
            urlVar.data = JSON.stringify({"coords": coords});
            urlVar.fileName = "coords.txt";
            urlVar.type = "txt";
            exportFile(urlVar, root + "/geometryService/export/txt");
        },
        /**
         * 导出成报件格式
         * @param geo
         * @private
         */
        _expToBj: function (geo) {
            var data = [];
            var coordinates = [];
            var wkt = null;
            var temprings = [];
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                if (geometry) {
                    var sr = geometry.spatialReference;
                    if (isNaN(sr.wkid) && sr.wkt)
                        wkt = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        var ring = rings[j];
                        temprings.push(ring);
                    }

                }
            }
            for (var i = 0; i < temprings.length; i++) {
                var ring = temprings[i];
                //coordinates = coordinates.concat(GeometryUtils.ring2Array(ring, temprings.indexOf(ring) + 1));
            }

            var dateTimePattern = "YYYY-MM-DD";
            data.push(["[属性描述]", "", "", "", "", "", "", "", "", ""]);
            if (wkt) {
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", new Date(dateTimePattern), "坐标系", wkt, "几度分带", "3"]);
            } else
                data.push(["格式版本号", "", "数据产生单位", "", "数据产生日期", new Date(dateTimePattern), "坐标系", "80国家大地坐标系", "几度分带", "3"]);
            data.push(["投影类型", "高斯克吕格", "计量单位", "米", "带号", coordinates[0][3].toString().substr(0, 2), "精度", "0.001", "转换参数", "0,0,0,0,0,0,0"]);
            data.push(["[地块坐标]", "", "", "", "", "", "", "", "", ""]);
            data.push([coordinates.length.toString().concat(", , , ,@")]);
            data = data.concat(coordinates);
            var urlVar = {};
            urlVar.data = JSON.stringify({"sheet1": data});

            urlVar.fileName = "coords_bj.txt";
            urlVar.type = "txt_bj";
            exportFile(urlVar, root + "/geometryService/export/txt");
        },
        /**
         * 导出成shp
         * @param geo
         * @private
         */
        _expToShp: function (geo) {
            var features = [];
            var sr, wkt;
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                var attr = geo[i].attributes;
                var coordinates = [];
                if (geometry) {
                    sr = geometry.spatialReference;
                    wkt = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        var ring = rings[j];
                        coordinates.push(ring);
                    }
                    if (attr.hasOwnProperty("地块名称")) {
                        lang.mixin(attr, {PRONAME: attr.地块名称});
                    }
                }
                var feature = {
                    "type": "Feature",
                    "geometry": {"type": geometry.type, "coordinates": coordinates},
                    "properties": attr
                };
                features.push(feature);
            }
            if (sr) {
                wkt = sr.wkid;
            }
            var urlVar = {};
            urlVar.geometry = JSON.stringify({"type": "FeatureCollection", "features": features});
            urlVar.sr = wkt;
            console.log(urlVar);
            exportFile(urlVar, root + "/geometryService/export/shp");
        },

        /**
         * 导出成cad格式
         * @param geo
         * @private
         */
        _expToCad: function (geo) {
            var features = [];
            var sr, wkt, proname;
            for (var i = 0; i < geo.length; i++) {
                var geometry = geo[i].geometry;
                var coordinates = [];
                if (geometry) {
                    sr = geometry.spatialReference;
                    wkt = sr.wkid;
                    var rings = geometry.rings;
                    for (var j = 0; j < rings.length; j++) {
                        var ring = rings[j];
                        coordinates.push(ring);
                    }
                    proname = geo[i].attributes.地块名称;

                }
                var feature = {
                    "type": "Feature",
                    "geometry": {"type": geometry.type, "coordinates": coordinates},
                    "properties": {"PRONAME": proname}
                };
                features.push(feature);
            }
            if (sr) {
                wkt = sr.wkid;
            }
            var urlVar = {};
            urlVar.geometry = JSON.stringify({"type": "FeatureCollection", "features": features});
            urlVar.sr = wkt;
            $.post(root + "/geometryService/rest/export/shp", urlVar, function (str_response) {
                var id = str_response;
                var data = {};
                if (window.location.port == null || window.location.port == "") {
                    var shpUrl = "http://" + window.location.hostname + root + "/file/download/" + id;
                } else {
                    var shpUrl = "http://" + window.location.hostname + ":" + window.location.port + root + "/file/download/" + id;
                }
                //全局dwgurl
                var gpUrl = dwgExpUrl;
                data.shpUrl = shpUrl;
                data.gpUrl = gpUrl;
                $.post(root + "/geometryService/rest/export/dwg", data, function (str_response) {
                    var urlstr = str_response;
                    console.log(urlstr.result);
                    window.location = urlstr.result;
                });
            });

        },
        _exportAttInfo: function (data) {
            var urlVar = {};
            //转json
            urlVar.data = JSON.stringify({"sheet1": data});
            urlVar.fileName = "coords.xls";
            exportFile(urlVar, root + "/geometryService/export/excel");
        },
        /**
         * 导入坐标文件
         * @param f file元素
         * @param sr 目标空间参考
         * @param xy x在前y在后为false(即前7后8)，否则为true
         * @returns {*} featureSet
         */
        impFromFile: function (f, sr, xy) {
            var d = new Deferred();
            var fileVal = f.val();
            if (fileVal == "")return;
            var tmp = fileVal.split('.');
            var suffix = tmp[tmp.length - 1];
            var fileName = (tmp[0].split("\\"))[tmp[0].split("\\").length - 1];
            suffix = suffix.toLowerCase();
            layer.close(msgHandler);
            msgHandler = layer.msg('正在导入...', {time: 0});
            switch (suffix) {
                case g.FORMAT_XLS:
                case g.FORMAT_XLSX: {
                    $.ajaxFileUpload({
                        fileElementId: f[0].id,
                        url: root + "/geometryService/excel/upload",
                        type: 'post',
                        dataType: 'json',
                        success: lang.hitch(this, this._uploadXlsOrZipOrTxt, suffix, sr, d),
                        error: function (data, status, e) {
                            alert(e);
                        }
                    });
                    break;
                }
                case g.FORMAT_ZIP: {
                    $.ajaxFileUpload({
                        fileElementId: f[0].id,
                        url: root + "/geometryService/zip/upload",
                        type: 'post',
                        dataType: 'json',
                        success: lang.hitch(this, this._uploadXlsOrZipOrTxt, suffix, sr, d)
                    });
                    break;
                }
                case g.FORMAT_DWG: {
                    var uid = RandomUuid().substr(0, 30);
                    $.ajaxFileUpload({
                        fileElementId: f[0].id,
                        url: root + "/file/upload2/" + uid,
                        type: 'post',
                        dataType: 'text',
                        success: lang.hitch(this, this._onUploadCompleteCad, fileName, suffix, sr, d)
                    });
                    break;
                }
                case g.FORMAT_TXT:
                    $.ajaxFileUpload({
                        fileElementId: f[0].id,
                        url: root + "/geometryService/txt/upload",
                        type: 'post',
                        dataType: 'json',
                        success: lang.hitch(this, this._uploadXlsOrZipOrTxt, suffix, sr, d),
                        error: function (data, status, e) {//服务器响应失败处理函数
                            alert(e);
                        }
                    });
                    break;
                default: {
                    console.log("not supported! ");
                    layer.close(msgHandler);
                    break;
                }
            }
            return d;
        },

        /**
         * 处理上传excel文件或zip文件或txt文件返回结果
         * @param suffix文件后缀
         * @param sr 目标空间参考
         * @param d
         * @param data
         * @private
         */
        _uploadXlsOrZipOrTxt: function (suffix, sr, d, data) {
            layer.close(msgHandler);
            if (data.msg) {
                d.errback(data.msg);
                layer.msg(data.msg, {time: 6000});
                return d;
            }
            var result;
            var info;
            var report = undefined;//报件信息
            switch (suffix) {
                case g.FORMAT_XLS:
                case g.FORMAT_XLSX:
                case g.FORMAT_TXT:
                    if (typeof data == 'string')
                        result = $.parseJSON(data).result;
                    else
                        result = data.result;
                    break;
                case g.FORMAT_ZIP: {
                    var _r = data.result;
                    if (_r.hasOwnProperty("type")) {
                        if ("bj" == _r.type) {
                            result = _r.value.feature;
                            info = _r.value;
                            report = esriLang.filter(_r.value, function (val, prop) {
                                return prop != 'feature';
                            });
                        } else {
                            result = _r.value;
                        }
                    }
                    break;
                }
            }
            var featureSet = JsonConverters.toEsri(JSON.parse(result), sr);
            geoDataStore.push(GeoDataStore.SK_UPLOAD, featureSet.features);
            d.callback({
                fs: featureSet,
                token: suffix,
                report: report == undefined ? undefined : dojoJSON.toJson(report),
                info: info
            });
        },

        /**
         * 处理上传cad文件返回结果
         * @param sr目标空间参考
         * @param d
         * @param data
         * @private
         */
        _onUploadCompleteCad: function (fileName, suffix, sr, d, data) {
            if (data.hasOwnProperty("success") && !data.success) {
                console.log(data.msg);
                return;
            }
            var getGpResultDataComplete = function (result, messages) {
                layer.close(msgHandler);
                var features = result.value;
                var featureSet = new FeatureSet();
                for (var i = 0; i < features.length; i++) {
                    var featuresInner = features[i].features;
                    for (var j = 0; j < featuresInner.length; j++) {
                        featureSet.features.push(featuresInner[j]);
                    }
                }
                geoDataStore.push(GeoDataStore.SK_UPLOAD, featureSet.features);
                d.callback({fs: featureSet, token: suffix, fileName: fileName});
            };
            $.post(root + "/file/ids/" + data, null, function (evt) {
                if (evt.hasOwnProperty("success") && !evt.success) {
                    console.log(evt.msg);
                    return;
                }
                var filePath = window.location.origin + root + "/file/download/" + evt[0];
                if (dwgImpUrl == null || dwgImpUrl == "" || dwgImpUrl == "undefined") {
                    console.warn("未获取到参数dwg.imp.url");
                    return;
                }
                var gp = new Geoprocessor(dwgImpUrl);
                var params = {"input_cad_file": filePath};
                gp.setOutSpatialReference(sr);
                gp.execute(params, function (r) {
                    getGpResultDataComplete(r[0]);
                }, function errorCallback(e) {
                    if (esriLang.isDefined(e)) {
                        var msg = esriLang.substitute({
                            code: e.code,
                            message: e.message
                        }, "解析cad异常！错误码：${code} ,消息: ${message}");
                        console.error(msg);
                    }
                });
            });
        },

        /**
         * 非报件格式
         * @param upStr
         * @param xy x在前y在后为false(即前7后8)，否则为true
         * @returns polygons
         */
        parseBjTxtPolygons: function (upStr, xy) {
            try {
                var obj = GeometryUtils.txtStandardToPolygons(upStr, xy);
                return obj.polygonArray;
            } catch (error) {
                console.log(error);
            }
            return [];
        },
        /**
         * 转换空间参考
         * @param geoStr
         * @param inSR
         * @param outSR
         */
        _convertSpatialReference: function (geoStr, inSR, outSR) {
            var d = new Deferred();
            $.ajax({
                type: "POST",
                url: root + "/geometryService/rest/project",
                data: {geometry: geoStr, inSR: inSR, outSR: outSR},
                error: function (err) {
                    console.error("空间参考转换错误。");
                },
                success: function (data) {
                    var result = $.parseJSON(data);
                    var outSr = {};
                    outSr.wkid = outSR;
                    var featureSet = JsonConverters.toEsri(JSON.parse(result.result), outSr);
                    d.callback(featureSet);
                }
            });
            return d;
        }
    });

    /***
     *数据导出公共方法
     * @param data
     * @param url
     */
    function exportFile(data, url) {
        if (data == "") {
            console.error("无数据!");
            return;
        }
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;
        for (var key in data) {
            var hideInput = document.createElement("input");
            hideInput.type = "hidden";
            hideInput.name = key;
            hideInput.value = data[key];
            tempForm.appendChild(hideInput);
        }
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

    lang.mixin(g, {
        FORMAT_TXT: 'txt',
        FORMAT_XLS: 'xls',
        FORMAT_XLSX: 'xlsx',
        FORMAT_DWG: 'dwg',
        FORMAT_ZIP: 'zip',
        FORMAT_SHP: "shp",
        FORMAT_XLS_JZ:'xls_jz',
        FORMAT_TXT_BJ: "txt_bj"
    });
    return g;
});
