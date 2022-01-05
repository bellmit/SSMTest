<html>
<head>
    <title>监控${host.name!}</title>
    <meta name="tab" content="monitor"/>
</head>
<body>
<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}/console/monitor/index"><i class="icon icon-list"></i>主机列表</a>
		<a class="current">数据接口配置</a>
	</div>
</div>
<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	
    <br />
    <h4>${host.name!} <small>数据接口配置</small></h4>
	<div class="row-fluid">
		<div class="span12">
			<div class="widget-box">
				<div class="widget-title">
	                <h5>数据接口列表</h5>
					<div class="buttons">
						<a href="#J_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_MODAL" url="${base}/console/monitor/inf/ajax/edit?hostId=${host.id}">添加数据接口</a>
					</div>
	            </div><!-- /.widget-title -->
	            <div class="widget-content nopadding">
	            	<table class="table table-bordered small">
	            		<thead>
	            			<tr>
	            				<th width="30%">说明</th>
	            				<th width="20%">数据接口类型</th>
	            				<th>操作</th>
	            			</tr>
	            		</thead>
	            		<tbody>
	            			<#list infs as inf>
	            			<tr>
	            				<td>${inf.description!}</td>
	            				<td class="inf-name">${inf.name}</td>
	            				<td>
	            					<a href="#J_MODAL" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data" data-result-ctn="#J_MODAL" url="${base}/console/monitor/inf/ajax/edit?hostId=${host.id}&infId=${inf.id}">编辑</a>
	            					<#if inf.enabled>
	            					<a href="${base}/console/monitor/inf/toggle?infId=${inf.id}" class="btn btn-mini btn-danger">禁用</a>
	            					<#else>
	            					<a href="${base}/console/monitor/inf/toggle?infId=${inf.id}" class="btn btn-mini btn-success">启用</a>
	            					</#if>
	            					<a href="${base}/console/monitor/inf/remove?infId=${inf.id}" class="btn btn-mini btn-inverse j_btn_del" data-ask="删除该数据接口会同时删除其所有关联数据，确定吗？">删除</a>
	            				</td>
	            			</tr>
	            			</#list>
	            		</tbody>
	            	</table>
	            </div>
            </div>
		</div><!-- span -->
	</div><!-- row -->
</div><!-- /.container-fluid -->
<div id="J_MODAL" class="modal fade in hide">
	
</div>

<script>
document.ready = function(){
	var page_monitor = {};
	page_monitor.xnamemap = {
		'db' : '数据库监控',
		'zabbix' : '服务器常规监控'
	}
	$('.inf-name').each(function(){
		var self = $(this);
		self.html( page_monitor.xnamemap[self.html()] );
	});
}
</script>
</body>
</html>