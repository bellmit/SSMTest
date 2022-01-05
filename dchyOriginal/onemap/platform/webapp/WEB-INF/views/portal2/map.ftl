<@core.html title="${env.getEnv('local.title')}国土资源一张图" import="jquery">
<script type="text/javascript">
    $(function () {
        var url = "<@core.rootPath/>/map/${env.getMainTpl()!'YZT_DEFAULT'}";
        var iframe = document.getElementById("mapIframe");
        iframe.src = url.concat(window.location.search);
        $("#map").height($(this).height() - 3);
    });
</script>
<style type="text/css">
    body {
        overflow-y: hidden;
        margin: 0;
        width: 100%;
        height: 100%;
    }

    #map {
        width: 100%;
        height: 100%
    }
</style>
<div id="map">
    <iframe id="mapIframe" src="" frameborder="0" width="100%" height="100%">
    </iframe>
</div>
</@core.html>