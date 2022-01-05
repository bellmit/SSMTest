<template>
    <div class="table">
        <div class="layui-table-tool"  style="border: 1px solid #e6e6e6;border-bottom: none">
            <span class="layui-btn main-btn-a" v-if="readonly" @click="editable">编辑</span>
            <span class="layui-btn main-btn-a" v-else @click="save">保存</span>
            <span class="layui-btn main-btn-a" v-if="!readonly" @click="addNew">新增</span>
            <span class="layui-btn main-btn-a bdc-delete-btn margin-left-10" v-if="!readonly" @click="deleteFile">删除</span>
        </div>
        <table class="file-data-table">
            <thead>
                <tr>
                    <th data-field="attachmentName" v-if="!readonly">
                        <div>
                            <Checkbox @on-change="checkAll" v-model="allCheck"></Checkbox>
                        </div>
                    </th>
                    <th data-field="attachmentTypeIndex">
                        <span>序号</span>
                    </th>
                    <th data-field="attachmentName">
                        <span>材料名称</span>
                    </th>
                    <th data-field="attachmentSSCLSX" v-if="!deleteColumns.includes('所属测量事项')">
                       <span>所属测量事项</span>
                    </th>
                    <th data-field="attachmentType">
                        <span>材料类型</span>
                    </th>
                    <th data-field="attachmentCount">
                        <span>材料份数</span>
                    </th>
                     <th data-field="attachmentBt">
                        <span>是否必传</span>
                    </th>
                </tr>
            </thead>
            <tbody id="fileTableData" class="form-edit" >
                <template v-for="(item,index) in data">
                    <tr data-index="N" class="data-type-zone" :key="index">
                        <td data-field="attachmentName" v-if="!readonly">
                            <div>
                                <Checkbox v-model="item.checked" @on-change="checkOne(item,index)"></Checkbox>
                            </div>
                        </td>
                        <td data-field="attachmentTypeIndex" style="width: 15%" align="center">
                            <div class="layui-table-cell laytable-cell-5-attachmentTypeIndex">
                                <span>{{index+1}}</span>
                            </div>
                        </td>
                        <td data-field="attachmentName" style="width: 20%" align="center">
                            <div v-if="readonly" :title="item.CLMC">{{item.CLMC}}</div>
                            <Input v-else type="text" v-model="item.CLMC" />
                        </td>
                        <td data-field="attachmentSSCLSX" v-if="!deleteColumns.includes('所属测量事项')" align="center">
                            <div class="layui-table-cell laytable-cell-5-attachmentPageSize">
                                <Select v-model="item.SSCLSXID" multiple @on-change="selectClsxChange(item,index)" filterable :disabled="readonly">
                                    <Option v-for="(opt) in SSCLSXList" :key="opt.DM" :value="opt.DM">{{opt.MC}}</Option>
                                </Select>
                            </div>
                        </td>
                        <td data-field="attachmentType" align="center">
                            <div class="layui-table-cell laytable-cell-5-attachmentPageSize">
                                <Select @on-change="selectChange(item,index)" :disabled="readonly" v-model="item.CLLXID">
                                    <Option v-for="(opt,j) in typeHtmlList" :key="j" :value="opt.value">{{opt.label}}</Option>
                                </Select>
                            </div>
                        </td>
                        <td data-field="attachmentCount" style="width: 10%" align="center">
                            <div class="layui-table-cell laytable-cell-5-attachmentCount">
                                <span v-if="readonly">{{item.MRFS}}</span>
                                <Input v-else v-model="item.MRFS"/>
                            </div>
                        </td>
                        <td data-field="attachmentBt" align="center">
                            <div class="layui-table-cell laytable-cell-5-attachmentPageSize">
                                <i-switch @on-change="changeNeed(item,index)" :disabled="readonly" v-model="item.SFBT" />
                            </div>
                        </td>
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>

<script>
import _ from "lodash";
export default {
    name: "uploadTable",
    props: {
        data: { //表数据
            type: Array,
            default: () => {
                return []
            }
        },
        SSCLSXList: {
            type: Array,
            default: () => {
                return []
            }
        },
        deleteColumns: { // 需要隐藏的列
            type: Array,
            default: () => {
                return []
            }
        },
        type: {
            type: String,
            default: 'upload'
        }
    },
    data() {
        return {
            table: "",
            laypage: "",
            form: "",
            isEdit: false,
            curUploadId: "",
            allCheck: false,
            readonly: true,
            typeHtmlList: [
                {
                    value: "1",
                    label: "原件正本"
                },
                {
                    value: "2",
                    label: "正本复印件"
                },
                {
                    value: "3",
                    label: "原件副本"
                },
                {
                    value: "4",
                    label: "副本复印件"
                },
                {
                    value: "5",
                    label: "其它"
                }
            ]
        }
    },
    methods: {
        save() {
            this.$emit("save")
        },
        setReadonly(){
            this.readonly = true;
        },
        editable(){
            this.readonly = false;
        },
        addNew(){
            this.$emit("tooladd")
        },
        deleteFile(){
            this.$emit("tooldelete")
        },
        changeNeed(item,index){
            item.index = index;
            this.$emit("required",item)
        },
        selectChange(select,index){
            select.index = index;
            this.$emit("select",select)
        },
        selectClsxChange(select,index){
            select.index = index;
            this.$emit("select-clsx",select)
        },
        checkAll(select){
            this.$emit("selectall",select)
        },
        checkOne(item,index){
            item.index = index;
            this.$emit("radio",item)
        }
    },
}
</script>
<style scoped>
    .table >>> .layui-table-body {
        overflow: visible;
    }
    .table >>> .layui-table-box {
        overflow: visible;
    }
    .table >>> .ivu-select-input {
        text-align: center;
    }
    .table >>> .ivu-select-single .ivu-select-selection {
        height: 30px;
    }
    #operation > span {
        display: inline-block;
    }
    .telescopic-sign-right i{
        padding-left: 5px;
        font-size:150%;
        position: relative;
        top: 2px;
    }
    .telescopic-sign-down i{
        padding-left: 5px;
        font-size:150%;
        position: relative;
        top:2px;
    }
    .form-edit {
        padding: 0!important;
    }
    .laytable-cell-5-attachmentName  {
        overflow: hidden;
        max-width: 260px;
        margin: 0 auto;
    }
    thead tr th {
        height: 30px;
        line-height: 30px;
    }
    .layui-table-cell, .layui-table-tool-panel li {
        overflow: visible;
    }
    .layui-table-cell >>> .ivu-select-input {
        height: 30px!important;
        line-height: 30px!important;
    }
</style>