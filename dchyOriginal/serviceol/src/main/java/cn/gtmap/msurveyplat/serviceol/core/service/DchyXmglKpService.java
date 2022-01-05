package cn.gtmap.msurveyplat.serviceol.core.service;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/8 15:30
 * @description
 */
public interface DchyXmglKpService {

    /**
     * 根据mlkid获取对应考评信息
     */
    int getKpResultByMlkId(String mlkId);
}
