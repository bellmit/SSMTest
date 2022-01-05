<template>
    <div class="announce">
        <div class="content-user-header"></div>
        <div class="announce-content">
            <div class="announce-item" @click="toDetail(announce)" v-for="(announce) in noticeList" :key="announce.TZGGID">
                <div class="news-item-first-arrow">
                    <img src="static/images/news-item-arrow.png" alt="">
                </div>
                <div class="news-item-title">{{announce.BT}}</div>
                <div class="news-item-arrow"><i class="fa fa-lg fa-angle-right"></i></div>
            </div>
        </div>
        <div class="moreinfo">
            <a v-if="totalPage>1" @click="getMore" href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                加载更多<i class="fa fa-chevron-down"></i>
            </a>
        </div>
    </div>
</template>
<script>
import { queryNoticeList } from "../../service/home"
export default {
    data() {
        return {
            noticeList: [],
            totalPage: 1,
            form: {
                page: 1,
                size: 10
            }
        }
    },
    mounted() {
        this.queryNoticeList();
    },
    methods: {
        queryNoticeList(){
            queryNoticeList(this.form).then(res=> {
                this.noticeList = res.data.dataList || [];
                this.totalPage = res.data.totalPage || 1;
            })
        },
        toDetail(announce){
            this.$router.push({path: "/announce/detail",query: {tzggid: announce.TZGGID}})
        },
        getMore(){
            this.form.size += 10;
            this.queryNoticeList();
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
</style>