<template>
    <div class="form-edit">
        <div v-for="(upload,index) in uploadList" :key="upload.CLMC+index">
            <div class="margin-bottom-20">
                <span class="title-list"></span>
                <span class="title-tip"><span v-if="upload.NEED==1" class="required-star">*</span>{{upload.CLMC}}</span>
            </div>
            <div @click="clickUpload(upload,index)">
                <van-uploader 
                    :multiple="true" 
                    v-model="upload.children"
                    :after-read="afterRead" 
                    @delete="deleteFile(upload,index)"
                    upload-icon="plus"
                />
            </div>
        </div>
    </div>
</template>
<script>
import { getUploadList, saveUploadFile, deleteFile } from "../../service/login"
import _ from "loadsh"
export default {
    props: {
        ssmkid: { // 所属模块id
            type: String,
            default: ""
        }, 
        glsxid: { // 关联事项id
            type: String,
            default: ""
        },
        sjclUrl: { // 获取收件材料的url
            type: String,
            default: "/fileoperation/getsjcl"
        },
        uploadAfterRead: { // 是否直接上传
            type: Boolean,
            default: true
        },
        uploadUrl: { // 上传材料的url
            type: String,
            default: "/fileoperation/uploadfiles"
        },
        clsxList: {
            type: Array,
            default: () => {
                return []
            }
        },
        uploadFileList: {
            type: Array,
            default: () => {
                return []
            }
        },
    },
    data() {
        return {
            uploadList: [],
            selectUpload: {},
            selectIndex: 0,
            chooseFileList: [],
            chooseUploadFile: []
        }
    },
    mounted() {
        if(!this.uploadFileList.length){
            this.getUploadList()
        } else {
            this.uploadList = _.cloneDeep(this.uploadFileList)
        }
    },
    methods: {
        validate(){
            let errorMsg = '';
            this.uploadList.forEach(upload => {
                if(upload.NEED == 1 && !upload.children.length){
                    errorMsg = upload.CLMC + "未上传材料"
                }
            })
            if(errorMsg){
                this.$dialog.alert({
                    message: errorMsg
                })
            }
            return errorMsg
        },
        getUploadList(){
            let params = {
                ssmkid: this.ssmkid,
                glsxid: this.glsxid,
                clsxList: this.clsxList
            }
            getUploadList(params,this.sjclUrl).then(res => {
                res.data.dataList.forEach(list => {
                    list.children = []
                })
                this.uploadList = res.data.dataList || [];
            })
        },
        queryUploadList(){
            return this.uploadList
        },
        clickUpload(upload,index){
            this.selectUpload = upload
            this.selectIndex = index
        },
        afterRead(file){
            if(this.uploadAfterRead){
                this.chooseUploadFile = [];
                this.uploadList[this.selectIndex] = this.uploadList[this.selectIndex] ? this.uploadList[this.selectIndex] : {};
                this.uploadList[this.selectIndex].children = [];
                this.uploadList[this.selectIndex].FS = 0;
                if(Array.isArray(file)){
                    for(let i=0;i < file.length;i++) {
                        file[i].glsxid = this.glsxid;
                        this.chooseUploadFile.push(file[i].file)
                    }
                } else {
                    this.chooseUploadFile.push(file.file)
                }
                let formData = new FormData();
                for(let i = 0;i < this.chooseUploadFile.length; i++){
                    console.log(this.chooseUploadFile[i])
                    formData.append('files', this.chooseUploadFile[i]);  
                    this.uploadList[this.selectIndex].FS += 1;
                    this.uploadList[this.selectIndex].YS = this.uploadList[this.selectIndex].FS;
                }
                formData.append('sjclid', this.uploadList[this.selectIndex].SJCLID || "");
                formData.append('glsxid', this.glsxid);
                formData.append('fs', this.uploadList[this.selectIndex].FS);
                formData.append('ys', this.uploadList[this.selectIndex].YS);
                formData.append('cllx', this.uploadList[this.selectIndex].CLLX);
                formData.append('clmc', this.uploadList[this.selectIndex].CLMC);
                formData.append('sjxxid', this.uploadList[this.selectIndex].SJXXID || "");
                formData.append('sjclpzid', this.uploadList[this.selectIndex].SJCLPZID || "");
                formData.append('ssmkid', this.ssmkid);
                let ssclsx = [];
                this.uploadList[this.selectIndex].CLSXLIST&&this.uploadList[this.selectIndex].CLSXLIST.forEach(clsx => {
                    if(this.clsxList.includes(clsx.DM)){
                        ssclsx.push(clsx.DM)
                    }
                });
                formData.append('ssclsx', ssclsx.join(",") || "");
                formData.append('xh', this.selectIndex);
                this.$toast.loading({
                    message: '上传中...',
                    forbidClick: true,
                });
                saveUploadFile(formData,this.uploadUrl).then(res => {
                    this.$toast.clear();
                    if(res){
                        let uploadList = _.cloneDeep(this.uploadList);
                        uploadList[this.selectIndex].children = Array.isArray(file) ? file : [file]
                        uploadList[this.selectIndex].WJZXID = res.data.wjzxid 
                        this.uploadList = _.cloneDeep(uploadList)
                    } else {
                        this.$dialog.alert({
                            message: "上传失败"
                        })
                    }
                }).catch(err => {
                    console.log(err)
                })
            } else {
                this.uploadList[this.selectIndex].children = Array.isArray(file) ? file : [file]
            }
        },
        deleteFile(file,index){
            if(this.uploadAfterRead){
                this.$toast.loading({
                    message: '删除中...',
                    forbidClick: true,
                });
                let WJZXID = file.WJZXID
                deleteFile({sjclId: file.SJCLID, wjzxid: WJZXID}).then(res => {
                    this.$toast.clear();
                    // 删除文件
                    let uploadList = [...this.uploadList];
                    uploadList.forEach((upload,index) => {
                        if(file.SJCLID == upload.SJCLID){
                            upload.children = [];
                            upload.WJZXID = "";
                        }
                    })
                    this.uploadList = [...uploadList]
                })
            } else {
                return true;
            }
        }
    },
}
</script>