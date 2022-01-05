<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3><#if field.id??>编辑<#else>新建</#if>字段</h3>
    </div><!-- /.modal-header -->
    <form id="J_FORM_ADD_LAYER" action="${base}/console/field/save" method="post" class="form-horizontal two-column j_validation_form">
        <div class="modal-body" style="height: 130px;">
            <div class="control-group row-fluid">
                <div class="span6">
                    <label for="layerName" class="control-label">名称</label>
                    <div class="controls">
                        <input class="validate[required]" name="name" type="text" value="${field.name!}"/>
                    </div>
                </div>
                <div class="span6">
                    <label for="alias" class="control-label">别名</label>
                    <div class="controls">
                        <input id="alias" class="validate[required]" name="alias" type="text" value="${field.alias!}"/>
                    </div>
                </div><!-- /span6 -->
            </div><!-- /row-fluid -->
            <div class="control-group row-fluid">
                <div class="span6">
                	<label for="" class="control-label">类型</label>
                    <div class="controls">
                        <select name="type" id="">
                        	<#list fieldMap?keys as k>
                        	<option value="${k}" <#if (field.id?? && field.type==k)>selected</#if> >${fieldMap[k]}</option>
                        	</#list>
                        </select>
                    </div>
                </div><!-- /span6 -->
            </div><!-- /row-fluid -->
        </div>
        <div class="modal-footer">
        	<#if field.id??>
        	<input type="hidden" name="id" value="${field.id!}"/>
        	</#if>
        	<input type="hidden" name="layerId" value="${layerId}"/>
        	<input type="hidden" name="mapId" value="${mapId}"/>
            <button type="submit" class="btn btn-primary">保存</button>
        </div>
    </form>