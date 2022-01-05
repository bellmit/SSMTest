<#assign tpl=tpl!>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>缓存清理-${tpl!}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#include "core/css-import.ftl"/>
</head>
<body>
<div class="cfg-nav row">
    <div class="col-xs-7 col-xs-offset-4 text-center">
        <ul class="nav nav-pills" role="tablist">
            <li role="presentation"><a href="<@core.rootPath/>/config/index">&nbsp;模板列表&nbsp;&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/services?tpl=${tpl!}">&nbsp;服务配置&nbsp;&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/widgets?tpl=${tpl!}">&nbsp;功能配置&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/attrs?tpl=${tpl!}">&nbsp;属性配置&nbsp;</a></li>
            <li role="presentation" class="nav-active"><a href="<@core.rootPath/>/config/tpl/clearcache?tpl=${tpl!}">&nbsp;缓存清理&nbsp;</a></li>
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
<div role="tabpanel" class="tab-pane fade in text-center" id="clearCache">
    <div class="clear-cache-container">
        <a class="btn btn-default" data-type="service">清理服务缓存</a>
        <a class="btn btn-default" data-type="region">清理行政区缓存</a>
    </div>
    <style scoped>
        #clearCache{
            margin-top: 20px;
        }
    </style>
</div>
</body>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<script type="text/javascript">
    $(".clear-cache-container").on('click','a',function(){
        var $this=$(this);
        var t=$this.data("type");
        var url=undefined;
        switch(t){
            case 'service':
                url='<@com.rootPath/>/map/'+ '${tpl!}' +'/clearServiceCache';
                break;
            case 'region':
                url='<@com.rootPath/>/map/'+ '${tpl!}' +'/clearRegionCache';
                break;
        }
        if (url != undefined) {
            layer.msg("执行中...", {time: 3000, shade: 0.35, shadeClose: !1});
            $.ajax({
                url: url,
                complete: function (evt) {
                    var r = $.parseJSON(evt.responseText);
                    if (r === true) {
                        layer.msg("缓存已清除!", {time: 1000});
                    }
                }
            });
        }
    });
</script>
</html>
