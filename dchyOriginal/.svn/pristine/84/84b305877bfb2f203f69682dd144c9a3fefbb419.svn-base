<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="委托编号 " class="form-list-search" prop="wtbh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.wtbh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="项目代码 " class="form-list-search" prop="gcxmbh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.gcxmbh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="工程名称 " class="form-list-search" prop="gcxmmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.gcxmmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='50' class="form-list-search">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getEntrustList(1,formInline.pageSize)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <Table
            :cols="tableCols"
            :data="myEntrustList"
            :count="totalNum"
            :page="formInline.page"
            :size="formInline.pageSize"
            :func="getEntrustList"
            :operation="operationList"
            @push="pushData"
        ></Table>
    </div>
</template>
<script>
import { getEntrustByPage } from "../../../service/commission"
export default {
    data() {
        return {
            formInline: {
                gcbh: "",
                gcmc: "",
                wtbh: "",
                chdwmc: "",
                page: 1,
                pageSize: 10
            },
            tableCols: [
                {
                    field: "ROWNUM_",
                    width: 70,
                    align: "center",
                    title: "序号",
                    fixed: "left"
                },
                {
                    field: "XQFBBH",
                    align: "center",
                    title: "委托编号"
                },
                {
                    field: "GCBH",
                    align: "center",
                    title: "项目代码"
                },
                {
                    field: "GCMC",
                    align: "center",
                    title: "工程名称"
                },
                {
                    field: "FBSJ",
                    align: "center",
                    title: "委托时间"
                },
                {
                    align: "center",
                    title: "操作",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ],
            operationList: ["push"],
            myEntrustList: [],
            totalNum: 0
        }
    },
    mounted() {
        this.getEntrustList();
    },  
    methods: {
        // 获取委托列表
        getEntrustList(page,pageSize){
           if(page){
               this.formInline.page = page;
               this.formInline.pageSize = pageSize;
           }
           this.$loading.show("加载中...")
           getEntrustByPage(this.formInline).then(res => {
               this.$loading.close()
               this.totalNum = res.data.totalNum || 0;
               this.myEntrustList = res.data.dataList || [];
           })
        },
        // 推送
        pushData(data){
            
        },
        //重置
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                pageSize: this.formInline.pageSize
            }
        }
    }
}
</script>
<style scoped>
</style>