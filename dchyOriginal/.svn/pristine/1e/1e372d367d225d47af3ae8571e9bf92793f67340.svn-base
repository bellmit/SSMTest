<form id="J_FORM_ADD_PROVIDER_STEP_2" action="${base}/console/provider/saveDs" class="form-horizontal two-column" method="post">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
			&times;
		</button>
		<h3>添加数据源服务提供者</h3>
	</div><!-- /.modal-header -->
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12 control-group">
				数据源服务提供者
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<input type="hidden" name="mapId" value="${mapId}"/>
		<#if sp.id??><input type="hidden" name="id" value="${sp.id}"/></#if>
		<#if (!isEdit)>
			<a href="#J_NEW_PROVIDER_MODAL_1" class="btn pull-left" data-toggle="modal" data-dismiss="modal" aria-hidden="true">上一步</a>
		</#if>
		<!-- <a class="btn btn-info">测试</a> <a id="J_BTN_ADD_PROVIDER" class="btn btn-primary">保存</a>-->
		<button type="submit" class="btn btn-primary">保存</button>
	</div>
</form>