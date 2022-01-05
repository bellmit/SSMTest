package cn.gtmap.msurveyplat.serviceol.service;

import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface DchyGldwTzggglService {

    /**
     * @param
     * @return
     * @description 2020/12/8 初始化加载一条公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> initGldwTzgggl();

    /**
     * @return
     * @paramparamMap
     * @description 2020/12/8 通过tzggid删除公告和相关附件
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> deleteGldwTzggglAndFjByTzggid(Map<String, Object> paramMap);

    /**
     * @param map
     * @return
     * @description 2020/12/1 管理单位 通知公告管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> getTzggByBtAndGglx(Map<String, Object> map);

    /**
     * @param data
     * @return
     * @description 2020/12/31 新增一条新的通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    int saveOrUpdateTzgg(Map<String, Object> data) throws UnsupportedEncodingException;

    /**
     * @return
     * @paramparamMap
     * @description 2020/12/8 通过tzggid删除公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, String> deleteGldwTzggglByTzggid(Map<String, Object> data);

    /**
     * @param data
     * @return
     * @description 2020/12/31 通过tzggid查看通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> queryGldwTzggglByTzggid(Map<String, Object> data);

    Map<String, Object> queryGldwTzgggl(String tzggid);

    Page<Map<String, Object>> getTzggByBszn(Map<String, Object> map);

    Page<Map<String, Object>> getOtherTzgg(Map<String, Object> map);
}
