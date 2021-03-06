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

        .number {
            font-size: 23px;
            color: #1c50b0;
            padding: 0 5px;
        }

    </style>
</#assign>
<#assign colName= result.colName!/>
<#assign fxbgResult= result.fxbgResult!/>
<#assign processList= result.processList!/>
<#assign sumArea= result.sumArea!/>
<#assign fixed=decimal!"####.####"/>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">

    <div class="container header">
        <h3><span class="icon icon-dashboard"/>&nbsp;
            ??????????????????????????????
            <h3>
                <div class="pull-right">
                    <h5 style="margin-right: 30px">??????:?????????
                    </h5>
                </div>
    </div>
    <div class="container wrapper">
        <div class="row">
            <div class="tab-pane fade in active">
                <div class="container basic-container">
                    <div class="row">
                        <div>
                            <ul class="nav nav-tabs">
                                <li class="dropdown active yjfl"><a href="#tabcxgh1"
                                                                    class="dropdown-toggle"
                                                                    data-toggle="tab">????????????</a></li>
                                <li class="dropdown yjfl"><a href="#tabcxgh2"
                                                             class="dropdown-toggle"
                                                             data-toggle="tab">????????????</a></li>
                            </ul>
                        </div>
                        <div class="tab-content">
                            <div id="tabcxgh1" class="tab-pane fade in active">
                                <table>
                                    <tr>
                                        <th>???????????????</th>
                                        <th>??????</th>
                                        <#list colName?keys as key>
                                            <th>${colName[key]!}</th>
                                        </#list>
                                    </tr>
                                    <#list processList as processData>
                                        <#assign totalAreaLast=processData.totalAreaLast!/>
                                        <#if !totalAreaLast?has_content>
                                            <tr>
                                                <td>${processData["xzqhmc"]!}</td>
                                                <td>${processData["totalArea"]!}</td>
                                                <#list colName?keys as key>
                                                    <td>${processData[key]!}</td>
                                                </#list>
                                            </tr>
                                        </#if>
                                    </#list>
                                    <#list processList as processData>
                                        <#assign totalAreaLast=processData.totalAreaLast!/>
                                        <#if totalAreaLast?has_content>
                                            <tr>
                                                <td>${processData["xzqhmc"]!}</td>
                                                <td>${processData["totalAreaLast"]!}</td>
                                                <#list colName?keys as key>
                                                    <td>${processData[key]!}</td>
                                                </#list>
                                            </tr>
                                        </#if>
                                    </#list>
                                </table>
                            </div>
                            <div id="tabcxgh2" class="tab-pane fade in">
                                <div class="well well-lg summary">
                                    <#if fxbgResult??>
                                        <ol>
                                            <#if sumArea??>
                                                <li>??????????????? <span
                                                            class="number">${(sumArea!)?string(fixed)}</span>m<sup>2</sup>???
                                                </li>
                                            </#if>
                                            <li>???????????????????????????????????????
                                                <#list fxbgResult?keys as key>
                                                    <#if !key_has_next>
                                                        ?????? <span
                                                            class="number">${key!}${(fxbgResult[key]!)?string(fixed)}</span>m<sup>2</sup>???
                                                    <#else>
                                                        ??????<span
                                                            class="number">${key!}${(fxbgResult[key]!)?string(fixed)}</span>m<sup>2</sup>???
                                                    </#if>
                                                </#list>
                                            </li>
                                        </ol>
                                    <#else >???????????????
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="export-container text-center">
                        <a class="btn btn-primary" data-excel='${reportExcel!}'  onclick="exportExcel(this,0);">?????? excel</a>
                        <a class="btn btn-primary"  onclick="exportWord();">????????????</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <script src="<@com.rootPath/>/static/thirdparty/jquery/jquery.tools.min.js"></script>
    <script type="text/javascript">
        /**
         * ?????????????????????excel
         */
        function exportExcel(f) {
            var data = $(f).data('excel');
            openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify(data), "cxghfx_bb.xml");
        }

        /**
         * ??????word(????????????)
         * */
        function exportWord() {
            var content = [];
            var $liArr = $("#tabcxgh2").find("ol").find("li");
            $.each($liArr, function (idx, item) {
                var text = $(item).text();
                var val = (idx + 1) + ":" + ($.trim(text)).replace(/\\\n/g, "").replace(/m2/g, " ?????????");
                content.push(val);
            });
            setTimeout(function () {
                if (content.length > 0) {
                    openPostWindow("<@com.rootPath/>/geometryService/export/analysis", JSON.stringify({data: content}),
                        "cxghfx_bb_doc.xml", "doc");
                }
            }, 500);
        }


        /***
         * export excel
         * @param url
         * @param data
         * @param fileName
         */
        function openPostWindow(url, data, fileName, fileType) {
            if (data === null || data === 'null' || data === undefined)
                alert("????????????????????????!");
            else {
                //??????data????????????????????????????????????????????????
                data = JSON.parse(data);
                data = JSON.stringify(data);
                //?????????????????????\\ ?????????freemarker????????????
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
    </script>
</@aBase.tpl>