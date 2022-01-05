<template>
    <div>
        <Tabs value="name1">
            <TabPane label="从业人员" name="name1"></TabPane>
            <Button slot="extra" type="primary" class="btn-cancel" @click="returnApply()">返回</Button>
        </Tabs>
        <!-- <div class="list-btn-oper">
            <Button type="primary" class="bdc-major-btn btn-h-32 width-90" v-if="from.startsWith('/survey/mlkapply')" @click="add">新增</Button>
            
        </div> -->
        <Table 
            :cols="tableCols" 
            :data="cyryxxList" 
            :page='form.page' 
            :size="form.size" 
            :count="totalNum" 
            :operation="operationList"
            :func="getCyryxxList"
            :tool="tool"
            @view="viewDetail"
            @edit="edit"
            @delete="deleteCyryxx"
            @deleteOpr="deleteOpr"
            @btn1="add"
        >
        </Table>
    </div>
</template>
<script>
import { getCyryList, initCyryxx, deleteCyryxx } from "../../../service/mlk"
export default {
    data() {
        return {
            form: {
                page: 1,
                size: 10
            },
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: 'RYXM',
                    title: "姓名",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'ZC',
                    title: "职称",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'ZSMC',
                    title: "证书名称",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'ZSBH',
                    title: "证书编号",
                    align: "center",
                    minWidth: 200,
                },
                {
                    field: 'JSRQ',
                    title: "证书有效期",
                    align: "center",
                    minWidth: 200,
                },
                {
                    title: "操作",
                    align: "center",
                    minWidth: 200,
                    toolbar: "#operation",
                }
            ],
            tool: '<div>' +
                '<span class="layui-btn main-btn-a" lay-event="btn1">新增</span>' +
            '</div>',
            operationList: ["view","edit","delete"],
            cyryxxList: [],
            totalNum: 0,
            mlkid: "",
            from: "",
            editorable: "false"
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/survey/cyryxx/add")&&to.query.type!="add"){
            this.pageInfo["cyryxxPageInfo"] = {...this.form}
        } else {
            this.pageInfo["cyryxxPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["cyryxxPageInfo"]){
            this.form = {...this.pageInfo["cyryxxPageInfo"]}
        }
        this.editorable = this.$route.query.editorable || this.editorable;
        if(this.editorable == "false"){
            this.tool = "";
        }
    },
    mounted(){
        this.mlkid = this.$route.query.mlkid;
        this.getCyryxxList();
        this.from = this.$route.query.from
    },
    methods: {
        // 初始化从业人员信息
        initCyryxx(){
            let params = {
                mlkid: this.mlkid
            }
            initCyryxx(params).then(res => {
                this.$router.push({path: "/survey/cyryxx/add",query: {mlkid: this.mlkid,from: this.$route.query.from,cyryid: res.data.cyryid,editorable: this.editorable,type: "add"}})
            })
        },
        getCyryxxList(page,size){
            if(page){
                this.form.size = size;
                this.form.page = page;
            }
            this.form.mlkid = this.mlkid;
            this.$loading.show("加载中...");
            getCyryList(this.form).then(res => {
                this.$loading.close();
                this.cyryxxList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
                
                sessionStorage.setItem("cyrynum",this.totalNum||"");
            })
        },
        deleteOpr(){
            if(this.editorable == "false"){
                this.$nextTick(() => {
                    this.cyryxxList.forEach((list,index) => {
                        $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='edit']").remove();
                        $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='delete']").remove();
                    })
                })
            }
        },
        returnApply(){
            if(this.$route.query.from){
                let from = this.$route.query.from
                this.$router.push({path: from,query: {mlkid: this.mlkid}})
            }
        },
        viewDetail(data){
            this.$router.push({path: "/survey/cyryxx/add", query: {mlkid: this.mlkid,from: this.$route.query.from,editorable: this.editorable,cyryid: data.CYRYID}})
        },
        edit(data){
            this.$router.push({path: "/survey/cyryxx/add", query: {mlkid: this.mlkid,from: this.$route.query.from,editorable: this.editorable,cyryid: data.CYRYID,isEdit: true}})
        },
        add(){
            this.initCyryxx();
        },
        deleteCyryxx(data){
            layer.confirm("确认删除?",(index) => {
                layer.close(index)
                this.$loading.show("删除中...")
                deleteCyryxx({cyryid: data.CYRYID}).then(res => {
                    layer.msg("删除成功")
                    this.$loading.close();
                    this.getCyryxxList()
                })
            })
        }
    },
}
</script>