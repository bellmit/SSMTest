layui.use(['jquery', 'element', 'laytpl', 'table', 'layer'], function () {
    var $ = layui.jquery
        , element = layui.element
        , layer = layui.layer
        , laytpl = layui.laytpl
        , table = layui.table;
    $(function () {
        var slbh = getIpHz().slbh;
        var xmid = getIpHz().xmid;
        var xmbh = getIpHz().xmbh;
        //动态获取url
        var _url = '';
        $.ajax({
            url: getContextPath() + "/oms/rest/v1.0/dchy/geturl",
            type: 'post',
            async: false,
            success: function (data) {
                _url = data;
            }
        });
        //获取项目生命线
        var xmDOList = [];
        $.ajax({
            url: _url + "/msurveyplat-server/rest/v1.0/onemap/xmxq/" + xmbh,
            //url: "http://192.168.50.60:8083/msurveyplat-server/rest/v1.0/onemap/xmxq/" + xmbh,
            type: 'post',
            async: false,
            success: function (data) {
                var list = data[3].dchyCgglXmDOList;
                if (list) {
                    xmDOList = list;
                }
                var getStageItemTpl = stageItemTpl.innerHTML,
                    mainContent = document.getElementById('mainContent');
                laytpl(getStageItemTpl).render(data, function (html) {
                    mainContent.innerHTML = html;
                });
                var itemWidth = 100 / (data.length) + '%';
                $('.main-content .stage-item').css('width', itemWidth);
            }
        });
        var h = $('.main-content').height();
        $('.mapping-content').css('top', 60 + 30 + h + 10 + 'px');
        $(window).resize(function () {
            h = $('.main-content').height();
            $('.mapping-content').css('top', 60 + 30 + h + 10 + 'px');
        });
        //测绘内容表
        table.render({
            elem: '#mappingTable',
            width: 1200,
            defaultToolbar: [],
            page: true,
            // loading: true,
            cols: [[ //表头
                { field: 'slbh', title: '受理编号', align: 'center' },
                { field: 'dchybh', title: '多测合一编号', align: 'center' },
                { field: 'slsj', title: '受理时间', align: 'center', templet: "<div>{{layui.util.toDateString(d.slsj, 'yyyy-MM-dd HH:mm:ss')}}</div>" },
                { field: 'rksj', title: '入库时间', align: 'center', templet: "<div>{{layui.util.toDateString(d.rksj, 'yyyy-MM-dd HH:mm:ss')}}</div>" },
                { field: 'xmzt', title: '项目状态', align: 'center', templet: '#xmztTpl' },
                { field: 'cz', title: '操作', align: 'center', templet: '#lookTpl' }
            ]],
            data: xmDOList
        });
        //联动高亮内容表
        $('.main-content .jgys-content').on('click', '.after-stage', function () {
            $(this).parent().parent().addClass('active').siblings().removeClass('active');
            var idx = $(this).parent().parent().index();
            $('.mapping-content .layui-table tbody').children("tr").eq(idx).addClass('active').siblings().removeClass('active');
        });
        //查看
        table.on('tool(LAY-mapping-table)', function (obj) {
            var data = obj.data; //获得当前行数据
            if (obj.event === 'lookBtn') {
                $.ajax({
                    url: _url + '/msurveyplat-server/rest/v1.0/onemap/xmckdz/' + data.xmid,
                    type: 'post',
                    success: function (data) {
                        window.open(data);
                    }
                });
            }
        })
    })
})