<template>
    <div>
        <van-tabs class="tab-custom" v-model="active">
            <van-tab title="未办结">
                <div class="search-top">
                    <van-field v-model="form.babh" label="" clearable placeholder="请输入项目编号">
                        <template #left-icon>
                            <van-icon @click="queryProjectList(true)" name="search" />
                            <div class="line-search"></div>
                        </template>
                    </van-field>
                </div>
                <div>
                    <Empty
                        v-if="!projectList.length"
                        :text="'目前没有未办结项目'"
                    ></Empty>
                    <div class="project-info" v-else>
                        <div class="project-items" v-for="(project,index) in projectList" :key="project.CHXMID + project.BABH + index">
                            <div class="project-title">
                                <div class="project-name">{{project.GCMC}}</div>
                                <div class="project-operation" @click="finishData(project)">
                                    <span class="blue">办结 <van-icon style="vertical-align: middle" name="arrow" /></span>
                                </div>
                            </div>
                            <div class="project-detail">
                                <div>
                                    <span class="detail-title">项目编号</span>
                                    <span class="margin-left-10">{{project.BABH}}</span>
                                </div>
                                <div>
                                    <span class="detail-title">测绘单位</span>
                                    <span class="margin-left-10">{{project.CHDWMC}}</span>
                                </div>
                                <div>
                                    <span class="detail-title">测绘阶段</span>
                                    <span class="margin-left-10">{{project.SSJD}}</span>
                                </div>
                            </div>
                            <div class="project-time">
                                {{project.SLSJ}}
                            </div>
                        </div>
                        <div v-if="totalPage > 1" @click="getMore" class="moreinfo margin-top-10">
                            <a href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                                加载更多<i class="fa fa-chevron-down"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </van-tab>
            <van-tab title="未评价">
                <div class="search-top">
                    <van-field v-model="pjForm.babh" label="" clearable placeholder="请输入项目编号">
                        <template #left-icon>
                            <van-icon @click="queryPjProjectList(true)" name="search" />
                            <div class="line-search"></div>
                        </template>
                    </van-field>
                </div>
                <div>
                    <Empty
                        v-if="!pjProjectList.length"
                        :text="'目前没有未办结项目'"
                    ></Empty>
                    <div class="project-info" v-else>
                        <div class="project-items" v-for="(project,index) in pjProjectList" :key="project.CHXMID + project.BABH + index">
                            <div class="project-title">
                                <div class="project-name">{{project.GCMC}}</div>
                                <div class="project-operation" @click="toEvaluate(project)">
                                    <span class="blue">评价 <van-icon style="vertical-align: middle" name="arrow" /></span>
                                </div>
                            </div>
                            <div class="project-detail">
                                <div>
                                    <span class="detail-title">项目编号</span>
                                    <span class="margin-left-10">{{project.BABH}}</span>
                                </div>
                                <div>
                                    <span class="detail-title">测绘单位</span>
                                    <span class="margin-left-10">{{project.CHDWMC}}</span>
                                </div>
                                <div>
                                    <span class="detail-title">测绘阶段</span>
                                    <span class="margin-left-10">{{project.CLJD ? project.CLJD.join(";") : ""}}</span>
                                </div>
                            </div>
                            <div class="project-time">
                                {{project.PJSJ}}
                            </div>
                        </div>
                        <div v-if="pjTotalPage > 1" @click="getPjMore" class="moreinfo margin-top-10">
                            <a href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                                加载更多<i class="fa fa-chevron-down"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </van-tab>
        </van-tabs>
    </div>
</template>
<script>
import { getJsdwProjectList, onlinecomplete, onlinecompletecheck, jsdwEvaludateList } from "../../service/myproject"
export default {
    data() {
        return {
            projectList: [],
            totalPage: 1,
            active: "未办结",
            form: {
                babh: "",
                xmzt: "2",
                page: 1,
                pageSize: 5
            },
            pjProjectList: [],
            pjTotalPage: 1,
            pjForm: {
                babh: "",
                pjzt: "0",
                page: 1,
                size: 5
            }
        }
    },
    mounted() {
        this.queryProjectList();
        this.queryPjProjectList();
    },
    methods: {
        queryProjectList(isSearch){
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            if(isSearch){
                this.form = {
                    ...this.form,
                    pageSize: 5,
                    page: 1
                }
            }
            getJsdwProjectList(this.form).then(res => {
                this.$toast.clear();
                this.projectList = res.data.dataList || [];
                this.totalPage = res.data.totalPage || 1;
            })
        },
        getMore(){
            this.form.pageSize += 5;
            this.queryProjectList();
        },
        getPjMore(){
            this.pjForm.size += 5;
            this.queryPjProjectList();
        },
        queryPjProjectList(isSearch){
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            if(isSearch){
                this.pjForm = {
                    ...this.pjForm,
                    size: 5,
                    page: 1
                }
            }
            jsdwEvaludateList(this.pjForm).then(res => {
                this.$toast.clear();
                this.pjProjectList = res.data.dataList || [];
                this.pjTotalPage = res.data.totalPage || 1;
            })
        },
        // 评价
        toEvaluate(item){
            this.$router.push({
                path: "/evaluate/add",
                query: {chxmid: item.CHXMID,chdwxxid:item.CHDWXXID, gcmc: item.GCMC,chdwmc: item.CHDWMC, type: "add"}
            })
        },
        // 办结项目
        finishData(data){
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            onlinecompletecheck({chxmid: data.CHXMID}).then(res => {
                this.$toast.clear()
                let msg = res.head.msg
                let code = res.head.code
                if(code == "1001"){
                    this.$toast(msg)
                } else if(code == "0000"){
                    this.$dialog.confirm({
                        tip: "提示",
                        message: msg
                    }).then(()=> {
                        this.$toast.loading({
                            message: '办结中...',
                            forbidClick: true,
                        });
                        onlinecomplete({chxmid: data.CHXMID}).then(res => {
                            this.$toast.clear()
                            this.queryProjectList(true);
                        })
                    })
                }
            })
        }
    },
}
</script>
<style lang="less" scoped>
    @import url(../process/project-list.less);
    .project-time {
        height: 50px;
        line-height: 50px;
    }
</style>