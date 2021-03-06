/**
 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
 * @description  公共方法 js
 */
//获取当前ip地址
function getContextPath(a) {
    var curRequestPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var ipAndPort = curRequestPath.indexOf(pathName);
    var localhostPath = curRequestPath.substring(0, ipAndPort);
    var _ip = curRequestPath.substring(0, ipAndPort - 5);
    if (a) {
        //return _ip + ':8085' ; //不加端口号IP
        return localhostPath;  //加端口号IP
    } else {
        return localhostPath + '/portal';  //加端口号IP
        //return localhostPath;
    }
}

function iframeIp() {
    return "http://192.172.0.101:8087/portal-ui/src/main/webapp/view";
}

function getIpHz() {
    var $paramArr = [];
    var $params = window.location.search.replace('?', '');
    var $paramSplit = $params.split('&');
    for (var i in $paramSplit) {
        var $subParam = $paramSplit[i].split('=');
        $paramArr[$subParam[0]] = $subParam[1];
    }
    return $paramArr;
}

function GetQueryString(url, name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = url.match(reg);//匹配正则
    if (r != null) return unescape(r[2]);
    return null;
}

//时间戳转换
Date.prototype.toLocaleString = function () {
    var month, date, hours, minutes, seconds;
    if (this.getMonth() + 1 < 10) {
        month = "0" + (this.getMonth() + 1);
    } else {
        month = '' + (this.getMonth() + 1);
    }
    if (this.getDate() < 10) {
        date = "0" + this.getDate();
    } else {
        date = this.getDate();
    }
    if (this.getHours() < 10) {
        hours = "0" + this.getHours();
    } else {
        hours = this.getHours();
    }
    if (this.getMinutes() < 10) {
        minutes = "0" + this.getMinutes();
    } else {
        minutes = this.getMinutes();
    }
    if (this.getSeconds() < 10) {
        seconds = "0" + this.getSeconds();
    } else {
        seconds = this.getSeconds()
    }
    return this.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
//使用例子
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") //输出结果： 2017-01-23 09:36:10.400
// (new Date()).Format("yyyy-M-d h:m:s.S")      //输出结果： 2017-1-23 9:36:35.572
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/**
 * 判断字符串是否为空
 *
 * @param str  目标字符串
 * @returns {boolean}  为空：true ; 不为空：false
 */
function isNullOrEmpty(str) {
    if (!str || "" == str || "null" == str || undefined == str || "undefined" == str) {
        return true;
    }

    return false;
}

/**
 * 警告提示
 * @param msg
 */
function warnMsg(msg) {
    layer.msg('<img src="../static/lib/bdcui/images/warn-small.png" alt="">' + msg);
}

function closeWin() {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

function closeParentWindow() {
    var index = parent.parent.layer.getFrameIndex(window.name);
    parent.parent.layer.close(index);
}

//表格刷新
function renderTable(url, tableId, currentId) {
    layui.use(['jquery', 'table'], function () {
        var table = layui.table;
        table.reload(currentId);
    })
}

//table刷新
function renderCurrentTable() {
    $(".bdc-list-tab .layui-tab-title .layui-this").click();
}

//把添加遮罩及移除遮罩方法添加在自己的通用js中，或者引入当前js，注释上面两行，调用即可使用
function addModel(message) {
    if (isNullOrEmpty(message)) {
        message = '加载中';
    }
    var modalHtml = '<div id="waitModalLayer">' +
        '<p class="bdc-wait-content">' +
        '<i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i>' +
        '<span>' + message + '</span>' +
        '</p>' +
        '</div>';
    $('body').append(modalHtml);
}

function removeModal() {
    $("#waitModalLayer").remove();
}

//叫号特定遮罩
function jhAddModel(message) {
    if (isNullOrEmpty(message)) {
        message = '加载中';
    }
    var modalHtml = '<div id="waitModalLayer" class="bdc-jh-model">' +
        '<p class="bdc-wait-content">' +
        '<i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i>' +
        '<span>' + message + '</span>' +
        '</p>' +
        '</div>';
    $('.bdc-jh-layer').append(modalHtml);
}

function removeJhModal() {
    $(".bdc-jh-model").remove();
}

//楼盘表跨域方法
function excute(url) {
    document.getElementById('test2').src = url;
}

//新增tab
function addTab(url, name) {
    var getIframeIndex = $('.layui-body .layadmin-tabsbody-item.layui-show', window.parent.document).index() + 1;

    $('.bdc-outer-tab>.layui-tab-title').find('li').removeClass('layui-this');
    $('.layui-body').find('.layadmin-tabsbody-item').removeClass('layui-show');
    $('.bdc-outer-tab>.layui-tab-title li:nth-child(' + getIframeIndex + ')').after('<li lay-id="test.html" class="layui-this"><i class="layui-icon layui-icon-left bdc-child-img"></i><span>' + name + '</span><i class="layui-icon layui-unselect layui-tab-close bdc-tab-close">ဆ</i></li>');
    $('.layui-body .layadmin-tabsbody-item:nth-child(' + getIframeIndex + ')').after('<div class="layadmin-tabsbody-item layui-show">' +
        '<iframe src="' + url + '" frameborder="0" class="layadmin-iframe"></iframe>' +
        '</div>');
}

//删除tab
function deleteTab() {
    var _this = $('.bdc-outer-tab .layui-tab-title .layui-this');
    var refreshIndex = _this.index() - 1;
    var prevIndex = _this.index();
    var nowIndex = _this.index() + 1;

    _this.parent().find('li').removeClass('layui-this');
    _this.parent().find('li:nth-child(' + prevIndex + ')').addClass('layui-this');
    _this.remove();
    $('.layui-body .layadmin-tabsbody-item:nth-child(' + nowIndex + ')').remove();
    $('.layui-body .layadmin-tabsbody-item').removeClass('layui-show');
    $('.layui-body .layadmin-tabsbody-item:nth-child(' + prevIndex + ')').addClass('layui-show');

    var thisLi = $('.bdc-outer-tab .layui-tab-title .layui-this');
    if (thisLi.attr('data-refresh') == 1) {
        var $frame = $('.layui-body .layadmin-tabsbody-item.layui-show iframe:first-child');
        $frame.attr('src', $frame.attr('src'));

        thisLi.removeAttr('data-refresh');
    }
}

//根据url刷新指定iframe
function refreshIframe(url, isRefresh) {
    // console.log(url);
    var iframeList = $('.layui-body .layadmin-tabsbody-item iframe:first-child');
    var index = 9999;
    for (var i = 0; i < iframeList.length; i++) {
//            console.log(iframeList[i].src);
        if (iframeList[i].src == url) {
            index = i;
        }
    }
    // console.log(index);
    if (index != 9999) {
        var newIndex = index + 2;
        if (isRefresh) {
            var $tab = $('.bdc-outer-tab .layui-tab-title li:nth-child(' + newIndex + ')');
            $tab.attr('data-refresh', '1');
        } else {
            var $frame = $('.layui-body .layadmin-tabsbody-item:nth-child(' + newIndex + ') iframe:first-child');
            // console.log($frame);
            $frame.attr('src', $frame.attr('src'));
        }
    }
}

//根据url替换指定iframe的url
function changeIframeUrl(oldUrl, newUrl) {
    // console.log(url);
    var iframeList = $('.layui-body .layadmin-tabsbody-item iframe:first-child');
    var changeIndex = 9999;
    for (var i = 0; i < iframeList.length; i++) {
//            console.log(iframeList[i].src);
        if (iframeList[i].src == oldUrl) {
            changeIndex = i;
        }
    }
    // console.log(index);
    if (changeIndex != 9999) {
        var newIndex = changeIndex + 2;
        var $frame = $('.layui-body .layadmin-tabsbody-item:nth-child(' + newIndex + ') iframe:first-child');
        // console.log($frame);
        $frame.attr('src', newUrl);
    }
}

/**
 * 不动产单元号分层显示
 */
function queryBdcdyh(bdcdyh) {
    bdcdyh.replace(/\s/g, "*");
    var newBdcdyh = bdcdyh.substring(0, 6) + " " + bdcdyh.substring(6, 12) + " " + bdcdyh.substring(12, 19) + " " + bdcdyh.substring(19, bdcdyh.length);
    return newBdcdyh;
}

//验证是否是虚拟单元号
function checkXn(bdcdyh) {
    if (bdcdyh && bdcdyh.length == 28 && bdcdyh.slice(6, 12) == '000000' && bdcdyh.slice(13, 14) != 'G' && bdcdyh.slice(13, 14) != 'H') {
        return "是";
    } else {
        return "否";
    }
}

function reverseString(str) {
    var RexStr = /\（|\）|\(|\)/g;
    str = str.replace(RexStr, function (MatchStr) {
        switch (MatchStr) {
            case "(":
                return ")";
                break;
            case ")":
                return "(";
                break;
            case "（":
                return '）';
                break;
            case "）":
                return "（";
                break;
        }
    });
    return str.split("").reverse().join("");
}

function reverseTableCell(reverseList) {
    var getTd = $('.layui-table-view .layui-table td');
    for (var i = 0; i < getTd.length; i++) {
        reverseList.forEach(function (v) {
            if ($(getTd[i]).attr('data-field') == v) {
                var getTdCell = $(getTd[i]).find('.layui-table-cell');
                getTdCell.css('direction', 'rtl');
                if (getTdCell.find('span').length == 0) {
                    var tdHtml = reverseString(getTdCell.html());
                    getTdCell.html('<span class="bdc-table-date">' + tdHtml + '</span>');
                }
            }
        });
    }
}

/**
 * 查看流程图
 */
function viewLct(gzlslid, tableId) {
    var strId = tableId == 'xmTable' ? '?wiid=' : '?taskid=';
    var _url = '';
    $.ajax({
        type: "get",
        url: getContextPath() + "/index/getCasUrl",
        async: false,
        success: function (res) {
            _url = res;
            console.log('_url:', _url + '/platform/showchart.action' + strId + gzlslid);
        }
    });

    var index = layer.open({
        title: '流程详细页面',
        type: 2,
        area: ['1150px', '600px'],
        content: [_url + '/platform/showchart.action' + strId + gzlslid]
    });
    layer.full(index);
}

//数据交互
function getReturnData(_path, _param, _type, _fn, async, ipType) {
    layui.use(['jquery', 'response'], function () {
        var $ = layui.jquery,
            response = layui.response,
            _url = ipType ? getContextPath(ipType) + _path : getContextPath() + _path;
        $.ajax({
            url: _url,
            type: _type,
            async: async ? false : true,
            contentType: 'application/json;charset=utf-8',
            data: _param,
            success: function (data) {
                _fn.call(this, data, data);
            },
            error: function (err) {
                removeModal();
                response.fail(err, '');
            }
        });
    });
}

/**
 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
 * @description 获取当前用户
 */
function queryCurrentUser() {
    var user = '';
    $.ajax({
        type: "GET",
        url: getContextPath() + "/rest/v1.0/user/info",
        async: false,
        data: {},
        success: function (data) {
            user = data;
        }, error: function (e) {
            response.fail(e, '');
        }
    });
    return user;
}

/**
 * 格式化 bdcdyh
 * @param bdcdyh 不动产单元号
 * @return {string} 返回格式化的不动产单元号字符串
 */
function formatBdcdyh(bdcdyh) {
    var result = "";
    if (!isNullOrEmpty(bdcdyh) && bdcdyh.length == 28) {
        result = bdcdyh.substring(0, 6) + ' '
            + bdcdyh.substring(6, 12) + ' '
            + bdcdyh.substring(12, 19) + ' '
            + bdcdyh.substring(19, 28);
    } else {
        result = bdcdyh;
    }
    return result;
}

/**
 * 日期格式化
 * @param timestamp
 * @returns {*}
 */
function formatDate(timestamp) {
    if (!timestamp) {
        return '';
    }

    var time = new Date(timestamp);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}

function add0(time) {
    if (time < 10) {
        return '0' + time;
    }
    return time;
}

function completeDate(date1, date2) {
    var oDate1 = new Date(date1);
    var oDate2 = new Date(date2);
    if (oDate1.getTime() > oDate2.getTime()) {
        return true;
    } else {
        return false;
    }
}

/**
 *根据模块管理中的元素配置设置权限
 * @param moduleCode 资源名
 */
function setElementAttrByModuleAuthority(moduleCode) {
    $.ajax({
        url: getContextPath() + "/rest/v1.0/workflow/process-instances/moduleAuthority/" + moduleCode,
        type: 'GET',
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data) {
                for (var i in data) {
                    id = i;
                    attr = data[i];
                    var element = document.getElementById(id);
                    if (element !== null) {
                        //1.隐藏2.只读3.必填
                        if (attr === "hidden") {
                            if (element.type === "button" || element.type === "submit" || element.type === "reset") {
                                element.setAttribute("style", "display:none;");
                            } else if (element.type === "textarea") {
                                element.parentElement.parentElement.setAttribute('hidden', 'hidden');
                            } else {
                                element.parentElement.parentElement.setAttribute('style', 'display:none;');
                            }
                        } else if (attr === "readonly") {
                            element.setAttribute("disabled", "off");
                            if (element.type === "button" || element.type === "submit" || element.type === "reset") {
                                // element.setAttribute('style',"background-color:#C7C4C1;color:black");
                                element.classList.add("layui-btn-disabled");
                            }
                        } else if (attr === "required") {
                            element.setAttribute('lay-verify', 'required');
                        }
                    }
                }
            }
            // 这里对input框进行样式修改，select框需要在各页面渲染处修改
            resetInputDisabledCss();
        }, error: function (xhr, status, error) {
            failMsg("页面权限请求设置失败，请联系管理员！");
        }
    });
}


//右下角弹出提示内容
function bottomShowTips(msg) {
    var bottomLayer = layer.open({
        type: 1,
        title: false,
        skin: 'bdc-tips-shadow',
        closeBtn: 0, //不显示关闭按钮
        shade: 0,
        area: ['240px'],
        offset: 'rb', //右下角弹出
        time: 5000,
        anim: 2,
        content: '<div class="bdc-bottom-tips" id="bottomTips" style="background-color:#fff;border-left: 4px solid #1d87d1;padding:10px 15px 15px;">' +
        '<h3 style="font-size: 16px;line-height: 30px;position: relative;margin-bottom: 6px;">您有一条新的信息:<i class="layui-icon layui-icon-close" style="font-size:20px;position: absolute;top: 50%;right: 0;transform: translateY(-50%);"></i></h3>' +
        '<p style="font-size: 14px;line-height: 24px;">' + msg + '</p>' +
        '</div>',
        success: function () {
            $('.bdc-tips-shadow .bdc-bottom-tips .layui-icon-close').on('click', function () {
                layer.close(bottomLayer);
            })
        }
    });
}

//右下角弹出强提示内容
function strongTips(msg, _fn) {
    var strongTipsHtml = '';
    if (msg) {
        msg.forEach(function (v) {
            strongTipsHtml += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">' + v + '</p>';
        });
    }
    var submitIndex = layer.open({
        type: 1,
        title: '提示',
        skin: 'bdc-strong-tips',
        area: ['320px'],
        content: strongTipsHtml,
        btn: ['确定'],
        btnAlign: 'c',
        closeBtn: 0,
        yes: function () {
            if (_fn) {
                _fn.call(this);
            }
            layer.close(submitIndex);
        }
    });
}

function failMsg(msg) {
    layer.msg('<img src="../static/lib/bdcui/images/error-small.png" alt="">' + msg);
}

//确认性基础弹出框
function baseLayer(content, _fn) {
    var index = layer.open({
        type: 1,
        title: '提示',
        skin: 'bdc-small-tips bdc-zf-tips',
        area: ['320px', '150px'],
        content: '是否' + content + '？',
        btn: ['确定', '取消'],
        btnAlign: 'c',
        yes: function () {
            layer.close(index);
            if (_fn) {
                _fn.call(this);
            }
        }
    });
}

/**
 * 设置input不可编辑的样式
 */
function resetInputDisabledCss() {
    $('i').remove('.fa');
    // 设置不可用的属性
    var img = '<img src="../static/lib/bdcui/images/lock.png" alt="">';
    $("input[disabled='off']").after(img);
    // 为页面设置焦点
    $('.content-title').click();
}

$(document).ajaxError(
    //所有ajax请求异常的统一处理函数，处理无登陆权限
    function (event, xhr, options, exc) {
        if (!isNullOrEmpty(xhr.responseText) && xhr.responseText.indexOf("登录") > -1
            && xhr.responseText.indexOf("用户名") > -1 && xhr.responseText.indexOf("密码") > -1) {
            window.top.location.reload();
        }
    }
);

function formatThirdPath(url) {
    if (url != null && url != "" && url != undefined) {
        if (url.indexOf("${") > -1) {
            console.log(url.indexOf("promanage.url"));
            url = url.replace("${promanage.url}",getContextPath(true)+"/msurveyplat-promanage");
            url = url.replace("${server.url}",getContextPath(true)+"/msurveyplat-server");
            url = url.replace("${portal.url}",getContextPath(true)+"/portal");
            url = url.replace("${exchange.url}",getContextPath(true)+"/msurveyplat-exchange");
            url = url.replace("${portalol.url}",getContextPath(true)+"/portal-ol");
            url = url.replace("${serviceol.url}",getContextPath(true)+"/msurveyplat-serviceol");
        }
    }
    return url;
}

/**
 * 获取URL地址中的参数
 * @param name 参数名称
 */
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (null != r) return unescape(r[2]);
        return null;
    }
})(jQuery);