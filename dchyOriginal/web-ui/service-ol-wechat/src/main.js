import Vue from 'vue'
import App from './App'
import router from './router'
import Errors from './components/error/error';
import Empty from './components/empty/empty';
import 'vant/lib/index.css';
import { yzRoute } from "./service/home"
import { Button, Toast, Dialog, Tabbar, TabbarItem, Cell, CellGroup, Cascader, Search } from 'vant';
import { Form, Field, Checkbox, CheckboxGroup, Step, Steps, Picker, Popup, Uploader, Rate } from 'vant';
import { Tab, Tabs, Icon } from "vant";
Vue.use(Button);
Vue.use(Toast);
Vue.use(Dialog);
Vue.use(Tabbar);
Vue.use(TabbarItem);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(Form);
Vue.use(Field);
Vue.use(Checkbox);
Vue.use(CheckboxGroup);
Vue.use(Step);
Vue.use(Steps);
Vue.use(Picker);
Vue.use(Popup);
Vue.use(Uploader);
Vue.use(Cascader);
Vue.use(Search);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(Icon);
Vue.use(Errors);
Vue.use(Rate);
Vue.component("Empty",Empty)
Vue.config.productionTip = false
//用户信息
Vue.prototype.userInfo = {};
// 存放各个页面的查询信息
Vue.prototype.pageInfo = {};
// router.beforeEach((to,from,next) => {
//     if(to.meta&&to.meta.access){
//         next();
//     } else {
//         yzRoute({url: to.path}).then(res => { 
//             if(res.head.code == "0000"){
//                 next()
//             } else {
//                 next({path: "/no/access"})
//             }
//         })
//     }
// })
new Vue({
    el: '#app',
    router,
    components: { App },
    template: '<App/>'
})