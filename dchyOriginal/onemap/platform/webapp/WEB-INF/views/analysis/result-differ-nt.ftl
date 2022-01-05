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

    .nav-tabs {
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
    <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;超出中心城区范围分析结果</h3>

    <div class="pull-right" style="margin-top:-43px;margin-right: auto;"><h5>单位: 公顷
    </div>
    <div id="basicContainer" class="container" style="width: 80%;">
        <#if result?size gt 0>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>地块编号</th>
                <th>地块名称</th>
                <th>超出面积</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <#assign total=0/>
                    <#list result as map>
                        <#assign total=total+map.differArea/>
                        <td>
                        ${map.dkbh!}
                        </td>
                        <td>
                        ${map.dkmc!}
                        </td>
                        <td>
                        ${map.differArea!}
                        </td>
                    </#list>
                </tr>
                <tr>
                    <td>合计</td>
                    <td>-</td>
                    <td>${total!}</td>
                </tr>

            </tbody>
        </table>
        <#else>
            <div style="margin-top:40px;">没有超出中心城区范围</div>
        </#if>
    </div>
</div>
</@aBase.tpl>
