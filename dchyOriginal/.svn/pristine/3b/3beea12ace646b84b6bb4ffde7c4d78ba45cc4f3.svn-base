<html>
<head>
    <title>组织机构与安全</title>
    <meta name="tab" content="auth"/>

</head>
<body>
<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <ul class="nav nav-tabs">
                        <li>
                            <a href="${base}/console/security/index">用户管理</a>
                        </li>
                        <li class="active">
                            <a href="${base}/console/security/role">角色管理</a>
                        </li>
                        <li>
                            <a href="${base}/console/security/privilege">资源管理</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-content ">

                    <!-- <div class="form-inline-wrapper" style="background-color: #F9F9F9;">
                        <form action="" class="form-inline form-search">
                            <input type="text" value="" placeholder="输入角色名"/>&nbsp;&nbsp;
                            <label for="">
                                角色
                                <select name="" id="">
                                    <option value="1">admin</option>
                                </select>
                            </label>&nbsp;&nbsp;
                            <button type="submit" id="J_BTN_QUERY_MAP" class="btn"><i class="icon-search"></i> 搜索</button>
                        </form>
                    </div> form-inline-wrapper -->

                    <div class="buttons-opt-wrap">
                        <div class="title-wrapper">
                            <h6>角色列表</h6>
                        </div>
                    <#if !ctx.externalUserMgr>
                        <div class="buttons">
                            <a href="#J_EDIT_ROLE" gtdata-result="#J_EDIT_ROLE" data-toggle="modal" url="${base}/console/security/ajax/role/edit" class="btn btn-mini btn-primary j_ajax_for_data">创建新角色</a>
                        </div>
                    </#if>
                    </div>

                    <table class="table table-bordered table-striped">
                        <thead><tr>
                            <th>名称</th>
                            <th>描述</th>
                            <th>操作</th>
                        </tr></thead>
                        <tbody>
                        <#list roles?sort_by('id') as r>
                        <tr class="<#if r.enabled>j_enabled<#else>j_disabled</#if>">
                            <td><a href="#">${r.name!}</a></td>
                            <td>${r.description!}</td>
                            <#if !ctx.externalUserMgr>
                                <td class="fn-tc">
                                    <#if !r.fixed>
                                        <#if r.enabled>
                                            <a href="${base}/console/security/ajax/role/toggle?roleId=${r.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
                                        <#else>
                                            <a href="${base}/console/security/ajax/role/toggle?roleId=${r.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
                                        </#if>
                                        <a href="#J_EDIT_ROLE" gtdata-result="#J_EDIT_ROLE" data-toggle="modal" url="${base}/console/security/ajax/role/edit?roleId=${r.id}" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>

                                    </#if>
                                    <a href="${base}/console/auth/grant?roleId=${r.id}" class="btn btn-mini btn-info">授权</a>
                                    <!-- <a href="${base}/console/security/role/remove?id=${r.id}" class="btn btn-mini btn-inverse j_btn_del">删除</a> -->
                                </td>
                            <#else>
                                <td class="fn-tc">
                                    <a href="${base}/console/auth/grant?roleId=${r.id}" class="btn btn-mini btn-info">授权</a>
                                </td>
                            </#if>
                        </tr>
                        </#list>
                        <!-- <tr>
	                        	<td><a href="#">administrator</a></td>
	                            <td class="fn-tc">
				                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
				                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-success j_enable_disable">启用</a>
				                    <a href="#J_EDIT_ROLE" gtdata-result="#J_EDIT_ROLE" data-toggle="modal" url="${base}/console/security/ajax/role/edit" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
				                    <a href="${base}/console/map/ajax/removeLayer?layerId=" class="btn btn-mini btn-inverse j_btn_del">删除</a>
				                 </td>
			                 </tr> -->
                        </tbody>
                    </table>
                </div><!-- END widget-content -->
            </div><!-- END widget-box -->
        </div><!-- END span -->
    </div><!-- END .row-fluid -->
</div><!-- END container-fluid -->
<div id="J_EDIT_ROLE" class="modal fade hide">

</div>
</body>
</html>
