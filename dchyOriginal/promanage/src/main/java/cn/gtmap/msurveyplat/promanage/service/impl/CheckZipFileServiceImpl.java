package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.promanage.service.CheckZipFileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/21
 * @description 成果检查多实现
 */
@Component
public class CheckZipFileServiceImpl {

    @Autowired
    private List<CheckZipFileService> checkZipFileServiceList;
    private static List<CheckZipFileService> staticCheckZipFileServiceList;

    @PostConstruct
    public void setParam() {
        this.staticCheckZipFileServiceList = this.checkZipFileServiceList;
    }

    public static CheckZipFileService getResultsSubmitServiceByCode(String code) {
        if (CollectionUtils.isNotEmpty(staticCheckZipFileServiceList)) {
            for (CheckZipFileService checkZipFileService : staticCheckZipFileServiceList) {
                if (StringUtils.equals(checkZipFileService.getCode(), code)) {
                    return checkZipFileService;
                }
            }
        }
        return null;
    }
}
