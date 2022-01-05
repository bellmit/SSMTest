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
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;建设用地管制区分析结果展示</h3>
    </div>
    <div style="float:right;margin-bottom: 10px;margin-top: -40px;margin-right: 20px;">
        <input type="button" value="导出excel" onclick="exportExcel()" style="color:#188074;"/></div>
    <div class="row">
        <div style="width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <li class="active"><a href="#JSYDGZQInfo" data-toggle="tab">建设用地管制区</a></li>
            </ul>
        </div>
        <div class="span9">
            <div class="tab-content large">
                <div id="JSYDGZQInfo" class="tab-pane fade in active"><@com.tdghsc1 key="建设用地管制区" unit = unit decimal = "${decimal!'####.####'}"></@com.tdghsc1></div>
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
        });
    }

    function exportFeature(o,type){
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
                var url='<@com.rootPath/>/geometryService/export/txt';
                var dataTxt=JSON.stringify({coords:data});
                if(dataTxt=="")
                {
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

    /**
     *
     * 导出excel
     */
    function exportExcel() {
        var fileName = "jsydgz_cz_analysis.xlsx";
        var officeVersion = '${env.getEnv("office.plugin.version")!}';
        if (officeVersion == 'old')
            fileName = "jsydgz_cz_analysis.xls";
        openPostWindow("<@com.rootPath/>/geometryService/export/excel", '${excelData}', fileName);
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