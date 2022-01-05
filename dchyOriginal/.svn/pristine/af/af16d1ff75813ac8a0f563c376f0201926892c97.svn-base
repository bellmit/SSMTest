package cn.gtmap.msurveyplat.server.util;

import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserUtil {

    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取用户信息
     */
    public UserInfo getCurrentUser(HttpServletRequest request) {
        UserInfo userInfo = null;
        String loginName = CommonUtil.formatEmptyValue(request.getSession().getAttribute("loginName"));
        if (StringUtils.isNotBlank(loginName)) {
            PfUserVo pfUserVo = exchangeFeignUtil.getUserByloginName(loginName);
            if (pfUserVo != null) {
                userInfo = new UserInfo();
                userInfo.setId(pfUserVo.getUserId());
                userInfo.setUsername(pfUserVo.getUserName());
                userInfo.setLoginName(pfUserVo.getLoginName());
                List<PfRoleVo> pfRoleVoList = exchangeFeignUtil.getPfRoleVoList(pfUserVo.getUserId());
                if (StringUtils.equals(pfUserVo.getUserId(), "0")) {
                    userInfo.setAdmin(true);
                } else {
                    userInfo.setPfRoleVoList(pfRoleVoList);
                    for (PfRoleVo pfRoleVo : pfRoleVoList) {
                        if (StringUtils.equals(pfRoleVo.getRoleName(), "系统管理员")) {
                            userInfo.setAdmin(true);
                            break;
                        }
                    }
                }
            }
        }
        return userInfo;
    }

}
