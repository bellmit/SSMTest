<html>
<head>
    <title>组织机构与安全</title>
    <meta name="tab" content="auth"/>
    <meta name="js" content="${base}/static/js/page_privilege_mgr.js" />
</head>
<body>
<div class="container-fluid">
    <#include "../../common/ret.ftl" />
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                   	<ul class="nav nav-tabs">
						<li>
							<a href="${base}/console/security/index">用户管理</a>
						</li>
						<li>
							<a href="${base}/console/security/role">角色管理</a>
						</li>
						<li class="active">
							<a href="${base}/console/security/privilege">资源管理</a>
						</li>
					</ul>
                </div>
                <div class="widget-content ">
                	
                	<div class="buttons-opt-wrap">
                		<div class="title-wrapper">
                			<h6>资源列表</h6>
                		</div>
                		<div class="buttons">
                			<a id="J_BTN_ROOT_PRI" href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/edit" data-toggle="modal" class="btn btn-primary btn-mini j_ajax_for_data">创建根资源</a>
                		</div>
                	</div>
                	
                    <table class="table table-bordered table-condensed">
                        <thead><tr>
                            <th width="30%">名称</th>
                            <th>资源</th>
                            <th>资源操作方式</th>
                            <th width="30%">操作</th>
                        </tr></thead>
                        <tbody>
                        	<#if perms??>
                        	<#list perms as perm>
                        		<tr class="<#if perm.enabled>j_enabled<#else>j_disabled</#if>" 
                        		id="${perm.id}" 
                        		<#if perm.parent??>data-parent="${r.parent.id}"</#if> 
                        		data-has-child="${perm.hasChildren()?string}"
                        		data-url="${base}/console/security/privilege/ajax/fetch/parentId=${perm.id}"
                        		>
                        			<td><i class="region-icon gridtree-root"></i>${perm.title!}</td>
                        			<td>${perm.resource!}</td>
                        			<td>
                        				<#if perm.getOperationsMap()??>
                        					<div class="label-wrap">
                        					<#assign optMap = perm.getOperationsMap()/>
                        					<#assign opts = perm.getOperations()/>
                        					<#list optMap?keys as optK>
	                        					<a class="label label-info <#if opts?seq_contains(optMap[optK])>j_self_opt</#if>" data-url="privilege/removeOperation?privilegeId=${perm.id}&operationId=${optMap[optK].id}">${optMap[optK].title!}</a>
                        					</#list>
                        					</div>
                        				</#if>
                        			</td>
                        			<td class="fn-tc">
                        				<#if perm.enabled>
                        				<a href="${base}/console/security/privilege/ajax/toggle?privilegeId=${perm.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
			                            <#else>
			                            <a href="${base}/console/security/privilege/ajax/toggle?privilegeId=${perm.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
			                            </#if>
                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/edit?privilegeId=${perm.id}" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/editOperation?privilegeId=${perm.id}" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data">添加操作</a>
                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/edit?parentId=${perm.id}" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data">创建子资源</a>
                        			</td>
                        		</tr>
                        		<#if perm.hasChildren()>
                        			<#list perm.sortChildren as permChild>
                        			<tr 
                        			class="<#if permChild.enabled>j_enabled<#else>j_disabled</#if>" 
                        			id="${permChild.id}" 
                        			<#if permChild.parent??>data-parent="${permChild.parent.id}"</#if>
                        			data-has-child="${permChild.hasChildren()?string}" 
                        			<#if permChild.hasChildren()>
                        			data-url="${base}/console/security/privilege/ajax/fetch?parentId=${permChild.id}"</#if> >
                        			
                        			
                        			
	                        			<td><i class="region-icon gridtree-level2"></i>${permChild.title!}</td>
	                        			<td>${permChild.resource!}</td>
	                        			<td>
	                        				<#if permChild.getOperationsMap()??>
	                        					<div class="label-wrap">
		                        				<#assign optChildMap = permChild.getOperationsMap()/>
		                        				<#assign opts = permChild.getOperations()/>
	                        					<#list optChildMap?keys as optK>
		                        					<a class="label label-info <#if opts?seq_contains(optChildMap[optK])>j_self_opt</#if>"  data-url="privilege/removeOperation?privilegeId=${permChild.id}&operationId=${optChildMap[optK].id}">${optChildMap[optK].title!}</a>
	                        					</#list>
	                        					</div>
	                        				</#if>
	                        			</td>
	                        			<td class="fn-tc">
	                        				<#if permChild.enabled>
	                        				<a href="${base}/console/security/privilege/ajax/toggle?privilegeId=${permChild.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
				                            <#else>
				                            <a href="${base}/console/security/privilege/ajax/toggle?privilegeId=${permChild.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
				                            </#if>
	                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/edit?privilegeId=${permChild.id}" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
	                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/editOperation?privilegeId=${permChild.id}" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data">添加操作</a>
	                        				<a href="#J_EDIT_SEC" data-result-ctn="#J_EDIT_SEC" url="${base}/console/security/privilege/ajax/edit?parentId=${permChild.id}" data-toggle="modal" class="btn btn-mini btn-info j_ajax_for_data">创建子资源</a>
	                        			</td>
	                        		</tr>
                        			</#list>
                        		</#if>
                        	</#list>
                        	</#if>
                        	<!--<td><a href="#">administrator</a></td>
                            <td class="fn-tc">
			                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
			                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-success j_enable_disable">启用</a>
			                    <a href="#J_ADD_LAYER_MODAL" data-toggle="modal" url="${base}/console/authMgr/usr/ajax/" data-result-ctn="#J_ADD_LAYER_MODAL" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
			                    <a href="${base}/console/map/ajax/removeLayer?layerId=" class="btn btn-mini btn-inverse j_btn_del">删除</a>
			                 </td>-->
                        </tbody>
                    </table>
                </div><!-- END widget-content -->
            </div><!-- END widget-box -->
        </div><!-- END span -->
    </div><!-- END .row-fluid -->
</div><!-- END container-fluid -->
<div id="J_EDIT_SEC" class="modal fade hide"></div>
</body>
</html>
