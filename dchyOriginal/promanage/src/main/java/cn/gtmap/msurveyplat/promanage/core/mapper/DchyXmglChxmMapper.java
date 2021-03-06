package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglChxmMapper {

    /**
     * 根据测绘工程编号查询已有测绘工程信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryChgcByChgcbh(Map<String, Object> paramMap);

    /**
     * 根据测绘工程编号查询已有需求发布编号
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryChxmByChgcbh(Map<String, Object> paramMap);

    /**
     * 根据需求发布编号查询测绘项目信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryChxmByXqfbbh(Map<String, Object> paramMap);

    /**
     * 根据测绘项目id查询备案信息详情
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryBaxxByChxmid(Map<String, String> paramMap);

    /**
     * 根据测绘项目id查询测量事项信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryClsxByChxmid(Map<String, String> paramMap);

    /**
     * 根据测绘项目id查询测量事项测绘体量信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryClsxChtlByChxmid(Map<String, String> paramMap);

    /**
     * 根据测绘项目id查询备案信息详情，测绘单位信息
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryChdwByChxmid(Map<String, String> paramMap);

    /**
     * 查询需求发布编号流水号
     *
     * @return
     */
    String queryMaxXqfbbh();

    /**
     * 获取受理编号流水号
     *
     * @return
     */
    String queryMaxSlbh();

    /**
     * 获取项目编号流水号
     *
     * @return
     */
    String queryMaxBabh();


    String queryUsername(String userid);

    /**
     * 根据测绘工程id查询已入库成果的测绘事项
     *
     * @return
     */
    List<Map<String, Object>> queryChxmByChgcid(Map<String, Object> paramMap);


}
