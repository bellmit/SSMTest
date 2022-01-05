<template>
    <div class="upload-info">
        <contactTable
            ref="contract-table"
            :data="uploadList" 
            :type="'download'"
            :deleteColumns="deleteColumns"
            :treeList="selectTreeList"
        ></contactTable>
    </div>
</template>
<script>
import _ from "loadsh"
import contactTable from "../table/contact-table"
import { queryHtUploadList } from "../../service/manage";
export default {
    name: "uploadHtInfo",
    components: {
        contactTable
    },
    props: {
        ssmkid: {
            type: String,
            default: ""
        },
        mlkid: {
            type: String,
            default: ""
        },
        deleteColumns: { // 需要隐藏的列
            type: Array,
            default: () => {
                return []
            }
        },
        selectTreeList: {
            type: Array,
            default: () => {
                return []
            }
        }
    },
    data() {
        return {
            selectedFile: [],
            visible: false,
            uploadList: [],
            newFileForm: {
                YS: 1,
                FS: 1
            },
            fileRule: {},
            chooseUploadFile: [],
            deleteAll: false
        }
    },
    watch: {
        mlkid: function(newVal,oldVal) {
            if(newVal != oldVal){
                this.getUploadList();
            }
        }
    },
    mounted() {
        if(this.mlkid){
            this.getUploadList();
        }
    },
    methods: {
        // 获取需要上传的文件
        getUploadList(){
            let params = {
                ssmkid: this.ssmkid,
                glsxid: this.mlkid
            }
            queryHtUploadList(params).then(res => {
                let uploadList = res.data.dataList;
                uploadList.forEach((list,index) => {
                    list.children = [];
                    list.clid = 'name'+index;
                    if(list.CLSX && list.CLSX.length){
                        let clsxList = [...list.CLSX]
                        list.CLSX = clsxList ? clsxList.map(cl => cl.CLSXDM).join(",") : ""
                        list.CLSXMC = clsxList ? clsxList.map(cl => cl.CLSXMC).join(",") : ""   
                    }
                    if(list.CHDWXX && list.CHDWXX.length){
                        let chdwList = [...list.CHDWXX];
                        list.CHDWMC = chdwList.map(chdw => chdw.CHDWMC).join(",")
                    }
                })
                this.uploadList = [...uploadList]
            })
        },
        // 更新选中的测绘事项
        updateSelectClsx(treeList){
            let clsx = []
            let clsxMC = []
            treeList.forEach(tree => {
                tree[0].children.forEach(child => {
                    clsx.push(child.id)
                    clsxMC.push(child.title)
                })
            })
            this.uploadList.forEach(upload => {
                let clsxList = upload.CLSX ? upload.CLSX.split(",") : [];
                let clsxMCList = upload.CLSXMC ? upload.CLSXMC.split(",") : [];
                let hasClsx = []
                let hasClsxMC = []
                clsxList.forEach((cl,index) => {
                    if(clsx.includes(cl)){
                        hasClsx.push(cl)
                    }
                })
                clsxMCList.forEach((cl,index) => {
                    if(clsxMC.includes(cl)){
                        hasClsxMC.push(cl)
                    }
                })
                upload.CLSX = hasClsx.join(",")
                upload.CLSXMC = hasClsxMC.join(",")
                this.$refs["contract-table"].resetSelectedClsx(hasClsx)
            })
        },
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