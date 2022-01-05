/**
 * 环境辅助类,用于判断浏览器类型及其他环境变量
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/14 10:00
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/has"], function (has) {
  var Browser={IE:"ie",mozilla:"mozilla",ff:"ff",opera:"opera",webkit:"webkit",chrome:"chrome"};

    /***
     * 判断是否是ie浏览器
     * @returns {*}
     * @private
     */
    function _isIE() {
        return has(Browser.IE);
    }

    /***
     * 判断是否是ie 6 7 8
     * @returns {boolean}
     * @private
     */
    function _lessThanIE9() {
        return has(Browser.IE) < 9;
    }

    /***
     *判断是否是webkit内核
     * @returns {*}
     * @private
     */
    function _isWebKit() {
        return has(Browser.webkit);
    }

    /***
     * 判断是否是chrome浏览器
     * @returns {*}
     * @private
     */
    function _isChrome() {
        return has(Browser.chrome);
    }

    return {
        isIE:_isIE,
        isChrome:_isChrome,
        isWebkit:_isWebKit,
        isLessThanIE9:_lessThanIE9
    };
});