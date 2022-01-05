<template>
    <div>
        <van-tabs class="tab-custom" v-model="active">
            <van-tab title="委托双方信息">
                <div class="tab-page">
                    <van-form ref="company-form" class="form-edit" label-align="right" label-width="10em">
                        <van-cell class="cell-info-tip">建设单位基本信息</van-cell>
                        <van-field
                            readonly
                            name="wtdw"
                            v-model="projectInfoData.wtdw"
                            label="建设单位名称"
                        />
                        <van-field
                            readonly
                            name="tyshxydm"
                            v-model="projectInfoData.tyshxydm"
                            label="统一社会信用代码"
                        />
                        <van-field
                            readonly
                            name="lxr"
                            v-model="projectInfoData.lxr"
                            label="联系人"
                        />
                        <van-field
                            readonly
                            name="lxdh"
                            v-model="projectInfoData.lxdh"
                            label="联系电话"
                        />
                        <van-cell class="cell-info-tip">测绘单位基本信息</van-cell>
                        <van-field
                            readonly
                            name="frlx"
                            v-model="projectInfoData.dwmc"
                            label="测绘单位名称"
                        />
                        <van-field
                            readonly
                            name="tyshxydm"
                            v-model="projectInfoData.chtyshxydm"
                            label="统一社会信用代码"
                        />
                        <van-field
                            readonly
                            name="lxr"
                            v-model="projectInfoData.chlxr"
                            label="联系人"
                        />
                        <van-field
                            readonly
                            name="lxdh"
                            v-model="projectInfoData.chlxdh"
                            label="联系电话"
                        />
                    </van-form>
                </div>
            </van-tab>
            <van-tab title="委托项目信息">
                <div class="tab-page">
                    <div class="tab-step-tip">
                        <span>项目基本信息</span>
                    </div>
                    <van-form ref="company-form" class="form-edit" label-align="right" label-width="10em">
                        <van-field
                            readonly
                            name="gcmc"
                            v-model="projectInfoData.gcmc"
                            label="工程名称"
                        />
                        <van-field
                            readonly
                            name="gcbh"
                            v-model="projectInfoData.gcbh"
                            label="项目代码"
                        />
                        <van-field
                            readonly
                            name="xmdz"
                            v-model="projectInfoData.xmdz"
                            label="工程地点"
                        />
                        <van-field
                            readonly
                            name="BABH"
                            v-model="projectInfoData.babh"
                            label="项目编号"
                        />
                        <van-field
                            readonly
                            name="SLSJ"
                            v-model="projectInfoData.slsj"
                            label="备案时间"
                        />
                    </van-form>
                    <div class="tab-step-tip margin-top-20">
                        <span>测绘事项</span>
                    </div>
                    <div class="tab-step-clsx">
                        <div class="clsx-items" v-for="(chjd,index) in clsxTree" :key="index">
                            <div class="chjd-item">
                                <span class="blue"><van-icon style="vertical-align:middle" name="play" /></span>
                                <span class="margin-left-10">{{chjd.MC}}</span>
                            </div>
                            <div class="clsx-item" v-for="(clsx,i) in chjd.children" :key="i">
                                <span class="blue"><van-icon style="vertical-align:middle" name="arrow" /></span>
                                <span class="margin-left-10">{{clsx.MC}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </van-tab>
            <van-tab title="附件材料">
                <div style="padding: 30px">
                     <div class="project-items" v-for="(file,index) in uploadList" :key="index">
                        <div class="project-title">
                            <div class="project-name">{{file.CLMC}}</div>
                            <div class="project-operation blue" @click="download(file)">
                                <span>下载 <van-icon style="vertical-align: middle" name="arrow" /></span>
                            </div>
                        </div>
                        <div class="project-detail">
                            <div>
                                <span class="detail-title">材料类型</span>
                                <span class="margin-left-10">{{file.CLLXMC}}</span>
                            </div>
                            <div>
                                <span class="detail-title">所属测量事项</span>
                                <span class="margin-left-10">{{file.SSCLSXMC}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </van-tab>
        </van-tabs>
    </div>
</template>
<script>
import _ from "loadsh"
import { queryJsdwWtxx } from "../../service/myproject"
import { getDictInfo } from "../../service/home"
import { getUploadList } from "../../service/login"
export default {
    data() {
        return {
            projectInfoData: {},
            active: "委托双方信息",
            chxmid: "",
            chdwxxid: "",
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
            ],
            checkedClsxList: "",
            clsxTree: [],
            uploadList: [],
            dictionaryTeeList: []
        }
    },
    mounted() {
        this.chxmid = this.$route.query.chxmid || ""
        this.chdwxxid = this.$route.query.chdwxxid || ""
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            let params = {
                zdlx: ["CLSX"]
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            getDictInfo(params).then(res => {
                this.$toast.clear();
                this.dictionaryTeeList = res.data.dataList;
                this.queryJsdwWtxx();
            })
        },
        download(item){
            if(!item.WJZXID){
                this.$toast("暂无文件")
            } else {
                location.href = '/msurveyplat-serviceol/fileoperation/download?wjzxid=' + item.WJZXID;
            }
        },
        queryJsdwWtxx(){
            let params = {
                chxmid: this.chxmid,
                chdwxxid: this.chdwxxid
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            queryJsdwWtxx(params).then(res => {
                this.$toast.clear();
                let info = res.data.dataList&&res.data.dataList.length ? res.data.dataList[0] : {};
                info.dwmc = info.CHDWMC;
                info.xmdz = (info.GCDZS || "") + (info.GCDZSS || "") + (info.GCDZQX || "") + (info.GCDZXX || "");
                for(let key in info){
                    info[_.toLower(key)] = info[key]
                }
                this.checkedClsxList = info.clsx ? info.clsx.split(",") : [];
                this.queryUploadList();
                this.renderClsx();
                this.projectInfoData.dwmc = info.chdwmc;
                this.projectInfoData.chtyshxydm = info.chjgtyshxydm;
                this.projectInfoData.chlxr = info.chjglxr;
                this.projectInfoData.chlxdh = info.chjglxdh;
                this.projectInfoData = {
                    ...this.projectInfoData,
                    ...info
                }
            })
        },
        queryUploadList(){
            let params = {
                ssmkid: "18",
                glsxid: this.chxmid
            }
            getUploadList(params,'/fileoperation/getsjclByGlsxid').then(res => {
                res.data.dataList&&res.data.dataList.forEach(list => {
                    let clsxmc = []
                    if(list.CLSXS){
                        clsxmc = list.CLSXS.map(c => c.MC)
                    }
                    list.SSCLSXMC = clsxmc.join(",")
                    list.CLLXMC = this.typeHtmlList.find(type => type.value == list.CLLX).label
                })
                this.uploadList = res.data.dataList || [];
            })
        },
        renderClsx(){
            let clsxTree = []
            this.dictionaryTeeList.forEach(clsx => {
                if(!clsx.FDM){
                    let find = this.checkedClsxList.find(list => list.startsWith(clsx.DM))
                    if(find){
                        clsxTree.push({
                            ...clsx,
                            children: []
                        })
                    }
                }
            })
            this.dictionaryTeeList.forEach(clsx => {
                if(clsx.FDM){
                    let find = this.checkedClsxList.find(list => list == clsx.DM)
                    if(find){
                        let findIndex = clsxTree.findIndex(c => c.DM == clsx.FDM)
                        clsxTree[findIndex].children.push({
                            ...clsx
                        })
                    }
                }
            })
            this.clsxTree = clsxTree
        }
    },
}
</script>
<style lang="less" scoped>
    @import url(../process/project-list.less);
</style>
<style scoped>
    .tab-page {
        background-color: #fff;
        min-height: calc(100vh - 100px);
        padding: 30px;
        box-sizing: border-box;
    }
    .cell-info-tip {
        border-left: 1px solid #d0d5da;
        text-align: center;
        background-color: #f3f4f6;
    }
    .cell-info-tip .van-cell__value--alone {
        text-align: center;
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
    .project-detail {
        border-bottom: none;
    }
</style>