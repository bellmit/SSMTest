layui.use(['element', 'layer', 'jquery', 'table', 'form', 'laypage'], function () {
    var element = layui.element;
    var $ = layui.jquery;
    var table = layui.table;
    var laypage = layui.laypage;
    var form = layui.form;


    var params = {
        xmbh: "",
        xmmc: "",
        gxywid: ""
    };

    var gcbhTool = '';
    //初始化加载
    selectYwmc();
    renderTable(1, 10);
    flushPage();

    //渲染业务名称下拉框
    function selectYwmc(gxywid) {
        let param = {
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

    //加载外层表格
    function renderTable(page, size, gxywid) {
        let xmbh = $("#xmbh").val();
        let xmmc = $("#xmmc").val();

        let params = {
            gcbh: xmbh,
            gcmc: xmmc,
        };
        var loadingLayer = layer.load(0, {
            shade: [0.2, '#fff'],
            content: '数据加载中!',
            success: function (layerContentStyle) {
                layerContentStyle.find('.layui-layer-content').css({
                    'padding-top': '35px',
                    'text-align': 'left',
                    'width': '90px'
                });
            }
        });
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxclcgxx/list?page=" + page + "&size=" + size,
            dataType: "json",
            async: false,
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                layer.close(loadingLayer);
                totalCount = data.totalElements;
                tableShow(page, size, data, gxywid);
            }
        });

    }

    function tableShow(page, size, data) {
        var content = data.content;
        var str = "";
        for (var i = 0; i < content.length; i++) {
            var chgcId = content[i].CHGCID;
            if (i == 0) {
                str += "<div class=\"layui-colla-item firstItme\" id='item'>";
            } else if (i == (content.length - 1)) {
                str += "<div class=\"layui-colla-item lastItme\" id='item'>";
            } else {
                str += "<div class=\"layui-colla-item\">";
            }

            str += "<div>";
            str += "<div class=\"layui-colla-title\">";
            str += "<span style='float: left;' hidden>" + chgcId + "</span>";
            str += "<div class='block'></div>";
            str += "<p style='float: left;width: 30px'>" + (size * (page - 1) + (i + 1)) + "、&nbsp </p>";
            str += "<span style='float: left;width: 300px'>" + content[i].GCBH + "</span>";
            str += "<span style='float: left;width: 400px'>" + content[i].GCMC + "</span>";
            str += "<span style='float: left;width: 300px'>" + content[i].WTDW + "</span>";
            str += "<i class='iconfont icon-zhankai1' style='float: right'></i>";
            str += "</div>";
            str += "<div class=\"layui-colla-content\" id='body'>";
            str += "</div>";
            str += "</div>";
            str += "</div>";
            str += "</div>";
        }
        document.getElementById('xmxx').innerHTML = str;
        layui.element.init();//动态渲染
    }


    //加载自定义分页
    function flushPage() {
        laypage.render({
            elem: 'pageHelper'
            , count: totalCount
            , layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
            , jump: function (obj, first) {
                if (!first) {
                    renderTable(obj.curr, obj.limit);
                    fireCustomEvent();
                }
            }
        })
    }

    //监听折叠
    element.on('collapse(test)', function (data) {
        var gxywid = $(".ywmc select option:selected").val();
        if (data.show) {
            var gcbh = data.title.context.childNodes[3].innerText.replace("工程编号：", "");
            //icon 变更
            data.title.context.childNodes[6].className = 'iconfont icon-zhankai';
            var content = data.content.context.parentNode.childNodes[1];
            var str = "<table class=\"layui-hide\" lay-filter=\"test\"></table>\n" +
                "<script type=\"text/html\" id='toolBar'>\n" +
                "  <div class=\"layui-btn-container\">\n" +
                "<button class=\"layui-btn layui-btn-sm detail-btn\" onclick=\"getFile('" + gcbh + "')\">获取</button>\n" +
                "  </div>\n" +
                "</script>";
            content.innerHTML = str;
            var elem = content.childNodes[0];
            gcbhTool = gcbh;
            loadTable(elem, gxywid);
            form.render();
        } else {
            data.title.context.childNodes[6].className = 'iconfont icon-zhankai1';
        }
    });

    //加载内部表格
    function loadTable(elem, gxywid) {
        let param = {
            gxywid: gxywid
        };
        var loadingLayer = layer.load(0, {
            shade: [0.2, '#fff'],
            content: '数据查询中!',
            success: function (layerContentStyle) {
                layerContentStyle.find('.layui-layer-content').css({
                    'padding-top': '35px',
                    'text-align': 'left',
                    'width': '90px'
                });
            }
        });
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/user/list",
            dataType: "json",
            data: JSON.stringify(param),
            async: false,
            contentType: "application/json",
            success: function (res) {
                layer.close(loadingLayer);
                table.render({
                    toolbar: '#toolBar', //开启头部工具栏，并为其绑定左侧模板
                    defaultToolbar: ['filter', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                        title: '提示'
                        , layEvent: 'LAYTABLE_TIPS'
                        , icon: 'layui-icon-tips'
                    }],
                    elem: elem,
                    data: res,
                    limit: res.length,
                    cols: [[
                        {type: 'numbers', title: '序号', align: 'center'}
                        , {field: 'gxywmc', title: '业务名称', align: 'center'}
                        , {field: 'clsx', title: '包含测绘事项', align: 'center', width: 1000}
                        , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barDemo'}
                    ]]
                });
            }
        });
    }

    // 点击查询
    $('#search').on('click', function () {
        var gxywid = $(".ywmc select option:selected").val();
        renderTable(1, 10, gxywid);
        flushPage();
        fireCustomEvent();
    });

    //重置按钮
    $("#reset").on('click', function () {
        params.xmbh = '';
        params.xmmc = "";
        params.gxywid = '';
        $("#xmmc").val('');
        $("#xmbh").val('');
        $(".ywmc select option:selected").val('');
        form.render();
        renderTable(1, 10);
        flushPage();
        fireCustomEvent();
    })


    //监听表格操作栏

    table.on('tool(test)', function (obj) {
        getFile(gcbhTool, obj.data.gxywid);
    });

    //获取下载url
    getFile = function (GCBH, gxywid) {
        var url = '';
        if (gxywid !== null && gxywid !== undefined) {
            url = getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxchgcclxx/" + GCBH + "/" + gxywid;
        } else {
            url = getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxchgcclxx/" + GCBH;
        }
        var loadingLayer = layer.load(0, {
            shade: [0.2, '#fff'],
            content: '下载获取中。。。',
            success: function (layerContentStyle) {
                layerContentStyle.find('.layui-layer-content').css({
                    'padding-top': '35px',
                    'text-align': 'left',
                    'width': '90px'
                });
            }
        });
        $.ajax({
            type: "GET",
            async: false,
            url: url,
            success: function (data) {
                layer.close(loadingLayer);
                if (data.code == "5001") {
                    layer.msg(data.msg)
                } else if (data.code == null || data.code == undefined) {
                    if (data.url == null || data.url == '') {
                        layer.msg("暂无成果可供下载");
                    } else {
                        window.location.href = data.url;
                    }
                }
            },
            error: function () {
                layer.msg("接口报错");
            }
        });
    }

    fireCustomEvent();

    function fireCustomEvent() {
        var item = document.getElementById("item").firstChild.firstChild;
        var e = document.createEvent("MouseEvents");
        e.initEvent("click", true, true);
        item.dispatchEvent(e);
    }
});