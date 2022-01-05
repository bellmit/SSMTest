<template>
    <div class="login-page">
        <div class="content-user-header"></div>
        <div class="login-content">
            <van-form class="van-login-form" :show-error-message="false" validate-first ref="company-form">
                <!-- <van-field
                    v-model="loginForm.loginName"
                    left-icon="graphic"
                    label=""
                    placeholder="请输入手机号"
                    :rules="[{ required: true, message: '请填写用户名' }]"
                />
                <van-field
                    v-model="loginForm.password"
                    type="password"
                    label=""
                    left-icon="lock"
                    placeholder="请输入验证码"
                    :rules="[{ required: true, message: '请填写密码' }]"
                >
                     <template #button>
                        <van-button size="small" type="primary" class="yzm-btn">获取短信验证码</van-button>
                    </template>
                </van-field> -->
                <van-field
                    v-model="loginForm.loginName"
                    left-icon="graphic"
                    label=""
                    placeholder="请输入用户名"
                    :rules="[{ required: true, message: '用户名不能为空' }]"
                />
                <van-field
                    v-model="loginForm.loginPassword"
                    type="password"
                    label=""
                    left-icon="lock"
                    placeholder="请输入密码"
                    :rules="[{ required: true, message: '密码不能为空' }]"
                />
                <van-field
                    v-model="loginForm.yzm"
                    label=""
                    left-icon="wap-nav"
                    placeholder="请输入验证码"
                    :rules="[{ required: true, message: '验证码不能为空' }]"
                >
                    <template #button>
                        <img :src="yzmSrc" @click="getYzm" class="yzm-png cursor-pointer margin-left-10" alt="">                                
                    </template>
                </van-field>
                <div class="relative-operate blue">
                    <span class="user-register">
                        <!-- <van-checkbox class="blue" v-model="loginForm.remeber" shape="square">记住我</van-checkbox> -->
                    </span>
                    <span class="forget-password">
                        忘记密码
                    </span>
                </div>
                <div>
                    <van-button block type="info" class="new-lg-btn" style="margin-top: 80px" @click="onSubmit">提交</van-button>
                    <van-button block class="new-lg-btn cancel-lg-btn margin-top-20" @click="toMine">取消</van-button>
                </div>
            </van-form>
        </div>
    </div>
</template>
<script>
import { loginWithYzm } from "../../service/login"
export default {
    data() {
        return {
            loginForm: {
                loginName: "",
                loginPassword: "",
                yzm: ""
            },
            yzmSrc: ""
        }
    },
    mounted() {
        this.getYzm();
    },
    methods: {
        onSubmit(){
            this.$refs["company-form"].validate().then(() => {
                this.$toast.loading({
                    message: '登录中...',
                    forbidClick: true,
                });
                loginWithYzm({...this.loginForm}).then(res => {
                    this.$toast.clear();
                    this.$router.go(-1)
                }).catch(err => {
                    this.getYzm();
                })
            }).catch(err => {
                this.$dialog.alert({
                    message: err[0].message
                })
            })
        },
        toMine(){
            this.$router.push("/mine")
        },
        getYzm(){
            this.yzmSrc = "";
            this.yzmSrc = config.portalOlContext + "/common/kaptcha?d=" + encodeURI(new Date());
        },
    },
}
</script>
<style lang="less" scoped>
    @import url(./login.less);
</style>
<style scoped>
.login-content >>> .van-field__left-icon .van-icon {
    font-size: 1.8rem!important;
    color: #999;
} 
.blue >>> .van-checkbox__label {
    color: #1d87d1;
}
.login-page {
    padding-bottom: 60px;
    background-color: #fff;
}
</style>