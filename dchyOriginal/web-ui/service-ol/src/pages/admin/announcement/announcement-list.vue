<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline"  :label-width="120" :rules="ruleInline" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="标题" prop="bt" class="form-list-search">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getAnnounceList(1,formInline.size)" v-model="formInline.bt" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="类型" prop="gglx" class="form-list-search">
                            <Select v-model="formInline.gglx" clearable class="form-search-item">
                                <Option v-for="item in typeList" :value="item.DM" :key="item.DM">{{ item.MC }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="20">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getAnnounceList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="announce-table">
            <Table
                :cols="tableCols" 
                :data="announceList" 
                :size="formInline.size" 
                :page="formInline.page" 
                :count="totalNum"
                :operation="operationList"
                :func="getAnnounceList"
                :tool="tool"
                @delete="deleteAnnounceent"
                @view="viewDetail"
                @edit="editAnnounce"
                @btn1="addAnnouncement"
            ></Table>
        </div>
    </div>
</template>
<script>
import util from '../../../service/util'
import { getAnnouncementList, initAnnouncement, deleteListAnnounce } from "../../../service/announcement"
import { getDictInfo } from "../../../service/mlk"
export default {
    data() {
        return {
            ruleInline: {},
            formInline: {
                bt: "",
                gglx: "",
                page: 1,
                size: 10
            },
            typeList: [],
            operationList: ["view","delete","edit"],
            totalNum: 0,
            announceList: [],
            tool: '<div>' +
                    '<span class="layui-btn main-btn-a" lay-event="btn1">发布新公告</span>' +
                '</div>',
            tableCols: [
                {
                    title: '序号',
                    align: "center",
                    width: 70,
                    field: "ROWNUM_",
                    fixed: "left"
                },
                {
                    field: "BT",
                    title: '标题',
                    align: "center"
                },
                {
                    field: "GGLX",
                    title: '类型',
                    align: "center",
                },
                {
                    field: "FBR",
                    title: '发布人',
                    align: "center",
                },
                {
                    field: "FBSJ",
                    title: '发布时间',
                    align: "center",
                },
                {
                    title: '操作',
                    align: "center",
                    toolbar: '#operation',
                }
            ]
        }
    },
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/admin/announcement/add")&&to.query.type!="add"){
            this.pageInfo["announcePageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["announcePageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["announcePageInfo"]){
            this.formInline = {...this.pageInfo["announcePageInfo"]}
        }
    },
    mounted() {
        this.getTypeList();
        this.getAnnounceList();
    },
    methods: {
        // 获取公告类型字典项
        getTypeList(){
            this.typeList.push({
                DM: "",
                MC: "全部"
            })
            getDictInfo({zdlx: ["GGLX"]}).then(res => {
                this.typeList = res.data.dataList;
            })
        },
        // 查看
        viewDetail(data){
            this.$router.push({path: "/admin/announcement/add", query: {tzggid: data.TZGGID,type:"view"}})
        },
        // 变价
        editAnnounce(data){
            this.$router.push({path: "/admin/announcement/add", query: {tzggid: data.TZGGID,type:"edit"}})
        },
        // 删除
        deleteAnnounceent(select){
            layer.confirm("确认删除?",(index) => {
                let params = {
                    tzggid: select.TZGGID
                }
                this.$loading.show("删除中...")
                deleteListAnnounce(params).then(res => {
                    this.$loading.close();
                    this.getAnnounceList();
                })
                layer.close(index);
            })
        },
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 获取公告list
        getAnnounceList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...")
            getAnnouncementList(this.formInline).then(res => {
                this.$loading.close();
                this.announceList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        // 新增
        addAnnouncement(){
            initAnnouncement().then(res => {
                this.$router.push({path: "/admin/announcement/add", query: {tzggid: res.tzggid,type:"add"}})
            })
        }
    },
}
</script>
<style lang="less" scoped>
    @import "./announcement.less";
</style>