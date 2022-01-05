package cn.gtmap.onemap.platform.event;

/**
 * exception for cmsService
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/9/24 13:50
 */
public class CmsException extends RuntimeException {

    private static final long serialVersionUID = -523441063034361848L;

    private Integer code;

    private String msg;

    public CmsException(Integer errorCode) {
        this.code = errorCode;
        this.msg=getDesc();
    }

    public CmsException(Integer errorCode,String errorMsg){
        this.code=errorCode;
        this.msg=errorMsg;
    }

    @Override
    public String getMessage() {
        return "异常代码:『"+code+"』"+"异常描述:『"+msg+"』";
    }

    /***
     *
     * @return
     */
    private String getDesc(){
        switch (code){
            case 10000:
                return "操作失败";
            case 10001:
                return "用户名不存在";
            case 10002:
                return "密码错误";
            case 10003:
                return "用户名已经登录";
            case 10004:
                return "用户名称不唯一";
            case 10005:
                return "用户已经过期";
            case 10006:
                return "用户已被删除";
            case 10007:
                return "P/MAC 不匹配 （或者用户已经过期，在通过IP/MAC获取用户信息的时候只获取正常用户）";
            case 10008:
                return "用户已经冻结";
            case 10009:
                return "用户无权限";
            case 10010:
                return "用户会话ID不存在";
            case 20001:
                return "索引编号已经存在";
            case 20002:
                return "父ID错误";
            case 20003:
                return "ID值不存在";
            case 20004:
                return "用户名已经存在";
            case 20005:
                return "系统内部错误";
            case 20006:
                return "Index code错误";
            case 20007:
                return "传递的参数错误";
            case 30001:
                return "查询不到内容";
            case 30002:
                return "访问被拒绝";
            case 30003:
                return "数据库访问异常";
            default:
                return null;
        }
    }
}
