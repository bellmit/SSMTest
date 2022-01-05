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
// stop default link behavior
$(document).on('click', '[href="#"],.disabled', function(e) {
    e.preventDefault();
});

// Place any jQuery/helper plugins in here.
(function($){
	var Slider = function( elem ){
		this.slider = $(elem).find('.slider');
		this.toggles = $(elem).find('.toggles').children('a');
		this.items = this.slider.children('li');
		this.items.css('display','none');
		this.showByIdx(0);
		this.autoRun();
	};
	
	Slider.prototype = {
		showByIdx : function( idx ){
			if( this.current === idx ) return;
			
			var self = this
				, desc = $(self.items[idx]).find('.banner-desc')
				, top = 0;
			
			this.items.fadeOut();
			this.items.find('.banner-desc').animate({'opacity':0, 'top': top-20 + 'px'},600);
			
			$(this.items[idx]).fadeIn(800, function(){
				desc.animate({'opacity':1, 'top': top + 'px'},400);
				
			});
			                  
			this.current = idx;
			this.showToggleByIdx(idx);
			return idx;
		}
		, showToggleByIdx : function( idx ){
			$(this.toggles).addClass('cloze').removeClass('open');
			$(this.toggles[idx]).removeClass('cloze').addClass('open');
		}
		, autoRun : function( idx ){
			var start = idx ? idx : 0,
				self = this;
			this.funcAuto = setInterval(function(){
				self.current === (self.items.length - 1) && (start = 0);
				self.showByIdx( start );
				start++;
			}, 8000);
		}
	};
	
	$.fn.slider = function(){
		var slider = new Slider(this);
		this.find('.toggles').children('a').click(function(){
			clearInterval( slider.funcAuto );
			slider.showByIdx( $(this).index() );
		});
	};
}(jQuery));

(function($){
	"use strict";
	
	var defaults = {
		fetchCallback : null
		, hideCallback : function( item ){
			item.find('.icon-folder-open').addClass('icon-folder-close').removeClass('icon-folder-open');
		}
		, openCallback : function( item ){
			console.log(item);
			item.find('.icon-folder-close').removeClass('icon-folder-close').addClass('icon-folder-open');
		}
	}
	
	, options
	
	, Tree = function(option, elem){
		this.options = option;
		this.elem = $(elem);
		this.init();
	};
	
	Tree.prototype = {
		init : function(){
			var self = this;
			$(self.elem).children('tr,li').each(function(){
				self.renderItem( this );
			});
			
			$(self.elem).delegate('._treetoggle', 'click', function(){
				self.move($(this).parents('li,tr')[0]);
			});
		}
		, renderItem : function( item ){
			if( !$(item).find('._treetoggle, ._root').length ){
				var pNum = this.parentNumber( item )// parents number
				, cStatus = this.childrenStatus( item );// children status
				// add blank based on parents
				// add toggle based on children
				this.addPrefix(pNum, cStatus, item);
				return item;
			}
		}
		, parentNumber : function( item ){
			var parentId = $(item).attr('data-parent')
				, num = 0;
			if( parentId ){
				num = num + 1;
				num = num + this.parentNumber( '#'+parentId );
			}
			return num;
		}
		// 1: 有且显示着， 0：没，不需要显示； 2：有，但是目前没有显示，需要异步加载
		, childrenStatus : function( item ){
			var back = $( '[data-parent="' + $(item).attr('id') + '"]' ).length ? 1 : 0;
			if( !back && $(item).attr('data-has-child') === 'true' ){
				back = 2;
			}
			return back;
		}
		, move : function( item ){
			var id = $(item).attr('id')
				, toggle = $(item).find('._treetoggle')
				, cStatus = this.childrenStatus( item );
			if( toggle.hasClass('_open') ){
				this.hideChildren(id);
			} else if ( toggle.hasClass('_close') && cStatus === 1 ){
				this.elem.find('[data-parent='+ id +']').removeClass('hide');
				$(item).find('._close').removeClass('_close').addClass('_open');
				this.options.openCallback( $(item) );
			} else {
				this.fetch( item );
			}
		}
		, hideChildren : function( id ){
			var self = this;
			this.elem.find('[data-parent='+ id +']').each(function(){
				$(this).addClass('hide');
				self.hideChildren( $(this).attr('id') );
			});
			$('#'+id).find('._open').removeClass('_open').addClass('_close');
			self.options.hideCallback( $('#'+id) );
		}
		, addPrefix : function(pNum, cStatus, item){
			var blank = "<i class='ico ico-blank'></i>"
				, open = "<i class='_treetoggle ico _open'></i>"
				, close = "<i class='_treetoggle ico _close'></i>"
				, root = "<i class='ico _root'></i>"
				, cell;
				
			cell = $(item).children('td:first-child').length ? $(item).children('td:first-child') : $(item).children('a');
			cell.html('<span>'+ cell.html() +'</span>');
			
			switch(cStatus){
				case 1:
					cell.prepend(open);
					break;
				case 0:
					cell.prepend(root);
					break;
				case 2:
					cell.prepend(close);
					break;
			}
			
			for( var i=0 ; i<pNum ; i++ ){
				cell.prepend(blank);
			}
		}
		, fetch : function( item ){
			var self = this
				, reqUrl = $(item).attr('data-url');
			if( !reqUrl ){
				alert('没有设置标签属性data-url');
				return;
			}
			reqUrl && $.ajax({
				url : reqUrl,
				dataType : 'html',
				success : function(msg){
					var div = $('<div />');
					div.html(msg);
					$(item).after(div.html())
						   .find('._treetoggle')
						   .removeClass('_close')
						   .addClass('_open');
					$( $(item).parents('ul,table')[0] ).children('tr,li').each(function(){
						self.renderItem( this );
					});
				}
			})
		}
	};
	
	$.fn.listtree = function( option ){
		return this.each( function(){
			$(this).addClass('_listtree');
			var options = $.extend( {}, defaults, option );
			var tree = $(this).data('tree');
			if( !tree ) $(this).data('tree', (tree = new Tree(options, this)));
		});
	};
	
	$.fn.gridtree = function( option ){
		return this.each( function(){
			var options = $.extend( {}, defaults, option );
			$(this).addClass('_gridtree');
			var tree = $(this).data('tree');
			if( !tree ) $(this).data('tree', (tree = new Tree(options, this)));
		});
	}
}(jQuery));

(function($){
	var LoadingPanel = function () {
        return this;
    };

    LoadingPanel.prototype = {
        tmpl: "<div id='_loadingWrap' class='._loading_wrap'><div class='_inner'><div class='_main'></div><div class='_cover'></div></div></div>",
        
        show: function (text, ctn) {
        	$('._loading_wrap').remove();
        	var wrap = ctn || 'body'
        		, txt = text || '正在努力加载'
        		, tmpl = $(this.tmpl)
            	, loading;
            this.elem = tmpl;
            loading = this.elem.find('._main');
            loading.html(txt);
            $(wrap).css('position','relative').append(this.elem);
        	this.elem.css('display','block');
        	loading.css('margin-left', '-' + loading.outerWidth()/2 + 'px');
        	
        },

        hide: function () {
            this.elem.fadeOut('fast',function(){
                $('._loading_wrap').remove();
            });
        }
    };
   	window.loading = new LoadingPanel();
}(jQuery));

function beforeRemove(){
	$('.j_btn_del,.j_ask').click(function(){
		var ask = $(this).attr('data-ask') || '确定执行删除操作吗？'
		return window.confirm(ask);
	});
}window.beforeRemove = beforeRemove;
	
function removeLastSplit(){
	$('ul').children('li:last-child').css('border-right','none');
}

var ajax4data = function() {
    $(document).delegate('.a4d', 'click', function() {
        var self = $(this)
			, url = self.attr('data-url') || self.attr('href')
            , sCtn = self.attr('data-result-ctn')
            , method , ctn;

        if (!sCtn) {
            alert('没有设定存放结果的容器选择符');
            return false;
        } else if (sCtn.search(/\(/) !== -1) {
            method = sCtn.substring(sCtn.indexOf('(') + 1, sCtn.indexOf(')'));
            sCtn = sCtn.substring(0, sCtn.indexOf('('));
        } else {
            method = 'html';
        }
        ctn = $(sCtn);
        url && $.ajax({
            url : url,
            dataType : 'html',
            cache : false,
            success : function(msg){
                ctn[method](msg);
                //ctn.find('form').validationEngine();
            }
        });
        return false;
    });
};
ajax4data();

(function(win){
	win.geo = {}
	win.geo.codemap = {
		'2364' : 'Xian_1980_3_Degree_GK_Zone_40',
		'2363' : 'Xian_1980_3_Degree_GK_Zone_39',
		'2361' : 'Xian_1980_3_Degree_GK_Zone_37',
		'2365' : 'Xian_1980_3_Degree_GK_Zone_41',
		'2385' : 'Xian_1980_3_Degree_GK_CM_120E',
		'4610' : 'GCS_Xian_1980',
		'4326' : 'GCS_WGS_1984'
	};
	
	win.geo.codeTrans = function( id ){
		var ctn = id || document;
		$(ctn).find('.geoCode').each(function(){
			var self = $(this)
				, str = geo.codemap[self.html()];
			if(str){
				self.html( str );
			} else {
				self.html(geo.codemap('2364'));
				// alert('can\'t find code - ' + self.html());
			}
		});
	};
}(window));

( function($) {
	var LinkRemove = function(opts) {
		this.opts = opts;
	}
	
	, defaults = {
		tpl : "<a href='#' class='_linkremove j_btn_del remove hide' style='position:absolute;top:0;right:0;-webkit-transition: display .5s ease .5s;text-decoration: none;'>×</a>",
		style : {
			'color' : 'white',
			'background-color' : '#CE3939',
			'padding' : '5px 10px',
			'font' : 'normal bold 18px arial',
			'border-radius' : '0 2px 0 0'
		}
	}
	
	LinkRemove.prototype = {
		render : function(url) {
			var back = $(this.opts.tpl);
			url && back.attr('href', url);
			back.css(this.opts.style);
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
		});
	};
	
}(jQuery));