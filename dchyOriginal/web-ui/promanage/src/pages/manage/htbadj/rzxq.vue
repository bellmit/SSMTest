<template>
    <div>
        <div class="form-title">
            <div class="list-title">日志详情</div>
            <div>
                <Button class="btn-cancel" @click="back">返回</Button>
            </div>
        </div>
        <div>
            <Table
                ref="tableRef"
                :cols="tableCols"
                :data="rzxqList"
                :count="totalNum"
                :page="recordForm.page"
                :size="recordForm.size"
                :func="queryList"
            ></Table>
        </div>
    </div>
</template>
<script>
import moment from "moment";
import { queryCzrzList } from "../../../service/manage"
export default {
    data() {
        return {
            recordForm: {
                page: 1,
                size: 10
            },
            recordRule: {},
            totalNum: 0,
            rzxqList: [],
            tableCols: [
                {
                    type: "numbers",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "CLSX",
                    title: "测绘事项",
                    align: "center",
                },
                {
                    field: "CZZT",
                    title: "状态",
                    align: "center"
                },
                {
                    field: "CZYY",
                    title: "原因",
                    align: "center"
                },
                {
                    field: "CZSJ",
                    title: "操作时间",
                    align: "center",
                    templet: function(d){
                        return moment(d.CZSJ).format("YYYY-MM-DD HH:mm:ss")
                    }
                },
                {
                    field: "CZR",
                    title: "操作人",
                    align: "center"
                }
            ],
            clsxid: ""
        }
    },
    mounted() {
        if(this.$route.query.clsxid){
            this.clsxid = this.$route.query.clsxid
        }
        this.queryList();
    },
    methods: {
        // 获取日志信息list
        queryList(page,size){
            if(page){
                this.recordForm.page = page;
                this.recordForm.size = size;
            }
            this.recordForm.clsxid = this.clsxid
            this.$loading.show("加载中...")
            queryCzrzList(this.recordForm).then(res => {
                this.$loading.close();
                this.rzxqList = res.data.dataList;
                this.totalNum = res.data.totalNum;
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
</style>