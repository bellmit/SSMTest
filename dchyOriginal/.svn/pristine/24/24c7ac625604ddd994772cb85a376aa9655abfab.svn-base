import { getAppUserInfo, disPatcherCheck } from "../service/home"
let user_mixins = {
    data () {
        return {
            ticket: "",
            username: "",
            isLogin: false
        }
    },
    created() {
        let self = this;
        lightAppJssdk.user.getTicket ({
            success:function(data){ //成功回调
                data = {};
                data.ticket = "cb75a63ba5a14c48ad20e3f03d52cc59"
                if(data.ticket){
                    self.ticket = data.ticket
                    self.getAppUserInfo();
                } else { }
            },
            fail:function(data){ //错误返回
            }
        });
    },
    methods: {
        toLogin(){
            lightAppJssdk.user.loginapp ({
                success:function(data){ //成功回调
                },
                fail:function(data){ //错误返回
                }
            });
        },
        disPatcherCheck(_func){
            let params = {
                ticket: this.ticket
            }
            this.$toast.loading({
                message: '加载中...',
                forbidClick: true,
            });
            disPatcherCheck(params).then(res => {
                this.$toast.clear();
                if(res.data.loginStatus == "appNotLogin"){
                    this.toLogin();
                } else if(res.data.loginStatus == "appLogin"){
                    this.$router.push({
                        path: "/authorize",
                        query: {
                            glsxid: res.data.glsxid
                        }
                    })
                } else if(res.data.loginStatus == "serviceLogin"){
                    _func();
                }
            })
        },
        getAppUserInfo(){
            let params = {
                ticket: this.ticket
            }
            getAppUserInfo(params).then(res => {
                if(res.data.id || res.data.dwmc || res.data.username){
                    this.username = res.data.username || res.data.dwmc || "";
                    this.isLogin = true;
                }
            })
        },
    },
}

export default user_mixins