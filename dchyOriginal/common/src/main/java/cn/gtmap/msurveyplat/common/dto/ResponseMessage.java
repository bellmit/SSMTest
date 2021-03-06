package cn.gtmap.msurveyplat.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 19:21
 * @description 返回到前端的包装对象
 */
public class ResponseMessage {

    public enum CODE {
        SUCCESS("0000", "成功"),
        MESSAGE_DISABLE("1111", "短信验证已禁用"),
        INTERFACE_DISABLE("1111", "接口验证已禁用"),
        SEND_WRONG("1112", "短信发送失败"),

        QUERY_FAIL("1000", "数据查询失败"),
        PARAMEMPTY_FAIL("1001", "查询失败，查询条件为空"),
        OPERA_FAIL("1002", "操作失败"),
        CONFIG_FAIL("1003", "缺少配置"),
        AUTHO_FAIL("2000", "用户名及密码验证失败"),
        PARAM_WRONG("2001", "短信参数异常!"),
        YZM_WRONG("2002", "验证码不正确!"),
        USERDISABLE_FAIL("2003", "该用户已被禁用"),
        USERVER_FAIL("2008", "用户验证失败"),
        VERIFY_CODE_ERROR("5005", "验证码错误"),
        SIGN_FAIL("2002", "用户签名失败"),
        USERREPEAT_FAIL("2003", "该用户已注册"),
        PHONEREPEAT_FAIL("2004", "该手机号已注册"),
        PHONENOTREPEAT_FAIL("2005", "该手机号未注册"),
        USERNOTEXIST_FAIL("2006", "用户不存在"),
        PASSWORD_FAIL("2007", "密码错误"),
        TOKEN_FAIL("2010", "安全token错误"),
        TOKEN_WRONG("2011", "TOKEN异常!"),
        TOKEN_EXPIRE("2012", "验证码异常!"),
        TOKEN_NONCORPUSER("2013", "非企业用户!"),
        TOKEN_INVALID("2014", "TOKEN无效"),
        SAVE_FAIL("2040", "保存失败"),
        QUERY_NULL("2030", "查询数据为空，暂无此数据"),
        CONFIGTABLE_NULL("2031", "配置表为空,请检查配置表"),
        DELETE_FAIL("2050", "删除失败"),
        UPLOAD_FAIL("2020", "上传失败"),
        DOWNLOAD_FAIL("2021", "下载失败"),
        FILENOTFOUND_FAIL("2022", "暂无合同"),
        EXCEPTION_MGS("2070", "服务器出现异常或错误"),
        INIT_FAIL("2060", "初始化失败"),
        ACCESS_AUTHORIZE_FAIL("2014", "访问无授权"),
        OPERATION_REPET("2015", "重复操作"),

        RESET_FAIL("3001", "重置失败"),
//        SYSTEM_WRONG("4000", "系统异常"),
        PARAMETER_FAIL("4001", "参数缺失"),
        PROCESS_FAIL("4002", "申请已在办理中"),
        BANJIE_FAIL("4003", "办结失败"),
        USER_FAIL("4005", "用户名和密码均不能为空"),
        NOTFIND_FAIL("4004", "未找到相关申请"),
        PROCESSOCCUPATION_FAIL("4006", "当前流程正在进行"),
        PROCESSHAVINGDONE_FAIL("4007", "当前流程已办结"),
        REMOVE_FAIL("5050", "该测绘单位尚有在建的工程，暂不允许移除"),
        LOGOUT_FAIL("5060", "该测绘单位尚有进行中的测绘项目，暂不允许注销"),
        FILENAME_FAIL("5001", "无对应的合同备案记录，请先进行合同备案！"),
        FILESUBMIT_FAIL("5002", "该项目已办结"),
        ENTRUSTED_VERIFICATION_FAIL("5003", "核验失败"),
        RESULT_PUSH_FAIL("5004", "成果包推送失败"),
        RESULT_SUBMIT_FAIL("5006", "成果包解析失败"),
        RESULT_SUBMIT_NO_FILE_FAIL("5007", "成果包中无规定内的文件"),
        RESULT_SUBMIT_NOT_CONSISTENCY_FAIL("5008", "成果包名称异常"),
        RESULT_SUBMIT_IS_MAJOR_PROJECT("5009", "当前项目为重大建设项目，请到窗口提交成果"),
        RESULT_SUBMIT_REQUEST_TIMED_OUT("5010", "请求线下数据超时"),
        RESULT_SUBMIT_CONFIG_EMPTY("5011", "成果包配置为空,请检查clcgpz表"),
        KEEP_ON_RECORD_FAIL("6060", "在线备案失败"),
        ONLINE_COMPLETE_FAIL("7020", "在线办结失败"),
        ONLINE_CG_PREVIEW_FAIL("7030", "在线成果预览失败"),
        ACHIEVEMENT_EVALUATION_FAIL("6070", "成果评价失败"),
        TEMPLATE_NOT_UPLOAD("6071", "请先上传新模板"),
        FILE_NOT_UPLOAD("6072", "请先上传注册材料"),
        ALTER_MLK_INFO_FAIL("6089", "名录库信息变更失败"),
        NO_FILES_TO_DOWNLOAD("6080", "暂无文件可供下载"),
        TYSHXYDM_REPET("7001", "统一社会用代码重复"),
        JSDWM_REPET("7002", "建设单位码重复"),
        DWMC_REPET("7003", "单位名称重复"),
        DEL_HTWJ_FAIL("7004", "在线备案合同取消失败"),
        HT_BG_SYN_FAIL("7005", "合同变更同步失败"),
        XMLY_FAIL("8001", "该项目为线上项目,请到办事大厅进行补推"),
        MLK_LOGOUT_AUDIT_FAIL("8002","名录库注销审核错误"),

        XMGL_GQ("9001","该项目已挂起");


        private String code;

        private String msg;

        CODE(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 代码        响应信息描述
     * 0000        成功
     * 1000        数据查询失败
     * 2000        用户名密码验证错误
     * 2010        安全token错误
     */

    private ResponseHead head = new ResponseHead();
    /**
     * 用户返回给浏览器的数据
     */
    private Map<String, Object> data = new HashMap<>();

    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean issuccess() {
        if (null != head) {
            if (StringUtils.equalsIgnoreCase(head.getCode(), CODE.SUCCESS.getCode())) {
                return true;
            }
        }
        return false;
    }
}
