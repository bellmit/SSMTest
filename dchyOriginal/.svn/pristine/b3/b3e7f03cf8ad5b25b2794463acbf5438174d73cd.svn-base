<#assign cssContent>
<style>
    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
    }

    table {
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
    }

    .nav-tabs {
        margin-bottom: 0px;
    }

    .detailPanel {
        display: none;
        padding: 10px;
    }
</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
<div class="container">
    <div>
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;分析结果展示</h3>
    </div>
    <#if general??>
        <div class="well" style="color: #188074;font-size: 15px;">图形面积:&nbsp;${general.geoArea!?string('0.####')}&nbsp;m<sup>2</sup>&nbsp;&nbsp;未覆盖面积:&nbsp;${general.unCoverArea!?string('0.####')}
            &nbsp;m<sup>2</sup>覆盖总面积:&nbsp;${general.analysisArea!?string('0.####')}&nbsp;m<sup>2</sup></div>
    </#if>
    <#if iframeUrl??>
        <iframe name="iframe" style="width: 100%;height: 100%;min-height: 650px;" frameborder="no"
                border="0"
                marginwidth="0"
                marginheight="0" scrolling="no" allowtransparency="yes">

        </iframe>
    <#else >
        <div style="margin: 20px auto;">
            <ul class="nav nav-tabs">
                <#list info as parent>
                    <li <#if parent_index==0>class="active"</#if>><a href="#info${parent_index}"
                                                                     data-toggle="tab">${parent["alias"]!}</a></li>
                </#list>
            </ul>
            <div class="tab-content">

                <#list info as parent>
                    <div id="info${parent_index}" class="tab-pane fade <#if parent_index==0> in active</#if>">
                        <h4 style="font-weight: normal; color:#188074"><span
                                class="icon icon-double-angle-right"></span>
                            分类面积 ${parent["value"]!?string("0.####")}&nbsp;m<sup>2</sup></a></h4>
                        <h4 style="font-weight: normal; color:#188074">
                            <span class="icon icon-double-angle-right toggle" style="cursor:pointer;"></span> 详细信息</h4>

                        <div class="detailPanel">
                            <#assign children=parent.children>
                            <table>
                                <tr>
                                    <#assign firstChild=children[0]>
                                    <th>序号</th>
                                    <#list firstChild as field>
                                        <#if field.name?starts_with("IN_") || field.name?starts_with("OG_")>
                                        <#else >
                                            <th>${field.alias}</th>
                                        </#if>
                                    </#list>
                                </tr>
                                <#list children as child>
                                    <tr>
                                        <td>${child_index+1}</td>
                                        <#list child as f>
                                            <#if f.name?starts_with("IN_") || f.name?starts_with("OG_")>
                                            <#else >
                                                <td>
                                                    <#if f.value??>
				                            	    <#if f.value?is_date>
                                                    ${f.value?string("yyyy-MM-dd")}
                                                    <#else>
                                                    ${f.value!}
                                                    </#if>
				                            	 <#else>
                                                    </#if>
                                                </td>
                                            </#if>
                                        </#list>
                                    </tr>
                                </#list>
                            </table>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </#if>
    <div style="text-align: center;padding-top: 20px;">
        <button id="exportBtn" type="button" class="btn btn-primary" data-toggle="tooltip" data-placement="bottom"
                title="<div style='float:left;'><button class='btn btn-default' onclick='exportExcel(0);'>分组</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' onclick='exportExcel(1);'>列表</button></div>">
            导&nbsp;出Excel
        </button>
    </div>
</div>

<script type="text/javascript">

    var linkData = '${iframeData!}';
    var linkUrl = '${iframeUrl!}';

    $(document).ready(function () {
        if (linkUrl != '') {
            goLink(linkUrl, linkData);
        } else {
            $('.toggle').click(function () {
                $("[data-toggle='tooltip']").tooltip('hide');

                if ($('.toggle').hasClass('icon-double-angle-down'))
                    $('.toggle').addClass('icon-double-angle-right').removeClass('icon-double-angle-down');
                else
                    $('.toggle').addClass('icon-double-angle-down').removeClass('icon-double-angle-right');
                $('.detailPanel').slideToggle("slow");
            });
        }
        $("[data-toggle='tooltip']").tooltip({
            html: true,
            trigger: 'click'
        });
    });
    /***
     *
     * go to link if accessible
     * */
    function goLink(action, postData) {

        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = action;
        tempForm.target = "iframe";

        var hideInput = document.createElement("input");
        hideInput.type = 'hidden';
        hideInput.name = 'data';
        hideInput.value = postData;

        tempForm.appendChild(hideInput);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

    /**
     * 导出excel
     */
    function exportExcel(type) {
        switch (type) {
            case 0:
                openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${excelData}', null);
                break;
            case 1:
                openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${excelList}', null);
                break;
        }
        $("[data-toggle='tooltip']").tooltip('hide');
    }
    /**
     * open window with post data
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
