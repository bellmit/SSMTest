<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>监控点元数据</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
    <@com.style name="static/thirdparty/ztree_normal/css/metroStyle/metroStyle.css"></@com.style>
    <style>
        body{
            font-size: 13px;
            overflow-x: hidden;
        }

        .layui-form-item{
            margin: 8px 4px 8px 4px;
        }

        .layui-form-label{
            width: 85px;
        }

        .panel{
            margin-top: 10px;
        }

        .panel-heading{
            color: #666;
        }

        .panel-heading{
            height:55px;
            margin-bottom: -10px;
        }
    </style>
</head>
<body>
<div style="margin: 0 10px 0 10px;">
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="pull-left">
                <div style="margin-bottom: 10px">
                    <span class="regionName">江苏省</span><span>&nbsp;&nbsp;&nbsp;&nbsp;当前在线:</span>
                    <small style="background-color: #d9e7fd">${currentStatus.onLine!}</small>
                    <span>&nbsp;&nbsp;</span>
                    <span>当前离线:</span>
                    <small style="background-color: #d9e7fd">${currentStatus.offLine!}</small>
                </div>
                <div name="regionCount" style="display: none">
                    <span class="regionName" name="regionName"></span><span>&nbsp;&nbsp;&nbsp;&nbsp;当前在线:</span>
                    <small style="background-color: #d9e7fd" name="regionOnLine"></small>
                    <span>&nbsp;&nbsp;</span>
                    <span>当前离线:</span>
                    <small style="background-color: #d9e7fd" name="regionOffLine"></small>
                </div>

            </div>
            <#--<div class="pull-left">-->
                <#--<select class="select" id="regionName" style="width: 120px;height: 30px;">-->
                    <#--<option value="">行政区划</option>-->
                    <#--<#list regions as regionName>-->
                        <#--<option value="${regionName!}">${regionName!}</option>-->
                    <#--</#list>-->
                <#--</select>-->
            <#--</div>-->
            <div class="pull-right">
                    <div class="ui toggle checkbox">
                        <input type="checkbox" name="useCache">
                        <label>是否显示在线状态</label>
                    </div>
                </div>
                <input type="text" style="width: 140px;height: 30px;" id="searchInput" placeholder="设备名称/设备编码">
                <i class="layui-icon" style="font-size: 18px;cursor: pointer;" title="点击查询" id="search">&#xe615;</i>
                <button type="button" class="layui-btn layui-btn-sm" id="addCamera">
                    <i class="layui-icon">&#xe61f;</i>增加
                </button>
                <button type="button" class="layui-btn layui-btn-sm" id="importExcel">
                    <i class="layui-icon">&#xe62f;</i>导入配置
                </button>
                <button type="button" class="layui-btn layui-btn-sm" id="exportExcel">
                    <i class="layui-icon">&#xe601;</i>导出配置
                </button>
            </div>
        </div>
        <div class="row cl" style="margin-top: 20px">
            <div class="col-md-2">
                <div class="panel panel-default" style="margin: 10 0 10 10;overflow:auto">
                    <div id="regionTree" class="ztree" style="height: 475px">
                    </div>
                </div>
            </div>
            <div class="col-md-10">
                <table id="cameraTable" lay-filter="cameraTable"></table>
            </div>
        </div>
    </div>
</div>
<div style="display: none" id="addCameraTpl">
    <form class="layui-form" id="cameraForm">
        <div class="layui-form-item">
            <label class="layui-form-label">区域名称</label>
            <div class="layui-input-block">
                <input type="text" name="regionName" required lay-verify="required" placeholder="区域名称" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">设备名称</label>
            <div class="layui-input-block">
                <input type="text" name="name" required lay-verify="required" placeholder="设备名称" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">区域编码</label>
            <div class="layui-input-block">
                <input type="text" name="vcuId" required lay-verify="required" placeholder="区域编码" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">设备编码</label>
            <div class="layui-input-block">
                <input type="text" name="indexCode" required lay-verify="required" placeholder="设备编码" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">X坐标</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="y" required lay-verify="required" placeholder="X坐标" autocomplete="off"-->
                       <#--class="layui-input">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">Y坐标</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="x" required lay-verify="required" placeholder="Y坐标" autocomplete="off"-->
                       <#--class="layui-input">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">高度</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="height" required lay-verify="required|number" placeholder="高度" autocomplete="off"-->
                       <#--class="layui-input">-->
            <#--</div>-->
        <#--</div>-->
        <div class="layui-form-item">
            <label class="layui-form-label">平台</label>
            <div class="layui-input-block">
                <input type="text" name="platform" required lay-verify="required" placeholder="平台" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">设备类型</label>
            <div class="layui-input-block">
                <select name="type" lay-verify="required">
                    <option value=""></option>
                    <option value="normal" selected>普通设备</option>
                    <option value="mobile">移动设备</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">负责人</label>
            <div class="layui-input-block">
                <input type="text" name="linkman" lay-verify="required" placeholder="负责人" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">联系电话</label>
            <div class="layui-input-block">
                <input type="text" name="phone" lay-verify="required" placeholder="联系电话" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="submitCamera">立即提交</button>
                <button type="reset" id="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>
</body>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>
<@com.script name="static/js/preview.js"></@com.script>
<@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
<@com.script name="static/thirdparty/ztree_normal/js/jquery.ztree.all.js"></@com.script>

<script type="text/html" id="deviceTypeTpl">
    {{#  if(d.type == 'normal'){ }}
    普通设备
    {{#  } else if(d.type == 'mobile'){ }}
    移动设备
    {{#  } else{ }}
    其他设备
    {{#  } }}
</script>

<script type="text/html" id="toolBar">
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
</script>
<script type="text/html" id="uploadToolBar">
    {{#if (d.fileId != undefined){}}
    <a class="layui-btn layui-btn-primary  layui-btn-xs"  value="{{d.fileId}}" lay-event="viewFile">下载</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs"  value="{{d.fileId}}" lay-event="deleteFile">删除</a>
    {{#}else{}}
    <a class="layui-btn  layui-btn-xs reportUpload" id="reportUpload{{d.id}}">上传</a>
    {{#}}}
</script>

<script type="text/javascript">

    var videoTreeObj,table,tableOption;
    //表格渲染参数

    layui.use(["table","layer","upload","form"], function () {
        tableOption = {
            elem: '#cameraTable'
            ,height: 475
            ,url: '<@core.rootPath/>/video/config/region/page' //数据接口
            ,page: true //开启分页
            , cols: [[ //表头
                {field: 'regionName', title: '区域名称', width: '10%', edit: "text", sort: true}
                , {field: 'name', title: '设备名称', width: '10%', edit: "text", sort: true}
                , {field: 'vcuId', title: '区域编码', width: '10%', edit: "text", sort: true}
                , {field: 'indexCode', title: '设备编码', width: '10%', edit: "text", sort: true}
                // , {field: 'x', title: 'X坐标', width: '10%', edit: "text", sort: true}
                // , {field: 'y', title: 'Y坐标', width: '10%', edit: "text", sort: true}
                // , {field: 'height', title: '高度', width: '6%', edit: "text", sort: true}
                , {field: 'platform', title: '平台', width: '10%', edit: "text", sort: true}
                , {field: 'type', title: '设备类型', width: '10%', templet: '#deviceTypeTpl', sort: true}
                , {field: 'linkman', title: '负责人', width: '10%', edit: "text", sort: true}
                , {field: 'phone', title: '联系电话', width: '10%', edit: "text", sort: true}
                // ,{field: 'fileId',title:'检验报告', width: '15%', align: 'center', toolbar: '#uploadToolBar'}
                //新增字段
                // ,{field: 'platform', title: '负责人姓名', width: '8%', edit: "text", sort: true}
                // ,{field: 'platform', title: '联系方式', width: '8%', edit: "text", sort: true}
                // ,{field: 'platform', title: '联系方式', width: '8%', edit: "text", sort: true}
                ,{fixed: 'right', width: '10%',title:'操作', align: 'center', toolbar: '#toolBar'}

            ]]
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'size' //每页数据量的参数名，默认：limit
            }
            ,response: {
                statusName: 'status' //数据状态的字段名称，默认：code
                ,statusCode: null //成功的状态码，默认：0
                ,msgName: '' //状态信息的字段名称，默认：msg
                ,countName: 'totalElements' //数据总数的字段名称，默认：count
                ,dataName: 'content' //数据列表的字段名称，默认：data
            },
            done: function(res, curr, count){
                //重新初始化
                initYSBGUpload();
            }
        };

        table = layui.table;
        var layer = layui.layer;
        var upload = layui.upload;
        var form = layui.form;

        table.render(tableOption);

        //表格内容编辑
        table.on('edit(cameraTable)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
            $.ajax({
                url:"<@core.rootPath/>/video/metadata/update",
                data:{camera:JSON.stringify(obj.data)},
                success: function (r) {
                    if (r)
                        layer.msg(" 更新成功！", {icon: 1, time: 1000});
                    else
                        layer.msg(" 更新失败！", {icon: 2, time: 1000});
                }
            });
        });

        //文本框检索
        $("#search").on("click",function(){
            var condition = $("#searchInput").val();
            $.trim(condition);
            if(condition){
                tableOption.where = {region:"",condition:condition};
                table.reload('cameraTable',tableOption);
            }else{
                tableOption.where = {region: $("#regionName").val(),condition:""};
                table.reload('cameraTable',tableOption);
            }
        });

        //监听工具条
        table.on('tool(cameraTable)', function(obj){ //注：tool是工具条事件名，cameraTable是table原始容器的属性 lay-filter="对应的值"
            // var data = obj.data; //获得当前行数据
            // var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            toolbarEventsFactory(obj);
        });

        //初始化上传监控点控件
        var uploadInst = upload.render({
            elem: '#importExcel' //绑定元素
            ,accept:"file"
            ,exts:"xls|xlsx"
            ,url: '<@core.rootPath/>/video/metadata/import/file' //上传接口
            ,done: function(data){
                if(data.result == true){
                    layer.msg('导入解析成功！',{icon: 1,time: 1000});
                    table.reload('cameraTable',tableOption);
                }else{
                    layer.msg("导入解析异常，请查看日志！",{icon:2,time:1000});
                }
            }
            ,error: function(){
                layer.msg("导入失败！",{icon:2,time:1000});
            }
        });
        //初始化验收报告控件
        function initYSBGUpload() {
            //根据命名约束上传
            $(".reportUpload").map(function () {
                var id= $(this).attr("id");
                //根据id获取cameraID
                var cameraid = id.replace("reportUpload","");
                //初始化upload控件
                upload.render({
                    elem: '#'+id, //绑定元素,
                    url:"<@core.rootPath/>/file/uploadReport/"+cameraid,
                    accept: 'file' ,//允许上传的文件类型 ,
                    before : function(obj){
                        layer.load();
                    },
                    done: function (res) {
                        layer.closeAll('loading');
                        layer.msg("上传成功！", {icon: 1, time: 2000});
                        table.reload('cameraTable',tableOption);
                        initYSBGUpload();
                    }
                    , error: function () {
                        layer.msg("上传失败！", {icon: 2, time: 2000})
                    }
                });
            });
        }

        //添加设备
        $("#addCamera").on("click",function(){
            var addLayer = layer.open({
                type: 1,
                title:"添加设备",
                area: ['650px', '560px'],
                content: $("#addCameraTpl").html(),
                success:function () {
                    $("#reset").click();
                    form.on('submit(submitCamera)', function(data){
                        $.ajax({
                            url:"<@core.rootPath/>/video/metadata/update",
                            data:{camera:JSON.stringify(data.field)},
                            success: function (r) {
                                layer.close(addLayer);
                                if (r) {
                                    layer.msg("添加成功！", {icon: 1, time: 1000});
                                    table.reload('cameraTable', tableOption);

                                }
                                else {
                                    layer.msg("添加失败！", {icon: 2, time: 1000});
                                }
                            }
                        });
                        return false;
                    });
                }

            });
        });

        debugger
        $('#useCache').on('switch-change', function (e, data) {
            // var $el = $(data.el)
            //         , value = data.value;
            // console.log(e, $el, value);
        });

        //事件工厂
        function toolbarEventsFactory(obj) {
            if(!obj){
                console.log("事件参数obj为空");
                return;
            }
            var data = obj.data;
            function deleteCamera() {
                layer.confirm('确认删除此设备么？', function(index){
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    //向服务端发送删除指令
                    $.ajax({
                        url:"<@core.rootPath/>/video/metadata/delete",
                        data:{camera:data.id},
                        success: function (r) {
                            if (r) {
                                layer.msg("删除成功！", {icon: 1, time: 1000});
                            }
                            else {
                                layer.msg("删除失败！", {icon: 2, time: 1000});
                            }
                        }
                    });
                });
            }
            function deleteFile() {
                //删除
                layer.confirm('确认删除此附件么？', function(index){
                    $.ajax({
                        url:"<@core.rootPath/>/file/delete/"+data.fileId+"/"+data.id,
                        success: function (r) {
                            if (!r) {
                                layer.msg("删除成功！", {icon: 1, time: 1000});
                                //刷新界面
                                table.reload('cameraTable',tableOption);
                            }
                            else {
                                layer.msg("删除失败！", {icon: 2, time: 1000});
                            }
                        }
                    });
                });
                //刷新
            }
            function viewFile() {
                window.location.href = "<@core.rootPath/>/file/download/" + data.fileId;
            }
            switch (obj.event){
                case "delete":
                    deleteCamera();
                    break;
                case "deleteFile":
                    deleteFile();
                    break;
                case "viewFile":
                    viewFile();
                    break;
            }
        }
    });
    //导出配置
    $("#exportExcel").on('click', function(){
        window.location = '<@core.rootPath/>/video/metadata/export'
    });
    //初始化树
    function initTree() {
        $.ajax({
            url:  "<@core.rootPath/>/video/fetch/root/groups",
            success: function (r) {
                renderVideoTreeData(r);
            }
        });
    }
    //渲染树
    function renderVideoTreeData(data) {
        //查询行政区下所有摄像头
        function getRegionByName() {
            $.ajax({
                url: "<@core.rootPath/>/video/fetch/region/cameras",
                data: {regionName: treeNode.name},
                success: function (r) {
                    if (r instanceof Array && r.length > 0) {
                        videoTreeObj.addNodes(treeNode, r);
                    } else {
                        console.warn('此区域未发现监控点！');
                    }
                }
            });
        }
        for(var i =0;i<data.length;i++){
            data[i].open="close";
        }
        $("#regionTree").empty();
        var treeSetting = {
            view: {
                dblClickExpand: false,
                showLine: true,
                selectedMulti: false,
                showIcon:true
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: ""
                }
            },
            callback: {
                beforeClick: function (treeId, treeNode){
                    debugger;
                    var arr =[];
                    var regionNames="";
                    arr=getAllChildren(treeNode);
                    for(var i=0;i<arr.length;i++){
                        regionNames=regionNames+"|"+arr[i]["name"];
                    }
                    tableOption.where = {regions:regionNames,condition: ""};
                    table.reload('cameraTable',tableOption);

                    $.ajax({
                        url:"<@core.rootPath/>/video/getCamerasCountByRegID",
                        data:{regionID:treeNode["id"]},
                        success:function (er) {
                            //
                            $("[name=regionCount]").show();
                            $("[name=regionName]").text(treeNode["name"]);
                            $("[name=regionOnLine]").text(er["regOnLine"]);
                            $("[name=regionOffLine]").text(er["regOffLine"]);
                        }
                    });
                }
            }
        };
        videoTreeObj = $.fn.zTree.init($("#regionTree"), treeSetting, data);
        // var scrollHeight = $(window).height() - 300;
        // var width = $('regionTree').width();
    }
    //初始化树
    initTree();

    function getAllChildren(node) {
        var result =[];
        if(node["children"]&&node["children"].length>0){
            for(var i=0;i<node["children"].length;i++){
                var item = node["children"][i];
                var nodes= getAllChildren(item);
                result= result.concat(nodes);
            }
        }else {
            result.push(node);
        }
        return result;
    }

</script>