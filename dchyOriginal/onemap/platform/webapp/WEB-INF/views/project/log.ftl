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

<@master.html title="项目操作日志" css=cssContent>
<div id="mainDiv" class="pd-20">
    <div class="text-c">
        <form id="logForm">
            年份：
            <span class="select-box">
              <select class="select" size="1" name="year" style="width: 100px;">
              </select>
            </span>

            日期范围：
            <input type="text" id="start" name="startDate" class="input-text laydate-icon" style="width:180px;">
            -
            <input type="text" id="end" name="endDate" class="input-text laydate-icon" style="width:180px;">

            <input type="text" name="username" id="" placeholder="用户名称" style="width:250px" class="input-text">
            <button name="" id="searchLogBtn" class="btn btn-success" type="button"><i class="fa fa-search"></i> 搜日志</button>
            <button name="" id="exportOperationLogBtn" class="btn btn-success" type="button"><i class="fa fa-sign-out"></i>导出日志</button>
        </form>
    </div>

    <div class="mt-20">
        <table class="table table-border table-bordered table-bg table-hover table-sort">
            <thead>
            <tr class="text-c">
                <th width="20%">用户名</th>
                <th width="20%">行政区</th>
                <th width="20%">项目名称</th>
                <th width="20%">操作内容</th>
                <th width="20%">操作时间</th>
            </tr>
            </thead>
            <tbody id="operationLogList">

            </tbody>
        </table>
    </div>

    <div id="operationPageCont" class="mt-20 text-c">

    </div>
</div>
    <@com.script name="static/thirdparty/laydate/laydate.js"></@com.script>
    <@com.script name="static/js/cfg/main.js"></@com.script>
<script type="application/javascript">
    /**
     * 初始化加载日志信息
     */
    $(document).ready(function(){
        getOperationLogs({}, 1, 10);
        initDateSelector();

    });


    function initDateSelector() {
        var year = new Date().getFullYear();
        var $selector = $("[name=year]");
        for(var i =year;i>2014;i--){
            var $option =$("<option></option>").html(i).attr("value",i);
            $selector.append($option);
        }
    }
    /**
     * 条件查询系统日志
     */
    $("#searchLogBtn").on("click", function(){
        getOperationLogs($("#logForm").serializeObject(), 1, 10);
    });

    /**
     * 导出日志
     */
    $("#exportOperationLogBtn").on("click", function(){
        window.location='<@core.rootPath/>/project/log/export?condition='+JSON.stringify($("#logForm").serializeObject());
    });

    /**
     * 加载日志信息
     * @param condition
     * @param page
     * @param size
     */
    function getOperationLogs(condition, page, size){
        var index = layer.load(2, {
            shade: [0.4,'#fff'] //0.1透明度的白色背景
        });

        $.ajax({
            url: '<@core.rootPath/>/project/log/search',
            method: 'POST',
            data: {
                condition: JSON.stringify(condition),
                page: page,
                size: size
            },
            success: function(rp){
                layer.close(index);
                var totalPages = 1;
                if (rp.result.totalPages != undefined && rp.result.totalPage != 0){
                    totalPages = rp.result.totalPages;
                }

                laypage({
                    cont: 'operationPageCont',
                    pages: totalPages,
                    curr: function(){
                        return page ? page : 1;
                    }(),
                    jump: function(e, first){ //触发分页后的回调
                        if(!first){
                            getOperationLogs(condition, e.curr, size);
                        }
                    }
                });

                $("#operationLogList").empty();
                if (rp.result.content != null && rp.result.content.length > 0){
                    $("#operationLogList").append(Mustache.render($("#OperationLogItem").html(), rp.result));
                }else{
                    $("#operationLogList").append('<tr><td colspan="7" class="text-c">暂无日志信息！</td></tr>');
                }
            }
        });
    }

    /**
     * 查询的时间范围
     * @type {{elem: string, format: string, min: string, max: string, istime: boolean, istoday: boolean, choose: Function}}
     */
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

</script>
<script id="OperationLogItem" type="x-tpl-mustache">
    {{#content}}
    <tr class="text-c">
        <td>{{userName}}</td>
        <td>{{regionName}}</td>
        <td>{{proName}}</td>
        <td>{{optContent}}</td>
        <td>{{createAt}}</td>
    </tr>
    {{/content}}
</script>
</@master.html>