package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyXmglZxkfService {

    /**
     * @param
     * @return
     * @description 2021/4/8 初始化提问
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage initIssues();

    /**
     * @param map
     * @return
     * @description 2021/4/9 通过issuesid删除
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage delIssues(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 保存用户单位提问信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage saveIssues(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 用人单位留言列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryMessageList(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 管理单位留言列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryGldwMessageList(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 我的留言
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryMyIssuesList(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 通过留言id获取标题和问题
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryIssuesByid(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 管理单位回复提问
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage saveAnswer(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/8 管理单位留言列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryAnswerList(Map<String, Object> map);

    /**
     * @param userid 用户id
     * @return
     * @description 2021/4/8 通过用户id获取当前用户单位信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    DchyXmglYhdw queryYhdwByUserid(String userid);

    /**
     * @param userid 用户id
     * @return
     * @description 2021/4/8 通过用户id获取角色id
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String queryRoleidByUserid(String userid);

    /**
     * @param issuesid
     * @return
     * @description 2021/4/8 当回复提问以后,删除待办任务,新建已办任务
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void delDbrw(String issuesid);

    /**
     * @param pageListByPage
     * @return
     * @description 2021/4/8 数据库中的bytes转为String到页面
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void formatBytesToString(Page<Map<String, Object>> pageListByPage);

    /**
     * @param
     * @return
     * @description 2021/5/17 获取当前用户并通过当前用户获取其所在单位的所有用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryUserListByUserid();
}
