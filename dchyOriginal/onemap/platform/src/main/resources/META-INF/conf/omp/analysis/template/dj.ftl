<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <#--<li class="active"><a href="#tabdj" data-toggle="tab">登记汇总</a></li>-->
        <li class="active"><a href="#tabdj2" data-toggle="tab">登记详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <#--<div id="tabdj" class="tab-pane fade in">-->
        <#--<#if (tplData.result?size>0)>-->
            <#--<table>-->
                <#--<tr>-->
                    <#--<th>已发证面积</th>-->
                    <#--<th>已发证数量</th>-->
                <#--</tr>-->
                <#--<tr>-->
                    <#--<td>${env.sumSequence(tplData.result,"area")!}</td>-->
                    <#--<td>${env.sumSequence(tplData.result,"num")!}</td>-->
                <#--</tr>-->
            <#--</table>-->
        <#--<#else >无分析结果</#if>-->
    <#--</div>-->
    <div id="tabdj2" class="tab-pane fade in active">
        <#if (tplData.result?size>0)>
            <table>
                <#assign hyperlinks=tplData.hyperlinks!/>
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