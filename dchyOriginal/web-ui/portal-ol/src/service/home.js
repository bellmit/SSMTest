import httpService from './request';
const portalOlContext = config.portalOlContext;
const serviceOlContext = config.serviceOlContext;

// 获取用户信息
export function getUserInfo(params){
    return httpService.getDataWithCode(portalOlContext + "/index/getUserMessage", params)
}
// 获取字典项
export function getDictInfo(params){
    return httpService.post(portalOlContext + '/zdx/getzdxx ', params)
}
// 获取需要验证的表单
export function getYzList(params){
    return httpService.post(portalOlContext + '/bdbtxyz/getBdbtxyzList', params)
}
// 业务逻辑验证
export function yzYwlj(params){
    return httpService.postDireact(portalOlContext + '/ywljyz/excuteYwljyz', params)
}
// 获取通知公告的列表
export function queryNoticeList(params){
    return httpService.post(portalOlContext + '/tzgg/getOtherTzgg', params)
}
// 获取通知公告详情
export function queryNoticeDetail(params){
    return httpService.postDataDirect(portalOlContext + '/tzgg/getTzggByTzggid', params)
}
// 获取已上传的文件
export function queryFileList(params) {
    return httpService.post(portalOlContext + '/fileoperation/getsjcl', params)
}
// 名录库查看获取名录库列表
export function queryMlkList(params){
    return httpService.post(portalOlContext + '/mlk/getmlkbymc', params)
}
// 获取办事指南通知公告
export function queryGuideList(params) {
    return httpService.post(portalOlContext + '/tzgg/getTzggByBszn', params)
}
// 获取模板下载列表
export function queryTemplateList(params){
    return httpService.post(portalOlContext + '/mbxx/getMbQyzt', params)
}
// 获取名录库从业人员详情信息
export function querycyrybymlkid(params){
    return httpService.post(portalOlContext + '/mlk/querycyrybymlkid', params)
}
// 获取名录库机构详情信息
export function querymlkdetails(params){
    return httpService.post(portalOlContext + '/mlk/querymlkdetails', params)
}
// 获取名录库机构诚信记录
export function queryMlkCxRecord(params) {
    return httpService.post(portalOlContext + "/mlk/querycxjlbymlkid", params)
}