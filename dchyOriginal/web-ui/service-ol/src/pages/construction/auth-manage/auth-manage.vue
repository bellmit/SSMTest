<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="姓名" class="form-list-search" prop="yhmc">
                            <Input type="text" class="form-search-item" v-model="formInline.yhmc" @keydown.enter.native.prevent="getAnthList(1,formInline.size)" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="证件号码" class="form-list-search" prop="yhzjhm">
                            <Input type="text" class="form-search-item" v-model="formInline.yhzjhm" @keydown.enter.native.prevent="getAnthList(1,formInline.size)" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="启用状态" class="form-list-search" porp="isvalid">
                            <Select v-model="formInline.isvalid" clearable class="form-search-item">
                                <Option v-for="item in qyztList" :value="item.DM" :key="item.MC">{{ item.MC }}</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='50' class="form-list-search">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getAnthList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <Table 
            :cols="tableCols"
            :data="myAuthList"
            :count="totalNum"
            :page="formInline.page"
            :size="formInline.size"
            :func="getAnthList"
            :operation="operationList"
            @enable="enable"
            @disable="disable"
            @wtsdownload="wtsdownload"
            @deleteOpr="deleteOpr"
        ></Table>
    </div>
</template>
<script>
import { queryUserList,changeUserState } from "../../../service/user"
import { getDictInfo } from "../../../service/mlk"
import util from '../../../service/util'
export default {
    data() {
        return {
            formInline: {
                yhmc: "",
                yhzjhm: "",
                isvalid: "",
                page: 1,
                size: 10
            },
            // tool: '<div>' +
            //         '<span class="layui-btn main-btn-a" lay-event="btn1">授权委托书下载</span>'+
            //     '</div>',
            tableCols: [
                // {
                //     type: "checkbox",
                //     align: "center",
                //     width: 50,
                //     fixed: "left",
                // },
                {
                    field: "ROWNUM_",
                    width: 70,
                    align: "center",
                    title: "序号",
                    fixed: "left"
                },
                {
                    field: "YHMC",
                    align: "center",
                    title: "姓名"
                },
                {
                    field: "YHZJZL",
                    align: "center",
                    title: "证件类型",
                },
                {
                    field: "YHZJHM",
                    align: "center",
                    title: "证件号码"
                },
                {
                    field: "ISVALID",
                    align: "center",
                    title: "启用状态",
                    templet: function(d){
                        let className= d.ISVALID==="1" ? "color-finish" : "color-unfinish"
                        if(d.ISVALID == "1"){
                            return "<span class='"+className+"'>"+"已启用"+"</span>"
                        }else{
                            return "<span class='"+className+"'>"+"未启用"+"</span>"
                        }
                        
                    }
                },
                {
                    align: "center",
                    title: "操作",
                    toolbar: "#operation",
                    minWidth: 180
                }
            ],
            operationList: ["enable","disable","wtsdownload"],
            totalNum: 0,
            myAuthList:[
                {
                    NAME: "qi",
                    QYZT:"启用中"
                }
            ],
            qyztList:[
            ],
            ruleInline:{}, 
            selectedList: []       
        }
    },
    mounted() {
        this.getAnthList();
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["SFZT"]
            }
            getDictInfo(params).then(res => {
                res.data.dataList.forEach(list => {
                    if(list.ZDLX == "SFZT" && list.DM =="0" ){
                        list.MC = "未启用"
                        
                    }else{
                        list.MC = "已启用"
                    }
                    this.qyztList.push(list)
                })
                this.qyztList.unshift({
                    DM: "",
                    MC: "全部"
                })
            })
        },
        getAnthList(){
            this.$loading.show("加载中...")
            queryUserList(this.formInline).then(res => {
                this.$loading.close();
                this.totalNum = res.data.totalNum;
                res.data.dataList.forEach(item => {
                    item.YHZJZL = util.convertZlZjh(item.YHZJZL)
                })
                this.myAuthList = res.data.dataList;
            })
        },
        // 重置查询条件
        resetForm() {
            this.$refs["formInline"].resetFields();
        },
        //授权书下载
        wtsdownload(item,i){
            if(!item.WJZXID){
                layer.msg("暂无材料")
            } else if(item.WJZXID){
                if (!location.origin) {
                    location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
                }
                location.href=location.origin + '/msurveyplat-serviceol/fileoperation/download?wjzxid=' + item.WJZXID
            }
        },
        // 删除操作
        deleteOpr(data){
            if(this.myAuthList && this.myAuthList.length){
                this.myAuthList.forEach((list, index) => {
                if (list.ISVALID == "0") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='disable']").remove();
                } else {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='enable']").remove();
                }
            });
            }
        },
        // 禁用
        disable(item){
            layer.confirm("确认禁用该用户?",(index) => {
                layer.close(index)
                let params = {
                    userid: item.YHID,
                    state: "0"
                }
                this.$loading.show("加载中...")
                changeUserState(params).then(res => {
                    layer.msg("禁用成功")
                    this.$loading.close();
                    this.getAnthList();
                })
            })
        },
        // 启用
        enable(item){
            layer.confirm("确认启用该用户?",(index) => {
                layer.close(index)
                let params = {
                    userid: item.YHID,
                    state: "1"
                }
                this.$loading.show("加载中...")
                changeUserState(params).then(res => {
                    layer.msg("启用成功")
                    this.$loading.close();
                    this.getAnthList();
                })
            })
        }
    }
}
</script>