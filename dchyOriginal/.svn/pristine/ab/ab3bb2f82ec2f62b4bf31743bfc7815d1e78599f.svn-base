<form id="J_FORM_BIND_ROLE" action="${base}/console/security/user/bindrole" class="form-horizontal form-normal" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>绑定角色</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label">指定角色</label>
                <div class="controls">
                	<select name="roleIds" id="" multiple>
                		<#list roles?sort_by("id") as role>
                		<option value="${role.id}" <#if user.roles??>${user.roles?seq_contains(role)?string("selected","")}</#if> >${role.name!}</option>
                		</#list>
                	</select>
                </div>
            </div>
        </div>
        <div class="modal-footer">
        	<#if user.id??><input type="hidden" name="userId" value="${user.id}"/></#if>
            <a href="" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>