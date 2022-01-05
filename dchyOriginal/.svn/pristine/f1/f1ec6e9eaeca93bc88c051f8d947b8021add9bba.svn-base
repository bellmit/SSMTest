/**
 * 东台批而未供分析
 * 并可以打印输出添加标记后的地图 以及添加动态标绘 并结合分析 进行扩展
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "mustache",
    "map/core/BaseWidget",
    "handlebars",
    "layer"], function (declare, lang, Mustache, BaseWidget, Handlebars, layer) {

    var __map, _config;


    var me = declare([BaseWidget], {
        /***
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            _config = this.getConfig();
            _init();
        }
    });

    /***
     * init
     * @private
     */
    function _init() {
        layer.config();
        var linkUrl = _config.linkUrl;

        $("#jsyldBtn").click(function () {
            if (linkUrl != undefined && linkUrl != null) {
                window.open(linkUrl);
            }
        });

    }

    return me;
});
