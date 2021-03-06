import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            redirect: "/survey/application",
        },
        {
            path: '/no/access',
            name: 'home',
            component: () =>
                import ( /* webpackChunkName: "404" */ '@/pages/404')
        },
        {
            path: '/review/fj',
            meta: {
                menuName: '我的待办'
            },
            name: '',
            component: () =>
                import ( /* webpackChunkName: "reviewfj" */ '@/pages/admin/review/review-fj')
        },
        {
            path: '/review/cg/fj',
            name: '',
            component: () =>
                import ( /* webpackChunkName: "reviewCgFj" */ '@/pages/construction/review/review-fj')
        },
        {
            path: '/admin', //管理员用户登录
            name: 'admin',
            component: () => 
                import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
                },
                {
                    path: 'user',
                    name: '用户管理',
                    component: () => 
                        import ( /* webpackChunkName: "user" */ '@/pages/admin/user/user'),
                },
                {
                    path: 'announcement',
                    meta: {
                        menuName: '通知公告管理'
                    },
                    name: '通知公告管理',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/announcement/announcement-list')
                },
                {
                    path: 'announcement/add',
                    meta: {
                        menuName: '通知公告管理'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/announcement/announcement-add')
                },
                {
                    path: 'review',
                    meta: {
                        menuName: '我的待办'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/review/review-list')
                },
                {
                    path: 'review/review',
                    meta: {
                        menuName: '我的待办'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/review/review')
                },
                {
                    path: 'completed',
                    meta: {
                        menuName: '我的已办'
                    },
                    name: '我的已办',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/completed/completed')
                },
                {
                    path: 'completed/view',
                    meta: {
                        menuName: '我的已办'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "completedView" */ '@/pages/survey/application/application-view')
                },
                {
                    path: 'evaluation',
                    meta: {
                        menuName: '我的事项'
                    },
                    name: '考评管理',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/evaluation/evaluation')
                },
                {
                    path: 'evaluation/list',
                    meta: {
                        menuName: '我的事项'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/evaluation/evaluation-list')
                },
                {
                    path: 'evaluation/add',
                    meta: {
                        menuName: '我的事项'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/evaluation/evaluation-add')
                },
                {
                    path: 'evaluation/cx',
                    meta: {
                        menuName: '我的事项'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/evaluation/evaluation-cx')
                },
                {
                    path: 'template',
                    meta: {
                        menuName: '模板管理'
                    },
                    name: '模板管理',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/template/template')
                },
                {
                    path: 'template/add',
                    meta: {
                        menuName: '模板管理'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "announcement" */ '@/pages/admin/template/template-add')
                },
                {
                    path: 'unmlk',
                    name: '非名录库机构查看',
                    component: () =>
                        import ( /* webpackChunkName: "unmlkList" */ '@/pages/admin/unmlk/unmlk')
                },
                {
                    path: 'unmlk/view',
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "unmlkView" */ '@/pages/admin/unmlk/unmlk-view')
                },
                {
                    path: 'mlk/list',
                    name: '测绘单位查看',
                    component: () =>
                        import ( /* webpackChunkName: "chdwList" */ '@/pages/admin/chdwgl/chdw-list')
                },
                {
                    path: 'mlk/view',
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "chdwList" */ '@/pages/admin/chdwgl/chdw-view')
                },
                {
                    path: 'mlk/review/list',
                    name: '我的待办',
                    component: () =>
                        import ( /* webpackChunkName: "chdwList" */ '@/pages/admin/chdwgl/review-list')
                },
                {
                    path: 'mlk/completed/list',
                    name: '我的已办',
                    component: () =>
                        import ( /* webpackChunkName: "chdwList" */ '@/pages/admin/chdwgl/completed-list')
                },
                {
                    path: "advice/list",
                    meta: {
                        menuName: ""
                    },
                    name: '留言列表',
                    component: () =>
                        import ( /* webpackChunkName: "adviceList" */ '@/pages/admin/advice/advice-list')
                },
                {
                    path: "advice/add",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "addAdvice" */ '@/pages/admin/advice/advice-add')
                },
                {
                    path: "myadvice",
                    meta: {
                        menuName: ""
                    },
                    name: '留言回复',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/admin/advice/myadvice')
                },
                {
                    path: "config/fj",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "configFj" */ '@/pages/admin/config/configuration-fj')
                },
                {
                    path: "data/push",
                    meta: {
                        menuName: ""
                    },
                    name: '数据推送',
                    component: () =>
                        import ( /* webpackChunkName: "configFj" */ '@/pages/admin/push/push-data')
                }
            ]
        },
        {
            path: "/survey", //测绘单位登录
            name: "survey",
            component: () => 
                import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
                },
                {
                    path: "application",
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "application" */ '@/pages/survey/application/application-list')
                },
                {
                    path: "application/view",
                    meta: {
                        menuName: "我的申请"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "application" */ '@/pages/survey/application/application-view')
                },
                {
                    path: "edit/record",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "editRecord" */ '@/pages/survey/application/edit-record')
                },
                {
                    path: "edit/record/detail",
                    meta: {
                        menuName: "我的申请"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "editRecordDetail" */ '@/pages/survey/application/edit-record-detail')
                },
                {
                    path: "mlkapply",
                    meta: {
                        menuName: "名录库入驻"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "mlkapply" */ '@/pages/survey/mlkapply/mlkapply')
                },
                {
                    path: "cyryxx",
                    meta: {
                        menuName: "名录库入驻"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "cyryxx" */ '@/pages/survey/cyryxx/cyryxx')
                },
                {
                    path: "cyryxx/add",
                    meta: {
                        menuName: "名录库入驻"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "cyryxx" */ '@/pages/survey/cyryxx/cyryxx-edit')
                },
                {
                    path: "project",
                    meta: {
                        menuName: "项目查询"
                    },
                    name: '项目查询',
                    component: () =>
                        import ( /* webpackChunkName: "project" */ '@/pages/survey/project/project')
                },
                {
                    path: "project/view",
                    meta: {
                        menuName: "项目查询"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "constructionpublish" */ '@/pages/construction/publish/publish-add')
                },
                {
                    path: "myproject",
                    meta: {
                        menuName: "我的测绘项目"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "myproject" */ '@/pages/survey/myproject/myproject')
                },
                {
                    path: "myproject/view",
                    meta: {
                        menuName: "我的测绘项目"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "myproject" */ '@/pages/survey/myproject/myproject-view')
                },
                {
                    path: "evaluation",
                    meta: {
                        menuName: "评价管理"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "evaluation" */ '@/pages/survey/evaluation/evaluation')
                },
                {
                    path: "evaluation/view",
                    meta: {
                        menuName: "评价管理"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "evaluation" */ '@/pages/survey/evaluation/evaluate-view')
                },
                {
                    path: "message",
                    meta: {
                        menuName: "消息提醒"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "message" */ '@/pages/survey/message/message')
                },
                {
                    path: "commission",
                    name: '项目委托管理',
                    component: () =>
                        import ( /* webpackChunkName: "commissionList" */ '@/pages/survey/commission/commission-list')
                },
                {
                    path: "dataRequest",
                    name: '基础数据申请',
                    component: () =>
                        import ( /* webpackChunkName: "dataRequest" */ '@/pages/survey/dataRequest/dataRequest')
                },
                {
                    path: "apply/view",
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "applyView" */ '@/pages/survey/dataApply/completed-view')
                },
                {
                    path: "submitCg",
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "submitCg" */ '@/pages/survey/myproject/submit-cg')
                },
                {
                    path: "advice/list",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "adviceList" */ '@/pages/construction/advice/advice-list')
                },
                {
                    path: "advice/add",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "addAdvice" */ '@/pages/construction/advice/advice-add')
                },
                {
                    path: "myadvice",
                    meta: {
                        menuName: ""
                    },
                    name: '我的留言',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/construction/advice/myadvice')
                },
                {
                    path: "ht/edit",
                    meta: {
                        menuName: ""
                    },
                    name: '合同变更',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/survey/myproject/edit-ht')
                }
            ]
        },
        {
            path: "/construction", //建设单位登录
            name: "construction",
            component: () => 
                import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
                },
                {
                    path: 'mlk/list',
                    meta: {
                        menuName: '名录库列表'
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "mlkList" */ '@/pages/construction/commission/mlk-list')
                },
                {
                    path: 'mlk/view',
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "mlkView" */ '@/pages/construction/commission/mlk-view')
                },
                {
                    path: 'myproject',
                    meta: {
                        menuName: "我的测绘项目"
                    },
                    name: '我的测绘项目',
                    component: () => 
                        import ( /* webpackChunkName: "constructionmyproject" */ '@/pages/construction/myproject/myproject'),
                },
                {
                    path: 'publish',
                    meta: {
                        menuName: "需求发布管理"
                    },
                    name: '需求发布',
                    component: () => 
                        import ( /* webpackChunkName: "constructionpublish" */ '@/pages/construction/publish/publish'),
                },
                {
                    path: 'publish/add',
                    meta: {
                        menuName: "需求发布管理"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "constructionpublish" */ '@/pages/construction/publish/publish-add'),
                },
                {
                    path: 'commission',
                    meta: {
                        menuName: "委托项目信息"
                    },
                    name: '我的委托',
                    component: () => 
                        import ( /* webpackChunkName: "constructionpublish" */ '@/pages/construction/commission/commission-list'),
                },
                {
                    path: 'commission/add',
                    meta: {
                        menuName: "委托项目信息"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "constructionpublish" */ '@/pages/construction/commission/commission-add'),
                },
                {
                    path: 'evaluate',
                    meta: {
                        menuName: "测绘项目评价"
                    },
                    name: '测绘项目评价',
                    component: () => 
                        import ( /* webpackChunkName: "constructionevaluate" */ '@/pages/construction/evaluate/evaluate'),
                },
                {
                    path: 'evaluate/add',
                    meta: {
                        menuName: "测绘项目评价"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "constructionevaluate" */ '@/pages/construction/evaluate/evaluate-add'),
                },
                {
                    path: "message",
                    meta: {
                        menuName: "消息提醒"
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "message" */ '@/pages/survey/message/message')
                },
                {
                    path: "advice/list",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "adviceList" */ '@/pages/construction/advice/advice-list')
                },
                {
                    path: "advice/add",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "addAdvice" */ '@/pages/construction/advice/advice-add')
                },
                {
                    path: "myadvice",
                    meta: {
                        menuName: ""
                    },
                    name: '我的留言',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/construction/advice/myadvice')
                },
                {
                    path: "auth",
                    meta: {
                        menuName: ""
                    },
                    name: '授权管理',
                    component: () =>
                        import ( /* webpackChunkName: "authManage" */ '@/pages/construction/auth-manage/auth-manage')
                },
                {
                    path: "ht/edit",
                    meta: {
                        menuName: ""
                    },
                    name: '合同变更',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/construction/myproject/edit-ht')
                },
                {
                    path: "ht/edit/record",
                    meta: {
                        menuName: ""
                    },
                    name: '',
                    component: () =>
                        import ( /* webpackChunkName: "myadvice" */ '@/pages/construction/myproject/edit-ht-record')
                }
            ]
        }
    ]
})