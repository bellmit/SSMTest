<html>
<head>
    <title>行政区管理</title>
    <meta name="tab" content="region"/>
    <meta name="js" content="${base}/static/js/page_region_mgr.js" />
</head>
<body>
	<div class="container-fluid">
		
		<#include "../../common/ret.ftl" />
	
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<h5>行政区列表</h5>
						<div class="buttons j_btns">
							<a id="J_ADD_PROVINCE" url="${base}/console/region/ajax/edit" data-result-ctn="#J_REGION_EDIT" href="#J_REGION_EDIT" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data">添加省</a>
							<a id="J_ADD_CITY" url="${base}/console/region/ajax/edit" href="#J_REGION_EDIT" data-toggle="modal" data-result-ctn="#J_REGION_EDIT" class="btn btn-mini btn-primary hide j_ajax_for_data">添加市</a>
							<a id="J_ADD_DINSTINCT" url="${base}/console/region/ajax/edit" href="#J_REGION_EDIT" data-toggle="modal" data-result-ctn="#J_REGION_EDIT" class="btn btn-mini btn-primary hide j_ajax_for_data">添加区县</a>
							<a id="J_ADD_VILAGE" url="${base}/console/region/ajax/edit" href="#J_REGION_EDIT" data-toggle="modal" data-result-ctn="#J_REGION_EDIT" class="btn btn-mini btn-primary hide j_ajax_for_data">添加乡村</a>
							<a id="J_ADD_ROAD" url="${base}/console/region/ajax/edit" href="#J_REGION_EDIT" data-toggle="modal" data-result-ctn="#J_REGION_EDIT" class="btn btn-mini btn-primary hide j_ajax_for_data">添加街道</a>
							<a href="#J_REGION_EDIT" data-toggle="modal" data-result-ctn="#J_REGION_EDIT" url="${base}/console/region/ajax/edit" class="btn btn-mini btn-info hide j_normal j_ajax_for_data j_edit_region">编辑</a>
							<a href="${base}/console/region/remove" class="btn btn-mini btn-danger hide j_normal j_remove_region">删除</a>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered small table-hover gridtree">
							<thead>
								<tr>
									<th style="width:70%">名称</th>
									<th>行政代码</th>
									<th>类型</th>
								</tr>
							</thead>
							<tbody>
								<#list regions?sort_by("id") as r>
									<tr id="${r.id}" data-url="${base}/console/region/ajax/fetch?parentId=${r.id}" <#if r.parent??>data-parent="${r.parent.id}"</#if> data-level="${r.level.name()}" data-has-child="${r.hasChildren()?string}">
										<td style="width:70%"><i class="region-icon region-${r.level.name()}"></i>${r.name}</td>
										<td class="fn-tc">${r.id}</td>
										<td class="fn-tc">${r.level.label}</td>
									</tr>
									<#if r.hasChildren()>
										<#list r.children?sort_by("id") as rc>
										<tr id="${rc.id}" data-url="${base}/console/region/ajax/fetch?parentId=${rc.id}" <#if rc.parent??>data-parent="${rc.parent.id}"</#if> data-level="${rc.level.name()}" data-has-child="${rc.hasChildren()?string}">
											<td style="width:70%"><i class="region-icon region-${rc.level.name()}"></i>${rc.name}</td>
											<td class="fn-tc">${rc.id}</td>
											<td class="fn-tc">${rc.level.label}</td>
										</tr>
										</#list>
									</#if>
								</#list>
							</tbody>
						</table>
					</div>
				</div><!-- widget -->
			</div><!-- span -->
		</div><!-- row -->
	</div><!-- container -->
<div id="J_REGION_EDIT" class="modal fade hide">
	
</div>
</body>
</html>