<div class="container-fluid" style="width: 98%;padding: 10px">
    <form class="form-horizontal" id="inpectForm">
        <div class="form-group">
            <label for="name" class="col-xs-2 control-label text-right">巡查名称</label>
            <div class="col-xs-10">
                <input type="text" class="form-control" name="name" id="name" placeholder="" datatype="*" nullmsg="巡查名称不可为空！" value="{{name}}" />
            </div>
        </div>

        <div class="form-group">
            <label for="inspector" class="col-xs-2 control-label text-right">巡查人员</label>
            <div class="col-xs-10">
                <input type="text" class="form-control" name="inspector" id="inspector" placeholder="" datatype="*" nullmsg="巡查人不可为空！" value="{{inspector}}">
            </div>
        </div>

        <div class="form-group">
            <label for="createAt" class="col-xs-2 control-label text-right">巡查时间</label>
            <div class="col-xs-4">
                <input type="text" class="form-control" name="createAt" id="createAt" placeholder="" value="{{createAt}}" disabled="disabled" readonly="readonly">
            </div>

            <label for="otherInspector" class="col-xs-2 control-label">是否疑似违法</label>
            <div class="col-xs-2" style="padding-top: 8px;">
                <input type="checkbox" class="form-control" id="sfyswf" {{isChecked}} >
            </div>
        </div>

		<div class="form-group">
			<label for="jsqk" class="col-xs-2 control-label text-right">建设情况</label>
            <div class="col-xs-4">
                <select class="form-control" id="constructionState" name="constructionState">
				  <option value="圈建围墙" {{selected constructionState '圈建围墙'}}>圈建围墙</option>
				  <option value="基础建设" {{selected constructionState '基础建设'}}>基础建设</option>
				  <option value="部分建设" {{selected constructionState '部分建设'}}>部分建设</option>
				  <option value="已建成" {{selected constructionState '已建成'}}>已建成</option>
				</select>
            </div>
			<label for="mqxz" class="col-xs-2 control-label text-right">目前现状</label>
			<div class="col-xs-4">
				 <select class="form-control" id="status" name="status">
				  <option value="未停工" {{selected status '未停工'}}>未停工</option>
				  <option value="已停工" {{selected status '已停工'}}>已停工</option>
				  <option value="已拆除" {{selected status '已拆除'}}>已拆除</option>
				</select>
			</div>
		</div>

		<div class="form-group">
            <label for="actualArea" class="col-xs-2 control-label text-right">实际占地面积（亩）</label>
            <div class="col-xs-4">
                <input type="text" class="form-control" name="actualArea" id="actualArea" placeholder="" value="{{actualArea}}">
            </div>

            <label for="qzgd" class="col-xs-2 control-label text-right">其中耕地（亩）</label>
            <div class="col-xs-4" style="padding-top: 8px;">
                <input type="text" class="form-control" name="farmlandArea" id="farmlandArea" placeholder="" value="{{farmlandArea}}">
            </div>
        </div>

        <div class="form-group">
            <label for="fact" class="col-xs-2 control-label text-right">基本事实</label>
            <div class="col-xs-10">
                <textarea id="fact" name="fact" class="form-control" >{{fact}}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label for="remaker" class="col-xs-2 control-label text-right">备注</label>
            <div class="col-xs-10">
                <textarea id="remaker" name="remark" class="form-control" >{{remark}}</textarea>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-12 text-center">
                <button class="btn btn-default" id="saveInspectBtn"> 保存 </button>
            </div>
        </div>

        <input name="id" type="hidden" value="{{id}}"/>
        <input name="proId" type="hidden" value="{{proId}}"/>
		<input name="createAt" type="hidden" value="{{createAt}}"/>
        <input name="sfyswf" type="hidden" value="{{sfyswf}}"/>
        <input name="userId" type="hidden" value="{{userId}}"/>
    </form>
</div>