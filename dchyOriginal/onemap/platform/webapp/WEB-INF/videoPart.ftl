<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="shortcut icon" type="image/x-icon" href="<@com.rootPath/>/static/img/favicon.ico" media="screen"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

<@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
<@com.style name="static/thirdparty/agsapi/3.14/esri/css/esri.min.css"></@com.style>
<@com.style name="static/thirdparty/agsapi/3.14/dijit/themes/claro/claro.css"></@com.style>
<@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.min.css"></@com.style>
<@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
<@com.style name="static/css/iconfont/iconfont_omp.css"></@com.style>
<@com.style name="static/css/theme/light-blue/map.css"></@com.style>
<@com.style name="static/thirdparty/pace/pace-theme-minimal.css"></@com.style>
</head>

<body class="claro">

<!--左侧面板开始-->
<div>
    <a class="main-operation-handle" title="展开"><i class="fa fa-bars fa-lg"></i></a>
</div>
<div class="main-operation usel">
    <!--左侧菜单项-->
    <div id="verticalMenu" class="vertical-operation-menu"></div>
    <!--对应左侧菜单的内容栏-->
    <div id="popupwin_area"></div>
</div>
<!--左侧面板结束-->

<!--右侧面板开始-->
<div class="right_panel">
    <div class="history-close-btn pull-right" title="隐藏"><span class="fa fa-times"></span></div>
    <ul id="recordsTab" class="nav nav-tabs m-t-10" role="tablist">
        <li role="presentation" class="active">
            <a href="#photos" aria-controls="photos" role="tab" data-toggle="tab">照片记录</a>
        </li>
        <li role="presentation">
            <a href="#record" aria-controls="record" role="tab" data-toggle="tab">巡查记录</a>
        </li>
    </ul>
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="photos">
        </div>
        <div role="tabpanel" class="tab-pane" id="record" data-proId="">
        </div>
    </div>
</div>
<!--右侧面板结束-->

</body>
<@com.script name="static/thirdparty/pace/pace.min.js"></@com.script>
<@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
<script type="text/javascript">

    Pace.start({
        document: true,
        elements: false,
        restartOnRequestAfter: false,
        restartOnPushState: false
    });

    var dwgImpUrl = '${env.getEnv('dwg.imp.url')!}';
    var dwgExpUrl = '${env.getEnv('dwg.exp.url')!}';
    //获取视频参数配置
    var platform = '${env.getEnv('video.platform')!}';
    var username = '${env.getEnv('video.userName')!}'.split("@")[0];
    var domain = '${env.getEnv('video.userName')!}'.split("@")[1];
    var userpwd = '${env.getEnv('video.userPwd')!}';
    var server = '${env.getEnv('video.server')!}';
    var serverPort = '${env.getEnv('video.port')!}';

    var userName = '${ctx.getUserName()!}';
    var userId = '${ctx.getUserId()!}';
    var isAdmin = '${env.isAdmin()?string!'false'}' == 'true' ? true : false;
    var leasUrl = '${env.getEnv('leas.url')!}';
    var isNeedSend = '${env.getEnv('send.inspect.record')!'false'?string}' == 'true' ? true : false; //是否需要推送巡查记录
    var omsUrl = '${path_oms!}';
    var locPostParams = '${locParams!}';
    var compareMaps = '${compareMaps!}';
    var proid='${proid!}';
    var indexCodePost="${indexCodePost!}";
</script>

<!--[if IE]>
<script type="text/javascript" src="/omp/static/thirdparty/excanvas/excanvas.js"></script>
<![endif]-->
<@com.script name="static/js/config.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<!--[if lt IE 9]>
<script type="text/javascript" src="static/js/hack/html5shiv.js"></script>
<script type="text/javascript" src="static/js/hack/respond.min.js"></script>
<script type="text/javascript" src="static/thirdparty/h-ui/lib/PIE_IE678.js"></script>
<script type="text/javascript" src="static/js/json2.js"></script>
<![endif]-->
<@com.script name="static/js/preview.js"></@com.script>

</html>
