
    <form id="J_FORM_SAVE_GROUP" action="${base}/console/group/save" method="post" class="form-horizontal form-normal j_validation_form">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3><#if group.id??>编辑分类<#else>新增分类</#if></h3>
        </div>
        <div class="modal-body">
            <div class="row-fluid">
            	<#if parent??>
            	<input type="hidden" name="parentId" value="${parent.id}"/>
            	<p class="controls">为 <strong>${parent.name}</strong> 创建子分类</p>
            	</#if>
            	<div class="span12">
	            	<div class="control-group">
		                <label class="control-label">分类名称</label>
		                <div class="controls">
		                    <input type="text" class="validate[required]" name="name" value="${group.name!}"/>
		                    <span class="help-inline"></span>
		                </div>
		            </div>
            	</div>
            </div>
        </div>
        <div class="modal-footer">
        	<#if group.id??>
        	<input type="hidden" name="id" value="${group.id}"/>
        	</#if>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>
