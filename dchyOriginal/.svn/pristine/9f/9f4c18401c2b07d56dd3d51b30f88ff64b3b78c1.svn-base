<#if (tplData.result?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab_gyjsyd_1" data-toggle="tab">分析详情</a></li>
        <li><a href="#tab_gyjsyd_2" data-toggle="tab">地块定位</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tab_gyjsyd_1" class="tab-pane fade in active">
        <#assign gyjsyd=tplData!/>
        <#assign result=gyjsyd.result/>
        <table style="margin-top: 30px;">
            <tr>
                <th>序号</th>
                <th>类别</th>
                <th>面积</th>
                <th>详细</th>
            </tr>
            <#list result[0..] as item>
                <tr>
                    <td>${item_index+1}</td>
                    <td>${item.type}</td>
                    <td>${item.area}</td>
                    <td style="text-align: center;" rel="#detailPanel${item_index}"><a
                            onclick="detailInfo(${item_index});" style="cursor:pointer;"><span>详细信息</span></a></td>
                </tr>
            </#list>
        </table>
        <#list result[0..] as parent>
            <#assign info = parent.info/>
            <div class="detailPanel" id="detailPanel${parent_index}">
                <div style="width: 100%;margin-top: 30px; max-height: 600px;overflow-y: auto;padding-bottom: 20px;">
                    <#if (info?size>0)>
                        <table>
                            <tr>
                                <th>序号</th>
                                <#list info[0]?keys as key>
                                    <th><#if key=="SHAPE">
                                        相交图形
                                    <#else >
                                    ${key!}
                                    </#if></th>
                                </#list>
                            </tr>
                            <#list info[0..] as detail>
                                <tr>
                                    <td>${detail_index+1}</td>
                                    <#list detail?keys as key>
                                        <td>
                                            <#if key=="SHAPE">
                                                <a class="btn btn-success btn-small"
                                                   onclick="gotoLocation('${detail[key]!}','gyjsydfx')">查看</a>
                                            <#else >
                                            ${detail[key]}
                                            </#if>
                                        </td>
                                    </#list>
                                </tr>
                            </#list>
                        </table>
                    <#else ><h5>无</h5>
                    </#if>
                </div>
            </div>
        </#list>
    </div>
    <div id="tab_gyjsyd_2" class="tab-pane fade">
        <button class="btn btn-default btn-loc" data-geo='${tplData.intersectGeo!}'>压盖国有建设用地地块</button>
        <button class="btn btn-default btn-loc" data-geo='${tplData.diffGeo!}'>未压盖地块</button>
    </div>
</div>
<#else >无分析结果！</#if>