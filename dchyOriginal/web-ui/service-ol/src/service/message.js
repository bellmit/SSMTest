import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// 获取消息列表
export function getMessageList(params){
    return httpService.post(serviceOlContext + "/message/getMessage", params)
}
// 已读操作
export function changeStatus(params){
    return httpService.post(serviceOlContext + "/message/changeStatus", params)
}