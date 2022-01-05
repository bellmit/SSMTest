<template>
    <div class="message-page">
        <div class="message-lists" v-for="(message,index) in messageList" @click="toMessageDetail(message)" :key="message.GLSXID + index">
            <div class="message-img">
                <img src="static/images/tip.png" alt="">
                <div v-if="message.DQZT == '0'" class="wd-tip"></div>
            </div>
            <div class="message-detail">
                <div class="message-title">{{message.XXSXMC || "消息提醒"}}</div>
                <div class="message-text"><span>{{message.XXNR}}</span></div>
            </div>
            <div class="message-time">
                <span>{{message.FSSJ}}</span>
            </div>
        </div>
        <div v-if="totalPage > 1" @click="getMore" class="moreinfo margin-top-10">
            <a href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                加载更多<i class="fa fa-chevron-down"></i>
            </a>
        </div>
    </div>
</template>
<script>
import { getMessage, changeMessageStatus } from "../../service/myproject"
export default {
    data() {
        return {
            form: {
                page: 1,
                pageSize: 10
            },
            totalPage: 1,
            messageList: []
        }
    },
    mounted() {
        this.getMessage()
    },
    methods: {
        getMessage(){
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            getMessage(this.form).then(res => {
                this.$toast.clear();
                this.messageList = res.data.dataList || [];
                this.totalPage = res.data.totalPage || 1;
            })
        },
        getMore(){
            this.form.pageSize += 10;
            this.getMessage();
        },
        toMessageDetail(item){
            let params = {
                yhxxidlist: [item.YHXXID]
            }
            if(item.DQZT == "0"){
                this.$toast.loading({
                    message: '加载中...',
                    forbidClick: true,
                });
                changeMessageStatus(params).then(res => {
                    this.$toast.clear();
                    this.$router.push({
                        path: "/message/detail",
                        query: {
                            xxsxmc: item.XXSXMC ? encodeURIComponent(item.XXSXMC) : '',
                            xxnr: item.XXNR ? encodeURIComponent(item.XXNR) : '',
                            fssj: item.FSSJ ? encodeURIComponent(item.FSSJ) : ''
                        }
                    })
                })
            } else {
                this.$router.push({
                    path: "/message/detail",
                    query: {
                        xxsxmc: item.XXSXMC ? encodeURIComponent(item.XXSXMC) : '',
                        xxnr: item.XXNR ? encodeURIComponent(item.XXNR) : '',
                        fssj: item.FSSJ ? encodeURIComponent(item.FSSJ) : ''
                    }
                })
            }
        }
    },
}
</script>
<style scoped>
    .message-page {
        min-height: 100vh;
        background-color: #fff;
    }
    .message-lists {
        height: 120px;
        line-height: 1;
        padding: 20px 30px;
        border-bottom: 1px solid #d0d5da;
        display: flex;
        justify-content: flex-start;
    }
    .message-img {
        width: 60px;
        position: relative;
        text-align: center;
        margin-left: 10px;
    }
    .message-img img {
        margin-top: 10px;
    }
    .message-detail {
        width: 50%;
        overflow: hidden;
        margin-left: 60px;
        line-height: 1.5;
    }
    .message-title {
        font-size: 32px;
        color: #333;
    }
    .message-text {
        font-size: 24px;
        color: #666;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
    .message-time {
        flex: 1;
        text-align: right;
        font-size: 24px;
        color: #999;
    }
    .wd-tip {
        width: 14px;
        height: 14px;
        border-radius: 50%;
        background-color: red;
        position: absolute;
        right: 0;
        top: 12px;
    }
</style>