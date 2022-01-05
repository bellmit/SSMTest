/**
 * browser util 浏览器工具类 用于判断浏览器种类等
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/7/14 9:43
 * File:    BrowserUtil
 * (c) Copyright gtmap Corp.2015
 */
(function (window, document, undefined) {
    var BrowserUtil;
    if (!BrowserUtil) {
        BrowserUtil = {};
    }
    if (typeof define === 'function' && define.amd) {
        define(BrowserUtil);
    }
    BrowserUtil.sys = function () {
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :

            (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :

                (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :

                    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :

                        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
        return Sys;
    }
}(window,document));
