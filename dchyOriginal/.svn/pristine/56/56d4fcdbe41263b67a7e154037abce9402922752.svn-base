<html>
<head>
    <title>Identify (GDDK)</title>
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
            &gt; <a href="${mapUrl}/identify"><i>identify</i></a>
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<h2>Identify (GDDK)</h2>
<div class="rbody">
    <form id="sform" action="${mapUrl}/identify" target="_blank">
        <table class="formTable">
            <tr>
                <td>Geometry:</td>
                <td>
                    <textarea name="geometry" rows="5" cols="55">${RequestParameters.geometry!}</textarea>
                </td>
            </tr>
            <tr valign="top">
                <td>Geometry Type:</td>
                <td>
                    <select name="geometryType">
                        <option value="esriGeometryPoint" selected="true">Point</option>
                        <option value="esriGeometryPolyline">Polyline</option>
                        <option value="esriGeometryPolygon">Polygon</option>
                        <option value="esriGeometryEnvelope">Envelope</option>
                    </select>
                </td>
            </tr>
            <tr valign="top">
                <td>Spatial Reference:</td>
                <td><input type="text" name="sr" value="${RequestParameters.sr!}" size="75"/></td>
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
            <td>Tolerance:</td>
            <td><input type="text" name="tolerance" value="${RequestParameters.tolerance!}" /></td>
            </tr>
            <tr valign="top">
                <td>Map Extent:</td>
                <td><input type="text" name="mapExtent" value="${RequestParameters.mapExtent!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Image Display:</td>
                <td><input type="text" name="imageDisplay" value="${RequestParameters.imageDisplay!}"/></td>
            </tr>
            <tr>
                <td>Return Geometry:</td>
                <td>
                    <input type="radio" name="returnGeometry" value="true" checked="true"/> True &nbsp;
                    <input type="radio" name="returnGeometry" value="false"/> False
                </td>
            </tr>
            <tr>
                <td colspan="2" align="left">
                    <input type="hidden" name="f" value="pjson"/>
                    <input type="submit" value="Identify (GET)"/>
                    <input type="submit" onclick="this.form.method = 'post';" value="Identify (POST)"/>
                </td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
        var sf = document.getElementById('sform');
        <#list ['geometryType','f'] as field>
            <#if RequestParameters[field]??>sf.${field}.value = '${RequestParameters[field]}';</#if>
        </#list>
        <#list ['returnGeometry'] as field>
        var arr = document.getElementsByName('${field}');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].value == '${RequestParameters[field]!}') {
                arr[i].checked = true;
                break;
            }
        }
        </#list>
        sf.geometry.focus();
    </script>
</div>
<br/><br/>
</body>
</html>
