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
                <div class="project-items" v-for="(project,index) in projectList" :key="project.CHXMID + project.BABH + index">
                    <div class="project-title">
                        <div class="project-name">{{project.GCMC}}</div>
                        <div class="project-operation" @click="toProcessInfo(project)">
                            <span class="blue">进度查看 <van-icon style="vertical-align: middle" name="arrow" /></span>
                        </div>
                    </div>
                    <div class="project-detail">
                        <div>
                            <span class="detail-title">项目编号</span>
                            <span class="margin-left-10">{{project.BABH}}</span>
                        </div>
                        <div>
                            <span class="detail-title">当前节点</span>
                            <span class="margin-left-10 blue">{{project.DQJD}}</span>
                        </div>
                    </div>
                    <div class="project-time">
                        {{project.JDSJ}}
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
        toProcessInfo(item){
            this.$router.push({
                path: "/process/info",
                query: {
                    chxmid: item.CHXMID
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
    @import url(./project-list.less);
    .search-top {
        display: flex;
        justify-content: flex-start;
    }
    .project-time {
        height: 50px;
        line-height: 50px;
    }
</style> 