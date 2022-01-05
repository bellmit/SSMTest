<html>
<head>
    <TITLE>视频</TITLE>
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE">
    <META http-equiv="Content-Type" content="text/html; charset=utf-8">
    <META http-equiv="Pragma" content="no-cache">
    <META http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <META http-equiv="Expires" content="0">
<@com.script name="static/js/video/dss/jquery-1.7.1.min.js"></@com.script>
<@com.script name="static/js/video/json2.js"></@com.script>
<@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
<@com.script name="static/thirdparty/layui/layui.js"></@com.script>
    <script>
        var szIP = "${szIP!}";
        var szPort = "${szPort!}";
        var szUsername = "${szUsername!}";
        var szPassword = "${szPassword!}";
        var deviceId = "${deviceId!}";
        var coding = "${coding!}";
        var defaultHeight=$(window).height()-20;
        var defaultWidth=$(window).width();

        var gWndId = 0;

        //视频直播控件初始化
        $(function () {
            $("#ml").height(defaultHeight);
            $("#sp").height(defaultHeight);
            $("#ml").width(215);
            $("#sp").width(defaultWidth-265);
            init();
            ButtonLogin_onclick();
        });

        //初始化
        function init() {
            /*try{
             var obj = new ActiveXObject("DPSDK_OCX.DPSDK_OCXCtrl.1");
             }catch(e){
             alert("控件未注册，请先注册控件！");
             return;
             }*/
            var obj = document.getElementById("DPSDK_OCX");
            gWndId = obj.DPSDK_CreateSmartWnd(0, 0, 100, 100);

            ButtonCreateWnd_onclick();
            //obj.DPSDK_SetLog(2, "D:\\DPSDK_OCX_log", false, false); //初始化后设置日志路径
            for (var i = 1; i <= 4; i++) {
                obj.DPSDK_SetToolBtnVisible(i, false);
            }
            obj.DPSDK_SetToolBtnVisible(7, false);
            obj.DPSDK_SetToolBtnVisible(9, false);
            obj.DPSDK_SetControlButtonShowMode(1, 0);
            obj.DPSDK_SetControlButtonShowMode(2, 0);
        }
        //初始化窗口数量
        function ButtonCreateWnd_onclick() {
            var obj = document.getElementById("DPSDK_OCX");
            var nWndCount = "4";
            obj.DPSDK_SetWndCount(gWndId, nWndCount);
            obj.DPSDK_SetSelWnd(gWndId, 0);
        }
        //登陆
        function ButtonLogin_onclick() {
            var obj = document.getElementById("DPSDK_OCX");
            var nRet = obj.DPSDK_Login(szIP, szPort, szUsername, szPassword);
            if (nRet == 0) {
                var aa = obj.DPSDK_LoadDGroupInfo();
                var xmlDoc = obj.DPSDK_GetDGroupStr();
                var arr=parseXml(xmlDoc);
                setTimeout(function () {
                    //播放
                    if(arr!=null&&arr.length>0){
                        if(arr[0].children.length>0){
                            var iChannel=$("#"+arr[0].children[0].id);
                            $("#iChannelID").find(iChannel).addClass("layui-this");
                            ButtonStartRealplayByWndNo_onclick(arr[0].children[0].value);
                        }
                    }
                }, 1000);
            }
        }
        //播放
        function ButtonStartRealplayByWndNo_onclick(szCameraId) {
            var obj = document.getElementById("DPSDK_OCX");
            //var szCameraId = document.getElementById("textCameraID").value;
            var nStreamType = "1";
            var nMediaType = "3";
            var nTransType = "1";

            var nWndNo = obj.DPSDK_GetSelWnd(gWndId);
            var nRet = obj.DPSDK_StartRealplayByWndNo(gWndId, nWndNo, szCameraId, nStreamType, nMediaType, nTransType);
        }

        function parseXml(xmlStr) {
            //创建文档对象
            var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
            xmlDoc.async = "false";
            xmlDoc.loadXML(xmlStr);
            //提取网点数据
            var departments = xmlDoc.getElementsByTagName('Department');
            var arr = [];
            if (departments != null && departments.length > 0) {
                for (var i = 0; i < departments.length; i++) {
                    var tepCoding = departments[i].getAttribute("coding");
                    if (tepCoding == coding) {
                        var devices = departments[i].getElementsByTagName("Device");
                        var channels = departments[i].getElementsByTagName("Channel");
                        if (devices != null && devices.length > 0) {
                            for (var j = 0; j < devices.length; j++) {
                                var map = {};
                                var deviceId = devices[j].getAttribute("id");
                                map.id = deviceId;
                                map.children = [];
                                if (channels != null && channels.length) {
                                    for (var t = 0; t < channels.length; t++) {
                                        var channelId = channels[t].getAttribute("id");
                                        if (channelId.indexOf(deviceId) >= 0) {
                                            var channel = {};
                                            channel.id = deviceId+"_"+t;
                                            channel.value = channelId;
                                            map.children.push(channel);
                                        }
                                    }
                                }
                                arr.push(map);
                            }
                        }
                    }
                }
            }


            //设备信息
            var devices = xmlDoc.getElementsByTagName('Devices');
            if (devices != null && devices.length > 0) {
                for (var i = 0; i < devices.length; i++) {
                    var deviceArr = devices[i].getElementsByTagName("Device");
                    if (deviceArr != null && deviceArr.length > 0) {
                        for (var t = 0; t < deviceArr.length; t++) {
                            var tempId = deviceArr[t].getAttribute("id");
                            var channels = deviceArr[t].getElementsByTagName("Channel");
                            if (arr.length > 0) {
                                for (var j = 0; j < arr.length; j++) {
                                    if (tempId == arr[j].id) {
                                        arr[j].name = deviceArr[t].getAttribute("name");
                                        if (arr[j].children.length > 0) {
                                            for (var r = 0; r < arr[j].children.length; r++) {
                                                var childId = arr[j].children[r].value;
                                                if (channels != null && channels.length > 0) {
                                                    for (var p = 0; p < channels.length; p++) {
                                                        var channelId = channels[p].getAttribute("id");
                                                        if (channelId == childId) {
                                                            arr[j].children[r].name = channels[p].getAttribute("name");
                                                            break
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //插入列表
            if (arr.length > 0) {
                for (var j = 0; j < arr.length; j++) {
                    var li = $("<li class=\"layui-nav-item layui-nav-itemed\"><a href=\"javascript:;\">" + arr[j].name + "</a><dl id=\"" + arr[j].id + "\" class=\"layui-nav-child\"></dl></li>");
                    $("#iChannelID").append(li);
                    if (arr[j].children.length > 0) {
                        for (var r = 0; r < arr[j].children.length; r++) {
                            var childId = arr[j].children[r].id;
                            var childName = arr[j].children[r].name;
                            var childvalue = arr[j].children[r].value;
                            $("#"+arr[j].id).append("<dd id='" + childId + "' ><a href='javascript:ButtonStartRealplayByWndNo_onclick(\"" + childvalue + "\");'>" + childName + "</a></dd>");
                        }
                    }
                }
            }
            return arr;
        }

        layui.use('element', function(){
            var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块

            //监听导航点击
            element.on('nav(demo)', function(elem){
            });
        });
    </script>
</head>
<body>
<div class="layui-container" style="width: 100%">
    <div class="layui-row" style="width: 100%">
        <div id="ml"  style="text-align: right;overflow-y: auto;overflow-x:hidden;background-color: #4E5465" class="layui-col-xs3 layui-col-sm3 layui-col-md4">
            <ul id="iChannelID" class="layui-nav layui-nav-tree layui-inline" lay-filter="demo"
                style="margin-right: 10px;text-align: left;">

            </ul>
        </div>
        <div id="sp" class="layui-col-xs9 layui-col-sm9 layui-col-md8">
            <object id="DPSDK_OCX" classid="CLSID:D3E383B6-765D-448D-9476-DFD8B499926D" style="width: 100%; height: 100%"
                    codebase="DpsdkOcx.cab#version=1.0.0.0"></object>
        </div>
    </div>
</div>
</body>
</html>

