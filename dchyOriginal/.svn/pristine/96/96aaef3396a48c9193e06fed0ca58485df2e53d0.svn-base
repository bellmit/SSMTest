<html>
<head>
    <title>Export Map (GDDK)</title>
    <link href="${base}/static/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<table width="100%" class="userTable">
    <tr>
        <td class="titlecell">
            GTMap REST Services Directory
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<#assign mapUrl>${base}/arcgisrest/<#if folder??>${folder}/</#if>${map}/MapServer</#assign>
<table width="100%" class="navTable">
    <tr valign="top">
        <td class="breadcrumbs">
            <a href="${base}/arcgisrest">Home</a>
            &gt; <a href="${base}/arcgisrest/${folder!}"><#if folder??>${folder}<#else>services</#if></a>
            &gt; <a href="${mapUrl}">${map} (MapServer)</a>
            &gt; <a href="${mapUrl}/export"><i>export</i></a>
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<h2>Export Map (GDDK)</h2>

<div class="rbody">
    <table class="infoTable" cellspacing="5">
        <tr valign="top">
            <td>
                <img style="border:2px solid #000000;" src="http://192.168.51.20:6080/arcgis/rest/directories/arcgisoutput/GDDK_MapServer/_ags_mapcbb0baddd0604d1a8cba32564956a04d.png"/>
            </td>
            <td>
                Width: 400<br/>
                Height: 400<br/>
                Extent:
                <ul>
                    XMin: 4.048521239688053E7<br/>
                    YMin: 3575174.0048416452<br/>
                    XMax: 4.0490768657993056E7<br/>
                    YMax: 3580730.2659541713<br/>
                    Spatial Reference: 2364
                    &nbsp;(2364)
                    <br/> <br/>
                </ul>
                <br/>
                Scale: 52500.00000003622
            </td>
        </tr>
    </table>
    <form id="sform" action="${mapUrl}/export">
        <table class="formTable">
            <tr valign="top">
                <td>Bounding Box:</td>
                <td><input type="text" name="bbox" value="${RequestParameters.bbox!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Bounding Box Spatial Reference:</td>
                <td><input type="text" name="bboxSR" value="${RequestParameters.bboxSR!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Layers:</td>
                <td><input type="text" name="layers" value="${RequestParameters.layers!}"/></td>
            </tr>
            <tr valign="top">
                <td>Layer Definitions:</td>
                <td><input type="text" name="layerDefs" value="${RequestParameters.layerDefs!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Image Size:</td>
                <td><input type="text" name="size" value="${RequestParameters.layerDefs!}"/></td>
            </tr>
            <tr valign="top">
                <td>Image Spatial Reference:</td>
                <td><input type="text" name="imageSR" value="${RequestParameters.layerDefs!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Image Format:</td>
                <td>
                    <select name="format">
                        <option value="png" selected="true">PNG</option>
                        <option value="png8">PNG8</option>
                        <option value="png24">PNG24</option>
                        <option value="png32">PNG32</option>
                        <option value="jpg">JPG</option>
                        <option value="pdf">PDF</option>
                        <option value="bmp">BMP</option>
                        <option value="gif">GIF</option>
                        <option value="svg">SVG</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Background Transparent:</td>
                <td>
                    <input type="radio" name="transparent" value="true"/> True &nbsp;
                    <input type="radio" name="transparent" value="false" checked="true"/> False
                </td>
            </tr>
            <tr valign="top">
                <td>DPI:</td>
                <td><input type="text" name="dpi" value="${RequestParameters.dpi!}"/></td>
            </tr>
            <tr>
                <td>Format:</td>
                <td>
                    <select name="f">
                        <option value="html">HTML</option>
                        <option value="pjson">JSON</option>
                        <option value="image">Image</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="left">
                    <input type="submit" value="Export Map Image (GET)"/>
                    <input type="submit" onclick="this.form.method = 'post';" value="Export Map Image (POST)"/>
                </td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
        var sf = document.getElementById('sform');
        <#list ['format','f'] as field>
            <#if RequestParameters[field]??>sf.${field}.value = '${RequestParameters[field]}';</#if>
        </#list>
        <#list ['transparent'] as field>
        var arr = document.getElementsByName('${field}');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].value == '${RequestParameters[field]}') {
                arr[i].checked = true;
                break;
            }
        }
        </#list>
        sf.bbox.focus();
    </script>
    <br/><br/>
</div>
<br/><br/>
</body>
</html>
