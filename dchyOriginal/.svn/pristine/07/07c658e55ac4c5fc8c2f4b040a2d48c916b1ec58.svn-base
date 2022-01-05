<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab22" data-toggle="tab">视频监控点汇总</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tab22" class="tab-pane fade in active">
        <#if (tplData.result?size>0)>
            <table>
                <#list tplData.result as item>
                    <#if item_index==0>
                        <tr>
                            <#list item?keys as key>
                                <th>
                                    <#if key=="SHAPE">
                                        相交图形
                                    <#else >
                                    ${key!}
                                    </#if>
                                </th>
                            </#list>
                        </tr>
                    </#if>
                    <tr>
                        <#list item?keys as key>
                            <td>
                                <#if key=="SHAPE">
                                    <a class="btn btn-success btn-small"
                                       onclick=gotoLocation('${item[key]!}','default');>查看</a>
                                <#else >
                                ${item[key]!}
                                </#if>
                            </td>
                        </#list>
                    </tr>
                </#list>
            </table>
        <#else >无
        </#if>
    </div>
</div>
<#else >无分析结果</#if>