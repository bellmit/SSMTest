<template>
    <div>
        <div class="search-form">
            <div class="position-relative">
                    <span class="table-title">测绘项目: {{ xmmc }}</span>
                    <span class="table-title">测绘单位: {{ chdw }}</span>
                    <div class="position-right">
                        <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="cancel">返回</Button>
                    </div>
            </div>
        </div>
        <div class="margin-top-20">
            <Table
                ref="tableRef"
                :cols="tableCols"
                :data="htbadjList"
                :count="totalNum"
                :tool="tool"
                :page="recordForm.page"
                :size="recordForm.size"
                :operation="operationList"
                :func="getHtdadjList"
                @view="viewDetail"
                @resume="resume"
                @pause="pause"
                @stop="stop"
                @deleteOpr="deleteOpr"
                @check="selectData"
                @btn1="resumeAll"
                @btn2="pauseAll"
                @btn3="stopAll"
            ></Table>
        </div>
        <Modal
            class="modal-base form-remove"
            v-model="visible"
            :title="operationType == 'pause'?'暂停原因':'停止原因'"
            width="600"
            :mask-closable="false"
            :footer-hide="true"
            closable
        >
            <Form class="form-edit" ref="remove-form" @on-validate="validateChecked" :model="removeForm" :rules="fileRule" :label-width="100" >
                <FormItem v-model="removeForm.czyy" prop="czyy" label="原因">
                    <Input v-model="removeForm.czyy" style="width: 400px" :rows="4" type="textarea" placeholder=""/>
                </FormItem>
            </Form>
            <div class="save-btn">
                <Button type="primary" class="btn-h-36" @click="confirmRemove()">确认</Button>
                <Button class="margin-left-10 btn-h-36" @click="cancle()">取消</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import moment from "moment";
import { queryHtbadjSljlList, resumeBadj, pauseBadj, stopBadj } from "../../../service/manage"
export default {
    data() {
        return {
            recordForm: {
                page: 1,
                size: 10
            },
            recordRule: {},
            removeForm: {
                czyy: ""
            },
            fileRule: {
                czyy: {
                    required: true,
                    message: "原因不能为空"
                }
            },
            totalNum: 0,
            xmmc:"",
            chdw:"",
            operationList: ["view","resume","pause","stop"],
            htbadjList: [],
            slbh: "",
            visible: false,
            tool: '<div>' +
                    '<span class="layui-btn main-btn-a" lay-event="btn1">全部恢复</span>' +
                    '<span class="layui-btn main-btn-a margin-left-10" lay-event="btn2">全部暂停</span>'+
                    '<span class="layui-btn main-btn-a margin-left-10" lay-event="btn3">全部停止</span>'+
                '</div>',
            error: "",
            operationType: "",
            selectedList: [],
            tableCols: [
                {
                    type: "checkbox",
                    align: "center",
                    width: "5%"
                },
                {
                    field: "CLSX",
                    title: "测绘事项",
                    align: "center"
                   
                },
                {
                    field: "SSJD",
                    title: "所属阶段",
                    align: "center",
                    width: "20%"
                },
                {
                    field: "CLZT",
                    title: "状态",
                    align: "center",
                    width: "15%"
                },
                {
                    title: "操作",
                    align: "center",
                    width: "30%",
                    toolbar: "#operation"
                }
            ],
            chxmid: "1234566"
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/manage/htbadj/rzxq")){
            this.pageInfo["rzxqPageInfo"] = {...this.recordForm}
        } else {
            this.pageInfo["rzxqPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["rzxqPageInfo"]){
            this.recordForm = {...this.pageInfo["rzxqPageInfo"]}
        }
    },
    mounted() {
       if(this.$route.query.slbh){
           this.slbh = this.$route.query.slbh
       } 
       if(this.$route.query.chxmid){
           this.chxmid = this.$route.query.chxmid
       }
       this.xmmc = this.$route.query.gcmc
       this.chdw = this.$route.query.chdwmc ? this.$route.query.chdwmc.substr(0, this.$route.query.chdwmc.length - 1) : ""; 
       this.getHtdadjList();
    },
    methods: {
        // 获取合同备案登记列表
        getHtdadjList(page,size){
            if(page){
                this.recordForm.page = page
                this.recordForm.size = size
            }
            this.$loading.show("加载中...")
            this.recordForm.chxmid = this.chxmid
            queryHtbadjSljlList(this.recordForm).then(res => {
                this.$loading.close();
                this.htbadjList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&this.error!=error){
                this.error = error
                this.$error.show(error);
                setTimeout(() => {
                    this.error = ""
                },1500)
            }
        },
        // 确认停止
        confirmRemove(){
            this.$refs["remove-form"].validate(valid => {
                if(valid){
                    this.visible = false;
                    let params = {
                        czyy: this.removeForm.czyy,
                        clsxidList: this.selectedList.map(list => { return {clsxid: list.CLSXID} })
                    }
                    if(this.operationType == "pause"){
                        this.$loading.show("暂停中...")
                        params.czzt = 2
                        pauseBadj(params).then(res => {
                            this.$loading.close()
                            this.getHtdadjList()
                            this.$refs["remove-form"].resetFields();
                        })
                    } else if(this.operationType == "stop"){
                        this.$loading.show("停止中...")
                        params.czzt = 3
                        stopBadj(params).then(res => {
                            this.$loading.close()
                            this.getHtdadjList()
                            this.$refs["remove-form"].resetFields();
                        }) 
                    }
                }
            })
        },
        cancle(){
            this.$refs["remove-form"].resetFields();
            this.visible = false;
        },
        // 全部停止
        stopAll(){
            if(!this.selectedList.length){
                layer.msg("至少选择一条数据")
                return;
            }
            layer.confirm("是否确定停止所选测绘事项?", (index) => {
                layer.close(index)
                this.visible = true;
                this.operationType = "stop";
            })
        },
        // 全部暂停
        pauseAll(){
            if(!this.selectedList.length){
                layer.msg("至少选择一条数据")
                return;
            }
            layer.confirm("是否确定暂停所选测绘事项?", (index) => {
                layer.close(index)
                this.visible = true;
                this.operationType = "pause"
            })
        },
        // 全部恢复
        resumeAll(){
            if(!this.selectedList.length){
                layer.msg("至少选择一条数据")
                return;
            }
            layer.confirm("是否确认恢复?", (index) => {
                layer.close(index)
                let params = {
                    czzt: 1,
                    clsxidList: this.selectedList.map(list => { return {clsxid: list.CLSXID} })
                }
                this.$loading.show("恢复中...")
                resumeBadj(params).then(res => {
                    this.$loading.close()
                    this.getHtdadjList()
                })
            })
        },
        // 停止
        stop(data){
            layer.confirm("确认停止?",(index) => {
                layer.close(index)
                this.selectedList = [data]
                this.visible = true;
                this.operationType = "stop";
            })
        },
        // 暂停
        pause(data){
            layer.confirm("确认暂停?",(index) => {
                layer.close(index)
                this.selectedList = [data]
                this.visible = true;
                this.operationType = "pause";
            })
        },
        // 恢复
        resume(data){
            layer.confirm("是否确认恢复?", (index) => {
                layer.close(index)
                this.$loading.show("恢复中...")
                let params = {
                    czzt: 1,
                    clsxidList: [
                        {
                            clsxid: data.CLSXID
                        }
                    ]
                }
                resumeBadj(params).then(res => {
                    this.$loading.close()
                    this.getHtdadjList()
                })
            })
        },
        // 选中一项
        selectData(data){
            this.selectedList = [...data]
        },
        cancel(){
            this.$router.push("/manage/htbadj")
        },
        deleteOpr(){
            this.htbadjList.forEach((list, index) => {
                if (list.CLZT == "正常") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='resume']").remove();
                } else if (list.CLZT == "停止") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='stop']").remove();
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='resume']").remove();
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='pause']").remove();
                    $(".layui-table tr[data-index="+index+"] input[type='checkbox']").next().remove();
                    $(".layui-table tr[data-index="+index+"] input[type='checkbox']").remove();
                } else if (list.CLZT == "暂停") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='stop']").remove();
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='pause']").remove();
                }
            });
        },
        // 查看操作
        viewDetail(data){
            this.$router.push({
                path: `/manage/htbadj/rzxq`,
                query: {clsxid: data.CLSXID}
            });
        },
    },
}
</script>
<style scoped>
    .table-title {
        margin-right: 20px;
    }
    .modal-base .form-edit {
        padding: 0!important;
    }
</style>