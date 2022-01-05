/*
 * 为模板添加服务用到的js
 * 
 * @author: Ray Zhang
 * @datetime: 2013-04-26 15:17
 */
$(document).ready(function(){
	
	var loading = ui.loading;
	
	// AJAX 获取map
	function ajaxForMaps(){
		var url = $(this).attr('url') || $(this).attr('href');
		url && $.get(url, function(msg){
			$('#J_BIND_MAP_FORM').html(msg);
		});
		return false;
	}
	$(document).delegate('#J_BTN_ADD_MAP, .j_group_nav_item, .j_pagin_link', 'click', ajaxForMaps);
	
	// 全选操作
	$(document).delegate('.j_check_all', 'click', function(){
		var self = $(this);
		var table = self.parents('table');
		var cbxs = table.find('tbody').find('input[type="checkbox"]');
		if( self.is(':checked') ){
			cbxs.each(function(){
				if(!$(this).attr('disabled')){
					this.checked = true;
				}
			});
		} else {
			cbxs.each(function(){
				if(!$(this).attr('disabled')){
					this.checked = false;
				}
			});
		}
	});
	
	$('#J_BIND_MAP_FORM').submit(function(){
        if($(this).find(':checked').length == 0)
            return false;
        return true;
    })
});