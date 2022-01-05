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
            path: '/home', // 办事大厅首页
            name: 'home',
            component: () =>
                import ( /* webpackChunkName: "home" */ '@/pages/home/home')
        },
        {
            path: '/login', //登录页
            name: 'login',
            component: () =>
                import ( /* webpackChunkName: "login" */ '@/pages/login/login')
        },
        {
            path: '/find_pwd', //找回密码
            name: 'find_pwd',
            component: () =>
                import ( /* webpackChunkName: "find_pwd" */ '@/pages/register/find_pwd')
        },
        {
            path: '/register', //注册页
            name: 'register',
            component: () =>
                import ( /* webpackChunkName: "register" */ '@/pages/register/register')
        },
        {
            path: '/certificate', //证书
            name: 'certificate',
            component: () =>
                import ( /* webpackChunkName: "certificate" */ '@/pages/register/certificate')
        },
        {
            path: '/authorize', //授权页
            name: 'authorize',
            component: () =>
                import ( /* webpackChunkName: "authorize" */ '@/pages/register/authorize')
        },
        {
            path: '/directory',
            name: 'directory',
            component: () =>
                import ( /* webpackChunkName: "directory" */ '@/pages/directory/directory')
        },
        {
            path: '/guide',
            name: 'guide',
            component: () =>
                import ( /* webpackChunkName: "guide" */ '@/pages/guide/guide')
        },
        {
            path: '/announce/detail',
            name: 'announce',
            component: () =>
                import ( /* webpackChunkName: "announce" */ '@/pages/announce/announce-detail')
        },
        {
            path: '/notice',
            name: 'notice',
            component: () =>
                import ( /* webpackChunkName: "announce" */ '@/pages/notice/notice')
        },
        {
            path: '/template',
            name: 'template',
            component: () =>
                import ( /* webpackChunkName: "template" */ '@/pages/template/template')
        },
        {
            path: '/directory/mlk-view',
            name: 'mlk-view',
            component: () =>
                import ( /* webpackChunkName: "mlk-view" */ '@/pages/directory/mlk-view')
        }
    ]
})