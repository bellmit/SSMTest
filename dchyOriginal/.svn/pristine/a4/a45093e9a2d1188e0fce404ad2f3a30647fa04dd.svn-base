import httpService from './request';
const serviceOlContext = config.serviceOlContext;
const portalOlContext = config.portalOlContext;
//模板管理列表-查询
export function getTemplate(params) {
    return httpService.post(serviceOlContext + '/htmbgl/queryMbxxByPage', params)
}

//模板管理列表-新增
export function addTemplate(params) {
    return httpService.post(serviceOlContext + '/htmbgl/saveMbxx', params)
}

//模板管理列表-禁用-启用
export function enableAndDisabled(params) {
    return httpService.post(serviceOlContext + '/htmbgl/saveMbqyzt', params)
}

// 获取用户信息
export function getUserInfo(){
    return httpService.get(portalOlContext + "/index/getUserMessage")
}

// 初始化新增模板
export function getMbid(params){
    return httpService.get(serviceOlContext + "/htmbgl/initMbgl", params)
}

// 取消新增模板
export function cancelTemplate(params){
    return httpService.post(serviceOlContext + "/htmbgl/deleteMbglByMbid", params)
}

