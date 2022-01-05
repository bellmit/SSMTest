/**
 * 图片批量预览
 * 能够批量预览
 * Created by zhayw on 2016/4/6.
 */
define(["dojo/topic",
    "dojo/_base/array",
    "dojo/date",
    "dojo/date/locale",
    "layer",
    "hbarsUtils",
    "static/js/map/core/support/ScreenHelper",
    "static/js/map/core/support/Environment",
    "text!static/js/map/template/imagesView/timeline.html",
    "text!static/js/map/template/imagesView/timelineCanvasContent.html",
    "text!static/js/map/template/imagesView/timelineContent.html",
    "text!static/js/map/template/imagesView/upload.html",
    "css!static/js/map/template/imagesView/timeline.css",
    "css!static/thirdparty/timelinr/jquery.timelinr.css",
    "css!static/thirdparty/imageviewer/imageviewer.css",
    "static/thirdparty/jquery/jquery.ajaxfileupload",
    "timelinr",
    "static/thirdparty/imageviewer/imageviewer"
    ],
    function (topic, arrayUtils, dateUtils, locale, layer, HbarsUtils,ScreenHelper,Environment,timelineTpl,timelineCanvasContent,
              timelineContentTpl, uploadTpl) {
    var optionSettings = {
        year: "#year",
        month: "#month",
        pannableImage: ".pannable-image",
        timelineContent: "#timeline", //放置图片的div id,
        recordYear:"#recordYear"

    }, resultData = null, _showDate = null, _proId = null, screenHight = ScreenHelper.getInstance().screenHeight;

    /**
     * 初始化数据，及转化数据
     * @param year
     * @param month
     * @param datas
     * @private
     */
    function _initData(year, month, day, datas) {
        var result = {data: [], years: [], month:[]};
        var monthData = [];
        var daysInMonth = 0;                //一个月当中的天数
        var defaultDay = 1;                 //默认定位天数
        var hasDataOfDay = true;            //标记当天是否存在相片数据

        //获取对应年份及月份下的照片数据
        arrayUtils.filter(datas, function (yearItem) {
            if (yearItem.year == year) {
                arrayUtils.filter(yearItem.yearData, function (monthItem) {
                    if (monthItem.month == month)
                        monthData = monthData.concat(monthItem.monthData);
                });
            }
            result.years.push({year: yearItem.year});
        });

        if (monthData.length > 0) {
            _proId = monthData[0].parentId;  //获取关联项目的id
        }

        daysInMonth = getMaxDayInMonth(year, month);

        for(var i = 1; i<=getMaxMonthOfYear(year); i++){
            result.month.push(i);
        }

        //组织本月的所有展示数据
        for (var i = 1; i <= daysInMonth; i++) {
            $.each(monthData, function (j,item) {
                var imgDay = locale.parse(item.createTime, {
                    selector: 'date',
                    datePattern: 'yyyy-MM-dd HH:mm:ss'
                }).getDate();
                if (imgDay == i) {
                    result.data.push({
                        id: item.id,
                        day: i,
                        thumb: root + '/file/thumb/' + item.id,
                        origin: root + '/file/original/' + item.id ,
                        name: item.name
                    });
                    hasDataOfDay = true;
                    return false;
                } else {
                    hasDataOfDay = false;
                }
            });
            if (!hasDataOfDay || monthData.length == 0)
                result.data.push({id: year + '-' + month + '-' + i, day: i, origin: '/omp/nopic'});
            if (day == i) defaultDay = i;
        }

        if(result.years.length == 0){
            result.years.push({year:year});
        }
        result.defaultDay = defaultDay;
        return result
    }

    /**
     * 渲染模板并显示
     * @param result
     * @private
     */
    function _renderToShow() {
        var viewerDom = HbarsUtils.renderTpl(timelineTpl, resultData);
        layer.open({
            type: 1,
            title: !1,
            closeBtn: !0,
            content: viewerDom,
            area: [screenHight<=768?'900px':'1024px', screenHight<=768?'560px':'650px'],
            shade: 0.8,
            shadeClose: true,
            success: function () {
                $(optionSettings.timelineContent).empty();
                if(Environment.isIE()){
                    $(optionSettings.timelineContent).append(HbarsUtils.renderTpl(timelineCanvasContent, resultData));
                    $("#issues").data("issues",resultData.data); //绑定数据
                }else{
                    $(optionSettings.timelineContent).append(HbarsUtils.renderTpl(timelineContentTpl, resultData));
                }
                $("#issues li").height($(".layui-layer").eq($(".layui-layer").length -1).height()-130);
                $("#issues li").width($(".layui-layer").eq($(".layui-layer").length -1).width()-20);

                $(optionSettings.year).val(_showDate.getFullYear());
                $(optionSettings.month).val(_showDate.getMonth() + 1);
                if(Environment.isIE()){
                    //ie方式使用canvas标签展示图片，开启配置isCanvas为true
                    $(optionSettings.pannableImage).ImageViewer({isCanvas:true});
                    $().timelinr({startAt: resultData.defaultDay, autoPlay: 'false', isCanvas:true});
                    G_vmlCanvasManager.init_(document);     //canvas 标签ie浏览器初始化
                }else{
                    $(optionSettings.pannableImage).ImageViewer({isCanvas:false});
                    $().timelinr({startAt: resultData.defaultDay, autoPlay: 'false'});
                }
                _listeners();

                /**
                 * 播放监听
                 */
                _autoPlayImage();
                
            },
            cancel:function () {
                if(sendMessage){
                    sendMessage("imgHistory","closePop");
                }
            }
        });
    }

    /**
     *
     * @param proId
     * @param callback
     * @private
     */
    function _loadData(proId, condition, callback) {
        $.ajax({
            url: root + '/project/records/all',
            method: 'post',
            data: {proid: proId, condition: condition},
            async: false,
            success: function (rp) {
                console.log(rp.records);
                callback(rp.records);
            }
        });
    }


    /**
     * 页面监听
     * @private
     */
    function _listeners() {
        /**
         * 监听年份
         */
        $(optionSettings.year).off('change').on('change', function () {
            var condition = {
                year: $(this).val(),
                month: $(optionSettings.month).val()
            };

            _loadData(_proId, JSON.stringify(condition), function (data) {

                _showDate = locale.parse(condition.year+'-'+condition.month+'-01', {
                    selector: 'date',
                    datePattern: 'yyyy-MM-dd'
                });
                resultData = _initData(_showDate.getFullYear(), _showDate.getMonth() + 1, _showDate.getDate(), data);
                for(var i = 1; i<=getMaxMonthOfYear(year); i++){
                    resultData.month.push(i);
                }
                $(optionSettings.timelineContent).empty();
                $(optionSettings.timelineContent).append(HbarsUtils.renderTpl(timelineContentTpl, resultData));
                $("#issues li").height($(".layui-layer").eq($(".layui-layer").length -1).height()-130);
                $("#issues li").width($(".layui-layer").eq($(".layui-layer").length -1).width()-20);
                $(optionSettings.month).empty();
                $(optionSettings.month).append(HbarsUtils.renderTpl('{{#month}}<option value="{{.}}">{{.}}月</option>{{/month}}',resultData))
                $(optionSettings.pannableImage).ImageViewer();
                $().timelinr({startAt: 1});
                $(this).blur();
                data = null;
                condition = null;
                _listeners();

            });
        });

        /**
         * 监听月份
         */
        $(optionSettings.month).off('change').on('change', function () {
            $('#stopPlay').trigger('click');
            var condition = {
                year: $(optionSettings.year).val(),
                month: $(this).val()
            };

            var _func = _loadData;

            _func(_proId, JSON.stringify(condition), function (data) {
                _showDate = locale.parse(condition.year+'-'+condition.month+'-01', {
                    selector: 'date',
                    datePattern: 'yyyy-MM-dd'
                });
                resultData = _initData(_showDate.getFullYear(), _showDate.getMonth() + 1, _showDate.getDate(), data);
                $(optionSettings.timelineContent).empty();
                if(Environment.isIE()){
                    $(optionSettings.timelineContent).append(HbarsUtils.renderTpl(timelineCanvasContent, resultData));
                    $("#issues").data("issues",resultData.data); //绑定数据
                }else{
                    $(optionSettings.timelineContent).append(HbarsUtils.renderTpl(timelineContentTpl, resultData));
                }

                $("#issues li").height($(".layui-layer").eq($(".layui-layer").length -1).height()-130);
                $("#issues li").width($(".layui-layer").eq($(".layui-layer").length -1).width()-20);

                if(Environment.isIE()){
                    $(optionSettings.pannableImage).ImageViewer({isCanvas:true});
                    $().timelinr({startAt: 1, autoPlay: 'false', isCanvas:true});
                    G_vmlCanvasManager.init_(document);     //canvas 标签ie浏览器初始化
                }else{
                    $(optionSettings.pannableImage).ImageViewer({isCanvas:false});
                    $().timelinr({startAt: 1, autoPlay: 'false'});
                }
                $(this).blur();
                data = null;
                condition = null;
                _listeners();
            });

            _func = null;
        });

        /**
         * 照片未加载到处理
         */
        $(optionSettings.pannableImage).error(function () {
            var id = $(this).data('id');
            $('#' + id).html(HbarsUtils.renderTpl(uploadTpl, {proId: _proId, id: id}));

            $('#upload_' + id).on('change', function () {
                $("#viewfile_" + id).val($(this).val());
            });

            $('#btn_' + id).on('click', function () {
                if ($('#upload_' + id).val() === "")return;
                $.ajaxFileUpload({
                    fileElementId: "upload_" + id,
                    url: root + '/file/pic/upload/' + _proId,
                    data: {picid: id},  //1.照片丢失替换的picId为uuid形式；2当日没有照片的数据，picId为当日的日期格式yyyy-MM-dd
                    dataType: 'text',
                    success: function (data, textStatus) {
                        var imgHtml =
                            '<li id="{{id}}" class="selected">' +
                            '<img src="{{origin}}" id="img_{{id}}" data-id="{{id}}" data-high-res-src="{{origin}}" class="pannable-image" />' +
                            '</li>';

                        $("#dates li a.selected").attr('href', "#"+data);
                        $('#' + id).replaceWith(HbarsUtils.renderTpl(imgHtml, {
                            id: data,
                            origin: '/omp/file/original/' + data + '?' + Math.random()
                        }));

                        $('.btn-quarter').removeAttr("disabled");
                        $(optionSettings.recordYear).removeAttr("disabled");
                        $(optionSettings.pannableImage).ImageViewer();
                        $("#issues li").height($(".layui-layer").eq($(".layui-layer").length -1).height()-130);
                        $("#issues li").width($(".layui-layer").eq($(".layui-layer").length -1).width()-20);
                        $("#dates li a.selected").trigger('click');

                        var condition = {
                            year: $(optionSettings.year).val(),
                            month: $(optionSettings.month).val()
                        };

                        _loadData(_proId, JSON.stringify(condition), function (datas) {
                            var season = getQuarterOfMonth(condition.month);
                            $(optionSettings.recordYear).val(condition.year);
                            $('#option' + season).trigger('click');
                            topic.publish("img/upload/quarterRender", {
                                year: condition.year,
                                season: season,
                                proId: _proId,
                                datas: datas
                            });
                        });
                    }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert("图片上传失败！");
                    }
                });
            });
        });


        if(Environment.isIE()){
            /**
             * ie方式自定义上传事件触发
             */
            $('#upload').on('change', function () {
                $("#viewfile").val($(this).val());
            });

            /**
             * ie方式自定义上传事件触发
             */
            $("#btn_upload").off('click').on('click', function () {
                var id = $(settings.datesDiv +" ." + settings.datesSelectedClass).attr("href").replace("#", "");
                if ($('#upload').val() === "") return;

                $.ajaxFileUpload({
                    fileElementId: "upload",
                    url: root + '/file/pic/upload/' + _proId,
                    data: {picid: id},  //1.照片丢失替换的picId为uuid形式；2当日没有照片的数据，picId为当日的日期格式yyyy-MM-dd
                    dataType: 'text',
                    success: function (data, textStatus) {
                        var condition = {
                            year: $(optionSettings.year).val(),
                            month: $(optionSettings.month).val()
                        };

                        $.each(resultData.data, function (i, n) {
                            if(n.id == id){
                                n.thumb = root + '/file/thumb/' + data
                                n.origin = root + '/file/original/' + data
                                return false;
                            }
                        });

                        $("#issues").data("issues",resultData.data); //同步绑定数据

                        $('#viewfile').val('');

                        //load图片展示
                        new settings.imgUploadEvent($(settings.issuesDiv + " li .pannable-image"),
                            $(settings.issuesDiv + " li .iv-image-view canvas"), '/omp/file/original/'+data);

                        _loadData(_proId, JSON.stringify(condition), function (datas) {
                            var season = getQuarterOfMonth(condition.month);
                            $(optionSettings.recordYear).val(condition.year);
                            $('#option' + season).trigger('click');
                            topic.publish("img/upload/quarterRender", {
                                year: condition.year,
                                season: season,
                                proId: _proId,
                                datas: datas
                            });
                        });
                    }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert("图片上传失败！");
                    }
                });
            });
        }
    }


            /**
     *  自动播放
     * @constructor
     */
    function _autoPlayImage() {
        var timer = [];//自动播放定时器
        $('.layui-layer-close2').on('click', function () {
            if(timer.length>0){
                stopAutoPlay(timer);
                timer = [];
            }
        });
        $('.layui-layer-shade').on('click', function () {
            if(timer.length>0){
                stopAutoPlay(timer);
                timer = [];
            }
        });

        $('#autoPlay').on('click', function () { //开始播放
            $(this).hide();
            $('#stopPlay').show();
            var speed = $('#playSpeed').val();
            timer.push(setInterval(newAutoPlay, speed));
        });

        $('#stopPlay').on('click', function () { //停止播放
            $(this).hide();
            $('#autoPlay').show();
            if(timer.length>0){
                stopAutoPlay(timer);
                timer = [];
            }
        });

        $('#playSpeed').on('change', function () {
            var speed = $(this).val();
            $('#autoPlay').hide();
            $('#stopPlay').show();
            if(timer.length>0){
                stopAutoPlay(timer);
                timer = [];
            }
            timer.push(setInterval(newAutoPlay, speed));
            $(this).blur();
        });

        /**
         * 自定义自动播放
         */
        function newAutoPlay() {
            if (typeof window.CollectGarbage === 'function') {
                CollectGarbage()
            }

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

        /**
         * 清除定时器，停止播放
         * @param timerArr
         */
        function stopAutoPlay(timerArr) {
            for(var i = 0; i<timerArr ;i++){
                clearInterval(timerArr[i]);
            }

            if (typeof window.CollectGarbage === 'function') {
                CollectGarbage()
            }
        }

    }

    /**
     * 获取月份所属季度
     * @param month
     * @returns {number}
     */
    function getQuarterOfMonth(month) {
        if (month < 4)return 1;
        else if (month < 7)return 2;
        else if (month < 10)return 3;
        else return 4;
    }

    /**
     * 获取最大天数
     * @param year
     * @param month
     * @returns {*}
     */
    function getMaxDayInMonth(year, month){
        var max = dateUtils.getDaysInMonth(new Date(year, month - 1));
        var now = new Date();
        if(now.getFullYear() == year && now.getMonth()+1 == month){
            max = now.getDate();
        }

        return max;
    }

    /**
     *  获取最大月份
     * @param year
     */
    function getMaxMonthOfYear(year){
        var now = new Date();
        if(now.getFullYear() == year){
            return now.getMonth() + 1;
        }

        return 12;
    }

    /**
     * 展示图片
     * @param
     * @param showdate 展示照片的日期
     * @param datas  后台请求道的数据
     * @param options  其他参数
     * @private
     */
    function _showImage(showdate, datas, options) {
        _showDate = showdate;
        resultData = _initData(showdate.getFullYear(), showdate.getMonth() + 1, showdate.getDate(), datas);
        _renderToShow();
    }

    /**
     * 查询项目关联的所有照片记录
     * 默认是本年度的
     * @param showdate
     * @param proId
     * @param options
     * @private
     */
    function _queryProImages(showdate, proId, options) {
        _proId = proId;
        if (showdate == null) {
            showdate = new Date();
        }
        _showDate = showdate;
        var condition = {
            year: showdate.getFullYear(),
            momth: showdate.getMonth() + 1
        };

        _loadData(_proId, JSON.stringify(condition), function (data) {
            resultData = _initData(showdate.getFullYear(), showdate.getMonth() + 1, showdate.getDate(), data);
            _renderToShow();
        });
    }

    return {
        showImages: _showImage,
        queryProImages: _queryProImages
    };
});
