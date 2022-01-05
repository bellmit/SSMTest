<template>
    <div class="color-bgc height-100">
        <Header :showUser="false"></Header>
        <div class="bdc-location">
            <img src="static/images/index-home.png" alt="">
            <span>当前位置:&nbsp;</span>
            <span class="layui-breadcrumb" lay-separator=">" lay-filter="location">
            <span @click="toHome()" style="cursor:pointer;">首页</span>
            <span lay-separator>></span>
            <a><cite>办事指南</cite></a>
            </span>
        </div>
        <div class="mlk-container bdc-container">
            <div class="bdc-content-title">
                <span class="bdc-now-name">办事指南</span>
            </div> 
            <div class="guide-items">
                <div v-for="(item,index) in guideList" :key="index" class="guide-item position-relative">
                    <span :class="item.type?'guide-title':'guide-title cursor-pointer'" @click="toDetail(item)">{{index+1}}、{{item.BT}}</span>
                    <span class="position-right">
                        <Button class="btn-operation" @click="onlineHandle(item)">在线办理</Button>
                        <Button @click="toDetail(item)" v-if="!item.type" class="btn-operation margin-left-10">办事指南</Button></span>
                </div>
            </div>
            <div class="mlk-page">
                <Page :total="totalNum" show-total @on-change="changePage" @on-page-size-change="changeSize" size="small" show-elevator show-sizer />
            </div>
        </div>
    </div>
</template>
<script>
import { queryGuideList, getUserInfo } from "../../service/home"
import { linkToPortal } from "../../service/request";
export default {
    data() {
        return {
            formInline: {
                page: 1,
                size: 10
            },
            totalNum: 1,
            defaultList: [
                {
                    BT: "测绘单位申请入驻名录库",
                    type: "yz"
                },
                {
                    BT: "建设单位在线发布项目需求",
                    type: "yz"
                }
            ],
            guideList: [],
            userInfo: ""
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/announce/detail")){
            this.pageInfo["guidePageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["guidePageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["guidePageInfo"]){
            this.formInline = {...this.pageInfo["guidePageInfo"]}
        }
    },
    mounted() {
        this.queryGuideList()  
        this.getUserInfo();
        this.access_token = sessionStorage.getItem("access_token") || ""
    },
    methods: {
        getUserInfo(){
            getUserInfo().then(res => {
                this.userInfo = res.data;
                sessionStorage.setItem("userInfo",JSON.stringify(res.data))
            }).catch(err => {
                sessionStorage.removeItem("userInfo")
            })
        },
        toHome(){
            let token = sessionStorage.getItem("access_token") || ""
            this.$router.push({
                path: "/home",
                query: {
                    token
                }
            })
        },
        queryGuideList() {
            this.$loading.show("加载中...")
            queryGuideList(this.formInline).then(res => {
                this.$loading.close()
                let data = res.data.dataList || []
                this.guideList = [...this.defaultList, ...data];
                this.totalNum = res.data.totalNum ? res.data.totalNum + 2 : 2;
            }).catch(err=> {
                this.guideList = [...this.defaultList];
                this.totalNum = 2;
            })  
        },
        changePage(page){
            this.formInline.page = page
            this.queryGuideList();
        },
        changeSize(size){
            this.formInline.size = size
            this.queryGuideList();
        },
        // 用户跳转
        linkToPortalMenu(menuId=""){
            if (!location.origin) {
                location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
            }
            location.href = location.origin + "/portal-ol/login/disPatcher?token=" + this.access_token + "&rid=" + menuId
        },
        onlineHandle(item){
            if(!this.access_token){
                linkToPortal();
                return;
            } else {
                this.linkToPortalMenu();
            }
        },
        toDetail(item){
            if(item.type){
                return;
            }
            this.$router.push({path: "/announce/detail", query: {tzggid: item.TZGGID,name: "办事指南"}})
        }
    },
}
</script>
<style lang="less" scoped>
    @import "./guide.less";
</style>