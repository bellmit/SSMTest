<#assign mapUrl>${base}/arcgisrest/<#if folder??>${folder}/</#if>${map}/MapServer</#assign>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <title>ArcGIS JavaScript API: ${map}</title>
    <style type="text/css">
        @import "http://serverapi.arcgisonline.com/jsapi/arcgis/2.6/js/dojo/dijit/themes/tundra/tundra.css";
        @import "${base}/static/css/jsapi.css";
    </style>
    <script type="text/javascript"> var djConfig = { parseOnLoad: true };
    </script>
    <script type="text/javascript" src="http://serverapi.arcgisonline.com/jsapi/arcgis/?v=2.6"></script>
    <script type="text/javascript"> dojo.require("dijit.layout.BorderContainer");
    dojo.require("dijit.layout.ContentPane");
    dojo.require("esri.map");
    var map;
    function init() {
        map = new esri.Map("map");
        var layer = new esri.layers.${tileInfo???string('ArcGISTiledMapServiceLayer','ArcGISDynamicMapServiceLayer')}("${mapUrl}");
        map.addLayer(layer);
        var resizeTimer;
        dojo.connect(map, 'onLoad', function (theMap) {
            dojo.connect(dijit.byId('map'), 'resize', function () {
                clearTimeout(resizeTimer);
                resizeTimer = setTimeout(function () {
                    map.resize();
                    map.reposition();
                }, 500);
            });
        });
    }
    dojo.addOnLoad(init);
    </script>
</head>
<body class="tundra">
<div dojotype="dijit.layout.BorderContainer" design="headline" gutters="true" style="width: 100%; height: 100%; margin: 0;">
    <div dojotype="dijit.layout.ContentPane" region="top" id="navtable">
        <div style="float:left;" id="breadcrumbs">ArcGIS JavaScript API: ${map}</div>
        <div style="float:right;" id="help">
            Built using the <a href="http://help.arcgis.com/en/webapi/javascript/arcgis/">ArcGIS JavaScript API</a>
        </div>
    </div>
    <div id="map" dojotype="dijit.layout.ContentPane" region="center"></div>
</div>
</body>
</html>
