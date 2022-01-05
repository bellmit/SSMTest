									<#list perms as permChild>
                        			<tr 
                        			class="<#if permChild.enabled>j_enabled<#else>j_disabled</#if>" 
                        			id="${permChild.id}" 
                        			<#if permChild.parent??>data-parent="${permChild.parent.id}"</#if> 
                        			data-has-child="${permChild.hasChildren()?string}"
                        			data-url="${base}/console/security/privilege/ajax/fetch?parentId=${permChild.id}"
                        			>
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