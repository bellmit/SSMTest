<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>${env.getEnv('local.title')}国土资源“一张图”-信息门户子系统</title>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/bootstrap/bootstrap.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/font/css/font-awesome.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/gridtree/gridtree.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/loading/loading.css"/>

    <link rel="stylesheet" href="<@com.rootPath/>/static/css/hack.css"/>
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
            margin: 0 auto;
        }

        .tab-content {
            padding: 20px;
            border: 1px solid #ddd;
            border-top: none;
            border-radius: 0 0 5px 5px;

        }

    </style>
</head>
<#macro getSequence str>
    <#if str?index_of(",") gt 0>
        <#assign sequence=str?split(",")/>
    <ul class="ul inline">
        <#list sequence as item>
            <li>${item!}</li>
        </#list>
    </ul>
    <#else >${str!}</#if>
</#macro>
<body>
<div class="container" style="width: 85%;">
    <div>
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;
            数据核查结果表</h3>
    </div>
    <div style="margin: 20px auto;">
        <div class="tab-content">
            <table>
                <tr>
                    <th>序号</th>
                <#list showFields as field>
                    <th>${field.alias!}</th>
                </#list>
                    <th>重叠面积(m<sup>2</sup>)</th>
                    <th>重叠面积(hm<sup>2</sup>)</th>
                </tr>
            <#if results?size &gt;0>
                <#list results as item>
                <tr>
                    <td>${item_index+1}</td>
                    <#list showFields as field>
                        <td>${item[field.name]!}</td>
                    </#list>
                    <td>${item.SHAPE_AREA!}</td>
                    <td>${item.SHAPE_AREA?double*0.0001}</td>
                </tr>
                </#list>
            <#else >
                <div style="text-align: center"><h4>无分析结果</h4></div>
            </#if>

            </table>
        </div>
    </div>
    <div style="text-align: center;padding-top: 20px;">
        <a class="btn btn-primary" onclick="exportExcel();">导出excel</a>
    </div>
</div>
<script src="<@com.rootPath/>/js/jquery/jquery_1.9.0.js"></script>
<script src="<@com.rootPath/>/js/portal/lib/bootstrap.js"></script>
<script src="<@com.rootPath/>/static/js/plugins.js"></script>
<script type="text/javascript">
    /**
     * 导出excel
     */
    function exportExcel() {
        openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${excelData}');
    }
    function openPostWindow(url, data) {
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;
        var hideInput1 = document.createElement("input");
        hideInput1.type = "hidden";
        hideInput1.name = "data"
        hideInput1.value = data;
        tempForm.appendChild(hideInput1);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }
</script>
</body>
</html>