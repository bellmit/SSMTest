<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数据字典管理</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <@com.style name="static/css/plugins/jquery.treegrid.css"></@com.style>
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
    <style>
        body{
            font-size: 13px;
            overflow-x: hidden;
        }
        .container{
            margin-top: 30px;
        }
        .dicts-panel{
            margin-top: 20px;
        }
        .dicts-panel .panel-heading{
            height: 52px;
        }
        .dicts-panel .panel-heading >a:first-child{
            margin-right:15px;
        }
        /*table*/
        .table th {
            line-height: 20px;
            height: auto;
            font-size: 12px;
            padding: 3px 10px;
            border-bottom: 0;
            text-align: center;
            color: #666666;
            background-color: #efefef;
            background-image: -webkit-gradient(linear, 0 0%, 0 100%, from(#fdfdfd), to(#eaeaea));
            background-image: -webkit-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -moz-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -ms-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -o-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fdfdfd', endColorstr='#eaeaea',GradientType=0 );
        }

        .dict-edit,
        .dict-del,
        .dict-item-add,
        .item-edit,
        .item-del{
            margin-left:6px;
        }

        .treegrid-expander {
            height: 14px !important;
        }

        .td-ban{
            color:#ddd;
        }

        .form-container{
            margin-top:10px!important;
            width:98%;
        }
        .modal-content{
            width:510px!important;
        }

        .Validform_wrong{
            color:#F00;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="panel panel-default dicts-panel">
        <div class="panel-heading">
            <a class="btn btn-sm btn-primary" id="addDictBtn"><i class="fa fa-plus"></i> 添加字典</a>
            <a class="btn btn-sm btn-danger" id="delBatchBtn"><i class="fa fa-trash-o"></i> 批量删除</a>
        </div>
        <table class="table table-condensed table-bordered table-hover tree">
            <thead>
            <tr>
                <th width="20%">名称</th>
                <th width="20%">标题</th>
                <th width="20%">值</th>
                <th>操作</th>
                <th width="25"><input type="checkbox" id="rootCheckbox"/></th>
            </tr>
            </thead>
            <#list dicts as dict>
                <#assign items=dict.items/>
                <tr class="treegrid-${dict.id!}" id="${dict.id!}">
                    <td>${dict.name!}</td>
                    <td>${dict.title!}</td>
                    <td>${dict.value!}</td>
                    <td>
                        <a class="btn btn-xs btn-primary dict-edit" data-id="${dict.id!}">编辑</a>
                        <a class="btn btn-xs btn-danger dict-del"  data-id="${dict.id!}" data-name="${dict.name!}">删除</a>
                        <a class="btn btn-xs btn-info dict-item-add"  data-id="${dict.id!}">添加子项</a>
                    </td>
                    <td><input type="checkbox" data-id="${dict.id!}" /></td>
                </tr>
                <#list items as item>
                    <tr class="treegrid-${item.id!} treegrid-parent-${dict.id!}">
                        <td>${item.name!}</td>
                        <td>${item.title!}</td>
                        <td>${item.value!}</td>
                        <td>
                            <a class="btn btn-xs btn-success item-edit" data-id="${item.id!}" data-dict="${dict.id!}">编辑</a>
                            <a class="btn btn-xs btn-inverse item-del" data-id="${item.id!}" data-dict="${dict.id!}" data-name="${item.name!}">删除</a>
                        </td>
                        <td><i class="fa fa-ban td-ban"></i></td>
                    </tr>
                </#list>
            </#list>
        </table>
    </div>
</div>
</body>

<#--dict edit modal-->
<div class="modal fade" id="dictEditModal" tabindex="-1" role="dialog"
     aria-labelledby="dictEditModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="dictEditModalLabel">
                    编辑字典
                </h4>
            </div>
            <div class="modal-body">

            </div>
        </div>
    </div>
</div>

<#--item edit modal-->
<div id="itemEditModal" class="modal fade" role="dialog"
     aria-hidden="true" aria-labelledby="itemModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="itemModalLabel">
                    编辑子项
                </h4>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div>
</div>

<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.treegrid.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.cookie.js"></@com.script>
<@com.script name="static/thirdparty/handlebars/handlebars-v4.0.5.js"></@com.script>
<@com.script name="static/thirdparty/Validform/5.3.2/Validform.min.js"></@com.script>
<@com.script name="static/js/cfg/main.js"></@com.script>

<#--dict edit template-->
<script id="dict-edit-template" type="text/x-handlebars-template">
    <div class="container form-container">
        <form class="form-horizontal" id="dictForm">
            {{#if dict.id}}
            <input type="hidden"  name="id" value="{{dict.id}}">
            {{/if}}
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="dictName">名称</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="dictName" name="name" datatype="*" nullmsg="名称不能为空!"
                           {{#if ajaxurl}}ajaxurl="{{ajaxurl}}" errormsg="该名称在数据库中已存在!"{{/if}}
                    {{#if dict.name}}value="{{dict.name}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="dictTitle">标题</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="dictTitle" name="title"
                    {{#if dict.title}}value="{{dict.title}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="dictValue">值</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="dictValue" name="value"
                    {{#if dict.value}}value="{{dict.value}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-11 text-center">
                    <button class="btn btn-primary" id="saveDictBtn"> 保存 </button>
                </div>
            </div>
        </form>
    </div>
</script>

<#--item edit template-->
<script id="item-edit-template" type="text/x-handlebars-template">
    <div class="container form-container">
        <form class="form-horizontal" id="itemForm">
            <input type="hidden" name="dictId" value="{{item.dictId}}">
            {{#if item.id}}
            <input type="hidden"  name="id" value="{{item.id}}">
            {{/if}}
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="itemName">名称</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="itemName" name="name" datatype="*" nullmsg="名称不能为空!"
                    {{#if ajaxurl}}ajaxurl="{{ajaxurl}}" errormsg="该名称在数据库中已存在!"{{/if}}
                    {{#if item.name}}value="{{item.name}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="itemTitle">标题</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="itemTitle" name="title"
                    {{#if item.title}}value="{{item.title}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label text-right" for="itemValue">值</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" id="itemValue" name="value"
                    {{#if item.value}}value="{{item.value}}"{{/if}} >
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-11 text-center">
                    <button class="btn btn-primary" id="saveItemBtn"> 保存 </button>
                </div>
            </div>
        </form>
    </div>
</script>

<script type="text/javascript">

    $(document).ready(function () {

        var dictEditSource=$("#dict-edit-template").html();
        var itemEditSource=$("#item-edit-template").html();

        //初始化treegrid
        $('.tree').treegrid({
            initialState:'collapsed',
            saveState:true
        });

        //批量选中字典
        $("#rootCheckbox").on('click', function () {
            if ($(this)[0].checked) {
                $.each($("input[type='checkbox']"), function (idx, item) {
                    item.checked = true;
                });
            }
            else {
                $.each($("input[type='checkbox']"), function (idx, item) {
                    item.checked = false;
                });
            }
        });

        //批量删除字典
        $("#delBatchBtn").on('click',function(){
            var ids=[];
            var sels = $("input[type='checkbox']:checked").length;
            if(sels>0){
                for (var i = 0; i < sels; i++) {
                    var cb = $("input[type='checkbox']:checked")[i];
                    if ($(cb).attr("id") != "rootCheckBox")
                        ids.push($(cb).data("id"));
                }
                layer.confirm($("#rootCheckbox")[0].checked?'确认删除所有字典?':'确认删除选中的字典?', {icon: 3, title:'提示'}, function(index){
                    delBatch(ids);
                });
            }
        });

        //新增字典
        $("#addDictBtn").on('click',function(){
            var template = Handlebars.compile(dictEditSource);
            $("#dictEditModal").find("div[class='modal-body']").empty();
            $("#dictEditModal").find("div[class='modal-body']").append(template({dict:{},ajaxurl:root+"/config/dicts/validate"}));
            $("#dictEditModalLabel").text("新增字典");
            $("#dictEditModal").modal({'backdrop':'static'});

            validateForm($("#dictForm"),function(){
                saveDict(this.serializeObject());
            });
        });

        //编辑字典
        $(".dict-edit").on('click',function(){
            var dictId=$(this).data("id");
            var template = Handlebars.compile(dictEditSource);
            $.getJSON(root+"/config/dicts/get/"+dictId,function(data){
                $("#dictEditModal").find("div[class='modal-body']").empty();
                $("#dictEditModal").find("div[class='modal-body']").append(template({dict:data}));
                $("#dictEditModalLabel").text("编辑字典");
                $("#dictEditModal").modal({'backdrop':'static'});
                validateForm($("#dictForm"),function(){
                    saveDict(this.serializeObject());
                });
            });
        });

        //删除单个字典
        $(".dict-del").on('click',function(){
            var dId=$(this).data("id");
            var dName=$(this).data("name");
            layer.confirm("确认删除字典 "+dName+" ?",{icon:3,title:'提示'},function(){
                ajaxRequest(root+"/config/dicts/delete/"+dId,null,null);
            });

        });

        //添加字典项
        $(".dict-item-add").on('click',function(){
            var dId=$(this).data("id");
            var template = Handlebars.compile(itemEditSource);
            $("#itemEditModal").find("div[class='modal-body']").empty();
            $("#itemEditModal").find("div[class='modal-body']").append(template({item:{dictId:dId},ajaxurl:root+"/config/dicts/item/validate"}));
            $("#itemModalLabel").text("新增子项");
            $("#itemEditModal").modal({'backdrop':'static'});

            validateForm($("#itemForm"),function(){
                saveItem(dId,this.serializeObject());
            });
        });

        //编辑字典项
        $(".item-edit").on('click',function(){
            var itemId=$(this).data("id");
            var dId=$(this).data("dict");
            $.getJSON(root+"/config/dicts/item/get",{dictId:dId,id:itemId},function(data){
                console.log(data);
                var template = Handlebars.compile(itemEditSource);
                $("#itemEditModal").find("div[class='modal-body']").empty();
                $("#itemEditModal").find("div[class='modal-body']").append(template({item:data}));
                $("#itemEditModal").modal({'backdrop':'static'});
                validateForm($("#itemForm"),function(){
                    saveItem(dId,this.serializeObject());
                });
            });

        });
        //删除字典项
        $(".item-del").on('click',function(){
            var itemId=$(this).data("id");
            var dId=$(this).data("dict");
            layer.confirm("确认删除子项"+$(this).data("name")+" ?",{icon:3,title:'提示'},function(){
                ajaxRequest(root+"/config/dicts/item/delete/"+itemId,{dictId:dId},null);
            });
        });

    });

    /**
     * 验证表单
     * */
    function validateForm(form,callback) {
        form.Validform({
            tiptype: 3,
            label: ".label",
            showAllError: true,
            ignoreHidden:true,
            callback: function (data) {
                if (callback != undefined)
                    callback.call(data);
                return false;
            },
            ajaxurl:{
                success:function(data,obj){
                    if(data.status==="y"){
                        $(obj).removeClass("Validform_error");
                        $(obj).next("span").remove();
                    }else{
                        $(obj).next("span").removeClass("Validform_loading").addClass("Validform_wrong");
                        $(obj).next("span").text($(obj).attr("errormsg"));
                    }
                }
            }
        });
    }

    /**
     * 保存dict
     * */
    function saveDict(data){
        ajaxRequest(root+"/config/dicts/save",{content:JSON.stringify(data)},function(){
            $("#dictEditModal").modal('hide');
            layer.msg("操作成功!", {icon: 1});
            window.location.reload();
        });
    }

    /***
     * 保存item
     * */
    function saveItem(dictId,data){
        ajaxRequest(root+"/config/dicts/item/save",{dictId:dictId,content:JSON.stringify(data)},function(){
            $("#dictEditModal").modal('hide');
            layer.msg("操作成功!", {icon: 1});
            window.location.reload();
        });
    }

    /***
     * 批量删除字典
     * @param ids
     */
    function delBatch(ids) {
        if (ids.length > 0) {
            ajaxRequest(root + '/config/dicts/batch/delete', {ids: ids.join(",")}, null);
        }
    }

    /***
     * ajax request
     * @param url
     * @param data
     * @param callback
     */
    function ajaxRequest(rqUrl, rqData, callback) {
        if (rqUrl != undefined) {
            layer.msg('执行中..',{time:8000});
            $.ajax({
                url: rqUrl,
                data: rqData || {},
                method:'POST'
            }).done(function (rp) {
                layer.closeAll();
                if (rp.success == false) {
                    layer.msg(rp.msg, {icon: 2});
                } else {
                    if (callback != undefined)
                        callback.call(this,rp);
                    else
                    {
                        layer.msg("操作成功!", {icon: 1});
                        window.location.reload();
                    }

                }
            });
        }
    }
</script>
</html>