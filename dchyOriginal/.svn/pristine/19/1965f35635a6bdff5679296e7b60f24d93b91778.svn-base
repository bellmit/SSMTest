<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#ysTab1" data-toggle="tab">预审汇总</a></li>
        <li><a href="#ysTab2" data-toggle="tab">预审详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="ysTab1" class="tab-pane fade in active">
        <#if tplData.level??&&tplData.level=="mas">
            <#if (tplData.result?size>0)&&tplData.stat??>
                <#assign statItem = tplData.stat/>
                <table>
                    <tr>
                        <th>预审面积</th>
                        <th>未预审面积</th>
                        <th>涉及地块数</th>
                    </tr>
                    <tr>
                        <td>${statItem.coverArea!}</td>
                        <td>${statItem.unCoverArea!}</td>
                        <td>${statItem.count!}</td>
                    </tr>
                </table>
            <#else >无结果</#if>
        </#if>
    </div>
    <div id="ysTab2" class="tab-pane fade in">
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
                                        <a class="btn btn-success btn-small"
                                           onclick=gotoLocation('${detailItem[key]!}','ysdk');>查看</a>
                                    <#else >
                                    ${detailItem[key]!}
                                    </#if>
                                </td>
                            </#list>
                        </tr>
                    </#if>
                </#list>
            </table>
        <#else >无</#if>
    </div>
</div>
<#else >无分析结果</#if>