/**
 *
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/12/3 19:25
 * File:    Index
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "esri/lang",
    "map/manager/ConfigManager",
    "map/core/BaseWidget",
    "map/MainMap",
    "jqueryUI",
    "static/thirdparty/jquery/jquery-ui-slider-pips.min",
    "css!static/thirdparty/jquery/jquery-ui-slider-pips.css",
    "css!static/thirdparty/jquery/jquery-ui.min"], function (declare, lang,arrayUtil, topic,esriLang,ConfigManager,BaseWidget,MainMap) {

    var __map,minZoomLevel,maxZoomLevel,_initExtent,_ioConfig,configManager = ConfigManager.getInstance();
    var Nav = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            _ioConfig = this.getConfig();
            initComponent();
            addListener();
        }
    });


    var lods = [];
    var zoomFactor;
    var $zoomInControl,$zoomOutControl,$zoomSlider;
    var sliderCreated=false;
    var layers;
    var layerTypes;

    /**
     * 初始化控件外观
     * 根据当前地图的lods进行初始化slider
     * 若没有lods 则只显示放大缩小按钮
     * @private
     */
    function initComponent() {

        //初始化dom变量
        $zoomInControl = $(".map-control.zoom-in");
        $zoomOutControl = $(".map-control.zoom-out");
        $zoomSlider = $('.zoom-slider');
        var $zoomControl=$(".zoom-control");
        layers = _ioConfig.layers;
        layerTypes = configManager.getAppConfig().map.operationalLayers;
        initSlider();
        EventBus.listener(EventBus.MAIN_MAP_INITIALIZED,lang.hitch(this,initSlider));
        //添加对比地图后隐藏控件
        topic.subscribe(MapTopic.MAP_CONTRAST_ADD,function(){
            $zoomControl.hide('fade',400);
        });
        //移除所有的对比地图后显示控件
        topic.subscribe(MapTopic.MAP_MAIN_CHECKED, function (evt) {
            if (esriLang.isDefined(evt) && esriLang.isDefined(evt.isSingle)) {
                $zoomControl.show('fade', 400);
            }
        });
    }
    /***
     * 初始化slider
     */
    function initSlider(){
        _initExtent = __map.extent;
        minZoomLevel = __map.getMinZoom();
        maxZoomLevel = __map.getMaxZoom();
        lods = lods.length > 0 ? lods : __map._params.lods;
        zoomFactor = __map.getZoom();
        if (!isDynamicMode()) {
            if (zoomFactor === maxZoomLevel)
                $(".zoom-in").addClass('zoom-unable');
            else if (zoomFactor === minZoomLevel)
                $(".zoom-out").addClass('zoom-unable');
        }
        if (lods != undefined && lods.length > 0 && sliderCreated===false) {
            $zoomSlider.show();
            $zoomSlider
                .slider({
                    max: lods.length-1,
                    min: 0,
                    range: 'min',
                    orientation: "vertical",
                    create:function(){
                        sliderCreated=true;
                        addSliderListener();
                    }
                })
                .slider("pips", {
                    first: "pip",
                    last: "pip"
                }).slider("float", {
                    formatLabel: function (val) {
                        var lod = findLODByLevel(val);
                        if (lod != undefined)
                            return "1:" + lod.scale.toFixed(0);
                    }
                }).on('slidechange', function (evt, ui) {
                    $(".ui-slider-tip").css("left", ($(".ui-slider-tip").width() + 8) * (-1) + "px");
                    if (ui.value === lods.length-1) {
                        $(".ui-slider-handle").css("bottom", "97%");
                    }
                    if (__map.getLevel() != ui.value)
                        __map.setLevel(ui.value);
                });
        }
    }

    /***
     * 添加slider监听
     */
    function addSliderListener(){
        __map.on('extent-change', function (evt) {
            //根据配置比例尺改变图层的动态静态切换
            changeLayerStatus();
            if (evt.levelChange && evt.lod != undefined) {
                zoomFactor = __map.getZoom();
                if (zoomFactor === minZoomLevel)
                    $zoomOutControl.addClass('zoom-unable');
                else if (zoomFactor === maxZoomLevel)
                    $zoomInControl.addClass('zoom-unable');
                else {
                    if ($zoomInControl.hasClass('zoom-unable'))
                        $zoomInControl.removeClass('zoom-unable');
                    if ($zoomOutControl.hasClass('zoom-unable'))
                        $zoomOutControl.removeClass('zoom-unable');
                }
                if ($zoomSlider.slider("value") != evt.lod.level)
                    $zoomSlider.slider("value", evt.lod.level);
            }
        });
    }

    /**
     * 添加监听
     * @private
     */
    function addListener() {
        $(".map-control").on('click', function () {
            var type = $(this).data('type');
            switch (type) {
                case 'in':
                    if (isDynamicMode())
                        __map.setZoom(0);
                    else if (zoomFactor < maxZoomLevel) {
                        zoomFactor = zoomFactor + 1;
                        __map.setZoom(zoomFactor);
                    }
                    break;
                case 'out':
                    if (isDynamicMode())
                        __map.setZoom(1);
                    else if (zoomFactor > minZoomLevel) {
                        zoomFactor = zoomFactor - 1;
                        __map.setZoom(zoomFactor);
                    }
                    break;
                case 'globe':
                    if (_initExtent != undefined)
                        __map.setExtent(_initExtent);
                    break;
                case 'fullscreen':
                    var $this = $(this);
                    if ($this.hasClass('active')) {
                        exitFullScreen(document.documentElement);
                        $this.removeClass('active');
                    } else {
                        fullScreen(document.documentElement);
                        $this.addClass('active');
                    }
                    break;
            }
        });
        //若未配置lods 则在加入切片服务后 重新初始化组件
        var layerAddHandler = __map.on('layer-add', function (evt) {
            if(lods==undefined){
                var layer = evt.layer;
                if (esriLang.isDefined(layer.tileInfo)) {
                    lods = layer.tileInfo.lods;
                    if (lods != undefined && lods.length > 0) {
                        initComponent();
                        layerAddHandler.remove();
                    }
                }
            }
        });
    }

    /***
     * 全屏显示某个ele
     * @param el
     */
    function fullScreen(el) {
        var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen,
            wscript;

        if(typeof rfs != "undefined" && rfs) {
            rfs.call(el);
            return;
        }

        if(typeof window.ActiveXObject != "undefined") {
            wscript = new ActiveXObject("WScript.Shell");
            if(wscript) {
                wscript.SendKeys("{F11}");
            }
        }
    }

    /**
     * 退出全屏
     * @param el
     */
    function exitFullScreen(el) {
        var el= document,
            cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen,
            wscript;

        if (typeof cfs != "undefined" && cfs) {
            cfs.call(el);
            return;
        }

        if (typeof window.ActiveXObject != "undefined") {
            wscript = new ActiveXObject("WScript.Shell");
            if (wscript != null) {
                wscript.SendKeys("{F11}");
            }
        }
    }

    /***
     * find LOD
     * @param level
     */
    function findLODByLevel(level) {
        var r = arrayUtil.filter(lods, function (item) {
            return item.level === level;
        });
        if (r.length > 0)return r[0];
        return undefined;
    }

    /**
     * 是否仅仅只有动态服务
     * @returns {boolean}
     */
    function isDynamicMode(){
        return minZoomLevel===-1&&maxZoomLevel===-1;
    }

    /**
     * 根据配置比例尺改变图层的动态静态切换
     */
    function changeLayerStatus(){
        //当前页面的比例尺
        var currentScale = parseInt(__map.getScale().toFixed(0));
        if(layers != null && __map.layerIds != null){
            for(var i = 0;i < layers.length; i++){
                //配置的图层id
                var configId = layers[i].id;
                //配置的图层比例尺
                var configScale = parseInt(layers[i].scale);
                for(var j = 0; j < __map.layerIds.length; j++){
                    //当前勾选图形id
                    var currentId = __map.layerIds[j];
                    //根据layerid获取图层
                    var currentLayer = __map.getLayer(currentId);
                    if(configId == currentId){
                        var flag = judgeIsDynamic(currentLayer);
                        if(currentScale < configScale && flag == "false"){
                            //在这个if中，如果当前是静态则切换成 动态 并 删除原图层
                            addLayer(currentLayer, "dynamic");
                        }else if (currentScale >= configScale && flag == "true") {
                            //在这个判断中 当前图层是动态的话则 切换成静态 并删除原图层
                            addLayer(currentLayer, "tiled");
                        }
                    }
                }
            }
        }
    }

    /**
     * 加载图层
     * @param layer
     * @param optLayers
     */
    function addLayer(currentLayer, type){
        __map.removeLayer(currentLayer);
        var newLayer = returnOptLayer(currentLayer, type);
        if(newLayer.type == "dynamic"){
            newLayer.maxScale = 0;
            newLayer.minScale = 0;
        }
        MainMap.addLayer(newLayer, newLayer.index);
    }

    /**
     * 返回对应的配置layer,并将当前layer的值赋给最新的layer
     * @param layer
     */
    function returnOptLayer(layer, type){
        var newLayer;
        //获取配置的图层属性er.getAppConfig().map.operationalLayers;
        var layerId = layer.id;
        if(layerTypes != null){
            for(var i = 0; i < layerTypes.length; i++){
                if(layerId == layerTypes[i].id){
                    layerTypes[i].type = type;
                    newLayer = layerTypes[i];
                }
            }
        }
        //此处需要在加入visibleAtMapScale和visable属性
        newLayer.visible = true;
        newLayer.visibleAtMapScale = true;
        return newLayer;
    }

    /**
     * 判断动态和静态两种类型 排除其他类型
     * @param currentLayer
     * @returns {string}
     */
    function  judgeIsDynamic(currentLayer){
        var flag = "";
        if(layerTypes != null){
            for(var i = 0; i < layerTypes.length; i++){
                if(currentLayer.id == layerTypes[i].id){
                    if(layerTypes[i].type == "dynamic"){
                        flag = "true";
                    }else if(layerTypes[i].type == "tiled"){
                        flag = "false";
                    }
                }
            }
        }
        return flag;
    }

    return Nav;
});