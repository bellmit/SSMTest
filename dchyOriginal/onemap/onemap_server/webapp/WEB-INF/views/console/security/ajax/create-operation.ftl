<form action="privilege/saveOperation" class="form-horizontal form-normal" method="post">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>添加操作方式</h3>
        </div>
        <div class="modal-body">
        	<div class="row-fluid">
        		<div class="span12">
        			<div class="control-group">
        				<label for="" class="control-label"></label>
        				<div class="controls">
        					<strong>${privilege.title!}</strong> 添加操作方式
        				</div>
        			</div>
        			<div class="control-group">
            			<label for="" class="control-label">操作名</label>
            			<div class="controls">
            				<input type="text" name="name" class="validate[required]" value=""/>
            			</div>
            		</div>
            		<div class="control-group">
            			<label for="" class="control-label">显示名称</label>
            			<div class="controls">
            				<input type="text" name="title" class="validate[required]" value=""/>
            			</div>
            		</div>
        		</div>
        	</div>
        </div>
        <div class="modal-footer">
        	<input type="hidden" name="privilegeId" value="${privilege.id}"/>
            <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">关闭</a>
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </form>