$(document).ready(function(){
	$('.banner').slider();
	removeLastSplit();
	beforeRemove();
	
	setTimeout(function(){
        $('.accordion').affix({
                offset: {
                    top: function() {
                        return $(window).width() <= 980 ? 150 : 140
                    }
                }
         });
	}, 100);
});
