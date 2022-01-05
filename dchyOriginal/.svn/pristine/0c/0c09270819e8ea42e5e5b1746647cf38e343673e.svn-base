package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 6k
 */
@Repository
public interface DchyXmglSjclMapper {

    /**
     * 获取到收件材料信息
     *
     * @param glsxid String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> getHtxxSjclXx(@Param(value = "glsxid") String glsxid);

    /**
     * query Sjcl By SsmkId And Glsxid
     *
     * @param ssmkId String
     * @param glsxId String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclBySsmkIdAndGlsxid(@Param("ssmkId") String ssmkId, @Param("glsxId") String glsxId);

    /**
     * query SjclBySjclpzId And SjxxId
     *
     * @param sjclpzId String
     * @param glsxId   String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclBySjclpzIdAndSjxxId(@Param("sjclpzId") String sjclpzId, @Param("glsxId") String glsxId);

    /**
     * get Sjxx By GlsxId
     *
     * @param glsxId String
     * @return String
     */
    String getSjxxByGlsxId(@Param("glsxId") String glsxId);

    /**
     * querySjclpzBySjclpzIdAndSsmkId
     *
     * @param sjclpzId String
     * @param ssmkId   String
     * @return Map<String, Object>
     */
    Map<String, Object> querySjclpzBySjclpzIdAndSsmkId(@Param("sjclpzId") String sjclpzId, @Param("ssmkId") String ssmkId);

    /**
     * querySsclsxsBySjclpzId
     *
     * @param sjclpzId String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySsclsxsBySjclpzId(@Param("sjclpzId") String sjclpzId);

    /**
     * querySjclsByGlsxIdAndSsmkId
     *
     * @param glsxId String
     * @param ssmkId String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclsByGlsxIdAndSsmkId(@Param("glsxId") String glsxId, @Param("ssmkId") String ssmkId);

    /**
     * query Zd By In Dm
     *
     * @param list List<Object>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryZdByInDm(@Param("list") List<Object> list);

    /**
     * countSjclBySjxxIdAndSjclpzId
     *
     * @param sjxxId   String
     * @param sjclpzId String
     * @return int
     */
    int countSjclBySjxxIdAndSjclpzId(@Param(value = "sjxxId") String sjxxId, @Param(value = "sjclpzId") String sjclpzId);

    /**
     * querySjclBySjxxId
     *
     * @param glsxId String
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclBySjxxId(@Param(value = "glsxId") String glsxId);

}