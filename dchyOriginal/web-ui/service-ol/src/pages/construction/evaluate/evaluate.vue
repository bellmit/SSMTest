<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="工程名称 " class="form-list-search" prop="gcmc">
                            <Input type="text" @keydown.enter.native.prevent="getEvaluateList(1,formInline.size)" class="form-search-item" v-model="formInline.gcmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="测绘单位 " class="form-list-search" prop="chdwmc">
                            <Input type="text" @keydown.enter.native.prevent="getEvaluateList(1,formInline.size)" class="form-search-item" v-model="formInline.chdwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="当前状态 " class="form-list-search" prop="pjzt">
                            <Select v-model="formInline.pjzt" class="form-search-item">
                                <Option v-for="item in ztList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='20'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getEvaluateList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table
                :cols="tableCols"
                :data="evaluateList"
                :count="totalNum"
                :page="formInline.page"
                :size="formInline.size"
                :func="getEvaluateList"
                :operation="operationList"
                @view="viewDetail"
                @evaluate="evaluateProject"
                @deleteOpr="deleteOpr"
            ></Table>
        </div>
    </div>
</template>
<script>
import { jsdwEvaludateList, checkStatus } from "../../../service/evaluate"
export default {
    data() {
        return {
            formInline: {
                chdwmc: "",
                gcmc: "",
                pjzt: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            ztList: [
                {
                    value: "",
                    label: "全部"
                },
                {
                    value: "1",
                    label: "已评价"
                },
                {
                    value: "0",
                    label: "未评价"
                }
            ],
            evaluateList: [],
            totalNum: 0,
            operationList: ["view","evaluate"],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "GCMC",
                    title: "工程名称",
                    minWidth: 200,
                    align: "center"
                },
                {
                    field: "CHDWMC",
                    title: "测绘单位",
                    minWidth: 200,
                    align: "center"
                },
                {
                    field: "PJZT",
                    title: "状态",
                    minWidth: 200,
                    align: "center",
                    templet: function(d){
                        if(d.PJZT == "1"){
                            return "已评价"
                        } else {
                            return "未评价"
                        }
                    }
                },
                {
                    field: "FWPJ",
                    title: "满意度",
                    width: 120,
                    align: "center",
                    templet: function(d){
                        if(d.FWPJ == "1"){
                            return '差'
                        }else if(d.FWPJ == "2"){
                            return '较差'
                        }else if(d.FWPJ == "3"){
                            return '一般'
                        }else if(d.FWPJ == "4"){
                            return '满意'
                        }else if(d.FWPJ == "5"){
                            return '非常满意'
                        } else{
                            return ""
                        }
                    }
                },
                {
                    title: "操作",
                    minWidth: 200,
                    align: "center",
                    toolbar: "#operation",
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/construction/evaluate/add")){
            this.pageInfo["conEvaluatePageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["conEvaluatePageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["conEvaluatePageInfo"]){
            this.formInline = {...this.pageInfo["conEvaluatePageInfo"]}
        }
    },
    mounted() {
        this.getEvaluateList();
    },
    methods: {
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 获取评论列表
        getEvaluateList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...")
            jsdwEvaludateList(this.formInline).then(res => {
                this.$loading.close();
                this.evaluateList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        // 删除操作
        deleteOpr(){
            this.evaluateList.forEach((list,index) => {
                if(list.PJR || list.PJSJ){
                    $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='evaluate']").remove();
                } else {
                    $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='view']").remove();
                }
            })
        },
        // 查看详情
        viewDetail(item){
            this.$router.push({path: "/construction/evaluate/add", query: {chxmid: item.CHXMID,chdwxxid:item.CHDWXXID, type: "view"}})
        },
        // 评价
        evaluateProject(item){
            let params = {
                chxmid: item.CHXMID
            }
            checkStatus(params).then(res=> {
                this.$router.push({path: "/construction/evaluate/add", query: {chxmid: item.CHXMID,chdwxxid:item.CHDWXXID, gcmc: item.GCMC,chdwmc: item.CHDWMC, type: "add"}})  
            })
        }
    },
}
</script>