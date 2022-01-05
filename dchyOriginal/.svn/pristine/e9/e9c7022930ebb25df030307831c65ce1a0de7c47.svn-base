<html>
<head>
    <title>日志管理</title>
    <meta name="tab" content="logging"/>
    <meta name="js" content="${base}/static/js/page_map_edit.js"/>
</head>
<body>

<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="row-fluid">

        <div class="widget-box">

            <div class="widget-title">
                <ul class="nav nav-tabs">
                    <li>
                        <a href="${base}/console/log/arcgis/query">ArcGIS服务器</a>
                    </li>
                    <li>
                        <a href="${base}/console/log/webserver/query">应用服务器</a>
                    </li>
                    <li class="active">
                        <a href="#">用户行为</a>
                    </li>
                </ul>
            </div>
            <!-- widget-title -->

            <div class="widget-content">
                <div class="buttons-opt-wrap title-wrapper" style="margin-bottom: 10px;">
                    <form id="J_FORM_ARC_Q" action="${base}/console/log/audit/query" class="form-inline" method="get">
                        <label for="startTime">起始时间 <input id="startTime" type="text" name="startTime" readonly class="datepicker" style="width:130px" value="${startTime!}"/></label>
                        <label for="endTime">结束时间 <input id="endTime" type="text" name="endTime" readonly class="datepicker" style="width:130px" value="${endTime!}"/></label>
                        <button class="btn btn-primary">查询</button>
                        <a href="#" id="J_BTN_DEL" class="btn btn-mini btn-danger">清除日志</a>
                    </form>
                </div>
                <table class="table table-bordered table-condensed table-hover">
                <#if page.content?? && (page.content?size>0)>
                    <thead>
                    <tr>
                        <th>类型</th>
                        <th>时间</th>
                        <th>内容</th>
                        <th>操作用户ip地址</th>
                        <th>操作用户</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list page.content?sort_by("createAt")?reverse as log>
                        <tr>
                            <td>
                                <div class="fn-tc">${log.catalog!}</div>
                            </td>
                            <td>
                                <div class="fn-tc">${log.createAt?string("yyyy-MM-dd HH:mm")}</div>
                            </td>
                            <td>
                                <div class="fn-tc">${log.content!}</div>
                            </td>
                            <td>
                                <div class="fn-tc">${log.ip!}</div>
                            </td>
                            <td>
                                <div class="fn-tc"><#if log.user??>${log.user.viewName!}</#if></div>
                            </td>
                        </tr>
                        </#list>
                    <tr>
                        <td colspan="5">
                            <div id="J_PAGIN" class="pagination alternate clearfix">
                                <a href="#" class="prev">&lt;</a><span></span><a href="#" class="next">&gt;</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                <#else>
                    <br/>
                    <tr>
                        <td colspan="5">
                            <br/>

                            <p class="fn-tc">查无对应结果</p><br/>
                        </td>
                    </tr>
                </#if>
                </table>
            </div>
            <!-- /.widget-content -->

        </div>
        <!-- /.widget-box -->

    </div>
    <!-- /.row -->

</div>
<!-- /.container-fluid -->
<script>
    window.onload = function () {
        refreshPagin('#J_PAGIN', ${page.number}, ${page.size}, ${page.totalElements}, 10);

        $('#J_BTN_DEL').click(function () {
            var startTime = $.trim($('#startTime').val())
                    , endTime = $.trim($('#endTime').val())
                    , ask;
            if (startTime === "" && endTime === "") {
                ask = "您确定清空所有日志？";
            } else if (startTime === "") {
                ask = "您确定清空 " + endTime + " 以前的所有日志？";
            } else if (endTime === "") {
                ask = "您确定清空 " + startTime + " 以后的所有日志？";
            } else {
                ask = "您确定清空从" + startTime + " 到 " + endTime + " 的所有日志？";
            }
            $(this).attr('href', "${base}/console/log/audit/clean?startTime=" + startTime + "&endTime=" + endTime);
            return window.confirm(ask);
        })
    }
</script>
</body>
</html>