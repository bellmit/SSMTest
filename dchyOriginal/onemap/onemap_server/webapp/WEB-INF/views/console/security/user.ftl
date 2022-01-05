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
                        <li class="active">
                            <a href="${base}/console/security/index">用户管理</a>
                        </li>
                        <li>
                            <a href="${base}/console/security/role">角色管理</a>
                        </li>
                        <li>
                            <a href="${base}/console/security/privilege">资源管理</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-content ">

                    <div class="buttons-opt-wrap">
                        <div class="title-wrapper">
                            <h6>用户列表</h6>
                        </div>
                    <#if !ctx.externalUserMgr>
                        <div class="buttons">
                            <form action="" class="form-inline form-search" style="display: inline-block;">
                                <input type="text" name="username" class="validate[required]" value="${username!}" placeholder="输入用户名"/>
                                <!-- <label for="">
                                    角色
                                    <select name="" id="">
                                        <option value="1">admin</option>
                                    </select>
                                </label>&nbsp;&nbsp; -->
                                <button type="submit" id="J_BTN_QUERY_MAP" class="btn"><i class="icon-search"></i> 搜索</button>&nbsp;
                            </form>
                            <span style="color:#cdcdcd;">|</span>&nbsp;
                            <a href="#J_EDIT_USR" data-result-ctn="#J_EDIT_USR" data-toggle="modal" url="${base}/console/security/ajax/user/edit" class="btn btn-mini btn-primary j_ajax_for_data">创建新用户</a>
                        </div>
                    </#if>
                    </div>

                    <table class="table table-bordered table-striped">
                        <thead><tr>
                            <th style="width: 150px;">账号</th>
                            <th style="width: 200px;">名称</th>
                            <th>用户角色</th>
                        <#if !ctx.externalUserMgr><th>操作</th></#if>
                        </tr></thead>
                        <tbody>
                        <#list users?sort_by('id') as u>
                        <tr class="<#if u.enabled>j_enabled<#else>j_disabled</#if>">
                            <td>${u.name!}</td>
                            <td>${u.viewName!}</td>
                            <td class="label-wrap">
                                <#if u.roles??>
                                    <#list u.roles?sort_by("id") as role>
                                        <a class="label label-info" href="#">${role.name!}</a>&nbsp;
                                    </#list>
                                </#if>
                            </td>
                            <#if !ctx.externalUserMgr>
                                <td class="fn-tc">
                                    <#if u.enabled>
                                        <a href="${base}/console/security/ajax/user/toggle?userId=${u.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
                                    <#else>
                                        <a href="${base}/console/security/ajax/user/toggle?userId=${u.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
                                    </#if>
                                    <a href="#J_EDIT_USR" data-result-ctn="#J_EDIT_USR" data-toggle="modal" url="${base}/console/security/ajax/user/edit?userId=${u.id}"  class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
                                    <a href="#J_BIND_ROLE" data-result-ctn="#J_BIND_ROLE" data-toggle="modal" url="${base}/console/security/ajax/user/bindrole?userId=${u.id}" class="btn btn-mini btn-primary j_ajax_for_data">绑定角色</a>
                                    <!-- <a href="${base}/console/security/user/remove?id=${u.id}" class="btn btn-mini btn-inverse j_btn_del">删除</a> -->
                                </td>
                            </#if>
                        </tr>
                        </#list>
                        <!--<td>admin</td>
                            <td>administrator</td>
                            <td class="fn-tc">
			                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
			                    <a href="${base}/console/authMgr/usr/ajax/toggle/" class="btn btn-mini btn-success j_enable_disable">启用</a>
			                    <a href="#J_ADD_LAYER_MODAL" data-toggle="modal" url="${base}/console/authMgr/usr/ajax/" data-result-ctn="#J_ADD_LAYER_MODAL" class="btn btn-mini btn-primary j_ajax_for_data">编辑</a>
			                    <a href="#J_ADD_LAYER_MODAL" data-toggle="modal" url="${base}/console/authMgr/usr/ajax/" data-result-ctn="#J_ADD_LAYER_MODAL" class="btn btn-mini btn-primary j_ajax_for_data">授权</a>
			                    <a href="${base}/console/map/ajax/removeLayer?layerId=" class="btn btn-mini btn-inverse j_btn_del">删除</a>
			                 </td>-->
                        </tbody>
                    </table>
                </div><!-- END widget-content -->
            </div><!-- END widget-box -->
        </div><!-- END span -->
    </div><!-- END .row-fluid -->
</div><!-- END container-fluid -->
<div id="J_EDIT_USR" class="modal fade hide">

</div>
<div id="J_BIND_ROLE" class="modal fade hide">

</div>
</body>
</html>
