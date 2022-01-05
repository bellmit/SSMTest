<template>
    <div class="form-edit review">
        <div class="review-title">审核意见</div>
        <Form :model="reviewData" @on-validate="validateChecked" :rules="reviewRule" ref="review-info" label-position="left" :label-width="240">
            <FormItem v-model="reviewData.shjg" prop="shjg" label="审核结果：">
                <RadioGroup v-model="reviewData.shjg" @on-change="changeShjg">
                    <Radio label="pass">通过</Radio>
                    <Radio label="back">退回</Radio>
                </RadioGroup>
            </FormItem>

            <FormItem v-model="reviewData.blyj" prop="blyj" label="审核意见：">
                <Input v-model="reviewData.blyj" :rows="3" type="textarea" placeholder=""/>
            </FormItem>
            <FormItem v-model="reviewData.shr" prop="shr" label="审核人：">
                <img :src="signImg"  v-if="signImg" alt="" style="float: left; width: 130px; height: 49px; margin-top: -9px">
                <div style="float: right; margin-top:-18px">
                    <span class="fa fa-pencil" @click="sign" style="cursor:pointer">签名</span>
                    <br/>
                    <span class="fa fa-trash" @click="remove" style="cursor:pointer">删除</span>
                </div>
            </FormItem>
            <FormItem v-model="reviewData.jssj" prop="jssj" label="审核时间：">
                <!-- <DatePicker type="date" v-model="reviewData.jssj" placeholder=""></DatePicker> -->
                <span>{{nowtime}}</span>
            </FormItem>
        </Form>
    </div>
</template>
<script>
import { getSignxx } from "../../service/review";
import { getSign } from "../../service/review";
import { removeSign } from "../../service/review";
export default {
    props: {
        reviewData: {
            type: Object,
            default: () => {
                return {}
            }
        }
    },
    data() {
        return {
            signImg:"",
            reviewRule: {
                shjg: {
                    required: true,
                    message: "请选择审核结果"
                },
                blyj: {
                    required: false,
                    message: "审核意见不能为空"
                },
                // shr: {
                //     required: false,
                //     message: "审核人不能为空"
                // },
                // jssj: {
                //     required: false,
                //     message: "审核时间不能为空"
                // }
            },
            form:{
                dbrwid:"",
                dqjd:""
            },
            signId:"",
            timeInterval: "",
            nowtime: '',//当前日期时间
            
        }
    },
    mounted() {
        this.form.dbrwid = this.$route.query.dbrwid;
        this.form.dqjd = "sh";
        this.getInfo();
    },
    beforeDestroy(){
        clearInterval(this.timeInterval)
    },
    methods: {
        getInfo(){
            getSignxx(this.form).then(res => {
                this.signId = res.data.qmid;
            })
        },
        validate(){
            let validate = true;
            this.$refs["review-info"].validate(valid => {
                validate = valid;
            })  
            return validate;
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error){
                this.$error.show(error)
            }
        },
        changeShjg(select){
            this.$emit("shjgchange",select)
            if(select == "back"){
                this.reviewRule.blyj.required = true
            }else {
               this.reviewRule.blyj.required = false 
            }
        },
        // 确认签名
        sign(){
            getSignxx(this.form).then(res => {
                this.signId = res.data.qmid;
                if (!location.origin) {
                    location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
                }
                this.signImg = location.origin + "/msurveyplat-serviceol/shxx/sign/" +this.signId
            })
            
        },
        // 删除签名
        remove(){
            getSignxx(this.form).then(res => {
                this.signId = res.data.qmid;
                removeSign({signId: this.signId}).then(resRemove => {
                this.signImg = "";
            })
            })
            
        },
        getTime(){
				this.timeInterval = setInterval(()=>{
					//new Date() new一个data对象，当前日期和时间
					//toLocaleString() 方法可根据本地时间把 Date 对象转换为字符串，并返回结果。
                    this.nowtime = new Date().toLocaleString();
				},1000)
			},
      
    },
    created(){
			this.getTime();
		}
}
</script>
<style scoped>
    .review {
        max-width: 800px;
        min-width: 740px;
        margin: 0 auto;
    }
    .review >>> .ivu-form-item {
        margin-bottom: 0!important;
        height: 50px;
        min-width: 740px;
        border: 1px solid rgb(247, 247, 247);
    }
    .review >>> .ivu-form-item:nth-child(2){
        height: 100px;
    }
    .review >>> .ivu-form-item:nth-child(n+2) {
        border-top: none;
    }
    .review >>> .ivu-form .ivu-form-item-label {
        height: 100%;
        line-height: 2;
        background-color: rgb(240, 239, 239);
        text-align: center;
    }
    .review >>> .ivu-form .ivu-form-item:nth-child(2) .ivu-form-item-label {
        line-height: 80px;
    }
    .review >>> .ivu-form .ivu-form-item-content {
        padding: 10px;
        min-width: 500px;
    }
    .review-title {
        width: 100%;
        text-align: center;
        padding: 60px 0 30px;
        font-weight: bold;
        color: #7F7F7F;
        font-size: 18px;
    }
</style>