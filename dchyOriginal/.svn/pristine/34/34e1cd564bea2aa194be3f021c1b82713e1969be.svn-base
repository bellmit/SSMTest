/*
 * 服务管理 - 服务编辑页面
 * 
 * @Author: ray zhang
 * @date: 2013-04-05 13:23
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function () {
	
    /*
     * ajax添加服务组
     */
    function addGroupCallback( msg ){	// 添加服务组成功回调
        var $ctn = $('#J_SELECT_GROUP_CTN');
        $ctn.html(msg);
        $ctn.find('select').children('option').each(function () {
            ($(this).html() == name) && $(this).attr('selected', 'selected');
        });
        renderInputs();
        $("#J_NEW_GROUP_MODAL").data('modal').hide();
    }
    ajaxSubmit('J_FORM_ADD_GROUP', 'J_BTN_ADD_GROUP', 'click', addGroupCallback, '正在添加服务组');

    
	/*
	 * 验证表单
	 */
    function validateForm(){
        $("#J_FORM_EDIT").validationEngine();
        $("#J_FORM_ADD_GROUP").validationEngine();
    }
    validateForm();
    
	
	/*
	 * 上传图片
	 */
	$('#J_UPLOAD_NAIL').click(function(){// 点击提交上传图表单
		
		var self = $(this)
			, val = $('#J_IMG').val()
			, p = val.lastIndexOf('.')
			, type = val.substring(p)
			, reqUrl = self.attr('href');
		
		if( type === ".jpg" || type === ".jpeg" || type === ".png" ){
			
			loading.show('正在上传图片');
			
			$.ajaxFileUpload({
				url: reqUrl,
				secureuri: false,
				dataType: "html",
				fileElementId: 'J_IMG',
				success: function( msg, status ){
					
					if( msg.length !== 32 ){
						loading.hide();
						alerts('发现异常','error', msg);
						return;
					}
					
					var imgCtn = $('#J_NAIL_WRAP')
						, img = imgCtn.children('img')
						, srcs = img.attr('src');
					
					img.remove();
					
					//var newImg = new Image();
					//newImg.src = srcs+"?d="+ new Date();
					var newImg = $('<img />');
					newImg.attr('src',srcs + '?d=' + new Date());
					try{
						$('#J_NAIL_WRAP').html(newImg);
					}catch(e){
						console.log(e);
					}
					loading.hide();
					self.next('.text-success').removeClass('hide');
				}
			});
		}
		
		return false;
	});
    
    /*
     * 添加服务提供者
     */
    var $provideCtn = $('#J_PROVIDER_TBODY'),
    	$step2Ctn = $('#J_NEW_PROVIDER_MODAL_2');
    function next(msg){
    	$step2Ctn.html(msg);
    	renderInputs('J_FORM_ADD_PROVIDER_STEP_2');
    	$('#J_FORM_ADD_PROVIDER_STEP_2').validationEngine();
    }
    // ajaxSubmit('J_FORM_ADD_PROVIDER_STEP_2', 'J_BTN_ADD_PROVIDER', 'click', done, '正在提交服务提供者');
    ajaxSubmit('J_FORM_ADD_PROVIDER_STEP_1', 'J_BTN_ADD_PROVIDER_NEXT', 'click', next, '正在努力加载中');
    
    
    /*
     * 改变服务提供者顺序
     */
    var changeId = '';
    function changeProviderOrderCallback(msg){
    	loading.hide();
    	$provideCtn.html(msg);
    	var $tds = $('#' + changeId).children('td');
    	$tds.css('background-color','#FFF2CB');
    	beforeRemove();
    }
    $provideCtn.delegate('.j_btn_change_order', 'click',  function(){
    	loading.show('正在移动服务提供者');
    	changeId = $(this).parent().parent().attr('id');
    	$.get($(this).attr('href'), changeProviderOrderCallback);
    	return false;
    });
	
	/*
	 * 点击select查询图层
	 */
	$('#J_LFT').change(function(){
		var lft = $(this).children(':selected').val()
			, path = window.location.pathname;
			
		window.location.href=path + "?lft=" + lft;
	});
});