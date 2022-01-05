<html>
<head>
    <title>Folder: /</title>
    <link href="${base}/static/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<table width="100%" class="userTable">
    <tr>
        <td class="titlecell">
            GTMap REST Services Directory
        </td>
        <td align="right">
            &nbsp;
        </td>
    </tr>
</table>
<table width="100%" class="navTable">
    <tr valign="top">
        <td class="breadcrumbs">
            <a href="${base}/arcgisrest">Home</a>
            &gt; <a href="${base}/arcgisrest/${folder!}"><#if folder??>${folder}<#else>services</#if></a>
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
<h2>Folder: /</h2>

<div class="rbody">
    <b>Current Version: </b>${currentVersion}
    <br/><br/>
    <b>Folders: </b>
    <ul>
    <#list folders as folder>
        <li><a href="${base}/arcgisrest/${folder}">${folder}</a></li></#list>
    </ul>
    <b>Services: </b>
    <ul>
    <#list services as service>
        <li><a href="${base}/arcgisrest/${service.name}/MapServer">${service.name}</a> (MapServer)</li>
    </#list>
        <li><a href="${base}/arcgisrest/Geometry/GeometryServer">Geometry</a> (GeometryServer)</li>
    </ul>
    <b>Supported Interfaces:</b>
    &nbsp;&nbsp;<a target="_blank" href="?f=pjson">REST</a>
    <br/><br/>
</div>
<br/><br/>
</body>
</html>