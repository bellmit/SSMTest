layui.use(['element', 'layer', 'jquery', 'table', 'form', 'laypage'], function () {
    var element = layui.element;
    var $ = layui.jquery;
    var table = layui.table;
    var laypage = layui.laypage;
    var form = layui.form;

    var totalElements;
    var host;

    // 点击查询
    $('#search').on('click', function () {
        renderTable(page, size);
        flushPage();
        fireCustomEvent();
    });


    //重置按钮
    $("#reset").on('click', function () {
        $("#xmbh").val("");
        $("#xmmc").val("");
        $("#jsdw").val("");
        renderTable(page, size);
        flushPage();
        fireCustomEvent();
    })

    // 点击查看
    queryDetail = function (chgcId) {
        var url = host + "/platform/fc.action?readOnly=true&proid=" + chgcId;
        window.open(url);
    }

    let page = 1;
    let size = 10;

    renderTable(page, size);
    flushPage();


    function tableShow(page, size, data) {
        var content = data.content;
        var str = "";
        for (var i = 0; i < content.length; i++) {
            var CHGCID = content[i].CHGCID;
            if (i == 0) {
                str += "<div class=\"layui-colla-item firstItme\" id='item'>";
            } else if (i == (content.length - 1)) {
                str += "<div class=\"layui-colla-item lastItme\" id='item'>";
            } else {
                str += "<div class=\"layui-colla-item\">";
            }

            str += "<div>";
            str += "<div class=\"layui-colla-title\">";
            str += "<span style='float: left;' hidden>" + CHGCID + "</span>";
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
        if (content.length == 1) {
            document.getElementById("item").className = 'layui-colla-item firstItme lastItme';
        }
        element.init();//动态渲染
    }

    //列表数据
    function renderTable(page, size) {
        let xmbh = $("#xmbh").val();
        let xmmc = $("#xmmc").val();
        let jsdw = $("#jsdw").val();
        let params = {
            gcbh: xmbh,
            gcmc: xmmc,
            wtdw: jsdw
        };
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxchgcxx/list?page=" + page + "&size=" + size,
            dataType: "json",
            async: false,
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                totalElements = data.gxchgcxx.totalElements;
                host = data.attachmentHost;
                tableShow(page, size, data.gxchgcxx);
            }
        });
    }

    //刷新页码
    function flushPage() {
        laypage.render({
            elem: 'pageHelper'
            , count: totalElements
            , layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
            , jump: function (obj, first) {
                if (!first) {
                    renderTable(obj.curr, obj.limit);
                    fireCustomEvent();
                }
            }
        });
    }


    //监听折叠
    element.on('collapse(test)', function (data) {
        if (data.show) {
            var CHGCID = data.title.context.firstChild.innerHTML;
            var proID = data.title.context.childNodes[3].innerHTML.replace(/项目编号：/, '');
            //icon 变更
            data.title.context.childNodes[6].className = 'iconfont icon-zhankai';
            var content = data.content.context.parentNode.childNodes[1];
            var str = "<table class=\"layui-hide\" lay-filter=\"test\"></table>\n";
            var tool = "<div class=\"layui-btn-container\">\n" +
                "<button class=\"layui-btn layui-btn-sm detail-btn\" onclick=\"queryDetail('" + proID + "')\">查看</button>\n" +
                "</div>";
            content.innerHTML = str;
            var elme = content.childNodes[0];
            loadTable(elme, tool, CHGCID);
            form.render();
        } else {
            data.title.context.childNodes[6].className = 'iconfont icon-zhankai1';
        }
    });


    //数据表格加载
    function loadTable(elem, tool, chgcid) {
        $.ajax({
            type: "GET",
            async: false,
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxchgcxx/" + chgcid,
            success: function (res) {
                table.render({
                    toolbar: tool, //开启头部工具栏，并为其绑定左侧模板
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
                        , {field: 'clsxmc', title: '测绘事项', align: 'center'}
                        , {field: 'zxrksj', title: '最新入库时间', align: 'center'}
                        , {field: 'wtdw', title: '测绘单位', align: 'center', width: 800}
                        , {field: 'zxshrybm', title: '最新审核人员（部门）', align: 'center'}
                        , {field: 'zxshsj', title: '最新审核时间', align: 'center'}
                    ]]
                });
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


