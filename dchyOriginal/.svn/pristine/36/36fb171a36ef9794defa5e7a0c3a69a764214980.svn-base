/**
 * mock https
 * 利用mockjax进行模拟后台请求返回数据情景
 * 用于测试
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/6/1 9:05
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["static/thirdparty/jquery/jquery.mockjax.min",
    "dojo/domReady!"],function () {
    return {
        mockJson:function(url,response){
            $.mockjax({
                url:url,
                status:200,
                responseTime:100,
                responseText:{result:response,success:true}
            });
        }
    };
});