<html>
<head>
    <TITLE>视频</TITLE>
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=EDGE">
    <META http-equiv="Content-Type" content="text/html; charset=utf-8">
    <META http-equiv="Pragma" content="no-cache">
    <META http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <META http-equiv="Expires" content="0">
    <style type="text/css">
        html,body{
            width:100%;
            height:100%;
        }
    </style>
<@com.script name="static/js/video/dss/jquery-1.7.1.min.js"></@com.script>
    <script>
        var token = "${token!}";
        var iStreamType = "${iStreamType!}";
        var indexCode = "${indexCode!}";
        var netZoneId = "${netZoneId!}";

        //视频直播控件初始化
        $(function () {
            init();
        });

        //初始化
        function init() {
            $.ajax({
                url: "/" + window.location.pathname.split("/")[1] + "/network/open/getResponse",
                data: {
                    token: token,
                    iStreamType: iStreamType,
                    indexCode: indexCode,
                    netZoneId: netZoneId
                },
                success: function (r) {
                    if (r != null) {
                        var OCXobj = document.getElementById("PlayViewOCX");
                        OCXobj.SetOcxMode(0);
                        OCXobj.SetWndNum(1);
                        console.log(r);
                        var ret = OCXobj.StartTask_Preview_InWnd(r,0);
                    }
                }
            });
        }
    </script>
</head>
<body>
<!-- 添加预览控件（需要先在windows下注册） -->
<object classid="clsid:04DE0049-8359-486e-9BE7-1144BA270F6A" id="PlayViewOCX"  width="100%" height="100%" name="ocx" >
</object>
</body>
</html>

