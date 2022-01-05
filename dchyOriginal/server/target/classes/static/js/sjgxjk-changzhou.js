layui.use(['table', 'jquery', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;


    var params = {
        gxywmc: "",
        gxbmid: "",
        gxjsid: ""
    };

    var param_page = {
        page: "",
        size: "",
        gxywmc: "",
        gxbmmc: "",
        gxjsmc: ""
    };

    form.on('select(gxbmmc)', function (data) {
        layui_gxbmid = data.value;
        selectRoles(data.value);
    });

    form.on('select(gxjsmc)', function (data) {
        layui_gxjsid = data.value;
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
            })
        } else {
            var RoleHtml = '<option value="">请选择共享角色</option>';
            $('.gxjsmc select').html(RoleHtml);
            form.render('select');
        }
    }


    function loadTable(page, size, params) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/list?page=" + page + "&size=" + size,
            type: "POST",
            dataType: "json",
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (res) {
                layui.table.render({
                    elem: "#xmxx",
                    data: res.content,
                    limit: size,
                    cols: [[
                        {
                            field: '', width: 70, title: '序号', align: 'center', templet: function (data) {
                                return (page - 1) * size + data.LAY_INDEX;
                            }
                        }
                        , {field: 'GXYWMC', title: '共享业务', align: 'center'}
                        , {field: 'PZSJ', title: '配置时间', align: 'center'}
                        , {field: 'GXBMMC', title: '共享部门', align: 'center'}
                        , {field: 'GXJSMC', title: '共享角色', align: 'center'}
                        , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barDemo', width: 300}
                    ]],
                    done: function () {
                        layui.laypage.render({
                            elem: 'xmxx_params',
                            limits: [10, 20, 30], //10,20,30
                            count: res.totalElements,  //显示总条数
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
        params.gxywmc = $("#gxywmc").val();
        params.gxbmid = $(".gxbmmc select option:selected").val();
        params.gxjsid = $(".gxjsmc select option:selected").val();
        loadTable(1, 10, params);
    });

    //点击新增
    $('#add').on('click', function () {
        window.location = 'add-changzhou.html?event=add'
    });

    //重置按钮
    $("#reset").on('click', function () {
        params.gxywmc = '';
        params.gxbmid ="";
        params.gxjsid="";
        $("#gxbmmc").val('');
        $("#gxjsmc").val('');
        form.render();
    })


    //监听操作栏
    table.on('tool(xmxx)', function (obj) {
        var loadPage = $(".layui-laypage-skip").find("input").val() //当前页码值
        var loadSize = $(".layui-laypage-limits").find("option:selected").val() //分页数目
        var data = obj.data;
        if (obj.event === "unable") {
            $.ajax({
                type: "POST",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/disable/" + data.GXYWXXID,
                data: data.GXYWXXID,
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                async: false,
                success: function (res) {
                    layer.msg('禁用成功！');
                    loadTable(1, 10, params);
                },
                error: function (err) {
                    layer.msg('禁用失败！')
                }
            })
        } else if (obj.event === "able") {
            $.ajax({
                type: "POST",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/disable/" + data.GXYWXXID,
                data: data.GXYWXXID,
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                async: false,
                success: function (res) {
                    layer.msg('启用成功！');
                    loadTable(1, 10, params);
                },
                error: function (err) {
                    layer.msg('启用失败！')
                }
            })
        } else if (obj.event === "detail") {
            var url = "add-changzhou.html?gxywxxid=" + data.GXYWXXID + "&event=detail" + "&loadPage=" + loadPage + "&loadSize=" + loadSize;
            window.location = url;
        } else if (obj.event === "edit") {
            var url = "add-changzhou.html?gxywxxid=" + data.GXYWXXID + "&event=edit" + "&loadPage=" + loadPage + "&loadSize=" + loadSize;
            window.location = url;
        }
    })
});
