/**
 *
 * @author monarchCheng
 * @create 2017-06-01 11:11
 **/
define(["layer"], function (layer) {

    //Default State
    var isSupported = false;
    function result() {
        console.log(isSupported);
        if (!isSupported) {
            layer.open({
                type: 2,
                title: "资源下载",
                skin: 'layui-layer-lan',
                area: ['600px', '150px'],
                content: root + '/static/js/cfg/template/resource-download.html'
            });
        }
    }

    function detect(url) {
        var form = $('<form>' +
            ' <input id="protocol" value="cmrv" placeholder="custom protocol" style="height: 29px;font-size: 15px;"/>' +
            ' <iframe id="hiddenIframe" src="about:blank" style="display:none"></iframe>' +
            ' <a id="hiddenLink" style="display:none;" href="#">custom protocol</a>' +
            '</form>');
        form.appendTo(document.body);
        $.browser = {};
        $.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
        $.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
        $.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
        $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
        //Handle Click on Launch button
        if ($.browser.mozilla) {
            launchMozilla(url);
        } else if ($.browser.chrome || $.browser.webkit) {
            launchChrome(url);
        } else if ($.browser.msie) {
            launchIE(url);
        }
        document.body.removeChild(form[0]);
    }

    //Handle IE
    function launchIE(url) {
        var aLink = $('#hiddenLink')[0];
        isSupported = false;
        aLink.href = url;
        //Case 1: protcolLong
        // console.log("Case 1");
        if (navigator.appName == "Microsoft Internet Explorer"
            && aLink.protocolLong == "Unknown Protocol") {
            isSupported = false;
            result();
            return;
        }
        //IE10+
        if (navigator.msLaunchUri) {
            navigator.msLaunchUri(url,
                function () {
                    isSupported = true;
                    result();
                }, //success
                function () {
                    isSupported = false;
                    result();
                }  //failure
            );
            return;
        }

        var myWindow = window.open('', '', 'width=0,height=0');

        myWindow.document.write("<iframe src='" + url + "'></iframe>");

        setTimeout(function () {
            try {
                myWindow.location.href;
                myWindow.setTimeout("window.close()", 1000);
                isSupported = true;
                result();
            } catch (e) {
                myWindow.close();
                result();
            }
        }, 1000);

    }

    //Handle Firefox
    function launchMozilla(url) {
        var iFrame = $('#hiddenIframe')[0];
        isSupported = false;
        //Set iframe.src and handle exception
        try {
            iFrame.contentWindow.location.href = url;
            isSupported = true;
            result();
        } catch (e) {
            //FireFox
            if (e.name == "NS_ERROR_UNKNOWN_PROTOCOL") {
                isSupported = false;
                result();
            }
        }
    }

    //Handle Chrome
    function launchChrome(url) {
        // var protcolEl = $('#protocol')[0];
        var protcolEl = window;
        while (protcolEl != protcolEl.parent) {
            protcolEl = protcolEl.parent;
        }

        isSupported = false;
        protcolEl.focus();
        protcolEl.onblur = function () {
            isSupported = true;
            console.log("Text Field onblur called");
        };
        //will trigger onblur
        location.href = url;
        //Note: timeout could vary as per the browser version, have a higher value
        setTimeout(function () {
            protcolEl.onblur = null;
            result();
        }, 500);
    }

    return {detect: detect};
});