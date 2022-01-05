/**
 * map infoWindow 定制地图的弹出窗
 * 说明：
 * 1.绑定graphic要素，根据要素属性显示弹出窗内容，默认simple样式
 * 3.其他方式待实现
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 10:03
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "esri/Color",
    "static/js/map/core/support/Environment",
    "handlebars",
    "text!map/template/infowindow/info-basic-tpl.html",
    "text!map/template/infowindow/infoWindowTitle.html",
    "css!map/template/infowindow/infoWindow.css"],function (declare,lang,array,Deferred,Color,Environment,Handlebars,SimpleInfoTpl,infoWindowTitle) {

    var instance,me=declare(null,{

        /***
         * map
         */
        map:null,

        /***
         * info window of current map
         */
        infoWindow:undefined,
        /***
         * graphic
         */
        graphic:null,

        /***
         *eg.[{name:'xx',alias:'xxx',value:'12sd'}]
         */
        data:[],

        /***
         * 标题字段名称 eg.'PRONAME'
         */
        titleField:'',

        /***
         * 标题
         */
        title:'',

        /***
         * 展示的内容
         */
        content:'',

        /***
         * 链接 eg:{tip:'ccc',url:'http://www.google.com/'}
         */
        link:{},

        /***
         * 弹出窗的位置
         */
        position:null,

        /***
         * 样式 默认:'simple'
         */
        style:'',

        constructor:function(){
            this.setStyle(this.INFO_STYLE_SIMPLE);
        },
        /***
         * show
         */
        show: function (data,gra,tf,pos,title,link) {
            this.data=data;
            this.graphic=gra;
            this.titleField=tf;
            this.position=pos;
            this.title=title;
            this.link=link;
            this._prepare();
            if (this.infoWindow.isShowing)this.hide();
            this.infoWindow.setContent(this.content);
            this._setInfoStyle();
            this.infoWindow.show(this.position);
            this.infoWindow.reposition();
            var  mapContainerHeight = $("#main-map_container").height();
            var popupHeight = $(".esriPopupWrapper").height();
            if(Environment.isIE() || popupHeight>=mapContainerHeight){  //ie低分辨率下必须要resize后才能正确显示弹框位置
                if($(".scroll").length>0)
                    $(".scroll").attr("height","350px");
                else
                    $(".scroll-none-ie").attr("height","350px");
                this.infoWindow.resize($(".sizer").width(), popupHeight>=mapContainerHeight? 350:popupHeight);
            }else {
                $(".scroll").addClass("scroll-none-ie");
                $(".scroll-none-ie").removeClass("scroll");
                $(".scroll-none-ie").css("max-height",popupHeight);
            }

            var win= this.infoWindow;
            $('.close').on('click',function(){
               win.hide();
            });
        },

        /**
         * 展示详细信息
         * @param tpl
         * @param pos
         * @param title
         * @returns {Deferred}
         */
        showDetail:function(tpl,pos,title){
            if(title!=undefined&&title!=null){
                tpl=_renderTpl(infoWindowTitle,{title:title})+tpl;
            }
            var d=new Deferred();
            this.position=pos;
            this.infoWindow.setContent(tpl);
            this._setInfoStyle();
            if (this.infoWindow.isShowing)this.hide();
            this.infoWindow.show(this.position);
            d.callback(this.position);
            $('.close').on('click',lang.hitch(this,this.hide));
            return d;
        },

        /***
         *
         * @private
         */
        _prepare:function(){
            if (this.map == null)
                throw new Error("map need to bind first!");
            if(this.data.length==0)
                throw new Error("无展示数据!");
            if(this.titleField!=''){
                this.title=this._getTitle();
            }
            this.content=this._getContent();
        },
        /***
         * hide
         */
        hide:function(){
          this.infoWindow.hide();
        },
        /***
         * 设置样式
         */
        _setInfoStyle: function () {
            $(".sizer .titlePane").addClass('hidden');
            if (this.style === this.INFO_STYLE_SIMPLE) {
                $('.esriPopupWrapper .sizer:first-child').hide();
                $('.esriPopup .contentPane').css('padding-top','0px!important');
            }
            $.each($('.esriPopupWrapper .sizer'), function () {
                if (!$(this).hasClass('content'))$(this).remove();
            });
        },
        /***
         *
         * @returns {null}
         * @private
         */
        _getTitle: function () {
            var arr = array.filter(this.data, lang.hitch(this, function (item) {
                return item.name === this.titleField;
            }));
            return arr.length == 0 ? null : arr[0].value;
        },

        /***
         * 生成弹出窗的内容
         * @private
         */
        _getContent:function(){
            return _renderTpl(SimpleInfoTpl,{fields:this.data,title:this.title,link:this.link});
        },
        /***
         * 绑定map
         * @param m
         */
        bindMap:function(m){
            this.map=m;
            this.infoWindow= m.infoWindow;
        },
        /***
         * 设置超链接
         * @param v
         */
        setLink:function(v){
            link=v;
        },
        /***
         *
         * @param s
         */
        setStyle:function(s){
            style=s;
        }


    });

    /***
     * render tpl
     * @param tpl
     * @param data
     * @returns {*}
     * @private
     */
    function _renderTpl(tpl, data) {
        if (tpl != undefined) {
            var tempplate = Handlebars.compile(tpl);
            return tempplate(data);
        }
        return undefined;
    }

    lang.mixin(me,{
        INFO_STYLE_SIMPLE:'simple',
        INFO_STYLE_ESRI:'esri'
    });

    /**
     * get instance
     *
     * @returns {*}
     */
    me.getInstance = function () {
        if (instance === undefined) {
            instance = new me();
        }
        return instance;
    };
    return me;
});