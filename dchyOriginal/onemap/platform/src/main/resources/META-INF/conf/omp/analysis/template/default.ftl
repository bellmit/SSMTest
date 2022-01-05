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
                        <a class="btn btn-success btn-small" onclick=gotoLocation('${item[key]!}','default');>查看</a>
                    <#else >
                    ${item[key]!}
                    </#if>
                </td>
            </#list>
        </tr>
    </#list>
</table>
<#else >
<div class="tab-content" style="border-top: 1px solid #ddd;">
    无分析结果
</div>
</#if>