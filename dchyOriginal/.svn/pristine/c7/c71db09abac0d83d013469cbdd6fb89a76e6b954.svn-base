/*
 * 服务列表用到js
 * 
 * @author: Ray Zhang
 * @datetime: 2013-04-23 15:17
 */
$(document).ready(function() {
	! function() {
		/*
		 * @name: 显示在右上角的面板
		 *
		 * @param: dom: 面板jquery selector 选择符
		 * 			options: { onReset: 重置时回调函数, onConfirm: 确定时回调函数 }
		 */
		var ConfirmPanel = function(dom, options) {
			this.elem = $(dom);
			this.options = $.extend({}, ConfirmPanel.defaults, options);
			this.init();
		}
		ConfirmPanel.defaults = {
			onReset : null,
			onConfirm : null
		}

		ConfirmPanel.prototype = {
			init : function() {
				var self = this;

				this.elem.find('.j_reset').click(function() {
					self.options.onReset && self.options.onReset();
					self.hide();
				});

				this.elem.find('.j_confirm').click(function() {
					self.options.onConfirm && self.options.onConfirm();
				});
			},

			show : function() {
				var $elem = this.elem;
				if ($elem.hasClass('fix-top'))
					return;
				$elem.fadeIn(500, function() {
					$elem.addClass('fix-top');
				});
			},

			hide : function() {
				var $elem = this.elem;
				if (!$elem.hasClass('fix-top'))
					return;
				$elem.css('display', 'none').removeClass('fix-top');

			}
		}
		window.ConfirmPanel = ConfirmPanel;
	}(); 
	
	! function() {
		var a = {
			opacity : 0.8,
			cursor : 'move',
			handle : 'td.drag-area',
			items : 'tr',
			placeholder : "sort_placeholder",
			scroll : true,
			revert : 150,
			forceHelperSize : true,

			start : function(e, ui) {
				var td_length = ui.helper.children('td').length;
				ui.placeholder.html($('<td />').attr({
					'colspan' : td_length,
					'padding' : 0
				}));
				ui.placeholder.children('td').html($('<div/>'));
				ui.placeholder.find('div').height(ui.helper.height());
			},

			stop : function(e, ui) {
				ui.item.css('background-color', '#FFFABC');
				resetOrder();
				confirmPanel.show();
			}
		};

		var $tbody = $('#J_SERVICES_TBODY')
		tbodyHTML = $tbody.html();
		$tbody.sortable(a);

		function resetOrder() {
			$tbody.children('tr').each(function(i) {
				$(this).children('td:first-child').html(i);
			})
		}

		function resort() {
			$tbody.fadeOut('500', function() {
				$tbody.html(tbodyHTML);
				$tbody.fadeIn('500');
			})
		}

		function confirm() {
			$tbody.children('tr').each(function(i) {
				var $td = $(this).children('td:first-child'), order = $td.html(), id = $(this).attr('id');

				changeOrder(id, order);
			})

			$jsonInput.val(JSON.stringify(services));
			$jsonForm.submit();
		}

		function changeOrder(id, order) {
			for (i in services) {
				if (services[i]['id'] == id) {
					services[i]['index'] = order;
					break;
				}
			}
		}

		var $jsonForm = $('#J_FORM_SERVICES'), $jsonInput = $jsonForm.children('[name="mapsJson"]'), services = $.parseJSON($jsonInput.val());
		
		confirmPanel = new ConfirmPanel('#J_CONFIRM_PANEL', {
			onReset : resort,
			onConfirm : confirm
		});
		
		
		// AJAX 请求编辑服务
		function ajaxForEdit(){
			ui.loading.show('正在与后台通信');
			var url = $(this).attr('url') || $(this).attr('href');
			url && $.get(url, function(msg){
				$('#J_EDIT_SERVICE_MODAL').html(msg);
				$("#J_FORM_EDIT_SERVICE").validationEngine();
				ui.loading.hide();
			});
			return false;
		}
		$(document).delegate('.j_btn_edit_service','click',ajaxForEdit);
	}();
    // AJAX 请求服务图例配置
    function ajaxForlegend(){
        ui.loading.show('正在与后台通信');
        var url = $(this).attr('url') || $(this).attr('href');
        url && $.get(url, function(msg){
            $('#J_LEGEND_CONFIG_MODAL').html(msg);
            $("#J_FORM_LEGEND_CONFIG").validationEngine();
            ui.loading.hide();
        });
        return false;
    }
    $(document).delegate('.j_btn_legend_config','click',ajaxForlegend);

});
