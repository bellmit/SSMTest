<template>
    <div class="evaluate-page">
        <div class="evaluate-title">
            <span>评价内容</span>
        </div>
        <div class="evaluate-form">
            <van-form validate-first ref="company-form" label-align="right" label-width="7em">
                <van-field
                    readonly
                    name="gcmc"
                    v-model="evaluateForm.gcmc"
                    label="工程名称"
                    placeholder="请输入工程名称"
                />
                <van-field
                    readonly
                    v-model="evaluateForm.chdwmc"
                    name="chdwmc"
                    label="测绘单位"
                />
                <van-field
                    readonly
                    rows="2"
                    name="clsxmc"
                    type="textarea"
                    v-model="evaluateForm.clsxmc"
                    label="测绘事项"
                />
            </van-form>
            <div class="margin-top-20">
                <van-form class="pj-pf" validate-first ref="evaluate-form" label-align="right" label-width="7em">
                    <van-field 
                        class="requireStar" 
                        name="rate" 
                        label="评分"
                    >
                        <template #input>
                            <van-rate v-model="evaluateForm.fwpj" />
                        </template>
                    </van-field>
                    <van-field
                        class="pj-input"
                        v-model="evaluateForm.pjyj"
                        rows="2"
                        autosize
                        label=""
                        maxlength="200"
                        show-word-limit
                        type="textarea"
                        placeholder="请输入评价内容..."
                    />
                </van-form>
            </div>
            <div class="submit-btn">
                <van-button class="new-lg-btn margin-top-20" @click="evaluate" >评价</van-button>
            </div>
        </div>
    </div>
</template>
<script>
import { getDictInfo } from "../../service/home"
import { queryJsdwWtxx, jsdwEvaludate } from "../../service/myproject"
export default {
    data() {
        return {
            evaluateForm: {
                gcmc: "",
                chdwmc: "",
                fwpj: 0,
                pjyj: ""
            },
            chxmid: "",
            chdwxxid: "",
            dictionaryInfoTree: [],
            checkedClsxList: [],
            checkedClsxMcList: []
        }
    },
    mounted() {
        this.chxmid = this.$route.query.chxmid || "";
        this.chdwxxid = this.$route.query.chdwxxid || "";
        this.evaluateForm.gcmc = this.$route.query.gcmc || ""
        this.evaluateForm.chdwmc = this.$route.query.chdwmc || ""
        this.getDictInfo();
    },
    methods: {
        // 获取项目详情
        queryEntrustByChxmid(){
            let params = {
                chxmid: this.chxmid,
                chdwxxid: this.chdwxxid
            }
            this.$toast.loading({
                message: "加载中...",
                forbidClick: true
            })
            queryJsdwWtxx(params).then(res => {
                this.$toast.clear();
                let xmInfo = res.data.dataList&&res.data.dataList.length ? res.data.dataList[0] : {}
                this.checkedClsxList = xmInfo.CLSX.split(",") || [];
                this.renderTreeList();
            })
        },
        // 渲染树结构
        renderTreeList(){
            let clsxList = this.dictionaryInfoTree;
            if(clsxList.length){
                this.checkedClsxMcList = []
                clsxList.forEach(clsx => {
                    if(clsx.FDM&&this.checkedClsxList.includes(clsx.DM)){
                        this.checkedClsxMcList.push(clsx.MC)
                    }
                })
                this.evaluateForm = {
                    ...this.evaluateForm,
                    clsxmc: this.checkedClsxMcList.join(";")
                }
            }
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["CLSX"]
            }
            getDictInfo(params).then(res => {
                this.dictionaryInfoTree = res.data.dataList || [];
                this.queryEntrustByChxmid();
            })
        },
        // 评价
        evaluate(){
            if(!this.evaluateForm.fwpj){
                this.$dialog.alert({
                    message: "评分不能为空"
                })
            } else {
                this.$toast.loading({
                    message: '加载中...',
                    forbidClick: true,
                });
                this.evaluateForm.chxmid = this.chxmid;
                this.evaluateForm.chdwxxid = this.chdwxxid;
                jsdwEvaludate(this.evaluateForm).then(res => {
                    this.$toast.clear();
                    this.$router.go(-1)
                })
            }
        }
    },
}
</script>
<style scoped>
.evaluate-page {
    padding-bottom: 20px;
}
    .evaluate-title {
        height: 80px;
        line-height: 80px;
        font-size: 32px;
        padding-left: 30px;
        color: #666;
    }
    .evaluate-form >>> .van-field__value {
        padding-left: 20px;
    }
    .pj-input >>> .van-field__value {
        padding-left: calc(7em - 52px)
    }
    .evaluate-form >>> .van-rate__icon {
        font-size: 32px;
    }
    .evaluate-form >>> .van-rate {
        margin-top: 20px;
    }
    .evaluate-form >>> .van-rate__icon--full {
        color: #f5a623;
    }
    .evaluate-form >>> .van-field__word-limit {
        font-size: 28px;
        padding: 20px;
        color: #999;
    }
    .pj-pf >>> .van-cell::after {
        border: none!important;
    }
    .submit-btn {
        text-align: center;
    }
</style>