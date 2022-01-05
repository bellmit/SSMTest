/*
 * 服务管理 - 服务元数据 - 首页
 * 
 * @Author: ray zhang
 * @date: 2013-04-05 13:23
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function () {
    refreshPagin( '#J_PAGIN', _page, _size, _count, 10 );// 刷新分页元素
    
	var toggleId = '';
    	
    function callback(msg){// 开启、关闭服务ajax回调函数
    	var mapCtn = $('#'+toggleId);
    	mapCtn.fadeOut('fast',function(){
    		mapCtn.html(msg);
			loading.hide();
    		mapCtn.fadeIn();
			beforeRemove();
    	});
    }
    
    $('#J_MAP_LIST').delegate('.j_btn_trigger','click',function(){// 绑定开启、关闭服务操作
    	if( window.confirm('你需要' + $(this).html() + '服务吗?') ){
			var reqUrl = $(this).attr('href');
			toggleId = $(this).attr('data-ctnId');
			loading.show('正在与后台通信');
			$.get(reqUrl, callback);
		}
		return false;
    });
	
});