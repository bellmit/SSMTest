<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tabnt" data-toggle="tab">现状基本农田汇总</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tabnt" class="tab-pane fade in active">
        <#if (tplData.result?size>0)>
            <table>
                <#assign firstItem=tplData.result[0]/>
                <tr>
                    <#list firstItem?keys as key>
                        <th>
                            <#if key=="SHAPE">
                                相交图形
                            <#else >
                            ${key!}
                            </#if>
                        </th>
                    </#list>
                </tr>
                <#list tplData.result as detailItem>
                    <#if firstItem??>
                        <tr>
                            <#list firstItem?keys as key>
                                <td>
                                    <#if key=="SHAPE">
                                        <a class="btn btn-success btn-small" onclick=gotoLocation('${detailItem[key]!}','nt');>查看</a>
                                    <#else >
                                    ${detailItem[key]!}
                                    </#if>
                                </td>
                            </#list>
                        </tr>
                    </#if>
                </#list>
            </table>
        <#else >无分析结果</#if>
    </div>
</div>
<#else >无分析结果</#if>