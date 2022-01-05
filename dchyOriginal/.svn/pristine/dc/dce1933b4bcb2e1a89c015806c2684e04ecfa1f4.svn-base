package cn.gtmap.msurveyplat.portalol.model.token;

public enum ReturnCode {

    SUCCESS("0000","成功"),
    MESSAGE_DISABLE("1111","短信验证已禁用"),
    INTERFACE_DISABLE("1111","接口验证已禁用"),
    SEND_WRONG("1112","短信发送失败"),
    PARAM_WRONG("20001", "短信参数异常!"),
    YZM_WRONG("20002", "验证码不正确!"),
    TOKEN_WRONG("30001","TOKEN异常!"),
    TOKEN_EXPIRE("30002","验证码异常!"),
    SYSTEM_WRONG("4000","系统异常");

    /** 返回状态码 */
    private String code;

    /** 返回消息 */
    private String message;

    private ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }
}

