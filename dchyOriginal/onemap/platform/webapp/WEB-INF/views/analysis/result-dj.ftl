<#assign cssContent>
<style>
    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;

    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: center;
        vertical-align: middle;
        min-width: 60px;
    }

    table {
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
    }

    .tab-content {
        padding: 20px;
        border: 1px solid #ddd;
        border-top: none;
        border-radius: 0 0 5px 5px;
    }

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent>
<div class="container" style="width: 85%;">
    <div>
        <h3 style="font-weight: normal; color:#188074">基本农田核销情况一览表</h3>
        <div class="pull-right" style="margin-top: -35px;">(单位：${unit.alias!'亩'})</div>
        <div style="margin: 20px auto;">
            <div class="tab-content">
                <table>
                    <#if results??>
                        <#list results as item>
                            <#if item??>
                                <tr>
                                    <th rowspan="2">基本农田总面积</th>
                                    <th colspan="4">其中：耕地</th>
                                    <th colspan="8">其中：可调整地类</th>
                                </tr>
                                <tr>
                                    <th>小计</th>
                                    <th>水田</th>
                                    <th>旱地</th>
                                    <th>水浇地</th>
                                    <th>小计</th>
                                    <th>可调整茶园</th>
                                    <th>可调整果园</th>
                                    <th>可调整其他园地</th>
                                    <th>可调整有林地</th>
                                    <th>可调整其他林地</th>
                                    <th>可调整人工牧草地</th>
                                    <th>可调整坑塘水面</th>
                                </tr>
                                <tr>
                                    <td>${item.total!''}</td>

                                    <td>${item.gdmap['total']!''}</td>
                                    <td>${item.gdmap['011']!''}</td>
                                    <td>${item.gdmap['012']!''}</td>
                                    <td>${item.gdmap['013']!''}</td>
                                    <td>${item.ktmap['total']!''}</td>
                                    <td>${item.ktmap['021']!''}</td>
                                    <td>${item.ktmap['022']!''}</td>
                                    <td>${item.ktmap['023']!''}</td>
                                    <td>${item.ktmap['031']!''}</td>
                                    <td>${item.ktmap['033']!''}</td>
                                    <td>${item.ktmap['042']!''}</td>
                                    <td>${item.ktmap['114']!''}</td>
                                </tr>
                            </#if>
                        </#list>
                    <#else>
                        <div style="text-align: center"><h4>无分析结果</h4></div>
                    </#if>
                </table>
            </div>
        </div>
    </div>
</div>
</@aBase.tpl>