package cn.gtmap.msurveyplat.exchange.service.share;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/14
 * @description 共享测绘工程信息接口
 */
public interface GxchgcxxService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param paramMap
     * @param babh 工程编号
     * @return
     * @description 获取共享测绘工程材料下载地址
     */
    Map getGxchgcclDownUrl(Map paramMap, String babh);

}
