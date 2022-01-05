import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//查询我的项目列表
export function publishProject(params) {
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/saveJsdwFbxq', params)
}
// 查询发布请求
export function getPublishList(params) {
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/jsdwCkxq', params)
}
// 查看详情
export function queryPublishDetail(params) {
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/queryChxmxxByChxmid', params)
}
// 项目代码查重校验
export function checkGcbh(params){
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/queryGcmcByGcbh', params)
}
// 获取需要验证的表单
export function getYzList(params){
    return httpService.post(serviceOlContext + '/bdbtxyz/getBdbtxyzList', params)
}
// 获取已选中的测绘事项
export function getSelectClsx(params) {
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/queryClsxByChxmid', params)
}
// 初始化发布信息
export function initPublish(){
    return httpService.get(serviceOlContext + '/jsdwFbxqgl/initJsdwFbxq')
}
// 删除发布需求初始化信息
export function deleteInitInfo(params){
    return httpService.post(serviceOlContext + '/jsdwFbxqgl/deleteJsdwFbxq ', params)
}
// 业务逻辑验证
export function yzYwlj(params){
    return httpService.postDireact(serviceOlContext + '/ywljyz/excuteYwljyz', params)
}
