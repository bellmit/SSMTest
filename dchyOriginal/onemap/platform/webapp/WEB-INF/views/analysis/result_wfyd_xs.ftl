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

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
    <#assign unit=resultMap.unit/>
    <#assign fixed>
        <#if decimal??>
        ${decimal}
        <#else >
            <#switch unit?upper_case>
                <#case 'SQUARE'>0.00<#break>
                <#case 'ACRES'>0.0000<#break>
                <#case 'HECTARE'>0.0000<#break>
                <#default>0.##
            </#switch>
        </#if>
    </#assign>
<#--??????-->
<div class="container header">
    <h3>
        <span class="icon icon-dashboard"/>&nbsp;
        ????????????????????????????????????
    </h3>
    <div class="pull-right">
        <h5 style="margin-right: auto">??????:
            <#switch unit?upper_case>
                <#case 'SQUARE'>?????????<#break>
                <#case 'ACRES'>???<#break>
                <#case 'HECTARE'>??????<#break>
                <#default>?????????
            </#switch>
        </h5>
    </div>
</div>
<div class="container wrapper">
    <div class="row">
        <table>
            <tr>
                <td rowspan="4">??????????????????<br>????????????</td>
                <td rowspan="4">${resultMap["fh?????????"]!?string(fixed)}</td>
                <td rowspan="4">??????</td>
                <td>????????????</td>
                <td colspan="2">${resultMap["fh????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>??????</td>
                <td colspan="2">${resultMap["fh??????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>???????????????</td>
                <td colspan="2">${resultMap["fh???????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>????????????</td>
                <td colspan="2">${resultMap["fh????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td rowspan="5">?????????????????????<br>????????????</td>
                <td rowspan="5">${resultMap["bfh?????????"]!?string(fixed)}</td>
                <td rowspan="5">??????</td>
                <td>????????????</td>
                <td colspan="2">${resultMap["bfh????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td rowspan="2">??????</td>
                <td>??????????????????</td>
                <td>${resultMap["?????????????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>??????</td>
                <td>${resultMap["bfh??????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>???????????????</td>
                <td colspan="2">${resultMap["bfh???????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>????????????</td>
                <td colspan="2">${resultMap["bfh????????????"]!?string(fixed)}</td>
            </tr>
            <tr>
                <td>??????</td>
                <td>${resultMap["zmj"]!?string(fixed)}</td>
                <td colspan="2">??????</td>
                <td colspan="2">${resultMap["zmj"]!?string(fixed)}</td>
            </tr>
        </table>
    </div>
    <div class="export-container text-center">
        <a class="btn btn-primary" data-excel='${exportXls!}'
           onclick="exportExcel(this);">?????? excel</a>
    </div>
</div>

<script src="/omp/static/thirdparty/jquery/jquery-scrolltofixed-min.js"></script>
<script type="text/javascript">
    /**
     * ?????????????????????excel
     */
    function exportExcel(f) {
        var data = $(f).data('excel');
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "wfydfx_xs.xml");
    }

    function openPostWindow(url, data, fileName) {
        if (data == "") {
            alert("???????????????!");
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
        var type = $("#flSxzt").val();
        var hideInput3 = document.createElement("input");
        hideInput3.type = "hidden";
        hideInput3.name = "type";
        hideInput3.value = type;
        tempForm.appendChild(hideInput1);
        if (fileName != null && fileName != "null" && fileName != "")
            tempForm.appendChild(hideInput2);
        tempForm.appendChild(hideInput3);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

</script>
</@aBase.tpl>
