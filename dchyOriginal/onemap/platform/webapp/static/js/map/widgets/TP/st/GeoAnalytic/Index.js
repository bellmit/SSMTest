/**
 * 省厅大数据分析模块
 * Created by yxf on 2017/9/25.
 * 分析流程：
 * - 上传 发布 shapefile 压缩包 生成 feature service
 * - 调用 join 的 gp 服务， 分别与 dltb xzdw 进行 join
 * - 查询 上一步 join 返回的 Feature service 的所有要素
 * - 将两个join 结果 以及 分析图形的 结果 传给 soe 接口  得到 soe 结果
 * - soe 结果 调用 分析服务
 * - 结束
 * todo:
 * - 异常处理更完备
 * - 功能流程更完善
 * - 模块独立化程度更高
 * - console.time 监测时间花费
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "dojo/json",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/FeatureLayer",
    "esri/graphic",
    "esri/lang",
    "esri/urlUtils",
    "map/core/BaseWidget",
    "map/core/QueryTask",
    "layer",
    "hbarsUtils",
    "map/core/EsriSymbolsCreator",
    "map/utils/MapUtils",
    "text!./tpl/xzResultTpl.html",
    "./DataProcessor",
    "static/thirdparty/jquery/jquery.cookie",
    "./APIOptions"], function (declare, lang, arrayUtil, topic, dojoJson, RandomUuid, FeatureLayer, Graphic, esriLang, urlUtils,
                               BaseWidget, QueryTask, layer, HbarsUtils, EsriSymbolsCreator, MapUtils, resultTpl, DataProcessor) {

    var _map, _widgetConfig;
    var me = declare([BaseWidget], {

        /**
         *
         */
        constructor: function () {
        },
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            _widgetConfig = this.getConfig();
            _init();
        },
        /**
         *
         */
        onPause: function () {
        },
        /**
         *
         */
        onOpen: function () {
        },
        /**
         *
         */
        onDestroy: function () {

        }
    });

    var _token, _tokenObj;

    //要素发布服务参数
    var PUBLISH_PARAMS_TPL = '{"name": "${service_name}", "targetSR" : { "wkid" : ${sr} },"copyrightText": "jsgt"}';
    // 默认 item 类型
    var SHAPE_FILE = 'Shapefile';
    // ags token 存放在 cookie 中的名称
    var COOKIE_AGSW = "agswtoken";
    // 轮询间隔时间
    var POLL_INTERVAL_TIME = 3000;

    // 待分析图形的 feature service 到图层序号 eg ..../0
    var _shpFeatureLayer, _shpPublished = false;

    var proxyUrl = window.location.origin + "/omp/map/proxy";

    var LYR_URL_PARAMS = '{"url": "${lyr}" }';

    var _fileTimer, _msgHandler;

    // gp 任务名称
    var GP_TASKS = {
        JoinFeatures: '/JoinFeatures',
        OverlayLayers: '/OverlayLayers',
        IntersectAll: '/intesectAll'
    };

    // gp 任务状态标识
    var GP_JOB_STATUS = {
        Submitted: 'esriJobSubmitted',
        Waiting: 'esriJobWaiting',
        Executing: 'esriJobExecuting',
        Succeeded: 'esriJobSucceeded',
        Failed: 'esriJobFailed',
        TimedOut: 'esriJobTimedOut',
        Cancelling: 'esriJobCancelling',
        Cancelled: 'esriJobCancelled'
    };

    /**
     * 初始化各项参数
     * @private
     */
    function _init() {
        lang.mixin(AgsAPI, _widgetConfig);
        //请求token
        _getToken();
        //监听 shp 文件上传事件
        _addFileListener();
        // 监听分析 清除 按钮事件
        _addOptListener();
    }

    /**
     * 从portal 获取 token
     * @private
     */
    function _getToken() {
        $.cookie(COOKIE_AGSW, null);
        _ajaxRequest(AgsAPI.portalRestRoot().concat("generateToken"), {
            username: AgsAPI.portalUser.username,
            password: AgsAPI.portalUser.password,
            client: 'referer',
            referer: window.location.hostname,
            expiration: '60',
            f: 'json'
        }, true, false, function (ret) {
            _tokenObj = lang.clone(ret);
            _token = ret.token;
            //token 存入 cookie
            $.cookie(COOKIE_AGSW, _token);
            console.log('---token 获取成功!----');
        }, function (e) {
            layer.msg(e.message, {icon: 0});
        });
    }

    /**
     * 分析 、清除按钮监听
     * @private
     */
    function _addOptListener() {
        $("#geoAnalysisBtn").on('click', function () {
            _doAnalysis();
        });
        $("#gaClearBtn").on('click', function () {
            _doClear();
        });
    }

    var shpFeature, xzdwJoinResult, dltbJoinResult;

    var xzdwIntersectRet, dltbIntersectRet;

    var xzdwTblName, dltbTblName, shpTblName;

    var analysisResult;

    /**
     * clear
     * @private
     */
    function _doClear() {
        layer.closeAll();

        shpFeature = undefined;
        _shpFeatureLayer = undefined;
        analysisResult = undefined;
        xzdwTblName = undefined;
        dltbTblName = undefined;
        xzdwJoinResult = undefined;
        dltbJoinResult = undefined;
        shpTblName = undefined;

        _map.graphics.clear();
        _addFileListener();

    }

    /**
     * 上传文件监听
     * @private
     */
    function _addFileListener() {
        $("#shpFileInput").off('change');
        $("#shpFileInput").on('change', function () {
            _addItem($(this).val(), "geo_analytic");
            _fileTimer = setTimeout(function () {
                _addFileListener();
            }, 2000);
        });
    }

    /**
     * 添加项目到 portal
     * @param file
     * @param title
     * @param tags  默认是 ga-data
     * @param type 默认是 Shapefile
     * @private
     * @return eg.
     * {
     *  "success": true,
     *  "id": "cd1544b0b26449309644eda3335f108c",
     *  "folder": null
     *  }
     *
     */
    function _addItem(file, tags, type) {

        if (file === undefined) return false;
        var tmp = file.split('.');
        var suffix = tmp[tmp.length - 1];
        var fileName = (tmp[0].split("\\"))[tmp[0].split("\\").length - 1];
        suffix = suffix.toLowerCase();

        if (suffix != "zip") {
            layer.msg('只支持 zip 包格式!');
            return false;
        }
        if (esriLang.isDefined(_msgHandler)) {
            layer.close(_msgHandler);
        }
        //检查是否已经存在同名item 若存在  则先删除
        // console.log('---pre check---');
        _ajaxRequest(AgsAPI.portalRestRoot() + 'search', {
            q: 'type: Shapefile AND tags: geo_analytic',
            token: _token,
            sortField: 'created',
            sortOrder: 'desc',
            num: 50,
            f: 'json'
        }, true, true, function (data) {
            var res = data.results;
            var filters = arrayUtil.filter(res, function (item) {
                return item.title === fileName;
            });
            if (filters.length > 0) {
                _deleteItem(filters[0].id);
            }
        });

        _msgHandler = layer.msg('正在上传...', {icon: 16, shade: 0.1, time: 5000});
        var url = proxyUrl + "?dataType=json&requestUrl=" + AgsAPI.portalContentRoot() + "users/" + AgsAPI.portalUser.username + "/addItem";
        shpTblName = undefined;
        print(null);
        console.time('分析用时');
        $.ajaxFileUpload({
            fileElementId: 'shpFileInput',
            url: url,
            type: 'post',
            dataType: 'JSON',
            data: {
                title: fileName,
                tags: tags,
                type: type || SHAPE_FILE,
                token: _token,
                f: 'json'
            },
            secureuri: false,
            success: function (ret) {
                layer.closeAll();
                _addFileListener();
                var r = dojoJson.parse(ret);
                if (r.success) {
                    //上传成功后 发布服务
                    _publishItem(r.id, "ga_shp");
                } else {
                    var err = r.error;
                    if (err.code === 409) {
                        layer.msg('文件 [ ' + fileName + '.' + suffix + ' ] 已存在!', {icon: 0, time: 4000});
                    } else {
                        layer.msg(r.error.message, {icon: 0, time: 4000});
                    }
                    print(err, '---添加要素至portal异常: %o', MSG_LEVEL.WARN);
                }
            }
        });
    }

    /**
     * 删除同名的item
     * @param itemId
     * @param callback
     * @private
     */
    function _deleteItem(itemId) {
        var url = proxyUrl + "?dataType=json&requestUrl=" + AgsAPI.portalContentRoot() + "users/" + AgsAPI.portalUser.username + "/items/" + itemId + '/delete';
        _ajaxRequest(url, {
            token: _token,
            f: 'json'
        }, false, true, function (ret) {
            if (ret.success === true) {
                print(itemId, '---[ %s ] 删除成功');
            }
        });
    }

    /**
     * 发布 item（上传成功的）
     * @param itemId  id of item to be published
     * @param type    item type 默认是 Shapefile
     * @param name    服务名称
     * @param overwrite  是否覆盖同名服务 默认 false
     * @private
     * @return eg.
     *  {
     *      "services": [
     *          {
     *              "type": "Feature Service",
     *              "serviceurl": "https://gis155.jsgt.local/arcgis/rest/services/Hosted/ga_data/FeatureServer",
     *              "serviceItemId": "635de7f066494869953ab51313811263",
     *              "encodedServiceURL": "https://gis155.jsgt.local/arcgis/rest/services/Hosted/ga_data/FeatureServer",
     *              "jobId": "jbcefa5696b9243e0bc97529081c29208"
     *          }
     *      ]
     *  }
     *
     */
    function _publishItem(itemId, name, type) {
        var data = {
            itemID: itemId,
            fileType: type || SHAPE_FILE,
            publishParameters: esriLang.substitute({
                service_name: name.concat('_' + RandomUuid().substr(0, 8)),
                sr: AgsAPI.sr
            }, PUBLISH_PARAMS_TPL),
            overwrite: true,
            f: 'json',
            token: _token
        };
        var url = AgsAPI.portalContentRoot() + "users/" + AgsAPI.portalUser.username + "/publish";
        _ajaxRequest(url, data, true, false, function (ret) {
            // layer.closeAll();
            if (ret.error) {
                layer.msg(ret.error.message, {icon: 0, time: 4000});
                print(ret.error, '---发布要素异常: %o', MSG_LEVEL.WARN);
            } else {
                //待分析图形服务地址
                var service = ret.services[0];
                if (service != undefined) {
                    if (service.hasOwnProperty('success')) {
                        layer.msg(service.error.message, {icon: 0, time: 4000});
                        print(service.error, '---发布要素异常: %o', MSG_LEVEL.ERROR);
                    } else {
                        layer.msg("要素发布成功!", {icon: 1});
                        _shpFeatureLayer = service.serviceurl + "/0";
                        _shpPublished = true;
                        print(_shpFeatureLayer, "---要素已成功发布: %s");
                        _msgHandler = layer.msg("生成图形...", {icon: 16, shade: 0.05, time: 12000});
                        // 获取结果
                        var clock = window.setInterval(function () {
                            _pollQueryUrl(_shpFeatureLayer, {}, clock, function (ret) {
                                shpFeature = ret;
                                print("---成功获取分析图形 features -----");
                                if (esriLang.isDefined(_msgHandler)) {
                                    layer.close(_msgHandler);
                                }
                                var fs = ret.features;
                                if (lang.isArray(fs)) {
                                    _add2Map(fs);
                                }
                                shpTblName = getFeatureTblName(_shpFeatureLayer);
                                print(shpTblName, '【shp 地块】 table: %s');
                            });
                        }, POLL_INTERVAL_TIME);
                    }
                }
            }
        });
    }

    /**
     * add features to map
     * @param features
     * @private
     */
    function _add2Map(features) {
        var gras = [];
        arrayUtil.forEach(features, function (item) {
            var g = new Graphic(item);
            g.setSymbol(EsriSymbolsCreator.defaultFillSymbol);
            gras.push(g);
            _map.graphics.add(g);
        });
        if (gras.length > 0) {
            MapUtils.setMap(_map);
            MapUtils.locateFeatures(gras);
        }
    }

    /**
     * 进行分析
     * @private
     */
    function _doAnalysis() {
        // clear vars 
        dltbIntersectRet = undefined;
        xzdwIntersectRet = undefined;
        dltbJoinResult = undefined;
        xzdwJoinResult = undefined;
        xzdwTblName = undefined;
        dltbTblName = undefined;
        analysisResult = undefined;
        $("#msgContainer").empty();
        if (esriLang.isDefined(_shpFeatureLayer) && _shpPublished === true) {
            _msgHandler = layer.msg("正在分析，请稍后……", {icon: 16, shade: 0.1, time: 600000});
            // join 地类图斑
            _join(AgsAPI.dltb, _shpFeatureLayer, 'dltb_join', function (data) {
                var jobId = data.jobId;
                var clock = window.setInterval(function () {
                    _pollGpState('join', jobId, clock, function (ret) {
                        print(ret.value.url, '---【地类图斑】join 运算结果: %s');
                        _addMsg('【地类图斑】join 运算结果:' + ret.value.url);
                        var tUrl = ret.value.url;
                        if (esriLang.isDefined(tUrl)) {
                            dltbTblName = getFeatureTblName(tUrl);
                            print(dltbTblName, '---【地类图斑】join table: %s');
                            _doIntersect();
                        }
                    });
                }, POLL_INTERVAL_TIME);
            });
            // join 线状地物
            _join(AgsAPI.xzdw, _shpFeatureLayer, 'xzdw_join', function (data) {
                var jobId = data.jobId;
                var clock = window.setInterval(function () {
                    _pollGpState('join', jobId, clock, function (ret) {
                        var retUrl = ret.value.url;
                        print(retUrl, '---【线状地物】join 运算结果: %s');
                        _addMsg('【线状地物】join 运算结果:' + retUrl);
                        if (retUrl != undefined) {
                            xzdwTblName = getFeatureTblName(retUrl);
                            print(xzdwTblName, '---【线状地物】join table: %s');
                            _doIntersect();
                        }
                    });
                }, POLL_INTERVAL_TIME);
            });
        } else {
            layer.msg('请先上传 shp 文件（或等待上传操作完成）！', {icon: 0});
        }
    }

    /**
     * 将 shp 和地类图斑 以及 线状地物层 进行 join
     * @private
     */
    function _join(targetLayer, joinLayer, outputName, callback) {
        _ajaxRequest(AgsAPI.gaGpServer() + GP_TASKS.JoinFeatures + "/submitJob", {
            targetLayer: esriLang.substitute({lyr: targetLayer}, LYR_URL_PARAMS),
            joinLayer: esriLang.substitute({lyr: joinLayer}, LYR_URL_PARAMS),
            joinOperation: 'JoinOneToOne',
            spatialRelationship: 'Intersects',
            outputName: outputName.concat('_' + RandomUuid().substr(0, 8)),
            context: '{"dataStore": "relational"}',
            f: 'json',
            token: _token
        }, true, false, function (ret) {
            callback(ret);
        }, function (e) {
            layer.msg(e, {icon: 0});
        });
    }

    /**
     * 获取 feature layer 的 table name
     * @param joinRetUrl
     */
    function getFeatureTblName(joinRetUrl) {
        var url = joinRetUrl.substring(0, joinRetUrl.length - 2);
        var arr = url.split('rest/');
        url = arr[0] + 'admin/' + arr[1].replace('/FeatureServer', '.FeatureServer');
        var tblName = undefined;
        _ajaxRequest(url, {
            token: _token,
            f: 'json'
        }, false, true, function (ret) {
            tblName = ret.jsonProperties.layers[0].adminLayerInfo.tableName;
        });
        return tblName;
    }


    /**
     * 轮询查询 feature service 结果
     * @param featureServiceUrl
     * @param data
     * @param successCallback
     * @param clock
     * @private
     */
    function _pollQueryUrl(featureServiceUrl, data, clock, successCallback) {
        var url = featureServiceUrl.concat("/query");
        var options = {
            spatialRel: 'esriSpatialRelIntersects',
            returnGeometry: true,
            returnTrueCurves: false,
            where: '1=1',
            token: _token,
            f: 'json'
        };
        if (data === undefined) {
            data = {};
        }
        lang.mixin(data, options);
        $.ajax({
            type: "POST",
            url: url,
            dataType: 'json',
            data: data,
            success: function (ret) {
                if (ret.hasOwnProperty("error")) {
                    console.warn(ret.error);
                    return false;
                }
                successCallback(ret);
                if (clock != undefined) {
                    window.clearInterval(clock);
                }
            },
            fail: function (xhr, err) {
                console.error(err);
            }
        });
    }

    /**
     * 轮询 gp 结果
     * @param type
     * @param jobId
     * @param successCallback
     */
    function _pollGpState(type, jobId, clock, successCallback) {
        var url = '';
        switch (type) {
            case 'join':
                url = AgsAPI.gaGpServer() + GP_TASKS.JoinFeatures + '/jobs/' + jobId;
                break;
            case 'overlay':
                url = AgsAPI.spGpServer() + GP_TASKS.OverlayLayers + '/jobs/' + jobId;
                break;
            case GP_TASKS.IntersectAll:
                url = AgsAPI.isGpServer() + GP_TASKS.IntersectAll + '/jobs/' + jobId;
                break;
            default:
                break;
        }
        //查询 gp 服务执行状态
        $.ajax({
            type: "POST",
            url: url,
            dataType: 'json',
            data: {
                f: 'json',
                token: _token
            },
            success: function (data) {
                var jobStatus = data.jobStatus;
                switch (jobStatus) {
                    case GP_JOB_STATUS.Submitted:
                    case GP_JOB_STATUS.Waiting:
                    case GP_JOB_STATUS.Executing:
                        print('---gp任务类型:[ ' + type + ' ] 任务ID:[' + jobId + '] 当前状态 [ ' + jobStatus + '] ');
                        _addMsg('任务类型:[' + type + '] <br/> 任务状态 [' + jobStatus + ']');
                        break;
                    case GP_JOB_STATUS.Cancelling:
                    case GP_JOB_STATUS.Cancelled:
                    case GP_JOB_STATUS.TimedOut:
                    case GP_JOB_STATUS.Failed:
                        print('---gp任务类型:[ ' + type + ' ] 任务ID:[' + jobId + '] 当前状态 [ ' + jobStatus + ']', '%s', MSG_LEVEL.WARN);
                        _addMsg('任务类型:[' + type + '] <br/> 任务状态 [' + jobStatus + ']', 'error');
                        window.clearInterval(clock);
                        layer.msg('gp 服务执行异常');
                        break;
                    case GP_JOB_STATUS.Succeeded: {
                        window.clearInterval(clock);
                        if ('join' === type) {
                            var output = url + '/results/output';
                            $.ajax({
                                type: "POST",
                                url: output,
                                dataType: 'json',
                                data: {
                                    f: 'json',
                                    token: _token
                                },
                                async: false,
                                success: function (data) {
                                    successCallback(data);
                                }
                            });
                        } else if (GP_TASKS.IntersectAll === type) {
                            successCallback(url + '/results/Intersect_Layer');
                        } else {
                            successCallback(url + '/results/outputLayer');
                        }
                    }
                    default:
                        break;
                }
            }
        });
    }


    /**
     * intersect all
     * @private
     */
    function _doIntersect() {
        if (esriLang.isDefined(shpTblName)) {
            if (!esriLang.isDefined(xzdwIntersectRet) && esriLang.isDefined(xzdwTblName)) {
                _ajaxRequest(AgsAPI.isGpServer() + GP_TASKS.IntersectAll + "/submitJob", {
                    FeatureLayer: shpTblName,
                    LayerName: xzdwTblName,
                    token: _token,
                    f: 'json'
                }, false, false, function (ret) {
                    var clock = window.setInterval(function () {
                        _pollGpState(GP_TASKS.IntersectAll, ret.jobId, clock, function (ret) {
                            print(ret, '【线状地物】相交运算结果: %s');
                            xzdwIntersectRet = ret;
                            _executeXzAnalysis();
                        });
                    }, POLL_INTERVAL_TIME);
                });
            }
            if (!esriLang.isDefined(dltbIntersectRet) && esriLang.isDefined(dltbTblName)) {
                _ajaxRequest(AgsAPI.isGpServer() + GP_TASKS.IntersectAll + "/submitJob", {
                    FeatureLayer: shpTblName,
                    LayerName: dltbTblName,
                    token: _token,
                    f: 'json'
                }, false, false, function (ret) {
                    var clock = window.setInterval(function () {
                        _pollGpState(GP_TASKS.IntersectAll, ret.jobId, clock, function (ret) {
                            print(ret, '【地类图斑】相交运算结果: %s');
                            dltbIntersectRet = ret;
                            _executeXzAnalysis();
                        });
                    }, POLL_INTERVAL_TIME);
                });
            }

        } else
            return false;
    }

    /**
     * 调用 esri 的 微服务 进行现状分析的逻辑处理
     * @private
     */
    function _executeXzAnalysis() {
        if (esriLang.isDefined(analysisResult)) return false;
        if (esriLang.isDefined(xzdwTblName) &&
            !(esriLang.isDefined(dltbIntersectRet) && esriLang.isDefined(xzdwIntersectRet))) {
            return false;
        }
        if (esriLang.isDefined(dltbIntersectRet) && esriLang.isDefined(dltbTblName)) {
            var restUrl = AgsAPI.xzAnaServer();
            var data = {
                dltbT1: dltbTblName,
                token: _token,
                f: 'json'
            };
            if (esriLang.isDefined(xzdwTblName)) {
                lang.mixin(data, {
                    xzdwT1: xzdwTblName
                });
            }
            $.ajax({
                type: "POST",
                url: '/omp/map/proxy?requestUrl=' + restUrl,
                dataType: 'json',
                data: data,
                success: function (data) {
                    console.timeEnd('分析用时');
                    $("#msgContainer").empty();
                    layer.close(_msgHandler);
                    // console.log(data);
                    if (typeof data === 'string') {
                        data = data.replace('success_jsonpCallback', '').replace(/\(/g, '').replace(/\);/g, '');
                        analysisResult = dojoJson.parse(data);
                        _showResult(analysisResult);
                    } else if (typeof data === 'object') {
                        if (data.hasOwnProperty('success')) {
                            layer.msg(data.msg);
                            return false;
                        }
                        _showResult(data);
                    }
                },
                error: function () {
                }
            });

        }
    }

    /**
     * 展示分类结果
     * @param data
     * @private
     */
    function _showResult(data) {
        var dp = new DataProcessor(data);
        var html = HbarsUtils.renderTpl(resultTpl, dp.parse());
        layer.open({
            type: 1,
            title: '分析结果',
            area: ['720px', '320px'], //宽高
            maxmin: true,
            content: html
        });
    }

    /**
     * 进行 ajax 请求
     * @param url
     * @param data
     * @param type
     * @param snyc
     * @param callback
     * @param failBack
     * @private
     */
    function _ajaxRequest(url, data, loadingState, sync, callback, failBack) {
        var tmp;
        $.ajax({
            url: url,
            method: 'post',
            data: data,
            timeout: 360000,
            dataType: 'json',
            async: !sync,
            before: function () {
                if (loadingState) {
                    tmp = layer.msg('请求数据中...', {
                        icon: 16
                        , shade: 0.01
                    });
                }
            },
            success: function (ret) {
                callback(ret);
            },
            error: function (e) {
                print(e, undefined, MSG_LEVEL.ERROR);
                if (failBack)
                    failBack(e);
            },
            complete: function () {
                if (esriLang.isDefined(tmp)) {
                    layer.close(tmp);
                }
            }
        })
    }

    // 消息级别 枚举
    var MSG_LEVEL = {
        DEBUG: 'debug',
        LOG: 'log',
        INFO: 'info',
        WARN: 'warn',
        ERROR: 'error'
    };

    /***
     * 利用 console 输出消息
     * @param msg      消息内容  如果传 null 则会调用 console.clear() 清除控制台
     * @param level    消息级别 debug/log/info/warn/error 若忽略 则默认 log
     */
    function print(msg, fmt, level) {
        if (msg === null) {
            console.clear();
            return false;
        }
        switch (level) {
            case MSG_LEVEL.DEBUG:
                esriLang.isDefined(fmt) ? console.debug(fmt, msg) : console.debug(msg);
                break;
            case MSG_LEVEL.INFO:
                esriLang.isDefined(fmt) ? console.info(fmt, msg) : console.info(msg);
                break;
            case MSG_LEVEL.WARN:
                esriLang.isDefined(fmt) ? console.warn(fmt, msg) : console.warn(msg);
                break;
            case MSG_LEVEL.ERROR:
                esriLang.isDefined(fmt) ? console.error(fmt, msg) : console.error(msg);
                break;
            case MSG_LEVEL.LOG:
            default:
                esriLang.isDefined(fmt) ? console.log(fmt, msg) : console.log(msg);
                break;
        }
    }

    /**
     * add msg to container
     * @param msg
     * @private
     */
    function _addMsg(msg, level) {
        var color = "#333";
        if (level == 'warn') {
            color = "#f0ad4e";
        } else if (level == 'error') {
            color = "#d2322d";
        }
        $("#msgContainer").prepend('<p style="color: ' + color + '">' + msg + '</p>');
        $("#msgContainer").scrollTop();
    }


    return me;
})
;