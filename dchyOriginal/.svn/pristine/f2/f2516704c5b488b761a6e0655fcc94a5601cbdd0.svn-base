import httpService from './request';
const serviceOlContext = config.serviceOlContext;
// 用户管理列表-查询
export function queryUserList(params) {
    return httpService.post(serviceOlContext + '/user/queryUserByUsername', params)
}

// 用户管理-修改密码
export function changeUserPassword(params) {
    return httpService.post(serviceOlContext + '/user/changePassword', params)
}

// 用户管理-启用禁用
export function changeUserState(params) {
    return httpService.post(serviceOlContext + '/user/changeUserState', params)
}

// 用户管理-获取所有角色
export function queryRoleList() {
    return httpService.get(serviceOlContext + '/user/queryAllRole')
}

// 用户管理-获取所有角色
export function postRole(params) {
    return httpService.post(serviceOlContext + '/user/distributionRoleAuthority', params)
}

// 路由验证
export function yzRoute(params){
    return httpService.get(serviceOlContext + '/view' + params.url)
}

export function reset(params){
    return httpService.get(serviceOlContext + '/auth/reset')
}