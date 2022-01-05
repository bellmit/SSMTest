$(function () {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    var ids = [], items = {}, charts = {}, lastTs = new Date().getTime();
    $('.chart-item').each(function () {
        var item = $(this);
        var id = item.attr('item-id');
        ids.push(id);
        items[id] = item;
    });
    $.ajax({
        url: "history",
        traditional: true,
        data: {id: ids, end: lastTs, traditional: true}
    }).done(function (data) {
            $.each(data, function (id, data0) {
                var item = items[id], name = item.attr('item-name');
                charts[id] = new Highcharts.Chart({
                    credits: {
                        enabled: false
                    },
                    chart: {
                        renderTo: item[0],
                        type: 'spline',
                        zoomType: 'x',
                        animation: Highcharts.svg
                    },
                    title: {
                        text: name
                    },
                    tooltip: {
                        formatter: function () {
                            return Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/><strong>' + this.series.name + '</strong>' + ': ' + this.y;
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        minRange: 180000
                    },
                    yAxis: {
                        title: {
                            text: null
                        }
                    },
                    series: [
                        {
                            shadow : true,
                            name: name,
                            data: data0
                        }
                    ]
                });

            });
        });
    setInterval(function () {
        var now = new Date().getTime();
        if (ids.length == 0) {
            return;
        }
        $.ajax({
            url: "history",
            traditional: true,
            data: {id: ids, start: lastTs, end: now}
        }).done(function (data) {
                $.each(data, function (id, data0) {
                    $.each(data0, function (n, v) {
                        charts[id].series[0].addPoint(v, true, true);
                    });
                });
                lastTs = now;
            });
    }, 15000);
});