<template>
    <transition name = "fade">
        <div class="error-box" v-show="show">
            <div class="error-content">
                <div class="text">
                    <Icon class="icon-tip" type="ios-alert-outline" />
                    {{text}}
                </div>
            </div>
        </div>
    </transition>
</template>

<script>
export default {
    props: {
        show: Boolean,
        text: {
          type: String,
          default: '必填项不能为空'
        },
    }
}
</script>
<style lang="less" scope>
    .error-box {
        z-index: 9999999999;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        .error-content {
            border: 1px solid #dcdee2;
            padding: 0 10px;
            padding-right: 30px;
            min-width: 180px;
            max-width: 560px;
            height: 70px;
            line-height: 70px;
            text-align: center;
            .icon-tip {
                color: #1d87d1;
                font-size: 42px;
                vertical-align: middle;
                font-weight: bold;
            }
            background-color: #fff;
        }
    }
</style>