package cn.gtmap.msurveyplat.promanage.core.service.mq.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 通过mq线上推送文件到线下创建成果提交
 * @time 2021/3/13 14:41
 */
public interface CgtjService {

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: fileName
     * @param: inputStream
     * @param: xsSqxxid
     * @param: xsSqbh
      * @return cn.gtmap.msurveyplat.common.domain.TaskData
      * @time 2021/3/13 14:46
      * @description 成果提交
      */
    ResponseMessage cgjc(String fileName, InputStream inputStream, String xsSqxxid, String xsSqbh) throws IOException;

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: fileName
     * @param: inputStream
     * @param: xsSqxxid
     * @param: xsSqbh
     * @return cn.gtmap.msurveyplat.common.domain.TaskData
     * @time 2021/3/13 14:46
     * @description 成果提交
     */
    ResponseMessage cgtj(Map<String, Object> param) throws IOException;

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param:
      * @return java.lang.String
      * @time 2021/3/13 14:43
      * @description 多实现代码
      */
    String getCode();
}
