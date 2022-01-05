<template>
    <div>
        <Form class="form-edit" @on-validate="validateChecked" :model="projectInfo" ref="project-info" :rules="ruleInline" :label-width="142">
            <Row class="margin-top-20">
                <FormItem v-model="projectInfo.clsx" class="requireStar" prop="clsx" label="测绘事项">
                    <Row class="select-row-tree stage-tree">
                        <div v-for="(tree,index) in treeList" :key="index">
                            <Tree :data="tree"></Tree>
                        </div>
                    </Row>
                </FormItem>
            </Row>
            <Row>
                <i-col span="8">
                    <FormItem v-model="projectInfo.babh" prop="babh" label="项目编号">
                        <Input v-model="projectInfo.babh" :readonly="readonly" />
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem v-model="projectInfo.gcbh" prop="gcbh" label="项目代码">
                        <Input :readonly="readonly" v-model="projectInfo.gcbh" class="list-form-width"/>
                    </FormItem>
                </i-col>
                <i-col span="8">
                    <FormItem v-model="projectInfo.gcmc" prop="gcmc" label="工程名称">
                        <Input :readonly="readonly" v-model="projectInfo.gcmc" class="list-form-width"/>
                    </FormItem>
                </i-col>
            </Row>
            <Row>
                <i-col span="16">
                     <div class="gcdd-item">
                        <FormItem v-model="projectInfo.gcdzs" prop="gcdzs" label="工程地点 ">
                            <Row>
                                <i-col span="16">
                                    <Row>
                                        <i-col span="8">
                                            <Select :disabled="readonly" v-model="projectInfo.gcdzs" @on-select="selectGcdzs">
                                                <Option v-for="(item,index) in gcdzsList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                            </Select>
                                        </i-col>
                                        <i-col span="8">
                                            <Select :disabled="readonly" v-model="projectInfo.gcdzss" @on-select="selectGcdzss">
                                                <Option v-for="(item,index) in gcdzssList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                            </Select>
                                        </i-col>
                                        <i-col span="8">
                                            <Select :disabled="readonly" v-model="projectInfo.gcdzqx">
                                                <Option v-for="(item,index) in gcdzqxList" :key='index' :value="item.DM">{{item.MC}}</Option>
                                            </Select>
                                        </i-col>
                                    </Row>
                                </i-col>
                                <i-col span="8">
                                    <Input :readonly="readonly" v-model="projectInfo.gcdzxx" />
                                </i-col>
                            </Row>
                        </FormItem>
                    </div>
                </i-col>
            </Row>
        </Form>
    </div>
</template>
<script>
import { getDictInfo, queryInfoByGcbh } from "../../service/manage"
import moment from "moment"
import _ from "lodash"
export default {
    props: {
        projectInfo: { //基本信息
            type: Object,
            default: () => {
                return {}
            }
        },
        readonly: { //是否只读
            type: Boolean,
            default: false
        },
        glxqfbbhList: {
            type: Array,
            default: () => {
                return []
            }
        },
        clsxList: {
            type: Array,
            default: () => {
                return []
            }
        }
    },
    watch: {
        clsxList: {
            deep: true,
            handler: function(newVal,oldVal){
               this.getDictInfo()
            }
        }
    },
    data() {
        return {
            gcdzsList: [],
            gcdzssList: [],
            gcdzqxList: [],
            type: 'gl',
            clsxMcList: [],
            ruleInline: {
                gcbh: {
                    required: true,
                    trigger: "blur",
                    validator: (rule,value,callback) => {
                        if(!value || !value.trim()){
                            callback("必填项不能为空")
                        } else {
                            if(this.unLastSubmit&&!this.readonly){
                                this.type = 'gl'
                                this.projectInfo.xqfbbh = "";
                                queryInfoByGcbh({gcbh: value.trim()}).then(res => {
                                    res.data.chxmxx&&res.data.chxmxx.forEach(list => {
                                        list.FBSJ = list.FBSJ ? moment(list.FBSJ).format("YYYY-MM-DD HH:mm:ss") : "";
                                    })
                                    this.$emit("select-gxxq",{chxmList: res.data.chxmxx || [],chgcxx: res.data.chgcxx})
                                })
                            }
                            callback()
                        }
                        callback()
                    }
                },
                gcmc: {
                    required: true,
                    trigger: "blur",
                    message: "必填项不能为空"
                },
                gcdzs: {
                    required: true,
                    trigger: "blur",
                    message: "必填项不能为空"
                }
            },
            unLastSubmit: true,
            firstStage: [],
            dictionaryInfo: [],
            treeList: [],
            selectFirst: false,
            selectSecond: false
        }
    },
    mounted() {
        if(this.$route.query.type == 'edit'){
            this.type = 'edit'
        }
        if(this.projectInfo.clsx && this.projectInfo.clsx.includes("1001")){
            this.selectFirst = true;
        }
        if(this.projectInfo.clsx && this.projectInfo.clsx.includes("2001")){
            this.selectSecond = true;
        }
    },
    methods: {
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&!this.unLastSubmit){
                this.$error.show(error)
            }
        },
        selectGxXq(select){
            this.$emit("xqfbbh",select)
        },
         // 选择省
        selectGcdzs(select){
            let gcdzssList = []
            this.dictionaryInfo.forEach(dict => {
                if((dict.FDM == select.value) && dict.ZDLX=="GCDZ"){
                    gcdzssList.push(dict)
                }
            })
            this.gcdzssList = [...gcdzssList]
        },
        // 选择市
        selectGcdzss(select){
            var gcdzqxList = [];
            this.dictionaryInfo.forEach(dict => {
                if((dict.FDM == select.value)&& dict.ZDLX=="GCDZ"){
                    gcdzqxList.push(dict)
                }
            })
            this.gcdzqxList = [...gcdzqxList]
        },
        // 获取测绘事项的名称
        getClsxMcList(){
            this.treeList.forEach(tree => {
                tree[0].children.forEach(t => {
                    if(this.clsxList.includes(t.id)){
                        this.clsxMcList.push(t.title)
                    }
                })
            })
        },
        // 获取字典项
        getDictInfo(){
            let params = {
                zdlx: ["CLSX","GCDZ"]
            }
            let treeList = []
            this.$loading.show("加载中...")
            getDictInfo(params).then(res => {
                this.$loading.close();
                this.dictionaryInfo = res.data.dataList;
                res.data.dataList.forEach(list => {
                    let find = this.clsxList.find(clsx => clsx&&clsx.startsWith(list.DM))
                    // 重新渲染树结构，添加icon
                    if(!list.FDM&&list.ZDLX=="CLSX" && find){
                        treeList.push(
                            {
                                title: list.MC, 
                                expand: true,
                                disabled: true,
                                id: list.DM, 
                                dm: Number(list.DM),
                                children: [],
                                render: (h, { root, node, data }) => {
                                    return h('span', [
                                        h('Icon', {
                                            props: {
                                                type: 'icon fa fa-folder-open'
                                            },
                                            style: {
                                                marginRight: '8px'
                                            }
                                        }),
                                        h('span', data.title)
                                    ])
                                }
                            }
                        )
                    }
                    if(!list.FDM&&list.ZDLX=="GCDZ"){
                        this.gcdzsList.push(list)
                    }
               })
                res.data.dataList.forEach(list => {
                    if(list.FDM&&list.ZDLX=="CLSX"){
                        treeList.forEach(tree => {
                            if(tree.id == list.FDM && this.clsxList.includes(list.DM)){
                                tree.children.push({
                                    title: list.MC, 
                                    id: list.DM,
                                    disabled: true,
                                     render: (h, { root, node, data }) => {
                                        return h('span', [
                                            h('Icon', {
                                                props: {
                                                    type: 'icon fa fa-file-text-o'
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
                treeList = _.orderBy(treeList,["dm"],['asc'])
                treeList.forEach(tree => {
                    this.treeList.push([tree])
                })
                if(this.projectInfo.gcdzs){
                    this.selectGcdzs({value: this.projectInfo.gcdzs})
                }
                if(this.projectInfo.gcdzss){
                    this.selectGcdzss({value: this.projectInfo.gcdzss})
                }
           })
        },
        // 校验
        validate(){
            let validate = true
            this.projectInfo.clsx = this.getSelectedClsx();
            this.unLastSubmit = false
            this.$refs["project-info"].validate(valid => {
                this.unLastSubmit = true
                validate = valid
            })
            return validate
        },
        // 获取选中的测绘事项
        getSelectedClsx(){
            let clsx = []
            if(this.selectFirst){
                this.firstStage.forEach(first => {
                    first.children.forEach(tree => {
                        clsx.push(tree.id)
                    })
                })
            }
            if(this.selectSecond){
                this.treeList.forEach(tree => {
                    tree[0].children.forEach(t => {
                        clsx.push(t.id)
                    })
                })
            }
            return clsx.join(";")
        }
    },
}
</script>
<style scoped>
    .select-row-tree >>> .ivu-tree-arrow {
        display: none;
    }
    .select-row-tree {
        display: flex;
        justify-content: flex-start;
    }
    .select-row-tree >>> .ivu-tree {
        margin-right: 10px;
    }
    .select-row-tree >>> .ivu-tree-title .ivu-icon {
        font-family: "FontAwesome";
        color: #1d87d1;
    }
    .stage-tree {
        padding: 10px;
        min-height: 140px;
        background-color: #e8f3fa;
    }
    .stage-tree span {
        margin-right: 40px;
    }
    .stage-next {
        flex-grow: 1;
    }
    .gcdd-item {
        width: calc(50% + 442px)
    }
    @media (max-width: 1600px) {
        .gcdd-item {
            width: calc( 50% + 342px);
        }
    }
</style>