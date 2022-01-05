package cn.gtmap.msurveyplat.exchange.service.onemap;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 一张图接口
 */
public interface OnemapService {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @return
     * @description 获取附件树信息
     */
    Map<String, Object> getAttachmentTree(String slbh);
}
