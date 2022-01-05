<html>
<head>
    <title>chart</title>
    <meta name="tab" content="monitor"/>
    <meta name="js" content="${base}/static/js/monitor/all-map.js"/>
</head>
<body>
<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}/console/monitor/index"><i class="icon icon-list"></i>主机列表</a>
		<a class="current">地图访问统计</a>
	</div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <h5>主机 ${host.name} 地图访问统计</h5>
                </div>
                <div class="widget-content nopadding">
                	<div class="form-inline-wrapper">
	                	<form action="all" class="form-inline">
	                		<label for="start">起始时间 <input id="start" type="text" name="start" class="datepicker middle" value="${start!}"/></label>
	                		<label for="end">结束时间 <input id="end" type="text" name="end" class="datepicker middle" value="${end!}"/></label>
	                		<label for="maxSize">最大地图数 <input id="maxSize" class="small" name="maxSize" type="text" value="${max!}"/></label>
	                		<input type="hidden" name="hostId" value="${host.id}"/>
	                		<input type="submit" class="btn btn-primary" value="查询"/>
	                	</form>
                	</div>
                	<div id="dataCtn">
                		
                	</div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
var item_data = ${item_data};
</script>
</body>
</html>