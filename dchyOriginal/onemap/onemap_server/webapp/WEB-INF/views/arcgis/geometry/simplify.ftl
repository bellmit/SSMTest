<html>
<head>
    <title>Simplify Geometries (GeometryServer)</title>
    <link href="${base}/static/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<table width="100%" class="userTable">
    <tr>
        <td class="titlecell">GTMap Services Directory</td>
    </tr>
</table>
<table class="navTable" width="100%">
    <tbody>
    <tr valign="top">
        <td class="breadcrumbs">
            <a href="${base}/arcgisrest">Home</a> > <a href="${base}/arcgisrest/Geometry/GeometryServer">Geometry (GeometryServer)</a> </td>
        <td align="right" id="help">
    </tr>
    </tbody>
</table>
<h2>Simplify Geometries (GeometryServer)</h2>
<div class='rbody'>
    <form target="_blank" method="post">
        <input type="hidden" name="f" value="pjson"/>
        <table style="border:1px solid #000000;">
        <tr>
        <td>Spatial Reference: </td>
        <td><input type="text" name="sr" value="" /></td>
        </tr>
        <tr valign="top">
        <td>Geometries:</td>
        <td><textarea name="geometries" rows="10" cols="50"></textarea></td>
        </tr>
        <tr>
        <tr><td colspan="2"><input type="submit" value="Simplify" /></td></tr>
        </table>
    </form>
</div>
</body>
</html>
