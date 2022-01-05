<template>
    <div class="mine-user">
        <div class="user-img">
            <div><span>头像</span></div>
            <div class="user-tx">
                <img src="static/images/personal_header.png" alt="">
            </div>
        </div>
        <div class="margin-top-20"></div>
        <van-form ref="company-form" label-align="left" label-width="8em">
            <van-field
                readonly
                name="frlx"
                v-model="userInfo.username"
                label="姓名"
            />
            <van-field
                readonly
                name="sjhm"
                v-model="userInfo.sjhm"
                label="手机号码"
            />
            <van-field
                readonly
                name="dwmc"
                v-model="userInfo.dwmc"
                label="单位名称"
            />
            <div class="submit-btn">
                <van-button class="new-lg-btn" @click="outlogin">退出登录</van-button>
            </div>
        </van-form>
    </div>
</template>
<script>
import { getUserInfo } from "../../service/home"
import { logoutPortalOl } from "../../service/login"
export default {
    data() {
        return {
            access_token: "",
            userInfo: {
                username: "",
                sjhm: "",
                dwmc: ""
            }
        }
    },
    mounted() {
        this.getUserInfo()
    },
    methods: {
        getUserInfo(){
            let params = {
                token: this.access_token
            }
            getUserInfo(params).then(res => {
                if(res.data.id || res.data.dwmc || res.data.username){
                    this.userInfo = res.data;
                }
            })
        },
        outlogin(){
            this.$toast.loading({
                message: '退出登录...',
                forbidClick: true,
            });
            logoutPortalOl().then(res => {
                this.$router.push("/mine")
            })
        },
        back(){
            this.$router.push("/mine")
        }
    },
}
</script>
<style scoped>
    .user-img {
        padding: 0 30px;
        box-sizing: border-box;
        height: 120px;
        line-height: 120px;
        display: flex;
        background-color: #fff;
        justify-content: space-between;
    }
    .user-tx img{
        width: 80px;
        height: 80px;
        overflow: hidden;
        border-radius: 50%;
        margin-top: 20px;
    }
    .mine-user >>> .van-field__label {
        padding-left: 30px;
    }
    .mine-user >>> .van-cell__value {
        padding-right: 30px;
    }
    .mine-user >>> .van-field__control {
        text-align: right;
    }
    .submit-btn {
        text-align: center;
    }
</style>