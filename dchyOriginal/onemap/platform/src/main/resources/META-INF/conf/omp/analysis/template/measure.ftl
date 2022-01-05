<#if (tplData?keys?size>0)>
    <#assign unit=tplData.unit! />
    <#assign decimal=tplData.decimal! />
    <#assign xzResult=tplData.xzResult! />
    <#assign meaResult=tplData.meaResult! />
    <#assign fixed>
        <#if decimal??>
            ${decimal}
        <#else >
            <#switch unit?upper_case>
                <#case 'SQUARE'>0.##<#break>
                <#case 'ACRES'>0.####<#break>
                <#case 'HECTARE'>0.####<#break>
                <#default>0.##
            </#switch>
        </#if>
    </#assign>
    <div class="tab-content" style="overflow: auto;">
        <div id="tab" class="tab-pane fade in active" style="overflow: auto">
            <table>
                <tr>
                    <th colspan="2" rowspan="2">被征(用)地单位</th>
                    <th colspan="2" rowspan="2"></th>
                    <th rowspan="2">权属类别</th>
                    <th rowspan="2">面积总计</th>
                    <th colspan="4">耕地(01)</th>
                    <th colspan="4">园地(02)</th>
                    <th colspan="4">林地(03)</th>
                    <th colspan="4">草地(04)</th>
                    <th colspan="6">城镇村及工矿用地(20)</th>
                    <th colspan="7">交通运输用地(10)</th>
                    <th colspan="10">水域及水利设施用地(11)</th>
                    <th colspan="7">其他用地</th>
                </tr>
                <tr>
                    <th>合计</th>
                    <th>水田(011)</th>
                    <th>水浇地(012)</th>
                    <th>旱地(013)</th>
                    <th>合计</th>
                    <th>果园(021)</th>
                    <th>茶园(022)</th>
                    <th class="longWidth">其他园地(023)</th>
                    <th>合计</th>
                    <th>有林地(031)</th>
                    <th class="mediumWidth">灌木林地(032)</th>
                    <th class="mediumWidth">其他林地(033)</th>
                    <th>合计</th>
                    <th class="longWidth">天然牧草地(041)</th>
                    <th class="longWidth">人工牧草地(042)</th>
                    <th class="mediumWidth">其他草地(043)</th>
                    <th>合计</th>
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
                    <th>合计</th>
                    <th class="mediumWidth">河流水面(111)</th>
                    <th class="mediumWidth">湖泊水面(112)</th>
                    <th class="mediumWidth">水库水面(113)</th>
                    <th class="mediumWidth">坑塘水面(114)</th>
                    <th class="mediumWidth">沿海滩涂(115)</th>
                    <th class="mediumWidth">内陆滩涂(116)</th>
                    <th>水渠(117)</th>
                    <th>合计</th>
                    <th>水工建筑用地(118)</th>
                    <th>冰川及永久积雪(119)</th>
                    <th>合计</th>
                    <th class="longWidth">设施农用地(122)</th>
                    <th>田坎(123)</th>
                    <th>盐碱地(124)</th>
                    <th>沼泽地(125)</th>
                    <th>沙地(126)</th>
                    <th>裸地(127)</th>
                </tr>
                <tr>
                    <#assign item = meaResult.categoryB>
                    <td colspan="2" rowspan="3">集体合计</td>
                    <td colspan="2">勘测成果</td>
                    <td>集体</td>
                    <td>${meaResult["sumAreaJt"]!?string(fixed)}</td>
                    <td>${item["01_jt"]!?string(fixed)}</td>
                    <td><#if item["011_jt"]??> ${item["011_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["012_jt"]??>${item["012_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["013_jt"]??>${item["013_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["02_jt"]!?string(fixed)}</td>
                    <td><#if item["021_jt"]??>${item["021_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["022_jt"]??>${item["022_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["023_jt"]??>${item["023_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["03_jt"]!?string(fixed)}</td>
                    <td><#if item["031_jt"]??>${item["031_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["032_jt"]??>${item["032_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["033_jt"]??>${item["033_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["04_jt"]!?string(fixed)}</td>
                    <td><#if item["041_jt"]??>${item["041_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["042_jt"]??>${item["042_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["043_jt"]??>${item["043_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["20"]!?string(fixed)}</td>
                    <td><#if item["201_jt"]??>${item["201_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["202_jt"]??>${item["202_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["203_jt"]??>${item["203_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["204_jt"]??>${item["204_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["205_jt"]??>${item["205_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["10_jt"]!?string(fixed)}</td>
                    <td><#if item["101_jt"]??>${item["101_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["102_jt"]??>${item["102_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["104_jt"]??>${item["104_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["105_jt"]??>${item["105_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["106_jt"]??>${item["106_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["107_jt"]??>${item["107_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["11_jt"]!?string(fixed)}</td>
                    <td><#if item["111_jt"]??>${item["111_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["112_jt"]??>${item["112_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["113_jt"]??>${item["113_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["114_jt"]??>${item["114_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["115_jt"]??>${item["115_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["116_jt"]??>${item["116_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["117_jt"]??>${item["117_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["118_jt"]??>${item["118_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["119_jt"]??>${item["119_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["12_jt"]!?string(fixed)}</td>
                    <td><#if item["122_jt"]??>${item["122_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["123_jt"]??>${item["123_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["124_jt"]??>${item["124_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["125_jt"]??>${item["125_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["126_jt"]??>${item["126_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["127_jt"]??>${item["127_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                </tr>
                <tr>
                    <#assign item = xzResult.categoryB>
                    <td colspan="2">现状数据</td>
                    <td>集体</td>
                    <td>${xzResult["sumAreaJt"]!?string(fixed)}</td>
                    <td>${item["01_jt"]!?string(fixed)}</td>
                    <td><#if item["011_jt"]??> ${item["011_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["012_jt"]??>${item["012_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["013_jt"]??>${item["013_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["02_jt"]!?string(fixed)}</td>
                    <td><#if item["021_jt"]??>${item["021_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["022_jt"]??>${item["022_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["023_jt"]??>${item["023_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["03_jt"]!?string(fixed)}</td>
                    <td><#if item["031_jt"]??>${item["031_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["032_jt"]??>${item["032_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["033_jt"]??>${item["033_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["04_jt"]!?string(fixed)}</td>
                    <td><#if item["041_jt"]??>${item["041_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["042_jt"]??>${item["042_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["043_jt"]??>${item["043_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["20"]!?string(fixed)}</td>
                    <td><#if item["201_jt"]??>${item["201_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["202_jt"]??>${item["202_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["203_jt"]??>${item["203_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["204_jt"]??>${item["204_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["205_jt"]??>${item["205_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["10_jt"]!?string(fixed)}</td>
                    <td><#if item["101_jt"]??>${item["101_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["102_jt"]??>${item["102_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["104_jt"]??>${item["104_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["105_jt"]??>${item["105_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["106_jt"]??>${item["106_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["107_jt"]??>${item["107_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["11_jt"]!?string(fixed)}</td>
                    <td><#if item["111_jt"]??>${item["111_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["112_jt"]??>${item["112_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["113_jt"]??>${item["113_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["114_jt"]??>${item["114_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["115_jt"]??>${item["115_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["116_jt"]??>${item["116_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["117_jt"]??>${item["117_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["118_jt"]??>${item["118_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["119_jt"]??>${item["119_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["12_jt"]!?string(fixed)}</td>
                    <td><#if item["122_jt"]??>${item["122_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["123_jt"]??>${item["123_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["124_jt"]??>${item["124_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["125_jt"]??>${item["125_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["126_jt"]??>${item["126_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["127_jt"]??>${item["127_jt"]!?string(fixed)}<#else >
                            0</#if></td>
                </tr>
                <tr>
                    <#assign item1 = meaResult.categoryB>
                    <#assign item2 = xzResult.categoryB>
                    <td colspan="2">偏离情况</td>
                    <td></td>
                    <td><#if xzResult["sumAreaJt"]==0>无<#else >
                        ${((meaResult["sumAreaJt"]-xzResult["sumAreaJt"])*100/xzResult["sumAreaJt"])!?string(fixed)}</#if></td>
                    <td><#if !item2["01_jt"]??>无<#elseif item2["01_jt"]!=0>
                            ${((item1["01_jt"]-item2["01_jt"])*100/item2["01_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["011_jt"]??>无<#elseif item2["011_jt"]!=0>
                            ${((item1["011_jt"]-item2["011_jt"])*100/item2["011_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["012_jt"]??>无<#elseif item2["012_jt"]!=0>
                            ${((item1["012_jt"]-item2["012_jt"])*100/item2["012_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["013_jt"]??>无<#elseif item2["013_jt"]!=0>
                            ${((item1["013_jt"]-item2["013_jt"])*100/item2["013_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["02_jt"]??>无<#elseif item2["02_jt"]!=0>
                            ${((item1["02_jt"]-item2["02_jt"])*100/item2["02_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["021_jt"]??>无<#elseif item2["021_jt"]!=0>
                            ${((item1["021_jt"]-item2["021_jt"])*100/item2["021_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["022_jt"]??>无<#elseif item2["022_jt"]!=0>
                            ${((item1["022_jt"]-item2["022_jt"])*100/item2["022_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["023_jt"]??>无<#elseif item2["023_jt"]!=0>
                            ${((item1["023_jt"]-item2["023_jt"])*100/item2["023_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["03_jt"]??>无<#elseif item2["03_jt"]!=0>
                            ${((item1["03_jt"]-item2["03_jt"])*100/item2["03_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["031_jt"]??>无<#elseif item2["031_jt"]!=0>
                            ${((item1["031_jt"]-item2["031_jt"])*100/item2["031_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["032_jt"]??>无<#elseif item2["032_jt"]!=0>
                            ${((item1["032_jt"]-item2["032_jt"])*100/item2["032_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["033_jt"]??>无<#elseif item2["033_jt"]!=0>
                            ${((item1["033_jt"]-item2["033_jt"])*100/item2["033_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["04_jt"]??>无<#elseif item2["04_jt"]!=0>
                            ${((item1["04_jt"]-item2["04_jt"])*100/item2["04_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["041_jt"]??>无<#elseif item2["041_jt"]!=0>
                            ${((item1["041_jt"]-item2["041_jt"])*100/item2["041_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["042_jt"]??>无<#elseif item2["042_jt"]!=0>
                            ${((item1["042_jt"]-item2["042_jt"])*100/item2["042_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["043_jt"]??>无<#elseif item2["043_jt"]!=0>
                            ${((item1["043_jt"]-item2["043_jt"])*100/item2["043_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["20_jt"]??>无<#elseif item2["20_jt"]!=0>
                            ${((item1["20_jt"]-item2["20_jt"])*100/item2["20_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["201_jt"]??>无<#elseif item2["201_jt"]!=0>
                            ${((item1["201_jt"]-item2["201_jt"])*100/item2["201_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["202_jt"]??>无<#elseif item2["202_jt"]!=0>
                            ${((item1["202_jt"]-item2["202_jt"])*100/item2["202_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["203_jt"]??>无<#elseif item2["203_jt"]!=0>
                            ${((item1["203_jt"]-item2["203_jt"])*100/item2["203_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["204_jt"]??>无<#elseif item2["204_jt"]!=0>
                            ${((item1["204_jt"]-item2["204_jt"])*100/item2["204_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["205_jt"]??>无<#elseif item2["20_jt"]!=0>
                            ${((item1["205_jt"]-item2["205_jt"])*100/item2["205_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["10_jt"]??>无<#elseif item2["10_jt"]!=0>
                            ${((item1["10_jt"]-item2["10_jt"])*100/item2["10_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["101_jt"]??>无<#elseif item2["101_jt"]!=0>
                            ${((item1["101_jt"]-item2["101_jt"])*100/item2["101_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["102_jt"]??>无<#elseif item2["102_jt"]!=0>
                            ${((item1["102_jt"]-item2["102_jt"])*100/item2["102_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["104_jt"]??>无<#elseif item2["104_jt"]!=0>
                            ${((item1["104_jt"]-item2["104_jt"])*100/item2["104_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["105_jt"]??>无<#elseif item2["105_jt"]!=0>
                            ${((item1["105_jt"]-item2["105_jt"])*100/item2["105_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["106_jt"]??>无<#elseif item2["106_jt"]!=0>
                            ${((item1["106_jt"]-item2["106_jt"])*100/item2["106_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["107_jt"]??>无<#elseif item2["107_jt"]!=0>
                            ${((item1["107_jt"]-item2["107_jt"])*100/item2["107_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["11_jt"]??>无<#elseif item2["11_jt"]!=0>
                            ${((item1["11_jt"]-item2["11_jt"])*100/item2["11_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["111_jt"]??>无<#elseif item2["111_jt"]!=0>
                            ${((item1["111_jt"]-item2["111_jt"])*100/item2["111_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["112_jt"]??>无<#elseif item2["112_jt"]!=0>
                            ${((item1["112_jt"]-item2["112_jt"])*100/item2["112_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["113_jt"]??>无<#elseif item2["113_jt"]!=0>
                            ${((item1["113_jt"]-item2["113_jt"])*100/item2["113_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["114_jt"]??>无<#elseif item2["114_jt"]!=0>
                            ${((item1["114_jt"]-item2["114_jt"])*100/item2["114_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["115_jt"]??>无<#elseif item2["115_jt"]!=0>
                            ${((item1["115_jt"]-item2["115_jt"])*100/item2["115_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["116_jt"]??>无<#elseif item2["116_jt"]!=0>
                            ${((item1["116_jt"]-item2["116_jt"])*100/item2["116_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["117_jt"]??>无<#elseif item2["117_jt"]!=0>
                            ${((item1["117_jt"]-item2["117_jt"])*100/item2["117_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["118_jt"]??>无<#elseif item2["118_jt"]!=0>
                            ${((item1["118_jt"]-item2["118_jt"])*100/item2["118_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["119_jt"]??>无<#elseif item2["119_jt"]!=0>
                            ${((item1["119_jt"]-item2["119_jt"])*100/item2["119_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["12_jt"]??>无<#elseif item2["12_jt"]!=0>
                            ${((item1["12_jt"]-item2["12_jt"])*100/item2["12_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["122_jt"]??>无<#elseif item2["122_jt"]!=0>
                            ${((item1["122_jt"]-item2["122_jt"])*100/item2["122_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["123_jt"]??>无<#elseif item2["123_jt"]!=0>
                            ${((item1["123_jt"]-item2["123_jt"])*100/item2["123_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["124_jt"]??>无<#elseif item2["124_jt"]!=0>
                            ${((item1["124_jt"]-item2["124_jt"])*100/item2["124_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["125_jt"]??>无<#elseif item2["125_jt"]!=0>
                            ${((item1["125_jt"]-item2["125_jt"])*100/item2["125_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["126_jt"]??>无<#elseif item2["126_jt"]!=0>
                            ${((item1["126_jt"]-item2["126_jt"])*100/item2["126_jt"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["127_jt"]??>无<#elseif item2["127_jt"]!=0>
                            ${((item1["127_jt"]-item2["127_jt"])*100/item2["127_jt"])!?string(fixed)}<#else >无</#if></td>
                </tr>
                <tr>
                    <#assign item = meaResult.categoryB>
                    <td colspan="2" rowspan="3">国有合计</td>
                    <td colspan="2">勘测成果</td>
                    <td>国有</td>
                    <td>${meaResult["sumAreaGy"]!?string(fixed)}</td>
                    <td>${item["01_gy"]!?string(fixed)}</td>
                    <td><#if item["011_gy"]??> ${item["011_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["012_gy"]??>${item["012_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["013_gy"]??>${item["013_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["02_gy"]!?string(fixed)}</td>
                    <td><#if item["021_gy"]??>${item["021_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["022_gy"]??>${item["022_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["023_gy"]??>${item["023_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["03_gy"]!?string(fixed)}</td>
                    <td><#if item["031_gy"]??>${item["031_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["032_gy"]??>${item["032_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["033_gy"]??>${item["033_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["04_gy"]!?string(fixed)}</td>
                    <td><#if item["041_gy"]??>${item["041_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["042_gy"]??>${item["042_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["043_gy"]??>${item["043_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["20_gy"]!?string(fixed)}</td>
                    <td><#if item["201_gy"]??>${item["201_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["202_gy"]??>${item["202_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["203_gy"]??>${item["203_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["204_gy"]??>${item["204_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["205_gy"]??>${item["205_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["10_gy"]!?string(fixed)}</td>
                    <td><#if item["101_gy"]??>${item["101_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["102_gy"]??>${item["102_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["104_gy"]??>${item["104_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["105_gy"]??>${item["105_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["106_gy"]??>${item["106_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["107_gy"]??>${item["107_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["11_gy"]!?string(fixed)}</td>
                    <td><#if item["111_gy"]??>${item["111_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["112_gy"]??>${item["112_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["113_gy"]??>${item["113_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["114_gy"]??>${item["114_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["115_gy"]??>${item["115_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["116_gy"]??>${item["116_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["117_gy"]??>${item["117_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["118_gy"]??>${item["118_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["119_gy"]??>${item["119_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["12_gy"]!?string(fixed)}</td>
                    <td><#if item["122_gy"]??>${item["122_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["123_gy"]??>${item["123_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["124_gy"]??>${item["124_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["125_gy"]??>${item["125_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["126_gy"]??>${item["126_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["127_gy"]??>${item["127_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                </tr>
                <tr>
                    <#assign item = xzResult.categoryB>
                    <td colspan="2">现状数据</td>
                    <td>集体</td>
                    <td>${xzResult["sumAreaGy"]!?string(fixed)}</td>
                    <td>${item["01_gy"]!?string(fixed)}</td>
                    <td><#if item["011_gy"]??> ${item["011_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["012_gy"]??>${item["012_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["013_gy"]??>${item["013_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["02_gy"]!?string(fixed)}</td>
                    <td><#if item["021_gy"]??>${item["021_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["022_gy"]??>${item["022_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["023_gy"]??>${item["023_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["03_gy"]!?string(fixed)}</td>
                    <td><#if item["031_gy"]??>${item["031_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["032_gy"]??>${item["032_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["033_gy"]??>${item["033_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["04_gy"]!?string(fixed)}</td>
                    <td><#if item["041_gy"]??>${item["041_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["042_gy"]??>${item["042_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["043_gy"]??>${item["043_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["20_gy"]!?string(fixed)}</td>
                    <td><#if item["201_gy"]??>${item["201_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["202_gy"]??>${item["202_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["203_gy"]??>${item["203_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["204_gy"]??>${item["204_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["205_gy"]??>${item["205_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["10_gy"]!?string(fixed)}</td>
                    <td><#if item["101_gy"]??>${item["101_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["102_gy"]??>${item["102_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["104_gy"]??>${item["104_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["105_gy"]??>${item["105_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["106_gy"]??>${item["106_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["107_gy"]??>${item["107_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["11_gy"]!?string(fixed)}</td>
                    <td><#if item["111_gy"]??>${item["111_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["112_gy"]??>${item["112_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["113_gy"]??>${item["113_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["114_gy"]??>${item["114_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["115_gy"]??>${item["115_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["116_gy"]??>${item["116_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["117_gy"]??>${item["117_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["118_gy"]??>${item["118_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["119_gy"]??>${item["119_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td>${item["12_gy"]!?string(fixed)}</td>
                    <td><#if item["122_gy"]??>${item["122_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["123_gy"]??>${item["123_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["124_gy"]??>${item["124_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["125_gy"]??>${item["125_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["126_gy"]??>${item["126_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                    <td><#if item["127_gy"]??>${item["127_gy"]!?string(fixed)}<#else >
                            0</#if></td>
                </tr>
                <tr>
                    <#assign item1 = meaResult.categoryB>
                    <#assign item2 = xzResult.categoryB>
                    <td colspan="2">偏离情况</td>
                    <td></td>
                    <td><#if xzResult["sumAreaGy"]==0>无<#else >
                            ${((meaResult["sumAreaGy"]-xzResult["sumAreaGy"])*100/xzResult["sumAreaGy"])!?string(fixed)}</#if></td>
                    <td><#if !item2["01_gy"]??>无<#elseif item2["01_gy"]!=0>
                            ${((item1["01_gy"]-item2["01_gy"])*100/item2["01_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["011_gy"]??>无<#elseif item2["011_gy"]!=0>
                            ${((item1["011_gy"]-item2["011_gy"])*100/item2["011_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["012_gy"]??>无<#elseif item2["012_gy"]!=0>
                            ${((item1["012_gy"]-item2["012_gy"])*100/item2["012_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["013_gy"]??>无<#elseif item2["013_gy"]!=0>
                            ${((item1["013_gy"]-item2["013_gy"])*100/item2["013_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["02_gy"]??>无<#elseif item2["02_gy"]!=0>
                            ${((item1["02_gy"]-item2["02_gy"])*100/item2["02_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["021_gy"]??>无<#elseif item2["021_gy"]!=0>
                            ${((item1["021_gy"]-item2["021_gy"])*100/item2["021_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["022_gy"]??>无<#elseif item2["022_gy"]!=0>
                            ${((item1["022_gy"]-item2["022_gy"])*100/item2["022_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["023_gy"]??>无<#elseif item2["023_gy"]!=0>
                            ${((item1["023_gy"]-item2["023_gy"])*100/item2["023_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["03_gy"]??>无<#elseif item2["03_gy"]!=0>
                            ${((item1["03_gy"]-item2["03_gy"])*100/item2["03_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["031_gy"]??>无<#elseif item2["031_gy"]!=0>
                            ${((item1["031_gy"]-item2["031_gy"])*100/item2["031_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["032_gy"]??>无<#elseif item2["032_gy"]!=0>
                            ${((item1["032_gy"]-item2["032_gy"])*100/item2["032_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["033_gy"]??>无<#elseif item2["033_gy"]!=0>
                            ${((item1["033_gy"]-item2["033_gy"])*100/item2["033_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["04_gy"]??>无<#elseif item2["04_gy"]!=0>
                            ${((item1["04_gy"]-item2["04_gy"])*100/item2["04_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["041_gy"]??>无<#elseif item2["041_gy"]!=0>
                            ${((item1["041_gy"]-item2["041_gy"])*100/item2["041_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["042_gy"]??>无<#elseif item2["042_gy"]!=0>
                            ${((item1["042_gy"]-item2["042_gy"])*100/item2["042_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["043_gy"]??>无<#elseif item2["043_gy"]!=0>
                            ${((item1["043_gy"]-item2["043_gy"])*100/item2["043_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["20_gy"]??>无<#elseif item2["20_gy"]!=0>
                            ${((item1["20_gy"]-item2["20_gy"])*100/item2["20_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["201_gy"]??>无<#elseif item2["201_gy"]!=0>
                            ${((item1["201_gy"]-item2["201_gy"])*100/item2["201_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["202_gy"]??>无<#elseif item2["202_gy"]!=0>
                            ${((item1["202_gy"]-item2["202_gy"])*100/item2["202_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["203_gy"]??>无<#elseif item2["203_gy"]!=0>
                            ${((item1["203_gy"]-item2["203_gy"])*100/item2["203_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["204_gy"]??>无<#elseif item2["204_gy"]!=0>
                            ${((item1["204_gy"]-item2["204_gy"])*100/item2["204_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["205_gy"]??>无<#elseif item2["20_gy"]!=0>
                            ${((item1["205_gy"]-item2["205_gy"])*100/item2["205_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["10_gy"]??>无<#elseif item2["10_gy"]!=0>
                            ${((item1["10_gy"]-item2["10_gy"])*100/item2["10_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["101_gy"]??>无<#elseif item2["101_gy"]!=0>
                            ${((item1["101_gy"]-item2["101_gy"])*100/item2["101_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["102_gy"]??>无<#elseif item2["102_gy"]!=0>
                            ${((item1["102_gy"]-item2["102_gy"])*100/item2["102_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["104_gy"]??>无<#elseif item2["104_gy"]!=0>
                            ${((item1["104_gy"]-item2["104_gy"])*100/item2["104_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["105_gy"]??>无<#elseif item2["105_gy"]!=0>
                            ${((item1["105_gy"]-item2["105_gy"])*100/item2["105_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["106_gy"]??>无<#elseif item2["106_gy"]!=0>
                            ${((item1["106_gy"]-item2["106_gy"])*100/item2["106_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["107_gy"]??>无<#elseif item2["107_gy"]!=0>
                            ${((item1["107_gy"]-item2["107_gy"])*100/item2["107_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["11_gy"]??>无<#elseif item2["11_gy"]!=0>
                            ${((item1["11_gy"]-item2["11_gy"])*100/item2["11_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["111_gy"]??>无<#elseif item2["111_gy"]!=0>
                            ${((item1["111_gy"]-item2["111_gy"])*100/item2["111_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["112_gy"]??>无<#elseif item2["112_gy"]!=0>
                            ${((item1["112_gy"]-item2["112_gy"])*100/item2["112_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["113_gy"]??>无<#elseif item2["113_gy"]!=0>
                            ${((item1["113_gy"]-item2["113_gy"])*100/item2["113_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["114_gy"]??>无<#elseif item2["114_gy"]!=0>
                            ${((item1["114_gy"]-item2["114_gy"])*100/item2["114_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["115_gy"]??>无<#elseif item2["115_gy"]!=0>
                            ${((item1["115_gy"]-item2["115_gy"])*100/item2["115_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["116_gy"]??>无<#elseif item2["116_gy"]!=0>
                            ${((item1["116_gy"]-item2["116_gy"])*100/item2["116_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["117_gy"]??>无<#elseif item2["117_gy"]!=0>
                            ${((item1["117_gy"]-item2["117_gy"])*100/item2["117_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["118_gy"]??>无<#elseif item2["118_gy"]!=0>
                            ${((item1["118_gy"]-item2["118_gy"])*100/item2["118_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["119_gy"]??>无<#elseif item2["119_gy"]!=0>
                            ${((item1["119_gy"]-item2["119_gy"])*100/item2["119_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["12_gy"]??>无<#elseif item2["12_gy"]!=0>
                            ${((item1["12_gy"]-item2["12_gy"])*100/item2["12_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["122_gy"]??>无<#elseif item2["122_gy"]!=0>
                            ${((item1["122_gy"]-item2["122_gy"])*100/item2["122_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["123_gy"]??>无<#elseif item2["123_gy"]!=0>
                            ${((item1["123_gy"]-item2["123_gy"])*100/item2["123_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["124_gy"]??>无<#elseif item2["124_gy"]!=0>
                            ${((item1["124_gy"]-item2["124_gy"])*100/item2["124_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["125_gy"]??>无<#elseif item2["125_gy"]!=0>
                            ${((item1["125_gy"]-item2["125_gy"])*100/item2["125_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["126_gy"]??>无<#elseif item2["126_gy"]!=0>
                            ${((item1["126_gy"]-item2["126_gy"])*100/item2["126_gy"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["127_gy"]??>无<#elseif item2["127_gy"]!=0>
                            ${((item1["127_gy"]-item2["127_gy"])*100/item2["127_gy"])!?string(fixed)}<#else >无</#if></td>
                </tr>
                <tr>
                    <#assign item = meaResult.categoryB>
                    <td colspan="2" rowspan="3">合计</td>
                    <td colspan="2">勘测成果</td>
                    <td></td>
                    <td>${meaResult["sumArea"]!?string(fixed)}</td>
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
                <tr>
                    <#assign item = xzResult.categoryB>
                    <td colspan="2">现状数据</td>
                    <td></td>
                    <td>${xzResult["sumArea"]!?string(fixed)}</td>
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
                <tr>
                    <#assign item1 = meaResult.categoryB>
                    <#assign item2 = xzResult.categoryB>
                    <td colspan="2">偏离情况</td>
                    <td></td>
                    <td><#if xzResult["sumArea"]==0>无<#else >
                            ${((meaResult["sumArea"]-xzResult["sumArea"])*100/xzResult["sumArea"])!?string(fixed)}</#if></td>
                    <td><#if !item2["01"]??>无<#elseif item2["01"]!=0>
                            ${((item1["01"]-item2["01"])*100/item2["01"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["011"]??>无<#elseif item2["011"]!=0>
                            ${((item1["011"]-item2["011"])*100/item2["011"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["012"]??>无<#elseif item2["012"]!=0>
                            ${((item1["012"]-item2["012"])*100/item2["012"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["013"]??>无<#elseif item2["013"]!=0>
                            ${((item1["013"]-item2["013"])*100/item2["013"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["02"]??>无<#elseif item2["02"]!=0>
                            ${((item1["02"]-item2["02"])*100/item2["02"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["021"]??>无<#elseif item2["021"]!=0>
                            ${((item1["021"]-item2["021"])*100/item2["021"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["022"]??>无<#elseif item2["022"]!=0>
                            ${((item1["022"]-item2["022"])*100/item2["022"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["023"]??>无<#elseif item2["023"]!=0>
                            ${((item1["023"]-item2["023"])*100/item2["023"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["03"]??>无<#elseif item2["03"]!=0>
                            ${((item1["03"]-item2["03"])*100/item2["03"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["031"]??>无<#elseif item2["031"]!=0>
                            ${((item1["031"]-item2["031"])*100/item2["031"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["032"]??>无<#elseif item2["032"]!=0>
                            ${((item1["032"]-item2["032"])*100/item2["032"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["033"]??>无<#elseif item2["033"]!=0>
                            ${((item1["033"]-item2["033"])*100/item2["033"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["04"]??>无<#elseif item2["04"]!=0>
                            ${((item1["04"]-item2["04"])*100/item2["04"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["041"]??>无<#elseif item2["041"]!=0>
                            ${((item1["041"]-item2["041"])*100/item2["041"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["042"]??>无<#elseif item2["042"]!=0>
                            ${((item1["042"]-item2["042"])*100/item2["042"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["043"]??>无<#elseif item2["043"]!=0>
                            ${((item1["043"]-item2["043"])*100/item2["043"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["20"]??>无<#elseif item2["20"]!=0>
                            ${((item1["20"]-item2["20"])*100/item2["20"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["201"]??>无<#elseif item2["201"]!=0>
                            ${((item1["201"]-item2["201"])*100/item2["201"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["202"]??>无<#elseif item2["202"]!=0>
                            ${((item1["202"]-item2["202"])*100/item2["202"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["203"]??>无<#elseif item2["203"]!=0>
                            ${((item1["203"]-item2["203"])*100/item2["203"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["204"]??>无<#elseif item2["204"]!=0>
                            ${((item1["204"]-item2["204"])*100/item2["204"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["205"]??>无<#elseif item2["20"]!=0>
                            ${((item1["205"]-item2["205"])*100/item2["205"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["10"]??>无<#elseif item2["10"]!=0>
                            ${((item1["10"]-item2["10"])*100/item2["10"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["101"]??>无<#elseif item2["101"]!=0>
                            ${((item1["101"]-item2["101"])*100/item2["101"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["102"]??>无<#elseif item2["102"]!=0>
                            ${((item1["102"]-item2["102"])*100/item2["102"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["104"]??>无<#elseif item2["104"]!=0>
                            ${((item1["104"]-item2["104"])*100/item2["104"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["105"]??>无<#elseif item2["105"]!=0>
                            ${((item1["105"]-item2["105"])*100/item2["105"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["106"]??>无<#elseif item2["106"]!=0>
                            ${((item1["106"]-item2["106"])*100/item2["106"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["107"]??>无<#elseif item2["107"]!=0>
                            ${((item1["107"]-item2["107"])*100/item2["107"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["11"]??>无<#elseif item2["11"]!=0>
                            ${((item1["11"]-item2["11"])*100/item2["11"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["111"]??>无<#elseif item2["111"]!=0>
                            ${((item1["111"]-item2["111"])*100/item2["111"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["112"]??>无<#elseif item2["112"]!=0>
                            ${((item1["112"]-item2["112"])*100/item2["112"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["113"]??>无<#elseif item2["113"]!=0>
                            ${((item1["113"]-item2["113"])*100/item2["113"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["114"]??>无<#elseif item2["114"]!=0>
                            ${((item1["114"]-item2["114"])*100/item2["114"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["115"]??>无<#elseif item2["115"]!=0>
                            ${((item1["115"]-item2["115"])*100/item2["115"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["116"]??>无<#elseif item2["116"]!=0>
                            ${((item1["116"]-item2["116"])*100/item2["116"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["117"]??>无<#elseif item2["117"]!=0>
                            ${((item1["117"]-item2["117"])*100/item2["117"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["118"]??>无<#elseif item2["118"]!=0>
                            ${((item1["118"]-item2["118"])*100/item2["118"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["119"]??>无<#elseif item2["119"]!=0>
                            ${((item1["119"]-item2["119"])*100/item2["119"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["12"]??>无<#elseif item2["12"]!=0>
                            ${((item1["12"]-item2["12"])*100/item2["12"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["122"]??>无<#elseif item2["122"]!=0>
                            ${((item1["122"]-item2["122"])*100/item2["122"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["123"]??>无<#elseif item2["123"]!=0>
                            ${((item1["123"]-item2["123"])*100/item2["123"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["124"]??>无<#elseif item2["124"]!=0>
                            ${((item1["124"]-item2["124"])*100/item2["124"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["125"]??>无<#elseif item2["125"]!=0>
                            ${((item1["125"]-item2["125"])*100/item2["125"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["126"]??>无<#elseif item2["126"]!=0>
                            ${((item1["126"]-item2["126"])*100/item2["126"])!?string(fixed)}<#else >无</#if></td>
                    <td><#if !item2["127"]??>无<#elseif item2["127"]!=0>
                            ${((item1["127"]-item2["127"])*100/item2["127"])!?string(fixed)}<#else >无</#if></td>
                </tr>
            </table>
        </div>
    </div>
<#else >无分析结果</#if>
