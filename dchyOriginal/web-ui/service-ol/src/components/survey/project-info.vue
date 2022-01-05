<template>
    <div class="form-edit" style="width: 100%">
        <Form ref="formValidate" @on-validate="validateChecked" :model="projectInfoData" inline :rules="ruleInline" :label-width='142'>
            <Row>
                <i-col span="12">
                    <FormItem label="建设单位名称 " prop="wtdw">
                        <Tooltip v-if="projectInfoData.wtdw" style="width: 100%" :content="projectInfoData.wtdw">
                            <Input :readonly="readonly" v-model="projectInfoData.wtdw"/>
                        </Tooltip>
                        <Input v-else :readonly="readonly" v-model="projectInfoData.wtdw"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="统一社会信用代码 " prop="tyshxydm">
                        <Tooltip v-if="projectInfoData.tyshxydm" style="width: 100%" :content="projectInfoData.tyshxydm" >
                            <Input :readonly="readonly" v-model="projectInfoData.tyshxydm"/>
                        </Tooltip>
                        <Input v-else :readonly="readonly" v-model="projectInfoData.tyshxydm"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <FormItem label="联系人 " prop="lxr">
                        <Input :readonly="readonly" v-model="projectInfoData.lxr"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="联系电话 " prop="lxdh">
                        <Input :readonly="readonly" v-model="projectInfoData.lxdh"/>
                    </FormItem>
                </i-col>
            </Row>
        </Form>
    </div>
</template>
<script>
export default {
    name: "AgencyInfo",
    props: {
        projectInfoData: {
            type: Object,
            default: function(){
                return {
                    dwmc: ""
                }
            }
        },
        readonly: {
            type: Boolean,
            default: true
        }
    },
    data() {
        return {
            ruleInline: {
                dwmc: [
                    {
                        required: true,
                        message: '不能为空'
                    }
                ]
            }
        }
    },
    methods: {
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error){
                this.$Message.error(error)
            }
        },
        validate(){
            let validate = true;
            this.$refs["formValidate"].validate(valid => {
                validate = valid
            })
            return validate;
        }
    },
}
</script>
<style scoped>
    .form-edit >>> .ivu-form-item {
        width: 100%;
        height: 42px;
        line-height: 42px;
        margin-bottom: 0;
    }
</style>
