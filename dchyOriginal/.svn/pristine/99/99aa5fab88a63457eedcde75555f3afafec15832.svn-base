<html>
<head>
    <title>Layer: ${name} (ID: ${id})</title>
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
            &gt; <a href="${mapUrl}/${id}">${name}</a>
        </td>
        <td align="right">

        </td>
    </tr>
</table>
<table>
    <tr>
        <td class="apiref">
            <a href="?f=pjson" target="_blank">JSON</a>
        </td>
    </tr>
</table>
<h2>Layer: ${name} (ID: ${id})</h2>

<div class="rbody">
    <b>Name:</b> ${name} <br/><br/>
    <b>Display Field:</b> ${displayField!} <br/><br/>
    <b>Type: </b> ${type!} <br/><br/>
    <b>Description: </b> ${description!} <br/><br/>
    <b>Default Visibility: ${defaultVisibility?string} </b> <br/><br/>
    <b>Min Scale: </b> ${minScale!'0'}<br/><br/>
    <b>Max Scale: </b> ${maxScale!'0'}<br/><br/>
    <b>Extent:</b>
    <ul>
        XMin: ${extent.xmin}<br/>
        YMin: ${extent.ymin}<br/>
        XMax: ${extent.xmax}<br/>
        YMax: ${extent.ymax}<br/>
        Spatial Reference: ${extent.spatialReference.wkid!}
        &nbsp;(${extent.spatialReference.wkid!})
    </ul>
    <br/>
    <b>Fields: </b>
    <ul>
    <#list fields as field>
        <li>
        ${field.name}
            <i>
                (
                type: ${field.type}
                , alias: ${field.alias}
                <#if field.length??>, length:${field.length}</#if>
                )
            </i>
        </li>
    </#list>
    </ul>
    <br/>
    <b>Supported Operations</b>:
    &nbsp;&nbsp;<a href="${mapUrl}/${id}/query">Query</a>
</div>
<br/><br/>
</body>
</html>