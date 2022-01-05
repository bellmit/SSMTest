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
<#assign year=result.year!/>
<#assign yearResultMap=result.yearResultMap!/>
<#assign fixed>
0.00
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
<div class="container header">
    <h3>
        <span class="icon icon-dashboard"/>&nbsp;
        批而未供分析结果展示</h3>
    <div class="pull-right">
        <h5 style="margin-right: 30px"></h5>
        单位：平方米
    </div>
</div>
<div class="container wrapper">
    <div class="row">
        <#if yearResultMap?size gt 0>
            <div class="col col-lg-2 col-md-2 col-sm-2 col-xs-2">
                <ul class="nav nav-pills nav-stacked scroll-fixed" role="tablist">
                    <#list yearResultMap?keys as key>
                        <li class="text-center <#if key_index==0>active</#if>">
                            <a href="#content${key}" data-toggle="tab">${key!}年</a>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="tab-content col col-lg-10 col-md-10 col-sm-10 col-xs-10">
                <#if yearResultMap?size gt 0>
                    <#list yearResultMap?keys as key>
                        <div id="content${key}" class="tab-pane fade in <#if key_index==0>active</#if>">
                            <#assign yearResult=yearResultMap[key]/>
                            <div id="basicContainer${key}" class="container basic-container">
                                <div class="row">
                                    <div>
                                        <ul class="nav nav-tabs">
                                            <li class="dropdown active">
                                                <a href="#tab${key}Count" class="dropdown-toggle"
                                                   data-toggle="tab">合计</a>
                                            </li>
                                            <#list yearResult?keys as xzqKey>
                                                <#if xzqKey!="shpList" && xzqKey!="yearTotalMap">
                                                    <#assign xzqMap=yearResult[xzqKey] />
                                                    <li class="dropdown">
                                                        <a href="#tab${key}${xzqKey}" class="dropdown-toggle"
                                                           data-toggle="tab">${xzqMap.xzqmc!}</a>
                                                    </li>
                                                </#if>
                                            </#list>

                                        </ul>
                                    </div>
                                    <div class="tab-content">
                                        <div id="tab${key}Count"
                                             class="tab-pane fade in active">
                                            <#assign yearTotalMap=yearResult["yearTotalMap"]/>
                                            <#assign detailList=yearTotalMap["detailDataList"]/>
                                            <#if detailList??>
                                                <table>
                                                    <tr>
                                                        <th>项目名称</th>
                                                        <th>报批面积</th>
                                                        <th>已供面积</th>
                                                        <th>未供面积</th>
                                                        <th>宗地</th>
                                                    </tr>
                                                    <#list detailList as detailData>
                                                        <tr>
                                                            <td>${detailData["XMMC"]!}</td>
                                                            <td>${detailData["DKMJ"]!?string(fixed)}</td>
                                                            <td>${detailData["ygArea"]!?string(fixed)}</td>
                                                            <td>${detailData["wgArea"]!?string(fixed)}</td>
                                                            <td>${detailData["zd"]!0}</td>
                                                        </tr>
                                                    </#list>
                                                    <#assign sumMap=yearTotalMap["sumMap"]/>
                                                    <tr>
                                                        <td>合计</td>
                                                        <td>${sumMap["DKMJ"]!?string(fixed)}</td>
                                                        <td>${sumMap["ygArea"]!?string(fixed)}</td>
                                                        <td>${sumMap["wgArea"]!?string(fixed)}</td>
                                                        <td>${sumMap["zd"]!0}</td>
                                                    </tr>
                                                </table>
                                            </#if>
                                            <div class="export-container text-center">
                                                <a class="btn btn-primary"
                                                   data-excel='${yearTotalMap.exportXls!}' onclick="exportExcel(this);">导出
                                                    excel</a>
                                                <a class="btn btn-primary" data-shp='${yearTotalMap.shpId!}'
                                                   onclick="exportFeatures(this);">导出 图形</a>
                                            </div>
                                        </div>
                                        <#list yearResult?keys as xzqKey>
                                            <#if xzqKey!="shpList" && xzqKey!="yearTotalMap">
                                                <#assign xzqMap=yearResult[xzqKey] />
                                                <div id="tab${key}${xzqKey}" class="tab-pane fade in ">
                                                    <#assign detailList=xzqMap["detailDataList"]/>
                                                    <#if detailList??>
                                                        <table>
                                                            <tr>
                                                                <th>项目名称</th>
                                                                <th>报批面积</th>
                                                                <th>已供面积</th>
                                                                <th>未供面积</th>
                                                                <th>宗地</th>
                                                            </tr>
                                                            <#list detailList as detailData>
                                                                <tr>
                                                                    <td>${detailData["XMMC"]!}</td>
                                                                    <td>${detailData["DKMJ"]!?string(fixed)}</td>
                                                                    <td>${detailData["ygArea"]!?string(fixed)}</td>
                                                                    <td>${detailData["wgArea"]!?string(fixed)}</td>
                                                                    <td>${detailData["zd"]!0}</td>
                                                                </tr>
                                                            </#list>
                                                            <#assign sumMap=xzqMap["sumMap"]/>
                                                            <tr>
                                                                <td>合计</td>
                                                                <td>${sumMap["DKMJ"]!?string(fixed)}</td>
                                                                <td>${sumMap["ygArea"]!?string(fixed)}</td>
                                                                <td>${sumMap["wgArea"]!?string(fixed)}</td>
                                                                <td>${sumMap["zd"]!0}</td>
                                                            </tr>
                                                        </table>
                                                    </#if>
                                                    <div class="export-container text-center">
                                                        <a class="btn btn-primary" data-excel='${xzqMap.exportXls!}'
                                                           onclick="exportExcel(this);">导出
                                                            excel</a>
                                                        <a class="btn btn-primary" data-shp='${xzqMap.shpId!}'
                                                           onclick="exportFeatures(this);">导出
                                                            图形</a>
                                                    </div>
                                                </div>
                                            </#if>
                                        </#list>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
        <#else>
            <h5 class="text-muted text-center">无结果</h5>
        </#if>
    </div>
</div>

<script src="/omp/static/thirdparty/jquery/jquery-scrolltofixed-min.js"></script>
<script type="text/javascript">
    /**
     * 导出分析结果至excel
     */
    function exportExcel(f) {
        var data = $(f).data('excel');
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "dt_pewg.xml");
    }

    function openPostWindow(url, data, fileName) {
        if (data == "") {
            alert("无导出数据!");
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

    /**
     * 导出shp
     * @param type
     * */
    function exportFeatures(f) {
        var shpId = $(f).data('shp');
        if (shpId == null && shpId == '') {
            alert("生成shpId失败 无法执行导出操作");
            return;
        }
        var shpUrl = '${path_omp!'/omp'}/file/download/'.concat(shpId);
        window.location.target = '_blank';
        window.location.href = shpUrl;
    }

</script>
</@aBase.tpl>