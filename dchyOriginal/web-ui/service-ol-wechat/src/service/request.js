import axios from 'axios';
import util from './util';
import Vue from "vue"

let httpService = {
    get(url, params) {
        return new Promise((resolve, reject) => {
            axios.get(url, {
                params: params
            }).then(res => {
                Vue.prototype.$toast.clear();
                if(res.data){
                    resolve(res.data);
                }else {
                    resolve();
                }
            }).catch(err => {
                
                reject(err.data)
            })
        })
    },
    getDataWithCode(url, params){
        return new Promise((resolve, reject) => {
            axios.get(url, {
                params: params
            }).then(res => {
                Vue.prototype.$toast.clear();
                if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                    resolve(res.data);
                }else {
                    if(res.data&&res.data.head){
                        let msg = res.data.head.msg;
                        Vue.prototype.$toast(msg)
                    }
                    reject();
                }
            }).catch(err => {
                
                Vue.prototype.$toast('请求失败');
                reject(err.data)
            })
        })
    },
    postDataDirect(url, params, json){
        let headers = {},
            data;
        let access_token = sessionStorage.getItem("access_token") || localStorage.getItem("access_token") || '';
        data = {
            "head": {},
            "data": params
        }
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.post(url, data, { headers: headers })
                .then(res => {
                    Vue.prototype.$toast.clear();
                    if(res.data.code=="0000"){
                        resolve(res.data);
                    }else {
                        Vue.prototype.$toast(res.data.msg)
                        reject()
                    }
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    
                    reject(err.data)
                })
        })
    },
    postDireact(url, params, json) {
        let headers = {},
            data;
        let access_token = sessionStorage.getItem("access_token") || localStorage.getItem("access_token") || '';
        data = {
            "head": {},
            "data": params
        }
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.post(url, data, { headers: headers })
                .then(res => {
                    Vue.prototype.$toast.clear();
                    resolve(res.data);
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    
                    reject(err.data)
                })
        })
    },
    post(url, params, json) {
        let headers = {},
            data;
        let access_token = sessionStorage.getItem("access_token") || localStorage.getItem("access_token") || '';
        data = {
            "head": {},
            "data": params
        }
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.post(url, data, { headers: headers })
                .then(res => {
                    
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    }else if(res.data&&res.data.head&&(res.data.head.code == '1000')){
                        Vue.prototype.$toast("数据查询失败")
                        reject()
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2000')){
                        Vue.prototype.$toast("用户名及密码验证失败")
                        reject()
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2010')){
                        Vue.prototype.$toast("安全token错误")
                        reject()
                    }else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            Vue.prototype.$toast(msg)
                        }
                        reject();
                    }
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    
                    reject(err.data)
                })
        })
    },
    postWithoutHead(url, params, json) {
        let headers = {},
            data = params;
        let access_token = sessionStorage.getItem("access_token") || localStorage.getItem("access_token") || '';
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.post(url, {...params}, { headers: headers })
                .then(res => {
                    Vue.prototype.$toast.clear();
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    } else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            Vue.prototype.$toast(msg)
                        }
                        reject();
                    }
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    
                    reject(err.data)
                })
        })
    },
    postWithFile(url, params, json){
        let headers = {};
        headers['Content-type'] = 'multipart/form-data';
        return new Promise((resolve, reject) => {
            axios.post(url, params, { headers: headers })
                .then(res => {
                    Vue.prototype.$toast.clear();
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    }else if(res.data&&res.data.head&&(res.data.head.code == '1000')){
                        Vue.prototype.$toast("数据查询失败")
                        reject();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2000')){
                        Vue.prototype.$toast("用户名及密码验证失败")
                        reject();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2010')){
                        Vue.prototype.$toast("安全token错误")
                        reject();
                    }else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            Vue.prototype.$toast(msg)
                        }
                        reject();
                    }
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    
                    reject(err.data)
                })
        })
    },
    put(url, params, json) {
        let headers = {},
            data;
            let access_token = sessionStorage.getItem("access_token") || localStorage.getItem("access_token") || util.getCookie('access_token') || '';
        data = {
            "head": {
                origin: "2",
                sign: "",
                xzqydm: "",
                access_token: access_token
            },
            "data": params
        }
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.post(url, data, { headers: headers })
                .then(res => {
                    Vue.prototype.$toast.clear();
                    if(res.data&&res.data.head&&res.data.head.code === '0000'){
                        resolve(res.data);
                    }else {
                        resolve();
                    }
                })
                .catch(err => {
                    Vue.prototype.$toast('请求失败');
                    reject(err.data)
                })
        })
    },
    delete(url, params) {
        return new Promise((resolve, reject) => {
            axios.delete(url, {
                params: params
            }).then(res => {
                Vue.prototype.$toast.clear();
                if(res.data&&res.data.head&&res.data.head.code === '0000'){
                    resolve(res.data);
                }else {
                    if(res.data&&res.data.head){
                        let msg = res.data.head.msg;
                        Vue.prototype.$toast(msg)
                    }
                    reject();
                }
            }).catch(err => {
                Vue.prototype.$toast('请求失败');
                reject(err.data)
            })
        })
    },
}

export function redirectToHome(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + '/portal-ol' + '/#/home'
}
export function linkToServerAdmin(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + "/msurveyplat-serviceol" + '/#/admin/review'
}
export function linkToServerSurvey(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + "/msurveyplat-serviceol" + '/#/survey/application'
}
export function linkToServerConstruct(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + "/msurveyplat-serviceol" + '/#/construction/myproject'
}
export function linkToProManage(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + "/msurveyplat-promanage" + '/#/home'
}
export function linkToPortal(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + '/portal/view/index.html'
}
// 跳转到cas登录页
export function redirectToCasLosgin(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = config.casLogin + '/cas/login?service='+ location.origin +'/portal-ol/home'
}
export default httpService;