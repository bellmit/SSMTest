<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>国土资源“一张图”-信息门户子系统</title>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/bootstrap/bootstrap.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/font/css/font-awesome.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/loading/loading.css" />
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/hack.css"/>
    <!--[if lt IE 9]>

    <script src="<@com.rootPath/>/static/js/hack/html5shiv.js"></script>

    <script src="<@com.rootPath/>/static/js/hack/respond.min.js"></script>

    <![endif]-->
    <style>
        .loadingWrap{
            position: absolute;
            top: 0;
            left: 0;
            height: 100%;
            width: 100%;
            display: block;
        }
        .loadingWrap .inner{
            position: relative;
            height: 100%;
            width: 100%;
        }
        .loadingWrap .inner .main{
            background: #FFF url('/omp/static/css/plugins/loading/loading/loading_analysis.gif') 0px 20px no-repeat;
            /*height: 60px;*/
            position: absolute;
            z-index: 10000;
            top: 35%;
            left: 40%;
            line-height: 32px;
            padding-left: 100px;
            /*padding-right: 20px;*/
            margin-top: -30px;
            border: 0px solid #222;
            color: #332F2D;
            font-size: 15px;

        }
        .loadingWrap .inner .cover{
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 9000;
            overflow: hidden;
            background-color: #FFF;
            opacity: .8;
            filter: alpha(opacity=80);
        }
    </style>
</head>
<body>
<div id="loading" class='loadingWrap'>
    <div class='inner'>
        <div class='main'>${title!} 进行中,请耐心等待...</div>
        <div class='cover'></div>
    </div>
</div>
<div class="container" style="width: 100%;height: 100%;overflow-y: hidden;">
<iframe id="analysisFrame" name="analysisResult" style="width: 100%;height: 100%" frameborder="no" border="0"
        marginwidth="0"
        marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>
</div>
<script src="<@com.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js"></script>
<script src="<@com.rootPath/>/static/js/plugins.js"></script>
<script type="text/javascript">
    var params='${params!}';
    var type='${type!}';

    $(function () {

        $(".container").hide();
        $("#loading").show();
        doRedirect();

        window.onbeforeunload = onbeforeunload_handler;
        $("#analysisFrame").load(function () {
            $("#loading").hide();
            $(".container").show();
        });
    });

    /***
     * 分析正在进行中 关闭浏览器 给出提示
     * */
    function onbeforeunload_handler(){
       if($("#loading").css("display")=="block"){
           var warning="分析操作尚未完成，关闭后无法查看结果!";
           return warning;
       }
    }
    /***
     *转向分析结果页面
     */
    function doRedirect(){

        if (params == null || params == "{}" || type == 'common')return;

        var dataObj = JSON.parse(params.replace(/\\/g, ""));

        var url = "/omp/geometryService/analysis/${type!}";
        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = url;
        tempForm.target = "analysisResult";
        tempForm.appendChild(createHiddenInput("geometry",'${geometry!}'));
        for (var key in dataObj) {
            tempForm.appendChild(createHiddenInput(key, dataObj[key]));
        }
        tempForm.appendChild(createHiddenInput("HubPassed",true));
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

    /***
     * create hidden input element
     * @param name
     * @param value
     */
    function createHiddenInput(name, value) {
        var hideInput = document.createElement("input");
        hideInput.type = "hidden";
        hideInput.name = name;
        if (typeof value == "object")
            hideInput.value = JSON.stringify(value);
        else
            hideInput.value = value;
        return hideInput;
    }
</script>
</body>
</html>