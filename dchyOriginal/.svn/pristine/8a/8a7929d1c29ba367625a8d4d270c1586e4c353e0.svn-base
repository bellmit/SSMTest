<template>
    <div class="evaluate-add">
        <div class="form-title">
            <div class="list-title">诚信记录</div>
        </div>
        <Form class="form-edit" ref="evaluate-form" @on-validate="validateChecked" :model="evaluateForm" :rules="ruleInline" :label-width="114">
            <FormItem v-model="evaluateForm.chdwmc" prop="chdwmc" label="测绘单位">
                <Input v-model="evaluateForm.chdwmc" readonly style="width: 600px"/>
            </FormItem>
            <FormItem v-model="evaluateForm.cxpj" prop="cxpj" label="诚信评价">
                <Input v-model="evaluateForm.cxpj" style="width: 600px" :rows="4" type="textarea" placeholder=""/>
            </FormItem>
        </Form>
        <div class="return-btn">
            <Button type="primary" class="bdc-major-btn btn-h-34 width-80" @click="submit">确定</Button>
            <Button class="btn-cancel btn-h-34 width-80 margin-left-20" @click="cancel">返回</Button>
        </div>
    </div>
</template>
<script>
import yz_mixins from "../../../service/yz_mixins";
import { cxEvaluation } from "../../../service/evaluate";
import _ from "lodash"
export default {
    mixins: [yz_mixins],
    data() {
        return {
            evaluateForm: {
                chdwmc: "",
                cxpj: ""
            },
            ssmkid: "",
            bdid: "",
            kpid: "",
            mlkid: "",
            ruleInline: {
                chdwmc: {
                    required: false,
                    message: "必填项不能为空"
                },
                cxpj: {
                    required: true,
                    message: "必填项不能为空"
                }
            }
        }
    },
    mounted() {
        this.evaluateForm.chdwmc = this.$route.query.chdwmc || "";
        this.mlkid = this.$route.query.mlkid || "";
    },
    methods: {
        cancel(){
            this.$router.push("/admin/evaluation")
        },
        // 提交评价
        submit(){
            this.unLastSubmit= false;
            this.$refs["evaluate-form"].validate(valid => {
                setTimeout(() => {
                    this.unLastSubmit = true;
                },500)
                if(valid){
                    this.saveData()
                }
            })
        },
        saveData(){
            this.$loading.show("加载中...")
            this.evaluateForm.mlkid = this.mlkid;
            cxEvaluation(this.evaluateForm).then(res => {
                layer.msg("评价成功")
                this.$loading.close();
                this.cancel()
            })
        }
    },
}
</script>
<style scoped>
    .evaluate-add {
        width: 100%;
        height: 100%;
        position: relative;
    }
    .form-edit {
        padding: 0!important;
        padding-top: 20px!important;
    }
    .form-edit >>> .ivu-form-item {
        margin-bottom: 30px!important;
    }
    .return-btn {
        position: absolute;
        left: 260px;
        bottom: -80px;
    }
    .evaluate-add >>> .layui-rate {
        padding: 0;    
    }
</style>