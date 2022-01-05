/*!
 * --------------------------------------
 * chosen select
 * 封装了chosen插件(依赖jq),实现下拉框的检索、多选等功能
 * 用法:
 * ChosenSelect({
 *      elem: '.chosen-select',
 *      lang: ''
 * });
 *
 * 开启多选：<select multiple />;增加'multiple'属性
 *
 * chosen官方地址:
 * https://harvesthq.github.io/chosen
 * -------------------------------------
 * Author: yingxiufeng
 * Date:   2017/3/4 
 * Version:v1.0 (c) Copyright gtmap Corp.2017
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "static/thirdparty/chosen/chosen.min",
    "css!static/thirdparty/chosen/chosen.min.css"], function (declare, lang) {
    var me = declare(null,{
        /**
         * dom元素进行初始化控件
         */
        elem: undefined,
        /**
         * 界面语言
         */
        lang: 'zh_cn',
        /**
         * 禁用检索功能，将不再显示搜索框来让用户检索选项
         */
        disable_search: false,
        /**
         * 当可选选项数目小于或等于此值时，自动禁用检索选项
         * 默认值 5
         */
        disable_search_threshold: 5,
        /**
         * 继承 <select> 的 CLASS
         */
        inherit_select_classes: false,
        /**
         * 单选空值占位文本
         */
        placeholder_text: '请选择一项',
        /***
         * 多选空值占位文本
         */
        placeholder_text_multiple: '请选择一项或多项',
        /**
         * 启用任意位置检索
         */
        search_contains: true,

        constructor: function (options) {
            lang.mixin(this, options);
            try {
                var dom = $(this.elem);
                dom.chosen(this);
                dom.on('chosen:ready', this.onReady);
                dom.on('change', this.onChange);
                dom.on('chosen:maxselected', this.onMaxSelected);
                dom.on('chosen:showing_dropdown', this.onShowDropDown);
                dom.on('chosen:hiding_dropdown', this.onhideDropDown);
                dom.on('chosen:no_results', this.onFindNoResult);
            } catch (e) {
                console.error(e);
            }
        },
        onReady: function (evt, params) {
            //do something
        },
        onChange: function (evt, params) {
            //do something
        },
        onMaxSelected: function (evt, params) {
            //do something
        },
        onShowDropDown: function (evt, params) {
            //do something
        },
        onhideDropDown: function (evt, params) {
            //do something
        },
        onFindNoResult: function (evt, params) {
            //do something
        },
        /**
         * 触发chosen事件
         * @param type
         */
        triggerEvent: function (type) {
            $(this.elem).trigger(type);
        }
    });
    return me;
});