package cn.gtmap.msurveyplat.serviceol.config;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.impl.AuthorizeServiceImpl;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.Container;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.plat.vo.PfRoleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description
 * @time 2021/3/27 14:38
 */
public class InterfaceAuthCheckInterceptor implements HandlerInterceptor {
    private Logger       logger = LoggerFactory.getLogger(this.getClass());
    private EntityMapper entityMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        if (o instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) o;
            if (null != h.getMethodAnnotation(CheckTokenAno.class)) {
                String authToken = httpServletRequest.getHeader("token");
                if (org.apache.commons.lang.StringUtils.isEmpty(authToken)) {
                    throw new RuntimeException("Header中无效token值");
                }
                if (!TokenCheckUtil.TOKEN.equals(authToken)) {
                    throw new RuntimeException("Header中无效token值");
                }
            }
            CheckInterfaceAuth checkInterfaceAuth = h.getMethodAnnotation(CheckInterfaceAuth.class);
            if (checkInterfaceAuth != null) {
                UserInfo userInfo = UserUtil.getCurrentUser();
                if (null != userInfo && !userInfo.isAdmin() && StringUtils.isNotBlank(userInfo.getPassword())) {
                    List<PfRoleVo> roleList   = userInfo.getLstRole();
                    List<String>   roleIdList = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(roleList)) {
                        for (PfRoleVo pfRoleVo : roleList) {
                            roleIdList.add(pfRoleVo.getRoleId());
                        }
                    }
                    String uri = checkInterfaceAuth.uri();
                    if (StringUtils.isNotBlank(uri)) {
                        uri = httpServletRequest.getContextPath() + uri;
                    } else {
                        uri = httpServletRequest.getRequestURI();
                    }
                    AuthorizeServiceImpl authorizeService = (AuthorizeServiceImpl) Container.getBean("authorizeServiceImpl");
                    boolean              authorized       = authorizeService.checkAuthorized(Constants.DCHY_XMGL_AUTHORIZE_ZYLX_INTERFACE, Constants.DCHY_XMGL_AUTHORIZE_SQLX_YXFW, uri, roleIdList);
                    if (!authorized) {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ACCESS_AUTHORIZE_FAIL.getMsg(), ResponseMessage.CODE.ACCESS_AUTHORIZE_FAIL.getCode())));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, Object o, Exception e) throws Exception {

    }

    public EntityMapper getEntityMapper() {
        return entityMapper;
    }

    public void setEntityMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }
}
