<template>
    <div class="review-check">
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
import reviewCheck from "../review/review-check"
import { resultsDeliveryLogList,queryProcessInfo } from "../../../service/data-apply"
export default {
    components: {
        reviewCheck
    },
    data() {
        return {
            processCols: [
                {
                    field: "index",
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
                    field: "index",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "jsr",
                    title: "接收人",
                    align: "center"
                },
                {
                    field: "lxdh",
                    title: "联系电话",
                    align: "center"
                },
                {
                    field: "jssj",
                    title: "接收时间",
                    align: "center"
                },
            ],
            applyInfoData: [],
            processData: [],
            sqxxid: "",
            slbh: ""
        }
    },
    mounted() {
        this.xmid = this.$route.query.xmid || "";
        this.slbh = this.$route.query.slbh || "";
        this.resultsDeliveryLogList();
        this.queryProcessInfo();
        if(this.$route.query.reviewType == "view" || this.$route.query.type){
            $(".review-check").css("margin","10px")
            $(".review-check").css("height","calc(100vh - 20px)")
            $(".review-check").css("padding","10px")
            $(".review-check .review-check").css("height","auto")
            $(".review-check .review-check").css("margin","0")
            $(".review-check .review-check").css("padding","0")
        }
    },
    methods: {
        // 交付日志获取
        resultsDeliveryLogList(){
            let params = {
                page: 1,
                size: 10,
                xmid: this.xmid
            }
            resultsDeliveryLogList(params).then(res => {
                res.data.data.forEach((list,index) => {
                    list.index = index + 1;
                })
                this.applyInfoData = res.data.data || [];
            })
        },
        // 进度获取
        queryProcessInfo(){
            let params = {
                xmid: this.xmid,
                slbh: this.slbh
            }
            queryProcessInfo(params).then(res => {
                res.data.dataList.forEach((list,index) => {
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
    .review-check {
        background-color: #fff;
        height: 100vh;
        padding: 0 10px;
    }
</style>