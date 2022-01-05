<template>
    <div class="form-edit" >
        <Form ref="formValidate" @on-validate="validateChecked" :model="projectInfoData" inline :rules="ruleInline" :label-width='114'>
            <Row>
                <i-col span="8">
                    <FormItem label="委托单位" prop="wtdw">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.wtdw"/>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem label="联系人" prop="lxr">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.lxr"/>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem label="联系电话" prop="lxdh">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.lxdh"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="16">
                    <div class="chdw-item">
                        <FormItem label="测绘单位" v-if="queryChdw" class="requireStar" :prop="chdwMultiChoose?'chdw':'chdwId'">
                            <Select v-if="chdwMultiChoose" :disabled="readonly" v-model="projectInfoData.chdw" clearable filterable @on-change="changeChdw" multiple :class="readonly||!showChdwManualEntry?'':'chdw-lr'">
                                <Option v-for="item in chdwList" :key="item.CHDWID+item.CHDWMC" :value="item.CHDWID">{{item.CHDWMC}}</Option>
                            </Select>
                            <Select v-else :disabled="readonly" v-model="projectInfoData.chdwId" clearable filterable @on-change="changeSingleChdw" :class="readonly||!showChdwManualEntry?'':'chdw-lr'">
                                <Option v-for="item in chdwList" :key="item.CHDWID+item.CHDWMC" :value="item.CHDWID">{{item.CHDWMC}}</Option>
                            </Select>
                            <Button class="btn-h-32 btn-cancel margin-left-10 float-right" v-if="!readonly&&showChdwManualEntry" :disabled="readonly" @click="manualInput">手动录入</Button>
                        </FormItem>
                    </div>
                </i-col>
                <i-col span="8">
                    <FormItem label="测绘总时限" v-if="queryChdw" prop="chzsx">
                        <Input :readonly="readonly" class="list-form-width" v-model="projectInfoData.chzsx"/>
                        <span class="form-unit">天</span>
                    </FormItem>
                </i-col>
            </Row>
        </Form> 
        <Modal
            class="modal-base"
            v-model="visible"
            :title="'录入测绘机构'"
            width="800"
            :mask-closable="false"
            :footer-hide="true"
            closable
        >
            <Form class="form-edit modal-form" ref="lrch-form" @on-validate="validateChecked" :model="companyForm" :rules="companyInline" :label-width="114">
                <Row>
                    <i-col span="12">
                        <FormItem v-model="companyForm.chdwmc" label="测绘机构" prop="chdwmc">
                            <Input v-model="companyForm.chdwmc"/>
                        </FormItem>
                    </i-col>
                    <i-col span="12">
                        <FormItem v-model="companyForm.tyshxydm" label="统一社会信用代码" prop="tyshxydm">
                            <Input v-model="companyForm.tyshxydm"/>
                        </FormItem>
                    </i-col>
                </Row>
                <Row>
                    <i-col span="12">
                        <FormItem v-model="companyForm.frdb" label="法人代表" prop="frdb">
                            <Input v-model="companyForm.frdb"/>
                        </FormItem>
                    </i-col>
                    <i-col span="12">
                        <FormItem v-model="companyForm.zzdj" label="测绘资质等级" prop="zzdj">
                            <Select v-model="companyForm.zzdj">
                                <Option v-for="(item,index) in zzdjList" :key="index" :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                </Row>
                <Row>
                    <i-col span="12">
                        <FormItem v-model="companyForm.lxr" label="联系人" prop="lxr">
                            <Input v-model="companyForm.lxr"/>
                        </FormItem>
                    </i-col>
                    <i-col span="12">
                        <FormItem v-model="companyForm.lxdh" label="联系电话" prop="lxdh">
                            <Input v-model="companyForm.lxdh"/>
                        </FormItem>
                    </i-col>
                </Row>
                <Row>
                    <FormItem v-model="companyForm.bgdzs" label="办公地址" class="requireStar">
                        <!-- <Input v-model="companyForm.dwdz"/> -->
                        <i-col span="5">
                            <Select v-model="companyForm.bgdzs" @on-select="selectGcdzs">
                                <Option v-for="item in gcdzsList" :key='item.DM' :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </i-col>
                        <i-col span="5">
                            <Select v-model="companyForm.bgdzss" @on-select="selectGcdzss">
                                <Option v-for="item in gcdzssList" :key='item.DM' :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </i-col>
                        <i-col span="5">
                            <Select v-model="companyForm.bgdzqx">
                                <Option v-for="item in gcdzqxList" :key='item.DM' :value="item.DM">{{item.MC}}</Option>
                            </Select>
                        </i-col>
                        <i-col span="9">
                            <Input style="margin-left: 5px;" v-model="companyForm.bgdzxx" />
                        </i-col>
                    </FormItem>
                </Row>
            </Form>
            <div class="submit-back" >
                <Button type="primary" class="btn-h-34 bdc-major-btn" @click="saveMlk()">确认</Button>
                <Button class="btn-h-34 btn-cancel margin-left-10" @click="cancel()">取消</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import { getDictInfo, saveChdw, queryChdwList } from "../../service/manage"
import _ from "lodash"
export default {
    name: "AgencyInfo",
    props: {
        projectInfoData: { //基本信息
            type: Object,
            default: function(){
                return {
                    dwmc: ""
                }
            }
        },
        allChdwList: {
            type: Array,
            default: function(){
                return []
            }
        },
        queryChdw: { 
            type: Boolean,
            default: true
        },
        readonly: { //是否只读
            type: Boolean,
            default: false
        }
    },
    watch: {
        allChdwList: {
            deep: true,
            handler: function(newVal,oldVal){
                if(newVal&&newVal.length){
                    this.chdwList = _.cloneDeep(newVal)
                }
            }
        }
    },
    data() {
        return {
            companyForm: {},
            chdwMultiChoose: config.chdwMultiChoose,
            showChdwManualEntry: config.showChdwManualEntry,
            companyInline: {
                chdwmc: {
                    required: true,
                    message: "必填项不能为空"
                },
                tyshxydm: {
                    required: true,
                    message: "必填项不能为空"
                },
                frdb: {
                    required: true,
                    message: "必填项不能为空"
                },
                zzdj: {
                    required: true,
                    message: "必填项不能为空"
                },
                lxr: {
                    required: true,
                    message: "必填项不能为空"
                },
                lxdh: {
                    required: true,
                    type: 'number',
                    trigger: "blur",
                    validator: (rule,value,callback) => {
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        } else if(value&&!(/^([0-9]{3,4}-)?[0-9]{7,8}$/).test(value)&&!(/^1\d{10}$/.test(value))){
                            callback("联系电话格式错误")
                        }
                        callback();
                    }
                }
            },
            zzdjList: [],
            ruleInline: {
                wtdw: {
                    required: true,
                    message: "必填项不能为空",
                    trigger: "blur",
                },
                lxr: {
                    required: true,
                    message: "必填项不能为空",
                    trigger: "blur",
                },
                chdw: {
                    required: true,
                    message: "必填项不能为空",
                },
                chdwId: {
                    required: true,
                    message: "必填项不能为空",
                },
                chzsx: {
                    required: false,
                    trigger: "blur",
                    validator: (rule,value,callback) => {
                        if(value&&!/^[0-9]*$/.test(value)){
                            callback("测绘总时限必须为数字")
                        }else {
                            callback()
                        }
                        callback()
                    }
                },
                lxdh: {
                    required: true,
                    type: 'number',
                    trigger: "blur",
                    validator: (rule,value,callback) => {
                        if(!value||!value.trim()){
                            callback("必填项不能为空")
                        } else if(value&&!(/^([0-9]{3,4}-)?[0-9]{7,8}$/).test(value)&&!(/^1\d{10}$/.test(value))){
                            callback("联系电话格式错误")
                        }
                        callback();
                    }
                }
            },
            visible: false,
            dictionaryInfo: [],
            gcdzsList: [],
            gcdzssList: [],
            gcdzqxList: [],
            unLastSubmit: true,
            chdwList: [],
            testChdw: ["4CFM5015P96JU31F"]
        }
    },
    mounted() {
        this.getDictInfo();
        if(this.queryChdw){
            this.queryChdwList();
        }
    },
    methods: {
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["GCDZ","ZZDJ"]
            }
            getDictInfo(params).then(res => {
                this.dictionaryInfo = res.data.dataList;
                res.data.dataList.forEach(list => {
                    if(!list.FDM&&list.ZDLX=='GCDZ'){
                        this.gcdzsList.push(list)
                    }
                    if(list.ZDLX=="ZZDJ"){
                        this.zzdjList.push(list)
                    }
                })
                // if(!this.companyForm.bgdzs){
                //     this.companyForm.bgdzs = this.gcdzsList.length ? this.gcdzsList[0].DM : ""
                //     this.selectGcdzs({value: this.companyForm.bgdzs})
                // }
            })
        },
        // 获取测绘单位
        queryChdwList(){
            let params = {
                clsxList: []
            }
            queryChdwList(params).then(res => {
                this.chdwList = res.data.dataList;
                if(this.projectInfoData.chdwId){
                    this.changeSingleChdw(this.projectInfoData.chdwId)
                }
            })
        },
        // 修改多选选中的测绘单位
        changeChdw(select){
            let chdw = []
            this.chdwList.forEach(c => {
                if(select.includes(c.CHDWID)){
                    chdw.push({chdwmc: c.CHDWMC,chdwid: c.CHDWID,dwlx: c.DWLX})
                }
            })
            this.$emit("selectChdw",chdw)
        },
        // 修改单选选中的测绘单位
        changeSingleChdw(select){
            let chdw = []
            this.chdwList.forEach(c => {
                if(select === c.CHDWID){
                    chdw.push({chdwmc: c.CHDWMC,chdwid: c.CHDWID,dwlx: c.DWLX})
                }
            })
            this.projectInfoData.chdwId = select;
            this.$emit("selectChdw",chdw)
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&!this.unLastSubmit){
                this.$error.show(error)
            }
        },
        // 手动录入测绘单位
        saveMlk(){
            this.unLastSubmit = false;
            this.$refs["lrch-form"].validate(valid => {
                setTimeout(() => {
                    this.unLastSubmit = true
                },500)
                if(valid){
                    if(!this.companyForm.bgdzs || !this.companyForm.bgdzss || !this.companyForm.bgdzqx || !this.companyForm.bgdzxx){
                        this.$error.show("请输入完整的办公地址")
                        return;
                    }
                    this.$loading.show("录入中...")
                    saveChdw(this.companyForm).then(res => {
                        this.$loading.close();
                        this.$refs["lrch-form"].resetFields();
                        this.companyForm.bgdzqx = "";
                        this.companyForm.bgdzxx = "";
                        this.visible = false;
                        this.queryChdwList();
                    })
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
            // if(!this.companyForm.bgdzss){
            //     this.companyForm.bgdzss = this.gcdzssList.length ? this.gcdzssList[0].DM : ""
            //     this.selectGcdzss({value: this.companyForm.bgdzss})
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
        // 校验
        validate(){
            let validate = true;
            this.unLastSubmit = false
            this.$refs["formValidate"].validate(valid => {
                setTimeout(() => {
                    this.unLastSubmit = true;
                },500)
                validate = valid
            })
            return validate;
        },
        manualInput(){
            this.visible = true;
        },
        // 取消录入
        cancel(){
            this.visible = false;
            this.$refs["lrch-form"].resetFields();
            this.companyForm.bgdzqx = "";
            this.companyForm.bgdzxx = "";
        }
    },
}
</script>
<style scoped>
    .modal-form {
        margin-top: 20px;
        padding: 0 20px!important;
    }
    .chdw-lr {
        width: 84%;
    }
    .chdw-item {
        width: calc( 50% + 414px);
    }
    @media (max-width: 1600px) {
        .chdw-lr {
            width: 80%;
        }
        .chdw-item {
            width: calc( 50% + 314px);
        }
    }
</style>