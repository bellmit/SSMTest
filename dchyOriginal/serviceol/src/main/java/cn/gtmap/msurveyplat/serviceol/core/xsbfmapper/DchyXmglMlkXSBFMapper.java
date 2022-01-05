package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
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
public interface DchyXmglMlkXSBFMapper {

    /**
     * 获取所有的从业人员集合
     * @return
     */
    List<Map<String,Object>> getAllCyrysList();


    /**
     * 根据mlkid获取对应从业人员信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> queryCyryByMlkIdByPage(@Param("mlkid") String mlkid);


    /**
     * 根据所属模块id获取对应需要上传的材料信息
     * @return
     */
    List<Map<String,Object>> queryUploadFileBySsmkId(@Param(value = "ssmkid") String ssmkid);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @return java.lang.String
     * @time 2020/12/3 19:43
     * @description 查询是否存在有效的名录库信息
     */
    List<Map<String,Object>> getMlkYxztByUserid(String userid);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @return java.lang.String
     * @time 2020/12/3 19:43
     * @description 查询是否存在申请中的名录库信息
     */
    List<Map<String,Object>> getMlkSqztByUserid(String userid);

    /**
     * 获取到收件材料信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> getSjclXx(@Param(value = "mlkid") String mlkid);


    /**
     * 获取到收件材料信息
     * @param glsxid
     * @return
     */
    List<Map<String,Object>> getSjclXxByGlsxid(@Param(value = "glsxid") String glsxid);

    List<Map<String,Object>> getSjclXxFromSxbf(@Param(value = "mlkid") String mlkid, @Param(value = "ssmkid") String ssmkid);

    /**
     * 根据名录库id获取对应名录库信息
     * @param mlkId
     * @return
     */
    List<Map<String, Object>> getMlkXxById(@Param(value = "mlkId") String mlkId);

    /**
     * 根据单位编号获取对应mlk信息
     * @param dwbh
     * @return
     */
    List<DchyXmglMlk> getMlkByDwbh(@Param(value = "dwbh") String dwbh);

    /**
     * 获取到合同信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> getHtxx(@Param(value = "mlkid") String mlkid);

}
