import httpService from "./request";
const serviceOlContext = config.serviceOlContext;
// 附件材料配置-获取收件材料配置
export function queryConfigFj(params){
    return httpService.get(serviceOlContext + "/configure/sjcl", params)
}
// 附件材料配置-新增和修改数据
export function editConfig(params){
    return httpService.postWithoutHead(serviceOlContext + "/configure/sjcl", params)
}
// 附件材料配置-删除数据
export function deleteConfig(params) {
    return httpService.deleteWithBody(serviceOlContext + "/configure/sjcl", params)
}
// 附件材料配置-查询测量事项接口
export function queryClsxList() {
    return httpService.get(serviceOlContext + "/configure/sjcl/clsx")
}
// 附件材料配置-查询未创建的所属模块
export function querySsmkList() {
    return httpService.get(serviceOlContext + "/configure/sjcl/ssmk")
}