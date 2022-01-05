package cn.gtmap.msurveyplat.server.web;

import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.server.util.UserUtil;
import com.gtis.common.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/26
 * @description 基础Controller
 */
@Controller
public class BaseController {

    @Autowired
    private UserUtil userUtil;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 日志对象常量
     */
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    public UserInfo getCurrentUser(HttpServletRequest request) {
        return userUtil.getCurrentUser(request);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取通用唯一标识符
     */
    public String getUuid() {
        return UUIDGenerator.generate18();
    }


}
