<#assign tpl=tpl!>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>地图模板配置-${tpl!}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
    <@com.style name="static/thirdparty/jqgrid/css/ui.jqgrid-bootstrap.css"></@com.style>
    <@com.style name="static/css/theme/default/cfg.css"></@com.style>
    <@com.style name="static/thirdparty/h-ui/lib/iconfont/iconfont.css"></@com.style>
    <@com.style name="static/thirdparty/semantic-ui/dropdown/dropdown.css"></@com.style>
    <style>
        .grid-panel {
            margin: 12px;
            margin-left: auto;
            margin-right: auto;
        }

        .grid-toolbar {
            position: relative;
            left: 25px;
        }

        .grid-content {
            margin-top: 5px;
            position: absolute;
            left: 25px;
            width: 100%;
        }

        .ui-jqgrid
        .ui-jqgrid-bdiv {
            overflow-x: hidden;
        }

        .form-control {
            padding: 3px 6px;
            font-size: 13px;
            height: 30px;
        }

        .grid-row-btn {
            margin-right: 6px;
        }

        .ui-jqgrid .ui-jqgrid-hdiv {
            background-color: #f5fafe;
        }

    </style>
</head>
<body>
<div class="cfg-nav row">
    <div class="col-xs-7 col-xs-offset-4 text-center">
        <ul class="nav nav-pills" role="tablist">
            <li role="presentation"><a href="<@core.rootPath/>/config/index">&nbsp;模板列表&nbsp;&nbsp;</a></li>
            <li role="presentation" class="nav-active"><a href="<@core.rootPath/>/config/tpl/services?tpl=${tpl!}">&nbsp;服务配置&nbsp;&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/widgets?tpl=${tpl!}">&nbsp;功能配置&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/attrs?tpl=${tpl!}">&nbsp;属性配置&nbsp;</a></li>
            <li role="presentation"><a href="<@core.rootPath/>/config/tpl/clearcache?tpl=${tpl!}">&nbsp;缓存清理&nbsp;</a></li>
        </ul>
    </div>
    <div class="clo-xs-1 pull-right">
        <a class="btn btn-info radius" href="<@core.rootPath/>/map/${tpl!}" title="预览模板" style="margin-right: 5px" target="_blank" >
            <i class="fa fa-eye" aria-hidden="true" style="color:white"></i>
        </a>
        <a class="btn btn-success radius" href="javascript:location.replace(location.href);" title="刷新" >
            <i class="fa fa-refresh" aria-hidden="true" style="color:white"></i>
        </a>
    </div>
</div>
<div class=".container-fluid grid-panel" id="gridPanel">
    <div class="grid-toolbar">
        <button class="btn btn-success" type="button" id="addDemo" data-toggle="modal"><i class="fa fa-plus"></i>&nbsp;&nbsp;添&nbsp;加</button>
    </div>
    <div class="grid-content">
        <table id="serviceGrid"></table>
        <div id="servcieGridPager"></div>
    </div>
</div>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery-ui.min.js"></@com.script>
<@com.script name="static/thirdparty/jqgrid/js/jquery.jqGrid.min.js"></@com.script>
<@com.script name="static/thirdparty/jqgrid/js/i18n/grid.locale-cn.js"></@com.script>
<@com.script name="static/thirdparty/treeview/bootstrap-treeview.min.js"></@com.script>
<@com.script name="static/thirdparty/mustache/mustache.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>

<script id="serviceAddItem" type="x-tpl-mustache">
    <div class="list-group-item service-add-item" data-id="{{id}}">
    <div class="pull-right">
    <a class="t-icon fa fa-plus" title="添加服务" data-info="{{info}}"></a>
    </div>
    <i class="fa fa-file-o"></i>&nbsp;&nbsp;{{name}}
    </div>
</script>
<script id="serviceModal" type="x-tpl-mustache">
    <div id="add-layers-modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="m-header">
                <a class="close-btn"></a>
                <a class="m-title">添加地图服务</a>
            </div>
            <div class="m-body">
                <div class="col-md-12 column">
                    <div class="row clearfix">
                        <div class="col-md-4 column">
                            <div class="tree-panel">
                                <i>无数据</i>
                            </div>
                        </div>
                        <div class="col-md-8 column">
                            <div class="layers-panel panel panel-default">
                                <div class="panel-body list-group">
                                    <i>无服务</i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</script>
<script type="text/javascript">
    var allServices = [];
    var gridOptions={};
    var $addDemo;
    var $btnLink;
    var treePanel;
    gridOptions.jsonReader={
        root: "services",
        id: "0"
    };
    gridOptions.height = window.innerHeight-105;
    $('#nextStep').on("click", function(){
        window.location.href = "<@com.rootPath/>/config/tpl/widgets?tpl=" + "${tpl!}";
    });

    var $grid;
    $(function(){
        $grid=$("#serviceGrid");
        initGridTable(gridOptions);
        $addDemo = $("#addDemo");
        $addDemo.attr("title","添加服务");
        $("#gridPanel").append(Mustache.render($("#serviceModal").html()));
        $addDemo.attr("href", "#add-layers-modal");
    });

    /**
     * 表格初始化
     * */
    function initGridTable(options){
        var gridWidth = $("#gridPanel").width() * 0.975;
        $grid.jqGrid({
            url:'<@com.rootPath/>/config/tpl/'+'${tpl!}'+'/services',
            datatype:"json",
            jsonReader:options.jsonReader,
            styleUI : 'Bootstrap',
            colNames : [ 'id','序号', '服务名称','服务别名', '服务类型','年份','服务地址','默认可见','透明度','修改信息','删除'],
            colModel : [
                {name : 'id',index : 'id',hidden:true,hidedlg:true,key:true},
                {name : 'index',index : 'index',width : gridWidth*0.05,resizable:false,sortable : false},
                {name : 'name',index : 'name',width : gridWidth*0.10,sortable : false,editable:true,edittype:'text'},
                {name : 'alias',index : 'alias',width : gridWidth*0.10,sortable : false,editable:true,edittype:'text'},
                {name: 'type', index: 'type', width: gridWidth*0.07, resizable: false, sortable: false,editable:true,edittype:'select',editoptions:{value:'tiled:静态服务;dynamic:动态服务'},
                    formatter: 'select'},
                {name : 'year',index : 'year',width : gridWidth*0.05,sortable : false,resizable:false},
                {name : 'url',index : 'url',width : gridWidth*0.33,sortable : false},
                {name : 'visible',index : 'visible',align:'center',width : gridWidth*0.05,sortable : false,editable:true,edittype:'checkbox',formatter:'checkbox'},
                {name : 'alpha',index : 'alpha',align : 'center',width : gridWidth*0.05,sortable : false, editable:true,edittype:'text'},
                {name:'act',index:'act',sortable:false,width:gridWidth*0.13,resizable:false,editable:false},
                {name:'del',index:'del',sortable:false,width:gridWidth*0.07,resizable:false,editable:false}
            ],
//            rowNum:options.pageSize||10,
            rowNum: 200,
            rowList:options.rowList||['5','10','15'],
            viewrecords: true,
            sortname:'index',
            sortorder: "desc",
            altRows:false,
            loadComplete:function(evt){
                allServices = evt.services;
            }
            ,
            gridComplete: function () {
                addOperBtns();
                sortableListener();
            },
            ondblClickRow: function (data, rowindex, rowobj) {
            },
            caption:"",
            height:options.height-40,
            autowidth:false
        });

    }
    /***
     * 添加编辑操作按钮
     */
    function addOperBtns() {
        var ids = $grid.jqGrid('getDataIDs');
        for (var i = 0; i < ids.length; i++) {
            var cl = ids[i];
            var be = "<a class='btn btn-primary btn-xs grid-row-btn' data-type='edit' data-row='"+ cl+"'><i class='fa fa-edit'></i>&nbsp;编辑</a>";
            var se = "<a class='btn btn-success btn-xs grid-row-btn' data-type='save' data-row='"+ cl+"'><i class='fa fa-check'></i>&nbsp;保存</a>";
            var ce = "<a class='btn btn-default btn-xs grid-row-btn' data-type='cancel' data-row='"+ cl+"'><i class='fa fa-times'></i>&nbsp;取消</a>";
            var de = "<a class='btn btn-inverse btn-xs grid-row-btn' data-type='del' data-row='"+ cl+"'><i class='fa fa-trash-o'></i>&nbsp;删除</a>";
            $grid.jqGrid('setRowData', ids[i], {
                act: be + se + ce,
                del:de
            });
        }
        $(".grid-row-btn[data-type!='edit'][data-type!='del']").hide();
        //监听按钮的单击事件
        $(".grid-row-btn").on('click',function(){
            var type=$(this).data("type");
            var cl=$(this).data("row");
            switch(type){
                case 'edit':
                {
                    $grid.editRow(cl);
                    //禁用拖动排序
                    $grid.jqGrid('sortableRows',{disabled: true});
                    $(this).nextAll(".grid-row-btn[data-type!='del']").show();
                    $(this).hide();
                    break;
                }
                case 'save':
                {
                    $grid.saveRow(cl);
                    updateSingleService($grid.getRowData(cl));
                    $(this).hide();
                    $(this).next().hide();
                    $(this).prev().show();
                    //启用拖动排序并添加监听
                    $grid.jqGrid('sortableRows',{disabled: false});
                    sortableListener();
                    clearCache();
                    //$grid.trigger('reloadGrid');
                    break;
                }
                case 'cancel':
                {
                    $grid.restoreRow(cl);
                    $(this).prev().hide();
                    $(this).hide();
                    $(this).prev().prev().show();
                    //启用拖动排序并添加监听
                    $grid.jqGrid('sortableRows',{disabled: false});
                    sortableListener();
                    break;
                }
                case 'del':
                {
                    layer.confirm("确定要删除这条服务吗？",function(){
                        $grid.delRowData(cl);
                        $.ajax({
                            url:'<@com.rootPath/>/config/tpl/'+'${tpl!}'+'/service/remove/'+ cl,
                            aysnc:false,
                            method:'POST',
                            success:function(rp){
                                if(rp == ""){
                                    $('alertModal').modal({
                                        backdrop:true,
                                        keyboard:true,
                                        show:true
                                    });
                                    layer.msg('删除成功！', {
                                                icon: 1,
                                                time: 2000
                                            }, function(){
                                                changeServicesIndex($(".ui-sortable").sortable("toArray"));
                                                clearCache();
                                            }
                                    );
                                }
                            }
                        });
                    });
                    break;
                }
            }
        });
    }

    /***
     *拖动排序
     */
    function sortableListener(){
        $grid.jqGrid('sortableRows',{
            update:function(){
                var a = $(".ui-sortable").sortable("toArray");
                changeServicesIndex(a);
                $grid.trigger('reloadGrid');
                layer.msg('修改成功!', {
                            icon: 1,
                            time: 2000
                        },function(){
                            clearCache();
                        }
                );
            }
        });
    }

    /***
     * 修改服务顺序
     */
    function changeServicesIndex(a){
        getTplServices();
        $(allServices).each(function(index, item){
            item.index = a.indexOf(item.id) + 1;
        });
        updateServices(allServices);
    }

    /***
     * 点击添加服务按钮时，获取所有分组服务并进行渲染
     */
    $('#addDemo').on('click',function(){
        $.ajax({
            url: '<@com.rootPath/>/config/services/groups',
            method:'POST',
            success:function(evt){
                if(evt.hasOwnProperty("success") && evt.success == false){
                    alert(evt.msg);
                    return;
                }
                var treeData = changeChildren2Node(evt);
                treePanel = $("#add-layers-modal .m-body .tree-panel");
                treePanel.empty();
                treePanel.treeview({data: treeData, levels: 1, nodeIcon: 'glyphicon glyphicon-file'});
                treePanel.on('nodeSelected', function(evt, data){
                    var listGroup = $("#add-layers-modal .layers-panel .list-group");
                    listGroup.empty();
                    $.ajax({
                        url:'<@com.rootPath/>/config/services/groups',
                        method:'POST',
                        data:{groupId:data.id},
                        success:function(event){
                            if(event != undefined && event.length > 0){
                                listGroup.append("<i class='btn btn-link' title='添加当前分组的所有服务（自动忽略已经添加的服务）'>添加当前分组服务</i>");
                                $btnLink = $('#add-layers-modal .m-body .btn-link');
                                $($btnLink).data("info", event);
                                $.each(event, function (index, item){
                                    Mustache.parse($("#serviceAddItem").html());
                                    item.info = JSON.stringify(item);
                                    listGroup.append(Mustache.render($("#serviceAddItem").html(), item));
                                });
                                var allowedAdd = 0;
                                var ids = $grid.jqGrid('getDataIDs');
                                listGroup.children().each(function(i, v){
                                    $.each(allServices,function(i, n){
                                        if(n.id == $(v).data('id')){
                                            disableClick($(v));
                                        }
                                    });
                                    if(ids.indexOf($(v).data('id')) === -1){
                                        allowedAdd += 1;
                                    }
                                });
                                if(allServices.length > 0 && allowedAdd === 1){
                                    disableClick($btnLink);
                                    disaleBtnlink($btnLink);
                                }
                                addListeners();
                            }
                            else{
                                listGroup.append("<i>该目录下没有服务</i>");
                            }
                        }
                    });
                });
            }
        });
    });
    /***
     * 转换子项为node叶子节点
     * @param value
     * @returns {*}
     */
    function changeChildren2Node(value){
        return $.map(value, function(item, index){
                    item.text = item.name;
                    delete item.name;
                    if(!item.children) return item;
                    item.nodes = changeChildren2Node(item.children);
                    delete item.childern;
                    return item;
                }
        );
    }
    /***
     *监听服务添加按钮和添加所有服务
     */
    function addListeners(){
        $('.t-icon').off();
        $('.btn-link').off();
        $('.t-icon').on('click', function(){
            var $this = this;
            var s=$($this).data("info");
            s.index = allServices.length + 1;
            if (s.type === "export")s.type = "dynamic";
            var arr = s.url.split(window.location.host);
            if (arr.length > 1) {
                arr = s.url.split("/oms/");
                s.url = "/oms/" + arr[1];
            } else {
                s.url = arr[arr.length - 1];
            }
            console.log(arr);
            var info = JSON.stringify(s);
            $.ajax({
                url: "<@com.rootPath/>/config/tpl/" +"${tpl!}"+ "/service/save",
                method: 'post',
                data: {service: info},
                success: function (rp){
                    if(rp == ''){
                        disableClick($($this).parent().parent());
                        $grid.trigger("reloadGrid");
                        clearCache();
                    }
                }
            });
        });

        $('.btn-link').on("click", function(){
            var $this = this;
            var s = $($this).data("info");
            $(s).each(function(index, item){
                if(item.type === "export")item.type = "dynamic";
                var arr = item.url.split(window.location.host);
                item.url = arr[arr.length -1];
            });
            var ids = $grid.jqGrid('getDataIDs');
            var arr = [];
            $.each(s, function(i, n){
                if(ids.indexOf(n.id) === -1) arr.push(n);
            });
            allServices = allServices.concat(arr);
            $.each(allServices, function(index, item){
                item.index = index + 1;
            });
            updateServices(allServices);
            disableClick($($this).parent().children());
            disaleBtnlink($this);
            clearCache();
        });
    }

    function disaleBtnlink($this){
        $($this).off();
        $($this).css({"text-decoration": "line-through"});
    }
    /***
     * 服务已经存在与模板中 则将其置为灰 不可编辑
     * @param $this
     */
    function disableClick($this) {
        $this.attr('class', 'list-group-item-added');
        $this.find('.fa-plus').attr('class', 't-icon fa fa-ban');
        $this.find('.fa-ban').attr('title', '已添加');
        $this.css({'position': 'relative', 'display': 'block', 'padding': '10px 15px', 'margin-bottom': '1px',
            'background-color': '#fff', 'border-bottom': '1px solid #dddddd', 'color': '#777'});
        $this.find('.fa-ban').off();
    }
    /***
     * 更新单条数据
     */
    function updateSingleService(v) {
        var s = undefined;
        for (var cell in v){
            if(cell === "act" || cell === "del") delete v[cell];
            if(cell === "visible") v[cell] = v[cell] === "Yes" ? "true" : "false";
        }
        $.each(allServices,function(index, item){
            if(item.id === v.id){
                for (var property in item){
                    for (var cell in v){
                        if(property === cell){
                            item[property] = v[cell];
                        }
                    }
                }
                s = item;
            }
        });

        if (typeof s === "string")
            s = s;
        if (typeof s === "object")
            s = JSON.stringify(s);
        if (s != undefined) {
            $.ajax({
                url: '<@com.rootPath/>/config/tpl/' + '${tpl!}' + '/service/save',
                method: 'POST',
                data: {service: s},
                success: function (rp) {
                    if (rp == '') {
                        layer.msg('修改成功！', {
                                    icon: 1,
                                    time: 2000
                                }, function () {
                                }
                        );
                    }
                }
            });
        }
    }
    /***
     * 更新多条数据
     */
    function updateServices(d){
        var s = undefined;
        if(typeof d === "string")
            s = d;
        if(typeof d === "object")
            s = JSON.stringify(d);
        if(s != undefined){
            $.ajax({
                url:'<@com.rootPath/>/config/tpl/'+'${tpl!}'+'/services/save',
                method:'POST',
                data:{services : s},
                success:function(rp){
                    $('#alertModal').modal({
                        backdrop:true,
                        keyboard:true,
                        show:true
                    });
                    if(rp == ''){
                        $grid.trigger('reloadGrid');
                    }
                }

            });
        }
    }

    /***
     * 获取当前模板中的所有服务
     */
    function getTplServices(){
        $.ajax({
            url: '<@com.rootPath/>/config/tpl/'+'${tpl!}'+'/services',
            async:false,
            complete:function(evt){
                allServices = $.parseJSON(evt.responseText).services;
            }
        });
    }

    /***
     * 清理服务缓存
     */
    function clearCache(){
        $.ajax({
            url: '<@com.rootPath/>/map/'+ '${tpl!}' +'/clearServiceCache',
            method: 'post',
            complete: function (evt) {
                var r = $.parseJSON(evt.responseText);
                if (r !== true) {
                    layer.msg("自动清理缓存失败，请手动进行清理!", {time: 1000});
                }
            }
        });
    }

</script>
</body>
</html>
