<template>
    <div class="color-bgc height-100">
        <Header :showUser="false"></Header>
        <div class="bdc-location">
            <img src="static/images/index-home.png" alt="">
            <span>当前位置:&nbsp;</span>
            <span class="layui-breadcrumb" lay-separator=">" lay-filter="location">
            <span @click="toHome()" style="cursor:pointer;">首页</span>
            <span lay-separator>></span>
            <a><cite>模板下载</cite></a>
            </span>
        </div>
        <div class="mlk-container bdc-container">
            <div class="bdc-content-title">
                <span class="bdc-now-name">模板下载</span>
            </div> 
            <div class="news-header">
                <div class="news-col">
                    <div class="news-col-title">模板名称</div>
                    <div class="news-col-date">模板类型</div>
                    <div class="news-col-type">发布时间</div>
                    <div class="news-col-type">操作</div>
                </div>
            </div>
             <div class="news-details">
                <div class="news-items" v-for="(item, index) in templateList" :key="index">
                    <div class="news-title">{{ item.MBMC }}</div>
                    <div class="news-mblx">{{ item.MBLX }}</div>
                    <div class="news-date">{{ item.SCSJ }}</div>    
                    <div class="news-date">
                        <a @click="dwonloadFj(item)" href="javascript: void(0)">下载</a>    
                    </div>    
                </div>
            </div>
            <div class="mlk-page">
                <Page :total="totalNum" show-total @on-change="changePage" @on-page-size-change="changeSize" size="small" show-elevator show-sizer />
            </div>
        </div>
    </div>
</template>
<script>
import { queryTemplateList } from "../../service/home"
export default {
    data() {
        return {
            formInline: {
                page: 1,
                pageSize: 10
            },
            totalNum: 1,
            templateList: []
        }
    },
    mounted() {
        this.queryTemplateList()  
    },
    methods: {
        toHome(){
            this.$router.push("/")
        },
        queryTemplateList() {
            this.$loading.show("加载中...")
            queryTemplateList(this.formInline).then(res => {
                this.$loading.close();
                this.templateList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })  
        },
        changePage(page){
            this.formInline.page = page
            this.queryTemplateList();
        },
        dwonloadFj(item){
            if(!item.WJZXID){
                layer.msg('暂无材料')
                return
            }
            if (!location.origin) {
                  location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
            }
            location.href=location.origin + '/portal-ol/fileoperation/download?wjzxid=' +item.WJZXID
        },
        changeSize(size){
            this.formInline.pageSize = size
            this.queryTemplateList();
        }
    },
}
</script>
<style lang="less" scoped>
    @import "./template.less";
</style>