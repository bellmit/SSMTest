<#assign cssContent>
<style type="text/css">
    .select-box {
        border: solid 1px #ddd;
        box-sizing: border-box;
        vertical-align: middle;
        display: inline-block;
        width: auto;
    }

    .laydate-icon, .laydate-icon-default, .laydate-icon-danlan, .laydate-icon-dahong, .laydate-icon-molv{
        height: 31px!important;
    }
</style>
</#assign>

<@master.html title="监控点日志" css=cssContent>
<div id="mainDiv" class="pd-20">
    <div class="text-c">
        <form id="logForm">
            <div style="margin-bottom: 5px;">
            年份：
            <span class="select-box">
              <select class="select" size="1" name="year" style="width: 100px;">
              </select>
            </span>
            月份：
            <span class="select-box">
                <select class="select" size="1" name="month" style="width: 100px;">
                    <option value="">选择月份</option>
                    <#list 1..12 as i>
                        <option value="${i}">${i}</option>
                    </#list>
            </select>
            </span>

            行政区：
                <span class="select-box">
                <select class="select" size="1" name="regionName" style="width: 100px;">
                    <option value="">选择行政区</option>
                    <#list regions as region>
                        <option value="${region.name}">${region.name}</option>
                    </#list>
            </select>
            </span>

            摄像头名称：
            <input type="text" id="cameraName" name="cameraName" class="input-text" style="width:180px;">
            </div>

            日期范围：
            <input type="text" id="start" name="startDate" class="input-text laydate-icon" style="width:180px;">
            -
            <input type="text" id="end" name="endDate" class="input-text laydate-icon" style="width:180px;">

            <input type="text" name="username" id="" placeholder="用户名称" style="width:250px" class="input-text">
            <button name="" id="logBtn" class="btn btn-success" type="button"><i class="fa fa-search"></i> 搜日志</button>
            <button name="" id="exportBtn" class="btn btn-success" type="button"><i class="fa fa-sign-out"></i>导出日志</button>
            <button name="" id="bindOrganBtn" class="btn btn-success" type="button"><i class="fa fa-sign-out"></i>行政区部门初始化绑定</button>
        </form>
    </div>

    <div class="mt-20">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <thead>
            <tr class="text-c">
                <th width="15%">部门</th>
                <th width="15%">用户名</th>
                <th width="15%">摄像头名称</th>
                <th width="15%">行政区名称</th>
                <th width="15%">设备id</th>
                <th width="10%">操作内容</th>
                <th width="15%">操作时间</th>
                <#--<td width="10%">总耗时</td>-->
                <#--<td width="10%">下载</td>-->
            </tr>
            </thead>
            <tbody id="logList">

            </tbody>
        </table>
    </div>

    <div id="pageCont" class="mt-20 text-c">

    </div>
</div>
    <@com.script name="static/thirdparty/laydate/laydate.js"></@com.script>
    <@com.script name="static/js/cfg/main.js"></@com.script>
<script type="text/javascript">
    var serializeForm = null;
    $(document).ready(function(){
        //请求日志
        getDatas({},1, 10);
        initDateSelector();
        var start = {
            elem: '#start',
            format: 'YYYY/MM/DD hh:mm:ss',
            min: '1970-01-01 00:00:00', //设定最小日期为1970
            max: '2099-06-16 23:59:59', //最大日期
            istime: true,
            istoday: true,
            choose: function(datas){
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: '#end',
            format: 'YYYY/MM/DD hh:mm:ss',
            min: laydate.now(),
            max: '2099-06-16 23:59:59',
            istime: true,
            istoday: false,
            choose: function(datas){
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);

        $("#logBtn").on("click", function(){
            getDatas($("#logForm").serializeObject(), 1,10 );
        });

        $("#exportBtn").on("click", function(){
            window.location='<@core.rootPath/>/video/log/export?condition='+JSON.stringify($("#logForm").serializeObject());
        });

        $("#bindOrganBtn").on("click", function(){
            var condition = $("#logForm").serializeObject();
            $.ajax({
                url:'<@core.rootPath/>/video/log/bindOrganByUserId',
                method:'post',
                data:{
                    condition:JSON.stringify(condition)
                },
                success:function(data){
                    alert(data.result);
                }
            });
        });
    });

    //获取日志数据
    function getDatas(condition, page, size){
        condition = $("#logForm").serializeObject();
        var index = layer.load(2, {
            shade: [0.4,'#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            url:'<@core.rootPath/>/video/log/search',
            method:'post',
            data:{
                page:page,
                size:size,
                condition:JSON.stringify(condition)
            },
            success:function(rp){
                layer.close(index);
                var totalPages = 1;
                if( rp.result.totalPages!=undefined && rp.result.totalPages!=0){
                    totalPages = rp.result.totalPages;
                }
                laypage({
                    cont: 'pageCont',
                    pages: totalPages,
                    curr: function(){
                        return page ? page : 1;
                    }(),
                    jump: function(e, first){ //触发分页后的回调
                        if(!first){ //一定要加此判断，否则初始时会无限刷新
                            getDatas({},e.curr, size)
                        }
                    }
                });
                $("#logList").empty();
                if(rp.result.content!=null && rp.result.content.length>0){
                    $("#logList").append(Mustache.render($("#logItem").html(), rp.result))
                }else{
                    $("#logList").append('<tr><td colspan="7" class="text-c">暂无日志信息！</td></tr>')
                }
            }
        });
    }

    function initDateSelector() {
        var year = new Date().getFullYear();
        var $selector = $("[name=year]");
        for(var i =year;i>2014;i--){
            var $option =$("<option></option>").html(i).attr("value",i);
            $selector.append($option);
        }
    }
</script>

<script id="logItem" type="x-tpl-mustache">
    {{#content}}
    <tr class="text-c">
        <td>{{userDept}}</td>
        <td>{{userName}}</td>
        <td>{{cameraName}}</td>
        <td>{{regionName}}</td>
        <td>{{cameraId}}</td>
        <td>{{optContent}}</td>
        <td>{{createAt}}</td>
        <#--<td>{{totalTime}}</td>-->
        <#--<td><a href="/omp/video/log/download/{{id}}" title="定时拍照设备xml信息">下载xml</a></td>-->
    </tr>
    {{/content}}
</script>
</@master.html>