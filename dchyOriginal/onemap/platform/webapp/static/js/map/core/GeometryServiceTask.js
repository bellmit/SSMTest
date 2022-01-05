/**
 * 地理处理服务核心类
 * Created by yxf on 2016/1/21.
 */
define(["dojo/_base/lang",
    "dojo/on",
    "esri/SpatialReference",
    "esri/tasks/GeometryService",
    "esri/tasks/BufferParameters",
    "esri/tasks/AreasAndLengthsParameters",
    "esri/tasks/LengthsParameters",
    "esri/tasks/ProjectParameters",
    "esri/geometry/Geometry",
    "esri/symbols/SimpleFillSymbol",
    "map/manager/ConfigManager"], function (lang, on, SpatialReference, GeometryService, BufferParameters, AreasAndLengthsParameters,
                                            LengthsParameters, ProjectParameters, Geometry, SimpleFillSymbol, ConfigManager) {

    var _url = ConfigManager.getInstance().getAppConfig().geometryService.url;
    var _geometryService = undefined;
    var _bufferOption = {
        unit: GeometryService.UNIT_METER, bufferSpatialReference: new SpatialReference({wkid: 2364}),
        outSpatialReference: new SpatialReference({wkid: 2364})
    };
    var _measureOption = {
        lengthUnit: GeometryService.UNIT_METER,
        calculationType: 'preserveShape',
        areaUnit: GeometryService.UNIT_SQUARE_METERS
    };
    var DEFAULT_BUFFER_DISTANCE = 2000;//默认缓冲距离2km

    /***
     * 投影操作
     * @param geometry
     * @param outSr
     * @param callback
     * @param option
     * @private
     */
    var _project = function (geometry, outSr, transWkid, callback, option) {
        if (_geometryService == undefined) {
            _geometryService = new GeometryService(_url);
        }
        on.once(_geometryService, 'project-complete', lang.hitch(this, callback));
        on.once(_geometryService, 'error', onErrorHandler);
        var projectParams = new ProjectParameters();
        projectParams.geometriees = lang.isArray(geometry) ? geometry : [geometry];
        projectParams.outSR = outSr;
        projectParams.transformation = {wkid: transWkid};
        lang.mixin(projectParams, option);
        _geometryService.project(projectParams);
    };
    /***
     * buffer 操作
     * @param geometry
     * @param distance
     * @param callback
     * @param option
     * @private
     */
    var _buffer = function (geometry, distance, callback, option) {
        if (_geometryService == undefined) {
            _geometryService = new GeometryService(_url);
        }
        if (distance == undefined)
            distance = DEFAULT_BUFFER_DISTANCE;
        on.once(_geometryService, 'buffer-complete', lang.hitch(this, callback));
        on.once(_geometryService, 'error', onErrorHandler);

        lang.mixin(_bufferOption, option);
        var params = new BufferParameters();
        params.geometries = lang.isArray(geometry) ? geometry : [geometry];
        params.distances = lang.isArray(distance) ? distance : [distance];
        params.unit = _bufferOption.unit;
        params.bufferSpatialReference = _bufferOption.bufferSpatialReference;
        params.outSpatialReference = _bufferOption.outSpatialReference;
        _geometryService.buffer(params);
    };

    /***
     * lengths
     * @param geometry
     * @param callback
     * @private
     */
    var _getLengths = function (geometry, callback, option) {
        if (_geometryService === undefined) {
            _geometryService = new GeometryService(_url);
        }
        on.once(_geometryService, "lengths-complete", lang.hitch(this, callback));
        on.once(_geometryService, "error", onErrorHandler);

        lang.mixin(_measureOption, option);
        var lengthParams = new LengthsParameters();
        lengthParams.lengthUnit = _measureOption.lengthUnit;
        lengthParams.calculationType = _measureOption.calculationType;
        lengthParams.polylines = lang.isArray() ? geometry : [geometry];
        _geometryService.lengths(lengthParams);
    };

    /***
     * areas & lengths
     * @param geometry
     * @param callback
     * @private
     */
    var _getAreasAndLengths = function (geometry, callback, option) {
        if (_geometryService === undefined) {
            _geometryService = new GeometryService(_url);
        }
        on.once(_geometryService, "lengths-complete", lang.hitch(this, callback));
        on.once(_geometryService, "error", onErrorHandler);

        lang.mixin(_measureOption, option);
        var areasAndLengthParams = new AreasAndLengthsParameters();
        areasAndLengthParams.areaUnit = _measureOption.areaUnit;
        areasAndLengthParams.lengthUnit = _measureOption.lengthUnit;
        areasAndLengthParams.calculationType = _measureOption.calculationType;
        _simplify(geometry, function (geos) {
            areasAndLengthParams.polygons = geos;
            _geometryService.areasAndLengths(areasAndLengthParams);
        });
    };

    /***
     * simplify 操作
     * @param geometry
     * @param callback
     * @private
     */
    var _simplify = function (geometry, callback) {
        _geometryService.simplify(lang.isArray(geometry) ? geometry : [geometry], function (simplifiedGeometries) {
            if (callback) callback(simplifiedGeometries);
        });
    };

    /***
     * buffer error
     * @param ex
     */
    function onErrorHandler(ex) {
        console.error(ex.message);
    }

    return {
        project: _project,
        buffer: _buffer,
        simplify: _simplify,
        length: _getLengths,
        areaAndLength: _getAreasAndLengths
    };
});