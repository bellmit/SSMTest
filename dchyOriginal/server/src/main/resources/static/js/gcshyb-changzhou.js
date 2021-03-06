layui.use(['table', 'jquery', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;


    var params = {
        babh: "",
        gcbh: "",
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
        selectOrgan();
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

    //渲染部门和角色下拉
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


    function loadTable(page, size, params) {
        params.page = page;
        params.size = size;
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gxsq/gxywsqshyb",
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
                        , {field: 'SQBMMC', title: '申请部门', align: 'center'}
                        , {field: 'SHSJ', title: '审核时间', align: 'center',templet: function(d){
                            return formatDate(d.SHSJ)
                        }}
                        , {field: 'SHZT', title: '审核结果', align: 'center',templet: function(d){
                            return d.SHZT == "99" ? "通过" : d.SHZT == "98" ? "不通过" : d.SHZT == "1" ? "审核中" : "/"
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
        params.sqbmid = "";
        params.shjg = "";
        $("#babh").val('');
        $("#gcbh").val('');
        $(".ywmc select option:selected").val("");
        $(".gxbmmc select option:selected").val("");
        $(".shjg select option:selected").val("");
        form.render();
    })
});
