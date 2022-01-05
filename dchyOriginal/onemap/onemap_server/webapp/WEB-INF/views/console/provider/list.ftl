<html>
<head>
    <title>服务管理 - 服务编辑</title>
    <meta name="tab" content="map"/>
    <meta name="js" content="${base}/static/js/page_map_edit.js"/>
</head>
<body>
<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}"><i class="icon-tasks"></i>服务列表</a>
        <a href="${base}/console/map/edit?id=${mapId}">编辑服务</a>
        <a class="current">服务提供者</a>
    </div>
</div>
<!-- /#breadcrumbWrapper -->

<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="row-fluid">

        <div class="widget-box span12">

            <div class="widget-title">
                <ul class="nav nav-tabs">
                    <li><a href="${base}/console/map/edit?id=${mapId}">基本信息</a></li>
                    <li class="active"><a href="#">服务提供者</a></li>
                    <li><a href="${base}/console/layer/showLayers/${mapId}">图层信息</a></li>
                    <li><a href="${base}/console/map/monitor?id=${mapId}">监控</a></li>
                </ul>
            </div>
            <!-- widget-title -->

            <div class="widget-content">

                <div class="tab-content">

                    <div class="tab-pane active">

                        <div class="row-fluid">

                            <div class="span12">
                                <div class="title-wrapper">
                                    <h6>服务提供者列表</h6>

                                    <div class="title-btn-wrapper">
                                        <a href="#J_NEW_PROVIDER_MODAL_1" data-toggle="modal" class="btn btn-mini btn-primary">添加</a>
                                    </div>
                                </div>

                                <div class="table-wrapper">
                                    <table class="table table-bordered table-striped">
                                        <thead>
                                        <tr>
                                            <th style="width: 150px;">名称</th>
                                            <th style="width: 150px;">类型</th>
                                            <th>说明</th>
                                            <th style="width: 300px;">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="J_PROVIDER_TBODY">
                                        <#include "ajax/provider-list.ftl">
                                        </tbody>
                                    </table>
                                </div>
                                <!-- /.table-wrapper -->

                            </div>
                            <!-- /.span6 -->

                        </div>
                        <!-- /.row-fluid -->

                    </div>
                    <!-- /.tab-pane -->

                </div>
                <!-- /.tab-content -->

            </div>
            <!-- /.widget-content -->

        </div>
        <!-- /.widget-box -->

    </div>
    <!-- /.row -->

</div>
<!-- /.container-fluid -->

<div id="J_NEW_PROVIDER_MODAL_1" class="modal fade hide">
<#include "ajax/provider-add-step1.ftl">
</div>
<div id="J_NEW_PROVIDER_MODAL_2" class="modal fade hide">

</div>
<div id="J_PROVIDER_MSG" class="modal fade hide">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>提供者错误</h3>
    </div>
    <div class="modal-body">
    </div>
</div>
</body>
</html>