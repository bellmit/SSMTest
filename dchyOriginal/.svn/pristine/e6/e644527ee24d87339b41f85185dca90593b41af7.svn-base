<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>JSON编辑器</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/omp/static/thirdparty/josnVeiw/app-min.css">
    <link rel="stylesheet" type="text/css" href="/omp/static/thirdparty/josnVeiw/jsoneditor-min.css">
    <script type="text/javascript" src="/omp/static/thirdparty/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/omp/static/thirdparty/josnVeiw/jsoneditor-min.js"></script>
    <script type="text/javascript" src="/omp/static/thirdparty/josnVeiw/ace-min.js"></script>
    <script type="text/javascript" src="/omp/static/thirdparty/josnVeiw/app-min.js"></script>
    <style>
        #header, #codeEditor, #splitter {
            display: none;
        }
    </style>
</head>
<body>
<div id="header">
    <a href="#" class="header">
        <img alt="JSON Editor Online" title="JSON Editor Online" src="#" id="logo">
    </a>
    <div id="menu">
        <ul>
            <li>
                <a id="clear" title="Clear contents">清空</a>
            </li>
            <li>
                <a id="open" title="Open file from disk">
                    打开
                    <span id="openMenuButton" title="Open file from disk or url">
                    &#x25BC;
                    </span>
                </a>
                <ul id="openMenu">
                    <li>
                        <a id="menuOpenFile" title="Open file from disk">打开文件</a>
                    </li>
                    <li>
                        <a id="menuOpenUrl" title="Open file from url">打开地址</a>
                    </li>
                </ul>
            </li>
            <li>
                <a id="save" title="Save file to disk">保存</a>
            </li>
        </ul>
    </div>
</div>
<div id="auto">
    <div id="contents">
        <div id="codeEditor" style="display: none;"></div>
        <div id="splitter" style="display: none;">
            <div id="buttons">
                <div>
                    <button id="toTree" class="convert" title="Copy code to tree editor">
                        <div class="convert-right"></div>
                    </button>
                </div>
                <div>
                    <button id="toCode" class="convert" title="Copy tree to code editor">
                        <div class="convert-left"></div>
                    </button>
                </div>
            </div>
            <div id="drag">
            </div>
        </div>

        <div id="treeEditor"></div>

        <script type="text/javascript">
            app.load();
            app.resize();
            $(".redo").after('<button class="saveCfg" title="保存修改"></button>');
            /**
             * 重新加载json视图结果
             * @param data
             */
            function reloadCfg(data) {
                app.setValue(data);
                app.resize();
                app.CodeToTree();
            }

            /**
             * 获取提交结果
             */
            function getSubmitJson() {
                return app.getResult();
            }

            function resizeIframe() {
                app.resize();
            }

        </script>

    </div>
</div>
</body>
</html>