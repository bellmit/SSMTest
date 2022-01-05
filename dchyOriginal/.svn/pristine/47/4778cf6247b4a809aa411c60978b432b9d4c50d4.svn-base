import util from './util'
import { getYzList, yzYwlj } from "./manage"
let yz_mixins = {
    data () {
        return {
            btxyzList: [],
            ywljyzList: [],
            ssmkid: "19",
            bdid: "9",
            error: "",
            unLastSubmit: true,
            ruleInline: {}
        }
    },
    created() {
        this.getYzList();
    },
    methods: {
        getYzList(){
            getYzList({ssmkid: this.ssmkid,bdid: this.bdid}).then(res => {
                if(res.data){
                    this.btxyzList = res.data.btxyzList
                    this.ywljyzList = res.data.ywljyzList
                    this.ruleInline = util.getBtxYzRuleInline(this.ruleInline,this.btxyzList)
                }
            })
        },
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error&&this.error!=error&&!this.unLastSubmit){
                this.error = error
                this.$error.show(error);
                setTimeout(() => {
                    this.error = ""
                    this.$error.close();
                },1000)
            }
        },
        validateWithBlur(prop, status, error){
            if(error&&this.error!=error){
                this.error = error
                this.$error.error(error);
                setTimeout(() => {
                    this.error = ""
                    this.$error.close();
                },1000)
            }
        },
        yzYwlj(params, _fn){
            let yzParams = {
                data: {...params},
                yzsxid: this.ywljyzList,
                ssmkid: this.ssmkid,
                bdid: this.bdid
            }
            yzYwlj(yzParams).then(res => {
                if(res.data.yzjg){
                    _fn()
                } else {
                    if(res.data.yzms == "2"){
                        layer.msg(res.data.yztsxx)
                    }else if(res.data.yzms == "1"){
                        layer.confirm(res.data.yztsxx + ",是否确认继续保存?",() => {
                            _fn()
                        })
                    }
                }
            })
        }
    },
}

export default yz_mixins