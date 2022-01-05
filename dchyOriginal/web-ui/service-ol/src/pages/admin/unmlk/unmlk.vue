<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :label-width="120" :rules="ruleInline" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="测绘单位" class="form-list-search" prop="chdwmc" >
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getUnMlkList(1,formInline.pageSize)" v-model="formInline.chdwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="20">
                            <Button type="primary" class="btn-h-32 bdc-major-btn margin-left-20" @click="getUnMlkList(1,formInline.pageSize)">查询</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="announce-table">
            <Table
                :cols="tableCols" 
                :data="UnMlkList" 
                :size="formInline.pageSize" 
                :page="formInline.page" 
                :count="totalNum"
                :operation="operationList"
                :func="getUnMlkList"
                @view="viewDetail"
            ></Table>
        </div>
    </div>
</template>
<script>
import util from '../../../service/util'
import { queryUnmlkByPage } from "../../../service/mlk"
export default {
    data() {
        return {
            ruleInline: {},
            formInline: {
                chdwmc: "",
                chxmid: "",
                page: 1,
                pageSize: 10
            },
            operationList: ["view"],
            totalNum: 0,
            UnMlkList: [],
            tableCols: [
                {
                    title: '序号',
                    align: "center",
                    width: 70,
                    field: "ROWNUM_",
                    fixed: "left"
                },
                {
                    field: "DWMC",
                    title: '测绘单位',
                    align: "center"
                },
                {
                    field: "ZZDJ",
                    title: '资质等级',
                    align: "center",
                },
                {
                    field: "TYSHXYDM",
                    title: '统一社会信用代码',
                    align: "center",
                },
                {
                    field: "FRDB",
                    title: '法人代表',
                    align: "center",
                },
                {
                    field: "BGDZ",
                    title: '办公地址',
                    align: "center",
                },
                {
                    field: "LRSJ",
                    title: '录入时间',
                    align: "center",
                },
                {
                    field: "LRR",
                    title: '录入人',
                    align: "center",
                },
                {
                    title: '操作',
                    align: "center",
                    toolbar: '#operation',
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/unmlk/view")){
            this.pageInfo["unmlkPageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["unmlkPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["unmlkPageInfo"]){
            this.formInline = {...this.pageInfo["unmlkPageInfo"]}
        }
    },
    mounted() {
        this.getUnMlkList();
    },
    methods: {
        viewDetail(data){
            this.$router.push({path: "/admin/unmlk/view", query: {chdwid: data.CHDWID,chdwmc: data.CHDWMC, type:"view"}})
        },
        getUnMlkList(page,pageSize){
            if(page){
                this.formInline.page = page;
                this.formInline.pageSize = pageSize;
            }
            this.$loading.show("加载中...")
            queryUnmlkByPage(this.formInline).then(res => {
                this.$loading.close();
                this.UnMlkList = res.data.dataList.content;
                this.totalNum = res.data.dataList.totalElements;
                this.UnMlkList.forEach((list,index) =>{
                    list.BGDZ = ( list.BGDZS + list.BGDZSS + list.BGDZQX + list.BGDZXX) || "";
                })
            })
        }
    },
}
</script>