<!doctype html>
<html lang="en">
<head>
        <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
        <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>

        <@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>
        <@com.script name="static/js/preview.js"></@com.script>
        <@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
        <@com.script name="static/thirdparty/mustache/mustache.js"></@com.script>
    <style type="text/css">
        #dataTable{
            width: 90%;
            text-align: center;
            margin: 10px auto;
        }
        #dataTable tr{
            line-height: 40px;
        }
        #dataTable td,th{
            text-align: center;
        }
    </style>

    <script type="text/javascript">

        var dict ={};

        window.onload =function (ev) {
            init();
            //默认
            BindEvent();
        }

        function init() {
            var endDate = new Date();
            var startDate = new Date(endDate.getTime()- 7*24 * 60 * 60 * 1000);
            var startDateStr =convertDateToStr(startDate);
            var endDateStr = convertDateToStr(endDate);
            $("#startDate").val(startDateStr);
            $("#endDate").val(endDateStr);
            $.ajax({
                url: "/omp/video/cameraRegions",
                success: function (data) {
                    if(data != null) {
                        $.each(data.results, function (index, value) {
                            value.length=value.children.length + 2;
                            var organArr = [];
                            $.each(value.children, function (i, v) {
                                organArr.push(v.name);
                            });
                            dict[value.name] = {organArr: organArr,cameraCount:0,useFre:0,useCount:0,offLine:0};
                        });
                        for(var i=0;i<data["results"].length;i++){
                            var item =  data["results"][i];
                            item.index = i+1;
                        }
                        var tableHtml = Mustache.render($('#cameraStatisticsTpl').html(), data);
                        $('.tableContainer').append(tableHtml);
                        refreshData(startDateStr,endDateStr);
                    }
                }
            });
        }

        function convertDateToStr(date) {
            var year = date.getFullYear();
            var month =date.getMonth()+1;
            month =(month<10 ? "0"+month:month);
            var hours =date.getHours();
            var mins = date.getMinutes();
            var seconds = date.getSeconds();
            var day = date.getDate(); //获取当前日(1-31)
            date =(day<10 ? "0"+month:month);
            if(hours<10){
                hours ="0"+hours;
            }
            if(mins<10){
                mins ="0"+mins;
            }
            if(seconds<10){
                seconds ="0"+seconds;
            }
            return year+"-"+month+"-"+day+" "+hours+":" + mins+":"+seconds;
        }

        function BindEvent() {
            //初始化时间控件
            layui.use('laydate', function (laydate) {
                laydate.render({
                    elem: '#startDate',
                    type: 'datetime',
                    change: function(value, date){ //监听日期被切换
                        startDate = date;
                    }
                });

                laydate.render({
                    elem: '#endDate',
                    type: 'datetime',
                    change: function(value, date){ //监听日期被切换
                        endDate = date;
                    }
                });
            });

            $("#search").click(function () {
                var startDateStr = $("#startDate").val();
                var endDateStr = $("#endDate").val();
                if(startDate<endDate){
                    layui.use(["layer"],function (layer) {
                        layer.msg("开始时间不能大于结束时间！");
                    });
                    return;
                }
                refreshData(startDateStr,endDateStr);
            });

            $("#export").click(function () {
                var startDateStr = $("#startDate").val();
                var endDateStr = $("#endDate").val();
                if(startDate<endDate){
                    layui.use(["layer"],function (layer) {
                        layer.msg("开始时间不能大于结束时间！");
                    });
                    return;
                }
                window.location='<@core.rootPath/>/video/export?startDateStr='+startDateStr+"&endDateStr="+endDateStr;
            });
        }

        function refreshData(startDateStr,endDateStr) {
            $.post("/omp/video/countCameraLog",{startDateStr:startDateStr,endDateStr:endDateStr},function (data) {
                statisticsData(data);
                fetchData(data);
            });
        }
        //分发渲染数据
        function fetchData(data) {
            $("[data-name=cameraCount],[data-name=useFre],[data-name=useCount],[data-name=offLine]").html("");
            for(var rowName in data){
                var dataItem = data[rowName];
                //获取所有兄弟元素
                $siblings = $("[data-name="+rowName+"]").siblings();
                for(var key in dataItem){
                    $siblings.map(function (a,b) {
                        var $b = $(b);
                        var value = $b.attr("data-name");
                        if(value){
                            if(dataItem[value]&&dataItem[value]!=0){
                                if(value == 'useFre') {
                                    $b.html(dataItem[value].toFixed(4));
                                } else {
                                    $b.html(dataItem[value]);
                                }
                            }
                        }
                    })
                }
            }
        }
        //统计
        function statisticsData(data) {
            //初始化数据
            for(var organName in dict){
                dict[organName]["cameraCount"]=0;
                dict[organName]["useFre"]=0;
                dict[organName]["useCount"]=0;
                dict[organName]["offLine"]=0;
                dict[organName]["noUseCount"]=0;
            }
            for(var organName in data){
                var organ =data[organName];
                var groupName = group(organName);
                if(dict[groupName]){
                    dict[groupName]["cameraCount"]+=organ["cameraCount"];
                    dict[groupName]["useCount"]+=organ["useCount"];
                    dict[groupName]["offLine"]+=organ["offLine"];
                    dict[groupName]["noUseCount"]+=organ["noUseCount"];
                    dict[groupName]["useFre"]+=(organ["useFre"]/dict[groupName]["organArr"].length);
                }
            }
            //小计赋值
            //合计统计
            var all ={cameraCount:0,useFre:0,useCount:0,offLine:0,noUseCount:0};
            for(var groupName in dict){
                data[groupName+"小计"] = dict[groupName];
                all["cameraCount"]+=dict[groupName]["cameraCount"];
                all["useCount"]+=dict[groupName]["useCount"];
                all["offLine"]+=dict[groupName]["offLine"];
                all["noUseCount"]+=dict[groupName]["noUseCount"];
            }
            debugger
            data["总共合计"] =all;
        }
        //判断是哪组
        function group(organName) {
            for(var groupName in dict){
                if(groupName==organName){
                    return organName;
                }
                if(!dict[groupName]){
                    continue;
                }
                var organs = dict[groupName]["organArr"];
                for(var j=0;j<organs.length;j++){
                    if(organName==organs[j]){
                        return groupName;
                    }
                }
            }
        }
    </script>

    <script id="cameraStatisticsTpl" type="x-tpl-mustache">
            <table style="border-collapse: collapse;" id="dataTable" border="1" cellspacing="0">
                <tr style="line-height: 40px;font-size: 20px;">
                    <th colspan="7">"慧眼守土"监控探头使用频率表</th>
                </tr>
                <tr>
                    <th>序号</th>
                    <th>行政区</th>
                    <th>使用总次数</th>
                    <th>监控点总个数</th>
                    <th>平均使用次数</th>
                    <th>未使用点数</th>
                    <th>备注</th>
                </tr>
                {{#results}}
                    <tr>
                        <td>{{index}}</td>
                        <td data-name="{{name}}">{{name}}</td>
                        <td data-name="useCount"></td>
                        <td data-name="cameraCount"></td>
                        <td data-name="useFre"></td>
                        <td data-name="noUseCount"></td>
                        <td data-name="bz"></td>
                    </tr>
                {{/results}}
                <tr>
                    <th colspan="2" data-name="总共合计">合计</th>
                    <td data-name="useCount"></td>
                    <td data-name="cameraCount"></td>
                    <td data-name="useFre"></td>
                    <td data-name="noUseCount"></td>
                    <td data-name="bz"></td>
                </tr>
            </table>
        </script>
</head>
<body>
<div class="tableContainer" style="width: 100%;height: 100%;">
    <div class="layui-form">
        <div class="layui-form-item" style="width: 50%;margin: auto">
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 100px">开始时间</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="startDate" placeholder="开始时间">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" style="width: 100px">结束时间</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="endDate" placeholder="结束时间">
                </div>
            </div>
            <div class="layui-inline">
                <button class="layui-btn" id="search" style="margin-left: 40px">查询</button>
                <button class="layui-btn" id="export" style="margin-left: 40px">导出</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
