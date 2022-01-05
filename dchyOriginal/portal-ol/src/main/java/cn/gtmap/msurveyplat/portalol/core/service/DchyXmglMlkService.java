package cn.gtmap.msurveyplat.portalol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyXmglMlkService {

    /**
     * 根据dwmc模糊查询
     * @return
     */
    Page<Map<String, Object>> getMlkLikeMcByPage(Map<String,Object> data);


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
    List<Map<String,Object>> queryCyryByMlkId(String mlkid);

    /**
     * 根据所属模块id获取对应需要上传的材料信息
     * @param ssmkid
     * @return
     */
    List<Map<String,Object>> queryUploadFileBySsmkId( String ssmkid);

    String queryMlkIdByDwmc(String dwmc);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @time 2020/12/3 19:48
     * @description 通过用户id获取名录库状态
     */
    Map<String,Object> queryMlkXxZtByUserid(String userid);


    /**
     * 获取到收件材料信息
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> getSjclXx(String mlkid);

    /**
     * 初始化材料信息与收件信息
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    public void  initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);


    public void clearClxxBySjxxId(String sjxxid);

    List<DchyXmglMlk> getMlkByMc(String dwmc);

    DchyXmglMlk getMlkById(String mlkid);

    /**
     * 根据mlkid获取对应考评信息
     */
    int getKpResultByMlkId(String mlkId);

    /**
     * 根据mlkid查询mlk详情
     * @param data
     * @return
     */
    List<Map<String,Object>> queryMlkDetails(Map<String,Object> data);

    /**
     * 根据mlkid查询从业人员信息
     * @param data
     * @return
     */
    Page<Map<String,Object>> queryCyryByMlkid(Map<String,Object> data);

    /**
     * 根据mlkid查询诚信记录
     * @param data
     * @return
     */
    Page<Map<String,Object>> queryCxjlByMlkid(Map<String,Object> data);

    /**
     * @param mlkid
     * @return
     * @description 2021/3/20 通过mlkid获取相关的测量事项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map queryClsxByMlkid(String mlkid);
}
