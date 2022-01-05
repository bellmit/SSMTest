<template>
    <div class="review-file">
        <Header></Header>
        <div class="bdc-container content">
            <div class="content-left">
                <div class="left-tree">
                    <div class="tree-header">
                        <span>文件夹</span>
                        <span></span>
                    </div>
                    <div class="tree-content">
                        <Tree @on-toggle-expand="toggleTree" @on-select-change="selectTreeChange" :data="treeData"></Tree>
                    </div>
                </div>
                <div class="left-detail">
                    <div class="tree-header">
                        <span>详细信息</span>
                        <span></span>
                    </div>
                    <div class="detail-content">
                        <div>
                            <span>上传者：{{sjr}}</span>
                        </div>
                        <div>
                            <span>时间：{{sjsj}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="content-right">
                <div class="file-operation">
                    <div>
                        <Button type="primary" class="bdc-major-btn" @click="viewDetail()">查看</Button>
                    </div>
                    <div class="opr-right">
                        <span class="cursor-pointer file-opr" title="后退" @click="backToPre()">
                            <Icon type="md-arrow-back" />
                        </span>
                        <span class="cursor-pointer file-opr" title="前进" @click="goAheadToNext()">
                            <Icon type="md-arrow-forward" />
                        </span>
                        <span class="cursor-pointer file-opr" title="向上" @click="upToPre()">
                            <Icon type="md-arrow-up" />
                        </span>
                        <span class="cursor-pointer file-opr" title="刷新" @click="getTreeData()">
                            <Icon type="md-refresh" />
                        </span>
                        <span class="cursor-pointer file-opr" title="下载" @click="downloadFile()">
                            <Icon type="md-download" />
                        </span>
                    </div>
                </div>
                <div class="file-detail" @click="handlerBgClick">
                    <div v-for="(file,index) in showFileData" @dblclick="dbClickHandler(file)" @click.stop="clickHandler(file,index)" :class="file.active?'file-item file-item-active':'file-item'" :key="index">
                        <div>
                            <img v-if="file.type=='0' || !file.type" src="static/images/file.png" class="icon-file" alt="">
                            <i v-else class="fa fa-file-text-o icon-file-detail" aria-hidden="true"></i>
                        </div>
                        <div class="margin-top-10">
                            {{file.title}}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import _ from "lodash";
import { queryFileList, getFileNums, onlinebafilepreview } from "../../service/review"
export default {
    data() {
        return {
            backData: [],
            aheadData: [],
            treeData: [],
            viewType: ["png","jpg","jpeg","txt"],
            selectTree: null,
            showFileData: [],
            fileData: [],
            preStep: 0,
            nextStep: 0,
            lastSelectFile: null,
            selectFile: null,
            currentTree: {},
            sjr: "",
            sjsj: ""
        }
    },
    mounted() {
        this.getTreeData()
    },
    methods: {
        // 获取数据
        getTreeData(){
            this.$loading.show("加载中...")
            if(this.$route.query.type == "xm"){
                onlinebafilepreview({chxmid: this.$route.query.chxmid}).then(res => {
                    this.$loading.close();
                    this.treeData = _.cloneDeep([{...res.data.dataList}]);
                    this.sjr = res.data.dataList.sjr
                    this.sjsj = res.data.dataList.sjsj
                    this.treeData[0].selected = true;
                    this.treeData = _.cloneDeep(this.setTreeData(this.treeData));
                    this.$nextTick(() => {
                        this.fileData = _.cloneDeep(this.treeData[0].children)
                        this.showFileData = _.cloneDeep(this.treeData[0].children)
                        this.selectTree = _.cloneDeep(this.treeData[0])
                    })
                })
            } else {
                queryFileList({glsxid: this.$route.query.mlkid}).then(res => {
                    this.$loading.close();
                    this.treeData = _.cloneDeep(res.data.dataList.lists);
                    this.sjr = res.data.dataList.sjr
                    this.sjsj = res.data.dataList.sjsj
                    this.treeData[0].selected = true;
                    this.treeData = _.cloneDeep(this.setTreeData(this.treeData));
                    this.$nextTick(() => {
                        this.fileData = _.cloneDeep(this.treeData[0].children)
                        this.showFileData = _.cloneDeep(this.treeData[0].children)
                        this.selectTree = _.cloneDeep(this.treeData[0])
                    })
                })
            }
        },
        // 修改树图标
        setTreeData(treeData){
            let data = []
            treeData.forEach(tree => {
                if(tree.type == '0' || !tree.type){
                    tree.render = (h, { root, node, data }) => {
                        return h('span', [
                            h('Icon', {
                                props: {
                                    type: 'icon fa fa-folder'
                                },
                                style: {
                                    marginRight: '8px'
                                }
                            }),
                            h('span', data.title)
                        ])
                    }
                } else {
                    tree.render = (h, { root, node, data }) => {
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
                }
                if(tree.children && tree.children.length){
                    tree.children = _.cloneDeep(this.setTreeData(tree.children))
                }
                data.push(tree)
            })
            return data;
        },
        // 树的展开和收起
        toggleTree(tree){
            if(tree.expand){
                tree.render = (h, { root, node, data }) => {
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
            } else {
                tree.render = (h, { root, node, data }) => {
                    return h('span', [
                        h('Icon', {
                            props: {
                                type: 'icon fa fa-folder'
                            },
                            style: {
                                marginRight: '8px'
                            }
                        }),
                        h('span', data.title)
                    ])
                }
            }
        },
        // 修改树的选中
        selectTreeChange(tree){
            if(tree[0]&&tree[0].children){
                this.fileData = _.cloneDeep(tree[0].children)
                this.showFileData = _.cloneDeep(this.fileData)
                this.preStep = 0;
                this.nextStep = 0;
                this.aheadData = [];
                this.backData = [];
                this.selectTree = _.cloneDeep(tree[0])
                this.selectFile = null;
            } else if(tree[0]){
                this.selectTree = _.cloneDeep(tree[0])
                this.showFileData = [];
                this.selectFile = null;
            } else if(this.selectTree){
                // this.selectFile = tree;
                this.preStep = 0;
                this.nextStep = 0;
                this.aheadData = [];
                this.backData = [];
                this.showFileData = _.cloneDeep(this.selectTree.children);
                this.treeData = _.cloneDeep(this.findTree(this.treeData,this.selectTree));
            }
        },
        // 修改第二次点击树时选中状态不变
        findTree(tree,select){
            let find;
            tree.forEach(t => {
                if((t.title + t.wjzxid)  === (select.title + select.wjzxid)){
                    t.selected= true;
                } else if(t.children && t.children.length){
                    this.findTree(t.children,select)
                }
            })
            return tree
        },
        // 后退
        backToPre(){
            let length = this.backData.length;
            this.preStep += 1;
            if(length > 0 && this.backData[length - this.preStep]){
                this.aheadData.push(_.cloneDeep(this.showFileData));
                this.showFileData = _.cloneDeep(this.backData[length - this.preStep])
                this.selectFile = this.findSelectFile(this.showFileData)
            } else {
                this.preStep -= 1;
            }
        },
        // 获取向上一级的数据
        getUpTree(treeData){
            let upTree = []
            this.showFileData.forEach(file => {
                delete file.active
            })
            
            treeData.forEach(tree => {
                if(tree.children && tree.children.length){
                    tree.children.forEach(t => {
                        if(_.isEqual(this.showFileData,t.children)){
                            upTree = _.cloneDeep(tree.children)
                        }
                    })
                    if(!upTree.length){
                        upTree = this.getUpTree(tree.children)
                    }
                }
            })
            return upTree
        },
        // 向上
        upToPre(){
            let upTree = _.cloneDeep(this.getUpTree(this.treeData));
            this.selectFile = null;
            this.showFileData = upTree.length ? upTree: this.showFileData;
        },
        // 前进
        goAheadToNext(){
            let length = this.aheadData.length;
            this.nextStep += 1;
            if(length > 0 && this.aheadData[length - this.nextStep]){
                this.backData.push(_.cloneDeep(this.showFileData))
                this.showFileData = _.cloneDeep(this.aheadData[length - this.nextStep])
                this.selectFile = this.findSelectFile(this.showFileData)
            } else {
                this.nextStep -= 1;
            }
        },
        // 双击打开
        dbClickHandler(file){
            if((file.type == '0' || !file.type)&& file.children){
                this.aheadData = [];
                this.nextStep = 0;
                if(!this.backData[this.backData.length - this.preStep - 1]){
                    this.backData = [];
                    this.preStep = 0;
                }
                this.selectFile = null;
                this.backData.push(_.cloneDeep(this.showFileData))
                this.showFileData = _.cloneDeep(file.children);
            } else {
                this.viewFile(file)
            }
        },
        // 获取选中项
        findSelectFile(fileData){
            let select = null;
            fileData.forEach(file => {
                if(file.active){
                    select = file
                } else if(file.children&&file.children.length&&!select){
                    select = this.findSelectFile(file.children)
                }
            })
            return select
        },
        // 点击整体body,取消选中
        handlerBgClick(){
            let showFileData = _.cloneDeep(this.showFileData)
            showFileData.forEach((f,i) => {
                f.active = false
            })
            this.selectFile = null;
            this.showFileData = _.cloneDeep(showFileData)
        },
        // 单击选中
        clickHandler(file,index){
            this.selectFile = _.cloneDeep(file);
            let showFileData = _.cloneDeep(this.showFileData)
            showFileData.forEach((f,i) => {
                f.active = false
                if(index === i){
                    f.active = true
                }
            })
            this.showFileData = _.cloneDeep(showFileData)
        },
        // 下载文件
        downloadFile(){
            let select = this.selectFile ? this.selectFile : this.selectTree
            if(select){
                if (!location.origin) {
                  location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
                }
                let params = {
                    wjzxid: select.wjzxid
                }
                getFileNums(params).then(res => {
                    let cyryid = select.cyryid || "";
                    let ssmkid = select.ssmkid || "";
                    location.href=location.origin + config.msurveyplatContext + '/fileoperation/download?wjzxid=' + select.wjzxid + "&cyryid=" + cyryid + "&ssmkid=" + ssmkid;
                })
            } else {
                layer.msg("暂无数据")
            }
        },
        // 预览文件
        viewFile(file){
           if (!location.origin) {
                location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
            }
            let href = location.origin + encodeURI(config.msurveyplatContext + '/fileoperation/onlinepreview?wjzxid=' + file.wjzxid + "&fileName=" + file.title);
            window.open(href);
        },
        // 查看
        viewDetail(){
            let select = this.selectFile ? this.selectFile : ""
            if(select){
                if(select.type == '0' || !select.type){
                    this.dbClickHandler(select)
                } else {
                    this.viewFile(select)
                }
            } else {
                layer.msg("未选择数据")
            }
        },
    },
}
</script>
<style scoped>
    @import "./review-fj.css";
    .content-left >>> .ivu-icon-ios-arrow-forward {
        font-family: "FontAwesome";
    }
    .content-left >>> .ivu-icon-ios-arrow-forward:before {
        content: "\f0da";
    }
    .content-left >>> .ivu-tree-title {
        width: calc(100% - 32px);
        height: 32px;
        line-height: 32px;
        border-radius: 0;
    }
    .content-left >>> .ivu-tree-title .ivu-icon {
        font-family: "FontAwesome";
        color: #1d87d1;
    }
    .content-left >>> .ivu-tree-title {
         overflow: hidden;
        text-overflow:ellipsis; 
        white-space: nowrap;
    }
    .content-left >>> .ivu-tree-arrow {
        height: 32px;
        line-height: 32px;
    }
    .content-left >>> .ivu-tree-title-selected, 
    .content-left >>> .ivu-tree-title-selected:hover,
    .content-left >>> .ivu-tree-title:hover {
        background-color: #f3f4f6;
    }
</style> 