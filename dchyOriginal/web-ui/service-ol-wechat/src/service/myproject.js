import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// 查询建设单位我的项目列表
export function getJsdwProjectList(params){
    return httpService.post(serviceOlContext + "/jsdwFbxqgl/jsdwCkxm", params)
}
// 我的测绘项目-项目进度
export function queryOnlineProcess(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinerecordprcess', params)
}

// 建设单位-办结
export function onlinecomplete(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinecomplete', params)
}
// 建设单位-办结前检查
export function onlinecompletecheck(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinecompletecheck', params)
}
// 获取建设单位评价列表
export function jsdwEvaludateList(params){
    return httpService.post(serviceOlContext + "/jsdw/getxmpjlist", params)
}
// 获取建设单位（委托信息）
export function queryJsdwWtxx(params){
    return httpService.post(serviceOlContext + '/chdwxmcx/queryWtxxByChdwxxid', params)
}
// 建设单位评价
export function jsdwEvaludate(params){
    return httpService.post(serviceOlContext + "/jsdw/getpjxxbyid", params)
}
// 获取未读已读
export function getMessage(params){
    return httpService.post(serviceOlContext + "/message/getMessage", params)
}
// 已读
export function changeMessageStatus(params) {
    return httpService.post(serviceOlContext + "/message/changeStatus", params)
}