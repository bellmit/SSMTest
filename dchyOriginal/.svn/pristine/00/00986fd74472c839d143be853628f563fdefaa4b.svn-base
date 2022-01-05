import httpService from "./request";
const msurveyplatContext = config.msurveyplatContext;
// 初始化成果提交
export function initSubmitSqxx(){
    return httpService.post(msurveyplatContext + "/submit/initSqxx")
}
// 调用portal初始化信息接口
export function createTask(params){
    return httpService.get("/portal/index/createTask",params)
}
// 删除初始化信息
export function deleteSqxx(params){
    return httpService.post(msurveyplatContext + "/submit/delSqxx", params)
}
// 获取sqxx是否已提交
export function initCgtj(params){
    return httpService.post(msurveyplatContext + "/submit/initCgtj", params)
}
// 校验文件
export function checkZipFile(params){
    return httpService.postWithFile(msurveyplatContext + "/submit/checkZipFiles", params)
}
// 成果上传
export function uploadZipFile(params){
    return httpService.post(msurveyplatContext + "/submit/zipUpload", params)
}
// 文件是否覆盖
export function coverFile(params){
    return httpService.post(msurveyplatContext + "/submit/cfwjUpload", params)
}