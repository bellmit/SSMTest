package cn.gtmap.msurveyplat.server.service.ywxx;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.common.dto.UploadParamDTO;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/12
 * @description 受理单信息
 */
public interface SldxxService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid
     * @return
     * @description 获取受理单信息
     * */
    InitDataResultDTO getSldxx(String xmid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param initDataResultDTO
     * @return
     * @description 保存受理单信息
     * */
    void saveSldxx(InitDataResultDTO initDataResultDTO);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param dchyCgglSjclDO
     * @return
     * @description 新增收件材料信息
     * */
    String saveSjcl(DchyCgglSjclDO dchyCgglSjclDO, String xmid) ;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slclidList 收件材料id
     * @return
     * @description 删除收件材料信息
     * */
    void deleteSjcl(List<String> slclidList);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @param xmid 项目ID
     * @param sjclid 收件材料ID
     * @param sfpl 是否批量
     * @return
     * @description 获取附件上传参数
     */
    UploadParamDTO getUploadParam(String slbh, String xmid, String sjclid, Boolean sfpl);
}
