/*
 * onemap server project common javascript
 * 
 * @Author: ray zhang
 * @date: 2013-04-05 13:23
 * 
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 */


$(document).ready(function(){
	// Avoid `console` errors in browsers that lack a console.
	(function() {
	    var method;
	    var noop = function () {};
	    var methods = [
	        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
	        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
	        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
	        'timeStamp', 'trace', 'warn'
	    ];
	    var length = methods.length;
	    var console = (window.console = window.console || {});
	
	    while (length--) {
	        method = methods[length];
	
	        // Only stub undefined methods.
	        if (!console[method]) {
	            console[method] = noop;
	        }
	    }
	}());
	// placeholder hack
	(function(){
		var doc = document,
		inputs = doc.getElementsByTagName('input'),
		supportPlaceholder = 'placeholder' in doc.createElement('input'),
		placeholder = function(input){
			var text = input.getAttribute('placeholder'),
				defaultValue = input.value;

			if( !defaultValue ){
				input.value = text;
			}
			input.onfocus = function(){
				if(input.value === text){
					this.value = '';
				}
			}
			input.onblur = function(){
				if(input.value === ''){
					this.value = text;
				}
			}
		};

		if(!supportPlaceholder){
			for(var i = 0, len = inputs.length; i < len; i++){
				var input = inputs[i], text = input.getAttribute('placeholder');
				if(input.type === 'text' && text){
					placeholder(input);
				}
			}    
		}
	}());
	
    // stop default link behavior
    $(document).on('click', '[href="#"],.disabled', function(e) {
        e.preventDefault();
    });
	/*
	 * 
	 * 页面效果交互部分开始
	 * 
	 * 
	 */
	// === Sidebar navigation === //
	$('.submenu > a').click(function(e){
		e.preventDefault();
		var submenu = $(this).siblings('ul');
		var li = $(this).parents('li');
		var submenus = $('#sidebar li.submenu ul');
		var submenus_parents = $('#sidebar li.submenu');
		if(li.hasClass('open'))
		{
			if(($(window).width() > 768) || ($(window).width() < 479)) {
				submenu.slideUp();
			} else {
				submenu.fadeOut(250);
			}
			li.removeClass('open');
		} else 
		{
			if(($(window).width() > 768) || ($(window).width() < 479)) {
				submenus.slideUp();			
				submenu.slideDown();
			} else {
				submenus.fadeOut(250);			
				submenu.fadeIn(250);
			}
			submenus_parents.removeClass('open');		
			li.addClass('open');	
		}
	});
	
	var ul = $('#sidebar > ul');
	
	$('#sidebar > a').click(function(e){
		e.preventDefault();
		var sidebar = $('#sidebar');
		if(sidebar.hasClass('open'))
		{
			sidebar.removeClass('open');
			ul.slideUp(250);
		} else 
		{
			sidebar.addClass('open');
			ul.slideDown(250);
		}
	});
	
	$('#J_PHONE_NAV_TOP').html($('#sidebar > ul > .active > a').html());
	
	// === Resize window related === //
	$(window).resize(function()
	{
		if($(window).width() > 479)
		{
			ul.css({'display':'block'});	
			$('#content-header .btn-group').css({width:'auto'});		
		}
		if($(window).width() < 479)
		{
			ul.css({'display':'none'});
			fix_position();
		}
		if($(window).width() > 768)
		{
			$('#user-nav > ul').css({width:'auto',margin:'0'});
            $('#content-header .btn-group').css({width:'auto'});
		}
		
		workbench();
		
	});
	
	if($(window).width() < 468)
	{
		ul.css({'display':'none'});
		fix_position();
	}
	if($(window).width() > 479)
	{
	   $('#content-header .btn-group').css({width:'auto'});
		ul.css({'display':'block'});
	}
	
	// === Tooltips === //
	$('.tip').tooltip();	
	$('.tip-left').tooltip({ placement: 'left' });	
	$('.tip-right').tooltip({ placement: 'right' });	
	$('.tip-top').tooltip({ placement: 'top' });	
	$('.tip-bottom').tooltip({ placement: 'bottom' });	
	
	// === Fixes the position of buttons group in content header and top user navigation === //
	function fix_position(){
		var uwidth = $('#user-nav > ul').width();
		$('#user-nav > ul').css({width:uwidth,'margin-left':'-' + uwidth / 2 + 'px'});
        
        var cwidth = $('#content-header .btn-group').width();
        $('#content-header .btn-group').css({width:cwidth,'margin-left':'-' + uwidth / 2 + 'px'});
	}
	
	// === Workbench === //
	function workbench(){
		$('#footer').css({'position':'absolute','bottom':'0'});
		
		var cheight = $(window).height() - $('#header').height();
		$('#content').css('min-height',cheight - 18);
		$('#content').css('height','auto');
		
	}workbench();
	
	/*!
	 *render form elements
	 */
	function renderInputs(ctn){
		
		
		var $ctn;
		if( ctn ){
			if (typeof ctn === 'string'){
				$ctn = $('#'+ctn);
			} else {
				$ctn = $(ctn);
			}
    		//$ctn.find('input[type=checkbox],input[type=radio],input[type=file]').uniform();
    		$ctn.find('select').select2();
    	} else {
    		//$('input[type=checkbox],input[type=radio],input[type=file]').uniform();
			$('.uploader .filename').html('');
			$('.uploader .action').html('选择文件');
			$('select').select2();
    	}
    }window.renderInputs = renderInputs;
	if( !$.browser.msie ){
		$(document).delegate('input[type=checkbox],input[type=radio]', 'click', function(){
			$(this).is(':checked') ? $(this).parent('span').addClass( 'checked' ) : $(this).parent('span').removeClass( 'checked' );
		});
	}
    renderInputs();
	
	
	(function (glbl, $) {
		/*! loading  */
		glbl.ui = {};

		var LoadingWin = function (text) {
			this.$elem = $(this.tmpl);
			this.setText(text);
			$('body').append(this.$elem);
			this.$loading = this.$elem.find('._gtis_ui_loading');
			this.$cover = this.$elem.find('._gtis_ui_cover');
			return this;
		};

		LoadingWin.prototype = {
			tmpl: "<div id='_GTIS_UI_LOADING_WIN'><div class='_gtis_ui_loading'></div><div class='_gtis_ui_cover'></div></div>",

			setText: function (text) {
				$(this.$loading).html(text + '...');
			},

			show: function (text) {
				this.$cover.css('display','block');
				this.$loading.fadeIn();
				text && this.setText(text);
				var loading = this.$loading
					, ml = loading.width();
				loading.css('margin-left', '-' + ml + 'px');
			},

			hide: function () {
				var self = this;
				this.$loading.fadeOut('fast',function(){
					self.$cover.css('display','none');
				});
			}
		};
		glbl.loading = new LoadingWin();

		/*! pagin  */
		function refreshPagin(dom, page, size, count, max, pre) {
			Array.prototype.contains = function(needle) {
				for (i in this) {
					if (this[i] == needle)
						return true;
				}
				return false;
			}
			
			if( !dom ) dom = '#J_PAGIN';
			if( !max ) max = 10;
			
			var maxnum = 5, 
				pagin = $(dom),
                pagePre = 'page',
                sizePre = 'size',
				prev = pagin.children('.prev'), 
				next = pagin.children('.next'), 
				href = location.href, prefix = '?';
			
			if (!pre) {
				if (href.indexOf('?') > -1 && href.indexOf(pagePre) == -1) {
					prefix = href + '&';
				} else if (href.indexOf(pagePre) > -1) {
					prefix = href.substring(0, href.indexOf(pagePre));
				} else {
					prefix = '?';
				}
			} else {
				prefix = pre;
			}
			
			var total = Math.ceil(count / size);
			
			if (page == 1) {
				prev.addClass('disabled');
			}
			if (page == total) {
				next.addClass('disabled');
			}
			
			pagin.children('span').children().remove();
            for (i = 0; i < total; i++) {
				var a = $('<a />').attr({
					'href' : prefix + pagePre + '=' + i + '&' + sizePre + '=' + size + ''
                }).html(i + 1);
				pagin.children('span').append(a);
				if (i == page) {
					a.addClass('active');
					a.attr('href', '#');
				}
			}
			
			prev.attr({
				'href' : prefix + pagePre + '=' + (page - 1) + '&' + sizePre + '=' + size + ''
			});
			next.attr({
				'href' : prefix + pagePre + '=' + (page + 1) + '&' + sizePre + '=' + size + ''
			});
			
			function simplify(page) {
				var items = pagin.children('span').children('a'), 
					active = pagin.children('span').children('.active'), 
					aIndex, 
					disNum = [];
					
				if (items.length > max) {
					// get active index
					aIndex = active.index() + 1;
			
					items.each(function(i, link) {
						if (disNum.length >= max)
							return false;
			
						i = i + 1;
			
						if (i === aIndex || i === aIndex - 1 || i === aIndex + 1 || i === 1 || i === 2 || i === items.length - 1 || i === items.length) {
							disNum.push(i);
							return;
						}
					})
					for (var i = aIndex - 2; i > 2; i--) {
						if (disNum.length >= max)
							break;
						disNum.contains(i) || disNum.push(i);
					}
			
					for (var i = aIndex + 2; i < items.length - 1; i++) {
						if (disNum.length >= max)
							break;
						disNum.contains(i) || disNum.push(i);
					}
			
					disNum.sort(function(a, b) {
						return a > b;
					});
			
					items.each(function(i, link) {
						i = i + 1;
						disNum.contains(i) || $(this).remove();
					});
			
					items = pagin.children('span').children('a');
			
					for (var i = 1; i < disNum.length; i++) {
						if (disNum[i] - disNum[i - 1] > 1) {
							$(items[i - 1]).after('<strong> ... </strong>');
						}
					}
				}
			}
			simplify(page);
			pagin.find('a').addClass('j_pagin_link');
		}glbl.refreshPagin = refreshPagin;
		
	}(window, window.jQuery));
	
	
	
	
	
	
	

	/*!
	 * @name: 启禁用
	 */
	$('tr.j_disabled td').css('opacity',.4);
	$('tr.j_enabled td').css('opacity',1);
	// $('tr').css('transition','opacity .5s ease');
	$(document).delegate('.j_enable_disable', 'click', function(){
		var self = $(this)
			, reqUrl = self.attr('url') || self.attr('href')
			, tr = self.parents('tr');
		
		if(!reqUrl || reqUrl === "#"){
			alert('没有设定url或href');
			return false;
		}else{
			reqUrl = reqUrl + "&d=" + new Date();
		}
		
		$.get(reqUrl, function(msg){
			if( msg === "success" ){
				if( tr.hasClass("j_disabled") ){
					self.removeClass('btn-success').addClass('btn-danger').html('禁用');
					tr.removeClass('j_disabled').addClass('j_enabled');
					alerts('启用成功','success');
				} else if ( tr.hasClass("j_enabled") ){
					self.removeClass('btn-danger').addClass('btn-success').html('启用');
					tr.removeClass('j_enabled').addClass('j_disabled');
					alerts('禁用成功','success');
				}
				$('.j_disabled td').css('opacity',.4);
				$('.j_enabled td').css('opacity',1);
			}else{
				alerts('操作失败','error',msg);
			}
		});
		
		return false;
	});
	/*!
	 * ajax请求一些数据，将抓取到的数据显示在指定列表中
	 *
	 */
	function ajax4Data() {
		$(document).delegate('.j_ajax_for_data, .j_ajax4data', 'click', function() {
			var url = $(this).attr('url') || $(this).attr('href')
				, sCtn = $(this).attr('data-result-ctn') || $(this).attr('gtdata-result')
				, method;
				
			if (!sCtn) {
				alert('没有设定存放结果的容器选择符');
				return false;
			} else if( sCtn.search(/\(/) !== -1 ) {
				method = sCtn.substring( sCtn.indexOf('(') + 1, sCtn.indexOf(')') );
				sCtn = sCtn.substring( 0, sCtn.indexOf('(') );
			} else {
				method = 'html';
			}
			var ctn = $(sCtn);
			loading.show('正在请求数据');
			url && $.get(url, function(msg) {
				ctn[method](msg);
				renderInputs(ctn);
				ctn.find('form').validationEngine();
				loading.hide();
			});
			return false;
		});
	}ajax4Data();
	
	/*!
	 * 显示alert信息
	 */
    window.alerts = function( msg, type, memo, id ){
		var a = id && $('#'+id) || $('#J_ALERT'),
			ch = a.children('.j_alert_ctn');
		type && a.removeClass('alert-success alert-error').addClass('alert-'+type);
		msg && ch.html("<strong>" + msg + "</strong>&nbsp;&nbsp;");
		memo && ch.append(memo)
		
		$.trim( ch.html() ) && a.fadeIn(1000,function(){
			setTimeout(function(){
				a.fadeOut();
			},5000);
		});
    }
	alerts();
/*!
 * ajax提交表单插件
 */
( function($) {
		$.fn.ajaxSubmit = function(opts) {
			this.validationEngine();
			var form = this;
			if (!form.attr('action')) {
				alert(form.attr('id') + '的action没有设定');
				return;
			}

			if (!form.validationEngine('validate')) {// 为form进行validationEngine插件验证，如果验证失败退出
				return;
			}
			
			opts.loading && loading.show( opts.loading );
			
			$.ajax({
				url : form.attr('action'),
				type : form.attr('method') || 'get',
				data : form.serialize(),
				success : function(msg) {
					opts.callback && opts.callback(msg);
					loading.hide();
				}
			});
			return this;
		};

		$(document).delegate('.j_ajax_form', 'click', function() {
			var fId = $(this).attr('data-form');
			if (fId) {
				$('#' + fId).ajaxSubmit();
			} else {
				$($(this).parents('form')[0]).ajaxSubmit();
			}
			return false;
		});
	}(jQuery));
/*!
 * 右上角删除 v0.2.1
 * 
 * log:
 * 0.2 增加style自定义
 * 0.2.1 将url改为data-url
 */
( function($) {
		var LinkRemove = function(opts) {
			this.opts = opts;
		}
		, defaults = {
			tpl : "<a href='#' class='_linkremove j_btn_del remove hide' style='position:absolute;top:0;right:0;-webkit-transition: display .5s ease .5s;text-decoration: none;'>×</a>"
			, style : {
				'color' : 'white',
				'background-color' : '#CE3939',
				'padding' : '5px 10px',
				'font' : 'normal bold 18px arial'
			}
		}

		LinkRemove.prototype = {
			render : function(url) {
				var back = $(this.opts.tpl);
				url && back.attr('href', url);
				back.css( this.opts.style );
				return back;
			}
		}

		$.fn.closeremove = function(opts) {
			return this.each(function() {
				var self = $(this)
				, options = $.extend(true, {}, defaults, opts);
				self.css('position', 'relative');
				$link = new LinkRemove(options).render(self.attr('data-url'));
				self.append($link);
				self.hover(function() {
					$(this).children('._linkremove').css('display', 'block');
				}, function() {
					$(this).children('._linkremove').css('display', 'none');
				});
				
				$(this).children('._linkremove').click(function(){
					return window.confirm('确定删除吗？');
				});
			});
		}
}(jQuery));
    /*
     * @name:	绑定AJAX提交表单函数
     * 
     * @param:	要提交的表单 ID, 触发表单提交的按钮 ID, 触发的事件类型， 回调函数， 等待提交时显示的字符
     */
    function ajaxSubmit(formId, triggerId, eventType, callback, waitText){
        $('#'+triggerId).live(eventType,function(){
            var form = $("#"+formId);
            if( !form.attr('action') ){
                console.log(form.attr('id') + '的action没有设定');
                return;
            }
            
            if( !form.validationEngine('validate') ){				// 为form进行validationEngine插件验证，如果验证失败退出
            	return;
            }
            
            loading.setText(waitText);
            loading.show();
            $.ajax({
                url : form.attr('action'),
                type : form.attr('method') || 'get',
                data : form.serialize(),
                success: function(msg){
                    callback(msg);
                    beforeRemove();
                    loading.hide();
                }
            })
        });
    }window.ajaxSubmit = ajaxSubmit;
    
    /*!
     * @name: 删除前的confirm
     */
    function beforeRemove(){
    	$('.j_btn_del,.j_ask').click(function(){
			var ask = $(this).attr('data-ask') || '确定执行删除操作吗？'
    		return window.confirm(ask);
   	 	});
    }window.beforeRemove = beforeRemove;
    beforeRemove();
    
    /*!
     * @name:	AJAX删除函数
     * 
     * @param:	传入数据列表的ID，触发事件句柄，回调
     * @notice:	需要将 ajax 请求 url 写在 href 上, 然后触发删除事件的按钮添加一个 .j_btn_del class
     */
    function ajaxRemove(containerId, eventType, callback){
    	$('#'+containerId).delegate('.j_btn_del', eventType, function(){
    		loading.setText('正在删除');
    		loading.show();
    		$.get( $(this).attr('href'), function(msg){
    			loading.hide();
    			callback( msg );
    			beforeRemove();
    		} );
    		return false;
    	});
    }window.ajaxRemove = ajaxRemove;
    
    // clear form
    function clearForm(){
    	$('.j_btn_clear').click(function(){
    		var $form = $(this).parents('form'),
    			$inputs = $form.find('input[type="text"],input[type="password"],textarea'),
    			$radio = $form.find(':radio:first'),
    			$select = $form.find('select');
    			
    		$inputs.val('');
    		$radio.click();
    		$radio.click();
    		$select.children(':first-child');
    	});
    }clearForm();
	
    // 提交表单显示loading窗口
    $('[type="submit"]').click(function(){
		var loadStr = $(this).attr('data-loading') || '正在与后台通信';
    	loading.show(loadStr);
    });
	
	// 全局validation engine
    var _V_SET = {
	    	onFieldFailure : function(ui, info){
	    		var cg = $( $(ui).parents('.control-group')[0] );
				
				cg.removeClass('success').addClass('error');
				if( info ){
					var infos = info && info.replace(/\<br\/\>/g,"@").split('@');
					$(ui).next('.help-inline').html(infos[0]);
				}
	    		loading.hide();
	    	},
	    	
			onFieldSuccess : function(ui){
	    		var cg = $( $(ui).parents('.control-group')[0] );
				cg.removeClass('error');
				$(ui).next('.help-inline').html('');
	    	},
	    	
			showPrompts : false,
			scroll : false,
			dataType : "html"
	    };
    $.validationEngine.defaults = $.extend({}, $.validationEngine.defaults, _V_SET);
	$('form').validationEngine();
	
	$('.datepicker').datetimepicker({
		dateFormat: "yy-mm-dd"
		, maxDate : 0
		, monthNames: [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ]
		, timeFormat: "HH:mm"
		, hourText : "时"
		, minuteText : "分"
		, timeText : "时间"
		, currentText : "现在"
		, closeText : "OK"
	});
});