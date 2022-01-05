<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>地图属性配置-${tpl!}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#include "core/css-import.ftl"/>
    <@com.style name="static/thirdparty/h-ui/lib/iconfont/iconfont.css"></@com.style>
    <style>
        .wizard-container{
            clear: both;
            width: 100%;
        }
        .wizard-container .tab-btn{
            margin:5px 20px 0 0;
        }
    </style>
</head>
<body>
<div class="cfg-nav row">
    <div class="col-xs-7 col-xs-offset-4 text-center">
        <ul class="nav nav-pills" role="tablist">
            <li role="presentation"><a href="<@core.rootPath/>/config/index">&nbsp;模板列表&nbsp;&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/services?tpl=${tpl!}">&nbsp;服务配置&nbsp;&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/widgets?tpl=${tpl!}">&nbsp;功能配置&nbsp;</a></li>
            <li role="presentation" class="nav-active"><a href="<@core.rootPath/>/config/tpl/attrs?tpl=${tpl!}">&nbsp;属性配置&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/clearcache?tpl=${tpl!}">&nbsp;缓存清理&nbsp;</a></li>
        </ul>
    </div>
    <div class="clo-xs-1 pull-right">
        <a class="btn btn-info radius" href="<@core.rootPath/>/map/${tpl!}" title="预览模板" style="margin-right: 5px" target="_blank" >
            <i class="fa fa-eye" aria-hidden="true" style="color:white"></i>
        </a>
        <a class="btn btn-success radius" href="javascript:location.replace(location.href);" title="刷新" >
            <i class="fa fa-refresh" aria-hidden="true" style="color:white"></i>
        </a>
    </div>
</div>
<div  id="sectionAttrs" style="padding-left:10px">
    <div class="attrs-info"></div>
</div>
<#include "core/js-import.ftl"/>
<script type="text/javascript">
    $('.dropdown-toggle').dropdown();
    require(["static/js/cfg/tpl-attr-display"], function (TplAttrDisplay) {
        TplAttrDisplay.init('${tpl!}');
    });
</script>
</body>
</html>
