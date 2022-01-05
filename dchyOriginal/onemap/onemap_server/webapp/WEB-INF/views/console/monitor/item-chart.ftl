<html>
<head>
    <title>chart</title>
    <meta name="tab" content="monitor"/>
    <meta name="js" content="${base}/static/js/monitor/item_chart.js"/>
</head>
<body>
<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}/console/monitor/index"><i class="icon icon-list"></i>主机列表</a>
		<a href="${base}/console/monitor/host-charts?hostId=${item.host.id}">监控图表</a>
		<a class="current">${item.name}历史数据</a>
	</div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box nopadding">
                <div class="widget-title">
                    <h5>监控图表</h5>
                </div>
                <div class="widget-content">
                	<div id="chart-container" item-id="${item.id}" item-name="${item.description!item.name}"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>