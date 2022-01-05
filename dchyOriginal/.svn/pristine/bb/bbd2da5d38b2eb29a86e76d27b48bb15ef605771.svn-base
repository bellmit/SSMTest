<template>
    <div>
        <Tabs value="name1">
            <TabPane label="变更记录" name="name1"></TabPane>
            <Button type="primary" class="bth-h-34 btn-cancel" slot="extra" @click="goBack()">返回</Button>
        </Tabs>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100" inline>
                <FormItem label="修改时间" prop="xgsj">
                    <DatePicker type="date" v-model="formInline.czkssj" class="list-form-width"></DatePicker>
                    <span>-</span>
                    <DatePicker type="date" v-model="formInline.czjssj" class="list-form-width"></DatePicker>
                </FormItem>
                <FormItem :label-width='0'>
                    <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getRecordList(1,formInline.size)">查询</Button>
                    <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                </FormItem>
            </Form>
        </div>
        <div>
            <Table 
                :cols='tableCols' 
                :data="recordList" 
                :count="totalNum" 
                :page="formInline.page" 
                :size="formInline.size" 
                :func="getRecordList"
            ></Table>
        </div>
    </div>
</template>
<script>
import moment from "moment"
import { syncHtRecord } from "../../../service/commission"
export default {
    data() {
        return {
            formInline: {
                czkssj: "",
                czjssj: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            recordList: [],
            totalNum: 0,
            tableCols: [
                {
                    type: "numbers",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "DWMC",
                    title: "变更单位",
                    align: "center"
                },
                {
                    field: "CZR",
                    title: "变更人员",
                    align: "center"
                },
                {
                    field: "CZSJ",
                    title: "变更时间",
                    align: "center"
                }
            ],
            chxmid: ""
        }
    },
    mounted() {
        this.chxmid = this.$route.query.chxmid || "";
        this.getRecordList(); 
    },
    methods: {
        goBack(){
            this.$router.go(-1);
        },
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 获取编辑记录list
        getRecordList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.formInline.chxmid = this.chxmid;
            this.formInline.czkssj = this.formInline.czkssj ? moment(this.formInline.czkssj).format("YYYY-MM-DD") : ""
            this.formInline.czjssj = this.formInline.czjssj ? moment(this.formInline.czjssj).format("YYYY-MM-DD") : ""
            this.$loading.show("加载中...")
            syncHtRecord(this.formInline).then(res => {
                this.$loading.close();
                this.recordList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        }
    },
}
</script>
<style scoped>
    
</style>