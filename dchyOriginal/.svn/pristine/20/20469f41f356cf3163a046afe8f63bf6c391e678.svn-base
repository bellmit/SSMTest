<!DOCTYPE html>
<html lang="en">
    <head>
        <title>慧眼守土-后台管理</title>
        <link rel="shortcut icon" type="image/x-icon" href="<@com.rootPath/>/static/img/favicon.ico" media="screen"/>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
        <@com.style name="static/thirdparty/agsapi/3.14/esri/css/esri.min.css"></@com.style>
        <@com.style name="static/thirdparty/agsapi/3.14/dijit/themes/claro/claro.css"></@com.style>
        <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.min.css"></@com.style>
        <@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
        <@com.style name="static/css/iconfont/iconfont_omp.css"></@com.style>
        <@com.style name="static/css/theme/light-blue/map.css"></@com.style>
        <@com.style name="static/thirdparty/pace/pace-theme-minimal.css"></@com.style>
        <@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
        <style type="text/css">
            .admin-menu-container{
                float: right;
            }
            .admin-menu-container .admin-menu{
                margin: 0px;
                padding: 0px;
                height: 46px;
            }
            .admin-menu-container .admin-menu li{
                list-style: none;
                height:45px;
                width: 120px;
                float: right;
                line-height: 45px;
                color: #F2F2F2 !important;
                font-size: 17px;
                text-align: center;
                cursor: pointer;
            }

            .admin-menu-container .admin-menu .admin-btn:hover{
                background-color: #385fdd;
            }

            #main_iframe{
                width: 100%;
                frameborder="no";
                border="0" ;
                marginwidth="0";
                marginheight="0" ;
                scrolling="no";
                allowtransparency="yes";
            }
        </style>
        <script type="text/javascript">
            var host = window.location.host;
            window.onload =function () {
                var name ="${name}";
                var $div = $("[name="+name+"]");
                $div.parent().css("background-color","#385fdd");
                var url= $div.attr("url");
                $("#main_iframe").attr("src","http://"+host+url);
                bindEvents();
            }
            //绑定单击事件
            function bindEvents() {
                $(".admin-btn").click(function (evt) {
                    var name= $(evt.currentTarget).find("a").attr("name");
                    if(!name){
                        return;
                    }
                    var url ="http://"+host+"/omp/config/admin?name="+name;
                    window.open(url)
                });
            }
        </script>
    </head>

    <body class="claro">
        <!--头部-->
        <div class="nav-topbar" style="display: block">
            <a href="javascript:history.go(0);" class="m-logo"></a>
        <#--标题-->
            <span class="nav-title" style="display: inline;">江苏省慧眼守土监管平台</span>
        
            <div class="admin-menu-container">
                <ul class="admin-menu">
                    <li class="admin-btn"><a url="<@com.rootPath/>/video/log" target="_blank" name="jkrz">监控日志</a></li>
                    <li class="admin-btn"><a url="<@com.rootPath/>/video/cameraStatisticsST" target="_blank" name="syxl">使用效率</a></li>
                    <li class="admin-btn"><a url="<@com.rootPath/>/video/cameraView" target="_blank" name="cktp">查看图片</a></li>
                    <li class="admin-btn"><a url="<@com.rootPath/>/video/offline" target="_blank" name="lxtj">离线统计</a></li>
                    <li class="admin-btn"><a href="<@com.rootPath/>/config" target="_blank">后台管理</a></li>
                </ul>
            </div>
        </div>
        <iframe id="main_iframe" style="height: 90%">
        </iframe>
    </body>
</html>