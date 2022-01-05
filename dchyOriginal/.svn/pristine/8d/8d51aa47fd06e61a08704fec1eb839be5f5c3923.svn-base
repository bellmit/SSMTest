$(function () {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var item = $('#chart-container'), id = item.attr('item-id'), name = item.attr('item-name'), chart, lastTs = new Date().getTime();
    $.post(_ctx + '/console/monitor/trend', {id: id}, function (data) {
        chart = new Highcharts.StockChart({
            credits: {
                enabled: false
            },
            chart: {
                renderTo: item[0],
                zoomType: 'x',
                animation: Highcharts.svg,
                height: 600
            },
            title: {
                text: name
            },
            tooltip: {
                formatter: function () {
                    return Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/><strong>' + name + '</strong>' + ': ' + this.y;
                }
            },
            xAxis: {
                events: {
                    afterSetExtremes: afterSetExtremes
                }
            },
            series: [
                {
                    shadow: true,
                    name: name,
                    data: data['history'],
                    dataGrouping: {
                        enabled: false
                    }
                }
            ],
            navigator: {
                adaptToUpdatedData: false,
                series: {
                    data: data['trend']
                }

            },
            scrollbar: {
                liveRedraw: false
            },
            rangeSelector: {
                buttons: [
                    {
                        type: 'minute',
                        count: 10,
                        text: '10分'
                    },
                    {
                        type: 'minute',
                        count: 30,
                        text: '30分'
                    },
                    {
                        type: 'hour',
                        count: 1,
                        text: '1时'
                    },
                    {
                        type: 'hour',
                        count: 2,
                        text: '2时'
                    },
                    {
                        type: 'hour',
                        count: 3,
                        text: '3时'
                    },
                    {
                        type: 'hour',
                        count: 6,
                        text: '6时'
                    },
                    {
                        type: 'hour',
                        count: 12,
                        text: '12时'
                    },
                    {
                        type: 'day',
                        count: 1,
                        text: '1天'
                    },
                    {
                        type: 'day',
                        count: 7,
                        text: '1周'
                    },
                    {
                        type: 'month',
                        count: 1,
                        text: '1月'
                    },
                    {
                        type: 'month',
                        count: 3,
                        text: '3月'
                    },
                    {
                        type: 'year',
                        count: 1,
                        text: '一年'
                    },
                    {
                        type: 'all',
                        text: '全部'
                    }
                ],
                selected: 1
            }
        });
    });
    function afterSetExtremes(e) {
        $.post('history', {id: id, start: Math.round(e.min), end: Math.round(e.max)}, function (data) {
            chart.series[0].setData(data[id]);
        });
    }
});