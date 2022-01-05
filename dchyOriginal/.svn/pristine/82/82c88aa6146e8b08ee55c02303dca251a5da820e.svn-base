<template>
    <div>
        <!-- <div class="content-title">我的待办 <span class="font-color-tip">（{{totalNum}}）</span></div> -->
        <Tabs value="name1" class="tab-review">
            <TabPane :label="labelName" name="name1"></TabPane>
        </Tabs>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline >
                <Row>
                    <i-col span="6">
                        <FormItem label="测绘单位" class="form-list-search" prop="dwmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getReviewList(1,formInline.size)" v-model="formInline.dwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="10">
                        <FormItem label="申请时间" class="form-list-search" prop="sqsj">
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
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getReviewList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table
                :cols="tableCols"
                :data="reviewList"
                :count="totalNum"
                :page="formInline.page"
                :size="formInline.size"
                :func="getReviewList"
                :operation="operationList"
                @check="checkDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import { reviewQuery } from "../../../service/review"
import { isCurrentUser } from "../../../service/review"
import moment from "moment";
export default {
    data() {
        const _self = this;
        return {
            ksOptions: {},
            jsOptions: {},
            form:{
                page: 1,
                size: 10
            },
            labelName: (h) => {
                return h('div', [
                    h('span', '我的待办'),
                    h('Badge', {
                        props: {
                            count: _self.totalNum
                        }
                    })
                ])
            },
            formInline: {
                dwmc: "",
                // sqsj: "",
                kssj: "",
                jssj: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            reviewList: [],
            totalNum: 0,
            mlkid: "",
            dbrwid: "",
            operationList: ["check"],
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
                    field: "SQZT",
                    title: '状态',
                    align: "center",
                    templet: function(d){
                        let className= d.SQZT==="待审核" ? "" : "color-warn"
                        let SQZT = d.SQZT || ""
                        return "<span class='"+className+"'>"+SQZT+"</span>"
                    }
                },
                {
                    title: '操作',
                    align: "center",
                    toolbar: '#operation'
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/review/review")){
            this.pageInfo["checkPageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["checkPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["checkPageInfo"]){
            this.formInline = {...this.pageInfo["checkPageInfo"]}
        }
    },
    mounted() {
        this.getReviewList();
    },
    methods: {
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 查询
        getReviewList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.formInline.kssj = this.formInline.kssj ? moment(this.formInline.kssj).format("YYYY-MM-DD") : "";
            this.formInline.jssj = this.formInline.jssj ? moment(this.formInline.jssj).format("YYYY-MM-DD") : "";
            this.$loading.show("加载中...");
            reviewQuery(this.formInline).then(res => {
                this.$loading.close();
                this.reviewList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        checkDetail(data){
            this.form.dbrwid = data.DBRWID
            
            isCurrentUser(this.form).then(res => {
                let current = res.data.dataList && res.data.dataList.length ? res.data.dataList[0] : {};
                if(current.isEnter == "true"){
                    sessionStorage.setItem("mlkid",data.MLKID)
                    this.$router.push({path: "/admin/review/review", query: {mlkid: data.MLKID, dbrwid: data.DBRWID}})
                }else{
                    layer.msg("用户验证失败")
                }
            })
            
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
<style scoped>
    .content-title {
        margin-bottom: 20px;
    }
    .tab-review >>> .ivu-tabs-ink-bar{
        width: 110px!important;
    }
    .tab-review >>> .ivu-tabs-tab > div span  {
        margin-right: 10px;
    } 
    .tab-review >>> .ivu-tabs-tab > div .ivu-badge-count {
        background-color: #f24b43;
    }
</style>