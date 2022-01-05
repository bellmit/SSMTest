<template>
    <div>
        <div class="form-title">
            <div class="list-title">非名录库机构信息</div>
            <div>
                <Button class="btn-cancel" @click="bank">返回</Button>
            </div>
        </div>
        <agencyInfo
            :agencyInfoData="agencyInfoData"
        ></agencyInfo>
    </div>
</template>
<script>
import agencyInfo from "../../../components/admin/agency-info"
import { queryUnmlkByPage } from "../../../service/mlk"
export default {
    components: {
        agencyInfo
    },
    data() {
        return {
           agencyInfoData: {},
           formInline: {
                chdwmc: "",
                chdwid: "",
                page: 1,
                pageSize: 10
            },
        }
    },
    mounted() {
        if(this.$route.query.chdwid){
            this.formInline.chdwid = this.$route.query.chdwid
        }
        if(this.$route.query.chdwmc){
            this.formInline.chdwmc = this.$route.query.chdwmc
        }
        this.getUnMlkList();
    },
    methods: {
        // 获取基本信息
        getUnMlkList(){
            this.$loading.show("加载中...")
            queryUnmlkByPage(this.formInline).then(res => {
                this.$loading.close();
                let info = {...this.agencyInfoData}
                info = res.data.dataList.content[0];
                for(let key in info){
                    info[_.toLower(key)] = info[key];
                    delete info[key]
                }
                info.bgdz = (  info.bgdzs + info.bgdzss + info.bgdzqx + info.bgdzxx) || "";
                this.agencyInfoData = {...info}
                
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
</style>