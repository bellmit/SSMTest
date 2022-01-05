<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="测绘单位 " class="form-list-search" prop="dwmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getChdwList(1,formInline.size)" v-model="formInline.dwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="单位性质 " class="form-list-search" prop="dwxz">
                            <Select v-model="formInline.dwxz" clearable class="form-search-item">
                                <Option v-for="(item,index) in dwxzList" :key="index" :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </FormItem>
                    </i-col> 
                    <i-col span="6">
                        <FormItem label="资质等级 " class="form-list-search" prop="zzdj">
                            <Select v-model="formInline.zzdj" clearable class="form-search-item">
                                <Option v-for="(item,index) in zzdjList" :key="index" :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </FormItem> 
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='20'>
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getChdwList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm(1,formInline.size)">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div>
            <Table
                :cols="tableCols"
                :data="chdwList"
                :count="totalNum"
                :page="formInline.page"
                :size="formInline.size"
                :func="getChdwList"
                :operation="operationList"
                @view="viewDetail"
                @remove="removeMlk"
            ></Table>
        </div>
        <Modal
            class="modal-base form-remove"
            v-model="visible"
            :title="'移出原因'"
            width="700"
            :mask-closable="false"
            :footer-hide="true"
            closable
        >
            <Form class="form-edit" ref="remove-form" @on-validate="validateChecked" :model="removeForm" :rules="fileRule" :label-width="114" >
                <FormItem v-model="removeForm.ycyy" prop="ycyy" label="移出原因：">
                    <Select v-model="removeForm.ycyy" multiple style="width: 500px">
                        <Option v-for="list in ycyyList" :key="list.DM" :value="list.MC">{{list.MC}}</Option>
                    </Select>
                </FormItem>
                <FormItem v-model="removeForm.ycbcsm" prop="ycbcsm" label="补充说明：">
                    <Input v-model="removeForm.ycbcsm" style="width: 500px" :rows="4" type="textarea" placeholder=""/>
                </FormItem>
            </Form>
            <div class="save-btn">
                <Button type="primary" class="bdc-major-btn" @click="confirmRemove()">确认</Button>
                <Button class="margin-left-10 btn-cancel" @click="cancelAdd()">取消</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import { queryChdwList } from "../../../service/mlk"
import { getDictInfo } from "../../../service/mlk"
import { removeMlk, Isconstructproject } from "../../../service/evaluate"
import moment from "moment";
export default {
    data() {
        return {
            formInline: {
                dwmc: "",
                dwxz: "",
                zzdj: "",
                page: 1,
                size: 10
            },
            ruleInline: {},
            dwxzList: [],
            zzdjList: [],
            visible: false,
            chdwList: [],
            removeForm: {
                ycyy: "",
                ycbcsm: ""
            },
            ycyyList: [],
            fileRule: {
                ycyy: {
                    required: true,
                    message: "移除原因不能为空"
                }
            },
            totalNum: 0,
            error: "",
            operationList: ["view","remove"],
            error: "",
            unLastSubmit: true,
            selectItem: {},
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    title: '测绘单位',
                    align: "center",
                    field: 'DWMC'
                },
                {
                    field: "FRDB",
                    title: '法人代表',
                    align: "center",
                },
                {
                    field: "CHZZZSBH",
                    title: '测绘资质证书编号',
                    align: "center",
                },
                {
                    field: "TYSHXYDM",
                    title: '统一社会信用代码',
                    align: "center",
                },
                {
                    field: "DWXZMC",
                    title: '单位性质',
                    align: "center",
                    width: 150
                },
                {
                    field: "ZZDJMC",
                    title: '资质等级',
                    align: "center",
                    width: 150
                },
                {
                    field: "CYRYNUM",
                    title: '从业人员数量',
                    align: "center",
                    width: 160
                },
                {
                    title: '操作',
                    align: "center",
                    minWidth: 180,
                    toolbar: '#operation',
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/mlk/view")){
            this.pageInfo["chdwListInfo"] = {...this.formInline}
        } else {
            this.pageInfo["chdwListInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["chdwListInfo"]){
            this.formInline = {...this.pageInfo["chdwListInfo"]}
        }
    },
    mounted() {
        this.getChdwList();
        this.getDictInfo();
    },
    methods: {
        // 获取字典项
        getDictInfo(){
            var params = {
                zdlx: ["DWXZ","ZZDJ","YCMLKYY"]
            }
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(r => {
                    if(r.ZDLX == "DWXZ"){
                        this.dwxzList.push(r)
                    }else if(r.ZDLX == "ZZDJ"){
                        this.zzdjList.push(r) 
                    } else {
                        this.ycyyList.push(r)
                    }
                })
                this.dwxzList.unshift({
                    DM: "",
                    MC: "全部"
                })
                this.zzdjList.unshift({
                    DM: "",
                    MC: "全部"
                })
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&this.error!=error&&!this.unLastSubmit){
                this.error = error
                this.$Message.error(error);
                setTimeout(() => {
                    this.error = ""
                },1500)
            }
        },
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        getChdwList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...");
            queryChdwList(this.formInline).then(res => {
                this.$loading.close();
                this.chdwList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        viewDetail(data){
            this.$router.push({path: "/admin/mlk/view", query: {mlkid: data.MLKID, ybrwid: data.YBRWID, sqid: data.SQXXID, from: this.$route.path}})
        },
        // 移出名录库
        removeMlk(data){
            layer.confirm("是否确认移出名录库？",(index) => {
                let params = {
                    mlkid: data.MLKID
                }
                this.$loading.show("加载中...")
                Isconstructproject(params).then(res => {
                    this.$loading.close();
                    if(res){
                        this.visible = true;
                        this.selectItem = data
                    }
                })
                layer.close(index)
            })
        },
        //确认移出
        confirmRemove(){
            this.unLastSubmit = false
            this.$refs["remove-form"].validate(valid => {
                this.unLastSubmit = true
                if(valid){
                    let params = {
                        mlkid: this.selectItem.MLKID,
                        ycyy: this.removeForm.ycyy,
                        ycdw: this.selectItem.CHDWMC,
                        ycbcsm: this.removeForm.ycbcsm
                    }
                    this.$loading.show("加载中...")
                    removeMlk(params).then(res => {
                        this.$loading.close();
                        layer.msg("移出成功")
                        this.resetRemoveForm();
                        this.getChdwList();
                        this.visible = false;
                    })
                }
            })
        },
        resetRemoveForm(){
            this.removeForm = {
                ycyy: "",
                ycbcsm: ""
            }
        },
        cancelAdd(){
            this.visible = false;
            this.resetRemoveForm();
        }
    },
}
</script>
<style scoped>
    .modal-base .form-edit {
        padding: 0!important;
    }
    .form-edit >>> .ivu-item-form {
        margin-bottom: 20px!important;
    }
</style>