/**
 * 应用配置的公共js方法
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/9 13:50
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
var reg = new RegExp("(^|&)" + "tpl" + "=([^&]*)(&|$)");
var tpl = undefined;
var r = window.location.search.substr(1).match(reg);
if (r != null && r != undefined) {
    tpl = unescape(r[2]);
}
var root = "/" + window.location.pathname.split("/")[1];
$(function () {

    /**
     * 搜索框的显示
     */
    $(".search-app-btn").on('click', function () {
        if ($(this).hasClass("fa-search")) {
            $(".search-panel").css("top", "102px");
            $(".search-panel").css("z-Index", "1");
            $(this).removeClass("fa-search").addClass("fa-times");
        } else {
            $(".search-panel").css("top", "0px");
            setTimeout(function () {
                $(".search-panel").css("z-Index", "-1");
            }, 250);
            $(this).removeClass("fa-times").addClass("fa-search");
        }
    });

    /**
     * 序列化表单
     * @returns {{}}
     */
    jQuery.fn.serializeObject = function(){
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }
});

