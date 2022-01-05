<template>
    <div>
        <Tabs value="name1">
            <TabPane label="变更记录" name="name1"></TabPane>
            <Button class="btn-cancel" @click="returnBack()" slot="extra">返回</Button>
        </Tabs>
        <div class="form-title">
            <div class="list-title">编辑记录</div>
        </div>
        <div>
            <Table 
                :cols='tableCols' 
                :data="recordList" 
                :count="totalNum" 
                :showPage="false"
                :unShowTool="true"
                :mergeCol="true"
            ></Table>
        </div>
        <div class="line-dashed margin-top-20 margin-bottom-10"></div>
        <div class="form-title">
            <div class="list-title">新增记录</div>
        </div>
        <div>
            <Table 
                :id="'addTableID'"
                :cols='addTableCols' 
                :data="addRecordList" 
                :count="totalNum" 
                :showPage="false"
                :unShowTool="true"
                :mergeCol="true"
            ></Table>
        </div>
        <div class="line-dashed margin-top-20 margin-bottom-10"></div>
         <div class="form-title">
            <div class="list-title">删除记录</div>
        </div>
        <div>
            <Table 
                :id="'deleteTableID'"
                :cols='deleteTableCols' 
                :data="deleteRecordList" 
                :count="totalNum" 
                :showPage="false"
                :showDefaultTool="false"
                :mergeCol="true"
            ></Table>
        </div>
    </div>
</template>
<script>
import { queryEditRecordDetail } from '../../../service/mlk'
export default {
    data() {
        return {
            formInline: {
                xgsj: "",
                xgr: "",
                page: 1,
                pageSize: 10
            },
            recordList: [],
            deleteRecordList: [],
            addRecordList: [],
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
                    field: "mkmc",
                    title: "模块名称",
                    align: "center"
                },
                {
                    field: "ZDMC",
                    title: "字段名称",
                    align: "center"
                },
                {
                    field: "YZ",
                    title: "原值",
                    align: "center"
                },
                {
                    field: "XZ",
                    title: "新值",
                    align: "center"
                }
            ],
            addTableCols: [
                {
                    type: "numbers",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "mkmc",
                    title: "模块名称",
                    align: "center"
                },
                {
                    field: "XZ",
                    title: "新值",
                    align: "center"
                }
            ],
            deleteTableCols: [
                {
                    type: "numbers",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "mkmc",
                    title: "模块名称",
                    align: "center"
                },
                {
                    field: "YZ",
                    title: "原值",
                    align: "center"
                }
            ],
            rzid: ""
        }
    },
    mounted() {
        if(this.$route.query.rzid){
            this.rzid = this.$route.query.rzid
        }
        this.getRecordList();
    },
    methods: {
        // 获取台账list
        getRecordList(){
            let recordList = [], addRecordList = [], deleteRecordList = []
            queryEditRecordDetail({rzid: this.rzid}).then(res => {
                res.data.czxx.forEach(cz => {
                    cz.cznr.forEach(nr => {
                        nr.xgzdxx.forEach(zdxx => {
                            if(cz.czlx == "修改"){
                                recordList.push({mkmc: nr.mkmc,...zdxx})
                            } else if(cz.czlx == "新增"){
                                addRecordList.push({mkmc: nr.mkmc,...zdxx})
                            } else if(cz.czlx == "删除"){
                                deleteRecordList.push({mkmc: nr.mkmc,...zdxx})
                            }
                        })
                    })
                })
                this.recordList = [...recordList]
                this.addRecordList = [...addRecordList]
                this.deleteRecordList = [...deleteRecordList]
            })
        },
        // 返回
        returnBack(){
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