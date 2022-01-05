<template>
    <div class="form-edit" style="width: 100%">
        <Form ref="formValidate" :model="agencyInfoData" inline :rules="ruleInline" :label-width='128'>
            <Row>
                <i-col span="12">
                    <FormItem label="测绘单位名称">
                        <Input :readonly="readonly" v-model="agencyInfoData.dwmc"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="统一社会信用代码">
                        <Input :readonly="readonly" v-model="agencyInfoData.tyshxydm"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <FormItem label="法人代表">
                        <Input :readonly="readonly" v-model="agencyInfoData.frdb"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="测绘资质等级">
                        <Input readonly v-model="agencyInfoData.zzdj"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <FormItem label="联系人">
                        <Input :readonly="readonly" v-model="agencyInfoData.lxr"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="联系电话">
                        <Input :readonly="readonly" v-model="agencyInfoData.lxdh"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <FormItem label="录入人">
                        <Input :readonly="readonly" v-model="agencyInfoData.lrr"/>
                    </FormItem>
                </i-col>
                <i-col span="12">
                    <FormItem label="录入时间">
                       <Input :readonly="readonly" v-model="agencyInfoData.lrsj"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="12">
                    <FormItem label="办公地址">
                        <Input :readonly="readonly" v-model="agencyInfoData.bgdz"/>
                    </FormItem>
                </i-col>
            </Row>
        </Form> 
    </div>
</template>
<script>
import util from "../../service/util" 
export default {
    name: "AgencyInfo",
    props: {
        agencyInfoData: { // 基本信息
            type: Object,
            default: function(){
                return {
                    dwmc: ""
                }
            }
        },
        readonly: { //当前表单是只读状态还是编辑状态
            type: Boolean,
            default: true
        },
    },
    data() {
        return {
            showCyry: true,
            ksOptions: {},
            jsOptions: {},
            dwxzList: [],
            zzdjList: [],
            ruleInline: {
            }
        }
    }
}
</script>
<style scoped>
    .form-edit {
        padding: 0 80px;
    }
</style>