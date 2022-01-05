import util from './util'
import { getYzList, yzYwlj } from "./home"
let yz_mixins = {
    data () {
        return {
            btxyzList: [],
            ywljyzList: [],
            ssmkid: "",
            bdid: "",
            yzsxid: "",
            ruleInline: {}
        }
    },
    created() {
        this.getYzList();
    },
    methods: {
        getYzList(){
            getYzList({ssmkid: this.ssmkid,bdid: this.bdid}).then(res => {
                this.btxyzList = res.data.btxyzList
                this.ywljyzList = res.data.ywljyzList
                this.yzsxid = res.data.yzsxid
                this.ruleInline = util.getBtxYzRuleInline(this.ruleInline,this.btxyzList)
            })
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