package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.domain.TaskData;
import cn.gtmap.msurveyplat.promanage.feign.PortalFeignService;
import com.gtis.config.AppConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@Service
public class PortalFeignServiceImpl {


    private static PortalFeignService portalFeignService = null;
    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param
     * @return
     * @description 初始化PortalFeignService接口
     * */
    static {
        portalFeignService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(35000, 35000))
                .retryer(new Retryer.Default(35000, 35000, 3))
                .target(PortalFeignService.class, AppConfig.getProperty("portal.url"));
    }


    public TaskData createTask(String wdid,String userId){
        return portalFeignService.createTask(wdid,userId);
    }

    public Object turnTask(String wiid,String userId){
        return portalFeignService.turnTask(wiid,userId);
    }
}
