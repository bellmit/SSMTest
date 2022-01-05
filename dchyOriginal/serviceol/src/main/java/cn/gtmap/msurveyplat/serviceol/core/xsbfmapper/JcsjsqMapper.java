package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JcsjsqMapper {

    //基础数据申请-我的测绘项目台账
    List<Map<String, Object>> queryBasicDataApplicationInfoByPage(Map<String, Object> map);

    //基础数据申请的基础数据范围小台账
    List<Map<String, Object>> querySjclListByBabhByPage(Map<String, Object> map);

    //通过chxmid获取备案信息
    List<Map<String, Object>> querBaxxByChxmid(Map<String, Object> map);

    //获取当前申请的操作日志
    List<Map<String, Object>> querySqxxListByPage(Map<String, Object> map);

    //根据受理编号获取项目信息
    List<Map<String, Object>> queryBaxxByBabh(Map<String, Object> map);

    //通过工程编号获取备案测量事项
    List<Map<String, Object>> queryBaClsxListByGcbh(Map<String, Object> paramMap);

    //通过测绘项目id获取测量事项备案时间等
    List<Map<String, Object>> queryClsxByChxmid(Map<String, Object> paramMap);

    /**
     * 获取到收件材料信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getHtxxSjclXx(@Param(value = "glsxid") String glsxid);

    /**
     * @param
     * @return
     * @description 2020/11/28 获取表测量事项信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> getClsxByChxmid(@Param(value = "htxxid") String htxxid);

    /**
     * @param
     * @description 项目查看台账获取测量事项
     */
    List<Map<String, Object>> getClsxByChxmids(@Param(value = "chxmid") String chxmid);

    /**
     * @param
     * @description 项目查看中点击查询按钮获取测量事项台账
     */
    List<Map<String, Object>> getClsxByChxmidlist(Map<String, Object> paramMap);

    /**
     * @param
     * @description 根据CLSXID获取对应的测绘单位信息
     */
    List<Map<String, Object>> getChdwxxByCLsx(Map<String, Object> paramMap);

    /**
     * @param
     * @return
     * @description 2020/11/28 获取表测绘单位信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> getChdwxxByChxmid(@Param(value = "htxxid") String htxxid);

    List<Map<String, Object>> getZdClsx(Map<String, Object> paramMap);

    /**
     * @param param
     * @return
     * @description 2021/3/15 管理单位抽查结果
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> evaluationCheckResultsByPage(Map<String, Object> param);

    /**
     * @param map
     * @return
     * @description 2021/3/19 根据glsxid和ssmkid获取材料
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map<String, Object>> getSjcl(Map<String, Object> map);
}
