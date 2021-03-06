package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgTjjl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/9
 * @description 材料提交mapper
 */
public interface DchyXmglCltjXSBFMapper {

    /**
     * @param
     * @return
     * @description 2021/1/9 获取成果目录配置
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcgpz> queryclcgpz();

    /**
     * @param
     * @return
     * @description 2021/1/9 获取成果目录配置通过pclcgpzid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcgpz> queryclcgpzByPclcgpzid(String pclcgpzid);

    /**
     * @param dchyXmglClcgTjjl
     * @return
     * @description 2021/1/28 更新提交成果的错误信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    int updateDchyXmglClcgTjjl(DchyXmglClcgTjjl dchyXmglClcgTjjl);

    /**
     * @param sqxxid
     * @return
     * @description 2021/1/28 通过当前流程的sqxxid获取最近提交的成果包错误信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcgTjjl> queryClcgTjjl(String sqxxid);

    /**
     * @param map
     * @return
     * @description 2021/1/18 获取最新的chxmid,clsxid,clsx
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryClcgBySlbh(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/3/1 通过测绘工程编号和测量事项判断该测量事项是否已经备案,
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map> queryHtxxBySlbhAndClsx(Map<String, Object> map);
}
