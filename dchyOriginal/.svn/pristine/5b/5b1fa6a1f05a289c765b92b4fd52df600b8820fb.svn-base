/**
 * Handlebars Utils
 * Created by yingxiufeng on 2017/6/16.
 */
define(["dojo/_base/lang",
    "dojo/_base/array",
    "handlebars"], function (lang, arrayUtil, Handlebars) {


    /**
     * compile
     * @param tpl
     */
    var compile = function (tpl) {
        return Handlebars.compile(tpl);
    };
    /***
     * 渲染模板
     * @param tpl
     * @param data
     */
    var render = function (tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    };

    /***
     * register helper
     * @param name
     * @param helper
     */
    var registerHelper = function (name, helper) {
        if (Handlebars.Utils.isFunction(helper)) {
            unregisterHelper(name);
            Handlebars.registerHelper(name, helper);
        } else {
            console.error("The helper argument is not a function!");
        }
    };

    /***
     * determines if an obj is an array
     * @param obj
     * @returns {*}
     */
    var isArray = function (obj) {
        return Handlebars.Utils.isArray(obj);
    };

    /***
     * unregister helper
     * @param name
     */
    var unregisterHelper = function (name) {
        Handlebars.unregisterHelper(name)
    };

    /**
     * register partial
     * @param name
     * @param tpl
     */
    var registerPartial = function (name, tpl) {
        Handlebars.registerPartial(name, tpl);
    };

    return {
        compile: compile,
        renderTpl: render,
        regHelper: registerHelper,
        unRegHelper: unregisterHelper,
        registerPartial: registerPartial,
        isArray: isArray
    };
});