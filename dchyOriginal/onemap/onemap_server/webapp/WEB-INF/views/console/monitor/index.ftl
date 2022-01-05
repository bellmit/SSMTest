<html>
<head>
    <title>系统监控</title>
    <meta name="tab" content="monitor"/>
</head>
<body>

<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <h5>主机列表</h5>
                    <div class="buttons">
                        <a href="#J_MODAL" data-result-ctn="#J_MODAL" data-toggle="modal" url="${base}/console/monitor/host/ajax/edit" class="btn btn-mini btn-primary j_ajax_for_data">添加新主机</a>
                    </div>
                </div><!-- /.widget-title -->
                <div class="widget-content nopadding">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th width="20%">名称</th>
                            <th width="30%">描述</th>
                            <th width="10%">监控项数量</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list hosts as host>
                        <tr>
                            <td><a href="#">${host.name!}</a></td>
                            <td>${host.description!}</td>
                            <td>${host.items?size}</td>
                            <td>
                                <a href="#J_MODAL" data-result-ctn="#J_MODAL" data-toggle="modal" url="${base}/console/monitor/host/ajax/edit?hostId=${host.id}" class="btn btn-mini btn-info j_ajax_for_data">编辑</a>
                                <#if host.enabled>
                                    <a href="host/toggle?hostId=${host.id}" class="btn btn-mini btn-danger">禁用</a>
                                <#else>
                                    <a href="host/toggle?hostId=${host.id}" class="btn btn-mini btn-success">启用</a>
                                </#if>
                                <a href="host/items?hostId=${host.id}" class="btn btn-info btn-mini">监控项</a>
                                <#if host.name!='mapService'>
                                    <a href="host/infs?hostId=${host.id}" class="btn btn-info btn-mini">数据接口</a>
                                    <a href="host/remove?hostId=${host.id}" class="btn btn-mini btn-inverse j_btn_del" data-ask="删除该 Host 会同时删除其所有关联数据，确定吗？">删除</a>
                                </#if>
                                <a href="host-charts?hostId=${host.id}" class="btn btn-primary btn-mini">监控图表</a>
                                <#if host.name=='mapService'>
                                    <a href="mapService/all?hostId=${host.id}" class="btn btn-primary btn-mini">地图访问统计</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div><!-- /.container-fluid -->
<div id="J_MODAL" class="modal fade in hide">

</div>
</body>
</html>