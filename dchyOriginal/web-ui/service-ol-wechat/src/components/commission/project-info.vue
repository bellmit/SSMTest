<template>
    <div class="company-info">
        <van-form validate-first ref="company-form" :show-error-message="false" label-align="right" label-width="8em">
            <van-field
                name="frlx"
                v-model="projectInfoData.gcmc"
                label="工程名称"
                :class="isRequired?'requireStar':''"
                placeholder="请输入工程名称"
                :rules="[{ required: isRequired, message: '工程名称不能为空' }]"
            />
            <van-field
                @blur="checkGcbh"
                v-model="projectInfoData.gcbh"
                name="gcbh"
                :class="isRequired?'requireStar':''"
                label="项目代码"
                placeholder="填写示例: 2019-320412-78-01-570112"
                :rules="[{ required: isRequired, message: '项目代码不能为空' }]"
            />
            <van-field
                readonly
                clickable
                name="gcdz"
                v-model="fieldValue"
                class="requireStar"
                label="工程地点"
                placeholder="请选择省市区"
                @click="showGcdzPicker = true"
                :rules="[{ required: true, message: '工程地点不能为空' }]"
            />
            <van-popup v-model="showGcdzPicker" position="bottom">
                <van-cascader
                    v-model="fieldValue"
                    title="请选择省市区"
                    :options="gcdzOptions"
                    active-color="#1d87d1"
                    @close="showGcdzPicker = false"
                    @finish="onFinish"
                />
            </van-popup>
            <van-field
                v-model="projectInfoData.gcdzxx"
                name="gcdzxx"
                class="requireStar"
                label="详细地点"
                placeholder="请输入详细地点"
                :rules="[{ required: true, message: '详细地点不能为空' }]"
            />
            <van-field
                v-model="projectInfoData.ys"
                name="ys"
                label="预算"
                placeholder="请输入预算"
            >
                <template #right-icon>
                    <div class="dw">
                        元
                    </div>
                </template>
            </van-field>
        </van-form>
    </div>
</template>
<script>
import { getDictInfo } from "../../service/home"
import { checkGcbh } from "../../service/commission"
import _ from "loadsh"
export default {
    props: {
        projectInfoData: {
            type: Object,
            default: () => {
                return {}
            }
        }
    },
    data() {
        return {
            gcdzOptions: [],
            fieldValue: "",
            isRequired: false,
            showGcdzPicker: false
        }
    },
    created() {
        this.getDictInfo()  
    },
    methods: {
        setRequired(required){
            this.isRequired = required
        },
        validate(){
            return this.$refs["company-form"].validate()
        },
        checkGcbh(){
            checkGcbh({gcbh: this.projectInfoData.gcbh}).then(res => {
                this.projectInfoData.gcmc = res.data.gcmc || this.projectInfoData.gcmc;
                this.projectInfoData.gcdzqx = res.data.gcdzqx || this.projectInfoData.gcdzqx;
                this.projectInfoData.gcdzs = res.data.gcdzs || this.projectInfoData.gcdzs;
                this.projectInfoData.gcdzss = res.data.gcdzss || this.projectInfoData.gcdzss;
                this.projectInfoData.gcdzxx = res.data.gcdzxx || this.projectInfoData.gcdzxx;
                this.fieldValue = this.getFieldValue()
            })
        },
        getFieldValue(){
            let value = "";
            let find = this.gcdzOptions.find(gcdzs => gcdzs.value == this.projectInfoData.gcdzs)
            if(find){
                value = find.text
                let findChild = find.children.find(gcdzss => gcdzss.value == this.projectInfoData.gcdzss)
                if(findChild){
                    value += "/" + findChild.text
                    let findLast = findChild.children.find(gcdzqx => gcdzqx.value == this.projectInfoData.gcdzqx)
                    if(findLast){
                        value += "/" + findLast.text
                    }
                }
            }
            return value
        },
        getDictInfo(){
            let params = {
                zdlx: ["GCDZ"]
            }
            let gcdzOptions = []
            getDictInfo(params).then(res => {
                res.data.dataList&&res.data.dataList.forEach(list => {
                    if(!list.FDM&&list.ZDLX=="GCDZ"){
                        gcdzOptions.push({
                            text: list.MC,
                            value: list.DM,
                            children: []
                        })
                    }
                })
                res.data.dataList&&res.data.dataList.forEach(list => {
                    if(list.FDM&&list.ZDLX=="GCDZ"){
                        let findIndex = gcdzOptions.findIndex(gcdz => gcdz.value == list.FDM)
                        if(findIndex != -1){
                            gcdzOptions[findIndex].children.push({
                                text: list.MC,
                                value: list.DM,
                                children: []
                            })
                        }
                    }
                })
                res.data.dataList&&res.data.dataList.forEach(list => {
                    if(list.FDM&&list.ZDLX=="GCDZ"){
                        gcdzOptions.forEach(options => {
                            let findIndex = options.children.findIndex(gcdz => gcdz.value == list.FDM)
                            if(findIndex != -1){
                                options.children[findIndex].children.push({
                                    text: list.MC,
                                    value: list.DM
                                })
                            }
                        })
                    }
                })
                this.gcdzOptions = _.cloneDeep(gcdzOptions)
            })
        },
        onFinish({ selectedOptions }) {
            this.showGcdzPicker = false;
            this.fieldValue = selectedOptions.map((option) => option.text).join('/');
            this.projectInfoData.gcdzs = selectedOptions[0].value;
            this.projectInfoData.gcdzss = selectedOptions[1].value;
            this.projectInfoData.gcdzqx = selectedOptions[2].value;
        },
    },
}
</script>
<style scoped>
    .dw {
        position: absolute;
        top: 0;
        right: 300px;
        color: #333;
    }
    .company-info >>> .van-cell__value {
        padding-left: 20px;
    }
</style>