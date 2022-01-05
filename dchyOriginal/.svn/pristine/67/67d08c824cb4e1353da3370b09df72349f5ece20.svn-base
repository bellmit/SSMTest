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
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;中心城区分析结果展示</h3>
        </div>
        <div class="row">
            <div class="span9">
                <div class="tab-content large">
                    <div class="tab-pane fade in active">
                        <table>
                            <#if result??>
                                <tr>
                                    <th>图形面积</th>
                                    <th>中心城区内</th>
                                    <th>中心城区外(m<sup>2</sup>)</th>
                                </tr>
                                <#list result as item>
                                    <tr>
                                        <td>${item["IN_SHAPE_AREA"]!?string("${decimal!'####.####'}")}</td>
                                        <td>${item["AREA"]!?string("${decimal!'####.####'}")}</td>
                                        <td>${(item["IN_SHAPE_AREA"]-item["AREA"])!?string("${decimal!'####.####'}")}</td>
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
    </script>
</@aBase.tpl>
