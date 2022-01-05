/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.gtmap.msurveyplat.serviceol.config;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ViewAuthFilter implements Filter {
    public static String REQUESTED_URL = "CasRequestedUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String requestPath = request.getServletPath();
        if (requestPath.indexOf("html") != -1) {
            session.setAttribute(REQUESTED_URL, requestPath);
        }
        if (StringUtils.indexOf(request.getRequestURI(), "/msurveyplat-serviceol/view/") == 0 ) {
            boolean authorized = false;
            UserInfo userInfo = UserUtil.getCurrentUser();
            if (null != userInfo && !userInfo.isAdmin()) {
                List<PfRoleVo> roleList = userInfo.getLstRole();
                List<String> roleIdList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(roleList)) {
                    for (PfRoleVo pfRoleVo : roleList) {
                        roleIdList.add(pfRoleVo.getRoleId());
                    }
                }
                AuthorizeServiceImpl authorizeService = (AuthorizeServiceImpl) Container.getBean("authorizeServiceImpl");
                authorized = authorizeService.checkAuthorized(Constants.DCHY_XMGL_AUTHORIZE_ZYLX_VIEW, Constants.DCHY_XMGL_AUTHORIZE_SQLX_YXFW, request.getRequestURI(), roleIdList);
            } else if (userInfo.isAdmin()) {
                authorized = true;
            }
            if (authorized) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSONObject.toJSONString(ResponseUtil.wrapSuccessResponse()));
                return;
            } else {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSONObject.toJSONString(ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ACCESS_AUTHORIZE_FAIL.getMsg(), ResponseMessage.CODE.ACCESS_AUTHORIZE_FAIL.getCode())));

                return;
            }
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}