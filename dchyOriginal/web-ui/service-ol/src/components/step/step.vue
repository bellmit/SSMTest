<template>
    <ul class='process-nav'>
        <li v-for='(step,index) in steps' :key='index' 
            :style="'width: '+(100/steps.length)+'%; z-index: ' + (steps.length - index)"
            :class="current==(index+1)?'process-nav-doing':current>(index+1)?'process-nav-done':''">
            <span class="process-name" :title='step.stepName'>{{step.orderNumber}}、{{step.stepName}}</span>
            <div v-if='index<steps.length-1' class="process-img">
                <div :class="current==(index+1)?'process-img-bg process-img-doing-bg':current>index?'process-img-bg process-img-done-bg':'process-img-bg'"></div>
                <img src="static/images/jiantou.png" alt="">
            </div>
        </li>
    </ul>
</template>
<script>
export default {
    props: {
        steps: {
            type: Array,
            default: () => []
        },
        current: {
            type: Number,
            default: 1
        }
    }
}
</script>
<style lang="less" scoped>
    .process-nav {
        width: 100%;
        box-sizing: border-box;
        padding: 0;
        margin: 0;
        background: #D0D5DA;
        position: relative;
        line-height: 38px;
        height: 38px;
        >li {
            float: left;
            min-width: 150px;
            display: table;
            background-color: #D0D5DA;
            height: 38px;
            line-height: 38px;
            color: #666;
            position: relative;
        }
        .process-nav-doing {
            background-color: #169BD5;
            color: #fff;
        }
        .process-name {
            padding-left: 30px;
            position: absolute;
            top: 0;
            font-size: 16px;
        }
        .process-img {
            z-index: 100;
            position: absolute;
            right: -20px;
            width: 20px;
            height: 36px;
            vertical-align: middle;
            .process-img-doing-bg {
                border-color: transparent transparent transparent #169BD5 !important;
            }
            .process-img-bg {
                position: absolute;
                border-width: 19px 0 19px 19px;
                width: 0;
                height: 0;
                border-color: transparent transparent transparent #D0D5DA;
                border-style: solid;
            }
            .process-img-done-bg {
                border-color: transparent transparent transparent #1675B8 !important;
            }
        }
    }
    .process-nav .process-nav-done {
        background-color: #1675B8;
        color: #fff;
    }
</style>