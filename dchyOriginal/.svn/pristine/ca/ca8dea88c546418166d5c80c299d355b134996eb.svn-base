<#-- @ftlvariable name="items" type="java.util.List<cn.gtmap.onemap.server.monitor.model.Item>" -->
<html>
<head>
    <title>chart</title>
    <meta name="tab" content="monitor"/>
    <meta name="js" content="${base}/static/js/monitor/host_charts.js"/>
</head>
<body>
<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}/console/monitor/index"><i class="icon icon-list"></i>主机列表</a>
        <a class="current">监控图表</a>
    </div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <h5>主机 ${host.name} 监控图表</h5>
                </div>
                <div class="widget-content">
                <#list page.content as item>
                    <h5>${item.name}
                        <small><a href="chart?id=${item.id}">[查看历史数据]</a></small>
                    </h5>
                    <div class="chart-item" item-id="${item.id}" item-name="${item.description!item.name}"></div>
                </#list>
                </div>
                <div id="J_PAGIN" class="pagination alternate clearfix">
                    <a href="#" class="prev">&lt;</a><span></span><a href="#" class="next">&gt;</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    window.onload = function () {
        refreshPagin('#J_PAGIN', ${page.number}, ${page.size}, ${page.totalElements}, 10);
    };
</script>
</body>
</html>