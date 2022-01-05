<template>
    <div class="application-view">
        <Tabs @on-click="changeTabs" v-model="current">
            <TabPane label="未读" name="wd">
                <div class="search-form">
             <Form ref="formInlineWd" :model="formInlineWd" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="10">
                        <FormItem label="接收时间 " class="form-list-search" prop="jssj" >
                            <Row class="form-list-search">
                                <i-col span="11">
                                    <DatePicker class="form-search-item" @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="formInlineWd.jssjq" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                                <i-col span="1">
                                    <div style="text-align:center">-</div>
                                </i-col>
                                <i-col span="12">
                                    <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="formInlineWd.jssjz" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                            </Row>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="20">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getMessageList(1)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm(1)">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
                </div>
                <Table 
                    :id="'wdTableId'"
                    :pageId="'wdPageId'"
                    :cols="wdCols" 
                    :data="wdList" 
                    :count="wdTotal" 
                    :tool="tool"
                    :operation="operationList"
                    :page="formInlineWd.page"
                    :size="formInlineWd.pageSize"
                    :func="getMessageList"
                    @read="read"
                    @check="selectData"
                    @btn1="readMore"
                    @link="linkToUrl"
                ></Table>
            </TabPane>
            <TabPane label="已读" name="yd">
                <div class="search-form">
                    <Form ref="formInlineYd" :model="formInlineYd" :rules="ruleInline" :label-width="120" inline>
                        <Row>
                            <i-col span="10">
                                <FormItem class="form-list-search" label="接收时间 " prop="jssj">
                                    <Row class="form-list-search">
                                        <i-col span="11">
                                            <DatePicker class="form-search-item" @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="formInlineYd.jssjq" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                        <i-col span="1">
                                            <div style="text-align:center">-</div>
                                        </i-col>
                                        <i-col span="12">
                                            <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="formInlineYd.jssjz" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                    </Row>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem :label-width="20">
                                    <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getMessageList(1)">查询</Button>
                                    <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm(1)">重置</Button>
                                </FormItem>
                            </i-col>
                        </Row>
                    </Form>
                </div>
                <Table 
                    v-if="current == 'yd'"
                    :id="'ydTableId'"
                    :operationId="'ydTableOperationId'"
                    :pageId="'ydPageId'"
                    :cols="ydCols" 
                    :data="ydList" 
                    :count="ydTotal" 
                    :page="formInlineYd.page"         
                    :size="formInlineYd.pageSize"
                    :func="getMessageList"
                    @read="read"
                    @link="linkToUrl"
                ></Table>
            </TabPane>
        </Tabs>
    </div>
</template>
<script>
import moment from "moment";
import { getMessageList,changeStatus } from "../../../service/message"

export default {
    data() {
        const _self = this;
        return {
            ksOptions: {},
            jsOptions: {},
            formInlineWd: {
                jssjq: "",
                jssjz: "",
                dqzt: "0",
                page: 1,
                pageSize: 10
            },
            tool: '<div>' +
            '<span class="layui-btn main-btn-a" lay-event="btn1">批量已读</span>' +
            '</div>',
            formInlineYd: {
                jssjq: "",
                jssjz: "",
                dqzt: "1",
                page: 1,
                pageSize: 10
            },
            operationList:["read"],
            current: "wd",
            level: "",
            ruleInline: {},
            wdList: [],
            wdTotal: 0,
            wdCols: [
                {
                    type:'checkbox',
                    fixed: "left"
                },
                {
                    field: "ROWNUM_",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "XXNR",
                    title: "消息内容",
                    align: "center",
                    templet: _self.addLink,
                    width: 630
                },
                {
                    field: "XXSXMC",
                    title: "消息事项",
                    align: "center"
                },
                {
                    field: "FSSJ",
                    title: "接收时间",
                    align: "center"
                },
                {
                    align: "center",
                    title: "操作",
                    toolbar: "#operation",
                    width: 120,
                }
            ],
            ydList: [],
            ydTotal: 0,
            ydCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    width: 70,
                    align: "center",
                    fixed: "left"
                },
                {
                    field: "XXNR",
                    title: "消息内容",
                    align: "center",
                    templet: _self.addLink,
                    width: 630
                },
                {
                    field: "XXSXMC",
                    title: "消息事项",
                    align: "center"
                },
                {
                    field: "FSSJ",
                    title: "接收时间",
                    align: "center"
                },
            ],
            yhxxidlist:[],
            selectedList: [],
        }
    },
    created() {
        // if(this.$route.path == "/construction/message"){
        //     this.ydCols.splice(this.ydCols.length-1,1)
        // }
    },
    mounted() {
       this.getMessageList();
    },
    methods: {
        // 重置查询表单
        resetForm(){
            if(this.current == "wd"){
                this.formInlineWd = {
                    page: this.formInlineWd.page,
                    pageSize: this.formInlineWd.pageSize
                }
            } else {
                this.formInlineYd = {
                    page: this.formInlineYd.page,
                    pageSize: this.formInlineYd.pageSize
                }
            }
        },
        getMessageList(page,size){
            if(this.current == "wd"){
                if(page){
                    this.formInlineWd.page = page
                    this.formInlineWd.pageSize = size || this.formInlineWd.pageSize 
                }
                this.formInlineWd.jssjq = this.formInlineWd.jssjq ? moment(this.formInlineWd.jssjq).format("YYYY-MM-DD") : "";
                this.formInlineWd.jssjz = this.formInlineWd.jssjz ? moment(this.formInlineWd.jssjz).format("YYYY-MM-DD") : "";
                this.$loading.show("加载中...")
                getMessageList(this.formInlineWd).then(res => {
                    this.$loading.close()
                    this.wdList = res.data.dataList;
                    this.wdTotal = res.data.totalNum;
                })
            }else if(this.current == 'yd'){
                if(page){
                    this.formInlineYd.page = page
                    this.formInlineYd.pageSize = size || this.formInlineYd.pageSize 
                }
                this.formInlineYd.jssjq = this.formInlineYd.jssjq ? moment(this.formInlineYd.jssjq).format("YYYY-MM-DD") : "";
                this.formInlineYd.jssjz = this.formInlineYd.jssjz ? moment(this.formInlineYd.jssjz).format("YYYY-MM-DD") : "";
                this.$loading.show("加载中...")
                getMessageList(this.formInlineYd).then(res => {
                    this.$loading.close()
                    this.ydList = res.data.dataList;
                    this.ydTotal = res.data.totalNum;
                })
                
            }
        },
        // 控制结束时间可选状态
        kssjChange(select){
            this.jsOptions.disabledDate = (date) => {
                return date && (moment(date).unix() < moment(select).unix());
            }
        },
        // 控制开始时间可选状态
        jssjChange(select){
            this.ksOptions.disabledDate = (date) => {
                return date && (moment(date).unix() > moment(select).unix());
            }
        },
        read(data){
            let params ={
                yhxxidlist: [data.YHXXID]
            }
            this.$loading.show("加载中...")
            changeStatus(params).then(res => {
                this.$loading.close()
                layer.msg("已阅读")
                this.getMessageList();
            })
        },
        selectData(data){
            this.selectedList = [...data]
        },
        //批量已读
        readMore(){
            if(!this.selectedList.length){
                layer.msg("至少选择一条数据")
                return;
            }
            layer.confirm("确认已读?", (index) => {
                layer.close(index)
                let params = {
                    yhxxidlist: this.selectedList.map(list => { return  list.YHXXID })
                }
                this.$loading.show("加载中...")
                changeStatus(params).then(res => {
                    layer.msg("已阅读")
                    this.getMessageList();
                })
            })
        },
        changeTabs(current){
            this.current = current;
            this.getMessageList(1);
        },
        linkToUrl(data){
            let query = {from: '/survey/message'}
            if(data && data.TZURL){
                if(data.DQZT == "0"){
                    let params ={
                        yhxxidlist: [data.YHXXID]
                    }
                    changeStatus(params).then(res => {
                        if(data.TZURL.startsWith("/survey/mlkapply")){
                            this.$router.push({path: "/survey/application",query})
                        } else {
                            this.$router.push({path: data.TZURL, query})
                        }
                    })
                }else if(data.DQZT == "1"){
                    if(data.TZURL.startsWith("/survey/mlkapply")){
                        this.$router.push({path: "/survey/application",query})
                    } else {
                        this.$router.push({path: data.TZURL, query})
                    }
                }
            }
        },
        addLink(d) {
        　　var addLink = d.XXNR;
            if(this.$route.path === "/construction/message" || d.SFTZ == "0" || !d.SFTZ){
                return '<span>'+d.XXNR+'</span>'
            }
            if ('' == addLink || null == addLink || undefined == addLink) {
                return '';
            }
            if (addLink.length > 0) {                  
                return '<a class="layui-table-link" href="javascript: void(0)" lay-event="link">' + d.XXNR + '</a>';                  
            }
        },
        deleteOpr(){
            if(this.$route.path === "/construction/message" && this.wdList){
                this.wdList.forEach((list,index) => {
                    $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='view']").remove();
                })
                if(this.current == 'yd'){
                this.ydList.forEach((list,index) => {
                    $(".table:eq(1)").find(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='view']").remove();
                })
                }
            }else if(this.$route.path === "/survey/message" && this.wdList){
                this.wdList.forEach((list,index) => {
                    if(list.SFTZ == "0" || !list.SFTZ){
                        $(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='view']").addClass("table-btn-disabled cursor-not-allowed");
                    }
                })
                if(this.current == 'yd' && this.ydList){
                this.ydList.forEach((list,index) => {
                    if(list.SFTZ == "0" || !list.SFTZ){
                        $(".table:eq(1)").find(".layui-table:eq(1) tr:eq("+index+")").find("td").last().find("span[lay-event='view']").addClass("table-btn-disabled cursor-not-allowed");
                    }
                })
            }
            }
            
        },
    }
}
 
</script>