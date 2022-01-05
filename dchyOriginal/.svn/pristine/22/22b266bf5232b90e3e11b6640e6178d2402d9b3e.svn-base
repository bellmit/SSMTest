import httpService from "./request";
const msurveyplatContext = config.msurveyplatContext;
// 项目统计-测绘单位
export function queryChdwList(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getchdwlist", params)
}
// 项目统计-各阶段项目数量-项目占比统计
export function queryXmslData(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getxmsl", params)
}
// 项目统计-项目备案记录
export function queryXmbajlData(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getXmbajllist", params)
}


// 项目分析-各区县项目状态统计
export function queryQxxmztData(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getxmslbyqx", params)
}
// 项目分析-各区县项目委托方式统计
export function queryQxxmwtfsData(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getxmwtfsbyqx", params)
}

// 业务分析
export function queryYwfxData(params){
    return httpService.postWithoutHead(msurveyplatContext + "/tjfx/getxmslbytotal", params)
}