<template>
    <div class="message-detail-page">
        <div class="margin-bottom-20">
            <span class="title-list"></span>
            <span class="title-tip">消息提醒详情</span>
        </div>
         <van-form ref="company-form" class="form-edit" label-align="right" label-width="8em">
            <van-field
                readonly
                name="xxsxmc"
                v-model="xxForm.xxsxmc"
                label="消息事项"
            />
            <van-field
                readonly
                name="fssj"
                v-model="xxForm.fssj"
                label="接收时间"
            />
            <van-field
                :rows="4"
                readonly
                name="xxnr"
                type="textarea"
                v-model="xxForm.xxnr"
                label="消息内容"
            />
        </van-form>
    </div>
</template>
<script>
export default {
    data() {
        return {
            xxForm: {
                xxsxmc: "",
                xxnr: "",
                fssj: ""
            }
        }
    },
    mounted() {
       this.xxForm.xxsxmc = this.$route.query.xxsxmc ? decodeURIComponent(this.$route.query.xxsxmc) : "" 
       this.xxForm.xxnr = this.$route.query.xxnr ? decodeURIComponent(this.$route.query.xxnr) : "" 
       this.xxForm.fssj = this.$route.query.fssj ? decodeURIComponent(this.$route.query.fssj) : "" 
       console.log(this.xxForm)
    },
    methods: {
        
    },
}
</script>
<style scoped>
    .message-detail-page {
        min-height: 100vh;
        background-color: #fff;
        padding: 30px;
    }
</style>