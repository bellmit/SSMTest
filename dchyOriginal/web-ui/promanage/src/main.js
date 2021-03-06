import Vue from 'vue'
import App from './App'
import router from './router'
import Loading from './components/loading/loading';
import Errors from './components/error/error';
import Header from './components/header/header';
import Table from "./components/table/table";
import ViewUI from 'view-design';
import Perm from "./service/perm";
import util from "./service/util";
import 'view-design/dist/styles/iview.css';
import data from "./store/data"
import { getResourceAuthorty } from "./service/manage"
import Vue2OrgTree from 'vue2-org-tree'
import 'vue2-org-tree/dist/style.css';
// html 转PDF
import htmlToPdf from './service/htmlToPdf'
// 使用Vue.use()方法就会调用工具方法中的install方法
Vue.use(htmlToPdf)
// tree组件
Vue.use(Vue2OrgTree)
Vue.use(ViewUI);
Vue.use(Errors);
//注册loading组件
Vue.use(Loading);
//头部公用
Vue.component("Header", Header);
Vue.component("Table", Table);
Vue.config.productionTip = false
//用户信息
Vue.prototype.userInfo = {};
// 存放各个页面的查询信息
Vue.prototype.pageInfo = {};
layui.use(['layer'],function(){
    Vue.prototype.layer = layer;
})
let moduleCode = util.getSearchParams("moduleCode")
if(moduleCode){
    getResourceAuthorty({moduleCode: moduleCode}).then(res => {
        let permList = res;
        Vue.prototype.perm = new Perm(permList);
        new Vue({
            el: '#app',
            router,
            data,
            components: { App },
            template: '<App/>'
        })
    }).catch(err => {
        Vue.prototype.perm = new Perm([]);
        new Vue({
            el: '#app',
            router,
            data,
            components: { App },
            template: '<App/>'
        })
    })
} else {
    Vue.prototype.perm = new Perm([]);
    new Vue({
        el: '#app',
        router,
        data,
        components: { App },
        template: '<App/>'
    })
}


