layui.use(['table', 'jquery', 'layer', 'form','laydate'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    // 渲染开始时间
    laydate.render({
        elem: '#kssj',
    });

    // 渲染结束时间
    laydate.render({
        elem: '#jssj',
    });


    var params = {
        babh: "",
        gcbh: "",
        kssj: "",
        jssj: "",
        gxbmmc: "",
        gxywid: ""
    };

    var param_page = {
        page: "",
        size: "",
        babh: "",
        gcbh: "",
        gxywid: ""
    };

    $(function () {
        var urlData = GetRequest();
        var loadPage = urlData.loadPage;
        var loadSize = urlData.loadSize;
        if (loadPage !== undefined && loadSize !== undefined && loadPage !== "undefined" && loadSize !== "loadSize") {
            loadTable(loadPage, loadSize, params);
        } else {
            //初始加载 表格
            loadTable(1, 10, params);
        }
    });
    //初始化加载
    selectYwmc();
    //渲染业务名称下拉框
    function selectYwmc(gxywid) {
        var param = {
            gxywid: gxywid
        };
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/user/list",
            dataType: "json",
            data: JSON.stringify(param),
            async: false,
            contentType: "application/json",
            success: function (res) {
                var html = '<option value="">全部</option>';
                res.forEach(function (item) {
                    html += '<option value=' + item.gxywid + '>' + item.gxywmc + '</option>'
                });
                $('.ywmc select').append(html);
                form.render('select');
            }
        });
    }
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
    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }


    function loadTable(page, size, params) {
        params.page = page;
        params.size = size;
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsq/gxywsqyb",
            type: "POST",
            dataType: "json",
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: "#xmxx",
                    data: res.data.dataList || [],
                    limit: size,
                    toolbar: true,
                    defaultToolbar: ['filter'],
                    cols: [[
                        {
                            field: '', width: 70, fixed: "left", title: '序号', align: 'center', templet: function (data) {
                                return (page - 1) * size + data.LAY_INDEX;
                            }
                        }
                        , {field: 'CHXMBABH', title: '项目编号', align: 'center'}
                        , {field: 'CHXMGCBH', title: '项目代码', align: 'center'}
                        , {field: 'GXYWMC', title: '申请业务', align: 'center'}
                        , {field: 'SQSJ', title: '申请时间', align: 'center', templet: function(d){
                            return formatDate(d.SQSJ)
                        }}
                        , {field: 'SHSJ', title: '审核时间', align: 'center',templet: function(d){
                            return formatDate(d.SHSJ)
                        }}
                        , {field: 'SHZT', title: '审核结果', align: 'center',templet: function(d){
                            return d.SHZT == "99" ? "通过" : d.SHZT == "98" ? "不通过" : "待审核"
                        }}
                        , {field: 'SHYJ', title: '审核意见', align: 'center'}
                    ]],
                    done: function () {
                        layui.laypage.render({
                            elem: 'xmxx_params',
                            limits: [10, 20, 30], //10,20,30
                            count: res.data.totalNum || 0,  //显示总条数
                            curr: page,
                            limit: size,
                            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    param_page.page = obj.curr;
                                    param_page.size = obj.limit;
                                    loadTable(param_page.page, param_page.size, params)
                                }
                            }
                        })
                    }
                })
            }
        });
    }


    // 点击查询
    $('#search').on('click', function () {
        params.babh = $("#babh").val();
        params.gcbh = $("#gcbh").val();
        var kssj = $("#kssj").val();
        var jssj = $("#jssj").val();
        if (jssj !== '') {
            if (kssj > jssj) {
                layer.msg("开始时间不能大于结束时间！")
                return false;
            }
        }
        params.kssj = kssj;
        params.jssj = jssj;
        params.gxywid = $(".ywmc select option:selected").val();
        params.sqbmid = $(".gxbmmc select option:selected").val();
        params.shjg = $(".shjg select option:selected").val();
        loadTable(1, 10, params);
    });

    //重置按钮
    $("#reset").on('click', function () {
        params.babh = '';
        params.gcbh ="";
        params.gxywid="";
        params.kssj ="";
        params.jssj ="";
        params.sqbmid = "";
        $("#babh").val('');
        $("#gcbh").val('');
        $("#kssj").val('');
        $("#jssj").val('');
        $(".ywmc select option:selected").val('');
        $(".gxbmmc select option:selected").val('');
        $(".shjg select option:selected").val('');
        form.render();
    })
});
