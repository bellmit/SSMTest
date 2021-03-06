import httpService from './request';
const msurveyplatContext = config.msurveyplatContext;
//我的待办-查询
export function reviewQuery(params) {
    return httpService.post(msurveyplatContext + '/gldwDblb/queryDblbList', params)
}
//我的待办-判断当前用户
export function isCurrentUser(params) {
    return httpService.post(msurveyplatContext + '/gldwDblb/isEnterPage', params)
}
//我的待办-审核页面
export function reviewCheck(params) {
    return httpService.post(msurveyplatContext + '/mlk/getchdwbymlkid', params)
}
//我的待办-附件列表
export function getScclList(params) {
    return httpService.post(msurveyplatContext + '/gldwDblb/getScclList', params)
}
//我的待办-审核意见
export function reviewSave(params) {
    return httpService.post(msurveyplatContext + '/shxx/shbj ', params)
}
//我的待办-审核意见-获取信息
export function getSignxx(params) {
    return httpService.get(msurveyplatContext + '/shxx/sign/'+params.taskid+'/'+params.xmid+'/'+params.dqjd)
}
//我的待办-审核意见-添加签名
export function getSign(params) {
    return httpService.get(msurveyplatContext + '/shxx/sign/'+params.signId, {})
}
//我的待办-审核意见-删除签名
export function removeSign(params) {
    return httpService.delete(msurveyplatContext + '/shxx/delsign/'+params.signId)
}
//我的待办-审核办结
export function checkOver(params) {
    return httpService.postWithoutHead(msurveyplatContext + '/shxx/shbj ', params)
}
//我的待办-文件下载
export function getProcessList(params) {
    return httpService.get(msurveyplatContext + '/fileoperation/download ', params)
}
export function queryFileList(params){
    return httpService.post(msurveyplatContext + "/contractfile/viewfilesinit",params)
}
export function queryGcTree(params){
    return httpService.post(msurveyplatContext + "/contractfile/chgcclcgyks", params)
}
export function queryDetailFile(params) {
    return httpService.post(msurveyplatContext + "/contractfile/viewchildfilesbyid", params)
}
// 获取附件数量
export function getFileNums(params) {
    return httpService.post(msurveyplatContext + '/fileoperation/getuploadfilenums', params)
}


// 审核页面接口
export function queryDetail(params){
    return httpService.post(msurveyplatContext + '/submit/queryXmxxByTaskid', params)
}

// 获取附件接口
export function queryFile(params){
    return httpService.post(msurveyplatContext + '/submit/viewfiles', params)
}

// 获取审核记录接口
export function queryReviewDetail(params) {
    return httpService.post(msurveyplatContext + '/submit/getShjlByTaskid', params)
}

// 审核办结
export function checkFinish(params){
    return httpService.post(msurveyplatContext + '/submit/checkFinish', params)
}

// 常州-备案列表
export function queryReviewList(params) {
    return httpService.post(msurveyplatContext + "/contractfileselect/queryrecordlist", params)
}
 
// 常州-委托线下审核
export function reviewCommission(params) {
    return httpService.post(msurveyplatContext + "/commission/reviewCommission", params)
}
// 常州-备案成功后处理
export function deleteXcsldj(params) {
    return httpService.post(msurveyplatContext + "/xcsldj/deleteXcsldj", params)
}
// 常州-成果下载
export function getuploadfilenums(params) {
    return httpService.post(msurveyplatContext + "/fileoperation/getuploadfilenums", params)
}
// 备案列表-材料附件
export function onlinebafilepreview(params) {
    return httpService.post(msurveyplatContext + "/contractfile/onlinebafilepreview", params)
}