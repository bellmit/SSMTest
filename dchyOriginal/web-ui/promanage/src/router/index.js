import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/home/home')
        },
        {
            path: '/home',
            name: 'Home',
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/home/home')
        },
        {
            path: '/review/fj',
            component: () =>
                import ( /* webpackChunkName: "reviewFj" */ '@/pages/review/review-fj')
        },
        {
            path: '/review/cl/fj',
            component: () =>
                import ( /* webpackChunkName: "reviewClFj" */ '@/pages/review/review-cl-fj')
        },
        {
            path: '/review/cg/fj',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "htbadj" */ '@/pages/admin/review/review-cg-fj'),
        },
        {
            path: '/review/cg/tree',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "htbadj" */ '@/pages/review/review-cg-tree'),
        },
        {
            path: '/pdf/view',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "chsldj" */ '@/pages/review/pdf-view'),
        },
        {
            path: '/manage/cgtj',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "chsldj" */ '@/pages/cgtj/submit-cg'),
        },
        {
            path: '/manage',
            name: 'manage',
            component: () => 
                        import ( /* webpackChunkName: "manage" */ '@/pages/manage/manage'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "Default" */ '@/pages/manage/manage'),
                },
                {
                    path: 'htbadj',
                    meta: {
                        menuName: "????????????"
                    },
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/htbadj-list'),
                },
                {
                    path: 'htbadj/stop',
                    meta: {
                        menuName: "??????????????????"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/stopAndRestore'),
                },
                {
                    path: 'htbadj/add',
                    meta: {
                        menuName: "??????????????????"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/htbadj-add'),
                },
                {
                    path: 'htbadj/rzxq',
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/rzxq.vue'),
                },
                {
                    path: '/edit/record',
                    meta: {
                        menuName: "??????????????????"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/edit-record'),
                },
                {
                    path: '/edit/record/detail',
                    meta: {
                        menuName: "??????????????????"
                    },
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/manage/htbadj/edit-record-detail'),
                },
                {
                    path: 'chsldj',
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "chsldj" */ '@/pages/cgtj/chsldj-list'),
                },
                {
                    path: 'chsldj/view',
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "chsldj" */ '@/pages/cgtj/chsldj-view'),
                },
                {
                    path: 'review/list',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "reviewList" */ '@/pages/project-ba/ybadj-list'),
                },
                {
                    path: 'evaluate/list',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "evaluateList" */ '@/pages/evaluate/cg-check-list'),
                },
                {
                    path: 'evaluate/add',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "evaluateAdd" */ '@/pages/evaluate/cg-check'),
                },
                {
                    path: 'evaluate/view',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "evaluateView" */ '@/pages/evaluate/cg-evaluate-view'),
                },
                {
                    path: 'xm/statistics',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "xmStatistics" */ '@/pages/statistics/xm-statistics'),
                },
                {
                    path: 'xm/business',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "xmBusiness" */ '@/pages/statistics/xm-business'),
                },
                {
                    path: 'xm/classify',
                    name: "????????????",
                    component: () => 
                        import ( /* webpackChunkName: "xmClassify" */ '@/pages/statistics/xm-classify'),
                },
                {
                    path: 'check/rule',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "checkRule" */ '@/pages/config/check-rule'),
                },
                {
                    path: 'config/fj',
                    name: "",
                    component: () => 
                        import ( /* webpackChunkName: "configurationFj" */ '@/pages/config/configuration-fj'),
                },
                {
                    path: 'record/list',
                    name: "????????????",
                    component: () => 
                        import ( /* webpackChunkName: "recordList" */ '@/pages/record/record-list'),
                },
                {
                    path: 'jsdw/list',
                    name: "??????????????????",
                    component: () => 
                        import ( /* webpackChunkName: "recordList" */ '@/pages/jsdw/jsdw-list'),
                },
                {
                    path: 'data/push',
                    name: "????????????",
                    component: () => 
                        import ( /* webpackChunkName: "recordList" */ '@/pages/project-ba/push-data'),
                }
            ]
        },
        {
            path: '/apply/review/check',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "htbadj" */ '@/pages/dataApply/review/review-check'),
        },
        {
            path: '/apply/completed/view',
            name: '',
            component: () => 
                import ( /* webpackChunkName: "completedView" */ '@/pages/dataApply/completed/completed-view'),
        },
        {
            path: '/apply',
            name: 'apply',
            component: () => 
                        import ( /* webpackChunkName: "manage" */ '@/pages/manage/manage'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "Default" */ '@/pages/manage/manage'),
                },
                {
                    path: 'review/list',
                    meta: {
                        menuName: "????????????"
                    },
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/dataApply/review/review-list'),
                },
                {
                    path: 'completed/list',
                    meta: {
                        menuName: "????????????"
                    },
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/dataApply/completed/completed-list'),
                }
            ]
        },
        {
            path: '/admin',
            name: 'admin',
            component: () => 
                        import ( /* webpackChunkName: "admin" */ '@/pages/admin/admin'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "Default" */ '@/pages/admin/admin'),
                },
                {
                    path: 'review/baseinfo',
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "htbadj" */ '@/pages/admin/review/review-base-info'),
                },
                {
                    path: 'review/check',
                    meta: {},
                    component: () => 
                        import ( /* webpackChunkName: "check" */ '@/pages/admin/review/review-info'),
                }
            ]
        },
        {
            path: '/changzhou',
            name: 'changzhou',
            component: () => 
                import ( /* webpackChunkName: "manage" */ '@/pages/manage/manage'),
            children: [
                {
                    path: '',
                    name: 'Default',
                    component: () => 
                        import ( /* webpackChunkName: "manage" */ '@/pages/manage/manage'),
                },
                {
                    path: 'htbadj',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "changzhouBadj" */ '@/pages/project-ba/dbadj-list'),
                },
                {
                    path: 'htbadj/add',
                    name: '',
                    component: () => 
                        import ( /* webpackChunkName: "changzhouBadj" */ '@/pages/project-ba/htbadj-add'),
                },
                {
                    path: 'cgtj/list',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "changzhouBadj" */ '@/pages/cgtj/submit-cg-list'),
                },
                {
                    path: 'cgtj/review',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "changzhouBadj" */ '@/pages/cgtj/cgtj-review-list'),
                },
                {
                    path: 'cgtj/completed',
                    name: '????????????',
                    component: () => 
                        import ( /* webpackChunkName: "changzhouBadj" */ '@/pages/cgtj/cgtj-completed-list'),
                }
            ]
        }
    ]
})