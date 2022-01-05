package cn.gtmap.msurveyplat.portal.service.impl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.portal.feign.ServerFeignService;
import com.gtis.config.AppConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description
 */
@Service
public class ServerFeignServiceImpl {

    private ServerFeignService init()
    {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(2000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(ServerFeignService.class, AppConfig.getProperty("server.url"));
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param initDataParamDTO 初始化对象
     * @return
     * @description 初始化
     * */
    public DchyCgglXmDO initData(InitDataParamDTO initDataParamDTO){
        ServerFeignService initService = init();
        return initService.initData(initDataParamDTO);
    }
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  xmid 项目id
     * @return
     * @description 删除业务数据
     * */
    public void deleteYwxx(String xmid) {
        ServerFeignService initService = init();
        initService.deleteYwxx(xmid);
    }

}
