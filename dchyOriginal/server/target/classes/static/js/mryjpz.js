/**
 * Created by liuqiang on 2020/4/15.
 */
//表格参数
var param_page = {
    rows: 10,
    page: 1,
    gzldyid:'',
    gzljdid: ''
};

// 下拉框参数
var param = {
    gzlmc: '',
    gzljdmc: ''
}

var gzlmcList = [];

var cjr = "";

var cjrid = "";
layui.use(['jquery','table', 'layer', 'form'], function () {
    var $ = layui.jquery,
        table = layui.table,
        layer = layui.layer,
        form = layui.form;

     // 获取工作流节点名称列表
    function changeGzljdid(id,gzljdmcInput) {
        $.ajax({
            url : getUrl() + "/rest/v1.0/mryj/process/jdlist"  ,
            type:'POST',
            async: false,
            dataType: 'json',
            data:{gzldyid:id},
            success: function (data) {
                $('#' + gzljdmcInput + "" ).empty();
                var gzljdmcHtml = '<option >'+ '</option>';
                for(var i=0;i<data.length;i++){
                    gzljdmcHtml += '<option value=' + data[i].id + '>' + data[i].name + '</option>';
                }
                $('#' + gzljdmcInput + "" ).append(gzljdmcHtml);
                form.render();
            }
        })
    };

    //工作流名称确认开始加载工作流节点名称和表格
    form.on('select(gzlmc)', function (data) {
        var str = "gzljdmc";
        var id = data.value;
        changeGzljdid(id,str);
        param_page.gzldyid = id;
        reloadTable (param_page);
    });

    //工作流节点名称确认以后开始加载表格
    form.on('select(gzljdmc)', function (data) {
        var $gzljdid = data.value;
        var $gzldyid =  $("#gzlmc option:selected").val();

        param_page.gzldyid = $gzldyid;
        if($gzljdid == " "){
            $gzljdid = null;
        }
        param_page.gzljdid = $gzljdid;
        reloadTable(param_page);
    });

    //工作流名称确认开始加载工作流节点名称
    form.on('select(gzlmcAdd)', function (data) {
        var str = "gzljdmcAdd";
        var id = data.value;
        changeGzljdid(id,str);
    });

    //工作流名称确认开始加载工作流节点名称
    form.on('select(gzlmcUpd)', function (data) {
        var str = "gzljdmcUpd";
        var id = data.value;
        changeGzljdid(id,str);
    });

    // 初始化表格
    function loadTable(_param) {
        $.ajax({
            url: getUrl()  + "/rest/v1.0/mryj/yjpzList",
            type: "POST",
            dataType: 'json',
            data: _param,
            success: function (res) {
                table.render({
                    elem: '#LAY_table_yjpzList',
                    id: 'LAY_table_yjpzList',
                    data: res,
                    height: 523,
                    toolbar: '#toolbarDemo',
                    defaultToolbar: [],
                    page:true,
                    limit: _param.rows,
                    cols: [[
                        {type: 'checkbox', fixed: 'left', width: '3%'}
                        , {field: 'gzlmc', title: '工作流名称', align: 'center' ,width: '25%'}
                        , {field: 'cjr', title: '创建人', align: 'center',width: '12%'}
                        , {field: 'gzljdmc', title: '工作流节点名称', align: 'center',width: '12%'}
                        , {field: 'mryj', title: '默认意见', align: 'center',width: '48.1%'}
                    ]],
                    parseData: function (data) { //res 即为原始返回的数据
                        return {
                            "code": "0", //解析接口状态
                            "msg": null, //解析提示文本
                            "count": data.records,
                            "data": data.rows //解析数据列表
                        }
                    }
                });
            },
            error: function () {
                layer.msg("请求失败");
            }
        });

    }

    //重载表格
    function reloadTable(_param) {
        $.ajax({
            url: getUrl()  + "/rest/v1.0/mryj/yjpzList",
            type: "POST",
            dataType: 'json',
            data: _param,
            success: function (res) {
                table.reload('LAY_table_yjpzList',{
                    data: res
                });
            },
            error: function () {
                layer.msg("请求失败");
            }
        });

    }

    // 头工具栏lay-event事件
    table.on('toolbar(LAY_table_yjpzList)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        var len = checkStatus.data.length;
        var data = checkStatus.data[0];

        switch(obj.event){
            case 'add':
                layer.open({
                    type: 1,//类型
                    title: "新增",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '350px'],
                    btnAlign: 'c',
                    shade: 0.3,
                    content: $('#showAdd').html(),//打开的内容
                    success: function () {
                        param.gzlmc = "gzlmcAdd";
                        param.gzljdmc = "gzljdmcAdd";
                        loadGzlmcInput(param);
                        loadGzljdmcInput(param);

                    },
                });
                break;
            case 'del':
                layer.confirm('是否删除', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    if(len == 0){
                        layer.msg("未选中要删除的用户")
                    }else if(len == 1){
                        $yjid = data.yjid;
                        del($yjid);
                    }else if(len>1){
                        var yjidList = [];
                        var dataList = checkStatus.data;
                        $.each(dataList,function(i,n) {
                            yjidList.push(n.yjid);
                        });
                        delAll(yjidList);
                    }
                });
                break;
            case 'upd':
                if(len == 0){
                    layer.msg("未选中用户")
                }else if(len>1){
                    layer.msg("不允许选择多条！");
                } else {
                    var $yjid = data.yjid;
                    var $gzlmc = data.gzlmc;
                    var $gzldyid = data.gzldyid;
                    var $gzljdmc = data.gzljdmc;
                    var $gzljdid = data.gzljdid;
                    var $mryj = data.mryj;
                    layer.open({
                        type: 1,//类型
                        title: "修改",
                        skin: 'layui-layer-rim', //加上边框
                        area: ['500px', '350px'],
                        btnAlign: 'c',
                        shade: 0.3,
                        content: $('#showUpd').html(),//打开的内容
                        success: function () {
                            param.gzlmc = "gzlmcUpd";
                            param.gzljdmc = "gzljdmcUpd";
                            loadGzlmcInput(param);
                            $("#gzlmcUpd option:selected").text($gzlmc);
                            $("#gzlmcUpd option:selected").val($gzldyid);

                            loadGzljdmcInput(param);
                            $("#gzljdmcUpd option:selected").text($gzljdmc) ;
                            $("#gzljdmcUpd option:selected").val($gzljdid);
                            $('#mryjUpd').val($mryj);
                            $('#yjidUpd').val($yjid);
                            form.render();
                            },
                    });
                }
                break;
        };

    });

     // 页面初始化装载表格
    $(document).ready(function () {
        param.gzlmc = "gzlmc";
        param.gzljdmc = "gzljdmc";
        loadGzlmcInput(param);
        form.render();
        loadGzljdmcInput(param);
        param_page.gzldyid =  $("#gzlmc").val();
        loadTable(param_page);
    });

    //删除
    function del(yjid) {
        $.ajax({
            url: getUrl()  + "/rest/v1.0/mryj/delMryjByYjid",
            dataType: 'text',
            type:'post',
            data:{yjid:yjid},
            success: function () {
                layer.msg("删除成功！");
                reloadTable(param_page);
            },
            error:function () {
                layer.msg("删除失败！");
            }
        });
    };

    //批量删除
    function delAll(yjidList) {
        var list = [];
        for (var key in yjidList) {
            list[key] = yjidList[key];
        }
        $.ajax({
            url: getUrl() + "/rest/v1.0/mryj/delAllMryj",
            dataType: "text",
            data: JSON.stringify(list),
            contentType:"application/json",
            type: "post",
            success: function (data) {
                layer.msg("删除成功！");
                reloadTable(param_page);
            }
        });
    };

    //增加
    form.on('submit(btnAdd)',function (data) {
        data.field.gzldyid = $("#gzlmcAdd option:selected").val();
        data.field.gzljdid =  $("#gzljdmcAdd option:selected").val();

        data.field.gzlmc = $("#gzlmcAdd option:selected").text();
        data.field.gzljdmc = $("#gzljdmcAdd option:selected").text();

        data.field.cjrid = cjrid;
        data.field.cjr = cjr;

        var mryj = getMryj(data.field.gzldyid,data.field.gzljdid);
        if(mryj == ""){
            $.ajax({
                url: getUrl()  + "/rest/v1.0/mryj/insertMryj",
                dataType: 'text',
                type: 'POST',
                data: data.field,
                success: function () {
                    reloadTable(param_page);
                    layer.closeAll();
                    layer.msg("新增成功");
                },
                error: function () {
                    layer.closeAll();
                    layer.msg("新增失败");
                }
            });
        }else {
            layer.confirm('该节点已存在,请点击修改!', {
                btn: ['确定','取消'] //按钮
            }, function(){
                layer.closeAll();
            }, function(){
                layer.closeAll();
            });


        }
        return false;
    });

    //修改
    form.on('submit(btnUpd)',function (data) {

        data.field.gzldyid = $("#gzlmcUpd option:selected").val();
        data.field.gzljdid = $("#gzljdmcUpd option:selected").val();

        data.field.gzljdmc = $("#gzljdmcUpd option:selected").text();
        data.field.gzlmc = $("#gzlmcUpd option:selected").text();

        data.field.cjrid = cjrid;
        data.field.cjr = cjr;

        var yjid = obtainYjid(data.field.gzlmc,data.field.gzljdmc);
        if(yjid != data.field.yjid){
            del(yjid);
        }
        $.ajax({
            url: getUrl()  + "/rest/v1.0/mryj/updMryjByYjid",
            dataType: 'text',
            type: 'POST',
            data: data.field,
            success: function () {
                reloadTable(param_page);
                layer.closeAll();
                layer.msg("修改成功");
            },
            error: function () {
                layer.closeAll();
                layer.msg("修改失败");
            }
        });
        return false;
    });

    // 动态加载工作流名称下拉框
    function loadGzlmcInput(_param) {
        $.ajax({
            url: getUrl()  + "/rest/v1.0/mryj/process/list",
            type:'get',
            async: false,
            dataType: 'json',
            success: function (res) {
                var gzlmcHtml = '<option >'+ '</option>';
                for(var i=0;i<res.length;i++){
                    gzlmcList[res[i].id ] = res[i].name;
                    gzlmcHtml += '<option value=' + res[i].id + '>' + res[i].name + '</option>';
                }
                $("#" + _param.gzlmc + "").append(gzlmcHtml);
                form.render();
            }
        });
    }

    //动态加载工作流节点名称下拉框
    function loadGzljdmcInput(_param) {
        var id =  $("#" +_param.gzlmc + " " +"option:selected").val();

        if(id != ""){
            $.ajax({
                url : getUrl()  + "/rest/v1.0/mryj/process/jdlist"  ,
                type:'POST',
                async: false,
                dataType: 'json',
                data:{gzldyid:id},
                success: function (data) {
                    $("#" + _param.gzljdmc + "").empty();
                    var gzljdmcHtml = '<option >'+ '</option>';
                    for(var i=0;i<data.length;i++){
                        gzljdmcHtml += '<option value=' + data[i].id + '>' + data[i].name + '</option>';
                    }
                    $("#" + _param.gzljdmc + "").append(gzljdmcHtml);
                    form.render();
                },
            })
        }

    }

    //根据节点获取默认意见 0:此节点名称和工作流名称无记录,1:表示有记录
    function getMryj(gzldyid,gzljdid) {
        var mryj = "";
        $.ajax({
            url: getUrl() + "/rest/v1.0/mryj/yjpzList",
            type: "POST",
            async:false,
            dataType: 'json',
            data: {gzldyid:gzldyid, gzljdid:gzljdid},
            success: function (res) {
                if(!$.isEmptyObject(res)){
                    mryj = mryj + "1";
                }

            }
        });
        return mryj;
    }

    //根据节点获取yjid
    function getYjid(gzldyid,gzljdid) {
        var yjid = "";
        $.ajax({
            url: getUrl() + "/rest/v1.0/mryj/yjpzList",
            type: "POST",
            async:false,
            dataType: 'json',
            data: {gzldyid:gzldyid, gzljdid:gzljdid},
            success: function (res) {
                if(!$.isEmptyObject(res)){
                    yjid = res[0].yjid;
                }

            }
        });
        return yjid;
    }

    //根据名称获取yjid
    function obtainYjid(gzlmc,gzljdmc) {
        var initYjid = "";
        $.ajax({
            url: getUrl() + "/rest/v1.0/mryj/obtainYjid",
            type: "POST",
            async:false,
            dataType: 'text',
            data: {gzlmc:gzlmc, gzljdmc:gzljdmc},
            success: function (res) {
                initYjid = res;
            }

        });
        return initYjid;
    }

    //获取页面创建人
    $.ajax({
        type: "GET",
        url: getContextPath() + "/index/getUser",
        data: {},
        success: function (data) {
            cjr = data.username;
            cjrid = data.id;
        }

    });
});
