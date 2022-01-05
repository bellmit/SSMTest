<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="<@com.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js"></script>
    <script>
        var itemId = 0;
        var url = '${url!}';

        function getVLC(name) {
            if (window.document[name]) {
                return window.document[name];
            }
            if (navigator.appName.indexOf("Microsoft Internet") == -1) {
                if (document.embeds && document.embeds[name])
                    return document.embeds[name];
            }
            else {
                return document.getElementById(name);
            }
        }

        function doGo(mrl) {
            var vlc = getVLC("vlc");
            itemId = vlc.playlist.add(mrl);
            vlc.playlist.playItem(itemId);
        }

        $(function () {
            if (!url) {
                alert("设备离线！");
            }
            //doGo(url);
        });

    </script>
</head>
<body>
<div style="margin-top: 40px">
    <#if url == ''>
    	设备离线！
    <#else>
    <OBJECT classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" id="vlc"
            codebase="<@com.rootPath/>/static/bin/axvlc.cab"
            width="700" height="750" id="vlc" events="True">
        <param name="MRL" value="${url!}"/>
        <param name="Src" value=""/>
        <param name="ShowDisplay" value="false"/>
        <param name="AutoLoop" value="False"/>
        <param name="AutoPlay" value="True"/>
        <param name="Time" value="false"/>
    </OBJECT>
    </#if>
</div>
</body>
</html>