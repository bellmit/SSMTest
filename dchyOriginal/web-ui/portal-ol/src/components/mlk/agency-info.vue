<template>
    <div class="form-edit" style="width: 100% ">
        <div class="agency-info">
            <div class="form-item" v-for="(item,index) in items" :key="index">
                <div class="item-label">{{item.label}}</div>
                <div class="item-value">{{agencyInfoData[item.value]}}</div>
            </div>
            <div class="form-chjd">
                <div class="item-label">可承接测绘阶段</div>
                <div class="item-value">
                    <div class="select-row-tree">
                        <div v-for="(tree,index) in treeList" :key="index">
                            <Tree :data="tree"></Tree>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import util from "../../service/util" 
import { getDictInfo } from "../../service/home"
export default {
    name: "AgencyInfo",
    props: {
        agencyInfoData: { 
            type: Object,
            default: function(){
                return {
                    dwmc: ""
                }
            }
        },
        readonly: { //当前表单是只读状态还是编辑状态
            type: Boolean,
            default: true
        },
    },
    data() {
        return {
            showCyry: true,
            ksOptions: {},
            jsOptions: {},
            checkedClsxList: [],
            dwxzList: [],
            zzdjList: [],
            treeList: [],
            items: [
                {
                    label: "测绘单位名称",
                    value: "dwmc"
                },
                {
                    label: "统一社会信用代码",
                    value: "tyshxydm"
                },
                {
                    label: "法人代表",
                    value: "frdb"
                },
                {
                    label: "测绘资质登记",
                    value: "zzdjmc"
                },
                {
                    label: "联系人",
                    value: "lxr"
                },
                {
                    label: "联系电话",
                    value: "lxdh"
                },
                {
                    label: "办公地址",
                    value: "bgdz"
                },
                {
                    label: "从业人员数量",
                    value: "cyrynum"
                }
            ],
            dictionaryList: [],
            ruleInline: {}
        }
    },
    mounted() {
        this.getDictInfo();
    },
    methods: {
        getDictInfo(){
            var params = {
                zdlx: ["CLSX"]
            }
            getDictInfo(params).then(res => {
                this.dictionaryList = res.data.dataList;
                this.renderClsxList();
            })
        },
        setClsxList(clsxList){
            this.checkedClsxList = [...clsxList];
            this.renderClsxList();
        },
        // 渲染测绘事项
        renderClsxList(){
            let treeList = [];
            this.dictionaryList.forEach(list => {
                let hasChecked = false;
                this.checkedClsxList.forEach(clsx => {
                    if(clsx.split("")[0] == list.DM){
                        hasChecked = true
                    }
                })
                if(!list.FDM&&list.ZDLX=="CLSX"&&hasChecked){
                    treeList.push({
                        title: list.MC, 
                        dm: Number(list.DM),
                        disabled:true, 
                        expand: true,
                        id: list.DM, 
                        children: [],
                        render: (h, { root, node, data }) => {
                            return h('span', [
                                h('Icon', {
                                    props: {
                                        type: 'md-arrow-dropright'
                                    }
                                }),
                                h('span', data.title)
                            ])
                        }
                    })
                }
            })
            this.dictionaryList.forEach(list => {
                if(list.FDM&&list.ZDLX=="CLSX"){
                    treeList.forEach(tree => {
                        if((tree.id == list.FDM) && this.checkedClsxList.includes(list.DM)){
                            tree.children.push({
                                title: list.MC, 
                                checked: this.checkedClsxList.includes(list.DM), 
                                disabled:true, 
                                id: list.DM,
                                render: (h, { root, node, data }) => {
                                    return h('span', [
                                        h('Icon', {
                                            props: {
                                                type: 'ios-arrow-forward'
                                            },
                                            style: {
                                                marginRight: '8px'
                                            }
                                        }),
                                        h('span', data.title)
                                    ])
                                }
                            })
                        }
                    })
                }
            })
            treeList = _.orderBy(treeList,['dm'],['asc'])
            this.treeList = [];
            treeList.forEach(tree => {
                this.treeList.push([tree])
            })
        },
        cyryxx(){
            this.$router.push({path: "/directory/cyryxx",query: {mlkid: this.mlkid}})
        }
    },
}
</script>
<style scoped>
    .form-edit {
        padding: 0 20px;
    }
    .form-edit >>> .ivu-form-item {
        margin-top: 12px;
    }
    .stage-tree {
        width: 100%;
        padding: 5px;
        overflow-x: auto;
        background-color: #e8f3fa;
    }
    .agency-info {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        border-top: 1px solid #8ec2e0;
        border-left: 1px solid #8ec2e0;
    }
    .form-item {
        width: 50%;
        display: flex;
        justify-content: flex-start;
        height: 40px;
        line-height: 40px;
    }
    .form-chjd {
        width: 100%;
        display: flex;
        justify-content: flex-start;
        height: 220px;
    }
    .form-chjd .item-label {
        line-height: 220px;
    }
    .form-chjd .item-value {
        padding-top: 20px;
        overflow-x: auto;
    }
    .select-row-tree >>> .ivu-tree-title .ivu-icon {
        color: #1d87d1;
    }
    .select-row-tree >>> .ivu-tree-title .ivu-icon-md-arrow-dropright {
        font-size: 22px;
        vertical-align: middle;
    }
    .select-row-tree >>> .ivu-tree-title {
        line-height: 26px;
    }
    .item-label {
        width: 200px;
        background-color: #e8f3fa;
        font-weight: bold;
        color: #666;
        padding-right: 10px;
        text-align: right;
        border-right: 1px solid #8ec2e0;
        border-bottom: 1px solid #8ec2e0;
    }
    .item-value {
        flex-grow: 1;
        padding-left: 10px;
        border-right: 1px solid #8ec2e0;
        border-bottom: 1px solid #8ec2e0;
    }
    .select-row-tree >>> .ivu-tree-arrow {
        display: none;
    }
    .select-row-tree >>> .ivu-tree-title:hover {
        background-color: #fff;
    }
</style>