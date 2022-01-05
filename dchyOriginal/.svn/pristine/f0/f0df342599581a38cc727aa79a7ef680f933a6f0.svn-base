<html>
<head>
    <title>任务管理</title>
    <meta name="tab" content="task"/>
</head>
<body>

<div class="container-fluid">
    <div class="row-fluid">

        <div class="span12">

            <div class="widget-box">
                <div class="widget-title">
                    <h5>任务列表</h5>
                    <div class="buttons">
                        <a href="${base}/console/task/index" class="btn btn-primary btn-mini">刷新</a>
                    </div>
                </div>
                <div class="widget-content nopadding">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>说明</th>
                            <th>开始时间</th>
                            <th style="width:200px;">进度</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list tasks as task>
                        <tr>
                            <td>${task.title!}</td>
                            <td><#if task.startAt??>${task.startAt?datetime}</#if></td>
                            <td><div class="percent"><span>${task.completeCount}/${task.totalCount}</span><div style="width: ${task.countPercent}%"></div></div></td>
                            <td class="fn-tc">
                                <a href="${base}/console/task/stop?id=${task.id}" class="btn btn-mini btn-inverse j_btn_del">停止</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                        <tfoot>

                        </tfoot>
                    </table>
                </div>
                <!-- END widget-content -->

            </div>
            <!-- /widget-box -->

        </div>
        <!-- /.span12 -->

    </div>
    <!-- /.row-fluid -->

</div>
<!-- /container-fluid -->
<script type="text/javascript">
    setTimeout(function(){
        location.href='${base}/console/task/index';
    },5000);
</script>
</body>
</html>