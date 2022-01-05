/**
 * 通透镜工具
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/24 10:44
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    'dojo/topic',
    "esri/map",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/layer"], function (declare, lang,topic, Map,ArcGISDynamicMapServiceLayer,ArcGISTiledMapServiceLayer,Layer) {

    var lyr_id, lyr_index, lyr_div,lyr_type;
    var map_drag_handler = null;
    var map_up_handler=null;
    var spy_map_div = "spy_map_div";
    var spyMap = null;
    var magnify_factor=1;//缩放因子eg.2---放大2倍显示

    var spy = declare(null, {

        /***
         *
         */
        map: undefined,
        /***
         * 进行spy的图层服务
         */
        spyLyr: null,
        /***
         * active
         */
        activated: false,

        /***
         *
         */
        radius:80,


        constructor: function (option) {
            lang.mixin(this, option);
        },

        /***
         *  activate the tool
         */
        activate: function () {
            if (this.spyLyr == null) {
                console.error("spyLyr is null!");
                return;
            }
            lyr_id = this.spyLyr.id;
            lyr_div = document.getElementById('main-map_' + lyr_id);
            if (this.spyLyr.visible)
                this.spyLyr.setVisibility(false);
            this.activated = true;

            //添加通透map
            $("#main-map").append('<div id="' + spy_map_div + '" class="spy-map-div"><div id="spy_map"></div></div>');


            spyMap = new Map("spy_map",{
                logo: false,
                slider: false
            });
            $("#spy_map").css("width",this.map.width);
            $("#spy_map").css("height",this.map.height);

            $("#" + spy_map_div).css("width", this.radius * 2);
            $("#" + spy_map_div).css("height", this.radius * 2);
            $("#" + spy_map_div).css("border-radius", "50%");
            $("#" + spy_map_div).hide();   //隐藏通透镜

            spyMap.setLevel(this.map.getLevel()*magnify_factor);
            spyMap.centerAt(this.map.extent.getCenter());
            spyMap.setExtent(this.map.extent);
            
            /**
             * 订阅主地图extent变化
             */
            topic.subscribe('map-extent-changed', lang.hitch(this,function (evt) {
                if(this.activated==true){
                    spyMap.setExtent(evt.extent);
                }
            }));

            this._addSpyLyr();

            spyMap.disablePan();
            spyMap.disableDoubleClickZoom();
            spyMap.disableScrollWheelZoom();
            spyMap.disableSnapping();
            spyMap.disableKeyboardNavigation();

            this.map.disablePan();
            map_drag_handler= this.map.on("mouse-drag", lang.hitch(this,this._onMouseDrag));
            map_up_handler = this.map.on('mouse-up', lang.hitch(this, this._onMouseUp));
        },
        /***
         *
         * @param e
         * @private
         */
        _onMouseDrag:function(e){
            e.stopPropagation();
            this.map.disablePan();
            spyMap.disablePan();
            spyMap.disableDoubleClickZoom();
            spyMap.disableScrollWheelZoom();
            spyMap.disableSnapping();
            spyMap.disableKeyboardNavigation();
            $("#" + spy_map_div).show(); //显示通透镜
            var offsetX = e.screenPoint.x;
            var offsetY = e.screenPoint.y;
            var mapX = e.mapPoint.x;
            var mapY = e.mapPoint.y;
            this._refreshDivSize(offsetX, offsetY, mapX, mapY);
        },
        _onMouseUp:function(e){
          if(this.activated==true)
              $("#" + spy_map_div).hide();   //隐藏通透镜
        },
        /***
         *
         * @private
         */
        _addSpyLyr:function(){
            var opt={visible:true,id:lyr_id,type:lyr_type,url:this.spyLyr.url};
            switch (lyr_type) {
                case "dynamic":
                case "export":
                    spyMap.addLayer(new ArcGISDynamicMapServiceLayer(opt.url, opt));
                    break;
                case "tiled":
                    spyMap.addLayer(new ArcGISTiledMapServiceLayer(opt.url, opt));
                    break;
            }
        },
        /***
         *
         * @param offsetX
         * @param offsetY
         * @param mapX
         * @param mapY
         * @private
         */
        _refreshDivSize: function (offsetX, offsetY, mapX, mapY) {
            var t = offsetY - this.radius;
            var l = offsetX - this.radius;
            $("#spy_map").css("top", -(offsetY - this.radius) + "px");
            $("#spy_map").css("left", -(offsetX - this.radius) + "px");

            $("#" + spy_map_div).css("top", t + "px");
            $("#" + spy_map_div).css("left", l + "px");
        },
        /***
         * de activate the tool
         */
        deactivate: function () {
            this.map.enablePan();
            $("#" + spy_map_div).remove();
            if (map_drag_handler != null)map_drag_handler.remove();
            if (map_up_handler != null)map_up_handler.remove();

            if (this.spyLyr.visible == false)
                this.spyLyr.setVisibility(true);
            this.activated = false;
        },
        /***
         * is active or not
         * @returns {boolean}
         */
        isActive: function () {
            return this.activated;
        },
        /***
         *
         * @param m
         */
        setMap: function (m) {
            this.map = m;
        },
        /***
         *
         * @param l
         */
        setLayer: function (l) {
            this.spyLyr = l.layer;
            lyr_index = l.index;
            lyr_type= l.type;
        },
        /***
         *
         * @param r
         */
        setRadius:function(r){
            this.radius=r;
        }
    });
    return spy;
});