<#-- core -->

<#-- base html -->
<#macro html title="" import="">
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width">
    <title>${title}</title>
    <script type="text/javascript" language="javascript">
        var rootPath = "<@com.rootPath/>";
    </script>
    <#if import??>
        <#list import?split(",") as lib>
            <#if lib?trim != "">
                <#switch lib>
                    <#case "jquery">
                        <@script name="resources/js/vendor/jquery-1.10.1.min.js"></@script>
                        <#break />
                    <#case "swf">
                        <@script name="resources/js/swfobject.js"></@script>
                        <#break />
                    <#case "bootstrap">
                        <@script name="resources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></@script>
                        <@script name="resources/js/vendor/bootstrap.min.js"></@script>
                        <@style name="resources/css/bootstrap-flaty.css"></@style>
                        <@style name="resources/css/bootstrap-responsive.css"></@style>
                        <#break/>
                    <#case "main">
                        <@style name="resources/css/main.css"></@style>
                        <@script name="resources/js/main.js"></@script>
                        <@style name="resources/css/messenger/messenger.css"></@style>
                        <@style name="resources/css/messenger/messenger-theme-flat.css"></@style>
                        <#break/>
                    <#default>
                        <@style name="resources/css/${lib}.css"></@style>
                        <@script name="resources/js/${lib}.js"></@script>
                </#switch>
            </#if>

        </#list>
    </#if>
</head>
<body>
    <#nested />
</body>
</html>
</#macro>

<#-- main html -->
<#macro main title="">
    <@html title="${title!}" import="jquery,bootstrap,main,plugins">

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a class="brand" href="#">Project name</a>

                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li class="active"><a href="#">Home</a></li>
                        <li><a href="#about">About</a></li>
                        <li><a href="#contact">Contact</a></li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b
                                    class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li class="nav-header">Nav header</li>
                                <li><a href="#">Separated link</a></li>
                                <li><a href="#">One more separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                    <form class="navbar-form pull-right">
                        <input class="span2" type="text" placeholder="Email">
                        <input class="span2" type="password" placeholder="Password">
                        <button type="submit" class="btn">Sign in</button>
                    </form>
                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>

    <div class="container">

        <#nested />

    </div> <!-- /container -->

    </@html>
</#macro>

<#-- simple html -->
<#macro simple title="">
    <@html title="${title!}" import="jquery,bootstrap,main,plugins">

    <div id="container" class="container">

        <#nested />

    </div> <!-- /container -->

    </@html>
</#macro>


<#-- flash -->
<#macro swf title="" swfname="">
    <@com.html title="${title!}" import="swf">
    <style type="text/css" media="screen">
        html, body {
            height: 100%;
        }

        body {
            margin: 0;
            padding: 0;
            overflow: auto;
            text-align: center;
            background-color: #25639c;
        }

        object:focus {
            outline: none;
        }

        #flashContent {
            top: 50%;
            width: 360px;
            margin: 0px auto;
        }

        #flashContent p {
            color: #ffffff;
        }
    </style>

        <#nested />

    <script type="text/javascript">
        var swfVersionStr = "11.5.0";
        var xiSwfUrlStr = "playerProductInstall.swf";
        var flashvars = {};
        var params = {};
        params.quality = "high";
        params.bgcolor = "#fcfffb";
        params.allowscriptaccess = "sameDomain";
        //params.allowfullscreen = "true";
        params.allowFullScreenInteractive = "true";
        params.wmode = "direct";
        var attributes = {};
        attributes.id = "${swfname}";
        attributes.name = "${swfname}";
        attributes.align = "middle";
        swfobject.embedSWF(
                "<@rootPath/>/resources/flash/${swfname}.swf", "flashContent",
                "100%", "100%",
                swfVersionStr, xiSwfUrlStr,
                flashvars, params, attributes);
        swfobject.createCSS("#flashContent", "display:block;text-align:left;");
    </script>

    <div id="flashContent">
        <p>
            亲，难道你不知道看Flash要装Adobe Flash Player的么，记得安装11.5.0以上的版本哦！<br/>
            点击以下链接即可下载安装哦~<br/>安装后记得重启浏览器。
        </p>
        <script type="text/javascript">
            var axUrl = "<@rootPath/>/bin/install_flash_player_ax.exe";
            var otherUrl = "<@rootPath/>/bin/install_flash_player.exe";
            var onlineUrl = "http://get.adobe.com/cn/flashplayer/";
            var playerUrl = window.ActiveXObject ? axUrl : otherUrl;
            document.write("<a href='" + onlineUrl + "'><images src='<@rootPath/>/images/get_flash_player.gif'/> </a>");
        </script>
    </div>
    </@com.html>
</#macro>

<#-- import js -->
<#macro script name>
<script src="<@rootPath/>/${name}" type="text/javascript"></script>
</#macro>

<#-- import css -->
<#macro style name>
<link href="<@rootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>

<#-- root path -->
<#macro rootPath>${springMacroRequestContext.getContextPath()}</#macro>

