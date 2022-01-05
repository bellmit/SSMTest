/**
 * Created by wangcheng on 2017/3/28.
 */
define([
    "dojo/_base/declare",
    "map/core/BaseWidget",
    "layer"
], function (declare,
             BaseWidget,layer) {
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {

        },

        onPause: function () {
            
        },

        onOpen: function () {
            layui.use('layer', function(){
                var layer = layui.layer;
                var height = $(window).height();
                var width = $(window).width();
                height = height+"px";
                width = width*0.9+"px";
                var host = window.location.host;
                try {
                    layer.open({
                        type: 2,
                        title: '查看摄像头台账',
                        shadeClose: true,
                        shade: false,
                        maxmin: true, //开启最大化最小化按钮
                        area: [width,height],
                        content: 'http://'+host+'/omp/video/metadata'
                    });
                }catch (er){
                    console.log(er);
                }

            });
        }
    });

    return me;
});