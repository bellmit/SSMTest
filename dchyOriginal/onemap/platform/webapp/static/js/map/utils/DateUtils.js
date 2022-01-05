/**
 * date utils
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/5/26 14:14
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/lang","static/thirdparty/moment/moment"],function (lang,moment) {

    /***
     * 格式化当前时间
     * @param pattern
     * @returns {*}
     */
    var formatNow=function(pattern){
         return format(undefined,pattern);
    };

    /***
     * 获取季度
     * @param iso  iso 格式的时间字符串
     * @returns {*}
     */
    var quarter=function(iso){
        return iso===undefined?moment().quarter():moment(iso).quarter();
    };

    /***
     * 获取月份
     * @param date
     */
    var month=function(date){
        return date===undefined?moment().month():moment(date).month();
    };
    /***
     * 格式化时间
     * @param datetime iso string
     * @param pattern
     * @returns {*}
     */
    var format=function(datetime,pattern){
        return datetime===undefined?moment().format(pattern):moment(datetime).format(pattern);
    };


    return {
        formatNow:formatNow,
        format:format,
        quarter:quarter,
        month:month
    };
});