package cn.gtmap.msurveyplat.server.service.feign.impl;

import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.UserTaskDto;
import cn.gtmap.msurveyplat.server.service.feign.ExchangeSignService;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfSignVo;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeSignServiceImpl implements ExchangeSignService {

    private ExchangeSignService init() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(2000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(ExchangeSignService.class, AppConfig.getProperty("exchange.url"));
    }

    @Override
    public PfSignVo getSign(String signId) {
        if (StringUtils.isNotBlank(signId)) {
            ExchangeSignService exchangeSignService = init();
            return exchangeSignService.getSign(signId);
        }
        return null;
    }

    @Override
    public PfSignVo getSignImage(String signId) {
        if (StringUtils.isNotBlank(signId)) {
            ExchangeSignService exchangeSignService = init();
            return exchangeSignService.getSignImage(signId);
        }
        return null;
    }

    @Override
    public void deleteSign(String signId) {
        if (StringUtils.isNotBlank(signId)) {
            ExchangeSignService exchangeSignService = init();
            exchangeSignService.deleteSign(signId);
        }
    }

    @Override
    public void updateAutoSign(PfSignVo pfSignVo) {
        if (null != pfSignVo) {
            ExchangeSignService exchangeSignService = init();
            exchangeSignService.updateAutoSign(pfSignVo);
        }
    }

    @Override
    public void insertAutoSign(PfSignVo pfSignVo) {
        if (null != pfSignVo) {
            ExchangeSignService exchangeSignService = init();
            exchangeSignService.insertAutoSign(pfSignVo);
        }
    }

    @Override
    public List<PfSignVo> getSignList(String signKey, String proid) {
        if (StringUtils.isNotBlank(signKey) && StringUtils.isNotBlank(proid)) {
            ExchangeSignService exchangeSignService = init();
            return exchangeSignService.getSignList(signKey, proid);
        }
        return null;
    }

    @Override
    public List<ProcessDefData> getWorkflowDefinitionList() {
        ExchangeSignService exchangeSignService = init();
        return exchangeSignService.getWorkflowDefinitionList();

    }

    @Override
    public List<UserTaskDto> getWorkflowDefinitionListByid(String gzldyid) {
        ExchangeSignService exchangeSignService = init();
        return exchangeSignService.getWorkflowDefinitionListByid(gzldyid);
    }

}
