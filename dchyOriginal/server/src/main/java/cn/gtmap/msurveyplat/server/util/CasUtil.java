package cn.gtmap.msurveyplat.server.util;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/27
 * @description cas工具类
 */
public class CasUtil {
    /**
     * @param request
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 从cas中获取用户名
     */
    public static String getAccountNameFromCas(HttpServletRequest request) {
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if (assertion != null) {
            AttributePrincipal principal = assertion.getPrincipal();
            return principal.getName();
        }
        return null;
    }
}