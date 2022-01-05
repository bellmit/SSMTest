/*
 * 授权
 * 
 * @Author: ray zhang
 * @date: 2013-05-17 13:51
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function () {
    $('tbody').gridtree({ 
		trigger : '.gridtree_trigger'
		, fetchCallback : function( item ){
			renderInputs( item );
			bindInitStatus( item );
		}
	});
	
	
	
	function isChanged( tr ){
			var back = false;
			$(tr).find('form').find('input[type="checkbox"]').each(function(){
				var self = $(this)
					, now = self.is(':checked')
					, status = self.data('initStatus');
				
				if( status === undefined ){
					status = self.attr('data-init-status');
				}
				
				if( now != status ){
					back = true;
				}
			});
			return back;
		}
		
	function isAnyChanged(){
		var back = false;
		$('.j_save').each(function(){
			if( $(this).is(':visible')){
				back = true;
			}
		});
		if ( back ){
			$('#J_BTN_SAVE_ALL').fadeIn( function(){
				$(this).css('display','inline-block');
			} );
		}else{
			$('#J_BTN_SAVE_ALL').css('display','none');
		}
	}
	
	function callbackSubmitGrantInfo(msg, tr){
		if( msg === "success" ){
			alerts('授权成功' , msg);
			$(tr).find('form').find('form input[type="checkbox"]').each(function(){
				var self = $(this);
				self.data('initStatus',self.is(':checked'));
			});
			$(tr).find('.j_save').css('display','none');
		} else {
			alerts( msg, 'error' );
		}
	}
	
	function bindInitStatus(tr){
		$(tr).find('form').find('input[type="checkbox"]').each(function(){
			$(this).data('initStatus', $(this).is(':checked'));
			$(this).attr('data-init-status', $(this).is(':checked'));
		});
	}
	
	$('tbody').children('tr').each(function(){
		bindInitStatus( this );
	});
	
	$('tbody').delegate('input[type=checkbox]', 'change', function(){
		var tr = $(this).parents('tr')[0];
		if( isChanged( tr ) ){
			$(tr).children('td:last-child').children('.j_save').fadeIn(function(){
				$(this).css('display','inline-block');
			});
		} else {
			$(tr).children('td:last-child').children('.j_save').css('display','none');
		}
		isAnyChanged();
	});
	
	$('tbody').delegate('.j_save', 'click', function(){
		var tr = $(this).parents('tr')[0];
		$(tr).find('form').ajaxSubmit({
			callback : function(msg){
				callbackSubmitGrantInfo(msg, tr);
			}
		});
	});
	
	$('#J_BTN_SAVE_ALL').click(function(){
		$.browser.msie && loading.show('正在保存');
		$('.j_save:visible').click();
		$(this).css('display','none');
		$.browser.msie && loading.hide();
	});
		
	$(document).delegate('.j_check_all', 'click', function() {
		var self = $(this)
			, parent = self.parents('tr')[0]
			, cbxs = $(parent).find('form').find('input[type="checkbox"]');
		
		$(this).is(':checked') ? $(this).parent('span').addClass( 'checked' ) : $(this).parent('span').removeClass( 'checked' );
		
		cbxs.each(function() {
			if( !$(this).attr('disabled') && this.checked != self.is(':checked') ){
				this.checked = self.is(':checked');
				$(this).is(':checked') ? $(this).parent('span').addClass( 'checked' ) : $(this).parent('span').removeClass( 'checked' );
				$(this).trigger('change');
			}
		});
	});
});