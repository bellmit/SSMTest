/**
 * @file 一张图export模块 供业务系统调用
 * <p>
 *     用法:
 *
 OneMap({
            loginUser: '',
            ompUrl: '/omp',
            mapTpl: 'YZT_DEFAULT',
            platform: 'hk',
            userName: undefined,
            userPwd: undefined,
            server: undefined,
            port: undefined,
        , camerasCallback:function (data) {
        omp.preview(data[0].indexCode);
    }
    })
 ;
 * </p>
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/2/29 11:01
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
;
!function (window, $, undefined) {
    if (!window.console) {
        var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

        window.console = {};
        for (var i = 0; i < names.length; ++i)
            window.console[names[i]] = function () {
            }
    }
    if (window["context"] == undefined) {
        if (!window.location.origin) {
            window.location.origin = window.location.protocol + "//" + window.location.hostname +
                (window.location.port ? ':' + window.location.port : '');
        }
        window["context"] = location.origin + "/V6.0";
    }

    var onemap = function () {
        this.init.apply(this, arguments);
    };
    var cameraArray = [];
    onemap.prototype = {
        /**
         * 初始化
         */
        init: function () {
            var args = Array.prototype.slice.call(arguments);
            if (args && args.length > 0) {
                var config = args[0];
                var getType = Object.prototype.toString;
                if (config && getType.call(config) == "[object Object]") {
                    this.config = $.extend({
                        loginUser: '',
                        ompUrl: '/omp',
                        mapTpl: 'YZT_DEFAULT',
                        platform: 'hk',
                        userName: undefined,
                        userPwd: undefined,
                        server: undefined,
                        port: undefined,
                        poi: undefined,
                        camerasCallback: function () {
                        },
                        poiCallback: function () {
                        },
                        appConfigCallback: function () {
                        }
                    }, config);
                    // 获取视频多平台配置
                    this.getVideoPlats();
                    this.getAppProperty();
                    if (this.config.poi === undefined)
                        this.getCameras();
                    else
                        this.getPoiCameras(this.config);
                }
            }
        },

        /**
         * 获取 videoPlats.yml
         */
        getVideoPlats: function () {
            var c = this.config;
            $.ajax({
                url: this.config.ompUrl + "/config/app/video",
                dataType: 'json',
                success: function (ret) {
                    if (ret.hasOwnProperty('success')) {
                        verbose(ret.msg, 'error');
                    } else {
                        $.extend(c, {videoPlats: ret});
                    }
                }
            });
        },

        /**
         * 获取系统视频相关配置项
         */
        getAppProperty: function () {
            var c = this.config;
            var arr = ["userName", "userPwd", "server", "port", "platform"];
            var tmp = {};
            for (var i in arr) {
                var name = arr[i];
                $.ajax({
                    url: this.config.ompUrl + "/config/app/property?prop=" + "video." + name,
                    async: false,
                    dataType: 'json',
                    success: function (val) {
                        tmp[name] = val.result;
                    }
                });
            }
            this.config = $.extend(c, tmp);
        },
        /**
         * 获取地图模板配置
         */
        getAppConfig: function () {
            var m = this;
            $.ajax({
                url: this.config.ompUrl + "/map/" + this.config.mapTpl + "/config"
            }).done(function (evt) {
                if (evt.hasOwnProperty("success") && evt.success == false) {
                    verbose(evt.msg, 'error');
                } else {
                    this.appConfig = evt;
                    m.config.appConfigCallback(this.appConfig);
                    verbose('get app config successfully!');
                }
            });
        },
        /**
         * 获取所有的监控点
         */
        getCameras: function () {
            var m = this;
            $.ajax({
                url: this.config.ompUrl + '/transitService/video/rest/all'
            }).done(function (evt) {
                if (typeof(evt) != "object") {
                    evt = JSON.parse(evt);
                }
                if (evt.hasOwnProperty("success") && evt.success === false)
                    verbose(evt.msg, 'error');
                else {
                    this.cameras = evt;
                    cameraArray = evt;
                    m.config.camerasCallback(this.cameras);
                    verbose('get cameras successfully!');
                }
            });
        },
        /**
         * 获取poi视频点
         * 需要返回携带距离 传递参数：
         * {containDis: true}
         */
        getPoiCameras: function () {
            var m = this;
            var poi = m.config.poi;
            var conf = arguments[0];
            $.ajax({
                url: this.config.ompUrl + '/transitService/video/rest/poi',
                data: {geometry: poi, bufferSize: 2000, containDis: conf.containDis || false},
                method: 'post'
            }).done(function (evt) {
                if (typeof(evt) != "object") {
                    evt = JSON.parse(evt);
                }
                if (evt.hasOwnProperty("success") && evt.success === false)
                    verbose(evt.msg, 'error');
                else {
                    this.cameras = evt;
                    cameraArray = evt;
                    m.config.poiCallback(this.cameras);
                    verbose('get poi cameras successfully!');
                }
            });
        },
        /***
         * 设置摄像头的ptz
         * @param indexCode
         * @param geoPnt geojson格式的点坐标
         */
        setPtzByGeo: function (indexCode, geoPnt) {
            if (indexCode == null || indexCode === undefined)return;
            if (geoPnt != null && geoPnt != undefined) {
                var inSR = "2364";
                $.ajax({
                    url: this.config.ompUrl + '/video/camera/ptz',
                    data: {indexCode: indexCode, inSR: inSR, point: geoPnt, platform: this.config.platform},
                    success: function (r) {
                        if (r.hasOwnProperty("success") && r.success == false) {
                            console.warn(r.msg);
                        }
                    }
                });
            }
        },
        /**
         * 预览视频
         */
        preview: function () {
            var data = arguments[0];
            if (data === undefined) {
                throw new Error('You should get a camera first.');
            }
            var indexCode = typeof data === 'string' ? data : data.indexCode;
            var camera = query(indexCode, this.config, cameraArray);
            if (camera !== undefined && data.hasOwnProperty("preset")) {
               $.extend(camera, {RunPrefNum: data.preset});
            }
            var viewStr = JSON.stringify(camera).replace(/\"/g, '\'');
            verbose(viewStr);
            var url = "";
            var tmpUrl = this.config.ompUrl;
            var arr = tmpUrl.indexOf("http");
            if (arr === 0) {
                url = tmpUrl;
            } else {
                url = window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '') + tmpUrl;
            }
            var tokenStr = this.config.loginUser.concat("token" + randToken());
            $.ajax({
                url: this.config.ompUrl + '/video/cache/set/' + tokenStr,
                method: 'post',
                data: {data: viewStr}
            }).done(function (r) {
                if (r.success === false)
                    verbose('set cache error:' + r.msg, 'error');
                else
                    window.location = 'CMRV://|' + url + '/video/cache/get?token=' + r + '|';
            });
            return tokenStr;
        }
    };

    /**
     * debug/log/info/error sth verbose
     */
    function verbose() {
        var args = Array.prototype.slice.call(arguments);
        if (args && args.length > 0) {
            if (args.length > 1) {
                switch (args[1]) {
                    case 'error':
                        console.error(args[0]);
                        break;
                    case 'warn':
                        console.warn(args[0]);
                        break;
                    case 'info':
                        console.info(args[0]);
                        break;
                    case 'log':
                        console.log(args[0]);
                        break;
                    default:
                        verbose(args[0]);
                        break;
                }
            } else
                console.log(arguments[0]);
        }
    }

    /**
     * 数组中取对象
     * @param data
     * @param value
     * @param array
     * @returns {string}
     */
    function query(data, config, array) {
        var target, device, plat;
        if (typeof data === 'string') {
            var arr = $.grep(array, function (item) {
                return item.indexCode === data;
            });
            if (arr.length === 0) {
                throw new Error('[' + data + '] not found in array of cameras.');
            } else {
                device = arr[0];
            }
        } else {
            device = data;
        }
        if (device.hasOwnProperty('platform')) {
            // 若设备元数据携带参数 platform
            var platName = device.platform;
            var tmpArr = $.grep(config.videoPlats, function (p) {
                return p.name === platName;
            });
            if (tmpArr.length === 0) {
                verbose('platform [' + platName + '] not found in videoPlats.', 'warn');
            } else {
                plat = tmpArr[0];
            }
        }

        if (plat === undefined) {
            plat = {
                platType: config.platform,
                userName: config.userName,
                password: config.userPwd,
                port: config.port
            };
        }
        var vculist = {};
        if(plat.platType === 'hk'){
            vculist=[{
                devicelist: [{
                    DeviceId: device.indexCode,
                    DeviceName: encodeURIComponent(device.name),
                    IndexCode: device.indexCode,
                    nodaname: encodeURIComponent(device.name),
                    devid:  device.indexCode,
                    cmrip: device.ip,
                    cmrport: device.port,
                    cmruser: plat.userName,
                    cmrpass: plat.password,
                    serverip: plat.server,
                    serverport: plat.port
                }]
            }]
        }else{
            vculist ={
                devicelist: [{
                    DeviceId: device.indexCode,
                    DeviceName: encodeURIComponent(device.name),
                    IndexCode: device.indexCode,
                    nodaname: encodeURIComponent(device.name)
                }]
            }
        }

        if (plat.platType === 'hw') {
            $.extend(vculist, {
                VcuId: device.vcuId,
                VcuName: device.regionName
            });

            vculist = [vculist];
        }

        if(plat.platType==="ivs"){
            var cmrvParam = {
                ExeVersionCheck:window.location+"/omp/static/bin/camaraViewer7.inf",
                RunPrefNum: "1",
                platfrom: "HWIVS",
                UserName: plat.userName,
                Password: plat.password,
                Port: plat.port,
                Server: plat.server,
                vculist: [
                    {
                        VcuId: device.indexCode + "#" + device.vcuId,
                        VcuName: device.name
                    }
                ]
            };

            return cmrvParam;
        }


        target = {
            operid: "1",
            platfrom: plat.platType.toUpperCase(),
            vculist: vculist
        };
        switch (plat.platType) {
            case 'hw':
                target.UserName = plat.userName.split("@")[0];
                target.Domain = plat.userName.split("@")[1];
                target.Password = plat.password;
                target.Server = plat.server;
                break;
            case 'dh':
                target.UserName = plat.userName;
                target.Password = plat.password;
                target.Server = plat.server;
                break;
            case 'hk':
                target.UserName = plat.userName;
                target.Password = plat.password;
                target.Server = plat.server;
                target.port = plat.port;
                break;
        }
        return target;
    }

    function randToken() {
        var random = "";
        for (var i = 0; i < 6; i++) {
            random += Math.floor(Math.random() * 10);
        }
        return random;
    }

    window.OneMap = $.fn.OneMap = function (options) {
        return new onemap(options);
    };

}(window, jQuery);
