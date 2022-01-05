<form action="${base}/console/monitor/inf/save" class="form-horizontal two-column j_validation_form">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>添加数据接口</h4>
	</div>
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12">
				<div class="control-group">
					<label class="control-label">说明</label>
					<div class="controls">
						<input type="text" name="description" value="${inf.description!}"/>
					</div>
				</div>
				<div class="control-group">
					<label for="" class="control-label"> 监控类别</label>
					<div class="controls">
						<label class="radio" for="radioDB" style="padding-top: 0;"><input id="radioDB" href="#oraclePane" type="radio" name="type" value="db" data-toggle="tab"/> Oracle</label>
						<label class="radio" for="radioZabbix"><input id="radioZabbix" href="#normalPane" type="radio" name="type" value="zabbix" data-toggle="tab"/> 常规监控</label>
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="tab-content">
				<div id="oraclePane" class="tab-pane">
					<div class="span6">
						<div class="control-group">
							<label for="" class="control-label">url</label>
							<div class="controls">
								<input type="text" name="url" value="${inf.attrs.url!}"/>
							</div>
						</div>
						<div class="control-group">
							<label for="" class="control-label">username</label>
							<div class="controls">
								<input type="text" name="username" value="${inf.attrs.username!}"/>
							</div>
						</div>
					</div>
					<div class="span6">
						<div class="control-group">
							<label for="" class="control-label">driver</label>
							<div class="controls">
								<input type="text" name="driver" value="${inf.attrs.driver!}"/>
							</div>
						</div>
						<div class="control-group">
							<label for="" class="control-label">password</label>
							<div class="controls">
								<input type="text" name="password" value="${inf.attrs.password!}"/>
							</div>
						</div>
					</div>
				</div>
				<div id="normalPane" class="tab-pane">
					<div class="span6">
						<div class="control-group">
							<label for="" class="control-label">host</label>
							<div class="controls">
								<input type="text" name="xhost" value="${inf.attrs.host!}"/>
							</div>
						</div>
					</div>
					<div class="span6">
						<div class="control-group">
							<label for="" class="control-label">port</label>
							<div class="controls">
								<input type="text" name="port" value="${inf.attrs.port!}"/>
							</div>
						</div>
					</div><!-- span -->
				</div>
			</div><!-- tab-content -->
		</div>
	</div>
	<div class="modal-footer">
		<#if inf.id??><input type="hidden" name="infId" value="${inf.id}" /></#if>
		<input type="hidden" name="hostId" value="${hostId}"/>
		<input type="submit" class="btn btn-primary" value="保存"/>
	</div>
</form>

<#if inf.attrs??>
<script>
	<#if inf.attrs.url??>
		$('#radioDB').click();
		$('#radioDB').parent().click();
	<#elseif inf.attrs.host??>
		$('#radioZabbix').click();
		$('#radioZabbix').parent().click();
	</#if>
</script>
</#if>
