<template>
    <div class="announce">
        <div class="content-user-header"></div>
        <div class="announce-content">
            <div class="announce-item" v-for="(announce) in guideList" @click="toAnnounce(announce)" :key="announce.TZGGID">
                <div class="news-item-first-arrow">
                    <img src="static/images/news-item-arrow.png" alt="">
                </div>
                <div class="news-item-title">{{announce.BT}}</div>
                <div class="news-item-arrow"><i class="fa fa-lg fa-angle-right"></i></div>
            </div>
        </div>
        <div class="moreinfo">
            <a v-if="totalPage > 1" @click="getMore" href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                加载更多<i class="fa fa-chevron-down"></i>
            </a>
        </div>
    </div>
</template>
<script>
import { queryGuideList } from "../../service/home"
import { getUserInfo } from "../../service/home"
import { initEntrust } from "../../service/commission"
import user_mixins from "../../utils/user"
export default {
    mixins: [user_mixins],
    data() {
        return {
            guideList: [
                {
                    BT: "建设单位在线委托",
                    type: "wt"
                }
            ],
            totalPage: 1,
            form: {
                page: 1,
                size: 10
            }
        }
    },
    mounted() {
        this.queryGuideList();
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
        queryGuideList(){
            queryGuideList(this.form).then(res=> {
                this.guideList = res.data.dataList || [];
                this.totalPage = res.data.totalPage || 1;
           })
        },
        toAnnounce(item){
            this.disPatcherCheck(() => {
                if(item.type != 'wt'){
                    this.$dialog.alert({
                        message: "该功能手机端暂未开放，请至网页端办理"
                    })
                } else {
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
                }
            })
        },
        getMore(){
            this.form.size += 10;
            this.queryGuideList();
        },
    },
}
</script>
<style scoped>
    .announce {
        background-color: #fff;
    }
    .content-user-header {
        width: 100%;
        height: 370px;
        background: url('~static/images/home-bg.png') no-repeat;
        background-size: 100%;
    }
    .announce-content {
        min-height: calc(100vh - 370px);
        background-color: #fff;
        margin: 10px 0;
    }
    .announce-item {
        position: relative;
        min-height: 60px;
        font-size: 32px;
        line-height: 1.5;
        padding: 30px 36px;
        box-sizing: border-box;
        border-bottom: 1px solid #D3D3D3;
    }
    .news-item-first-arrow {
        position: absolute;
        top: 35px;
    }
    .news-item-first-arrow img {
        width: 32px;
        height: auto;
    }
    .news-item-title {
        padding-left: 38px;
        padding-right: 32px;
        
        text-overflow: ellipsis;
        white-space: normal;
        overflow: hidden;
    }
    .news-item-arrow {
        position: absolute;
        top: 25px;
        right: 30px;
    }
    .moreinfo {
        text-align: center;
        display: block;
    }
</style>