import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// 获取建设单位评价列表
export function jsdwEvaludateList(params){
    return httpService.post(serviceOlContext + "/jsdw/getxmpjlist", params)
}
// 建设单位评价
export function jsdwEvaludate(params){
    return httpService.post(serviceOlContext + "/jsdw/getpjxxbyid", params)
}
// 检查建设单位数据状态
export function checkStatus(params){
    return httpService.post(serviceOlContext + "/jsdw/checkchxmisfinish", params)
}
// 建设单位获取评价详情
export function queryJsdwEvaluateDetail(params) {
    return httpService.post(serviceOlContext + "/jsdw/getpjxxbyid", params)
}
// 管理单位获取测绘单位列表
export function queryChdwList(params){
    return httpService.post(serviceOlContext + "/gldw/getchdwkplist", params)
}
// 管理单位移出名录库
export function removeMlk(params){
    return httpService.post(serviceOlContext + "/gldw/removemlkbyid", params)
}
// 移除之前判断是否有在建的工程
export function Isconstructproject(params) {
    return httpService.post(serviceOlContext + "/gldw/isconstructproject", params)
}
// 管理人员评价
export function glryEvaludate(params){
    return httpService.post(serviceOlContext + "/gldw/savechdwevalbyid", params)
}
// 管理人员诚信记录
export function cxEvaluation(params){
    return httpService.post(serviceOlContext + "/gldw/savechdwcxjl", params)
}
// 获取评价年份
export function getKpnd(params){
    return httpService.get(serviceOlContext + `/gldw/getndkp/${params.mlkid}`)
}
// 获取所有的年度考评
export function getNdkpList(){
    return httpService.get(serviceOlContext + "/gldw/getNdkp")
}
// 获取测绘单位名录库评价等级
export function queryChdwPjdj(){
    return httpService.post(serviceOlContext + "/chdw/getxydj")
}
// 测绘人员-查询建设单位评价
export function chdwQueryJsEvaluate(params){
    return httpService.post(serviceOlContext + '/chdw/getchdwpjxx', params)
}
// 测绘单位查询管理单位评价
export function chdwQueryGlEvaluate(params){
    return httpService.post(serviceOlContext + '/chdw/getchdwkpinfo', params)
}
//评价管理-（管理人员）管理单位考评记录
export function getgldwTableList(params) {
    return httpService.post(serviceOlContext + '/gldw/getgldwkpbymlkid', params)
}
// 评价管理-（管理人员）查询建设单位考评记录
export function getJsdwTableList(params) {
    return httpService.post(serviceOlContext + '/gldw/getjsdwevalbyid', params)
}

// 评价管理-考评结果字典表
export function getKpDictInfo(params) {
    return httpService.postDireact(serviceOlContext + '/chdw/getkpjgbyzd', params)
}
// 测绘人员角色-管理单位抽查结果
export function evaluationCheckResults(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/evaluationCheckResults', params)
}
// 管理单位抽查结果查看详情
export function evaluationCheckResultsView(params) {
    return httpService.post(serviceOlContext + '/jcsjsq/evaluationCheckResultsView', params)
}
// 管理人员角色-管理单位抽查结果
export function gldwEvaluationCheckResults(params) {
    return httpService.post(serviceOlContext + '/gldw/evaluationCheckResults', params)
}