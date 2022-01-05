package cn.gtmap.msurveyplat.server.service.onemap;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.vo.ProjectDetailVo;
import cn.gtmap.msurveyplat.common.vo.ProjectInformationVo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 一张图接口
 */
public interface OnemapService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param chxmbh 测绘项目编号
     * @return
     * @description 通过测绘项目编号获取项目详情信息
     */
    List<ProjectDetailVo> getProjectDetailVoList(String chxmbh) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, ClassNotFoundException;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 根据项目地址获取查看项目url地址
     */
    String getViewProjectUrl(String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @param xmmc 项目名称
     * @param xmdz 项目地址
     * @return
     * @description 获取项目信息
     * */
    List<DchyCgglXmDO> getDchyCgglXm(String xmbh, String xmmc, String xmdz);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param dchybh 多测合一编号
     * @return
     * @description 获取项目信息
     */
    ProjectInformationVo getProjectInformationVo(String dchybh) throws IOException, ClassNotFoundException;


}
