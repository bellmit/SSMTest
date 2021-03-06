package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyXmglJcsjsqService {

    /**
     * @param
     * @return
     * @description 2021/4/12 初始化基础数据申请流程获取工作流定义id
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String initSqxx();

    /**
     * @param map
     * @return
     * @description 2021/3/9 初始化生成基础数据申请数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage initBasicDataApplication(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/9 查看基础数据申请详情
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage applicationInfoView(Map<String, Object> map);

    /**
     * @param map
     * @return wjzxid
     * @description 2021/3/23 成果交付
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String resultsDelivery(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/23 基础数据交付日志
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage resultsDeliveryLogList(Map<String, Object> map);

    /**
     * @param map 受理编号
     * @return
     * @description 2021/3/30 通过受理编号获取流程信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage getProcessInfoBySlbh(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/23 保存审核意见
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage saveCheckOpinion(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/9 基础数据操作待办台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryDbSqxxList(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/4/13 基础数据操作待办台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryYbSqxxList(Map<String, Object> map);

    /**
     * @param slbh   受理编号
     * @param sqxxid 申请信息id
     * @return
     * @description 2021/3/10 根据备案编号获取申请信息 生成一条申请信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void generateSqxxBybabh(String slbh, String sqxxid);


    /**
     * @param data
     * @return
     * @description 2021/3/11 成果随机抽查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> resultsSpotRandCheck(Map<String, Object> data);

    /**
     * 抽查评价详情
     *
     * @param data
     * @return
     */
    List<Map<String, Object>> spotCheckEvaluationDetails(Map<String, Object> data);

    /**
     * @param map
     * @return
     * @description 2021/3/12 成果抽查台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Page<Map<String, Object>> queryResultsSpotCheckList(Map<String, Object> map);


    /**
     * 成果评价
     */
    int achievementEvaluation(Map<String, Object> data);

    /**
     * 更新抽查表
     *
     * @param cgccid
     * @return
     */
    void updateCgcc(String cgccid);

    /**
     * @param sqxxid
     * @param yhxxpzid
     * @return
     * @description 2021/6/5 基础数据申请过程中,制作完成和交付完提醒用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void jcsjsqXxtx(String sqxxid, String yhxxpzid);

    /**
     * @param dwmc
     * @return
     * @description 2021/6/5 通过单位名称获取mlkid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String queryMlkidByDwmc(String dwmc);

}