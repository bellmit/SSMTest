<#assign cssContent>
<style>
    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
        height: 50px;
        text-align: center;
        color:black !important;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        max-height: 60px;
        min-width: 90px;
        text-align: center;
        color:black !important;
    }

    table {
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 15px;
        width: 80%;
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
<div class="container" style="width: 1240px;">
    <div class="row">
        <div style="float:left;margin: 20px">
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;常州市局耕地质量等级分析结果展示</h3>
        </div>
        <div style="float:right;margin-top:50px;margin-right: 83px;"><h5>单位: 平方米</h5>
        </div>
        <div style="float:right;margin-top:50px;margin-right: 30px">
            <div style="line-height:35px;font-weight:700">
                地块面积:${geoArea!}
            </div>
        </div>
    </div>

    <div class="row">
        <div style="width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <#list result?keys as key>
                    <li <#if key_index==0>class="active"</#if>><a href="#${key}"
                                                                  data-toggle="tab">${key}</a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="span12">
            <div class="tab-content large">
                <#list result?keys as key>
                    <#assign detail=result[key]!/>
                    <div id="${key!}" class="tab-pane fade in <#if key_index==0>active</#if>">
                        <table class="table-bordered">
                            <tr>
                                <th>质量等级</th>
                                <th>质量分析结果</th>

                            </tr>
                            <#list detail as k>
                                <tr>
                                    <td>${k['zldj']!}</td>
                                    <td>${k['area']!0}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </#list>
            </div>
        </div>
    </div>
    <div style="text-align: center;padding-top: 20px;">
        <a class="btn btn-primary" style="margin-right: 12px;" onclick="exportExcel();">导出excel</a>
    </div>
</div>

<script type="text/javascript">
    var result = '${resultStr!}';

    function exportExcel(){
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", result, "czsjgd_export.xml");
    }

    function openPostWindow(url, data, fileName) {
        if (data == "") {
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
</@aBase.tpl>