import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// (建设单位，测绘单位)-留言列表-获取台账
export function queryAdviceList(params){
    return httpService.post(serviceOlContext + "/zxkf/queryMessageList", params)
}
// (建设单位，测绘单位)-留言列表-初始化咨询
export function initAdviceSqxx(){
    return httpService.get(serviceOlContext + "/zxkf/initIssues")
}
// (建设单位，测绘单位)-留言列表-删除咨询
export function deleteAdviceSqxx(params){
    return httpService.post(serviceOlContext + "/zxkf/delIssues", params)
}
// (建设单位，测绘单位)-留言列表-新增咨询
export function addNewAdvice(params) {
    return httpService.post(serviceOlContext + "/zxkf/saveIssues", params)
}
// (建设单位，测绘单位)-我的留言-获取台账
export function queryMyAdvice(params) {
    return httpService.post(serviceOlContext + "/zxkf/queryMyIssuesList", params)
}

// (管理单位)-留言回复-获取台账
export function queryAdviceReply(params) {
    return httpService.post(serviceOlContext + "/zxkf/queryAnswerList", params)
}
// (管理单位)-留言回复-回复详情
export function queryIssuseInfo(params) {
    return httpService.post(serviceOlContext + "/zxkf/queryIssuesByid", params)
}
// (管理单位)-留言回复-回复
export function adviceReply(params) {
    return httpService.post(serviceOlContext + "/zxkf/saveAnswer", params)
}
// (管理单位)-留言列表-获取台账
export function queryAdminAdviceList(params) {
    return httpService.post(serviceOlContext + "/zxkf/queryGldwMessageList", params)
}