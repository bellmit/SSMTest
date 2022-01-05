import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//我的待办-查询
export function reviewQuery(params) {
    return httpService.post(serviceOlContext + '/gldwDblb/queryDblbList', params)
}
//我的待办-判断当前用户
export function isCurrentUser(params) {
    return httpService.post(serviceOlContext + '/gldwDblb/isEnterPage', params)
}
//我的待办-审核页面
export function reviewCheck(params) {
    return httpService.post(serviceOlContext + '/mlk/getchdwbymlkid', params)
}
// 我的待办-获取注销原因
export function queryYcyy(params){
    return httpService.get(serviceOlContext + `/mlk/getycyybymlkid/${params.mlkid}`)
}
// 我的待办-确认审核
export function reviewData(params){
    return httpService.post(serviceOlContext + `/mlk/mlkzxsh`, params)
}
//我的待办-附件列表
export function getScclList(params) {
    return httpService.post(serviceOlContext + '/gldwDblb/getScclList', params)
}
//我的待办-审核意见
export function reviewSave(params) {
    return httpService.post(serviceOlContext + '/shxx/shbj ', params)
}
//我的待办-审核意见-获取信息
export function getSignxx(params) {
    return httpService.get(serviceOlContext + '/shxx/sign/'+params.dbrwid+'/'+params.dqjd, {params})
}
//我的待办-审核意见-添加签名
export function getSign(params) {
    return httpService.get(serviceOlContext + '/shxx/sign/'+params.signId, {})
}
//我的待办-审核意见-删除签名
export function removeSign(params) {
    return httpService.delete(serviceOlContext + '/shxx/delsign/'+params.signId, {})
}
//我的待办-审核办结
export function checkOver(params) {
    return httpService.postWithoutHead(serviceOlContext + '/shxx/shbj', params)
}
//我的待办-文件下载
export function getProcessList(params) {
    return httpService.get(serviceOlContext + '/fileoperation/download', params)
}
// 获取附件数量
export function getFileNums(params) {
    return httpService.post(serviceOlContext + '/fileoperation/getuploadfilenums', params)
}
// 获取附件查看的信息
export function queryFileList(params){
    return httpService.post(serviceOlContext + "/mlk/viewattachments", params)
}

// 成果附件查看
export function queryCgFileList(params){
    return httpService.post(serviceOlContext + "/onlinedelegation/onlinegcpreview", params)
}
export function queryDetailFile(params){
    return httpService.post(serviceOlContext + "/onlinedelegation/onlinegcpreviewbyid", params)
}
export function getCgFileNums(params){
    return httpService.post(serviceOlContext + "/onlinedelegation/onlinegetuploadfilenums", params)
}


// 名录库注销-待办列表
export function getMlkDbList(params){
    return httpService.post(serviceOlContext + "/mlk/getmlkdb", params)
}
// 名录库注销-一般列表
export function getMlkYbList(params){
    return httpService.post(serviceOlContext + "/mlk/getmlkyb", params)
}