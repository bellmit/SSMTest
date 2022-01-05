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

    var layui_gxbmid = "";
    var layui_gxjsid = "";


    // 渲染开始时间
    laydate.render({
        elem: '#kssj'
    });

    // 渲染结束时间
    laydate.render({
        elem: '#jssj'
    });

    loadTableGxrz(1, 10, param);

    // 动态加载部门角色和部门
    selectOrgan();

    // 台账查询
    $('#search').on('click', function () {
        param.gxkssj = $("#kssj").val();
        param.gxjssj = $("#jssj").val();
        param.gxywmc = $("#gxywmc").val();
        param.gxbmid = layui_gxbmid;
        param.gxjsid = layui_gxjsid;
        if (param.gxkssj != '' && param.gxjssj != '') {
            if (param.gxkssj > param.gxjssj) {
                layer.msg("开始时间不能大于结束时间！");
                return false;
            }
        }
        loadTableGxrz(1, 10, param)
    });

    //重置按钮
    $('#reset').on('click', function () {
        param.gxywmc = '';
        param.gxjssj = '';
        param.gxkssj = '';
        param.gxbmid = '';
        param.gxjsid = '';
        layui_gxbmid = '';
        layui_gxjsid = ''
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

    //数据表格加载
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
                        , {field: 'GXYWMC', title: '共享业务', align: 'center'}
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


});




