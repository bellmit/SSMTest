<template>
    <div style="height: 100%">
        <div class="form-title">
            <div class="list-title">名录库机构信息</div>
            <div><Button class="btn-cancel btn-h-34" @click="back">返回</Button></div>
        </div>
        <agencyInfo
            ref="agencyInfo"
            :agencyInfoData="agencyInfoData"
            :readonly="true"
            :showCyryBtn="false"
        ></agencyInfo>
        <div class="form-title margin-top-20">
            <div class="list-title">从业人员信息</div>
        </div>
        <Table 
            :cols="tableCols" 
            :data="cyryxxList" 
            :page='form.page' 
            :size="form.size" 
            :count="totalNum" 
            :func="querycyrybymlkid"
        >
        </Table>
    </div>
</template>
<script>
import agencyInfo from "../../../components/survey/agency-info"
import { reviewCheck } from "../../../service/review"
import { getCyryList } from "../../../service/mlk"
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
    },
    methods: {
        // 获取从业人员列表
        querycyrybymlkid(page,size){
            if(page){
                this.form.size = size;
                this.form.page = page;
            }
            this.form.mlkid = this.mlkid;
            this.$loading.show("加载中...");
            getCyryList(this.form).then(res => {
                this.$loading.close();
                this.cyryxxList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        //获取基本信息
        querymlkdetails(){
            this.$loading.show("加载中...")
            reviewCheck({mlkid: this.mlkid}).then(res => {
                this.$loading.close();
                let info = res.data.dataList[0];
                for(let key in info){
                    info[_.toLower(key)] = info[key];
                    delete info[key]
                }
                if(info.clsxdms){
                    let clsxList = info.clsxdms.split(";");
                    this.$refs["agencyInfo"].setClsxList(clsxList)
                }
                this.agencyInfoData = info
            })
        },
        back(){
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
    .content {
        padding: 10px;
        margin-top: 10px;
        padding-bottom: 80px;
        min-height: calc(100vh - 80px);
        background-color: #fff;
    }
</style>