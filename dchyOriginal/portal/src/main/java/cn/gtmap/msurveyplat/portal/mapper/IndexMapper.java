package cn.gtmap.msurveyplat.portal.mapper;


import cn.gtmap.msurveyplat.portal.entity.DchyCgglSjxx;
import cn.gtmap.msurveyplat.portal.entity.DchyCgglSqr;
import cn.gtmap.msurveyplat.portal.entity.DchyCgglXm;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author szh
 * @description 首页项目创建
 */
@Mapper
@Component
public interface IndexMapper {
    /**
     * @param dchyCgglXm 项目信息
     * @description 插入项目信息
     */
    void insertXm(DchyCgglXm dchyCgglXm);

    /**
     *
     * @param xmid 项目id
     * @return 项目信息
     * @description 查找项目信息
     */
    DchyCgglXm getXmById(String xmid);

    /**
     * @param dchyCgglXm 项目信息
     * @description 更新项目信息
     */
    void updateXm(DchyCgglXm dchyCgglXm);

    /**
     * @param cgglSjxx  收件信息
     * @description  初始化收件信息
     */
    void insertSjxx(DchyCgglSjxx cgglSjxx);

    /**
     * @param sqr 申请人信息
     * @description 保存申请人信息
     */
    void insertSqr(DchyCgglSqr sqr);
}
