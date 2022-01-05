/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:35
 * File:    MapManager
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/topic",
    "dojo/_base/array",
    "map/MainMap",
    "dojo/domReady!"], function (declare,topic, arrayUtil, MainMap) {
    var instance, me = declare(null, {
        /**
         * map config
         */
        mapConfig: {},
        /**
         * map div
         */
        mapDiv: '',
        /**
         * main map
         */
        mainMap: undefined,
        /**
         * current map
         */
        currentMap: undefined,
        /**
         *
         */
        constructor: function () {
        },
        /**
         * init map
         * @param value
         */
        initMap: function (value, _mapDiv) {
            mapConfig = value;
            mapDiv = _mapDiv;
            _initMainMap();
        },
        /**
         * get map config
         * @returns {*}
         */
        getMapConfig: function () {
            return mapConfig;
        },
        /**
         * get map div
         * @returns {*}
         */
        getMapDiv: function () {
            return mapDiv;
        },
        /**
         * set main map
         * @param map
         */
        setMainMap: function (map) {
            mainMap = map;
            currentMap = map;
        },
        /**
         * get main map
         * @returns {*}
         */
        getMainMap: function () {
            return mainMap;
        },
        /**
         * get current map
         *
         * @returns {*}
         */
        getCurrentMap: function () {
            try {
                return currentMap ? currentMap : mainMap;
            }catch (e) {
                return null;
            }
        },

        /**
         * set current map
         */
        setCurrentMap: function (val) {
            this.currentMap = val;
        },
        /**
         * resize map content
         *
         * @param value
         */
        resizeMapContent: function (value) {
            _resizeMapContent(value);
        }

    });

    /**
     *
     */
    EventBus.listener(EventBus.CONTENT_RESIZE, function () {
        instance.getCurrentMap().resize();
    });

    /**
     * 初始化main map
     * @private
     */
    function _initMainMap() {
        log("地图初始化...");
        //监听地图加载完成事件
        EventBus.listener(EventBus.MAIN_MAP_INITIALIZED, function () {
            log("地图初始化完成!");
            _resizeMapContent();
            initContrast();
        });
        //监听地图功能模块全部加载事件
        EventBus.listener(EventBus.WIDGETS_LOADED, function () {
            setTimeout(function () {
                //初始化地图范围或初始比例尺
                var mc = instance.getMapConfig();
                var initExtent = mc.initExtent;
                var defaultScale = Number(mc.defaultScale);
                var locationFlag=true;
                if (defaultScale > 0) {
                    MainMap.setScale(defaultScale);
                    locationFlag=false;
                    EventBus.trigger(EventBus.MAIN_MAP_INIT_EXTENT_SET);
                } else if (initExtent) {
                    try {
                        MainMap.setExtent(mc.initExtent).then(function () {
                            log("地图初始范围设置完成!");
                            EventBus.trigger(EventBus.MAIN_MAP_INIT_EXTENT_SET);
                        });
                    }catch (er) {
                        EventBus.trigger(EventBus.MAIN_MAP_INIT_EXTENT_SET);
                    }
                }
            }, 100);
        });
        var opt = {};
        if (instance.getMapConfig().lods) opt.lods = instance.getMapConfig().lods;
        MainMap.init(instance.getMapDiv(), opt);
        instance.setMainMap(MainMap);
        addOptLayer();
        addBaseLayer();
        log('地图服务初始化完成!');
    }

    /**
     * 初始化对比
     */
    function initContrast() {
        //打开对比地图
        if (mapConfig.hasOwnProperty("dockWidgets")) {
            var dockWidgets=mapConfig.dockWidgets;
            arrayUtil.forEach(dockWidgets, function (dockWidget) {
                if (dockWidget.hasOwnProperty("url")&&dockWidget.url=="DataList"&&dockWidget.hasOwnProperty("config")){
                    var dataListConfig=dockWidget.config;
                    if (dataListConfig.hasOwnProperty("contrast")){
                        var contrast=dataListConfig.contrast;
                        if (contrast != null &&contrast.layers!=null&& contrast.layers.length > 0) {
                            arrayUtil.forEach(contrast.layers, function (items) {
                                if (items != null && items.children !=null && items.children.length > 0) {
                                    var arr = [];
                                    arrayUtil.forEach(items.children, function (item) {
                                        arr.push(item);
                                    });
                                    if (arr.length>0){
                                        topic.publish(MapTopic.MAP_CONTRAST_ADD, {layers: arr,name:items.name,contrastName:contrast.name});
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        _resizeMapContent();
    }

    /**
     * resize map content
     */
    function _resizeMapContent(leftOffset) {
        /**
         * set map size
         */
        $(instance.getMapDiv()).height($(instance.getMapDiv()).parent().height());
        $(instance.getMapDiv()).children("[id$='_root']").height($(instance.getMapDiv()).height());
        MainMap.resize();
    }

    /**
     * add base layer
     */
    function addBaseLayer() {
        log('初始化 base maps');
        var baseLayers = [];
        var dockWidgets = instance.getMapConfig().dockWidgets;
        if (dockWidgets!=undefined &&dockWidgets.length > 0) {
            arrayUtil.forEach(dockWidgets, function (item) {
                if (item.id == "DataList" && item.hasOwnProperty("config") && item.config.hasOwnProperty("baseLayers")) {
                    baseLayers=item.config.baseLayers;
                }
            });
        }
        MainMap.initBaseMaps(baseLayers);
    }

    /**
     * add opt layer
     */
    function addOptLayer() {
        log('初始化 operational layers');
        MainMap.addLayers(instance.getMapConfig().operationalLayers);
    }

    /**
     * get instance
     *
     * @returns {*}
     */
    me.getInstance = function () {
        if (instance === undefined) {
            instance = new me();
        } else {
            //
        }
        return instance;
    };

    return me;
});