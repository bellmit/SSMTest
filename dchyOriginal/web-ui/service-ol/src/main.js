import Vue from 'vue'
import App from './App'
import router from './router'
import Loading from './components/loading/loading';
import Errors from './components/error/error';
import ViewUI from 'view-design';
import Header from './components/header/header';
import Table from "./components/table/table";
import data from "./store/data"
import ReviewStep from "./components/step/step";
import 'view-design/dist/styles/iview.css';
import { yzRoute } from "./service/user"
Vue.use(ViewUI);
//注册loading组件
Vue.use(Loading);
Vue.use(Errors);
Vue.config.productionTip = false
//用户信息
Vue.prototype.userInfo = {};
// 存放各个页面的查询信息
Vue.prototype.pageInfo = {};
layui.use(['layer'],function(){
    Vue.prototype.layer = layer;
})
//头部公用
Vue.component("Header", Header);
Vue.component("Table", Table);
Vue.component("ReviewStep", ReviewStep);
router.beforeEach((to,from,next) => {
    yzRoute({url: to.path}).then(res => {
        if(res.head.code == "0000"){
            next()
        } else {
            next({path: "/no/access"})
        }
    })
})
new Vue({
    el: '#app',
    router,
    data,
    components: { App },
    template: '<App/>'
})