<form action="${base}/console/monitor/host/save" class="form-horizontal form-normal j_validation_form">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>编辑主机信息</h4>
	</div>
	<div class="modal-body">
		<div class="control-group">
			<label class="control-label">名称</label>
			<div class="controls">
				<input type="text" name="name" class="validate[required]" value="${host.name!}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述</label>
			<div class="controls">
				<input type="text" name="description" class="validate[required]" value="${host.description!}"/>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<#if host.id??><input type="hidden" name="hostId" value="${host.id}" /></#if>
		<input type="submit" class="btn btn-primary" value="保存"/>
	</div>
</form>