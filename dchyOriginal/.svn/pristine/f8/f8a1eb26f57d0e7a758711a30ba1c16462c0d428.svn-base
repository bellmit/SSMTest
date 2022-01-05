<#macro main nav="" js="" css="" showFooter="true">
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
	<title>${env.getEnv('local.title')}国土资源“一张图”-信息门户子系统</title>
	<link rel="stylesheet" href="<@com.rootPath/>/static/css/bootstrap/bootstrap.css" />
	<link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/font/css/font-awesome.css" />
	<link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/gridtree/gridtree.css" />
	<link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/loading/loading.css" />
	<!--[if IE 8]>
	<link rel="stylesheet" href="<@com.rootPath/>/static/css/hack.css" />
	<![endif]-->
	${css!}
</head>
	<body>
		<div id="hearder" class="navbar navbar-inverse">
        <div class="navbar-inner">
            <div class="container">
                <a href="<@com.rootPath/>/portal2" class="brand" style="background: url(${path_omp}/resources/img/${env.getEnv('local.path')}/logo2.png) 0 0 no-repeat;">${env.getEnv('local.title')}国土资源“一张图”信息门户</a>
                <div class="pull-right">
                    <div class="small-nav">
                        <ul class="nav nav-pills">
                            <li><a><i class="icon icon-user"></i>&nbsp;您好！<@sec.name/>&nbsp;${ctx.getTitle()!}</a></li>
                            <li><a href="<@core.rootPath/>">主页</a></li>
                            <#if env.getEnv('main.level')='provincial'>
                            <li><a href="http://172.18.2.70/homepages/singleLogin.jsp?license=${ctx.getToken()!}" target="_blank">政务管理平台</a></li></#if>
                            <li><a href="#">帮助</a></li>
                            <#if env.getEnv('main.level')='provincial'>
                                <li><a href="#" data-ask="确定退出系统吗?" class="i_quit">&nbsp;退出</a></li>
                            <#else>
                                <li><a href="${path_oms}/logout?url=${path_omp}" data-ask="确定退出系统吗?" class="j_ask"><i class="icon icon-signout"></i>&nbsp;注销</a></li>
                            </#if>
                        </ul>
                    </div>
                    <div class="main-nav">
                        <#if env.getEnv('main.level')='provincial'>
                            <ul class="nav nav-pills">
                                <li class="<#if nav=="onemap">active</#if>"><a
                                        href="<@com.rootPath/>/portal2/onemap" target="_self">一张图</a>
                                </li>
                                <li class="<#if nav=="zt">active</#if>"><a href="<@com.rootPath/>/portal2/tpl/index"
                                                                           target="_self">专题图</a></li>

                                <li class="<#if nav=="chart">active</#if>"><a href="<@com.rootPath/>/portal2/statistic"
                                                                              target="_self">统计图表</a></li>
                                <li class="<#if nav=="res">active</#if>"><a href="<@com.rootPath/>/portal2/rescenter/db"
                                                                            target="_self">资源中心</a></li>
                                <li class="<#if nav=="api">active</#if>"><a href="<@com.rootPath/>/portal2/api/index"
                                                                            target="_self">地图API</a></li>
                            </ul>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#nested />
    <#if showFooter=='true'>
    <div class="footer">
        <div class="container" style="text-align: center">
            &copy;${env.getEnv('local.title')}国土资源信息中心 Copyright Reserved 2004-2013 All Rights Reserved 版权所有
        </div>
    </div>
    </#if>
    <script src="<@com.rootPath/>/static/js/portal/lib/jquery-1.10.2.min.js"></script>
    <script src="<@com.rootPath/>/static/js/portal/lib/bootstrap.js"></script>
    <script src="<@com.rootPath/>/static/js/plugins.js"></script>
    <script src="<@com.rootPath/>/static/js/main.js"></script>
    ${js!}
	<script>
        $(function(){
            $('.i_quit').click(function(){
                window.opener=null;
                window.open('','_self');
                window.close();
            });
        })
	</script>
</body>
</html>
</#macro>
<#macro treeMenu nodes="">
    <#list nodes as item>
        <#assign children=item.children>

        <li id="${item["id"]!}" data-has-child="<#if (children?size>0)>true<#else>false</#if>"
            data-parent="${item["parentId"]!}">
                <a href="<#if (item["parentId"]??)><#if env.hasStatisAuth(item.name)><#if (item.url?length>0)>${item["url"]}<#else>#</#if><#else><@com.rootPath/>/portal2/forbidden</#if><#else>#</#if>" target="iframe">
                    <i class="icon  icon-<#if (children?size>0)>folder-open<#else>copy</#if>"></i><#if (children?size>0)>
                <strong><#else></#if><span>${item["name"]}</span><#if (children?size>0)></strong><#else></#if></a>


        </li>
        <@base.treeMenu nodes=children></@base.treeMenu>

    </#list>
</#macro>
