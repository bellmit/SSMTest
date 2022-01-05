import httpService from './request';
const msurveyplatContext = config.msurveyplatContext;
// 获取用户信息
export function getUserInfo(){
    return httpService.get(msurveyplatContext + "/index/getUser")
}

export function getSystemByUser(){
  return httpService.get(msurveyplatContext + "/index/getSystemByUser")
}
