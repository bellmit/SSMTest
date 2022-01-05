	<form action="${base}/console/security/user/save" class="form-horizontal form-normal j_validation_form" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3><#if user.id??>编辑<#else>新建</#if>用户</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label">用户名</label>
                <div class="controls">
                    <input type="text" class="validate[required]" name="name" value="${user.name!}"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">真实姓名</label>
                <div class="controls">
                    <input type="text" class="validate[required]" name="viewName" value="${user.viewName!}"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">密码</label>
                <div class="controls">
                    <input type="text" name="pass" value=""/>
                </div>
            </div>
        </div>
        <div class="modal-footer">
        	<#if user.id??><input type="hidden" name="userId" value="${user.id}"/></#if>
            <a href="" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>