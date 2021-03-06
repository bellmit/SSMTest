package cn.gtmap.msurveyplat.promanage.web.main;


import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.ConstructionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@Controller
@RequestMapping("/construction")
public class ConstructionController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ConstructionService constructionService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 查询建设单位列表
     */
    @GetMapping(value = "/queryConstructionList")
    @ResponseBody
    public Object queryConstructionList() {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> resultList = constructionService.queryConstructionList();
            DataSecurityUtil.decryptMapList(resultList);
            responseMessage = ResponseUtil.wrapResponseBodyByList(resultList);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 查询备案登记信息
     */
    @RequestMapping(value = "/queryConstructionInfo")
    @ResponseBody
    public Object queryConstructionInfo(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Page<Map<String, Object>> resultList = constructionService.queryConstructionInfo(map);
            DataSecurityUtil.decryptMapList(resultList.getContent());
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultList);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
