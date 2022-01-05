/*!
 * camera view resolver
 * 摄像头可视域 解释器
 * 可用于向后台请求摄像头的可视域 以及更新前端的样式
 * Author: yingxiufeng
 * Date:   2016/10/21 
 * Version:v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/Evented",
    "dojo/_base/Deferred",
    "dojo/_base/lang",
    "dojo/_base/array",
    "esri/lang"], function (declare, Evented, Deferred, lang, arrayUtil, esriLang) {

    /**
     * 类型 批次---一次性请求 一次性处理返回 solo---单个请求 单个处理返回
     * @type {{BATCH: string, SOLO: string ,COMMON:string}}
     */
    var RESOLVE_TYPE = {
        BATCH: 'batch',
        SOLO: 'solo',
        COMMON: 'common'
    };
    /**
     * rest请求地址
     */
    var reqUrl = root + "/video/camera/view/scope";

    var instance, me = declare([Evented], {

        /**
         * 计数器
         */
        counter: 0,
        /***
         * 关联的监控点
         */
        cameras: [],
        /***
         * 默认类型是单个处理
         */
        type: RESOLVE_TYPE.SOLO,
        /***
         * 是否正在执行一次任务
         */
        isRunning: false,
        /***
         *
         * @param data
         */
        setCameras: function (data) {
            this.cameras = data;
        },
        /***
         *
         * @param t
         */
        setType: function (t) {
            this.type = t;
        },
        /***
         * 开始请求绘制可视域
         */
        start: function () {
            var run = this.isRunning;
            if (run === true) {
                warn("任务正在进行中...");
                return;
            }
            var cameras = this.cameras;
            var type = this.type;
            if (esriLang.isDefined(cameras)) {
                this.isRunning = true;
                //如果传递的参数是空数组 则停止当前任务
                if (cameras.length == 0) {
                    this.stop();
                    return;
                }
                var _successHandler = this._ajaxSuccess;
                var _errorHandler = this._ajaxError;
                var _counterHandler = this._increaseCounter;
                var _self = this;

                if (type === RESOLVE_TYPE.BATCH) {
                    var icStr = getIndexCodes(cameras);
                    $.ajax({
                        url: reqUrl,
                        data: {indexCode: icStr, platform: cameras[0].platform},
                        method: 'post',
                        timeout: 10000,
                        success: lang.hitch(this, _successHandler),
                        error: lang.hitch(this, _errorHandler)
                    });
                } else if (type === RESOLVE_TYPE.SOLO) {
                    arrayUtil.forEach(cameras, function (camera) {
                        if (camera.type == "normal") {
                            $.ajax({
                                url: reqUrl,
                                data: {indexCode: camera.indexCode,platform:camera.platform},
                                method: 'post',
                                beforeSend: lang.hitch(_self, _counterHandler),
                                success: lang.hitch(_self, _successHandler),
                                error: lang.hitch(_self, _errorHandler)
                            });
                        }
                    });
                } else if (type === RESOLVE_TYPE.COMMON) {
                    var hk_cameras = [];
                    arrayUtil.forEach(cameras, function (camera) {
                        if (camera.platform.startWith("hw")) {
                            $.ajax({
                                url: reqUrl,
                                data: {indexCode: camera.indexCode, platform: camera.platform},
                                method: 'post',
                                beforeSend: lang.hitch(_self, _counterHandler),
                                success: lang.hitch(_self, _successHandler),
                                error: lang.hitch(_self, _errorHandler)
                            });
                        } else {
                            hk_cameras.push(camera);
                        }
                    });
                    var hkCameraIndexCode = getIndexCodes(hk_cameras);
                    $.ajax({
                        url: reqUrl,
                        data: {indexCode: hkCameraIndexCode, platform: hk_cameras[0].platform},
                        method: 'post',
                        timeout: 10000,
                        success: lang.hitch(this, _successHandler),
                        error: lang.hitch(this, _errorHandler)
                    });

                }
            }
        },
        /***
         * 停止
         */
        stop: function () {
            this.counter = 0;
            this.isRunning = false;
            //触发 停止事件
            this.emit('stopped');
        },
        /***
         * 计数器递增
         * @private
         */
        _increaseCounter: function () {
            this.counter += 1;
        },
        /**
         *
         * @param data
         * @param textStatus
         * @private
         */
        _ajaxSuccess: function (data) {
            if (esriLang.isDefined(data.success) && data.success === false) {
                error("请求可视域参数失败: " + data.msg);
            } else {
                this.emit('success', data);
            }
            if (this.counter == 0) {
                this.stop();
                return;
            }
            this.counter -= 1;
            if (this.counter == 0)this.stop();
        },
        /**
         *
         * @param XMLHttpRequest
         * @param textStatus
         * @private
         */
        _ajaxError: function (XMLHttpRequest, textStatus) {
            error("请求可视域参数失败: " + textStatus);
            if (this.counter == 0) {
                this.stop();
                return;
            }
            this.counter -= 1;
            if (this.counter == 0)this.stop();
        }
    });

    /***
     * 获取所有设备的indexcode
     * @param data
     * @returns {*}
     */
    function getIndexCodes(data) {
        var arr = [];
        arrayUtil.forEach(data, function (item) {
            if (item.type == 'normal') {
                arr.push(item.indexCode);
            }
        });
        return arr.join(",");
    }

    lang.mixin(me, RESOLVE_TYPE);

    me.getInstance = function () {
        if (instance === undefined) {
            instance = new me();
        }
        return instance;
    };
    return me;
});