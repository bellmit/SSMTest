package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxHtxxGx;
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
     * 根据所属模块id获取对应需要上传的材料信息
     * @return
     */
    List<Map<String,Object>> queryUploadFileBySsmkId(@Param(value = "ssmkid") String ssmkid);

    /**
     * 获取到收件材料信息
     * @param glsxid
     * @return
     */
    List<Map<String,Object>> getSjclXx(@Param(value = "glsxid") String glsxid,@Param(value = "ssmkId") String ssmkId);

    /**
     * 获取到合同信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> getHtxx(@Param(value = "mlkid") String mlkid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx2(@Param(value = "mlkid") String mlkid, @Param(value = "ssmkid") String ssmkid);


    DchyXmglChxmClsx getChxmClsxByXmidAndClsx(@Param(value = "chxmid")String chxmid, @Param("clsx") String clsx);

    String getHtxxIdByClsxId(@Param(value = "clsxid") String clsxid);

    int getClsxCountByHtid(@Param(value = "htxxid") String htxxid);

    String getClsxidByChxmidAndClsx(@Param("chxmid") String chxmid,@Param("clsx") String clsx);

    DchyXmglClsxHtxxGx getClsxHtGxByChxmidAndClsxid(@Param("chxmid") String chxmid,@Param("clsxid") String clsxid);
}
