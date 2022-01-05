package cn.gtmap.msurveyplat.server.service.shxx;


import cn.gtmap.msurveyplat.common.domain.DchyCgglGcjsspxxDo;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/26
 * @description 初始化审核信息表单
 */
public interface InitShxxFormService {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 初始化审核信息参数对象
     * @return 初始化审核信息结果
     * @description 初始化审核信息表单主逻辑
     */
    List<ShxxVO> init(ShxxParamDTO shxxParamDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 更新审核信息参数对象
     * @return 更新审核信息结果
     * @description 更新审核信息表单主逻辑
     */
    ShxxVO update(ShxxParamDTO shxxParamDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxid 审核信息ID
     * @return 审核信息结果
     * @description 删除签名主逻辑
     */
    ShxxVO deleteSign(String shxxid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param shxxParamDTO 初始化审核信息参数对象
     * @return 初始化审核信息结果
     * @description 保存或者更新工程建设审批审核信息
     */
    DchyCgglGcjsspxxDo saveOrUpdateDchyCgglGcjsspxxDo(ShxxParamDTO shxxParamDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return 工程建设审批信息对象
     * @description 根据项目ID获取工程建设审批审核信息
     */
    DchyCgglGcjsspxxDo getDchyCgglGcjsspxxDoByXmid(String xmid);


}
