<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
<@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.min.css"></@com.style>
<@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
<@com.style name="static/thirdparty/layer/skin/layer-omp.css"></@com.style>

    <title>实时预览</title>
    <style>
        .main {
            width: 690px;
            margin: 2px auto;
            text-align: center;
            padding: 2px;
        }

        .main .zoom-ul {
            width: 100%;
            margin: 20px 5px;
        }

        .main ul a {
            margin-right: 10px;
        }

        .player-panel {
            width: 500px;
            height: 400px;
            float: right;
        }

        .tool-panel {
            position: absolute;
            padding: 8px;
            width: 190px;
            height: 400px;
            border: 1px dotted #eee;
        }

        .tool-table {
            width: 90%;
            margin: auto;
            margin-top: 10px;
        }

        fieldset {
            border: none;
            padding: 0;
            border-top: 1px solid #eee;
        }

        legend {
            margin-left: 50px;
            padding: 0 10px;
            font-size: 13px;
            border: 0;
            margin-bottom: 10px;
            width: auto;
        }

        .preset-wrapper {
            margin: auto;
            display: none;
        }

        .preset-li {
            text-align: left !important;
            margin-left: 20px;
            line-height: 20px;
            height: 20px;
            margin-top: 5px;
            margin-bottom: 5px;
            border-bottom: 1px solid #eee;
        }

        #presetList {
            max-height: 100px;
            overflow-y: scroll;
            overflow-x: hidden;
        }
    </style>
</head>
<body onbeforeunload="closeSession();">
<div class="main">
    <div class="tool-panel">
        <h5>${cameraName!}</h5>
        <table cellpadding="4" cellspacing="0" class="tool-table">
            <tr>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="5">↖</a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="2">↑</a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="8">↗</a></td>
            </tr>
            <tr>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="4">←</a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="1"><i class="fa fa-stop"></i></a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="7">→</a></td>
            </tr>
            <tr>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="6">↙</a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="3">↓</a></td>
                <td><a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="9">↘</a></td>
            </tr>
        </table>
        <ul class="list-unstyled zoom-ul">
            <li>
                <a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="23"><i class="fa fa-plus"></i>&nbsp;放大</a>
                <a class="btn btn-primary btn-sm ptz-ctrl"  data-opt="24"><i class="fa fa-minus"></i>&nbsp;缩小</a>
            </li>
            <li></li>
        </ul>
        <#--<div class="text-center preset-wrapper">-->
            <#--<fieldset><legend>预置位</legend></fieldset>-->
            <#--<ul class="list-unstyled">-->
                <#--<li>-->
                    <#--<a class="btn btn-primary btn-sm preset-btn"  data-opt="set" title="设置当前位置为预设位">设置</a>-->
                    <#--<a class="btn btn-primary btn-sm preset-btn"  data-opt="get">查询</a>-->
                <#--</li>-->
            <#--</ul>-->
            <#--<ul id="presetList" class="list-unstyled"></ul>-->
        <#--</div>-->
    </div>
    <div class="player-panel">
        <object id="ocx" style="width:100%;height: 100%; " classid="CLSID:3556A474-8B23-496F-9E5D-38F7B74654F4"
                codebase="<@com.rootPath/>/bin/ocx/IVS_OCX.cab#version=2,2,0,9">
        </object>
    </div>
</div>
</body>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<@com.script name="static/js/ocx/realplay.js"></@com.script>
<script>
    <#--ivs 平台预览视频-->
    var user = '${user!}';
    var pwd = '${pwd!}';
    var ip = '${server!}';
    var port = '${port!}';
    var cameraCode = '${camera.indexCode!}'+'#'+"${camera.vcuId!}";

    $(function () {
        layer.config();
        ocx = document.getElementById("ocx");
        init();
        login(user, pwd, ip, port);
        setLayout(1);
        setTitlebar(0);
        setScale(2);

        var r = startPlay(cameraCode);
        if (r === 0) {
            //启用云台控制及其他按钮
            $("a:disabled").removeAttr("disabled");
            $(".ptz-ctrl").on("mousedown", function () {
                ptzControl($(this).data("opt"));
            });
            $(".ptz-ctrl").on("mouseup", function () {
                ptzControl(1);
            });

            $(".preset-btn").on("click",function () {
                var opt = $(this).data("opt");
                switch (opt){
                    case 'get':
                        refreshPresets();
                        break;
                    case 'set':
                        layer.open({
                            type: 1,
                            offset: ['200px','5px'],
                            title: '提示',
                            content: '<div style="margin: auto 20px;"><p>输入预置位名称:</p><input type="text" id="presetNameInput"></div>',
                            btn: ['确定'],
                            yes: function () {
                                 var ret = addPreset(cameraCode,$("#presetNameInput").val());
                                 if(ret){
                                     runPreset(ret);
                                 }
                                layer.closeAll();
                                layer.msg('设置成功');
                            }
                        });
                        break;
                }
            });
        }
    });

    /***
     * refresh presets
     */
    function refreshPresets() {
        var presets = getPresetList(cameraCode);
        if(presets.length > 0){
            $("#presetList").empty();
            $.each(presets,function (index,item) {
                $("#presetList").append("<li class='preset-li' data-idx=" + item.index + "><span>" + item.name + "</span>" +
                        "<a class='btn btn-danger btn-xs pull-right preset-opera-btn' data-opt='del'><i class='fa fa-times'></i></a>" +
                        "<a class='btn btn-info btn-xs pull-right preset-opera-btn' data-opt='run'><i class='fa fa-play'></i></a></li>");
            });
            $(".preset-opera-btn").on('click',function () {
                var optcode = $(this).data('opt');
                var presetIndex = $(this).parent().data('idx');
                switch (optcode){
                    case 'run':
                        runPreset(presetIndex);
                        break;
                    case 'del':
                        var ret = delPreset(cameraCode,presetIndex);
                        if(ret === 0){
                            layer.msg("删除成功!");
                            refreshPresets();
                        }
                        break;
                }
            });

        }
    }

</script>
</html>