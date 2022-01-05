<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="项目代码 " class="form-list-search" prop="gcxmbh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.gcxmbh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="工程名称 " class="form-list-search" prop="gcxmmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.gcxmmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="委托编号 " class="form-list-search" prop="wtbh">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.wtbh" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="测绘单位 "  class="form-list-search" prop="chdwmc">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getEntrustList(1,formInline.pageSize)" v-model="formInline.chdwmc" placeholder=""/>
                        </FormItem>
                    </i-col>
                </Row>
                <Row>
                    <i-col span="6">
                        <FormItem label="当前状态 " class="form-list-search" prop="wtzt">
                            <Select v-model="formInline.wtzt" clearable class="form-search-item">
                                <Option v-for="item in wtztList" :value="item.DM" :key="item.MC">{{ item.MC }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='50' class="form-list-search">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getEntrustList(1,formInline.pageSize)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <!-- <div class="ch-tip margin-top-10 margin-bottom-10">
            <img src="static/images/kp-tip.png" style="vertical-align: middle" alt=""> 
            <span class="font-size-16">您当前进行中测绘业务：</span>
            <span class="font-color-tip">{{count}}</span>
        </div> -->
        <Table
            :cols="tableCols"
            :data="myEntrustList"
            :count="totalNum"
            :page="formInline.page"
            :size="formInline.pageSize"
            :tool="tool"
            :func="getEntrustList"
            :operation="operationList"
            @view="viewDetail"
            @delete="deleteItem"
            @btn1="addWt"
            @deleteOpr="deleteOpr"
        ></Table>
    </div>
</template>
<script>
import { initEntrust,getEntrustByPage,getDictInfo,deleteEntrust } from "../../../service/commission"
export default {
    data() {
        return {
            formInline: {
                gcbh: "",
                gcmc: "",
                wtbh: "",
                chdwmc: "",
                wtzt: "",
                page: 1,
                pageSize: 10
            },
            tool: '<div>' +
                    '<span class="layui-btn main-btn-a" lay-event="btn1">新增委托</span>' +
                '</div>',
            count: 0,
            ruleInline: {},
            wtztList: [],
            dictionaryInfo: [],
            tableCols: [
                {
                    field: "ROWNUM_",
                    width: 70,
                    align: "center",
                    title: "序号",
                    fixed: "left"
                },
                {
                    field: "XQFBBH",
                    align: "center",
                    title: "委托编号"
                },
                {
                    field: "GCBH",
                    align: "center",
                    title: "项目代码"
                },
                {
                    field: "GCMC",
                    align: "center",
                    title: "工程名称"
                },
                {
                    field: "CHDWMC",
                    align: "center",
                    title: "测绘单位"
                },
                {
                    field: "FBSJ",
                    align: "center",
                    title: "委托时间"
                },
                {
                    field: "WTZT",
                    align: "center",
                    title: "状态",
                    templet: function(d){
                        let className= d.WTZT==="已办结" ? "color-finish" :d.WTZT === "已备案"?"color-processing": "color-unfinish"
                        return "<span class='"+className+"'>"+d.WTZT+"</span>"
                    }
                },
                {
                    align: "center",
                    title: "操作",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ],
            operationList: ["view","delete"],
            myEntrustList: [],
            totalNum: 0
        }
    },
    // 进入路由时获取上一次的页面信息
    beforeRouteLeave (to, from, next) {
        if(to.fullPath.startsWith("/construction/commission/add")&&to.query.type!="add"){
            this.pageInfo["commissionPageInfo"] = {...this.formInline}
        } else {
            this.pageInfo["commissionPageInfo"] = null
        }
        next()
    },
    created() {
        if(this.pageInfo["commissionPageInfo"]){
            this.formInline = {...this.pageInfo["commissionPageInfo"]}
        }
    },
    mounted() {
        this.getEntrustList();
        this.getDictInfo();
    },  
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["WTZT"]
            }
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(list => {
                    //去除重新备案、已退回状态
                    if(list.ZDLX == "WTZT" && list.MC !="重新备案" &&list.MC !="已退回" && list.MC != "待备案"){
                        this.wtztList.push(list)
                    }
                })
                this.wtztList.unshift({
                    DM: "",
                    MC: "全部"
                })
            })
        },
        // 获取委托列表
       getEntrustList(page,pageSize){
           if(page){
               this.formInline.page = page;
               this.formInline.pageSize = pageSize;
           }
           this.$loading.show("加载中...")
           getEntrustByPage(this.formInline).then(res => {
               this.$loading.close()
               this.totalNum = res.data.totalNum;
               this.myEntrustList = res.data.dataList;
           })
       },
        // 新增委托
        addWt(){
            initEntrust().then(res => {
                this.$router.push({
                path: `/construction/commission/add`,
                query: { chxmid: res.data.chxmid, wtbh: res.data.wtbh, wtdw: res.data.wtdw, lxr: res.data.lxr, lxdh: res.data.lxdh, type:"add" } 
                });
            })
        },
        // 查看详情
        viewDetail(data){
            if(data.WTZT === "待委托"){
                this.$router.push({
                    path: `/construction/commission/add`,
                    query: { chxmid: data.CHXMID, chdwxxid: data.CHDWXXID,type:"edit",wtzt: data.WTZT}
                });
            } else if(data.WTZT === "待接受"){
                this.$router.push({
                    path: `/construction/commission/add`,
                    query: { chxmid: data.CHXMID, chdwxxid: data.CHDWXXID,type:"retrieve",wtzt: data.WTZT}
                });
            } else {
                this.$router.push({
                    path: `/construction/commission/add`,
                    query: { chxmid: data.CHXMID, chdwxxid: data.CHDWXXID,wtzt: data.WTZT,type:"view",wtzt: data.WTZT}
                });
            }
        },
        // 删除委托
        deleteItem(data){
            if(data.WTZT != "待委托"){
                return
            }
            layer.confirm("确认删除?",(index) => {
                layer.close(index)
                this.$loading.show("删除中...")
                deleteEntrust({chxmid: data.CHXMID}).then(res => {
					layer.msg("删除成功")
                    this.$loading.close();
                    this.getEntrustList()
                })
            })
        },
        //重置
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                pageSize: this.formInline.pageSize
            }
        },
        // 删除操作
        deleteOpr(data){
            if(this.myEntrustList && this.myEntrustList.length){
                this.myEntrustList.forEach((list, index) => {
                if (list.WTZT != "待委托" ) {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='delete']").addClass("table-btn-disabled cursor-not-allowed");
                } 
            });
            }
        },
    },
}
</script>
<style scoped>
</style>