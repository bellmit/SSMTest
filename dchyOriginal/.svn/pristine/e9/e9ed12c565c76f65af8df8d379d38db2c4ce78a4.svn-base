<template>
    <div>
        <Step
            :steps="steps"
            :active="active"
        ></Step>
        <div class="line-solid"></div>
        <div>
            <div class="company-info">
                <div>
                    <!-- <div class="margin-bottom-20">
                        <span class="title-list"></span>
                        <span class="title-tip">用户授权</span>
                    </div> -->
                    <div class="evaluate-title">
                        <span>用户授权</span>
                    </div>
                    <van-form validate-first :show-error-message="false" ref="company-form" v-if="active == 0" label-align="right" :label-width="'10em'">
                        <van-field
                            readonly
                            clickable
                            name="frlx"
                            v-model="companyForm.frlxmc"
                            label="法人类型"
                            placeholder="请选择法人类型"
                            @click="showPicker = true"
                            :rules="[{ required: true, message: '法人类型不能为空' }]"
                        />
                        <van-popup v-model="showPicker" position="bottom">
                            <van-picker
                                value-key="MC"
                                show-toolbar
                                :columns="frlxList"
                                @confirm="onConfirm"
                                @cancel="showPicker = false"
                            />
                        </van-popup>
                        <van-field
                            v-model="companyForm.dwmc"
                            name="dwmc"
                            label="单位名称"
                            placeholder="请输入单位名称"
                            :rules="[{ required: true, message: '单位名称不能为空' }]"
                        />
                        <van-field
                            v-model="companyForm.tyshxydm"
                            name="tyshxydm"
                            label="统一社会信用代码"
                            placeholder="请输入统一社会信用代码"
                            :rules="[{ required: true, message: '统一社会信用代码不能为空' }]"
                        />
                        <van-field
                            v-model="companyForm.frmc"
                            name="frmc"
                            label="法定代表人姓名"
                            placeholder="请输入法定代表人姓名"
                            :rules="[{ required: true, message: '法定代表人姓名不能为空' }]"
                        />
                        <van-field
                            readonly
                            clickable
                            v-model="companyForm.frzjzlmc"
                            name="frzjzl"
                            label="证件类型"
                            placeholder="请选择证件类型"
                            @click="showZjlxPicker = true"
                            :rules="[{ required: true, message: '证件类型不能为空' }]"
                        />
                        <van-popup v-model="showZjlxPicker" position="bottom">
                            <van-picker
                                value-key="MC"
                                show-toolbar
                                :columns="zjlxList"
                                @confirm="onZjlxConfirm"
                                @cancel="showZjlxPicker = false"
                            />
                        </van-popup>
                        <van-field
                            v-model="companyForm.frzjhm"
                            name="frzjzl"
                            label="法定代表人证件号码"
                            placeholder="请输入法定代表人证件号码"
                            :rules="[{ required: true, message: '法定代表人证件号码不能为空' }]"
                        />
                    </van-form>
                    <van-form v-if="active==1" validate-first :show-error-message="false" ref="user-form" label-align="right" :label-width="'10em'">
                        <van-field
                            readonly
                            v-model="companyForm.yhmc"
                            name="yhmc"
                            label="姓名"
                            placeholder="请输入单位姓名"
                            :rules="[{ required: true, message: '姓名不能为空' }]"
                        />
                        <van-field
                            readonly
                            name="yhzjzl"
                            v-model="companyForm.yhzjzlmc"
                            label="证件类型"
                            placeholder="请选择证件类型"
                            :rules="[{ required: true, message: '证件类型不能为空' }]"
                        />
                        <van-field
                            readonly
                            v-model="companyForm.yhzjhm"
                            name="yhzjhm"
                            label="证件号码"
                            placeholder="请选择证件号码"
                            :rules="[{ required: true, message: '证件号码不能为空' }]"
                        />
                    </van-form>
                    <div style="padding: 30px" v-show="active == 2">
                        <div class="step-title margin-bottom-20">请上传所需附件材料：</div>
                        <uploadFile
                            ref="upload-file"
                            :uploadAfterRead="false"
                            :glsxid="glsxid"
                            :ssmkid="ssmkid"
                        >
                        </uploadFile>
                    </div>
                </div>
                <div class="submit-btn">
                    <van-button v-if="active > 0" class="new-lg-btn" @click="preStep">上一步</van-button>
                    <van-button v-if="active < 2" class="new-lg-btn margin-top-20" @click="nextStep">下一步</van-button>
                    <van-button v-if="active == 2" class="new-lg-btn margin-top-20" @click="onSubmit" >提交</van-button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import Step from "../../components/step/step.vue"
import { gryhsqRegister, queryCertificateInfo } from "../../service/login";
import { getDictInfo } from "../../service/home";
import uploadFile from "../../components/upload/upload-file.vue"
export default {
    components: {
        Step,
        uploadFile
    },
    data() {
        return {
            companyForm: {
                frlx: "",
                dwmc: "",
                tyshxydm: "",
                frmc: "",
                frzjzl: "",
                frzjhm: "",
                yhmc: "",
                yhzjzl: "",
                yhzjhm: ""
            },
            frlxList: [],
            cllxList: [],
            zjlxList: [],
            showPicker: false,
            showZjlxPicker: false,
            uploadList: [],
            glsxid: "",
            ssmkid: '25',
            active: 0,
            ticket: "",
            steps: [
                {
                    name: "单位信息"
                },
                {
                    name: "个人信息"
                },
                {
                    name: "附件材料"
                }
            ]
        }
    },
    created() {
        this.glsxid = this.$route.query.glsxid || "";
        let self = this;
        lightAppJssdk.user.getTicket ({
            success:function(data){ //成功回调
                data = {};
                data.ticket = "cb75a63ba5a14c48ad20e3f03d52cc59"
                if(data.ticket){
                    self.ticket = data.ticket
                    self.queryCertificateInfo()
                } else {}
            },
            fail:function(data){ //错误返回
            }
        });
    },
    mounted() {
        this.getDictInfo();
    },
    methods: {
        // 获取用户信息
        queryCertificateInfo(){
            let params = {
                ticket: this.ticket
            }
            queryCertificateInfo(params).then(res => {
                this.companyForm.frlx = res.data.frxx.frlx || "";
                this.companyForm.dwmc = res.data.frxx.dwmc || "";
                this.companyForm.frmc = res.data.frxx.frmc || "";
                this.companyForm.frzjhm = res.data.frxx.frzjhm || "";
                this.companyForm.frzjzl = res.data.frxx.frzjzl || "";
                this.companyForm.tyshxydm = res.data.frxx.tyshxydm || "";
                this.companyForm.yhmc = res.data.grxx.yhmc || "";
                this.companyForm.yhzjzl = res.data.grxx.yhzjzl || "";
                this.companyForm.yhzjhm = res.data.grxx.yhzjhm || "";
                this.getYhzjzlmc();
            })
        },
        getYhzjzlmc(){
            if(this.zjlxList.length&&this.companyForm.yhzjzl){
                this.companyForm.yhzjzlmc = this.zjlxList.find(zjlx => zjlx.DM == this.companyForm.yhzjzl).MC
            }
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["CLLX","FRLX","ZJZL"]
            }
            this.zjlxList = [];
            this.cllxList = [];
            this.frlxList = [];
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(list => {
                    if(list.ZDLX == "CLLX"){
                        this.cllxList.push(list)
                    } else if(list.ZDLX == "FRLX"){
                        this.frlxList.push(list)
                    } else if(list.ZDLX == "ZJZL"){
                        this.zjlxList.push(list)
                    }
                    this.getYhzjzlmc()
                })
            })
        },
        onConfirm(select){
            this.companyForm.frlx = select.DM;
            this.companyForm.frlxmc = select.MC;
            this.showPicker = false;
        },
        onZjlxConfirm(select){
            this.companyForm.frzjzl = select.DM;
            this.companyForm.frzjzlmc = select.MC;
            this.showZjlxPicker = false
        },
        preStep(){
            this.active -= 1;
        },
        nextStep(){
            if(this.active == 0){
                this.$refs["company-form"].validate().then(() => {
                    this.active += 1;
                }).catch(err => {
                    this.$dialog.alert({
                        message: err[0].message
                    })
                })
            } else {
                this.active += 1;
            }
        },
        onSubmit(){
            let message = this.$refs["upload-file"].validate();
            if(!message){
                let formData = new FormData();
                let uploadList = this.$refs["upload-file"].queryUploadList();
                for(let i = 0;i < uploadList[0].children.length; i++){
                    formData.append('files', uploadList[0].children[i].file); 
                }
                formData.append('frlx', this.companyForm.frlx || "");
                formData.append('dwmc', this.companyForm.dwmc || "");
                formData.append('glsxid', this.glsxid || "");
                formData.append('tyshxydm', this.companyForm.tyshxydm || "");
                formData.append('frmc', this.companyForm.frmc || "");
                formData.append('frzjzl', this.companyForm.frzjzl || "");
                formData.append('frzjhm', this.companyForm.frzjhm || "");
                formData.append('yhmc', this.companyForm.yhmc || "");
                formData.append('yhzjzl', this.companyForm.yhzjzl || "");
                formData.append('yhzjhm', this.companyForm.yhzjhm || "");
                formData.append('sjclid', uploadList[0].SJCLID || "");
                formData.append('clmc', uploadList[0].CLMC);
                formData.append('cllx', uploadList[0].CLLX);
                formData.append('sjxxid', uploadList[0].SJXXID || "");
                formData.append('ssmkid', this.ssmkid);
                formData.append('ticket', this.ticket);
                formData.append('xh', 0);
                this.$toast.loading({
                    message: '授权中...',
                    forbidClick: true,
                });
                gryhsqRegister(formData,this.ticket).then(res => {
                    this.$toast.clear();
                    this.$router.push("/home")
                })
            }
        }
    },
}
</script>
<style lang="less" scoped>
    .company-info {
        background-color: #fff;
        // padding: 30px;
        min-height: calc(100vh - 147px);
        padding-bottom: 40px;
    }
    .submit-btn {
        margin-top: 60px;
        text-align: center;
    }
    .evaluate-title {
        height: 80px;
        line-height: 80px;
        font-size: 32px;
        padding-left: 30px;
        color: #666;
        background-color: #eaedf1;
    }
</style>
<style scoped>
    .company-info >>> .van-cell__value {
        padding-left: 20px;
    }
</style>