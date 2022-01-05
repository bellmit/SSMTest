/*
 * 服务管理 - 服务组管理
 * 
 * @Author: ray zhang
 * @date: 2013-04-05 13:23
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function () {
    
    /*
     * 改变服务组顺序
     
    var $jglc = $("#J_GROUP_LIST_CTN")
   		, changeId = '';
    function changeOrderCallback(msg){
    	loading.hide();
    	$jglc.html(msg);
    	var $tds = $('#' + changeId).children('td');
    	$tds.css('background-color','#FFF2CB');
		beforeRemove();
    }
    $jglc.delegate('.j_btn_change_order', 'click',  function(){
    	loading.show('正在移动服务组');
    	changeId = $(this).parent().parent().attr('id');
    	$.get($(this).attr('href'), changeOrderCallback);
    	return false;
    });*/
	$('tbody').gridtree();
});