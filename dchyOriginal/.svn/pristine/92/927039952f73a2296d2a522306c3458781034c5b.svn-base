/**
 * Created by wangcheng on 2017/3/28.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "layer",
    "dojo/topic",
    "map/core/BaseWidget"
], function (declare, lang, arrayUtil, layer, topic, BaseWidget) {
    var _compareMaps,_appConfig;

    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            _compareMaps=JSON.parse((compareMaps||getUrlParams().compareMaps));
            _appConfig = this.getAppConfig();
            _init();
        },

        onPause: function () {
        },

        onOpen: function () {
        }
    });

    function _init() {
        //地图加载完毕后进行操作
        EventBus.listener(EventBus.MAIN_MAP_INIT_EXTENT_SET, addContrastMaps);
    }

    /**
     * 添加对比地图
     */
    function addContrastMaps() {
        setTimeout(function () {
            //解析参数配置
            for (var i in _compareMaps) {
                var layerArr = [];
                arrayUtil.forEach(_compareMaps[i], function (item) {
                    var result = arrayUtil.filter(_appConfig.map.operationalLayers, function (opLyr) {
                        return opLyr.id === item;
                    });
                    if (lang.isArray(result) && result.length > 0) {
                        layerArr.push(result[0]);
                    }
                });
                topic.publish(MapTopic.MAP_CONTRAST_ADD, {layers: layerArr});
            }
        }, 300);
    }

    return me;
});