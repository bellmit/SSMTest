layui.use(['jquery', 'form', 'layer', 'element', 'laydate', 'table', 'laypage'], function () {
    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer,
        element = layui.element,
        laydate = layui.laydate,
        laypage = layui.laypage,
        table = layui.table;

    //全局变量
    var param = {
        kssj: "",
        jssj: "",
        jsdw: "",
        chdw: ""
    }

    var param_page = {
        page: "",
        size: "",
        kssj: "",
        jssj: "",
        jsdw: "",
        chdw: ""
    }
    var jsdwWtslData = []
    var chdwCjslData = []
    var jsdw_xdata = []
    var jsdw_ydata = []
    var chdw_xdata = []
    var chdw_ydata = []
    var jsdwResultData = []
    var chdwResultData = []


    //渲染时间控件
    laydate.render({
        elem: '#kssj'
    });
    laydate.render({
        elem: '#jssj'
    });

    //渲染echarts
    var jsdw_leftCharts = echarts.init(document.getElementById('jsdw_left'));
    var chdw_rightCharts = echarts.init(document.getElementById('chdw_right'));

    //初始化加载方法
    $(function () {
        getJsdw()
        getChdw()
        getJsdwWtxmsl()
        getChdwCjsl()
        getXmbajlByPage(1, 10, param)
    })


    //获取建设单位和测绘单位列表
    function getJsdw() {
        $.ajax({
            url: getContextPath() + "/msurveyplat-server/rest/v1.0/yyfx/yhdw/list",
            type: "post",
            data: {},
            dataType: "json",
            contentType: "application/json",
            success: function (res) {
                var jsdwHtml = '<option value="">全部</option>'
                res.forEach(function (item) {
                    jsdwHtml += '<option>' + item.WTDW + '</option>'
                })
                $('.jsdw select').html(jsdwHtml)
                form.render('select')
            },
            erroe: function () {
                layer.msg("接口报错")
            }
        })
    }

    //获取测绘单位
    function getChdw() {
        $.ajax({
            url: getContextPath() + "/msurveyplat-server/rest/v1.0/cgtj/chdwxx/list",
            type: 'post',
            dataType: 'json',
            data: {},
            contentType: "application/json",
            success: function (res) {
                var html = '<option value="">全部</option>';
                res.forEach(function (item) {
                    html += '<option>' + item.DWMC + '</option>'
                });
                $('.chdw select').append(html)
                form.render('select')
            },
            error: function () {
                layer.msg("接口报错！")
            }
        })
    }

    //获取建设单位委托项目数量
    function getJsdwWtxmsl() {
        $.ajax({
            url: getContextPath() + "/msurveyplat-server/rest/v1.0/yyfx/jsdwwtxm/list",
            type: 'post',
            data: {},
            contentType: "application/json",
            success: function (res) {
                jsdwWtslData = res
                if (res.length >= 5) {
                    jsdw_xdata = []
                    jsdw_ydata = []
                    jsdwResultData = []
                    for (var i = 4; i >= 0; i--) {
                        jsdw_xdata.push(res[i].WTSL)
                        jsdw_ydata.push(res[i].WTDW)
                        var oneData = []
                        oneData.name = res[i].WTDM
                        oneData.value = res[i].WTSL
                        jsdwResultData.push(oneData)
                    }
                } else {
                    jsdw_xdata = []
                    jsdw_ydata = []
                    jsdwResultData = []
                    for (var i = res.length; i >= 0; i--) {
                        jsdw_xdata.push(res[i].WTSL)
                        jsdw_ydata.push(res[i].WTDW)
                        var oneData = []
                        oneData.name = res[i].WTDM
                        oneData.value = res[i].WTSL
                        jsdwResultData.push(oneData)
                    }
                }


                jsdw_leftOption = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                    },
                    yAxis: {
                        type: 'category',
                        data: jsdw_ydata
                    },
                    series: [{
                        name: "委托项目数量",
                        data: jsdw_xdata,
                        type: 'bar',
                        barWidth: '60%',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        },
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'right',
                                    textStyle: {
                                        color: 'black',
                                        fontSize: 14
                                    }
                                }
                            }
                        }
                    }]

                }
                jsdw_leftCharts.setOption(jsdw_leftOption)

            },
            error: function () {
                layer.msg("接口报错")
            }
        })
    }

    function getChdwCjsl() {
        $.ajax({
            url: getContextPath() + "/msurveyplat-server/rest/v1.0/yyfx/chdwcjsl/list",
            type: 'post',
            data: {},
            contentType: "application/json",
            success: function (res) {
                chdwCjslData = res
                if (res.length >= 5) {
                    chdw_xdata = []
                    chdw_ydata = []
                    chdwResultData = []
                    for (var i = 4; i >= 0; i--) {
                        chdw_xdata.push(res[i].CJSL)
                        chdw_ydata.push(res[i].CHDWMC)
                        var oneData = []
                        oneData.name = res[i].CHDWMC
                        oneData.value = res[i].CJSL
                        chdwResultData.push(oneData)
                    }
                } else {
                    chdw_xdata = []
                    chdw_ydata = []
                    chdwResultData = []
                    for (var i = res.length; i >= 0; i++) {
                        chdw_xdata.push(res[i].CJSL)
                        chdw_ydata.push(res[i].CHDWMC)
                        var oneData = []
                        oneData.name = res[i].CHDWMC
                        oneData.value = res[i].CJSL
                        chdwResultData.push(oneData)
                    }
                }


                chdw_rightOption = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                    },
                    yAxis: {
                        type: 'category',
                        data: chdw_ydata
                    },
                    series: [{
                        name: "承接项目数量",
                        data: chdw_xdata,
                        type: 'bar',
                        barWidth: '60%',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        },
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'right',
                                    textStyle: {
                                        color: 'black',
                                        fontSize: 14
                                    }
                                }
                            }
                        }
                    }]

                }
                chdw_rightCharts.setOption(chdw_rightOption)
            },
            error: function () {
                layer.msg("接口报错")
            }
        })
    }

    function loadchdw() {
        layer.open({
            type: "1",
            title: "测绘单位承接项目数量",
            area: ['1000px', '600px'],
            shade: 0.3,
            content: $('#chdwModal').html(),
            success: function () {
                table.render({
                    elem: "#LAY_table_chdw",
                    data: chdwCjslData,
                    limit: chdwCjslData.length,
                    cols: [[
                        {type: 'numbers', title: '序号', fixed: 'left', width: 100},
                        {field: "CHDWMC", title: "测绘单位名称", align: "center"},
                        {field: "CJSL", title: "承接项目数量", align: "center"}
                    ]]
                })
            }
        })
    }

    function loadjsdw() {
        layer.open({
            type: "1",
            title: "建设单位委托项目数量",
            area: ['1000px', '600px'],
            shade: 0.3,
            content: $('#jsdwModal').html(),
            success: function () {
                table.render({
                    elem: "#LAY_table_jsdw",
                    data: jsdwWtslData,
                    limit: jsdwWtslData.length,
                    cols: [[
                        {type: 'numbers', title: '序号', fixed: 'left', width: 100},
                        {field: "WTDW", title: "建设单位名称", align: "center"},
                        {field: "WTSL", title: "委托项目数量", align: "center"}
                    ]]
                })
            }
        })
    }

    $("#moreChdw").on("click", function () {
        loadchdw()
    })

    $("#moreJsdw").on("click", function () {
        loadjsdw()
    })


    //获取项目备案记录
    function getXmbajlByPage(page, size, param) {
        $.ajax({
            url: getContextPath() + "/msurveyplat-server/rest/v1.0/yyfx/xmbajl/list?page=" + page + "&size=" + size,
            type: 'post',
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json",
            success: function (res) {
                table.render({
                    elem: '#xmbajl',
                    data: res.content,
                    limit: size,
                    cols: [[
                        {field: 'ROWNUM_', title: '序号', align: 'center'},
                        // {field: 'SLBH', title: '备案编号', align: 'center'},
                        {field: 'BABH', title: '项目编号', align: 'center'},
                        {field: 'GCBH', title: '项目代码', align: 'center'},
                        {field: 'GCMC', title: '工程名称', align: 'center'},
                        {field: 'WTDW', title: '建设单位名称', align: 'center'},
                        {field: 'CHDWMC', title: '测绘单位名称', align: 'center'},
                        {field: 'SLSJ', title: '备案时间', align: 'center'},
                    ]],
                    done: function () {
                        laypage.render({
                            elem: 'xmbajl_page',
                            limits: [10, 20, 30],
                            count: res.totalElements,
                            curr: page,
                            limit: size,
                            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    param_page.page = obj.curr;
                                    param_page.size = obj.limit;
                                    getXmbajlByPage(param_page.page, param_page.size, param)
                                }
                            }
                        })
                    }
                })
            },
            error: function () {
                layer.msg("接口报错")
            }
        })
    }

    //查询按钮
    $('#search').on('click', function () {
        param.kssj = $("#kssj").val()
        param.jssj = $("#jssj").val()
        if (param.kssj != '' && param.jssj != '') {
            if (param.kssj > param.jssj) {
                layer.msg("开始时间不能大于结束时间！");
                return false;
            }
        }
        param.jsdw = $(".jsdw select option:selected").text()
        param.chdw = $(".chdw select option:selected").text()
        if (param.jsdw == "全部") {
            param.jsdw = ""
        }
        if (param.chdw == "全部") {
            param.chdw = ""
        }
        getXmbajlByPage(1, 10, param);
    })

    //重置按钮
    $('#reset').on('click', function () {
        param.kssj = '';
        param.jssj = '';
        param.jsdw = '';
        param.chdw = '';
        $('#chdw').val("")
        $('#jsdw').val("")
        form.render()
        getXmbajlByPage(1, 10, param);
    })
})