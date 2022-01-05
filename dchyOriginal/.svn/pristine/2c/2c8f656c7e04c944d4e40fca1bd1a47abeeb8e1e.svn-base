package cn.gtmap.msurveyplat.serviceol.core.service;


import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 签名接口
 */
public interface SignService {
    /**
     * @param rwid 根据任务id获取审核信息
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取审核信息
     **/
    ShxxVO getShxxVO(String rwid, String dqjd);


    /**
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新审核信息
     **/
    ShxxVO updateShxx(ShxxParamDTO shxxParamDTO);

    /**
     * @param signId 签名ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 删除签名
     **/
    boolean deleteShxxSign(String signId);

    /**
     * @return byte[]
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: signId
     * @time 2020/12/4 14:41
     * @description 通过签名id获取签名图片
     */
    byte[] getSignPicBySignId(String signId);

    Map<String, Object> shbj(Map<String, Object> pram);
}
