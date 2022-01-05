package cn.gtmap.msurveyplat.server.util;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.server.feign.PromangeFeginService;
import com.gtis.config.AppConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/3/17
 * @description
 */
@Component
public class PromangeFeginServiceUtil {

    private static PromangeFeginService promangeFeginService = null;

    /**
     * @param
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 初始化PromangeFeginService接口
     */
    static {
        promangeFeginService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(35000, 35000))
                .retryer(new Retryer.Default(35000, 35000, 3))
                .target(PromangeFeginService.class, AppConfig.getProperty("promanage.url"));
    }

    /**
     * @param param 项目状态参数
     * @return 字典项
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    public Object getAllZzdjs(@RequestBody Map<String, Object> param) {
        return promangeFeginService.getAllZzdjs(param);
    }

    /**
     * @param param 查询信息
     * @return 项目查看台账
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */

    public ResponseMessage queryResultsManagement(@RequestBody Map<String, Object> param) {
        return promangeFeginService.queryResultsManagement(param);
    }

    /**
     * @param param chxmid 测绘项目id
     * @return 成果提交记录
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    public ResponseMessage getProjectConstrctInfo(@RequestBody Map<String, Object> param) {
        return promangeFeginService.getProjectConstrctInfo(param);
    }


}
