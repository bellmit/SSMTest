<template>
    <div style="padding-bottom: 100px">
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="关键词 " class="form-list-search" prop="title">
                            <Input type="text" class="form-search-item" placeholder="请输入" @keydown.enter.native.prevent="getAdviceList(1,formInline.size)" v-model="formInline.title"/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="状态 " class="form-list-search" prop="status">
                            <Select clearable v-model="formInline.status">
                                <Option value="">全部</Option>
                                <Option value="1">已回复</Option>
                                <Option value="0">待回复</Option>
                            </Select>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width='50' class="form-list-search">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="getAdviceList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="line-dashed"></div>
        <div>
            <div v-for="(item,index) in adviceList" :key="item.ANSWERID" class="advice-item margin-top-10">
                <div class="advice-title">
                    <div class="advice-title-detail">
                        <div :class="item.STATUS=='1'? 'zt-yhf advice-zt': 'zt-dhf advice-zt' ">{{item.STATUS == "1" ? "已回复": "待回复"}}</div>
                        {{item.TITLE}}
                    </div>
                    <div class="font-color-999">{{item.CJSJ}}</div>
                </div>
                <div class="advice-ques">
                    <div>提问：</div>
                    <div class="advice-ans-detail1">{{item.ISSUESCONTENT}}</div>
                </div>
                <div v-if="item.STATUS=='1'" class="advice-ans">
                    <div>回复：</div>
                    <div style="display: inline-block" :class="item.fold?'advice-ans-detail1':'advice-ans-detail word-wrap'">
                        {{item.ANSWERCONTENT}}
                        <span v-if="item.fold" @click="handlerFold(item,index)" class="cursor-pointer font-color-major">收起</span>
                    </div>
                    <div v-if="!item.fold" @click="handlerFold(item,index)" class="show-all cursor-pointer font-color-major">显示全部</div>
                </div>
            </div>
        </div>
        <div class="advice-page">
            <Page 
                :total="totalNum" 
                size="small" 
                :current="formInline.page"
                :page-size='formInline.size' 
                :page-size-opts="pageOpts" 
                show-elevator 
                show-sizer 
                show-total
                @on-change="changePage"
                @on-page-size-change="changeSize"
            />
        </div>
    </div>
</template>
<script>
import _ from "loadsh"
import { queryMyAdvice } from "../../../service/advice"
export default {
    data() {
        return {
            formInline: {
                title: "",
                status: "",
                size: 10,
                page: 1
            },
            pageOpts: [10,20,50,100],
            totalNum: 2,
            adviceList: []
        }
    },
    mounted() {
        this.getAdviceList();
    },
    methods: {
        // 修改page
        changePage(page){
            this.formInline.page = page
            this.getAdviceList()
        },
        // 修改size
        changeSize(size){
            this.formInline.size = size
            this.getAdviceList()
        },
        // 获取我的留言列表
        getAdviceList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...")
            queryMyAdvice(this.formInline).then(res => {
                this.$loading.close();
                this.adviceList = res.data.dataList || [];
                this.totalNum = res.data.totalNum || 0;
                this.$nextTick(() => {
                    this.adviceList.forEach((item,index) => {
                        let width = $(".advice-item:eq("+index+") .advice-ans .advice-ans-detail").width();
                        if(!item.ANSWERCONTENT || !(item.ANSWERCONTENT.length*14 > width)){
                            $(".advice-item:eq("+index+") .advice-ans .show-all").addClass("hide")
                        }
                    })
                })
            })
        },
        resetForm(){
            this.$refs["formInline"].resetFields();
        },
        // 收起展开
        handlerFold(item,index){
            item.fold = !item.fold;
            let adviceList = _.cloneDeep(this.adviceList)
            adviceList[index] = {...item}
            this.adviceList = _.cloneDeep(adviceList)
        }
    },
}
</script>
<style lang="less" scoped>
    @import "./advice.less";
</style>