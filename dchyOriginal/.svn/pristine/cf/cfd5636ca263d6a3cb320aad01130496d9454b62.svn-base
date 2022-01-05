<template>
    <div class="home-page">
        <div class="content-user-header"></div>
        <div>
            <!-- 功能管理 -->
            <!-- <div v-for="(menu,index) in menuList" :key="index" class="margin-bottom-20">
                <van-cell class="home-cell-head" value="">
                    <template #title>
                        <span class="custom-title"><span class="title-list"></span><span class="title-tip">{{menu.title}}</span></span>
                    </template>
                </van-cell>
                <div class="cell-items">
                    <div class="manage-item" v-for="(m,i) in menu.menus" :key="i">
                        <a href="javascript:void(0)">
                            <img :src="'static/images/'+m.img" alt="">
                            <br>
                            <span class="manage-item-title">{{m.name}}</span>
                        </a>
                    </div>
                </div>
            </div> -->
        </div>
        <div class="cell-items cell-nologin margin-bottom-20">
            <div class="item-nologin" @click="toGuide">
                <div class="item-content">
                    <div class="item-name">
                        <div class="option-item-name">办事指南</div>
                        <div class="item-name-describtion">办事流程介绍</div>
                    </div>
                    <div class="item-img">
                        <img src="static/images/home-bszn.png" alt="">
                    </div>
                </div>
            </div>
            <div class="item-nologin" @click="onlineCommission">
                <div class="item-content">
                    <div class="item-name">
                        <div class="option-item-name">在线委托</div>
                        <div class="item-name-describtion">委托全天在线</div>
                    </div>
                    <div class="item-img">
                        <img src="static/images/home-wt.png" alt="">
                    </div>
                </div>
            </div>
            <div class="item-nologin" @click="toProcess">
                <div class="item-content">
                    <div class="item-name">
                        <div class="option-item-name">进度查询</div>
                        <div class="item-name-describtion">办件进度查询</div>
                    </div>
                    <div class="item-img">
                        <img src="static/images/home-jdcx.png" alt="">
                    </div>
                </div>
            </div>
            <div class="item-nologin"  @click="toMlk"> 
                <div class="item-content">
                    <div class="item-name">
                        <div class="option-item-name">名录库查看</div>
                        <div class="item-name-describtion">名录库信息查看</div>
                    </div>
                    <div class="item-img">
                        <img src="static/images/home-mlk.png" alt="">
                    </div>
                </div>
            </div>
            <div class="item-nologin" @click="toFinish">
                <div class="item-content">
                    <div class="item-name">
                        <div class="option-item-name">项目办结与评价</div>
                        <div class="item-name-describtion">项目评价</div>
                    </div>
                    <div class="item-img">
                        <img src="static/images/home-xmpj.png" alt="">
                    </div>
                </div>
            </div>
        </div>
        <!-- 通知公告 -->
        <div class="margin-bottom-20">
            <van-cell class="home-cell-head" value="">
                <template #title>
                    <div @click="toAnnounceMore" class="more-custom">
                        <span>更多>></span>
                    </div>
                    <div class="custom-title">
                        <span class="title-list"></span>
                        <span class="title-tip">通知公告</span>
                    </div>
                </template>
            </van-cell>
            <div class="cell-items notice-items">
                <div class="notice-item" v-for="(notice,index) in noticeList" @click="toDetail(notice)" :key="index">
                    <div class="notice-title">
                        <span>{{notice.BT}}</span>
                    </div>
                    <div class="notice-detail">
                        <div class="notice-type">{{notice.GGLX}}</div>
                        <div class="notice-time">{{notice.FBSJ}}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="top-message">
            <div>江苏政务服务网</div>
            <div>本服务由xx提供</div>
        </div>
    </div>
</template>
<script>
import { queryNoticeList } from "../../service/home"
import { initEntrust } from "../../service/commission"
import { getUserInfo } from "../../service/home"
import user_mixins from "../../utils/user"
export default {
    mixins: [user_mixins],
    data() {
        return {
            noticeList: [],
            menuList: [
                {
                    title: "名录库管理",
                    menus: [
                        {
                            name: "名录库入驻",
                            url: "",
                            img: "mlk-rz.png"
                        },
                        {
                            name: "名录库变更",
                            url: "",
                            img: "mlk-bg.png"
                        },
                        {
                            name: "名录库注销",
                            url: "",
                            img: "mlk-zx.png"
                        },
                        {
                            name: "变更记录展示",
                            url: "",
                            img: "mlk-jl.png"
                        }
                    ]
                },
                {
                    title: "委托管理",
                    menus: [
                        {
                            name: "我的委托",
                            url: "",
                            img: "my-wt.png"
                        },
                        {
                            name: "项目委托管理",
                            url: "",
                            img: "xmwt-manage.png"
                        }
                    ]
                },
                {
                    title: "测绘项目管理",
                    menus: [
                        {
                            name: "我的项目",
                            url: "",
                            img: "myproject.png"
                        },
                        {
                            name: "我的测绘项目",
                            url: "",
                            img: "mychproject.png"
                        }
                    ]
                }
            ],
            token: ""
        }
    },
    mounted() {
        this.queryNoticeList();
    },
    methods: {
        getUserInfo(){
            let params = {
                token: this.token
            }
            getUserInfo(params).then(res => {
                if(res.data.id || res.data.dwmc || res.data.username){
                    this.isLogin = true;
                }
            })
        },
        queryNoticeList(){
            let params = {
                page: 1,
                size: 10
            }
            queryNoticeList(params).then(res=> {
                if(res.data.dataList.length > 5){
                    this.noticeList = res.data.dataList.slice(0, 5);
                }else {
                    this.noticeList = res.data.dataList || [];
                }
            })
        },
        toDetail(announce){
            this.$router.push({path: "/announce/detail",query: {tzggid: announce.TZGGID}})
        },
        // 办事指南
        toGuide(){
            this.$router.push("/guide/list")
        },
        // 在线委托
        onlineCommission(){
            this.disPatcherCheck(() => {
                this.$toast.loading({
                    message: '加载中...',
                    forbidClick: true,
                });
                initEntrust().then(res => {
                    this.$toast.clear()
                    this.$router.push({
                        path: `/commission/online`,
                        query: { chxmid: res.data.chxmid, wtbh: res.data.wtbh, wtdw: res.data.wtdw, lxr: res.data.lxr, lxdh: res.data.lxdh } 
                    });
                })
            })
        },
        // 进度查询
        toProcess(){
            this.disPatcherCheck(() => {
                this.$router.push("/process/list")
            })
        },
        // 项目办结与评价
        toFinish(){
            this.disPatcherCheck(() => {
                this.$router.push("/finish/evaluate/list")
            })
        },
        toMlk(){
            this.$router.push("/mlk/list")
        },
        toAnnounceMore(){
            this.$router.push("/announce/more")
        }
    },
}
</script>
<style lang="less" scoped>
    @import url(./home.less);
    .top-message {
        text-align: center;
        color: #999;
        padding: 15px 0;
        position: fixed !important;
        bottom: 98px;
        width: 100%;
        background-color: #eaedf1;
        /* line-height: 98px;
        height: 98px; */
        box-sizing: border-box;
        /* border-top: 2px solid #d0d5da; */
        box-shadow: inset 0px 15px 15px -15px #d0d5da,
            inset 0px -15px 15px -15px #d0d5da;
    }
</style>