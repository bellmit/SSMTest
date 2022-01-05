<template>
    <div class="commission">
        <Step
            :steps="steps"
            :active="active"
        ></Step>
        <div class="line-solid"></div>
        <div v-show="active == 0" class="clsx-content">
            <Clsx
                ref="clsx"
            ></Clsx>
        </div>
        <div v-if="active == 1" class="company-info">
            <companyInfo
                ref="company-info"
                :projectInfoData="projectInfoData"
                :clsxList="checkedClsxList"
            ></companyInfo>
        </div>
        <div v-show="active == 2" class="company-info">
            <projectInfo
                ref="project-info"
                :projectInfoData="projectInfoData"
            ></projectInfo>
        </div>
        <div v-if="active == 3" class="company-info upload-file-info">
            <uploadFile
                ref="upload-file"
                :uploadFileList="projectInfoData.clxx"
                :glsxid="chxmid"
                :sjclUrl="'/fileoperation/querySjclByClsx'"
                :uploadUrl="'/fileoperation/uploadfilestosx'"
                :clsxList="checkedClsxList"
                :ssmkid="ssmkid"
            ></uploadFile>
        </div>
        <div v-if="active == 4" class="company-info upload-file-info">
            <stepTip
                :wtbh="projectInfoData.wtbh"
            ></stepTip>
        </div>
        <div v-if="active == 5" class="company-info upload-file-info">
            <successTip
                :wtbh="projectInfoData.wtbh"
            ></successTip>
        </div>
        <div class="step-bottom-btn">
            <van-button class="step-btn" v-if="active > 0 && active < 5" @click="preStep">上一步</van-button>
            <van-button class="step-btn" v-if="active < 4" @click="nextStep">下一步</van-button>
            <van-button class="step-btn" v-if="active == 4" @click="save">确认委托</van-button>
            <!-- <van-button class="step-btn" v-if="active == 5" @click="toHome">返回办事大厅</van-button> -->
        </div>
    </div>
</template>
<script>
import Step from "../../components/step/step.vue"
import Clsx from "../../components/clsx/clsx.vue"
import projectInfo from "../../components/commission/project-info.vue"
import companyInfo from "../../components/commission/company-info.vue"
import uploadFile from "../../components/upload/upload-file.vue"
import { saveEntrust, deleteEntrust } from "../../service/commission"
import stepTip from "../../components/commission/step-tip.vue"
import successTip from "../../components/commission/success-tip.vue"
import _ from "loadsh"
export default {
    components: {
        Step,
        Clsx,
        projectInfo,
        companyInfo,
        uploadFile,
        stepTip,
        successTip
    },
    data() {
        return {
            active: 0,
            checkedClsxList: [],
            projectInfoData: {
                clsx: []
            },
            glsxid: "",
            ssmkid: "18",
            steps: [
                {
                    name: "测绘事项"
                },
                {
                    name: "单位信息"
                },
                {
                    name: "项目信息"
                },
                {
                    name: "附件上传"
                },
                {
                    name: "提交申请"
                }
            ],
            chxmid: "",
            hasCommissioned: false,
            chdwxxid: ""
        }
    },
    created() {
        this.projectInfoData.wtdw = this.$route.query.wtdw || "";
        this.projectInfoData.lxr = this.$route.query.lxr || "";
        this.projectInfoData.lxdh = this.$route.query.lxdh || "";
        this.chxmid = this.$route.query.chxmid || "";
        this.chdwxxid = this.$route.query.chdwxxid || "";
        this.projectInfoData.wtbh = this.$route.query.wtbh || "";
        this.projectInfoData.slbh = this.$route.query.slbh || "";
    },
    beforeRouteLeave (to, from, next) {
        if(!this.hasCommissioned){
            this.$dialog.confirm({
                titile: "提示",
                message: "委托未保存，是否确认退出?"
            }).then(() => {
                this.$toast.loading({
                    message: '加载中...',
                    forbidClick: true,
                });
                deleteEntrust({chxmid: this.chxmid}).then(res => {
                    this.$toast.clear();
                    next();
                })
            }).catch(() => {
                this.$dialog.close()
            })
        } else {
            next()
        }
    },
    methods: {
        preStep(){
            this.active -= 1;
        },
        nextStep(){
            if(this.active == 0){
                let clsx = this.$refs["clsx"].querySelectClsx();
                if(!clsx || !clsx.length){
                    this.$dialog.alert({
                        message: "测量事项不能为空"
                    })
                    return;
                }
                this.checkedClsxList = clsx.map(c => c.DM)
                clsx.forEach(c => {
                    this.projectInfoData.clsx.push({
                        fdm: c.FDM,
                        clsxdm: c.DM,
                        jcrq: "",
                        yjjfrq: ""
                    })
                })
                let include = false;
                this.checkedClsxList.forEach(clsx => {
                    if(clsx.startsWith("3") || clsx.startsWith("4") || clsx.startsWith("5")){
                        include = true
                    }
                })
                if(include){
                    this.$refs["project-info"].setRequired(true)
                } else {
                    this.$refs["project-info"].setRequired(false)
                }
                this.active += 1;
            } else if(this.active == 1){
                this.$refs["company-info"].validate().then(() => {
                    this.active += 1;
                }).catch(err => {
                    this.$dialog.alert({
                        message: err[0].message
                    })
                })
            } else if(this.active == 2){
                this.$refs["project-info"].validate().then(() => {
                    this.active += 1;
                }).catch(err => {
                    this.$dialog.alert({
                        message: err[0].message
                    })
                })
            }else if(this.active == 3 ){
               let message = this.$refs["upload-file"].validate()
               if(message){
                   return;
               }
               this.projectInfoData.clxx = _.cloneDeep(this.$refs["upload-file"].queryUploadList());
               this.active += 1;
            } else {
                this.active += 1;
            }
        },
        save(){
            this.projectInfoData.wtzt = "2"
            this.projectInfoData.chxmid = this.chxmid;
            this.$dialog.confirm({
                title: "提示",
                message: "是否确认委托?"
            }).then(() => {
                this.$toast.loading({
                    message: '加载中...',
                    forbidClick: true,
                });
                saveEntrust(this.projectInfoData).then(res => {
                    this.$toast.clear();
                    this.hasCommissioned = true;
                    this.active += 1;
                })
            })
        },
        toHome(){
            this.$router.push("/home")
        }
    },
}
</script>
<style lang="less" scoped>
    .commission {
        background-color: #fff;
        min-height: 100vh;
    }
    .clsx-content {
        background-color: #eaedf1;
    }
    .upload-file-info {
        padding: 0 30px;
    }
    .btn-operation {
        padding: 30px;
    }
</style>