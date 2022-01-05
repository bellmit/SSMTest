import httpService from './request';
const serviceOlContext = config.serviceOlContext;
//我的已办-查询
export function completedQuery(params) {
    return httpService.post(serviceOlContext + '/gldwYblb/queryYblbList', params)
}
//我的已办-进度查看
export function getProcessList(params) {
    return httpService.get(serviceOlContext + '/shxxcx/shlb/'+params.sqxxid)
}





