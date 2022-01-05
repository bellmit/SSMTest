<template>
    <div style="width: 100%">
       <projectInfoView 
            :readonly="true" 
            :ssmkid="ssmkid"
            :bdid="bdid"
            :projectInfo="projectInfoData"
            :glxqfbbhList="glxqfbbhList"
            :clsxList="clsxList"
            ref="project-info"
        ></projectInfoView> 
        <div class="line-dashed margin-bottom-20"></div>
        <projectCompony 
            ref="project-company" 
            :readonly="true" 
            :projectInfoData="projectInfoData"
            :queryChdw="false"
            :allChdwList="chdwList"
        ></projectCompony>
    </div>
</template>
<script>
import projectInfoView from "../../../components/manage/project-info-view"
import projectCompony from "../../../components/changzhou/project-company"
import { queryDetail } from "../../../service/review"
import util from "../../../service/util"
export default {
    components: {
        projectInfoView,
        projectCompony
    },
    data() {
        return {
            ssmkid: "2",
            bdid: "2",
            projectInfoData: {},
            glxqfbbhList: [],
            taskid: "",
            clsxList: [],
            chdwList: []
        }
    },
    mounted() {
        this.taskid = util.getSearchParams("taskid") || this.$route.query.taskid
        this.gzlslid = util.getSearchParams("gzlslid") || this.$route.query.gzlslid
        this.xmid = util.getSearchParams("xmid") || this.$route.query.xmid
        this.queryBadjXX();
    },
    methods: {
        // 获取基本信息
        queryBadjXX(){
            this.$loading.show("加载中...")
            queryDetail({taskid: this.taskid,gzlslid: this.gzlslid,xmid: this.xmid}).then(res => {
                this.$loading.close();
                let baxxList = {...res.data.dataList.baxx}
                let projectInfo = {...this.projectInfoData}
                for(let key in baxxList){
                    projectInfo[_.toLower(key)] = baxxList[key]
                }
                this.chdwList = res.data.dataList.chdwList.map(chdw => {
                    return {
                        CHDWID: chdw.MLKID,
                        CHDWMC: chdw.CHDWMC,
                        MLKID: chdw.MLKID
                    }
                })
                let chdwList = res.data.dataList.chdwList.map(chdw => {return chdw.MLKID})
                projectInfo.chdwId = res.data.dataList.chdwList.map(chdw => {return chdw.CHDWMC}).join(",")
                this.clsxList = res.data.dataList.clsxList ? res.data.dataList.clsxList.map(clsx => clsx.CLSX) : []
                this.projectInfoData = {...projectInfo}
                // 级联选择市
                this.$refs["project-info"].selectGcdzs({value: this.projectInfoData.gcdzs})
                // 级联选择区县
                this.$refs["project-info"].selectGcdzss({value: this.projectInfoData.gcdzss})
            })
        }
    },
}
</script>
<style scoped>
    
</style>