<template>
    <div class="loading-box" v-show="show">
        <div class="loading-mask"></div>
        <div class="loading-content">
            <div class="animate">
            </div>
            <div class="text">
                <img src="static/images/loading.gif" alt="">
                {{text}}
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        show: Boolean,
        text: {
          type: String,
          default: '正在加载中...'
        },
    }
}
</script>
<style lang="less" scope>
    .loading-box {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        z-index: 9999999999;
        background-color: #000;
        opacity: 0.3;
        filter:Alpha(opacity=30);
        .loading-content {
            height: 100%;
            text-align: center;
            .text {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                color: #fff;
                width: 150px;
                height: 75px;
                line-height: 75px;
                margin: auto;
                font-size: 24px;
                img {
                    vertical-align: middle;
                    width: 32px;
                }
            }
        }
    }
</style>