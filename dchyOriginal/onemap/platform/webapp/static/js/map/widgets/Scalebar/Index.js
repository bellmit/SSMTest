/**
 * Created by yangjiawei on 2016/8/5.
 */
define(["dojo/_base/declare",
        "map/manager/ConfigManager",
        "map/core/BaseWidget",
        "esri/dijit/Scalebar"
    ],function(declare, configManager, BaseWidget, Scalebar){

    var __map, __config;
    var Scale = declare([BaseWidget],{
        constructor: function(){

        },
        onCreate: function(){
            __map = this.getMap().map();
            __config = this.getConfig();
            if (__config.hasOwnProperty("style")){
                addListener(__config.style);
            }else{
                addListener();
            }
        }
    });

    function addListener(t){
        switch (t){
            case "line" :;
            case "ruler" : scalebar(t); return;
            case "num" :;
            default: numericalScale();
        }
    }

    /**
     *scalebar/ numerical
     */
    function numericalScale(){
        __map.on("extent-change", function(evt){
            var _s = __map.getScale();
            try{
                $("#scaleInfo").html('<span>比例尺:</span> 1:' + _s.toFixed(0));
            }catch(e){

            }
        });
    }

    /**
     * scalebar/ line or ruler
     * @param t
     */
    function scalebar(t){
        var scalebar = new Scalebar({
            map: __map,
            scalebarStyle: t,
            scalebarUnit: "dual",
            attachTo: "bottom-left"
        });

        scalebar.show();
    }

    return Scale;
});