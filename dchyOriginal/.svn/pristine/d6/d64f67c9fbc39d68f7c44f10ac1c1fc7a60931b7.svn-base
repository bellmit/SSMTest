<template>
    <div class="upload-info">
        <uploadTableCommon
            :readonly="true"
            :data="uploadList" 
            :columns="columns"
            :deleteColumns="deleteColumns"
        ></uploadTableCommon>
    </div>
</template>
<script>
import _ from "loadsh"
import uploadTableCommon from "../table/upload-table-common"
import { getUploadList } from "../../service/manage";
export default {
    name: "uploadFileInfo",
    components: {
        uploadTableCommon
    },
    props: {
        ssmkid: {
            type: String,
            default: ""
        },
        needclsx: {
            type: Boolean,
            default: false
        },
        glsxid: {
            type: String,
            default: ""
        },
        clsxList: {
            type: Array,
            default: () => {
                return []
            }
        },
        deleteColumns: {
            type: Array,
            default: () => {
                return []
            }
        },
        sjclUrl: {
            type: String,
            default: "/fileoperation/getsjcl"
        },
        xmzt: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            uploadList: [],
            chooseUploadFile: [],
            columns: [
                {
                    title: "材料名称",
                    field: "CLMC",
                    width: "25%"
                },
                {
                    title: "所属测量事项",
                    field: "SSCLSXMC",
                    width: "25%"
                },
                {
                    title: "份数",
                    field: "FS",
                },
                {
                    title: "操作",
                    field: "operation",
                    type: "operation"
                }
            ]
        }
    },
    watch: {
        glsxid: function(newVal,oldVal) {
            if(newVal != oldVal){
                this.getUploadList();
            }
        },
        deleteColumns: {
            deep: true,
            handler: function(newVal,oldVal){
                let columns = _.cloneDeep(this.columns)
                if(newVal.includes("operation")&&this.columns.find(col => col.field == 'operation')){
                    columns.pop();
                    this.columns = _.cloneDeep(columns)
                }
            }
        }
    },
    mounted() {
        if(this.ssmkid != "23" && this.ssmkid != "18"){
            let columns = _.cloneDeep(this.columns)
            columns.splice(1,1)
            this.columns = _.cloneDeep(columns)
        }
         let columns = _.cloneDeep(this.columns)
        if(this.deleteColumns.includes("operation")&&this.columns.find(col => col.field == 'operation')){
            columns.pop();
            this.columns = _.cloneDeep(columns)
        }
        if(this.needclsx && this.clsxList.length){
            this.getUploadList();
        }else if(!this.needclsx&&this.glsxid){
            this.getUploadList();
        }
    },
    methods: {
        // 获取需要上传的文件
        getUploadList(){
            let params = {
                ssmkid: this.ssmkid,
                glsxid: this.glsxid,
                clsxList: this.xmzt&&this.xmzt!='1' ? null : this.clsxList
            }
            getUploadList(params,this.sjclUrl).then(res => {
                let uploadList = res.data.dataList;
                uploadList.forEach((list,index) => {
                    list.children = [];
                    list.clid = 'name'+index;
                    list.CLMC = list.SJCLPZCLMC || list.CLMC || "";
                    let clsxList = list.CLSXLIST || list.CLSXS;
                    if(clsxList){
                        list.SSCLSXMC = "";
                        let selectC = [];
                        clsxList.forEach(clsx => {
                            if(this.clsxList.includes(clsx.DM)){
                                selectC.push(clsx)
                            }
                        })
                        list.SSCLSXMC = selectC.map(s => s.MC).join('、')
                    }
                })
                this.uploadList = [...uploadList]
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