<template>
    <div >
        <div class="form-title">
            <div class="list-title">添加通知公告信息</div>
            <div>
                <Button type="primary" class="btn-h-34 bdc-major-btn" v-if="!readonly" @click="saveAnnouce()">确定</Button>
                <Button class="btn-h-34 margin-left-20 btn-cancel" @click="cancel">返回</Button>
            </div>
        </div>
        <Form class="form-edit" @on-validate="validateChecked" ref="announcementForm" :model="newAnnouncement" :rules="newRules">
            <FormItem label="标题" prop="bt"  :label-width="140">
                <Input type="text" :readonly="readonly" style="width: 800px" v-model="newAnnouncement.bt" placeholder=""/>
            </FormItem>
            <FormItem label="类型" prop="gglx" :label-width="140">
                <Select :disabled="readonly" v-model="newAnnouncement.gglx" style="width:400px">
                    <Option v-for="item in typeList" :value="item.DM" :key="item.DM">{{ item.MC }}</Option>
                </Select>
            </FormItem>
            <FormItem label="设置" prop="sfzdd"  :label-width="140">
                <Checkbox :disabled="readonly" v-model="newAnnouncement.sfzdd">置顶</Checkbox>
            </FormItem>
            <FormItem label="内容" v-model="newAnnouncement.ggnr" prop="ggnr" :label-width="140">
                <div id="toolbar" class="toolbar"></div>
                <div id="newsContent" class="text"></div>
            </FormItem>
        </Form>
        <div class="form-title">
            <div class="list-title">附件材料</div>
        </div>
        <uploadFile
            v-if="!readonly"
            :glsxid="tzggid"
            ref="upload-file"
            :ssmkid="ssmkid"
        ></uploadFile>
        <uploadFileInfo
            v-if="readonly"
            :glsxid="tzggid"
            :ssmkid="ssmkid"
        ></uploadFileInfo>
    </div>
</template>
<script>
import E from 'static/wangEditor/wangEditor'
import { saveAnnouncement, deleteAnnouncement, queryAnnounceDetail }  from '../../../service/announcement'
import { getDictInfo } from "../../../service/mlk"
import uploadFile from "../../../components/survey/upload-file"
import uploadFileInfo from "../../../components/survey/upload-file-info"
import _ from "loadsh"
import yz_mixins from "../../../service/yz_mixins"
export default {
    components: {
        uploadFile,
        uploadFileInfo
    },
    mixins: [yz_mixins],
    data() {
        return {
            newAnnouncement: {
                bt: "",
                gglx: "",
                sfzd: false,
                ggnr: ""
            },
            typeList: [],
            ssmkid: "7",
            bdid: "7",
            newRules: {
                bt: {
                    required: true,
                    message: "必填项不能为空"
                },
                gglx: {
                    required: true,
                    message: "必填项不能为空"
                },
                ggnr: {
                    required: true,
                    message: "必填项不能为空"
                }
            },
            $selectedImg: "",
            fileList: [],
            tzggid: "",
            readonly: false
        }
    },
    mounted() {
        if(this.$route.query.tzggid){
            this.tzggid = this.$route.query.tzggid;
        }
        if(this.$route.query.type == "view"){
            this.readonly = true;
        }
        this.newEditor();
        if(this.$route.query.type != "add"){
            this.queryAnnounceDetail();
        }
        this.getGglxList();
    },
    methods: {
        queryAnnounceDetail(){
            let params = {
                tzggid: this.tzggid
            }
            queryAnnounceDetail(params).then(res => {
                let info = res.dataList
                this.editor.txt.html(info.ggnr)
                this.newAnnouncement = {...info}
                this.newAnnouncement.sfzdd = info.sfzd == "1" ? true : false
            })
        },
        // 获取公告类型字典项
        getGglxList(){
            getDictInfo({zdlx: ["GGLX"]}).then(res => {
                this.typeList = res.data.dataList;
            })
        },
        // new edtor编辑器
        newEditor(){
            let _self = this;
            _self.editor = new E('#toolbar', '#newsContent');  // 两个参数也可以传入 elem 对象，class 选择器
            _self.editor.customConfig.uploadImgShowBase64 = true;  //可以以base64存储图片
            _self.editor.customConfig.pasteFilterStyle = false; //关闭掉粘贴样式的过滤
            _self.editor.customConfig.uploadImgMaxSize = 2 * 1024 * 1024;  //图片大小限制
            _self.editor.create();
            if(this.readonly){
                _self.editor.$textElem.attr('contenteditable', false)
            }
        },
        handleUpload(file){
            this.fileList.push(file)
            return false;
        },
        // 删除初始化数据
        deleteInitData(){
            let params = {
                tzggid: this.tzggid
            }
            this.$loading.show("加载中...")
            deleteAnnouncement(params).then(res => {
                this.$loading.close();
                this.$router.push('/admin/announcement')
            })
        },
        // 取消
        cancel(){
            if(this.$route.query.type == "add"){
                this.deleteInitData()
            } else {
                this.$router.push('/admin/announcement')
            }
        },
        // 保存公告
        saveAnnouce(){
            this.unLastSubmit = false;
            this.newAnnouncement.ggnr = this.editor.txt.html()
            let wjzxid = this.$refs["upload-file"].getWjzxid()
            this.$refs["announcementForm"].validate(valid => {
                this.unLastSubmit = true;
                if(valid){
                    this.newAnnouncement.tzggid = this.tzggid;
                    this.newAnnouncement.sfzd = this.newAnnouncement.sfzdd ? "1":"0";
                    this.newAnnouncement.wjzxid = wjzxid;
                    // this.yzYwlj(this.newAnnouncement,this.saveData)
                    let validate = this.$refs["upload-file"].validate();
                    if(!validate){
                        this.sveData()
                    }
                }
            })
        },
        sveData(){
            this.$loading.show("加载中...")
            saveAnnouncement(this.newAnnouncement).then(res => {
                this.$loading.close();
                layer.msg("保存成功")
                this.$router.push("/admin/announcement")
            })
        }
    },
}
</script>
<style lang="less" scoped>
    @import "./announcement.less";
</style>
<style scoped>
    .form-edit >>> .ivu-select .ivu-select-dropdown {
        z-index: 10002;
    }
    .form-title {
        display: flex;
        justify-content: space-between;
    }
</style>