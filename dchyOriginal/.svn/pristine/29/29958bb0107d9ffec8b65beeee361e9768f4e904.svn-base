/*
 * 日志查询模块
 * 
 * @Author: ray zhang
 * @date: 2013-06-13 09:22
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
 $(document).ready(function () {
    
    $('#J_BTN_CLEAN').click(function(){
		if( window.confirm('在服务器上清空所有日志？') ){
			var form = $(this).parent('form');
			form.attr( 'action', $(this).attr('data-url') );
			form.submit();
		}
	});
	
});