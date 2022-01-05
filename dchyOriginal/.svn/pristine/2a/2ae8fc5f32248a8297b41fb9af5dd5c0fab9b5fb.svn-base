<template>
    <div class="van-form">
        <div class="clsx-items" v-for="(chjd,index) in clsxList" :key="chjd.DM">
            <div class="clsx-jd">
                <div>
                    <van-checkbox v-model="chjd.checked" @change="changeCheck($event,chjd,index)" shape="square">{{chjd.MC}}</van-checkbox>
                </div>
                <div class="clsx-operation" @click="collapseClick(chjd,index)">
                    <span>{{chjd.collapse ? "收起" : "展开"}}测绘事项
                        <i style="margin-left: 5px" v-if="chjd.collapse" class="fa fa-chevron-down"></i>
                        <i style="margin-left: 5px" v-else class="fa fa-chevron-up"></i>
                    </span>
                </div>
            </div>
            <div class="clsx-item" v-if="chjd.collapse">
                <div v-for="(clsx) in chjd.children" :key="clsx.DM">
                    <van-checkbox v-model="clsx.checked" @change="changeClsxCheck($event,chjd,index)" shape="square">{{clsx.MC}}</van-checkbox>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { getDictInfo } from "../../service/home";
import _ from "loadsh"
export default {
    data() {
        return {
            clsxList: []
        }
    },
    created() {
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["CLSX"]
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            getDictInfo(params).then(res => {
                this.$toast.clear();
                res.data.dataList&&res.data.dataList.forEach(list => {
                    if(!list.FDM&&list.ZDLX=="CLSX"){
                        this.clsxList.push({
                            ...list,
                            collapse: true,
                            children: []
                        })
                    }
                })
                res.data.dataList&&res.data.dataList.forEach(list => {
                    if(list.FDM&&list.ZDLX=="CLSX"){
                        let findIndex = this.clsxList.findIndex(clsx => clsx.DM == list.FDM)
                        this.clsxList[findIndex].children.push(list)
                    }
                })
            })
        },
        changeCheck(value,chjd,index){
            let clsxList = _.cloneDeep(this.clsxList)
            clsxList[index].children.forEach(clsx => {
                clsx.checked = value
            })
            this.clsxList = _.cloneDeep(clsxList)
        },
        changeClsxCheck(value,chjd,index){
            let hasUnSelect = false;
            chjd.children.forEach(clsx => {
                if(!clsx.checked){
                    hasUnSelect = true
                }
            })
            // if(!hasUnSelect){
            //     this.$set(this.clsxList[index],"checked",false)
            // } 
        },
        // 获取选中的测量事项
        querySelectClsx(){
            let select = [];
            this.clsxList.forEach(clsx => {
                clsx.children.forEach(c => {
                    if(c.checked){
                        select.push(c)
                    }
                })
            })
            return select
        },
        // 收起展开
        collapseClick(chjd,index){
            let collapse = !chjd.collapse
            this.$set(this.clsxList[index],"collapse",collapse)
        }
    },
}
</script>
<style scoped>
    .van-form {
        line-height: 1.5;
    }
    .clsx-items {
        min-height: 100px;
        box-sizing: border-box;
        background-color: #fff;
        padding: 20px 30px;
    }
    .clsx-item {
        padding-left: 20px;
    }
    .clsx-item > div {
        margin-top: 20px;
        color: #999!important;
    }
    .clsx-item >>> .van-checkbox__label {
        color: #999!important;
    }
    .clsx-items:nth-child(n+2){
        margin-top: 20px;
    }
    .clsx-jd {
        display: flex;
        justify-content: space-between;
    }
    .clsx-operation {
        font-size: 24px;
        color: #1d87d1;
    }
</style>