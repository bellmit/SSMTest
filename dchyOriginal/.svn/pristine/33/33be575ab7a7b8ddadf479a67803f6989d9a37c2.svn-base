<html>
<head>
    <title>All Layers and Tables (${map})</title>
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
            &gt; <a href="${mapUrl}/layers">All Layers and Tables</a>
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
<h2>All Layers and Tables (${map})</h2>

<div class="rbody">
    <h2>Layers: </h2>
<#list layers as layer>
    <ul>
        <h3>Layer: <a href="${mapUrl}/${layer.id}">${layer.name}</a> (${layer.id})</h3>
        <b>Name:</b> ${layer.name} <br/><br/>
        <b>Display Field:</b> ${layer.displayField!} <br/><br/>
        <b>Type: </b> ${layer.type!} <br/><br/>
        <b>Description: </b> ${layer.description!} <br/><br/>
        <b>Default Visibility: ${layer.defaultVisibility?string} </b> <br/><br/>
        <b>Min Scale: </b> ${layer.minScale!'0'}<br/><br/>
        <b>Max Scale: </b> ${layer.maxScale!'0'}<br/><br/>
        <b>Extent:</b>
        <ul>
            XMin: ${layer.extent.xmin}<br/>
            YMin: ${layer.extent.ymin}<br/>
            XMax: ${layer.extent.xmax}<br/>
            YMax: ${layer.extent.ymax}<br/>
            Spatial Reference: ${layer.extent.spatialReference.wkid!}
            &nbsp;(${layer.extent.spatialReference.wkid!})
        </ul>
        <br/>
        <b>Fields: </b>
        <ul>
            <#list layer.fields as field>
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
    </ul>
</#list>
</div>
<br/><br/>
</body>
</html>