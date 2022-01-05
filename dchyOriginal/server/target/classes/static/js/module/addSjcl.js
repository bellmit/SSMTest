layui.use(['element', 'jquery', 'table', 'form'], function () {
    var $ = layui.jquery,
        element = layui.element,
        table = layui.table,
        form = layui.form;
    $(function () {
        //得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name); 
       //下拉选项
        var optionData = parent.sl_data.dchyZdSjlxDOList
        optionData.forEach(function (op) {
            $('#ajclType').append('<option value="' + op.dm + '">' + op.mc + '</option>');
        }, this);
        form.render('select');
        //新增按钮
        form.on('submit(bdc-addSjcl-save-btn-son)', function (data) {
            addModel('新增中');
            var addData = {
                clmc: data.field.clmc,
                fs: data.field.pages,
                ys: data.field.fs,
                sjlx: data.field.type
            }
            $.ajax({
                type: "post",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/save/sjcl/" + parent.xmid,
                data: JSON.stringify(addData),
                contentType: 'application/json;charset=utf-8',
                async: false,
                success: function (res) {
                    removeModal();
                    layer.msg('新增成功');
                    parent.layer.close(index);
                    parent.reloadTable();
                },
                error: function (err) {
                    layer.msg('新增失败')
                }
            })
        })
        //取消
        $('#addSjclCancelbtn').click(function (){
            parent.layer.close(index);
        })
    })
})