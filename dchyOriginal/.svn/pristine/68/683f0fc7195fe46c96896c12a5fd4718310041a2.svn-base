<template>
  <div>
    <van-tabs class="tab-custom">
      <van-tab title="基本信息" class="mine-user">
        <div class="mlk-view-title">名录库基本信息</div>
        <van-form
          ref="company-form"
          class="form-edit"
          label-align="right"
          label-width="9.2em"
        >
          <van-field
            readonly
            name="dwmc"
            v-model="agencyInfoData.dwmc"
            label="测绘单位名称"
          />
          <van-field
            readonly
            name="tyshxydm"
            v-model="agencyInfoData.tyshxydm"
            label="统一社会信用代码"
          />
          <van-field
            readonly
            name="frdb"
            v-model="agencyInfoData.frdb"
            label="法人代表"
          />
          <van-field
            readonly
            name="zzdjmc"
            v-model="agencyInfoData.zzdjmc"
            label="测绘资质等级"
          />
          <van-field
            readonly
            name="lxr"
            v-model="agencyInfoData.lxr"
            label="联系人"
          />
          <van-field
            readonly
            name="lxdh"
            v-model="agencyInfoData.lxdh"
            label="联系电话"
          />
          <van-field
            readonly
            name="bgdz"
            v-model="agencyInfoData.bgdz"
            label="办公地址"
          />
          <van-field
            readonly
            name="cyrynum"
            v-model="agencyInfoData.cyrynum"
            label="从业人员数量"
          />
        </van-form>
        <div class="tab-step-tip margin-top-20">
          <span>可承接测绘阶段</span>
        </div>
        <div class="tab-step-clsx">
          <div
            class="clsx-items"
            v-for="(chjd, index) in clsxTree"
            :key="index"
          >
            <div class="chjd-item">
              <span class="blue"
                ><van-icon style="vertical-align: middle" name="play"
              /></span>
              <span class="margin-left-10">{{ chjd.MC }}</span>
            </div>
            <div class="clsx-item" v-for="(clsx, i) in chjd.children" :key="i">
              <span class="blue"
                ><van-icon style="vertical-align: middle" name="arrow"
              /></span>
              <span class="margin-left-10">{{ clsx.MC }}</span>
            </div>
          </div>
        </div>
      </van-tab>
      <van-tab title="从业人员信息">
        <div class="project-info">
          <Empty
            v-if="!cyryxxList.length"
            :text="'暂无从业人员信息'"
          ></Empty>
          <div
            class="project-items"
            v-for="(cyryxx, index) in cyryxxList"
            :key="index"
          >
            <div class="project-detail">
              <div class="item-img">
                <img src="static/images/people.png" alt="" />
              </div>
              <span class="detail-title">姓名</span>
              <span class="margin-left-20">{{ cyryxx.RYXM }}</span>
            </div>
            <div class="project-detail">
              <div>
                <span class="detail-title">职称</span>
                <span class="margin-left-20">{{ cyryxx.ZC }}</span>
              </div>
              <div>
                <span class="detail-title">证书名称</span>
                <span class="margin-left-20">{{ cyryxx.ZSMC }}</span>
              </div>
            </div>
          </div>
          <div v-if="cyryTotalPage>1" @click="getMore" class="moreinfo margin-top-10">
            <a
              href="javascript:void(0)"
              style="
                height: 80px;
                line-height: 80px;
                font-size: 32px;
                width: 690px;
                color: #333;
                background: #ffffff;
              "
            >
              加载更多<i class="fa fa-chevron-down"></i>
            </a>
          </div>
        </div>
      </van-tab>
      <van-tab title="诚信记录">
        <div class="project-info">
          <Empty
            v-if="!cxRecordList.length"
            :text="'暂无从业人员信息'"
          ></Empty>
          <div
            class="project-items"
            v-for="(cxRecord, index) in cxRecordList"
            :key="index"
          >
            <div class="project-detail">
              <div class="item-img">
                <img src="static/images/honest.png" alt="" />
              </div>
              <span class="detail-title" style="width: 160px">记录时间</span>
              <span class="detail-content margin-left-20">{{
                cxRecord.JLSJ
              }}</span>
            </div>
            <div class="project-detail">
              <div>
                <span class="detail-content">{{ cxRecord.CXPJ }}</span>
              </div>
            </div>
          </div>
          <div v-if="cxTotalPage > 1" @click="getMorecxRecord" class="moreinfo margin-top-10">
            <a
              href="javascript:void(0)"
              style="
                height: 80px;
                line-height: 80px;
                font-size: 32px;
                width: 690px;
                color: #333;
                background: #ffffff;
              "
            >
              加载更多<i class="fa fa-chevron-down"></i>
            </a>
          </div>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>
<script>
import {
  querymlkdetails,
  querycyrybymlkid,
  queryMlkCxRecord,
  getDictInfo,
} from "../../service/home";
export default {
  data() {
            return {
            agencyInfoData: {
                dwmc: "",
                tyshxydm: "",
                frdb: "",
            },
            form: {
                page: 1,
                size: 5,
            },
            cxForm: {
                page: 1,
                size: 5,
            },
            cyryxxList: [],
            cyryTotalPage: 1,
            cxTotalPage: 1,
            cxRecordList: {
                JLSJ: "2020-2-20",
                CXPJ: "以下是诚信记录内容",
            },
            clsxTree: [],
            checkedClsxList: "",
            };
        },
            mounted() {
                if (this.$route.query.mlkid) {
                this.mlkid = this.$route.query.mlkid;
                sessionStorage.setItem("mlkid", this.mlkid);
                }
                // this.querymlkdetails();
                this.querycyrybymlkid();
                this.queryMlkCxRecord();
                this.getDictInfo();
            },
            methods: {
                getDictInfo() {
                    let params = {
                        zdlx: ["CLSX"],
                    };
                    this.$toast.loading({
                        message: "加载中...",
                        forbidClick: true,
                    });
                    getDictInfo(params).then((res) => {
                        this.$toast.clear();
                        this.dictionaryTeeList = res.data.dataList;
                        this.querymlkdetails();
                    });
                },
                //获取基本信息
                querymlkdetails() {
                    this.$toast.loading({
                        message: "加载中...",
                        forbidClick: true,
                    });
                    querymlkdetails({ mlkid: this.mlkid }).then((res) => {
                        this.$toast.clear();
                        this.agencyInfoData = res.data.dataList[0];
                        if (this.agencyInfoData.clsxdms) {
                        this.checkedClsxList = this.agencyInfoData.clsxdms.split(";");
                        this.renderClsx();
                        }
                    });
                },
                querycyrybymlkid() {
                    this.$toast.loading({
                        message: "加载中...",
                        forbidClick: true,
                    });
                    this.form.mlkid = this.mlkid;
                    querycyrybymlkid(this.form).then((res) => {
                        this.$toast.clear();
                        this.cyryxxList = res.data.dataList || [];
                        this.totalNum = res.data.totalNum || 0;
                        this.cyryTotalPage = res.data.totalPage || 1;
                    });
                },
                queryMlkCxRecord() {
                    this.$toast.loading({
                        message: "加载中...",
                        forbidClick: true,
                    });
                    let params = {
                        page: this.cxForm.page,
                        size: this.cxForm.size,
                        mlkid: this.mlkid,
                };
                queryMlkCxRecord(params).then((res) => {
                    this.$toast.clear();
                    this.cxRecordList = res.data.dataList || [];
                    this.cxRecordTotal = res.data.totalNum || 0;
                    this.cxTotalPage = res.data.totalPage || 1;
                });
                },
                getMore() {
                    this.form.size += 5;
                    this.querycyrybymlkid();
                },
                getMorecxRecord() {
                    this.cxForm.size += 5;
                    this.queryMlkCxRecord();
                },
                renderClsx() {
                    let clsxTree = [];
                    this.dictionaryTeeList.forEach((clsx) => {
                        if (!clsx.FDM) {
                        let find = this.checkedClsxList.find((list) =>
                            list.startsWith(clsx.DM)
                        );
                        if (find) {
                            clsxTree.push({
                            ...clsx,
                            children: [],
                            });
                        }
                        }
                    });
                    this.dictionaryTeeList.forEach((clsx) => {
                        if (clsx.FDM) {
                        let find = this.checkedClsxList.find((list) => list == clsx.DM);
                        if (find) {
                            let findIndex = clsxTree.findIndex((c) => c.DM == clsx.FDM);
                            clsxTree[findIndex].children.push({
                            ...clsx,
                            });
                        }
                        }
                    });
                    this.clsxTree = clsxTree;
                },
            },
};
</script>
<style scoped>
        .mlk-view-title {
        font-size: 28px;
        color: #333;
        margin: 10px;
        }
        .mine-user {
        background-color: #fff;
        padding: 20px;
        }
        .project-info {
        padding: 20px;
        }
        .project-items {
        min-height: 200px;
        background-color: #fff;
        }
        .project-items:nth-child(n + 2) {
        margin-top: 30px;
        }
        .project-title {
        font-size: 30px;
        padding: 0 20px;
        line-height: 2;
        display: flex;
        justify-content: space-between;
        }
        .project-operation {
        font-size: 28px;
        }
        .project-detail {
        font-size: 24px;
        padding: 20px;
        line-height: 2.2;
        border-top: 1px solid #d0d5da;
        }
        .detail-content {
        color: #999;
        }
        .detail-title {
        color: #999;
        display: inline-block;
        width: 96px;
        text-align: right;
        }
        .project-time {
        padding: 0 20px;
        line-height: 2;
        font-size: 24px;
        box-sizing: border-box;
        color: #999;
        }
        .item-img {
        display: inline-block;
        position: absolute;
        margin-left: 11px;
        margin-top: 4px;
        }
        .tab-step-tip {
        font-size: 28px;
        margin-bottom: 20px;
        }
        .tab-step-clsx {
        padding: 0 30px;
        box-sizing: border-box;
        background-color: #d9ebf7;
        }
        .clsx-items {
        padding: 20px 0;
        border-bottom: 1px solid #d0d5da;
        line-height: 1.8;
        }
        .clsx-items:nth-last-child(1) {
        border-bottom: none;
        }
        .clsx-item {
        padding-left: 20px;
        color: #666;
        font-size: 22px;
        }
</style>>