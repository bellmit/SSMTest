<#if (tplData?keys?size>0)>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#TDYTQInfo" data-toggle="tab">土地用途分区</a></li>
        <li><a href="#JSYDGZQInfo" data-toggle="tab">建设用地管制区</a></li>
        <li><a href="#GHJBNTTZInfo" data-toggle="tab">规划基本农田调整</a></li>
        <li><a href="#MZZDJSXMInfo" data-toggle="tab">重点建设项目</a></li>
    </ul>
</div>
<div class="span9">
    <div class="tab-content large">
        <div id="TDYTQInfo" class="tab-pane fade in active"><@tdghsc key="土地用途分区"></@tdghsc></div>
        <div id="JSYDGZQInfo" class="tab-pane fade"><@tdghsc key="建设用地管制区"></@tdghsc></div>
        <div id="GHJBNTTZInfo" class="tab-pane fade"><@tdghsc key="规划基本农田调整"></@tdghsc></div>
        <div id="MZZDJSXMInfo" class="tab-pane fade"><@tdghsc key="重点建设项目"></@tdghsc></div>
    </div>
</div>
<#else >无分析结果</#if>

<#-- macro -->
<#macro tdghsc key="">
    <#assign gh=tplData!/>
    <#if gh.result??&&gh.result[key]??>
        <#assign info=gh.result["${key}"].info>
    <table style="margin-top: 30px;">
        <tr>
            <th>类别</th>
            <th>所占面积(m<sup>2</sup>)</th>
            <th>详细</th>
        </tr>
        <#list info[1..] as item>
            <tr>
                <td style="text-align: center;">${item["LXMC"]!}</td>
                <td>${item["AREA"]!?string("####")}</td>
                <td style="text-align: center;" rel="#detailPanel${key}${item_index}"><a
                        onclick="detailInfo(${item_index});" style="cursor:pointer;"><span>详细信息</span></a></td>
            </tr>
        </#list>
    </table>
        <#list info[1..] as parent>
            <#assign detail=parent.detail>
        <div class="detailPanel" id="detailPanel${key}${parent_index}">
            <div style="width: 100%;margin-top: 30px; max-height: 580px;overflow-y: auto;padding-bottom: 20px;">
                <#if (detail?size>0)>
                    <table>
                        <tr>
                            <th>序号</th>
                            <#list detail[0]?keys as key>
                                <th>${key}</th>
                            </#list>
                        </tr>
                        <#list detail as child>
                            <tr>
                                <td>${child_index+1}</td>
                                <#list child?keys as key>
                                    <td><#if (key?contains("标识码")||key?contains("面积"))&&child[key]?exists>${child[key]!?string('####')}<#else>${child[key]!?string}</#if></td>
                                </#list>
                            </tr>
                        </#list>
                    </table><#else ><h5>无</h5></#if>
            </div>
        </div>
        </#list>
    <#else ><h5>该分类下无分析结果</h5>
    </#if>
</#macro>