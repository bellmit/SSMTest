<form id="J_FORM_ADD_PROVIDER_STEP_2" action="${base}/console/provider/saveIndex" class="form-horizontal two-column" method="post">
	<div>
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
				&times;
			</button>
			<h3>添加索引服务提供者</h3>
		</div><!-- /.modal-header -->
		<div class="modal-body" style="max-height:500px">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>索引名称</th>
						<th>空间参考</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<#list indexs as i>
					<tr>
						<td>${i.name!}</td>
						<td>${i.wkid!}</td>
						<td class="fn-tc">
							<label class="radio">
								<input name="indexId" type="radio" value="${i.id}" <#if i_index == 0>checked</#if>/>
							</label>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<input type="hidden" name="mapId" value="${mapId}"/>
			<#if sp.id??><input type="hidden" name="id" value="${sp.id}"/></#if>
			<#if (!isEdit)>
				<a href="#J_NEW_PROVIDER_MODAL_1" class="btn pull-left" data-toggle="modal" data-dismiss="modal" aria-hidden="true">上一步</a>
			</#if>
			<!-- <a id="J_BTN_ADD_PROVIDER" class="btn btn-primary">保存</a> -->
			<button type="submit" class="btn btn-primary">保存</button>
		</div>
	</div>
</form>