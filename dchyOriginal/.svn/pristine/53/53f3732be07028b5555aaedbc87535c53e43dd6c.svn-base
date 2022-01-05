<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>全文检索</title>
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
        .fullSearchs-panel{
            margin-top: 20px;
        }
        .fullSearchs-panel .panel-heading{
            height: 52px;
        }
        .fullSearchs-panel .panel-heading >a:first-child{
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

        .fullSearch-edit,
        .fullSearch-del{
            margin-left:6px;
        }

        .form-container {
            width: 680px;
            padding-top: 12px;
        }

    </style>
</head>
<body>
<div class="container">
    <div class="panel panel-default fullSearchs-panel">
        <div class="panel-heading">
            <a class="btn btn-sm btn-primary" id="addFullSearchBtn"><i class="fa fa-plus"></i> 添加图层</a>
            <a class="btn btn-sm btn-danger" id="delBatchBtn"><i class="fa fa-trash-o"></i> 批量删除</a>
        </div>
        <table class="table table-condensed table-bordered table-hover tree">
            <thead>
            <tr>
                <th width="30%">图层名</th>
                <th width="30%">标题</th>
                <th>操作</th>
                <th width="25"><input type="checkbox" id="rootCheckbox"/></th>
            </tr>
            </thead>
        <#list fullsearch as fullsearch>
            <#assign layer=fullsearch/>
            <tr class="treegrid-${layer.id!}" id="${layer.layer!}">
                <td>${layer.layer!}</td>
                <td>${layer.title!}</td>
                <td>
                    <a class="btn btn-xs btn-primary fullSearch-edit" data-layer='${layer.layer!}'>编辑</a>
                    <a class="btn btn-xs btn-danger fullSearch-del"  data-layer='${layer.layer!}' >删除</a>
                </td>
                <td><input type="checkbox" data-id="${layer.layer!}" /></td>
            </tr>
        </#list>
        </table>
    </div>
</div>
</body>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.treegrid.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.cookie.js"></@com.script>
<@com.script name="static/thirdparty/handlebars/handlebars-v4.0.5.js"></@com.script>
<@com.script name="static/thirdparty/Validform/5.3.2/Validform.min.js"></@com.script>
<@com.script name="static/js/cfg/main.js"></@com.script>

<script id="search-edit-template" type="text/x-handlebars-template">
    <div class="form-container">
            <form class="form-horizontal" id="searchDetail">
                <div class="form-group">
                    <label for="tcmc" class="col-sm-2 control-label">图层名称</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="tcmc" name="layer" value="{{layer.layer}}" {{#if layer.layer}} readonly {{/if}}>
                    </div>
                </div>

                <div class="form-group">
                    <label for="zd" class="col-sm-2 control-label">字段</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="zd" name="fields" placeholder="多个以逗号分隔" value="{{layer.fields}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="zj" class="col-sm-2 control-label">主键</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="zj" name="id" value="{{layer.id}}">
                    </div>
                </div>


                <div class="form-group">
                    <label for="bt" class="col-sm-2 control-label">标题</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="bt" name="title" value="{{layer.title}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="zbt" class="col-sm-2 control-label">子标题</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="zbt" name="subtitle" value="{{layer.subtitle}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="nrzd" class="col-sm-2 control-label">内容字段</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="nrzd" name="content"  placeholder="多个以逗号分隔" value="{{layer.content}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="zm" class="col-sm-2 control-label">组名</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="zm" name="group" value="{{layer.group}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="clj" class="col-sm-2 control-label">超链接</label>

                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="clj" name="link" value="{{layer.link}}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="sjy" class="col-sm-2 control-label">数据源</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="sjy" name="dataSource" value="{{layer.dataSource}}">
                    </div>
                </div>
                <div class="form-group" style="text-align: center;">
                    <button type="button" class="btn btn-primary save-fullsearch">保存</button>
                </div>
            </form>
    </div>
</script>

<script>
    var fullSearch;
    var template = Handlebars.compile($('#search-edit-template').html());
    $(function getFullSearch(){
        $.ajax({
            url: root + "/config/fullsearch/config",
            method: 'post',
            async: false,
            success: function (rp) {
                fullSearch = rp;
            }
        });
    });

    /**
     * 编辑图层
     */
    $('.fullSearch-edit').on('click',function(){
        var layerName = $(this).data('layer');
        var objs =fullSearch.layers;
        for(var i in objs){
              if(objs[i].layer === layerName){
                  layer.closeAll();
                  layer.open({
                      type: 1,
                      title: layerName,
                      area: '700px',
                      shade: 0,
                      content: template({layer:objs[i]}),
                      btn: []
                  });
              }
            $('.save-fullsearch').on('click',function(){
                saveFullSearch();
            });
        }

    });

    /**
     * 删除图层
     */
    $('.fullSearch-del').on('click',function(){
        var layerName = $(this).data('layer');
            layer.confirm('确定要删除该检索图层吗？', {
                btn: ['是', '否'] //按钮
            }, function () {
                var jstc = fullSearch.layers;
                for (var i = 0; i < jstc.length; i++) {
                    if (jstc[i].layer === layerName) {
                        jstc.splice(i, 1);
                        break;
                    }
                }
                var inlayers = {};
                inlayers.layers = jstc;
                var inlayer = JSON.stringify(inlayers);
                $.ajax({
                    url: root + "/config/fullsearch/update",
                    method: 'post',
                    async: false,
                    data: {config: inlayer},
                    success: function () {
                        layer.msg('删除成功！',{icon: 1,time:700});
                        setInterval("window.location.reload()",700);
                    }
                });
            })

    });


    $('#addFullSearchBtn').on('click',function(){
        layer.closeAll();
        layer.open({
            type: 1,
            title: '添加图层',
            area: '700px',
            shade: 0,
            content: template({layer:{}}),
            btn: []
        });
        $('.save-fullsearch').on('click',function(){
            var tcmc = $('#tcmc').val();
            if(tcmc==undefined||tcmc==''){
                layer.msg('图层名称不可为空！',{icon:0,time:2000});
                return;
            }else{
                for(var i in fullSearch.layers){
                    if(tcmc===fullSearch.layers[i].layer){
                        layer.msg('该图层已存在，请检查后重新输入！',{icon:0,time:2000});
                        return;
                    }
                }
            }
            saveFullSearch();
        });
    });

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

    function saveFullSearch(){
        var searchSize = 0;
        var form = serializeObject($('#searchDetail'));
        var searchChilds = '';
        $.ajax({
            url: root + "/config/fullsearch/config",
            method: 'post',
            async: false,
            success: function (rp) {
                searchChilds = rp['layers'];
                searchSize = searchChilds.length;
            }
        });
        for (var i = 0; i < searchChilds.length; i++) {
            if (searchChilds[i].layer == form.layer) {
                searchChilds.splice(i, 1);
                break;
            }
        }

        searchChilds.splice(0, 0, form);
        var layers = {};
        layers['layers'] = searchChilds;
        var inlayer = JSON.stringify(layers);
        $.ajax({
            url: root + "/config/fullsearch/update",
            method: 'post',
            async: false,
            data: {config: inlayer},
            success: function () {
                layer.msg('保存成功！',{icon: 1,time:700});
                setInterval("window.location.reload()",700);
            }
        });
    }

    /**
     * 批量删除
     */
    $("#delBatchBtn").on('click',function(){
        var ids=[];
        var sels = $("input[type='checkbox']:checked").length;
        if(sels>0){
            for (var i = 0; i < sels; i++) {
                var cb = $("input[type='checkbox']:checked")[i];
                if ($(cb).attr("id") != "rootCheckBox")
                    ids.push($(cb).data("id"));
            }
            layer.confirm($("#rootCheckbox")[0].checked?'确认删除所有检索图层?':'确认删除选中的检索图层?', {icon: 3, title:'提示'}, function(index){
                delBatch(ids);
            });
        }
    });

    function delBatch(layers){
        var allLayer = fullSearch.layers;
        for(var i in layers){
            var layerName = layers[i];
            for(var j in allLayer){
                if(layerName==allLayer[j].layer){
                    allLayer.splice(j,1);
                }
            }
        }

        var inlayer = {};
        inlayer.layers = allLayer;
        $.ajax({
            url: root + "/config/fullsearch/update",
            method: 'post',
            async: false,
            data: {config: JSON.stringify(inlayer)},
            success: function () {
                layer.msg('删除成功！',{icon: 1,time:700});
                setInterval("window.location.reload()",700);
            }
        });
    }

    var serializeObject = function (form) {
        var o = {};
        var a = form.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

</script>
</html>
