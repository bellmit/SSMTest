<template>
    <div>
        <Tabs value="name1">
            <TabPane label="修改记录" name="name1"></TabPane>
            <Button type="primary" class="bth-h-34 btn-cancel" slot="extra" @click="goBack()">返回</Button>
        </Tabs>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="100" inline>
                <FormItem label="修改时间" prop="xgsj">
                    <DatePicker type="date" v-model="formInline.kssj" class="list-form-width"></DatePicker>
                    <span>-</span>
                    <DatePicker type="date" v-model="formInline.jssj" class="list-form-width"></DatePicker>
                </FormItem>
                <FormItem label="修改人" class="margin-left-20" prop="xgr">
                     <!-- <Input type="text" class="list-form-width" v-model="formInline.xgr" placeholder=""/> -->
                     <Select v-model="formInline.xgr" clearable class="list-form-width">
                         <Option v-for="(item,index) in xgrList" :key="index" :value="item.userId">{{item.userName}}</Option>
                     </Select>
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
                :operation="operationList"
                :func="getRecordList"
                @view="viewDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import moment from "moment"
import { queryEditRecordList, queryUserList } from '../../../service/manage'
export default {
    data() {
        return {
            formInline: {
                kssj: "",
                jssj: "",
                xgr: "",
                page: 1,
                size: 10
            },
            xgrList: [],
            ruleInline: {},
            recordList: [],
            totalNum: 0,
            operationList: ["view"],
            tableCols: [
                {
                    type: "numbers",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "GCBH",
                    title: "项目代码",
                    align: "center"
                },
                {
                    field: "GCMC",
                    title: "工程名称",
                    align: "center"
                },
                {
                    field: "XMDZ",
                    title: "工程地址",
                    align: "center"
                },
                {
                    field: "XGRMC",
                    title: "修改人员",
                    align: "center"
                },
                {
                    field: "XGSJ",
                    title: "修改时间",
                    sort: true,
                    align: "center"
                },
                {
                    title: "操作",
                    align: "center",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ],
            chxmid: ""
        }
    },
    mounted() {
        if(this.$route.query.chxmid){
            this.chxmid = this.$route.query.chxmid
        }
        this.queryUserList();
        this.getRecordList(); 
    },
    methods: {
        // 获取用户list
        queryUserList(){
            queryUserList().then(res => {
                this.xgrList = res.data.dataList;
                this.xgrList.unshift({
                    userId: "",
                    userName: "全部"
                })
            })
        },
        goBack(){
            let from = this.$route.query.from
            this.$router.push(from);
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
            this.formInline.kssj = this.formInline.kssj ? moment(this.formInline.kssj).format("YYYY-MM-DD") : ""
            this.formInline.jssj = this.formInline.jssj ? moment(this.formInline.jssj).format("YYYY-MM-DD") : ""
            this.$loading.show("加载中...")
            queryEditRecordList(this.formInline).then(res => {
                this.$loading.close();
                this.recordList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        // 查看详情
        viewDetail(item){
            this.$router.push({path: "/edit/record/detail",query: {rzid: item.RZID}})
        }
    },
}
</script>
<style scoped>
    
</style>