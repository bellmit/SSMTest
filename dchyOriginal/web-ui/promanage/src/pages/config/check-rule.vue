<template>
    <div>
        <div class="form-title">
            <div class="list-title">检查规则配置</div>
            <div>
                <Button type="primary" v-if="!editrule" @click="handelRuleEdit" class="bdc-major-btn btn-h-34">编辑</Button>
                <Button type="primary" v-else @click="handelRuleSubmit" class="bdc-major-btn btn-h-34">保存</Button>
            </div>
        </div>
        <vue2-org-tree
            name="test"
            class="check-rule-tree"
            :data="ruleTreeData"
            :horizontal="horizontal"
            :collapsable="collapsable"
            :label-class-name="labelClassName"
            :render-content="renderRuleContent"
            @on-expand="onExpand"
        />
        <div class="line-dashed margin-bottom-10"></div>
        <div class="form-title">
            <div class="list-title">成果目录配置 <span v-if="editable">（</span><span v-if="editable" >可右击添加文件</span><span v-if="editable">）</span></div>
            <div>
                <Button type="primary" v-if="!editable" @click="handelEdit" class="bdc-major-btn btn-h-34">编辑</Button>
                <Button type="primary" v-else @click="handelSubmit" class="bdc-major-btn btn-h-34">保存</Button>
            </div>
        </div>
        <vue2-org-tree
            name="test"
            class="search-form"
            :data="treeData"
            :horizontal="horizontal"
            :collapsable="collapsable"
            :label-class-name="labelClassName"
            :render-content="renderContent"
            @on-expand="onExpand"
        />
        <div v-if="showMenu">
            <ul id="menu">
                <li @click="addFold" v-if="clickData&&clickData.FDM&&clickData.ZDLX=='CLSX'"><span>新增文件夹</span></li>
                <li @click="addFile" v-if="clickData&&clickData.WJLX=='1'"><span>新增文件</span></li>
                <li @click="deleteFile" v-if="clickData&&clickData.WJLX"><span>删除</span></li>
            </ul>
        </div>
    </div>
</template>
<script>
import _ from "loadsh";
import { queryCgjcgz, queryCgmlTree, editCgmlTree } from "../../service/config"
export default {
    data() {
        return {
            horizontal: true,
            collapsable: true,
            editable: false,
            showMenu: false,
            editrule: false,
            labelClassName: "bg-white",
            clickData: null,
            ruleTreeData: {
                id: 0,
                ywms: "检查规则",
                expand: true,
                children: [{
                    id: 1,
                    ywms: "检查类型选择",
                    expand: true,
                    children: []
                }]
            },
            treeData: {
                id: 0,
                MC: "成果目录配置",
                expand: true,
                children: []
            }
        }
    },
    mounted() {
        $("body").on("click",() => {
            this.showMenu = false;
        })
        this.queryCgjcgz();
        this.queryCgmlTree();
    },
    methods: {
        // 获取目录配置
        queryCgmlTree(){
            this.$loading.show("加载中...")
            queryCgmlTree().then(res => {
                this.$loading.close();
                if(res.data.data&&res.data.data.length) {
                    res.data.data[0].expand = true;
                    if(res.data.data[0].children){
                        res.data.data[0].children.forEach(c => {
                            c.children&&c.children.forEach(child => {
                                child.expand = true;
                            })
                            c.expand = true;
                        })
                    }
                }
                this.treeData.children = res.data.data || [];
            })
        },
        // 获取检查规则配置
        queryCgjcgz(){
            this.$loading.show("加载中...")
            queryCgjcgz().then(res => {
                this.$loading.close();
                res.data.data.forEach(r => {
                    r.type = "check"
                })
                this.ruleTreeData.children[0].children = res.data.data || []
            })
        },
        // 渲染检查规则树结构
        renderRuleContent(h, data) {
            const _self = this;
            return h('div',[
                data.type == "check" ? h('Checkbox',{
                    props: {
                        'disabled': !this.editrule,
                        'value': data.sfqy == '1' ? true: false
                    },
                    on: {
                        'on-change': (checked) => {
                            _self.handleCheckRule(data,checked)
                        }
                    }
                }) : "",
                h('span',data.ywms)
            ])
        },
        // 获取选中的树
        getCheckTree(treeData,data,checked){
            treeData.forEach(tree => {
                if(tree.ywyzxxid == data.ywyzxxid){
                    data.checked = checked;
                    if(data.checked){
                        data.sfqy = "1"
                    } else {
                        data.sfqy = "0"
                    }
                    if(data.children){
                        data.children.forEach(c => {
                            c.checked = checked;
                            if(c.checked){
                                c.sfqy = "1"
                            } else {
                                c.sfqy = "0"
                            }
                        })
                    }
                } else if(tree.children){
                    this.getCheckTree(tree.children,data,checked)
                }
            })
            return treeData;
        },
        // 修改勾选项
        handleCheckRule(data,type){
            let ruleTreeData = {...this.ruleTreeData};
            this.ruleTreeData = {
                ...this.ruleTreeData,
                children: this.getCheckTree(ruleTreeData.children,data,type)
            };
        },
        // 渲染成果目录配置树结构
        renderContent(h, data) {
            const _self = this;
            let inputValue = "";
            return h('div',[
                !data.edit ? h('div',{
                    style: {
                        color: data.CLCGPZID&&this.editable ? "#2d5de1" :"#333"
                    },
                    on: {
                        contextmenu: (event) => {
                            if(data.CLCGPZID&&this.editable){
                                event.preventDefault();
                                _self.handleRightClick(data, event)
                            }
                        }
                    }
                }, data.MC||data.CLMC): h('div',[
                    h("Input", {
                        style: {
                            width: "120px"
                        },
                        on: {
                            "on-change": (e) => {
                                inputValue = e.target.value
                            }
                        }
                    }),
                    h("span", [
                        h("Icon",{
                            style: {
                                marginLeft: "5px",
                                fontSize: "18px",
                                cursor: "pointer"
                            },
                            props: {
                                type: "md-checkmark"
                            },
                            on: {
                                click: () => {
                                    if(!inputValue){
                                        layer.msg("不能为空")
                                        return;
                                    }
                                    let treeData = {..._self.treeData}
                                    _self.treeData = {
                                        ..._self.treeData,
                                        children: _self.handleTreeData(treeData.children,data,"saveFile",inputValue)
                                    }
                                }
                            }
                        }),
                        h("Icon",{
                            style: {
                                marginLeft: "5px",
                                fontSize: "18px",
                                cursor: "pointer"
                            },
                            props: {
                                type: "md-close"
                            },
                            on: {
                                click: () => {
                                    let treeData = {..._self.treeData}
                                    _self.treeData = {
                                        ..._self.treeData,
                                        children: _self.handleTreeData(treeData.children,data,"delete")
                                    }
                                }
                            }
                        })
                    ])
                ])
            ])
        },
        // 成果目录配置的添加删除操作
        handleTreeData(treeData,data,type,value){
            treeData.forEach((tree,index) => {
                if(tree.CLCGPZID == data.CLCGPZID){
                    if(type == "delete"){
                        treeData.splice(index,1)
                    } else if(type == "addFile"){
                        if(!tree.children){
                            tree.children = []
                        }
                        tree.children.push({
                            CLCGPZID: tree.CLCGPZID + tree.children.length + 1,
                            CLMC: "",
                            WJLX: "2",
                            CLSX: this.clickData.CLSX,
                            expand: true,
                            edit: true
                        })
                    } else if(type == "saveFile"){
                        tree.CLMC = value;
                        tree.edit = false;
                    } else if(type == "addFold"){
                        if(!tree.children){
                            tree.children = []
                        }
                        tree.children.push({
                            CLCGPZID: tree.CLCGPZID + tree.children.length + 1,
                            MC: "",
                            WJLX: "1",
                            CLSX: this.clickData.DM,
                            expand: true,
                            edit: true,
                            children: []
                        })
                    }
                } else if(tree.children){
                    this.handleTreeData(tree.children,data,type,value)
                }
            })
            return treeData;
        },
        // 新增文件夹
        addFold(){
            let treeData = {...this.treeData}
            this.treeData = {
                ...this.treeData,
                children: this.handleTreeData(treeData.children,this.clickData,"addFold")
            }
        },
        // 新增文件
        addFile(){
            let treeData = {...this.treeData}
            this.treeData = {
                ...this.treeData,
                children: this.handleTreeData(treeData.children,this.clickData,"addFile")
            }
        },
        // 删除文件
        deleteFile(){
            layer.confirm("确认删除 "+ (this.clickData.CLMC || this.clickData.MC) +"?",(index) => {
                layer.close(index)
                let treeData = {...this.treeData}
                this.treeData = {
                    ...this.treeData,
                    children: this.handleTreeData(treeData.children,this.clickData,"delete")
                }
            })
        },
        // 展开收起
        collapse(list,expand) {
            let _this = this;
            list.forEach(function(child) {
                _this.$set(child, "expand", expand);
                child.children && _this.collapse(child.children,expand);
            });
            this.$forceUpdate();
        },
        // 展开收起操作
        onExpand(e,data) {
            if ("expand" in data) {
                data.expand = !data.expand;
                if (data.expand && data.children) {
                    this.collapse(data.children,true);
                }
                if (!data.expand && data.children) {
                    this.collapse(data.children,false);
                }
                this.$forceUpdate();
            } else {
                this.$set(data, "expand", true);
                if (data.children) {
                    this.collapse(data.children,true);
                }
            }
        },
        // 右击
        handleRightClick(data, e){
            this.clickData = data;
            this.showMenu = true;
            this.$nextTick(() => {
                // 获取节点
                var menu = document.getElementById('menu');

                //获取可视区宽度,高度
                var winWidth = document.documentElement.clientWidth || document.body.clientWidth;
                var winHeight = document.documentElement.clientHeight || document.body.clientHeight;
                var e = e || window.event;
                // 获取鼠标坐标
                var mouseX = e.clientX;
                var mouseY = e.clientY;

                // 判断边界值，防止菜单栏溢出可视窗口
                if (mouseX >= (winWidth - menu.offsetWidth)) {
                    mouseX = winWidth - menu.offsetWidth;
                } else {
                    mouseX = mouseX
                }
                let scrollTop = 0;
                if(document.documentElement&&document.documentElement.scrollTop){
                    scrollTop = document.documentElement.scrollTop;
                }else if(document.body){
                    scrollTop = document.body.scrollTop;
                }
                if (mouseY > winHeight - menu.offsetHeight) {
                    mouseY = winHeight - menu.offsetHeight;
                } else {
                    mouseY = mouseY + scrollTop;
                }
                menu.style.left = mouseX + 'px';
                menu.style.top = mouseY + 'px';
                return false;
            })
        },
        // 编辑
        handelEdit(){
            this.editable = true;
        },
        // 编辑检查规则
        handelRuleEdit(){
            this.editrule = true;
        },
        // 保存检查规则
        handelRuleSubmit(){
            this.handelSubmit();
        },
        // 获取保存的数据
        getClParams(){
            let clParams = []
            this.treeData.children.forEach(tree => {
                let newTree = {};
                newTree.clcgpzid = tree.CLCGPZID || null;
                newTree.pclcgpzid = tree.PCLCGPZID || null;
                newTree.children = _.cloneDeep(tree.children);
                newTree.clmc = tree.MC;
                newTree.wjlx = "1";
                newTree.clsx = tree.DM;
                if(newTree.children){
                    newTree.children.forEach(child => {
                        let newC = {...child};
                        for(let key in child){
                            delete child[key]
                        }
                        child.clsx = newC.DM;
                        child.pclcgpzid = newC.PCLCGPZID || null;
                        child.clcgpzid = newC.CLCGPZID || null;
                        child.clmc = newC.MC;
                        child.children = _.cloneDeep(newC.children);
                        child.wjlx = "1";
                        child.children&&child.children.forEach(c => {
                            for(let key in c){
                                c[_.toLower(key)] = c[key]
                                if(key != 'children'){
                                    delete c[key]
                                }
                            }
                            c.pclcgpzid = c.pclcgpzid || null;
                            c.children&&c.children.forEach(list => {
                                for(let key in list){
                                    list[_.toLower(key)] = list[key]
                                    delete list[key]
                                }
                                list.pclcgpzid = list.pclcgpzid || null;
                                list.children = list.children || null;
                            })
                        })
                    })
                }
                clParams.push(newTree)
            })
            return clParams
        },
        // 保存
        handelSubmit(){
            let clParams = this.getClParams()
            let params = {
                "dchyXmglLcgpzs": clParams,
                "dchyXmglJcgzs": this.ruleTreeData.children[0].children
            }
            this.$loading.show("保存中...")
            editCgmlTree(params).then(res => {
                this.editable = false;
                this.editrule = false;
                this.$loading.close();
            })
        }
    },
}
</script>
<style scoped>
.form-title {
  display: flex;
  justify-content: space-between;
}
#menu {
    width: 180px;
    position: absolute;
    z-index: 100;
    background-color: #fff;
    box-shadow: 2px 2px 8px 1px rgba(0, 0, 0, .2);
}
#menu li {
    list-style: none;
    width: 100%;
    cursor: pointer;
}
#menu li span {
    display: inline-block;
    text-decoration: none;
    color: #555;
    width: 100%;
    padding: 7px 10px;
    cursor: pointer;
}
#menu li:first-of-type {
    border-radius: 5px 5px 0 0;
}
#menu li span:hover{
    background: #eee;
}
.check-rule-tree >>> .horizontal .org-tree-node:after {
    width: 120px!important;
}
.check-rule-tree >>> .horizontal .org-tree-node {
    padding-left: 121px!important;
}
.check-rule-tree >>> .org-tree > .org-tree-node {
    padding-left: 51px!important;
}
.check-rule-tree >>> .org-tree > .org-tree-node > .org-tree-node-children > .org-tree-node {
    padding-left: 31px!important;
}
.search-form {
    overflow-x: auto;
}
</style>