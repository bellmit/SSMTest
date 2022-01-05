<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="114" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="工程名称" class="form-list-search" prop="gcmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getProjectList(1,formInline.pageSize)" v-model="formInline.gcmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="当前状态" class="form-list-search" prop="xmzt">
                            <Select v-model="formInline.xmzt" class="form-search-item">
                                <Option v-for="item in ztList" :value="item.DM" :key="item.DM">{{ item.MC }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='20'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getProjectList(1,formInline.pageSize)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table 
                :cols='tableCols' 
                :data="projectList" 
                :count="totalNum" 
                :page="formInline.page" 
                :size="formInline.pageSize" 
                :operation="operationList"
                :func="getProjectList"
                @view="viewDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import { chdwQueryProject } from "../../../service/myproject"
import { getDictInfo } from "../../../service/mlk"
export default {
    data() {
        return {
            formInline: {
                gcmc: "",
                xmzt: "",
                page: 1,
                pageSize: 10
            },
            ruleInline: {},
            ztList: [],
            projectList: [],
            totalNum: 0,
            operationList: ["view"],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "XQFBBH",
                    title: "需求发布编号",
                    align: "center"
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
                    field: "CGFS",
                    title: "采购方式",
                    align: "center"
                },
                {
                    field: "CLSX",
                    title: "测绘事项",
                    align: "center"
                },
                {
                    field: "FBSJ",
                    title: "发布时间",
                    align: "center",
                },
                {
                    field: "XMZT",
                    title: "当前状态",
                    align: "center",
                    templet: function(d){
                        let className= d.XMZT==="已办结" ? "color-finish" :d.XMZT==="已备案"?"color-processing": "color-unfinish"
                        return "<span class='"+className+"'>"+d.XMZT+"</span>"
                    }
                },
                {
                    title: "操作",
                    align: "center",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/survey/project/view")&&to.query.type!="add"){
            this.pageInfo["projectPageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["projectPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["projectPageInfo"]){
            this.formInline = {...this.pageInfo["projectPageInfo"]}
        }
    },
    mounted() {
        this.getProjectList();
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["XMZT"]
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
                pageSize: this.formInline.pageSize
            }
        },
        getProjectList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.pageSize = size;
            }
            this.$loading.show("加载中...")
            chdwQueryProject(this.formInline).then(res => {
                this.$loading.close();
                this.projectList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        viewDetail(data){
            this.$router.push({path: "/survey/project/view", query: {gcbh: data.GCBH,type:"view",chxmid: data.CHXMID}})
        }
    },
}
</script>
<style scoped>
    
</style>