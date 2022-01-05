<template>
    <div>
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
                :text="'您当前没有创建项目'"
            ></Empty>
            <div class="project-info" v-else>
                <div class="project-items" v-for="(project,index) in projectList"  @click="toProjectInfo(project)" :key="project.CHXMID + project.BABH + index">
                    <div class="project-title">
                        <div class="project-name">{{project.GCMC}}</div>
                        <div class="project-operation">
                            <span><van-icon style="vertical-align: middle" name="arrow" /></span>
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
                    </div>
                    <div class="project-time">
                        <div>
                            {{project.SLSJ}}
                        </div>
                        <div>
                            <span class="project-xmzt blue">
                                <img v-if="project.XMZT == '已备案'" src="static/images/xmzt-ba.png" alt="">
                                <img v-if="project.XMZT == '已办结'" src="static/images/xmzt-bj.png" alt="">
                                <span>{{project.XMZT}}</span>
                            </span>
                            <span v-if="project.SFGQ == '1'" class="project-xmzt orange">
                                <img src="static/images/xmzt-gq.png" alt="">
                                <span>已挂起</span>
                            </span>
                            <span v-if="project.SFCQ == '1'" class="project-xmzt red">
                                <img src="static/images/xmzt-cq.png" alt="">
                                <span>已超期</span>
                            </span>
                        </div>
                    </div>
                </div>
                <div v-if="totalPage > 1" @click="getMore" class="moreinfo margin-top-10">
                    <a href="javascript:void(0)" style="height: 80px;line-height: 80px;font-size: 32px;width: 690px;color: #333;background: #ffffff;">
                        加载更多<i class="fa fa-chevron-down"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { getJsdwProjectList } from "../../service/myproject"
export default {
    data() {
        return {
            projectList: [],
            totalPage: 1,
            form: {
                babh: "",
                pageSize: 5,
                page: 1
            }
        }
    },
    mounted() {
        this.queryProjectList();
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
        toProjectInfo(item){
            this.$router.push({
                path: "/mine/project-blsx",
                query: {
                    chxmid: item.CHXMID,
                    chdwxxid: item.CHDWXXID
                }
            })
        },
        getMore(){
            this.form.pageSize += 5;
            this.queryProjectList();
        }
    },
}
</script>
<style lang="less" scoped>
    @import url(../process/project-list.less);
    .search-top {
        display: flex;
        justify-content: flex-start;
    }
    .project-xmzt {
        margin-left: 5px;
    }
    .project-xmzt img{
        vertical-align: middle;
        margin-right: 5px;
        width: 24px;
    }
</style> 