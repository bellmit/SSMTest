<#if (tplData?keys?size>0)>
    <div>
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab11" data-toggle="tab">报批汇总</a></li>
            <li><a href="#tab22" data-toggle="tab">报批详情</a></li>
            <#if (tplData.result.xzDetail?size>0)&&(tplData.result.xzDetail[0].categoryB??)>
            <li><a href="#tab33" data-toggle="tab">未批详情</a></li>
            </#if>
        </ul>
    </div>
    <div class="tab-content">
        <div id="tab11" class="tab-pane fade in active">
            <#if (tplData.result?size>0)>
                <#assign infoItems=tplData.result.info/>
                <table>
                    <tr>
                        <th>类型</th>
                        <th>面积_平方米</th>
                        <th>面积_公顷</th>
                        <th>面积_亩</th>
                    </tr>
                    <#list infoItems as infoItem>
                        <tr>
                            <td>${infoItem["type"]!}</td>
                            <td><#if (infoItem.area &lt;0)||(infoItem.area==0)>
                                    0<#else>${infoItem["area"]!?string("0.####")}</#if></td>
                            <td>${infoItem["area_gq"]!?string("0.####")}</td>
                            <td>${infoItem["area_m"]!?string("0.####")}</td>
                        </tr>
                    </#list>
                </table>
            <#else >无分析结果</#if>
        </div>
        <div id="tab22" class="tab-pane fade">
            <#if (tplData.result?size>0)>
                <#assign detailItems=tplData.result.detail/>
                <#assign hyperlinks=tplData.hyperlinks!/>
                <#if detailItems?size &gt;0><#assign firstItem=detailItems[0]/>
                    <table>
                        <tr>
                            <#list firstItem?keys as key>
                                <th>
                                    <#if key=="SHAPE">
                                        相交图形
                                    <#else >
                                        ${key!}
                                    </#if>
                                </th>
                            </#list>
                        </tr>
                        <#list detailItems as detailItem>
                            <#if firstItem??>
                                <tr>
                                    <#list firstItem?keys as key>
                                        <#assign temp=true/>
                                        <td>
                                            <#if hyperlinks??>
                                                <#list hyperlinks as hyperlink>
                                                    <#if hyperlink[key]??>
                                                        <#assign link=hyperlink[key][detailItem_index]/>
                                                        <#if link[detailItem[key]]??>
                                                            <#assign temp=false/>
                                                            <a href="${link[detailItem[key]]}"
                                                               target="_blank">${detailItem[key]!}</a>
                                                        </#if>
                                                    </#if>
                                                </#list>
                                            </#if>
                                            <#if temp>
                                                <#if key?upper_case=="SHAPE">
                                                    <a class="btn btn-success btn-small"
                                                       onclick=gotoLocation('${detailItem[key]!}','bp');>查看</a>
                                                <#else>
                                                    <#if detailItem[key]??>
                                                        <#if detailItem[key]?is_number>
                                                            ${detailItem[key]!?string("0.####")}
                                                        <#else>
                                                            ${detailItem[key]!}
                                                        </#if>
                                                    <#else>
                                                        空
                                                    </#if>
                                                </#if>
                                            </#if>
                                        </td>
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
        <div id="tab33" class="tab-pane fade" style="overflow: auto;">
            <#if (tplData.result.xzDetail?size>0)&&(tplData.result.xzDetail[0].categoryB??)>
                <#assign first=tplData.result.xzDetail[0].categoryB>
                <table>
                    <tr>
                        <th rowspan="2"><#if tplData.level??&&tplData.level=="mas">地块名称<#else >坐落单位</#if></th>
                        <#if isShowQsdwmc?? &&  isShowQsdwmc=="true">
                            <th  rowspan="2">权属单位</th>
                        </#if>
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
                    <#list tplData.result.xzDetail as xzq>
                        <#assign item=xzq.categoryB>
                        <tr>
                            <#--<td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>-->
                            <#if isShowQsdwmc?? && isShowQsdwmc=="true">
                                <#if xzq.xzqmc=="合计">
                                    <td colspan="2"><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                <#else>
                                    <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                                    <td>${xzq["qsdwmc"]!}</td>
                                </#if>
                            <#else>
                                <td><#if xzq.xzqmc??&&xzq.xzqmc!="null">${xzq["xzqmc"]!}<#else >${xzq["xzqdm"]!}</#if></td>
                            </#if>
                            <td>${xzq["sumArea"]!?string("0.####")}</td>
                            <td>${item["01"]!?string("0.####")}</td>
                            <td><#if item["011"]??> ${item["011"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["012"]??>${item["012"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["013"]??>${item["013"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["02"]!?string("0.####")}</td>
                            <td><#if item["021"]??>${item["021"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["022"]??>${item["022"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["023"]??>${item["023"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["03"]!?string("0.####")}</td>
                            <td><#if item["031"]??>${item["031"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["032"]??>${item["032"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["033"]??>${item["033"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["04"]!?string("0.####")}</td>
                            <td><#if item["041"]??>${item["041"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["042"]??>${item["042"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["043"]??>${item["043"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["20"]!?string("0.####")}</td>
                            <td><#if item["201"]??>${item["201"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["202"]??>${item["202"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["203"]??>${item["203"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["204"]??>${item["204"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["205"]??>${item["205"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["10"]!?string("0.####")}</td>
                            <td><#if item["101"]??>${item["101"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["102"]??>${item["102"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["104"]??>${item["104"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["105"]??>${item["105"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["106"]??>${item["106"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["107"]??>${item["107"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["11"]!?string("0.####")}</td>
                            <td><#if item["111"]??>${item["111"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["112"]??>${item["112"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["113"]??>${item["113"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["114"]??>${item["114"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["115"]??>${item["115"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["116"]??>${item["116"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["117"]??>${item["117"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["118"]??>${item["118"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["119"]??>${item["119"]!?string("0.####")}<#else >0</#if></td>
                            <td>${item["12"]!?string("0.####")}</td>
                            <td><#if item["122"]??>${item["122"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["123"]??>${item["123"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["124"]??>${item["124"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["125"]??>${item["125"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["126"]??>${item["126"]!?string("0.####")}<#else >0</#if></td>
                            <td><#if item["127"]??>${item["127"]!?string("0.####")}<#else >0</#if></td>
                        </tr>
                    </#list>
                </table>
            <#else ><h5>该分类下无分析结果</h5></#if>
        </div>
    </div>
<#else >无分析结果</#if>
