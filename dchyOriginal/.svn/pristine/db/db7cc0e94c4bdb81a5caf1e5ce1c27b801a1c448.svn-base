
<style type="text/css">

    table tr td {
        border: 1px solid #ddd;
        padding: 5px 10px;
    }

    table tr th {
        border: 1px solid #ddd;
        padding: 10px;
    }

    table {
        width: 100%;
        background-color: transparent;
        border: 1px solid #ddd;
        table-layout: auto;
        border-collapse: collapse;
    }
    table td{
        text-align: center;
    }
    h4{
        margin-left: 5px;
    }
</style>
<div style="height: 50%;width: 100%;">
    <div id="textDiv" style="width: 45%;float: left;height: <#if type?contains("db")>480<#else >360</#if>px;">
        <#if (type?contains("yzt")||type?contains("db"))>
       <h4>数据存储使用概述(单位:G)</h4>
        <table style="background-color: transparent;">
            <tr>
                <#if type?contains("yzt")><th rowspan="4">数据库存储</th></#if>
                <th>数据库容量</th>
                <td>${(dbZdx!?double/1024)!?string('####.##')}</td>
            </tr>
            <tr>
                <th>已用大小</th>
                <td>${(dbYydx!?double/1024)!?string('####.##')}</td>
            </tr>
            <tr>
                <th>剩余大小</th>
                <td>${(dbSydx!?double/1024)!?string('####.##')}</td>
            </tr>
            <tr>
                <th>使用百分比</th>
                <td>${(dbYydx!?double*100/dbZdx)!?string('####.##')}%</td>
            </tr>

            <#if type?contains("yzt")>
            <tr>
                <th rowspan="2">文件存储</th>
                <th>影像数据大小</th>
                <td>${yxdx!?string('###0.00')}</td>
            </tr>
            <tr>
                <th>切片数据大小</th>
                <td>${qpdx!?string('###0.00')}</td>
            </tr>
            <#elseif type?contains("db")>
            <#list dbList as item>
            <tr>
                <th>${item.ywlxmc}占用(百分比)</th>
                <td>${(item.yydx!?double/1024)!?string('####.##')}(${(item.yydx!?double*100/dbYydx)!?string('#0.00')}%)</td>
            </tr>
            </#list>
            </#if>

        <#elseif type?contains("file")>
            <h4>文件使用概述(单位:G)</h4>
            <table class="table" style="background-color: transparent;">
                <tr>
                    <th>文件使用空间:</th>
                    <td>${wjzdx!?string('###0.##')}</td>
                </tr>
                <tr>
                    <th>影像数据占用大小(百分比)</th>
                    <td>${yxdx!?string('###0.##')}(${(yxdx!?double*100/wjzdx)!?string('#0.00')}%)</td>
                </tr>
                <tr>
                    <th>切片数据占用大小(百分比)</th>
                    <td>${qpdx!?string('###0.##')}(${(qpdx!?double*100/wjzdx)!?string('#0.00')}%)</td>
                </tr>
            </table>
            <#else>
            <h4>${nf!}年文件使用概述(单位:G)</h4>
            <table class="table" style="background-color: transparent;">
                <tr>
                    <th>文件使用空间:</th>
                    <td>${wjzdx!?string('###0.##')}</td>
                </tr>
                <tr>
                    <th>影像数据占用大小(百分比)</th>
                    <td>${ndyxdx!?string('###0.##')}(${(ndyxdx!?double*100/wjzdx)!?string('#0.00')}%)</td>
                </tr>
                <tr>
                    <th>切片数据占用大小(百分比)</th>
                    <td>${ndqpdx!?string('###0.##')}(${(ndqpdx!?double*100/wjzdx)!?string('#0.00')}%)</td>
                </tr>
            </table>

        </#if>
        </table>
    </div>
    <div id="chartDiv" style="width: 50%;float: right;height: 300px;">

    </div>
</div>
<div style="width: 100%;height: 50%;float: left;">
    <table style="background-color: transparent;">
    <#if type?contains("db")>
        <tr>
            <th style="width: 120px;text-align: center">表空间名称</th>
            <th>总大小</th>
            <th>已用大小</th>
            <th>剩余大小</th>
            <th>使用百分比</th>
            <th hidden="true">文件位置</th>
        </tr>
        <#list tableData as data>
        <tr>
            <td style="width: 100px;">${data.bkjmc}</td>
            <td>${(data.bkjzdx!?double/1024)!?string('###0.##')}</td>
            <td>${(data.bkjyydx!?double/1024)!?string('###0.##')}</td>
            <td>${(data.bkjsydx!?double/1024)!?string('###0.##')}</td>
            <td>${(data.bkjyydx!?double*100/data.bkjzdx)!?string('#0.00')}%</td>
            <td style="width:210px; " hidden="true">${data.bkjwjlj}</td>
        </tr>
        </#list>
        <#--<#elseif type?contains("file")>-->
            <#--<tr>-->
                <#--<th>类型</th>-->
                <#--<th>年份</th>-->
                <#--<th>数据大小</th>-->
                <#--<th>所占百分比</th>-->
                <#--<th>位置</th>-->
            <#--</tr>-->
            <#--<#list tableData as data>-->
            <#--<tr>-->
                <#--<td>${data.lx}</td>-->
                <#--<td>${data.nf}</td>-->
                <#--<td>${data.wjdx!?string('###0.##')}</td>-->
                <#--<td>${(data.wjdx!?double*100/wjzdx)!?string('#0.00')}%</td>-->
                <#--<td>${data.wz}</td>-->
            <#--</tr>-->
            <#--</#list>-->
        <#elseif type?contains("yzt")>
        <tr>
            <th rowspan="${dbSize}">数据库存储</th>
            <th>类型</th>
            <th>年份</th>
            <th>数据大小</th>
            <th>位置</th>
        </tr>
        <#list tableData as data>
            <tr>
                <#if (data_index+1)=dbSize><th rowspan="${fileSize}">文件存储</th></#if>
                <td>${data.lx}</td>
                <td>${data.nf!}</td>
                <td><#if (data.lx!?contains("影像")||data.lx!?contains("切片"))>${data.sjdx!?string('###0.##')}<#else >${(data.sjdx!?double/1024)!?string('###0.##')}</#if></td>
                <td>${data.wz}</td>
            </tr>
        </#list>
        <#else >
        <tr>
            <th>类型</th>
            <th>年份</th>
            <th>数据大小</th>
            <th>所占百分比</th>
            <th hidden="true">位置</th>
        </tr>
        <#list tableData as data>
            <tr>
                <td>${data.lx}</td>
                <td>${data.nf}</td>
                <td>${data.wjdx!?string('###0.##')}</td>
                <td>${(data.wjdx!?double*100/wjzdx)!?string('#0.00')}%</td>
                <td hidden="true">${data.wz}</td>
            </tr>
        </#list>

    </#if>
    </table>
</div>
<script type="text/javascript">
    $(function(){

        Highcharts.theme = {
            colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
            chart: {
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
                    stops: [
                        [0, 'rgb(255, 255, 255)'],
                        [1, 'rgb(240, 240, 255)']
                    ]
                },
                borderWidth: 2,
                plotBackgroundColor: 'rgba(255, 255, 255, .9)',
                plotShadow: true,
                plotBorderWidth: 1
            },
            title: {
                style: {
                    color: '#000',
                    font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
                }
            },
            subtitle: {
                style: {
                    color: '#666666',
                    font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
                }
            },
            xAxis: {
                gridLineWidth: 1,
                lineColor: '#000',
                tickColor: '#000',
                labels: {
                    style: {
                        color: '#000',
                        font: '11px Trebuchet MS, Verdana, sans-serif'
                    }
                },
                title: {
                    style: {
                        color: '#333',
                        fontWeight: 'bold',
                        fontSize: '12px',
                        fontFamily: 'Trebuchet MS, Verdana, sans-serif'

                    }
                }
            },
            yAxis: {
                minorTickInterval: 'auto',
                lineColor: '#000',
                lineWidth: 1,
                tickWidth: 1,
                tickColor: '#000',
                labels: {
                    style: {
                        color: '#000',
                        font: '11px Trebuchet MS, Verdana, sans-serif'
                    }
                },
                title: {
                    style: {
                        color: '#333',
                        fontWeight: 'bold',
                        fontSize: '12px',
                        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                    }
                }
            },
            legend: {
                itemStyle: {
                    font: '9pt Trebuchet MS, Verdana, sans-serif',
                    color: 'black'

                },
                itemHoverStyle: {
                    color: '#039'
                },
                itemHiddenStyle: {
                    color: 'gray'
                }
            },
            labels: {
                style: {
                    color: '#99b'
                }
            },

            navigation: {
                buttonOptions: {
                    theme: {
                        stroke: '#CCCCCC'
                    }
                }
            }
        };

        Highcharts.setOptions(Highcharts.theme);
        var type='${type!}';

        $("#chartDiv").highcharts({

            chart:{
                plotBackgroundColor:null,
                plotBorderWidth:null,
                plotShadow:false
            },
            title:{
                text:'空间占用情况',
                style:{

                    fontFamily:'microsoft yahei',
                    fontSize:'16px'
                },
                x:-20
            },
            tooltip:{

                formatter:function () {
                    return this.point.name + '<br/><b>' + this.percentage.toFixed(2) + '</b>%';
                }
            },
            legend:{
                enabled:true,
                layout:'horizontal',
                align:'center',
                verticalAlign:'bottom'
            },
            plotOptions:{
                pie:{
                    allowPointSelect:true,
                    cursor:'pointer',
                    dataLabels:{
                        enabled:false,
                        color:'#000000',
                        connectorColor:'#000000',
                        formatter:function () {
                            return '<b>' + this.point.name + '</b>: ' + this.percentage.toFixed(2) + ' %';
                        }
                    }
                }
            },
            credits:{
                enabled:false
            },
            exporting:{
                enabled:false
            },
            series:[
                {
                    type:'pie',
                    name:'占用空间',
                    data:[
                            <#if type?contains("db")>
                            <#list dbList as item>
                            ['${item.ywlxmc!}',Number(${item.yydx!?string('####.###')})]<#if item_has_next>,</#if>
                            </#list>
                            <#elseif type?contains("file")>
                            ['影像',Number(${yxdx!?string('####.###')})],
                            ['切片',Number(${qpdx!?string('####.###')})]
                            <#elseif type?contains("yzt")>
                            ['数据库',Number(${(dbZdx!?double/1024)!?string('####.###')})],
                            ['影像',Number(${yxdx!?string('####.###')})],
                            ['切片',Number(${qpdx!?string('####.###')})]
                            <#else >
                             ['影像',Number(${ndyxdx!?string('####.###')})],
                             ['切片',Number(${ndqpdx!?string('####.###')})]

                            </#if>

                    ],
                    showInLegend:true
                }
            ]
        });
    })
</script>