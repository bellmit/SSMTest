<template>
    <div class="main-content">
        <div class="search-form">
            <Form :model="recordForm" :rules="recordRule" inline :label-width="120">
                <Row>
                    <i-col span="6">
                        <FormItem label="流程受理编号" class="form-list-search" v-model="recordForm.slbh">
                            <Input v-model="recordForm.slbh" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="测绘单位" class="form-list-search" v-model="recordForm.jsdw">
                            <Input v-model="recordForm.jsdw" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="测绘单位" class="form-list-search" v-model="recordForm.chdw">
                            <Input v-model="recordForm.chdw" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="50">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryList(1,recordForm.pageSize)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="line-dashed"></div>
        <Table
            ref="tableRef"
            :id="'tableId'"
            :cols="tableCols"
            :data="cgInfoList"
            :count="totalNum"
            :operation="operationList"
            :page="recordForm.page"
            :size="recordForm.pageSize"
            :func="queryList"
            @deleteOpr="deleteOpr"
            @check="checkData"
        ></Table>
    </div>
</template>
<script>
import _ from "lodash";
import { initSubmitSqxx } from "../../service/result-manage"
import { getDictInfo, queryCgDbInfoList, deleteOtherAssignment } from "../../service/manage"
import util from '../../service/util';
export default {
    data() {
        return {
            recordForm: {
                page: 1,
                pageSize: 10
            },
            recordRule: {},
            totalNum: 0,
            operationList: ["check"],
            ztList: [],
            cgInfoList: [],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "BABH",
                    title: "项目编号",
                    align: "center",
                    minWidth: 240
                },
                {
                    title: "流程受理编号",
                    align: "center",
                    field: "text1"
                },
                {
                    field: "GCBH",
                    title: "项目代码",
                    align: "center"
                },
                {
                    field: "GCMC",
                    title: "工程名称",
                    align: "center"
                },
                {
                    field: "text2",
                    title: "建设单位",
                    align: "center"
                },
                {
                    field: "text3",
                    title: "测绘单位",
                    align: "center"
                },
                {
                    field: "RKSJ",
                    title: "提交时间",
                    align: "center",
                    maxWidth: 180
                },
                {
                    field: "XMZTMC",
                    title: "项目状态",
                    align: "center",
                    hide: true,
                    width: 100,
                    templet: function(d){
                        let className= d.XMZTMC==="已办结" ? "color-finish": d.XMZTMC ==="已受理" ? "color-processing": "color-unfinish"
                        return "<span class='"+className+"'>"+d.XMZTMC+"</span>"
                    }
                },
                {
                    field: "CGTJZTMC",
                    title: "成果状态",
                    align: "center",
                    width: 140,
                    templet: function(d){
                        let className= d.CGTJZTMC==="已入库" ? "color-finish": d.CGTJZTMC ==="审核中" ? "color-processing": "color-unfinish"
                        return "<span class='"+className+"'>"+ ( d.CGTJZTMC || '/')+"</span>"
                    }
                },
                {
                    title: "操作",
                    align: "center",
                    minWidth: 180,
                    toolbar: "#operation"
                }
            ],
            wdid: "",
            resourceid: "",
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/manage/chsldj/view")){
            this.pageInfo["chsldjPageInfo"] = {...this.recordForm}
        } else {
            this.pageInfo["chsldjPageInfo"] = null
        }
        next()
    },
    created() {
        this.resourceid = util.getSearchParams("resourceid") || this.$route.query.resourceid;
        if(this.pageInfo["chsldjPageInfo"]){
            this.recordForm = {...this.pageInfo["chsldjPageInfo"]}
        }
    },
    mounted() {
        this.getDictInfo();
        this.queryList();
        this.initSubmitSqxx();
    },
    methods: {
        // 初始化成果提交申请
        initSubmitSqxx(){
            initSubmitSqxx().then(result => {
                this.wdid = result.data.sqxxid;
                sessionStorage.setItem("wdid",this.wdid)
            })
        },
        // 成果浏览
        viewCgFj(data){
            const { href } = this.$router.resolve({
                path: "/review/fj",
                query: {chgcid: data.CHGCID, chxmid:data.CHXMID}
            });
            window.open(href);
        },
        deleteOpr(){
            this.cgInfoList.forEach((list,index) => {
                if(list.CGTJZTMC != '已退回' && list.CGTJZTMC != '待审核' && list.CGTJZTMC != '审核中'){
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='check']").addClass("table-btn-disabled cursor-not-allowed");
                }
            })
        },
        // 审核
        checkData(data){
            if(data.CGTJZTMC != '已退回' && data.CGTJZTMC != '待审核' && data.CGTJZTMC != '审核中'){
                return
            }
            this.$loading.show("加载中...")
			let userid = localStorage.getItem("userId") || ""
            deleteOtherAssignment({taskid: data.taskId,userid}).then(res => {
                this.$loading.close();
                let href = `/portal/view/service-page.html?taskid=${data.taskId}&chxmid=${data.CHXMID}&reviewType=verify&xmid=${data.text9}&gzlslid=${data.procInsId}&formKey=&wdid=${this.wdid}&type=db&ywlx=FCCH`
                window.open(href)
            })
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["XMCGZT"]
            }
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(list => {
                    if(list.ZDLX == "XMCGZT"){
                        this.ztList.push(list)
                    }
                })
                this.ztList.unshift({
                    DM: "",
                    MC: "全部"
                })
            })
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
                this.recordForm.pageSize = size;
            }
            this.$loading.show("加载中...")
            queryCgDbInfoList(this.recordForm).then(res => {
                this.$loading.close();
                res.data.content&&res.data.content.forEach((c,index) => {
					c.ROWNUM_ = (index + 1) + this.recordForm.pageSize*(this.recordForm.page - 1);
				})
                this.cgInfoList = res.data.content || [];
                this.totalNum = res.data.totalElements || 0;
            }) 
        }
    },
}
</script>
<style scoped>
@import "./chsldj.less";
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