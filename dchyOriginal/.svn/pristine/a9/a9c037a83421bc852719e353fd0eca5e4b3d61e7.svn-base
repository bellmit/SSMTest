<template>
    <div style="height: 100%">
        <Header></Header>
        <div class="bdc-location">
            <img src="static/images/index-home.png" alt="">
            <span>当前位置:&nbsp;</span>
            <span class="layui-breadcrumb" lay-separator=">" lay-filter="location">
            <span @click="toHome()" style="cursor:pointer;">首页</span>
            <span lay-separator>></span>
            <a @click="bank"><cite>名录库查看</cite></a>
            <span lay-separator>></span>
            <a><cite>名录库详情</cite></a>
            </span>
        </div>
        <div class="mlk-container bdc-container content">
            <div class="bdc-content-title margin-bottom-10">
                <span class="bdc-now-name">名录库详情</span>
            </div> 
            <div class="detail-info">
                <div class="form-title margin-top-20">
                    <div class="list-title">基本信息</div>
                </div>
                <agencyInfo
                    ref="agencyInfo"
                    :agencyInfoData="agencyInfoData"
                    :readonly="true"
                ></agencyInfo>
                <div class="line-solid margin-top-20"></div>
                <div class="form-title margin-top-20">
                    <div class="list-title">从业人员信息</div>
                </div>
                <Table 
                    class="table-view"
                    :cols="tableCols" 
                    :data="cyryxxList" 
                    :page='form.page' 
                    :size="form.size" 
                    :count="totalNum" 
                    :func="querycyrybymlkid"
                >
                </Table>
                <div class="line-solid margin-top-20"></div>
                <div class="form-title margin-top-20">
                    <div class="list-title">诚信记录</div>
                </div>
                <Table 
                    :id="'cxRecordTable'"
                    :pageId="'cxRecordPage'"
                    class="table-view"
                    :cols="recordCols" 
                    :data="cxRecordList" 
                    :page='cxForm.page' 
                    :size="cxForm.size" 
                    :count="cxRecordTotal" 
                    :func="queryMlkCxRecord"
                >
                </Table>
            </div>
        </div>
    </div>
</template>
<script>
import agencyInfo from "../../components/mlk/agency-info"
import { querymlkdetails, querycyrybymlkid, queryMlkCxRecord } from "../../service/home"
export default {
    components: {
        agencyInfo
    },
    data() {
        return {
           agencyInfoData: {},
           mlkid: "",
           form: {
                page: 1,
                size: 10
            },
            cxForm: {
                page: 1,
                size: 10
            },
            cxRecordList: [],
            cxRecordTotal: 0,
            recordCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "CXPJ",
                    title: "诚信评价",
                    align: "center"
                },
                {
                    field: "JLSJ",
                    title: "记录时间",
                    align: "center",
                    width: "20%"
                }
            ],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: 'RYXM',
                    title: "姓名",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'ZC',
                    title: "职称",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'ZSMC',
                    title: "证书名称",
                    align: "center",
                    minWidth: 200,
                    width: "20%"
                },
                
            ],
            cyryxxList: [],
            totalNum: 0,
        }
    },
    mounted() {
        if(this.$route.query.mlkid){
            this.mlkid = this.$route.query.mlkid
            sessionStorage.setItem("mlkid",this.mlkid);
        }
        this.querymlkdetails();
        this.querycyrybymlkid();
        this.queryMlkCxRecord();
    },
    methods: {
        queryMlkCxRecord(page,size){
            if(page){
                this.cxForm.size = size;
                this.cxForm.page = page;
            }
            let params = {
                page: this.cxForm.page,
                size: this.cxForm.size,
                mlkid: this.mlkid
            }
            queryMlkCxRecord(params).then(res => {
                this.cxRecordList = res.data.dataList || [];
                this.cxRecordTotal = res.data.totalNum || 0;
            })
        },
        querycyrybymlkid(page,size){
            if(page){
                this.form.size = size;
                this.form.page = page;
            }
            this.form.mlkid = this.mlkid;
            this.$loading.show("加载中...");
            querycyrybymlkid(this.form).then(res => {
                this.$loading.close();
                this.cyryxxList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        toHome(){
            let token = sessionStorage.getItem("access_token") || ""
            this.$router.push({
                path: "/home",
                query: {
                    token
                }
            })
        },
        //获取基本信息
        querymlkdetails(){
            this.$loading.show("加载中...")
            querymlkdetails({mlkid: this.mlkid}).then(res => {
                this.$loading.close();
                this.agencyInfoData = res.data.dataList[0]
                if(this.agencyInfoData.clsxdms){
                    let clsxList = this.agencyInfoData.clsxdms.split(";");
                    this.$refs["agencyInfo"].setClsxList(clsxList)
                }
            })
        },
        bank(){
            this.$router.go(-1)
        }
    },

}
</script>
<style scoped>
    .form-title {
        display: flex;
        justify-content: space-between;
        
    }
    .mlk-container {
        border: 1px solid #d0d5da;
    }
    .detail-info {
        padding: 0 10px;
    }
    .content {
        margin-top: 10px;
        padding-bottom: 80px;
        min-height: calc(100vh - 110px);
        background-color: #fff;
    }
</style>