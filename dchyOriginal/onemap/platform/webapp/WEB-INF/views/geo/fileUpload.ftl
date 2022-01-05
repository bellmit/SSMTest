<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width">
    <title>上传文件测试页面</title>
<@com.script name="resources/js/vendor/jquery-1.10.1.min.js"></@com.script>
<@com.script name="static/js/plupload.full.min.js"></@com.script>
<@com.style name="resources/css/main.css"></@com.style>
<@com.script name="resources/js/main.js"></@com.script>
<@com.style name="resources/css/messenger/messenger.css"></@com.style>
<@com.style name="resources/css/messenger/messenger-theme-flat.css"></@com.style>
</head>
<body>
<div id="filelist">Your browser doesn't have Flash, Silverlight or HTML5 support.</div>
<br/>

<div id="container">
    <a id="pickfiles" href="javascript:;">[选择文件]</a>
    <a id="uploadfiles" href="javascript:;">[开始上传]</a>
</div>

<br/>
<pre id="console"></pre>

<script type="text/javascript" language="javascript">
    var root_path='<@com.rootPath/>';
    var uploader = new plupload.Uploader({
        runtimes : 'html5,flash,silverlight,html4',
        browse_button : 'pickfiles', // you can pass in id...
        container: document.getElementById('container'), // ... or DOM Element itself
        url : '/omp/geometryService/shp/upload?layerName=SDE.TEST&check=false',
        flash_swf_url : '../js/Moxie.swf',
        silverlight_xap_url : '../js/Moxie.xap',

        filters : {
            max_file_size : '10mb',
            mime_types: [
                {title : "Image files", extensions : "jpg,gif,png"},
                {title : "Zip files", extensions : "zip"},
                {title:"xls files",extensions:"xls,xlsx"}
            ]
        },

        init: {
            PostInit: function() {
                document.getElementById('filelist').innerHTML = '';

                document.getElementById('uploadfiles').onclick = function() {
                    uploader.start();
                    return false;
                };
            },

            FilesAdded: function(up, files) {
                plupload.each(files, function(file) {
                    document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
                });
            },

            UploadProgress: function(up, file) {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            },

            Error: function(up, err) {
                document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
            }
        }
    });

    uploader.init();
    uploader.init

</script>
</body>
</html>