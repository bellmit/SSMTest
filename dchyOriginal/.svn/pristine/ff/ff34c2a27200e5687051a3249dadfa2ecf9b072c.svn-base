<template>
    <div>
        <Tabs>
            <TabPane label="项目信息" name="applyInfo">
                <reviewCheck></reviewCheck>
            </TabPane>
            <TabPane label="进度查看" name="processInfo">
                <div class="form-title">
                    <div class="list-title">进度信息</div>
                    <div>
                        <Button class="btn-h-34 btn-cancel margin-left-10" @click="handlerCancel">返回</Button>
                    </div>
                </div>
                <div>
                    <Table
                        :id="'processId'"
                        :data="processData"
                        :cols="processCols"
                        :showPage="false"
                    ></Table>
                </div>
                <div class="form-title margin-top-20">
                    <div class="list-title">基础数据交付日志</div>
                </div>
                <div>
                    <Table
                        :id="'applyInfoId'"
                        :data="applyInfoData"
                        :cols="applyInfoCols"
                        :showPage="false"
                    ></Table>
                </div>
            </TabPane>
        </Tabs>
    </div>
</template>
<script>
import reviewCheck from "./review-check"
import { resultsDeliveryLogList,queryProcessInfo } from "../../../service/myproject"
export default {
    components: {
        reviewCheck
    },
    data() {
        return {
            processCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "HJ",
                    title: "环节",
                    align: "center"
                },
                {
                    field: "BLRY",
                    title: "办理人员",
                    align: "center"
                },
                {
                    field: "BLSJ",
                    title: "办理时间",
                    align: "center"
                },
                {
                    field: "BLJG",
                    title: "办理结果",
                    align: "center"
                },
                {
                    field: "YJ",
                    title: "意见",
                    align: "center"
                },
            ],
            applyInfoCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "JSR",
                    title: "接收人",
                    align: "center"
                },
                {
                    field: "LXDH",
                    title: "联系电话",
                    align: "center"
                },
                {
                    field: "JSSJ",
                    title: "接收时间",
                    align: "center"
                },
            ],
            applyInfoData: [],
            processData: [],
            jcsjsqxxid: "",
            slbh: ""
        }
    },
    mounted() {
        this.jcsjsqxxid = this.$route.query.jcsjsqxxid || "";
        this.slbh = this.$route.query.slbh || "";
        this.resultsDeliveryLogList();
        this.queryProcessInfo();
    },
    methods: {
        resultsDeliveryLogList(){
            resultsDeliveryLogList({jcsjsqxxid: this.jcsjsqxxid}).then(res => {
                res.data.data.forEach((list,index) => {
                    list.index = index + 1;
                })
                this.applyInfoData = res.data.data || [];
            })
        },
        queryProcessInfo(){
            let params = {
                jcsjsqxxid: this.jcsjsqxxid,
                slbh: this.slbh
            }
            queryProcessInfo(params).then(res => {
                res.data.dataList&&res.data.dataList.forEach((list,index) => {
                    list.index = index + 1;
                })
                this.processData = res.data.dataList || [];
            })
        },
        handlerCancel(){
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