package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 签名接口
 */
public interface SignService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @description 更新审核信息
     **/
    ShxxVO updateShxx(ShxxParamDTO shxxParamDTO);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param signId 签名ID
     * @return
     * @description 删除签名
     **/
    boolean deleteShxxSign(String signId);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: signId
      * @return byte[]
      * @time 2020/12/4 14:41
      * @description 通过签名id获取签名图片
      */
    byte[] getSignPicBySignId(String signId);

}
