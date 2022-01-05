/**
 * 地图鹰眼
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 9:47
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "map/core/BaseWidget",
    "esri/map",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/dijit/OverviewMap"], function (declare, arrayUtil,BaseWidget,Map,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer,OverviewMap) {
    var _map,_ovConfig;
    var overview = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            _ovConfig=this.getConfig();
            $('#Overview').addClass('overview-q').addClass('overview-q');
            var over = new OverviewMap({
                map:_map,
                expandFactor:2,
                opacity:0.8,
                visible:true
            },$(".Overview_content")[0]);
            over.startup();
            $('#Overview').on('click','.overview-icon',function(evt){
                var $this = $(this);
                if($this.hasClass('open-icon')){//此时状态处于关闭
                    $('#Overview').removeClass('overview-h');
                    $('#Overview').addClass('overview-q');
                    $this.removeClass('open-icon').addClass('close-icon').attr('title','隐藏鹰眼');
                    over.show();
                }
                else{//此时状态处于打开
                    $this.removeClass('close-icon').addClass('open-icon').attr('title','显示鹰眼');
                    over.hide();
                    $('#Overview').removeClass('overview-q');
                    $('#Overview').addClass('overview-h');
                }
            });
        },
        onOpen: function () {

        },
        onPause:function(){

        }
    });
    return overview;
});