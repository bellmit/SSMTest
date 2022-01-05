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

        .nav-tabs {
            margin-bottom: 0px;
        }

        .detailPanel {
            display: none;
            padding: 10px;
        }

    </style>
</head>
<body>
<div id="hearder" class="navbar navbar-inverse">
    <div class="navbar-inner">
        <div class="container">
            <a href="<@com.rootPath/>/portal2" class="brand"
               style="background: url(${path_omp}/resources/img${env.getEnv('local.path')}/logo2.png) 0 0 no-repeat;">${env.getEnv('local.title')}
                国土资源“一张图”信息门户</a>

            <div class="pull-right">
                <div class="small-nav">
                    <ul class="nav nav-pills">
                        <li><a href="${path_omp}">主页</a></li>
                    <#if env.getEnv('main.level')='provincial'>
                        <li><a href="http://172.18.2.70/homepages/singleLogin.jsp?license=${ctx.getToken()!}"
                               target="_blank">政务管理平台</a></li></#if>
                        <li><a href="#">帮助</a></li>
                    <#if env.getEnv('main.level')='provincial'>
                        <li><a href="#" data-ask="确定退出系统吗?" class="i_quit">&nbsp;退出</a></li>
                    <#else>
                        <li><a href="${path_oms}/logout?url=${path_omp}" data-ask="确定退出系统吗?" class="j_ask"><i
                                class="icon icon-signout"></i>&nbsp;退出</a></li>
                    </#if>
                    </ul>
                </div>
                <div class="main-nav">
                    <ul class="nav nav-pills">
                        <li><a href="<@com.rootPath/>/portal2/onemap/11">一张图</a></li>
                        <li><a href="<@com.rootPath/>/portal2/tpl/index">专题图</a></li>
                        <li><a href="#">统计报表</a></li>
                        <li><a href="<@com.rootPath/>/portal2/rescenter/db">资源中心</a></li>
                        <li><a href="<@com.rootPath/>/portal2/api/index">地图API</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<#macro getValue item key>
    <#list item?keys as itemKey>
        <#if itemKey=key>
        ${item[key]!}
        </#if>
    </#list>
</#macro>

<div class="container">
    <div>
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;分析结果展示</h3>
    </div>
    <div style="float:right;"><input type="button" value="导出excel" onclick="exportExcel()" style="color:#188074;"/>
    </div>
    <div style="margin: 20px auto;">
        <table class="table" style="background-color: transparent;">
            <tr>
                <th rowspan="2" style="text-align: center;vertical-align: middle; min-width: 100px">项目类型</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">行政区名称</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">行政区代码</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">项目名称</th>
                <th colspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">允许建设用地区（010）</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">有条件建设用地区（020）</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">限制建设用地区（030）</th>
                <th rowspan="2"  style="text-align: center;vertical-align: middle; min-width: 100px">禁止建设用地区（040）</th>
            </tr>
            <tr>
                <th  style="text-align: center;vertical-align: middle; min-width: 100px">现状建设用地(011)</th>
                <th  style="text-align: center;vertical-align: middle; min-width: 100px">新增建设用地(012)</th>
            </tr>
        <#list result as item>
        <#assign  pros = item.value>
        <#assign firstPro=pros[0]>
            <tr>
                <td rowspan="${item.value?size}" style="text-align: center;vertical-align: middle;">${item.type!}</td>
                <td>${firstPro["OG_PRO_XZQMC"]!}</td>
                <td>${firstPro["OG_PRO_REGIONCODE"]!}</td>
                <td>${firstPro["OG_PRO_PRONAME"]!}</td>
                <td>${firstPro["011"]!?string("0.####")}</td>
                <td>${firstPro["012"]!?string("0.####")}</td>
                <td>${firstPro["020"]!?string("0.####")}</td>
                <td>${firstPro["030"]!?string("0.####")}</td>
                <td>${firstPro["040"]!?string("0.####")}</td>
                <#--<td><#if firstPro.GZQLXDM='011'>${firstPro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                <#--<td><#if firstPro.GZQLXDM='012'>${firstPro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                <#--<td><#if firstPro.GZQLXDM='020'>${firstPro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                <#--<td><#if firstPro.GZQLXDM='030'>${firstPro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                <#--<td><#if firstPro.GZQLXDM='040'>${firstPro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
            </tr>
                <#list pros[1..] as pro>
                <tr>
                    <td>${pro["OG_PRO_XZQMC"]!}</td>
                    <td>${pro["OG_PRO_XZQDM"]!}</td>
                    <td>${pro["OG_PRO_XMMC"]!}</td>
                    <td>${pro["011"]!?string("0.####")}</td>
                    <td>${pro["012"]!?string("0.####")}</td>
                    <td>${pro["020"]!?string("0.####")}</td>
                    <td>${pro["030"]!?string("0.####")}</td>
                    <td>${pro["040"]!?string("0.####")}</td>
                    <#--<td><#if pro.GZQLXDM='011'>${pro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                    <#--<td><#if pro.GZQLXDM='012'>${pro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                    <#--<td><#if pro.GZQLXDM='020'>${pro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                    <#--<td><#if pro.GZQLXDM='030'>${pro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                    <#--<td><#if pro.GZQLXDM='040'>${pro["SHAPE_AREA"]!?string("0.####")}</#if></td>-->
                </tr>
                </#list>

        </#list>
        </table>
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
        var fileName = "gzqyd_analysis.xlsx";
        var officeVersion = '${env.getEnv("office.plugin.version")!}';
        if(officeVersion=='old')
            fileName = "gzqyd_analysis.xls";
        openPostWindow("<@com.rootPath/>/geometryService/export/excel/gzqyd",'${excelData}','gzqyd_analysis.xlsx');
    }
    function openPostWindow(url, data, fileName) {
        if(data=="")
        {
            alert("无导出数据!");
            return;
        }
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;
        var hideInput1 = document.createElement("input");
        hideInput1.type = "hidden";
        hideInput1.name = "data"
        hideInput1.value = data;
        var hideInput2 = document.createElement("input");
        hideInput2.type = "hidden";
        hideInput2.name = "fileName"
        hideInput2.value = fileName;
        tempForm.appendChild(hideInput1);
        if (fileName != null && fileName != "null" && fileName != "")
            tempForm.appendChild(hideInput2);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }
</script>
<style type="text/css">
</style>
</body>
</html>