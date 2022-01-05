<html>
<head>
    <title>服务管理 - 字段编辑</title>
    <meta name="tab" content="map"/>
    <meta name="submenu" content="map_list"/>
    <meta name="js" content="${base}/static/js/page_map_edit.js" />
</head>
<body>

<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}"><i class="icon-tasks"></i>服务列表</a>
        <a href="${base}/console/map/edit?id=${mapId}">编辑服务</a>
        <a href="${base}/console/layer/showLayers/${mapId}">图层列表</a>
        <a class="current">编辑字段</a>
    </div>
</div><!-- /#breadcrumbWrapper -->

<div class="container-fluid">
<#include "../../common/ret.ftl" />
<div class="row-fluid">

    <div class="widget-box">

        <div class="widget-content">
            <div class="row-fluid">
                <div class="span12">
                    <div class="title-wrapper">
                        <h4 class="title">图层${layer.name} <small>字段列表</small></h4>
                        <div class="title-btn-wrapper">
                            <a url="${base}/console/field/ajax/edit?layerId=${layer.id}&mapId=${mapId}" data-result-ctn="#J_FIELD_MODAL" href="#J_FIELD_MODAL" data-toggle="modal" class="btn btn-primary btn-mini j_ajax_for_data">添加字段</a>
                        </div>
                    </div>
                    <table class="table table-bordered table-striped" style="margin-top:10px;">
                        <thead><tr>
                            <th>名称</th>
                            <th>别名</th>
                            <th>类型</th>
                            <th>操作</th>
                        </tr></thead>
                        <tbody>
                            <#list fields as f>
                            <tr class="<#if f.enabled>j_enabled<#else>j_disabled</#if>">
                                <td>${f.name!}</td>
                                <td>${f.alias!}</td>
                                <td>${f.type.label}</td>
                                <td class="fn-tc">
                                    	<#if f.enabled>
			                            <a href="${base}/console/field/ajax/toggle?id=${f.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
			                            <#else>
			                            <a href="${base}/console/field/ajax/toggle?id=${f.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
			                            </#if>
                                    <a href="#J_FIELD_MODAL" url="${base}/console/field/ajax/edit?id=${f.id}&layerId=${layer.id}&mapId=${mapId}" data-result-ctn="#J_FIELD_MODAL" data-toggle="modal" class="btn btn-primary btn-mini j_ajax_for_data">编辑</a>
                                    <a href="${base}/console/field/remove?id=${f.id}&layerId=${layer.id}&mapId=${mapId}" class="btn btn-inverse btn-mini j_btn_del">删除</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div><!-- /span -->

            </div><!-- /row-fluid -->

        </div><!-- /widget-content -->

    </div><!-- /widget-box -->

</div><!-- /.row -->

</div><!-- /.container-fluid -->
<div id="J_FIELD_MODAL" class="modal fade hide">
	
</div><!-- /#J_ADD_FIELD_MODAL -->
</body>
</html>