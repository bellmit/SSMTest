layui.use(['jquery', 'element','laytpl'], function () {
    var $ = layui.jquery
        , element = layui.element
        , laytpl = layui.laytpl;
    $(function () {
        var stageData = [
            {
                title: '用地规划许可阶段',
                checkAll:'true',
                list: [
                    {
                        name: '地形测量',
                        check:'true',
                        info: [
                            '地形测量报告'
                        ]
                    },
                    {
                        name: '土地勘测定界',
                        check:'true',
                        info: [
                            '勘测定界技术报告书','勘测定界图'
                        ]
                    },
                    {
                        name: '土地调查',
                        check:'true',
                        info: [
                            '调查记录表','土地权属报告'
                        ]
                    },
                    {
                        name: '拨地测量',
                        check:'true',
                        info: [
                            '拨地测量成果报告'
                        ]
                    }
                ]
            },
            {
                title: '工程规划许可阶段',
                checkAll:'true',
                list: [
                    {
                        name: '建设图测绘',
                        check:'true',
                        info: [
                            '建设图技术测绘报告'
                        ]
                    },
                    {
                        name: '房产面积预测',
                        check:'true',
                        info: [
                            '房产面积预测技术说明','房产预测成果报告'
                        ]
                    }
                ]
            },
            {
                title: '施工许可阶段',
                checkAll:'checking',
                list: [
                    {
                        name: '放线测量',
                        check:'true',
                        info: [
                            '放线测量技术说明','放线测量成果报告'
                        ]
                    },
                    {
                        name: '验线测量',
                        check:'false',
                        info: [
                            '验线测量技术说明','验线测量成果报告'
                        ]
                    }
                ]
            },
            {
                title: '竣工验收阶段',
                checkAll:'false',
                list: [
                    {
                        name: '规划竣工核实测量',
                        check:'false',
                        info: [
                            '规划竣工测绘概况说明','规划竣工测绘技术报告'
                        ]
                    },
                    {
                        name: '地籍测量',
                        check:'false',
                        info: [
                            '地籍测量概况说明','不动产测量报告'
                        ]
                    },
                    {
                        name: '房产测量',
                        check:'false',
                        info: [
                            '房产测量概况说明','房产测量成果报告'
                        ]
                    }
                ]
            },
            {
                title: '不动产登记阶段',
                checkAll:'false',
                list: [
                    {
                        name: '首次登记',
                        check:'false',
                        info: [
                            '不动产登记申请书',
                            '申请人身份证明',
                            '其他必要材料',
                            '契税完税凭证',
                            '土地权属来源材料',
                            '不动产权籍调查成果'
                        ]
                    }
                ]
            },
        ];
        var getStageItemTpl = stageItemTpl.innerHTML,
            mainContent = document.getElementById('mainContent');
        laytpl(getStageItemTpl).render(stageData, function (html) {
            mainContent.innerHTML = html;
        });
        var checkData = $('.circle').attr('data-check');
        $('.circle').addClass(checkData == 'true'?'after-stage':checkData == 'false'?'before-stage' :'stage-ing');
    })
})