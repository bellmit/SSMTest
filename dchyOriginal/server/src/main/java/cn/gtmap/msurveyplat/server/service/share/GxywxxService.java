package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.GxywxxDTO;
import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.common.dto.YhGxywxxDTO;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务信息接口
 */
public interface GxywxxService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 页数
     * @param size 每页大小
     * @param gxywxxDTO 共享业务参数
     * @return
     * @description
     */
    Page<Map> getGxywxxByPage(int page, int size, GxywxxDTO gxywxxDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gxywxxDTO 共享业务信息参数
     * @return
     * @description 新增或修改成果管理系统共享业务信息
     */
    void saveOrUpdateDchyCgglGxywxxDO(GxywxxDTO gxywxxDTO);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gxywxxid 共享业务信息ID
     * @return
     * @description 禁用或者启用共享业务信息
     */
    void disableEnableGxywxxById(String gxywxxid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取共享业务内容配置信息
     */
    List<Map> getGxywnrpzxx();

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gxywxxid 共享业务信息ID
     * @return
     * @description 获取共享业务信息
     */
    GxywxxDTO getGxywxx(String gxywxxid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param userInfo 用户信息
     * @return
     * @description 获取用户共享业务信息
     */
    List<YhGxywxxDTO> getYhGxywxx(YhGxywxxDTO yhGxywxxDTO, UserInfo userInfo);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @return
     * @description 获取所有共享业务信息
     */
    List<YhGxywxxDTO> getAllGxywxx(YhGxywxxDTO yhGxywxxDTO);

    List<PfOrganVo> getPfOrganVoList();

    List<PfRoleVo> getRoleListByOrganid(String organid);

}
