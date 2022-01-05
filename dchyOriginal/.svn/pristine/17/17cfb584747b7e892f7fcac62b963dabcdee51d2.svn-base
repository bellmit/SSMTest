<#assign cssContent>
<style>
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
        border: 1px solid #ddd;
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

    a:hover {
        color: purple;
    }
</style>
</#assign>

<@aBase.tpl showHeader="false" css=cssContent>
<div class="container" style="width: 1240px;">
    <div class="row">
        <div style="float:left;">
            <h3 style="font-weight: normal; color:#188074"><span class="icon icon-columns"></span>&nbsp;综合分析结果展示</h3>
        </div>
    </div>

    <div class="row">
        <div style="width:220px;float:left;">
            <ul class="nav nav-pills nav-stacked">
                <#list result?keys as key>
                    <#assign value=result[key]!/>
                    <li <#if key_index==0>class="active"</#if>><a href="#${key}"
                                                                  data-toggle="tab">${value["alias"]!}</a>
                    </li>
                </#list>
            <#--<li><a href="#map" data-toggle="tab">重叠图形</a></li>-->
            </ul>
        </div>
        <div class="span12">
            <div class="tab-content">
                <#list result?keys as key>
                    <#assign item=result[key]!/>
                    <div id="${key!}" class="tab-pane fade in <#if key_index==0>active</#if>">
                        <iframe name="view_${key!}" style="width: 100%;height: 100%;min-height: 572px;" frameborder="no"
                                border="0"
                                marginwidth="0"
                                marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
                    </div>
                </#list>
            <#--<div id="map" class="tab-pane fade in">-->
            <#--<iframe name="view_map" style="width: 100%;height: 100%;min-height: 672px;" frameborder="no" border="0"-->
            <#--marginwidth="0"-->
            <#--marginheight="0" scrolling="yes" allowtransparency="yes" src="<@com.rootPath/>/map/${tpl!}?hideLeftPanel=true&hideTopBar=true&action=location&params={%22type%22:%22urlLocation%22,%22params%22:{%22url%22:%22javascript:getCoords()%22}}"></iframe>-->
            <#--</div>-->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var data = '${linkData!}';
    var link = '${link!}';
    var og_area = '${ogArea!}'; //分析图形的面积
    var og_geo = '${ogGeo!}'; //分析图形 geojson

    $(function () {
        var obj = JSON.parse(data);
        for (var k in obj) {
            goLink(k, obj[k]);
        }
    });

    /***
     * 返回分析的坐标串 geojson 格式
     * */
    function getCoords() {
        return ${geo!};
    }

    /***
     *
     * go to link if accessible
     * */
    function goLink(key, value) {
        var obj = {};
        obj[key] = value;
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = link;
        tempForm.target = "view_".concat(key);
        tempForm.appendChild(createHiddenInput("data", obj));
        tempForm.appendChild(createHiddenInput("ogArea", og_area));
        tempForm.appendChild(createHiddenInput("ogPro", JSON.stringify(getPropertyOfGeo())));
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

    /***
     * 取出分析图形的properties 组织成array
     * */
    function getPropertyOfGeo() {
        var _arr = [];
        var geo = JSON.parse(og_geo);
        if (geo && geo.hasOwnProperty("type")) {
            switch (geo["type"]) {
                case "Feature":
                    _arr.push(geo["properties"]);
                    break;
                case "FeatureCollection":
                    geo["features"].forEach(function (item) {
                        if (item.hasOwnProperty("properties"))
                            _arr.push(item["properties"]);
                    });
                    break;
            }
            console.log(JSON.stringify(_arr));
        }
        return _arr;
    }
    /***
     * create hidden input element
     * @param name
     * @param value
     */
    function createHiddenInput(name, value) {
        var hideInput = document.createElement("input");
        hideInput.type = "hidden";
        hideInput.name = name;
        if (typeof value == "object")
            hideInput.value = JSON.stringify(value);
        else
            hideInput.value = value;
        return hideInput;
    }
</script>
</@aBase.tpl>

