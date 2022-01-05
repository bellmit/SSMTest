<template>
    <div class="upload-info">
        <uploadTableCommon
            :data="uploadList" 
            :readonly="readonly"
            :uploadId="'attachmentUploadHtFileInput'"
            :columns="columns"
            :check="true"
            :projectInfoData="projectInfoData"
            @upload="uploadHtFile"
            @deleteFj="deleteFj"
        ></uploadTableCommon>
    </div>
</template>
<script>
import _ from "loadsh"
import uploadTableCommon from "../table/upload-table-common"
import util from "../../service/util"
import { saveUploadHtFile, deleteFile, queryHtUploadList, getZdClsx } from "../../service/manage";
export default {
    name: "uploadFile",
    components: {
        uploadTableCommon
    },
    props: {
        ssmkid: {
            type: String,
            default: ""
        },
        glsxid: {
            type: String,
            default: ""
        },
        readonly: {
            type: Boolean,
            default: false
        },
        clsxList: {
            type: Array,
            default: () => {
                return []
            }
        },
        projectInfoData: {
            type: Object,
            default: () => {
                return {}
            }
        },
        xmzt: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            uploadList: [],
            newFileForm: {
                YS: 1,
                FS: 1,
                CLLX: "2"
            },
            columns: [
                {
                    title: "材料名称",
                    field: "CLMC",
                    width: '15%'
                },
                {
                    title: "测绘阶段",
                    field: "CHJD"
                },
                {
                    title: "测量事项",
                    field: "CLSXMC"
                },
                {
                    title: "操作",
                    width: '10%',
                    field: "operation",
                    type: "operation"
                }
            ],
            chooseUploadFile: [],
            curSjxxid: "",
            allClsxList: []
        }
    },
    watch: {
        xmzt: function(newVal,oldVal){
            if((newVal != oldVal) && newVal != "1"){
                this.columns.pop();
            }
        }
    },
    mounted() {
        this.getZdClsx()
    },
    methods: {
        getZdClsx(){
            let params = {
                chxmid: this.glsxid
            }
            getZdClsx(params).then(res => {
                this.allClsxList = res.data.dataList || [];
            })
        },
        // 根据阶段生成的上传合同信息
        getUploadList(){
            let params = {
                ssmkid: this.ssmkid,
                glsxid: this.glsxid,
                clsxList: this.clsxList
            }
            queryHtUploadList(params).then(res => {
                let uploadList = [];
                res.data.dataList.forEach((list,index) => {
                    let newFile = list.length ? list[0] : {};
                    newFile.YS = newFile.YS || 1;
                    newFile.FS = newFile.FS;
                    if(newFile.CHDWXX && newFile.CHDWXX.length){
                        let chdwList = [...newFile.CHDWXX];
                        newFile.CHDWID = chdwList.map(chdw => chdw.CHDWID)
                    }
                    if(newFile.CLSXS && newFile.CLSXS.length){
                        let clsxList = [...newFile.CLSXS]
                        let find = this.allClsxList.find(c => c.DM == clsxList[0]) || {};
                        let fClsxFind = this.allClsxList.find(c => c.DM == find.FDM);
                        let clsxMc = [];
                        clsxList.forEach(clsx => {
                            let clsxFind =  this.allClsxList.find(c => c.DM == clsx) || {};
                            clsxMc.push(clsxFind.MC)
                        })
                        newFile.CLSXMC = clsxMc.join("、")
                        newFile.CHJD = fClsxFind ? fClsxFind.MC : "";
                    }
                    newFile.CLLX = newFile.CLLX || "2";
                    newFile.children = [];
                    newFile.clid = 'name'+index;
                    uploadList.push({...newFile})
                })
                this.uploadList = [...uploadList]
            })
        },
        // 校验必传材料
        validate(){
            let errorMsg = '';
            this.uploadList.forEach(upload => {
                if(upload.NEED == 1 && !upload.WJZXID){
                    errorMsg = upload.CLMC + "未上传材料"
                }
            })
            if(errorMsg){
                this.$error.show(errorMsg)
            }
            return errorMsg
        },
        // 获取uploadList
        queryUploadList(){
            return this.uploadList;
        },
        // 清空文件中心
        clearFileList(){
            this.uploadList = []
        },
        // 上传附件
        uploadHtFile(data){
            this.chooseUploadFile = [];
            this.uploadList[data.index].children = [];
            this.uploadList[data.index].FS = 0;
            for(let i=0;i < data.files.length;i++) {
                data.files[i].chxmid = this.glsxid;
                this.chooseUploadFile.push(data.files[i])
            }
            let formData = new FormData();
            for(let i = 0;i < this.chooseUploadFile.length; i++){
                formData.append('files', this.chooseUploadFile[i]);  
                this.uploadList[data.index].FS += 1;
                this.uploadList[data.index].YS = this.uploadList[data.index].FS;
            }
            let chdwid = this.uploadList[data.index].CHDWID ? this.uploadList[data.index].CHDWID.join(",") : "";
            formData.append('chxmid', this.glsxid);
            formData.append('fs', this.uploadList[data.index].FS);
            formData.append('ys', this.uploadList[data.index].YS);
            formData.append('cllx', this.uploadList[data.index].CLLX);
            formData.append('clmc', this.uploadList[data.index].CLMC);
            formData.append('htxxid', this.uploadList[data.index].HTXXID || "");
            formData.append('chdwid', chdwid || this.projectInfoData.chdwId);
            formData.append('clsx', this.uploadList[data.index].CLSXS.join(",") || "");
            formData.append('chjd', this.uploadList[data.index].CHJD || "");
            formData.append('xh', data.index);
            this.$loading.show("上传中...")
            saveUploadHtFile(formData).then(res => {
                this.$loading.close();
                if(res){
                    for(let i=0;i < data.files.length;i++) {
                        let uploadList = [...this.uploadList];
                        uploadList[data.index].children.push(data.files[i]);
                        uploadList[data.index].open = true;
                        uploadList[data.index].WJZXID = res.data.wjzxid
                        this.uploadList = [...uploadList]
                    }
                }else {
                    this.$Message.error("上传文件失败")
                }
            }).catch(err => {
                console.log(err)
            })
        },
        getWjzxid(){
            let wjzxid = this.uploadList.length ? this.uploadList[0].WJZXID : ""
            return wjzxid
        },
        // 删除上传的附件
        deleteFj(select){
            let WJZXID = select.file.WJZXID
            deleteFile({sjclId: select.file.SJCLID, wjzxid: WJZXID}).then(res => {
                // 删除文件
                let uploadList = [...this.uploadList];
                uploadList.forEach((upload,index) => {
                    if(upload.clid == select.file.clid){
                        upload.children = [];
                        upload.WJZXID = "";
                    }
                })
                this.uploadList = [...uploadList]
            })
        }
    },
}
</script>
<style scoped>
   .upload-info >>> .layui-border-box, .layui-border-box * {
       margin: 0;
   }
   .upload-info >>> td:nth-last-child(2) .layui-table-cell {
       overflow: visible;
   }
   .form-edit {
        padding: 20px;
    }
    .form-title >>> .ivu-upload {
        display: inline-block;
    }
    .mlk-top {
        display: flex;
        justify-content: space-between;
    }
</style>