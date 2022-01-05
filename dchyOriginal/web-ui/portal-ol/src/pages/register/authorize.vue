<template>
    <div>
        <Header :showUser="false"></Header>
        <div class="bdc-container register-container">
            <div class="container">
                <div class="form-title">
                    <div class="list-title">单位信息</div>
                </div>
                <Form :model="companyForm" ref="fr-form" class="form-edit" @on-validate="validateChecked" :rules="ruleInline" :label-width="162">
                    <Row>
                        <i-col span="8">
                            <FormItem label="法人类型 " :class="{'requireStar': btxyzList.includes('frlx'),'': true}" prop="frlx">
                                <Select v-model="companyForm.frlx">
                                    <Option v-for="(frlx) in frlxList" :key="frlx.DM" :value="frlx.DM">{{frlx.MC}}</Option>
                                </Select>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="单位名称 " :class="{'requireStar': btxyzList.includes('dwmc'),'': true}" prop="dwmc">
                                <Input v-model="companyForm.dwmc"/>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="统一社会信用代码 " :class="{'requireStar': btxyzList.includes('tyshxydm'),'': true}" prop="tyshxydm">
                                <Input v-model="companyForm.tyshxydm"/>
                            </FormItem>
                        </i-col>
                    </Row>
                     <Row>
                        <i-col span="8">
                            <FormItem label="法定代表人姓名 " :class="{'requireStar': btxyzList.includes('frmc'),'': true}" prop="frmc">
                                <Input v-model="companyForm.frmc"/>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="证件类型 " :class="{'requireStar': btxyzList.includes('frzjzl'),'': true}" prop="frzjzl">
                                <Select v-model="companyForm.frzjzl">
                                    <Option v-for="(zjlx) in zjlxList" :key="zjlx.DM" :value="zjlx.DM">{{zjlx.MC}}</Option>
                                </Select>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="法定代表人证件号码 " :class="{'requireStar': btxyzList.includes('frzjhm'),'': true}" prop="frzjhm">
                                <Input v-model="companyForm.frzjhm"/>
                            </FormItem>
                        </i-col>
                    </Row>
                </Form>
                <div class="form-title margin-top-10">
                    <div class="list-title">个人信息</div>
                </div>
                <Form :model="companyForm" ref="yh-form" class="form-edit" @on-validate="validateChecked" :rules="ruleInline" :label-width="162">
                     <Row>
                        <i-col span="8">
                            <FormItem label="姓名 " :class="{'requireStar': btxyzList.includes('yhmc'),'': true}" prop="yhmc">
                                <Input readonly v-model="companyForm.yhmc"/>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="证件类型 " :class="{'requireStar': btxyzList.includes('yhzjzl'),'': true}" prop="yhzjzl">
                                <Select disabled v-model="companyForm.yhzjzl">
                                    <Option v-for="(zjlx) in zjlxList" :key="zjlx.DM" :value="zjlx.DM">{{zjlx.MC}}</Option>
                                </Select>
                            </FormItem>
                        </i-col>
                        <i-col span="8">
                            <FormItem label="证件号码 " :class="{'requireStar': btxyzList.includes('yhzjhm'),'': true}" prop="yhzjhm">
                                <Input readonly v-model="companyForm.yhzjhm"/>
                            </FormItem>
                        </i-col>
                    </Row>
                </Form>
                <div class="form-title margin-top-10">
                    <div class="list-title">附件材料</div>
                </div>
                <uploadTableCommon
                    :data="uploadList" 
                    :showDefaultTool="false"
                    :columns="columns"
                    @upload="uploadFile"
                    @deleteFj="deleteFj"
                ></uploadTableCommon>
                <div class="save-btn margin-top-20">
                    <Button class="bdc-major-btn btn-height-32" type="primary" @click="authorizeClick()">确定</Button>
                    <Button class="btn-cancel btn-height-32 margin-left-10" @click="cancelClick()">取消</Button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import uploadTableCommon from "../../components/table/upload-table-common"
import { getUploadList, gryhsqRegister, queryCertificateInfo } from "../../service/login";
import { getDictInfo } from "../../service/home";
import _ from "loadsh";
import yz_mixins from "../../service/yz_mixins";
export default {
    mixins: [yz_mixins],
    components: {
        uploadTableCommon
    },
    data() {
        const _self = this;
        return {
            companyForm: {
                frlx: "",
                dwmc: "",
                tyshxydm: "",
                frmc: "",
                frzjzl: "",
                frzjhm: "",
                yhmc: "",
                yhzjzl: "",
                yhzjhm: ""
            },
            zjlxList: [],
            frlxList: [],
            ruleInline: {
                frlx: {
                    required: false,
                    message: "必填项不能为空"
                },
                dwmc: {
                    message: "必填项不能为空"
                },
                tyshxydm: {
                    message: "必填项不能为空"
                },
                frmc: {
                    message: "必填项不能为空"
                },
                frzjzl: {
                    required: false,
                    message: "必填项不能为空"
                },
                frzjhm: {
                    message: "必填项不能为空"
                },
                yhmc: {
                    message: "必填项不能为空"
                },
                yhzjzl: {
                    required: false,
                    message: "必填项不能为空"
                },
                yhzjhm: {
                    message: "必填项不能为空"
                },
            },
            uploadList: [],
            columns: [
                {
                    title: "材料名称",
                    field: "CLMC",
                    width: '25%'
                },
                {
                    title: "份数",
                    field: "FS"
                },
                {
                    title: "类型",
                    field: "CLLX",
                    type: "select",
                    value: "DM",
                    label: "MC",
                    options: []
                },
                {
                    title: "操作",
                    field: "operation",
                    type: "operation"
                }
            ],
            chooseUploadFile: [],
            cllxList: [],
            glsxid: "",
            ssmkid: "",
            error: "",
            token: "",
            ssmkid: '25',
            bdid: 'authorize',
            lastSubmit: false
        }
    },
    mounted() {
        this.token = this.$route.query.token || "";
        this.glsxid = this.$route.query.glsxid || "";
        this.getDictInfo();
        this.queryCertificateInfo();
        this.querySjcl();
    },
    methods: {
        // 获取用户信息
        queryCertificateInfo(){
            let token = this.$route.query.token || ""
            let params = {
                token
            }
            this.$loading.show("加载中...")
            queryCertificateInfo(params).then(res => {
                this.$loading.close();
                this.companyForm.yhmc = res.data.name || "";
                this.companyForm.yhzjzl = res.data.pagerstype || "";
                this.companyForm.yhzjhm = res.data.cardid || "";
            })
        },
        // 获取收件材料
        querySjcl(){
            let params = {
                glsxid: this.glsxid,
                ssmkid: this.ssmkid
            }
            getUploadList(params).then(res => {
                this.uploadList = res.data.dataList || [];
            })
        },
        // 验证附件
        validateFj(){
            let errorMsg = '';
            this.uploadList.forEach(upload => {
                if(upload.NEED == 1 && (!upload.children || !upload.children.length)){
                    errorMsg = upload.CLMC + "未上传材料"
                }
            })
            if(errorMsg){
                this.$error.show(errorMsg)
            }
            return errorMsg
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["CLLX","FRLX","ZJZL"]
            }
            this.zjlxList = [];
            this.cllxList = [];
            this.frlxList = [];
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(list => {
                    if(list.ZDLX == "CLLX"){
                        this.cllxList.push(list)
                    } else if(list.ZDLX == "FRLX"){
                        this.frlxList.push(list)
                    } else if(list.ZDLX == "ZJZL"){
                        this.zjlxList.push(list)
                    }
                })
                this.$set(this.columns[2],"options",this.cllxList)
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            this.error = error
            if(error&&this.error&&this.lastSubmit){
                this.$error.show(error);
                setTimeout(() => {
                    this.error = ""
                    this.$error.close();
                },1500)
            }
        },
        // 授权
        authorizeClick(){
            this.lastSubmit = true;
            this.$refs["fr-form"].validate(valid => {
                if(valid){
                    this.$refs["yh-form"].validate(v => {
                        setTimeout(() => {
                            this.lastSubmit = false
                        },500)
                        if(v){
                            let validate = this.validateFj();
                            if(validate){
                                return;
                            }
                            if(this.yzsxid){
                                this.yzYwlj(this.companyForm,this.authorize);
                            } else {
                                this.authorize();
                            }
                        }
                    })
                }
            })
        },
        authorize(){
            let formData = new FormData();
            for(let i = 0;i < this.chooseUploadFile.length; i++){
                formData.append('files', this.chooseUploadFile[i]); 
            }
            formData.append('frlx', this.companyForm.frlx || "");
            formData.append('dwmc', this.companyForm.dwmc || "");
            formData.append('glsxid', this.glsxid || "");
            formData.append('tyshxydm', this.companyForm.tyshxydm || "");
            formData.append('frmc', this.companyForm.frmc || "");
            formData.append('frzjzl', this.companyForm.frzjzl || "");
            formData.append('frzjhm', this.companyForm.frzjhm || "");
            formData.append('yhmc', this.companyForm.yhmc || "");
            formData.append('yhzjzl', this.companyForm.yhzjzl || "");
            formData.append('yhzjhm', this.companyForm.yhzjhm || "");
            formData.append('sjclid', this.uploadList[0].SJCLID || "");
            formData.append('clmc', this.uploadList[0].CLMC);
            formData.append('cllx', this.uploadList[0].CLLX);
            formData.append('sjxxid', this.uploadList[0].SJXXID || "");
            formData.append('ssmkid', this.ssmkid);
            formData.append('token', this.token);
            formData.append('xh', 0);
            this.$loading.show("授权中...")
            gryhsqRegister(formData).then(res => {
                this.$loading.close();
                layer.msg("授权成功")
                setTimeout(() => {
                    if (!location.origin) {
                        location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
                    }
                    location.href = location.origin + "/portal-ol/login/disPatcher?token=" + this.token + "&rid=" + "";
                },500)
            })
        },
        // 取消
        cancelClick(){
            this.$router.push({
                path: "/home",
                query: {
                    token: this.token
                }
            })
        },
        // 上传附件
        uploadFile(data){
            this.chooseUploadFile = [];
            this.uploadList[data.index].children = [];
            for(let i=0;i < data.files.length;i++) {
                this.chooseUploadFile.push(data.files[i])
            }
            for(let i=0;i < data.files.length;i++) {
                let uploadList = [...this.uploadList];
                uploadList[data.index].children.push(data.files[i]);
                uploadList[data.index].open = true;
                this.uploadList = [...uploadList];
            }
        },
         // 删除上传的附件
        deleteFj(select){
            // 删除文件
            let uploadList = [...this.uploadList];
            uploadList.forEach((upload,index) => {
                if(upload.clid == select.file.clid){
                    upload.children = [];
                    upload.WJZXID = "";
                }
            })
            this.uploadList = [...uploadList]
        }
    },
}
</script>
<style scoped>
    .container {
        background-color: #fff;
        padding: 20px;
        min-height: calc(100vh - 70px);
    }
    .register-container {
        margin: 10px auto 0;
    }
    .form-edit {
        width: 100%;
        padding: 0 0!important;
    }
</style>