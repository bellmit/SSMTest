<template>
    <div>
        <div class="search-form">
            <Form :model="recordForm" ref="search-form" inline :label-width="150">
                <Row>
                    <i-col span="6">
                        <FormItem label="建设单位" class="form-list-search">
                            <Input v-model="recordForm.jsdwmc" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="统一社会信用代码" class="form-list-search">
                            <Input v-model="recordForm.tyshxydm" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="建设单位码" class="form-list-search">
                            <Input v-model="recordForm.jsdwm" @keydown.enter.native.prevent="queryList(1,recordForm.pageSize)" class="form-search-item"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="50">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryList(1,recordForm.pageSize)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="line-dashed"></div>
        <div>
            <Table
                ref="tableRef"
                :cols="tableCols"
                :data="jsdwList"
                :count="totalNum"
                :tool="tool"
                :page="recordForm.page"
                :size="recordForm.pageSize"
                :func="queryList"
                @btn1="manualJsdw"
            ></Table>
        </div>
        <Modal 
            class="modal-base" 
            v-model="visible" 
            :title="'录入建设单位'"
            width="600"
            :mask-closable="false" 
            :footer-hide="true" 
            closable>
            <Form class="form-edit modal-form" ref="jsdw-form" @on-validate="validateChecked" :model="jsdwForm" :rules="jsdwInline" :label-width="150">
                <FormItem v-model="jsdwForm.dwmc" label="建设单位名称" prop="dwmc">
                    <Input v-model="jsdwForm.dwmc"/>
                </FormItem>
                <FormItem v-model="jsdwForm.tyshxydm" label="统一社会信用代码" prop="tyshxydm">
                    <Input v-model="jsdwForm.tyshxydm"/>
                </FormItem>
                <FormItem v-model="jsdwForm.jsdwm" label="建设单位码" prop="jsdwm">
                    <Input v-model="jsdwForm.jsdwm" placeholder="请输入单位名称大写首字母前六位"/>
                </FormItem>
                <FormItem v-model="jsdwForm.lxr" label="联系人" prop="lxr">
                    <Input v-model="jsdwForm.lxr"/>
                </FormItem>
                <FormItem v-model="jsdwForm.lxdh" label="联系电话" prop="lxdh">
                    <Input v-model="jsdwForm.lxdh"/>
                </FormItem>
            </Form>
            <div class="submit-back" >
                <Button type="primary" class="btn-h-34 bdc-major-btn" @click="saveJsdw()">确认</Button>
                <Button class="btn-h-34 btn-cancel margin-left-10" @click="cancelClick()">取消</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import { queryJsdwManageList } from "../../service/manage"
import { saveJsdw } from "../../service/manage"
export default {
    data() {
        return {
            recordForm: {
                page: 1,
                pageSize: 10,
                jsdwmc: "",
                tyshxydm: "",
                jsdwm: ""
            },
            totalNum: 0,
            ksOptions: {},
            jsOptions: {},
            jsdwList: [],
            visible: false,
            selectRecord: {},
            tool: '<div>' +
                '<span class="layui-btn main-btn-a" lay-event="btn1">新增</span>' +
            '</div>',
            unLastSubmit: true,
            jsdwForm: {
                dwmc: "",
                tyshxydm: "",
                jsdwm:"",
                lxr: "",
                lxdh: ""
            },
            jsdwInline: {
                dwmc: { required: true, message: "必填项不能为空", trigger: 'blur'},
                tyshxydm: { required: true, message: "必填项不能为空", trigger: 'blur'},
                jsdwm: { 
                    required: true,
                    trigger: 'blur',
                    validator: (rule,value,callback) => {
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        } else if(value.length>6){
                            callback("建设单位码不能超过6个字符")
                        } else if(!/^[A-Z0-9]{0,6}$/.test(value)){
                            callback("建设单位码格式错误")
                        }
                        callback();
                    }
                },
                lxr: { required: true, message: "必填项不能为空", trigger: 'blur'},
                lxdh: { 
                    required: true, 
                    type: 'number',
                    trigger: "blur",
                    validator: (rule,value,callback) => {
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        } else if(value&&!(/^([0-9]{3,4}-)?[0-9]{7,8}$/).test(value)&&!(/^1\d{10}$/.test(value))){
                            callback("联系电话格式错误")
                        }
                        callback();
                    }
                }
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
                    field: "DWMC",
                    title: "建设单位",
                    align: "center",
                    width: 200
                },
                {
                    field: "TYSHXYDM",
                    title: "统一社会信用代码",
                    align: "center"
                },
                {
                    field: "JSDWM",
                    title: "建设单位码",
                    align: "center"
                },
                {
                    field: "LXR",
                    title: "联系人",
                    align: "center"
                },
                {
                    field: "LXDH",
                    title: "联系电话",
                    align: "center"
                },
                {
                    field: "LRSJ",
                    title: "录入时间",
                    align: "center"
                },
                {
                    field: "LRR",
                    title: "录入人",
                    align: "center"
                }
            ]
        }
    },
    mounted() {
        this.queryList();
    },
    methods: {
        // 重置查询项
        resetForm(){
            this.recordForm = {
                page: this.recordForm.page,
                pageSize: this.recordForm.pageSize
            }
        },      
        // 查询
        queryList(page,size){
            if(page){
                this.recordForm.page = page;
                this.recordForm.pageSize = size;
            }
            this.$loading.show("加载中...");
            queryJsdwManageList(this.recordForm).then(res => {
                this.$loading.close();
                this.jsdwList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&!this.unLastSubmit){
                this.$error.show(error)
            }
        },
        // 新增建设单位
        saveJsdw(){
            this.unLastSubmit = false;
            this.$refs["jsdw-form"].validate(valid => {
                setTimeout(() => {
                    this.unLastSubmit = true;
                },500)
                if(valid){
                    let jsdwForm = {...this.jsdwForm}
                    if(jsdwForm.jsdwm.length < 6){
                        let len = jsdwForm.jsdwm.length
                        let jsdwm = jsdwForm.jsdwm;
                        for(let i = len; i < 6; i++){
                            jsdwm += '0'
                        }
                        jsdwForm.jsdwm = jsdwm;
                    }
                    this.$loading.show("录入中...")
                    saveJsdw(jsdwForm).then(res => {
                        this.$loading.close();
                        this.cancelClick();
                    })
                }
            })
        },
        cancelClick(){
            this.visible = false;
            this.$refs["jsdw-form"].resetFields();
        },
        // 新增
        manualJsdw(){
            this.visible = true;
        }
    },
}
</script>
<style scoped>
    .pre-content {
        padding: 10px;
        background-color: #000;
        color: #fff;
    }    
    .modal-form {
        margin-top: 20px;
        padding: 0 20px!important;
    }
</style>