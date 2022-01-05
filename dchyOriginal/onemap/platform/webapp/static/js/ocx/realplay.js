/**
 * 华为视频播放控制 js
 * Created by alex on 2017/11/29.
 */
var ocx = undefined;

/**
 * 替换形如 {{name}} 占位符
 * @return {String}
 */
String.prototype.tpl = function () {
    if (arguments.length === 0) return this;
    var obj = arguments[0];
    var s = this;
    for (var key in obj) {
        s = s.replace(new RegExp("\\{\\{" + key + "\\}\\}", "g"), obj[key]);
    }
    return s;
};

/**
 * pre check
 */
function preCheck() {
    if (ocx === undefined) {
        throw new Error('ocx cannot be null!');
    }
}

/**
 * 解析 xml
 * 返回一个结果对象
 * callRet: 调用结果
 * methodRet: 方法执行结果
 * dataRet: 方法返回信息
 * {
 *    callRet: 'CALL_RESULT_SUCCESS',
 *    methodRet: 'NVS_RET_SUCCESS',
 *    dataRet: {...}
 * }
 * @param xml
 * @return {*|jQuery}
 */
function parseXmlRet(xml) {
    var xmlObj = $.parseXML(xml);
    var ret = {};
    ret.callRet = $(xmlObj).find('CALL_RESULT').attr('CallResult');
    ret.methodRet = $(xmlObj).find('METHOD_RESULT').attr('MethodResult');
    var dataObj = $(xmlObj).find('METHOD_RETURN');
    if (dataObj !== undefined && dataObj[0] !== undefined) {
        var dataRet = {};
        $(dataObj[0].attributes).each(function (i, val) {
            var key = val.nodeName;
            dataRet[key] = val.value;
        });
        ret.dataRet = dataRet;
    }
    console.log("method result: %s", xml);
    return ret;
}

/**
 * 登出平台
 * @param username
 * @param password
 * @param domain
 * @param server
 */
function loginOut() {
    preCheck();
    var methodStr = '<CALL_METHOD><METHOD Name="USER_LOGOUT"/></CALL_METHOD>';
    var retXml = ocx.callMethod(methodStr);
    console.log("loginOut---"+retXml);
    return parseXmlRet(retXml);
}


/**
 * 登录平台
 * @param username
 * @param password
 * @param domain
 * @param server
 */
function login(username, password, domain, server) {
    preCheck();
    loginOut();
    var methodStr = '<CALL_METHOD><METHOD Name="USER_LOGIN"/><PARAMS UserName="{{user}}" Domain="{{domain}}" Password="{{password}}" Server="{{server}}" /></CALL_METHOD>';
    var retXml = ocx.callMethod(methodStr.tpl({
        user: username,
        password: password,
        domain: domain,
        server: server
    }));
    console.log("login---"+retXml);
    return parseXmlRet(retXml);
}

/**
 * 退出平台
 * @return {*|jQuery}
 */
function logout() {
    preCheck();
    var methodStr = '<CALL_METHOD><METHOD Name="USER_LOGOUT"/></CALL_METHOD>';
    var retXml = ocx.callMethod(methodStr);
    return parseXmlRet(retXml);
}

/**
 *  设置窗格布局
 * @param grids  窗格数 1/4/6/8/9/16
 */
function setLayout(grids) {
    var methodStr = '<CALL_METHOD><METHOD Name="SET_GRID_LAYOUT"/><PARAMS DivideMode="{{divideMode}}" MarginWidth="0" /></CALL_METHOD>';
    var retXml = ocx.callMethod(methodStr.tpl({
        divideMode: getDivideMode(grids)
    }));
    return parseXmlRet(retXml);
}

/**
 * get divide mode
 * @param n
 * @return {string}
 */
function getDivideMode(n) {
    var mode = 'FOUR_SCREEN';
    switch (n) {
        case 1:
            mode = 'SINGLE_SCREEN';
            break;
        case 4:
            mode = 'FOUR_SCREEN';
            break;
        case 6:
            mode = 'SIX_SCREEN';
            break;
        case 8:
            mode = 'EIGHT_SCREEN';
            break;
        case 9:
            mode = 'NINE_SCREEN：';
            break;
        case 16:
            mode = 'SIXTEEN_SCREEN';
            break;
        default:
            break;
    }
    return mode;
}

/**
 * 开始实时浏览视频
 * @param vcuId
 * @param devId
 * @param wndId  窗格id 默认1
 * @return {*|jQuery}
 */
function startRealPlay(vcuId, devId, wndId) {
    wndId = wndId === undefined ? 1 : wndId;
    var methodStr = '<CALL_METHOD><METHOD Name="START_REAL_VIDEO"/><PARAMS VcuId="{{vcuId}}" DeviceId="{{devId}}" StreamType="MAIN_STREAM" ProtocolType="TCP" LinkMode="TRANSPORT" PlayType="LOCAL_PLAY" WndId="{{wndId}}" /></CALL_METHOD>';
    var retXml = ocx.callMethod(methodStr.tpl({
        vcuId: vcuId,
        devId: devId,
        wndId: wndId
    }));
    console.log("uuuuu-"+retXml);
    return parseXmlRet(retXml);
}

/**
 * 停止 视频播放
 * @param businessId  业务id 上一个方法返回的信息
 */
function stopRealPlay(businessId) {
    var methodStr = '<CALL_METHOD><METHOD Name="STOP_VIDEO" /><PARAMS BusinessId="{{businessId}}"></PARAMS></CALL_METHOD>';
    return parseXmlRet(ocx.callMethod(methodStr.tpl({
        businessId: businessId
    })));
}

/**
 *
 * @param userId
 * @param vcuId
 * @param deviceId
 * @param command   （PTZ_STOP|PTZ_UP|PTZ_DOWN|PTZ_LEFT|PTZ_UP_LEFT|PTZ_DOWN_LEFT|PTZ_RIGHT|PTZ_UP_RIGHT|PTZ_DOWN_RIGHT|PTZ_LENS_ZOOM_OUT|PTZ_LENS_ZOOM_IN）
 * @param ptzParam1  SERIES_MODE(连续)|STEP_MODE（点动）
 * @param ptzParam2  1-10（速度|步长）
 * @returns {*|jQuery}
 */
function ptzControl(userId, vcuId, deviceId, command, ptzParam1, ptzParam2) {
    var methodStr = '<CALL_METHOD><METHOD Name="PTZ_CTRL" /><PARAMS UserId="{{UserId}}" VcuId="{{vcuId}}" DeviceId="{{deviceId}}" Command="{{command}}" PtzParam1="{{ptzParam1}}" PtzParam2="{{ptzParam2}}"></PARAMS></CALL_METHOD>';
    return parseXmlRet(ocx.callMethod(methodStr.tpl({
        userId: userId,
        vcuId: vcuId,
        deviceId: deviceId,
        command: command,
        ptzParam1: ptzParam1,
        ptzParam2: ptzParam2
    })));
}

function resize() {
    ocx.Resize();
}
