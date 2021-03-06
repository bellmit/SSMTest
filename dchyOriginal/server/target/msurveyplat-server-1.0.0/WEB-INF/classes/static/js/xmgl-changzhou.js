layui.use(['element', 'layer', 'jquery', 'table', 'form', 'laypage',"laydate"], function () {
    var element = layui.element;
    var $ = layui.jquery;
    var table = layui.table;
    var laypage = layui.laypage;
    var form = layui.form;
    var laydate = layui.laydate;
    var totalCount = 0;
    var gcdzDictData = [];

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
        gcmc: "",
        chdwmc: "",
        kssj: "",
        jssj: "",
        xmdz: "",
        chdwmc: ""
    };

    form.on("select(gcdzs)",function(obj){
        $('#gcdzss').html("")
        $('#gcdzqx').html("")
        params.gcdzss = "";
        params.gcdzqx = "";
        var value = obj.value;
        var html = '<option value="">全部</option>';
        var sqData = []
        gcdzDictData.forEach(function(gcdz){
            if(gcdz.FDM == value){
                sqData.push(gcdz)
            }
        })
        sqData.forEach(function (item) {
            html += '<option value=' + item.DM + '>' + item.MC + '</option>'
        });
        $('#gcdzss').append(html);
        form.render('select');
    })

    form.on("select(gcdzss)",function(obj){
        $('#gcdzqx').html("")
        params.gcdzqx = "";
        var value = obj.value;
        var html = '<option value="">全部</option>';
        var sqData = []
        gcdzDictData.forEach(function(gcdz){
            if(gcdz.FDM == value){
                sqData.push(gcdz)
            }
        })
        sqData.forEach(function (item) {
            html += '<option value=' + item.DM + '>' + item.MC + '</option>'
        });
        $('#gcdzqx').append(html);
        form.render('select');
    })
    
    var gcbhTool = '';
    renderTable(1, 10);
    flushPage();
    //初始化加载
    selectGcdzqx();
    //渲染业务名称下拉框
    function selectGcdzqx() {
        var param = {
            data: {
                zdlx: ["GCDZ"]
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
                var sData = [];
                gcdzDictData = res.data.dataList || [];
                gcdzDictData.forEach(function(gcdz){
                    if(!gcdz.FDM){
                        sData.push(gcdz)
                    }
                })
                sData.forEach(function (item) {
                    html += '<option value=' + item.DM + '>' + item.MC + '</option>'
                });
                $('#gcdzs').append(html);
                form.render('select');
            }
        });
    }

    //加载外层表格
    function renderTable(page, size) {
        var babh = $('#babh').val();
        var gcbh = $("#gcbh").val();
        var gcmc = $("#gcmc").val();
        var chdwmc = $("#chdwmc").val();
        var kssj = $("#kssj").val();
        var jssj = $("#jssj").val();
        var gcdzxx = $("#gcdzxx").val();
        var gcdzqx = $("#gcdzqx option:selected").val();
        var gcdzs = $("#gcdzs option:selected").val();
        var gcdzss = $("#gcdzss option:selected").val();
        var jsdwmc = $("#jsdwmc").val();
        if (jssj !== '') {
            if (kssj > jssj) {
                layer.msg("开始时间不能大于结束时间！")
                return false;
            }
        }
        var params = {
            page: page,
            size: size,
            babh: babh,
            gcbh: gcbh,
            gcmc: gcmc,
            chdwmc: chdwmc,
            kssj: kssj,
            jssj: jssj,
            gcdzxx: gcdzxx,
            gcdzqx: gcdzqx,
            gcdzss: gcdzss,
            gcdzs: gcdzs,
            jsdwmc: jsdwmc,
        }
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
            url: getContextPath(1) + "/msurveyplat-promanage/management/querychgcfortree",
            dataType: "json",
            async: false,
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                layer.close(loadingLayer);
                totalCount = data.data.totalNum || 0;
                tableShow(page, size, data);
            }
        });

    }

    function tableShow(page, size, data) {
        var content = data.data.dataList || [];
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
            str += "<span style='float: left;width: 300px' data-index="+content[i].CHGCID+">项目代码：" + content[i].GCBH + "</span>";
            str += "<span style='float: left;width: 400px'>工程名称：" + content[i].GCMC + "</span>";
            str += "<span style='float: left;width: 300px'>建设单位：" + content[i].WTDW + "</span>";
            str += "<span style='float: left;'>项目地址：" + (content[i].GCDZSMC || "") + (content[i].GCDZSSMC || "") + (content[i].GCDZQXMC || "") + (content[i].GCDZXX || "") + "</span>";
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
        if (data.show) {
            var chgcid = data.title.context.childNodes[3].getAttribute("data-index");
            //icon 变更
            data.title.context.childNodes[7].className = 'iconfont icon-zhankai';
            var content = data.content.context.parentNode.childNodes[1];
            var str = "<table class=\"layui-hide\" lay-filter=\"test\"></table>\n" +
                "<script type=\"text/html\" id='toolBar"+chgcid+"'>\n" +
                "  <div class=\"layui-btn-container\">\n" +
                "<button class=\"layui-btn layui-btn-sm detail-btn\" onclick=\"viewCg('" + chgcid + "')\">成果浏览</button>\n" +
                "  </div>\n" +
                "</script>";
            content.innerHTML = str;
            var elem = content.childNodes[0];
            gcbhTool = chgcid;
            loadTable(elem);
            form.render();
        } else {
            data.title.context.childNodes[7].className = 'iconfont icon-zhankai1';
        }
    });

    //加载内部表格
    function loadTable(elem) {
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
        layer.close(loadingLayer);
        $.ajax({
            type: "GET",
            url: getContextPath(1) + "/msurveyplat-promanage/management/querychxmbygcid/" + gcbhTool,
            dataType: "json",
            async: false,
            success: function (res) {
                layer.close(loadingLayer);
                var id = 'toolBar' + gcbhTool
                table.render({
                    toolbar: '#'+id, //开启头部工具栏，并为其绑定左侧模板
                    defaultToolbar: ['filter', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                        title: '提示'
                        , layEvent: 'LAYTABLE_TIPS'
                        , icon: 'layui-icon-tips'
                    }],
                    elem: elem,
                    data: res.data.dataList || [],
                    limit: res.data.dataList ? res.data.dataList.length : 0,
                    cols: [[
                        {type: 'numbers', title: '序号', align: 'center',width: 70,fixed: "left"}
                        , {field: 'BABH', title: '项目编号', align: 'center'}
                        , {field: 'SLSJ', title: '备案时间', align: 'center'}
                        , {field: 'CHDWMC', title: '测绘单位', align: 'center' }
                        , {field: 'CLSXMC', title: '测量事项', align: 'center'}
                        , {field: 'CGTJZTMC', title: '成果状态', align: 'center', width: 120}
                        ,{
                            field: "XMZTMC",
                            title: "办结状态",
                            align: "center",
                            width: 120,
                            templet: function(d){
                                let className= d.XMZTMC==="已办结" ? "color-finish": d.XMZTMC ==="已受理" ? "color-processing": "color-unfinish"
                                return "<span class='"+className+"'>"+d.XMZTMC+"</span>"
                            }
                        }
                    ]]
                });
            }
        });
    }

    // 点击查询
    $('#search').on('click', function () {
        renderTable(1, 10);
        flushPage();
        fireCustomEvent();
    });

    //重置按钮
    $("#reset").on('click', function () {
        params.xmbh = '';
        params.gcbh = "";
        params.gcmc = "";
        params.chdwmc = "";
        params.kssj = '';
        params.jssj = '';
        params.gcdzxx = '';
        params.gcdzqx = '';
        params.gcdzs = '';
        params.gcdzss = '';
        params.jsdwmc = '';
        $("#xmmc").val('');
        $("#gcbh").val('');
        $("#gcmc").val('');
        $("#chdwmc").val('');
        $("#kssj").val('');
        $("#jssj").val('');
        $("#gcdzxx").val('');
        $("#jsdwmc").val('');
        $("#gcdzqx option:selected").val('');
        $("#gcdzs option:selected").val('');
        $("#gcdzss option:selected").val('');
        form.render();
    })

    //获取下载url
    viewCg = function (chgcid) {
        window.open("/msurveyplat-promanage/#/review/cg/tree?chgcid=" + chgcid);
    }

    fireCustomEvent();

    function fireCustomEvent() {
        var item = document.getElementById("item") ? document.getElementById("item").firstChild.firstChild : "";
        if(!item){
            return
        }
        var e = document.createEvent("MouseEvents");
        e.initEvent("click", true, true);
        item.dispatchEvent(e);
    }
});