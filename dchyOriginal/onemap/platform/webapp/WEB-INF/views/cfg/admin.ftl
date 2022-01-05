<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="/omp/static/thirdparty/h-ui/lib/html5.js"></script>
    <script type="text/javascript" src="/omp/static/thirdparty/lib/respond.min.js"></script>
    <script type="text/javascript" src="/omp/static/thirdparty/lib/PIE_IE678.js"></script>
    <![endif]-->
    <@com.style name="static/thirdparty/h-ui/css/H-ui.min.css"></@com.style>
    <@com.style name="static/thirdparty/h-ui/css/H-ui.admin.css"></@com.style>
    <@com.style name="static/thirdparty/h-ui/skin/blue/skin.css"></@com.style>
    <@com.style name="static/thirdparty/h-ui/lib/iconfont/iconfont.css"></@com.style>
    <title>数据中心管理后台</title>
    <link rel="shortcut icon" type="image/x-icon" href="/omp/static/img/favicon-setting.ico" media="screen" />
</head>
<body>
<header class="Hui-header cl"><a class="Hui-logo l" href="#">数据中心管理后台</a><span class="Hui-subtitle l">V1.0</span>
    <ul class="Hui-userbar">
        <#--<li>系统管理员</li>-->
        <li class="dropDown dropDown_hover"> <a href="#" class="dropDown_A">使用帮助 <i class="Hui-iconfont" style="font-size: 16px;">&#xe633;</i></a>
            <ul class="dropDown-menu menu radius box-shadow">
                <li><a href="<@com.rootPath/>/static/doc/数据中心(视频监控)系统部署与配置说明手册v3.0.pdf" target="_blank">pdf文档</a></li>
                <li><a href="<@com.rootPath/>/static/doc/数据中心(视频监控)系统部署与配置说明手册v3.0.docx" target="_blank">word文档</a></li>
            </ul>
        </li>
    </ul>
</header>
<aside class="Hui-aside">
    <input runat="server" id="divScrollValue" type="hidden" value="" />
    <div class="menu_dropdown bk_2">
        <dl id="menu-tpl">
            <dt><i class="Hui-iconfont">&#xe60c;</i>&nbsp;模板管理</dt>
            <dd></dd>
        </dl>
        <dl id="menu-video">
            <dt><i class="Hui-iconfont">&#xe650;</i> 视频管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a _href="<@com.rootPath/>/video/metadata" data-title="元数据配置" href="javascript:void(0)">监控点</a></li>
                    <li><a _href="<@com.rootPath/>/video/statistic" data-title="监控统计" href="javascript:void(0)">监控统计</a></li>
                    <li><a _href="<@com.rootPath/>/video/offline" data-title="离线统计" href="javascript:void(0)">离线统计</a></li>
                    <li><a _href="<@com.rootPath/>/video/cameraStatisticsCommon" data-title="使用频率统计表" href="javascript:void(0)">使用频率统计表</a></li>
                    <li><a _href="<@com.rootPath/>/video/log" data-title="监控日志" href="javascript:void(0)">监控日志</a></li>
                    <li><a _href="<@com.rootPath/>/video/unusedRecently" data-title="最近未使用监控告警" href="javascript:void(0)">监控告警</a></li>
                    <li><a _href="<@com.rootPath/>/project/log" data-title="系统日志" href="javascript:void(1)">系统日志</a></li>
                    <li><a _href="<@com.rootPath/>/static/js/cfg/template/resource-download.html" data-title="资源下载" href="javascript:void(0)">资源下载</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-system">
            <dt><i class="Hui-iconfont">&#xe62e;</i> 系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a _href="<@com.rootPath/>/config/dicts" data-title="数据字典" href="javascript:void(0)">数据字典</a></li>
                    <li><a _href="<@com.rootPath/>/config/fullsearch" data-title="全文检索" href="javascript:void(0)">全文检索</a></li>
                    <#--<li><a _href="<@com.rootPath/>/config/app" data-title="参数设置" href="javascript:void(0)">参数设置</a></li>-->
                    <li><a _href="<@com.rootPath/>/config/detect" data-title="控件检测" href="javascript:void(0)">控件检测</a></li>
                </ul>
            </dd>
        </dl>
    </div>
</aside>
<div class="dislpayArrow"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<#--main-->
<section class="Hui-article-box">
    <div id="Hui-tabNav" class="Hui-tabNav">
        <div class="Hui-tabNav-wp">
            <ul id="min_title_list" class="acrossTab cl">
                <li class="active"><span title="地图模板">地图模板</span><em></em></li>
            </ul>
        </div>
        <div class="Hui-tabNav-more btn-group"><a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a></div>
    </div>
    <div id="iframe_box" class="Hui-article">
        <div class="show_iframe">
            <div style="display:none" class="loading"></div>
            <iframe name="tplManager" scrolling="yes" frameborder="0"  src="<@com.rootPath/>/config/index"></iframe>
        </div>
    </div>
</section>
<@com.script name="static/thirdparty/jquery/jquery.min.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<@com.script name="static/thirdparty/h-ui/js/H-ui.js"></@com.script>
<@com.script name="static/thirdparty/h-ui/js/H-ui.admin.js"></@com.script>
<@com.script name="static/js/cfg/main.js"></@com.script>
</body>
</html>