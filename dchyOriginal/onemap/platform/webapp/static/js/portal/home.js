$(document).ready(function(){
	selectNavGuide(0, 'J_NAV_HEAD');
	
	// 以下是测试代码
	/*function fetchNum(id, c){
		var num = parseInt( $('#' + id).html() ),
			t = num + c,
			loop = 0;
		
		var inter = setInterval(function(){
			
			if( t > num ){
				$('#' + id).html(num);
				num++;
			} else if ( t < num) {
				$('#' + id).html(num);
				num--;
			} else {
				$('#' + id).html(num);
				clearInterval(inter);
				if( loop !== 0 ){
					focus();
				}
			}
			
			loop++;
		},50);
		
		function focus(){
			var fs;
			if($('#' + id).parent().hasClass('top')){
				fs = '40px';
			}else{
				fs = '26px';
			}
			
			var large = parseInt( fs.replace('px','') ) + 10;
			$('#' + id).css('font-size', large + 'px');
			setTimeout(function(){
				$('#' + id).css('font-size',fs);
			},200);
		}
	}
	
	fetchNum('J_MAP_NUM', parseInt(5*Math.random()));
	fetchNum('J_TPL_NUM', parseInt(5*Math.random()));
	fetchNum('J_HISTORY_NUM', parseInt(5*Math.random()));
	
	setInterval(function(){
		
		var i1 = parseInt(10*Math.random())
			, i2 = parseInt(10*Math.random())
			, i3 = parseInt(10*Math.random());
		//console.log(i1+ "  " + i2 + "  " + i3);
		fetchNum('J_MAP_NUM', i1);
		fetchNum('J_TPL_NUM', i2);
		fetchNum('J_HISTORY_NUM', i3);
		
	},5000);*/
});