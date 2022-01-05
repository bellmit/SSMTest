package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ContractRegistrationFileMapper {

    /**
     * 分页多条件查询检索合同登记备案信息
     * @param param
     * @return
     */
    List<Map<String,Object>> getContractRegisterFileByPage(Map<String,Object> param);

    /**
     * 分页获取项目管理列表
     * @param param
     * @return
     */
    List<Map<String,Object>> getProjectManagerListByPage(Map<String,Object> param);

    List<Map<String,Object>> getChdwxxByChxmId(Map<String,Object> param);

    List<Map<String,Object>> getClsxZd(@Param("clsxSet")Set<String> param);

    List<DchyXmglZd> getConcreteClsxZd(Map<String,Object> param);

    List<DchyXmglClcgpz> getClmlFromClcgpz();

    List<String> getClmlFromClcgpzByClmc();

    String getClmlDm(@Param(value = "id") String id);

    List<DchyXmglClcg> getClcgByChgcid(Map<String,Object> param);

    List<DchyXmglClcg> getClcgByChxmid(Map<String,Object> param);

    List<DchyXmglClcg> getClcgBySqxxid(Map<String,Object> param);

    List<String> getClcgPzWj();
}
