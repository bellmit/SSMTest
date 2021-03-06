<#assign cssContent>
<style>
    ::-webkit-scrollbar {
        width:10px;
        height:10px
    }
    ::-webkit-scrollbar-button:vertical {
        display:none
    }
    ::-webkit-scrollbar-corner,::-webkit-scrollbar-track {
        background-color:#e2e2e2
    }
    ::-webkit-scrollbar-thumb {
        border-radius:0;
        background-color:rgba(0,0,0,.3)
    }
    ::-webkit-scrollbar-thumb:vertical:hover {
        background-color:rgba(0,0,0,.35)
    }
    ::-webkit-scrollbar-thumb:vertical:active {
        background-color:rgba(0,0,0,.38)
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
<#assign fixed>
    <#switch unit?upper_case>
        <#case 'SQUARE'>0.##<#break>
        <#case 'ACRES'>0.####<#break>
        <#case 'HECTARE'>0.####<#break>
        <#default>0.##
    </#switch>
</#assign>
<#--??????-->
<div class="container header">
    <h3>
    <span class="icon icon-dashboard" />&nbsp;
    <#if data?size lte 1>
        ${data[0].year!}
    </#if>
    ????????????????????????????????????
    </h3>
    <div class="pull-right">
        <h5 style="<#if data?size gt 1>
                margin-right: auto
        <#else>
                margin-right: 30px
        </#if>">??????:
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
    <#if data?size gt 0>
        <#if data?size gt 1>
        <div class="col col-lg-2 col-md-2 col-sm-2 col-xs-2">
            <ul class="nav nav-pills nav-stacked scroll-fixed" role="tablist">
            <#list data as dataItem>
                <li class="text-center <#if dataItem_index==0>active</#if>">
                    <a href="#content${dataItem.year}" data-toggle="tab">${dataItem.year!}???</a>
                </li>
            </#list>
            </ul>
        </div>
        </#if>
        <div class="tab-content col <#if data?size == 1>col-lg-12 col-md-12 col-sm-12 col-xs-12<#else>col-lg-10 col-md-10 col-sm-10 col-xs-10</#if> ">
            <#list data as dataRow>
                <div id="content${dataRow.year}" class="tab-pane fade in <#if dataRow_index==0>active</#if>">
                    <#if dataRow.reportData??>
                        <#assign reportData=dataRow.reportData>
                        <div class="btn-group pull-right btn-options">
                            <button type="button" class="btn btn-default active" title="????????????" data-type="basic" data-year="${dataRow.year!}">
                                <span class="icon icon-list icon-large"></span>
                            </button>
                            <button type="button" class="btn btn-default" title="????????????" data-type="func" data-year="${dataRow.year!}">
                                <span class="icon icon-columns icon-large"></span>
                            </button>
                        </div>
                    </#if>
                    <#if dataRow.analysisData??>
                        <#assign analysisData=dataRow.analysisData>
                            <#if dataRow.analysisTotal??>
                                <#assign analysisTotal=dataRow.analysisTotal>
                                <#assign report = dataRow.reportData>
                            </#if>
                        <#--???????????????????????????-->
                        <#if analysisData?size gt 0>
                            <#if analysisData[0]??>
                                <#assign firstA=analysisData[0].categoryA>
                                <#assign firstB=analysisData[0].categoryB>
                            </#if>
                            <div id="basicContainer${dataRow.year!}" class="container basic-container">
                                <div class="row">
                                    <div>
                                        <ul class="nav nav-tabs">
                                            <li class="active"><a href="#tab${dataRow.year!}1" data-toggle="tab">????????????</a></li>
                                            <li><a href="#tab${dataRow.year!}2" data-toggle="tab">????????????</a></li>
                                            <#if firstB??>
                                                <#if firstB?keys?seq_contains('?????????') >
                                                    <li><a href="#tab${dataRow.year!}3" data-toggle="tab">?????????</a></li>
                                                </#if>
                                            </#if>
                                        </ul>
                                    </div>
                                    <div class="tab-content">
                                        <div id="tab${dataRow.year!}1" class="tab-pane fade in active">
                                            <#if firstA??>
                                                <table>
                                                    <tr>
                                                        <th>????????????</th>
                                                        <th>??????</th>
                                                        <#list firstA as infoItem>
                                                            <#if infoItem["dlbm"]?contains(',')><#else >
                                                                <th>${infoItem["dlmc"]!}
                                                                    (${infoItem["dlbm"]!})
                                                                </th></#if>
                                                        </#list>
                                                    </tr>
                                                    <#list analysisData as xzq>
                                                        <tr>
                                                            <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                                            <td>${xzq["sumArea"]!?string(fixed)}</td>
                                                            <#assign areaInfo=xzq.categoryA>
                                                            <#list areaInfo as item>
                                                                <#if item["dlbm"]?contains(',')><#else >
                                                                    <td>${item["area"]!?string(fixed)}</td></#if>
                                                            </#list>
                                                        </tr>
                                                    </#list>
                                                </table>
                                            <#else >???????????????
                                            </#if>
                                        </div>
                                        <div id="tab${dataRow.year!}2" class="tab-pane fade">
                                            <#if firstB??>
                                                <table>
                                                    <tr>
                                                        <th rowspan="3">????????????</th>
                                                        <th rowspan="3">??????</th>
                                                        <th rowspan="3">??????(01)</th>
                                                        <th colspan="6">??????</th>
                                                        <th rowspan="3">??????(02)</th>
                                                        <th colspan="6">??????</th>
                                                        <th rowspan="3">??????(03)</th>
                                                        <th colspan="6">??????</th>
                                                        <th rowspan="3">??????(04)</th>
                                                        <th colspan="6">??????</th>
                                                        <th rowspan="3">????????????????????????(20)</th>
                                                        <th colspan="10">??????</th>
                                                        <th rowspan="3">??????????????????(10)</th>
                                                        <th colspan="12">??????</th>
                                                        <th rowspan="3">???????????????????????????(11)</th>
                                                        <th colspan="18">??????</th>
                                                        <th rowspan="3">????????????</th>
                                                        <th colspan="12">??????</th>
                                                    </tr>
                                                    <tr>
                                                        <th colspan="2">??????(011)</th>
                                                        <th colspan="2">?????????(012)</th>
                                                        <th colspan="2">??????(013)</th>
                                                        <th colspan="2">??????(021)</th>
                                                        <th colspan="2">??????(022)</th>
                                                        <th colspan="2">????????????(023)</th>
                                                        <th colspan="2">?????????(031)</th>
                                                        <th colspan="2">????????????(032)</th>
                                                        <th colspan="2">????????????(033)</th>
                                                        <th colspan="2">???????????????(041)</th>
                                                        <th colspan="2">???????????????(042)</th>
                                                        <th colspan="2">????????????(043)</th>
                                                        <th colspan="2">??????(201)</th>
                                                        <th colspan="2">?????????(202)</th>
                                                        <th colspan="2">??????(203)</th>
                                                        <th colspan="2">????????????(204)</th>
                                                        <th colspan="2">???????????????????????????(205)</th>
                                                        <th colspan="2">????????????(101)</th>
                                                        <th colspan="2">????????????(102)</th>
                                                        <th colspan="2">????????????(104)</th>
                                                        <th colspan="2">????????????(105)</th>
                                                        <th colspan="2">??????????????????(106)</th>
                                                        <th colspan="2">??????????????????(107)</th>
                                                        <th colspan="2">????????????(111)</th>
                                                        <th colspan="2">????????????(112)</th>
                                                        <th colspan="2">????????????(113)</th>
                                                        <th colspan="2">????????????(114)</th>
                                                        <th colspan="2">????????????(115)</th>
                                                        <th colspan="2">????????????(116)</th>
                                                        <th colspan="2">??????(117)</th>
                                                        <th colspan="2">??????????????????(118)</th>
                                                        <th colspan="2">?????????????????????(119)</th>
                                                        <th colspan="2">???????????????(122)</th>
                                                        <th colspan="2">??????(123)</th>
                                                        <th colspan="2">?????????(124)</th>
                                                        <th colspan="2">?????????(125)</th>
                                                        <th colspan="2">??????(126)</th>
                                                        <th colspan="2">??????(127)</th>
                                                    </tr>
                                                    <tr>
                                                        <#list 1..38 as i>
                                                        <th>??????</th>
                                                        <th>??????</th>
                                                        </#list>
                                                    </tr>
                                                    <#list analysisData as xzq>
                                                        <#assign item=xzq.categoryB>
                                                        <tr>
                                                            <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                                            <td>${xzq["sumArea"]!?string(fixed)}</td>
                                                            <td>${item["01"]!?string(fixed)}</td>
                                                            <td><#if item["011_gy"]??>${item["011_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["011_jt"]??>${item["011_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["012_gy"]??>${item["012_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["012_jt"]??>${item["012_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["013_gy"]??>${item["013_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["013_jt"]??>${item["013_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["02"]!?string(fixed)}</td>
                                                            <td><#if item["021_gy"]??>${item["021_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["021_jt"]??>${item["021_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["022_gy"]??>${item["022_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["022_jt"]??>${item["022_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["023_gy"]??>${item["023_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["023_jt"]??>${item["023_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["03"]!?string(fixed)}</td>
                                                            <td><#if item["031_gy"]??>${item["031_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["031_jt"]??>${item["031_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["032_gy"]??>${item["032_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["032_jt"]??>${item["032_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["033_gy"]??>${item["033_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["033_jt"]??>${item["033_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["04"]!?string(fixed)}</td>
                                                            <td><#if item["041_gy"]??>${item["041_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["041_jt"]??>${item["041_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["042_gy"]??>${item["042_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["042_jt"]??>${item["042_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["043_gy"]??>${item["043_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["043_jt"]??>${item["043_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["20"]!?string(fixed)}</td>
                                                            <td><#if item["201_gy"]??>${item["201_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["201_jt"]??>${item["201_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["202_gy"]??>${item["202_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["202_jt"]??>${item["202_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["203_gy"]??>${item["203_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["203_jt"]??>${item["203_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["204_gy"]??>${item["204_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["204_jt"]??>${item["204_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["205_gy"]??>${item["205_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["205_jt"]??>${item["205_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["10"]!?string(fixed)}</td>
                                                            <td><#if item["101_gy"]??>${item["101_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["101_jt"]??>${item["101_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["102_gy"]??>${item["102_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["102_jt"]??>${item["102_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["104_gy"]??>${item["104_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["104_jt"]??>${item["104_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["105_gy"]??>${item["105_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["105_jt"]??>${item["105_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["106_gy"]??>${item["106_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["106_jt"]??>${item["106_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["107_gy"]??>${item["107_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["107_jt"]??>${item["107_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["11"]!?string(fixed)}</td>
                                                            <td><#if item["111_gy"]??>${item["111_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["111_jt"]??>${item["111_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["112_gy"]??>${item["112_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["112_jt"]??>${item["112_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["113_gy"]??>${item["113_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["113_jt"]??>${item["113_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["114_gy"]??>${item["114_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["114_jt"]??>${item["114_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["115_gy"]??>${item["115_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["115_jt"]??>${item["115_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["116_gy"]??>${item["116_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["116_jt"]??>${item["116_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["117_gy"]??>${item["117_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["117_jt"]??>${item["117_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["118_gy"]??>${item["118_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["118_jt"]??>${item["118_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["119_gy"]??>${item["119_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["119_jt"]??>${item["119_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td>${item["12"]!?string(fixed)}</td>
                                                            <td><#if item["122_gy"]??>${item["122_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["122_jt"]??>${item["122_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["123_gy"]??>${item["123_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["123_jt"]??>${item["123_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["124_gy"]??>${item["124_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["124_jt"]??>${item["124_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["125_gy"]??>${item["125_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["125_jt"]??>${item["125_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["126_gy"]??>${item["126_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["126_jt"]??>${item["126_jt"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["127_gy"]??>${item["127_gy"]!?string(fixed)}<#else >0</#if></td>
                                                            <td><#if item["127_jt"]??>${item["127_jt"]!?string(fixed)}<#else >0</#if></td>
                                                        </tr>
                                                    </#list>
                                                </table>
                                            <#else >???????????????</#if>
                                        </div>
                                        <#if firstB??&&(firstB?keys?seq_contains('?????????')) >
                                            <div id="tab${dataRow.year!}3" class="tab-pane fade">
                                                <#if firstB??>
                                                    <table>
                                                        <tr>
                                                            <th>????????????</th>
                                                            <th>??????</th>
                                                            <th>?????????</th>
                                                            <th>????????????</th>
                                                            <th>????????????</th>
                                                        </tr>
                                                        <#list analysisData as xzq>
                                                            <tr>
                                                                <#assign itemB=xzq.categoryB>
                                                                <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                                                <td>${xzq["sumArea"]!?string(fixed)}</td>
                                                                <td><#if firstB?keys?seq_contains('?????????') >${itemB["?????????"]!?string(fixed)}</#if></td>
                                                                <td><#if firstB?keys?seq_contains('????????????') >${itemB["????????????"]!?string(fixed)}</#if></td>
                                                                <td><#if firstB?keys?seq_contains('????????????') >${itemB["????????????"]!?string(fixed)}</#if></td>
                                                            </tr>
                                                        </#list>
                                                    </table>
                                                </#if>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                                <div class="export-container text-center">
                                    <a class="btn btn-primary" data-excel='${dataRow.exportXls!}' onclick="exportExcel(this,0);">?????? excel</a>
                                    <#if dataRow.shpId??>
                                        <div class="btn-group">
                                            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                ???????????? <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li><a data-shpid='${dataRow.shpId!}' onclick='exportFeatures(this,0);'>shp?????????(.zip)</a></li>
                                                <li><a data-shpid='${dataRow.shpId!}' onclick='exportFeatures(this,1);'>cad??????(.dwg)</a></li>
                                            </ul>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                            <#if report??>
                                <div id="funcContainer${dataRow.year!}" class="container func-container" style="width: 80%;">
                                    <div class="panel panel-primary" style="margin-top:20px; ">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">????????????????????????????????????</h3>
                                        </div>
                                        <div class="panel-body">
                                            <form class="form-horizontal" role="form">
                                                <div class="form-group">
                                                    <label class="col-sm-1 control-label">???????????????</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled" value="${report.rArea!?string(fixed)}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">???????????????</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled" value="${analysisTotal.sumArea!?string(fixed)}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">????????????</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled"
                                                               value="${analysisTotal.sumAreaGy!?string(fixed)}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">????????????</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled"
                                                               value="${analysisTotal.sumAreaJt!?string(fixed)}">
                                                    </div>
                                                </div>
                                                <div class="form-group" style="text-align: center;">
                                                    <label class="col-sm-2 control-label">??????</label>

                                                    <div class="col-sm-3">
                                                        <div class="form-control label-success">
                        <span style="color: white;"><#if (report.sumResult &gt;0)>
                            ???&nbsp;<#elseif (report.sumResult &lt;0)>
                            ???&nbsp;</#if>${(env.absDouble(report.sumResult))!?string(fixed)} m<sup>2</sup></span>
                                                        </div>
                                                    </div>
                                                    <label class="col-sm-2 control-label">??????</label>

                                                    <div class="col-sm-3">
                                                        <div class="form-control label-warning">
                                                            <span style="color: white;">${(env.absDouble(report.sumMistake))!?string(fixed)}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div style="width: 320px;float: left;padding-right: 20px;">
                                            <div class="panel panel-primary">
                                                <div class="panel-heading">
                                                    <h3 class="panel-title">?????????</h3>
                                                </div>
                                                <div class="panel-body">
                                                    <table class="infoTable">
                                                        <tr>
                                                            <th>???????????????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${report.rNydArea!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.nydResult &gt;0)>???<#elseif (report.nydResult &lt;0)>???</#if>${(env.absDouble(report.nydResult))!?string(fixed)} ???">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.nydMistake))!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="panel panel-primary">
                                                <div class="panel-heading">
                                                    <h3 class="panel-title">??????</h3>
                                                </div>
                                                <div class="panel-body">
                                                    <table class="infoTable">
                                                        <tr>
                                                            <th>???????????????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${report.rGdArea!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.gdResult &gt;0)>???<#elseif (report.gdResult &lt;0)>???</#if>${(env.absDouble(report.gdResult))!?string(fixed)} ???">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.gdMistake))!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="panel panel-primary">
                                                <div class="panel-heading">
                                                    <h3 class="panel-title">????????????</h3>
                                                </div>
                                                <div class="panel-body">

                                                    <table class="infoTable">
                                                        <tr>
                                                            <th>???????????????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${report.rJsydArea!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.jsydResult &gt;0)>???<#elseif (report.jsydResult &lt;0)>???</#if>${(env.absDouble(report.jsydResult))!?string(fixed)} ???">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.jsydMistake))!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="panel panel-primary">
                                                <div class="panel-heading">
                                                    <h3 class="panel-title">????????????</h3>
                                                </div>
                                                <div class="panel-body">
                                                    <table class="infoTable">
                                                        <tr>
                                                            <th>???????????????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${report.rWlydArea!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.wlydResult &gt;0)>???<#elseif (report.wlydResult &lt;0)>???</#if>${(env.absDouble(report.wlydResult))!?string(fixed)} ???">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>??????</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.wlydMistake))!?string(fixed)}">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <#if analysisTotal??>
                                            <#assign tot=analysisTotal.categoryB/>
                                        </#if>
                                        <div class="panel panel-primary" style="max-height: 1005px;overflow-y: scroll;">
                                            <div class="panel-heading">
                                                <h3 class="panel-title">??????????????????</h3>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>????????????</th>
                                                    <th>????????????</th>
                                                    <th>????????????</th>
                                                    <th>????????????</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td style="font-weight: bold">?????????</td>
                                                    <td><#if tot?keys?seq_contains("?????????")>${tot["?????????"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("?????????_gy")>${tot["?????????_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("?????????_jt")>${tot["?????????_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("01")>${tot["01"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("01_gy")>${tot["01_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("01_jt")>${tot["01_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("02")>${tot["02"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("02_gy")>${tot["02_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("02_jt")>${tot["02_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("03")>${tot["03"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("03_gy")>${tot["03_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("03_jt")>${tot["03_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>???????????????</td>
                                                    <td><#if tot?keys?seq_contains("041")>${tot["041"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("041_gy")>${tot["041_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("041_jt")>${tot["041_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>???????????????</td>
                                                    <td><#if tot?keys?seq_contains("042")>${tot["042"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("042_gy")>${tot["042_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("042_jt")>${tot["042_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????</td>
                                                    <td><#if tot?keys?seq_contains("104")>${tot["104"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("104_gy")>${tot["104_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("104_jt")>${tot["104_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????</td>
                                                    <td><#if tot?keys?seq_contains("114")>${tot["114"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("114_gy")>${tot["114_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("114_jt")>${tot["114_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("117")>${tot["117"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("117_gy")>${tot["117_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("117_jt")>${tot["117_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>???????????????</td>
                                                    <td><#if tot?keys?seq_contains("122")>${tot["122"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("122_gy")>${tot["122_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("122_jt")>${tot["122_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("123")>${tot["123"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("123_gy")>${tot["123_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("123_jt")>${tot["123_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight: bold">????????????</td>
                                                    <td><#if tot?keys?seq_contains("????????????")>${tot["????????????"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("????????????_gy")>${tot["????????????_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("????????????_jt")>${tot["????????????_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????????????????</td>
                                                    <td><#if tot?keys?seq_contains("20")>${tot["20"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("20_gy")>${tot["20_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("20_jt")>${tot["20_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????</td>
                                                    <td><#if tot?keys?seq_contains("101")>${tot["101"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("101_gy")>${tot["101_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("101_jt")>${tot["101_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????</td>
                                                    <td><#if tot?keys?seq_contains("102")>${tot["102"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("102_gy")>${tot["102_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("102_jt")>${tot["102_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>????????????</td>
                                                    <td><#if tot?keys?seq_contains("105")>${tot["105"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("105_gy")>${tot["105_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("105_jt")>${tot["105_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????????????????</td>
                                                    <td><#if tot?keys?seq_contains("106")>${tot["106"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("106_gy")>${tot["106_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("106_jt")>${tot["106_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????????????????</td>
                                                    <td><#if tot?keys?seq_contains("107")>${tot["107"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("107_gy")>${tot["107_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("107_jt")>${tot["107_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight: bold">????????????</td>
                                                    <td><#if tot?keys?seq_contains("????????????")>${tot["????????????"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("????????????_gy")>${tot["????????????_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("????????????_jt")>${tot["????????????_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("126")>${tot["126"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("126_gy")>${tot["126_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("126_jt")>${tot["126_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>??????</td>
                                                    <td><#if tot?keys?seq_contains("127")>${tot["127"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("127_gy")>${tot["127_gy"]!?string(fixed)}</#if></td>
                                                    <td><#if tot?keys?seq_contains("127_jt")>${tot["127_jt"]!?string(fixed)}</#if></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </#if>
                        <#else > <h5 class="text-warning text-center">???????????????</h5>
                    </#if>
                </div>
            </#list>
        </div>
        <#else ><h5 class="text-muted text-center">?????????</h5>
    </#if>
    </div>
</div>



<script src="/omp/static/thirdparty/jquery/jquery-scrolltofixed-min.js"></script>
<script type="text/javascript">

    var data = '${resultStr!}';

    $(document).ready(function () {

        $('.func-container').hide();
        $('.disabled').attr('disabled', true);

        $("[data-toggle='tooltip']").tooltip({
            html: true,
            trigger: 'click'
        });
        //??????????????????????????????
        $(".btn-options").find("button").on('click',function(){
            $("[data-toggle='tooltip']").tooltip('hide');
            var $this=$(this);
            $(".btn-options").find("button").removeClass('active');
            $this.addClass('active');
            var type=$this.data("type");
            var year=$this.data("year");
            if(type==="basic"){
                $('#basicContainer'+year).fadeIn("slow");
                $('#funcContainer'+year).fadeOut("fast");
            }else{
                $('#funcContainer'+year).fadeIn("slow");
                $('#basicContainer'+year).fadeOut("fast");
            }
        });
        //????????????????????????????????????
        $(".scroll-fixed").scrollToFixed({marginTop:20});

    });

    /**
     * ??????shp/dwg
     * @param type
     * */
    function exportFeatures(f,type) {
        var shpId = $(f).data('shpid');
        if (shpId == null && shpId == '') {
            alert("??????shpId?????? ????????????????????????");
            return;
        }
        var shpUrl = '${path_omp!'/omp'}/file/download/'.concat(shpId);
        var gpUrl = '${env.getEnv('dwg.exp.url')!}';
        switch (type) {
            case 0:
                window.location.target = '_blank';
                window.location.href = shpUrl;
                break;
            case 1:
                if (gpUrl == '') {
                    alert("??????dwg?????????GP????????????????????????????????????!");
                    return;
                }
                $.ajax({
                    type: 'post',
                    sync: true,
                    url: '<@com.rootPath/>/geometryService/rest/export/dwg',
                    data: {shpUrl: shpUrl, gpUrl: gpUrl},
                    success: function (_r) {
                        if (_r && _r.success == false)
                            alert(_r.msg);
                        else {
                            window.location.target = '_blank';
                            window.location.href = _r.result;
                        }
                    },
                    fail: function () {
                        alert(arguments[2]);
                    }
                });
                break;
        }
    }
    /**
     * ?????????????????????excel
     * @param type 0---???????????? 1----??????????????????
     */
    function exportExcel(f,type) {
        var data = $(f).data('excel');
        switch (type) {
            case 0:
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "tdlyxz.xml");
                break;
            case 1:
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "tdlyxz_report.xml");
                break;
        }
    }

    function openPostWindow(url, data, fileName) {
        if (data == "") {
            alert("???????????????!");
            return;
        }
        var tempForm = document.createElement("form");
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
