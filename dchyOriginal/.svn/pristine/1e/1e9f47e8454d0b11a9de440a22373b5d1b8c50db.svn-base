<#--综合分析江阴订制页面-->
<#assign  xz= result.xz!/>
<#assign gh = result.gh!/>
<#assign gd=result.gd!/>
<#assign cb=result.cb!/>
<#assign kc=result.kc!/>
<#assign dz=result.dz!/>
<#assign zd=result.zd!/>
<#assign cl=result.cl!/>
<#assign sp=result.sp!/>
<#assign nt=result.nt!/>
<#assign bp=result.bp!/>
<#assign dj=result.dj!/>

<#assign xzexport= xz.resultStr!/>
<#assign ghexport = gh.excelData!/>
<#assign bpexport = bp.excelData!/>
<#assign gdexport = gd.excelData!/>
<#assign cbexport = cb.excelData!/>
<#assign djexport = dj.excelData!/>
<#assign dzexport = dz.excelData!/>
<#assign zdexport = zd.excelData!/>
<#assign kcexport = kc.excelData!/>
<#assign clexport = cl.excelData!/>
<#assign spexport = sp.excelData!/>
<#assign ntexport = nt.excelData!/>

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
        min-width: 80px;
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

    .large {
        min-height: 572px;
        border: 0px !important;
        border-radius: 4px;
        overflow: hidden;
        word-break: break-all;
    }

    .tab-content {
        padding: 20px;
        border: 1px solid #ddd;
        border-top: none;
        border-radius: 0 0 5px 5px;
    }

    h5 {
        font-weight: normal;
        color: #000;
        margin-left: 10px;
    }

    a:hover {
        color: purple;
    }

    .row {
        margin-left: 2px;
    }
</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent>
<div class="container" style="width: 1240px;">
    <div class="row">
        <div style="float:left;">
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;综合分析结果展示</h3>
        </div>
        <div style="float:right;margin-top: 20px;margin-right: 83px;">
            <input type="button" value="导出excel" onclick="exportExcel()" style="color:#188074;"/>
        </div>
    </div>
    <div class="row">
        <div style="width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <#list result?keys as key>
                    <#assign value=result[key]!/>
                    <li <#if key_index==0>class="active"</#if>><a href="#${key}"
                                                                  data-toggle="tab">${value["alias"]!}</a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="span12">
            <div class="tab-content large">
                <#list result?keys as key>
                    <#assign tplData=result[key]!/>
                    <div id="${key!}" class="tab-pane fade in <#if key_index==0>active</#if>">
                        <#if tplData?keys?size &gt;0>
                            <div>
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#tab_i_${key_index!}" data-toggle="tab">汇总</a></li>
                                    <li><a href="#tab_d_${key_index!}" data-toggle="tab">详情</a></li>
                                </ul>
                            </div>
                            <div class="tab-content">
                                <div id="tab_i_${key_index!}" class="tab-pane fade in active">
                                    <#if tplData??>
                                        <#assign infoItems=tplData.info/>
                                        <table>
                                            <tr>
                                                <th>套合地块数</th>
                                                <th>套合面积</th>
                                                <th>未套合面积</th>
                                            </tr>
                                            <#list infoItems as infoItem>
                                                <tr>
                                                    <td>${infoItem["count"]!}</td>
                                                    <td>${infoItem["coverArea"]!?string("0.####")}</td>
                                                    <td>${infoItem["unCoverArea"]!?string("0.####")}</td>
                                                </tr>
                                            </#list>
                                        </table>
                                    <#else >无分析结果</#if>
                                </div>
                                <div id="tab_d_${key_index!}" class="tab-pane fade">
                                    <#if tplData??>
                                        <#assign detailItems=tplData.detail/>
                                        <#if detailItems?size &gt;0>
                                            <#assign firstItem=detailItems[0]/>
                                            <table>
                                                <tr>
                                                    <#list firstItem?keys as key>
                                                        <#if key=="IN_SHAPE_AREA"> <#else ><th>${key!}</th></#if>
                                                    </#list>
                                                </tr>
                                                <#list detailItems as detailItem>
                                                    <#if firstItem??>
                                                        <tr>
                                                            <#list firstItem?keys as key>
                                                                <#if key=="IN_SHAPE_AREA"><#else >
                                                                <td>${detailItem[key]!}</td>
                                                                </#if>
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
                    </div>
                </#list>
            </div>
        </div>
    </div>
</div>
<#--<script src="<@com.rootPath/>/js/jquery/jquery.tools.min.js"></script>-->
<script type="text/javascript">

    /***
     * 返回分析的坐标串 geojson 格式
     * */
    function getCoords() {
        return ${geo!};
    }

    /**
     * 导出word(分析报告)
     * */
    function exportWord() {
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", '${summaryData!}', "multi_report.xml", "doc");
    }
    /**
     * 导出excel
     */
    function exportExcel() {

        var fileType = ".xlsx";
        var officeVersion = '${env.getEnv("office.plugin.version")!}';
        if (officeVersion == 'old')
            fileType = ".xls";
        if ($('#xz').hasClass("active")) {
            <#if result.xz??>
                openPostWindowXZ("<@com.rootPath/>/geometryService/export/analysis", '${result.xz.resultStr!}', "tdlyxz.xml");
            </#if>
        }
        else if ($('#gh').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${ghexport}', "ghscfx_analysis" + fileType);
        else if ($('#bp').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${bpexport}', "bpfx_analysis" + fileType);
        else if ($('#gd').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${gdexport}', "gdfx_analysis" + fileType);
        else if ($('#kc').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${kcexport}', "kcfx_analysis" + fileType);
        else if ($('#dz').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${dzexport}', "dzfx_analysis" + fileType);
        else if ($('#zd').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${zdexport}', "zdfx_analysis" + fileType);
        else if ($('#cl').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${clexport}', "clbzdfx_analysis" + fileType);
        else if ($('#sp').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${spexport}', "spkzdfx_analysis" + fileType);
        else if ($('#nt').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${ntexport}', "jbntfx_analysis" + fileType);
        else if ($('#dj').hasClass("active"))
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${djexport}', "dj_analysis" + fileType);

    }

    /***
     * export excel
     * @param url
     * @param data
     * @param fileName
     */
    function openPostWindow(url, data, fileName, fileType) {
        if (data == null || data == 'null')
            alert("无分析数据可导出!");
        else {
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

            var hideInput3 = document.createElement("input");
            hideInput3.type = "hidden";
            hideInput3.name = "fileType"
            hideInput3.value = fileType;

            tempForm.appendChild(hideInput1);
            if (fileName != null && fileName != "null" && fileName != "")
                tempForm.appendChild(hideInput2);

            if (fileType != null && fileType != "null" && fileType != "")
                tempForm.appendChild(hideInput3);

            document.body.appendChild(tempForm);
            tempForm.submit();
            document.body.removeChild(tempForm);
        }
    }
    /***
     * for tdlyxz
     * @param url
     * @param data
     * @param fileName
     */
    function openPostWindowXZ(url, data, fileName) {
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

