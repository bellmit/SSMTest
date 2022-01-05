package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgShjl;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ResultsSubmitService {
    /**
     * @param
     * @return
     * @description 2021/1/8 初始化生成一条申请信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    String initSqxx();

    ResponseMessage initCgtj(String xmid);

    /**
     * @param map
     * @param uploadFileName
     * @param inputStream
     * @return
     * @description 2021/1/8 成果检查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage checkZipFiles(Map<String, Object> map, String uploadFileName, InputStream inputStream) throws IOException;

    /**
     * @param
     * @return
     * @description 2021/1/8 成果提交
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage zipUpload(String gzlslid, String glsxid, List<Map<String, String>> errorInfoModels);

    /**
     * @param sqxxid
     * @return
     * @description 2021/1/14 取消时删除申请信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> delSqxx(String sqxxid);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/22 审核成果
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryXmxxByTaskid(Map<String, String> paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/25 审核办结
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage checkFinish(Map paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/29 获取当前流程的审核记录
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage getShjlByTaskid(Map paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/31 获取当前测量成果的所有的审核记录
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcgShjl> getAllShjlByTaskid(Map<String, String> paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/29 获取当前流程的测量成果
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcg> getClcgByTaskid(Map<String, String> paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/1/29  通过taskid gzlslid获取当前的测量事项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String, Object> queryClsxListByTaskid(Map<String, String> paramMap);

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/3/12 10:04
     * @description 获取code代码，区分多实现方法
     */
    String getCode();
}
