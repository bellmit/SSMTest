import util from './util'
import { getYzList } from "./publish"
let yz_mixins_common = {
    data () {
        return {
            btxyzList: [],
            ywljyzList: [],
            ruleInline: {},
            errorList: [],
            unLastSubmit: true,
            error: ""
        }
    },
    props: {
        ssmkid: {
            type: String,
            default: ""
        },
        bdid: {
            type: String,
            default: ""
        }
    },
    created() {
        if(this.ssmkid&&this.bdid){
            getYzList({ssmkid: this.ssmkid,bdid: this.bdid}).then(res => {
                if(res.data){
                    this.ruleInline = util.getBtxYzRuleInline(this.ruleInline,res.data.btxyzList)
                    this.btxyzList = res.data.btxyzList
                    this.ywljyzList = res.data.ywljyzList
                }
            })
        }
    },
    methods: {
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
    },
}

export default yz_mixins_common