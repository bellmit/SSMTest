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
    .nav-tabs{
        height: 40px !important;
    }

    h3, h5 {
        font-weight: normal;
        color: #188074;
        margin-left: 10px;
    }

    .wrapper {
        margin-top: 30px;
        width: 80%;
    }

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent>
<div class="container wrapper">
    <h3><span class="icon icon-columns"></span>&nbsp;
        <#if years?size == 1>
            <#assign obj=years[0].year>
        ${obj}
        </#if>土地利用现状分析结果展示</h3>
    </h3>
    <div class="pull-right" style="margin-top:-43px;
        <#if years?size == 1>
                margin-right: 30px
        <#else>
                margin-right: auto
        </#if>
    ;"><h5>单位:
        <#switch unit>
            <#case 'SQUARE'>平方米<#break>
            <#case 'ACRES'>亩<#break>
            <#case 'HECTARE'>公顷<#break>
            <#default>平方米
        </#switch></h5>
    </div>
</div>
    <#assign fixed>
        <#switch unit>
            <#case 'SQUARE'>0.##<#break>
            <#case 'ACRES'>0.####<#break>
            <#case 'HECTARE'>0.####<#break>
            <#default>0.##
        </#switch>
    </#assign>

    <#if report??>
    <div class="btn-group pull-right" data-toggle="buttons-radio"
         style="margin-top: -43px;margin-right: 130px;width: 15%;">
        <button type="button" class="btn btn-default active basic" title="分类结果" onclick="showDiv($(this));"><span
                class="icon icon-columns icon-large"
                style="color:#188074"></span></button>
        <button type="button" class="btn btn-default func" title="审核对比" onclick="showDiv($(this));"><span
                class="icon icon-list icon-large" style="color:#188074"></span></button>
    </div>
    </#if>
    <#if result?size gt 0>
        <#if result[0]??>
            <#assign firstA=result[0].categoryA>
            <#assign firstB=result[0].categoryB>
        </#if>
    </#if>

<div id="basicContainer" class="container" style="width: 80%;">
    <div class="row" style="margin-bottom: 15px;">
        <div>
            <ul class="nav nav-tabs">
                <li class="active"><a href="#tab1" data-toggle="tab">一级分类</a></li>
                <li><a href="#tab2" data-toggle="tab">二级分类</a></li>
                <#if firstB??>
                    <#if firstB?keys?seq_contains('农用地') >
                        <li><a href="#tab3" data-toggle="tab">三大类</a></li>
                    </#if>
                </#if>
            </ul>
        </div>
        <div class="tab-content" style="margin-top: 5px;overflow-x: auto;">
            <div id="tab1" class="tab-pane fade in active">
                <#if (result?size>0)>
                    <#if firstA??>
                        <table>
                            <tr>
                                <th>坐落单位</th>
                                <th>合计</th>
                                <#list firstA as infoItem>
                                    <#if infoItem["dlbm"]?contains(',')><#else >
                                        <th>${infoItem["dlmc"]!}
                                            (${infoItem["dlbm"]!})
                                        </th></#if>
                                </#list>
                            </tr>
                            <#list result as xzq>
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
                    <#else >无分析结果 查看后台日志
                    </#if>
                <#else ><h5>该分类下无分析结果</h5>
                </#if>
            </div>
            <div id="tab2" class="tab-pane fade">
                <#if (result?size>0)>
                    <#if firstB??>
                        <table>
                            <tr>
                                <th rowspan="2">坐落单位</th>
                                <th rowspan="2">合计</th>
                                <th rowspan="2">耕地(01)</th>
                                <th colspan="3">其中</th>
                                <th rowspan="2">园地(02)</th>
                                <th colspan="3">其中</th>
                                <th rowspan="2">林地(03)</th>
                                <th colspan="3">其中</th>
                                <th rowspan="2">草地(04)</th>
                                <th colspan="3">其中</th>
                                <th rowspan="2">城镇村及工矿用地(20)</th>
                                <th colspan="5">其中</th>
                                <th rowspan="2">交通运输用地(10)</th>
                                <th colspan="6">其中</th>
                                <th rowspan="2">水域及水利设施用地(11)</th>
                                <th colspan="9">其中</th>
                                <th rowspan="2">其他用地</th>
                                <th colspan="6">其中</th>
                            </tr>
                            <tr>
                                <th>水田(011)</th>
                                <th>水浇地(012)</th>
                                <th>旱地(013)</th>
                                <th>果园(021)</th>
                                <th>茶园(022)</th>
                                <th>其他园地(023)</th>
                                <th>有林地(031)</th>
                                <th>灌木林地(032)</th>
                                <th>其他林地(033)</th>
                                <th>天然牧草地(041)</th>
                                <th>人工牧草地(042)</th>
                                <th>其他草地(043)</th>
                                <th>城市(201)</th>
                                <th>建制镇(202)</th>
                                <th>村庄(203)</th>
                                <th>采矿用地(204)</th>
                                <th>风景名胜及特殊用地(205)</th>
                                <th>铁路用地(101)</th>
                                <th>公路用地(102)</th>
                                <th>农村道路(104)</th>
                                <th>机场用地(105)</th>
                                <th>港口码头用地(106)</th>
                                <th>管道运输用地(107)</th>
                                <th>河流水面(111)</th>
                                <th>湖泊水面(112)</th>
                                <th>水库水面(113)</th>
                                <th>坑塘水面(114)</th>
                                <th>沿海滩涂(115)</th>
                                <th>内陆滩涂(116)</th>
                                <th>水渠(117)</th>
                                <th>水工建筑用地(118)</th>
                                <th>冰川及永久积雪(119)</th>
                                <th>设施农用地(122)</th>
                                <th>田坎(123)</th>
                                <th>盐碱地(124)</th>
                                <th>沼泽地(125)</th>
                                <th>沙地(126)</th>
                                <th>裸地(127)</th>
                            </tr>
                            <#list result as xzq>
                                <#assign item=xzq.categoryB>
                                <tr>
                                    <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                    <td>${xzq["sumArea"]!?string(fixed)}</td>
                                    <td>${item["01"]!?string(fixed)}</td>
                                    <td><#if item["011"]??> ${item["011"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["012"]??>${item["012"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["013"]??>${item["013"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["02"]!?string(fixed)}</td>
                                    <td><#if item["021"]??>${item["021"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["022"]??>${item["022"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["023"]??>${item["023"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["03"]!?string(fixed)}</td>
                                    <td><#if item["031"]??>${item["031"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["032"]??>${item["032"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["033"]??>${item["033"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["04"]!?string(fixed)}</td>
                                    <td><#if item["041"]??>${item["041"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["042"]??>${item["042"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["043"]??>${item["043"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["20"]!?string(fixed)}</td>
                                    <td><#if item["201"]??>${item["201"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["202"]??>${item["202"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["203"]??>${item["203"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["204"]??>${item["204"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["205"]??>${item["205"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["10"]!?string(fixed)}</td>
                                    <td><#if item["101"]??>${item["101"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["102"]??>${item["102"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["104"]??>${item["104"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["105"]??>${item["105"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["106"]??>${item["106"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["107"]??>${item["107"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["11"]!?string(fixed)}</td>
                                    <td><#if item["111"]??>${item["111"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["112"]??>${item["112"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["113"]??>${item["113"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["114"]??>${item["114"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["115"]??>${item["115"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["116"]??>${item["116"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["117"]??>${item["117"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["118"]??>${item["118"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["119"]??>${item["119"]!?string(fixed)}<#else >0</#if></td>
                                    <td>${item["12"]!?string(fixed)}</td>
                                    <td><#if item["122"]??>${item["122"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["123"]??>${item["123"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["124"]??>${item["124"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["125"]??>${item["125"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["126"]??>${item["126"]!?string(fixed)}<#else >0</#if></td>
                                    <td><#if item["127"]??>${item["127"]!?string(fixed)}<#else >0</#if></td>
                                </tr>
                            </#list>
                        </table>
                    <#else >无分析结果 查看后台日志</#if>
                <#else ><h5>该分类下无分析结果</h5></#if>
            </div>
            <#if firstB??&&(firstB?keys?seq_contains('农用地')) >
                <div id="tab3" class="tab-pane fade">
                    <#if (result?size>0)>
                        <#if firstB??>
                            <table>
                                <tr>
                                    <th>坐落单位</th>
                                    <th>合计</th>
                                    <th>农用地</th>
                                    <th>建设用地</th>
                                    <th>未利用地</th>
                                </tr>
                                <#list result as xzq>
                                    <tr>
                                        <#assign itemB=xzq.categoryB>
                                        <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                        <td>${xzq["sumArea"]!?string(fixed)}</td>
                                        <td><#if firstB?keys?seq_contains('农用地') >${itemB["农用地"]!?string(fixed)}</#if></td>
                                        <td><#if firstB?keys?seq_contains('建设用地') >${itemB["建设用地"]!?string(fixed)}</#if></td>
                                        <td><#if firstB?keys?seq_contains('未利用地') >${itemB["未利用地"]!?string(fixed)}</#if></td>
                                    </tr>
                                </#list>
                            </table>
                        </#if>
                    </#if>
                </div>
            </#if>
        </div>
    </div>
    <div style="text-align: center;padding-top: 20px;">
        <a class="btn btn-primary" style="margin-right: 12px;" onclick="exportExcel(0);">导出excel</a>
        <#if exportable>
            <div class="btn-group">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    导出图形 <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a  onclick='exportFeatures(0);'>shp压缩包(.zip)</a></li>
                    <li><a  onclick='exportFeatures(1);'>cad文件(.dwg)</a></li>
                </ul>
            </div>
        </#if>
    </div>
</div>
    <#if report??>
    <div id="funcContainer" class="container" style="width: 80%;">
    <div class="panel panel-primary" style="margin-top:20px; ">
        <div class="panel-heading">
            <h3 class="panel-title">土地权属地类面积审核信息</h3>
        </div>
        <div class="panel-body">
            <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="col-sm-1 control-label">上报总面积</label>

                    <div class="col-sm-2">
                        <input type="input" class="form-control disabled" value="${report.rArea!?string(fixed)}">
                    </div>
                    <label class="col-sm-1 control-label">分析总面积</label>

                    <div class="col-sm-2">
                        <input type="input" class="form-control disabled" value="${totalResult.sumArea!?string(fixed)}">
                    </div>
                    <label class="col-sm-1 control-label">国有面积</label>

                    <div class="col-sm-2">
                        <input type="input" class="form-control disabled"
                               value="${totalResult.sumAreaGy!?string(fixed)}">
                    </div>
                    <label class="col-sm-1 control-label">集体面积</label>

                    <div class="col-sm-2">
                        <input type="input" class="form-control disabled"
                               value="${totalResult.sumAreaJt!?string(fixed)}">
                    </div>
                </div>
                <div class="form-group" style="text-align: center;">
                    <label class="col-sm-2 control-label">结果</label>

                    <div class="col-sm-2">
                        <div class="form-control label-success">
                        <span style="color: white;"><#if (report.sumResult &gt;0)>
                            多&nbsp;<#elseif (report.sumResult &lt;0)>
                            少&nbsp;</#if>${(env.absDouble(report.sumResult))!?string(fixed)} m<sup>2</sup></span>
                        </div>
                    </div>
                    <label class="col-sm-2 control-label">误差</label>

                    <div class="col-sm-2">
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
                <h3 class="panel-title">农用地</h3>
            </div>
            <div class="panel-body">
                <table class="infoTable">
                    <tr>
                        <th>上报总面积</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="${report.rNydArea!?string(fixed)}">
                        </td>
                    </tr>
                    <tr>
                        <th>结果</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="<#if (report.nydResult &gt;0)>多<#elseif (report.nydResult &lt;0)>少</#if>${(env.absDouble(report.nydResult))!?string(fixed)} ㎡">
                        </td>
                    </tr>
                    <tr>
                        <th>误差</th>
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
                <h3 class="panel-title">耕地</h3>
            </div>
            <div class="panel-body">
                <table class="infoTable">
                    <tr>
                        <th>上报总面积</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="${report.rGdArea!?string(fixed)}">
                        </td>
                    </tr>
                    <tr>
                        <th>结果</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="<#if (report.gdResult &gt;0)>多<#elseif (report.gdResult &lt;0)>少</#if>${(env.absDouble(report.gdResult))!?string(fixed)} ㎡">
                        </td>
                    </tr>
                    <tr>
                        <th>误差</th>
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
                <h3 class="panel-title">建设用地</h3>
            </div>
            <div class="panel-body">

                <table class="infoTable">
                    <tr>
                        <th>上报总面积</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="${report.rJsydArea!?string(fixed)}">
                        </td>
                    </tr>
                    <tr>
                        <th>结果</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="<#if (report.jsydResult &gt;0)>多<#elseif (report.jsydResult &lt;0)>少</#if>${(env.absDouble(report.jsydResult))!?string(fixed)} ㎡">
                        </td>
                    </tr>
                    <tr>
                        <th>误差</th>
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
                <h3 class="panel-title">未利用地</h3>
            </div>
            <div class="panel-body">
                <table class="infoTable">
                    <tr>
                        <th>上报总面积</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="${report.rWlydArea!?string(fixed)}">
                        </td>
                    </tr>
                    <tr>
                        <th>结果</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="<#if (report.wlydResult &gt;0)>多<#elseif (report.wlydResult &lt;0)>少</#if>${(env.absDouble(report.wlydResult))!?string(fixed)} ㎡">
                        </td>
                    </tr>
                    <tr>
                        <th>误差</th>
                        <td>
                            <input type="input" class="form-control disabled"
                                   value="${(env.absDouble(report.wlydMistake))!?string(fixed)}">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
        <#if totalResult??>
            <#assign tot=totalResult.categoryB/>
        </#if>
    <div class="panel panel-primary" style="max-height: 1005px;overflow-y: scroll;">
        <div class="panel-heading">
            <h3 class="panel-title">详细地类面积</h3>
        </div>
        <table class="table">
            <thead>
            <tr>
                <th>地类名称</th>
                <th>合计面积</th>
                <th>国有面积</th>
                <th>集体面积</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td style="font-weight: bold">农用地</td>
                <td><#if tot?keys?seq_contains("农用地")>${tot["农用地"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("农用地_gy")>${tot["农用地_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("农用地_jt")>${tot["农用地_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>耕地</td>
                <td><#if tot?keys?seq_contains("01")>${tot["01"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("01_gy")>${tot["01_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("01_jt")>${tot["01_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>园地</td>
                <td><#if tot?keys?seq_contains("02")>${tot["02"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("02_gy")>${tot["02_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("02_jt")>${tot["02_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>林地</td>
                <td><#if tot?keys?seq_contains("03")>${tot["03"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("03_gy")>${tot["03_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("03_jt")>${tot["03_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>天然牧草地</td>
                <td><#if tot?keys?seq_contains("041")>${tot["041"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("041_gy")>${tot["041_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("041_jt")>${tot["041_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>人工牧草地</td>
                <td><#if tot?keys?seq_contains("042")>${tot["042"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("042_gy")>${tot["042_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("042_jt")>${tot["042_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>农村道路</td>
                <td><#if tot?keys?seq_contains("104")>${tot["104"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("104_gy")>${tot["104_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("104_jt")>${tot["104_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>坑塘水面</td>
                <td><#if tot?keys?seq_contains("114")>${tot["114"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("114_gy")>${tot["114_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("114_jt")>${tot["114_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>沟渠</td>
                <td><#if tot?keys?seq_contains("117")>${tot["117"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("117_gy")>${tot["117_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("117_jt")>${tot["117_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>设施农用地</td>
                <td><#if tot?keys?seq_contains("122")>${tot["122"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("122_gy")>${tot["122_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("122_jt")>${tot["122_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>田坎</td>
                <td><#if tot?keys?seq_contains("123")>${tot["123"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("123_gy")>${tot["123_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("123_jt")>${tot["123_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td style="font-weight: bold">建设用地</td>
                <td><#if tot?keys?seq_contains("建设用地")>${tot["建设用地"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("建设用地_gy")>${tot["建设用地_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("建设用地_jt")>${tot["建设用地_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>城镇村及工矿用地</td>
                <td><#if tot?keys?seq_contains("20")>${tot["20"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("20_gy")>${tot["20_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("20_jt")>${tot["20_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>铁路用地</td>
                <td><#if tot?keys?seq_contains("101")>${tot["101"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("101_gy")>${tot["101_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("101_jt")>${tot["101_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>公路用地</td>
                <td><#if tot?keys?seq_contains("102")>${tot["102"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("102_gy")>${tot["102_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("102_jt")>${tot["102_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>机场用地</td>
                <td><#if tot?keys?seq_contains("105")>${tot["105"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("105_gy")>${tot["105_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("105_jt")>${tot["105_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>港口码头用地</td>
                <td><#if tot?keys?seq_contains("106")>${tot["106"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("106_gy")>${tot["106_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("106_jt")>${tot["106_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>管道运输用地</td>
                <td><#if tot?keys?seq_contains("107")>${tot["107"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("107_gy")>${tot["107_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("107_jt")>${tot["107_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td style="font-weight: bold">未利用地</td>
                <td><#if tot?keys?seq_contains("未利用地")>${tot["未利用地"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("未利用地_gy")>${tot["未利用地_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("未利用地_jt")>${tot["未利用地_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>沙地</td>
                <td><#if tot?keys?seq_contains("126")>${tot["126"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("126_gy")>${tot["126_gy"]!?string(fixed)}</#if></td>
                <td><#if tot?keys?seq_contains("126_jt")>${tot["126_jt"]!?string(fixed)}</#if></td>
            </tr>
            <tr>
                <td>裸地</td>
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

<script type="text/javascript">

    var data = '${resultStr!}';

    var reportXls = '${reportXls!}';//导出报件excel

    var shpId = '${shpId!}';//shp的zip包的在线下载id

    $(document).ready(function () {
        $('#funcContainer').hide();
        $('.disabled').attr('disabled', true);

        $("[data-toggle='tooltip']").tooltip({
            html: true,
            trigger: 'click'
        });
    });
    /**
     * 显示/隐藏div
     * */
    function showDiv(obj) {
        $("[data-toggle='tooltip']").tooltip('hide');
        if (obj.hasClass('basic')) {
            $('#basicContainer').fadeIn("slow");
            $('#funcContainer').fadeOut("fast");
        } else if (obj.hasClass('func')) {
            $('#funcContainer').fadeIn("slow");
            $('#basicContainer').fadeOut("fast");
        }
    }

    /**
     * 导出shp/dwg
     * @param type
     * */
    function exportFeatures(type) {
        if (shpId == null && shpId == '') {
            alert("生成shpId失败 无法执行导出操作");
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
                    alert("导出dwg所需的GP服务地址为空，请检查配置!");
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
     * 导出分析结果至excel
     * @param type 0---基本样式 1----报件对比样式
     */
    function exportExcel(type) {
        switch(type){
            case 0:
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", data, "tdlyxz.xml");
                break;
            case 1:
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", reportXls, "tdlyxz_report.xml");
                break;
        }
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
