import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//查询测绘单位我的项目列表
export function getMyprojectList(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/queryBasicDataApplicationInfo', params)
}
// 测绘单位查询项目
export function chdwQueryProject(params){
    return httpService.post(serviceOlContext + '/chdwxmcx/getChdwXmcxList', params)
}
// 测绘单位
export function queryChsl(){
    return httpService.post(serviceOlContext + "/chdwxmcx/chdwCkxmCount")
}

// 获取测绘单位项目信息
export function queryChdwSqxx(params){
    return httpService.post(serviceOlContext + '/chdwxmcx/queryChdwxxByChdwxxid', params)
}
// 获取测绘单位项目信息(新)
// export function myProjectInfoView(params){
//     return httpService.post(serviceOlContext + '/jcsjsq/myProjectInfoView', params)
// }
// 获取建设单位（委托信息）
export function queryJsdwWtxx(params){
    return httpService.post(serviceOlContext + '/chdwxmcx/queryWtxxByChdwxxid', params)
}
// 获取建设单位委托信息-上传材料
export function getUploadFile(params){
    return httpService.post(serviceOlContext + '/chdwxmcx/queryScclxx', params)
}
// 获取测绘单位办理信息（委托办理信息）
// export function queryWtblxx(params){
//     return httpService.post(serviceOlContext + '/chdwxmcx/queryQtblxxByChxmid', params)
// }

// 查询建设单位我的项目列表
export function getJsdwProjectList(params){
    return httpService.post(serviceOlContext + "/jsdwFbxqgl/jsdwCkxm", params)
}
// 查询建设单位我的项目列表-所属阶段
export function querySsjd(){
    return httpService.get(serviceOlContext + "/jsdwFbxqgl/ssjdList")
}
//建设单位-合同下载                     
export function jsdwHtDownload(params){
    return httpService.post(serviceOlContext + "/jsdwFbxqgl/jsdwHtDownload", params)
}
// 建设单位-合同下载                     
export function jsdwdownload(params){
    return httpService.post(serviceOlContext + "/jsdwFbxqgl/download", params)
}
// 测绘单位-合同下载                     
export function chdwHtDownload(params){
    return httpService.post(serviceOlContext + "/chdwxmcx/chdwHtDownload", params)
}
//基础数据申请信息台账
export function querySqxxList(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/querySqxxList', params)
}
//初始化生成基础数据申请数据
export function initBasicDataApplication(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/initBasicDataApplication', params)
}
//查看基础数据申请详情
export function applicationInfoView(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/applicationInfoView', params)
}

// 我的测绘项目-上传基础数据范围
export function uploadBaseData(params){
    return httpService.postWithFile(serviceOlContext + "/jcsjsq/uploadFile", params)
}
// 我的测绘项目-上传基础数据范围
export function getZdClsx(params){
    return httpService.post(serviceOlContext + "/jcsjsq/getZdClsx", params)
}
// 获取建设单位委托信息-上传材料
export function getsjcl(params){
    return httpService.post(serviceOlContext + '/jcsjsq/getsjcl', params)
}
// 成果提交初始化
export function initSqxx(params){
    return httpService.post(serviceOlContext + '/submit/initSqxx', params)
}

// 成果检查
export function checkZipFile(params){
    return httpService.postWithFile(serviceOlContext + "/submit/checkZipFiles", params)
}

// 成果提交
export function uploadZipFile(params){
    return httpService.post(serviceOlContext + '/submit/zipUpload', params)
}
// 建设单位-办结
export function onlinecomplete(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinecomplete', params)
}
// 建设单位-办结前检查
export function onlinecompletecheck(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinecompletecheck', params)
}

// 我的测绘项目-项目进度
export function queryOnlineProcess(params){
    return httpService.post(serviceOlContext + '/onlinedelegation/onlinerecordprcess', params)
}

// 我的测绘项目-查看-附件材料
export function onlinebafilepreview(params){
    return httpService.post(serviceOlContext + "/onlinedelegation/onlinebafilepreview", params)
}
// 基础数据申请-交付日志
export function resultsDeliveryLogList(params){
    return httpService.post(serviceOlContext + "/jcsjsq/resultsDeliveryLogList", params)
}
// 基础数据下载-进度查看
export function queryProcessInfo(params){
    return httpService.post(serviceOlContext + "/jcsjsq/getProcessInfoBySlbh", params)
}