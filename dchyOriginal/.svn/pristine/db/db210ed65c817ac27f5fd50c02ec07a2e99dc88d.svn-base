<template>
    <div class="step-tip">
        <div class="tip">
            <img src="static/images/success.png" style="vertical-align: middle" alt="">
            委托成功!
        </div>
        <div class="margin-top-20">您的委托编号为<span class="blue">{{wtbh}}</span></div>
        <div class="margin-top-20">委托单位核验备案后，可在“<span @click="toHome" class="blue">个人中心</span>”我的项目中查看</div>
    </div>
</template>
<script>
export default {
    props: {
        wtbh: {
            type: String,
            default: ""
        }
    },
    methods: {
        toHome(){
            this.$router.push("/mine")
        }
    },
}
</script>
<style lang="less" scoped>
    .step-tip {
        text-align: center;
        padding: 100px 0;
    }
</style>