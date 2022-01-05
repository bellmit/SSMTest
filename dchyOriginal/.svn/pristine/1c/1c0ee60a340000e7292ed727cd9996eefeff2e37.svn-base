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

     .func-container {
        width: 100%;
    }

    .func-header-panel {
        margin-top: 20px;
    }

    .func-main-wrapper {
        width: 320px;
        float: left;
        padding-right: 20px;
    }
    .unit-sub{
        bottom: 0;
        font-size: 15px;
    }
</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
<div class="container header">
    <h3><span class="icon icon-dashboard"></span>&nbsp;土地权属地类面积审核结果展示<sub class="unit-sub">(单位:公顷)</sub></h3>
</div>
<div class="container wrapper">
    <div class="row">
        <#if data?size gt 0>
            <#if data?size gt 1>
                <div class="col col-lg-2 col-md-2 col-sm-2 col-xs-2">
                    <ul class="nav nav-pills nav-stacked scroll-fixed" role="tablist">
                        <#list data as dataItem>
                            <li class="text-center <#if dataItem_index==0>active</#if>">
                                <a href="#content${dataItem.year}" data-toggle="tab">${dataItem.year!}年</a>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>
            <div class="tab-content col col-lg-10 col-md-10 col-sm-10 col-xs-10">
                <#list data as dataRow>
                    <div id="content${dataRow.year}" class="tab-pane fade in <#if dataRow_index==0>active</#if>">
                        <#if dataRow.analysisData??>
                            <#assign analysisData=dataRow.analysisData>
                            <#if dataRow.analysisTotal??>
                                <#assign analysisTotal=dataRow.analysisTotal>
                                <#assign report = dataRow.reportData>
                            </#if>
                            <#if report??>
                                <div id="funcContainer${dataRow.year!}" class="container func-container">
                                    <div class="panel panel-primary func-header-panel">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">土地权属地类面积审核信息</h3>
                                        </div>
                                        <div class="panel-body">
                                            <form class="form-horizontal" role="form">
                                                <div class="form-group">
                                                    <label class="col-sm-1 control-label">上报总面积</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled" value="${report.rArea!?string('0.####')}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">分析总面积</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled"
                                                               value="${analysisTotal.sumArea!?string('0.####')}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">国有面积</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled"
                                                               value="${analysisTotal.sumAreaGy!?string('0.####')}">
                                                    </div>
                                                    <label class="col-sm-1 control-label">集体面积</label>

                                                    <div class="col-sm-2">
                                                        <input type="input" class="form-control disabled"
                                                               value="${analysisTotal.sumAreaJt!?string('0.####')}">
                                                    </div>
                                                </div>
                                                <div class="form-group" style="text-align: center;">
                                                    <label class="col-sm-2 control-label">结果</label>

                                                    <div class="col-sm-2">
                                                        <div class="form-control label-success">
                                                            <span style="color: white;"><#if (report.sumResult &gt;0)>
                                                                多&nbsp;<#elseif (report.sumResult &lt;0)>
                                                                少&nbsp;</#if>${(env.absDouble(report.sumResult))!?string('0.####')} m<sup>2</sup></span>
                                                        </div>
                                                    </div>
                                                    <label class="col-sm-2 control-label">误差</label>

                                                    <div class="col-sm-2">
                                                        <div class="form-control label-warning">
                                                            <span style="color: white;">${(env.absDouble(report.sumMistake))!?string('0.####')}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="func-main-wrapper">
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
                                                                       value="${report.rNydArea!?string('0.####')}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>结果</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.nydResult &gt;0)>多<#elseif (report.nydResult &lt;0)>少</#if>${(env.absDouble(report.nydResult))!?string('0.####')} ㎡">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>误差</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.nydMistake))!?string('0.####')}">
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
                                                                       value="${report.rGdArea!?string('0.####')}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>结果</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.gdResult &gt;0)>多<#elseif (report.gdResult &lt;0)>少</#if>${(env.absDouble(report.gdResult))!?string('0.####')} ㎡">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>误差</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.gdMistake))!?string('0.####')}">
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
                                                                       value="${report.rJsydArea!?string('0.####')}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>结果</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.jsydResult &gt;0)>多<#elseif (report.jsydResult &lt;0)>少</#if>${(env.absDouble(report.jsydResult))!?string('0.####')} ㎡">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>误差</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.jsydMistake))!?string('0.####')}">
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
                                                                       value="${report.rWlydArea!?string('0.####')}">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>结果</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="<#if (report.wlydResult &gt;0)>多<#elseif (report.wlydResult &lt;0)>少</#if>${(env.absDouble(report.wlydResult))!?string('0.####')} ㎡">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>误差</th>
                                                            <td>
                                                                <input type="input" class="form-control disabled"
                                                                       value="${(env.absDouble(report.wlydMistake))!?string('0.####')}">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <#if analysisTotal??>
                                            <#assign tot=analysisTotal.categoryB/>
                                        </#if>
                                        <div class="panel panel-primary" style="max-height: 1005px;overflow-y: auto;">
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
                                                    <td><#if tot?keys?seq_contains("农用地")>${tot["农用地"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("农用地_gy")>${tot["农用地_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("农用地_jt")>${tot["农用地_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>耕地</td>
                                                    <td><#if tot?keys?seq_contains("01")>${tot["01"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("01_gy")>${tot["01_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("01_jt")>${tot["01_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>园地</td>
                                                    <td><#if tot?keys?seq_contains("02")>${tot["02"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("02_gy")>${tot["02_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("02_jt")>${tot["02_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>林地</td>
                                                    <td><#if tot?keys?seq_contains("03")>${tot["03"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("03_gy")>${tot["03_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("03_jt")>${tot["03_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>天然牧草地</td>
                                                    <td><#if tot?keys?seq_contains("041")>${tot["041"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("041_gy")>${tot["041_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("041_jt")>${tot["041_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>人工牧草地</td>
                                                    <td><#if tot?keys?seq_contains("042")>${tot["042"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("042_gy")>${tot["042_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("042_jt")>${tot["042_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>农村道路</td>
                                                    <td><#if tot?keys?seq_contains("104")>${tot["104"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("104_gy")>${tot["104_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("104_jt")>${tot["104_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>坑塘水面</td>
                                                    <td><#if tot?keys?seq_contains("114")>${tot["114"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("114_gy")>${tot["114_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("114_jt")>${tot["114_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>沟渠</td>
                                                    <td><#if tot?keys?seq_contains("117")>${tot["117"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("117_gy")>${tot["117_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("117_jt")>${tot["117_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>设施农用地</td>
                                                    <td><#if tot?keys?seq_contains("122")>${tot["122"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("122_gy")>${tot["122_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("122_jt")>${tot["122_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>田坎</td>
                                                    <td><#if tot?keys?seq_contains("123")>${tot["123"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("123_gy")>${tot["123_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("123_jt")>${tot["123_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight: bold">建设用地</td>
                                                    <td><#if tot?keys?seq_contains("建设用地")>${tot["建设用地"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("建设用地_gy")>${tot["建设用地_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("建设用地_jt")>${tot["建设用地_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>城镇村及工矿用地</td>
                                                    <td><#if tot?keys?seq_contains("20")>${tot["20"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("20_gy")>${tot["20_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("20_jt")>${tot["20_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>铁路用地</td>
                                                    <td><#if tot?keys?seq_contains("101")>${tot["101"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("101_gy")>${tot["101_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("101_jt")>${tot["101_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>公路用地</td>
                                                    <td><#if tot?keys?seq_contains("102")>${tot["102"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("102_gy")>${tot["102_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("102_jt")>${tot["102_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>机场用地</td>
                                                    <td><#if tot?keys?seq_contains("105")>${tot["105"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("105_gy")>${tot["105_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("105_jt")>${tot["105_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>港口码头用地</td>
                                                    <td><#if tot?keys?seq_contains("106")>${tot["106"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("106_gy")>${tot["106_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("106_jt")>${tot["106_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>管道运输用地</td>
                                                    <td><#if tot?keys?seq_contains("107")>${tot["107"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("107_gy")>${tot["107_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("107_jt")>${tot["107_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td style="font-weight: bold">未利用地</td>
                                                    <td><#if tot?keys?seq_contains("未利用地")>${tot["未利用地"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("未利用地_gy")>${tot["未利用地_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("未利用地_jt")>${tot["未利用地_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>沙地</td>
                                                    <td><#if tot?keys?seq_contains("126")>${tot["126"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("126_gy")>${tot["126_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("126_jt")>${tot["126_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                <tr>
                                                    <td>裸地</td>
                                                    <td><#if tot?keys?seq_contains("127")>${tot["127"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("127_gy")>${tot["127_gy"]!?string('0.####')}</#if></td>
                                                    <td><#if tot?keys?seq_contains("127_jt")>${tot["127_jt"]!?string('0.####')}</#if></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        <#else > <h5 class="text-warning text-center">分析无结果</h5>
                        </#if>
                    </div>
                </#list>
            </div>
        <#else ><h5 class="text-muted text-center">无结果</h5>
        </#if>
    </div>
</div>

<script src="/omp/static/thirdparty/jquery/jquery-scrolltofixed-min.js"></script>
<script type="text/javascript">

    $(document).ready(function () {
        $('.disabled').attr('disabled', true);
        $("[data-toggle='tooltip']").tooltip({
            html: true,
            trigger: 'click'
        });
        //添加结果展示切换监听
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
        //固定头部和左侧的导航元素
        $(".scroll-fixed").scrollToFixed({marginTop:20});

    });
    /**
     * 导出shp/dwg
     * @param type
     * */
    function exportFeatures(f,type) {
        var shpId = $(f).data('shpid');
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

    /***
     * post window method
     * @param url
     * @param data
     * @param fileName
     */
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
