/**
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/18 15:36
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/lang","layer"],function(lang,layer){

    var option={area:'900px'};
    var index;
    /**
     * 弹出框显示内容
     * @param title
     * @param content
     * @param success
     * @op 参数选项
     */
    function _open(title,content, success, op){
        var type = 1;
        if(content instanceof Object){
            type = 1;
        }else if(typeof content === 'string' && String && content.startsWith("http://")){
            type = 2;
        }
        option=lang.mixin(option,op);
        index = layer.open({
            type: type,
            title: title,
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            area: option.area,
            content: content,
            success:success
        });
    }

    /**
     * 自动封装指定选择器的click事件弹出内容
     * @param selector
     * @param title
     * @param content
     * @param option
     * @private
     */
    function _openByListener(selector, title, content, success, option){
        selector.on('click',function(){
            _open(title, content, success, option)
        });
    }

    function _close(){
        layer.close(index);
    }

    return{
        showDialog:_open,
        showSimpleDialog:_openByListener,
        closeDialog:_close
    }
});