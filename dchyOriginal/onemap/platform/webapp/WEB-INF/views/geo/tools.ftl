<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>onemap测试页面</title>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/font/css/font-awesome.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/gridtree/gridtree.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/plugins/loading/loading.css"/>
    <link rel="stylesheet" href="<@com.rootPath/>/static/css/hack.css"/>
</head>
<body>


<div id="omp" class="container" style="margin-top: 30px;width: 80%;">

</div>
<br/>
<script src="<@core.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="<@core.rootPath/>/static/js/export/OneMap.js" type="text/javascript"></script>

<script type="text/javascript" language="javascript">
    var root_path = '<@com.rootPath/>';
  var omp= OneMap({mapTpl:'tpl_test',camerasCallback:function(data){
       console.log(data);
      omp.preview();
   }});
//    console.log($.fn.OneMap({mapTpl: 'tpl_test'}).getAppConfig());
//    var $omp = $.fn.OneMap({mapTpl: 'tpl_test'}).getCameras();

//    setTimeout(function(){
//        console.log($omp.cameras[0]);
//    },4000);

</script>
</body>
</html>