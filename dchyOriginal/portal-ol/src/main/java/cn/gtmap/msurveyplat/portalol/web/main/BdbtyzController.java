package cn.gtmap.msurveyplat.portalol.web.main;


import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.utils.ServiceOlFeignUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-27
 * description
 */

@RestController
@RequestMapping("/bdbtxyz")
public class BdbtyzController {

    @Autowired
    ServiceOlFeignUtil serviceOlFeignUtil;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 多测合一表单必填验证
     */
    @RequestMapping(value = "/getBdbtxyzList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage getBdbtxyzList(@RequestBody Map map) {
        return serviceOlFeignUtil.getBdbtxyzList(map);
    }

}
