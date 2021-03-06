import httpService from './request';
const serviceOlContext = config.serviceOlContext;
const msurveyplatContext = config.msurveyplatContext;
// 获取字典项
// 获取名录库需要上传的文件
export function getUploadList(params,sjclUrl='/fileoperation/getsjcl'){
    return httpService.post(msurveyplatContext + sjclUrl, params)
}
// 获取需要上传的合同材料
export function queryHtUploadList(params) {
    return httpService.post(msurveyplatContext + '/fileoperationhtxx/getHtxx', params)
}
// 保存材料
export function saveUploadFile(url,params){
    return httpService.postWithFile(msurveyplatContext + url, params)
}
// 上传合同材料
export function saveUploadHtFile(params) {
    return httpService.postWithFile(msurveyplatContext + '/fileoperationhtxx/uploadfiles', params)
}
// 获取字典项
export function getDictInfo(params){
    return httpService.post(msurveyplatContext + '/zdx/getzdxx', params)
}
// 删除上传附件
export function deleteFile(params) {
    return httpService.post(msurveyplatContext + "/fileoperation/deletefile", params)
}
// 删除合同附件列
export function deleteHtFjList(params){
    return httpService.post(msurveyplatContext + "/fileoperationhtxx/deletefileHtxxJl", params)
}
// 新增上传材料列
export function saveFjList(params){
    return httpService.post(msurveyplatContext + "/fileoperation/saveSjclpz", params)
}
// 删除上传资料附件列
export function deleteFjList(params){
    return httpService.post(msurveyplatContext + "/fileoperation/deletefileJl", params)
}
// 业务逻辑验证
export function yzYwlj(params){
    return httpService.postDireact(serviceOlContext + '/ywljyz/excuteYwljyz', params)
}
// 获取需要验证的表单
export function getYzList(params){
    return httpService.post(serviceOlContext + '/bdbtxyz/getBdbtxyzList', params)
}
// 获取合同备案登记列表(合同备案登记list页)
export function queryHtbadjList(params) {
    return httpService.post(msurveyplatContext + '/contractfileselect/queryinformationtoberecorded', params)
}
// 查询是否已办结
export function checkFinishStatus(params){
    return httpService.post(msurveyplatContext + '/contractfile/checkproarchstatus', params)
}
// 办结操作
export function finishHtbadj(params){
    return httpService.post(msurveyplatContext + '/contractfile/projectcomplete', params)
}
// 点击新增登记单初始化登记单数据
export function initDjdData(params) {
    return httpService.getDataWithCode(msurveyplatContext + '/xcsldj/initXcsldj', params)
}
// 删除初始化的登记单数据
export function deleteDjdData(params) {
    return httpService.post(msurveyplatContext + '/xcsldj/deleteXcsldj', params)
}
// 根据项目代码加载信息
export function queryInfoByGcbh(params){
    return httpService.post(msurveyplatContext + '/xcsldj/queryChxxByChgcbh', params)
}
// 根据需求发布编号查询测绘项目信息
export function queryChxmInfo(params){
    return httpService.post(msurveyplatContext + '/xcsldj/queryChxmByXqfbbh', params)
}
// 保存合同备案登记
export function saveBadj(params){
    return httpService.post(msurveyplatContext + '/xcsldj/saveJsdwFbxq', params)
}
// 查询备案登记信息
export function queryBadjXX(params) {
    return httpService.post(msurveyplatContext + '/xcsldj/queryBaxxByChxmid', params)
}

// 获取合同备案登记列表(暂停恢复终止页面)
export function queryHtbadjSljlList(params) {
    return httpService.post(msurveyplatContext + '/contractfile/queryCzsxList', params)
}
// 恢复所选事项
export function resumeBadj(params){
    return httpService.post(msurveyplatContext + '/contractfile/changeCzzt', params)
}
// 暂停所选事项
export function pauseBadj(params){
    return httpService.post(msurveyplatContext + '/contractfile/changeCzzt', params)
}
// 停止所选事项
export function stopBadj(params){
    return httpService.post(msurveyplatContext + '/contractfile/changeCzzt', params)
}
// 获取日志详情
export function queryCzrzList(params) {
    return httpService.post(msurveyplatContext + '/contractfile/queryCzrzList', params)
}


// 获取修改记录列表
export function queryEditRecordList(params) {
    return httpService.post(msurveyplatContext + '/log/xgrzcx', params)
}
// 获取修改人信息
export function queryUserList() {
    return httpService.getDataWithCode(msurveyplatContext + "/index/getXmglry")
}
// 获取修改记录详情
export function queryEditRecordDetail(params){
    return httpService.getDataWithCode(msurveyplatContext + "/log/rzck/" + params.rzid)
}
// 获取测绘单位list
export function queryChdwList(params) {
    return httpService.post(msurveyplatContext + '/xcsldj/queryChdwList', params)
}
// 获取建设单位list
export function queryJsdwList(params) {
    return httpService.get(msurveyplatContext + '/construction/queryConstructionList')
}
// 添加测绘单位
export function saveChdw(params){
    return httpService.post(msurveyplatContext + '/xcsldj/saveChdw', params)
}
// 添加建设单位
export function saveJsdw(params){
    return httpService.post(msurveyplatContext + '/xcsldj/saveJsdw', params)
}
// 项目查看列表查询
export function searchProject(params){
    return httpService.post(msurveyplatContext + '/management/queryResultsManagement', params)
}
//测绘的受理管理查看表格
export function getStepTable(params){
    return httpService.post(msurveyplatContext + '/contractfile/getprojectinfo', params)
}
// 获取字典项
export function getStepDictInfo(params){
    return httpService.post(msurveyplatContext + '/contractfile/getclsxzd', params)
}
// 查看测绘事项
export function getZdClsx(params){
    return httpService.post(msurveyplatContext + '/zdx/getZdClsx', params)
}
// 查看测绘事项-详情
export function getProjectClsxInfo(params){
    return httpService.post(msurveyplatContext + '/contractfile/getProjectClsxInfo', params)
}
// 权限控制获取
export function getResourceAuthorty(params){
    return httpService.get('/portal/index/getResourceAuthorty', params)
}

// 常州- 成果提交查询
export function queryCgInfoList(params){
    return httpService.post(msurveyplatContext + "/management/queryResultsManagement", params)
}
// 常州- 成果提交查询待办
export function queryCgDbInfoList(params){
    return httpService.post(msurveyplatContext + "/management/queryResultsManagementDB", params)
}
// 常州- 成果提交查询已办
export function queryCgYbInfoList(params){
    return httpService.post(msurveyplatContext + "/management/queryResultsManagementYB", params)
}
// 常州- 成果提交查询-查看
export function queryProjectInfo(params){
    return httpService.post(msurveyplatContext + "/contractfile/getprojectinfo", params)
}
// 常州- 成果抽查-台账
export function queryresultsspotchecklist(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/queryresultsspotchecklist", params)
}
// 常州- 成果抽查-抽查项目数
export function resultsspotrandomcheck(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/resultsspotrandomcheck", params)
}
// 常州- 成果抽查-评价
export function achievementevaluation(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/achievementevaluation", params)
}
// 常州- 成果抽查-查看评价信息
export function spotcheckevaluationdetails(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/spotcheckevaluationdetails", params)
}

// 我的测绘项目-上传基础数据范围
export function uploadBaseData(params,url="/jcsjsq/uploadFile"){
    return httpService.postWithFile(msurveyplatContext + url, params)
}

// 日志管理
export function queryRecordList(params) {
    return httpService.post(msurveyplatContext + "/czrz/queryCzrzList", params)
}
// 获取建设单位管理台账
export function queryJsdwManageList(params){
    return httpService.post(msurveyplatContext + "/construction/queryConstructionInfo", params)
}
// 获取文件流
export function queryDeliverData(params){
    return httpService.getWithBlob(msurveyplatContext + `/pdf/tyjfd/${params.chxmid}`)
}
// 确认是否已交付
export function isdeleiverData(params){
    return httpService.post(msurveyplatContext + `/submit/tyjfjc`,params)
}
// 确认交付
export function deliverData(params){
    return httpService.post(msurveyplatContext + `/submit/tyjfqr`,params)
}
// 流程-删除
export function deleteOtherAssignment(params){
    return httpService.postDireact(`/portal/index/deleteOtherAssignment/${params.taskid}/${params.userid}`)
}

// 备案列表-挂起解挂
export function saveBaxmSfgq(params){
    return httpService.post(msurveyplatContext + `/contractfileselect/saveBaxmSfgq`,params)
}