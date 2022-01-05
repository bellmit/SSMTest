<template>
    <div class="evaluate-add">
        <div class="form-title">
            <div class="list-title">满意度</div>
        </div>
        <Form class="form-edit " :model="evaluateForm" ref="evaluate-form" @on-validate="validateChecked" :rules="ruleInline" :label-width="114">
            <FormItem v-model="evaluateForm.gcmc" prop="gcmc" label="工程名称">
                <Input style="width: 600px" v-model="evaluateForm.gcmc" readonly/>
            </FormItem>
            <FormItem v-model="evaluateForm.chdwmc" prop="chdwmc" label="测绘单位">
                <Input style="width: 600px" v-model="evaluateForm.chdwmc" readonly/>
            </FormItem>
            <FormItem v-model="evaluateForm.fwpj" prop="fwpj" label="满意度">
                <RadioGroup v-model="evaluateForm.fwpj">
                    <Radio :disabled="readonly" :label="5">非常满意</Radio>
                    <Radio :disabled="readonly" :label="4">满意</Radio>
                    <Radio :disabled="readonly" :label="3">一般</Radio>
                    <Radio :disabled="readonly" :label="2">较差</Radio>
                    <Radio :disabled="readonly" :label="1">差</Radio>
                </RadioGroup>
            </FormItem>
            <FormItem v-model="evaluateForm.pjyj" prop="pjyj" label="评价意见">
                <Input :readonly="readonly" v-model="evaluateForm.pjyj" style="width: 600px" :rows="4" type="textarea" placeholder=""/>
            </FormItem>
            <Row>
                <FormItem prop="clsx" label="测绘事项 ">
                    <Input readonly v-model="evaluateForm.clsxmc"  style="width: 600px" />
                </FormItem>
            </Row>
        </Form>
        <div class="return-btn">
            <Button type="primary" class="bdc-major-btn btn-h-34 width-80" v-if="!readonly" @click="submit">确定</Button>
            <Button class="btn-cancel btn-h-34 width-80 margin-left-20" @click="cancel">返回</Button>
        </div>
    </div>
</template>
<script>
import { jsdwEvaludate, queryJsdwEvaluateDetail } from "../../../service/evaluate"
import { queryJsdwWtxx } from "../../../service/myproject"
import { getDictInfo } from "../../../service/mlk"
import yz_mixins from "../../../service/yz_mixins"
import _ from "loadsh"
export default {
    mixins: [yz_mixins],
    data() {
        return {
            evaluateForm: {
                gcmc: "",
                chdwmc: "",
                fwpj: "",
                pjyj: ""
            },
            ssmkid: "",
            bdid: "",
            ruleInline: {
                fwpj: {
                    required: true,
                    message: "必填项不能为空"
                }
            },
            chxmid: "",
            readonly: false,
            dictionaryInfoTree: [],
            treeList: [],
            checkedClsxList: [],
            checkedClsxMcList: []
        }
    },
    mounted() {
        if(this.$route.query.chxmid){
            this.chxmid = this.$route.query.chxmid  
        }
        this.chdwxxid = this.$route.query.chdwxxid;
        if(this.$route.query.gcmc){
            this.evaluateForm.gcmc = this.$route.query.gcmc
            this.evaluateForm.chdwmc = this.$route.query.chdwmc
        }
        if(this.$route.query.type == "view"){
             this.getDetail();
            this.readonly = true
        }
        this.getDictInfo();
    },
    methods: {
        // 获取项目详情
        queryEntrustByChxmid(){
            let params = {
                chxmid: this.chxmid,
                chdwxxid: this.chdwxxid
            }
            this.$loading.show("加载中...")
            queryJsdwWtxx(params).then(res => {
                this.$loading.close();
                let xmInfo = res.data.dataList&&res.data.dataList.length ? res.data.dataList[0] : {}
                this.checkedClsxList = xmInfo.CLSX.split(",") || [];
                this.renderTreeList();
            })
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["CLSX"]
            }
            getDictInfo(params).then(res => {
                this.dictionaryInfoTree = res.data.dataList || [];
                this.queryEntrustByChxmid();
            })
        },
        // 渲染树结构
        renderTreeList(){
            let clsxList = this.dictionaryInfoTree;
            if(clsxList.length){
                this.checkedClsxMcList = []
                clsxList.forEach(clsx => {
                    if(clsx.FDM&&this.checkedClsxList.includes(clsx.DM)){
                        this.checkedClsxMcList.push(clsx.MC)
                    }
                })
                this.evaluateForm = {
                    ...this.evaluateForm,
                    clsxmc: this.checkedClsxMcList.join(";")
                }
            }
        },
        // 获取基本信息
        getDetail(){
            let params = {
                chxmid: this.chxmid,
                chdwxxid: this.chdwxxid
            }
            queryJsdwEvaluateDetail(params).then(res => {
                let info = res.data.dataList&&res.data.dataList.length?res.data.dataList[0]:{}
                for(let key in info){
                    info[_.toLower(key)] = info[key]
                }
                this.evaluateForm = {
                    clsxmc: this.evaluateForm.clsxmc,
                    ...info
                }
            })
        },
        cancel(){
            this.$router.push("/construction/evaluate")
        },
        // 确认评价
        submit(){
            this.unLastSubmit= false;
            this.$refs["evaluate-form"].validate(valid => {
                this.unLastSubmit = true;
                if(valid){
                    // this.yzYwlj(this.evaluateForm, this.saveData)
                    this.saveData();
                }
            })
        },
        // 确认评价
        saveData(){
            this.$loading.show("加载中...")
            this.evaluateForm.chxmid = this.chxmid;
            this.evaluateForm.chdwxxid = this.chdwxxid;
            jsdwEvaludate(this.evaluateForm).then(res => {
                layer.msg("评价成功")
                this.$loading.close();
                this.cancel();
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
    .form-content {
        margin: 20px auto;
    }
    .form-edit {
        padding: 0 200px 0 0!important;
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
    .stage-tree {
        width: 100%;
        padding: 5px;
        overflow-x: auto;
        background-color: #e8f3fa;
    }
</style>