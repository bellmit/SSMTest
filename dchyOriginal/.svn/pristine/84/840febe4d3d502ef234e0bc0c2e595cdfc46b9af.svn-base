<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tabcb" data-toggle="tab">储备汇总</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tabcb" class="tab-pane fade in active">
        <#if (tplData.result?size>0)>
            <table>
                <#assign hyperlinks=tplData.hyperlinks!/>
                <#assign firstItem=tplData.result[0]/>
                <tr>
                    <#list firstItem?keys as key>
                        <th>${key!}</th>
                    </#list>
                </tr>
                <#list tplData.result as detailItem>
                    <#if firstItem??>
                        <tr>
                            <#list firstItem?keys as key>
                                <#assign temp=true/>
                                <td>
                                    <#if hyperlinks??>
                                        <#list hyperlinks as hyperlink>
                                            <#if hyperlink[key]??>
                                                <#assign link=hyperlink[key][detailItem_index]/>
                                                <#if link[detailItem[key]]??>
                                                    <#assign temp=false/>
                                                    <a href="${link[detailItem[key]]}"
                                                       target="_blank">${detailItem[key]!}</a>
                                                </#if>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <#if temp>
                                    ${detailItem[key]!}
                                    </#if>
                                </td>
                            </#list>
                        </tr>
                    </#if>
                </#list>
            </table>
        </#if>
    </div>
</div>
<#else>
无分析结果
</#if>