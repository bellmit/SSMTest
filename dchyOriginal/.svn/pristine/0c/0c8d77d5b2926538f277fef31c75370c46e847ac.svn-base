<template>
    <div class="mlk-list">
        <div class="search-top">
            <van-field v-model="queryForm.dwmc" label="" clearable placeholder="请输入测绘单位名称">
                <template #left-icon>
                    <van-icon @click="getMlkList(true)" name="search" />
                    <div class="line-search"></div>
                </template>
            </van-field>
        </div>
        <div class="mlk-items" v-for="(item,index) in mlkList" @click="toMlkDetail(item)" :key="index">
            <div style="border-bottom: 1px solid #ccc;"><span :class="!item.SFDJ||item.SFDJ=='0'?'mlk-name':'font-color-666'" >{{item.DWMC}}</span></div>
            <div class="mlk-item" >
                <div class="freeze-item" v-if="item.SFDJ&&item.SFDJ=='1'">
                    <img src="static/images/ydj.png" alt="">
                </div>
                <div class="mlk-img" >
                    <img v-if="!item.MLKTP" src="static/images/fz.png" alt="">
                    <img v-else :src="item.MLKTP" alt="">
                </div>
                <div class="mlk-detail">
                    <div class="mlk-grade line-height-28"><span class="font-color-666">资质等级:</span> {{item.ZZDJMC}}</div>
                    <div class="mlk-grade line-height-28"><span class="font-color-666">评价等级:</span> 
                    <van-rate color="#ffd21e"  allow-half :class="item.SFDJ&&item.SFDJ=='1'?'color-disabled':''" v-model="item.PJDJ" />({{item.PJDJ}}分)</div>
                    <div class="mlk-grade line-height-28"><span class="font-color-666">联系电话:</span> {{item.LXDH}}</div>
                </div>
            </div>
        </div>
        <div v-if="totalPage>1" @click="getMore" class="moreinfo margin-top-10">
                <a href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                                加载更多<i class="fa fa-chevron-down"></i>
                            </a>
        </div>
    </div>
</template>
<script>
import { queryMlkList } from "../../service/home"
export default {
    data() {
        return {
            totalNum: 0,
            mlkList: [],
            dictionaryList: [],
            totalPage: 1,
            queryForm: {
                dwmc: "",
                page: 1,
                size: 5
            },
        }
    },
    mounted() {
        this.getMlkList();
    },
    methods: {
        getMlkList(isSearch){
            if(isSearch){
                this.queryForm = {
                    ...this.queryForm,
                    page: 1,
                    size: 5
                }
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            queryMlkList(this.queryForm).then(res => {
                this.$toast.clear()
                res.data.dataList&&res.data.dataList.forEach(mlk => {
                    mlk.PJDJ = parseInt(mlk.PJDJ)
                })
                this.mlkList = res.data.dataList || [];
                this.totalPage = res.data.totalPage || 1;
                this.totalNum = res.data.totalNum || 0;
            })
        },
        back(){
            window.close()
        },
        toMlkDetail(data){
            this.$router.push({path: "/mlk/mlk-view", query:{mlkid: data.MLKID}})
        },
        getMore(){
            this.queryForm.size += 5;
            this.getMlkList();
        }
    },
}
</script>
<style scoped>
    .color-disabled >>> .van-rate__icon--full:before,
    .color-disabled >>> .van-rate__icon--half:before {
        color: #666;
    }
    .mlk-items {
       min-height: calc(100vh - 370px);
        
        margin: 25px;
        background-color: #fff;
        min-height: 60px;
        font-size: 32px;
        padding: 20px 36px;
    }
    .mlk-item {
        position: relative;
        box-sizing: border-box;
        margin: 10px;
        display: flex;
        /* justify-content: space-between; */
        flex-wrap: wrap;
    }
    .mlk-img {
        height: 100%;
        margin-right: 60px;
        width: 35%;
        margin-top: 10px;
        }
    .mlk-img  img {
        width: 100%;
        height: 180px;
        margin-top: 10px;
    }
    .mlk-detail {
        height: 180px;
    }
    .freeze-item {
        position: absolute;
        padding: 0 5px;
        left: -5px;
        top: 20px;
        z-index: 10;
    }
    
    .mlk-name {
        font-size: 32px;
        line-height: 30px;
        font-weight:bold;
        margin-bottom: 20px;
        display: block;
    }
    .mlk-grade {
        font-size: 24px;
        margin-top: 32px;
    }
    .moreinfo {
        text-align: center;
        display: block;
    }
</style>