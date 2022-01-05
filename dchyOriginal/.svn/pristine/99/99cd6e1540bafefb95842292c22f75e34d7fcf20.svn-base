<template>
    <div class='content-header'>
        <div class='header-inner'>
            <div class='header-logo'>
                <div class="home-title">{{placeSevice}}<span class="font-sun">“</span>多测合一<span class="font-sun">”</span>网上服务大厅</div>
                <div class="title-bot-line">{{placePinyin}} "MULTI TEST IN ONE" ONLINE SERVICE HALL</div>
            </div>
            <div class='header-title' @click='backToLogin()'>
                <img src="static/images/back-to-index.png" alt="">
                返回网站首页
            </div>
            <div class='header-nav'>
                <!-- <span class='user-center' v-if="showUser" @click='linkToUser()'>用户中心</span> -->
                <!-- <span @click='outLogin()'>退出登录</span> -->
            </div>
        </div>
    </div>
</template>
<script>
import { redirectToHome } from "../../service/request"
import  Vue from 'vue';
export default {
    name: 'Header',
    props: {
        showUser: {
            type: Boolean,
            default: true
        }
    },
    data() {
        return {
            placeSevice: config.placeSevice,
            placePinyin: config.placePinyin
        }
    },
    methods: {
        backToLogin() {
            // this.$router.push("/home");
            redirectToHome();
        },
        outLogin(){
            
        },
        linkToUser(){
            this.$router.push("/home");
        }
    },
}
</script>
<style scoped lang='less'>
 .content-header {
     background-color: #1d87d1;
     width: 100%;
     height: 60px;
     flex-shrink: 0;
     .home-title {
        color: #fff;
        font-size: 24px;
        font-weight: bold;
    }
    .title-bot-line {
        color: #fff;
        font-size: 14px;
        font-family: "Arial";
    }
    .header-inner {
        margin: 0 auto;
        position: relative;
        height: 60px;
        display: flex;
        justify-content: flex-start;
        .header-title {
            height: 30px;
            margin-top: 16px;
            margin-left: 20px;
            font-size: 14px;
            color: #d6e5f4;
            opacity: .8;
            cursor: pointer;
            border: 1px solid #fff;
            border-radius: 5px;
            padding: 5px 10px;
            margin-right: 5px;
        }
        .header-nav {
            position: absolute;
            right: 0;
            top: 0;
            height: 60px;
            color: #fff;
            font-size: 16px;
            line-height: 60px;
            display: flex;
            justify-content: flex-end;
            text-align: right;
            span {
                cursor: pointer;
            }
        }
    }  
 } 
</style>