package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface DchyXmglJcsjsqService {

    /**
     * @param map
     * @return
     * @description 2021/3/8 基础数据申请-我的测绘项目台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryBasicDataApplicationInfo(Map<String, Object> map);

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
     * @return
     * @description 2021/3/11 项目信息查看
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage myProjectInfoView(Map<String, Object> map);

    /**
     * @param wjzxid             文件中心id
     * @param httpServletRequest
     * @param response
     * @return
     * @description 2021/3/9 通过文件中心下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseEntity<byte[]> downLoadFileByWjzxid(String wjzxid, HttpServletRequest httpServletRequest, HttpServletResponse response);

    /**
     * @param map
     * @return
     * @description 2021/3/9 基础数据操作日志台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage querySqxxList(Map<String, Object> map);

    /**
     * @param slbh 受理编号
     * @return
     * @description 2021/3/10 根据备案编号获取申请信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Integer generateSqxxBybabh(String slbh);

    /**
     * @param gcbh
     * @return
     * @description 2021/2/2 通过工程编号获取已经办结的测量事项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String,Object>> queryBaClsxListByGcbh(String gcbh);

    /**
     * 获取到合同信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getHtxxSjclXx(String glsxid);

    /**
     * 根据所属模块id获取对应需要上传的材料信息
     *
     * @param ssmkid
     * @return
     */
    List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid);

    /**
     * 初始化合同信息与收件信息
     *
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    void initHtxxSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);

    /**
     * 获取到收件材料信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getSjclXx(String glsxid);

    /**
     * 初始化材料信息与收件信息
     *
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    void initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);

    //获取测量事项与测绘项目树形表
    List<Map<String, Object>> getZdClsx(Map<String, Object> paramMap);

    /**
     * @param map
     * @return
     * @description 2021/3/15 管理单位抽查结果台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage evaluationCheckResults(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/15 管理单位抽查结果台账查看
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage evaluationCheckResultsView(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/19 获取收件材料,评价的材料
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage getsjcl(Map<String, Object> map);

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
     * @param jcsjsqxxid 基础数据申请信息id
     * @return
     * @description 2021/4/6 通过基础数据申请信息id获取申请信息id
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String queryXmidByJcsjsqxxid(String jcsjsqxxid);
}
