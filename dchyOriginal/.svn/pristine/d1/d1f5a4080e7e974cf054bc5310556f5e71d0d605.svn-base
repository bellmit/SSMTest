
layui.use(['jquery', 'element', 'tree', 'util', 'form', 'laytpl'], function () {
    var $ = layui.jquery
        , element = layui.element
        , form = layui.form
        , tree = layui.tree
        , layer = layui.layer
        , laytpl = layui.laytpl
        , util = layui.util;
    $(function () {
        //切换导航详情
        $('.left-nav').on('click', 'a', function () {
            $('.map-left-pane').css('display', 'block');
            var index = $(this).parent().index();
            $(this).parent().addClass('show-this').siblings().removeClass('show-this');
            $('.widget-pane').eq(index).addClass('show-detail').siblings().removeClass('show-detail');
        })
        //显示/隐藏导航详情
        $('.map-left-pane').on('click', '.layui-icon-shrink-right', function () {
            $('.map-left-pane').hide();
        })

        $('.layer-content').on('click', 'h3', function () {
            $('.layer-list-control').toggle();
        })
        $('.list-check-box').on('click', '.layui-icon', function () {
            if ($(this).hasClass("layui-icon-triangle-d")) {
                $(this).removeClass('layui-icon-triangle-d').addClass('layui-icon-triangle-r').parent().next().toggle();
            } else {
                $(this).removeClass('layui-icon-triangle-r').addClass('layui-icon-triangle-d').parent().next().toggle();
            }


        })
        //树形列表
        var protree = layui.protree;
        //后台传入的 标题列表
        var arr = [{
            id: 1,
            name: "现状数据",
            pid: 0
        }, {
            id: 2,
            name: "规划数据",
            pid: 0
        }, {
            id: 3,
            name: "规划竣工范围",
            pid: 2
        }, {
            id: 4,
            name: "规划竣工基底面积图",
            pid: 2
        },
            , {
            id: 5,
            name: "规划竣工地下室",
            pid: 2
        },
            , {
            id: 6,
            name: "规划竣工绿地面积图",
            pid: 2
        }, {
            id: 7,
            name: "基础地理",
            pid: 1
        }, {
            id: 8,
            name: "行政区划",
            pid: 7
        }, {
            id: 9,
            name: "地籍数据",
            pid: 0
        }, {
            id: 9,
            name: "地籍数据",
            pid: 0
        }, {
            id: 10,
            name: "界址点",
            pid: 9
        }, {
            id: 11,
            name: "宗地",
            pid: 9
        }, {
            id: 12,
            name: "房产数据",
            pid: 0
        }, {
            id: 13,
            name: "房屋基底范围",
            pid: 12
        }, {
            id: 14,
            name: "地下室范围",
            pid: 12
        }
        ];
        protree.init('#treeMenu', {
            arr: arr,
            close: false,
            simIcon: "fa fa-file-o",
            mouIconOpen: "fa fa-folder-open-o",
            mouIconClose: "fa fa-folder-o",
        });
        // var layerList = [
        //     { title: '规划竣工范围', layerProp: '面' },
        //     { title: '规划竣工基地面积图', layerProp: '面' },
        //     { title: '规划竣工分层房屋面', layerProp: '面' },
        //     { title: '规划竣工地下室', layerProp: '面' },
        //     { title: '规划竣工绿地面积图', layerProp: '面' },
        //     { title: '规划竣工内部道路', layerProp: '线' },
        //     { title: '房屋基底范围', layerProp: '面' },
        //     { title: '地下室范围', layerProp: '面' },
        //     { title: '界址点', layerProp: '点' },
        //     { title: '宗地', layerProp: '面' },
        // ]
        // var getlayerTpl = layerTpl.innerHTML,
        //     layerListBOx = document.getElementById('layerList');
        // laytpl(getlayerTpl).render(layerList, function (html) {
        //     layerListBOx.innerHTML = html;
        // });
        // 信息查询结果
        var messageResultData = [
            { num: '651', bsm: '371828202003301111', ysdm: '2001010101' },
            { num: '652', bsm: '371828202003301112', ysdm: '2001010102' },
            { num: '653', bsm: '371828202003301113', ysdm: '2001010103' },
            { num: '654', bsm: '371828202003301114', ysdm: '2001010104' },
        ]
        var getMessageResultTpl = messageResultTpl.innerHTML,
            messageResult = document.getElementById('messageResult');
        laytpl(getMessageResultTpl).render(messageResultData, function (html) {
            messageResult.innerHTML = html;
        });
        //地图工具条切换
        $('.tool-list-box>a').click(function () {
            $(this).toggleClass('show-this-tool').parent().siblings().find('a').removeClass('show-this-tool');
            $(this).parent().siblings().find('.tool-item').removeClass('show-tool');
            $(this).parent().find('.tool-item').toggleClass('show-tool');
        })
        //关闭地图工具条
        $('.tool-list-box .layui-icon-close').click(function () {
            $(this).parent().parent().removeClass('show-tool').siblings().removeClass('show-this-tool');
        })
        //地图工具条显示/隐藏
        $('.tool-control-btn').click(function () {
            $(this).toggleClass('rotate-left');
            if ($(this).next().find('.tool-list-box').hasClass("hideMove")) {
                $(this).next().find('.tool-list-box').removeClass("hideMove").addClass('showMove');
            } else {
                $(this).next().find('.tool-list-box').addClass("hideMove").removeClass('showMove');
            }
        })

    })
});

