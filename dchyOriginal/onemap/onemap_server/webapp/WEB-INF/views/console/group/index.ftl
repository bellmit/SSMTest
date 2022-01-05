<html>
<head>
    <title>地图分类管理</title>
    <meta name="tab" content="map_group"/>
    <meta name="submenu" content="map_group"/>
    <meta name="js" content="${base}/static/js/page_group_index.js" />
</head>
<body>

<div class="container-fluid">
	<#include "../../common/ret.ftl" />
    <div class="row-fluid">

        <div class="span12">
        	
            <div class="widget-box">

                <div class="widget-title">
                    
                    <h5>地图分类列表</h5>
					
					<div class="buttons">
						<a href="#J_EDIT_GROUP_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" url="${base}/console/group/ajax/edit">创建地图分类</a>
						
					</div>
                </div>
                <!-- /.widget-title -->

                <div class="widget-content nopadding">
					
					<table class="table table-bordered table-striped table-hover table-condensed">
						<thead>
							<tr>
								<th width="50%">名称</th>
								<th width="15%">创建时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<#list groups as g>
							<tr id="${g.id}" <#if g.parent??>data-parent="${g.parent.id}"</#if> <#if g.children??>data-has-child="${g.children?size!}</#if>">
								<td>${g.name}</td>
								<td><div class="fn-tc">${g.createAt}</div></td>
								<td>
									<a href="#J_EDIT_GROUP_MODAL" class="btn btn-mini btn-info j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" data-toggle="modal" url="${base}/console/group/ajax/edit?id=${g.id}">编辑</a>
									<a href="#J_EDIT_GROUP_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" url="${base}/console/group/ajax/edit?parentId=${g.id}&">创建子分类</a>
									<a href="${base}/console/group/remove?id=${g.id}" class="btn btn-mini btn-danger j_btn_del">删除</a>
								</td>
							</tr>
								<#if (g.children?size>0)>
								<#list g.children as gc>
									<tr id="${gc.id}" <#if gc.parent??>data-parent="${gc.parent.id}"</#if> data-has-child="${gc.children?size!}" data-url="${base}/console/group/ajax/fetch?parentId=${gc.id}">
										<td>${gc.name}</td>
										<td><div class="fn-tc">${gc.createAt}</div></td>
										<td>
											<a href="#J_EDIT_GROUP_MODAL" class="btn btn-mini btn-info j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" data-toggle="modal" url="${base}/console/group/ajax/edit?id=${gc.id}">编辑</a>
											<a href="#J_EDIT_GROUP_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" url="${base}/console/group/ajax/edit?parentId=${gc.id}&">创建子分类</a>
											<a href="${base}/console/group/remove?id=${gc.id}" class="btn btn-mini btn-danger j_btn_del">删除</a>
										</td>
									</tr>
								</#list>
								</#if>
							</#list>
						</tbody>
					</table>

                </div>
                <!-- /widget-content -->

            </div>
            <!-- /widget-box -->

        </div>
        <!-- /.span12 -->

    </div>
    <!-- /.row-fluid -->

</div>
<!-- /container-fluid -->
<div id="J_EDIT_GROUP_MODAL" class="modal fade hide">
	
</div>
</body>
</html>