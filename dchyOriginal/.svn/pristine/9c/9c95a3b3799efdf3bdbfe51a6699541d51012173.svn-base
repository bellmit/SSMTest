<form action="${base}/console/monitor/item/save" class="form-vertical j_validation_form">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h5>${host.name}编辑监控项</h5>
	</div>
	<div class="modal-body" style="overflow: hidden;">
		<div class="row-fluid">
			<div class="span12" style="padding-left: 25px;">
				<div class="control-group">
					<label for="key" class="control-label">Key</label>
					<div class="controls">
						<input style="width: 92%" type="text" name="key" class="validate[required]"  <#if item.fixed>disabled</#if> value="${item.key!}"/>
						<#if item.fixed><input type="hidden" name="key" value="${item.key!}" /></#if>
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6" style="padding-left: 25px;">
				<div class="control-group">
					<label for="hostName" class="control-label">名称</label>
					<div class="controls">
						<input type="text" name="name" class="validate[required]" value="${item.name!}"/>
					</div>
				</div>
				<div class="control-group" class="control-label">
					<label for="hostName">Interface</label>
					<div class="controls">
						<#if item.fixed>
						<input type="text" disabled value="${item.inf.name}"/>
						<input type="hidden" name="infId" value="${item.inf.id}"/>
						<#else>
						<select name="infId">
							<#list infs as inf>
								<option value="${inf.id}">${inf.name}</option>
							</#list>
						</select>
						</#if>
					</div>
				</div>
				<div class="control-group">
					<label for="dataType" class="control-label">数据类型</label>
					<div class="controls">
						<#if item.fixed>
						<input type="text" disabled value="${item.dataType}"/>
						<input type="hidden" name="dataType" value="${item.dataType}"/>
						<#else>
						<select name="dataType">
							<#list dataTypes as dt>
								<option <#if item.dataType==dt>selected</#if> value="${dt}">${dt}</option>
							</#list>
						</select>
						</#if>
					</div>
				</div>
				<div class="control-group">
					<label for="valueType" class="control-label">值类型</label>
					<div class="controls">
						<#if item.fixed>
						<input type="text" disabled value="${item.valueType}"/>
						<input type="hidden" name="valueType" value="${item.valueType}"/>
						<#else>
						<select name="valueType" id="">
							<#list valueTypes as vt>
								<option <#if item.valueType==vt>selected</#if> value="${vt}">${vt}</option>
							</#list>
						</select>
						</#if>
					</div>
				</div>
				<div class="control-group">
					<label for="storeType" class="control-label">存储方式</label>
					<div class="controls">
						<#if item.fixed>
						<input type="text" disabled value="${item.storeType}"/>
						<input type="hidden" name="storeType" value="${item.storeType}"/>
						<#else>
						<select name="storeType" id="">
							<#list storeTypes as st>
								<option <#if item.storeType==st>selected</#if> value="${st}">${st}</option>
							</#list>
						</select>
						</#if>
					</div>
				</div>
			</div><!-- span -->
			<div class="span6" style="padding-left: 10px;">
				<div class="control-group">
					<label for="hostName" class="control-label">说明</label>
					<div class="controls">
						<input type="text" name="description" class="validate[required]" value="${item.description!}"/>
					</div>
				</div>
				<div class="control-group">
					<label for="format" class="control-label">数据格式</label>
					<div class="controls">
						<input id="format" type="text" name="format" value="${item.format!}"/>
					</div>
				</div>
				<div class="control-group">
					<label for="interval" class="control-label">读取间隔(秒)</label>
					<div class="controls">
						<input id="interval" type="text" name="interval" class="span4 validate[required,custom[number,min[10]]]" value="${item.interval!}"/>
						<span class="muted">&nbsp;&nbsp;大于等于10</span>
					</div>
				</div>
				<div class="control-group">
					<label for="history" class="control-label">详细数据保存天数</label>
					<div class="controls">
						<input id="history" type="text" name="history" class="span4 validate[required,custom[number]]" value="${item.history!}"/>
					</div>
				</div>
				<div class="control-group">
					<label for="trend" class="control-label">合计数据保存天数</label>
					<div class="controls">
						<input id="trend" type="text" name="trend" class="span4 validate[required,custom[number]]" value="${item.trend!}"/>
					</div>
				</div>
			</div><!-- span -->
		</div><!-- row -->
	</div>
	<div class="modal-footer">
		<#if item.id??><input type="hidden" name="itemId" value="${item.id}" /></#if>
		<input type="hidden" name="hostId" value="${host.id}"/>
		<input type="submit" class="btn btn-primary" value="保存"/>
	</div>
</form>