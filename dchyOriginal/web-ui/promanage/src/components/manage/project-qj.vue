<template>
    <div>
        <Form class="form-edit" ref="formValidate" @on-validate="validateChecked" :model="projectInfoData" inline :rules="ruleInline" :label-width='114'>
            <Row>
                <i-col span="8">
                    <FormItem v-model="projectInfoData.qjfs" label="取件方式" prop="qjfs">
                        <RadioGroup v-model="projectInfoData.qjfs">
                            <Radio :disabled="readonly" label="1">窗口取件</Radio>
                            <Radio :disabled="readonly" label="2">快递取件</Radio>
                        </RadioGroup>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem v-model="projectInfoData.yyqjsj" label="预约取件时间" prop="yyqjsj">
                       <DatePicker type="date" :disabled="readonly" placeholder="" v-model="projectInfoData.yyqjsj" class="list-form-width"></DatePicker>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem label="取件地点" prop="qjdz">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.qjdz"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="8">
                    <FormItem v-model="projectInfoData.sjrlxdh" label="联系电话" prop="sjrlxdh">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.sjrlxdh"/>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem label="收件人" prop="sjr">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.sjr"/>
                    </FormItem>
                </i-col>
                 <i-col span="8">
                    <FormItem v-model="projectInfoData.slsj" label="备案时间" prop="slsj">
                       <DatePicker type="datetime" disabled placeholder="" v-model="projectInfoData.slsj" class="list-form-width"></DatePicker>
                    </FormItem>
                </i-col>
            </Row>
             <Row>
               <FormItem v-model="projectInfoData.sjdd" label="收件地址" prop="sjdd">
                       <!-- <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.sjdd"/> -->
                        <div class="list-form-width">
                            <i-col span="8">
                                <Select :disabled='readonly' v-model="projectInfoData.sjdds" @on-select="selectGcdzs">
                                    <Option v-for="(item,index) in gcdzsList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                </Select>
                            </i-col>
                            <i-col span="8">
                                <Select :disabled="readonly" v-model="projectInfoData.sjddss" @on-select="selectGcdzss">
                                    <Option v-for="(item,index) in gcdzssList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                </Select>
                            </i-col>
                            <i-col span="8">
                                <Select :disabled="readonly" v-model="projectInfoData.sjddqx">
                                    <Option v-for="(item,index) in gcdzqxList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                </Select>
                            </i-col>
                        </div>
                        <Input style="margin-left: 5px;width: 120px" :readonly="readonly" v-model="projectInfoData.sjddxx" />
                    </FormItem>
            </Row>
        </Form>
    </div>
</template>
<script>
import { getDictInfo } from "../../service/manage"
export default {
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
            default: false
        }
    },
    data() {
        return {
            unLastSubmit: true,
            ruleInline: {
                sjrlxdh: {
                    validator: (rule,value,callback) => {
                        if(!value||!value.trim()){
                            callback()
                        } else if(value&&!(/^([0-9]{3,4}-)?[0-9]{7,8}$/).test(value)&&!(/^1\d{10}$/.test(value))){
                            callback("联系电话格式错误")
                        }
                        callback();
                    }
                }
            },
            dictionaryInfo: [],
            gcdzsList: [],
            gcdzssList: [],
            gcdzqxList: []
        }
    },
    mounted() {
        this.getDictInfo();
    },
    methods: {
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["GCDZ"]
            }
            getDictInfo(params).then(res => {
                this.dictionaryInfo = res.data.dataList;
                res.data.dataList.forEach(list => {
                    if(!list.FDM&&list.ZDLX=='GCDZ'){
                        this.gcdzsList.push(list)
                    }
                })
                if(this.projectInfoData.sjdds){
                    this.selectGcdzs({value: this.projectInfoData.sjdds})
                } 
                // else {
                //     this.projectInfoData.sjdds = this.gcdzsList.length ? this.gcdzsList[0].DM : ""
                //     this.selectGcdzs({value: this.projectInfoData.sjdds})
                // }
                if(this.projectInfoData.sjddss){
                    this.selectGcdzss({value: this.projectInfoData.sjddss})
                }
            })
        },
        // 选择省
        selectGcdzs(select){
            let gcdzssList = []
            this.dictionaryInfo.forEach(dict => {
                if((dict.FDM == select.value) && dict.ZDLX=="GCDZ"){
                    gcdzssList.push(dict)
                }
            })
            this.gcdzssList = [...gcdzssList]
            // if(!this.projectInfoData.sjddss){
            //     this.projectInfoData.sjddss = this.gcdzssList.length ? this.gcdzssList[0].DM : ""
            //     this.selectGcdzss({value: this.projectInfoData.sjddss})
            // }
        },
        // 选择市
        selectGcdzss(select){
            var gcdzqxList = [];
            this.dictionaryInfo.forEach(dict => {
                if((dict.FDM == select.value)&& dict.ZDLX=="GCDZ"){
                    gcdzqxList.push(dict)
                }
            })
            this.gcdzqxList = [...gcdzqxList]
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&!this.unLastSubmit){
                this.$error.show(error)
            }
        },
        // 校验
        validate(){
            let validate = true;
            this.unLastSubmit = false
            this.$refs["formValidate"].validate(valid => {
                this.unLastSubmit = true
                validate = valid
            })
            return validate;
        }
    },
}
</script>