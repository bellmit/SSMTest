/**
 * Created by Ypp on 2019/8/19.
 * 标准版中新建任务js
 */

//新建任务内容高度
var carouselHeight = '';
//新建任务默认展示个数
var carouselCount = 15;
//新建任务一行展示个数
var carouselline = 5;
//查询宽度
var taskToolsWidth = "310px";
var taskToolsClickWidth = "95%";

//认领询问 特殊功能
var isConfirmRl = false;
//转发需要根据lcPageEdition判断显示哪个send.html
var lcPageEdition = 'common';
// 判断高级查询是否居中
var dbSearch = 'center';
var ybSearch = '';
var xmSearch = 'center';
var rlSearch = '';

// 认领列表 li 标签
var rlContentLi = "'<li>认领列表</li>'";

layui.use(['jquery', 'table'], function () {
    var $ = layui.jquery,
        table = layui.table;

    $(function () {
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
                    {field: '', title: '流程状态', templet: '#dbstateTpl', width: 90},
                    {field: 'text1', width: 150, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                    {field: 'text2', width: 150, title: '建设单位'},
                    {field: 'text3', width: 210, title: '测绘单位'},
                    {field: 'text4', minWidth: 180, title: '项目地址', hide: true},
                    {field: 'text6', minWidth: 180, title: '项目编号'},
                    {field: 'text10', minWidth: 180, title: '项目名称'},
                    {field: 'text5', width: 90, title: '受理人'},
                    {field: 'text7', title: '流程名称', minWidth: 140},
                    {field: 'text8', title: '节点名称', width: 100},
                    // { field: 'text6', width: 150, title: '审核状态' },
                    {fixed: 'right', title: '流程图', templet: '#dblcTpl', width: 80}
                ]],
                parseData: function (res) { //res 即为原始返回的数据

                    if (res.totalElements > 99) {
                        $('#todoTab .bdc-list-num-tips').html('99+');
                    } else {
                        $('#todoTab .bdc-list-num-tips').html(res.totalElements);
                    }
                    res.content.forEach(function (v) {
                        if (v.startTime) {
                            var newStartTime = new Date(v.startTime);
                            v.newStartTime = newStartTime.toLocaleString();
                        }
                    });
                    return {
                        "code": res.code, //解析接口状态 
                        "msg": res.msg, //解析提示文本
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
                toolbar: toolbar,
                title: '已办任务表格',
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
                    {field: '', title: '流程状态', templet: '#dbstateTpl', width: 90},
                    {field: 'text1', width: 150, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                    {field: 'text2', width: 150, title: '建设单位'},
                    {field: 'text3', width: 210, title: '测绘单位'},
                    {field: 'text4', minWidth: 180, title: '项目地址', hide: true},
                    {field: 'text6', minWidth: 180, title: '项目编号'},
                    {field: 'text10', minWidth: 180, title: '项目名称'},
                    {field: 'text5', width: 90, title: '受理人'},
                    {field: 'text7', title: '流程名称', minWidth: 140},
                    {field: 'text8', title: '节点名称', width: 100},
                    // { field: 'text6', width: 150, title: '审核状态' },
                    {fixed: 'right', title: '流程图', templet: '#dblcTpl', width: 80}
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
                    });
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.msg, //解析提示文本
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
                limits: [10, 30, 50, 70, 90, 110, 130, 150],
                defaultToolbar: ['filter'],
                cols: [[
                    {type: 'checkbox', width: 50, fixed: 'left'},
                    {field: '', title: '流程状态', templet: '#dbstateTpl', width: 90},
                    {field: 'text1', width: 150, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                    {field: 'text2', width: 150, title: '建设单位'},
                    {field: 'text3', width: 210, title: '测绘单位'},
                    {field: 'text4', minWidth: 180, title: '项目地址', hide: true},
                    {field: 'text6', minWidth: 180, title: '项目编号'},
                    {field: 'text10', minWidth: 180, title: '项目名称'},
                    {field: 'text5', width: 90, title: '受理人'},
                    {field: 'text7', title: '流程名称', minWidth: 140},
                    {field: 'text8', title: '节点名称', width: 100},
                    // { field: 'text6', width: 150, title: '审核状态' },
                    {fixed: 'right', title: '流程图', templet: '#dblcTpl', width: 80}
                ]],
                parseData: function (res) { //res 即为原始返回的数据
                    //              	console.log('项目列表data:',res);
                    res.content.forEach(function (v) {
                        if (v.startTime) {
                            var startNewTime = new Date(v.startTime);
                            v.startTime = startNewTime.toLocaleString();
                        }
                        if (v.endTime) {
                            var newEndTime = new Date(v.endTime);
                            v.endTime = newEndTime.toLocaleString();
                        }
                    });
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.msg, //解析提示文本
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
    });

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
});