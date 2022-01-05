<#if metaData??>
    <#assign data=metaData!>
<div class="base-info-wrap">
    <div class="base-info-inner">
        <a href="#" class="toggle"><span class="icon icon-angle-right"></span></a>

        <div class="base-info">
            <a href="<@com.rootPath/>/portal2/metaInfo/${(data["id"])!}" class="btn btn-mini btn-inverse">元数据 <i
                    class="icon icon-double-angle-right"></i></a>
            <h4>基本数据信息</h4>
            <table class="table" style="background-color: transparent;">
                <tr>
                    <th>年度</th>
                    <td>${(data["nd"])!}</td>
                </tr>
                <tr>
                    <th>业务类型</th>
                    <td>${(data["ywlx"])!}</td>
                </tr>
                <tr>
                    <th>表空间</th>
                    <td>${(data["bkjmc"])!}</td>
                </tr>
                <tr>
                    <th>状态</th>
                    <td>${(data["zt"])!}</td>
                </tr>
                <tr>
                    <th>总共大小</th>
                    <td id="sumSize">${(data["bkjzdx"]!?double/1024)!?string('###0.##')} G</td>
                </tr>
                <tr>
                    <th>已用大小</th>
                    <td id="usedSize">${(data["bkjyydx"]!?double/1024)!?string('###0.##')} G</td>
                </tr>
                <tr hidden="true">
                    <th>剩余大小</th>
                    <td id="restSize">${(data["bkjsydx"]!?double/1024)!?string('###0.##')} G</td>
                </tr>
                <tr style="display: none;">
                    <th>文件路径</th>
                    <td>${(data["bkjwjlj"])!}</td>
                </tr>
                <tr>
                    <th>数据总数</th>
                    <td>${sum!?c}</td>
                </tr>
                <tr>
                    <th>行政区数</th>
                    <td>${xzqNum!?c}</td>
                </tr>
            </table>
        </div>
    </div>
</div>


<div id="chartDiv2" style="display: <#if xzdm?contains('[null]')>none;<#else >block;</#if>"></div>
<br/>
<div id="chartDiv1"></div>

<script src="<@com.rootPath/>/static/js/black-opt.js"></script>
<script type="text/javascript">
    var usedPer = (${metaData["bkjyydx"]!?double!?string('###0.##')}/${metaData["bkjzdx"]!?double!?string('###0.##')})*100;
    var unusedPer = 100 - usedPer;
    $(function () {
                $('#chartDiv1').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {
                        text: '容量使用情况',
                        style: {

                            fontFamily: 'microsoft yahei',
                            fontSize: '16px'
                        },
                        x: -20
                    },
                    tooltip: {

                        formatter:function () {
                            return this.point.name + '<br/><b>' + this.percentage.toFixed(2) + '</b>%';
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                formatter: function () {
                                    return '<b>' + this.point.name + '</b>: ' + this.percentage.toFixed(2) + ' %';
                                }
                            }
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    exporting: {
                        enabled: false
                    },
                    series: [
                        {
                            type: 'pie',
                            name: '占用空间',
                            data: [
                                ['已用大小', Number(usedPer.toFixed(3))],
                                ['剩余大小', Number(unusedPer.toFixed(3))]
                            ]
                        }
                    ]
                });

                $("#chartDiv2").highcharts({
                    chart: {
                        type: 'line',
                        zoomType: "x"

                    },
                    title: {
                        text: '行政区数据数',
                        style: {

                            fontFamily: 'microsoft yahei',
                            fontSize: '16px'
                        },
                        x: -20 //center
                    },
                    xAxis: {
                        title: {
                            text: '行政区'
                        },
                        categories: ${xzdm!}
//                labels:{
//                    step:
//                }
                    },
                    yAxis: {
                        title: {
                            text: '数据数(条)'
                        },
                        min: 0,
                        labels: {
                            formatter: function () {
                                return this.value;
                            }
                        }
                    },
                    tooltip: {

                        formatter: function () {
                            return '<b>' + this.x + '</b><br/>' + this.series.name + ':<b>' + this.y + '</b>';
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    exporting: {
                        enabled: false
                    },
                    legend: {

                        enabled: false
                    },
                    series: [
                        {
                            name: '数据总数',
                            data: ${sjzs!}
                        }
                    ]
                });
            }
    );

</script>
<#else>
<div align="center" style="margin-top: 30px;"><span id="errorMsg">没有相关信息!</span></div></#if>