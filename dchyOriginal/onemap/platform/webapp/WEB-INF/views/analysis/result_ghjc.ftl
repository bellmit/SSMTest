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

        .detailPanel {
            display: none;

            z-index: 10000;

            background-color: #fafafa;

            width: 675px;
            min-height: 200px;
            max-height: 630px;
            /*overflow-y: auto;*/
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

        .summary {
            font-size: 16px;
        }

        .number {
            font-size: 20px;
            color: #1c50b0;
            padding: 0 5px;
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
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;规划检查分析结果展示</h3>
        </div>
        <div>该地块
            <#if Pass["Pass"]=="符合要求">
            <sapn style="font-size: 20px;
            color: #1c50b0;">${(Pass["Pass"])}</sapn>
                <#else>
                    <sapn style="font-size: 20px;
            color: #b02014;">${(Pass["Pass"])}</sapn>
            </#if>
            地块总面积为 <span class="number">${(Pass["总面积"]!)?string("0.####")}</span>m<sup>2</sup>，
        </div>
        <div>
            永久农田占用面积为<span class="number">${(Pass["永久农田占用"]!)?string("0.####")}</span>m<sup>2</sup>，
        </div>
        <div style="margin-bottom: 10px" >
            不在允许建设用地区也不在重点建设项目区内面积<span class="number">${(Pass["未占用"]!)?string("0.####")}</span>m<sup>2</sup>，占比<span class="number">${(Pass["占比"]!)?string("0.####")}%</span>。
        </div>
        <div class="row">
            <div style="width:220px;float:left;">
                <ul class="nav nav-pills nav-stacked">
                    <li class="active"><a href="#TDYTQInfo" data-toggle="tab">土地用途分区</a></li>
                    <li><a href="#JSYDGZQInfo" data-toggle="tab">建设用地管制区</a></li>
                    <#if localVersion?? && localVersion == "heb">
                    <#else>
                        <li><a href="#GHJBNTTZInfo" data-toggle="tab">规划基本农田调整</a></li>
                        <li><a href="#MZZDJSXMInfo" data-toggle="tab">重点建设项目</a></li>
                    </#if>
                </ul>
            </div>
            <div class="span9">
                <div class="tab-content large">
                    <div id="TDYTQInfo"
                         class="tab-pane fade in active"><@com.tdghsc3 key="土地用途分区" unit = unit decimal = "${decimal!'####.####'}"></@com.tdghsc3></div>
                    <div id="JSYDGZQInfo"
                         class="tab-pane fade">
                        <table style="margin-top: 20px;">
                            <#if result["建设用地管制区"]??>
                                <tr>
                                    <th>类别</th>
                                    <th>细类</th>
                                    <th>所占面积(m<sup>2</sup>)</th>
                                    <th>所占比例%</th>
                                </tr>
                                <#list result["建设用地管制区"].info[1..] as item>
                                    <#if item["LXMC"]=="允许建设用地区">
                                        <tr>
                                            <td rowspan="4">
                                                ${item["LXMC"]!}
                                            </td>
                                            <td>${item["detail"][0]["名称"]!}</td>
                                            <td>${item["detail"][0]["面积"]!}</td>
                                            <td>${item["detail"][0]["比例"]!}</td>
                                        </tr>
                                        <tr>
                                            <td>${item["detail"][1]["名称"]!}</td>
                                            <td>${item["detail"][1]["面积"]!}</td>
                                            <td>${item["detail"][1]["比例"]!}</td>
                                        </tr>
                                        <tr>
                                            <td>${item["detail"][2]["名称"]!}</td>
                                            <td>${item["detail"][2]["面积"]!}</td>
                                            <td>${item["detail"][2]["比例"]!}</td>
                                        </tr>
                                        <tr>
                                            <td>${item["detail"][3]["名称"]!}</td>
                                            <td>${item["detail"][3]["面积"]!}</td>
                                            <td>${item["detail"][3]["比例"]!}</td>
                                        </tr>
                                    <#else>
                                        <tr>
                                            <td colspan="2">${item["LXMC"]!}</td>
                                            <td>${item["AREA"]!?string("${decimal!'####.####'}")}</td>
                                            <td>${item["PER"]!?string("${decimal!'####.####'}")}</td>
                                        </tr>
                                    </#if>
                                    <tr>

                                    </tr>

                                </#list>
                            <#else >
                                <div style="text-align: center"><h4>无分析结果</h4></div>
                            </#if>
                        </table>
                    </div>
                    <#if localVersion?? && localVersion == "heb">
                    <#else>
                        <div id="GHJBNTTZInfo"
                             class="tab-pane fade"><@com.tdghsc3 key="规划基本农田调整" unit = unit decimal = "${decimal!'####.####'}"></@com.tdghsc3></div>
                        <div id="MZZDJSXMInfo"
                             class="tab-pane fade"><@com.tdghsc3 key="重点建设项目" unit = unit decimal = "${decimal!'####.####'}"></@com.tdghsc3></div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <script src="<@com.rootPath/>/static/thirdparty/jquery/jquery.tools.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if (window.event.keyCode == 13) {
                alert(1);
            }
        });


        function exportFeature(o, type) {
            var data = $(o).data("geo");

            var shpUrl = '${path_omp!'/omp'}/file/download/'.concat(data);
            var gpUrl = '${env.getEnv('dwg.exp.url')!}';
            var txtUrl = '${path_omp!'/omp'}/geometryService/export/txt';
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
                case 3:
                    var url = '<@com.rootPath/>/geometryService/export/txt';
                    var dataTxt = JSON.stringify({coords: data});
                    if (dataTxt == "") {
                        alert("无导出数据!");
                        return;
                    }
                    var tempForm = document.createElement("form");
                    tempForm.method = "post";
                    tempForm.action = url;
                    var hideInput1 = document.createElement("input");
                    hideInput1.type = "hidden";
                    hideInput1.name = "data";
                    hideInput1.value = dataTxt;
                    var hideInput2 = document.createElement("input");
                    hideInput2.type = "hidden";
                    hideInput2.name = "fileName";
                    hideInput2.value = "coords.txt";
                    var hideInput3 = document.createElement("input");
                    hideInput3.type = "hidden";
                    hideInput3.name = "type";
                    hideInput3.value = "txt";
                    tempForm.appendChild(hideInput1);
                    tempForm.appendChild(hideInput2);
                    tempForm.appendChild(hideInput3);
                    document.body.appendChild(tempForm);
                    tempForm.submit();
                    document.body.removeChild(tempForm);
            }
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

    </script>
</@aBase.tpl>
