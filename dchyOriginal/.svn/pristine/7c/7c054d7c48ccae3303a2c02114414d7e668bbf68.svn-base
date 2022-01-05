<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>监控点离线时长统计</title>
        <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
    </head>
    <body>
        <div class="layui-main" style="margin-top: 20px;">
            <blockquote class="layui-elem-quote" style="margin-bottom: 0px !important;">
                <div class="layui-form">
                    <div class="layui-inline">
                        <select name="area" id="area" lay-filter="area">
                            <option value="">请选择一个市</option>
                        </select>
                    </div>
                    <div class="layui-inline">
                        <select name="street" id="street" lay-filter="street" lay-search>
                            <option value="">请先选择一个区县</option>
                        </select>
                    </div>
                    <div class="layui-inline">
                        <button id="weekReport" hidden="hidden">周报</button>
                        <button id="monthReport" hidden="hidden">月报</button>
                    </div>
                </div>
            </blockquote>
            <table id="trendTbl" lay-filter="trend"></table>
        </div>
    </body>
    <@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
    <@com.script name="static/js/cfg/main.js"></@com.script>
    <script type="text/html" id="timeTpl">
        {{# var oneWeek = 604800, oneDay = 86400, oneHour = 3600; }}
        {{# if(d.offlineTime >= oneWeek){ }}
        <span class="layui-badge">{{(d.offlineTime/86400).toFixed(1)}} 天</span>
        {{# } else if(d.offlineTime >= oneDay){ }}
        <span class="layui-badge layui-bg-orange">{{(d.offlineTime/86400).toFixed(1)}} 天</span>
        {{# } else if(d.offlineTime >= oneHour){ }}
        <span class="layui-badge layui-bg-cyan">{{(d.offlineTime/3600).toFixed(1)}} 小时</span>
        {{# } else { }}
        <span class="layui-badge layui-bg-green">{{d.offlineTime/60}} 分钟</span>
        {{#  } }}
    </script>

    <script id="regionSelectOption" type="text/html">
        {{#  layui.each(d, function(index, item){ }}
        <option value="{{item.name}}">{{item.name}}</option>
        {{#  }); }}
    </script>

    <script id="cameraSelectOption" type="text/html">
        {{#  layui.each(d, function(index, item){ }}
        <option value="{{item.indexCode}}">{{item.name}}</option>
        {{#  }); }}
    </script>
    <script id="reportTpl" type="text/html">
        <table cellspacing="0" cellpadding="0" border="0" class="layui-table">
            {{# layui.each(d,function(index,item){ }}
            <tr>
                <td>
                    <a class="btn btn-success-outline mr-10 ml-10" href="javascript:void(0)" onclick="downLoad('{{item.id}}')">&nbsp;&nbsp;{{item.name}}</a>
                </td>
            </tr>
            {{# });}}
        </table>
    </script>
    <script type="text/javascript">
        var rawData = ${data!'[]'};
        var region = [];

        layui.use(['jquery', 'form', 'table', 'laytpl'], function () {
            var table = layui.table, $ = layui.jquery, form = layui.form, laytpl = layui.laytpl;

            var dataTbl = table.render({
                elem: '#trendTbl'
                , height: 475
                , data: rawData
                , page: true //开启分页
                , cols: [[ //表头
                    {field: 'idx', title: '序号', type: 'numbers', width: 60}
                    , {field: 'region', title: '区域名称', minWidth: 120}
                    , {field: 'name', title: '监控点名称', minWidth: 150}
                    , {field: 'indexCode', title: '监控点设备编码', minWidth: 150}
                    , {field: 'offlineTime', title: '监控点离线时长', sort: true, templet: '#timeTpl'}
                ]],
                initSort: {
                    field: 'offlineTime' //排序字段，对应 cols 设定的各字段名
                    , type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
                }
            });

            $.ajax({
                url: "<@com.rootPath/>/video/fetch/root/groups",
                success: function (r) {
                    region = r;
                    var html = laytpl($("#regionSelectOption").html()).render(r);
                    $("#area").append(html);
                    form.render('select');
                    // 查询
                }
            });

            form.on('select(area)', function (data) {
                var selVal = data.value;
                if (selVal.length > 0) {
                    for (var i in region) {
                        if (region[i].name === selVal) {
                            var html = laytpl($("#regionSelectOption").html()).render(region[i].children);
                            $("#street").html('<option value="">请选择一个区县</option>' + html);
                            form.render('select');
                        }
                        var tblData = [];
                        layui.each(rawData, function (idx, item) {
                            if (item.xzq === selVal) {
                                tblData.push(item);
                            }
                        });
                        dataTbl.reload({data: tblData});
                    }
                } else {
                    dataTbl.reload({data: rawData});
                    $("#street").html('<option value="">请先选择一个区县</option>');
                    form.render('select');
                }

            });

            // 查询
            form.on('select(street)', function (data) {
                var selVal = data.value;
                var tblData = [];
                if (selVal.length > 0) {
                    layui.each(rawData, function (idx, item) {
                        if (item.region === selVal) {
                            tblData.push(item);
                        }
                    });
                } else {
                    tblData = rawData;
                    form.render('select');
                }
                dataTbl.reload({data: tblData});
            });

            $("#weekReport").bind("click", function () {
                $.ajax({
                    url: "<@com.rootPath/>/video/reports/weekReport",
                    success: function (data) {
                        debugger;
                        if (data != null && data.length > 0) {
                            layer.open({
                                type: 1,
                                area: ['340px', '215px'],
                                content: laytpl($("#reportTpl").html()).render(data)
                            });
                        }else{
                            layer.msg("无报告生成！",{icon:2,time:1000});
                        }

                    }
                });
            });
            $("#monthReport").bind("click", function () {
                $.ajax({
                    url: "<@com.rootPath/>/video/reports/monthReport",
                    success: function (data) {
                        if (data != null && data.length > 0) {
                            layer.open({
                                type: 1,
                                area: ['340px', '215px'],
                                content: laytpl($("#reportTpl").html()).render(data)
                            });
                        }else{
                            layer.msg("无报告生成！",{icon:2,time:1000});
                        }

                    }
                });
            });

        });
        function downLoad(id) {
            window.location.href = '<@com.rootPath/>/file/download/' + id;
        }
    </script>
</html>
