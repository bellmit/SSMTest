layui.use(['jquery', 'form', 'layer', 'element', 'laydate', 'table'], function () {
    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer
    element = layui.element
    laydate = layui.laydate
    table = layui.table


    form.on('select(gxbmmcselect)', function (data) {
        layui_gxbmid = data.value;
        layui_gxjsid = '';
        selectRoles(data.value);
    });

    form.on('select(gxjsmc)', function (data) {
        layui_gxjsid = data.value;
    });


    var param = {
        gxjssj: "",
        gxkssj: "",
        gxbmid: "",
        gxjsid: "",
        gxywmc: ""
    };


    var param_page = {
        page: "",
        size: "",
        gxjssj: "",
        gxkssj: "",
        gxbmid: "",
        gxjsid: ""
    }

    var echartParam = {
        gxbmid: "",
        gxjssj: "",
        gxkssj: "",
        gxjsid: "",

    }

    var left_xdata = [];
    var left_ydata = [];


    var resultdata = [];

    var layui_gxbmid = "";
    var layui_gxjsid = "";


    // 渲染开始时间
    var kssj;
    laydate.render({
        elem: '#kssj'
    });

    // 渲染结束时间
    var jssj;
    laydate.render({
        elem: '#jssj'
    });

    var leftChart = echarts.init(document.getElementById('left'));
    var rightChart = echarts.init(document.getElementById('right'));


    // 动态加载部门角色和部门
    selectOrgan();


    //初始加载 表格
    loadTable(1, 10, param);
    setOptionZxt(echartParam);


    /**
     * 加载表格
     * @param _param
     */
    function loadTable(page, size, param) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywrzxx/list?page=" + page + "&size=" + size,
            type: "post",
            dataType: 'json',
            data: JSON.stringify(param),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: '#tabgxjk',
                    data: res.content,
                    limit: size,
                    cols: [[
                        {field: 'ROWNUM_', title: '序号', align: 'center'}
                        , {field: 'GXBMMC', title: '共享部门', align: 'center'}
                        , {field: 'GXJSMC', title: '共享角色', align: 'center'}
                        , {field: 'GXYWMC', title: '共享业务', align: 'center'}
                        , {field: 'GXCS', title: '共享次数', align: 'center'}
                    ]],
                    done: function () {
                        layui.laypage.render({
                            elem: 'tabgxjk_param',
                            limits: [10, 20, 30], //10,20,30
                            count: res.totalElements,  //显示总条数
                            curr: page,
                            limit: size,
                            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    param_page.page = obj.curr;
                                    param_page.size = obj.limit;
                                    loadTable(param_page.page, param_page.size, param)
                                }
                            }
                        })

                    },
                });
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    function setOptionZxt(echartParam) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywrzxx/tjxxlist",
            type: "post",
            dataType: 'json',
            data: JSON.stringify(echartParam),
            contentType: "application/json",
            success: function (res) {
                left_xdata = [];
                left_ydata = [];
                resultdata = [];
                for (var i = 0; i < res.length; i++) {
                    left_xdata[i] = res[i].GXYWMC
                }
                for (var i = 0; i < res.length; i++) {
                    left_ydata[i] = res[i].GXCS
                }
                left_ydata = left_ydata.map(function (item, index, array) {
                    return item - 0;
                });
                leftoption = {
                    title: {
                        text: '各类业务共享数量统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        data: left_xdata,
                        axisTick: {
                            alignWithLabel: true
                        },
                        axisLabel: {
                            interval: 0,
                            formatter: function (value) {
                                var str = "";
                                var num = 4; //每行显示字数
                                var valLength = value.length; //该项x轴字数
                                var rowNum = Math.ceil(valLength / num); // 行数
                                if (rowNum > 1 && left_xdata.length > 6) {
                                    for (var i = 0; i < rowNum; i++) {
                                        var temp = "";
                                        var start = i * num;
                                        var end = start + num;

                                        temp = value.substring(start, end) + "\n";
                                        str += temp;
                                    }
                                    return str;
                                } else {
                                    return value;
                                }
                            }
                        }
                    },
                    yAxis: {
                        type: 'value'
                    },

                    series: [{
                        name: "共享次数",
                        data: left_ydata,
                        type: 'bar',
                        barWidth: '60%',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                };

                leftChart.setOption(leftoption);

                for (var i = 0; i < res.length; i++) {
                    var oneData = {};
                    oneData.value = res[i].GXCS;
                    oneData.name = res[i].GXYWMC;
                    resultdata.push(oneData);
                }


                rightoption = {
                    title: {
                        text: '各类业务共享占比统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} :  ({d}%)'
                    },
                    series: [
                        {
                            type: 'pie',
                            radius: '55%',
                            data: resultdata,

                        }
                    ]
                };
                rightChart.setOption(rightoption);

            },
            error: function () {
                layer.msg("数据为空");
            }
        });


    }


    // 台账查询
    $('#search').on('click', function () {
        echartParam.gxkssj = $("#kssj").val();
        echartParam.gxjssj = $("#jssj").val();
        echartParam.gxbmid = layui_gxbmid;
        echartParam.gxjsid = layui_gxjsid;
        if (echartParam.gxkssj != '' && echartParam.gxjssj != '') {
            if (echartParam.gxkssj > echartParam.gxjssj) {
                layer.msg("开始时间不能大于结束时间！");
                return false;
            }
        }
        setOptionZxt(echartParam)
        param.gxkssj = $("#kssj").val();
        param.gxjssj = $("#jssj").val();
        param.gxbmid = layui_gxbmid;
        param.gxjsid = layui_gxjsid;
        loadTable(1, 10, param)
    });


    //重置按钮
    $("#reset").on('click', function () {
        param.gxkssj = '';
        param.gxjssj = '';
        param.gxbmid = "";
        param.gxjsid = "";
        param.gxywmc = '';
        echartParam.gxkssj = '';
        echartParam.gxjssj = '';
        echartParam.gxbmid = '';
        echartParam.gxjsid = '';
        layui_gxbmid = '';
        layui_gxjsid = ''
        $("#gxbmmc").val('');
        $("#gxjsmc").val('');
        form.render();
    })


    function selectOrgan() {
        $.ajax({
            type: 'POST',
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/department/list",
            data: {},
            dataType: 'json',
            success: function (res) {
                var html = '';
                res.forEach(function (item) {
                    html += '<option value=' + item.organId + '>' + item.organName + '</option>'
                });
                $('.gxbmmc select').append(html);
                form.render('select');

            }
        })
    }

    function selectRoles(organid) {
        if (organid != null && organid != '') {
            $.ajax({
                type: "GET",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/role/list/" + organid,
                data: organid,
                dataType: "json",
                async: 'false',
                success: function (res) {
                    var RoleHtml = '<option value="">请选择共享角色</option>';
                    res.forEach(function (item) {
                        RoleHtml += '<option value=' + item.roleId + '>' + item.roleName + '</option>'
                    });
                    $('.gxjsmc select').html(RoleHtml);
                    form.render('select');
                }
            });
        } else {
            var RoleHtml = '<option value="">请选择共享角色</option>';
            $('.gxjsmc select').html(RoleHtml);
            form.render('select');
        }
    }

    //柱形图点击事件
    leftChart.on('click', function (data) {
        param.gxywmc = data.name
        loadTableGxrz(1, 10, param);
    })
    //饼图点击事件
    rightChart.on('click', function (data) {
        param.gxywmc = data.name
        loadTableGxrz(1, 10, param);
    })

    //点击上方图表，底部表格联动
    function loadTableGxrz(page, size, param) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywrzjl/list?page=" + page + "&size=" + size,
            type: "post",
            dataType: 'json',
            data: JSON.stringify(param),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: '#tabgxjk',
                    data: res.content,
                    limit: size,
                    cols: [[
                        {field: 'ROWNUM_', title: '序号', align: 'center'}
                        , {field: 'GXBMMC', title: '共享部门', align: 'center'}
                        , {field: 'GXJSMC', title: '共享角色', align: 'center'}
                        , {field: 'GXRMC', title: '共享人', align: 'center'}
                        , {field: 'GXSJ', title: '共享时间', align: 'center'}
                    ]],
                    done: function () {
                        layui.laypage.render({
                            elem: 'tabgxjk_param',
                            limits: [10, 20, 30], //10,20,30
                            count: res.totalElements,  //显示总条数
                            curr: page,
                            limit: size,
                            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    param_page.page = obj.curr;
                                    param_page.size = obj.limit;
                                    loadTableGxrz(param_page.page, param_page.size, param)
                                }
                            }
                        })

                    },
                });
            },
            error: function () {
                layer.msg("请求失败");
            }
        });
    }

    connetCharts(leftChart, rightChart);

    //顶部图表之间联动，点击一个另一个对应部分突出显示
    function connetCharts(chart1, chart2) {
        chart1.on('click', function (target) {
            var option = chart2.getOption();
            option.tooltip[0].alwaysShowContent = true;
            option.tooltip[0].triggerOn = "none";
            chart2.setOption(option, true);
            chart2.dispatchAction({
                type: 'highlight',
                name: target.name,
            })
        })

        chart2.on('click', function (target) {
            var option = chart1.getOption();
            option.tooltip[0].alwaysShowContent = true;
            option.tooltip[0].triggerOn = "none";
            chart1.setOption(option, true);
            chart1.dispatchAction({
                type: 'highlight',
                name: target.name,
            })
        })
    }

});




