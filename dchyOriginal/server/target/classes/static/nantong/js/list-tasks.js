/**
 * Created by Ypp on 2019/8/19.
 * 南通 特殊版本 新建任务js
 */

//新建任务内容高度
var carouselHeight = '132px';
//新建任务默认展示个数
var carouselCount = 15;
//新建任务一行展示个数
var carouselline = 5;
//查询宽度
var taskToolsWidth = "230px";
var taskToolsClickWidth = "90%";

//认领询问 特殊功能
var isConfirmRl = true;
//转发需要根据lcPageEdition判断显示哪个send.html
var lcPageEdition = 'nt';
// 判断待办高级查询是否居中
var dbSearch = '';
var ybSearch = 'center';
var xmSearch = '';
var rlSearch = '';

// 认领列表 li 标签
var rlContentLi = '<li id="rlTab">认领列表<span class="bdc-rl-num-tips bdc-rl-num-js"></span></li>';

var layer,$;
layui.use(['jquery', 'layer', 'form', 'laytpl', 'table'], function () {
    var form = layui.form,
        carousel = layui.carousel,
        laytpl = layui.laytpl;
    table = layui.table;
    layer = layui.layer;
    $ = layui.jquery;
    $(function(){
        var $ntTask = $('.bdc-build-task');
        //新建任务三级显示与隐藏
        $ntTask.on('click','.bdc-show-third',function(){
            var $this = $(this);
            if($this.html() == '展开'){
                $this.html('收起');
            }else {
                $this.html('展开');
            }
            $this.siblings('.bdc-third-rw').toggleClass('bdc-hide');
        });
        $ntTask.on('click','.bdc-details-second-title',function(){
            var $this = $(this);
            if($this.siblings('.bdc-show-third').html() == '展开'){
                $this.siblings('.bdc-show-third').html('收起');
            }else {
                $this.siblings('.bdc-show-third').html('展开');
            }
            $this.siblings('.bdc-third-rw').toggleClass('bdc-hide');
        });

    });
    var selectTaskObj = {
        "dbSearch": "#selectedTaskName",
        "ybSearch": "#selectedDoneTaskName",
        "xmSearch": "#selectedXmTaskName",
        "grSearch": "#selectedGrTaskName",
        "rlSearch": "#selectedRlTaskName"
    };
    // 联动对应处理
    form.on('select(selectedDefName)', function(data){
        if(data.value==undefined||data.value==null){
            return;
        }
        var id=selectTaskObj[data.elem.className];
        console.log(data);
        $.ajax({
            url: getContextPath()  + '/rest/v1.0/task/jdmcs?processDefKey=' + data.value,
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data){
                    $(id).empty();
                    $(id).append(new Option("请选择", ""));
                    $.each(data, function (index, item) {
                        $(id).append(new Option(item.name, item.name));// 下拉菜单里添加元素
                    });
                    layui.form.render("select");
                }
            }
        });
    });

    //渲染待办表格
    window.renderWaitTable = function (url, tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            url: url,
            toolbar: toolbar,
            title: '待办任务表格',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10, 30, 50, 70, 90, 110, 130, 150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: '', title: '流程状态', templet: '#stateTpl', minWidth: 90},
                {field: 'text1', minWidth: 110, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                {field: 'text3', minWidth: 100, title: '权利人'},
                {field: 'text5', minWidth: 100, title: '义务人'},
                {field: 'text4', minWidth: 200, title: '坐落'},
                {field: 'procStartUserName', minWidth: 100, title: '受理人'},
                {field: 'startUserDepName', minWidth: 100, title: '部门名称'},
                {field: 'processDefName', templet: '#lcmcTpl', title: '流程名称', minWidth: 160},
                {field: 'cnlzrq', minWidth: 150, title: '承诺领证日期'},
                {field: 'text7', minWidth: 200, title: '登记原因'},
                {field: 'taskName', title: '节点名称', width: 90},
                {field: 'newStartTime', title: '开始时间', width: 100, sort: true},
                {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
                {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
                {field: 'category', title: '业务类型', width: 90, hide: true},
                {field: 'claimStatusName', title: '认领状态', width: 90, hide: true},
                {fixed: 'right', title: '流程图', templet: '#lcTpl', minWidth: 75}
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                console.log(res);
                if (res.totalElements > 99) {
                    $('.bdc-list-num-tips').html('99+');
                } else {
                    $('.bdc-list-num-tips').html(res.totalElements);
                }
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var newStartTime = new Date(v.startTime);
                        v.newStartTime = newStartTime.toLocaleString();
                    }
                    if (v.date1) {
                        var cnlzrq = new Date(v.date1);
                        v.cnlzrq = formatNYR(cnlzrq);
                    }
                });
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');

                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    };

    //渲染已办表格
    window.renderDoneTable = function (tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            data: [],
            toolbar: toolbar,
            title: '已办任务表格',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10,30,50,70,90,110,130,150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: '', title: '流程状态', templet: '#stateTpl', minWidth: 90},
                {field: 'text1', minWidth: 110, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                {field: 'text3', minWidth: 100, title: '权利人'},
                {field: 'text5', minWidth: 100, title: '义务人'},
                {field: 'text4', minWidth: 200, title: '坐落'},
                {field: 'startUserDepName', minWidth: 100, title: '部门名称'},
                {field: 'processDefName', title: '流程名称', minWidth: 160},
                {field: 'cnlzrq', minWidth: 150, title: '承诺领证日期'},
                {field: 'text7', minWidth: 200, title: '登记原因'},
                {field: 'taskName', title: '节点名称', width: 90},
                {field: 'newStartTime', title: '开始时间', width: 100, sort: true},
                {field: 'newEndTime', title: '结束时间', width: 100, sort: true},
                {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
                {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
                {field: 'category', title: '业务类型', width: 90, hide: true},
                {field: 'taskAssName', title: '处理人', width: 90, hide: true},
                {fixed: 'right', title: '流程图', templet: '#lcTpl', minWidth: 75}
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var newStartTime = new Date(v.startTime);
                        v.newStartTime = newStartTime.toLocaleString();
                    }
                    if (v.endTime) {
                        var newEndTime = new Date(v.endTime);
                        v.newEndTime = newEndTime.toLocaleString();
                    }
                    if (v.date1) {
                        var cnlzrq = new Date(v.date1);
                        v.cnlzrq = formatNYR(cnlzrq);
                    }
                });
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');

                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    };

    //渲染项目列表
    window.renderListTable = function (tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            data: [],
            toolbar: toolbar,
            title: '用户数据表',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10,30,50,70,90,110,130,150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: 'procStatus', title: '流程状态', width: 90, templet: '#flowStateTpl'},
                {field: 'text1', minWidth: 110, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                {field: 'text3', minWidth: 100, title: '权利人'},
                {field: 'text5', minWidth: 100, title: '义务人'},
                {field: 'text4', minWidth: 200, title: '坐落'},
                {field: 'startUserDepName', minWidth: 100, title: '部门名称'},
                {field: 'procDefName', title: '流程名称', minWidth: 160},
                {field: 'cnlzrq', minWidth: 150, title: '承诺领证日期'},
                {field: 'text7', minWidth: 200, title: '登记原因'},
                {field: 'startUserName', title: '受理人', minWidth: 100},
                {field: 'startTime', title: '受理时间', width: 100, sort: true},
                {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
                {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
                {field: 'categoryName', title: '业务类型', width: 90, hide: true},
                {fixed: 'right', title: '流程图', templet: '#lcTpl', width: 75},
                {fixed: 'right', field: 'procTimeoutStatus', width: 90, title: '超期状态', templet: '#overStateTpl'}
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                // console.log(res);
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var startNewTime = new Date(v.startTime);
                        v.startTime = startNewTime.toLocaleString();
                    }
                    if (v.endTime) {
                        var newEndTime = new Date(v.endTime);
                        v.endTime = newEndTime.toLocaleString();
                    }
                    if (v.date1) {
                        var cnlzrq = new Date(v.date1);
                        v.cnlzrq = formatNYR(cnlzrq);
                    }
                });
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');

                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    };

    // 渲染认领列表
    window.renderRlTable = function (tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            data: [],
            toolbar: toolbar,
            title: '认领任务表',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10,30,50,70,90,110,130,150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: 'text1', minWidth: 110, title: '受理编号',templet: '#slbhTpl', event: 'openpage'},
                {field: 'text3', minWidth: 100, title: '权利人'},
                {field: 'text5', minWidth: 100, title: '义务人'},
                {field: 'text4', minWidth: 200, title: '坐落'},
                {field: 'startUserDepName', minWidth: 100, title: '部门名称'},
                {title: '流程名称', field: 'processDefName', minWidth: 160},
                {field: 'cnlzrq', minWidth: 150, title: '承诺领证日期'},
                {field: 'text7', minWidth: 200, title: '登记原因'},
                {field: 'procStartUserName', title: '受理人', minWidth: 100},
                {field: 'taskName', title: '节点名称', width: 90},
                {field: 'newStartTime', title: '开始时间', width: 100, sort: true},
                {field: 'newEndTime', title: '结束时间', width: 100, sort: true},
                {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
                {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
                {field: 'category', title: '业务类型', width: 90, hide: true},
                {field: 'taskAssName', title: '处理人', width: 90, hide: true},
                {fixed: 'right', title: '流程图', templet: '#lcTpl', minWidth: 75}
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var newStartTime = new Date(v.startTime);
                        v.newStartTime = newStartTime.toLocaleString();
                    }
                    if (v.endTime) {
                        var newEndTime = new Date(v.endTime);
                        v.newEndTime = newEndTime.toLocaleString();
                    }
                    if (v.date1) {
                        var cnlzrq = new Date(v.date1);
                        v.cnlzrq = formatNYR(cnlzrq);
                    }
                });
                //获取互联网+的处理
                if($('.bdc-rl-num-word').length>0 && $('.bdc-rl-num-word').html().indexOf("互联网")!=-1){
                    var rlUrl = getContextPath() + "/rest/v1.0/task/claim/list";
                    $.ajax({
                        type: "POST",
                        url: rlUrl,
                        data: {sply: "3"},
                        success: function (data) {
                            if(data && data.hasOwnProperty("totalElements")){
                                $('.bdc-rl-num-js').html(data.totalElements)
                            }
                        }
                    });
                }else{
                    if (res.totalElements > 99) {
                        $('.bdc-rl-num-js').html('99+');
                    } else {
                        $('.bdc-rl-num-js').html(res.totalElements);
                    }
                }
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');

                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    };

    //表格高度自适应
    function changeTableHeight() {
        if ($('.bdc-list-tab .layui-tab-content .layui-show .layui-table-main>.layui-table').height() == 0) {
            $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body .layui-none').html('<img src="../static/lib/bdcui/images/table-none.png" alt="">无数据');
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body').height('56px');
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-fixed .layui-table-body').height('56px');
        } else {
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-main.layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height());
            $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height());
            $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-fixed .layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height() - 17);
        }
    }

    /**
     * 去除时分秒
     * @param timestamp
     * @returns {string}
     */
    function formatNYR(timestamp) {
        if (!timestamp) {
            return '';
        }

        var time = new Date(timestamp);
        var y = time.getFullYear();
        var m = time.getMonth() + 1;
        var d = time.getDate();
        return y + '-' + add0(m) + '-' + add0(d) ;
    }

});
