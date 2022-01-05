<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>系统参数设置</title>
<@com.style name="static/thirdparty/h-ui/css/H-ui.min.css"></@com.style>
<@com.style name="static/thirdparty/h-ui/css/H-ui.admin.css"></@com.style>
<@com.style name="static/thirdparty/h-ui/skin/blue/skin.css"></@com.style>
<@com.style name="static/thirdparty/h-ui/lib/iconfont/iconfont.css"></@com.style>
    <style>
        body{
            font-size: 13px;
            overflow-x: hidden;
        }
        .container{
            margin-top: 60px;
        }
        .apps-panel{
            margin-top: 20px;
        }
        .apps-panel .panel-heading{
            height: 52px;
            line-height: 52px;
        }
        .apps-panel .panel-heading >a:first-child{
            margin-right:15px;
        }
        /*table*/
        .table th {
            line-height: 20px;
            height: auto;
            font-size: 12px;
            padding: 3px 10px;
            border-bottom: 0;
            text-align: center;
            color: #666666;
            background-color: #efefef;
            background-image: -webkit-gradient(linear, 0 0%, 0 100%, from(#fdfdfd), to(#eaeaea));
            background-image: -webkit-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -moz-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -ms-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -o-linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            background-image: -linear-gradient(top, #fdfdfd 0%, #eaeaea 100%);
            filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fdfdfd', endColorstr='#eaeaea',GradientType=0 );
        }

        .table td{
            border: 1px solid #ddd!important;
        }

        .app-edit{
            margin-left:6px;
        }

        .app-key{
            font-size: 16px;
         }

        .app-input{
            padding-left: 8px;
            font-size: 16px;
            width: 95%;
            height: 100%;
        }


    </style>
</head>
<body>
<div class="container" id="app">
    <div class="panel panel-default apps-panel">
        <div class="panel-heading" style="text-align: center;background-color: #f2f2f2;font-size: 20px;font-weight: bold">
           参数设置
        </div>
        <table class="table table-condensed table-bordered table-hover tree">
            <thead>
            <tr>
                <th width="45%">配置名称</th>
                <th width="45%">值</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody v-for="value in props">
            <tr>
                <td class="app-key" style="padding-left: 16px;"> {{ $key }}</td>
                <td><input type="text" class="app-input" value="{{ value }}" id="{{ $key }}" data-id="{{ $key }}"></td>
                <td style="text-align: center">
                    <a class="btn btn-xs btn-primary app-edit">修改</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
<@com.script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@com.script>
<@com.script name="static/thirdparty/layer/layer.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.treegrid.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.cookie.js"></@com.script>
<@com.script name="static/js/cfg/main.js"></@com.script>
<@com.script name="static/thirdparty/vue/vue.min.js"></@com.script>
</body>
<script>
    $(function(){
       var vm = new Vue({
            el: '#app',
            data: {props:${application}}
        });

        $(".app-edit").on("click",function(){
            var value = $(this).parent().parent().find("input").val();
            var id = $(this).parent().parent().find("input").data('id');
            if(value!=undefined&&value!=''){
                $.ajax({
                    url:root + "/config/app/alter",
                    data:{prop:id,value:value},
                    async:false,
                    success:function(r){
                        layer.msg("修改成功!",{icon:1,time:1000});
                    }

                });
            }else{
                layer.msg("值不能为空!",{icon:2,time:1000});
                return;
            }
        });
    });

</script>
</html>