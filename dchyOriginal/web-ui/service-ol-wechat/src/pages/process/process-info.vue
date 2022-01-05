<template>
    <div class="process-info">
        <div class="message-tip">
            <img src="static/images/dengdai.png" alt="">
            <div class="margin-top-20">
                <span class="tip-text">{{currentText}}处理中</span>
            </div>
        </div>
        <div class="process-online">
            <van-steps class="step-vertical" direction="vertical" :active='-1'>
                <van-step v-for="(step ,index) in onlineProcessDetail" :class="step.steps.length? 'step-active': ''" :key="index">
                    <div class="step-name">{{step.name}}</div>
                    <p v-for="(detail,i) in step.steps" :key="i">
                        {{detail.name}}：{{detail.value}}
                    </p>
                </van-step>
            </van-steps>
        </div>
    </div>
</template>
<script>
import { queryOnlineProcess } from "../../service/myproject"
export default {
    data() {
        return {
            chxmid: "",
            currentText: "",
            onlineProcessDetail: []
        }
    },
    mounted() {
        this.chxmid = this.$route.query.chxmid || "";
        this.queryOnlineProcess();
    },
    methods: {
        queryOnlineProcess(){
            let params = {
                chxmid: this.chxmid
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            queryOnlineProcess(params).then(res => {
                this.$toast.clear();
                res.data.dataList&&res.data.dataList.forEach(list => {
                    list.steps = []
                    if(list.details&&list.details.length){
                        this.currentText = list.name
                    }
                    list.details&&list.details.forEach(detail => {
                        for(let key in detail){
                            list.steps.push({
                                name: key,
                                value: detail[key]
                            })
                        }
                    })
                })
                this.onlineProcessDetail = res.data.dataList || [];
            })
        },
    },
}
</script>
<style lang="less" scoped>
    .process-info {
        min-height: 100vh;
        background-color: #fff;
    }
    .message-tip {
        padding-top: 40px;
        text-align: center;
    }
    .process-online {
        padding: 60px;
    }
    .tip-text {
        font-size: 40px;
    }
</style>