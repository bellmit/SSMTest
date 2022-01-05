package cn.gtmap.msurveyplat.server.service.onemap.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.vo.ProjectDetailVo;
import cn.gtmap.msurveyplat.common.vo.ProjectInformationVo;
import cn.gtmap.msurveyplat.common.vo.SurveyItemVo;
import cn.gtmap.msurveyplat.server.config.DjxlGxConfig;
import cn.gtmap.msurveyplat.server.config.ProjectDetailConfig;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglXmService;
import cn.gtmap.msurveyplat.server.service.onemap.OnemapService;
import cn.gtmap.msurveyplat.server.util.Constants;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 一张图接口实现
 */
@Service
public class OnemapServiceImpl implements OnemapService {
    @Autowired
    private DchyCgglXmService dchyCgglXmService;
    @Autowired
    private DjxlGxConfig djxlGxConfig;
    @Autowired
    private ProjectDetailConfig projectDetailConfig;
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<ProjectDetailVo> getProjectDetailVoList(String chxmbh) throws  IOException, ClassNotFoundException {
        List<ProjectDetailVo> projectDetailVoList = null;
        /**
         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
         * @description 获取默认配置的项目详情
         */
        List<ProjectDetailVo> projectDetailVoConfigList = projectDetailConfig.getProjectDetailVoList();
        projectDetailVoList = CommonUtil.deepCopy(projectDetailVoConfigList);
        List<DchyCgglXmDO> dchyCgglXmDOList = dchyCgglXmService.getDchyCgglXmDOList(chxmbh,Constants.RKZT_YRK_DM,Constants.XMZT_YBJ_DM);
        if(CollectionUtils.isNotEmpty(projectDetailVoList)&&CollectionUtils.isNotEmpty(dchyCgglXmDOList)) {
            for(ProjectDetailVo projectDetailVo:projectDetailVoList) {
                List<SurveyItemVo> surveyItemVoList = projectDetailVo.getSurveyItemVoList();
                if(StringUtils.equals(projectDetailVo.getChjdmc(),Constants.CHJD_JGYS_MC)&&CollectionUtils.isNotEmpty(surveyItemVoList)) {
                    projectDetailVo.setDchyCgglXmDOList(dchyCgglXmDOList);
                    for(SurveyItemVo surveyItemVo:surveyItemVoList) {
                        for(DchyCgglXmDO dchyCgglXmDO:dchyCgglXmDOList) {
                            String chsxmc = getChsxByDjxl(dchyCgglXmDO.getDjxl());
                            if(StringUtils.isNotBlank(chsxmc)&&StringUtils.equals(surveyItemVo.getChsxmc(),chsxmc)) {
                                surveyItemVo.setSfwcsx(true);
                                surveyItemVo.setXmid(dchyCgglXmDO.getXmid());
                            }
                        }
                    }
                }
            }
        }
        if(projectDetailVoList == null) {
            projectDetailVoList = new ArrayList<>();
        }
        return projectDetailVoList;
    }

    @Override
    public String getViewProjectUrl(String xmid) {
        StringBuilder viewProjectUrl = new StringBuilder();
        String ckxmUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("onemap.xmxq.ckxm.url"));
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        if(dchyCgglXmDO != null&&StringUtils.isNotBlank(ckxmUrl)&&StringUtils.isNotBlank(xmid)) {
            viewProjectUrl.append(AppConfig.getProperty("portal.url")).append(ckxmUrl).append("&xmid=").append(dchyCgglXmDO.getXmid()).append("&gzlslid=").append(dchyCgglXmDO.getGzlslid());
        }
        return viewProjectUrl.toString();
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param djxl 登记小类
     * @return
     * @description 根据登记小类获取测绘事项名称
     */
    private String getChsxByDjxl(String djxl) {
        String chsxmc = "";
        if(StringUtils.isNotBlank(djxl)) {
            List<Map> djxlChjdMapList = djxlGxConfig.getDjxlChsxMapList();
            for(Map djxlChjdMap:djxlChjdMapList) {
                if(StringUtils.equals(CommonUtil.formatEmptyValue(djxlChjdMap.get("djxl")),djxl)){
                    chsxmc = CommonUtil.formatEmptyValue(djxlChjdMap.get("chsxmc"));
                    break;
                }
            }
        }
        return chsxmc;
    }


    @Override
    public List<DchyCgglXmDO> getDchyCgglXm(String xmbh, String xmmc, String xmdz) {
        Example example = new Example(DchyCgglXmDO.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(xmbh)) {
            criteria.andLike("chxmbh",xmbh);
        }
        if (StringUtils.isNotBlank(xmmc)) {
            criteria.andLike("xmmc",xmmc);
        }
        if (StringUtils.isNotBlank(xmdz)) {
            criteria.andLike("xmdz",xmdz);
        }
        criteria.andEqualTo("djxl","0001").andEqualTo("rkzt",Constants.RKZT_YRK_DM).andEqualTo("xmzt",Constants.XMZT_YBJ_DM);
        return entityMapper.selectByExample(example);
    }

    @Override
    public ProjectInformationVo getProjectInformationVo(String dchybh) throws IOException, ClassNotFoundException {
        ProjectInformationVo projectInformationVo = new ProjectInformationVo();
        List<DchyCgglXmDO> dchyCgglXmDOList = dchyCgglXmService.getDchyCgglXmDOListByDchybh(dchybh,Constants.RKZT_YRK_DM,Constants.XMZT_YBJ_DM);
        if(CollectionUtils.isNotEmpty(dchyCgglXmDOList)) {
            DchyCgglXmDO dchyCgglXmDO = dchyCgglXmDOList.get(0);
            projectInformationVo.setDchyCgglXmDO(dchyCgglXmDOList.get(0));
            List<DchyCgglXmDO> dchyCgglXmDOListTemp = dchyCgglXmService.getDchyCgglXmDOList(dchyCgglXmDO.getChxmbh(),Constants.RKZT_YRK_DM,Constants.XMZT_YBJ_DM);

            List<ProjectDetailVo> projectDetailVoConfigList = projectDetailConfig.getProjectDetailVoList();
            List<SurveyItemVo> surveyItemVoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(projectDetailVoConfigList)) {
                for(ProjectDetailVo projectDetailVo:projectDetailVoConfigList) {
                    if(projectDetailVo.getSfdqchjd()) {
                        surveyItemVoList = CommonUtil.deepCopy(projectDetailVo.getSurveyItemVoList());
                        break;
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(surveyItemVoList)&&CollectionUtils.isNotEmpty(dchyCgglXmDOListTemp)) {
                for(SurveyItemVo surveyItemVo:surveyItemVoList) {
                    for(DchyCgglXmDO dchyCgglXmDOTemp:dchyCgglXmDOListTemp) {
                        String chsxmc = getChsxByDjxl(dchyCgglXmDOTemp.getDjxl());
                        if(StringUtils.isNotBlank(chsxmc)&&StringUtils.equals(surveyItemVo.getChsxmc(),chsxmc)) {
                            surveyItemVo.setSfwcsx(true);
                            surveyItemVo.setXmid(dchyCgglXmDOTemp.getXmid());
                            surveyItemVo.setSlbh(dchyCgglXmDOTemp.getSlbh());
                        }
                    }
                }
            }
            projectInformationVo.setSurveyItemVoList(surveyItemVoList);
        }
        return projectInformationVo;
    }
}
