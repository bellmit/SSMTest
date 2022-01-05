<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="测绘单位 " class="form-list-search" prop="dwmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getPublishList(1,formInline.size)" v-model="formInline.dwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="10">
                        <FormItem label="办理时间 " class="form-list-search" prop="sqsj">
                            <!-- <DatePicker type="date"  class="list-form-width" v-model="formInline.sqsj" placeholder=""></DatePicker> -->
                            <Row class="form-list-search">
                                <i-col span="11">
                                    <DatePicker class="form-search-item"  @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="formInline.kssj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                                <i-col span="1">
                                    <div style="text-align:center">-</div>
                                </i-col>
                                <i-col span="12">
                                    <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="formInline.jssj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                            </Row>
                        </FormItem> 
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='20'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getPublishList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm(1,formInline.size)">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table
                :cols="tableCols"
                :data="publishList"
                :count="totalNum"
                :page="formInline.page"
                :size="formInline.size"
                :func="getPublishList"
                :operation="operationList"
                @view="viewDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import { completedQuery } from "../../../service/completed"
import moment from "moment";
export default {
    data() {
        return {
             form:{
                page: 1,
                size: 10
            },
            ksOptions: {},
            jsOptions: {},
            formInline: {
                dwmc: "",
                sqsj: "",
                kssj: "",
                jssj: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            publishList: [],
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
                    title: '测绘单位',
                    align: "center",
                    field: 'DWMC'
                },
                {
                    field: "LXR",
                    title: '联系人',
                    align: "center",
                },
                {
                    field: "LXDH",
                    title: '联系电话',
                    align: "center",
                },
                {
                    field: "BLSX",
                    title: '办理事项',
                    align: "center",
                },
                {
                    field: "SQSJ",
                    title: '申请时间',
                    align: "center"
                },
                 {
                    field: "JRSJ",
                    title: '办理时间',
                    align: "center"
                },
                {
                    field: "SQZT",
                    title: '状态',
                    align: "center",
                    templet: function(d){
                        let className= d.SQZT==="通过" ? "color-finish" : "color-unfinish"
                        return "<span class='"+className+"'>"+d.SQZT+"</span>"
                    }
                },
                {
                    title: '操作',
                    align: "center",
                    toolbar: '#operation',
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/completed/view")){
            this.pageInfo["completedPageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["completedPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["completedPageInfo"]){
            this.formInline = {...this.pageInfo["completedPageInfo"]}
        }
    },
    mounted() {
        this.getPublishList();
    },
    methods: {
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        getPublishList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.formInline.kssj = this.formInline.kssj ? moment(this.formInline.kssj).format("YYYY-MM-DD") : "";
            this.formInline.jssj = this.formInline.jssj ? moment(this.formInline.jssj).format("YYYY-MM-DD") : "";
            this.$loading.show("加载中...");
            completedQuery(this.formInline).then(res => {
                this.$loading.close();
                this.publishList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        viewDetail(data){
            // const { href } = this.$router.resolve({
            //     path: `/admin/completed/view`
                
            // });
            // window.open(href)
            sessionStorage.setItem("mlkid",data.MLKID)
            this.$router.push({path: "/admin/completed/view", query: {mlkid: data.MLKID, ybrwid: data.YBRWID, sqid: data.SQXXID, from: this.$route.path}})
        },
        // 控制结束时间可选状态
        kssjChange(select){
            this.jsOptions.disabledDate = (date) => {
                return date && (moment(date).unix() < moment(select).unix());
            }
        },
        // 控制开始时间可选状态
        jssjChange(select){
            this.ksOptions.disabledDate = (date) => {
                return date && (moment(date).unix() > moment(select).unix());
            }
        },
    },
}
</script>