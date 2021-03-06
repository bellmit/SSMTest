package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglKp;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DchyXmglGldwService {

    /**
     * 根据考评时间，考评结果获取项目评价信息
     *
     * @param param
     * @return
     */
    Page<Map<String, Object>> queryChdwKpStatusByPage(Map<String, Object> param);

    /**
     * 新增测绘单位的考评记录
     *
     * @param param
     * @return
     */
    DchyXmglKp saveChdwKpxx(Map<String, Object> param);

    /**
     * 新增测绘单位的诚信记录
     *
     * @param param
     * @return
     */
    Map<String,String> saveChdwCxjl(Map<String, Object> param);

    /**
     * 根据chdwxxid获取建设单位评价记录
     *
     * @param data
     * @return
     */
    Page<Map<String, Object>> getChdwPJxxById(Map<String, Object> data);

    Page<Map<String, Object>> getGldwKpxxByMlkId(Map<String, Object> data);

    void removeMlkById(Map<String, Object> data);

    /**
     * 获取建设单位评价记录
     *
     * @param data
     * @return
     */
    Page<Map<String, Object>> getChdwEvalByid(Map<String, Object> data);

    /**
     * 移除名录库之前需判断该测绘单位是否有在建的工程，如果有则不允许移除，只是不能再承接测绘项目
     *
     * @param data
     * @return
     */
    boolean isConstructProject(Map<String, Object> data);

    /**
     * @param map
     * @return
     * @description 2021/3/15 管理单位抽查结果台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage evaluationCheckResults(Map<String, Object> map);

    /**
     * @param glsxid
     * @return
     * @description 2021/3/23 通过关联事项id获取wjzxid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public List<String> queryWjzxidListByGlsxid(String glsxid);


    Set<String> getKpNdkpByMlkid(String mlkid);
}
