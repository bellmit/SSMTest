<#if tplData.result?size &gt;0 >
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
                    <td>${item[key]!}</td>
                </#list>
            </tr>
        </#list>
    </table>
<#else >无分析结果
</#if>