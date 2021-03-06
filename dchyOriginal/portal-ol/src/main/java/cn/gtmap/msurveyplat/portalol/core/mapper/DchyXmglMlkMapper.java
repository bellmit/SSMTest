package cn.gtmap.msurveyplat.portalol.core.mapper;

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
     * @return
     */
    List<Map<String,Object>> getAllCyrysList();


    /**
     * 根据dwmc模糊查询
     * @return
     */
    List<Map<String,Object>> queryMlsLikeMcByPage(@Param("dwmc") String dwmc);

    /**
     * 根据mlkid获取对应从业人员信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> queryCyryByMlkId(@Param("mlkid") String mlkid);


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
     * 根据sjxxid清空对应clxx
     * @param sjxxId
     */
    void clearClxxBySjxxId(@Param(value = "sjxxid") String sjxxId);


    /**
     * 根据mlkid获取对应从业人员信息
     * @param map
     * @return
     */
    List<Map<String,Object>> queryCyryXxByMlkidByPage(Map<String,Object> map);

    /**
     * 根据mlkid获取对应诚信记录
     * @param map
     * @return
     */
    List<Map<String,Object>> queryCxjlByMlkidByPage(Map<String,Object> map);
}
