package cn.gtmap.msurveyplat.serviceol.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZxkfIssues;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglZxkfMapper {

    /**
     * @param map
     * @return
     * @description 2021/4/8 用人单位 留言列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryMessageListByPage(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 用人单位 我的留言列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryMyIssuesListByPage(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 管理单位留言回复
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryAnswerListByPage(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/5/20 通过id获取提问信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglZxkfIssues queryIssuesById(Map<String, Object> map);
}
