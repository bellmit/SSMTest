!(function(){
	$(document).ready(function(){
		
		// === Workbench === //
		function workbench(){
			var cheight = $(window).height() - $('#header').height() - $('.toolbar').height();
			$('.J_auto_high').css( {'height':cheight - 53, 'overflow-y':'auto'});
			$('.J_no_overflow').css('overflow-y','hidden');
		}
		workbench();
		
		//===  Window resize related ===//
		$(window).resize(function(){
			workbench();
		});
		
	});
})();