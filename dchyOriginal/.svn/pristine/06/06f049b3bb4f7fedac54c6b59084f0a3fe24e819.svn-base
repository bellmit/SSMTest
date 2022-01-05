<@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>
<@com.script name="static/js/json2.js"></@com.script>
<@com.script name="static/js/preview.js"></@com.script>
<@com.script name="static/js/startupVideo.js"></@com.script>
<script type="text/javascript">
    window.onload=function (ev) {
        var cameraParam;
        var type =${type};
        var indexCode ='${indexCode!}';
        var cameras;
        if(typeof('${cameras!}') == "undefined" || '${cameras!}' == ''){
            return;
        }else{
            cameras = ${cameras!};
        }
        cameraParam = cameras;
        var no ='${pNo}';
        if(!cameraParam || cameraParam.length<1){
            alert("未能找到摄像头");
            return;
        }
        if(cameraParam.length==1){
            cameraParam =cameraParam[0].indexCode;
        }
        var errorCode ='${errorCode!}';
        if(errorCode == 1){
            alert("未能找到摄像头");
            return;
        }
        if(type=='0'){
            startupVideo(type,cameraParam);
        }else if(type=='1'){
            startupVideo(type,cameraParam,no);
        }
    }
</script>