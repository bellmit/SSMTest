<html>
<head>
    <title>服务管理 - 图层编辑</title>
    <meta name="tab" content="map"/>
    <meta name="js" content="${base}/static/js/page_map_edit.js"/>
</head>
<body>
<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}"><i class="icon-tasks"></i>服务列表</a>
        <a href="${base}/console/map/edit?id=${mapId}">编辑服务</a>
        <a class="current">图层列表</a>
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
                    <li><a href="${base}/console/provider/showProviders/${mapId}">服务提供者</a></li>
                    <li class="active"><a href="#">图层信息</a></li>
                    <li><a href="${base}/console/map/monitor?id=${mapId}">监控</a></li>
                </ul>
            </div>
            <!-- widget-title -->

            <div class="widget-content">

                <div class="tab-content">

                    <div class="tab-pane active">

                        <div class="row-fluid">

                            <div class="span12">
                                <div class="title-wrapper" style="margin-bottom: 15px;">
                                    <h6>图层列表</h6>

                                    <div class="title-btn-wrapper">
                                        <span style="font-size:13px;">图层类型:</span>
                                        <select id="J_LFT">
                                        <#list layerFetchTypes as lft>
                                            <option value="${lft}" <#if lft==layerFt>selected</#if>>${lft}</option>
                                        </#list>
                                        </select>
                                        &nbsp;&nbsp;
                                        <a href="#J_ADD_LAYER_MODAL" data-toggle="modal" url="${base}/console/layer/ajax/edit?mapId=${mapId}" data-result-ctn="#J_ADD_LAYER_MODAL" class="btn btn-mini btn-primary j_ajax_for_data">添加图层</a>
                                    </div>
                                </div>
                                <!-- title-wrapper -->

                                <div class="table-wrapper">
                                    <table class="table table-bordered table-striped">
                                        <thead>
                                        <tr>
                                            <th style="width:30px;">顺序</th>
                                            <th>名称</th>
                                            <th>别名</th>
                                            <th>类型</th>
                                            <th style="width: 200px;">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if layers??>
                                            <#list layers?sort_by("index") as l >
                                            <tr class="<#if l.enabled>j_enabled<#else>j_disabled</#if>">
                                                <td>${l.index!}</td>
                                                <td><a href="${base}/console/map/editLayer?id=${l.id}">${l.name!}</a></td>
                                                <td>${l.alias!}</td>
                                                <td>${l.catalog!}</td>
                                                <td class="fn-tc">
                                                    <#if l.enabled>
                                                        <a href="${base}/console/layer/ajax/toggle?id=${l.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
                                                    <#else>
                                                        <a href="${base}/console/layer/ajax/toggle?id=${l.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
                                                    </#if>
                                                    <a href="${base}/console/field/all?id=${l.id}&layerId=${l.id}&mapId=${mapId}" class="btn btn-mini btn-primary">字段</a>
                                                    <a href="#J_ADD_LAYER_MODAL" data-toggle="modal" url="${base}/console/layer/ajax/edit?id=${l.id}&mapId=${mapId}" data-result-ctn="#J_ADD_LAYER_MODAL" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
                                                    <a href="${base}/console/layer/remove?id=${l.id}&mapId=${mapId}" class="btn btn-mini btn-inverse j_btn_del">删除</a>
                                                </td>
                                            </tr>
                                            </#list>
                                        </#if>
                                        </tbody>
                                    </table>
                                </div>
                                <!-- /.table-wrapper -->

                            </div>
                            <!-- /.span -->

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

<div id="J_ADD_LAYER_MODAL" class="modal fade hide">

</div>
<!-- /#J_ADD_LAYER_MODAL -->
</body>
</html>