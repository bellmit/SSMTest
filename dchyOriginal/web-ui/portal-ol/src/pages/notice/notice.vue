<template>
  <div class="color-bgc height-100">
    <Header :showUser="false"></Header>
    <div class="bdc-location">
        <img src="static/images/index-home.png" alt="">
        <span>当前位置:&nbsp;</span>
        <span class="layui-breadcrumb" lay-separator=">" lay-filter="location">
          <span @click="toHome()" style="cursor:pointer;">首页</span>
          <span lay-separator>></span>
          <a><cite>通知公告</cite></a>
        </span>
    </div>
    <div class="mlk-container bdc-container">
      <div class="mlk-search">
          <div class="bdc-content-title">
            <span class="bdc-now-name">通知公告</span>
        </div> 
        <div class="mlk-title">
            <Input style="width: 250px" v-model="formData.title" @keydown.enter.native.prevent="getNoticeList(1,formData.size)" class="margin-left-10" placeholder="请输入标题或内容"/>
            <Button class="main-btn-a margin-left-10" @click="getNoticeList(1,formData.size)">查询</Button>
        </div>
    </div>
    <div class="news-col-detail">
          <div class="news-col-title">标题</div>
          <div class="news-col-date-detail">日期
            <span class="ivu-table-sort">
              <i class="ivu-icon ivu-icon-md-arrow-dropup"  @click="getNoticeList(1,formData.size,'asc')"></i>
              <i class="ivu-icon ivu-icon-md-arrow-dropdown"  @click="getNoticeList(1,formData.size,'desc')"></i>
            </span>
          </div>
    </div>
    <div class="news-details">
      <div class="news-items" v-for="(item, index) in newsList" :key="index">
        <div class="news-title" @click="toDetail(item)">{{ item.BT }}</div>
        <div class="news-date">{{ item.FBSJ }}</div>
      </div>
    </div>
    <div class="mlk-page">
      <Page :total="totalNum" show-total size="small" @on-change="changePage" @on-page-size-change="changeSize" show-elevator show-sizer />
    </div>
    </div>
  </div>
</template>
<script>
import { queryNoticeList } from "../../service/home"
export default {
  data() {
    return {
      formData: {
        page: 1,
        size: 10,
        title: "",
        sfzx: ""
      },
      totalNum: 1,
      newsList: [],
      sortType: null,
    };
  },
  beforeRouteLeave (to, from, next) {
      if(to.fullPath.startsWith("/announce/detail")){
          this.pageInfo["noticePageInfo"] = {...this.formData}
      } else {
          this.pageInfo["noticePageInfo"] = null
      }
      next()
  },
  created() {
      if(this.pageInfo["noticePageInfo"]){
          this.formData = {...this.pageInfo["noticePageInfo"]}
      }
  },
  mounted() {
       this.getNoticeList() 
    },
    methods: {
      changePage(page){
        this.formData.page = page
        this.getNoticeList();
      },
      toHome(){
          let token = sessionStorage.getItem("access_token") || ""
          this.$router.push({
              path: "/home",
              query: {
                  token
              }
          })
      },
      changeSize(size){
        this.formData.size = size
        this.getNoticeList();
      },
      getNoticeList(page,size,sfzx){ 
        this.$loading.show("加载中...")
        if(sfzx == 'asc'){
          this.formData.sfzx = "asc"
        }else{
          this.formData.sfzx = ""
        }
        let params = {...this.formData}
        queryNoticeList(params).then(res => {
          this.$loading.close();
          this.newsList = res.data.dataList;
          this.totalNum = res.data.totalNum
        })
      },
      toDetail(item){
          this.$router.push({path: "/announce/detail", query: {tzggid: item.TZGGID,name: "通知公告"}})
      }
    },
};
</script>
<style lang="less" scoped>

@import "./notice.less";
.checked{
    color:black;
}
</style>