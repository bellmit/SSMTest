<#assign cssContent>
    <style type="text/css">
        .select-box {
            border: solid 1px #ddd;
            box-sizing: border-box;
            vertical-align: middle;
            display: inline-block;
            width: auto;
        }

        .laydate-icon, .laydate-icon-default, .laydate-icon-danlan, .laydate-icon-dahong, .laydate-icon-molv {
            height: 31px !important;
        }
    </style>
</#assign>

<@master.html title="分析操作日志" css=cssContent>
    <div id="mainDiv" class="pd-20">
        <div class="text-c">
            <form id="logForm">
                年份：
                <span class="select-box">
              <select class="select" size="1" name="YEAR" style="width: 100px;">
                    <option value="">选择年份</option>
                    <#list years as year>
                        <option value="${year}">${year}</option>
                    </#list>
              </select>
            </span>

                日期范围：
                <input type="text" id="start" name="startDate" class="input-text laydate-icon" style="width:180px;">
                -
                <input type="text" id="end" name="endDate" class="input-text laydate-icon" style="width:180px;">

                <input type="text" name="USERNAME" id="" placeholder="用户名称" style="width:250px" class="input-text">
                <input type="text" name="TYPE" id="" placeholder="分析类型" style="width:250px" class="input-text">
                <button name="" id="searchLogBtn" class="btn btn-success" type="button"><i class="fa fa-search"></i> 搜日志
                </button>
                <button name="" id="exportOperationLogBtn" class="btn btn-success" type="button"><i
                            class="fa fa-sign-out"></i>导出日志
                </button>
            </form>
        </div>

        <div class="mt-20">
            <table class="table table-border table-bordered table-bg table-hover table-sort">
                <thead>
                <tr class="text-c">
                    <th width="20%">用户名</th>
                    <th width="20%">部门</th>
                    <th width="20%">分析类型</th>
                    <th width="20%">分析时间</th>
                    <th width="20%">分析目的</th>
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
    <script src="<@com.rootPath/>/static/js/json2.js"></script>
    <@com.script name="static/js/cfg/main.js"></@com.script>
    <script type="text/javascript">
        var serializeForm = null;

        /**
         * 初始化加载日志信息
         */
        $(document).ready(function () {
            getOperationLogs({}, 1, 10);
        });

        /**
         * 条件查询系统日志
         */
        $("#searchLogBtn").on("click", function () {
            getOperationLogs($("#logForm").serializeObject(), 1, 10);
        });

        /**
         * 导出日志
         */
        $("#exportOperationLogBtn").on("click", function () {
            window.location = '<@core.rootPath/>/geometryService/log/export?condition=' + JSON.stringify($("#logForm").serializeObject());
        });

        /**
         * 加载日志信息
         * @param condition
         * @param page
         * @param size
         */
        function getOperationLogs(condition, page, size) {
            var index = layer.load(2, {
                shade: [0.4, '#fff'] //0.1透明度的白色背景
            });
            $.ajax({
                url: '<@core.rootPath/>/geometryService/log/search',
                method: 'post',
                data: {
                    page: page,
                    size: size,
                    condition: JSON.stringify(condition)
                },
                success: function (rp) {
                    layer.close(index);
                    var totalPages = 1;
                    if (rp.totalPages != undefined && rp.totalPages != 0) {
                        totalPages = rp.totalPages;
                    }

                    laypage({
                        cont: 'operationPageCont',
                        pages: totalPages,
                        curr: function () {
                            return page ? page : 1;
                        }(),
                        jump: function (e, first) { //触发分页后的回调
                            if (!first) {
                                getOperationLogs({}, e.curr, size);
                            }
                        }
                    });

                    $("#operationLogList").empty();
                    if (rp.content != null && rp.content.length > 0) {
                        $("#operationLogList").append(Mustache.render($("#OperationLogItem").html(), rp));
                    } else {
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
            choose: function (datas) {
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
            choose: function (datas) {
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);

    </script>
    <script id="OperationLogItem" type="x-tpl-mustache">
    {{#content}}
    <tr class="text-c">
        <td>{{uSERNAME}}</td>
        <td>{{dEPARTMENT}}</td>
        <td>{{tYPE}}</td>
        <td>{{tIME}}</td>
        <td>{{pURPOSE}}</td>
    </tr>
    {{/content}}


    </script>
</@master.html>