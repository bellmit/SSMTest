<#if (tplData?keys?size>0)>
    <#assign unit=tplData.unit! />
    <#assign totalResult=tplData.totalResult! />
    <#assign fixed>
        <#switch unit>
            <#case 'SQUARE'>0.##<#break>
            <#case 'ACRES'>0.####<#break>
            <#case 'HECTARE'>0.####<#break>
            <#default>0.##
        </#switch>
    </#assign>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab1" data-toggle="tab">一级分类</a></li>
        <li><a href="#tab2" data-toggle="tab">二级分类</a></li>
        <#if tplData.result[0]??&&(tplData.result[0].categoryB??)&&(tplData.result[0].categoryB?keys?seq_contains('农用地')) >
            <li><a href="#tab3" data-toggle="tab">三大类</a></li>
        </#if>
    </ul>
</div>
<div class="tab-content" style="overflow: auto;">
    <div id="tab1" class="tab-pane fade in active" style="overflow: auto">
        <#if (tplData.result?size>0)&&(tplData.result[0].categoryA??)>
            <table>
                <tr>
                    <th><#if tplData.level??&&tplData.level=="mas">地块名称<#else >坐落单位</#if></th>
                    <th>权属单位</th>
                    <th>合计</th>
                    <#assign info=tplData.result[0].categoryA>
                    <#list info as infoItem>
                        <#if infoItem["dlbm"]?contains(",")><#else >
                            <th <#if infoItem["dlbm"]=='12'>class="mediumWidth"</#if>>${infoItem["dlmc"]!}<#if infoItem["dlbm"]?contains(',')><#else >
                                (${infoItem["dlbm"]!})</#if></th></#if>
                    </#list>
                </tr>
                <#list tplData.result as xzq>
                    <tr>
                        <#if !xzq_has_next>
                            <td colspan="2"><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                        <#else>
                            <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                            <td>${xzq["qsdwmc"]!}</td>
                        </#if>
                        <td>${xzq["sumArea"]!?string(fixed)}</td>
                        <#assign areaInfo=xzq.categoryA>
                        <#list areaInfo as item>
                            <#if item["dlbm"]?contains(",")><#else >
                                <td>${item["area"]!?string(fixed)}</td></#if>
                        </#list>
                    </tr>
                </#list>
            </table>
        <#else ><h5>该分类下无分析结果</h5>
        </#if>
    </div>
    <div id="tab2" class="tab-pane fade" style="overflow: auto;">
        <#if (tplData.result?size>0)&&(tplData.result[0].categoryB??)>
            <#assign first=tplData.result[0].categoryB>
            <table>
                <tr>
                    <th rowspan="2"><#if tplData.level??&&tplData.level=="mas">地块名称<#else >坐落单位</#if></th>
                    <th rowspan="2">权属单位</th>
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
                    <th class="longWidth">其他园地(023)</th>
                    <th>有林地(031)</th>
                    <th class="mediumWidth">灌木林地(032)</th>
                    <th class="mediumWidth">其他林地(033)</th>
                    <th class="longWidth">天然牧草地(041)</th>
                    <th class="longWidth">人工牧草地(042)</th>
                    <th class="mediumWidth">其他草地(043)</th>
                    <th>城市(201)</th>
                    <th>建制镇(202)</th>
                    <th>村庄(203)</th>
                    <th class="mediumWidth">采矿用地(204)</th>
                    <th>风景名胜及特殊用地(205)</th>
                    <th class="mediumWidth">铁路用地(101)</th>
                    <th class="mediumWidth">公路用地(102)</th>
                    <th class="mediumWidth">农村道路(104)</th>
                    <th class="mediumWidth">机场用地(105)</th>
                    <th>港口码头用地(106)</th>
                    <th>管道运输用地(107)</th>
                    <th class="mediumWidth">河流水面(111)</th>
                    <th class="mediumWidth">湖泊水面(112)</th>
                    <th class="mediumWidth">水库水面(113)</th>
                    <th class="mediumWidth">坑塘水面(114)</th>
                    <th class="mediumWidth">沿海滩涂(115)</th>
                    <th class="mediumWidth">内陆滩涂(116)</th>
                    <th>水渠(117)</th>
                    <th>水工建筑用地(118)</th>
                    <th>冰川及永久积雪(119)</th>
                    <th class="longWidth">设施农用地(122)</th>
                    <th>田坎(123)</th>
                    <th>盐碱地(124)</th>
                    <th>沼泽地(125)</th>
                    <th>沙地(126)</th>
                    <th>裸地(127)</th>
                </tr>
                <#list tplData.result as xzq>
                    <#assign item=xzq.categoryB>
                    <tr>
                        <#if !xzq_has_next>
                            <td colspan="2"><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                        <#else>
                            <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                            <td>${xzq["qsdwmc"]!}</td>
                        </#if>
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
        <#else ><h5>该分类下无分析结果</h5></#if>
    </div>
    <#if tplData.result[0]??&&(tplData.result[0].categoryB??)&&(tplData.result[0].categoryB?keys?seq_contains('农用地')) >
        <div id="tab3" class="tab-pane fade">
            <#if (tplData.result?size>0)&&(tplData.result[0].categoryB??)>
                <#assign firstB=tplData.result[0].categoryB>
                <table>
                    <tr>
                        <th><#if tplData.level??&&tplData.level=="mas">地块名称<#else >坐落单位</#if></th>
                        <th>权属单位</th>
                        <th>合计</th>
                        <th>农用地</th>
                        <th>建设用地</th>
                        <th>未利用地</th>
                    </tr>
                    <#list tplData.result as xzq>
                        <tr>
                            <#assign itemB=xzq.categoryB>
                            <#if !xzq_has_next>
                                <td colspan="2"><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                            <#else>
                                <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                <td>${xzq["qsdwmc"]}</td>
                            </#if>
                            <td>${xzq["sumArea"]!?string(fixed)}</td>
                            <td><#if firstB?keys?seq_contains('农用地') >${itemB["农用地"]!?string(fixed)}</#if></td>
                            <td><#if firstB?keys?seq_contains('建设用地') >${itemB["建设用地"]!?string(fixed)}</#if></td>
                            <td><#if firstB?keys?seq_contains('未利用地') >${itemB["未利用地"]!?string(fixed)}</#if></td>
                        </tr>
                    </#list>
                </table>
            <#else><h5>该分类下无分析结果</h5></#if>
        </div>
    </#if>
    <div style="text-align: center;padding-top: 20px;">
        <#if tplData?keys?seq_contains('shpId')><a class="btn btn-primary" data-toggle="tooltip" data-placement="bottom"
                                                   style="margin-right: 12px;"
                                                   title="<div style='float:left;'><button class='btn btn-default' onclick=exportFeatures('${tplData.shpId!}',0);> shp</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' onclick=exportFeatures('${tplData.shpId!}',1);>dwg</button></div>">导出图形</a></#if>
    </div>
</div>
<#else >无分析结果</#if>