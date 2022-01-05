class Perm {
    permList: any = {};
    constructor(permList:any){
        this.permList = permList
    }
    hasPerm(code: string){
        let permission = false
        for(let key in this.permList){
            if(key === code && this.permList[key] === "available"){
                permission = true
            }
        }
        return permission
    }
    formPerm(code: string, type: string){
        let permission = false
        for(let key in this.permList){
            if(key === code && this.permList[key] === type){
                permission = true
            }
        }
        return permission
    }
}
export default Perm;