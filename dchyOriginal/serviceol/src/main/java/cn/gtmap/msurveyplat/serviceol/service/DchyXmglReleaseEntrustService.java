package cn.gtmap.msurveyplat.serviceol.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2021/02/22
 * @description
 */
public interface DchyXmglReleaseEntrustService {

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位查看发布的委托
     */
    Page<Map<String, Object>> getEntrustByPage(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看详情
     */
    List<Map<String, Object>> queryEntrustByChxmid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description  发布委托初始化
     */
    Map<String,Object> initEntrust();

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布线上委托
     */
    boolean saveEntrust(Map<String, Object> param);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布线上委托取回
     */
    boolean retrieveEntrust(Map<String, Object> param);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description  发布委托删除
     */
    Map<String,Object> deleteEntrust(Map<String, Object> paramMap);

}
