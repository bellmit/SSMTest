<#assign cssContent>
<style>
    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
        height: 50px;
        text-align: center;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        max-height: 60px;
        min-width: 90px;
        text-align: center;
    }

    table {
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 15px;
        width: 100%;
    }

    .infoTable {
        background-color: transparent;
        border: 0px solid #DDDDDD;
        table-layout: auto;
        border-collapse: collapse;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 15px;
        width: 100%;
    }

    .infoTable tr th {
        border: 0px solid #DDDDDD;
        max-height: 60px;
        width: 100px;
        text-align: right;
    }

    .infoTable tr td {
        border: 0px solid #DDDDDD;
        padding: 5px 10px;
        height: 50px;
        text-align: left;
    }
    .nav-tabs{
        height: 40px !important;
    }

    h5 {
        font-weight: normal;
        color: #188074;
        margin-left: 10px;
    }

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
<div class="container" style="margin-top: 30px;width: 80%;">
    <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;南通市允许建设用地剩余面积分析结果展示</h3>

    <div class="pull-right" style="margin-top:-43px;margin-right: auto;"><h5>单位: 公顷
    </div>
    <div id="basicContainer" class="container" style="width: 80%;">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>行政区</th>
                <th>允许建设用地管制区面积</th>
                <th>报批地块面积</th>
                <th>允许建设用地剩余面积</th>
            </tr>
            </thead>
            <tbody>
               <#-- <#if resLst??>-->
                   <#list resultList as map>
                    <tr>
                        <td>
                       <#list map?keys as itemKey>
                           <#if itemKey=="xzqmc">
                               ${map[itemKey]}
                           </#if>
                       </#list></td>
                    <td>
                       <#list map?keys as itemKey>
                           <#if itemKey=="jsArea">
                              ${map[itemKey]}
                           </#if>
                       </#list>
                    </td>
                    <td>
                       <#list map?keys as itemKey>
                           <#if itemKey=="bpArea">
                              ${map[itemKey]}
                           </#if>
                       </#list>
                    </td>
                    <td>
                       <#list map?keys as itemKey>
                           <#if itemKey=="remainArea">
                               ${map[itemKey]}
                           </#if>
                       </#list>
                   </td>
                   </tr>
                   </#list>
            <#--</#if>-->
            </tbody>
        </table>
    </div>
</div>
</@aBase.tpl>
