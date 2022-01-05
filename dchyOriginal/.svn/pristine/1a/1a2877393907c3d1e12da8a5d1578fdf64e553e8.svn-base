<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#ZdTab1" data-toggle="tab">宗地汇总</a></li>
        <li><a href="#ZdTab2" data-toggle="tab">宗地详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="ZdTab1" class="tab-pane fade in active">
         <#if (tplData.result?size>0)>
         <#assign statItem = tplData.result/>
            <table>
                <tr>
                    <th>已发证面积</th>
                    <th>未发证面积</th>
                    <th>涉及宗地数</th>
                </tr>
                <tr>
                    <td>${(statItem.fzArea!0)?string("###.####")}</td>
                    <td>${(statItem.wfzArea!0)?string("###.####")}</td>
                    <td>${(statItem.zdNum!0)?string("###")}</td>
                </tr>
            </table>
        <#else >无结果</#if>
    </div>
    <div id="ZdTab2" class="tab-pane fade in">
        <#if (tplData.result?size>0)&&(tplData.result.detail?size>0)>
            <table>
                <#assign firstItem=tplData.result.detail[0]/>
                <tr>
                    <#list firstItem?keys as key>
                        <#if key!="FZ">
                            <th>
                                <#if key=="SHAPE">
                                    相交图形
                                <#elseif key=="ZDH_1">
                                    宗地号
                                    <#elseif key=="QLR">
                                        权利人
                                        <#elseif key=="YDJH_1">
                                        用地件号
                                        <#else >
                                            ${key!}
                                </#if>
                            </th>
                        </#if>
                    </#list>
                </tr>
                <#list tplData.result.detail as detailItem>
                    <#if firstItem??>
                        <tr>
                            <#list firstItem?keys as key>
                                <#if key!="FZ">
                                <td>
                                    <#if key=="SHAPE">
                                        <a class="btn btn-success btn-small" onclick=gotoLocation('${detailItem[key]!}','zd');>查看</a>
                                    <#else >
                                    ${detailItem[key]!}
                                    </#if>
                                </td>
                                </#if>
                            </#list>
                        </tr>
                    </#if>
                </#list>
            </table>
        <#else >无</#if>
    </div>
</div>
<#else >无分析结果</#if>
