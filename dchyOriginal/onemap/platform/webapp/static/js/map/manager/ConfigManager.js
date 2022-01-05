/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:35
 * File:    ConfigManager
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "esri/request",
    "./MapManager",
    "./WidgetsManager",
    "dojo/domReady!"],function (declare,lang,arrayUtil,esriRequest,MapManager,WidgetsManager) {
    var instance , me = declare(null, {

        appConfig: {},
        contenxt: '',
        constructor: function () {
            //
        },
        /**
         * load config
         */
        loadConfig: function (tplPath) {
            _loadConfig(tplPath);
        },
        /**
         * set app config
         * @param value
         */
        setAppConfig: function (value) {
            appConfig = value;
        },
        /**
         * get app config
         * @returns {*}
         */
        getAppConfig: function () {
            return appConfig;
        },
        /**
         * set context div
         *
         * @param value
         */
        setContext: function (value) {
            context = value;
        },
        /**
         * get context
         *
         * @returns {*}
         */
        getContext: function () {
            return context;
        },
        /***
         *  get config operational layers
         */
        getOperaLayers:function(){
            if(appConfig!=undefined){
                return appConfig.map.operationalLayers;
            }
        },

        /***
         *  get config base layers
         */
        getBaseLayers:function(){
            if(appConfig!=undefined){
                var baseLayers=[];
                var dockWidgets = appConfig.dockWidgets;
                if (dockWidgets.length > 0) {
                    arrayUtil.forEach(dockWidgets, function (item) {
                        if (item.id == "DataList" && item.hasOwnProperty("config") && item.config.hasOwnProperty("baseLayers")) {
                            baseLayers=item.config.baseLayers;
                        }
                    });
                }
                return baseLayers;
            }
        }

    });

    var mapManager = MapManager.getInstance(), widgetsManager = WidgetsManager.getInstance();

    /**
     * load config
     * @param tpl      path to tpl
     * @param callback callback
     * @param error
     * @private
     */
    function _loadConfig(url) {
        var er = new esriRequest({
            url: url
        });
        er.then(function (res) {
            var c = res;
            if (c.success==false) {
                alert(c.msg);
                return;
            }
            try {
                _setConfig(c);
                log("config loaded ");
            } catch (err) {
                log("load map config error [ " + err + " ] ");
            }
        }, function (evt) {
            alert("load tpl config error [" + evt + "]");
        });
    }

    /**
     * set config
     *
     * @param config
     * @private
     */
    function _setConfig(config) {
        instance.setAppConfig(config);
        //此处需要把 logo 等其他属性加入到map的配置中去
        var logo = config.logo;
        var title  = config.title;
        try{
            if (logo != undefined && logo != "" && logo != "/omp/static/img/new/logo.jpg") {
                $(".m-logo").css("background", "url(" + logo + ") no-repeat");
            }
            if (title) {
                $(".nav-title").html(title);
            }
            $(".nav-title").show();
        }catch(e){
            log(e.getMessages());
        }finally{
            if (!config.coordinateVisible)
                $("#coordsInfo").remove();
            try {
                _setMap(config.map,{name:config.name,title:config.title,coordinateVisible:config.coordinateVisible,logoVisible:config.logoVisible,logo:config.logo,dockWidgets:config.dockWidgets});
            }catch (er) {
            }
            _setWidgets(config);
        }
    }

    /**
     * set map config
     *
     * @param mapConfig
     * @private
     */
    function _setMap(mapConfig,options) {
        var $mapDiv = $('<div id="main-map"></div>').appendTo($('#' + instance.getContext()));
        mapManager.initMap(lang.mixin(mapConfig,options), $mapDiv[0]);
    }

    /**
     * set widgets
     *
     * @param config
     * @private
     */
    function _setWidgets(config) {
        widgetsManager.initWidgets(config, instance.getContext());
    }

    /**
     * get instance
     *
     * @returns {*}
     */
    me.getInstance = function () {
        if (instance === undefined) instance = new me();
        return instance;
    };
    return me;
});