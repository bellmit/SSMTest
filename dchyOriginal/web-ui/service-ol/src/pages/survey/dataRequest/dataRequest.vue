<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="114" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="项目编号 " class="form-list-search" prop="xmbabh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getProjectList(1,formInline.size)" v-model="formInline.xmbabh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="项目代码 " class="form-list-search" prop="gcbh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getProjectList(1,formInline.size)" v-model="formInline.gcbh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="工程名称 " class="form-list-search" prop="gcmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getProjectList(1,formInline.size)" v-model="formInline.gcmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="建设单位 " class="form-list-search" prop="jsdw">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getProjectList(1,formInline.size)" v-model="formInline.jsdw" placeholder=""/>
                        </FormItem>
                    </i-col>
                </Row>
                <Row>
                    <i-col span="6">
                        <FormItem label="当前状态 " class="form-list-search" prop="xmzt">
                            <Select v-model="formInline.xmzt" clearable class="form-search-item">
                                <Option v-for="item in ztList" :value="item.DM" :key="item.MC">{{ item.MC }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='50'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getProjectList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <Table
            :cols="tableCols"
            :data="myprojectList"
            :count="totalNum"
            :page="formInline.page"
            :size="formInline.size"
            :func="getProjectList"
            :operation="operationList"
            @view="viewDetail"
        ></Table>
    </div>
</template>
<script>
import { querySqxxList } from "../../../service/myproject"
import { getDictInfo } from "../../../service/mlk"
export default {
    data() {
        return {
            formInline: {
                slbh: "",
                gcmc: "",
                gcbh: "",
                jsdw: "",
                xmzt: "",
                page: 1,
                size: 10
            },
            newFileForm:{},
            count: 0,
            visible: false,
            ruleInline: {},
            ztList: [],
            tableCols: [
                {
                    type:'checkbox',
                    fixed: "left"
                },
                {
                    field: "ROWNUM_",
                    width: 70,
                    align: "center",
                    title: "序号",
                    fixed: "left"
                },
                {
                    field: "XMBABH",
                    align: "center",
                    title: "项目编号"
                },
                {
                    field: "BABH",
                    align: "center",
                    title: "受理编号",
                    hide: true
                },
                {
                    field: "GCBH",
                    align: "center",
                    title: "项目代码"
                },
                {
                    field: "GCMC",
                    align: "center",
                    title: "工程名称"
                },
                {
                    field: "JSDW",
                    align: "center",
                    title: "建设单位"
                },
                {
                    field: "SQFS",
                    align: "center",
                    title: "申请方式"
                },
                {
                    field: "SQSJ",
                    align: "center",
                    title: "申请时间"
                },
                {
                    field: "DQZT",
                    align: "center",
                    title: "当前状态",
                    templet: function(d){
                        let className= d.DQZT==="已办结" ? "color-finish" :d.DQZT==="已备案"?"color-processing": "color-unfinish"
                        return "<span class='"+className+"'>"+d.DQZT+"</span>"
                    }
                },
                {
                    align: "center",
                    title: "操作",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ],
            operationList: ["view"],
            myprojectList: [],
            totalNum: 0
        }
    },
    mounted() {
        this.getProjectList();
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["DQSQZT"]
            }
            getDictInfo(params).then(res => {
                this.ztList = res.data.dataList;
                this.ztList.unshift({
                    DM: "",
                    MC: "全部"
                })
            })
        },
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 查询
        getProjectList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...")
            querySqxxList(this.formInline).then(res => {
                this.$loading.close();
                this.myprojectList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        viewDetail(data){
            this.$router.push({
                path: `/survey/apply/view`,
                query: {slbh: data.BABH, chxmid:data.CHXMID, jcsjsqxxid:data.JCSJSQXXID}
            });
        },
    },
}
</script>
<style scoped>
</style>
