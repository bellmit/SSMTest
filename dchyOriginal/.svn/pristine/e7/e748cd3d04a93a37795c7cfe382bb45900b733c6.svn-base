<html>
<head>
    <title>地图管理</title>
    <meta name="tab" content="map"/>
    <meta name="submenu" content="map_list"/>
    <meta name="js" content="${base}/static/js/page_map_index.js"/>
</head>
<body>
<script>//${page.number + 1}
var _page = ${page.number}
            , _count = ${page.totalElements}
            , _size = ${page.size}
</script>
<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="row-fluid">

        <div class="span12">

            <div class="widget-box">

                <div class="widget-title">

                    <h5>服务列表</h5>

                    <div class="buttons">
                        <a href="edit" class="btn btn-mini btn-primary" gtdata-toggle="breadpane"><i class="icon-white icon-plus"></i>&nbsp;新增服务</a>
                        <a href="#importModal" data-toggle="modal" class="btn btn-primary btn-mini"><i class="icon-white icon-chevron-down"></i>&nbsp;导入单个服务</a>
                        <a href="#importsModal" data-toggle="modal" class="btn btn-primary btn-mini"><i class="icon-white icon-align-justify"></i>&nbsp;批量导入服务</a>
                    </div>
                    <!-- /.buttons -->

                </div>
                <!-- /.widget-title -->

                <div class="widget-content nopadding">

                    <div class="form-inline-wrapper">
                        <form action="${base}/console/map/index" method="get" class="form-inline form-search">
                            <label for="srch_all" class="radio"><input id="srch_all" type="radio" checked="checked" name="status" value=""/>全部</label>
                            <label for="srch_runing" class="radio"><input id="srch_runing" type="radio" <#if mapQuery.status?? && mapQuery.status="RUNNING">checked="checked"</#if> name="status" value="RUNNING"/>运行中</label>
                            <label for="srch_stop" class="radio"><input id="srch_stop" type="radio" <#if mapQuery.status?? && mapQuery.status="STOP">checked="checked"</#if> name="status" value="STOP"/>停止</label>
                            <!--<label for="srch_susp" class="radio"><input id="srch_susp" type="radio" <#if status?? && status="PAUSE">checked="checked"</#if> name="Q_status" value="PAUSE"/>暂停</label>-->
                            &nbsp;
                            <label for="select-group">服务组 <select id="select-group" name="groupName">
                            <#if group??>
                                <option value="">所有</option>
                                <#list group as g>
                                    <option value="${g.name}" <#if mapQuery.groupName?? && mapQuery.groupName == g.name>selected</#if> >${g.name}</option>
                                </#list>
                            </#if>
                            </select></label>
                            <label>
                                <input name="keyword" type="text" placeholder="输入关键字" value="${mapQuery.keyword!}">
                                <button type="submit" id="J_BTN_QUERY_MAP" class="btn"><i class="icon-search"></i> 搜索</button>
                                <a href="${base}" class="btn btn-link btn-mini" style="padding-top:8px;">[清除条件]</a>
                            </label>
                        </form>
                    </div>
                    <!-- /.form-inline-wrapper -->

                    <div class="service-list-wrapper">
                        <ul id="J_MAP_LIST" class="service-list">
                        <#list page.content?sort_by("weight") as map>
                            <li id="J_MAP_ITEM_${map.id}" class="clearfix" style="height:55px;">
                                <#include "map-list-item.ftl"/>
                            </li>
                        </#list>
                        <#if page.content?size==0>
                            <p class="fn-tc">
                                <br/>
                                没有查询到结果
                                <br/>
                                <br/>
                            </p>
                        <#else>
                            <li class="list-bottom clearfix">
                                <div id="J_PAGIN" class="pagination alternate clearfix">
                                    <a class="prev" href="#">&lt;</a><span></span><a class="next" href="#">&gt;</a>
                                </div>
                            </li>
                        </#if>
                        </ul>
                    </div>
                    <!-- /.service-list-wrapper -->

                </div>
                <!-- /widget-content -->

            </div>
            <!-- /widget-box -->

        </div>
        <!-- /.span12 -->

    </div>
    <!-- /.row-fluid -->

</div>
<!-- /container-fluid -->

<#include "import-modal.ftl"/>
<#include "imports-modal.ftl"/>

<@sec.perm>123</@sec.perm>

</body>
</html>
