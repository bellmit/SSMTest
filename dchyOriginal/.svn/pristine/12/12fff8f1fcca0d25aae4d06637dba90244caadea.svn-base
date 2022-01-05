<#macro tpl showHeader="true" css="" bootVersion="">
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>${env.getEnv('local.title')}国土资源“一张图”-数据中心</title>
    <link rel="shortcut icon" type="image/x-icon" href="<@com.rootPath/>/static/img/favicon.ico" media="screen"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/thirdparty/bootstrap/css/bootstrap-v3.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/font/css/font-awesome.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/loading/loading.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/hack.css" />
    ${css!}
    <script src="<@com.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js"></script>
    <script src="<@com.rootPath/>/static/js/portal/lib/bootstrap-v3.js"></script>
    <script src="<@com.rootPath/>/static/js/plugins.js"></script>

</head>
<body>
<#if showHeader=="true">
<div id="hearder" class="navbar navbar-inverse">
    <div class="navbar-inner">
        <div class="container">
            <a href="<@com.rootPath/>/portal2" class="brand" style="background: url(${path_omp}/resources/img/${env.getEnv('local.path')}/logo2.png) 0 0 no-repeat;">${env.getEnv('local.title')}国土资源“一张图”信息门户</a>
            <div class="pull-right">
                <div class="small-nav">
                    <ul class="nav nav-pills">
                        <li><a href="${path_omp}">主页</a></li>
                        <#if env.getEnv('main.level')='provincial'>
                            <li><a href="http://172.18.2.70/homepages/singleLogin.jsp?license=${ctx.getToken()!}" target="_blank">政务管理平台</a></li></#if>
                        <li><a href="#">帮助</a></li>
                        <#if env.getEnv('main.level')='provincial'>
                            <li><a href="#" data-ask="确定退出系统吗?" class="i_quit">&nbsp;退出</a></li>
                        <#else>
                            <li><a href="${path_oms}/logout?url=${path_omp}" data-ask="确定退出系统吗?" class="j_ask"><i class="icon icon-signout"></i>&nbsp;退出</a></li>
                        </#if>
                    </ul>
                </div>
                <div class="main-nav">
                    <ul class="nav nav-pills">
                        <li><a href="<@com.rootPath/>/portal2/onemap/11">一张图</a></li>
                        <li><a href="<@com.rootPath/>/portal2/tpl/index">专题图</a></li>
                        <li><a href="#">统计报表</a></li>
                        <li><a href="<@com.rootPath/>/portal2/rescenter/db">资源中心</a></li>
                        <li><a href="<@com.rootPath/>/portal2/api/index">地图API</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</#if>
<!--[if lt IE 9]>

<script src="<@com.rootPath/>/static/js/hack/html5shiv.js"></script>

<script src="<@com.rootPath/>/static/js/hack/respond.min.js"></script>

<![endif]-->
<#nested />
</body>
</html>
</#macro>
