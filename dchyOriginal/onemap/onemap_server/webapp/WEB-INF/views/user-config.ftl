<form id="J_FORM_UCONFIG" action="${base}/ajax/saveUserConfig" class="form-horizontal form-normal form-with-help j_validation_form" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>用户设置</h3>
        </div>
        <div class="modal-body" style="padding-top: 0;">
        	<div class="control-group">
        		<div id="J_ALERT_UCONFIG" class="alert hide">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<div class="j_alert_ctn"></div>
				</div>
        	</div>
        	<div class="control-group">
                <label class="control-label">账号</label>
                <div class="controls">
                    <input type="text" name="viewName" value="${user.name!}" disabled/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">姓名</label>
                <div class="controls">
                    <input type="text" class="validate[required]" name="viewName" value="${user.viewName!}"/>
                    <span class="help-inline">显示称谓</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">原始密码</label>
                <div class="controls">
                    <input type="password" class="validate[required,ajax[ajaxOldPassCheck]]" name="oldpass"/>
                    <span class="help-inline">输入原始密码</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">新密码</label>
                <div class="controls">
                    <input id="J_NEW_PASSWORD" type="password" class="validate[required]"/>
                    <span class="help-inline">创建新密码</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">确认密码</label>
                <div class="controls">
                    <input type="password" class="validate[required,equals[J_NEW_PASSWORD]" name="password"/>
                    <span class="help-inline">再输入一次上面的密码</span>
                </div>
            </div>
        </div>
        <div class="modal-footer">
        	<input type="hidden" name="userId" value="${user.id}"/>
            <a href="#" id="J_CANCEL_UCONFIG" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <a id="J_SUBMIT_UCONFIG" class="btn btn-primary">提交</a>
        </div>
    </form>