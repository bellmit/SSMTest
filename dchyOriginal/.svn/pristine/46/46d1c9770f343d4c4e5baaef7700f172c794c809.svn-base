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

    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: center;
        vertical-align: middle;
        min-width: 60px;
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

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">

    <#macro getSequence str>
        <#if str?index_of(",") gt 0>
            <#assign sequence=str?split(",")/>
        <ul class="ul inline">
            <#list sequence as item>
                <li>${item!}</li>
            </#list>
        </ul>
        <#else >${str!}</#if>
    </#macro>
<div class="container" style="width: 85%;">
    <div>
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;${year!}
            年国家遥感监测图斑核查情况一览表</h3>
    </div>
    <div class="pull-right" style="margin-top: -35px;">(单位：${unit.alias!'亩'})</div>
    <div style="margin: 20px auto;overflow-x: auto;">
        <table>
            <#if result??>
                <tr>
                    <th rowspan="3">定位</th>
                    <th rowspan="3">图斑编号</th>
                    <th rowspan="3">图斑类型</th>
                    <th rowspan="3">标识码</th>
                    <th rowspan="3">图斑位置</th>
                    <th rowspan="3">项目名称</th>
                    <th rowspan="3">建设单位</th>
                    <th colspan="5">监测情况</th>
                    <th colspan="7">批准情况</th>
                    <th colspan="6" rowspan="2">供地情况</th>
                    <th colspan="4">土地利用总体规划情况</th>
                    <th colspan="6">现场核查情况</th>
                    <th rowspan="3">处置意见</th>
                    <th rowspan="3">现场取证照片（超链接）</th>
                    <th rowspan="3">备注</th>
                </tr>
                <tr>
                    <th rowspan="2">面积</th>
                    <th colspan="2">农用地面积</th>
                    <th rowspan="2">建设用地面积</th>
                    <th rowspan="2">未利用地面积</th>
                    <th colspan="5">已批情况</th>
                    <th colspan="2">未批已建</th>
                    <th rowspan="2">允许建设区</th>
                    <th rowspan="2">有条件建设区</th>
                    <th rowspan="2">禁止和限制建设区</th>
                    <th rowspan="2">基本农田</th>
                    <th rowspan="2">图斑建设状况与类型</th>
                    <th colspan="2">未批已建</th>
                    <th colspan="3">实地伪变化</th>
                </tr>
                <tr>
                    <th>小计</th>
                    <th>耕地面积</th>
                    <th>面积</th>
                    <th>耕地面积</th>
                    <th>批次名称</th>
                    <th>农转用批文</th>
                    <th>获批档号</th>
                    <th>面积</th>
                    <th>耕地面积</th>
                    <th>已供面积</th>
                    <th>未供面积</th>
                    <th>供地编号</th>
                    <th>项目名称</th>
                    <th>用地单位</th>
                    <th>供地档号</th>
                    <th>面积</th>
                    <th>耕地面积</th>
                    <th>面积</th>
                    <th>耕地面积</th>
                    <th>原因类型</th>
                </tr>
                <#list result as item>
                    <tr>
                        <td><button class="btn btn-success btn-small" onclick=goLocation(${item.LOC!}); style="padding: 5px;background-color: #52A252;color: #fff;">定位</button></td>
                    <#--基本情况-->
                        <td>${item.OG_PRO_JCBH!}</td>
                        <td>${item.OG_PRO_TBLX!}</td>
                        <td>${item.OG_PRO_BSM!}</td>
                        <td>${item.OG_PRO_XZMC!}</td>
                        <td></td>
                        <td></td>
                    <#--监测情况-->
                        <td>${item.OG_PRO_JCMJ!}</td>
                        <td>${item.JC_NYD_AREA!}</td>
                        <td>${item.JC_GD_AREA!}</td>
                        <td>${item.JC_JSYD_AREA!}</td>
                        <td>${item.JC_WLYD_AREA!}</td>
                    <#--批准情况-->
                        <td>${item.BP_AREA!}</td>
                        <td>${item.BP_GD_AREA!}</td>
                        <td style="min-width: 130px;"><@getSequence item.PCH/></td>
                        <td style="min-width: 130px;"><@getSequence item.NZYPW/></td>
                        <td style="min-width: 130px;"><@getSequence item.DAH/></td>
                        <td>${item.WPYJ_AREA!}</td>
                        <td>${item.WPYJ_GD_AREA!}</td>
                    <#--供地情况-->
                        <td>${item.YG_AREA!}</td>
                        <td>${item.WG_AREA!}</td>
                        <td style="min-width: 80px;"><@getSequence item.GDBH/></td>
                        <td style="min-width: 130px;"><@getSequence item.XMMC/></td>
                        <td style="min-width: 130px;"><@getSequence item.YDDW/></td>
                        <td style="min-width: 130px;"><@getSequence item.CRBH/></td>
                    <#--土地利用总体规划情况-->
                        <td>${item.YXJSQ_AREA!}</td>
                        <td>${item.YTJJSQ_AREA!}</td>
                        <td>${item.JZHXZJSQ_AREA!}</td>
                        <td>${item.JBNT_AREA!}</td>
                    <#--现场核查情况-->
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    <#--意见照片备注-->
                        <td></td>
                        <td></td>
                        <td></td>

                    </tr>
                </#list>
            <#else >
                <div style="text-align: center"><h4>无分析结果</h4></div>
            </#if>
        </table>
    </div>
    <div style="text-align: center;padding-top: 20px;">
        <a class="btn btn-primary" onclick="exportExcel();">导出excel</a>
    </div>
</div>
    <script src="<@com.rootPath/>/static/js/JSONUtil.js"></script>
<script type="text/javascript">

    var data = '${resultStr}';
    /**
     * 导出excel
     */
    function exportExcel() {
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis", data, "jctb_mas.xml");
    }
    function openPostWindow(url, data, fileName) {
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
        hideInput3.name = "year";
        hideInput3.value = '${year!}';
        var hideInput4 = document.createElement("input");
        hideInput4.type = "hidden";
        hideInput4.name = "unit";
        hideInput4.value = '${unit.alias!}';
        tempForm.appendChild(hideInput1);
        if (fileName != null && fileName != "null" && fileName != "")
            tempForm.appendChild(hideInput2);
        tempForm.appendChild(hideInput3);
        tempForm.appendChild(hideInput4);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }
    function goLocation(geo) {
        if (geo != "" && geo != undefined) {
            var tpl = 'test111';
            var url = '<@com.rootPath/>/map/' + tpl + '?action=location&hideTopBar=true&hideLeftPanel=true&params=';
            var params = {};
            var locParam = {};
            locParam.type="FeatureCollection";
            params.type = "coordsLocation";
            params.params = locParam;
            locParam.features=[geo];
            url = url.concat(JSONUtil.encode(params));
            window.open(url, '_blank');
        }
    }
</script>
</@aBase.tpl>
