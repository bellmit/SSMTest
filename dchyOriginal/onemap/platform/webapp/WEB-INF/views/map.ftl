<!DOCTYPE html>
<html lang="en">
<head>
    <title>数据中心-地图浏览</title>
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
<@com.style name="static/css/theme/light-blue/project-info.css"></@com.style>
<@com.style name="static/thirdparty/pace/pace-theme-minimal.css"></@com.style>
<@com.style name="static/css/theme/light-blue/material-nav.css"></@com.style>
</head>

<body class="claro">

<#if topBarVisible!true><#include "common/head.ftl"/></#if>

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

<!--主地图部分开始-->
<div id="map-content" class="usel">
    <div id="scaleInfo"></div>
    <div id="coordsInfo">
        <b>X:</b><span>0</span>
        &nbsp;
        <b>Y:</b><span>0</span>
    </div>
    <div id="basemapToggle"></div>
</div>
<!--主地图部分结束-->

<!--材料导航、项目图层开始--> 
<div class="material-and-layers">
<!--材料导航-->
    <div class="material-nav">
        <div class="nav-top">
            <h3>材料导航</h3>
            <i class="layui-icon material-close-btn">&#x1006;</i>
        </div>
        <div class="nav-main">
            <div id="materialTreeMenu" class="demo-tree-more"></div>
        </div>
    </div>
    <!--项目图层-->
    <div class="right-layers">
        <div class="nav-top">
            <h3>图层</h3>
            <i class="layui-icon layers-close-btn">&#x1006;</i>
        </div>
        <div class="nav-main">
           <div id="projectLayerTree" class="demo-tree"></div>
        </div>
    </div>
</div>
<!--材料导航、项目图层结束-->
<!--项目信息开始--> 
<div class="project-info">
    <div class="top">
        <h4>项目信息</h4>
        <div class="btn-box">
            <button class="layui-btn space-tree-btn">项目图层</button>
            <button class="layui-btn project-tree-btn">项目详情</button>
            <i class="project-info-close-btn">×</i>
        </div>
    </div>
    
    <table class="layui-table">
        <tbody>
            <tr>
                <td>项目编号</td>
                <td class="xmbh"></td>
            </tr>
            <tr>
                <td>项目名称</td>
                <td class="xmmc"></td>
            </tr>
            <tr>
                <td>项目地址</td>
                <td class="xmdz"></td>
            </tr>
            <tr>
                <td>建设单位</td>
                <td class="jsdw"></td>
            </tr>
            <tr>
                <td>已完成测量事项</td>
                <td class="finished-box">
                   <form class="layui-form" action="">
                   </form>
                </td>
            </tr>
        </tbody>
    </table>
</div>
<!--项目信息结束-->

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

<!--地图下方的结果展示面板-->
<div id="result-container">
    <div class="shrink"><a class="btn btn-default btn-sm" title="收起"><i class="fa fa-chevron-down"></i></a></div>
    <div class="content" style="height: 300px;"></div>
    <div class="expand"><a class="btn btn-default btn-sm" title="展开"><i class="fa fa-chevron-up"></i></a></div>
</div>
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
    var tpl = '${tpl!}';

    var userName = '${ctx.getUserName()!}';
    var userId = '${ctx.getUserId()!}';
    var isAdmin = '${env.isAdmin()?string!'false'}' == 'true' ? true : false;
    var leasUrl = '${env.getEnv('leas.url')!}';
    var insightUrl = '${env.getEnv('insight.url')!}';
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
<@com.script name="static/thirdparty/echarts/echarts.min.js"></@com.script>
<@com.script name="static/thirdparty/agsapi/3.14/init.js"></@com.script>

<!--[if lt IE 9]>
<script type="text/javascript" src="static/js/hack/html5shiv.js"></script>
<script type="text/javascript" src="static/js/hack/respond.min.js"></script>
<script type="text/javascript" src="static/thirdparty/h-ui/lib/PIE_IE678.js"></script>
<script type="text/javascript" src="static/js/json2.js"></script>
<![endif]-->
<#if env.getEnv("local.path") == 'changzhou'>
    <@com.script name="static/js/busi/archive.changzhou.js"/>
</#if>

<@com.script name="static/js/init.js"></@com.script>
<@com.script name="static/js/preview.js"></@com.script>
<@com.script name="static/js/insight-remote.js"></@com.script>
<@com.script name="static/js/common.js"></@com.script>
<@com.script name="static/js/project-info.js"></@com.script>


<@com.script name="static/js/dchyxxglpt/material-nav.js"></@com.script>
</html>
