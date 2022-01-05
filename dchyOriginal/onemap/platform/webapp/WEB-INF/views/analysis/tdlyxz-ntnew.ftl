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
<div class="container" style="margin-top: 30px;width: 80%;">
    <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;
        <#if years?size lt 3 >
            <#list years as yearItem>
                <#if yearItem.year != "2009">
                    <#assign obj= yearItem.year!>
                ${obj!}
                </#if>
            </#list>
        </#if>土地利用现状分析结果展示</h3>
    <#if years?size gt 2>
        <ul class="nav nav-pills nav-stacked" style="margin-top:10px;margin-left:auto;width: 100px;float:left;">
            <#assign indexs = 0/>
            <#list years as item>
                <li <#if indexs==0>class="active"</#if> style="text-align: center;">
                    <#if item.year != "2009">
                        <a href="#${item.year!}" data-toggle="tab">${item.year!}</a>
                        <#assign indexs = indexs+1/>
                    </#if>
                </li>
            </#list>
        </ul>
    </#if>

    <div class="pull-right" style="margin-top:-43px;margin-right: auto;"><h5>单位:
        <#switch unit>
            <#case 'SQUARE'>平方米<#break>
            <#case 'ACRES'>亩<#break>
            <#case 'HECTARE'>公顷<#break>
            <#default>平方米
        </#switch></h5></div>
    <#assign fixed>
        <#switch unit>
            <#case 'SQUARE'>0.##<#break>
            <#case 'ACRES'>0.####<#break>
            <#case 'HECTARE'>0.####<#break>
            <#default>0.##
        </#switch>
    </#assign>
    <#list years as compareYear>
        <#if compareYear.year == "2009">
            <#assign compareItem=compareYear/>
            <#assign compareItemReport =compareItem.report!/>
        </#if>
    </#list>

    <#if years?size gt 0>
        <div class="tab-content large">
            <#assign index=0/>
            <#list years as yearItem>
                <#assign report=yearItem.report!>
                <#assign result=yearItem.result!>
                <#assign totalResult=yearItem.totalResult!>
                <#if yearItem.year != "2009">
                    <div id="${yearItem.year!}" class="tab-pane fade in <#if index==0>active</#if>">
                        <div id="resultStr${yearItem.year!}"
                             style="visibility: hidden;height: 0px;">${yearItem.resultStr!}</div>
                        <#if report?keys?size gt 0>
                            <div class="btn-group pull-right" data-toggle="buttons-radio"
                                 style="margin-top: -43px;margin-right: 70px;width: 13%;">
                                <button type="button" class="btn btn-default active basic" title="分类结果"
                                        onclick="showDiv($(this),${yearItem.year!});"><span
                                        class="icon icon-columns icon-large"
                                        style="color:#188074"></span></button>
                                <button type="button" class="btn btn-default func" title="审核对比"
                                        onclick="showDiv($(this),${yearItem.year!});"><span
                                        class="icon icon-list icon-large" style="color:#188074"></span></button>
                            </div>
                        </#if>
                    <#--分析结果展示-->
                        <#if result?size gt 0>
                            <#if result[0].analysis??>
                                <#assign firstA=result[0].analysis.categoryA!>
                                <#assign firstB=result[0].analysis.categoryB!>
                            </#if>
                            <div id="basicContainer${yearItem.year!}" class="container"
                                 style=" <#if years?size gt 2>width: 90%;<#else >width: 100%;</#if>margin-top: auto;margin-right: -15px;">
                                <div class="row" style="margin-bottom: 15px;">
                                    <ul class="nav nav-tabs">
                                        <li class="active"><a href="#tab1${yearItem.year!}"
                                                              data-toggle="tab">一级分类</a></li>
                                        <li><a href="#tab2${yearItem.year!}"
                                               data-toggle="tab">二级分类</a></li>
                                        <#if firstB??>
                                            <#if firstB?keys?seq_contains('农用地') >
                                                <li><a href="#tab3${yearItem.year!}"
                                                       data-toggle="tab">三大类</a></li>
                                            </#if>
                                        </#if>
                                    </ul>
                                    <div class="tab-content"
                                         style="margin-top: 5px;overflow-x: auto;">
                                        <div id="tab1${yearItem.year!}"
                                             class="tab-pane fade in active">
                                            <#if result?size gt 0>
                                                <#if firstA??>
                                                    <table>
                                                        <tr>
                                                            <th>地块编号</th>
                                                            <th>地块名称</th>
                                                            <th>行政区<br>名称</th>
                                                            <th>合计</th>
                                                            <#list firstA as infoItem>
                                                                <#if infoItem["dlbm"]?contains(',')><#else >
                                                                    <th>${infoItem["dlmc"]!}
                                                                        (${infoItem["dlbm"]!}
                                                                        )
                                                                    </th></#if>
                                                            </#list>
                                                        </tr>
                                                        <#list result as reItem>
                                                            <#if (reItem.analysis?size>0)>
                                                                <tr>
                                                                    <td>${reItem.dkbh!}</td>
                                                                    <td>${reItem.dkmc!}</td>
                                                                    <#assign xzq = reItem.analysis!/>
                                                                    <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                                                    <td>${xzq["sumArea"]!}{}</td>
                                                                    <#assign areaInfo=xzq.categoryA>
                                                                    <#list areaInfo as item>
                                                                        <#if item["dlbm"]?contains(',')><#else >
                                                                            <td>${item["area"]!?string(fixed)}</td></#if>
                                                                    </#list>
                                                                </tr>
                                                            </#if>
                                                        </#list>
                                                    </table>
                                                <#else >无分析结果 查看后台日志
                                                </#if>

                                            </#if>

                                        </div>
                                        <div id="tab2${yearItem.year!}" class="tab-pane fade">
                                            <#if result?size gt 0>
                                                <#if firstB??>
                                                    <table>
                                                        <tr>
                                                            <th rowspan="2">地块编号</th>
                                                            <th rowspan="2">地块名称</th>
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
                                                        <#list result as reItem>
                                                            <#if reItem.analysis??>
                                                                <tr>
                                                                    <td rowspan>${reItem.dkbh!}</td>
                                                                    <td rowspan=>${reItem.dkmc!}</td>
                                                                    <#assign xzq=reItem.analysis>
                                                                    <#assign item=xzq.categoryB>
                                                                    <td>${xzq["sumArea"]!?string(fixed)}</td>
                                                                    <td>${item["01"]!?string(fixed)}</td>
                                                                    <td><#if item["011"]??> ${item["011"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["012"]??>${item["012"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["013"]??>${item["013"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["02"]!?string(fixed)}</td>
                                                                    <td><#if item["021"]??>${item["021"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["022"]??>${item["022"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["023"]??>${item["023"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["03"]!?string(fixed)}</td>
                                                                    <td><#if item["031"]??>${item["031"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["032"]??>${item["032"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["033"]??>${item["033"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["04"]!?string(fixed)}</td>
                                                                    <td><#if item["041"]??>${item["041"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["042"]??>${item["042"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["043"]??>${item["043"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["20"]!?string(fixed)}</td>
                                                                    <td><#if item["201"]??>${item["201"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["202"]??>${item["202"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["203"]??>${item["203"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["204"]??>${item["204"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["205"]??>${item["205"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["10"]!?string(fixed)}</td>
                                                                    <td><#if item["101"]??>${item["101"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["102"]??>${item["102"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["104"]??>${item["104"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["105"]??>${item["105"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["106"]??>${item["106"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["107"]??>${item["107"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["11"]!?string(fixed)}</td>
                                                                    <td><#if item["111"]??>${item["111"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["112"]??>${item["112"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["113"]??>${item["113"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["114"]??>${item["114"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["115"]??>${item["115"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["116"]??>${item["116"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["117"]??>${item["117"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["118"]??>${item["118"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["119"]??>${item["119"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td>${item["12"]!?string(fixed)}</td>
                                                                    <td><#if item["122"]??>${item["122"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["123"]??>${item["123"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["124"]??>${item["124"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["125"]??>${item["125"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["126"]??>${item["126"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                    <td><#if item["127"]??>${item["127"]!?string(fixed)}<#else >
                                                                        0</#if></td>
                                                                </tr>
                                                            </#if>
                                                        </#list>
                                                    </table>
                                                </#if>
                                            </#if>
                                        </div>
                                        <#if firstB??&&(firstB?keys?seq_contains('农用地')) >
                                            <div id="tab3${yearItem.year!}" class="tab-pane fade">
                                                <#if result?size gt 0>
                                                    <#if firstB??>
                                                        <table>
                                                            <tr>
                                                                <th>地块编号</th>
                                                                <th>地块名称</th>
                                                                <th>合计</th>
                                                                <th>农用地</th>
                                                                <th>建设用地</th>
                                                                <th>未利用地</th>
                                                            </tr>
                                                            <#list result as reItem>
                                                                <#if reItem.analysis??>
                                                                <tr>
                                                                    <td>${reItem.dkbh!}</td>
                                                                    <td>${reItem.dkmc!}</td>
                                                                    <#assign xzq= reItem.analysis>
                                                                        <#assign itemB=xzq.categoryB>
                                                                            <td>${xzq["sumArea"]!?string(fixed)}</td>
                                                                            <td><#if firstB?keys?seq_contains('农用地') >${itemB["农用地"]!?string(fixed)}</#if></td>
                                                                            <td><#if firstB?keys?seq_contains('建设用地') >${itemB["建设用地"]!?string(fixed)}</#if></td>
                                                                            <td><#if firstB?keys?seq_contains('未利用地') >${itemB["未利用地"]!?string(fixed)}</#if></td>
                                                                    </tr>
                                                                </#if>
                                                            </#list>
                                                        </table>
                                                    </#if>
                                                </#if>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                                <div style="text-align: center;padding-top: 20px;">
                                    <a class="btn btn-primary" style="margin-right: 12px;"
                                       onclick="exportExcel(0,'${yearItem.year!}');">导出excel</a>
                                    <#if exportable><a class="btn btn-primary" data-toggle="tooltip"
                                                       data-placement="bottom"
                                                       style="margin-right: 12px;"
                                                       title="<div style='float:left;'><button class='btn btn-default' onclick='exportFeatures(0,${yearItem.year!});'> shp</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' onclick='exportFeatures(1,${yearItem.year!});'>dwg</button></div>">导出图形</a></#if>
                                </div>
                            </div>
                        <#else>
                            <div style="margin-left: 200px;margin-top: 50px;"><h5>无分析结果</h5></div>
                        </#if>
                        <#if report?keys?size gt 0>
                            <div id="funcContainer${yearItem.year!}" class="container func"
                                 style=" <#if years?size gt 2>width: 90%;<#else >width: 100%;</#if>margin-top: auto;margin-right: -15px;">
                                <h3 style="font-weight:700; text-align: center">${yearItem.year!}年度土地利用现状分析结果展示及对比</h3>

                                <table class="table-bordered">
                                    <tr>
                                        <th></th>
                                        <th>上报面积</th>
                                        <th>分析面积</th>
                                        <th>结果</th>
                                        <th>误差</th>
                                        <th>2009年误差</th>
                                    </tr>
                                    <tr>
                                        <td>总面积</td>
                                        <td>${report.rArea!?string(fixed)}</td>
                                        <td>${totalResult.sumArea!?string(fixed)}</td>
                                        <td><#if (report.sumResult &gt;0)>
                                            多&nbsp;<#elseif (report.sumResult &lt;0)>
                                            少&nbsp;</#if>${(env.absDouble(report.sumResult))!}
                                            m<sup>2</sup></td>
                                        <td>${(env.absDouble(report.sumMistake))!}</td>
                                        <td>${(env.absDouble(compareItemReport.sumMistake))!}</td>
                                    </tr>
                                    <tr>
                                        <td>集体面积</td>
                                        <td>${report.rJtArea!0}</td>
                                        <td>${totalResult.sumAreaJt!?string(fixed)}</td>
                                        <td>${report.rJtResult!0}m<sup>2</sup></td>
                                      <#--  <td><#if (report.rJtResult &gt;0)>
                                            多&nbsp;<#elseif (report.rJtResult &lt;0)>
                                            少&nbsp;</#if>${(env.absDouble(report.rJtResult))!}
                                            m<sup>2</sup></td>-->
                                        <td>${(env.absDouble(report.jtMistake))!0}</td>
                                        <td>${(env.absDouble(compareItemReport.jtMistake))!}</td>
                                    </tr>
                                    <tr>
                                        <td>国有面积</td>
                                        <td>${report.rGyArea!?string(fixed)}</td>
                                        <td>${totalResult.sumAreaGy!?string(fixed)}</td>
                                        <td><#if (report.rGyResult &gt;0)>
                                            多&nbsp;<#elseif (report.rGyResult &lt;0)>
                                            少&nbsp;</#if>${(env.absDouble(report.rGyResult))!?string(fixed)}
                                            m<sup>2</sup></td>
                                        <td>${(env.absDouble(report.gyMistake))!}</td>
                                        <td>${(env.absDouble(compareItemReport.gyMistake))!}</td>
                                    </tr>
                                    <tr></tr>
                                    <#assign localCatalogB = totalResult.categoryB!/>
                                    <tr>
                                        <td>农用地</td>
                                        <td>${report.rNydArea!?string(fixed)}</td>
                                        <td><#if localCatalogB?keys?seq_contains('农用地') >${localCatalogB["农用地"]!?string(fixed)}</#if></td>
                                        <td><#if (report.nydResult &gt;0)>多<#elseif (report.nydResult &lt;0)>
                                            少</#if>${(env.absDouble(report.nydResult))!?string(fixed)} ㎡
                                        </td>
                                        <td>${(env.absDouble(report.nydMistake))!}</td>
                                        <td>${(env.absDouble(compareItemReport.nydMistake))!}</td>
                                    </tr>
                                    <tr>
                                        <td>其中耕地</td>
                                        <td>${report.rGdArea!?string(fixed)}</td>
                                        <td><#if localCatalogB?keys?seq_contains('01') >${localCatalogB["01"]!?string(fixed)}</#if></td>
                                        <td><#if (report.gdResult &gt;0)>多<#elseif (report.gdResult &lt;0)>
                                            少</#if>${(env.absDouble(report.gdResult))!?string(fixed)} ㎡
                                        </td>
                                        <td>${(env.absDouble(report.gdMistake))!?string(fixed)}</td>
                                        <td>${(env.absDouble(compareItemReport.gdMistake))!?string(fixed)}</td>
                                    </tr>
                                    <tr>
                                        <td>建设用地</td>
                                        <td>${report.rJsydArea!?string(fixed)}</td>
                                        <td><#if localCatalogB?keys?seq_contains('建设用地') >${localCatalogB["建设用地"]!?string(fixed)}</#if></td>
                                        <td><#if (report.jsydResult &gt;0)>多<#elseif (report.jsydResult &lt;0)>
                                            少</#if>${(env.absDouble(report.jsydResult))!?string(fixed)} ㎡
                                        </td>
                                        <td>${(env.absDouble(report.jsydMistake))!}</td>
                                        <td>${(env.absDouble(compareItemReport.jsydMistake))!}</td>
                                    </tr>
                                    <tr>
                                        <td>未利用地</td>
                                        <td>${report.rWlydArea!?string(fixed)}</td>
                                        <td><#if localCatalogB?keys?seq_contains('未利用地') >${localCatalogB["未利用地"]!?string(fixed)}</#if></td>
                                        <td><#if (report.wlydResult &gt;0)>多<#elseif (report.wlydResult &lt;0)>
                                            少</#if>${(env.absDouble(report.wlydResult))!?string(fixed)} ㎡
                                        </td>
                                        <td>${(env.absDouble(report.wlydMistake))!}</td>
                                        <td>${(env.absDouble(compareItemReport.wlydMistake))!}</td>
                                    </tr>
                                </table>

                                <h3 style="font-weight:700; text-align: center;margin-top: 40px">分析结果展示</h3>

                                <div class="pull-right" style="margin-top:-43px;margin-right: auto;"><h5>单位:
                                    <#switch unit>
                                        <#case 'SQUARE'>平方米<#break>
                                        <#case 'ACRES'>亩<#break>
                                        <#case 'HECTARE'>公顷<#break>
                                        <#default>平方米
                                    </#switch></h5></div>

                                <#if totalResult??>
                                    <#assign totB=totalResult.categoryB/>
                                    <#assign totA=totalResult.categoryA/>
                                </#if>
                                <#if compareItem??>
                                    <#assign compareTotB = compareItem.totalResult.categoryB/>
                                    <#assign compareTotA = compareItem.totalResult.categoryA/>
                                </#if>

                                <table class="table-bordered">
                                    <tr>
                                        <td colspan="3" rowspan="3"></td>
                                        <td colspan="3">${yearItem.year!}年度分析</td>
                                        <td colspan="3">2009年度分析</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="2">合计</td>
                                        <td colspan="2">其中</td>
                                        <td rowspan="2">合计</td>
                                        <td colspan="2">其中</td>
                                    </tr>
                                    <tr>
                                        <td>国有面积</td>
                                        <td>集体面积</td>
                                        <td>国有面积</td>
                                        <td>集体面积</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="32" style="width: 10px">拟占用土地总面积</td>
                                        <td colspan="2">总面积</td>
                                        <td>${totalResult.sumArea!?string(fixed)}</td>
                                        <td>${totalResult.sumAreaGy!?string(fixed)}</td>
                                        <td>${totalResult.sumAreaJt!?string(fixed)}</td>
                                        <td>${compareItem.totalResult.sumArea!?string(fixed)}</td>
                                        <td>${compareItem.totalResult.sumAreaGy!?string(fixed)}</td>
                                        <td>${compareItem.totalResult.sumAreaJt!?string(fixed)}</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="11">农用地</td>
                                        <td>小计</td>
                                        <td><#if totB?keys?seq_contains('农用地') >${totB["农用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('农用地_gy') >${totB["农用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('农用地_jt') >${totB["农用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('农用地') >${compareTotB["农用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('农用地_gy') >${compareTotB["农用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('农用地_jt') >${compareTotB["农用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>耕地</td>
                                        <td><#if totB?keys?seq_contains('01') >${totB["01"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('01_gy') >${totB["01_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('01_jt') >${totB["01_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('01') >${compareTotB["01"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('01_gy') >${compareTotB["01_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('01_jt') >${compareTotB["01_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>园地</td>
                                        <td><#if totB?keys?seq_contains('02') >${totB["02"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('02_gy') >${totB["02_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('02_jt') >${totB["02_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('02') >${compareTotB["02"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('02_gy') >${compareTotB["02_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('02_jt') >${compareTotB["02_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>林地</td>
                                        <td><#if totB?keys?seq_contains('03') >${totB["03"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('03_gy') >${totB["03_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('03_jt') >${totB["03_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('03') >${compareTotB["03"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('03_gy') >${compareTotB["03_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('03_jt') >${compareTotB["03_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>天然牧草地</td>
                                        <td><#if totB?keys?seq_contains('041') >${totB["041"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('041_gy') >${totB["041_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('041_jt') >${totB["041_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('041') >${compareTotB["041"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('041_gy') >${compareTotB["041_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('041_jt') >${compareTotB["041_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>人工牧草地</td>
                                        <td><#if totB?keys?seq_contains('042') >${totB["042"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('042_gy') >${totB["042_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('042_jt') >${totB["042_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('042') >${compareTotB["042"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('042_gy') >${compareTotB["042_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('042_jt') >${compareTotB["042_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>农村道路</td>
                                        <td><#if totB?keys?seq_contains('104') >${totB["104"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('104_gy') >${totB["104_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('104_jt') >${totB["104_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('104') >${compareTotB["104"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('104_gy') >${compareTotB["104_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('104_jt') >${compareTotB["104_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>坑塘水面</td>
                                        <td><#if totB?keys?seq_contains('114') >${totB["114"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('114_gy') >${totB["114_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('114_jt') >${totB["114_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('114') >${compareTotB["114"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('114_gy') >${compareTotB["114_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('114_jt') >${compareTotB["114_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>沟渠</td>
                                        <td><#if totB?keys?seq_contains('117') >${totB["117"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('117_gy') >${totB["117_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('117_jt') >${totB["117_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('117') >${compareTotB["117"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('117_gy') >${compareTotB["117_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('117_jt') >${compareTotB["117_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>设施农用地</td>
                                        <td><#if totB?keys?seq_contains('122') >${totB["122"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('122_gy') >${totB["122_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('122_jt') >${totB["122_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('122') >${compareTotB["122"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('122_gy') >${compareTotB["122_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('122_jt') >${compareTotB["122_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>田坎</td>
                                        <td><#if totB?keys?seq_contains('123') >${totB["123"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('123_gy') >${totB["123_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('123_jt') >${totB["123_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('123') >${compareTotB["123"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('123_gy') >${compareTotB["123_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('123_jt') >${compareTotB["123_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>

                                    <tr>
                                        <td rowspan="9">建设用地</td>
                                        <td>小计</td>
                                        <td><#if totB?keys?seq_contains('建设用地') >${totB["建设用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('建设用地_gy') >${totB["建设用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('建设用地_jt') >${totB["建设用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('建设用地') >${compareTotB["建设用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('建设用地_gy') >${compareTotB["建设用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('建设用地_jt') >${compareTotB["建设用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>城镇及工矿用地</td>
                                        <td><#if totB?keys?seq_contains('20') >${totB["20"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('20_gy') >${totB["20_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('20_jt') >${totB["20_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('20') >${compareTotB["20"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('20_gy') >${compareTotB["20_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('20_jt') >${compareTotB["20_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>铁路用地</td>
                                        <td><#if totB?keys?seq_contains('101') >${totB["101"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('101_gy') >${totB["101_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('101_jt') >${totB["101_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('101') >${compareTotB["101"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('101_gy') >${compareTotB["101_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('101_jt') >${compareTotB["101_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>公路用地</td>
                                        <td><#if totB?keys?seq_contains('102') >${totB["102"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('102_gy') >${totB["102_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('102_jt') >${totB["102_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('102') >${compareTotB["102"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('102_gy') >${compareTotB["102_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('102_jt') >${compareTotB["102_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>机场用地</td>
                                        <td><#if totB?keys?seq_contains('105') >${totB["105"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('105_gy') >${totB["105_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('105_jt') >${totB["105_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('105') >${compareTotB["105"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('105_gy') >${compareTotB["105_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('105_jt') >${compareTotB["105_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>港口码头用地</td>
                                        <td><#if totB?keys?seq_contains('106') >${totB["106"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('106_gy') >${totB["106_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('106_jt') >${totB["106_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('106') >${compareTotB["106"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('106_gy') >${compareTotB["106_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('106_jt') >${compareTotB["106_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>管道运输用地</td>
                                        <td><#if totB?keys?seq_contains('107') >${totB["107"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('107_gy') >${totB["107_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('107_jt') >${totB["107_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('107') >${compareTotB["107"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('107_gy') >${compareTotB["107_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('107_jt') >${compareTotB["107_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>水库水面</td>
                                        <td><#if totB?keys?seq_contains('113') >${totB["113"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('113_gy') >${totB["113_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('113_jt') >${totB["113_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('113') >${compareTotB["113"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('113_gy') >${compareTotB["113_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('113_jt') >${compareTotB["113_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>水工建筑用地</td>
                                        <td><#if totB?keys?seq_contains('118') >${totB["118"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('118_gy') >${totB["118_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('118_jt') >${totB["118_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('118') >${compareTotB["118"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('118_gy') >${compareTotB["118_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('118_jt') >${compareTotB["118_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>

                                    <tr>
                                        <td rowspan="11">未利用地</td>
                                        <td>小计</td>
                                        <td><#if totB?keys?seq_contains('未利用地') >${totB["未利用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('未利用地_gy') >${totB["未利用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('未利用地_jt') >${totB["未利用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('未利用地') >${compareTotB["未利用地"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('未利用地_gy') >${compareTotB["未利用地_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('未利用地_jt') >${compareTotB["未利用地_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>河流水面</td>
                                        <td><#if totB?keys?seq_contains('111') >${totB["111"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('111_gy') >${totB["111_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('111_jt') >${totB["111_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('111') >${compareTotB["111"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('111_gy') >${compareTotB["111_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('111_jt') >${compareTotB["111_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>湖泊水面</td>
                                        <td><#if totB?keys?seq_contains('112') >${totB["112"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('112_gy') >${totB["112_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('112_jt') >${totB["112_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('112') >${compareTotB["112"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('112_gy') >${compareTotB["112_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('112_jt') >${compareTotB["112_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>沿海滩涂</td>
                                        <td><#if totB?keys?seq_contains('115') >${totB["115"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('115_gy') >${totB["115_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('115_jt') >${totB["115_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('115') >${compareTotB["115"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('115_gy') >${compareTotB["115_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('115_jt') >${compareTotB["115_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>内陆滩涂</td>
                                        <td><#if totB?keys?seq_contains('116') >${totB["116"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('116_gy') >${totB["116_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('116_jt') >${totB["116_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('116') >${compareTotB["116"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('116_gy') >${compareTotB["116_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('116_jt') >${compareTotB["116_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>冰川及永久积雪</td>
                                        <td><#if totB?keys?seq_contains('119') >${totB["119"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('119_gy') >${totB["119_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('119_jt') >${totB["119_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('119') >${compareTotB["119"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('119_gy') >${compareTotB["119_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('119_jt') >${compareTotB["119_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>其他草地</td>
                                        <td><#if totB?keys?seq_contains('043') >${totB["043"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('043_gy') >${totB["043_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('043_jt') >${totB["043_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('043') >${compareTotB["043"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('043_gy') >${compareTotB["043_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('043_jt') >${compareTotB["043_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>盐碱地</td>
                                        <td><#if totB?keys?seq_contains('124') >${totB["124"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('124_gy') >${totB["124_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('124_jt') >${totB["124_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('124') >${compareTotB["124"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('124_gy') >${compareTotB["124_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('124_jt') >${compareTotB["124_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>沼泽地</td>
                                        <td><#if totB?keys?seq_contains('125') >${totB["125"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('125_gy') >${totB["125_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('125_jt') >${totB["125_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('125') >${compareTotB["125"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('125_gy') >${compareTotB["125_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('125_jt') >${compareTotB["125_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>沙地</td>
                                        <td><#if totB?keys?seq_contains('126') >${totB["126"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('126_gy') >${totB["126_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('126_jt') >${totB["126_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('126') >${compareTotB["126"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('126_gy') >${compareTotB["126_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('126_jt') >${compareTotB["126_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                    <tr>
                                        <td>裸地</td>
                                        <td><#if totB?keys?seq_contains('127') >${totB["127"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('127_gy') >${totB["127_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if totB?keys?seq_contains('127_jt') >${totB["127_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('127') >${compareTotB["127"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('127_gy') >${compareTotB["127_gy"]!?string(fixed)}<#else>
                                            0</#if></td>
                                        <td><#if compareTotB?keys?seq_contains('127_jt') >${compareTotB["127_jt"]!?string(fixed)}<#else>
                                            0</#if></td>
                                    </tr>
                                </table>
                                <div style="text-align: center;padding-top: 20px;">
                                    <a class="btn btn-primary" style="margin-right: 12px;" onclick="exportExcel(0,'${yearItem.year!}');">导出excel</a>
                                </div>
                            </div>
                        </#if>
                    </div>
                    <#assign index=index+1/>
                </#if>
            </#list>
        </div>
    </#if>
</div>

<script type="text/javascript">


    var data = '${resultStr!}';
    var shpIdsStr = '${shpIdsStr!}';//shp的zip包的在线下载id
    var reportXls = '${reportXls!}';//导出报件excel
    $(document).ready(function () {
        $('div[class~="func"]').hide();
        $('.disabled').attr('disabled', true);
        $("[data-toggle='tooltip']").tooltip({
            html: true,
            trigger: 'click'
        });
    });

    /**
     * 显示/隐藏div
     * */
    function showDiv(obj, year) {
        $("[data-toggle='tooltip']").tooltip('hide');
        if (obj.hasClass('basic')) {
            $('#basicContainer' + year).fadeIn("slow");
            $('#funcContainer' + year).fadeOut("fast");
        } else if (obj.hasClass('func')) {
            $('#funcContainer' + year).fadeIn("slow");
            $('#basicContainer' + year).fadeOut("fast");
        }
    }

    /**
     * 导出shp/dwg
     * @param type
     * */
    function exportFeatures(type, year) {
        var array = $.parseJSON('${shpFiles!}');
        $.each(array, function (idx, item) {
            if (year === item.year) {
                exportGeometry(type, item.shp);
            }
        });
    }

    /***
     *
     * */
    function exportGeometry(type, shpId) {
        var shpUrl = '${path_omp!'/omp'}/file/download/'.concat(shpId);
        var gpUrl = '${env.getEnv('dwg.gp.url')!}';
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
    function exportExcel(type, year) {
        switch (type) {
            case 0:
                var excel = $('#resultStr' + year).html();
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", excel, "tdlyxz-nt.xml");
                break;
            case 1:
                openPostWindow("<@com.rootPath/>/geometryService/export/analysis", reportXls, "tdlyxz_report.xml");
                break;
        }
    }

    /**
     * 审核信息导出exce
     * @param year
     */
    function exportExcel(year){

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
