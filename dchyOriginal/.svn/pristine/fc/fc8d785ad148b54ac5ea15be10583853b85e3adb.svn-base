<!doctype html>
<html lang="en">
<head>
        <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
        <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>

        <@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>
        <@com.script name="static/js/preview.js"></@com.script>
        <@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
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
        var dayDiff=0;
        var dict ={
            "海州分局":{organArr:["第一国土所","第二国土所","第三国土所","海州分局"],cameraCount:0,useFre:0,useCount:0,offLine:0},
            "高新区分局":{organArr:["第八国土所","高新区分局"],cameraCount:0,useFre:0,useCount:0,offLine:0},
            "云台山景区分局":{organArr:["第七国土所","云台山景区分局"],cameraCount:0,useFre:0,useCount:0,offLine:0},
            "连云分局":{organArr:["第五国土所","第六国土所","连云分局"],cameraCount:0,useFre:0,useCount:0,offLine:0},
            "开发区分局":{organArr:["第九国土所","开发区分局"],cameraCount:0,useFre:0,useCount:0,offLine:0},
            "徐圩分局":{organArr:["第四国土所","徐圩分局"],cameraCount:0,useFre:0,useCount:0,offLine:0}
        };

        window.onload =function (ev) {
            init();
            //默认
            BindEvent();
        }

        function init() {
            var now = new Date();
            var endDate= new Date(now.getFullYear(),now.getMonth(),now.getDate());
            var startDate = new Date(endDate.getFullYear(),endDate.getMonth(),endDate.getDate()-7);
            var startDateStr =convertDateToStr(startDate);
            var endDateStr = convertDateToStr(endDate);
            var endDateStas =convertDateToStr(new Date(endDate.getFullYear(),endDate.getMonth(),endDate.getDate()+1));
            $("#startDate").val(startDateStr);
            $("#endDate").val(endDateStr);
            dayDiff=7;
            refreshData(startDateStr,endDateStas);
        }

        function convertDateToStr(date) {
            var year = date.getFullYear();
            var month =date.getMonth()+1;
            month =(month<10 ? "0"+month:month);
            var day = date.getDate(); //获取当前日(1-31)
            date =(day<10 ? "0"+month:month);
            return year+"-"+month+"-"+day;
        }

        function BindEvent() {
            //初始化时间控件
            layui.use('laydate', function (laydate) {
                laydate.render({
                    elem: '#startDate',
                    change: function(value, date){ //监听日期被切换
                        startDate = date;
                    }
                });

                laydate.render({
                    elem: '#endDate',
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
                var end = new Date(endDateStr);
                var start = new Date(startDateStr);
                dayDiff = (end-start)/1000/60/60/24;
                end= new Date(end.getFullYear(),end.getMonth(),end.getDate()+1);
                var endDateStr= convertDateToStr(end);
                refreshData(startDateStr,endDateStr);
            });

            $("#export").click(function () {
                var filePath = "cameraStatistics.cpt";
                var reportUrl = "${reportUrl!}";
                var startDateStr = $("#startDate").val();
                var endDateStr = $("#endDate").val();
                if(startDate<endDate){
                    layui.use(["layer"],function (layer) {
                        layer.msg("开始时间不能大于结束时间！");
                    });
                    return;
                }
                var end = new Date(endDateStr);
                var start = new Date(startDateStr);
                dayDiff = ((end-start)/1000/60/60/24) + 1;
                end= new Date(end.getFullYear(),end.getMonth(),end.getDate()+1);
                var endDateStr= convertDateToStr(end);
                if($.trim(filePath) != "") {
                    location.href = cjkEncode(reportUrl + "/ReportServer?reportlet=" + filePath + "&startDate=" + startDateStr + "&endDate=" + endDateStr + "&diffDays=" + dayDiff + "&format=excel&extype=simple");
                }
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
                                $b.html(dataItem[value]);
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
            }
            for(var organName in data){
                var organ =data[organName];
                if(!organ){
                    continue;
                }
                var groupName = group(organName);
                if(dict[groupName]){
                    //直属分局
                    if(!organ["isZS"]){
                        dict[groupName]["cameraCount"]+=organ["cameraCount"];
                        dict[groupName]["offLine"]+=organ["offLine"];
                    }
                    dict[groupName]["useCount"]+=organ["useCount"];

                    // dict[groupName]["useFre"]+=organ["useFre"]/dict[groupName]["organArr"].length;
                }
            }

            //小计赋值
            //合计统计
            var all ={cameraCount:0,useFre:0,useCount:0,offLine:0};
            for(var groupName in dict){
                if(dict[groupName]["useCount"]){
                    dict[groupName]["useFre"] = (dict[groupName]["useCount"]/dict[groupName]["cameraCount"]/dayDiff).toFixed(4);

                }
                data[groupName+"小计"] = dict[groupName];
                all["cameraCount"]+=dict[groupName]["cameraCount"];
                all["useCount"]+=dict[groupName]["useCount"];
                all["offLine"]+=dict[groupName]["offLine"];
            }
            if(all["useCount"]){
                all["useFre"]=(all["useCount"]/all["cameraCount"]/dayDiff).toFixed(4);
            }

            data["总共合计"] =all;
        }
        //判断是哪组
        function group(organName) {
            for(var groupName in dict){
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

        function cjkEncode(text) {
            if (text == null) {
                return "";
            }
            var newText = "";
            for (var i = 0; i < text.length; i++) {
                var code = text.charCodeAt (i);
                if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".
                    newText += "[" + code.toString(16) + "]";
                } else {
                    newText += text.charAt(i);
                }
            }
            return newText;
        }
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












    <table style="border-collapse: collapse;" id="dataTable" border="1" cellspacing="0">
        <tr style="line-height: 40px;font-size: 20px;">
            <th colspan="6">连云港市区"慧眼守土"监控探头使用频率表</th>
        </tr>
        <tr>
            <th>分局</th>
            <th>中心所</th>
            <th>探头数</th>
            <th>使用频次</th>
            <th>使用强度（次/个*天）</th>
            <th>备注（掉线数,统计期末当天为准)</th>
        </tr>

        <tr>
            <td rowspan="5">海州分局</td>
            <td data-name="第一国土所">第一国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="第二国土所">第二国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="第三国土所">第三国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="海州分局" exclude>海州分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="海州分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td rowspan="3">高新区分局</td>
            <td data-name="第八国土所">第八国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="高新区分局" exclude>高新区分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="高新区分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>

        <tr>
            <td rowspan="3">云台山景区分局</td>
            <td data-name="第七国土所">第七国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="云台山景区分局" exclude>云台山景区分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="云台山景区分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>

        <tr>
            <td rowspan="4">连云分局</td>
            <td data-name="第五国土所">第五国土所</td>
            < <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="第六国土所">第六国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="连云分局" exclude>连云分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="连云分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>

        <tr>
            <td rowspan="3" exclude>开发区分局</td>
            <td data-name="第九国土所">第九国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="开发区分局">开发区分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useFre"></td>
            <td data-name="useCount"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="开发区分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>

        <tr>
            <td rowspan="3">徐圩分局</td>
            <td data-name="第四国土所">第四国土所</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="徐圩分局" exclude>徐圩分局</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
        <tr>
            <td data-name="徐圩分局小计">小计</td>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>

        <tr>
            <th colspan="2" data-name="总共合计">合计</th>
            <td data-name="cameraCount"></td>
            <td data-name="useCount"></td>
            <td data-name="useFre"></td>
            <td data-name="offLine"></td>
        </tr>
    </table>
</div>
</body>
</html>
