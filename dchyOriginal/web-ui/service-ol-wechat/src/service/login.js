import httpService from './request';
const portalOlContext = config.portalOlContext;
const serviceOlContext = config.serviceOlContext;
//获取登录cookie
export function getLoginCookie() {
    return httpService.post(portalOlContext + '/loginModel/getUserInfoFromCookie', {})
}
// 注册测绘单位
export function registerUser(params) {
    return httpService.postWithFile(portalOlContext + '/yhdwZc/registeredYhdw', params)
}
// 获取验证码
export function queryYzm(params) {
    return httpService.post(portalOlContext + "/yhdwZc/smsModel/sendMsg", params)
}
// 登录
export function login(params){
    return httpService.postWithFile("/cas/login", params)
}
// 验证手机号码
export function yzSjhm(params){
    return httpService.post(portalOlContext + '/yhdwZc/yzmIsVaild', params)
}
// 重新设置密码
export function resetPassword(params){
    return httpService.post(portalOlContext + '/yhdwZc/resetPassword', params)
}
// 登录
export function loginWithYzm(params){
    return httpService.postWithoutHead(portalOlContext + '/login/yzmLogin?yzm=' + params.yzm, params)
}
// 退出登录
export function logoutServiceOl(){
    return httpService.get(serviceOlContext + "/logout")
}
// 退出登录
export function logoutPortalOl(){
    return httpService.get(portalOlContext + "/ptlogout/allservice")
}

// 获取是否开启验证码的配置
export function querySfqyYzm(){
    return httpService.get(portalOlContext + '/yhdwZc/sfqyyzm')
}

// 获取认证用户信息
export function queryCertificateInfo(params){
    return httpService.get(portalOlContext + '/login/jszwfwAppgryhsqxx?ticket=' + params.ticket)
}
// 用户认证
export function qyrzRegister(params){
    return httpService.post(portalOlContext + '/yhdwZc/qyrzRegister?token='+params.token, params)
}
// 获取名录库需要上传的文件
export function getUploadList(params,sjclUrl='/fileoperation/getsjcl'){
    return httpService.post(serviceOlContext + sjclUrl, params)
}
// 保存材料
export function saveUploadFile(params,url='/fileoperation/uploadfiles'){
    return httpService.postWithFile(serviceOlContext + url, params)
}
// 删除上传附件
export function deleteFile(params) {
    return httpService.post(serviceOlContext + "/fileoperation/deletefile", params)
}
// 授权
export function gryhsqRegister(params,ticket) {
    return httpService.postWithFile(portalOlContext + "/yhdwZc/gryhsqAppRegister?ticket="+ticket , params)
}