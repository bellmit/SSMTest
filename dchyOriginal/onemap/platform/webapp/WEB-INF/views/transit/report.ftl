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
            <button id="fixed-header-btn" class="btn btn-info" onclick="export2Xls();">??????excel</button>
        </div>
    </div>
</div>
<#--header-->
<div id="header-wrap">
    <div id="header">
        <h3 style="font-weight: normal; color:#188074">&nbsp;${fileName!}</h3>
        <button id="header-btn" class="btn btn-info" onclick="export2Xls();">??????excel</button>
    </div>
</div>
<div style="margin: 20px;">
    <#switch id>
        <#case 0>
        <table>
            <tr>
                <th rowspan="2">????????????</th>
                <th rowspan="2">????????????</th>
                <th colspan="4">??????</th>
                <th colspan="4">??????</th>
                <th colspan="2">???????????????</th>
                <th colspan="2">??????</th>
            </tr>
            <tr>
                <th>????????????</th>
                <th>??????</th>
                <th>???????????????</th>
                <th>??????</th>
                <th>????????????</th>
                <th>??????</th>
                <th>???????????????</th>
                <th>??????</th>
                <th>??????</th>
                <th>??????</th>
                <th>??????</th>
                <th>??????</th>
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
            <#assign gy=data["?????????"]!/>
            <#assign qt=data["?????????"]!/>
            <#assign jt=data["?????????"]!/>
            <#assign sf=data["?????????"]!/>
            <#assign jz=data["?????????"]!/>

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
                <li class="active"><a href="#gy" data-toggle="tab">?????????</a></li>
                <li><a href="#jz" data-toggle="tab">?????????</a></li>
                <li><a href="#sf" data-toggle="tab">?????????</a></li>
                <li><a href="#jt" data-toggle="tab">?????????</a></li>
                <li><a href="#qt" data-toggle="tab">?????????</a></li>
            </ul>
        </div>
        <div class="span12" style="width: 1040px !important;">
        <div class="tab-content large" style="max-height: 100% !important;  overflow-y: hidden;">
        <div id="gy" class="tab-pane fade in active">
            <table>
                <tr>
                    <th>??????</th>
                    <th>????????????</th>
                    <th>????????????</th>
                    <th>????????????(???)</th>
                    <th>????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>????????????</th>
                    <th>??????</th>
                    <th>????????????</th>
                </tr>
                <#if count_gy &gt;0>
                    <#list categoryData_gy?keys as key>
                        <#assign xzqList=categoryData_gy[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">??????</td>
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
                                        <td colspan="3">??????</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('???')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">?????????</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="jz" class="tab-pane fade in">
            <table>
                <tr>
                    <th>??????</th>
                    <th>????????????</th>
                    <th>????????????</th>
                    <th>????????????(???)</th>
                    <th>????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>????????????</th>
                    <th>??????</th>
                    <th>????????????</th>
                </tr>
                <#if count_jz &gt;0>
                    <#list categoryData_jz?keys as key>
                        <#assign xzqList=categoryData_jz[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">??????</td>
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
                                        <td colspan="3">??????</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('???')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">?????????</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="sf" class="tab-pane fade in">
            <table>
                <tr>
                    <th>??????</th>
                    <th>????????????</th>
                    <th>????????????</th>
                    <th>????????????(???)</th>
                    <th>????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>????????????</th>
                    <th>??????</th>
                    <th>????????????</th>
                </tr>
                <#if count_sf &gt;0>
                    <#list categoryData_sf?keys as key>
                        <#assign xzqList=categoryData_sf[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">??????</td>
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
                                        <td colspan="3">??????</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('???')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">?????????</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="jt" class="tab-pane fade in">
            <table>
                <tr>
                    <th>??????</th>
                    <th>????????????</th>
                    <th>????????????</th>
                    <th>????????????(???)</th>
                    <th>????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>????????????</th>
                    <th>??????</th>
                    <th>????????????</th>
                </tr>
                <#if count_jt &gt;0>
                    <#list categoryData_jt?keys as key>
                        <#assign xzqList=categoryData_jt[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">??????</td>
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
                                        <td colspan="3">??????</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('???')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">?????????</td>
                    </tr>
                </#if>
            </table>
        </div>
        <div id="qt" class="tab-pane fade in">
            <table>
                <tr>
                    <th>??????</th>
                    <th>????????????</th>
                    <th>????????????</th>
                    <th>????????????(???)</th>
                    <th>????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>??????????????????</th>
                    <th>????????????</th>
                    <th>??????</th>
                    <th>????????????</th>
                </tr>
                <#if count_qt &gt;0>
                    <#list categoryData_qt?keys as key>
                        <#assign xzqList=categoryData_qt[key]/>
                        <#list xzqList as xzq>
                            <tr>
                                <#if xzqList?size==1>
                                    <td colspan="3">??????</td>
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
                                        <td colspan="3">??????</td>
                                    </#if>
                                </#if>
                                <td>${xzq["NGZMJ"]!}</td>
                                <td>${xzq["YDDW"]!}</td>
                                <td>${xzq["XYKGS"]!}</td>
                                <td>${xzq["XYJGS"]!}</td>
                                <td>${xzq["XMZT"]!}</td>
                                <td>${xzq["LYQK_FJ"]!?contains('???')?string('&radic;','')}</td>
                                <td>${xzq["BZ_FJ"]!}</td>
                                <td><#if xzq_has_next><button class="btn btn-success btn-small"
                                                         onclick=goLocation('${xzq.GEOJSON!}'); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if></td>
                            </tr>
                        </#list>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="10">?????????</td>
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
                <th>??????</th>
                <th>????????????</th>
                <th>??????????????????(???)</th>
                <th>??????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????(???/???)</th>
                <th>??????????????????(??????)</th>
                <th>?????????(???/???)</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????</th>
            </tr>
            <#list data as xzq>
                <tr>
                    <#if xzq_index==data?size-1>
                        <td colspan="2">${xzq["EJXZQ"]!'??????'}</td>
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
                                                                 onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if>
                    </td>
                </tr>
            </#list>
        </table>
            <#break />
        <#case 3>
        <table>
            <tr>
                <th>??????????????????</th>
                <th>??????</th>
                <th>????????????</th>
                <th>????????????(???)</th>
                <th>???????????????(???)</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>??????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????????????????</th>
                <th>????????????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
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
                                                                     onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if>
                        </td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 4>
        <table>
            <tr>
                <th>??????????????????</th>
                <th>??????</th>
                <th>????????????</th>
                <th>????????????(???)</th>
                <th>???????????????(???)</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>??????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????????????????</th>
                <th>????????????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????</th>
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
                                                                     onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></#if>
                        </td>
                    </tr>
                </#list>
            </#list>
        </table>
            <#break />
        <#case 5>
        <table>
            <tr>
                <th>??????????????????</th>
                <th>??????</th>
                <th>????????????</th>
                <th>????????????(???)</th>
                <th>???????????????(???)</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>??????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????????????????</th>
                <th>????????????????????????</th>
                <th>????????????</th>
                <th>????????????</th>
                <th>????????????</th>
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
                        <td><button class="btn btn-success btn-small" onclick=goLocation(${xzq.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></td>
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
                                    <th>??????</th>
                                    <th>??????</th>
                                    <th>??????</th>
                                    <th>????????????</th>
                                    <th>????????????</th>
                                    <th>????????????</th>
                                    <th>????????????</th>
                                    <th>???????????????</th>
                                    <th>??????????????????</th>
                                    <th>??????</th>
                                    <th>????????????</th>
                                    <th>????????????</th>
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
                                                   onclick=goLocation(${item.GEOJSON!}); style="padding: 5px;background-color: #52A252;color: #fff;">??????</button></td>
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
     * ??????excel
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
     * ????????????
     * @param geometry geojson ???????????????
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
     * ????????????
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
