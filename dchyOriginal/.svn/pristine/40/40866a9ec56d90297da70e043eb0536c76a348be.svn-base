/*
 * 行政区管理
 * 
 * @Author: ray zhang
 * @date: 2013-05-17 13:51
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */
$(document).ready(function () {
    $('tbody').gridtree();
	
	var selectedName = ""
		, selectedId = ""
		, pId = ""
		, removePrefix = $('.j_remove_region').attr('href')
		, editPrefix = $('#J_ADD_PROVINCE').attr('url');
	
	$('tbody').selectable({
		filter : "tr",
		cancel : "._treetoggle",
		selected : function( event, ui ) {
		
			$('.j_normal').hasClass('hide') && $('.j_normal').removeClass('hide');
			
			var	selectedItem = $(ui.selected)
				, id = selectedItem.attr('id')
				, cNodes = document.getElementById(id).getElementsByTagName('td')[0].childNodes
				, len = cNodes.length
				
				
			selectedId = id;
			selectedName = cNodes[len-1].nodeValue;
			var pId = $('#'+id).attr('data-parent') || "";
			if( pId ){
				pId = "&parentId=" + pId;
			}
			$('.j_edit_region').attr('url', editPrefix + "?id=" + id + pId );
			
			
			$('.j_btns').children('.btn-primary').removeClass('hide').addClass('hide');
			
			switch ( $(ui.selected).attr('data-level') ){
				case "PROVINCE":
					$('#J_ADD_PROVINCE').removeClass('hide');
					$('#J_ADD_CITY').attr('url',editPrefix + "?parentId=" + id)
									.removeClass('hide');
					break;
				case "CITY":
					$('#J_ADD_DINSTINCT').attr('url',editPrefix + "?parentId=" + id)
										 .removeClass('hide');
					break;
				case "DISTRICT":
					$('#J_ADD_ROAD').attr('url',editPrefix + "?parentId=" + id)
										 .removeClass('hide');
					break;
				case "COUNTY":
					$('#J_ADD_VILAGE').attr('url',editPrefix + "?parentId=" + id)
										 .removeClass('hide');
					break;
			}
		}
	});
	
	$('.j_remove_region').click(function(){
		$(this).attr('href', removePrefix + "?id=" + selectedId);
		return window.confirm('确定要删除 ' + selectedName.toString() + ' 这个行政区吗？');
	});
});