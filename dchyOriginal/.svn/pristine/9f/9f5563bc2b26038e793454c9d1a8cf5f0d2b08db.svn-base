<template>
    <div>
        <div class="search-form">
            <Form ref="formInline" :model="formInline" :label-width="120" :rules="ruleInline" inline>
                <Row>
                    <i-col span="6">
                        <FormItem label="用户名" prop="username" class="form-list-search">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getUserList(1,formInline.size)" v-model="formInline.username" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem label="联系人" prop="lxr" class="form-list-search">
                            <Input type="text" class="form-search-item" @keydown.enter.native.prevent="getUserList(1,formInline.size)" v-model="formInline.lxr" placeholder=""/>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="20">
                            <Button type="primary" class="btn-h-32 bdc-major-btn margin-left-20" @click="getUserList(1,formInline.size)">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="margin-top-10">
            <Table
                :cols="tableCols" 
                :data="userList" 
                :size="formInline.size" 
                :page="formInline.page" 
                :count="totalNum"
                :operation="operationList"
                :func="getUserList"
                @disable="disableUser"
                @enable="enableUser"
                @deleteOpr="deleteOpr"
                @role="editRole"
            ></Table>
        </div>
        <Modal
            class="modal-base form-role"
            v-model="visible"
            :title="'修改角色'"
            width="540"
            :mask-closable="false"
            :footer-hide="true"
            closable
        >
            <Form class="form-edit" @on-validate="validateChecked" ref="role-form" :model="roleForm" :rules="fileRule" :label-width="114" >
                <FormItem v-model="roleForm.username" prop="username" label="用户名 ">
                    <Input v-model="roleForm.username" readonly style="width: 85%" placeholder=""/>
                </FormItem>
                <FormItem v-model="roleForm.lxr" prop="lxr" label="联系人 ">
                    <Input v-model="roleForm.lxr" readonly style="width: 85%" placeholder=""/>
                </FormItem>
                <FormItem v-model="roleForm.lxdh" prop="lxdh" label="联系电话 ">
                    <Input v-model="roleForm.lxdh" readonly style="width: 85%" placeholder=""/>
                </FormItem>
                <!-- <FormItem v-model="roleForm.sfqy" readonly prop="sfqy" label="是否启用 ">
                    <RadioGroup v-model="roleForm.sfqy">
                        <Radio disabled :label="1">启用中</Radio>
                        <Radio disabled :label="0">未启用</Radio>
                    </RadioGroup>
                </FormItem> -->
                <FormItem v-model="roleForm.role" prop="role" label="角色 ">
                    <Select v-model="roleForm.role" style="width: 85%" filterable multiple>
                        <Option v-for="item in roleList" :key="item.roleId" :value="item.roleId">{{item.roleName}}</Option>
                    </Select>
                </FormItem>
            </Form>
            <div class="save-btn">
                <Button type="primary" class="btn-h-36" @click="role()">确认</Button>
                <Button class="margin-left-10 btn-h-36" @click="cancelRole()">取消</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import { queryUserList, changeUserState, queryRoleList, postRole } from "../../../service/user"
export default {
    data() {
        const _self = this;
        return {
            formInline: {
                username: "",
                size: 10,
                page: 1
            },
            userList: [],
            roleForm: {},
            fileRule: {
                role: {
                    required: true,
                    message: "必填项不能为空"
                }
            },
            roleList: [],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "USERNAME",
                    title: "用户名",
                    align: "center",
                },
                {
                    field: "LXR",
                    title: "联系人",
                    align: "center",
                },
                {
                    field: "LXDH",
                    title: "联系电话",
                    align: "center",
                },
                {
                    field: "JS",
                    title: "角色",
                    align: "center",
                },
                {
                    field: "SFQY",
                    title: "是否启用",
                    align: "center",
                    templet: function(d){
                        return d.SFQY == "1" ? "启用中" : "未启用"
                    }
                },
                {
                    title: "操作",
                    align: "center",
                    minWidth: 220,
                    toolbar: "#operation"
                }
            ],
            totalNum: 0,
            operationList: ["disable","enable","role"],
            ruleInline: {},
            visible: false,
            selectUser: {},
            error: "",
            unLastSubmit: true
        }
    },
    mounted() {
        this.getUserList();
        this.queryRoleList();
    },
    methods: {
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&!this.unLastSubmit){
                this.$error.show(error)
            }
        },
        // 获取角色
        queryRoleList(){
            queryRoleList().then(res => {
                this.roleList = res.data.data
            })
        },
        // 重置查询表单
        resetForm(){
            this.formInline = {
                page: this.formInline.page,
                size: this.formInline.size
            }
        },
        // 获取用户列表
        getUserList(page,size){
            if(page){
                this.formInline.page = page
                this.formInline.size = size
            }
            this.$loading.show("加载中...")
            queryUserList(this.formInline).then(res => {
                this.$loading.close();
                this.userList = res.data.dataList;
                this.totalNum = res.data.totalNum;
            })
        },
        // 删除操作
        deleteOpr(obj) {
            this.userList.forEach((list, index) => {
                if (list.SFQY == "1") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='enable']").remove();
                } else if (list.SFQY == "0") {
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='disable']").remove();
                    $(".layui-table:eq(1) tr:eq(" + index + ")").find("td").last().find("span[lay-event='role']").addClass("table-btn-disabled cursor-not-allowed");
                }
            });
        },
        // 修改角色
        editRole(data){
            if(data.SFQY == "0"){
                return
            }
            this.selectUser = data;
            this.roleForm = {
                username: data.USERNAME,
                lxr: data.LXR,
                lxdh: data.LXDH,
                sfqy: data.SFQY,
                role: data.ROLES.split(",")
            }
            this.visible = true;
        },
        // 确定修改角色
        role(){
            this.unLastSubmit = false;
            this.$refs["role-form"].validate(valid => {
                setTimeout(() => {
                    this.unLastSubmit = true
                },500)
                if(valid){
                    let params = {
                        userid: this.selectUser.USERID,
                        roles: this.roleForm.role.join(";")
                    }
                    this.$loading.show("加载中...")
                    postRole(params).then(res => {
                        this.$loading.close();
                        this.visible = false;
                        this.getUserList();
                    })
                }
            })
        },
        cancelRole(){
            this.visible = false;
        },
        // 禁用
        disableUser(item){
            layer.confirm("确认禁用该用户?",(index) => {
                layer.close(index)
                let params = {
                    userid: item.USERID,
                    state: "0"
                }
                this.$loading.show("加载中...")
                changeUserState(params).then(res => {
                    layer.msg("禁用成功")
                    this.$loading.close();
                    this.getUserList();
                })
            })
        },
        // 启用
        enableUser(item){
            layer.confirm("确认启用该用户?",(index) => {
                layer.close(index)
                let params = {
                    userid: item.USERID,
                    state: "1"
                }
                this.$loading.show("加载中...")
                changeUserState(params).then(res => {
                    layer.msg("启用成功")
                    this.$loading.close();
                    this.getUserList();
                })
            })
        }
    },
}
</script>
<style scoped>
    .form-role .form-edit {
        padding: 0 10px!important;
    }
</style>