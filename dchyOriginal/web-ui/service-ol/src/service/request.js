import axios from 'axios';
import util from './util';
import Vue from "vue"

let httpService = {
    get(url, params) {
        return new Promise((resolve, reject) => {
            axios.get(url, {
                params: params
            }).then(res => {
                Vue.$loading.close()
                if(res.data){
                    resolve(res.data);
                }else {
                    reject();
                }
            }).catch(err => {
                Vue.$loading.close()
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
                    Vue.$loading.close()
                    if(res.data.code=="0000"){
                        resolve(res.data);
                    }else {
                        layer.msg(res.data.msg)
                        reject()
                    }
                })
                .catch(err => {
                    Vue.$loading.close()
                    layer.msg("请求失败")
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
                    Vue.$loading.close()
                    resolve(res.data);
                })
                .catch(err => {
                    layer.msg("请求失败")
                    Vue.$loading.close()
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
                    Vue.$loading.close()
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    }else if(res.data&&res.data.head&&(res.data.head.code == '1000')){
                        layer.msg("数据查询失败")
                        reject()
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2000')){
                        layer.msg("用户名及密码验证失败")
                        reject()
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2010')){
                        layer.msg("安全token错误")
                        reject()
                    }else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            layer.msg(msg)
                        }
                        reject();
                    }
                })
                .catch(err => {
                    layer.msg("请求失败")
                    Vue.$loading.close()
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
            axios.post(url, params, { headers: headers })
                .then(res => {
                    Vue.$loading.close()
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    }else if(res.data&&res.data.head&&(res.data.head.code == '1000')){
                        layer.msg("数据查询失败")
                        resolve();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2000')){
                        layer.msg("用户名及密码验证失败")
                        resolve();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2010')){
                        layer.msg("安全token错误")
                        resolve();
                    }else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            layer.msg(msg)
                        }
                        resolve();
                    }
                })
                .catch(err => {
                    layer.msg("请求失败")
                    Vue.$loading.close()
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
                    Vue.$loading.close()
                    if(res.data&&res.data.head&&(res.data.head.code == '0000')){
                        resolve(res.data);
                    }else if(res.data&&res.data.head&&(res.data.head.code == '1000')){
                        layer.msg("数据查询失败")
                        reject();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2000')){
                        layer.msg("用户名及密码验证失败")
                        reject();
                    }else if(res.data&&res.data.head&&(res.data.head.code == '2010')){
                        layer.msg("安全token错误")
                        reject();
                    }else {
                        if(res.data&&res.data.head){
                            let msg = res.data.head.msg;
                            layer.msg(msg)
                        }
                        reject();
                    }
                })
                .catch(err => {
                    layer.msg("请求失败")
                    Vue.$loading.close()
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
                sign: "",
                xzqydm: "",
                access_token: access_token
            },
            "data": params
        }
        headers['Content-type'] = json ? 'application/json;charset=UTF-8' : 'application/json;charset=UTF-8';
        return new Promise((resolve, reject) => {
            axios.put(url, data, { headers: headers })
                .then(res => {
                    if(res.data&&res.data.head&&res.data.head.code === '0000'){
                        resolve(res.data);
                    }else {
                        reject();
                    }
                })
                .catch(err => {
                    layer.msg("请求失败")
                    reject(err.data)
                })
        })
    },
    delete(url, params) {
        return new Promise((resolve, reject) => {
            axios.delete(url, {
                params: params
            }).then(res => {
                Vue.$loading.close()
                if(res.data&&res.data.head&&res.data.head.code === '0000'){
                    resolve(res.data);
                }else {
                    reject();
                }
            }).catch(err => {
                layer.msg("请求失败")
                reject(err.data)
            })
        })
    },
    deleteWithHead(url,params) {
        let data = {
            head: {},
            data: params
        }
        return new Promise((resolve, reject) => {
            axios.delete(url, {
                data
            }).then(res => {
                Vue.$loading.close()
                if(res.data&&res.data.head&&res.data.head.code === '0000'){
                    resolve(res.data);
                }else {
                    if(res.data&&res.data.head){
                        let msg = res.data.head.msg;
                        layer.msg(msg)
                    } else {
                        layer.msg("请求失败")
                    }
                    reject();
                }
            }).catch(err => {
                layer.msg("请求失败")
                reject(err.data)
            })
        })
    },
    deleteWithBody(url, params) {
        return new Promise((resolve, reject) => {
            axios.delete(url, {
                data: params
            }).then(res => {
                Vue.$loading.close()
                if(res.data&&res.data.head&&res.data.head.code === '0000'){
                    resolve(res.data);
                }else {
                    if(res.data&&res.data.head){
                        let msg = res.data.head.msg;
                        layer.msg(msg)
                    } else {
                        layer.msg("请求失败")
                    }
                    reject();
                }
            }).catch(err => {
                layer.msg("请求失败")
                reject(err.data)
            })
        })
    },
}

// 跳转到cas登录页
export function redirectToCasLosgin(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = config.casLogin + '/cas/login?service='+ location.origin +'/portal-ol/home'
}
export function redirectToHome(){
    if (!location.origin) {
        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
    }
    location.href = location.origin + '/portal-ol' + '/#/home'
}
export default httpService;