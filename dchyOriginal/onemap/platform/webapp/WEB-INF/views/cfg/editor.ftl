<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>模板功能自定义</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
<@com.style name="static/thirdparty/codemirror/codemirror.css" />
<@com.style name="static/thirdparty/codemirror/theme/dracula.css" />
    <style>
        .code-wrapper {
            margin-top: 30px;
        }

        #htmlCode {
            padding-left: 0px !important;
        }

        #styleCode {
            padding-right: 0px !important;
        }

        #styleCode > .code-type {
            right: 0px;
        }

        .code-type {
            position: absolute;
            right: 15px;
            top: 0px;
            z-index: 999;
            border-radius: 0px !important;
        }

        #jsCode {
            margin-top: 20px;
        }

        .CodeMirror-hints {
            position: absolute;
            z-index: 10;
            overflow: hidden;
            list-style: none;

            margin: 0;
            padding: 2px;

            -webkit-box-shadow: 2px 3px 5px rgba(0, 0, 0, .2);
            -moz-box-shadow: 2px 3px 5px rgba(0, 0, 0, .2);
            box-shadow: 2px 3px 5px rgba(0, 0, 0, .2);
            border-radius: 3px;
            border: 1px solid silver;

            background: white;
            font-size: 90%;
            font-family: monospace;

            max-height: 20em;
            overflow-y: auto;
        }

        .CodeMirror-hint {
            margin: 0;
            padding: 0 4px;
            border-radius: 2px;
            white-space: pre;
            color: black;
            cursor: pointer;
        }

        li.CodeMirror-hint-active {
            background: #08f;
            color: white;
        }

        .btn-content {
            margin-top: 10px;
        }

        .tips {
            color: #ff79c6;
        }

    </style>
</head>
<body>
<div class="container code-wrapper">
    <div class="col-xs-12">
        <div class="col-xs-6" id="htmlCode"><span class="label label-info code-type">html</span></div>
        <div class="col-xs-6" id="styleCode"><span class="label label-success code-type">css</span></div>
    </div>
    <div class="col-xs-12" id="jsCode"><span class="label label-danger code-type">javascript</span></div>
    <div class="col-xs-12">
        <div class="col-xs-10"></div>
        <div class="col-xs-2 text-right btn-content">
            <a class="btn btn-success" id="saveAll">保存</a>&nbsp;
            <a class="btn btn-default" href="<@com.rootPath/>/config/widget/download?widget=${widget!}">下载</a>
        </div>
    </div>
    <div class="col-xs-12 tips">
        Tips: <br/>
        1、<code>Ctrl + S</code> 保存当前编辑模块
        <br/>
        2、<code>Alt + &rarr;</code> 自动补全
    </div>
</div>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/codemirror/codemirror.js"></@com.script>
<@com.script name="static/thirdparty/codemirror/mode/javascript.js"></@com.script>
<@com.script name="static/thirdparty/codemirror/mode/css.js"></@com.script>
<@com.script name="static/thirdparty/codemirror/mode/xml.js"></@com.script>
<@com.script name="static/thirdparty/codemirror/mode/htmlmixed.js"></@com.script>
<@com.script name="static/thirdparty/hint/show-hint.js"></@com.script>
<@com.script name="static/thirdparty/hint/xml-hint.js"></@com.script>
<@com.script name="static/thirdparty/hint/javascript-hint.js"></@com.script>
<@com.script name="static/thirdparty/hint/html-hint.js"></@com.script>
<@com.script name="static/thirdparty/hint/css-hint.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>


<script type="text/javascript">

    //js基础模板
    var baseWidgetJs = "define([\"dojo/_base/declare\",\n" +
            "     \"dojo/_base/lang\",\n" +
            "    \"map/core/BaseWidget\"\n" +
            "], function (declare,lang,BaseWidget) {\n" +
            "    var me = declare([BaseWidget], {\n" +
            "        constructor: function () {\n" +
            "        },\n" +
            "      \n" +
            "        onCreate: function () {\n" +
            "        },\n" +
            "      \n" +
            "        onPause: function () {\n" +
            "        },\n" +
            "      \n" +
            "        onOpen: function () {\n" +
            "        }\n" +
            "    });\n" +
            "    return me;\n" +
            "});";

    var options = {
        lineWrapping: true,
        lineNumbers: true,
        theme: 'dracula',
        smartIndent: true,
        "extraKeys": {
            "Alt-Right": "autocomplete",
            "Ctrl-S": "saveAll"
        }
    };

    var htmlCoder = CodeMirror(document.getElementById("htmlCode"), $.extend(options, {
        value: "<!--在这里编写html代码-->",
        mode: 'htmlmixed'
    }));

    var styleCoder = CodeMirror(document.getElementById("styleCode"), $.extend(options, {
        value: "/*在这里编写css模块*/",
        mode: 'css'
    }));

    var jsCoder = CodeMirror(document.getElementById("jsCode"), $.extend(options, {
        value: baseWidgetJs,
        mode: 'javascript'
    }));

    /**
     * 自动补齐
     */
    CodeMirror.commands.autocomplete = function (cm) {
        switch (cm.getMode().name) {
            case 'htmlmixed':
                cm.showHint({hint: CodeMirror.hint.html});
                break;
            case 'css':
                cm.showHint({hint: CodeMirror.hint.css});
                break;
            case 'javascript':
                cm.showHint({hint: CodeMirror.hint.javascript});
                break;
        }
    };


    CodeMirror.commands.saveAll = saveAll;

    /**
     * 保存模块内容
     */
    function saveTextToFile(cm) {
        var type = cm.getMode().name;
        var text = cm.getValue();
        $.ajax({
            url: "<@com.rootPath/>/config/save/new/widget",
            data: {type: type, widget: '${widget}', content: text},
            success: function (r) {

            },
            error: function (r) {
                console.log(r);
            }
        });

    }

    /**
     * 保存按钮监听
     */
    $("#saveAll").on("click", function () {
        saveAll();
    });

    /**
     * 保存所有模块内容
     */
    function saveAll(create) {
        saveTextToFile(htmlCoder);
        saveTextToFile(styleCoder);
        saveTextToFile(jsCoder);
        if (typeof create !== 'boolean'||!create)
            layer.msg("保存成功！", {icon: 1, time: 2000});
    }

    saveAll(true);
</script>
</body>
</html>