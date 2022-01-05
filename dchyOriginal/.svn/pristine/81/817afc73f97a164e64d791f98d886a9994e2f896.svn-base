/*
 * 功能配置用到的js
 *
 * @author: Ray Zhang
 * @datetime: 2013-04-23 15:17
 */
$(document).ready(function() {
	// 基本
	var Base = {
		id : "",
		name : "",
		set : function(name, val) {
			this[name] = val;
		},
		get : function(name) {
			return this[name];
		},
		createInstance : function(name, obj, ext) {
			var back = $.extend(true, {}, klass[name], obj);
			back = ext ? $.extend({}, back, ext) : back;
			return back;
		}
	}
	var klass = {}
		, operators = '<div class="select-wrap"><select name="operator"><option value="like">like</option><option value=">">></option><option value="<"><</option><option value="=">=</option><option value="!=">!=</option></select></div>';
	// 字段类
	klass.Field = $.extend({}, Base, {
		objectName : "Field",
		render : function( isQueryFields ){
			var self = this
				, showName = self.alias;
			!self.alias && (showName = self.name);
			var back = $("<li data-self='"+this.id+"' data-parent='J_TOGGLE_"+this.parentId+"' data-name='"+this.name+"' data-alias='"+this.alias+"' style='display:list-item'><a href='#'>"+showName+"</a></li>");
			back.children('a').append('<a href="#" class="remove">x</a>');
			if( isQueryFields ){
				var o = $(operators);
				o.children('select').children().each(function(){
					if( self.operator && self.operator === $(this).val() ){
						$(this).attr('selected','selected');
					}
				});
				back.children('a').append(o);
			}
			return back;
		}
	});
	// 图层类
	klass.Layer = $.extend({}, Base, {
		objectName : "Layer"
	});
	// 配置类
	klass.Config = $.extend({}, Base, {
		objectName : "Config"
	});
	// 功能类
	klass.Func = $.extend({}, Base, {
		objectName : "Func"
	});
	

	// 拖动开始的位置
	$('.j_sortable_start').sortable({
		opacity : .8,
		helper : 'clone',
		revert : 200,
		stop : function() {
			$(this).sortable('cancel');
		}
	});
	// 多行拖动结束的位置
	$('.j_sortable_end').sortable({
		revert : 150,
		receive : function(e, ui) {
			var item = ui.item.clone();
			item.children('a').append('<a class="remove">x</a>');
			if ( $(this).attr('name') === 'queryFields.fields' ){
				item.children('a').append(operators);
			}
			var isExsit = false, tmp = 0;

			$(this).children('li').each(function(i) {
				if ($(this).attr('data-self') == item.attr('data-self')) {
					if (tmp == 1) {
						isExsit = true;
					} else {
						tmp++;
					}
				}
			});
			!isExsit && $(this).append(item);
			
			var controlGroup = $(this).parent().parent()
			controlGroup.hasClass('error') && controlGroup.removeClass('error');
		}
	});
	// 单行拖动结束的位置
	$('.j_sortable_end_single').sortable({
		revert : 150,
		receive : function(e, ui) {
			if ($(this).children('li').length <= 1) {
				var nItem = ui.item.clone();
				nItem.children('a').append('<a class="remove">x</a>');
				$(this).append(nItem);
				
				var controlGroup = $(this).parent().parent();
				controlGroup.hasClass('error') && controlGroup.removeClass('error');
			}
		}
	});
	// 点击移除
	$('.j_sortable_end, .j_sortable_end_single').delegate('.remove', 'click', function() {
		var pId = $(this).parent().parent().attr('data-parent')
		, sItem = $(this).parent().parent()
		, sId = sItem.attr('data-self')
		, toggle = $('#' + pId)
		, list = $('#' + toggle.attr('href').substring(1));

		list.height() <= 0 && toggle.click();

		sItem.fadeOut("fast", function() {
			var list = toggle.parent().parent().find('ul').children('li');
			list.each(function(i) {
				if ($(this).attr('data-self') == sId) {
					$(this).focusresult();
				}
			});
			inputs = sItem.parent().parent().parent().find(':input').val('');
			sItem.remove();
		});
	});
	
	
	
	

	var form = document.getElementById('J_FORM_FUNC'),
	  	$form = $(form),
	  	$formWrap = $form.parent().parent(),
	  	$mainForm = $('#J_FUNCTION');
	// 禁用右侧表单
	function disableForm() {
		var div = $('<div class="_j_cover" style="border-radius: 4px;position: absolute; width: 100%; height: 100%; top:0; left: 0; z-index: 1000; background-color:white; opacity: 0; filter:alpha(opacity=50)"></div>')
		var position = $('.form-wrap').css('position');
		$('.form-wrap').css({
			'position' : 'relative',
			'opacity' : '.6'
		}).append(div);
		return position;
	}
	// 启用右侧表单
	function enableForm(position) {
		if ( !$('.form-wrap').children('._j_cover').length )
			return;
		$('.form-wrap').css({
			'position' : position,
			'opacity' : '1'
		}).children('._j_cover').remove();
		$(".j_sortable_start").sortable("option", "connectWith", "ul");
	}
	// 点击 .accordion-toggle 根据功能类型获取该图层对应的功能
	function bindClickAndQuery(position) {
		$('.accordion-toggle').click(function() {
			clearForm();
			enableForm(position);
			var layId = $(this).attr('id').substring(9),
				lalias = $(this).attr('data-alias'),
				lname = $(this).attr('name'),
				lIndex = $(this).attr('data-layerIndex'),
				layers = func.config.layers;
			
			for( i in layers ){
				if( layId == layers[i].id ){
					renderFuncForm(layers[i]);
					$formWrap.removeClass('create').addClass('update');
					return;
				}
			}
			$formWrap.addClass('create').removeClass('update');
			renderFuncForm(Base.createInstance('Layer',{
				id : layId ,
				layerName : lname,
				alias : lalias,
				layerIndex : parseInt(lIndex)
			}));
		});
	}
	// 将多级对象格式化成一级对象
	function multipleToSingle(obj, prefix){
		var back = {};
		prefix ? (prefix = prefix+".") : (prefix = "");
		for( i in obj ){
			if( Object.prototype.toString.apply(obj[i]) !== '[object Object]' || obj[i]['objectName'] === 'Field'){
				back[prefix + i] = obj[i];
			} else {
				var sObj = multipleToSingle(obj[i], i);
				$.extend(true, back, sObj);
			}
		}
		return back;
	}
	// 渲染表单
	function renderFuncForm(layer){
		var sLayer = multipleToSingle(layer);
		var elems = $form.find(':input,ul,:checkbox,:radio');
		
		for ( i in sLayer){
			elems.each(function(){
				var self = $(this);
				if( self.attr('name') === i ){
					switch(self.context.nodeName){
						case "INPUT":
							if(self.attr('type') === "checkbox"){
								
								self.attr('checked',true);
								
							} else if(self.attr('type') === "radio"){
								
								if( self.val() == sLayer[i] ){
									self.attr('checked',true);
									self.parent('label').click();
								}else{
									self.attr('checked',false);
								}
								
							} else {
								self.val(sLayer[i]);
							}
							break;
						case "TEXTAREA":
							self.val(sLayer[i]);
							break;
						case "SELECT":
							self.children().each(function(){
								if($(this).val() == sLayer[i]){
									self.val(sLayer[i]);
								}
							});
							break;
						case "UL":
							if( Object.prototype.toString.apply(sLayer[i]) === '[object Array]' ){
								 for( idx in sLayer[i]){
								 	var field = Base.createInstance('Field', sLayer[i][idx])
										, tpl;
									if( i==='queryFields.fields'){
										tpl = field.render( true );
									} else {
										tpl = field.render();
									}
								 	self.append(tpl);
								 }
							} else if(typeof sLayer === 'object') {
								self.append( Base.createInstance('Field', sLayer[i]).render() );
							}
							break;
					}
				}
			});
		}
	}
	// 清空表单
	function clearForm() {
		form.reset();
		$form.find('ul.drag-text, ul.drag-textarea').children().remove();
		$form.find('input[type="hidden"]').val('');
	}
	// 根据指定的字符串创建对象
	function createLabelledObject(name, value) {
		var back = {}, p;
		if (name && name.indexOf('.') > -1) {
			var nArray = name.split('.');
			for ( i = 0; i < nArray.length; i++) {
				if (i == 0) {
					if (back[nArray[i]]) {
						back[nArray[i]] = $.extend({}, back[nArray[i]]);
					} else {
						back[nArray[i]] = {};
					}
					p = back[nArray[i]];
				} else if (i == (nArray.length - 1)) {
					p[nArray[i]] = value;
				} else {
					if (p[nArray[i]]) {
						p[nArray[i]] = $.extend({}, p[nArray[i]]);
					} else {
						p[nArray[i]] = {};
					}
					p = p[nArray[i]];
				}
			}
		} else if(name){
			back[name] = value;
		}
		return back;
	}
	// 获取当前图层的配置信息
	function getLayerConfig() {
		var layer = $form.serializeObject();
		
		$form.find('.j_sortable_end_single').each(function(){
			var child = $(this).children(':first-child');
				
			if( child.html() ){
				var obj = Base.createInstance('Field', {
					id : child.attr('data-self'),
					name : child.attr('data-name'),
					alias : child.attr('data-alias'),
					parentId : child.attr('data-parent').substring(9)
				 });
				
				back = createLabelledObject($(this).attr('name'), obj);
				
				$.extend(true, layer, back);
			}
		});
		
		$form.find('.j_sortable_end').each(function() {
			var back = [], 
				name = $(this).attr('name');

			$(this).children().each(function() {
				var select = $(this).find('select[name=operator]')
					, ext = {};
				select.length && (ext = {'operator' : select.children(':selected').val()})
				
				back.push(Base.createInstance('Field', {
					id : $(this).attr('data-self'),
					name : $(this).attr('data-name'),
					alias : $(this).attr('data-alias'),
					parentId : $(this).attr('data-parent').substring(9)
				}, ext));
			});

			var list = createLabelledObject(name, back);
			$.extend(true, layer, list);
		});
		
		return layer;
	}
	//提交最终表单
	function submitMainForm(){
		$mainForm.children('input[name="config"]').val(JSON.stringify(func.config));
		$mainForm.ajaxSubmit({
			callback : function(msg) {
				var alert = $('#J_ALERT_2');
				alert.attr('class', 'alert hide alert-' + msg);
				if (msg == "success") {
					alert.find('strong').html('操作成功');
				} else {
					alert.find('strong').html('操作失败');
				}
				loopLayersAndMark();
				alert.fadeIn(500, function() {
					setTimeout(function() {
						alert.fadeOut();
					}, 4000);
				});
			}
		});
	}
	// 绑定表单清空、保存、删除操作
	function bindNormalForm() {
		$('[type="reset"]').click(clearForm);
		
		$('#J_LINK_REMOVE').click(function(){
			if(window.confirm('确定删除当前功能配置？')){
				var layer = getLayerConfig(),
					layers = func.config.layers,
					isRemoved = false;
				
				for( i in layers){
					if(layers[i].id == layer.id){
						layers.splice(i,1);
						isRemoved = true;
					}
				}
				
				if(isRemoved){
					clearForm();
					//disableForm();
					submitMainForm();
					$formWrap.addClass('create').removeClass('update');
				}
			}
			return res;
		})
		
		$form.validationEngine();
		
		$('#J_SUBMIT').click(function() {
			var passed = true;
			
			if( !$form.validationEngine('validate') ){
				passed = false;
			}
			
			$form.find('.j_required').each(function() {
				if ($(this).children().length <= 0) {
					$(this).parent().parent().addClass('error');
					passed = false;
				}
			});
			
			if (passed) {
				var layer = getLayerConfig()
					, layers = func.config.layers
					, isUpdated = false;
					
				for( i in layers){
					if(layers[i].id == layer.id){
						layers[i] = layer;
						isUpdated = true;
					}
				}
				
				!isUpdated && layers.push(layer);
				submitMainForm();
				$formWrap.removeClass('create').addClass('update');
			}
			return passed;
		});
	}
	//标记已经配置的图层
	function loopLayersAndMark(){
		var tpl = '<span class="text-success" style="position:absolute;right:5px;font-size:12px;">已配置</span>';
			
			if( func.config ){
				layers = func.config.layers;
			} else {
				return;
			}
			
			$('#J_ACCORDION').find('.accordion-heading').each(function(){
				
				var $link = $(this).children('a')
					, id = $link.attr('id').substring(9)
					, isExsit = false;
				if(layers.length === 0){
					$link.children('span').remove();
				}
				
				for( i in layers){
					if(layers[i].id == id){
						isExsit = true;
					}
				}
				if(isExsit){
					$link.css('position','relative');
					$link.append(tpl);
				} else {
					$link.children('span').remove();
				}
			});
	}
	//固定表单面板
	function fixFormPanel(){
		var fix = $('#J_FIX')
			, child = $(fix.children()[0]);
		$('.J_auto_high').scroll(function(){
			if( $(this).scrollTop() > 90 ){
				fix.css({'position':'absolute','top':'70px','width': child.width()+'px'});
			}else{
				fix.css('position','static');
			}
		});
	}

	// Main
	function main() {
		loopLayersAndMark();
		bindClickAndQuery(disableForm());
		bindNormalForm();
		fixFormPanel();
	}
	main();
	
	// 点击fields html设置相应的字段为必选同时将fields或html设为非必选
	window.choseDisplayFeature = function( feature ){
		if(feature === 'fields')
			$('ul[name=returnFields]').hasClass('j_required') || $('ul[name=returnFields]').addClass('j_required');
		else
			$('ul[name=returnFields]').removeClass('j_required');
			
	}
});