<#assign cssContent>
<style type="text/css">
    .leftDiv {
        width: 25%;
        min-width: 272px;
        padding: 0;
    }

    .laydate-icon, .laydate-icon-default, .laydate-icon-danlan, .laydate-icon-dahong, .laydate-icon-molv {
        height: 31px !important;
    }

    .laydate-icon {
        border: 1px solid #3bb4f2 !important;
    }

    .form-horizontal .form-label {
        color: #3bb4f2;
    }

    .input-text {
        border-color: #3bb4f2;
    }

    .select-box {
        border: solid 1px #3bb4f2;
    }

    .select-box option {
        color: #3bb4f2;
    }

    .expandBtnCls {
        height: 40px;
        width: 18px;
        padding: 0px;
        line-height: 40px !important;
        position: absolute;
        right: 0;
        top: 45%;
    }

    .panelShadow {
        box-shadow: -10px 10px 10px #E6E5E5
    }
</style>
</#assign>

<@master.html title="监控点统计" css=cssContent>
<div id="rightCondition" class="leftDiv container" style="display: none">
    <div class="panel panel-secondary panelShadow">
        <div class="panel-header"><i class="fa fa-line-chart"></i> 查询统计
            <i id="closePanel" class="fa fa-times f-r" title="隐藏" style="cursor: pointer;"></i>
        </div>
        <div class="panel-body">
            <form id="chartForm" class="form form-horizontal" novalidate="novalidate">
                <div class="row cl">
                    <label class="form-label  col-sm-4  col-xs-4 text-r">开始时间：</label>

                    <div class="formControls col-sm-8 col-xs-8">
                        <input type="text" id="start" name="startDate" class="input-text radius laydate-icon"
                               style="color: #00a3ef">
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label  col-sm-4  col-xs-4 text-r">结束时间：</label>

                    <div class="formControls col-sm-8 col-xs-8">
                        <input type="text" id="end" name="endDate" class="input-text radius laydate-icon"
                               style="color: #00a3ef">
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-sm-4  col-xs-4">统计方式：</label>

                    <div class="formControls col-sm-8 col-xs-8">
                        <span class="select-box radius ">
                          <select class="select" size="1" id="showType" style="color: #00a3ef">
                              <option value="" selected>请选择统计方式</option>
                              <option value="camera">按监控点统计</option>
                              <option value="user">按使用人统计</option>
                          </select>
                        </span>
                    </div>
                </div>

                <div id="tplPlace">

                </div>

                <div class="row cl text-c">
                    <button id="searchBtn" class="btn btn-secondary-outline radius"><i class="fa fa-search"></i> 查询
                    </button>
                    <button id="exportBtn" type="button" class="btn btn-secondary-outline radius"><i class="fa fa-sign-out"></i> 导出
                    </button>
                    <button id="exportAllBtn" type="button" class="btn btn-secondary-outline radius"><i class="fa fa-sign-out"></i> 导出全部
                    </button>
                    <button id="exportByOrgan" type="button" class="btn btn-secondary-outline radius"><i class="fa fa-sign-out"></i> 监控统计
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="container" style="position: relative">
    <div id="mainDiv" class="pd-20" style="margin: 0 auto 0">
    </div>
    <a id="openBtn" href="#rightCondition" class="btn btn-secondary-outline expandBtnCls">
        <i class="fa fa-angle-double-left"></i>
    </a>
</div>
    <@com.script name="static/thirdparty/laydate/laydate.js"></@com.script>
    <@com.script name="static/thirdparty/jquery/jquery.panelslider.min.js"></@com.script>
    <@com.script name="static/js/cfg/main.js"></@com.script>
    <@core.script name="static/thirdparty/echarts/echarts.min.js"></@core.script>
<script type="text/javascript">
    //图表参数
    var option = {
        title: {
            text: '监控点使用统计',
            left: 'left',
            textStyle: {
                color: '#3bb4f2'
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            x: 'center',
            data: []
        },
        radar: [
            {
                indicator: [],
                center: ['50%', '55%'],
                radius: 250,
                name: {
                    formatter: '【{value}】',
                    textStyle: {
                        color: '#3bb4f2'
                    }
                }
            }
        ],
        series: [
            {
                type: 'radar',
                radarIndex: 0,
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                tooltip: {
                    trigger: 'item'
                },
                data: [

                ]
            }
        ]
    };

    var myChart = null;

    $(function () {
        // 初始化布局宽度调整
        $("#mainDiv").css({
            width: $(window).width() - $("#rightCondition").width() - 40,
            height: $(window).height() * 0.85
        });
        $(".panel-body").css("height", $(window).height() - 71);

        // 初始化layDate日期插件
        initLayDate();

        //默认展开检索条件
        $.panelslider($("#rightCondition"), {side: 'right', clickClose: false, duration: 500});

        //必须以dom对象，不要使用jquery的选择器获取
        var mapDom = document.getElementById("mainDiv");

        // 基于准备好的dom，初始化echarts实例
        myChart = echarts.init(mapDom);
        var formData = $("#chartForm").serializeObject();
        formData.user='${userId!}';
        ajxaToShowChart(formData);
        myChart.setOption(option);

        //捕获监听
        listeners();
    });

    //初始化layDate时间控件
    function initLayDate() {
        laydate.skin('yalan'); //设置皮肤为雅兰
        var start = {
            elem: '#start',
            format: 'YYYY-MM-DD hh:mm:ss',
            min: '1970-01-01 00:00:00', //设定最小日期为1970
            max: '2099-06-16 23:59:59', //最大日期
            istime: true,
            istoday: true,
            fixed: true, //是否固定在可视区域
            choose: function (datas) {
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: '#end',
            format: 'YYYY-MM-DD hh:mm:ss',
            min: '1970-01-01 00:00:00',
            max: '2099-06-16 23:59:59',
            istime: true,
            istoday: false,
            fixed: true, //是否固定在可视区域
            choose: function (datas) {
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);
        $("#start").val(laydate.now(-7, 'YYYY-MM-DD hh:mm:ss'));
        $("#end").val(laydate.now(new Date().toTimeString(), 'YYYY-MM-DD hh:mm:ss'));
    }

    /**
     *  页面监听
     */
    function listeners() {
        // 展开条件检索
        $("#openBtn").panelslider({side: 'right', clickClose: false, duration: 200});

        // 收缩条件检索
        $("#closePanel").click(function () {
            $.panelslider.close();
        });

        //根据类型选择结果
        $("#showType").on('change', function () {
            var type = $(this).val();
            $.ajax({
                url: '<@core.rootPath/>/video/statistic/fetch',
                method: 'post',
                data: {
                    type: type
                },
                success: function (rp) {
                    $("#tplPlace").empty();
                    if ("camera" == type)
                        $("#tplPlace").append(Mustache.render($("#cameraListTpl").html(), rp));
                    else if ("user" == type)
                        $("#tplPlace").append(Mustache.render($("#userListTpl").html(), rp));
                }
            });
        });

        //按照条件查询
        $("#searchBtn").on('click', function () {
            var formData = $("#chartForm").serializeObject();
            if ($("#showType").val() == '') {
                layer.msg("请选择统计方式！", {icon: 5});
                return false;
            } else if ($("#user").val() == '') {
                layer.msg("请选择监控点使用人！", {icon: 5});
                return false;
            } else if ($("#camera").val() == '') {
                layer.msg("请选择一个监控点！", {icon: 5});
                return false;
            }
            ajxaToShowChart(formData);
            return false;
        });

        //按照条件查询
        $("#exportBtn").on('click', function () {
            var formData = $("#chartForm").serializeObject();
            if ($("#showType").val() == '') {
                layer.msg("请选择统计方式！", {icon: 5});
                return false;
            } else if ($("#user").val() == '') {
                layer.msg("请选择监控点使用人！", {icon: 5});
                return false;
            } else if ($("#camera").val() == '') {
                layer.msg("请选择一个监控点！", {icon: 5});
                return false;
            }
            exportExcel(formData);
            return false;
        });


        //按照条件查询
        $("#exportAllBtn").on('click', function () {
            var start = $("#start").val();
            var end = $("#end").val();

            exportAll({startDate:start,endDate:end});
            return false;
        });

        $("#exportByOrgan").on('click', function () {
            var start = $("#start").val();
            var end = $("#end").val();

            exportByOrgan({startDate:start,endDate:end});
            return false;
        });
    }


    function exportAll(data) {
        window.location = '<@core.rootPath/>/video/statistic/detail/exportAll?data=' + JSON.stringify(data);
    }

    function exportByOrgan(data) {
        window.location = '<@core.rootPath/>/video/statistic/detail/exportByOrgan?data=' + JSON.stringify(data);
    }
    /**
     * 异步获取图表数据
     * @returns {boolean}
     */
    function ajxaToShowChart(data) {
        $.ajax({
            url: '<@core.rootPath/>/video/statistic/detail',
            method: 'post',
            data: {
                data: JSON.stringify(data)
            },
            success: function (rp) {
                option.radar[0].indicator = rp.indicator;
                option.series[0].data = rp.data;
                option.legend.data = [];

                $.each(rp.data, function (i, n) {
                    option.legend.data.push(n.name);
                });
                if (rp.indicator.length == 0) {
                    layer.msg("没有查询到对应条件的结果！", {icon: 5});
                    return false;
                }
                myChart.clear();
                myChart.setOption(option);
            }
        });
    }


    function exportExcel(data) {
        window.location = '<@core.rootPath/>/video/statistic/detail/export?data=' + JSON.stringify(data);
    }
</script>

<script id="cameraListTpl" type="x-tpl-mustache">
<div class="row cl">
    <label class="form-label  col-sm-4  col-xs-4 text-r">监控点：</label>

    <div class="formControls col-sm-8 col-xs-8">
        <span class="select-box radius ">
          <select class="select" size="1" id="camera" name="camera" style="color: #00a3ef">
              <option value="" selected>请选择监控点</option>
              {{#result}}
              <option value="{{cameraId}}">{{cameraName}}</option>
             {{/result}}
          </select>
        </span>
    </div>
</div>

</script>

<script id="userListTpl" type="x-tpl-mustache">
 <div class="row cl">
    <label class="form-label  col-sm-4  col-xs-4 text-r">使用人：</label>
    <div class="formControls col-sm-8 col-xs-8">
        <span class="select-box radius ">
          <select class="select" size="1" id="user" name="user" style="color: #00a3ef">
              <option value="" selected>请选择使用人</option>
              {{#result}}
              <option value="{{userId}}">{{userName}}</option>
              {{/result}}
          </select>
        </span>
    </div>
</div>
</script>
</@master.html>