<#assign cssContent>
<style>
    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
        height: 50px;
        text-align: center;
        color:black !important;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
        max-height: 60px;
        min-width: 90px;
        text-align: center;
        color:black !important;
    }

    table {
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 15px;
        width: 80% !important;
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

</style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent bootVersion="-v3">
<div class="container" style="width: 1240px;">
    <div class="row">
        <div style="float:left;margin: 20px">
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;压覆矿分析结果</h3>
        </div>
        <div style="float:right;margin-top:60px;margin-right: 80px;">
            <h5>单位: 平方米</h5>
        </div>
        <div style="float:right;margin-top:60px;margin-right: 30px">
            <div style="line-height:35px;font-weight:700">地块面积:${geoArea!}
            </div>
        </div>
    </div>

    <div class="row">
        <div style="width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <#list result as item>
                    <li <#if item_index==0>class="active"</#if>><a href="#${item.fid!}"
                                                                  data-toggle="tab">${item.title!}</a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="span12">
            <div class="tab-content large">
                <#list result as item>
                    <#if item.fid=='center'>
                        <div id="${item.fid!}" class="tab-pane fade in <#if item_index==0>active</#if>">
                            <#assign centerResult=item.result />
                            <#if centerResult?size gt 0>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>类型</th>
                                        <th>超出规划控制区面积</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>中心城区规划控制区</td>
                                        <#if centerResult?size gt 0>
                                            <#assign total=0/>
                                            <#list centerResult as center>
                                                <#assign total=total+center.SHAPE_AREA/>
                                            </#list>
                                            <#assign total = geoArea-total/>
                                            <#if total gt 0>
                                                <td>${total!}</td>
                                            <#else>
                                                <td>0</td>
                                            </#if>
                                        <#else>
                                            <td>${origin!}</td>
                                        </#if>
                                    </tr>
                                    </tbody>
                                </table>

                                <div style="text-align: center;padding-top: 20px;">
                                    <a class="btn btn-primary" style="margin-right: 12px" onclick="exportExcel('${item.fid!}');">导出excel</a>
                                </div>
                            <#else>
                                该分类无分析结果！
                            </#if>
                        </div>
                    </#if>
                    <#if item.fid=='yfk'>
                        <div id="${item.fid!}" class="tab-pane fade in <#if item_index==0>active</#if>">
                            <#assign centerResult=item.result />
                            <#if centerResult?size gt 0>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>压覆矿产名称</th>
                                        <th>压覆矿产面积</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <#list centerResult as map>
                                        <tr>
                                            <#list map?keys as key>
                                                <#if key!="SHAPE_AREA">
                                                    <td>${map[key]!}</td>
                                                </#if>
                                            </#list>
                                            <td>${map["SHAPE_AREA"]!}</td>
                                        </tr>
                                        </#list>
                                    </tbody>
                                </table>
                                <div style="text-align: center;padding-top: 20px;">
                                    <a class="btn btn-primary" style="margin-right: 12px;" onclick="exportExcel('${item.fid!}');">导出excel</a>
                                </div>
                            <#else >
                                该分类无分析结果
                            </#if>
                        </div>
                    </#if>
                </#list>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var result = ${resultStr!};
    var orgin= '${geoArea!}';
    function exportExcel(o){
        var re={};
        for(var i=0;i<result.length;i++){
            var item = result[i];
            if(item.fid ==o){
                re = item;
            }
        }
        re.origin=Number(orgin.replace(",","").replace(",","").replace(",","").replace(",","").replace(",",""));
        openPostWindow("<@com.rootPath/>/geometryService/export/analysis",JSON.stringify({result:re}), "common-result.xml");
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