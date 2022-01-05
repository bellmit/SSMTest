package cn.gtmap.msurveyplat.portalol.utils;

import cn.gtmap.msurveyplat.common.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/22 16:24
 * @description
 */
public class VerifyUtil {

    /**
     * 判断验证码是否正确
     * @param request
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest request){
        String verifyCodeExpected = (String) request.getSession().getAttribute("verifyCode");
        String verifyCodeActual = CommonUtil.ternaryOperator(request.getParameter("yzm").trim());
        if(verifyCodeActual == null || ! verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)){
            return false;
        }
        return true;
    }

}
