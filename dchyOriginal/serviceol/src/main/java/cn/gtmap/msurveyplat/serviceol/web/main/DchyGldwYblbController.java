package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/1
 * @description 管理单位 管理人员-已办列表
 */
@Controller
@RequestMapping("/gldwYblb")
public class DchyGldwYblbController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repository repository;

    /**
     * @param param
     * @return
     * @description 2020/12/1 管理单位已办列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryYblbList")
    @ResponseBody
    public Object getYblbByDwmcAndBlsj(@RequestBody Map<String, Object> param) {

        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        int page = Integer.parseInt(data.get("page") != null ? CommonUtil.formatEmptyValue(data.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(data.get("size") != null ? CommonUtil.formatEmptyValue(data.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String dwmc = CommonUtil.formatEmptyValue(data.get("dwmc"));
        String kssj = CommonUtil.formatEmptyValue(data.get("kssj"));
        String jssj = CommonUtil.formatEmptyValue(data.get("jssj"));
        if (StringUtils.isNotBlank(dwmc)) {
            map.put("dwmc", dwmc);
        }

        if (StringUtils.isNotBlank(kssj)) {
            map.put("kssj", kssj);
        }

        if (StringUtils.isNotBlank(jssj)) {
            map.put("jssj", jssj);
        }
        map.put("userid", UserUtil.getCurrentUserId());
        Page<Map<String, Object>> dataPaging = repository.selectPaging("queryYblbByPage", map, page - 1, size);

        DataSecurityUtil.decryptMapList(dataPaging.getContent());

        return ResponseUtil.wrapResponseBodyByPage(dataPaging);
    }

}
