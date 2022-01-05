<@com.html title="网点视频" import="jquery,video,layui">
<script>
    var iProtocol = "${iProtocol!}";
    var szIP = "${szIP!}";
    var szPort = "${szPort!}";
    var szUsername = "${szUsername!}";
    var szPassword = "${szPassword!}";
    var iStreamType = "${iStreamType!}";

    var oLiveView = {
        iProtocol: iProtocol,            // protocol 1：http, 2:https
        szIP: szIP,                         // protocol ip
        szPort: szPort,                     // protocol port
        szUsername: szUsername,             // device username
        szPassword: szPassword,             // device password
        iStreamType: iStreamType,           // stream 1：main stream  2：sub-stream  3：third stream  4：transcode stream
        iChannelID: 1,                       // channel no
        bZeroChannel: false                 // zero channel
    };
    var szDeviceIdentify = oLiveView.szIP + "_" + oLiveView.szPort;
    //视频直播控件初始化
    $(function () {
        // 检查插件是否已经安装过
        var iRet = WebVideoCtrl.I_CheckPluginInstall();
        if (-1 == iRet) {
            alert("您还未安装过插件，双击开发包目录里的WebComponentsKit.exe安装！");
            return;
        }
        var oPlugin = {
            iWidth: $('#live_module').width(),             // plugin width
            iHeight: $('#live_module').height()             // plugin height
        };
        // 初始化插件参数及插入插件
        WebVideoCtrl.I_InitPlugin(oPlugin.iWidth, oPlugin.iHeight, {
            bWndFull: true,//是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
            iWndowType: 1,
            cbInitPluginComplete: function () {
                WebVideoCtrl.I_InsertOBJECTPlugin("live_module");

                // 检查插件是否最新
                if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
                    alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
                    return;
                }
                // 登录设备
                WebVideoCtrl.I_Login(oLiveView.szIP, oLiveView.iProtocol, oLiveView.szPort, oLiveView.szUsername, oLiveView.szPassword, {
                    success: function (xmlDoc) {
                        getChannelInfo();
                        var iChannel=$("#"+oLiveView.iChannelID);
                        $("#iChannelID").find(iChannel).addClass("layui-this");
                        setTimeout(function () {
                            //直播
                            WebVideoCtrl.I_StartRealPlay(szDeviceIdentify, {
                                iStreamType: oLiveView.iStreamType,
                                iChannelID: oLiveView.iChannelID,
                                bZeroChannel: oLiveView.bZeroChannel
                            });
                        }, 1000);
                    }
                });
            }
        });
    });

    function getChannelInfo() {
        var szDeviceIdentify = szIP;
        $("#iChannelID").empty();
         var oSel = $("#iChannelID");

        if (null == szDeviceIdentify) {
            return;
        }

        // 模拟通道
        WebVideoCtrl.I_GetAnalogChannelInfo(szDeviceIdentify, {
            async: false,
            success: function (xmlDoc) {
                var oChannels = $(xmlDoc).find("VideoInputChannel");

                $.each(oChannels, function (i) {
                    var id = $(this).find("id").eq(0).text(),
                            name = $(this).find("name").eq(0).text();
                    if ("" == name) {
                        name = "Camera " + (i < 9 ? "0" + (i + 1) : (i + 1));
                    }
                    oSel.append("<dd id='"+id+"' ><a href='javascript:initPlayBack("+id+");'>"+name+"</a></dd>");
                });
                showOPInfo(szDeviceIdentify + " 获取模拟通道成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(szDeviceIdentify + " 获取模拟通道失败！", status, xmlDoc);
            }
        });
        // 数字通道
        WebVideoCtrl.I_GetDigitalChannelInfo(szDeviceIdentify, {
            async: false,
            success: function (xmlDoc) {
                var oChannels = $(xmlDoc).find("InputProxyChannelStatus");

                $.each(oChannels, function (i) {
                    var id = $(this).find("id").eq(0).text(),
                            name = $(this).find("name").eq(0).text(),
                            online = $(this).find("online").eq(0).text();
                    if ("false" == online) {// 过滤禁用的数字通道
                        return true;
                    }
                    if ("" == name) {
                        name = "IPCamera " + (i < 9 ? "0" + (i + 1) : (i + 1));
                    }
                    oSel.append("<dd id='"+id+"'><a  href='javascript:initPlayBack("+id+");'>"+name+"</a></dd>");
                });
                showOPInfo(szDeviceIdentify + " 获取数字通道成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(szDeviceIdentify + " 获取数字通道失败！", status, xmlDoc);
            }
        });
        // 零通道
        WebVideoCtrl.I_GetZeroChannelInfo(szDeviceIdentify, {
            async: false,
            success: function (xmlDoc) {
                var oChannels = $(xmlDoc).find("ZeroVideoChannel");

                $.each(oChannels, function (i) {
                    var id = $(this).find("id").eq(0).text(),
                            name = $(this).find("name").eq(0).text();
                    if ("" == name) {
                        name = "Zero Channel " + (i < 9 ? "0" + (i + 1) : (i + 1));
                    }
                    if ("true" == $(this).find("enabled").eq(0).text()) {// 过滤禁用的零通道
                        oSel.append("<dd id='"+id+"'><a  href='javascript:initPlayBack("+id+");'>"+name+"</a></dd>");
                    }
                });
                showOPInfo(szDeviceIdentify + " 获取零通道成功！");
            },
            error: function (status, xmlDoc) {
                showOPInfo(szDeviceIdentify + " 获取零通道失败！", status, xmlDoc);
            }
        });
    }

    function initPlayBack(id) {
        $("#iChannelID").find("dd").removeClass("layui-this");
        var iChannel=$("#"+id);
        $("#iChannelID").find(iChannel).addClass("layui-this");
        WebVideoCtrl.I_Stop({
            success: function () {
                WebVideoCtrl.I_StartRealPlay(szDeviceIdentify, {
                    iStreamType: oLiveView.iStreamType,
                    iChannelID: id
                });
            }
        });
    }

    function showOPInfo(szInfo, status, xmlDoc) {
        var szTip = szInfo;
        if (typeof status != "undefined" && status != 200) {
            var szStatusString = $(xmlDoc).find("statusString").eq(0).text();
            var szSubStatusCode = $(xmlDoc).find("subStatusCode").eq(0).text();
            if ("" === szSubStatusCode) {
                szTip += "(" + status + ", " + szStatusString + ")";
            } else {
                szTip += "(" + status + ", " + szSubStatusCode + ")";
            }
        }
    }

    layui.use('element', function(){
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块

        //监听导航点击
        element.on('nav(demo)', function(elem){
            //console.log(elem)
            //layer.msg(elem.text());
        });
    });
</script>
<div class="layui-container">
    <div class="layui-row">
        <div class="layui-col-xs3 layui-col-sm3 layui-col-md4" style="text-align: right">
            <ul class="layui-nav layui-nav-tree layui-inline" lay-filter="demo" style="margin-right: 10px;text-align: left">
                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:;">通道</a>
                    <dl id="iChannelID" class="layui-nav-child">

                    </dl>
                </li>
            </ul>
        </div>
        <div class="layui-col-xs9 layui-col-sm9 layui-col-md8">
            <div id="live_module" style="height:500px;">
            </div>
        </div>
    </div>
</div>
</@com.html>
