package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywnrDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywsqDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywxxDO;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.dto.GxywxxDTO;
import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.common.dto.YhGxywxxDTO;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglGxywxxMapper;
import cn.gtmap.msurveyplat.server.service.share.GxywsqService;
import cn.gtmap.msurveyplat.server.service.share.GxywxxService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.alibaba.fastjson.JSON;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务信息服务
 */
@Service
public class GxywxxServiceImpl implements GxywxxService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private DchyCgglGxywxxMapper dchyCgglGxywxxMapper;
    @Autowired
    private GxywsqService gxywsqService;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;


    private final static String GXYWXX_SFJY_YES = "1"; //禁用
    private final static String GXYWXX_SFJY_NO = "0"; //非禁用
    private final boolean GXYWSQ_SQSH = AppConfig.getBooleanProperty("gxywsq.sqsh", false);


    @Override
    public Page<Map> getGxywxxByPage(int page, int size, GxywxxDTO gxywxxDTO) {
        Map param = new HashMap();
        if (gxywxxDTO != null) {
            if (StringUtils.isNotBlank(gxywxxDTO.getGxywmc())) {
                param.put("gxywmc", gxywxxDTO.getGxywmc());
            }
            if (StringUtils.isNotBlank(gxywxxDTO.getGxbmid())) {
                param.put("gxbmid", gxywxxDTO.getGxbmid());
            }
            if (StringUtils.isNotBlank(gxywxxDTO.getGxjsid())) {
                param.put("gxjsid", gxywxxDTO.getGxjsid());
            }
        }
        return repository.selectPaging("getGxywxxByPage", param, page - 1, size);
    }

    @Override
    @Transactional
    public void saveOrUpdateDchyCgglGxywxxDO(GxywxxDTO gxywxxDTO) {
        if (gxywxxDTO != null && StringUtils.isNotBlank(gxywxxDTO.getGxywid())) {
            Example example = new Example(DchyCgglGxywnrDO.class);
            example.createCriteria().andEqualTo("gxywid", gxywxxDTO.getGxywid());
            entityMapper.deleteByExample(example);
            DchyCgglGxywxxDO dchyCgglGxywxxDO = entityMapper.selectByPrimaryKey(DchyCgglGxywxxDO.class, gxywxxDTO.getGxywid());
            if (dchyCgglGxywxxDO == null) {
                dchyCgglGxywxxDO = new DchyCgglGxywxxDO();
                dchyCgglGxywxxDO.setGxywid(UUIDGenerator.generate18());
                dchyCgglGxywxxDO.setPzsj(CalendarUtil.getCurHMSDate());
                dchyCgglGxywxxDO.setSfjy(GXYWXX_SFJY_NO);
            }
            dchyCgglGxywxxDO.setGxywmc(gxywxxDTO.getGxywmc());
            dchyCgglGxywxxDO.setGxbmid(gxywxxDTO.getGxbmid());
            dchyCgglGxywxxDO.setGxbmmc(gxywxxDTO.getGxbmmc());
            dchyCgglGxywxxDO.setGxjsid(gxywxxDTO.getGxjsid());
            dchyCgglGxywxxDO.setGxjsmc(gxywxxDTO.getGxjsmc());
            dchyCgglGxywxxDO.setGxkssj(gxywxxDTO.getGxkssj());
            dchyCgglGxywxxDO.setGxjssj(gxywxxDTO.getGxjssj());
            dchyCgglGxywxxDO.setSfcqyx(gxywxxDTO.getSfcqyx());

            dchyCgglGxywxxDO.setSjgxsj(CalendarUtil.getCurHMSDate());

            List<Object> recordList = new ArrayList<>();
            recordList.add(dchyCgglGxywxxDO);
            if (CollectionUtils.isNotEmpty(gxywxxDTO.getGxnrList())) {
                for (String clcgpzid : gxywxxDTO.getGxnrList()) {
                    DchyCgglGxywnrDO dchyCgglGxywnrDO = new DchyCgglGxywnrDO();
                    dchyCgglGxywnrDO.setGxywnrid(UUIDGenerator.generate18());
                    dchyCgglGxywnrDO.setGxywid(dchyCgglGxywxxDO.getGxywid());
                    dchyCgglGxywnrDO.setClcgpzid(clcgpzid);
                    recordList.add(dchyCgglGxywnrDO);
                }
            }
            entityMapper.batchSaveSelective(recordList);
        }
    }


    @Override
    public void disableEnableGxywxxById(String gxywxxid) {
        if (StringUtils.isNotBlank(gxywxxid)) {
            DchyCgglGxywxxDO dchyCgglGxywxxDO = entityMapper.selectByPrimaryKey(DchyCgglGxywxxDO.class, gxywxxid);
            String sfjy = dchyCgglGxywxxDO.getSfjy();
            if (dchyCgglGxywxxDO != null && GXYWXX_SFJY_NO.equals(sfjy)) {
                dchyCgglGxywxxDO.setSfjy(GXYWXX_SFJY_YES);
                entityMapper.saveOrUpdate(dchyCgglGxywxxDO, dchyCgglGxywxxDO.getGxywid());
            } else if (dchyCgglGxywxxDO != null && GXYWXX_SFJY_YES.equals(sfjy)) {
                dchyCgglGxywxxDO.setSfjy(GXYWXX_SFJY_NO);
                entityMapper.saveOrUpdate(dchyCgglGxywxxDO, dchyCgglGxywxxDO.getGxywid());
            }
        }
    }

    @Override
    public List<Map> getGxywnrpzxx() {
        Map param = new HashMap();
        List<DchyXmglClcgpz> dchyXmglClcgpzList = dchyCgglGxywxxMapper.getDchyXmglClcgpzList(param);
        return organizeData(dchyXmglClcgpzList);
    }

    @Override
    public GxywxxDTO getGxywxx(String gxywid) {
        GxywxxDTO gxywxxDTO = null;
        DchyCgglGxywxxDO dchyCgglGxywxxDO = entityMapper.selectByPrimaryKey(DchyCgglGxywxxDO.class, gxywid);
        if (dchyCgglGxywxxDO != null) {
            gxywxxDTO = JSON.parseObject(JSON.toJSONString(dchyCgglGxywxxDO), GxywxxDTO.class);
            List<DchyCgglGxywnrDO> dchyCgglGxywnrDOList = getDchyCgglGxywnrDOList(gxywid);
            List<String> clcgpzidList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(dchyCgglGxywnrDOList)) {
                for (DchyCgglGxywnrDO dchyCgglGxywnrDO : dchyCgglGxywnrDOList) {
                    clcgpzidList.add(dchyCgglGxywnrDO.getClcgpzid());
                }
            }
            gxywxxDTO.setGxnrList(clcgpzidList);
        }
        return gxywxxDTO;
    }

    @Override
    public List<YhGxywxxDTO> getYhGxywxx(YhGxywxxDTO yhGxywxxDTO, UserInfo userInfo) {
        List<YhGxywxxDTO> yhGxywxxDTOList = new ArrayList<>();
        if (userInfo != null) {
            Map param = new HashMap();
            if (yhGxywxxDTO != null) {
                param.put("gxywid", yhGxywxxDTO.getGxywid());
            }
            if (!userInfo.isAdmin()) {
                if (CollectionUtils.isNotEmpty(userInfo.getPfRoleVoList())) {
                    param.put("roleList", userInfo.getPfRoleVoList());
                }
            }
            List<DchyCgglGxywxxDO> dchyCgglGxywxxDOList = dchyCgglGxywxxMapper.getDchyCgglGxywxxDOList(param);
            if (CollectionUtils.isNotEmpty(dchyCgglGxywxxDOList)) {
                for (DchyCgglGxywxxDO dchyCgglGxywxxDO : dchyCgglGxywxxDOList) {
                    YhGxywxxDTO yhGxywxxDtoTemp = new YhGxywxxDTO();
                    yhGxywxxDtoTemp.setGxywid(dchyCgglGxywxxDO.getGxywid());
                    yhGxywxxDtoTemp.setGxywmc(dchyCgglGxywxxDO.getGxywmc());
                    List<DchyCgglGxywnrDO> dchyCgglGxywnrDOList = getDchyCgglGxywnrDOList(dchyCgglGxywxxDO.getGxywid());
                    if (CollectionUtils.isNotEmpty(dchyCgglGxywnrDOList)) {
                        param.clear();
                        param.put("dchyCgglGxywnrDOList", dchyCgglGxywnrDOList);
                        DchyXmglZd dchyXmglZd = dchyCgglGxywxxMapper.getDchyXmglZdClsxList(param);
                        if (dchyXmglZd != null) {
                            yhGxywxxDtoTemp.setClsx(dchyXmglZd.getMc());
                        }
                        if (!GXYWSQ_SQSH) {
                            yhGxywxxDtoTemp.setSqshzt(Constants.GXYWSQ_SHZT_SHTG);
                        } else {
                            DchyCgglGxywsqDO dchyCgglGxywsqDO = new DchyCgglGxywsqDO();
                            dchyCgglGxywsqDO.setSqrid(userInfo.getId());
                            dchyCgglGxywsqDO.setChxmid(yhGxywxxDTO.getChxmid());
                            dchyCgglGxywsqDO.setGxywid(yhGxywxxDtoTemp.getGxywid());
                            // 获取申请状态，默认未申请
                            yhGxywxxDtoTemp.setSqshzt(gxywsqService.getGxywsqShzt(dchyCgglGxywsqDO));
                        }
                    }
                    yhGxywxxDTOList.add(yhGxywxxDtoTemp);
                }
            }
        }
        return yhGxywxxDTOList;
    }

    /**
     * @param yhGxywxxDTO
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description 获取所有共享业务信息
     */
    @Override
    public List<YhGxywxxDTO> getAllGxywxx(YhGxywxxDTO yhGxywxxDTO) {
        List<YhGxywxxDTO> yhGxywxxDTOList = new ArrayList<>();

        Map param = new HashMap();
        if (yhGxywxxDTO != null) {
            param.put("gxywid", yhGxywxxDTO.getGxywid());
        }
        List<DchyCgglGxywxxDO> dchyCgglGxywxxDOList = dchyCgglGxywxxMapper.getDchyCgglGxywxxDOList(param);
        if (CollectionUtils.isNotEmpty(dchyCgglGxywxxDOList)) {
            for (DchyCgglGxywxxDO dchyCgglGxywxxDO : dchyCgglGxywxxDOList) {
                YhGxywxxDTO yhGxywxxDtoTemp = new YhGxywxxDTO();
                yhGxywxxDtoTemp.setGxywid(dchyCgglGxywxxDO.getGxywid());
                yhGxywxxDtoTemp.setGxywmc(dchyCgglGxywxxDO.getGxywmc());
                List<DchyCgglGxywnrDO> dchyCgglGxywnrDOList = getDchyCgglGxywnrDOList(dchyCgglGxywxxDO.getGxywid());
                if (CollectionUtils.isNotEmpty(dchyCgglGxywnrDOList)) {
                    param.clear();
                    param.put("dchyCgglGxywnrDOList", dchyCgglGxywnrDOList);
                    DchyXmglZd dchyXmglZd = dchyCgglGxywxxMapper.getDchyXmglZdClsxList(param);
                    if (dchyXmglZd != null) {
                        yhGxywxxDtoTemp.setClsx(dchyXmglZd.getMc());
                    }
                    yhGxywxxDtoTemp.setSqshzt(Constants.GXYWSQ_SHZT_SHTG);

                }
                yhGxywxxDTOList.add(yhGxywxxDtoTemp);
            }
        }
        return yhGxywxxDTOList;
    }

    /**
     * @param dchyXmglClcgpzList 共享业务内容配置信息
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 递归组织共享业务内容信息数据
     */
    private List<Map> organizeData(List<DchyXmglClcgpz> dchyXmglClcgpzList) {
        List<Map> resultMapList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList)) {
            Map param = new HashMap();
            for (DchyXmglClcgpz dchyXmglClcgpz : dchyXmglClcgpzList) {
                Map resultMap = new HashMap();
                resultMap.put("id", dchyXmglClcgpz.getClcgpzid());
                resultMap.put("title", dchyXmglClcgpz.getClmc());
                resultMap.put("type", dchyXmglClcgpz.getWjlx()); //1：目录，2：文件
                resultMap.put("spread", true); //默认展开
                param.put("pclcgpzid", dchyXmglClcgpz.getClcgpzid());
                List<DchyXmglClcgpz> dchyXmglClcgpzListTemp = dchyCgglGxywxxMapper.getDchyXmglClcgpzList(param);
                List<Map> childList = organizeData(dchyXmglClcgpzListTemp);
                resultMap.put("children", childList);
                resultMapList.add(resultMap);
            }
        }
        return resultMapList;
    }


    /**
     * @param gxywid 共享业务ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务内容信息
     */
    private List<DchyCgglGxywnrDO> getDchyCgglGxywnrDOList(String gxywid) {
        List<DchyCgglGxywnrDO> dchyCgglGxywnrDOList = null;
        if (StringUtils.isNotBlank(gxywid)) {
            Example example = new Example(DchyCgglGxywnrDO.class);
            example.createCriteria().andEqualTo("gxywid", gxywid);
            dchyCgglGxywnrDOList = entityMapper.selectByExample(example);
        }
        return dchyCgglGxywnrDOList;
    }

    /**
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取部门信息
     */
    public List<PfOrganVo> getPfOrganVoList() {
        return exchangeFeignUtil.getPfOrganVoList();
    }

    /**
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取部门信息
     */
    public List<PfRoleVo> getRoleListByOrganid(String organid) {
        return exchangeFeignUtil.getRoleListByOrganid(organid);
    }

}
