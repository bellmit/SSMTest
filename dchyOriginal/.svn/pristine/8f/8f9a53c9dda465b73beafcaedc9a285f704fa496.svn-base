import httpService from "./request";
const msurveyplatContext = config.msurveyplatContext;
// 我的待办列表
export function queryReviewList(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/queryDbSqxxList", params)
}
// 基础数据申请详情查看
export function queryDataApplyDetail(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/applicationInfoView", params)
}
// 基础数据申请-初始化流程信息
export function initSqxx(){
    return httpService.get(msurveyplatContext + "/jcsjsq/initSqxx")
}
// 基础数据交付日志
export function resultsDeliveryLogList(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/resultsDeliveryLogList", params)
}
// 基础数据审核
export function applicationInfoView(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/saveCheckOpinion", params)
}
// 基础数据下载-交付成果
export function resultsDelivery(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/resultsDelivery", params)
}
// 基础数据下载-初始化生成基础数据申请数据
export function initBasicDataApplication(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/initBasicDataApplication", params)
}
// 基础数据下载-进度查看
export function queryProcessInfo(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/getProcessInfoBySlbh", params)
}

// 我的已办列表
export function queryCompletedList(params){
    return httpService.post(msurveyplatContext + "/jcsjsq/queryYbSqxxList", params)
}