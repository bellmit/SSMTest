/* ----------------------------------
 jQuery Timelinr 0.9.55
 tested with jQuery v1.6+

 Copyright 2011, CSSLab.cl
 Free under the MIT license.
 http://www.opensource.org/licenses/mit-license.php

 instructions: http://www.csslab.cl/2011/08/18/jquery-timelinr/
 ---------------------------------- */

jQuery.fn.timelinr = function(options){
	// default plugin settings
	settings = jQuery.extend({
		orientation: 							'horizontal',	// value: horizontal | vertical, default to horizontal
		containerDiv: 						'#timeline',	// value: any HTML tag or #id, default to #timeline
		datesDiv: 								'#dates',			// value: any HTML tag or #id, default to #dates
		datesSelectedClass: 			'selected',		// value: any class, default to selected
		datesSpeed: 							'normal',			// value: integer between 100 and 1000 (recommended) or 'slow', 'normal' or 'fast'; default to normal
		issuesDiv: 								'#issues',		// value: any HTML tag or #id, default to #issues
		issuesSelectedClass: 			'selected',		// value: any class, default to selected
		issuesSpeed: 							'fast',				// value: integer between 100 and 1000 (recommended) or 'slow', 'normal' or 'fast'; default to fast
		issuesTransparency: 			0.2,					// value: integer between 0 and 1 (recommended), default to 0.2
		issuesTransparencySpeed: 	500,					// value: integer between 100 and 1000 (recommended), default to 500 (normal)
		prevButton: 							'#prev',			// value: any HTML tag or #id, default to #prev
		nextButton: 							'#next',			// value: any HTML tag or #id, default to #next
		arrowKeys: 								'false',			// value: true | false, default to false
		startAt: 									1,						// value: integer, default to 1 (first)
		autoPlay: 								'false',			// value: true | false, default to false
		autoPlayDirection: 				'forward',		// value: forward | backward, default to forward
		autoPlayPause: 						2000,					// value: integer (1000 = 1 seg), default to 2000 (2segs)
		isCanvas:false,                   //support h5 tag [canvas] to show image
		imgUploadEvent : undefined,
		canvasContext : undefined
	}, options);

	$(function(){
		/**
		 *  CanvasImgHelper  支持canvas标签展示图片
		 * @constructor
		 */
		function CanvasImgHelper($baseImageDom, $canvasDom, $imgUrl){
			this.img = new Image();
			this.canvasDom = $canvasDom;
			this.baseImageDom = $baseImageDom;
			this.imgUrl = $imgUrl;
			var self = this;

			this.img.onload = function (e) {
				self.onImageLoad(e);
			};
			this.img.onabort = function (e) {
				self.onImageAbort(e);
			};
			this.img.onerror = function (e) {
				self.onImageError(e);
			};
			self.setImage();
		}

		/**
		 *
		 * @type {{}}
		 */
		CanvasImgHelper.prototype = {
			constructor: CanvasImgHelper,
			initCanvas:function () {
				this.load(this.img.width, this.img.height);
			},
			load:function (w, h) {
				if(this.getLoaded()){
					try{
						console.log(this.imgUrl);
						this.onImageCompleted();
						if(!settings.canvasContext){
							settings.canvasContext = this.canvasDom[0].getContext("2d");
						}
						this.canvasDom.css({'display':'block'});
						this.canvasDom[0].width=w;
						this.canvasDom[0].height=h;
						var viewer = this.baseImageDom.data("ImageViewer");
						viewer.refreshView();
						w = viewer.imageWidth!=undefined?viewer.imageWidth:w;
						h= viewer.imageHeight!=undefined?viewer.imageHeight:h;
						settings.canvasContext.clearRect(0, 0, w, h);
						settings.canvasContext.drawImage(this.img, 0, 0, w, h);
						this.canvasDom.data("src", this.imgUrl);
						viewer = null;
						this.canvasDom = null;
						this.imgUrl = null;
						this.img = null;
						//ie 浏览器强制触发下浏览器自身的垃圾回收机制
						if (typeof window.CollectGarbage === 'function') {
							CollectGarbage()
						}
					}catch (e){
						console.error("canvas tag: getContext('2d') " + e);
					}
				}
			},
			onImageLoad: function (e) {
				this.imageLoaded = true;
				this.initCanvas();
			},
			onImageError: function (e) {
				this.imageLoaded = false;
				//加载出错时提供上传界面
				$(settings.issuesDiv + ' .iv-container').addClass("hidden");
				$(settings.issuesDiv + ' .record-upload-container').removeClass("hidden");
			},
			onImageAbort: function (e) {
				this.imageLoaded = false;
			},
			onImageCompleted:function () {
				$(settings.issuesDiv + ' .iv-container').removeClass("hidden");
				$(settings.issuesDiv + ' .record-upload-container').addClass("hidden");
			},
			setImage:function () {
				this.imageLoaded = false;
				this.img.src = this.imgUrl;
			},
			getLoaded: function () {
				return this.imageLoaded;
			}
		}
		
		// Checks if required elements exist on page before initializing timelinr | improvement since 0.9.55
		if ($(settings.datesDiv).length > 0 && $(settings.issuesDiv).length > 0) {
			// setting variables... many of them
			var howManyDates = $(settings.datesDiv+' li').length;
			var howManyIssues = $(settings.issuesDiv+' li').length;
			var currentDate = $(settings.datesDiv).find('a.'+settings.datesSelectedClass);
			var currentIssue = $(settings.issuesDiv).find('li.'+settings.issuesSelectedClass);
			var widthContainer = $(settings.containerDiv).width();
			var heightContainer = $(settings.containerDiv).height();
			var widthIssues = $(settings.issuesDiv).width();
			var heightIssues = $(settings.issuesDiv).height();
			var widthIssue = $(settings.issuesDiv+' li').width();
			var heightIssue = $(settings.issuesDiv+' li').height();
			var widthDates = $(settings.datesDiv).width();
			var heightDates = $(settings.datesDiv).height();
			var widthDate = $(settings.datesDiv+' li').width();
			var heightDate = $(settings.datesDiv+' li').height();

			var imgUrl = null;
			var $imgDom = $(settings.issuesDiv + " li .iv-image-view canvas");
			var $baseImageDom = $(settings.issuesDiv + " li .pannable-image");
			var monthDatas = $(settings.issuesDiv).data("issues");  //月份照片数据

			if(settings.isCanvas){
				settings.imgUploadEvent = CanvasImgHelper;
				howManyDates = monthDatas.length;
			}

			// set positions!
			if(settings.orientation == 'horizontal') {
				if(settings.isCanvas){  //single to show
					$(settings.issuesDiv).width(widthIssue);
				}else{
					$(settings.issuesDiv).width(widthIssue*howManyIssues);
				}
				$(settings.datesDiv).width(widthDate*howManyDates).css('marginLeft',widthContainer/2-widthDate/2);
				var defaultPositionDates = parseInt($(settings.datesDiv).css('marginLeft').substring(0,$(settings.datesDiv).css('marginLeft').indexOf('px')));
			} else if(settings.orientation == 'vertical') {
				$(settings.issuesDiv).height(heightIssue*howManyIssues);
				$(settings.datesDiv).height(heightDate*howManyDates).css('marginTop',heightContainer/2-heightDate/2);
				var defaultPositionDates = parseInt($(settings.datesDiv).css('marginTop').substring(0,$(settings.datesDiv).css('marginTop').indexOf('px')));
			}

			$(settings.datesDiv+' a').unbind('click').bind('click', function(event){
				event.preventDefault();
				var currentIndex = $(this).parent().prevAll().length;
				// moving the elements
				if(settings.orientation == 'horizontal') {
					if(!settings.isCanvas)
						$(settings.issuesDiv).animate({'marginLeft':-(widthIssue*currentIndex)},{queue:false, duration:settings.issuesSpeed});
				} else if(settings.orientation == 'vertical') {
					$(settings.issuesDiv).animate({'marginTop':-heightIssue*currentIndex},{queue:false, duration:settings.issuesSpeed});
				}

				// now moving the dates
				$(settings.datesDiv+' a').removeClass(settings.datesSelectedClass);
				$(this).addClass(settings.datesSelectedClass);
				if(settings.orientation == 'horizontal') {
					$(settings.datesDiv).animate({'marginLeft':defaultPositionDates-(widthDate*currentIndex)},{queue:false, duration:'settings.datesSpeed'});
				} else if(settings.orientation == 'vertical') {
					$(settings.datesDiv).animate({'marginTop':defaultPositionDates-(heightDate*currentIndex)},{queue:false, duration:'settings.datesSpeed'});
				}

				// prev/next buttons now disappears on first/last issue | bugfix from 0.9.51: lower than 1 issue hide the arrows | bugfixed: arrows not showing when jumping from first to last date
				if(!settings.isCanvas){
					// $(settings.issuesDiv+' li').animate({'opacity':settings.issuesTransparency},{queue:false, duration:settings.issuesSpeed}).removeClass(settings.issuesSelectedClass).eq(currentIndex).addClass(settings.issuesSelectedClass).fadeTo(settings.issuesTransparencySpeed,1);
					$(settings.issuesDiv+' li').stop().animate({'opacity':settings.issuesTransparency},{queue:false, duration:settings.issuesSpeed}).removeClass(settings.issuesSelectedClass).fadeTo(settings.issuesTransparencySpeed,1);
					$(settings.issuesDiv+' li').eq(currentIndex).attr("class", settings.issuesSelectedClass);
					if(howManyDates == 1) {
						$(settings.prevButton+','+settings.nextButton).fadeOut('fast');
					} else if(howManyDates == 2) {
						if($(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.prevButton).fadeOut('fast');
							$(settings.nextButton).fadeIn('fast');
						}
						else if($(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.nextButton).fadeOut('fast');
							$(settings.prevButton).fadeIn('fast');
						}
					} else {
						if( $(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.nextButton).fadeIn('fast');
							$(settings.prevButton).fadeOut('fast');
						}
						else if( $(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.prevButton).fadeIn('fast');
							$(settings.nextButton).fadeOut('fast');
						}
						else {
							$(settings.nextButton+','+settings.prevButton).fadeIn('slow');
						}
					}
				}else{
					debugger
					if(howManyDates == 1) {
						$(settings.prevButton+','+settings.nextButton).css("display", "none");
					} else if(howManyDates == 2) {
						if($(settings.datesDiv+' li a').eq(0).hasClass(settings.datesSelectedClass)) {
							$(settings.prevButton).css("display", "none");
							$(settings.nextButton).css("display", "block");
						}
						else if($(settings.datesDiv+' li a').eq(howManyDates-1).hasClass(settings.datesSelectedClass)) {
							$(settings.nextButton).css("display", "none");
							$(settings.prevButton).css("display", "block");
						}
					} else {
						if( $(settings.datesDiv+' li a').eq(0).hasClass(settings.datesSelectedClass) ) {
							$(settings.nextButton).css("display", "block");
							$(settings.prevButton).css("display", "none");
						}
						else if( $(settings.datesDiv+' li a').eq(howManyDates-1).hasClass(settings.datesSelectedClass) ) {
							$(settings.prevButton).css("display", "block");
							$(settings.nextButton).css("display", "none");
						}
						else {
							$(settings.nextButton+','+settings.prevButton).css("display", "block");
						}
					}
					//ie浏览器使用canvas时调用
					new CanvasImgHelper($baseImageDom, $imgDom, monthDatas[currentIndex].origin);
				}
			});

			$(settings.nextButton).unbind('click').bind('click', function(event){
				event.preventDefault();
				// bugixed from 0.9.54: now the dates gets centered when there's too much dates.
				var currentIndex = $(settings.issuesDiv).find('li.'+settings.issuesSelectedClass).index();
				if(!settings.isCanvas){
					if(settings.orientation == 'horizontal') {
						var currentPositionIssues = parseInt($(settings.issuesDiv).css('marginLeft').substring(0,$(settings.issuesDiv).css('marginLeft').indexOf('px')));
						var currentIssueIndex = currentPositionIssues/widthIssue;
						var currentPositionDates = parseInt($(settings.datesDiv).css('marginLeft').substring(0,$(settings.datesDiv).css('marginLeft').indexOf('px')));
						var currentIssueDate = currentPositionDates-widthDate;
						if(currentPositionIssues <= -(widthIssue*howManyIssues-(widthIssue))) {
							$(settings.issuesDiv).stop();
							$(settings.datesDiv+' li:last-child a').click();
						} else {
							if (!$(settings.issuesDiv).is(':animated')) {
								$(settings.issuesDiv).stop();
								// bugixed from 0.9.52: now the dates gets centered when there's too much dates.
								$(settings.datesDiv+' li').eq(currentIndex+1).find('a').trigger('click');
							}
						}
					} else if(settings.orientation == 'vertical') {
						var currentPositionIssues = parseInt($(settings.issuesDiv).css('marginTop').substring(0,$(settings.issuesDiv).css('marginTop').indexOf('px')));
						var currentIssueIndex = currentPositionIssues/heightIssue;
						var currentPositionDates = parseInt($(settings.datesDiv).css('marginTop').substring(0,$(settings.datesDiv).css('marginTop').indexOf('px')));
						var currentIssueDate = currentPositionDates-heightDate;
						if(currentPositionIssues <= -(heightIssue*howManyIssues-(heightIssue))) {
							$(settings.issuesDiv).stop();
							$(settings.datesDiv+' li:last-child a').click();
						} else {
							if (!$(settings.issuesDiv).is(':animated')) {
								// bugixed from 0.9.54: now the dates gets centered when there's too much dates.
								$(settings.datesDiv+' li').eq(currentIndex+1).find('a').trigger('click');
							}
						}
					}
				}else{
					currentIndex = $(settings.datesDiv +" ." + settings.datesSelectedClass).parent().prevAll().length;
					if(currentIndex>=0 && currentIndex < howManyDates-1){
						$(settings.datesDiv+' li').eq(currentIndex+1).find('a').trigger('click');
					}else if(currentIndex == howManyDates-1){
						$(settings.datesDiv+' li:last-child a').click();
					}
				}

				if(!settings.isCanvas){
					// prev/next buttons now disappears on first/last issue | bugfix from 0.9.51: lower than 1 issue hide the arrows
					if(howManyDates == 1) {
						$(settings.prevButton+','+settings.nextButton).fadeOut('fast');
					} else if(howManyDates == 2) {
						if($(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.prevButton).fadeOut('fast');
							$(settings.nextButton).fadeIn('fast');
						}
						else if($(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.nextButton).fadeOut('fast');
							$(settings.prevButton).fadeIn('fast');
						}
					} else {
						if( $(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.prevButton).fadeOut('fast');
						}
						else if( $(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.nextButton).fadeOut('fast');
						}
						else {
							$(settings.nextButton+','+settings.prevButton).fadeIn('slow');
						}
					}
				}
			});

			$(settings.prevButton).unbind('click').bind('click', function(event){
				event.preventDefault();
				// bugixed from 0.9.54: now the dates gets centered when there's too much dates.
				var currentIndex = $(settings.issuesDiv).find('li.'+settings.issuesSelectedClass).index();
				if(!settings.isCanvas){
					if(settings.orientation == 'horizontal') {
						var currentPositionIssues = parseInt($(settings.issuesDiv).css('marginLeft').substring(0,$(settings.issuesDiv).css('marginLeft').indexOf('px')));
						var currentIssueIndex = currentPositionIssues/widthIssue;
						var currentPositionDates = parseInt($(settings.datesDiv).css('marginLeft').substring(0,$(settings.datesDiv).css('marginLeft').indexOf('px')));
						var currentIssueDate = currentPositionDates+widthDate;
						if(currentPositionIssues >= 0) {
							$(settings.issuesDiv).stop();
							$(settings.datesDiv+' li:first-child a').click();
						} else {
							if (!$(settings.issuesDiv).is(':animated')) {
								// bugixed from 0.9.54: now the dates gets centered when there's too much dates.
								$(settings.datesDiv+' li').eq(currentIndex-1).find('a').trigger('click');
							}
						}
					} else if(settings.orientation == 'vertical') {
						var currentPositionIssues = parseInt($(settings.issuesDiv).css('marginTop').substring(0,$(settings.issuesDiv).css('marginTop').indexOf('px')));
						var currentIssueIndex = currentPositionIssues/heightIssue;
						var currentPositionDates = parseInt($(settings.datesDiv).css('marginTop').substring(0,$(settings.datesDiv).css('marginTop').indexOf('px')));
						var currentIssueDate = currentPositionDates+heightDate;
						if(currentPositionIssues >= 0) {
							$(settings.issuesDiv).stop();
							$(settings.datesDiv+' li:first-child a').click();
						} else {
							if (!$(settings.issuesDiv).is(':animated')) {
								// bugixed from 0.9.54: now the dates gets centered when there's too much dates.
								$(settings.datesDiv+' li').eq(currentIndex-1).find('a').trigger('click');
							}
						}
					}
				}else{
					currentIndex = $(settings.datesDiv +" ." + settings.datesSelectedClass).parent().prevAll().length;
					if(currentIndex>0 && currentIndex <= howManyDates-1){
						$(settings.datesDiv+' li').eq(currentIndex-1).find('a').trigger('click');
					}else if(currentIndex == 0){
						$(settings.datesDiv+' li:first-child a').click();
					}
				}


				if(!settings.isCanvas){
					// prev/next buttons now disappears on first/last issue | bugfix from 0.9.51: lower than 1 issue hide the arrows
					if(howManyDates == 1) {
						$(settings.prevButton+','+settings.nextButton).fadeOut('fast');
					} else if(howManyDates == 2) {
						if($(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.prevButton).fadeOut('fast');
							$(settings.nextButton).fadeIn('fast');
						}
						else if($(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass)) {
							$(settings.nextButton).fadeOut('fast');
							$(settings.prevButton).fadeIn('fast');
						}
					} else {
						if( $(settings.issuesDiv+' li:first-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.prevButton).fadeOut('fast');
						}
						else if( $(settings.issuesDiv+' li:last-child').hasClass(settings.issuesSelectedClass) ) {
							$(settings.nextButton).fadeOut('fast');
						}
						else {
							$(settings.nextButton+','+settings.prevButton).fadeIn('slow');
						}
					}
				}
			});
			// keyboard navigation, added since 0.9.1
			if(settings.arrowKeys=='true') {
				if(settings.orientation=='horizontal') {
					$(document).keydown(function(event){
						if (event.keyCode == 39) {
							$(settings.nextButton).click();
						}
						if (event.keyCode == 37) {
							$(settings.prevButton).click();
						}
					});
				} else if(settings.orientation=='vertical') {
					$(document).keydown(function(event){
						if (event.keyCode == 40) {
							$(settings.nextButton).click();
						}
						if (event.keyCode == 38) {
							$(settings.prevButton).click();
						}
					});
				}
			}
			// default position startAt, added since 0.9.3
			$(settings.datesDiv+' li').eq(settings.startAt-1).find('a').trigger('click');
			// autoPlay, added since 0.9.4
			if(settings.autoPlay == 'true') {
				setInterval(autoPlay, settings.autoPlayPause);
			}
		}
	});
};


// autoPlay, added since 0.9.4
function autoPlay(){
	var currentDate = $(settings.datesDiv).find('a.'+settings.datesSelectedClass);
	if(settings.autoPlayDirection == 'forward') {
		if(currentDate.parent().is('li:last-child')) {
			$(settings.datesDiv+' li:first-child').find('a').trigger('click');
		} else {
			currentDate.parent().next().find('a').trigger('click');
		}
	} else if(settings.autoPlayDirection == 'backward') {
		if(currentDate.parent().is('li:first-child')) {
			$(settings.datesDiv+' li:last-child').find('a').trigger('click');
		} else {
			currentDate.parent().prev().find('a').trigger('click');
		}
	}
}
