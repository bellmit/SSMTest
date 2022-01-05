<#if tplData?keys?size &gt;0>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab11" data-toggle="tab">汇总</a></li>
        <li><a href="#tab22" data-toggle="tab">详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tab11" class="tab-pane fade in active">
        <#if tplData??>
            <#assign infoItems=tplData.info/>
            <table>
                <tr>
                    <th>套合地块数</th>
                    <th>套合面积</th>
                    <th>未套合面积</th>
                </tr>
                <#list infoItems as infoItem>
                    <tr>
                        <td>${infoItem["count"]!}</td>
                        <td>${infoItem["coverArea"]!?string("0.####")}</td>
                        <td>${infoItem["uncoverArea"]!?string("0.####")}</td>
                    </tr>
                </#list>
            </table>
        <#else >无分析结果</#if>
    </div>
    <div id="tab22" class="tab-pane fade">
        <#if tplData??>
            <#assign detailItems=tplData.detail/>
            <#if detailItems?size &gt;0>
                <#assign firstItem=detailItems[0]/>
                <table>
                    <tr>
                        <#list firstItem?keys as key>
                            <th>${key!}</th>
                        </#list>
                    </tr>
                    <#list detailItems as detailItem>
                        <#if firstItem??>
                            <tr>
                                <#list firstItem?keys as key>
                                    <td>${detailItem[key]!}</td>
                                </#list>
                            </tr>
                        </#if>
                    </#list>
                </table>
            <#else >无
            </#if>
        <#else >无
        </#if>
    </div>
</div>
<#else >无分析结果</#if>