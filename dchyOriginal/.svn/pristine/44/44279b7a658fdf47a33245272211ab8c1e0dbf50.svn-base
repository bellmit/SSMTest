<template>
    <div>
        <Header :showUser="false"></Header>
        <div class="pwd-container bdc-container">
            <div class="form-title">
                <div class="list-title">忘记密码</div>
            </div>
            <div style="margin-top: 20px">
                <Form ref="pwd-form" class="form-edit" :model="pwdForm" @on-validate="validateChecked" :rules="pwdRules" :label-width="114">
                    <FormItem v-if="current==1" label="手机号码" prop="sjhm">
                        <Input v-model="pwdForm.sjhm"/>
                    </FormItem>
                    <FormItem v-if="current==1" label="验证码" prop="yzm">
                        <Input style="width: 70%" v-model="pwdForm.yzm"/>
                        <Button v-if='!hasYzm&&!timesUp' class="btn-cancel float-right" @click="getYzm">获取验证码</Button>
                        <Button v-if='hasYzm&&!timesUp' class="float-right">{{yzmTime}}s</Button>
                        <Button v-if='timesUp' class='btn-cancel float-right' @click="getYzm()">获取验证码</Button>
                    </FormItem>
                    <FormItem v-if="current==2" label="新密码" prop="password">
                        <Input v-model="pwdForm.password" type="password" placeholder="8-16位，由数字、下划线、字母组成"/>
                    </FormItem>
                    <FormItem v-if="current==2" label="确认新密码" prop="qrmm">
                        <Input type="password" v-model="pwdForm.qrmm"/>
                    </FormItem>
                </Form>
                <div class="submit-btn">
                    <Button v-if="current==1" class="bdc-major-btn btn-height-32" type="primary" @click="nextStep">下一步</Button>
                    <Button v-if="current==2" class="bdc-major-btn btn-height-32" type="primary" @click="submit">提交</Button>
                    <Button class="btn-cancel btn-height-32 margin-left-10" @click="toLogin">取消</Button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { queryYzm, resetPassword, yzSjhm } from "../../service/login"
export default {
    data() {
        const _self = this;
        return {
            pwdForm: {},
            hasYzm: false,
            current: 1,
            timesUp: false,
            isLastSubmit: false,
            countdownTime: "",
            yzmTime: 60,
            pwdRules: {
                sjhm: { required: true,message: "必填项不能为空",trigger: 'blur' },
                yzm: { required: true,message: "必填项不能为空",trigger: "blur" },
                password: {
                    required:true,
                    trigger: "blur",
                    validator: (rule,value,callback) => { 
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        }else if(!/^(?=.*\d+.*)(?=.*[a-zA-Z]+.*)(?=.*_.*)[\da-zA-Z_]{8,16}$/.test(value)){
                            callback("密码格式错误")
                        }
                        callback()      
                    }
                },
                qrmm: {
                    trigger: "blur",
                    required: true,
                    validator: (rule,value,callback) => { 
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        }else if(value!=_self.pwdForm.password){
                            callback("确认密码错误")
                        }
                        callback()      
                    }
                }
            }
        }
    },
    beforeDestroy() {
        clearInterval(this.countdownTime)
    },
    methods: {
        toLogin(){
            this.$router.push("/login")
        },
        nextStep(){
            this.isLastSubmit = true
            this.$refs["pwd-form"].validate(valid => {
                setTimeout(() => {
                    this.isLastSubmit = false;
                },500)
                if(valid){
                    // yzSjhm(this.pwdForm).then(res => {
                        
                    // })
                    this.current += 1;
                }
            })
        },
        submit(){
            this.isLastSubmit = true
            this.$refs["pwd-form"].validate(valid => {
                setTimeout(() => {
                    this.isLastSubmit = false;
                },500)
                if(valid){
                    let params = {
                        sjhm: this.pwdForm.sjhm,
                        yzm: this.pwdForm.yzm,
                        password: this.pwdForm.password
                    }
                    this.$loading.show("重置密码...")
                    resetPassword(params).then(res => {
                        this.$loading.close()
                        this.toLogin();
                    })
                }
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&this.isLastSubmit){
                this.$error.show(error);
            }
        },
        getYzm(){
            if(!this.pwdForm.sjhm){
                layer.msg("请先输入手机号码");
                return
            }
            this.timesUp = false;
            let params = {
                phones: [
                    {
                        phone: this.pwdForm.sjhm
                    }
                ]
            }
            this.$loading.show("发送中...")
            queryYzm(params).then(res => {
                this.$loading.close();
                this.hasYzm = true;
                this.countdownTime = setInterval(()=>{
                    this.yzmTime--;
                    if(this.yzmTime <= 0){
                        this.timesUp = true;
                        clearInterval(this.countdownTime);
                    }
                },1000)
            })
        },
    },
}
</script>
<style scoped>
    .pwd-container {
        min-height: calc(100vh - 70px);
        margin-top: 10px;
        background-color: #fff;
        padding: 10px;
    }
    .form-edit {
        padding: 0!important;
        width: 600px;
    }
    .submit-btn {
        margin-left: 200px;
        margin-top: 60px;
    }
    .form-edit >>> .ivu-form-item {
        margin-bottom: 30px!important;
    }
</style>