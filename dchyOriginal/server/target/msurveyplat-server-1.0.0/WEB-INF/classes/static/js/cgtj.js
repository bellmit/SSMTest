layui.use(['jquery', 'form', 'layer', 'element', 'laydate', 'table', 'laypage'], function () {
        var $ = layui.jquery,
            form = layui.form,
            layer = layui.layer,
            element = layui.element,
            laydate = layui.laydate,
            laypage = layui.laypage,
            table = layui.table;

        //定义全局参数
        var param = {
            pjkssj: "",
            pjkssj: "",
            mlkid: ""
        }
        var param_zl = {
            pjkssj: "",
            pjkssj: "",
            chdw: ""
        }


        var param_page = {
            page: "",
            size: "",
            pjkssj: "",
            pjkssj: "",
            mlkid: ""
        }

        var paramzl_page = {
            page: "",
            size: "",
            pjkssj: "",
            pjkssj: "",
            chdw: ""
        }

        var mydsl_xdata = [];
        var mydsl_ydata = [];
        var mydResultData = [];
        var zlResultData = [];
        var zlsl_xdata = [];
        var zlsl_ydata = [];

        //渲染时间控件
        laydate.render({
            elem: '#mydpjkssj'
        });
        laydate.render({
            elem: '#mydpjjssj'
        });
        laydate.render({
            elem: '#zlpjkssj'
        });
        laydate.render({
            elem: '#zlpjjssj'
        });

        //渲染echarts
        var myd_leftCharts = echarts.init(document.getElementById('myd_left'));
        var myd_rightCharts = echarts.init(document.getElementById('myd_right'));
        var zl_leftCharts = echarts.init(document.getElementById('zl_left'));
        var zl_rightCharts = echarts.init(document.getElementById('zl_right'));


        //初始化加载方法
        $(function () {
            getChdw();
            getCgMyd(param)
            getCgzl(param_zl)
            getJsdwPjjlBypage(1, 10, param)
            getGldwCcjg(1, 10, param_zl)
        })

        $("#export").on('click',function(){
            addModel();
            $("#mydsltj").css("display","block")
            $("#zlsltj").css("display","block")
            $("#zlzbtj").css("display","block")
            $("#mydzbtj").css("display","block")
            var inner = $(".layui-tab-title .layui-this").attr("data-type");
            var innerId = "cgzl-export"
            var innerName = "满意度统计"
            if(inner == "zl"){
                innerId = "zl-export"
                innerName = "质量统计"
            }
            html2canvas(document.getElementById(innerId), {
                onrendered: function(canvas) {
                    removeModal();
                    $("#mydsltj").css("display","inline-block")
                    $("#mydzbtj").css("display","inline-block")
                    $("#zlsltj").css("display","inline-block")
                    $("#zlzbtj").css("display","inline-block")
                    var contentWidth = canvas.width;
                    var contentHeight = canvas.height;
                    //一页pdf显示html页面生成的canvas高度;
                    var pageHeight = contentWidth / 592.28 * 841.89;
                    //未生成pdf的html页面高度
                    var leftHeight = contentHeight;
                    //页面偏移
                    var position = 0;
                    //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
                    var imgWidth = 595.28;
                    var imgHeight = 592.28/contentWidth * contentHeight;

                    var pageData = canvas.toDataURL('image/jpeg', 1.0);

                    var pdf = new jsPDF('', 'pt', 'a4');

                    //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                    //当内容未超过pdf一页显示的范围，无需分页
                    if (leftHeight < pageHeight) {
                        pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth*0.8, imgHeight*0.8);
                    } else {    // 分页
                        while(leftHeight > 0) {
                            pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                            leftHeight -= pageHeight;
                            position -= 841.89;
                            //避免添加空白页
                            if(leftHeight > 0) {
                                pdf.addPage();
                            }
                        }
                    }
                    pdf.save(innerName + '.pdf');
                }
            });
         
        })

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
                        html += '<option value=' + item.MLKID + '>' + item.DWMC + '</option>'
                    });
                    $('.mydchdw select').append(html)
                    $('.zlchdw select').html(html)
                    form.render('select')

                },
                error: function () {
                    layer.msg("接口报错！")
                }
            })
        }

        //获取成果满意度
        function getCgMyd(data) {
            $.ajax({
                url: getContextPath() + "/msurveyplat-server/rest/v1.0/cgtj/cgmyd/list",
                type: 'post',
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (res) {
                    mydsl_xdata = []
                    mydsl_ydata = []
                    mydResultData = []
                    for (var i = 0; i < res.length; i++) {
                        mydsl_xdata.push(res[i].FWPJ)
                        mydsl_ydata.push(res[i].CGSL)
                        var oneData = {}
                        oneData.name = res[i].FWPJ
                        oneData.value = res[i].CGSL
                        mydResultData.push(oneData);
                    }
                    mydleftoption = {
                        color: ["#5470c6"],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        xAxis: {
                            type: 'category',
                            data: mydsl_xdata,
                            axisTick: {
                                alignWithLabel: true
                            },
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            name: "成果数量",
                            data: mydsl_ydata,
                            type: 'bar',
                            barWidth: '60%',
                            showBackground: true,
                            backgroundStyle: {
                                color: 'rgba(220, 220, 220, 0.8)'
                            },
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'inside', //在上方显示
                                        textStyle: { //数值样式
                                            color: '#fff',
                                            fontSize: 16
                                        }
                                    }
                                }
                            }
                        }]
                    }
                    mydrightOption = {
                        color: ["#5470c6","#f85858","#658090","#fcdc88","#96c847","#4cc0e8","#e68787"],
                        tooltip: {
                            trigger: 'item',
                            formatter: '{b} : {c} ({d}%)'
                        },
                        series: [
                            {
                                type: 'pie',
                                radius: '90%',
                                data: mydResultData,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            formatter: '{b} : {c} ({d}%)'
                                        }
                                    }
                                }
                            }
                        ]
                    }
                    myd_leftCharts.setOption(mydleftoption)
                    myd_rightCharts.setOption(mydrightOption)
                },
                error: function () {
                    layer.msg("接口报错")
                }
            })
        }

        //获取成果质量
        function getCgzl(data) {
            $.ajax({
                url: getContextPath() + "/msurveyplat-server/rest/v1.0/cgtj/cgzl/list",
                type: 'post',
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (res) {
                    zlsl_xdata = []
                    zlsl_ydata = []
                    zlResultData = []
                    for (var i = 0; i < res.length; i++) {
                        zlsl_xdata.push(res[i].CGPJ)
                        zlsl_ydata.push(res[i].CGSL)
                        var oneData = {}
                        oneData.name = res[i].CGPJ
                        oneData.value = res[i].CGSL
                        zlResultData.push(oneData);
                    }
                    zlleftoption = {
                        color: ["#5470c6"],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        xAxis: {
                            type: 'category',
                            data: zlsl_xdata,
                            axisTick: {
                                alignWithLabel: true
                            },

                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            name: "成果数量",
                            data: zlsl_ydata,
                            type: 'bar',
                            barWidth: '60%',
                            showBackground: true,
                            backgroundStyle: {
                                color: 'rgba(220, 220, 220, 0.8)'
                            },
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'inside', //在上方显示
                                        textStyle: { //数值样式
                                            color: '#fff',
                                            fontSize: 16
                                        }
                                    }
                                }
                            }
                        }]
                    }
                    zlrightOption = {
                        color: ["#5470c6","#f85858","#658090","#fcdc88","#96c847","#4cc0e8","#e68787"],
                        tooltip: {
                            trigger: 'item',
                            formatter: '{b} :  ({d}%)'
                        },
                        series: [
                            {
                                type: 'pie',
                                radius: '90%',
                                data: zlResultData,
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true, //开启显示
                                            formatter: '{b} : {c} ({d}%)'
                                        }
                                    }
                                }
                            }
                        ]
                    }
                    zl_leftCharts.setOption(zlleftoption)
                    zl_rightCharts.setOption(zlrightOption)
                },
                error: function () {
                    layer.msg("接口报错")
                }
            })
        }

        //获取建设单位评价记录
        function getJsdwPjjlBypage(page, size, params) {
            $.ajax({
                url: getContextPath() + "/msurveyplat-server/rest/v1.0/cgtj/jsdwpjjl/list?page=" + page + "&size=" + size,
                type: "post",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (res) {
                    table.render({
                        elem: '#jsdwpjjl',
                        data: res.content,
                        limit: size,
                        cols:
                            [[
                                {field: 'ROWNUM_', title: '序号', align: 'center', width: 80},
                                // {field: 'SLBH', title: '备案编号', align: 'center'},
                                {field: 'BABH', title: '项目编号', align: 'center'},
                                {field: 'CHGCBH', title: '项目代码', align: 'center'},
                                {field: 'GCMC', title: '工程名称', align: 'center'},
                                {field: 'CHDWMC', title: '测绘单位名称', align: 'center'},
                                {
                                    field: 'FWPJ', title: '评价结果', align: 'center', templet: function (data) {
                                        switch (data.FWPJ) {
                                            case 1:
                                                return "差"
                                                break
                                            case  2:
                                                return "较差"
                                                break
                                            case 3:
                                                return "一般"
                                                break
                                            case 4:
                                                return "满意"
                                                break
                                            case 5:
                                                return "非常满意"
                                                break

                                        }
                                    }
                                },
                                {field: 'PJYJ', title: '评价意见', align: 'center'},
                                {field: 'PJSJ', title: '评价时间', align: 'center'},
                            ]],
                        done: function () {
                            laypage.render({
                                elem: 'jsdwpjjl_page',
                                limits: [10, 20, 30],
                                count: res.totalElements,
                                curr: page,
                                limit: size,
                                layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                                jump: function (obj, first) {
                                    if (!first) {
                                        param_page.page = obj.curr;
                                        param_page.size = obj.limit;
                                        getJsdwPjjlBypage(param_page.page, param_page.size, params)
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

        //获取管理单位抽查结果
        function getGldwCcjg(page, size, params) {
            $.ajax({
                url: getContextPath() + "/msurveyplat-server/rest/v1.0/cgtj/gldwccjg/list?page=" + page + "&size=" + size,
                type: "post",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (res) {
                    table.render({
                        width: 1630,
                        elem: "#gldwccjg",
                        data: res.content,
                        cellMinWidth: 80,
                        limit: size,
                        cols: [[
                            {field: 'ROWNUM_', title: '序号', align: 'center', width: 80},
                            // {field: 'BABH', title: '备案编号', align: 'center'},
                            {field: 'BABH', title: '项目编号', align: 'center'},
                            {field: 'GCBH', title: '项目代码', align: 'center'},
                            {field: 'GCMC', title: '工程名称', align: 'center'},
                            {field: 'CHDWMC', title: '测绘单位名称', align: 'center'},
                            {
                                field: 'CGPJ', title: '评价结果', align: 'center', width: 150, templet: function (obj) {
                                    if (obj.CGPJ == 1) {
                                        return "合格"
                                    } else {
                                        return "不合格"
                                    }
                                }
                            },
                            {field: 'PJYJ', title: '评价意见', align: 'center'},
                            {field: 'PJSJ', title: '评价时间', align: 'center', width: 250}
                        ]],
                        done: function () {
                            laypage.render({
                                elem: 'gldwccjg_page',
                                limits: [10, 20, 30],
                                count: res.totalElements,
                                curr: page,
                                limit: size,
                                layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                                jump: function (obj, first) {
                                    if (!first) {
                                        paramzl_page.page = obj.curr;
                                        paramzl_page.size = obj.limit;
                                        getGldwCcjg(paramzl_page.page, paramzl_page.size, params)
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
        $('#mydsearch').on('click', function () {
            param.pjkssj = $("#mydpjkssj").val()
            param.pjjssj = $("#mydpjjssj").val()
            if (param.pjkssj != '' && param.pjjssj != '') {
                if (param.pjkssj > param.pjjssj) {
                    layer.msg("开始时间不能大于结束时间！");
                    return false;
                }
            }
            param.mlkid = $(".mydchdw select option:selected").val()
            getCgMyd(param)
            getJsdwPjjlBypage(1, 10, param)
        })

        $('#zlsearch').on('click', function () {
            param_zl.pjkssj = $("#zlpjkssj").val()
            param_zl.pjjssj = $("#zlpjjssj").val()
            param_zl.chdw = $(".zlchdw select option:selected").text()
            if (param_zl.chdw == "全部") {
                param_zl.chdw = ""
            }

            if (param_zl.pjkssj != '' && param_zl.pjjssj != '') {
                if (param_zl.pjkssj > param_zl.pjjssj) {
                    layer.msg("开始时间不能大于结束时间！");
                    return false;
                }
            }
            getCgzl(param_zl)
            getGldwCcjg(1, 10, param_zl)
        })

        //重置按钮
        $('#mydreset').on('click', function () {
            param.pjjssj = '';
            param.pjkssj = '';
            param.mlkid = '';
            $('#mydchdw').val("")
            form.render()
            getCgMyd(param)
            getJsdwPjjlBypage(1, 10, param)
        })

        $('#zlreset').on('click', function () {
            param_zl.pjjssj = '';
            param_zl.pjkssj = '';
            param_zl.chdw = '';
            $('#zlchdw').val("")
            form.render()
            getCgzl(param_zl)
            getGldwCcjg(1, 10, param_zl)
        })


    }
)