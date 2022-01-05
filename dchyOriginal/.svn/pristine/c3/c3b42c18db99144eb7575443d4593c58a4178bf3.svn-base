<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3><#if layer.id??>编辑图层<#else>新增图层</#if></h3>
</div><!-- /.modal-header -->
<form id="J_FORM_ADD_LAYER" action="${base}/console/layer/save" method="post" class="form-horizontal j_validation_form">
    <div class="modal-body">
    	<div class="row-fluid">
    		<div class="span6">
    			<div class="control-group">
                    <label for="alias" class="control-label">数据源选择</label>
                    <div class="controls">
                        <select name="dataSourceId" id="">
                            <option value="">默认</option>
                        <#list dataSrcs as ds>
                            <option value="${ds.id}">${ds.name!}</option>
                        </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label for="layerName" class="control-label">名称</label>
                    <div class="controls">
                        <input id="layerName" class="validate[required]" name="name" type="text" value="${layer.name!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="layerName" class="control-label">要素名</label>
                    <div class="controls">
                        <input id="featureId" name="featureId" type="text" value="${layer.featureId!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="alias" class="control-label">空间参考</label>
                    <div class="controls">
                        <input id="wildcard" name="featureWkid" type="text" value="${layer.featureWkid!}"/>
                    </div>
                </div>
    		</div>
    		
    		<div class="span6">
    			<div class="control-group">
                <#if layer.id??>
                    <label for="layerName" class="control-label">图层序号</label>
                    <div class="controls">
                        <input type="text" disabled value="${layer.index!}"/>
                    </div>
                </#if>
                </div>
                <div class="control-group">
                    <label for="alias" class="control-label">别名</label>
                    <div class="controls">
                        <input id="alias" name="alias" type="text" value="${layer.alias!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="alias" class="control-label">匹配符</label>
                    <div class="controls">
                        <input id="wildcard" name="wildcard" type="text" value="${layer.wildcard!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <label for="indexed">
                            <input name="indexed" type="checkbox" <#if layer.indexed>checked</#if> />是否索引
                        </label>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <label for="indexed">
                            <input name="visibility" type="checkbox" <#if layer.visibility>checked</#if> />是否可见
                        </label>
                    </div>
                </div>
    		</div>
    	</div>
    </div>

    <div class="modal-footer">
        <input name="mapId" type="hidden" value="${mapId}"/>
    <#if layer.id??><input name="id" type="hidden" value="${layer.id}"/></#if>
        <button type="submit" class="btn btn-primary">保存</button>
    </div>
</form>