<template>
    <div class="main-content">
        <div class="search-form">
            <Form :model="recordForm" :rules="recordRule" inline :label-width="100">
                <Row>
                    <i-col span="6">
                        <FormItem label="项目代码" class="form-list-search" v-model="recordForm.gcbh">
                            <Input v-model="recordForm.gcbh" @keydown.enter.native.prevent="queryList(1,recordForm.size)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="工程名称" class="form-list-search" v-model="recordForm.gcmc">
                            <Input v-model="recordForm.gcmc" @keydown.enter.native.prevent="queryList(1,recordForm.size)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="项目编号" class="form-list-search" v-model="recordForm.babh">
                            <Input v-model="recordForm.babh" @keydown.enter.native.prevent="queryList(1,recordForm.size)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="20">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryList(1,recordForm.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="line-dashed"></div>
        <div class="margin-top-10 collapse-table">
            <div v-for="(item,index) in projectList" :key="index" class="margin-top-10">
                <div class="form-title">
                    <div class="list-title">
                        <div class="info-title">
                            <div class="margin-left-10 title-tip" :title="item.GCBH"><span>项目代码：</span>{{item.GCBH}}</div>
                            <div class="margin-left-10 title-tip" :title="item.GCMC"><span>工程名称：</span>{{item.GCMC}}</div>
                            <div class="margin-left-10 title-tip" :title="item.WTDW"><span>建设单位：</span>{{item.WTDW}}</div>
                            <div class="margin-left-10"></div>
                        </div>
                        <div class="collapse-icon"  style="margin-top: -4px">
                            <Icon type="ios-arrow-dropup" class="dropup" title="收起" @click="handlerClick(item,index)" v-show="item.collapse"/>
                            <Icon type="ios-arrow-dropdown" class="dropdown" title="展开" @click="handlerClick(item,index)" v-show="!item.collapse"/>
                        </div>
                    </div>
                </div>
                <div v-show="item.collapse" class="collapse-item margin-bottom-10">
                    <div class="collapse-content">
                        <Table
                            ref="tableRef"
                            :id="'tableId'+index"
                            :cols="tableCols"
                            :data="item.CHILDREN"
                            :operation="operationList"
                            :tool="tool"
                            :showPage="false"
                            @btn1="pushData(item)"
                            @push="push"
                        ></Table>
                    </div>
                </div>
                <div class="line-dashed"></div>
            </div>
        </div>
        <div class="page-item margin-top-10">
            <Page :total="totalNum" :page-size="recordForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :current="recordForm.page" show-total show-elevator show-sizer />
        </div>
    </div>
</template>
<script>
import _ from "lodash";
import { searchProject } from "../../service/manage"
export default {
    data() {
        return {
            recordForm: {
                page: 1,
                pageSize: 10
            },
            recordRule: {},
            totalNum: 0,
            operationList: ["push"],
            projectList: [],
            tool: '<div>' +
                '<span class="layui-btn main-btn-a" lay-event="btn1">推送</span>' +
            '</div>',
            tableCols: [
                {
                    field: "index",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "BABH",
                    title: "项目编号",
                    align: "center",
                },
                {
                    field: "CHJD",
                    title: "测绘阶段",
                    align: "center"
                },
                {
                    field: "CHDWMC",
                    title: "测绘单位",
                    align: "center"
                },
                {
                    field: "CHSX",
                    title: "测绘事项",
                    align: "center",
                    minWidth: 250,
                    templet: function(d){
                        return d.CLSX&&d.CLSX.length ? d.CLSX.join(",") : "";
                    }
                },
                {
                    field: "BASJ",
                    title: "备案时间",
                    align: "center",
                    width: 140
                },
                {
                    title: "操作",
                    align: "center",
                    minWidth: 180,
                    fixed: "right",
                    toolbar: "#operation"
                }
            ],
            wdid: "",
            resourceid: "",
        }
    },
    mounted() {
        this.queryList();
    },
    methods: {
        // 展开收起点击事件
        handlerClick(item,index){
            if(item.collapse){
                $(".collapse-item:eq("+index+")").slideUp(200);
                $(".collapse-icon:eq("+index+") .dropdown").show()
                $(".collapse-icon:eq("+index+") .dropup").hide()
            } else {
                $(".collapse-item:eq("+index+")").slideDown(200);
                $(".collapse-icon:eq("+index+") .dropdown").hide()
                $(".collapse-icon:eq("+index+") .dropup").show()
            }
            item.collapse= !item.collapse
        },
        // 修改页数
        changePage(page){
            this.recordForm.page = page
            this.queryList();
        },
        // 修改每页显示条数
        changePageSize(size){
            this.recordForm.pageSize = size
            this.queryList();
        },
        // 推送操作
        push(data){
            
        },
        // 重置查询项
        resetForm(){
            this.recordForm = {
                page: this.recordForm.page,
                pageSize: this.recordForm.pageSize
            }
        },  
        // 查询
        queryList(page,size){
            if(page){
                this.recordForm.page = page;
                this.recordForm.size = size;
            }
            this.$loading.show("加载中...")
            searchProject(this.recordForm).then(res => {
                this.$loading.close();
                this.projectList = res.data.dataList || [];
                if(this.projectList.length){
                    this.projectList[0].collapse = true;
                }
                this.projectList.forEach(list => {
                    list.CHILDREN&&list.CHILDREN.forEach((l,index) => {
                        l.index = index + 1;
                    })
                })
                this.totalNum = res.data.totalNum || 0;
            }) 
        },
        // 查看操作
        pushData(data){
            
        }
    },
}
</script>
<style scoped>
@import "../cgtj/chsldj.less";
    .collapse-table >>> .table .layui-table-view {
        margin-top: 0;
        border-top: none;
        border-left: none;
        border-right: none;
    }
    .collapse-table >>> .page-table {
        border-left: none;
        border-right: none;
    }
    .collapse-title {
        display: flex;
        justify-content: flex-start;
    }
    .title-tip {
        width: 300px;
        overflow: hidden;
        text-overflow:ellipsis; 
        white-space: nowrap;
    }
    .form-title {
        padding: 5px 0;
    }
</style>