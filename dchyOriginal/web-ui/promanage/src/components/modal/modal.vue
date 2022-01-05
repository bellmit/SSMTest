<template>
    <div v-if='show' id='modal-contanier'>
        <div class='modal-content' :style='`width: ${width}px;height: ${height}px`'>
            <div class='modal-header layui-layer-title'>
                {{title}}
            </div>
            <div class='modal-body layui-layer-content'>
                <slot></slot>
            </div>
            <span @click='close()' class='layui-layer-setwin'>
                <a href="javascript:void(0)" class="layui-layer-ico layui-layer-close layui-layer-close1"></a>
            </span>
            <div class="modal-footer-btn layui-layer-btn layui-layer-btn-c">
                <button v-for='(btn,index) in btns' :key='index' type='button' :class="btn.class" @click='btn.action'>{{btn.text}}</button>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props: {
        title: { //title: 弹出框标题;
            type: String,
            default: '提示'
        },
        btns: { // btns: 弹出框操作 
            type: Array,
            default: () => {
                return [
                    {
                        text: '确认',
                        class: 'main-btn-a',
                        action: () => {
                        }
                    },
                    {
                        text: '取消',
                        class: 'layui-btn layui-btn-primary margin-left-10',
                        action: () => {
                        }
                    }
                ]
            }
        },
        show: { //show: 控制弹出框显示
            type: Boolean,
            default: false
        },
        width: { //弹出框宽度
            type: Number,
            default: 1000
        },
        height: { //弹出框高度
            type: Number,
            default: 600
        }
    }, 
    methods: {
        save(){
            this.$emit('save')
        },
        close(){
            this.$emit('close')
        }
    },
}
</script>
<style lang="less" scoped>
    #modal-contanier {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        background:rgba(0,0,0,.3) ;
        z-index: 150;
        display: flex;
        justify-content: space-around;
        align-items: center;
        .modal-content {
            background-color: #fff;
            position: relative;
            .modal-body {
                padding: 0 10px;
                height: 500px;
                overflow: auto;
            }
            .modal-footer-btn {
                padding-top: 10px;
            }
        }
    }
</style>