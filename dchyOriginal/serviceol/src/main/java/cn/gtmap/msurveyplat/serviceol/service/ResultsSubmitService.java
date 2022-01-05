package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResultsSubmitService {
    /**
     * @param
     * @return
     * @description 2021/1/8 初始化生成一条申请信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage initSqxx();

    /**
     * @param sqxxid
     * @return
     * @description 2021/3/12 初始化成果提交记录
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage initCgtj(String sqxxid);

    /**
     * @param
     * @return
     * @description 2021/3/13 显示成果提交
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage checkZipFiles(String uploadFileName, MultipartFile file, String sqxxid);

    /**
     * @param
     * @return
     * @description 2021/1/8 成果提交
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage zipUpload(String glsxid, List<Map<String, String>> errorInfoModels);

    /**
     * @param glsxid
     * @return
     * @description 2021/3/14 通过申请信息id改测量成果提交状态
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage changeClcgztBySlbh(String glsxid);

    /**
     * @param slbh
     * @return
     * @description 2021/3/18 校验项目是否为重大项目
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    boolean isMajorProject(String slbh);
}
