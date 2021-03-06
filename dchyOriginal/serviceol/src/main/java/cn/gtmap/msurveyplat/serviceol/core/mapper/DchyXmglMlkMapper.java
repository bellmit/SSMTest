package cn.gtmap.msurveyplat.serviceol.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 名录库
 */
@Repository
public interface DchyXmglMlkMapper {

    /**
     * 获取所有的从业人员集合
     *
     * @return
     */
    List<Map<String, Object>> getAllCyrysList();

    /**
     * 根据mlkid获取对应从业人员信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> queryCyryByMlkIdByPage(@Param("mlkid") String mlkid);

    /**
     * 根据所属模块id获取对应需要上传的材料信息
     *
     * @return
     */
    List<Map<String, Object>> queryUploadFileBySsmkId(@Param(value = "ssmkid") String ssmkid);

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @time 2020/12/3 19:43
     * @description 查询是否存在有效的名录库信息
     */
    List<Map<String, Object>> getMlkYxztByUserid(String userid);

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @time 2020/12/3 19:43
     * @description 查询是否存在申请中的名录库信息
     */
    List<Map<String, Object>> getMlkSqztByUserid(String userid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx(@Param(value = "mlkid") String mlkid, @Param(value = "ssmkid") String ssmkid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx2(@Param(value = "mlkid") String mlkid, @Param(value = "ssmkid") String ssmkid);

    /**
     * 获取到收件材料信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getSjclXxByGlsxid(@Param(value = "glsxid") String glsxid);

    /**
     * 根据名录库id获取对应名录库信息
     *
     * @param mlkId
     * @return
     */
    List<Map<String, Object>> getMlkXxById(@Param(value = "mlkId") String mlkId);

    /**
     * 根据单位编号获取对应mlk信息
     *
     * @param dwbh
     * @return
     */
    List<DchyXmglMlk> getMlkByDwbh(@Param(value = "dwbh") String dwbh);

    /**
     * @param map
     * @return
     * @description 2021/2/24 首页名录库管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> queryMlkListByPage(Map<String, Object> map);

    List<Map<String,Object>> queryMlkDbrwByPage(Map<String,Object> map);

    List<Map<String,Object>> queryMlkYbrwByPage(Map<String,Object> map);

    List<DchyXmglYbrw> queryYbrw(Map<String,Object> map);

    /**
     * @param map
     * @return
     * @description 2021/2/24 根据chxmid获取对应的最新的一条mlkid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryMlkidBychxmid(Map<String, Object> map);

    /**
     * 获取线上线下测绘单位列表
     *
     * @return
     */
    List<Map<String, Object>> queryChdwList();

    /**
     * 多条件查询测绘单位信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryChdwsByMultConditionsByPage(Map<String, Object> map);

    int updateDchyXmglMlk(DchyXmglMlk dchyXmglMlk);

    int insertDchyXmglMlk(DchyXmglMlk dchyXmglMlk);

    List<DchyXmglKp> getLasterKpById(@Param("mlkid") String mlkid);

    List<DchyXmglDbrw> getDbrwBySqxxid(@Param("sqxxid") String sqxxid);

    DchyXmglSqxx getSqxxByMlkid(@Param("mlkid") String mlkid);

    List<DchyXmglYbrw> queryybrwLaster(@Param("sqxxid") String sqxxid);
}
