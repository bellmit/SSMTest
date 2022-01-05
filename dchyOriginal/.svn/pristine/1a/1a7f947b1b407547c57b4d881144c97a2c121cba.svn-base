<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Detect Custom Protocol</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <@com.style name="static/thirdparty/h-ui/css/H-ui.css"/>
    <@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
</head>
<body>
<div class="ml-30 mt-30">
    <input id="protocol" value="cmrv" placeholder="custom protocol" style="height: 29px;font-size: 15px;"/>
    <button id="launch" class="btn btn-success">检测</button>
    <p class="text-info">注:此处输入框的值使用默认值即可。</p>
</div>
<!-- Mozilla Only -->
<iframe id="hiddenIframe" src="about:blank" style="display:none"></iframe>
<!-- IE Case 1 -->
<a id="hiddenLink" style="display:none;" href="#">custom protocol</a>
<script>

    //Default State
    var isSupported = false;
    //Helper Methods
    function getProtocol(){
        return $('#protocol').val();
    }

    function getUrl(){
        return getProtocol()+"://info";
    }

    function result() {
        if (isSupported) {
            alert("视频控件已注册!");
        } else
            alert("视频控件尚未注册，请下载后注册！");
    }
    $(document).ready(function(){
        $.browser={};
        $.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
        $.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
        $.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
        $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
        //Handle Click on Launch button
        $('#launch').click(function(){
            if($.browser.mozilla){
                launchMozilla();
            }else if($.browser.chrome|| $.browser.webkit){
                launchChrome();
            }else if($.browser.msie){
                launchIE();
            }
        });
    });

    //Handle IE
    function launchIE(){
        var url = getUrl(),
                aLink = $('#hiddenLink')[0];
        isSupported = false;
        aLink.href = url;
        //Case 1: protcolLong
        // console.log("Case 1");
        if(navigator.appName=="Microsoft Internet Explorer"
                && aLink.protocolLong=="Unknown Protocol"){
            isSupported = false;
            result();
            return;
        }
        //IE10+
        if(navigator.msLaunchUri){
            navigator.msLaunchUri(url,
                    function(){ isSupported = true; result(); }, //success
                    function(){ isSupported=false; result();  }  //failure
            );
            return;
        }
        //Case2: Open New Window, set iframe src, and access the location.href
        //console.log("Case 2");
        var myWindow = window.open('','','width=0,height=0');
        // myWindow.document.write("<iframe src='"+ url + "></iframe>");
        setTimeout(function(){
            try{
                myWindow.location.href=url;
                myWindow.location.reload();
                isSupported = true;
            }catch(e){
                //Handle Exception
            }

            if(isSupported){
                myWindow.setTimeout('window.close()', 100);
            }else{
                myWindow.close();
            }
            result();
        }, 1000)

    }

    //Handle Firefox
    function launchMozilla(){
        var url = getUrl(),
                iFrame = $('#hiddenIframe')[0];
        isSupported = false;
        //Set iframe.src and handle exception
        try{
            iFrame.contentWindow.location.href = url;
            isSupported = true;
            result();
        }catch(e){
            //FireFox
            if (e.name == "NS_ERROR_UNKNOWN_PROTOCOL"){
                isSupported = false;
                result();
            }
        }
    }

    //Handle Chrome
    function launchChrome(){
        var url = getUrl(),
                protcolEl = $('#protocol')[0];

        isSupported = false;
        protcolEl.focus();
        protcolEl.onblur = function(){
            isSupported = true;
            console.log("Text Field onblur called");
        };
        //will trigger onblur
        location.href = url;
        //Note: timeout could vary as per the browser version, have a higher value
        setTimeout(function(){
            protcolEl.onblur = null;
            result();
        }, 500);
    }
</script>
</body>
</html>