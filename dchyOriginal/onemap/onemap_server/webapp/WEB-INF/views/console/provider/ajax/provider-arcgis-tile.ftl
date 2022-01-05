<form id="J_FORM_ADD_PROVIDER_STEP_2" action="${base}/console/provider/saveTile" class="form-horizontal two-column" method="post">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
			&times;
		</button>
		<h3>添加ArcGIS切片服务提供者</h3>
	</div><!-- /.modal-header -->
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12 control-group">
				<label for="" class="control-label">切片路径</label>
				<div class="controls"><input type="text" name="path" class="validate[required]" value="${params.path!}"/></div>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span6">
				<div class="span12 control-group">
					<label for="" class="control-label">最小X：</label>
					<div class="controls"><input type="text" name="xmin" class="validate[custom[number]]" <#if params.bound??>value="${params.bound?eval.xmin}"</#if>/></div>
				</div>
			</div>
			<div class="span6">
				<div class="span12 control-group">
					<label for="" class="control-label">最大X：</label>
					<div class="controls"><input type="text" name="xmax" class="validate[custom[number]]" <#if params.bound??>value="${params.bound?eval.xmax}"</#if>/></div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6">
				<div class="span12 control-group">
					<label for="" class="control-label">最小Y：</label>
					<div class="controls"><input name="ymin" type="text" class="validate[custom[number]]" <#if params.bound??>value="${params.bound?eval.ymin}"</#if>/></div>
				</div>
			</div>
			<div class="span6">
				<div class="span12 control-group">
					<label for="" class="control-label">最大Y：</label>
					<div class="controls"><input name="ymax" type="text" class="validate[custom[number]]" <#if params.bound??>value="${params.bound?eval.ymax}"</#if>/></div>
				</div>
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