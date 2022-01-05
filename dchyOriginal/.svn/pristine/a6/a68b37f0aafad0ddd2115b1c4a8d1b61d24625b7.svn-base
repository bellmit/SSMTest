/**
 * 视频预览
 * Created by yingxiufeng on 2016/3/25.
 */
if (typeof $j === "undefined")
    var $j = $;
/**
 * 预览摄像头
 * @param presetNum   预置位编号
 * @param param       视频参数
 * @param userName   千里眼平台用户名
 * @param userPwd    千里眼平台用户密码
 * @param userDomain  千里眼平台域
 * @param platformType  设备平台
 * @returns {string}
 */
function previewCamera(presetNum, param, userName, userPwd, userDomain, platformType, pServer) {
    var viewParam = {};
    if (presetNum != undefined && presetNum != null)
        viewParam.RunPrefNum = presetNum.toString();
    viewParam.platfrom = platformType ? platformType.toUpperCase() : platform.toUpperCase();
    switch (viewParam.platfrom) {
        case 'HW':
            viewParam.UserName = userName;
            viewParam.Domain = userDomain;
            viewParam.Password = userPwd;
            viewParam.Server = pServer;
            break;
        case 'DH':
            viewParam.UserName = userName;
            viewParam.Password = userPwd;
            viewParam.Server = pServer;
            break;
        case 'HK':
            break;
    }
    viewParam.vculist = param;
    var viewStr = JSON.stringify(viewParam);

//    viewStr="{'SetPrefNum':'1','RunPrefNum':'1','platfrom':'HK','vculist':[{'devicelist':[{'IndexCode':'00000000001310011935','nodaname':'test3','devid':'000000000001310935974','cmrip':'192.168.50.252','cmrport':'8000','cmruser':'admin','cmrpass':'qwer1234','serverip':'192.168.90.9','serverport':'7302'}]}]}";
//    viewStr = '{'RunPrefNum':'1',"platfrom":"HW","UserName":"xbgtqly","Domain":"czgt.cz.js","Password":"Xb2015","Server":"112.4.18.89","vculist":[{"VcuId":"025200255420000","VcuName":"孟城山江1","devicelist":[{"DeviceId":"025200255420101","DeviceName":"孟城山江1"}]}]}';
//    viewStr="{'platfrom':'DH','UserName':'system','Password':'123456','Server':'192.168.90.234','vculist':[{'VcuId':'001','VcuName':'001','devicelist':[{'DeviceId':'1000000$1$0$0','DeviceName':'test3'}]}]}";

    viewStr = viewStr.replace(/\"/g, '\'');
    console.log(viewStr);
    postVideoLog(viewParam.platfrom, param, 0);
    postVideoCache(viewStr);
}

/**
 * 设置摄像头的预置位
 * @param presetNum  预置位编号
 * @param presetName 预置位名称
 * @param param       视频参数
 * @param userName   千里眼平台用户名
 * @param userPwd    千里眼平台用户密码
 * @param userDomain  千里眼平台域
 * @param platformType  设备平台
 * @returns {string}
 */
function preSetCamera(presetNum, presetName, param, userName, userPwd, userDomain, platformType, pServer) {
    var viewParam = {};
    viewParam.SetPrefNum = presetNum.toString();
    viewParam.SetPrefName = presetName;
    if(typeof(platform) == "undefined"){
        platform = "ivs";
    }
    viewParam.platfrom = platformType ? platformType.toUpperCase() : platform.toUpperCase();
    switch (viewParam.platfrom) {
        case 'HW':
            viewParam.UserName = userName;
            viewParam.Domain = userDomain;
            viewParam.Password = userPwd;
            viewParam.Server = pServer;
            break;
        case 'DH':
            viewParam.UserName = userName;
            viewParam.Password = userPwd;
            viewParam.Server = pServer;
            break;
        case 'HK':
            viewParam.UserName = userName;
            viewParam.Domain = userDomain;
            viewParam.Password = userPwd;
            viewParam.Server = pServer;
            break;
    }
    viewParam.vculist = param;
    var presetStr = JSON.stringify(viewParam);
//    viewStr = '{"SetPrefNum":"1","SetPrefName":"asdasdasd","platfrom":"HW","UserName":"xbgtqly","Domain":"czgt.cz.js","Password":"Xb2015","Server":"112.4.18.89","vculist":[{"VcuId":"025200255420000","VcuName":"孟城山江1","devicelist":[{"DeviceId":"025200255420101","DeviceName":"孟城山江1"}]}]}';
    presetStr = presetStr.replace(/\"/g, '\'');
    console.log(presetStr);
    postVideoLog(viewParam.platfrom, param, 1);
    postVideoCache(presetStr);
}

/**
 * 生成日志信息
 * @param pf
 * @param list
 */
function parseToLog(pf, list, ot) {
    var logs = [];
    for (var i = 0, len = list.length; i < len; i++) {
        var vcu = list[i];
        var devices = vcu.devicelist;
        $j.each(devices, function (idx, item) {
            var param = {};
            param.platform = pf;
            param.cameraName = decodeURIComponent(item.DeviceName);
            param.indexCode = item.IndexCode;
            param.content = ot === 0 ? '查看' : '预设位设置';
            param.regionName = vcu.VcuName;
            logs.push(param);
        });
    }
    return logs;
}
/**
 * 保存操作日志
 * @param indexCode   监控点编码
 * @param cameraName  监控点名称
 * @param content     操作内容
 */
function postVideoLog(pf, list, type) {
    var data = parseToLog(pf, list, type);
    //兼容连云港慧眼守土
    if(window.otherLoginName){
        for(var i =0;i<data.length;i++){
            var item = data[i];
            item.loginUserName=window.otherLoginName;
        }
    }
    $j.each(data, function (idx, item) {
        $j.ajax({
            url: root + '/video/log/save',
            data: item
        }).done(function (r) {
            if (r.success === false)
                console.error('保存日志信息失败:' + r.msg);
        });
    });
}

/**
 * set  cache
 * @param param
 */
function postVideoCache(param) {
    $j.ajax({
        url: root + '/video/cache/set/' + userId.concat("token" + randTokenSuffix()),
        method: 'post',
        data: {data: param}
    }).done(function (r) {
        if (r.success === false)
            console.error('set cache error: ' + r.msg);
        else {
            console.log('param url: %s', window.location.origin + '/omp/video/cache/get?token=' + r);
            var url = 'CMRV://|' + window.location.origin + '/omp/video/cache/get?token=' + r + '|';
            window.location = url;
            if(window.closeCameraWindow){
                window.closeCameraWindow();
            }
        }
    });
}

/**
 * 生成tokne的6位数字后缀
 */
function randTokenSuffix() {
    var random = "";
    for (var i = 0; i < 6; i++) {
        random += Math.floor(Math.random() * 10);
    }
    return random;
}

/**
 * 检测控件是否注册
 * @param url
 */
function checkRegistry(url) {
    require(["js/map/utils/RegistryDetectUtils"], function (RegistryDetectUtils) {
        RegistryDetectUtils.detect(url);
    })

}


/**
 * 获取平台配置信息
 * @param platform
 */
function getCameraConfig(platform) {
    var config;
    $j.ajax({
        url: root + "/video/fetch/plat",
        data: {platName: platform},
        async: false,
        success: function (r) {
            config = r;
        }
    });
    return config;
}