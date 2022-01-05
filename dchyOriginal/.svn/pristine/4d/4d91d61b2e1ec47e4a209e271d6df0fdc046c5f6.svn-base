import httpService from './request';
const msurveyplatContext = config.msurveyplatContext;
// 登录
export function loginWithYzm(params){
    return httpService.postWithFile(msurveyplatContext + '/validatelogon', params)
}
// 退出登录
export function logOut(){
    return httpService.get("/portal/logout")
}