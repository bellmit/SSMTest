/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:30
 * File:    init
 * (c) Copyright gtmap Corp.2015
 */
(function () {
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
}());
/**
 * Event Bus
 *
 * */
var EventBus = {

    /**
     * content init
     */
    CONTENT_INITIALIZE: "contextInitialize",

    /**
     * main map resize
     */
    CONTENT_RESIZE: "contentResize",

    /**
     * all widgets loaded
     */
    WIDGETS_LOADED: "widgetsLoaded",

    /**
     * main map init
     */
    MAIN_MAP_INITIALIZED: "mainMapInitialized",

    /**
     * main map init extent set already
     */
    MAIN_MAP_INIT_EXTENT_SET: "mainMapInitExtentSet",

    /**
     * 主地图 图形要素清除事件
     */
    MAIN_MAP_GRAPHICS_REMOVED: "mainMapGrasRemoved",

    /**
     * 系统初始化完成
     */
    APP_INITIALIZED: "appInitialized",

    /**
     * app init
     */
    appInit: function () {
        $j(document).trigger(EventBus.CONTENT_INITIALIZE);
    },
    /**
     * trigger event
     *
     * @param evt event name
     * @param data
     */
    trigger: function (evt, data) {
        $j(document).trigger(evt, [data]);
    },
    /**
     * listener
     *
     * @param evt event name
     * @param callback Function
     */
    listener: function (evt, callback) {
        $j(document).on(evt, callback);
    }
};

/**
 * log info
 *
 * @param info
 */
var log = function (info) {
    var now = new Date();
    var timeTag = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getUTCDate() + " "
        + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
    console.log(timeTag + "|-INFO--  " + info);
};

/**
 * error
 *
 * @param err
 */
var error = function (err) {
    var now = new Date();
    var timeTag = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getUTCDate() + " "
        + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
    console.error(timeTag + '|-ERROR-- ' + err);
};

/***
 * warn
 * @param warnText
 */
var warn = function (warnText) {
    var now = new Date();
    var timeTag = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getUTCDate() + " "
        + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
    console.warn(timeTag + '|-WARN-- ' + warnText);
};

/***
 * 获取url参数
 * @returns {{}}
 */
var getUrlParams = function () {
    var result = {};
    var s = window.location.search;
    var param = s.substr(1);
    if (param == "")return result;
    param = decodeURIComponent(param);
    var arrWithEqual = param.split("&");
    for (var i = 0; i < arrWithEqual.length; i++) {
        var val = arrWithEqual[i];
        var firstIndex = val.indexOf("=");
        result[val.substring(0, firstIndex)] = val.substr(firstIndex + 1);
    }
    return result;
};

/**
 * map topics
 */
var MapTopic = {
    /**
     * 对比地图 添加
     */
    MAP_CONTRAST_ADD: "map/contrast-add",
    /**
     * 对比地图 缩放
     */
    MAP_CONTRAST_RESIZE: "map/contrast-resize",
    /**
     * 对比地图 移除
     */
    MAP_CONTRAST_REMOVE: "map/contrast-remove",
    /**
     * 对比地图 状态改变
     */
    MAP_CONTRAST_CHANGE: "map/contrast-change",
    /**
     * 主地图 选中
     */
    MAP_MAIN_CHECKED: "map/main-map-checked",
    /***
     * 地图加载图层发生更新
     */
    MAP_LAYERS_REFRESHED: "map/layers-refreshed"
};
/**
 *
 */
var tpl = window.location.pathname.split("/").reverse()[0], mainMapContentDiv = "map-content";

var urlParams = getUrlParams();

var loginUser = {name: userName, id: userId};

var appInitialized = false;

/***
 * 监听用户注销退出菜单
 */
var addSysListener = function () {
    var loginMenu;
    $(".sys-login").off('click').on('click', 'a', openSysMenu);
    $(document).on('click', function () {
        closeSysMenu();
    });
    /**
     *打开二级菜单
     */
    function openSysMenu(event) {
        event.stopPropagation();
        if (loginMenu != undefined && loginMenu.css("display") === "block") {
            closeSysMenu();
            return;
        }
        loginMenu = $(this).next('ul').eq(0);
        loginMenu.show();
        //切换账号
        $("#loginSwitchBtn").off('click').on('click', function () {
            closeSysMenu();
            if (confirm("确定注销当前用户？")) {
                window.location.href = omsUrl + '/login?url=' + window.location.href;
            }
            return false;
        });
        $("#logoutBtn").off('click').on('click', function () {
            closeSysMenu();
            if (confirm("确认退出系统?")) {
                var userAgent = navigator.userAgent;
                if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
                    window.location.href = omsUrl + "/logout";
                } else {
                    window.opener = null;
                    window.open('', '_self', '');
                    window.close();
                }
            }
        });
    }

    /**
     * 关闭二级菜单
     */
    function closeSysMenu() {
        if (loginMenu) loginMenu.eq(0).hide();
    }
};
/**
 * ready
 *
 */
$(function () {

    addSysListener();

    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        for (var s = this, i = 0; i < arguments.length; i++)
            s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
        return s;
    };
    /**
     *
     * @param str
     * @returns {boolean}
     */
    String.prototype.startWith = function (str) {
        if (this.substr(0, str.length) === str) {
            return true;
        } else {
            return false;
        }
    };

    /**
     *
     * @param str
     * @returns {boolean}
     */
    String.prototype.endWith = function (str) {
        if (this.substr(this.length - str.length, str.length) === str) {
            return true;
        } else {
            return false;
        }
    };

    /**
     * equalsIgnoreCase
     * @param s
     * @returns {boolean}
     */
    String.prototype.equalsIgnoreCase = function (s) {
        if (typeof s === "string")
            return this.toLowerCase() === s.toLowerCase();
        else
            console.error("arguments not string");
    };

    /**
     * 匹配El表达式中的${}值
     * @returns {String}
     */
    String.prototype.matchEL = function () {
        var that = this;
        var s = this.match(/\${(.*?)}/g);
        if (s instanceof Array) {
            for (var i in s) {
                if ((typeof i === "number") || !isNaN(parseInt(i, 10))) {
                    var key = s[i].substring(2, s[i].length - 1);
                    var value = getPropertyFromEgov(key);
                    if (value)
                        that = that.replace(s[i], value);
                }
            }
        }
        return that;
    };

    /**
     * 日期格式化
     * @param fmt
     * @returns {*}
     * @constructor
     */
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o){
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }
        return fmt;
    };

    /**
     * Date  toLocaleString
     * @returns {string}
     */
    Date.prototype.toLocaleString = function() {
        return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 " + this.getHours() + "点" + this.getMinutes() + "分" + this.getSeconds() + "秒";
    };
    /**
     * 从后台请求值
     * @param k
     * @returns {string}
     */
    function getPropertyFromEgov(k) {
        var value = "";
        $.ajax({
            url: root + "/config/property/egov",
            data: {key: k},
            async: false,
            success: function (r) {
                value = r;
            }
        });
        return value;
    }

    String.prototype.trim = function() {
        return this.replace(/(^\s*)|(\s*$)/g, ""); //正则匹配空格
    };

    /**
     *  ie toISOString
     */
    if (!Date.prototype.toISOString) {
        Date.prototype.toISOString = function () {
            function pad(n) {
                return n < 10 ? '0' + n : n;
            }
            return this.getUTCFullYear() + '-'
                + pad(this.getUTCMonth() + 1) + '-'
                + pad(this.getUTCDate()) + 'T'
                + pad(this.getUTCHours()) + ':'
                + pad(this.getUTCMinutes()) + ':'
                + pad(this.getUTCSeconds()) + '.'
                + pad(this.getUTCMilliseconds()) + 'Z';
        }
    }
    /**
     * 地图初始化范围设定完成之后 触发系统初始化完成事件
     */
    EventBus.listener(EventBus.MAIN_MAP_INIT_EXTENT_SET, function () {
        Pace.stop();
        appInitialized = true;
        EventBus.trigger(EventBus.APP_INITIALIZED);
    });

    /**
     * listener
     *
     */
    EventBus.listener(EventBus.CONTENT_INITIALIZE, function () {
            var rq = ["jquery", "map/manager/ConfigManager"];
            //头部菜单以及搜索框的初始化
            if ($(".nav-topbar").length > 0) {
                $(".nav-topbar").show();
            }
            require(rq,
                function ($, ConfigManager) {
                    var configManager = ConfigManager.getInstance();
                    log("config manager init");
                    Pace.restart();
                    configManager.setContext(mainMapContentDiv);
                    configManager.loadConfig(root + "/map/" + tpl + "/config");
                });
        }
    );
    EventBus.appInit();
});

