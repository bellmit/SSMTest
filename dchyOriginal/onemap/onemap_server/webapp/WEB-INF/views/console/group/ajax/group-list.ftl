<#list groups as g>
<tr id="${g.id}" <#if g.parent??>data-parent="${g.parent.id}"</#if> data-has-child="${g.children?size!}" data-url="${base}/console/group/ajax/fetch?parentId=${g.id}">
	<td>${g.name}</td>
	<td><div class="fn-tc">${g.createAt}</div></td>
	<td>
		<a href="#J_EDIT_GROUP_MODAL" class="btn btn-mini btn-info j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" data-toggle="modal" url="${base}/console/group/ajax/edit?id=${g.id}">编辑</a>
		<a href="#J_EDIT_GROUP_MODAL" data-toggle="modal" class="btn btn-mini btn-primary j_ajax_for_data" data-result-ctn="#J_EDIT_GROUP_MODAL" url="${base}/console/group/ajax/edit?parentId=${g.id}&">创建子分类</a>
		<a href="${base}/console/group/remove?id=${g.id}" class="btn btn-mini btn-danger j_btn_del">删除</a>
	</td>
</tr>
</#list>