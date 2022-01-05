<template>
    <div class="mine-body">
        <div class="content-user-header">
            <a href="javascript:void(0)" @click="toMineUser" class="user-name">
                <img src="static/images/personal_notLoggedIn.png" alt="">
                <br>
                <span class="userName" v-if="isLogin">{{username}}</span>
                <span class="userName" v-else>请登录</span>
            </a>
        </div>
        <div>
            <van-cell-group>
                <van-cell class="cell-item" @click="toMineUser" :is-link="true" value="">
                    <template #title>
                        <span class="custom-title"><img class="cell-item-img" src="static/images/my-userinfo.png" alt="">我的账户</span>
                    </template>
                     
                </van-cell>
                <van-cell class="cell-item" @click="toMineMessage" :is-link="true" value="">
                    <template #title>
                        <span class="custom-title"><img class="cell-item-img" src="static/images/message-tip.png" alt="">我的消息</span>
                    </template>
                </van-cell>
                <van-cell class="cell-item" @click="toMineProject" :is-link="true">
                    <template #title>
                        <span class="custom-title"><img class="cell-item-img" src="static/images/my-project.png" alt="">我的项目</span>
                    </template>
                </van-cell>
            </van-cell-group>
        </div>
        <div style="text-align: center">
            <van-button type="info" v-if="isLogin" class="new-lg-btn" @click="outlogin" style="margin-top: 80px">退出登录</van-button>
        </div>
    </div>
</template>
<script>
import { getUserInfo, getAppUserInfo } from "../../service/home"
import { logoutPortalOl } from "../../service/login"
import user_mixins from "../../utils/user"
export default {
    mixins: [user_mixins],
    data() {
        return {
            token: "",
            ticket: ""
        }
    },
    mounted() {},
    methods: {
        getUserInfo(){
            let params = {
                token: this.token
            }
            getUserInfo(params).then(res => {
                if(res.data.id || res.data.dwmc || res.data.username){
                    this.username = res.data.username || res.data.dwmc || "";
                    this.isLogin = true;
                }
            })
        },
        // 我的账户
        toMineUser(){
            this.disPatcherCheck(() => {
                this.$router.push("/mine/user")
            })
        },
        // 我的消息
        toMineMessage(){
            this.disPatcherCheck(() => {
                this.$router.push("/message/list")
            })
        },
        // 我的项目
        toMineProject(){
            this.disPatcherCheck(() => {
                this.$router.push("/mine/project")
            })
        },
        outlogin(){
            this.$toast.loading({
                message: '退出登录...',
                forbidClick: true,
            });
            logoutPortalOl().then(res => {
                this.$toast.clear();
                window.location.reload()
            })
        }
    },
}
</script>
<style scoped lang="less">
    @import url(./mine.less);
    .mine-body {
        min-height: 100vh;
        background-color: #fff;
    }
</style>