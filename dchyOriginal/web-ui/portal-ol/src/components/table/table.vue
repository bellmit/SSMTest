<template>
    <div class="table">
        <div :id="id" :lay-filter="id"></div>
        <div :id="pageId" class="page-table"></div>
        <input type="file" class="bdc-hide" id="attachmentUploadInput"/>
        <div v-show='false'>
            <div id="operation" >
                <span v-if="operation.includes('view')" class="margin-left-10 table-btn-a" lay-event="view">查看</span>
                <span v-if="operation.includes('edit')" class="margin-left-10 table-btn-a" lay-event="edit">编辑</span>
                <span v-if="operation.includes('download')" class="margin-left-10 table-btn-a" lay-event="download">下载</span>
                <span v-if="operation.includes('upload')" class="margin-left-10 table-btn-a" lay-event="upload">上传</span>
                <span v-if="operation.includes('htdownload')" class="margin-left-10 table-btn-a" lay-event="htdownload">下载合同</span>
                <span v-if="operation.includes('delete')" class="margin-left-10 table-btn-a bdc-delete-btn" lay-event="delete">删除</span>
                <span v-if="operation.includes('evaluate')" class="margin-left-10 table-btn-a" lay-event="evaluate">评价</span>
                <span v-if="operation.includes('check')" class="margin-left-10 table-btn-a" lay-event="check">审核</span>
                <span v-if="operation.includes('examination')" class="margin-left-10 table-btn-a" lay-event="examination">考评</span>
                <span v-if="operation.includes('remove')" class="margin-left-10 table-btn-a" lay-event="remove">移出名录库</span>
                <span v-if="operation.includes('disable')" class="margin-left-10 table-btn-a" lay-event="disable">禁用</span>
                <span v-if="operation.includes('enable')" class="margin-left-10 table-btn-a" lay-event="enable">启用</span>
            </div>
        </div>
    </div>
</template>

<script>
import _ from "lodash";
export default {
    name: "Table",
    props: {
        id: { //如果一个存在多个表格，需要传递id区分
            type: String,
            default: "tableRenderId"
        },
        pageId: { //如果一个存在多个表格且需要分页，需要传递pageId区分
            type: String,
            default: "tablePageId"
        },
        cols: { //表格列
            type: Array,
            default: () => {
                return []
            }
        },
        data: { //表数据
            type: Array,
            default: () => {
                return []
            }
        },
        size: { //每页显示条数
            type: Number,
            default: 10
        },
        showPage: { //是否需要分页
            type: Boolean,
            default: true
        },
        page: { //当前所在页
            type: Number,
            default: 1 
        },
        count: { //数据总数
            type: Number,
            default: 0
        },
        operation: { //数据操作
            type: Array,
            default: () => {
                return []
            }
        },
        func: { //需要分页时，需传递分页后调用的方法，注意：分页时，需要通过参数接收到修改的分页数据
            type: Function,
            default: () => {}
        },
        showDefaultTool: { //是否显示头部操作
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            table: "",
            laypage: "",
            form: ""
        }
    },
    watch: {
        data: {
            deep: true,
            handler: function(newVal,oldVal){
                if(!_.eq(newVal,oldVal)){
                    this.renderTable();
                }
            }
        }
    },
    mounted() {
        //上传事件订阅
        $("#attachmentUploadInput").on("change", function () {
            console.log(this.files)
        });
        if(!this.table){
            this.initTableEle();
        }
    },
    methods: {
        initTableEle(){
            layui.use(["table","laypage","form"], () => {
                this.table = layui.table;
                this.laypage = layui.laypage;
                this.form = layui.form;
                this.rendered();
                this.bindEvent();
            })
        },
        // 绑定表格事件
        bindEvent(){
            // 单选事件
            this.table.on("radio("+this.id+")", (obj) => {
                let selectIndex = obj.tr.attr("data-index");
                obj.data.index = selectIndex;
                this.$emit("radio",obj.data)
            })
            // 多选事件
            this.table.on("checkbox("+this.id+")", (obj) => {
                const { type, checked } = obj
                if(type == "all" && checked){
                    this.$emit("selectall")
                }else if(type == "one" && checked){
                    let selectIndex = obj.tr.attr("data-index");
                    obj.data.index = selectIndex;
                    this.$emit("radio",obj.data)
                }
            })
            // toolbar 头部操作
            this.table.on("toolbar("+this.id+")",(obj) => {
                const { event } = obj;
                if(event == "tool-add"){
                    this.$emit("tooladd")
                }else if(event == "tool-delete"){
                    this.$emit("tooldelete")
                }
            })
            //tool操作列
            this.table.on("tool("+this.id+")",(obj) => {
                const {field,event,data} = obj;
                if(event == "delete"){
                    this.$emit("delete", data)
                }else if(event == "download"){
                    this.$emit("download", data)
                }else if(event == "upload"){
                    this.$emit("upload", data);
                    $("#attachmentUploadInput").click();
                }else if(event == "view"){
                    this.$emit("view", data);
                }else if(event == "edit"){
                    this.$emit("edit", data);
                }else if(event == "htdownload"){
                    this.$emit("htdownload", data)
                }else if(event == "evaluate"){
                    this.$emit("evaluate", data)
                }else if(event == "check"){
                    this.$emit("check", data)
                }else if(event == "examination"){
                    this.$emit("examination", data)
                }else if(event == "remove"){
                    this.$emit("remove", data)
                }else if(event == "disable"){
                    this.$emit("disable", data)
                }else if(event == "enable"){
                    this.$emit("enable", data)
                }
            });
            // 表格中为下拉列表
            this.form.on('select(select)', (data) => {
                var elem = $(data.elem);
                var trElem = elem.parents('tr');
                this.$emit('select',{value: data.value,index: trElem.data('index')})
            })
        },
        // 更新表格数据
        renderTable(){
            if(this.table){
                this.rendered();
            }else{
                this.initTableEle();
            }
        },
        // 更新
        rendered(){
            let _self = this;
            this.table.render({
                elem: "#" + this.id,
                cols: [this.cols],
                limit: this.showPage ? this.size : 100000,
                defaultToolbar: [],
                data: this.data,
                toolbar: this.showDefaultTool?'<div>' +
                            '<span class="layui-btn main-btn-a" lay-event="tool-add">新增</span>' +
                            '<span class="layui-btn main-btn-a bdc-delete-btn margin-left-10" lay-event="tool-delete">删除</span>'+
                        '</div>' : null,
                done: () => {
                    var pageDomeId = this.pageId;
                    if (this.showPage) {
                        this.laypage.render({
                            elem: pageDomeId,
                            count: this.count,
                            curr: this.page,
                            limit: this.size,
                            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    _self.func(obj.curr,obj.limit);
                                }
                            }
                        })
                    }
                }
            })
        }
    },
}
</script>
<style scoped>
    .table >>> .layui-table-body {
        overflow: visible;
    }
    .table >>> .layui-table-box {
        overflow: visible;
    }
    #operation > span {
        display: inline-block;
    }
    .page-table {
        border: 1px solid #d0d5da;
        border-top: none;
        padding-left: 10px;
    }
    .table >>> .layui-table-view {
        margin: 10px 0 0 0;
    }
</style>