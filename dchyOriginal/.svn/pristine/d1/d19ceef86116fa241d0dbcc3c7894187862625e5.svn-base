<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="测绘单位 " class="form-list-search" prop="dwmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getMlkYbList(1,formInline.size)" v-model="formInline.dwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="10">
                        <FormItem label="申请时间" class="form-list-search" prop="sqsj">
                            <Row class="form-list-search">
                                <i-col span="11">
                                    <DatePicker class="form-search-item"  @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="formInline.kssqsj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                                <i-col span="1">
                                    <div style="text-align:center">-</div>
                                </i-col>
                                <i-col span="12">
                                    <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="formInline.jssqsj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                            </Row>
                        </FormItem> 
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='20'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getMlkYbList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm(1,formInline.size)">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table
                :cols="tableCols"
                :data="chdwList"
                :count="totalNum"
                :page="formInline.page"
                :size="formInline.size"
                :func="getMlkYbList"
                :operation="operationList"
                @view="viewDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import { getMlkYbList } from "../../../service/review"
import { getDictInfo } from "../../../service/mlk"
import moment from "moment";
export default {
    data() {
        return {
            formInline: {
                dwmc: "",
                dwxz: "",
                zzdj: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            dwxzList: [],
            zzdjList: [],
            chdwList: [],
            totalNum: 0,
            jsOptions: {},
            ksOptions: {},
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
                    field: "FRDB",
                    title: '法人代表',
                    align: "center",
                },
                {
                    field: "CHZZZSBH",
                    title: '测绘资质证书编号',
                    align: "center",
                },
                {
                    field: "TYSHXYDM",
                    title: '统一社会信用代码',
                    align: "center",
                },
                {
                    field: "DWXZMC",
                    title: '单位性质',
                    align: "center",
                    width: 150
                },
                {
                    field: "ZZDJMC",
                    title: '资质等级',
                    align: "center",
                    width: 150
                },
                {
                    field: "ZRSJ",
                    title: '申请时间',
                    align: "center"
                },
                {
                    field: "JSSJ",
                    title: '审核时间',
                    align: "center"
                },
                {
                    field: "SHJG",
                    title: '审核结果',
                    align: "center"
                },
                {
                    title: '操作',
                    align: "center",
                    minWidth: 180,
                    toolbar: '#operation',
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/mlk/view")){
            this.pageInfo["chdwListInfo"] = {...this.formInline}
        } else {
            this.pageInfo["chdwListInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["chdwListInfo"]){
            this.formInline = {...this.pageInfo["chdwListInfo"]}
        }
    },
    mounted() {
        this.getMlkYbList();
        this.getDictInfo();
    },
    methods: {
        // 获取字典项
        getDictInfo(){
            var params = {
                zdlx: ["DWXZ","ZZDJ"]
            }
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(r => {
                    if(r.ZDLX == "DWXZ"){
                        this.dwxzList.push(r)
                    }else if(r.ZDLX == "ZZDJ"){
                        this.zzdjList.push(r) 
                    }
                })
                this.dwxzList.unshift({
                    DM: "",
                    MC: "全部"
                })
                this.zzdjList.unshift({
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
        getMlkYbList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...");
            this.formInline.kssqsj = this.formInline.kssqsj ? moment(this.formInline.kssqsj).format("YYYY-MM-DD") : "";
            this.formInline.jssqsj = this.formInline.jssqsj ? moment(this.formInline.jssqsj).format("YYYY-MM-DD") : "";
            getMlkYbList(this.formInline).then(res => {
                this.$loading.close();
                this.chdwList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        viewDetail(data){
            this.$router.push({path: "/admin/mlk/view", query: {mlkid: data.MLKID, ybrwid: data.YBRWID, sqid: data.SQXXID, from: this.$route.path}})
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
        }
    },
}
</script>
<style scoped>
    .modal-base .form-edit {
        padding: 0!important;
    }
    .form-edit >>> .ivu-item-form {
        margin-bottom: 20px!important;
    }
</style>