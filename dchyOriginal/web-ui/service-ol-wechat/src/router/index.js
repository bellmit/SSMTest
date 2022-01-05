import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            redirect: "/home",
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/home/home')
        },
        {
            path: '/',
            name: 'index',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/home/index'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "admin" */ '@/pages/home/index'),
                },
                {
                    path: 'home',
                    name: 'home',
                    meta: {
                        access: true
                    },
                    component: () =>
                        import ( /* webpackChunkName: "home" */ '@/pages/home/home')
                },
                {
                    path: 'mine',
                    name: 'mine',
                    meta: {
                        access: true
                    },
                    component: () =>
                        import ( /* webpackChunkName: "home" */ '@/pages/mine/mine')
                }
            ]
        },
        {
            path: '/no/access',
            name: 'home',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "404" */ '@/pages/404')
        },
        {
            path: '/login',
            name: 'login',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "login" */ '@/pages/login/login')
        },
        {
            path: '/mine/user',
            name: 'mine',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "mineUser" */ '@/pages/mine/mine-user')
        },
        {
            path: '/mine/project',
            name: 'mine',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "mineProject" */ '@/pages/project/myproject-list')
        },
        {
            path: '/mine/project-blsx',
            name: 'mine',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "mineProjectBlsx" */ '@/pages/project/myproject-blsx')
        },
        {
            path: '/process/list',
            name: 'process',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "processList" */ '@/pages/process/process-list')
        },
        {
            path: '/process/info',
            name: 'process',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "processInfo" */ '@/pages/process/process-info')
        },
        {
            path: '/finish/evaluate/list',
            name: 'finish',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "finishAndEvaluate" */ '@/pages/finish/finish-evaluate')
        },
        {
            path: '/evaluate/add',
            name: 'evaluate',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "evaluate" */ '@/pages/finish/evaluate')
        },
        {
            path: '/message/list',
            name: 'message',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "messageList" */ '@/pages/message/message-list')
        },
        {
            path: '/message/detail',
            name: 'message',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "messageDetail" */ '@/pages/message/message-detail')
        },
        {
            path: '/mlk/list', // 名录库页面
            name: 'mlk',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "mlkList" */ '@/pages/mlk/mlk-list')
        },
        {
            path: '/mlk/mlk-view', // 名录库详情页面
            name: 'mlkView',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "mlkView" */ '@/pages/mlk/mlk-view')
        },
        {
            path: '/announce/more', // 更多通知公告页面
            name: 'announce',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "announceMore" */ '@/pages/announce/announce-list')
        },
        {
            path: '/announce/detail', // 通知公告详情页面
            name: 'announce',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "announceDetail" */ '@/pages/announce/announce-detail')
        },
        {
            path: '/guide/list', // 办事指南
            name: 'guide',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "guideList" */ '@/pages/guide/guide-list')
        },
        {
            path: '/commission/online', // 在线委托
            name: 'commission',
            meta: {
                access: false
            },
            component: () =>
                import ( /* webpackChunkName: "commissionOnline" */ '@/pages/commission/commission-online')
        },
        {
            path: '/authorize', // 授权页面
            name: 'authorize',
            meta: {
                access: true
            },
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/authorize/authorize')
        }
    ]
})