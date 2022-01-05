<html>
<head>
    <title>chart</title>
    <meta name="tab" content="monitor"/>
    <meta name="js" content="${base}/static/js/monitor/item_chart.js"/>
</head>
<body>

<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}"><i class="icon-tasks"></i>服务列表</a>
        <a href="${base}/console/map/edit?id=${map.id}">编辑服务</a>
        <a class="current">监控</a>
    </div>
</div>
<!-- /#breadcrumbWrapper -->

<div class="container-fluid">
    <div class="row-fluid">
        <div class="widget-box span12">
            <div class="widget-title">
                <ul class="nav nav-tabs">
                    <li><a href="${base}/console/map/edit?id=${map.id}">基本信息</a></li>
                    <li><a href="${base}/console/provider/showProviders/${map.id}">服务提供者</a></li>
                    <li><a href="${base}/console/layer/showLayers/${map.id}">图层信息</a></li>
                    <li class="active"><a href="#">监控</a></li>
                </ul>
            </div>
            <!-- widget-title -->


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
        </div>
    </div>
</div>
</body>
</html>