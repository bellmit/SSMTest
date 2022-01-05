import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//查询通知公告列表
export function getAnnouncementList(params) {
    return httpService.post(serviceOlContext + '/gldwTzgggl/getTzggByBtAndGglx', params)
}
// 初始化发布新公告
export function initAnnouncement(){
    return httpService.get(serviceOlContext + '/gldwTzgggl/initTzgggl')
}
// 删除初始化的通知公告
export function deleteAnnouncement(params){
    return httpService.post(serviceOlContext + '/gldwTzgggl/deleteGldwTzggglAndFjByTzggid', params)
}
// 删除列表通知公告
export function deleteListAnnounce(params){
    return httpService.post(serviceOlContext + '/gldwTzgggl/deleteGldwTzggglByTzggid', params)
}
// 保存通知公告
export function saveAnnouncement(params){
    return httpService.post(serviceOlContext + '/gldwTzgggl/saveOrUpdateTzgg', params)
}
// 查看通知公告详情
export function queryAnnounceDetail(params) {
    return httpService.postDataDirect(serviceOlContext + '/gldwTzgggl/queryGldwTzggglByTzggid', params)
}