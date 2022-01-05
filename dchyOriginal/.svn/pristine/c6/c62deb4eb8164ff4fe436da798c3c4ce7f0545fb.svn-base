<#list regions?sort_by("id") as r>
									<tr id="${r.id}" data-url="${base}/console/region/ajax/fetch?parentId=${r.id}" <#if r.parent??>data-parent="${r.parent.id}"</#if> data-level="${r.level.name()}" data-has-child="${r.hasChildren()?string}">
										<td style="width:70%"><i class="region-icon region-${r.level.name()}"></i>${r.name}</td>
										<td class="fn-tc">${r.id}</td>
										<td class="fn-tc">${r.level.label}</td>
									</tr>
								</#list>