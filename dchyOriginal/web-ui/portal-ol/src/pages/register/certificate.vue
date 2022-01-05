<template>
    <div>
        <Header :showUser="false"></Header>
        <div class="bdc-container register-container">
            <div class="container">
                <div class="form-title">
                    <div class="list-title">请认证账户类型</div>
                </div>
                <div class="position-relative">
                    <div class="tab-user">
                        <div class="survey-user" @click="swicthTab('survey')">
                            <img v-if="isSurvey" src="static/images/cehui_hover.png" alt="">
                            <img v-else src="static/images/cehui.png" alt="">
                            <div v-if="isSurvey" class="user-active">测绘单位</div>
                            <div v-else class="user-disable">测绘单位</div>
                        </div>
                        <div class="constrct-user" @click="swicthTab('constrct')">
                            <img v-if="isSurvey" src="static/images/jianshe.png" alt="">
                            <img v-else src="static/images/jianshe_hover.png" alt="">
                            <div v-if="isSurvey" class="user-disable">建设单位</div>
                            <div v-else class="user-active">建设单位</div>
                        </div>
                    </div>
                    <div v-if="isSurvey" class="survey-user-core"></div>
                    <div v-else class="construct-user-core"></div>
                </div>
                <div class="user-form form-edit margin-top-20">
                    <Form :model="certificateData" ref="registerForm" @on-validate="validateChecked" :rules="ruleInline" :label-width="142">
                        <FormItem v-if="isSurvey" label="测绘单位名称 " v-model="certificateData.dwmc" prop="dwmc">
                            <Input readonly v-model="certificateData.dwmc"/>
                        </FormItem>
                        <FormItem v-else label="建设单位名称" v-model="certificateData.dwmc" prop="dwmc">
                            <Input readonly v-model="certificateData.dwmc"/>
                        </FormItem>
                        <FormItem label="统一社会信用代码" v-model="certificateData.tyshxydm" prop="tyshxydm">
                            <Input readonly v-model="certificateData.tyshxydm"/>
                        </FormItem>
                        <FormItem v-if="!isSurvey" label="建设单位码" v-model="certificateData.jsdwm" prop="jsdwm">
                            <Input v-model="certificateData.jsdwm" placeholder="请输入单位名称大写首字母前六位"/>
                        </FormItem>
                        <FormItem :label="isSurvey ? '测绘单位联系人': '建设单位联系人'"  v-model="certificateData.lxr" prop="lxr">
                            <Input v-model="certificateData.lxr"/>
                        </FormItem>
                        <FormItem :label="isSurvey ? '测绘单位联系电话': '建设单位联系电话'" v-model="certificateData.sjhm" prop="sjhm">
                            <Input v-model="certificateData.sjhm"/>
                        </FormItem>
                    </Form>
                </div>
                <div class="save-btn">
                    <Button class="bdc-major-btn btn-height-32" type="primary" @click="cetificateClick()">确认</Button>
                    <Button class="btn-cancel btn-height-32 margin-left-10" @click="cancelClick()">取消</Button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { queryCertificateInfo, qyrzRegister } from "../../service/login"
export default {
    data() {
        const _self = this;
        return {
            certificateData: {
                dwmc: "",
                lxr: "",
                sjhm: "",
                tyshxydm: "",
                jsdwm: ""
            },
            isSurvey: true,
            ssmkid: "3",
            bdid: "3",
            yzmTime: 60,
            hasYzm: false,
            timesUp: false,
            flag: "",
            uploadFile: [],
            error: "",
            token: "",
            ruleInline: {
                dwmc: { required:true,message: "必填项不能为空",trigger: 'blur'},
                lxr: { required:true,message: "必填项不能为空",trigger: 'blur' },
                sjhm: { required:true,message: "必填项不能为空",trigger: 'blur' },
                tyshxydm: { required:true,message: "必填项不能为空",trigger: 'blur' },
                jsdwm: { 
                    required:true,
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
            }
        }
    },
    mounted() {
        this.queryCertificateInfo();
        this.token = this.$route.query.token || "";
    },
    methods: {
        queryCertificateInfo(){
            let token = this.$route.query.token || ""
            let params = {
                token
            }
            queryCertificateInfo(params).then(res => {
                this.certificateData.dwmc = res.data.name;
                this.certificateData.tyshxydm = res.data.creditcode;
                if(res.data.jsdwm){
                    this.isSurvey = false;
                    this.certificateData.jsdwm = res.data.jsdwm;
                    this.certificateData.lxr = res.data.lxr || "";
                    this.certificateData.sjhm = res.data.lxdh || "";
                }
            })
        },
        resetForm(){
            this.certificateData = {
                ...this.certificateData,
                lxr: "",
                sjhm: "",
                jsdwm: ""
            }
        },
        swicthTab(type){
            this.resetForm();
            if(type == "survey"){
                this.isSurvey = true;
            }else {
                this.isSurvey = false
            }
        },
        cancelClick(){
            this.$router.push({
                path: "/home",
                query: {
                    token: this.token
                }
            })
        },
        // 确认认证
        cetificateClick(){
            this.$refs["registerForm"].validate(valid => {
                if(valid){
                    let yhlx = this.isSurvey ? "2":"1"
                    this.certificateData.yhlx = yhlx;
                    let certificateData = {...this.certificateData}
                    if(!this.isSurvey&&certificateData.jsdwm.length < 6){
                        let len = certificateData.jsdwm.length
                        let jsdwm = certificateData.jsdwm;
                        for(let i = len; i < 6; i++){
                            jsdwm += '0'
                        }
                        certificateData.jsdwm = jsdwm;
                    }
                    let yhsf = this.isSurvey ? "测绘单位": "建设单位"
                    layer.confirm(`请确认用户身份：${yhsf}?`,(index) => {
                        layer.close(index)
                        this.$loading.show("认证中...")
                        certificateData.token = this.token;
                        qyrzRegister(certificateData).then(res => {
                            this.$loading.close();
                            layer.msg("认证成功")
                            setTimeout(() => {
                                if (!location.origin) {
                                    location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
                                }
                                location.href = location.origin + "/portal-ol/login/disPatcher?token=" + this.token + "&rid=" + "";
                            },500)
                        })
                    })
                }
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            this.error = error
            if(error&&this.error){
                this.$error.show(error);
                setTimeout(() => {
                    this.error = "";
                    this.$error.close();
                },1500)
            }
        }
    },
}
</script>
<style lang="less" scoped>
    .container {
        background-color: #fff;
        padding: 20px;
        min-height: calc(100vh - 70px);
    }
    .register-container {
        margin: 10px auto 0;
    }
    .survey-user-core {
        position: absolute;
        bottom: -10px;
        left: calc(25% - 10px);
        transform: rotate(-45deg);
        width: 20px;
        height: 20px;
        background-color: #f3f4f6;
    }
    .user-form {
        padding: 20px 180px!important;
    }
    .construct-user-core {
        position: absolute;
        bottom: -10px;
        left: calc(75% - 10px);
        transform: rotate(-45deg);
        width: 20px;
        height: 20px;
        background-color: #f3f4f6;
    }
    .tab-user {
        z-index: 1;
        height: 180px;
        padding: 20px;
        background-color: #f3f4f6;
        display: flex;
        position: relative;
        justify-content: flex-start;
        > div {
            width: 50%;
            cursor: pointer;
            text-align: center;
            vertical-align: middle;
        }
        .survey-user {
            border-right: 1px solid #ccc;
        }
        .user-active {
            margin-top: 10px;
            color: #1d87d1;
            font-weight: bold;
        }
        .user-disable {
            margin-top: 10px;
            color: #333;
            font-weight: bold;
        }
    }
</style>
<style scoped>
    .user-form >>> .ivu-form-item {
        margin-bottom: 15px!important;
    }
</style>