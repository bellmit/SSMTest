<#assign xz= result.xz!/>
<#assign gh = result.gh!/>
<#assign gd=result.gd!/>
<#assign kc=result.kc!/>
<#assign dz=result.dz!/>
<#assign zd=result.zd!/>
<#assign cl=result.cl!/>
<#assign sp=result.sp!/>
<#assign nt=result.nt!/>
<#assign bp=result.bp!/>
<#assign dj=result.dj!/>
<#assign gyjsydfx=result.gyjsydfx!/>
<#assign bdc=result.bdc!/>
<#assign fixed=decimal!"####.####"/>


<#--excel-->
<#assign xzexport= xz.resultStr!/>
<#assign ghexport = gh.excelData!/>
<#assign bpexport = bp.excelData!/>
<#assign gdexport = gd.excelData!/>
<#assign djexport = dj.excelData!/>
<#assign dzexport = dz.excelData!/>
<#assign zdexport = zd.excelData!/>
<#assign kcexport = kc.excelData!/>
<#assign clexport = cl.excelData!/>
<#assign spexport = sp.excelData!/>
<#assign ntexport = nt.excelData!/>
<#assign gyjsydfxexport = gyjsydfx.excelData!/>
<#assign bdcexport = bdc.excelData!/>

<#--根据分析的fid以及组织好的value对象 返回要显示的报告内容-->
<#macro generateSummaryText fid value number fixed>
    <#if value.total &gt;0||number&gt;0>
        <#switch fid>
            <#case "xz">
                本地块与${value.alias!}图层叠加，压盖农用地<span class="number">${(value.nyd?number)?string(fixed)}</span>m
                <sup>2</sup>,
                压盖建设用地<span class="number">${(value.jsyd?number)?string(fixed)}</span>m<sup>2</sup>,压盖未利用地<span
                    class="number">${(value.wlyd?number)?string(fixed)}</span>m<sup>2</sup>,
                合计
                <span class="number"><#if value.total?is_number>${value.total!?string(fixed)}<#else>${(value.total?number)?string(fixed)}</#if>
            </span>m<sup>2</sup>.
                <#break/>
            <#case "gh">
                本地块与${value.alias!}图层叠加分析,规划土地用途区图层面积<span class="number">${(value.tdytq?number)?string(fixed)}</span>m
                <sup>2</sup>,其中,
                基本农田保护区<span class="number">${(value.jbntbhq?number)?string(fixed)}</span>m<sup>2</sup>,
                一般农用地<span class="number">${(value.ybndq?number)?string(fixed)}</span>m<sup>2</sup>,
                城镇建设用地区<span class="number">${(value.csjsydq?number)?string(fixed)}</span>m<sup>2</sup>,
                村镇建设用地<span class="number">${(value.czjsydq?number)?string(fixed)}</span>m<sup>2</sup>,
                独立工矿用地区<span class="number">${(value.dlgkydq?number)?string(fixed)}</span>m<sup>2</sup>,
                风景旅游用地区<span class="number">${(value.fjlyydq?number)?string(fixed)}</span>m<sup>2</sup>,
                生态环境安全控制区<span class="number">${(value.sthjaqkzq?number)?string(fixed)}</span>m<sup>2</sup>,
                自然与文化遗产保护区<span class="number">${(value.zrywhycbhq?number)?string(fixed)}</span>m<sup>2</sup>,
                林业用地区<span class="number">${(value.lyydq?number)?string(fixed)}</span>m<sup>2</sup>,
                牧业用地区<span class="number">${(value.myydq?number)?string(fixed)}</span>m<sup>2</sup>,
                其他用地区<span class="number">${(value.qtydq?number)?string(fixed)}</span>m<sup>2</sup>;
                规划建设用地管制区图层面积<span class="number">${(value.jsydgzq?number)?string(fixed)}</span>m<sup>2</sup>,
                其中，允许建设用地区<span class="number">${(value.yxjsydq?number)?string(fixed)}</span>m<sup>2</sup>,
                有条件建设用地区<span class="number">${(value.ytjjsydq?number)?string(fixed)}</span>m<sup>2</sup>,
                限制建设用地区<span class="number">${(value.xzjsydq?number)?string(fixed)}</span>m<sup>2</sup>,
                禁止建设用地区<span class="number">${(value.jzjsydq?number)?string(fixed)}</span>m<sup>2</sup>.
                <#break/>
            <#case "bp">
                本地块与${value.alias!}图层叠加，压盖已报<span class="number">${(value.yb?number)?string(fixed)}</span>m<sup>2</sup>,
                压盖未报<span class="number">${(value.wb?number)?string(fixed)}</span>m<sup>2</sup>,合计<span
                    class="number">${(value.total!0)?string(fixed)}</span>m<sup>2</sup>.
                <#break/>
            <#case "gd">
                本地块与${value.alias!}图层叠加，压盖已供<span class="number">${(value.yg?number)?string(fixed)}</span>m<sup>2</sup>,
                压盖未供<span class="number">${(value.wg?number)?string(fixed)}</span>m<sup>2</sup>,合计<span
                    class="number">${(value.total!0)?string(fixed)}</span>m<sup>2</sup>.
                <#break/>
            <#case "cl">
                本地块与${value.alias!}图层叠加,压盖<span class="number">${(value.total!0)?string(fixed)}</span>个.
                <#break/>
            <#case "sp">
                本地块与${value.alias!}图层叠加,压盖<span class="number">${(value.total!0)?string(fixed)}</span>个.
            <#case "kc">
                本地块与${value.alias!}图层叠加,压盖<span class="number">${(number!0)?string(fixed)}</span>个.
                <#break/>
            <#case "tdlygh">
                本地块与${value.alias!}相关图层叠加,符合规划面积<span class="number">${(value.fhgh?number)?string(fixed)}
            </span>m<sup>2</sup>,
                不符合规划面积<span class="number">${(value.bfhgh?number)?string(fixed)}</span>m<sup>2</sup>,属于有条件建设区面积<span
                    class="number">${(value.ytj?number)?string(fixed)}</span>m<sup>2</sup>
                <#break/>
            <#case "gyjsydfx">
                本地块与${value.alias!}图层叠加,压盖<span class="number">${(value.total!0)?string(fixed)}</span>m<sup>2</sup>.
                <#break/>
            <#case "bdc">
                本地块与${value.alias!}图层叠加,${value.analysisResult!}
                <#break/>
            <#default>
                本地块与${value.alias!}图层叠加,压盖<span class="number">${(value.total!0)?string(fixed)}</span>m<sup>2</sup>.
                <#break/>
        </#switch>
    <#else >
        本地块与${value.alias!}图层无压盖.
    </#if>
</#macro>
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

        ::-webkit-input-placeholder {
            font-style: italic;
            font-size: 12px;
            color: #999;
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
            min-width: 80px;
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

        .large {
            min-height: 572px;
            border: 0px !important;
            border-radius: 4px;
            overflow: hidden;
            word-break: break-all;
        }

        .tab-content {
            padding: 20px;
            border: 1px solid #ddd;
            border-top: none;
            border-radius: 0 0 5px 5px;
        }

        .nav-tabs {
            margin-bottom: 0px;
        }

        .detailPanel {
            display: none;
            z-index: 10000;
            background-color: #fafafa;
            width: 675px;
            min-height: 200px;
            max-height: 630px;
            border: 1px solid #666;
            /* CSS3 styling for latest browsers */
            -moz-box-shadow: 0 0 90px 5px #000;
            -webkit-box-shadow: 0 0 90px #000;
        }

        .close {
            background-image: url("<@com.rootPath/>/img/overlay_close.png");
            position: absolute;
            right: 0px;
            top: -5px;
            cursor: pointer;
            height: 32px;
            width: 32px;
        }

        h5 {
            font-weight: normal;
            color: #000;
            margin-left: 10px;
        }

        a:hover {
            color: purple;
        }

        .number {
            font-size: 23px;
            color: #1c50b0;
            padding: 0 5px;
        }

        .summary {
            font-size: 16px;
        }

        .row {
            margin-left: 2px;
        }

        .left-menu {
            float: left;
            width: 220px;
        }

        .opt-btn-wrapper {
            float: right;
            margin-top: 20px;
            margin-right: 83px;
        }

        .opt-btn-wrapper input {
            color: #188074;
            margin-left: 10px;
        }

        .ret-title {
            font-weight: normal;
            color: #188074
        }

        .summary-out-wrapper {
            text-align: center;
            padding-top: 20px;
        }

        #bdc {
            max-height: 580px;
            overflow-y: auto;
        }

        .bdcDetail > table tr td {
            border: 1px solid #ddd;
            padding: 5px 4px;
            height: 50px;
            text-align: center;
            min-width: 100px;
        }

    </style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
    <div class="container" style="width: 1240px;">
        <#--header start-->
        <div class="row">
            <div class="pull-left">
                <h3 class="ret-title"><span class="icon icon-columns"></span>&nbsp;综合分析结果展示</h3>
            </div>
            <div class="opt-btn-wrapper">
                <input type="button" value="导出excel" onclick="javascript:exportExcel();"/>
                <input type="button" value="导出图形" onclick="javascript:exportShpOrDwg();"/>
                <input type="button" value="新页面查看" onclick="javascript:newPage();"/>
                <#if exportDiffShp==true>
                    <input type="button" value="导出不重叠图形" onclick="javascript:exportDiffShpOrDwg()">
                    <input type="button" value="导出均不重叠图形" onclick="javascript:exportAllDiffShpOrDwg()">
                </#if>
            </div>
        </div>
        <#--header end-->
        <div class="row">
            <div class="left-menu">
                <ul class="nav nav-pills nav-stacked">
                    <#list result?keys as key>
                        <#assign value=result[key]!/>
                        <li <#if key_index==0>class="active"</#if>>
                            <a href="#${key}" data-toggle="tab">${value["alias"]!}</a>
                        </li>
                    </#list>
                    <#--<li><a href="#map" data-toggle="tab">重叠图形</a></li>-->
                    <#if result??>
                        <li><a href="#Summary" data-toggle="tab">分析报告</a></li>
                    </#if>
                </ul>
            </div>
            <div class="span12">
                <div id="resultContainer" class="tab-content large">
                    <#list result?keys as key>
                        <#assign item=result[key]!/>
                        <div id="${key!}" class="tab-pane fade in <#if key_index==0>active</#if>"
                             data-exceldata='${item.excelData!}'>
                            ${item.tpl!}
                        </div>
                    </#list>
                    <div id="Summary" class="tab-pane fade in">
                        <div class="well well-lg summary">
                            <#if result??>
                                <ol>
                                    <#if ogArea??>
                                        <li>本地块面积 <span class="number">${(ogArea!)?string(fixed)}</span>m<sup>2</sup>
                                        </li>
                                    </#if>
                                    <#list result?keys as key>
                                        <#assign obj=result[key]!/>
                                        <#assign number = 0/>
                                        <#if obj.result??>
                                            <#assign number = obj.result?size/>
                                        </#if>
                                        <li><@generateSummaryText fid=key value=obj["summary"] number=number fixed=fixed/></li>
                                    </#list>
                                </ol>
                            <#else >无分析报告
                            </#if>
                        </div>
                        <#if result??>
                            <div class="summary-out-wrapper">
                                <a class="btn btn-primary" onclick="exportWord();">导出报告</a>
                            </div>
                        </#if>
                    </div>

                    <#--<iframe name="view_map" style="width: 100%;height: 100%;min-height: 672px;" frameborder="no" border="0"-->
                    <#--marginwidth="0"-->
                    <#--marginheight="0" scrolling="yes" allowtransparency="yes" src="<@com.rootPath/>/map/${tpl!}?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22urlLocation%22,%22params%22:{%22url%22:%22javascript:getCoords()%22}}"></iframe>-->
                    <#--</div>-->
                </div>
            </div>
        </div>
    </div>
    <script src="<@com.rootPath/>/static/thirdparty/jquery/overlay.min.js"></script>

    <script type="text/javascript">

        /**
         * 分析结果页面的目标 name 默认是打开新页面
         * 可传 iframe 名称
         */
       var targetName= '_blank';

        //eg. {'gh':'xxxx'}
        var shpHash = $.parseJSON('${shp!}');
        var geoJson = $.parseJSON('${geoJson!}');
        //进入方法渲染图层
        heightLightFeature(geoJson);

        //当前选中的左侧分类id
        var activeId = undefined;

        $(document).ready(function () {
            activeId = getActiveId();
            //bs tooltip
            $("[data-toggle='tooltip']").tooltip({
                html: true,
                trigger: 'click'
            });

            //左侧分类项点击事件
            $(".left-menu .nav").find("li a").on('click', function () {
                var href = $(this).attr("href");
                activeId = href.split('#')[1];
                if (activeId === 'Summary') {
                    $(".opt-btn-wrapper").hide();
                } else {
                    $(".opt-btn-wrapper").show();
                }
            });

            //不动产分析结果详情渲染
            $(".bdcDetail").height(document.body.clientHeight - 130);
            $(".bdcDetail").css("overflow-y", "auto");
        });

        /***
         * 返回分析的坐标串 geojson 格式
         * */
        function getCoords() {
            return ${geo!};
        }

        /***
         * 获取当前选中的分析id
         * */
        function getActiveId() {
            var $sel = $(".left-menu .nav").find("li[class='active'] a").eq(0);
            if ($sel != undefined) {
                var href = $sel.attr("href");
                return href.split('#')[1];
            }
        }

        /**
         * 导出word(分析报告)
         * */
        function exportWord() {
            var content = [];
            var $liArr = $("#Summary").find("ol").find("li");
            $.each($liArr, function (idx, item) {
                var text = $(item).text();
                var val = (idx + 1) + ":" + ($.trim(text)).replace(/\\\n/g, "").replace(/m2/g, " 平方米");
                content.push(val);
            });
            setTimeout(function () {
                if (content.length > 0) {
                    openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify({data: content}),
                        "multi_report_n.xml", "doc");
                }
            }, 500);
        }

        /**
         * 导出excel
         */
        function exportExcel() {
            var fileType = ".xlsx";
            var officeVersion = '${env.getEnv("office.plugin.version")!}';
            if (officeVersion === 'old') {
                fileType = ".xls";
            }
            switch (activeId) {
                case 'bdc':
                <#if result.bdc??>
                    openPostWindowXZ("<@com.rootPath/>/geometryService/export/analysis", '${result.bdc.excelData!}', "bdc.xml");
                </#if>
                    break;
                case 'xz':
                <#if result.xz??>
                    var xzData = ${xz.resultStr!};
                    // 添加是否显示权属单位名称参数
                    xzData.isShowQsdwmc = '${env.getEnv("tdlyxz.showQsdwmc")!}';
                    openPostWindowXZ("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(xzData), "tdlyxz.xml");
                </#if>
                    break;
                case 'gh':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${ghexport}', "ghscfx_analysis" + fileType);
                    break;
                case 'bp':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${bpexport}', "bpfx_analysis" + fileType);
                    break;
                case 'gd':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${gdexport}', "gdfx_analysis" + fileType);
                    break;
                case 'kc':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${kcexport}', "kcfx_analysis" + fileType);
                    break;
                case 'dz':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${dzexport}', "dzfx_analysis" + fileType);
                    break;
                case 'zd':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${zdexport}', "zdfx_analysis" + fileType);
                    break;
                case 'cl':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${clexport}', "clbzdfx_analysis" + fileType);
                    break;
                case 'sp':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${spexport}', "spkzdfx_analysis" + fileType);
                    break;
                case 'nt':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${ntexport}', "jbntfx_analysis" + fileType);
                    break;
                case 'dj':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${djexport}', "dj_analysis" + fileType);
                    break;
                case 'gyjsydfx':
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${gyjsydfxexport}', "gyjsyd_analysis" + fileType);
                    break;
                default: {
                    var item = $("#resultContainer").children(".active")[0];
                    var excelData = $(item).data("exceldata");
                    openPostWindow("<@com.rootPath/>/geometryService/export/excel", JSON.stringify(excelData), "default" + fileType);
                    break;
                }
            }
        }


        function gotoLocation(data, key) {
            window.parent.doBottomLoc(data);
        }

        function heightLightFeature(geojson) {
            window.parent.heightLightFeature(geojson);
        }

        /***
         * detail info
         * */
        function detailInfo(index) {
            $("td[rel]").overlay({
                closeOnClick: true
            });
        }

        /***
         * export excel
         * @param url
         * @param data
         * @param fileName
         */
        function openPostWindow(url, data, fileName, fileType) {
            if (data === null || data === 'null' || data === undefined)
                alert("无分析数据可导出!");
            else {
                //转换data格式删除额外的引起报错的导出数据
                data = JSON.parse(data);
                var haveShape = -1;
                for (var key in data) {
                    for (var i = 0; i < data[key][0].length; i++) {
                        if (data[key][0][i] === "SHAPE") {
                            haveShape = i;
                        }
                    }
                    if (haveShape !== -1) {
                        data[key].forEach(function (temp) {
                            temp.splice(haveShape, 1);
                        });
                    }
                }
                data = JSON.stringify(data);
                //替换字符串中的\\ 会引起freemarker解析错误
                data = data.replace(/\\/g, '');

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

                var hideInput3 = document.createElement("input");
                hideInput3.type = "hidden";
                hideInput3.name = "fileType";
                hideInput3.value = fileType;

                tempForm.appendChild(hideInput1);
                if (fileName != null && fileName != "null" && fileName != "")
                    tempForm.appendChild(hideInput2);

                if (fileType != null && fileType != "null" && fileType != "")
                    tempForm.appendChild(hideInput3);

                document.body.appendChild(tempForm);
                tempForm.submit();
                document.body.removeChild(tempForm);
            }
        }

        /***
         * for tdlyxz
         * @param url
         * @param data
         * @param fileName
         */
        function openPostWindowXZ(url, data, fileName) {
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

        /**
         * 导出图形
         * */
        function exportShpOrDwg() {
            var shpId = shpHash[activeId];
            if (shpId === undefined) {
                alert("当前分析无导出图形数据！");
            } else {
                exportFeatures(shpId, 0);
            }
        }

        /**
         * 在新页面查看
         */
        function newPage() {
            debugger;
            var url = '${path_omp!'/omp'}/geometryService/analysis/multiple/newPage';
            var shp=JSON.stringify(${shp!});
            var resultJSon=JSON.stringify(${resultJSon!});

            _execute(url, {
                shp: shp,
                result:resultJSon ,
                decimal: JSON.stringify(${decimal!}),
                geometry: JSON.stringify(${geometry!}),
                diffShpId: JSON.stringify(${diffShpId!}),
                iframeData: JSON.stringify(${iframeData!}),
                exportDiffShp:JSON.stringify(${exportDiffShpStr!}),
                // exportDiffShp:false,
                summaryData: JSON.stringify(${summaryData!}),
                ogArea: JSON.stringify(${ogAreaJson!}),
                geoJson: JSON.stringify(${geoJson!})
            });

        }


        function _execute(a, b) {
            var form = $("<form method='post' style='display:none;' target='" + targetName + "'></form>"),
                input;
            form.attr({"action": a});
            $.each(b, function (key, value) {
                input = $("<input type='hidden'>");
                input.attr({"name": key});
                input.val(value);
                form.append(input);
            });
            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
        }


        /**
         *导出不重叠
         */
        function exportDiffShpOrDwg() {
            if (activeId != "bp" && activeId != "gyjsydfx") {
                alert("仅支持报批分析和国有建设用地分析！");
                return;
            }
            var shpId = shpHash[activeId + "_diff"];
            if (shpId === undefined) {
                alert("当前分析无不重叠图形数据！");
            } else {
                exportFeatures(shpId, 0);
            }
        }

        /**
         * 导出与各图层均不重叠图形
         */
        function exportAllDiffShpOrDwg() {
            exportFeatures('${diffShpId!}', 0);
        }

        /**
         * 导出shp/dwg
         * @param type
         * */
        function exportFeatures(shpId, type) {
            $("[data-toggle='tooltip']").tooltip('hide');
            if (shpId == null || shpId == '') {
                alert("shpId为空 无法执行导出操作");
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
                        url: '${path_omp!'/omp'}/geometryService/rest/export/dwg',
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

    </script>
</@aBase.tpl>

