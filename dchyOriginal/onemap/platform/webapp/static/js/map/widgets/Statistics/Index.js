/**
 *  属性识别
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/12 16:50
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "dojox/uuid/generateRandomUuid",
    "esri/lang",
    "esri/graphic",
    "esri/tasks/IdentifyTask",
    "esri/tasks/FeatureSet",
    "esri/tasks/IdentifyParameters",
    "esri/geometry/Point",
    "esri/graphicsUtils",
    "map/core/BaseWidget",
    "map/core/GeometryIO",
    "slimScroll",
    "mustache",
    "layer",
    "handlebars",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "map/component/ListDataRenderer",
    "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
    "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, graphicsUtils, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
        MapUtils, MapPopup, ListDataRenderer) {

        var __map, _identifyConfig;
        var mapPopup = MapPopup.getInstance();


        var me = declare([BaseWidget], {

            onCreate: function () {
                __map = this.getMap().map();
                _identifyConfig = this.getConfig();

                _init();

            },
            onOpen: function () {
                __map = this.getMap().map();

                _init();

            },
            onPause: function () {
                _activated = false;
                $('.popup-Statistics-win').hide()
            },
            onDestroy: function () {
                $('.popup-Statistics-win').hide()

            }
        });

        //设置属性识别后信息弹出窗的样式
        var POPUP_OPTION_INFO = "infoWindow";
        var POPUP_OPTION_MODAL = "modal";

        //默认是infowindow样式
        var popUpStyle = POPUP_OPTION_INFO;

        /**
         *  初始化组件
         */
        function _init() {
            //读取弹出层方式
            if (_identifyConfig.popUpStyle && _identifyConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            layer.config(); 
            var w = window.innerWidth|| document.body.clientWidth;
            var h = window.innerHeight || document.body.clientHeight;
            $('.widget-Statistics').css('height', h - 80 + 'px');
            $('#map-content').css('width', w - 45 + 'px');
            $('.popup-window').hide();
            $('.popup-Statistics-win').show();
            var liHeight = ($('.ssdtgxtj-content').outerHeight()-48)/4;
            $('.ssdtgxtj-content').find('li').css({'height':liHeight + 'px','line-height':liHeight + 'px'});
            _echarts();
        }
        function _echarts() {
            var myChart1 = echarts.init(document.getElementById('eahrts1'));
            var myChart2 = echarts.init(document.getElementById('eahrts2'));
            // 指定图表的配置项和数据
            option1 = {
                color: ['#1d87d1', '#de56f6', '#6ccfd9'],

                tooltip: {
                    trigger: 'axis'
                },
                legend: {

                    data: ['规划竣工测量', '地籍测量', '房产测量']
                },
                grid: {
                    left: '0%',
                    right: '2%',
                    top: '20%',
                    bottom: '0%',
                    containLabel: true
                },
                toolbox: {
                    "show": false,
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    "axisTick": {
                        "show": false
                    },
                    "axisLine": {
                        "show": false

                    },
                    boundaryGap: false,
                    data: ['1月', '2月', '3月', '4月', '5月', '6月']
                },
                yAxis: {
                    "axisTick": {
                        "show": false
                    },
                    "axisLine": {
                        "show": false
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            type: 'dashed'
                        }
                    },
                    type: 'value'
                },
                series: [{
                    name: '规划竣工测量',
                    smooth: true,
                    type: 'line',
                    symbolSize: 8,
                    symbol: 'circle',
                    data: [1, 3, 5, 5.2, 5.7, 5.8, 7]
                }, {
                    name: '地籍测量',
                    smooth: true,
                    type: 'line',
                    symbolSize: 8,
                    symbol: 'circle',
                    data: [3, 5, 6, 7, 5.2, 6.5, 6.2]
                }, {
                    name: '房产测量',
                    smooth: true,
                    type: 'line',
                    symbolSize: 8,
                    symbol: 'circle',
                    data: [5, 7, 2, 3, 7, 8, 4]
                }]
            };
            option2 = {
                legend: {},
                tooltip: {},
                grid: {
                    left: '0%',
                    right: '0%',
                    top: '20%',
                    bottom: '10%',
                    containLabel: true
                },
                dataset: {
                    source: [
                        ['product', '规划竣工测量', '地籍测量', '房产测量'],
                        ['东城街道', 5, 7, 7],
                        ['儒林道', 2, 3, 2],
                        ['尧塘街道', 3, 2, 5],
                        ['朱林镇', 6, 5, 5],
                        ['指前道', 7, 7, 5],
                        ['直溪镇', 5, 5, 2],
                        ['靴阜镇', 5, 5, 2],
                        ['西城街道', 7, 8, 7],
                        ['金城镇', 2,3, 3],
                    ]
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,//横轴信息全部显示  
                        rotate: -65,//-55度角倾斜显示  
                    },
                },
                yAxis: {
                    plitLine: {
                        show: true,
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                series: [
                    { type: 'bar',barWidth : 5 },
                    { type: 'bar',barWidth : 5 },
                    { type: 'bar',barWidth : 5 },
                ],
                color: ['#1d87d1', '#de56f6', '#6ccfd9']
            };
            myChart1.setOption(option1);
            myChart2.setOption(option2);
        }
        return me;
    });