<!DOCTYPE html>
<html lang="en">
<head>
    <title>视频预览</title>
    <link href="<@com.rootPath/>/static/thirdparty/layui/css/layui.css" rel="stylesheet">
    <link href="<@com.rootPath/>/static/thirdparty/video/video-js.min.css" rel="stylesheet">
    <link href="<@com.rootPath/>/static/css/iconfont/iconfont_omp.css" rel="stylesheet">

    <script src="<@com.rootPath/>/static/thirdparty/video/ie8/videojs-ie8.min.js"></script>
    <script src="<@com.rootPath/>/static/thirdparty/video/video.min.js"></script>
    <style>
        .vjs-progress-control, .vjs-remaining-time-display {
            visibility: hidden;
        }

        .video-js .vjs-play-control.vjs-playing {
            visibility: hidden;
        }

        .video-js .vjs-tech {
            pointer-events: none;
        }

        .video-pane {
            float: left;
        }

        .control-pane {
            width: 200px;
            float: left;
            margin-left: 12px;
        }

        .control-pane table {
            width: 100%;
            margin-top: 24px;
            color: #616161;
        }

        .control-pane table tr {
            height: 35px;
            line-height: 35px;
        }

        .control-pane table i {
            font-size: 32px;
            cursor: pointer;
        }

        .control-pane table i:hover {
            color: #0288D1;
        }

        .text-center {
            text-align: center !important;
        }

        .text-left {
            text-align: left !important;
        }

        .text-right {
            text-align: right !important;
        }

        .ptz-control:active {
            position:relative;
            top:1px;
        }

        .vjs-control-bar{
            display: none!important;
        }

        .video-panel-container{
            padding: 8px;
        }

    </style>
</head>
<body>
<div class="video-panel-container">
    <div class="video-pane">
        <video id="videojs"
               class="video-js vjs-default-skin vjs-big-play-centered"
               controls autoplay preload="none" width="640" height="480"
               poster="" data-setup="{}">
            <source src="${liveUrl!}" type="rtmp/mp4">
            <p class="vjs-no-js">To view this video please enable JavaScript, and consider upgrading to a web
                browser
                that
                <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a></p>
        </video>
    </div>
    <i class="layui-icon" style="top: 0;right: 0;position: absolute;cursor: pointer" title="关闭" onclick="stopPlay()">&#x1006;</i>
    <div class="control-pane text-center">
        <h3>云台控制</h3>
        <table lay-skin="nob">
            <tbody style="padding: 16px;">
            <tr>
                <td class="text-right" title="放大"><i class="iconfont ptz-control" data-code="PTZ_LENS_ZOOM_IN">&#xe89e;</i></td>
                <td></td>
                <td class="text-left" title="缩小"><i class="iconfont ptz-control" data-code="PTZ_LENS_ZOOM_OUT">&#xe90a;</i></td>
            </tr>
            <tr>
            </tr>
            <tr>
                <td colspan="3" title="向上"><i class="iconfont ptz-control" data-code="PTZ_UP">&#xe631;</i></td>
            </tr>
            <tr>
                <td title="向左"><i class="iconfont ptz-control" data-code="PTZ_LEFT">&#xe618;</i></td>
                <td title="刷新"><i class="iconfont ptz-control">&#xe63a;</i></td>
                <td title="向右"><i class="iconfont ptz-control" data-code="PTZ_RIGHT">&#xe621;</i></td>
            </tr>
            <tr>
                <td colspan="3" title="向下"><i class="iconfont ptz-control" data-code="PTZ_DOWN">&#xe60a;</i></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
<script src="<@com.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js"></script>
<script>
    videojs.options.flash.swf = '<@com.rootPath/>/static/thirdparty/video/video-js.swf';
    videojs.options.techOrder = ['html5', 'flash'];
    var player = null;
    var liveUrl = '${liveUrl!}';
    var cameraCode = '${cameraCode!}';
    setupPlayer(liveUrl);
    /**
     * set up player
     * @param streamUrl
     * @param videoImg
     */
    function setupPlayer(streamUrl, videoImg) {
        if (videoImg !== undefined) {
            $("#videojs").attr("poster", videoImg);
        }

        if (streamUrl.indexOf("rtmp") === 0) {
            player = videojs("videojs", {
                notSupportedMessage: '您的浏览器没有安装或开启Flash,戳我开启！',
                techOrder: ["flash"],
                autoplay: true
            });
            player.on("error", function (e) {
                var $e = $(".vjs-error .vjs-error-display .vjs-modal-dialog-content");
                var $a = $("<a href='http://www.adobe.com/go/getflashplayer' target='_blank'></a>").text($e.text());
                $e.empty().append($a);
            })
        } else {
            console.warn('目前仅支持在线播放 RTMP 视频流!');
        }
    }

    $(".ptz-control").on("mousedown",function(){
        var code  = $(this).data("code");
        moveControl(code,"1");
    });

    $(".ptz-control").on("mouseup",function(){
        var code  = $(this).data("code");
        moveControl(code,"3");
    });


    function moveControl(code,param){
         $.ajax({
             url:"<@com.rootPath/>/video/ptz/move",
             data:{cameraCode:cameraCode,controlCode:code,param1:param,param2:"5"},
             success:function(r){
                     if(r!=0){
                         layer.msg("操作失败！",{icon:2,time:1000});
                     }
             }
         });
    }

    function stopPlay() {
        player.pause();
        player.dispose();
        parent.layer.closeAll();
        return true;
    }
</script>
</html>
