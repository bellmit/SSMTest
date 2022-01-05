/**
 * screen helper
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/2 18:28
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare", "dojo/_base/lang"], function (declare, lang) {

    var instance, s = declare("ScreenHelper", null, {

        /***
         * 屏幕的高度
         */
        screenHeight: undefined,
        /***
         *屏幕的宽度
         */
        screenWidth: undefined,

        constructor: function () {
            this.screenHeight = document.body.clientHeight;
            this.screenWidth = document.body.clientWidth;
            //console.log("此设备屏幕分辨率："+ this.screenHeight+" X "+ this.screenWidth);
        },
        /**
         * 获取屏幕的高度类型
         */
        heightLevel: function () {
            if (this.screenHeight <= 800) return s.LOW;
            else if (this.screenHeight <= 900) return s.MEDIUM;
            else if (this.screenHeight <= 1080) return s.HIGH;
            else return s.ULTIMATE;
        },
        /**
         * 获取屏幕的宽度类型
         */
        widthLevel: function () {
            if (this.screenWidth <= 1280) return s.LOW;
            else if (this.screenWidth <= 1440) return s.MEDIUM;
            else if (this.screenWidth <= 1920) return s.HIGH;
            else return s.ULTIMATE;
        },
        /***
         * 屏幕朝向 LANDSCAPE/PORTRAIT
         */
        orientation: function () {
            if (window.screen.orientation.type.indexOf(this.LANDSCAPE) > -1)
                return this.LANDSCAPE;
            else
                return this.PORTRAIT;
        }
    });
    lang.mixin(s, {
        LOW: 1,
        MEDIUM: 2,
        HIGH: 3,
        ULTIMATE: 4,
        LANDSCAPE: 'landscape',
        PORTRAIT: 'portrait'
    });
    s.getInstance = function () {
        if (instance == undefined)instance = new s();
        return instance;
    };
    return s;
});