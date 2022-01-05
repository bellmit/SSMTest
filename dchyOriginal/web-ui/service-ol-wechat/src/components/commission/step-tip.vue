<template>
    <div class="step-tip">
        <div class="tip">
            <img src="static/images/message-tip1.png" style="vertical-align: middle" alt="">
            请确认委托!
        </div>
        <div class="margin-top-20">您的委托编号为<span class="blue">{{wtbh}}</span></div>
    </div>
</template>
<script>
export default {
    props: {
        wtbh: {
            type: String,
            default: ""
        }
    }
}
</script>
<style lang="less" scoped>
    .step-tip {
        text-align: center;
        padding: 100px 0;
    }
</style>