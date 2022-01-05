$(document).ready(function(){
	var colors = [
			   '#2f7ed8', 
			   '#0d233a', 
			   '#8bbc21', 
			   '#910000',
			], data_num = [], j = 0;
	
	for( i in item_data.nums ){
		if(j == colors.length) j = 0;
		data_num.push( { y : item_data.nums[i], color : colors[j] } );
		j++;
	}
	
	Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
	$('#dataCtn').highcharts({
			credits: {
                enabled: false
            },
			colors : ['#000'],
            chart: {
                type: 'column'
            },
            title: {
                text: '地图服务访问统计'
            },
            xAxis: {
                categories: item_data.items,
				
            },
            yAxis: {
                min : 0,
				allowDecimals : false,
                title: {
                    text: '访问次数'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.0f} 次</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
				data : data_num, 
				name:'访问量',
				dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
			}],
        });
});