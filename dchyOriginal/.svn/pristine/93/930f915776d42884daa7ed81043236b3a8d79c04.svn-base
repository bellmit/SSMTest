package cn.gtmap.msurveyplat.exchange.service.sign;


import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;

import java.util.List;

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
     * @description 获取审核信息
     **/
    List<ShxxVO> getShxxVOList(ShxxParamDTO shxxParamDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @description 更新审核信息
     **/
    ShxxVO updateShxx(ShxxParamDTO shxxParamDTO);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxid 审核信息ID
     * @return
     * @description 删除签名
     **/
    void deleteShxxSign(String shxxid);
}
