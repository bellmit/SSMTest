<html>
<head>
    <title>Query: ${layerName} (ID: ${layer})</title>
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
            &gt; <a href="${mapUrl}/${layer}">${layerName}</a>
            &gt; <a href="${mapUrl}/${layer}/query"><i>query</i></a>
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<h2>Query: ${layerName} (ID: ${layer})</h2>

<div class="rbody">
    <div style="color:#ff6666"></div>
    <form id="sform" action="${mapUrl}/${layer}/query" target="_blank">
        <table class="formTable">
            <tr valign="top">
                <td>Where:</td>
                <td><input type="text" name="where" value="${RequestParameters.where!}" size="75"/></td>
            </tr>
            <tr valign="top">
                <td>Text:</td>
                <td><input type="text" name="text" value="${RequestParameters.text!}"/></td>
            </tr>
            <tr valign="top">
                <td>Object IDs:</td>
                <td><input type="text" name="objectIds" value="${RequestParameters.objectIds!}" size="75"/></td>
            </tr>
            <tr>
                <td>Input Geometry:</td>
                <td>
                    <textarea name="geometry" rows="5" cols="55">${RequestParameters.geometry!}</textarea>
                </td>
            </tr>
            <tr>
                <td>Geometry Type:</td>
                <td>
                    <select name="geometryType">
                        <option value="esriGeometryEnvelope">Envelope</option>
                        <option value="esriGeometryPoint">Point</option>
                        <option value="esriGeometryPolyline">Polyline</option>
                        <option value="esriGeometryPolygon">Polygon</option>
                        <option value="esriGeometryMultipoint">Multipoint</option>
                    </select>
                </td>
            </tr>
            <tr valign="top">
                <td>Input Spatial Reference:</td>
                <td><input type="text" name="inSR" value="${RequestParameters.inSR!}" size="75"/></td>
            </tr>
            <tr>
                <td>Spatial Relationship:</td>
                <td>
                    <select name="spatialRel">
                        <option value="esriSpatialRelIntersects">Intersects</option>
                        <option value="esriSpatialRelContains">Contains</option>
                        <option value="esriSpatialRelCrosses">Crosses</option>
                        <option value="esriSpatialRelEnvelopeIntersects">Envelope Intersects</option>
                        <option value="esriSpatialRelIndexIntersects">Index Intersects</option>
                        <option value="esriSpatialRelOverlaps">Overlaps</option>
                        <option value="esriSpatialRelTouches">Touches</option>
                        <option value="esriSpatialRelWithin">Within</option>
                        <option value="esriSpatialRelRelation">Relation</option>
                    </select>
                </td>
            </tr>
            <tr valign="top">
                <td>Out Fields:</td>
                <td><input type="text" name="outFields" value="${RequestParameters.outFields!}" size="75"/></td>
            </tr>
            <tr>
                <td>Return Geometry:</td>
                <td>
                    <input type="radio" name="returnGeometry" value="true" checked="true"/> True &nbsp;
                    <input type="radio" name="returnGeometry" value="false"/> False
                </td>
            </tr>
            <tr valign="top">
                <td>Output Spatial Reference:</td>
                <td><input type="text" name="outSR" value="${RequestParameters.outSR!}" size="75"/></td>
            </tr>
            <tr>
                <td>Return IDs Only:</td>
                <td>
                    <input type="radio" name="returnIdsOnly" value="true"/> True &nbsp;
                    <input type="radio" name="returnIdsOnly" value="false" checked="true"/> False
                </td>
            </tr>
            <tr>
                <td>Order By Fields:</td>
                <td><input type="text" name="orderByFields" value="${RequestParameters.orderByFields!}" size="50"/></td>
            </tr>
            <tr>
                <td colspan="2" align="left">
                    <input type="hidden" name="f" value="pjson"/>
                    <input type="submit" value="Query (GET)"/>
                    <input type="submit" onclick="this.form.method = 'post';" value="Query (POST)"/>
                </td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
        var sf = document.getElementById('sform');
        <#list ['geometryType','spatialRel','f'] as field>
            <#if RequestParameters[field]??>sf.${field}.value = '${RequestParameters[field]}';</#if>
        </#list>
        <#list ['returnGeometry','returnIdsOnly'] as field>
        var arr = document.getElementsByName('${field}');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].value == '${RequestParameters[field]!}') {
                arr[i].checked = true;
                break;
            }
        }
        </#list>
        sf.where.focus();
    </script>
</div>
<br/><br/>
</body>
</html>