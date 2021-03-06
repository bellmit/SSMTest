package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgTjjl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/9
 * @description 测量成果配置mapper
 */
@Repository
public interface DchyXmglClgcpzMapper {

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
     * @param sqxxid
     * @return
     * @description 2021/1/28 通过当前流程的sqxxid获取最近提交的成果包错误信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcgTjjl> queryClcgTjjl(String sqxxid);
}
