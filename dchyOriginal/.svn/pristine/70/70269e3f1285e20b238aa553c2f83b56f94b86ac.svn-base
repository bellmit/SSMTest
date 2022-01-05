/**
 * 地图卷帘工具
 * 实现原理：将选中的需卷帘的图层服务置于顶层 然后控制其显示
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/24 10:45
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang"], function (declare, lang) {

    var lyr_id,lyr_index,lyr_div,border_div;
    var map_handler=null;
    var swipe = declare(null, {

        /***
         *
         */
        map: undefined,
        /**
         * 要卷帘的图层服务
         */
        swipeLyr:null,
        /***
         *
         */
        activated: false,

        /**
         * 卷帘方向 水平/垂直/both
         * 默认是水平
         */
        direction: this.HORIZONTAL_DIRECTION,

        constructor: function (option) {
            lang.mixin(this, option);
        },
        /***
         *activate the tool
         */
        activate: function () {
            if(this.swipeLyr==null){
                console.error("swipeLyr is null!");
                return;
            }
            lyr_id=this.swipeLyr.id;
            lyr_div=document.getElementById('main-map_'+lyr_id);
            border_div= document.createElement("div");
            border_div.style.borderBottom="1px solid yellow";
            border_div.style.borderRight="1px solid yellow";
            border_div.style.position="absolute";
            border_div.id="border_div"+lyr_id;
            border_div.className="swipeToolDiv";
            if(lyr_div)lyr_div.appendChild(border_div);
            //将要卷帘的服务移动到最顶层
            this.map.reorderLayer(this.swipeLyr,this.map.layerIds.length);
            this.activated = true;
            map_handler= this.map.on('mouse-move',lang.hitch(this,this._onMouseMove));
        },
        _onMouseMove:function(e){
            //var  swipe=this;
            var  offsetX=e.screenPoint.x;
            var  offsetY=e.screenPoint.y;
            var  m_h_px=lyr_div.style.height;
            var  m_w_px=lyr_div.style.width;
            var  m_h=parseInt(m_h_px.substring(0,m_h_px.lastIndexOf('px')));//去掉单位px 取出数值
            var  m_w=parseInt(m_w_px.substring(0,m_w_px.lastIndexOf('px')));
            var  origin=getLayerTransform(lyr_div);
            var  clip_top=-origin.y+"px";
            var  clip_left=-origin.x+"px";//clip的左上起点
            var clip_bottom,clip_right;
                switch (this.direction){
                    case swipe.HORIZONTAL_DIRECTION:
                        clip_bottom=(offsetY-origin.y)+'px';
                        clip_right=(m_w-origin.x)+"px";
                        break;
                    case swipe.VERTICAL_DIRECTION:
                        clip_bottom=(m_h-origin.y)+'px';
                        clip_right=(offsetX-origin.x)+"px";
                        break;
                    case swipe.BOTH_DIRECTION:
                        clip_bottom=(offsetY-origin.y)+'px';
                        clip_right=(offsetX-origin.x)+"px";
                        break;
                }
            lyr_div.style.clip='rect('+clip_top+','+clip_right+','+clip_bottom+','+clip_left+')';
            border_div.style.top=clip_top;
            border_div.style.left=clip_left;
            border_div.style.width=parseInt(clip_right.substring(0,clip_right.lastIndexOf('px')))-parseInt(clip_left.substring(0,clip_left.lastIndexOf('px')))+'px';
            border_div.style.height=parseInt(clip_bottom.substring(0,clip_bottom.lastIndexOf('px')))-parseInt(clip_top.substring(0,clip_top.lastIndexOf('px')))+'px';
        },
        /***
         *de activate the tool
         */
        deactivate: function () {
            //恢复原始的图层顺序,移除监听
            if($('.swipeToolDiv')) $('.swipeToolDiv').remove();
            if(map_handler!=null)map_handler.remove();
            $(lyr_div).css('clip','auto');
            $(border_div).remove();
            this.map.reorderLayer(this.swipeLyr,lyr_index);
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
         * set swipe direction
         * @param val
         */
        setDirection:function(val){
            this.direction=val;
        },
        setMap:function(m){
            this.map=m;
        },
        setLayer:function(l){
            this.swipeLyr= l.layer;
            lyr_index= l.index;
        }
    });
    //获取图层右上角的坐标
    function getLayerTransform(swipeLyr) {
        var xorigin, yorigin, layerstyle = swipeLyr.style;
//chrome
        if (layerstyle['-webkit-transform']) {
            var s = layerstyle['-webkit-transform'];//格式为"translate(0px, 0px)"
            var xyarray = s.replace(/translate\(|px|\s|\)/, '').split(',');
            xorigin = parseInt(xyarray[0]);
            yorigin = parseInt(xyarray[1]);
        }
//firefox
        else if (layerstyle['transform']) {
            //swipeLyr.style['transform'] 格式为"translate3d(xpx,ypx,zpx)" 这样的字符串，现在通过匹配转为[z,y,z]的数组,分别将 px,translate3d,空格
            // var xyzarray=layerstyle.replace(/px/g,'').replace(/ /g,'').replace('translate3d(','').replace(')','').split(',')
            var layertransforstring=layerstyle['transform'];
            var xyz = layertransforstring.replace(/px|\s|translate3d\(|px|\)/g, '').split(',');
            xorigin = parseInt(xyz[0]);
            yorigin = parseInt(xyz[1]);
        }
//ie 8+
        else {
            xorigin = parseInt(swipeLyr.style.left.replace('px', ''));
            yorigin = parseInt(swipeLyr.style.top.replace('px', ''));
        }
        return {
            x: xorigin,
            y: yorigin
        }
    }
    lang.mixin(swipe, {
        HORIZONTAL_DIRECTION: 'horizontal',
        VERTICAL_DIRECTION: 'vertical',
        BOTH_DIRECTION: 'both'
    });
    return swipe;
});