<form action="${base}/console/security/role/save" class="form-horizontal form-normal j_validation_form" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3><#if role.id??>编辑<#else>新建</#if>角色</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label">角色名</label>
                <div class="controls">
                    <input type="text" name="name" class="validate[required]" value="${role.name!}"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">角色描述</label>
                <div class="controls">
                    <textarea name="description" class="validate[required]">${role.description!}</textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
        	<#if role.id??><input type="hidden" name="roleId" value="${role.id}"/></#if>
            <a href="" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>