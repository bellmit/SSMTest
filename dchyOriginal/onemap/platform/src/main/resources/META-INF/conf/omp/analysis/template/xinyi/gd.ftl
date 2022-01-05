<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tabgd1" data-toggle="tab">供地汇总</a></li>
        <li><a href="#tabgd2" data-toggle="tab">供地详情</a></li>
        <li><a href="#tabgd3" data-toggle="tab">地块定位</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tabgd1" class="tab-pane fade in active">
        <#if (tplData.result?size>0)>
            <#assign infoItems=tplData.result.info/>
            <table>
                <tr>
                    <th>类型</th>
                    <th>面积_平方米</th>
                    <th>面积_公顷</th>
                    <th>面积_亩</th>
                </tr>
                <#list infoItems as infoItem>
                    <tr>
                        <td>${infoItem["type"]!}</td>
                        <td>${infoItem["area"]!?string("0.####")}</td>
                        <td>${infoItem["area_gq"]!?string("0.####")}</td>
                        <td>${infoItem["area_m"]!?string("0.####")}</td>
                    </tr>
                </#list>
            </table>
        <#else >无分析结果</#if>
    </div>
    <div id="tabgd3" class="tab-pane fade">
        <button class="btn btn-default btn-loc" data-geo='${tplData.intersectGeo!}'>压盖已供地块</button>
        <button class="btn btn-default btn-loc" data-geo='${tplData.diffGeo!}'>压盖未供地块</button>
    </div>
    <div id="tabgd2" class="tab-pane fade in">
        <#if (tplData.result?size>0)>
            <#assign hyperlinks=tplData.hyperlinks! />
            <#assign detailItems=tplData.result.detail/>
            <#if detailItems?size &gt;0><#assign firstItem=detailItems[0]/>
                <table>
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
                    <#list detailItems as detailItem>
                        <#if firstItem??>
                            <tr>
                                <#list firstItem?keys as key>
                                    <#assign temp=true/>
                                    <td>
                                        <#if hyperlinks??>
                                            <#list hyperlinks as hyperlink>
                                                <#if hyperlink[key]??>
                                                    <#assign link=hyperlink[key][detailItem_index]/>
                                                    <#if link[detailItem[key]!"null"]??>
                                                        <#assign temp=false/>
                                                        <a href="${link[detailItem[key]!"null"]}"
                                                           target="_blank">${detailItem[key]!}</a>
                                                    </#if>
                                                </#if>
                                            </#list>
                                        </#if>
                                        <#if temp>
                                            <#if key=="SHAPE">
                                                <a class="btn btn-success btn-small"
                                                   onclick=gotoLocation('${detailItem[key]!}','gd');>查看</a>
                                            <#else >
                                            ${detailItem[key]!}
                                            </#if>
                                        </#if>
                                    </td>
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