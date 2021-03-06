package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglKpService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/8 15:28
 * @description 考评管理
 */
@RestController
@RequestMapping(value = "/kp")
public class DchyXmglKpController {

    protected final Log logger = LogFactory.getLog(DchyXmglKpController.class);

    @Autowired
    private DchyXmglKpService kpService;

    /**
     * 通过mlkid获取对应考评信息
     *
     * @return
     */
    @PostMapping(value = "/getkpbymlkid/{mlkid}")
    public int getEvaluationLevel(@PathVariable("mlkid") String mlkid) {
        int result = 0;
        try {
            result = kpService.getKpResultByMlkId(mlkid);

        } catch (Exception e) {
            logger.error("错误原因{}", e);
        }
        return result;
    }
}
