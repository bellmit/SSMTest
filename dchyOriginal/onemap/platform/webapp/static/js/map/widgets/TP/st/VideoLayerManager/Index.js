/**
 * Created by ycx on 2018/11/26.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "layer",
    "dojo/topic",
    "map/core/BaseWidget",
    "hbarsUtils",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/FeatureLayer",
    'esri/symbols/SimpleFillSymbol',
    "esri/symbols/SimpleLineSymbol",
    "esri/Color",
    "esri/geometry/Circle",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
], function (declare,
             lang,
             arrayUtil,
             layer, topic,
             BaseWidget,
             HbarsUtils,
             ArcGISDynamicMapServiceLayer,
             FeatureLayer,
             SimpleFillSymbol,
             SimpleLineSymbol,
             Color,
             Circle,
             GraphicsLayer,
             Graphic
             ) {
    var _map;
    var _appConfig;
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            _appConfig = this.getConfig();
            _map = this.getMap().map();
            _init();
        },

        onPause: function () {
            showOrHideVideoLayer(true);
        },

        onOpen: function () {
        }
    });

    function bindEvent() {
        $(".type li").click(function (e) {
            $item = $(e.target);
            var url=$item.attr("layerUrl");
            var domId = $item.attr("id");
            var isChoosed = $item.hasClass("choosed");
            if(isChoosed){
                hideLayer(domId);
                $item.removeClass("choosed");
            }else {
                showLayer(domId,url);
                $item.addClass("choosed");
            }

        });
    }

    /**
     * 隐藏或显示点
     * 默认隐藏
     */
    function showOrHideVideoLayer(data) {
        var layer = _map.getLayer("videoGraphicsLyr");
        for(var i in layer.graphics){
            var graphic =layer.graphics[i];
            if(typeof data=="boolean"){
                if(data){
                    graphic.show();
                }else {
                    graphic.hide();
                }
            }else {
                for(var i=0;i<data.length;i++){
                    var item = data[i];
                    if(graphic.attributes["indexCode"]==item){
                        graphic.show();
                        break;
                    }
                }
                graphic.hide();
            }

        }
    }
    /*
    显示图层
    过滤图层
     */
    function showLayer(domId,url) {
        var layer = _map.getLayer(domId);
        if(layer){
            layer.show();
        }else{
            var featureLayer = new FeatureLayer(url);
            featureLayer.id=domId;
            _map.addLayer(featureLayer);
        }
        changeCameraStyle();
        //showOrHideVideoLayer(false);
        //filterCamera(domId);
    }
    
    function filterCamera(layerId) {
        $.ajax({
            url:"/omp/video/getOverlayCamera",
            data:{
                layerName:layerId
            },
            success:function (obj) {
                if(!obj["error"]&&obj["data"]){
                    //成功
                    var data = obj["data"];
                    var videoLayer= _map.getLayer("videoGraphicsLyr");
                    for(var i in videoLayer.graphics){
                        var graphic = videoLayer.graphics[i];
                        //循环
                        for(var j = data.length;j>0;j--){
                            var indexCode =data[j];
                            if(!indexCode)continue;
                            if(graphic.attributes["indexCode"]==indexCode){
                                graphic.show();
                                data.splice(i,1);
                                continue;
                            }
                        }
                    }
                }
            }
        });
    }
    
    //隐藏专题图层
    function hideLayer(domId) {
        var layer = _map.getLayer(domId);
        if(layer){
            layer.hide();
        }
        //返回原有标志
        changeCameraStyle(true);
        //showOrHideVideoLayer(true);
    }
    //初始化
    function _init() {
        esriConfig.defaults.io.proxyUrl = "/omp/proxy.jsp";
        var layerParam = _appConfig.layerParam;
        //初始图层html
        var layerContent = HbarsUtils.renderTpl($("#layerItem").html(),{list:layerParam});
        $("#videoLayerContent .type").empty().html(layerContent);
        bindEvent();
    }

    /**
     * 修改摄像头符号
     */
    function changeCameraStyle(isHide) {
        var circleLayer = _map.getLayer("circleLayer");
        var videoLayer = _map.getLayer("videoGraphicsLyr");
        //返回原有标志
        if(isHide&&circleLayer){
            circleLayer.hide();
            videoLayer.show();
            return;
        }
        if (!circleLayer) {
            var graphics = videoLayer.graphics;
            var lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([255,168,199]), 2);
            var fill = new SimpleFillSymbol();
            fill.outline = lineSymbol;
            circleLayer = new GraphicsLayer({id: 'circleLayer'});
            for (var i = 0; i < graphics.length; i++) {
                var graphic = graphics[i];
                var point = graphic.geometry;
                var circleGeometry = new Circle(point, {
                    "radius": 1000,
                    "numberOfPoints": 400
                });
                var g = new Graphic(circleGeometry, fill);
                circleLayer.add(g);
            }
            _map.addLayer(circleLayer);
            videoLayer.hide();
        } else {
            circleLayer.show();
            videoLayer.hide();
        }
    }
    
    return me;
});