/**
 * share data store
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/4/19 10:26
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/Evented",
    "dojo/_base/Deferred"], function (declare, lang, arrayUtil, Evented, Deferred) {

    //事件
    var DATA_EVENT_CHANGE = "onChange";

    var SHARE_KEY = {
        SK_COMMON: "features",
        SK_LOC: "features_from_location",
        SK_IDENTIFY: "features_from_identify",
        SK_QUERY: "feature_from_query",
        SK_REGION: "region_info",
        SK_UPLOAD: "features_from_upload"
    };
    var top = {}, cache = top['_CACHE'] || {};
    top['_CACHE'] = cache;

    var instance, me = declare([Evented], {
        /***
         *  添加共享数据
         * @param key
         * @param data
         */
        push: function (key, data) {
            cache[key] = data;
            this.emit(DATA_EVENT_CHANGE, {type: key, features: data});
        },
        /***
         * 获取共享数据
         * @param key
         */
        fetch: function (key) {
            var deferred = new Deferred();
            deferred.callback({type: key, features: cache[key]});
            return deferred;
        },
        /***
         * 手动清空共享数据
         * @param key
         */
        remove: function (key) {
            if (cache && cache[key]) delete cache[key];
            this.emit(DATA_EVENT_CHANGE, {type: key, features: undefined});
        }
    });

    lang.mixin(me, SHARE_KEY);

    me.getInstance = function () {
        if (instance === undefined) {
            instance = new me();
        }
        return instance;
    };
    return me;
});