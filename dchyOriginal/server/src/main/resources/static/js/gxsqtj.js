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
    var left_series = [];


    var resultdata = [];
    var resultInnerData = [];

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
    
    addFind();
    addFindIndex();
    // 动态加载部门角色和部门
    selectOrgan();


    //初始加载 表格
    loadTable(1, 10, param);
    setOptionZxt(echartParam);
    selectShjg();
    //渲染业务名称下拉框
    function selectShjg() {
        var param = {
            data: {
                zdlx: ["SHJG"]
            },
            head: {}
        };
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-promanage/zdx/getzdxx",
            dataType: "json",
            data: JSON.stringify(param),
            async: false,
            contentType: "application/json",
            success: function (res) {
                var html = '<option value="">全部</option>';
                res.data.dataList.forEach(function (item) {
                    html += '<option value=' + item.DM + '>' + item.MC + '</option>'
                });
                $('.shjg select').append(html);
                form.render('select');
            }
        });
    }

    /**
     * 加载表格
     * @param _param
     */
    function loadTable(page, size, param) {
        param.page = page
        param.size = size
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsqtjxx/list",
            type: "post",
            dataType: 'json',
            data: JSON.stringify(param),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: '#tabgxjk',
                    data: res.data.data.content || [],
                    limit: size,
                    cols: [[
                        {field: 'ROWNUM_', title: '序号', align: 'center'}
                        , {field: 'GXBMMC', title: '申请部门', align: 'center'}
                        , {field: 'GXJSMC', title: '申请角色', align: 'center'}
                        , {field: 'GXYWMC', title: '申请业务', align: 'center'}
                        , {field: 'SQCS', title: '申请次数', align: 'center'}
                    ]],
                    done: function () {
                        layui.laypage.render({
                            elem: 'tabgxjk_param',
                            limits: [10, 20, 30], //10,20,30
                            count: res.data.data.totalElements || 0,  //显示总条数
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
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsqtjxx/shjgdata",
            type: "post",
            dataType: 'json',
            data: JSON.stringify(echartParam),
            contentType: "application/json",
            success: function (result) {
                var res = result.data.data
                left_xdata = [];
                left_ydata = [];
                resultdata = [];
                left_series = [];
                resultInnerData = [];
                for (var i = 0; i < res.length; i++) {
                    if(left_xdata.indexOf(res[i].GXYWMC) == -1){
                        left_xdata.push(res[i].GXYWMC)
                        resultInnerData.push({
                            value: res[i].GXCS,
                            name: res[i].GXYWMC
                        })
                    } else {
                        var index = resultInnerData.findIndex(function(result){return result.name == res[i].GXYWMC});
                        resultInnerData[index].value += res[i].GXCS
                    }
                }
                for (var i = 0; i < res.length; i++) {
                    var find = left_series.find(function(series){return series.name == res[i].SHZTMC})
                    if(!find){
                        var data = [];
                        var findXIndex = left_xdata.findIndex(function(left){return left === res[i].GXYWMC})
                        data[findXIndex] = res[i].GXCS
                        left_series.push({
                            name: res[i].SHZTMC,
                            data: data,
                            type: 'bar',
                            barWidth: '60%',
                            stack: 'total',
                            showBackground: true,
                            backgroundStyle: {
                                color: 'rgba(220, 220, 220, 0.8)'
                            }
                        })
                    } else {
                        var findXIndex = left_xdata.findIndex(function(left){return left === res[i].GXYWMC})
                        var findIndex = left_series.findIndex(function(series){return series.name == res[i].SHZTMC})
                        left_series[findIndex].data[findXIndex] = res[i].GXCS
                    }
                }
                left_ydata = left_ydata.map(function (item, index, array) {
                    return item - 0;
                });
                leftoption = {
                    title: {
                        text: '各类业务共享申请数量统计',
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

                    series: left_series
                };

                leftChart.setOption(leftoption);

                for (var i = 0; i < res.length; i++) {
                    var oneData = {};
                    oneData.value = res[i].GXCS;
                    oneData.name = res[i].GXYWMC;
                    oneData.shzt = res[i].SHZTMC;
                    resultdata.push(oneData);
                }

                var rightoption = {
                    title: {
                        text: '各类业务共享申请占比统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: function(params,ticket,callback){
                            var data = params.data;
                            var items = [];
                            resultdata.forEach(function(result){
                                if(result.name == data.name){
                                    items.push(result)
                                }
                            })
                            var str = "<div>"+ params.name +"：" + params.value + " ("+ params.percent + "%) " +"</div>";
                            items.forEach(function(item){
                                str += '<div>'+item.shzt+'：'+ item.value +'</div>'
                            })
                            return str
                        }
                    },
                    series: [
                        {
                            name: '访问来源',
                            type: 'pie',
                            selectedMode: 'single',
                            radius: [0, '60%'],
                            label: {
                                fontSize: 12,
                            },
                            data: resultInnerData
                        }
                    ]
                }
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
        param.shzt = $(".shjg select option:selected").val();
        loadTable(1, 10, param)
    });


    //重置按钮
    $("#reset").on('click', function () {
        param.gxkssj = '';
        param.gxjssj = '';
        param.gxbmid = "";
        param.gxjsid = "";
        param.gxywmc = '';
        param.shzt = '';
        echartParam.gxkssj = '';
        echartParam.gxjssj = '';
        echartParam.gxbmid = '';
        echartParam.gxjsid = '';
        layui_gxbmid = '';
        layui_gxjsid = ''
        $("#gxbmmc").val('');
        $("#gxjsmc").val('');
        $(".shjg select option:selected").val('');
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
                        , {field: 'GXBMMC', title: '申请部门', align: 'center'}
                        , {field: 'GXJSMC', title: '申请角色', align: 'center'}
                        , {field: 'GXRMC', title: '申请人', align: 'center'}
                        , {field: 'GXSJ', title: '申请时间', align: 'center'}
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