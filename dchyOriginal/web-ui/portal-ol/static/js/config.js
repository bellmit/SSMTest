var config = {
    msurveyplatContext: '/msurveyplat-promanage',
    serviceOlContext: '/msurveyplat-serviceol',
    portalOlContext: '/portal-ol',
    placeSevice: '常州市建设项目',
    placePinyin: 'CHANGZHOU CITY',
    showRegister: true, //是否可注册
    userErrorList: [  //用户报错信息
        {
            code: "2013",
            error: "该模块仅针对企业用户开放"
        },
        {
            code: "2003",
            error: "该用户已被禁用"
        },
        {
            code: "2070",
            error: "系统异常，未获取到用户信息"
        }
    ]
}
window.config = config;