									<#list perms as permChild>
                        			<tr 
                        			class="<#if permChild.enabled>j_enabled<#else>j_disabled</#if>" 
                        			id="${permChild.id}" 
                        			<#if permChild.parent??>data-parent="${permChild.parent.id}"</#if> 
                        			data-has-child="${permChild.hasChildren()?string}"
                        			<#if permChild.hasChildren()>data-url="${base}/console/auth/ajax/fetch?parentId=${permChild.id}&roleId=${roleId}"</#if>
                        			>
	                        			<td><i class="region-icon gridtree-level2"></i>${permChild.title!}</td>
	                        			<td>${permChild.resource!}</td>
	                        			<td>
	                        				<#if permChild.getOperationsMap()??>
	                        				<form action="${base}/console/auth/grant" method="post">
	                        					<div class="label-wrap">
		                        				<#assign optChildMap = permChild.getOperationsMap()/>
	                        					<#list optChildMap?keys as optK>
		                        					<#if grantedMap?? && grantedMap[permChild.id]??>
	                        							<#assign operationKeys=grantedMap[permChild.id]?keys>
	                        						</#if>
		                        					<label for="" class="checkbox">
		                        						<input <#if operationKeys?seq_contains(optChildMap[optK].id)>checked</#if> type="checkbox" name="optIds" value="${optChildMap[optK].id}" />
		                        						<a class="label label-info">${optChildMap[optK].title!}</a>
		                        					</label>
	                        					</#list>
	                        					</div>
	                        				<input type="hidden" name="roleId" value="${roleId}"/>
	                        				<input type="hidden" name="privilegeId" value="${permChild.id}"/>
	                        				</form>
	                        				</#if>
	                        			</td>
	                        			<td class="fn-tc">
	                        				<label class="checkbox inline"><input name="checkall" type="checkbox" value="checkall" class="j_check_all"/>全选</label>
	                        				<a href="#" class="btn btn-mini btn-primary hide j_save">保存</a>
	                        			</td>
	                        		</tr>
                        			</#list>