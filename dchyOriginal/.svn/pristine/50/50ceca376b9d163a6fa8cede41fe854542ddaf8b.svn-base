<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
<@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
    <title>定时任务管理</title>
    <style>
        .layui-container {
            margin: 2em auto;
        }
    </style>
</head>
<body>
<div class="layui-container">
    <blockquote class="layui-elem-quote">
        定时任务管理.
    </blockquote>
    <table id="jobTable" lay-filter="job"></table>
</div>
</body>
<@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
<#--toolbar-->
<script type="text/html" id="toolbar">
    {{# if(d.enable) { }}
    <a class="layui-btn layui-btn-xs layui-btn-normal  layui-btn-disabled" lay-event="setEnable">已启用</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="setDisable">禁用</a>
    {{# } else { }}
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="setEnable">启用</a>
    <a class="layui-btn layui-btn-danger  layui-btn-disabled layui-btn-xs" lay-event="setDisable">已禁用</a>
    {{# } }}
    <a class="layui-btn layui-btn-xs" lay-event="run">运行一次</a>
</script>
<script type="text/html" id="statusTpl">
    {{#  if(d.running) { }}
    <span class="layui-badge-dot layui-bg-green"></span> 执行中
    {{# } else { }}
    <span class="layui-badge-dot layui-bg-cyan"></span> 等待中
    {{#  } }}
</script>
<script>
    layui.use(['table', 'jquery'], function () {
        var table = layui.table;
        var $ = layui.jquery;

        table.render({
            elem: '#jobTable'
            , height: 'full-150'
            , url: '<@com.rootPath/>/sdm/jobs' //数据接口
            , page: false //开启分页
            , cols: [[ //表头
                {field: 'id', title: '序号', width: 80, fixed: 'left', type: 'numbers'}
                , {field: 'name', title: '名称', width: 100}
                , {field: 'class', title: '类', width: 250}
                , {field: 'method', title: '方法名', width: 120}
                , {field: 'time', title: '任务时间', width: 120}
                , {
                    field: 'running', title: '状态', width: 120, templet: '#statusTpl'
                }
                , {fixed: 'right', title: '操作', width: 200, align: 'left', toolbar: '#toolbar'}
            ]]
        });

        //监听工具条
        table.on('tool(job)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            if (layEvent === 'run') { //立即运行
                if (data.running) {
                    layer.msg('任务正在进行中,请刷新页面等待完成后再尝试.', {icon: 0});
                    return;
                }
                $.getJSON('/omp/sdm/run', {
                    job: data.name,
                    jobClass: data.class,
                    jobMethod: data.method
                }, function (ret) {
                    if (ret.hasOwnProperty('success')) {
                        console.error('error: %o', ret);
                        layer.msg(ret.msg, {icon: 0});
                        return;
                    }
                    //同步更新缓存对应的值
                    obj.update({
                        running: true
                    });
                });

            } else if (layEvent === 'setDisable') { //编辑
                $.getJSON('/omp/sdm/switch/' + data.name + '/0', {}, function (ret) {
                    //同步更新缓存对应的值
                    obj.update({
                        enable: false
                    });
                });

            } else if (layEvent === 'setEnable') {
                $.getJSON('/omp/sdm/switch/' + data.name + '/1', {}, function (ret) {
                    //同步更新缓存对应的值
                    obj.update({
                        enable: true
                    });
                });
            }
        });

    });
</script>
</html>