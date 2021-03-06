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
        bmmc: "",
        gxywid: ""
    };

    var param_page = {
        page: "",
        size: "",
        babh: "",
        gcbh: "",
        kssj: "",
        jssj: "",
        bmmc: "",
        gxywid: ""
    };

    form.on('radio(ispass)', function (data) {
        console.log(data)
    });

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
        selectOrgan();
    });
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

    //初始化加载
    selectYwmc();
    //渲染业务名称下拉框
    function selectYwmc(gxywid) {
        var param = {
            gxywid: gxywid
        };
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/all/list",
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
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsq/gxywsqshdb",
            type: "POST",
            dataType: "json",
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: "#xmxx",
                    toolbar: true,
                    data: res.data.dataList || [],
                    defaultToolbar: ['filter'],
                    limit: size,
                    cols: [[
                        {
                            field: '', width: 70,fixed: "left", title: '序号', align: 'center', templet: function (data) {
                                return (page - 1) * size + data.LAY_INDEX;
                            }
                        }
                        , {field: 'CHXMBABH', title: '项目编号', align: 'center'}
                        , {field: 'CHXMGCBH', title: '项目代码', align: 'center'}
                        , {field: 'GXYWMC', title: '申请业务', align: 'center'}
                        , {field: 'SQBMMC', title: '申请部门', align: 'center'}
                        , {field: 'SQRMC', title: '申请人', align: 'center'}
                        , {field: 'SQSJ', title: '申请时间', align: 'center', templet: function(d){
                            return formatDate(d.SQSJ)
                        }}
                        , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barDemo', width: 200}
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
        params.sqbmid = $(".gxbmmc select option:selected").val();
        params.gxywid = $(".ywmc select option:selected").val();
        loadTable(1, 10, params);
    });

    //重置按钮
    $("#reset").on('click', function () {
        params.babh = '';
        params.gcbh ="";
        params.kssj ="";
        params.jssj ="";
        params.gxywid="";
        params.sqbmid = "";
        $("#babh").val('');
        $("#gcbh").val('');
        $("#kssj").val('');
        $("#jssj").val('');
        $(".ywmc select option:selected").val('');
        $(".gxbmmc select option:selected").val('');
        form.render();
    })


    //监听操作栏
    table.on('tool(xmxx)', function (obj) {
        var d = obj.data;
        if (obj.event === "check") {
            var userid = localStorage.getItem("userId") || ""
            $.ajax({
                url: "/portal/index/deleteOtherAssignment/"+d.taskId+"/"+userid,
                type: "POST",
                dataType: "json",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (res) {
                    reviewData(d)
                }
            })
        }
    })
    function reviewData(d){
        layer.open({
            type: 1,
            title: "审核",
            area: ['1000px', '400px'],
            btnAlign: 'c',
            shade: 0.3,
            content: $('#shModal').html(),//打开的内容
            btn: [],
            success: function (layero,index) {
                form.render();
                $(".layui-btn-cancel").click(function(){
                    layer.close(index)
                    return false;
                })
                form.on("submit(formSh)",function(data){
                    var paramsSh = data.field;
                    paramsSh.gxsqid = d.GXSQID;
                    $.ajax({
                        url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsq/gxywsqsh",
                        type: "POST",
                        dataType: "json",
                        data: JSON.stringify(paramsSh),
                        contentType: "application/json",
                        success: function (result) {
                            if(result.head.code == "0000"){
                                getReturnData("/portal/index/turnTaskByWorkFlowInfo", { 'taskid': d.taskId }, "GET", function (data) {
                                    loadTable(params.page,params.size,params)
                                    layer.close(index)
                                    return false;
                                })
                            } else {
                                layer.close(index)
                                var msg = result.head.msg;
                                layer.msg(msg);
                                return false;
                            }
                        }
                    })
                    return false
                })
            }
        })
    }
});
