<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>一张图运维子系统 - ${_title!}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${base}/static/wro/console.css"/>
    <script src="${base}/static/js/jquery/jquery-1.8.3.js"></script>
    <script type="text/javascript">
        var _ctx = '${base}';
    </script>

</head>

<body>

<div id="header">
    <h1>江苏省国土资源管理一张图运维管理子系统</h1>
</div>

<div id="user-nav" class="navbar navbar-inverse">
    <ul class="nav btn-group">
        <li class="btn btn-inverse">
            <a href="#J_USER_CONFIG" data-toggle="modal" data-result-ctn="#J_USER_CONFIG" url="${base}/ajax/userConfig" class="j_ajax_for_data"><i class="icon icon-user"></i> <span class="text"><@sec.name/></span></a>
        </li>
        <li class="btn btn-inverse">
            <a href="${base}/logout" class="j_ask" data-ask="您确认退出系统吗？"><i class="icon icon-share-alt"></i> <span class="text">登出</span></a>
        </li>
    </ul>
</div>

<div id="sidebar">
    <a id="J_PHONE_NAV_TOP" href="#" class="visible-phone"><i class="icon icon-tasks"></i><span>服务管理</span></a>

    <ul style="display: block;">
        <li${(_meta.tab=='map')?string(' class="active"','')}><a href="${base}/console/map/index"><i class="icon icon-globe"></i><span>地图服务</span></a></li>
        <li${(_meta.tab=='map_group')?string(' class="active"','')}><a href="${base}/console/group/index"><i class="icon icon-th"></i><span>地图分类</span></a></li>
        <li${(_meta.tab=='ds')?string(' class="active"','')}><a href="${base}/console/ds/index"><i class="icon icon-list-alt"></i><span>数据源配置</span></a></li>
        <li${(_meta.tab=='index')?string(' class="active"','')}><a href="${base}/console/index/index"><i class="icon icon-flag"></i><span>索引配置</span></a></li>
        <li${(_meta.tab=='task')?string(' class="active"','')}><a href="${base}/console/task/index"><i class="icon icon-tasks"></i><span>任务列表</span></a></li>
        <li${(_meta.tab=='region')?string(' class="active"','')}><a href="${base}/console/region/index"><i class="icon icon-road"></i><span>行政区配置</span></a></li>
        <li${(_meta.tab=='auth')?string(' class="active"','')}><a href="${base}/console/security/index"><i class="icon icon-eye-close"></i><span>权限配置</span></a></li>
        <li${(_meta.tab=='monitor')?string(' class="active"','')}><a href="${base}/console/monitor/index"><i class="icon icon-screenshot"></i><span>系统监控</span></a></li>
        <li${(_meta.tab=='logging')?string(' class="active"','')}><a href="${base}/console/log/arcgis/query"><i class="icon icon-book"></i><span>日志管理</span></a></li>
        <li><a href="/omp/config"><i class="icon icon-picture"></i><span>模板配置</span></a></li>
        <li${(_meta.tab=='cache')?string(' class="active"','')}><a href="${base}/console/cache"><i class="icon icon-wrench"></i><span>缓存管理</span></a></li>
    </ul>
</div>
<div id="content">${_body}</div>
<ul class="typeahead dropdown-menu"></ul>
<div id="J_USER_CONFIG" class="modal hide fade"></div>
<script src="${base}/static/wro/console.js"></script>
<script src="${base}/static/js/global_user_config.js"></script>
<script src="${_meta.js}"></script>
</body>
</html>