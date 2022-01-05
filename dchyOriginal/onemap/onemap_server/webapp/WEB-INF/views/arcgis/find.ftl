<html>
<head>
    <title>Find (GDDK)</title>
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
            &gt; <a href="${mapUrl}/find"><i>find</i></a>
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<h2>Find (GDDK)</h2>

<div class="rbody">
    <div style="color:#ff6666"></div>
    <form id="sform" action="${mapUrl}/find" target="_blank" method="get">
        <table class="formTable">
            <tr valign="top">
                <td>Search Text:</td>
                <td><input type="text" name="searchText" value="${RequestParameters.searchText!}" size="75"/></td>
            </tr>
            <tr>
                <td>Contains:</td>
                <td>
                    <input type="radio" name="contains" value="true" checked="true"/> True &nbsp;
                    <input type="radio" name="contains" value="false"/> False
                </td>
            </tr>
            <tr valign="top">
                <td>Search Fields:</td>
                <td><input type="text" name="searchFields" value="${RequestParameters.searchFields!}" size="75"/></td>
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
                    <input type="submit" value="Find (GET)"/>
                    <input type="submit" onclick="this.form.method = 'post';" value="Find (POST)"/>
                </td>
            </tr>
        </table>
    </form>
    <script type="text/javascript">
        var sf = document.getElementById('sform');
        <#list ['format','f'] as field>
            <#if RequestParameters[field]??>sf.${field}.value = '${RequestParameters[field]}';</#if>
        </#list>
        <#list ['contains','returnGeometry'] as field>
        var arr = document.getElementsByName('${field}');
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].value == '${RequestParameters[field]!}') {
                arr[i].checked = true;
                break;
            }
        }
        </#list>
        sf.searchText.focus();
    </script>
</div>
<br/><br/>
</body>
</html>
