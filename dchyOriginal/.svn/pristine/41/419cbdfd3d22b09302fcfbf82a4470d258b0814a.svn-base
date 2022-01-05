package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
@Repository
public interface DchyXmglSjclpzMapper {

    /**
     * query DISTINCT SsmkId
     *
     * @return List<String>
     */
    List<String> querySsmkId();

    /**
     * query Result List<Map>
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryResultListMap();

    /**
     * query Clsx List<Map>
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryClsxListMap();

    /**
     * query Csmk List<Map>
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryCsmkListMap();

    /**
     * query Clsx Root
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryRootClsx();

    /**
     * query Clsx Children
     *
     * @param fatherDm String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryChildrenClsx(String fatherDm);

    /**
     * query LCGPZ Children
     *
     * @param pclcgpzid String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryChildrenLcgpz(String pclcgpzid);

    /**
     * 清空表内所有数据
     */
    void dropAllRecount();

    /**
     * @description 获取序号最大值
     */
    String queryMaxXhBySsmkid(@Param(value = "ssmkid") String ssmkid);

    /**
     * @description 根据ssmkid, 测量事项获取对应需要上传的材料信息
     */
    List<Map<String, Object>> querySjclpzBySsmkIdAndClsx(Map<String, Object> map);

    /**
     * querySsclsxIdList
     *
     * @param sjclpzIdStr String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySsclsxIdList(String sjclpzIdStr);

    /**
     * drop Ssclsx
     *
     * @param id String
     */
    void dropSsclsx(String id);

    /**
     * save Ssclsx
     *
     * @param ssclsxId String
     * @param sjclpzId String
     */
    void saveSsclsx(@Param("sjclpzId") String sjclpzId, @Param("ssclsxId") String ssclsxId);

    /**
     * get SjclpzIds
     *
     * @param list List<Object>
     * @return List<String>
     */
    List<Object> getSjclpzIds(@Param("list") List<Object> list);

    /**
     * querySjclpzBySsmkIdAndSsclsxAndInSjclpzIds
     *
     * @param ssmkId String
     * @param ssclsx String
     * @param list   List<Object>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclpzBySsmkIdAndSsclsxAndInSjclpzIds(@Param("ssmkId") String ssmkId, @Param("ssclsx") Object ssclsx, @Param("list") List<Object> list);

    /**
     * query SjclpzIds By SsclsxId In
     *
     * @param list List<Object>
     * @return List<String>
     */
    List<String> querySjclpzIdsBySsclsxIdIn(@Param("list") List<Object> list);

//    /**
//     * query ChxmId And Xmzt By SjclpzId
//     *
//     * @param sjclpzId String
//     * @return List<Map < String, Object>>
//     */
//    List<Map<String, Object>> queryChxmIdAndXmztBySjclpzId(@Param("sjclpzId") String sjclpzId);
//
//    /**
//     * update Disabled By SjclpzId
//     *
//     * @param sjclpzId String
//     * @param status   String
//     */
//    void updateDisabledBySjclpzId(@Param("status") String status, @Param("sjclpzId") String sjclpzId);

}
