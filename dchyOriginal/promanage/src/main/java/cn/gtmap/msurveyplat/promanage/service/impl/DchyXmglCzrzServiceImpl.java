package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglCzrzService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/30
 * @description 操作日志实现类
 */
@Service
public class DchyXmglCzrzServiceImpl implements DchyXmglCzrzService {

    protected final Log logger = LogFactory.getLog(DchyXmglCzrzServiceImpl.class);

    @Autowired
    private Repo repository;

    @Override
    public ResponseMessage queryCzrzList(Map<String, Object> param) {
        int page = Integer.parseInt(param.get("page") != null ? param.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(param.get("size") != null ? param.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        Page<Map<String, Object>> czrzListByPage = repository.selectPaging("queryXtCzrzListByPage", param, page - 1, pageSize);
        if (null != czrzListByPage) {
            List<Map<String, Object>> rows = czrzListByPage.getContent();
            logger.info("******************rows:" + rows);
            //前台无法解析czcs
            if (CollectionUtils.isNotEmpty(rows)) {
                for (Map<String, Object> dataMap : rows) {
                    String czcs = CommonUtil.formatEmptyValue(dataMap.get("CZCS"));
                    JSONObject jsonObject = JSONObject.parseObject(czcs);
                    String arg0 = CommonUtil.formatEmptyValue(jsonObject.get("arg0"));
                    if (StringUtils.isNotBlank(arg0) && arg0.contains("{")) {
                        JSONObject jsonObjectArg0 = null;
                        try {
                            jsonObjectArg0 = JSONObject.parseObject(arg0);
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                        dataMap.put("CZCS", jsonObjectArg0);
                    } else {
                        dataMap.put("CZCS", jsonObject);
                    }
                }
            }
        }
        return ResponseUtil.wrapResponseBodyByPage(czrzListByPage);
    }

}
