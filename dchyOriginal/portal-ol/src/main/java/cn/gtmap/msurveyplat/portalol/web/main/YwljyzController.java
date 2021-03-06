package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.utils.ServiceOlFeignUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-27
 * description
 */

@RestController
@RequestMapping("/ywljyz")
public class YwljyzController {

    private static final Log logger = LogFactory.getLog(YwljyzController.class);

    @Autowired
    private ServiceOlFeignUtil serviceOlFeignUtil;

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: map
     * @time 2021/5/14 17:08
     * @description 业务逻辑验证
     */
    @PostMapping(value = "/excuteYwljyz")
    public ResponseMessage excuteYwljyz(@RequestBody Map map) {
        return serviceOlFeignUtil.excuteYwljyz(map);
    }


}
