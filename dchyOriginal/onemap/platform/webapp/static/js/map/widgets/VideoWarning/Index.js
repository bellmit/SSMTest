/**
 * Created by wangcheng on 2017/3/28.
 */
define([
    "dojo/_base/declare",
    "map/core/BaseWidget",
    "map/widgets/VideoManager/Index"
], function (declare,
             BaseWidget,VideoManager) {
    var videoManager = null;
    var me = declare([BaseWidget], {
        constructor: function () {
            $.ajax({
                url: "/omp/project/getWarnProCount",
                type: 'POST',
                success: function (r) {
                    if(r>0){
                        changeDivColor(
                            {r: 255, g: 255, b: 255 },
                            { r: 255, g: 0, b: 0 },
                            function (color) { document.getElementById("btn_VideoWarning").style.backgroundColor = color; }
                        );
                    }
                }
            });
        },
        onCreate: function () {

        },

        onPause: function () {

        },

        onOpen: function () {
            videoManager=videoManager||new VideoManager();
            videoManager.openWarning();
        }
    });
    function changeDivColor(start,to,callback) {
        var r="r";
        var g="g";
        var b="b";
        var rCut = to[r]-start[r];
        var gCut = to[g]-start[g];
        var bCut = to[b]-start[b];
        var erCut =rCut/10;
        var egCut =gCut/10;
        var ebCut =bCut/10;
        var count =0;
        var isAdd =true;
        setInterval(function(){
            count++;
            if(isAdd){
                start[r]+=erCut;
                start[g]+=egCut;
                start[b]+=ebCut;
            }else{
                start[r]-=erCut;
                start[g]-=egCut;
                start[b]-=ebCut;
            }
            if(count==10){
                count=0;
                isAdd =!isAdd;
            }
            var color ='rgb(' + start[r] + ',' + start[g] + ',' + start[b] + ')';
            callback(color);
        },100);
    }
    return me;
});