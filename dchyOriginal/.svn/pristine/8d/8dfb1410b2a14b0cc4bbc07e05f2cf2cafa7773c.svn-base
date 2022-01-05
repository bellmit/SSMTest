<html>
<head>
    <title>监控${host.name!}</title>
    <meta name="tab" content="monitor"/>
    <meta name="js" content="${base}/static/js/module_chart.js" />
</head>
<body>
<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}/console/monitor/index"><i class="icon icon-list"></i>主机列表</a>
		<a class="current">监控项配置</a>
	</div>
</div>
<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	
    <br />
    <h4>${host.name!} <small>监控项配置</small></h4>
	<div class="row-fluid">
		<div class="span12">
            <div class="widget-box">
				<div class="widget-title">
	                <h5>监控项列表</h5>
					<div class="buttons">
						<#if host.fixed>
							<a href="${base}/console/monitor/importMaps?hostId=${host.id}" data-ask="现在导入所有未被监控的地图?" class="btn btn-mini btn-primary j_btn_del">导入所有地图</a>
						<#else>
							<a href="#J_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_MODAL" url="${base}/console/monitor/item/ajax/edit?hostId=${host.id}">添加监控项</a>
						</#if>
					</div>
	            </div><!-- /.widget-title -->
	            <div class="widget-content nopadding">
	            	<table class="table table-bordered">
	            		<thead>
	            			<tr>
	            				<th width="20%">名称</th>
	            				<th width="30%">描述</th>
	            				<th>操作</th>
	            			</tr>
	            		</thead>
	            		<tbody>
	            			<#list items as item>
	            			<tr>
	            				<td><a href="#">${item.name!}</a></td>
	            				<td>${item.description!}</td>
	            				<td>
	            					<a href="#J_MODAL" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data" data-result-ctn="#J_MODAL" url="${base}/console/monitor/item/ajax/edit?hostId=${host.id}&itemId=${item.id}">编辑</a>
	            					<#if item.enabled>
	            					<a href="${base}/console/monitor/item/toggle?itemId=${item.id}" class="btn btn-mini btn-danger">禁用</a>
	            					<#else>
	            					<a href="${base}/console/monitor/item/toggle?itemId=${item.id}" class="btn btn-mini btn-success">启用</a>
	            					</#if>
	            					<a href="${base}/console/monitor/item/remove?itemId=${item.id}" class="btn btn-mini btn-inverse j_btn_del" data-ask="确定删除该监控项？">删除</a>
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
</body>
</html>