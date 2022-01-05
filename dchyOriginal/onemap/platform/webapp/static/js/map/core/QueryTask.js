/**
 * query task based on esri query task
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 11:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/Deferred",
    "esri/layers/FeatureLayer",
    "esri/tasks/QueryTask",
    "esri/tasks/query",
    "esri/tasks/RelationshipQuery"], function (Deferred,FeatureLayer, EsriQueryTask, Query, RelationshipQuery) {

    /**
     * 要素查询
     * @param _url
     * @param _where
     * @param _geometry
     * @param _outFields
     * @param _map
     * @param callback
     * @param err
     * @private
     */
    var _query = function (_url, _where, _geometry, _outFields, _map, callback, err) {
        var queryTask = new EsriQueryTask(_url);
        var query = new Query();
        if (_where == null) _where = "1=1";
        if (_outFields == null) _outFields = ['*'];
        query.where = _where;
        if (_geometry && _geometry != undefined)query.geometry = _geometry;
        query.outSpatialReference = {wkid: _map.spatialReference.wkid};
        query.returnGeometry = true;
        query.outFields = _outFields;
        try {
            queryTask.execute(query, function (featureSet) {
                    callback(featureSet);
                },
                function (e) {
                    if (err) err("查询异常:");
                });
        } catch (e) {
            console.trace("warning: relative query fail");
        }
    };

    /***
     * deferred方式query
     * @param _url
     * @param _where
     * @param _geometry
     * @param _outFields
     * @param _map
     * @private
     */
    var _deferredQuery = function (_url, _where, _geometry, _outFields, _map) {
        var d = new Deferred();
        var queryTask = new EsriQueryTask(_url);
        var query = new Query();
        if (_where == null) _where = "1=1";
        if (_outFields == null || _outFields == undefined) _outFields = ['*'];
        query.where = _where;
        if (_geometry && _geometry != undefined) query.geometry = _geometry;
        if (_map.spatialReference != undefined && _map.spatialReference.wkid != null) {
            query.outSpatialReference = {wkid: _map.spatialReference.wkid};
        }
        query.returnGeometry = true; 
        query.outFields = _outFields;
        try {
            queryTask.execute(query, function (featureSet) {
                    d.callback({url: _url, featureSet: featureSet});
                },
                function (e) {
                    d.errback(new Error('查询异常:[' + e + ']'));
                });
        } catch (e) {
            d.errback(new Error('查询异常:[' + e + ']'));
        }
        return d;
    };
    /**
     * 查询要素数量
     * @param _url
     * @param _where
     * @param _geometry
     * @param callback
     * @param err
     * @private
     */
    var _queryForCount = function (_url, _where, _geometry, callback, err) {
        var queryTask = new EsriQueryTask(_url);
        var query = new Query();
        if (_where == null) _where = "1=1";
        query.where = _where;
        if (_geometry && _geometry != undefined)query.geometry = _geometry;
        queryTask.executeForCount(query, function (count) {
            callback(count);
        }, function (error) {
            if (err)err('查询异常');
            console.log(error);
        });
    };

    /**
     * 查询符合条件的要素的object id集合
     * @param _url
     * @param _where
     * @param _geometry
     * @param callback
     * @param err
     * @private
     */
    var _queryForIds = function (_url, _where, _geometry, callback, err) {
        var queryTask = new EsriQueryTask(_url);
        var query = new Query();
        if (_where == null) _where = "1=1";
        query.where = _where;
        if (_geometry && _geometry != undefined)query.geometry = _geometry;
        queryTask.executeForIds(query, function (results) {
            callback(results);
        }, function (error) {
            if (err)err('查询异常');
            console.log(error);
        });
    };

    /**
     * 查询关联要素
     * @param data
     * @param callback
     * @param err
     * @private
     */
    var _queryRelation = function (data, callback, err) {
        var layer = new FeatureLayer(data.url,
            {
                mode: FeatureLayer.MODE_SELECTION
            });

        var relatedQuery = new RelationshipQuery();
        relatedQuery.relationshipId = data.relationshipId;
        relatedQuery.objectIds = [data.objectIds];
        relatedQuery.outSpatialReference = new SpatialReference(data.wkid);
        relatedQuery.outFields = data.fields;
        relatedQuery.returnGeometry = true;

        layer.queryRelatedFeatures(relatedQuery,
            function (result) {
                if (callback)callback(result);
            },
            function (e) {
                if (err)err(e);
            });
    };
    return {
        query: _query,
        deferredQuery:_deferredQuery,
        queryForCount: _queryForCount,
        queryForIds: _queryForIds,
        queryRelation: _queryRelation
    };
});