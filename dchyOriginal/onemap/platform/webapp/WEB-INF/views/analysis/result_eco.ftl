<#assign cssContent>
    <style>
        table tr td {
            border: 1px solid #bbbbbb;
            padding: 5px 10px;

        }

        table tr th {
            border: 1px solid #bbbbbb;
            padding: 10px;
            background-color: #cccccc;
        }

        table {
            background-color: transparent;
            border: 1px solid #bbbbbb;
            table-layout: auto;
            border-collapse: collapse;
            width: 90%;
            margin-left: auto;
            margin-right: auto;
            /*margin-top: 20px;*/
            /*margin-bottom: 10px;*/
        }

        .large {
            min-height: 572px;
            border: 1px solid #ddd;
            border-radius: 4px;
            overflow: hidden;
            word-break: break-all;
        }
        h5 {
            font-weight: normal;
            color: #000;
            margin-left: 10px;
        }

    </style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
    <#assign unit = unit!>
    <div class="container">
        <div>
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;生态分析结果展示</h3>
        </div>
        <div style="float:right;margin-bottom: 10px;margin-top: -40px;margin-right: 20px;">
            <input type="button" value="导出excel" onclick="javascript:exportExcel()" style="color:#188074;"/></div>
        <div class="row">
            <div style="width:220px;float:left;">
                <ul class="nav nav-pills nav-stacked">
                    <li class="active"><a href="#ECOSJ" data-toggle="tab">省级红线图</a></li>
                    <li><a href="#ECOGJ" data-toggle="tab">国家级红线图</a></li>
                </ul>
            </div>
            <div class="span9">
                <div class="tab-content large">
                    <div id="ECOSJ" class="tab-pane fade in active">
                        <table>
                            <#if result.SJ??>
                                <tr>
                                    <th>序号</th>
                                    <th>图斑类型</th>
                                    <th>相交面积(m<sup>2</sup>)</th>
                                </tr>
                                <#list result.SJ as item>
                                    <tr>
                                        <td>${item_index+1}</td>
                                        <td>${item["MC"]!}</td>
                                        <td>${item["SHAPE_AREA"]!?string("${decimal!'####.####'}")}</td>
                                    </tr>
                                </#list>
                            <#else >
                                <div style="text-align: center"><h4>无分析结果</h4></div>
                            </#if>
                        </table>
                    </div>
                    <div id="ECOGJ" class="tab-pane fade">
                        <table>
                            <#if result.GJ??>
                                <tr>
                                    <th>序号</th>
                                    <th>图斑类型</th>
                                    <th>相交面积(m<sup>2</sup>)</th>
                                </tr>
                                <#list result.GJ as item>
                                    <tr>
                                        <td>${item_index+1}</td>
                                        <td>${item["HXQMC"]!}</td>
                                        <td>${item["SHAPE_AREA"]!?string("${decimal!'####.####'}")}</td>
                                    </tr>
                                </#list>
                            <#else >
                                <div style="text-align: center"><h4>无分析结果</h4></div>
                            </#if>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<@com.rootPath/>/static/thirdparty/jquery/jquery.tools.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if (window.event.keyCode==13){
                alert(1);
            }
        });
        function detailInfo(index) {

            $("td[rel]").overlay({
                closeOnClick: true
//              close:null

            });
        }

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
         *
         * 导出excel
         */
        function exportExcel() {
            var fileName = "eco_analysis.xlsx";
            var officeVersion = '${env.getEnv("office.plugin.version")!}';
            if (officeVersion == 'old')
                fileName = "eco_analysis.xls";
            openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${excelData!}', fileName);
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
