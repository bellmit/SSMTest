/**
 * base widget
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/4 8:55
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare", "dojo/_base/lang", 'dojo/topic',"dojo/domReady!"],function (declare,lang,topic) {
    return declare(null,{
        /**
         *
         */
        id: '',
        /**
         *
         */
        label: '',
        /**
         *
         */
        icon: 'icon',
        /**
         *
         */
        url: '',
        /**
         *
         */
        position: {
            left: NaN,
            right: NaN,
            top: NaN,
            bottom: NaN
        },
        /**
         *
         */
        open: false,
        /**
         *
         */
        map: undefined,
        /**
         * config
         */
        config: {},
        /**
         *
         */
        created: false,

        constructor: function () {
            var arg = arguments[0];
            lang.mixin(this, arg);
        },
        /**
         * create complete
         *
         * 重写此方法初始化Widget
         */
        onCreate: function () {

        },
        /**
         * fire on widget open
         *
         */
        onOpen: function () {

        },
        /**
         * fire on widget pause
         *
         */
        onPause: function () {

        },
        /**
         * fire before destroy
         *
         */
        onDestroy: function () {

        },
        /**
         * get widget label
         * @returns {string}
         */
        getLabel:function(){
          return this.label;
        },
        /**
         * set map
         *
         * @param value
         */
        setMap: function (value) {
            map = value;
        },
        /**
         * get map
         *
         * @returns {map|*}
         */
        getMap: function () {
            map.map().setMapCursor('default');
            return map;
        },
        /**
         * set config
         *
         * @param value
         */
        setConfig: function (value) {
            this.config = value;
        },
        /**
         * get config
         *
         * @returns {*}
         */
        getConfig: function () {
            return this.config;
        },

        /**
         *
         * @param value
         */
        setAppConfig:function(value){
            appConfig=value;
        },
        /**
         *
         * @returns {*|appConfig}
         */
        getAppConfig:function(){
            return appConfig;
        },
        /**
         * set position
         *
         * @param args
         */
        setPosition: function (args) {
            lang.mixin(position, args);
        },
        /**
         * show message
         * @param message
         * @param title
         */
        showMsg: function (message, title) {
            topic.publish("widget/message", {message: message, title: title, p: 1});
        },
        /**
         * show warning
         *
         * @param message
         * @param title
         */
        showWarn: function (message, title) {
            topic.publish("widget/message", {message: message, title: title, p: 2});
        },
        /**
         * show error
         *
         * @param message
         * @param title
         */
        showError: function (message, title) {
            topic.publish("widget/message", {message: message, title: title, p: 3});
        }

    });
});