import httpService from "./request";
const msurveyplatContext = config.msurveyplatContext;
// 附件材料配置-获取收件材料配置
export function queryConfigFj(params){
    return httpService.get(msurveyplatContext + "/configure/sjcl", params)
}
// 附件材料配置-新增和修改数据
export function editConfig(params){
    return httpService.postWithoutHead(msurveyplatContext + "/configure/sjcl", params)
}
// 附件材料配置-删除数据
export function deleteConfig(params) {
    return httpService.deleteWithBody(msurveyplatContext + "/configure/sjcl", params)
}
// 附件材料配置-查询测量事项接口
export function queryClsxList() {
    return httpService.get(msurveyplatContext + "/configure/sjcl/clsx")
}
// 附件材料配置-查询未创建的所属模块
export function querySsmkList() {
    return httpService.get(msurveyplatContext + "/configure/sjcl/ssmk")
}


// 检查规则配置
export function queryCgjcgz(){
    return httpService.get(msurveyplatContext + "/configure/getCgjcgz")
}

// 成果目录配置-获取目录树
export function queryCgmlTree(){
    return httpService.get(msurveyplatContext + "/configure/cgml/tree")
}
// 成果目录配置-获取目录树
export function editCgmlTree(params){
    return httpService.postWithoutHead(msurveyplatContext + "/configure/cgml/tree/gzjc", params)
}