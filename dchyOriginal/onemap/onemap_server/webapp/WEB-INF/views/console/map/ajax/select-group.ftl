<select id="group-select" name="groupId">
<#list group as g>
    <option value="${g.id}" <#if name=g.name>selected="selected"</#if>>${g.name}</option>
</#list>
</select>
&nbsp;&nbsp;<a href="#J_NEW_GROUP_MODAL" data-toggle="modal" class="btn btn-mini ">新增服务组</a>