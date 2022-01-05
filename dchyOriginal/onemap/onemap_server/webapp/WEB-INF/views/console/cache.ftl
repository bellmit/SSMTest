<html>
<head>
    <title>缓存管理</title>
    <meta name="tab" content="cache"/>
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <h5>缓存管理</h5>
                    <div class="buttons">
                        <a href="cache/clean?name=all" class="btn btn-primary btn-mini j_btn_del"><i class="icon-remove"></i>&nbsp;清除所有</a>
                    </div>
                </div>
                <div class="widget-content nopadding">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>数量</th>
                            <th>命中次数</th>
                            <th>未命中次数</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list caches as cache>
                        <tr>
                            <td>${cache.associatedCacheName}</td>
                            <td>${cache.objectCount}</td>
                            <td>${cache.cacheHits}</td>
                            <td>${cache.cacheMisses}</td>
                            <td class="fn-tc">
                                <a href="${base}/console/cache/clean?name=${cache.associatedCacheName}" class="btn btn-mini btn-inverse j_btn_del">清除</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                        <tfoot>

                        </tfoot>
                    </table>
                </div><!-- END widget-content -->

            </div>
            <!-- /widget-box -->

        </div>
        <!-- /.span12 -->

    </div>
    <!-- /.row-fluid -->

</div>
</body>
</html>
