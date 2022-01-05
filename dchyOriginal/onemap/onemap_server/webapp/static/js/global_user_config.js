/*
 * 全局页面 - 用户设置
 * 
 * @Author: ray zhang
 * @date: 2013-06-03 10:23
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function(){
	
	$(document).delegate('#J_SUBMIT_UCONFIG', 'click', function(){
		$('#J_FORM_UCONFIG').ajaxSubmit({
			callback : function( msg ){
				if( msg === "success" ){
					alerts('提交成功', 'success', '', 'J_ALERT_UCONFIG');
					setTimeout(function(){
						$('#J_CANCEL_UCONFIG').click();
					},1000);
				} else {
					alerts('提交失败', 'error', '', 'J_ALERT_UCONFIG');
				}
			}
			, loading : '正在修改信息'
		});
	});
});