package cn.gtmap.msurveyplat.promanage.utils;

import cn.gtmap.msurveyplat.promanage.core.service.BlsxBjService;
import cn.gtmap.msurveyplat.promanage.core.service.UploadService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DsxYwljUtil {

    @Autowired
    private List<BlsxBjService> blsxBjServiceList;

    private static List<BlsxBjService> blsxBjServiceStaticList;

    @Autowired
    private List<UploadService> uploadServiceList;

    private static List<UploadService> staticUploadServiceList;

    @PostConstruct
    public void setList() {
        this.blsxBjServiceStaticList = this.blsxBjServiceList;
        this.staticUploadServiceList = this.uploadServiceList;
    }

    public static BlsxBjService getBlsxBjServiceByCode(String code) {
        if (CollectionUtils.isNotEmpty(blsxBjServiceStaticList)) {
            for (BlsxBjService blsxBjService : blsxBjServiceStaticList) {
                if (StringUtils.equals(blsxBjService.getCode(), code)) {
                    return blsxBjService;
                }
            }
        }
        return null;
    }

    public static UploadService getUploadServiceBySsmkid(String code) {
        if (CollectionUtils.isNotEmpty(staticUploadServiceList)) {
            for (UploadService uploadService : staticUploadServiceList) {
                if (StringUtils.equals(uploadService.getSsmkid(), code)) {
                    return uploadService;
                }
            }
        }
        return null;
    }
}
