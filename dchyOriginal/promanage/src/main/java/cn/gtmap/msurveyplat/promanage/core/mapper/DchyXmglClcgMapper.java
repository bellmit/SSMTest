package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglClcgMapper {

    /**
     * 根据测绘工程编号测绘事项查询审核信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryShxxByChgcbhAndClsx(Map<String, Object> paramMap);

    /**
     * 根据测绘项目编号查询成果提交信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryClcgByChxmid(Map<String, Object> paramMap);

    /**
     * 根据用户id获取审核人角色
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryRoleByUserid(@Param(value = "userid") String userid);

    /**
     * @param sqxxid
     * @param clcgmc
     * @return
     * @description 2021/1/13 通过sqxxid和clcgmc获取当前的测量成果
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryClcgBysqxxidAndClcgmc(@Param("sqxxid") String sqxxid, @Param("clcgmc") String clcgmc);

    /**
     * @param chxmid
     * @param clsx
     * @param rksj
     * @return
     * @description 2021/4/15
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<DchyXmglClcg> queryClcgmcByChxmidAndClsxAndRksj(@Param("chxmid") String chxmid, @Param("clsx") String clsx, @Param("rksj") String rksj);

    /**
     * @Description: 通过babh和gcbh，获取成果已入库项目的测绘项目编号
     * @param: babh
     * @param: gcbh
     * @return java.util.List<java.lang.String>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @date 2021/6/17 15:19
    */
    List<String> queryCgrkChxmBabhList(@Param("babh") String babh, @Param("gcbh") String gcbh);
}
