	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3><#if region.name??>编辑<#else>新建</#if>行政区</h3>
    </div><!-- /.modal-header -->
    <form id="" action="${base}/console/region/save" method="post" class="form-horizontal two-column j_validation_form">
        <div class="modal-body" style="height: 220px;">
            <div class="row-fluid">
                <div class="span6">
                	<div class="control-group">
                		<label for="" class="control-label">名称</label>
                		<div class="controls">
                			<input type="text" name="name" class="validate[required]" value="${region.name!}"/>
                		</div>
                	</div>
                	<div class="control-group">
                		<label for="" class="control-label">类型</label>
                		<div class="controls">
                			<select name="level" id="">
                				<#list levels as l>
                					<option value="${l.name()}" <#if region.level?? && region.level==l>selected</#if> >${l.label}</option>
                				</#list>
                			</select>
                		</div>
                	</div>
                </div>
                <div class="span6">
                	<div class="control-group">
                		<label for="" class="control-label">行政区码</label>
                		<div class="controls">
                			<#if region.name??>
                			<input type="text" name="id" class="validate[required,custom[number]]" disabled="disabled" value="${region.id!}"/>
                			<input type="hidden" name="id" value="${region.id!}"/>
                			<#else>
                			<input type="text" name="id" class="validate[required,custom[number]]" value=""/>
                			</#if>
                		</div>
                	</div>
                	<#if parents??>
                	<div class="control-group">
                		<label for="" class="control-label">隶属于</label>
                		<div class="controls">
                			<select name="parent.id">
                				<#list parents as p>
                					<option value="${p.id!}" <#if p.id==parent.id>selected="selected"</#if>>${p.name!}</option>
                				</#list>
                			</select>
                		</div>
                	</div>
                	</#if>
                </div>
            </div><!-- row-fluid -->
            <div class="row-fluid">
            	<div class="span12">
            		<div class="control-group">
                		<label for="" class="control-label">范围</label>
                		<div class="controls">
                			<textarea type="text" name="geometry" rows="3">${region.geometry!}</textarea>
                		</div>
                	</div>
            	</div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="submit" class="btn btn-primary">保存</button>
        </div>
    </form>