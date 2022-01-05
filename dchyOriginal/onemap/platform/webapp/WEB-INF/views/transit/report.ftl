<#assign cssContent>
<style>
    body, html {
        background: #fff;
    }

    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
        text-align: center;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: center;
        vertical-align: middle;
        min-width: 60px;
    }

    table {
        width: 100%;
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
    }

    .tab-content {
        padding: 20px;
        border: 1px solid #ddd;
        border-top: none;
        border-radius: 0 0 5px 5px;
        max-height: 750px;
    }

    #fixed-header-slide {
        width: 95%;
        height: 50px;
        background: #fff;
        position: fixed;
        top: 0px;
        z-index: 2;
    }

    #fixed-header-wrap {
        width: 100%;
        height: 50px;
        position: absolute;
        bottom: 0px;
    }

    #fixed-header {
        width: 100%;
        height: 50px;
        margin: 0 auto;
        position: relative;
        z-index: 1;
        text-align: center;
    }

    #header-wrap {
        width: 100%;
        background: #fff;
        z-index: 3;
        position: relative;
    }

    #header {
        text-align: center;
        margin: 0 auto;
        position: relative;
        z-index: 3;
        height: 30px;
    }

    #fixed-header-btn {
        float: right;
        margin-top: -45px;
        margin-right: 50px;
        z-index: 1;
        position: relative;
        background-color: #188074;
        color: #fff;
        padding: 5px;
        cursor:pointer;
    }

    #header-btn {
        float: right;
        margin-top: -45px;
        margin-right: 50px;
        z-index: 3;
        position: relative;
        background-color: #188074;
        color: #fff;
        padding: 5px;
        cursor:pointer;
    }

</style>
</#assign>
<#assign id=rId!/>
<@aBase.tpl showHeader="false" css=cssContent>
<div class="container" style="width: 95%;">
<#--fixed header-->
<div id="fixed-header-slide">
    <div id="fixed-header-wrap">
        <div id="fixed-header">
            <h3 style="font-weight: normal; color:#188074">&nbsp;${fileName!}</h3>
            <button id="fixed-header-btn" class="btn btn-info" onclick="export2Xls();">导出excel</button>
        </div>
    </div>
</div>
<#--header-->
<div id="header-wrap">
    <div id="header">
        <h3 style="font-weight: normal; color:#188074">&nbsp;${fileName!}</h3>
        <button id="header-btn" class="btn btn-info" onclick="export2Xls();">导出excel</button>
    </div>
</div>
<div style="margin: 20px;">
    <#switch id>
        <#case 0>
        <table>
            <tr>
                <th rowspan="2">载体单位</th>
                <th rowspan="2">土地用途</th>
                <th colspan="4">开工</th>
                <th colspan="4">竣工</th>
                <th colspan="2">未到期开工</th>
                <th colspan="2">合计</th>
            </tr>
            <tr>
                <th>到期开工</th>
                <th>面积</th>
                <th>到期未开工</th>
                <th>面积</th>
                <th>到期竣工</th>
                <th>面积</th>
                <th>到期未竣工</th>
                <th>面积</th>
                <th>宗数</th>
                <th>面积</th>
                <th>宗数</th>
                <th>面积</th>
            </tr>
            <#list data?keys as key>
                <#assign list = data[key]!/>
                <#list list as item>
                    <tr>
                        <#if item_index==0>
                            <td rowspan="6">${key!}</td>
                        </#if>
                        <td>${item["tdyt"]!}</td>
                        <td>${item["kg"]!}</td>
                        <td>${item["kgmj"]!}</td>
                        <td>${item["wkg"]!}</td>
                        <td>${item["wkgmj"]!}</td>
                        <td>${item["jg"]!}</td>
                        <td>${item["jgmj"]!}</td>
                        <td>${item["wjg"]!}</td>
                        <td>${item["wjgmj"]!}</td>
                        <td>${item["wdkg"]!}</td>
                        <td>${item["wdkgmj"]!}</td>
                        <td>${item["count"]!}</td>
                        <td>${item["sumArea"]!}</td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 1>
            <#assign gy=data["工业类"]!/>
            <#assign qt=data["其他类"]!/>
            <#assign jt=data["交通类"]!/>
            <#assign sf=data["商服类"]!/>
            <#assign jz=data["居住类"]!/>

            <#assign count_gy = gy.count!/>
            <#assign categoryData_gy=gy.data/>

            <#assign count_qt = qt.count!/>
            <#assign categoryData_qt=qt.data/>

            <#assign count_jt=jt.count!/>
            <#assign categoryData_jt=jt.data/>

            <#assign count_sf=sf.count!/>
            <#assign categoryData_sf=sf.data/>

            <#assign count_jz=jz.count!/>
            <#assign categoryData_jz=jz.data/>
        <div class="row" style="margin-left: 10px !important;">
        <div style="max-width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <li class="active"><a href="#gy" data-toggle="tab">工业类</a></li>
                <li><a href="#jz" data-toggle="tab">居住类</a></li>
                <li><a href="#sf" data-toggle="tab">商服类</a></li>
                <li><a href="#jt" data-toggle="tab">交通类</a></li>
                <li><a href="#qt" data-toggle="tab">其他类</a></li>
            </ul>
        </div>
        <div class="span12" style="width: 1040px !important;">
        <div class="tab-content large" style="max-height: 100% !important;  overflow-y: hidden;">
        <div id="gy" class="tab-pane fade in active">
            <table>
                <tr>
                    <th>序号</th>
                    <th>载体单位</th>
                    <th>项目名称</th>
                    <th>占地面积(亩)</th>
                    <th>投资主体</th>
                    <th>约定开工时间</th>
                    <th>约定竣工时间</th>
                    <th>项目进展情况</th>
                    <th>履约情况</th>
                    <th>备注</th>
                    <th>图形定位</th>
                </tr>
                <#if count_gy &gt;0>
                    <#list categoryData_gy?keys as key>
                        <#assign xzqList=categoryData_gy[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">总计</td>
                                <#else >
                                    <#if xzq_has_next>
                                        <td>${xzq_index+1}</td>
                                    </#if>
                                    <#if xzq_index==0>
                                        <td rowspan="${xzqList?size-1}">${key!}</td>
                                    </#if>
                                    <#if xzq_has_next>
                                        <td>${xzq["YDXMMC"]!}</td>
                                    <#else >
                                        <td colspan="3">小计</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('是')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">无结果</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="jz" class="tab-pane fade in">
            <table>
                <tr>
                    <th>序号</th>
                    <th>载体单位</th>
                    <th>项目名称</th>
                    <th>占地面积(亩)</th>
                    <th>投资主体</th>
                    <th>约定开工时间</th>
                    <th>约定竣工时间</th>
                    <th>项目进展情况</th>
                    <th>履约情况</th>
                    <th>备注</th>
                    <th>图形定位</th>
                </tr>
                <#if count_jz &gt;0>
                    <#list categoryData_jz?keys as key>
                        <#assign xzqList=categoryData_jz[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">总计</td>
                                <#else >
                                    <#if xzq_has_next>
                                        <td>${xzq_index+1}</td>
                                    </#if>
                                    <#if xzq_index==0>
                                        <td rowspan="${xzqList?size-1}">${key!}</td>
                                    </#if>
                                    <#if xzq_has_next>
                                        <td>${xzq["YDXMMC"]!}</td>
                                    <#else >
                                        <td colspan="3">小计</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('是')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">无结果</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="sf" class="tab-pane fade in">
            <table>
                <tr>
                    <th>序号</th>
                    <th>载体单位</th>
                    <th>项目名称</th>
                    <th>占地面积(亩)</th>
                    <th>投资主体</th>
                    <th>约定开工时间</th>
                    <th>约定竣工时间</th>
                    <th>项目进展情况</th>
                    <th>履约情况</th>
                    <th>备注</th>
                    <th>图形定位</th>
                </tr>
                <#if count_sf &gt;0>
                    <#list categoryData_sf?keys as key>
                        <#assign xzqList=categoryData_sf[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">总计</td>
                                <#else >
                                    <#if xzq_has_next>
                                        <td>${xzq_index+1}</td>
                                    </#if>
                                    <#if xzq_index==0>
                                        <td rowspan="${xzqList?size-1}">${key!}</td>
                                    </#if>
                                    <#if xzq_has_next>
                                        <td>${xzq["YDXMMC"]!}</td>
                                    <#else >
                                        <td colspan="3">小计</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('是')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">无结果</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="jt" class="tab-pane fade in">
            <table>
                <tr>
                    <th>序号</th>
                    <th>载体单位</th>
                    <th>项目名称</th>
                    <th>占地面积(亩)</th>
                    <th>投资主体</th>
                    <th>约定开工时间</th>
                    <th>约定竣工时间</th>
                    <th>项目进展情况</th>
                    <th>履约情况</th>
                    <th>备注</th>
                    <th>图形定位</th>
                </tr>
                <#if count_jt &gt;0>
                    <#list categoryData_jt?keys as key>
                        <#assign xzqList=categoryData_jt[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">总计</td>
                                <#else >
                                    <#if xzq_has_next>
                                        <td>${xzq_index+1}</td>
                                    </#if>
                                    <#if xzq_index==0>
                                        <td rowspan="${xzqList?size-1}">${key!}</td>
                                    </#if>
                                    <#if xzq_has_next>
                                        <td>${xzq["YDXMMC"]!}</td>
                                    <#else >
                                        <td colspan="3">小计</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('是')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">无结果</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="qt" class="tab-pane fade in">
            <table>
                <tr>
                    <th>序号</th>
                    <th>载体单位</th>
                    <th>项目名称</th>
                    <th>占地面积(亩)</th>
                    <th>投资主体</th>
                    <th>约定开工时间</th>
                    <th>约定竣工时间</th>
                    <th>项目进展情况</th>
                    <th>履约情况</th>
                    <th>备注</th>
                    <th>图形定位</th>
                </tr>
                <#if count_qt &gt;0>
                    <#list categoryData_qt?keys as key>
                        <#assign xzqList=categoryData_qt[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">总计</td>
                                <#else >
                                    <#if xzq_has_next>
                                        <td>${xzq_index+1}</td>
                                    </#if>
                                    <#if xzq_index==0>
                                        <td rowspan="${xzqList?size-1}">${key!}</td>
                                    </#if>
                                    <#if xzq_has_next>
                                        <td>${xzq["YDXMMC"]!}</td>
                                    <#else >
                                        <td colspan="3">小计</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('是')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation('${xzq.GEOJSON!}'); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">无结果</td>
                    </tr>
                </#if>
            </table>
        </div>
        </div>
        </div>
        </div>
            <#break />
        <#case 2>
        <table>
            <tr>
                <th>序号</th>
                <th>意向单位</th>
                <th>建设用地面积(亩)</th>
                <th>用途</th>
                <th>产业类型</th>
                <th>处罚情况</th>
                <th>投资强度(万/亩)</th>
                <th>固定资产投资(万元)</th>
                <th>年税收(万/亩)</th>
                <th>建设情况</th>
                <th>企业来源</th>
                <th>处理结果</th>
                <th>图形定位</th>
            </tr>
            <#list data as xzq>
                <tr>
                    <#if xzq_index==data?size-1>
                        <td colspan="2">${xzq["EJXZQ"]!'合计'}</td>
                    <#else >
                        <td>${xzq_index+1}</td>
                        <td>${xzq["YXDW_FJ"]!}</td>
                    </#if>
                    <td>${xzq["JSYDMJ_FJ"]!}</td>
                    <td>${xzq["YT_FJ"]!}</td>
                    <td>${xzq["CYLX_FJ"]!}</td>
                    <td>${xzq["CFQK_FJ"]!}</td>
                    <td>${xzq["TZQD_FJ"]!}</td>
                    <td>${xzq["GDZCTZ_FJ"]!}</td>
                    <td>${xzq["NSS_FJ"]!}</td>
                    <td>${xzq["JSQK_FJ"]!}</td>
                    <td>${xzq["QYLY_FJ"]!}</td>
                    <td>${xzq["CLJG_FJ"]!}</td>
                    <td><#if xzq?keys?seq_contains('GEOJSON')><button class="btn btn-success btn-small"
                                                                 onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if>
                    </td>
                </tr>
            </#list>
        </table>
            <#break />
        <#case 3>
        <table>
            <tr>
                <th>项目载体单位</th>
                <th>序号</th>
                <th>项目名称</th>
                <th>出让面积(亩)</th>
                <th>未开工面积(亩)</th>
                <th>土地用途</th>
                <th>地块位置</th>
                <th>土地使用权人</th>
                <th>出让时间</th>
                <th>现场状况</th>
                <th>合同约定开工时间</th>
                <th>合同约定竣工时间</th>
                <th>处置意见</th>
                <th>图形定位</th>
            </tr>
            <#list data?keys as key>
                <#assign xzqList=data[key]/>
                <#list xzqList as xzq>
                    <tr>
                        <#if xzq_index==0>
                            <td rowspan="${xzqList?size}">${key!}</td></#if>
                        <td>${xzq_index+1}</td>
                        <td>${xzq["YDXMMC"]!}</td>
                        <td>${xzq["NGZMJ"]!}</td>
                        <td>${xzq["WKGMJ_FJ"]!}</td>
                        <td>${xzq["YT"]!}</td>
                        <td>${xzq["YDWZ"]!}</td>
                        <td>${xzq["YDDW"]!}</td>
                        <td>${xzq["PZSJ"]!}</td>
                        <td>${xzq["XCZK_FJ"]!}</td>
                        <td>${xzq["XYKGS"]!}</td>
                        <td>${xzq["XYJGS"]!}</td>
                        <td>${xzq["CZYJ_FJ"]!}</td>
                        <td><#if xzq?keys?seq_contains('GEOJSON')><button class="btn btn-success btn-small"
                                                                     onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if>
                        </td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 4>
        <table>
            <tr>
                <th>项目载体单位</th>
                <th>序号</th>
                <th>项目名称</th>
                <th>出让面积(亩)</th>
                <th>未开工面积(亩)</th>
                <th>土地用途</th>
                <th>地块位置</th>
                <th>土地使用权人</th>
                <th>出让时间</th>
                <th>现场状况</th>
                <th>合同约定开工时间</th>
                <th>合同约定竣工时间</th>
                <th>处置意见</th>
                <th>目前状态</th>
                <th>图形定位</th>
            </tr>
            <#list data?keys as key>
                <#assign xzqList=data[key]/>
                <#list xzqList as xzq>
                    <tr>
                        <#if xzq_index==0>
                            <td rowspan="${xzqList?size}">${key!}</td></#if>
                        <td>${xzq_index+1}</td>
                        <td>${xzq["YDXMMC"]!}</td>
                        <td>${xzq["NGZMJ"]!}</td>
                        <td>${xzq["WJCMJ_FJ"]!}</td>
                        <td>${xzq["YT"]!}</td>
                        <td>${xzq["YDWZ"]!}</td>
                        <td>${xzq["YDDW"]!}</td>
                        <td>${xzq["PZSJ"]!}</td>
                        <td>${xzq["XCZK_FJ"]!}</td>
                        <td>${xzq["XYKGS"]!}</td>
                        <td>${xzq["XYJGS"]!}</td>
                        <td>${xzq["CZYJ_FJ"]!}</td>
                        <td>${xzq["MQZT_FJ"]!}</td>
                        <td><#if xzq?keys?seq_contains('GEOJSON')><button class="btn btn-success btn-small"
                                                                     onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></#if>
                        </td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 5>
        <table>
            <tr>
                <th>项目载体单位</th>
                <th>序号</th>
                <th>项目名称</th>
                <th>出让面积(亩)</th>
                <th>未开工面积(亩)</th>
                <th>土地用途</th>
                <th>地块位置</th>
                <th>土地使用权人</th>
                <th>出让时间</th>
                <th>现场状况</th>
                <th>合同约定开工时间</th>
                <th>合同约定竣工时间</th>
                <th>处置意见</th>
                <th>处置结果</th>
                <th>图形定位</th>
            </tr>
            <#list data?keys as key>
                <#assign xzqList=data[key]/>
                <#list xzqList as xzq>
                    <tr>
                        <#if xzq_index==0>
                            <td rowspan="${xzqList?size}">${key!}</td></#if>
                        <td>${xzq_index+1}</td>
                        <td>${xzq["YDXMMC"]!}</td>
                        <td>${xzq["NGZMJ"]!}</td>
                        <td>${xzq["WJCMJ_FJ"]!}</td>
                        <td>${xzq["YT"]!}</td>
                        <td>${xzq["YDWZ"]!}</td>
                        <td>${xzq["YDDW"]!}</td>
                        <td>${xzq["PZSJ"]!}</td>
                        <td>${xzq["XCZK_FJ"]!}</td>
                        <td>${xzq["XYKGS"]!}</td>
                        <td>${xzq["XYJGS"]!}</td>
                        <td>${xzq["CZYJ_FJ"]!}</td>
                        <td>${xzq["CZJG_FJ"]!}</td>
                        <td><button class="btn btn-success btn-small" onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 7>
        <div class="row" style="margin-left: 10px !important;">
            <div style="max-width:220px;float:left;">
                <ul class="nav nav-pills nav-stacked">
                    <#list data?keys as key>
                        <li <#if key_index==0>class="active"</#if>><a href="#${key_index}" data-toggle="tab">${key!}</a>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="span12" style="width: 1040px !important;">
                <div class="tab-content large" style="max-height: 100% !important;  overflow-y: hidden;">
                    <#list data?keys as key>
                        <#assign list=data[key]!/>
                        <div id="${key_index}" class="tab-pane fade in <#if key_index==0>active</#if>">
                            <table>
                                <tr>
                                    <th>序号</th>
                                    <th>区域</th>
                                    <th>年度</th>
                                    <th>投资主体</th>
                                    <th>项目位置</th>
                                    <th>占地面积</th>
                                    <th>厂房面积</th>
                                    <th>已使用面积</th>
                                    <th>项目建设状态</th>
                                    <th>楼层</th>
                                    <th>目前状态</th>
                                    <th>图形定位</th>
                                </tr>
                                <#if list?size &gt; 0>
                                    <#list list as item>
                                        <tr>
                                            <td>${item_index+1}</td>
                                            <td>${item.XZQ!}</td>
                                            <td>${item.ND!}</td>
                                            <td>${item.TZZT!}</td>
                                            <td>${item.XMWZ!}</td>
                                            <td>${item.ZDMJ!}</td>
                                            <td>${item.CFMJ!}</td>
                                            <td>${item.YSYMJ!}</td>
                                            <td>${item.JSZT!}</td>
                                            <td>${item.LC!}</td>
                                            <td>${item.MQZT!}</td>
                                            <td><button class="btn btn-success btn-small"
                                                   onclick=goLocation(${item.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></td>
                                        </tr>
                                    </#list>
                                </#if>
                            </table>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
            <#break />
    </#switch>
</div>
<div style="text-align: center;padding-top: 10px;margin-bottom: 20px;">
</div>
</div>
<script src="<@com.rootPath/>/static/js/JSONUtil.js"></script>

<script type="text/javascript">

    /***
     * 导出excel
     * */
    function export2Xls() {
        var data = {
            fileName: '${fileName!}'.concat(".xml"),
            dataSource: '${dataSource!}',
            rId: '${rId!}',
            queryCondition: "${queryCondition!}",
            layerName: '${layerName!}'
        };
        openPostWindow("<@com.rootPath/>/transitService/report/output", data);
    }

    /***
     * 图形定位
     * @param geometry geojson 格式坐标串
     * */
    function goLocation(geo) {
        if (geo != "" && geo != undefined) {
            var tpl = 'test111';
            var url = '<@com.rootPath/>/map/' + tpl + '?action=location&hideTopBar=true&hideLeftPanel=true&params=';
            var params = {};
            var locParam = {};
            var geoParam={};
            locParam.type="FeatureCollection";
            params.type = "coordsLocation";
            params.params = locParam;
            geoParam.type="Feature";
            geoParam.geometry=geo;
            geoParam.crs={
                "type": "name",
                "properties": {
                    "name": "EPSG:4490"
                }};
            locParam.features=[geoParam];
            url = url.concat(JSONUtil.encode(params));
            window.open(url, '_blank');
        }
    }

    /***
     * 提交数据
     * @param url
     * @param data
     * @param fileName
     */
    function openPostWindow(url, data) {
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;

        for (var key in data) {
            var tmpInput = document.createElement("input");
            tmpInput.type = "hidden";
            tmpInput.name = key;
            tmpInput.value = data[key];
            tempForm.appendChild(tmpInput);
        }
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

</script>
</@aBase.tpl>
