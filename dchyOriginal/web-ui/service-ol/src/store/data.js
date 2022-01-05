const data = {
    state: {
        userInfo: {},
        mlkEditInfo: null,
        commissionEditInfo: null
    },
    setUserInfoAction(newValue){
        this.state.userInfo = {...newValue}
    },
    setMlkInfo(value){
        this.state.mlkEditInfo = value ? {...value}: null
    },
    setCommissionInfo(value){
        this.state.commissionEditInfo = value ? {...value} : null
    },
    getMlkInfo(){
        return this.state.mlkEditInfo
    },
    getCommissionInfo(){
        return this.state.commissionEditInfo
    }
}
export default data;