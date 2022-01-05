<template>
    <div>
        <div class="form-title">
            <div class="list-title">我要咨询</div>
        </div>
        <Form class="form-edit" @on-validate="validateChecked" ref="advice-form" :model="newAdvice" :rules="newRules">
            <FormItem label="主题" prop="title"  :label-width="140">
                <Input type="text" v-model="newAdvice.title" placeholder=""/>
            </FormItem>
            <FormItem label="正文" prop="content"  :label-width="140">
                <Input type="textarea" :rows="10" v-model="newAdvice.content" placeholder=""/>
            </FormItem>
        </Form>
        <div class="submit-btn save-btn">
            <Button type="primary" class="btn-h-34 bdc-major-btn" @click="submit()">提交</Button>
            <Button class="btn-h-34 btn-cancel margin-left-10" @click="cancel">返回</Button>
        </div>
    </div>
</template>
<script>
import { addNewAdvice, deleteAdviceSqxx } from "../../../service/advice"
export default {
    data() {
        return {
            error: "",
            newAdvice: {
                title: "",
                content: ""
            },
            newRules: {
                title: {
                    required: true,
                    message: "必填项不能为空"
                },
                content: {
                    required: true,
                    message: "必填项不能为空"
                }
            },
            unLastSubmit: true,
            issuesid: ""
        }
    },
    mounted() {
        this.issuesid = this.$route.query.issuesid
    },
    methods: {
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&this.error!=error&&!this.unLastSubmit){
                this.error = error
                this.$error.show(error);
                setTimeout(() => {
                    this.error = ""
                    this.$error.close();
                },1000)
            }
        },
        // 提交留言
        submit(){
            this.unLastSubmit = false;
            this.$refs["advice-form"].validate(valid => {
                this.unLastSubmit = true;
                if(valid){
                    let params = {
                        ...this.newAdvice,
                        issuesid: this.issuesid
                    }
                    this.$loading.show("加载中...")
                    addNewAdvice(params).then(res => {
                        this.$loading.close();
                        layer.msg("提交成功")
                        this.$router.go(-1)
                    })
                }
            })
        },
        // 取消
        cancel(){
            let params = {
                issuesid: this.issuesid
            }
            this.$loading.show("加载中...")
            deleteAdviceSqxx(params).then(res => {
                this.$loading.close();
                this.$router.go(-1)
            })
        }
    },
}
</script>
<style scoped>
    .form-title {
        display: flex;
        justify-content: space-between;
    }
    .submit-btn {
        margin-top: 100px
    }
</style>