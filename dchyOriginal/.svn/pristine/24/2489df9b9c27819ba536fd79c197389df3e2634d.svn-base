/**
 * 独立渲染历史记录面板
 * @author: <a href="mailto:monarchcheng1993@gmail.com">wangcheng</a>
 * @date: 2016-08-23
 */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/date/locale",
    "mustache",
    "layer",
    "laypage",
    "slimScroll",
    "handlebars",
    "map/manager/LayoutManager",
    "static/js/cfg/core/SerializeForm",
    "text!static/js/map/template/inspectRecord/leas-proList.html",
    "text!static/js/map/template/inspectRecord/leas-proList-partials.html",
    "text!static/js/map/template/project/pro-record-timeline-tpl.html",
    "css!static/thirdparty/imageviewer/imageviewer.css",
    "static/thirdparty/imageviewer/imageviewer",
    "static/thirdparty/laydate/laydate",
    "static/thirdparty/jquery/jquery.xdomainrequest.min",
    "static/thirdparty/Validform/5.3.2/Validform.min",
    "static/thirdparty/jquery/jquery.ajaxfileupload",
    "css!static/js/map/template/project/recordLine.css"
], function (arrayUtil, lang, locale, Mustache, layer, Laypage, SlimScroll, Handlebars,
             LayoutManager, SerializeForm, leasProlistTpl, leasProlistPartialsTpl, proRecordsTpl) {

    var recordFormTpl = null;
    var layoutManager = LayoutManager.getInstance();
    var __config, _proid, _indexCode;//配置信息
    var _hideInspect = false;

    /**
     * 初始化，增加Date format
     * @param proid
     * @param indexCode
     * @param config
     * @private
     */
    function _init(proid, indexCode, config, hideInspect) {
        Date.prototype.format = function (format) {
            var o = {
                "M+": this.getMonth() + 1, //month
                "d+": this.getDate(), //day
                "h+": this.getHours(), //hour
                "m+": this.getMinutes(), //minute
                "s+": this.getSeconds(), //second
                "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
                "S": this.getMilliseconds() //millisecond
            };

            if (/(y+)/i.test(format)) {
                format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }

            for (var k in o) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        };
        
        _hideInspect = hideInspect;
        __config = config;
        _proid = proid;
        _indexCode = indexCode;
        Handlebars.registerHelper('isMp4', function (context, options) {
            if (context != undefined && context.endWith(".mp4")) {
                return options.fn(this);
            } else
                return options.inverse(this);
        });

        renderHistoryRecords(proid, indexCode);
    }

    /**
     * 渲染历史记录
     * @param proid
     * @param indexCode
     * @param startTime
     * @param endTime
     */
    function renderHistoryRecords(proid, indexCode, startTime, endTime) {
        if (startTime === undefined) {
            var defaultStartTime = getBeforeDate(new Date(), 6);
            defaultStartTime.setHours(0, 0, 0);
            startTime = defaultStartTime.format("yyyy-MM-dd hh:mm:ss");
        }
        if (endTime === undefined) {
            var defaultEndTime = new Date();
            defaultEndTime.setHours(23, 59, 59);
            endTime = defaultEndTime.format("yyyy-MM-dd hh:mm:ss");
        }
        var loadMsgHandler = layer.msg('后台数据通信中..', {time: 8000});
        $.getJSON(root + "/project/records/".concat(proid), {startTime: startTime, endTime: endTime}, function (data) {
            layer.close(loadMsgHandler);
            var context = {
                records: data,
                proid: proid,
                endDate: data[0].date,
                startDate: data[(data.length - 1)].date,
                hideInspect: _hideInspect
            };
            Handlebars.registerHelper('if', function (conditional, options) {
                if (conditional)
                    return options.fn(this);
                else
                    return options.inverse(this);
            });
            Handlebars.registerHelper('getClock', function (dateString) {
                if (dateString != undefined)
                    return new Handlebars.SafeString("<span class=\"time\"><i class=\"fa fa-clock-o\"></i> " + dateString.split(" ")[1] + "</span>");
            });
            Handlebars.registerHelper('none_records', function (type) {
                switch (type) {
                    case 0: //无图片提供上传
                        if (this.images.length === 0)
                            return new Handlebars.SafeString(
                                '<div style="padding: 5px;" title="可上传图片或视频"><input type="text" name="viewfile" placeholder="选择文件" data-id=' + this.date + ' class="view-file" readonly="readonly" id="viewfile_' + this.date + '"/>' +
                                '<input type="file" size="27" name="files[]" data-id=' + this.date + ' data-parent="' + proid + '" id="upload_' + this.date + '" class="upload-file" />' +
                                '<button data-id=' + this.date + ' data-parent="' + proid + '" id="btn_' + this.date + '"class="view-label submit-file"><i class="fa fa-cloud-upload"></i>  上传</button>' +
                                '</div>'
                            );
                        break;
                    case 1: //无巡查记录情况
                        if (this.inspects.length === 0) {
                            var days = new Date(this.date).getTime() - new Date().getTime();
                            var time = Math.abs(parseInt(days / (1000 * 60 * 60 * 24)));
                            var limit = 1;
                            if (__config.hasOwnProperty("inspectRecordTimeLimit"))
                                limit = Math.abs(parseInt(__config.inspectRecordTimeLimit, 10));
                            if (time < limit) { //在允许添加时间范围内给予新增
                                return new Handlebars.SafeString(
                                    '<div class="record-timeline-item"><h3 class="record-timeline-header no-border">' +
                                    '<a class="btn btn-primary btn-xs btn-flat btn-inspect-new" data-date=' + this.date + '>新增巡查记录' + //userName +
                                    '</a></h3></div>'
                                );
                            } else { //超出时间范围不允许新增
                                return new Handlebars.SafeString(
                                    '<div class="record-timeline-item"><h3 class="record-timeline-header no-border">' +
                                    '没有巡查记录！' + //userName +
                                    '</h3></div>'
                                );
                            }
                        }
                        break;
                }
            });
            var template = Handlebars.compile(proRecordsTpl);
            var html = template(context);
            $(".right_panel").empty();
            $(".right_panel").append(html);

            //历史记录界面监听
            addHistoryRecordListeners(proid, startTime, endTime);
            //缩略图点击监听
            imageRecordListeners();
            //图片上传
            imageUploadListener();
            //巡查记录监听
            inspectRecordListeners(proid);
            //打开右侧面板
            layoutManager.openRightPanel();
        });
    }


    /**
     * 历史记录操作监听
     */
    function addHistoryRecordListeners(proid, startTime, endTime) {
        var $recordWrapper = $(".record-wrapper");
        $recordWrapper.slimScroll({
            height: $(window).height() - 90,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        var startInput = {
            elem: '#startInput',
            format: 'YYYY-MM-DD',
            min: '1970-01-01 00:00:00', //设定最小日期为1970
            max: laydate.now(),//最大日期
            istime: false,
            istoday: true,
            choose: function (datas) {
                endInput.min = datas; //开始日选好后，重置结束日的最小日期
                endInput.start = datas;//将结束日的初始值设定为开始日
            }
        };
        var endInput = {
            elem: '#endInput',
            format: 'YYYY-MM-DD',
            min: '1970-01-01 00:00:00',
            max: laydate.now(),
            istime: false,
            istoday: false,
            choose: function (datas) {
                startInput.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate.skin('danlan');
        laydate(startInput);
        laydate(endInput);
        //监听操作按钮事件
        $(".record-opt>li>a").on('click', function () {
            var opt = $(this).data("opt");
            var start = new Date(startTime.replace(/-/g, '/'));
            var end = new Date(endTime.replace(/-/g, '/'));
            var s, e;
            switch (opt) {
                case 'prevWeek': //查看上一周记录
                    s = getBeforeDate(lang.clone(start), 7);
                    e = getBeforeDate(lang.clone(start), 1);
                    renderNew();
                    break;
                case 'nextWeek': //查看下一周记录
                    if (end.getTime() >= (new Date()).getTime()) {
                        layer.msg("已经是最近时间！", {icon: 0, time: 700});
                        break;
                    }
                    e = getBeforeDate(lang.clone(end), -7);
                    s = getBeforeDate(lang.clone(end), -1);
                    if (e.getTime() > (new Date()).getTime())
                        renderHistoryRecords(proid);
                    else
                        renderNew();
                    break;
                case 'customDate': //自定义选择时间范围查看记录
                    if ($('#startInput').val() == "" || $('#endInput').val() == "") {
                        layer.msg("请选择日期!", {icon: 0, time: 700});
                        return;
                    }
                    var startArray = $('#startInput').val().split('-');
                    var endArray = $('#endInput').val().split('-');
                    s = new Date(startArray[0], startArray[1] - 1, startArray[2]);
                    e = new Date(endArray[0], endArray[1] - 1, endArray[2]);
                    renderNew();
                    break;
                case 'showCustom':
                    $('#customUl').toggle();
                    $(".record-opt").toggleClass('bottom-border');
                case 'backToTop': //滚动后回归顶部
                    $recordWrapper.slimScroll({scrollTo: '0px'});
                    break;
            }

            function renderNew() {
                s.setHours(0, 0, 0);
                e.setHours(23, 59, 59);
                renderHistoryRecords(proid, undefined, s.format("yyyy-MM-dd hh:mm:ss"), e.format("yyyy-MM-dd hh:mm:ss"));
            }
        });
        $recordWrapper.slimscroll().bind('slimscrolling', function (e, pos) {
            if (pos > 0) $(".btn-top").show();
            else
                $(".btn-top").hide();
        });
        $(".time-label>span").on('click', function () {
            $(this).parent().nextUntil(".time-label").slideToggle();
        });

        //关闭记录面板
        $(".history-close").on('click', function () {
            layoutManager.hideRightPanel();
            $('.pro-info-content').removeClass('record-clicked');
        });
    }


    /**
     * 上传照片监听
     */
    function imageUploadListener() {
        var isMp4 = false;
        $(".view-file").unbind('click').on('click', function () {
            $(this).next().click();
        });
        $('.upload-file').on('change', function () {
            var id = $(this).data('id');
            $("#viewfile_" + id).val($(this).val());
            isMp4 = $(this).val().endWith(".mp4");
        });
        $(".submit-file").unbind('click').on('click', function () {
            var id = $(this).data('id');
            var proId = $(this).data("parent");
            if ($(this).prev().val() == "" || $(this).prev().val() == undefined) {
                $(this).prev().click();
                return;
            }
            var upHandler = layer.msg("上传中...", {time: 5000});
            $.ajaxFileUpload({
                fileElementId: "upload_" + id,
                url: root + '/file/pic/upload/' + proId,
                data: {picid: id}, //picId为当日的日期格式yyyy-MM-dd
                dataType: 'text',
                success: function (fs) {
                    layer.close(upHandler);
                    $("#recordBody_" + id).html(isMp4 ? '<img src="/omp/static/img/map/defaultmp4.png" data-id="' + fs + '" title="点击播放视频" data-type="mp4">' :
                        '<img src="/omp/file/thumb/' + fs + '" alt="..." style="height:136px;" data-id=' + fs + ' title="点击查看大图">');
                    imageRecordListeners();
                },
                error: function (e) {
                    console.log(e);
                }
            })
        });
    }

    /**
     * 缩略图点击监听
     */
    function imageRecordListeners() {
        $('.record-timeline-item img').unbind('click').on("click", function () {
            var imgId = $(this).data('id');
            var type = $(this).data("type");
            if (type != undefined && type === "mp4") {
                //打开视频文件预览
                layer.open({
                    type: 2,
                    title: '视频播放',
                    shade: 0,
                    area: ['640px', '320px'],
                    content: ['/omp/video/mp4player?fileId=' + imgId, 'no']
                });
            } else {
                var content = '<div style="padding: 10px;"><img id="img_{{imgId}}" style="width: 880px;height: 580px;" src="/omp/file/original/{{imgId}}"></div>';
                layer.open({
                        title: false,
                        type: 1,
                        shadeClose: true,
                        closeBtn: 1,
                        area: ['900px', '600px'],
                        content: Mustache.render(content, {imgId: imgId})
                    }
                );
            }
        });
        //del
        var $delBtn = $('.record-timeline-item button');
        if ($delBtn.length > 0) {
            $delBtn.unbind('click').on('click', function () {
                var fId = $(this).data("rel");
                $.ajax({
                    url: root + "/file/delete/" + fId,
                    success: function (evt) {
                        if (evt.hasOwnProperty("success")) {
                            console.error(evt.msg);
                        } else {
                            layer.msg("删除成功!");
                            renderHistoryRecords(_proid, _indexCode);
                        }
                    }
                });
            });
        }
    }


    /**
     * 巡查结果页面监听
     */
    function inspectRecordListeners(proid, recordPage) {
        /**
         * 获取巡查表单模板
         */
        if (recordFormTpl == null) {
            $.ajax({
                "url": root + "/project/inspect/record/tpl",
                success: function (tpl) {
                    recordFormTpl = tpl.result;
                }
            });
        }

        /**
         * 新增巡查记录
         */
        addNewInspectRecord(proid, recordPage);

        /**
         * 查看巡查记录
         */
        $(".inspect-record-name").unbind('click').on('click', function () {
            var date = $(this).data("date");
            $.ajax({
                url: root + '/project/inspect/record/get/' + $(this).data('id'),
                method: 'post',
                success: function (rp) {
                    if (rp.result.sfyswf != 'false') {
                        rp.result.isChecked = "checked";
                    }

                    Handlebars.registerHelper('selected', function (param, itemName) {
                        if (param == itemName)
                            return "selected"
                        return "";
                    });
                    var formContent = Handlebars.compile(recordFormTpl);
                    //功能按钮数组
                    var layerBtns = [
                        ['照片', 'fa fa-file-image-o', proid],
                        ['视频', 'fa fa-eye', proid]
                    ];
                    //开启开关需要推送才会添加推送功能
                    if (isNeedSend) {
                        layerBtns.push(['推送', 'fa fa-paper-plane', '']);
                    }
                    var index = layer.open({
                        title: '查看巡查记录',
                        type: 1,
                        content: formContent(rp.result), //这里content是一个普通的String
                        area: '700px',
                        optbtn: layerBtns,
                        optbtn1: function (index, btn) {
                            showRecordImages(proid, date);
                        },
                        optbtn2: function (index, btn) {
                            startupVideoByProId(proid);
                            //startupVideo(0, $(btn).data("token"));
                        },
                        optbtn3: function (index, btn) {
                            var inspectInfo = SerializeForm.serializeObject($("#inpectForm"));
                            layer.close(index);
                            sendInspectRecord(0, 7, "{}", inspectInfo.id, recordPage);
                        },
                        success: function () {
                            $('#inpectForm').find(":checkbox").iCheck({
                                checkboxClass: 'icheckbox-blue',
                                radioClass: 'iradio-blue'
                            });

                            //如果是疑似违法或者已推送的，按钮灰掉
                            if (rp.result.leasProId != undefined || rp.result.sfyswf == 'false') {
                                $(".layui-layer-optbtn .btn").each(function () {
                                    if ($.trim($(this).text()) == "推送") {
                                        $(this).attr({"disabled": "disabled"});
                                    }
                                });
                            }

                            $("#sfyswf").on('ifChecked', function (event) {
                                $("input[name=sfyswf]").val(true);
                            });

                            $("#sfyswf").on('ifUnchecked', function (event) {
                                $("input[name=sfyswf]").val(false);
                            });

                            save(proid, "编辑记录成功！", "edit", function () {
                                layer.close(index);
                            });
                        }
                    });
                }
            });
        });

        /**
         * 删除巡查记录
         */
        $(".inspect-record-delete").unbind('click').on('click', function () {
            var id = $(this).data('id');
            var today = $(this).data("today");
            var date = $(this).data('date');
            var days = new Date(date).getTime() - new Date().getTime();
            var time = Math.abs(parseInt(days / (1000 * 60 * 60 * 24)));
            var limit = 1;
            if (__config.hasOwnProperty("inspectRecordTimeLimit"))
                limit = Math.abs(parseInt(__config.inspectRecordTimeLimit, 10));
            layer.confirm('确定要删除该记录吗？', {
                btn: ['是', '否'] //按钮
            }, function () {
                $.ajax({
                    url: root + '/project/inspect/record/del/' + id,
                    method: 'post',
                    async: false,
                    success: function (rp) {
                        if (rp.result == true) {
                            $("#inspect_" + id).remove();
                            var html = "";
                            if (time < limit) { //在允许添加时间范围内给予新增
                                html = '<div class="record-timeline-item"><h3 class="record-timeline-header no-border">' +
                                    '<a class="btn btn-primary btn-xs btn-flat btn-inspect-new" data-date=' + date + '>新增巡查记录' + //userName +
                                    '</a></h3></div>';
                            } else { //超出时间范围不予新增
                                html = '<div class="record-timeline-item"><h3 class="record-timeline-header no-border">' +
                                    '没有巡查记录！' +
                                    '</a></h3></div>'
                            }
                            $("#inspectRecord_" + date).append(html);
                            addNewInspectRecord(proid);
                            layer.msg("删除成功！", {time: 1500})
                        }
                    }
                });
            });
        });
    }

    /**
     * 根据peoid启动摄像头
     * @param proId
     */
    function startupVideoByProId(proId) {
        $.getJSON(root + "/video/cameras/fetch/".concat(proId), function (data) {
            startupVideo(0, data);
        })
    }

    /***
     * 打开预览视频
     * 从项目处进入后
     * @param type    类型 0--预览视频 1---设置预置位
     * @param pNo     预置位编号  type=0 时可为空 type=1 时不可为空
     * @param camera  监控点的设备编码/监控点对象/监控点数组
     */
    function startupVideo(type, camera, pNo) {
        var vcuList = [];
        var userName = null, userPwd = null, userDomain = null;
        if (typeof camera === "string") {
            var tmp = undefined;
            if (tmp !== undefined) {
                var _a = {};
                if (platform === 'hw' || platform === "dh") {
                    _a.VcuId = tmp.regionId;
                    _a.VcuName = tmp.regionName;
                    if (tmp.hasOwnProperty("username")) {
                        userName = tmp.username.split("@")[0];
                        userPwd = tmp.password;
                        userDomain = tmp.username.split("@")[1];
                    }
                }
                _a.devicelist = [
                    {
                        DeviceId: tmp.indexCode,
                        DeviceName: platform === 'hw' ? tmp.name : encodeURIComponent(tmp.name),
                        IndexCode: tmp.indexCode,
                        nodaname: platform === 'hw' ? tmp.name : encodeURIComponent(tmp.name),
                        devid: tmp.id,
                        cmrip: tmp.ip,
                        cmrport: tmp.port,
                        cmruser: username,
                        cmrpass: userpwd,
                        serverip: server,
                        serverport: serverPort
                    }
                ];
                vcuList.push(_a);
            }
        } else if (typeof camera === "object") {
            if (lang.isArray(camera)) {
                arrayUtil.forEach(camera, function (item) {
                    var v = {};
                    if (platform === 'hw' || platform === "dh") {
                        v.VcuId = item.regionId;
                        v.VcuName = item.regionName;
                        if (item.hasOwnProperty("username")) {
                            userName = item.username.split("@")[0];
                            userPwd = item.password;
                            userDomain = item.username.split("@")[1];
                        }
                    }
                    v.devicelist = [{
                        DeviceId: item.indexCode,
                        DeviceName: platform === 'hw' ? item.name : encodeURIComponent(item.name),
                        IndexCode: item.indexCode,
                        nodaname: platform === 'hw' ? item.name : encodeURIComponent(item.name),
                        devid: item.id,
                        cmrip: item.ip,
                        cmrport: item.port,
                        cmruser: username,
                        cmrpass: userpwd,
                        serverip: server,
                        serverport: serverPort
                    }];
                    vcuList.push(v);
                });
            } else {
                var vcu = {};
                if (platform === 'hw' || platform === "dh") {
                    vcu.VcuId = camera.regionId;
                    vcu.VcuName = camera.regionName;
                    if (camera.hasOwnProperty("username")) {
                        userName = camera.username.split("@")[0];
                        userPwd = camera.password;
                        userDomain = camera.username.split("@")[1];
                    }
                }
                vcu.devicelist = [
                    {
                        DeviceId: camera.indexCode,
                        DeviceName: platform === 'hw' ? camera.name : encodeURIComponent(camera.name),
                        IndexCode: camera.indexCode,
                        nodaname: platform === 'hw' ? camera.name : encodeURIComponent(camera.name),
                        devid: camera.id,
                        cmrip: camera.ip,
                        cmrport: camera.port,
                        cmruser: username,
                        cmrpass: userpwd,
                        serverip: server,
                        serverport: serverPort
                    }
                ];
                vcuList.push(vcu);
            }
        }
        if (vcuList.length > 0) {
            switch (type) {
                case 0:
                    previewCamera(pNo, vcuList, userName, userPwd, userDomain);
                    break;
                case 1:
                    preSetCamera(pNo, "预置位" + pNo, vcuList, userName, userPwd, userDomain);
                    break;
            }
        } else
            console.log("打开视频错误.");
    }

    /**
     * 保存巡查记录
     * @param proid
     * @param msg
     * @param type
     * @param callback
     */
    function save(proid, msg, type, callback) {
        $("#inpectForm").Validform({
            tiptype: 3,
            label: ".label",
            showAllError: true,
            ajaxPost: true,
            callback: function (data) {
                $("#addInspectRecord").attr("data-user");
                var data = JSON.stringify(SerializeForm.serializeObject($("#inpectForm")));
                var limit = 1;
                if (__config.hasOwnProperty("inspectRecordTimeLimit"))
                    limit = Math.abs(parseInt(__config.inspectRecordTimeLimit, 10));
                $.ajax({
                    url: root + '/project/inspect/record/save',
                    method: "post",
                    data: {data: data, proId: proid, type: type, limitTime: limit},
                    success: function (rp) {
                        if (rp.id) {
                            $("#inpectForm").find("input[name='id']").val(rp.id);
                            callback(rp);
                            layer.msg(msg, {
                                time: 2000
                            });
                            var context = {};
                            context.createAt = rp.createAt;
                            context.name = rp.name;
                            context.id = rp.id;
                            var date = (rp.createAt).split(" ")[0];
                            var today = locale.format(new Date(), {datePattern: 'yyyy-MM-dd', selector: 'date'});
                            context.date = date;
                            context.sfyswf = rp.sfyswf;
                            if (rp.leasProId) {
                                context.leasProId = rp.leasProId;
                            }
                            if (date == today) {
                                context.isToday = true;
                            }
                            Handlebars.registerHelper('getClock', function (dateString) {
                                if (dateString != undefined)
                                    return new Handlebars.SafeString("<span class=\"time\"><i class=\"fa fa-clock-o\"></i> " + dateString.split(" ")[1] + "</span>");
                            });
                            var html = '<i class="fa fa-user bg-aqua"></i>' +
                                '<div class="record-timeline-item" id="inspect_{{id}}">' +
                                '{{getClock createAt}}' +
                                '<h3 class="record-timeline-header no-border"><a href="#" class="inspect-record-name" data-id="{{id}}" data-date="{{date}}">{{name}}</a></h3>' +
                                '<i class="fa fa-times inspect-record-delete" data-id="{{id}}" title="删除记录" data-today="{{#if isToday}}today{{else}}{{/if}}" data-date="{{date}}" ></i>' +
                                '<i class="fa fa-warning pull-right illegal-icon {{#if sfyswf}}{{else}}hidden{{/if}}" title="疑似违法"></i>' +
                                ' <i id="sent_{{id}}" class="fa fa-paper-plane-o sent-icon {{#if leasProId}}{{else}}hidden{{/if}}" title="已推送"></i>' +
                                '</div>';
                            var tpl = Handlebars.compile(html);
                            $('#inspectRecord_' + date).html(tpl(context));
                            inspectRecordListeners(proid);
                        } else {
                            layer.msg(rp.message);
                        }
                    }
                });
                return false;
            }
        });
    }

    /**
     * 新增巡查记录
     * @param proid
     */
    function addNewInspectRecord(proid, recordPage) {
        $(".btn-inspect-new").unbind('click').on('click', function () {
            var date = new Date($(this).data('date'));
            var createAt = locale.format(date, {datePattern: 'yyyy-MM-dd HH:mm:ss', selector: 'date'});
            var name = userName + '  ' + locale.format(date, {datePattern: 'yyyy-MM-dd', selector: 'date'}) + ' 巡查记录';
            //功能按钮数组
            var layerBtns = [
                ['照片', 'fa fa-file-image-o', proid],
                ['视频', 'fa fa-eye', proid]
            ];
            //开启开关需要推送才会添加推送功能
            if (isNeedSend) {
                layerBtns.push(['推送', 'fa fa-paper-plane', '']);
            }
            var index = layer.open({
                title: '新增巡查记录',
                type: 1,
                content: Mustache.to_html(recordFormTpl, {
                    id: Math.uuid(),
                    name: name,
                    inspector: userName,
                    createAt: createAt
                }), //这里content是一个普通的String
                area: '700px',
                optbtn: layerBtns,
                optbtn1: function (index, btn) {
                    showRecordImages($(btn).data("token")); //展示照片
                },
                optbtn2: function (index, btn) {
                    startupVideoByProId(proid); //查看视频
                },
                optbtn3: function (index, btn) {
                    var inspectInfo = SerializeForm.serializeObject($("#inpectForm"));
                    layer.close(index);
                    sendInspectRecord(0, 7, "{}", inspectInfo.id, recordPage); //推送巡查记录
                },
                success: function () {
                    //未新增巡查记录不显示推送按钮
                    $(".layui-layer-optbtn .btn").each(function () {
                        if ($.trim($(this).text()) == "推送") {
                            $(this).css("display", "none");
                        }
                    });
                    $('#inpectForm').find(":checkbox").iCheck({
                        checkboxClass: 'icheckbox-blue',
                        radioClass: 'iradio-blue'
                    });

                    $("#sfyswf").on('ifChecked', function (event) {
                        $("input[name=sfyswf]").val(true);
                    });

                    $("#sfyswf").on('ifUnchecked', function (event) {
                        $("input[name=sfyswf]").val(false);
                    });

                    save(proid, "生成记录成功！", 'save', function (record) {
                        //记录保存成功后添加推送按钮
                        //如果是疑似违法，按钮灰掉
                        $(".layui-layer-optbtn .btn").each(function () {
                            if ($.trim($(this).text()) == "推送" && (record.sfyswf == true || record.sfyswf == 'true')) {
                                $(this).css("display", "inline-block");
                            } else if ($.trim($(this).text()) == "推送" && record.sfyswf == '') {
                                $(this).css("display", "none");
                            }
                        });
                        layer.close(index);
                    });
                }
            });
        });
    }


    /**
     * 展示记录照片
     * @param proid
     * @param date
     */
    function showRecordImages(proid, date) {
        $.getJSON(root + "/file/records/getByDate", {date: date, proId: proid}, function (rp) {
            var json = {
                "title": "照片记录",
                "id": proid + date,
                "start": 0
            };

            var data = [];
            for (var i in rp) {
                var d = {
                    "pid": rp[i],
                    "src": "/omp/file/original/" + rp[i]
                };

                data.push(d);
            }
            json.data = data;
            layer.photos({
                area: ['1100px', '600px'],
                photos: json
            });

        });
    }


    /**
     * 推送巡查记录到执法监察系统
     * @param recordPage
     * @param page
     * @param size
     * @param request
     * @param id     巡查记录的主键id
     */
    function sendInspectRecord(page, size, request, id, recordPage) {
        var result, isError = false;
        var index;

        function _loadData(page, size, request, callback) {
            var condition = $.parseJSON(request);
            var linkQueryStr = '';
            if (condition != undefined && typeof condition == 'object') {
                for (p in condition) {
                    linkQueryStr = "&" + p + "=" + encodeURI(condition[p]);
                }
            }
            //Cross-Domain AJAX for IE8 and IE9
            $.ajax({
                url: leasUrl + "/project/remoteSearch?page=" + page + "&size=" + size + linkQueryStr,
                contentType: 'text/plain',
                type: 'POST',
                dataType: 'json'
            }).done(function (data) {
                result = data.result;
                if (callback != undefined) callback(result);

            }).error(function (jqXHR, textStatus) {
                if (jqXHR.readyState == 0) {
                    isError = true;
                    layer.msg("执法监察系统接口无法访问！");
                } else if (jqXHR.readyState == 4) {
                    isError = true;
                    layer.msg("执法监察系统接口访问异常，错误代码：" + jqXHR.status + "！");
                }
            });
        };

        /**
         * 分页展示
         * @param records
         * @param page
         * @param size
         * @param condition
         * @private
         */
        function _showOnPage(records, page, size, condition) {
            Laypage({
                cont: $('#pageCtx'),
                pages: parseInt(records.totalPages),
                first: false,
                last: false,
                curr: function () {
                    return page ? page : 1;
                }(),
                jump: function (e, first) { //触发分页后的回调
                    if (!first) { //一定要加此判断，否则初始时会无限刷新
                        _loadData(e.curr - 1, size, condition, function (data) {
                            var partialsCont = Handlebars.compile(leasProlistPartialsTpl);
                            $("#leasProListItems").empty();
                            $("#leasProListItems").append(partialsCont(data));
                        });
                    }
                }
            });
        }

        function _openSendWindow() {
            Handlebars.registerPartial("listItems", leasProlistPartialsTpl);
            var listContent = Handlebars.compile(leasProlistTpl);

            index = layer.open({
                title: '<i class="fa fa-paper-plane-o" aria-hidden="true"></i>  推送巡查记录',
                type: 1,
                content: listContent(result), //这里content是一个普通的String
                area: ['950px', '580px'],
                success: function () {
                    var record;
                    //获取发送巡查记录的内容
                    $.ajax({
                        url: root + '/project/inspect/record/send/' + id,
                        success: function (rp) {
                            record = rp.result;
                            if (record.leasProId) {
                                $("#send").attr("disabled", "disabled");
                            }
                        }
                    });
                    var start = {
                        elem: '#startTime',
                        path: "/laydate",
                        format: 'YYYY-MM-DD hh:mm:ss',
                        max: '2099-06-16 23:59:59', //最大日期
                        istime: true,
                        istoday: false,
                        choose: function (datas) {
                            end.min = datas; //开始日选好后，重置结束日的最小日期
                            end.start = datas;//将结束日的初始值设定为开始日
                        }
                    };
                    var end = {
                        elem: '#endTime',
                        path: "/laydate",
                        format: 'YYYY-MM-DD hh:mm:ss',
                        max: '2099-06-16 23:59:59',
                        istime: true,
                        istoday: false,
                        choose: function (datas) {
                            start.max = datas; //结束日选好后，重置开始日的最大日期
                        }
                    };

                    $("#close").on('click', function () {
                        layer.close(index);
                    });

                    $("#send").on('click', function () {
                        if ($('#leasProListItems input:radio:checked').val() == undefined || $('#leasProListItems input:radio:checked').val() == null) {
                            layer.msg("请选择一个项目后推送！");
                            return;
                        }

                        sendHttpToLeas(record)
                        //发送请求
                        function sendHttpToLeas(data) {
                            var linkQueryStr = "?proId=" + $('#leasProListItems input:radio:checked').val() + "&data=" + encodeURI(JSON.stringify(data));
                            $.ajax({
                                url: leasUrl + '/project/remoteSave' + linkQueryStr,
                                contentType: 'text/plain',
                                type: 'POST',
                                dataType: 'json'
                            }).done(function (rp) {
                                if (rp.result) { // 更新记录状态
                                    var newRecord = {
                                        id: data.recordId,
                                        leasProId: $('#leasProListItems input:radio:checked').val()
                                    };

                                    $.ajax({
                                        url: root + '/project/inspect/record/save',
                                        method: "post",
                                        data: {
                                            data: JSON.stringify(newRecord),
                                            proId: $("#record").attr("data-proid"),
                                            type: 'edit'
                                        },
                                        success: function (rp) {
                                            if (rp.id) {
                                                layer.msg("记录推送成功，状态已改变！", {
                                                    time: 2000
                                                }, function () {
                                                    layer.close(index);
                                                    $('#sent' + rp.id).removeClass("hidden");
                                                });
                                            } else {
                                                layer.msg("推动记录成功，记录状态更新异常：" + rp.message);
                                            }
                                        }
                                    });
                                } else {
                                    layer.msg("巡查记录推送失败！");
                                }
                            }).error(function (jqXHR, textStatus) {
                                if (jqXHR.readyState == 0) {
                                    isError = true;
                                    layer.msg("执法监察系统接口无法访问！");
                                } else if (jqXHR.readyState == 4) {
                                    isError = true;
                                    layer.msg("执法监察系统接口访问异常，错误代码：" + jqXHR.status + "！");
                                }
                            });
                        }
                    });

                    $("#startTime").on('click', function () {
                        laydate(start);
                    });
                    $("#endTime").on('click', function () {
                        laydate(end);
                    });

                    $("#search").on('click', function () {
                        var condition = {};
                        arrayUtil.forEach($("#leasForm").serializeArray(), function (item) {
                            if (item.value != "") {
                                condition[item.name] = item.value;
                            }
                        });
                        var load = layer.load(2);
                        _loadData(0, 7, JSON.stringify(condition), function (data) {
                            layer.close(load);
                            var partialsCont = Handlebars.compile(leasProlistPartialsTpl);
                            $("#leasProListItems").empty();
                            $("#leasProListItems").append(partialsCont(data));
                            $('#pageCtx').empty();
                            _showOnPage(data, page, size, JSON.stringify(condition));
                        });
                    });

                    _showOnPage(result, page, size, request);

                }
            });
        }

        _loadData(page, size, request, function () {
            if (!isError) {
                _openSendWindow();
            }
        });
    }

    /**
     * 获取n天前的日期
     * @param d
     * @param n
     * @returns {Date}
     */
    function getBeforeDate(d, n) {
        var n = n;
        var year = d.getFullYear();
        var mon = d.getMonth() + 1;
        var day = d.getDate();
        if (day <= n) {
            if (mon > 1) {
                mon = mon - 1;
            }
            else {
                year = year - 1;
                mon = 12;
            }
        }
        d.setDate(d.getDate() - n);
        year = d.getFullYear();
        mon = d.getMonth() + 1;
        day = d.getDate();
        var s = year + "-" + (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day);
        return new Date(year, mon - 1, day);
    }

    return {
        renderRecord: _init
    };
});