<div class="container-fluid" style="width: 98%;padding: 10px">
    <form class="form-horizontal" id="inpectForm">
        <div class="form-group">
            <label for="name" class="col-xs-2 control-label text-right">巡查名称</label>
            <div class="col-xs-10">
                <input type="text" class="form-control" name="name" id="name" placeholder="" datatype="*" nullmsg="巡查名称不可为空！" value="{{name}}" />
            </div>
        </div>

        <div class="form-group">
            <label for="recordNO" class="col-xs-2 control-label text-right">巡报号</label>
            <div class="col-xs-10">
                <input type="text" class="form-control" name="recordNO" id="recordNO" placeholder="" datatype="*" nullmsg="巡报号不可为空！" value="{{recordNO}}" >
            </div>
        </div>

        <div class="form-group">
            <label for="inspector" class="col-xs-2 control-label text-right">巡查人</label>
            <div class="col-xs-10">
                <input type="text" class="form-control" name="inspector" id="inspector" placeholder="" datatype="*" nullmsg="巡查人不可为空！" value="{{inspector}}">
            </div>
        </div>

        <div class="form-group">
            <label for="otherInspector" class="col-xs-2 control-label text-right">其他巡查人</label>
            <div class="col-xs-6">
                <input type="text" class="form-control" name="otherInspector" id="otherInspector" placeholder="" value="{{otherInspector}}">
            </div>

            <label for="otherInspector" class="col-xs-2 control-label">是否疑似违法</label>
            <div class="col-xs-2" style="padding-top: 8px;">
                <input type="checkbox" class="form-control" id="sfyswf" {{isChecked}} >
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