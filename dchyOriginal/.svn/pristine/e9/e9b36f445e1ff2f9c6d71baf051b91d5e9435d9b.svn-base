/***
 * 打开预览视频
 * 从项目处进入后
 * @param type    类型 0--预览视频 1---设置预置位
 * @param pNo     预置位编号  type=0 时可为空 type=1 时不可为空
 * @param camera  监控点的设备编码/监控点对象/监控点数组
 */
function startupVideo(type, camera, pNo,cameras) {
    var platConfig;
    var vcuList = [];
    var platformType;
    var server;
    var userName = null, userPwd = null, userDomain = null;
    if (typeof camera === "string") {
        var tmp = queryCameraByIndex(camera);
        if (tmp !== undefined) {
            var _a = {};
            if (tmp.hasOwnProperty("platform")) {
                platConfig = getCameraConfig(tmp.platform.toLowerCase());
            } else {
                platConfig = getCameraConfig("");
            }
            if(!platformType){
                platformType = platConfig.platType;
            }
            if(!server){
                server = platConfig.server;
            }
            if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh" || platConfig.platType === 'HW' || platConfig.platType === "DH")) {
                _a.VcuId = tmp.vcuId;
                _a.VcuName = tmp.regionName;
                userName = platConfig.userName.split("@")[0];
                userPwd = platConfig.password;
                userDomain = platConfig.userName.split("@")[1];
            }else if(platformType=="ivs"){
                openIvsVideo(tmp.name,tmp.indexCode,tmp.vcuId,platConfig,type,pNo);
                return;
            }
            _a.devicelist = [
                {
                    DeviceId: tmp.indexCode,
                    DeviceName: encodeURIComponent(tmp.name),
                    // DeviceName: tmp.name,
                    IndexCode: tmp.indexCode,
                    nodaname: encodeURIComponent(tmp.name),
                    // nodaname: tmp.name,
                    devid: tmp.id,
                    cmrip: tmp.ip,
                    cmrport: tmp.port,
                    cmruser: platConfig.userName,
                    cmrpass: platConfig.password,
                    serverip: platConfig.server,
                    serverport: platConfig.port
                }
            ];
            vcuList.push(_a);
        }
    } else if (typeof camera === "object") {
        if (camera instanceof Array) {
            $.each(camera, function (cameraIndex) {
                var item =camera[cameraIndex];
                var v = {};
                if (item.hasOwnProperty("platform")) {
                    platConfig = getCameraConfig(item.platform.toLowerCase());
                } else {
                    platConfig = getCameraConfig("");
                }
                if(!platformType){
                    platformType = platConfig.platType;
                }
                if(!server){
                    server = platConfig.server;
                }
                if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh" || platConfig.platType === 'HW' || platConfig.platType === "DH")) {
                    v.VcuId = item.vcuId;
                    v.VcuName = item.regionName;
                    userName = platConfig.userName.split("@")[0];
                    userPwd = platConfig.password;
                    userDomain = platConfig.userName.split("@")[1];
                }else if(platformType=="ivs"){
                    openIvsVideo(item.name,item.indexCode,item.vcuId,platConfig,type,pNo);
                    return;
                }
                v.devicelist = [{
                    DeviceId: item.indexCode,
                    DeviceName: encodeURIComponent(item.name),
                    // DeviceName: item.name,
                    IndexCode: item.indexCode,
                    nodaname: encodeURIComponent(item.name),
                    // nodaname: item.name,
                    devid: item.id,
                    cmrip: item.ip,
                    cmrport: item.port,
                    cmruser: platConfig.userName,
                    cmrpass: platConfig.password,
                    serverip: platConfig.server,
                    serverport: platConfig.port
                }];
                vcuList.push(v);
            });
        } else {
            var vcu = {};
            if (camera.hasOwnProperty("platform")) {
                platConfig = getCameraConfig(camera.platform.toLowerCase());
            } else {
                platConfig = getCameraConfig("");
            }
            if(!platformType){
                platformType = platConfig.platType;
            }

            if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh")) {
                vcu.VcuId = camera.vcuId;
                vcu.VcuName = camera.regionName;
                userName = platConfig.userName.split("@")[0];
                userPwd = platConfig.password;
                userDomain = platConfig.userName.split("@")[1];
            }else if(platformType=="ivs"){
                openIvsVideo(camera.name,camera.indexCode,camera.vcuId,platConfig,type,pNo);
                return;
            }
            vcu.devicelist = [
                {
                    DeviceId: camera.indexCode,
                    DeviceName: encodeURIComponent(camera.name),
                    IndexCode: camera.indexCode,
                    nodaname: encodeURIComponent(camera.name),
                    devid: camera.id,
                    cmrip: camera.ip,
                    cmrport: camera.port,
                    cmruser: platConfig.userName,
                    cmrpass: platConfig.password,
                    serverip: platConfig.server,
                    serverport: platConfig.port
                }
            ];
            vcuList.push(vcu);
        }
    }
    if (vcuList.length > 0) {
        switch (type) {
            case 0:
                previewCamera(pNo, vcuList, userName, userPwd, userDomain, platformType, server);
                break;
            case 1:
                preSetCamera(pNo, "预置位" + pNo, vcuList, userName, userPwd, userDomain, platformType,server);
                break;
        }
    } else
        console.log("打开视频错误.");
}

function queryCameraByIndex(indexcode) {
    var camera = undefined;
    $.ajax({
        url: root + "/video/query/indexcode/" + indexcode,
        async: false,
        success: function (r) {
            if (r) {
                camera = r;
            }
        }
    });
    return camera;
}

var root = "/" + window.location.pathname.split("/")[1];

var userId='0';

/**
 * ivs 查看、设置
 * @param name
 * @param cameraId
 * @param vcuId
 * @param setPtz
 * @param id
 * @param proid
 * @param platform
 * @param type
 * @param pNo
 */
function openIvsVideo(name,cameraId,vcuId,platform,type,pNo){
    var cmrvParam = {
        //ExeVersionCheck:window.location.origin+"/omp/static/bin/camaraViewer7.inf",
        RunPrefNum: "1",
        platfrom: "HWIVS",
        UserName: platform.userName,
        Password: platform.password,
        Port: platform.port,
        Server: platform.server,
        vculist: [
            {
                VcuId: cameraId + "#" + vcuId,
                VcuName: name
            }
        ]
    };

    if(type==0){
        cmrvParam.RunPrefNum = pNo;
    }else if(type==1){
        cmrvParam.SetPrefNum = pNo;
        cmrvParam.SetPrefName = "预设位"+pNo+Math.random();
    }

    postVideoCache(JSON.stringify(cmrvParam));
}