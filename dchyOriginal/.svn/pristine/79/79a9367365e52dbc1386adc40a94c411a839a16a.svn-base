<html>
<head>
    <title>角色授权</title>
    <meta name="tab" content="auth"/>
    <meta name="js" content="${base}/static/js/page_grant.js" />
</head>
<body>

<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}/console/security/role"><i class="icon-tasks"></i>角色列表</a>
		<a class="current">角色授权</a>
	</div>
</div><!-- /#breadcrumbWrapper -->

<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	<div class="row-fluid">
		<div style="position: relative;">
			<h3>${role.name} <small>角色授权</small></h3>
			<div style="position:absolute;right:0;top:10px;">
				<a id="J_BTN_SAVE_ALL" href="#" class="btn btn-primary hide" onclick="">保存全部</a>
			</div>
		</div>
		<div class="widget-box">
			<div class="widget-content nopadding">
				<table class="table table-bordered table-condensed">
                        <thead><tr>
                            <th width="20%">名称</th>
                            <th width="10%">资源</th>
                            <th width="50%">授权使用方式</th>
                            <th width="20%">操作</th>
                        </tr></thead>
                        <tbody>
                        	<#if perms??>
                        	<#list perms as perm>
                        		
                        		<tr 
                        		class="<#if perm.enabled>j_enabled<#else>j_disabled</#if>" 
                        		id="${perm.id}" 
                        		<#if perm.parent??>data-parent="${r.parent.id}"</#if> 
                        		data-has-child="${perm.hasChildren()?string}"
                        		data-url="${base}/console/auth/ajax/fetch/parentId=${perm.id}"
                        		>
                        			
	                        			<td><i class="region-icon gridtree-root"></i>${perm.title!}</td>
	                        			<td>${perm.resource!}</td>
	                        			<td>
	                        				<form action="${base}/console/auth/grant" method="post">
	                        				<#if perm.getOperationsMap()??>
	                        					<div class="label-wrap">
	                        					<#assign optMap = perm.getOperationsMap()/>
	                        					<#list optMap?keys as optK>
	                        						<#if grantedMap?? && grantedMap[perm.id]??>
	                        							<#assign operationKeys=grantedMap[perm.id]?keys>
	                        						</#if>
		                        					<label class="checkbox inline"><input <#if operationKeys?seq_contains(optMap[optK].id)>checked</#if> type="checkbox" name="optIds" value="${optMap[optK].id}"/> <a class="label label-info">${optMap[optK].title!}</a></label>
	                        					</#list>
	                        					</div>
	                        				</#if>
	                        				<input type="hidden" name="roleId" value="${role.id}"/>
	                        				<input type="hidden" name="privilegeId" value="${perm.id}"/>
	                        				</form>
	                        			</td>
	                        			<td class="fn-tc">
	                        				<label class="checkbox inline"><input name="checkall" type="checkbox" value="checkall" class="j_check_all"/>全选</label>
	                        				<a href="#" class="btn btn-mini btn-primary j_save">保存</a>
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
                        			data-url="${base}/console/auth/ajax/fetch?parentId=${permChild.id}&roleId=${role.id}"</#if>
                        			>
	                        			<td><i class="region-icon gridtree-level2"></i>${permChild.title!}</td>
	                        			<td>${permChild.resource!}</td>
	                        			<td>
	                        				<form action="${base}/console/auth/grant" method="post">
	                        				<#if permChild.getOperationsMap()??>
	                        					<div class="label-wrap">
		                        				<#assign optChildMap = permChild.getOperationsMap()/>
	                        					<#list optChildMap?keys as optK>
	                        						<#if grantedMap?? && grantedMap[permChild.id]??>
	                        							<#assign operationKeys=grantedMap[permChild.id]?keys>
	                        						</#if>
		                        					<label class="checkbox inline"><input <#if operationKeys?seq_contains(optChildMap[optK].id)>checked</#if> type="checkbox" name="optIds" value="${optChildMap[optK].id}"/> <a class="label label-info">${optChildMap[optK].title!}</a></label>
	                        					</#list>
	                        					</div>
	                        				</#if>
	                        				<input type="hidden" name="roleId" value="${role.id}"/>
	                        				<input type="hidden" name="privilegeId" value="${permChild.id}"/>
	                        				</form>
	                        			</td>
	                        			<td class="fn-tc">
	                        				<label class="checkbox inline"><input name="checkall" type="checkbox" value="checkall" class="j_check_all"/>全选</label>
	                        				<a href="#" class="btn btn-mini btn-primary hide j_save">保存</a>
	                        			</td>
	                        		</tr>
                        			</#list>
                        		</#if>
                        	</#list>
                        	</#if>
                        </tbody>
                    </table>
				
			</div><!-- /.widget-content -->
		
		</div><!-- /.widget-box -->
	
	</div><!-- /.row -->

</div><!-- /.container-fluid -->

</body>
</html>