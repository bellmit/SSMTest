<template>
    <div id="newsContent">
        <h3 class="news-title">{{detail.bt}}</h3>
        <div class="news-base-info">
            <span class="news-time">发布时间：{{detail.fbsj}}</span>
            <span class="news-author margin-left-10">发布人：{{detail.fbr}}</span>
        </div>
        <div class="news-content" v-html="content"></div>
        <div class="news-fj">
            <span>附件: </span>
            <a href="javascript: void(0)" @click="dwonloadFj()">附件材料</a>
        </div>
    </div>
</template>
<script>
import { queryNoticeDetail } from "../../service/home"
import moment from "moment"
export default {
    data() {
        return {
            tzggid: "",
            detail: "",
            content: "",
            wjzxid: ""
        }
    },
    mounted() {
        this.tzggid = this.$route.query.tzggid || ""
        this.queryNoticeDetail();
    },
    methods: {
        queryNoticeDetail(){
            let params = {
                tzggid: this.tzggid
            }
            queryNoticeDetail(params).then(res => {
                res.dataList.fbsj = moment(res.dataList.fbsj).format("YYYY-MM-DD HH:mm:ss")
                this.detail = res.dataList
                this.content = res.dataList.ggnr;
                this.wjzxid = res.dataList.wjzxid;
            })
        },
        dwonloadFj(){
            if(!this.wjzxid){
                this.$toast('暂无材料')
                return
            }
            location.href = '/portal-ol/fileoperation/download?wjzxid=' +this.wjzxid
        }
    },
}
</script>
<style lang="less" scoped>
    #newsContent {
        box-sizing: border-box;
        min-height: 100vh;
        padding-bottom: 20px;
        background-color: #fff;
    }
    .news-title {
        font-size: 44px;
        text-align: left;
        font-weight: bold;
        margin: 0px 30px 20px 30px;
    }
    .news-base-info {
        text-align: center;
        line-height: 50px;
        color: #888;
        font-size: 28px;
        margin: 20px 30px;
    }
    .news-content {
        text-indent: 2rem;
        font-size: 32px;
        line-height: 64px;
        margin: 0px 30px 20px 30px;
    }
    .news-fj {
        padding: 0 30px;
        margin-top: 40px;
        color: #333;
    }
</style>