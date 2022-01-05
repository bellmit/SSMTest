<form action="privilege/save" class="form-horizontal form-normal" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3><#if privilege.id??>编辑<#else>新建</#if>权限</h3>
        </div>
        <div class="modal-body">
            <div class="row-fluid">
            	<div class="span12">
            		<#if parent??>
            		<div class="control-group">
            			<label for="" class="control-label">父级资源</label>
            			<div class="controls">
            				<p style="margin-top:3px;"><strong>${parent.title!}</strong> <strong>${parent.resource!}</strong></p>
            			</div>
            		</div>
            		<input type="hidden" name="parent.id" value="${parent.id}"/>
            		</#if>
            		<div class="control-group">
            			<label for="" class="control-label">资源</label>
            			<div class="controls">
            				<input type="text" name="resource" class="validate[required]" value="${privilege.resource!}"/>
            			</div>
            		</div>
            		<div class="control-group">
            			<label for="" class="control-label">资源描述</label>
            			<div class="controls">
            				<input type="text" name="title" value="${privilege.title!}"/>
            			</div>
            		</div>
            	</div>
            </div>
        </div>
        <div class="modal-footer">
        	<#if privilege.id??><input type="hidden" name="privilegeId" value="${privilege.id}"/></#if>
            <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>