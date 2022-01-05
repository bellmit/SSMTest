<#assign tpl=tpl!>
<#assign search = search!>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>地图模板定制</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <META HTTP-EQUIV="pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <META HTTP-EQUIV="expires" CONTENT="0">
<#include "core/css-import.ftl"/>
<@com.style name="static/thirdparty/animate/animate.min.css" />
</head>
<body>
<div id="mainDiv">
    <div class="search-panel-form">
        <div class="input-group pull-left" style="width: 25%;">
            <input type="text" class="form-control" placeholder="检索地图模板" id="searchInput"/>
            <span class="input-group-btn">
                <button class="btn btn-primary" type="button" id="searchShowBtn"><i class="fa fa-search"></i></button>
            </span>
        </div>
        <div class="pull-left" style="margin-left: 20px;">
            <a id="addBtn" class="btn btn-primary" title="添加地图模板" data-toggle="modal"><i class="fa fa-plus"></i>&nbsp;新建</a>
        </div>
    </div>
    <div class="tpls-content">
        <div class="tpls-list">
        </div>
    </div>
</div>
<#include "core/js-import.ftl"/>
<script type="text/javascript">
    require(["static/js/cfg/tpl-list-display"], function (list) {
        list.init();
    });
</script>
</body>
</html>