<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Mp4 Player</title>
<@com.style name="static/thirdparty/video/video-js.css"></@com.style>
    <!-- If you'd like to support IE8 -->
<@com.script name="static/thirdparty/video/videojs-ie8.min.js"></@com.script>
    <style>
        body {
            background-color: #191919
        }

        .wrapper {
            width: 640px;
            height: 360;
            margin-left: auto;
            margin-right: auto;
            margin-top: 10px;
            margin-bottom: 0px;
        }
    </style>
</head>

<body>
<div class="wrapper">
    <video id="my-video" class="video-js" controls preload="auto" width="640" height="264"
           poster="MY_VIDEO_POSTER.jpg" data-setup="{}">
        <source src="<@com.rootPath/>/file/download/${fileId!}" type="video/mp4">
        <!--<source src="http://127.0.0.1:7000/video/test3.mp4" type="video/mp4">-->
        <p class="vjs-no-js">
            To view this video please enable JavaScript, and consider upgrading to a web browser that
            <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
        </p>
    </video>
<@com.script name="static/thirdparty/video/video.min.js"></@com.script>
    <script type="text/javascript">
        videojs("my-video").ready(function () {
            var myPlayer = this;
            myPlayer.play();
        });
    </script>
</div>
</body>
</html>
