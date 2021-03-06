import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//添加从业文员信息
export function addCyryxx(params) {
    return httpService.post(serviceOlContext + '/cyry/savecyry', params)
}
// 保存材料
export function saveUploadFile(url,params){
    return httpService.postWithFile(serviceOlContext + url, params)
}
// 保存备案材料
export function uploadfilestoxsbf(params){
    return httpService.postWithFile(serviceOlContext + '/fileoperation/uploadfilestoxsbf', params)
}
// 获取字典项
export function getDictInfo(params){
    return httpService.post(serviceOlContext + '/zdx/getzdxx ', params)
}
// 获取测量事项字典项
export function getClsxDictInfo(params){
    return httpService.post(serviceOlContext + '/zdx/getZdClsx ', params)
}
// 保存名录库入驻数据
export function saveMlkData(params){
    return httpService.post(serviceOlContext + '/mlk/savemlk', params)
}
// 上传名录库图片
export function uploadMlkImg(params){
    return httpService.postWithFile(serviceOlContext + '/mlk/uploadMlktp', params)
}
// 根据名录库id获取从业人员信息
export function getCyryList(params) {
    return httpService.post(serviceOlContext + '/mlk/getcyrybymlkid',params)
}
// 获取名录库需要上传的文件
// export function getUploadList(params){
//     return httpService.post(serviceOlContext + '/fileoperation/getsjcl', params)
// }
export function getUploadList(url,params){
    return httpService.post(serviceOlContext + url, params)
}
// 获取备案后合同信息
export function queryHtInfo(params) {
    return httpService.get(serviceOlContext + `/onlinedelegation/beforehtchange/${params.glsxid}/${params.ssmkid}`)
}
// 获取线上备份库数据
export function getXsBfData(params){
    return httpService.post(serviceOlContext + '/fileoperation/getsjclxsbf', params)
}
// 获取名录库状态
export function getMlkStatus(params) {
    return httpService.get(serviceOlContext + "/mlk/getmlkzt",params)
}
// 新增上传材料列
export function saveFjList(params){
    return httpService.post(serviceOlContext + "/fileoperation/saveSjclpz", params)
}
// 删除附件
export function deleteFold(params) {
    return httpService.post(serviceOlContext + "/fileoperation/deletefileJl", params)
}
// 删除上传附件
export function deleteFile(params) {
    return httpService.post(serviceOlContext + "/fileoperation/deletefile", params)
}
// 获取申请id
export function getMlkSqid(){
    return httpService.postDireact(serviceOlContext + "/cyry/getsqxxid", {})
}
// 取回申请
export function moveSq(params){
    return httpService.post(serviceOlContext + '/gldwDblb/moveDblbxxByzrryid', params)
}
// 名录库提交申请
export function submitSqxx(params) {
    return httpService.post(serviceOlContext + '/gldwDblb/tjsq', params)
}
// 初始化申请信息
export function initSqxx(params){
    return httpService.post(serviceOlContext + "/mlk/initsqxx", params)
}
// 获取从业人员信息详情
export function queryCyryxxDetail(params){
    return httpService.post(serviceOlContext + '/cyry/getcyrybyid', params)
}
// 初始化从业人员信息
export function initCyryxx(params) {
    return httpService.post(serviceOlContext + '/cyry/initcyry', params)
}
// 删除从业人员信息
export function deleteCyryxx(params){
    return httpService.post(serviceOlContext + '/cyry/delcyrybyid', params)
}
// 业务逻辑验证
export function yzYwlj(params){
    return httpService.post(serviceOlContext + '/ywljyz/excuteYwljyz', params)
}
// 非名录库机构查看
export function queryUnmlkByPage(params){
    return httpService.post(serviceOlContext + '/chdw/queryUnmlkByPage', params)
}
//名录库入驻带入登录时信息
export function getinitmlkparam(){
    return httpService.post(serviceOlContext + '/mlk/getinitmlkparam', {})
}
// 名录库列表-获取名录库list
export function queryMlkList(params){
    return httpService.post(serviceOlContext + '/mlk/mlkck', params)
}
// 管理用户-测绘单位查看
export function queryChdwList(params){
    return httpService.post(serviceOlContext + "/mlk/querychdwsbymultconditions", params)
}
// 获取是否需要审核办结的配置项
export function queryReviewConfig(){
    return httpService.post(serviceOlContext+ "/shxx/isentryaudit")
}
// 测绘单位-名录库注销判断
export function mlkDestroyCheck(params){
    return httpService.post(serviceOlContext + "/mlk/checkbeforelogoutmlk", params)
}
// 测绘单位-名录库注销
export function mlkDestroy(params){
    return httpService.post(serviceOlContext + "/mlk/logoutmlk", params)
}
// 测绘单位-名录库变更
export function alterMlkInfo(params){
    return httpService.post(serviceOlContext + "/mlk/altermlkinfo", params)
}
// 测绘单位-变更记录
export function queryEditRecordList(params){
    return httpService.post(serviceOlContext + "/log/xgrzcx", params)
}
// 测绘单位-变更记录详情
export function queryEditRecordDetail(params){
    return httpService.get(serviceOlContext + "/log/rzck/" + params.rzid)
}
