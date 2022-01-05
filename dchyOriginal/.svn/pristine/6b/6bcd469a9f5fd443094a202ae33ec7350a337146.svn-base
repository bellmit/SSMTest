import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// 名录库查看获取名录库列表
export function queryMlkList(params){
    return httpService.post(serviceOlContext + '/mlk/getmlkbymc', params)
}
// 获取字典项
export function getDictInfo(params){
    return httpService.post(serviceOlContext + '/zdx/getzdxx ', params)
}
// 新增初始化获取建设单位
export function initEntrust(params) {
    return httpService.get(serviceOlContext + '/entrust/initEntrust', params)
}
// 新增初始化测绘单位列表
export function queryChdwList(params) {
    return httpService.post(serviceOlContext + '/entrust/queryChdwList', params)
}
// 保存委托
export function saveEntrust(params){
    return httpService.post(serviceOlContext + '/entrust/saveEntrust', params)
}
// 确认委托
export function updateEntrust(params){
    return httpService.post(serviceOlContext + '/entrust/updateEntrust', params)
}
// 查询委托（台账）
export function getEntrustByPage(params){
    return httpService.post(serviceOlContext + '/entrust/getEntrustByPage', params)
}
// 查看详情
export function queryEntrustByChxmid(params){
    return httpService.post(serviceOlContext + '/entrust/queryEntrustByChxmid', params)
}
// 删除
export function deleteEntrust(params){
    return httpService.post(serviceOlContext + '/entrust/deleteEntrust', params)
}
// 取回
export function retrieveEntrust(params){
    return httpService.post(serviceOlContext + '/entrust/retrieveEntrust', params)
}
// 测绘单位-项目委托管理
export function queryproject(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/queryprojectentrustfromchdw', params)
}
// 测绘单位-项目委托管理-当前待接受的委托
export function queryAcceptedNum(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/gettobeacceptednum', params)
}
// 测绘单位-项目委托管理-核验
export function verification(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/verification', params)
}
// 测绘单位-项目委托管理-修改核验状态
export function alterhtzt(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/alterhtzt', params)
}
// 测绘单位-项目委托管理-备案合同上传
export function keeponrecord(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/keeponrecord', params)
}
// 测绘单位-项目委托管理-备案审核意见
export function getauditopinion(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/getauditopinion', params)
}
// 建设单位-获取核验信息
export function queryHyyj(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/gethyyjbychxmid', params)
}
// 建设单位-点击核验获取上次填写的联系人，联系电话
export function beforeverificationinfo(params){
    return httpService.postDireact(serviceOlContext + '/onlinedelegation/beforeverificationinfo', params)
}
// 测绘单位-备案点击取消
export function deleteHtBeforeCancel(params){
    return httpService.deleteWithHead(serviceOlContext + '/onlinedelegation/delhtwj', params)
}
// 合同变更-同步合同信息
export function syncHtInfo(params) {
    return httpService.put(serviceOlContext + "/onlinedelegation/htsyn", params)
}
// 合同变更-合同变更记录
export function syncHtRecord(params) {
    return httpService.post(serviceOlContext + "/onlinedelegation/gethtbgrecord", params)
}
// 项目代码查重校验
export function checkGcbh(params){
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/queryGcmcByGcbh', params)
}