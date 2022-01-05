<html>
<head>
    <title>${map} (MapServer)</title>
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
<h2>${map} (MapServer)</h2>

<div class="rbody">
    <b>View In:</b>
    &nbsp;&nbsp;<a href="?f=jsapi" target="_blank">ArcGIS JavaScript</a>
    <br/><br/>
    <b>Map Name: </b> ${map} <br/><br/>
    <a href="${mapUrl}/layers">All Layers and Tables</a><br/><br/>
    <b>Layers: </b>
    <ul>
    <#list layers as layer>
        <li><a href="${mapUrl}/${layer.id}">${layer.name}</a> (${layer.id})</li></#list>
    </ul>
    <b>Description: </b> <br/><br/>
    <b>Copyright Text: </b> <br/> <br/>
    <b>Spatial Reference: </b>
${spatialReference.wkid!}&nbsp;(${spatialReference.wkid!})
    <br/><br/>
<#if tileInfo??>
    <b>Tile Info: </b>
    <ul>
        <li><b>Height: </b>${tileInfo.rows}</li>
        <li><b>Width: </b>${tileInfo.cols}</li>
        <li><b>DPI: </b>${tileInfo.dpi}</li>
        <li>
            <b>Levels of Detail: </b> <i>(# Levels: ${tileInfo.lods?size})</i>
            <ul>
                <#list tileInfo.lods as lod>
                    <li>
                        <b>Level ID: </b>${lod.level}
                        <ul>
                            Resolution: ${lod.resolution}<br/>
                            Scale: ${lod.scale}
                        </ul>
                    </li>
                </#list>
            </ul>
        </li>

        <li><b>Format: </b>${tileInfo.format!}</li>
        <li><b>Compression Quality: </b>${tileInfo.compressionQuality!}</li>

        <li>
            <b>Origin: </b>
            <ul>

                X: ${tileInfo.origin.x} <br/>
                Y: ${tileInfo.origin.y} <br/>

            </ul>
        </li>
        <li>
            <b>Spatial Reference: ${tileInfo.spatialReference.wkid!}&nbsp;(${tileInfo.spatialReference.wkid!})</b>
        </li>
    </ul>
</#if>

    <br/><br/>
    <b>Initial Extent: </b>
    <ul>
        XMin: ${initialExtent.xmin}<br/>
        YMin: ${initialExtent.ymin}<br/>
        XMax: ${initialExtent.xmax}<br/>
        YMax: ${initialExtent.ymax}<br/>
        Spatial Reference: ${spatialReference.wkid!}
        &nbsp;(${spatialReference.wkid!})
        <br/> <br/>
    </ul>
    <b>Full Extent: </b>
    <ul>
        XMin: ${fullExtent.xmin}<br/>
        YMin: ${fullExtent.ymin}<br/>
        XMax: ${fullExtent.xmax}<br/>
        YMax: ${fullExtent.ymax}<br/>
        Spatial Reference: ${spatialReference.wkid!}
        &nbsp;(${spatialReference.wkid!})
        <br/> <br/>
    </ul>
    <b>Units: </b> ${units!} <br/><br/>
    <b>Supported Image Format Types: </b> PNG32,PNG24,PNG,JPG,DIB,TIFF,EMF,PS,PDF,GIF,SVG,SVGZ,BMP<br/><br/>
    <b>Min Scale: </b> ${minScale!'0'}<br/><br/>
    <b>Max Scale: </b> ${maxScale!'0'}<br/><br/>
    <br/>
    <b>Supported Operations</b>:
    &nbsp;&nbsp;<a href="${mapUrl}/export?bbox=${fullExtent.xmin},${fullExtent.ymin},${fullExtent.xmax},${fullExtent.ymax}">Export Map</a>
    &nbsp;&nbsp;<a href="${mapUrl}/identify">Identify</a>
    &nbsp;&nbsp;<a href="${mapUrl}/find">Find</a>
    &nbsp;&nbsp;<a href="${mapUrl}/info/thumbnail">Thumbnail</a>
</div>
<br/><br/>
</body>
</html>