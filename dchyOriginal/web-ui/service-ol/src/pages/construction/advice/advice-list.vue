<template>
    <div style="padding-bottom: 100px">
        <Tabs value="name1">
            <TabPane label="留言列表" name="name1"></TabPane>
            <Button type="primary" class="bdc-major-btn" @click="addAdvice()" slot="extra">我要咨询</Button>
        </Tabs>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :label-width="120" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="关键词 " class="form-list-search" prop="title">
                            <Input type="text" class="form-search-item" placeholder="请输入" @keydown.enter.native.prevent="getAdviceList(1,formInline.size)" v-model="formInline.title"/>
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
                    <div><Icon type="ios-mail-outline" class="advice-title-icon"/>{{item.TITLE}}</div>
                    <div class="font-color-999">{{item.CJSJ}}</div>
                </div>
                <div class="advice-ques">
                    <div>提问：</div>
                    <div class="advice-ans-detail1">{{item.ISSUESCONTENT}}</div>
                </div>
                <div class="advice-ans">
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
import { queryAdviceList, initAdviceSqxx } from "../../../service/advice"
export default {
    data() {
        return {
            formInline: {
                title: "",
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
        // 新增留言
        addAdvice(){
            this.$loading.show("加载中...")
            initAdviceSqxx().then(res => {
                let issuesid = res.data.data
                this.$loading.close();
                if(this.$route.path.startsWith("/construction")){
                    this.$router.push({path: "/construction/advice/add", query: {issuesid}})
                } else {
                    this.$router.push({path: "/survey/advice/add", query: {issuesid}})
                }
            })
        },
        // 获取留言列表
        getAdviceList(page,size){
            if(page){
                this.formInline.page = page;
                this.formInline.size = size;
            }
            this.$loading.show("加载中...")
            queryAdviceList(this.formInline).then(res => {
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