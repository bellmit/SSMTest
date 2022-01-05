<div id="importModal" class="modal fade hide">
    <form action="import" class="form-horizontal form-normal" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>导入单个服务</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label">单个服务地址</label>
                <div class="controls">
                    <input type="text" name="url"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">账号</label>
                <div class="controls">
                    <input type="text" name="username"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">密码</label>
                <div class="controls">
                    <input type="text" name="password"/>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <button type="submit" class="btn btn-primary">导入</button>
        </div>
    </form>
</div>