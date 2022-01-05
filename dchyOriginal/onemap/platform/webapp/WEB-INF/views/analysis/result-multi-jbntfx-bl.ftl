<#assign cssContent>
<style>
    ::-webkit-scrollbar {
        width: 10px;
        height: 10px
    }

    ::-webkit-scrollbar-button:vertical {
        display: none
    }

    ::-webkit-scrollbar-corner, ::-webkit-scrollbar-track {
        background-color: #e2e2e2
    }

    ::-webkit-scrollbar-thumb {
        border-radius: 0;
        background-color: rgba(0, 0, 0, .3)
    }

    ::-webkit-scrollbar-thumb:vertical:hover {
        background-color: rgba(0, 0, 0, .35)
    }

    ::-webkit-scrollbar-thumb:vertical:active {
        background-color: rgba(0, 0, 0, .38)
    }

    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
        height: 50px;
        text-align: center;
    }

    table tr th {
        border: 1px solid #bbbbbb;
        padding: 10px;
        background-color: #cccccc;
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

    .wrapper,
    .header {
        width: 84%;
        margin-bottom: 15px;
    }

    .header > h3 {
        font-weight: normal;
        color: #188074
    }

    .header > div {
        margin-top: -43px;
        margin-right: auto;
    }

    .btn-options {
        margin-top: -10px;
    }

    .btn-options button {
        color: #188074;
    }

    .basic-container, .func-container {
        width: 100%;
    }

    .basic-container .row {
        margin-bottom: 15px;
    }

    .basic-container .tab-content {
        margin-top: 5px;
        overflow-x: auto;
    }

    .basic-container .export-container {
        padding-top: 20px;
    }

    .basic-container .export-container a {
        margin-right: 12px;
    }

    .func-header-panel {
        margin-top: 20px;
    }

    .func-main-wrapper {
        width: 320px;
        float: left;
        padding-right: 20px;
    }

    .dropdown-menu li {
        text-align: left;
    }

    .detailPanel {
        display: none;

        z-index: 10000;

        background-color: #fafafa;

        width: 675px;
        min-height: 200px;
        max-height: 630px;
        /*overflow-y: auto;*/
        border: 1px solid #666;

        /* CSS3 styling for latest browsers */
        -moz-box-shadow: 0 0 90px 5px #000;
        -webkit-box-shadow: 0 0 90px #000;
    }

    .close {

        background-image: url("<@com.rootPath/>/img/overlay_close.png");
        position: absolute;
        right: 0px;
        top: -5px;
        cursor: pointer;
        height: 32px;
        width: 32px;
    }

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
    <#assign fixed>
        <#switch unit?upper_case>
            <#case 'SQUARE'>0.##<#break>
            <#case 'ACRES'>0.####<#break>
            <#case 'HECTARE'>0.####<#break>
            <#default>0.##
        </#switch>
    </#assign>
<#--标题-->
<div class="container header">
    <h3>
        <#if data?size lte 1 >
        ${data[0].year!}
        </#if>
        基本农田分析
    </h3>
    <div class="pull-right">
        <h5 style="<#if data?size gt 1>
                margin-right: auto
        <#else>
                margin-right:30px
        </#if>">单位：
            <#switch unit?upper_case>
                <#case 'SQUARE'>平方米<#break>
                <#case 'ACRES'>亩<#break>
                <#case 'HECTARE'>公顷<#break >
                <#default>平方米
            </#switch>
        </h5>
    </div>
</div>
<div class="container wrapper">
    <div class="row">
        <#if data?size gt 0>
            <#if data?size gt 1>
                <div class="col col-lg-2 col-md-2 col-sm-2 col-xs-2">
                    <ul class="nav nav-pills nav-stacked scroll-fixed" role="tablist">
                        <#list data as dataItem>
                            <li class="text-center <#if dataItem_index==0>active</#if>">
                                <a href="#content${dataItem.year}" data-toggle="tab">${dataItem.year!}年</a>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>
            <div class="tab-content col <#if data?size == 1>col-lg-12 col-md-12 col-sm-12 col-xs-12<#else>col-lg-10 col-md-10 col-sm-10 col-xs-10</#if>">
                <#list data as dataRow>
                    <div id="content${dataRow.year}" class="tab-pane fade in <#if dataRow_index==0>active</#if>">
                        <#if dataRow.analysisData??>
                            <#assign analysisData=dataRow.analysisData>
                            <#if analysisData?size gt 0>
                                <div id="basicContainer${dataRow.year!}" class="container basic-container">
                                    <div class="row">
                                        <div class="tab-content">
                                            <div id="tab${dataRow.year!}" class="tab-pane fade in active">
                                                <table>
                                                    <tr>
                                                        <th>权属单位</th>
                                                        <th>总面积</th>
                                                        <th>权属性质</th>
                                                        <th>占用面积</th>
                                                        <th>详细</th>
                                                    </tr>
                                                    <#list analysisData as item>
                                                        <#assign qsxzGroupData=item.data>
                                                        <#assign firstOne = qsxzGroupData[0] />
                                                        <tr>
                                                            <td rowspan="${qsxzGroupData?size}">${item["QSDWMC"]!}</td>
                                                            <td rowspan="${qsxzGroupData?size}">${item["totalArea"]!?string(fixed)}</td>
                                                            <td>${firstOne["QSXZ"]!}</td>
                                                            <td>${firstOne["ZYJBNTMJ"]!?string(fixed)}</td>
                                                            <#if firstOne.QSDWMC =="合计">
                                                                <td>/</td>
                                                            <#else>
                                                                <td rel="#detailPanel${dataRow.year!}${item_index}0">
                                                                    <a onclick="detailInfo(${item_index}0);"
                                                                       style="cursor: pointer;"><span>详细信息</span></a>
                                                                </td>
                                                            </#if>
                                                        </tr>
                                                        <#if qsxzGroupData?size gt 1>
                                                            <#list qsxzGroupData[1..] as qsxzData>
                                                                <tr>
                                                                    <td>${qsxzData["QSXZ"]!}</td>
                                                                    <td>${qsxzData["ZYJBNTMJ"]!?string(fixed)}</td>
                                                                    <#if qsxzData.QSDWMC =="合计">
                                                                        <td>/</td>
                                                                    <#else>
                                                                        <td rel="#detailPanel${dataRow.year!}${item_index}${qsxzData_index +1}">
                                                                            <a onclick="detailInfo(${item_index}${qsxzData_index+1});"
                                                                               style="cursor: pointer;"><span>详细信息</span></a>
                                                                        </td>
                                                                    </#if>
                                                                </tr>
                                                            </#list>
                                                        </#if>
                                                    </#list>
                                                </table>
                                                <#list analysisData as analysis>
                                                    <#assign parentData=analysis.data>
                                                    <#list parentData as parent>
                                                        <#if parent.QSDWMC !="合计">
                                                            <#assign detail=parent.detail>
                                                            <div class="detailPanel"
                                                                 id="detailPanel${dataRow.year!}${analysis_index}${parent_index}">
                                                                <div style="width: 100%;margin-top: 30px;max-height: 580px;overflow-y: auto;padding-bottom: 20px;padding: 0px 20px 20px 20px;">
                                                                    <#if detail?size gt 0>
                                                                        <table>
                                                                            <tr>
                                                                                <th>序号</th>
                                                                                <th>权属单位</th>
                                                                                <th>权属性质代码</th>
                                                                                <th>基本农田面积</th>
                                                                                <th>标识码</th>
                                                                                <th>占用面积</th>
                                                                            </tr>
                                                                            <#list detail as child>
                                                                                <tr>
                                                                                    <td>${child_index+1}</td>
                                                                                    <td>${child["QSDWMC"]!}</td>
                                                                                    <td>${child["QSXZDM"]!}</td>
                                                                                    <td>${child["JBNTMJ"]!?string(fixed)}</td>
                                                                                    <td>${child["BSM"]!}</td>
                                                                                    <td>${child["ZYJBNTMJ"]!?string(fixed)}</td>
                                                                                </tr>
                                                                            </#list>
                                                                        </table>
                                                                    </#if>
                                                                </div>
                                                            </div>
                                                        </#if>
                                                    </#list>
                                                </#list>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                            <div class="export-container text-center">
                                <a class="btn btn-primary" data-excel='${dataRow.exportXls!}'
                                   onclick="exportExcel(this)">导出
                                    Excel</a>
                            </div>
                        <#else><h5 class="text-warning text-center">分析无结果</h5>
                        </#if>
                    </div>
                </#list>
            </div>
        <#else >无结果
        </#if>
    </div>
</div>


<script src="/omp/static/thirdparty/jquery/jquery-scrolltofixed-min.js"></script>
<script src="<@com.rootPath/>/static/thirdparty/jquery/jquery.tools.min.js"></script>
<script type="text/javascript">
    function detailInfo(index) {
        $("td[rel]").overlay({
            closeOnClick: true
        });
    };

    function exportExcel(f) {
        var data = $(f).data('excel');
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "jbnt_bl.xml");
    }

    function openPostWindow(url, data, fileName) {
        if (data == null) {
            alert("无导出数据");
            return;
        }
        var tempForm = document.createElement("form");
        tempForm.target = "blank";
        tempForm.method = "post";
        tempForm.action = url;
        var hideInput1 = document.createElement("input");
        hideInput1.type = "hidden";
        hideInput1.name = "data";
        hideInput1.value = data;
        var hideInput2 = document.createElement("input");
        hideInput2.type = "hidden";
        hideInput2.name = "fileName";
        hideInput2.value = fileName;
        tempForm.appendChild(hideInput1);
        if (fileName != null && fileName != "null" && fileName != "")
            tempForm.appendChild(hideInput2);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

</script>
</@aBase.tpl>
