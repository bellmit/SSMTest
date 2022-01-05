package cn.gtmap.msurveyplat.promanage.utils;

import cn.gtmap.msurveyplat.promanage.service.ResultsSubmitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ResultsSubmitServiceUtil {
    @Value("${cgtj.mode}")
    private String cgtjMode;

    private static String CGTJ_MODE;

    @Autowired
    private List<ResultsSubmitService> resultsSubmitServiceList;
    private static List<ResultsSubmitService> staticResultsSubmitServiceList;

    @Autowired
    private ResultsSubmitService resultsSubmitService;

    private static ResultsSubmitService staticResultsSubmitService;



    @PostConstruct
    public void setParam() {
        this.staticResultsSubmitServiceList = this.resultsSubmitServiceList;
        this.CGTJ_MODE = StringUtils.isNotBlank(this.cgtjMode) ? this.cgtjMode : Constants.CGTJ_MODE_CHGC;
        this.staticResultsSubmitService = this.resultsSubmitService;
    }

    public static ResultsSubmitService getResultsSubmitServiceByCode() {
        if (CollectionUtils.isNotEmpty(staticResultsSubmitServiceList)) {
            for (ResultsSubmitService resultsSubmitService : staticResultsSubmitServiceList) {
                if (StringUtils.equals(resultsSubmitService.getCode(), CGTJ_MODE)) {
                    return resultsSubmitService;
                }
            }
        }
        return staticResultsSubmitService;
    }

    public static String getCgtjMode() {
        return CGTJ_MODE;
    }
}
