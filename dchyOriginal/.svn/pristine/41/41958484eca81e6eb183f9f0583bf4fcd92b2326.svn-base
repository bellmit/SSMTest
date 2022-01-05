<template>
    <div>
        <van-steps class="customize-step" :active="active">
            <van-step v-for="(step,index) in steps" :key="index">{{step.name}}</van-step>
        </van-steps>
    </div>
</template>
<script>
export default {
    props: {
        steps: {
            type: Array,
            default: () => {
                return []
            }
        },
        active: {
            type: Number,
            default: 0
        }
    },
    data() {
        return {
        }
    },
}
</script>
<style scoped>
    .customize-step {
        padding: 30px 60px;
    }
    .customize-step >>>  .van-step--horizontal .van-step__circle-container,
    .customize-step >>> .van-step--horizontal .van-step__line {
        top: -20px;
        padding: 0;
    }
    .customize-step >>>  .van-step--horizontal:first-child .van-step__title{
        transform: translateX(-40px);
    }
    .customize-step >>>   .van-step--horizontal:last-child .van-step__title {
        transform: translateX(40px);
    }
    .customize-step >>> .van-steps__items {
        padding-top: 30px;
        padding-bottom: 0;
        margin: 0;
    }
    .customize-step >>> .van-step--horizontal .van-step__title ,
    .customize-step >>> .van-step__icon--active{
        font-size: 28px;
        color: #333;
    }
    .customize-step >>> .van-step__icon--active {
        color: #1d87d1;
        border: 8px solid #a2d0f1;
    }
    .customize-step >>> .van-icon-checked {
        width: 28px;
        height: 28px;
        border-radius: 50%;
        background-color: #1d87d1;
    }
    .customize-step >>> .van-step__circle,
    .customize-step >>> .van-step--horizontal .van-step__line  {
        background-color: #d0d5da;
    }
    .customize-step >>> .van-step--finish .van-step__circle,
    .customize-step >>> .van-step--finish .van-step__line {
        background-color: #1d87d1;
    }
    .customize-step >>> .van-icon-checked::before {
        content: '';
    }
    .customize-step >>> .van-step__circle {
        width: 28px;
        height: 28px;
    }
</style>