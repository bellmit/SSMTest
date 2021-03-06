package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxHtxxGx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.util.List;
import java.util.Map;

public interface DchyXmglMlkService {

    /**
     * 根据所属模块id获取对应需要上传的材料信息
     * @param ssmkid
     * @return
     */
    List<Map<String,Object>> queryUploadFileBySsmkId(String ssmkid);

    List<String> getHtxxIdByChxmid2(String chxmid);

    /**
     * 获取到收件材料信息
     *
     * @param mlkid
     * @return
     */
    List<Map<String, Object>> getSjclXx2(String mlkid, String ssmkid);

    /**
     * 获取到收件材料信息
     * @param glsxid
     * @return
     */
    List<Map<String,Object>> getSjclXx(String glsxid,String ssmkId);

    /**
     * 获取到合同信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> getHtxx(String mlkid);


    /**
     * 初始化材料信息与收件信息
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    void  initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);

    ResponseMessage delFile(String sjclId, String wjzxId);

    ResponseMessage delFileHtxx(String htxxId, String wjzxId);

    DchyXmglChxmClsx getChxmClsxByXmidAndClsx(String chxmid,String clsxid);

    String getHtxxIdByClsxId(String clsxid);

    int getClsxCountByHtid(String htxxid);

    String getClsxidByChxmidAndClsx(String chxmid,String clsx);

    DchyXmglClsxHtxxGx getClsxHtGxByChxmidAndClsxid(String chxmid,String clsxid);
}
