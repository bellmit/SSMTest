package cn.gtmap.msurveyplat.server.service.share.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywrzDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywxxDO;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgShjl;
import cn.gtmap.msurveyplat.common.dto.GxchgcClsxDTO;
import cn.gtmap.msurveyplat.common.dto.GxchgcxxDTO;
import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglGxywxxMapper;
import cn.gtmap.msurveyplat.server.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.server.service.share.GxchgcxxService;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享测绘工程信息服务
 */
@Service
public class GxchgcxxServiceImpl implements GxchgcxxService {
    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;
    @Autowired
    private DchyCgglGxywxxMapper dchyCgglGxywxxMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private EntityMapper entityMapper;

    private Logger logger = LoggerFactory.getLogger(GxchgcxxServiceImpl.class);

    @Override
    public Page<Map> getDchyXmglChgcxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO) {
        Map param = new HashMap();
        if (gxchgcxxDTO != null) {
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcbh())) {
                param.put("gcbh", gxchgcxxDTO.getGcbh());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcmc())) {
                param.put("gcmc", gxchgcxxDTO.getGcmc());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getWtdw())) {
                param.put("wtdw", gxchgcxxDTO.getWtdw());
            }
        }
        return repository.selectPaging("getDchyXmglChgcxxByPage", param, page - 1, size);
    }

    @Override
    public Page<Map> getDchyXmglClcgxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO) {
        Map param = new HashMap();
        if (gxchgcxxDTO != null) {
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcbh())) {
                param.put("gcbh", gxchgcxxDTO.getGcbh());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcmc())) {
                param.put("gcmc", gxchgcxxDTO.getGcmc());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getWtdw())) {
                param.put("wtdw", gxchgcxxDTO.getWtdw());
            }
        }
        return repository.selectPaging("getDchyXmglClcgxxByPage", param, page - 1, size);
    }

    @Override
    public Page<Map> getDchyClcgxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO, UserInfo userInfo) {
        logger.info("开始调用成果获取接口：rest/v1.0/gx/clcgxx/list");
        logger.info("成果查询入参page:{},size:{},gcxxDTO:{},userInfo:{}",page,size,gxchgcxxDTO,userInfo);
        Map param = new HashMap();
        // 判断是否有共享业务
        boolean gxqx = false;
        if (userInfo != null && StringUtils.isNotBlank(userInfo.getId())) {
            Map roleParam = new HashMap();
            List<PfRoleVo> pfRoleVoList = exchangeFeignUtil.getPfRoleVoList(userInfo.getId());
            logger.info("pfRoleVoList:{}",pfRoleVoList);
            if (!userInfo.isAdmin()) {
                if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
                    roleParam.put("roleList", pfRoleVoList);
                }
            }
            List<String> cgclclsxList = dchyCgglGxywxxMapper.getCgclClsxList(roleParam);
            logger.info("成果测量事项cgclsxList:{}",cgclclsxList);
            if (CollectionUtils.isNotEmpty(cgclclsxList) && !userInfo.isAdmin()) {
                param.put("cgclclsxList", cgclclsxList);
                gxqx = true;
            } else if (userInfo.isAdmin()) {
                gxqx = true;
            }
        }
        if (gxqx && gxchgcxxDTO != null) {
            if (StringUtils.isNotBlank(gxchgcxxDTO.getSlbh())) {
                param.put("slbh", gxchgcxxDTO.getSlbh());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getBabh())) {
                param.put("babh", gxchgcxxDTO.getBabh());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcbh())) {
                param.put("gcbh", gxchgcxxDTO.getGcbh());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getGcmc())) {
                param.put("gcmc", gxchgcxxDTO.getGcmc());
            }
            if (StringUtils.isNotBlank(gxchgcxxDTO.getWtdw())) {
                param.put("wtdw", gxchgcxxDTO.getWtdw());
            }
        } else if (!gxqx){
            // 无共享权限用户查询，不报错
            Pageable pageable = new PageRequest(page, size);
            return new PageImpl(Lists.newArrayList(), pageable, 0);
        }
        logger.info("分页查询参数param:{}",param);
        Page<Map> clcgxx = repository.selectPaging("getDchyClcgxxByPage", param, page - 1, size);
        logger.info("分页查询结果:{}",clcgxx.getContent());
        return clcgxx;
    }

    @Override
    public List<GxchgcClsxDTO> getGxChgcClsxListById(String chgcid) {
        List<GxchgcClsxDTO> gxchgcClsxDTOList = new ArrayList<>();
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = null;
        if (StringUtils.isNotBlank(chgcid)) {
            dchyXmglChxmClsxList = dchyXmglChgcMapper.getDchyXmglChgcClsxListByChgcid(chgcid);
        }
        if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
            GxchgcClsxDTO gxchgcClsxDTO = null;
            for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                gxchgcClsxDTO = new GxchgcClsxDTO();
                gxchgcClsxDTO.setClsxmc(dchyXmglChxmClsx.getClsx());
                gxchgcClsxDTO.setClsxid(dchyXmglChxmClsx.getClsxid());
                gxchgcClsxDTO.setChxmid(dchyXmglChxmClsx.getClsxid());

                List<String> clsxidList = null;
                if (StringUtils.isNotBlank(dchyXmglChxmClsx.getClsxid())) {
                    clsxidList = Arrays.asList(dchyXmglChxmClsx.getClsxid().split(","));
                }
                DchyXmglClcgShjl dchyXmglClcgShjl = null;
                if (CollectionUtils.isNotEmpty(clsxidList)) {
                    Map param = new HashMap();
                    param.put("clsxidList", clsxidList);
                    dchyXmglClcgShjl = dchyXmglChgcMapper.getNewDchyXmglClcgShjl(param);
                    gxchgcClsxDTO.setWtdw(dchyXmglChgcMapper.getChdmcByClsx(param));
                }

                if (StringUtils.isBlank(gxchgcClsxDTO.getWtdw()) && StringUtils.isNotBlank(gxchgcClsxDTO.getChxmid())) {
                    List<String> chxmidList = null;
                    if (StringUtils.isNotBlank(dchyXmglChxmClsx.getChxmid())) {
                        chxmidList = Arrays.asList(dchyXmglChxmClsx.getChxmid().split(","));
                    }
                    if (CollectionUtils.isNotEmpty(chxmidList)) {
                        Map param = new HashMap();
                        param.put("chxmidList", chxmidList);
                        gxchgcClsxDTO.setWtdw(dchyXmglChgcMapper.getChdmcByChxm(param));
                    }
                }

                if (dchyXmglClcgShjl != null) {
                    if (dchyXmglClcgShjl.getShsj() != null) {
                        gxchgcClsxDTO.setZxrksj(CalendarUtil.sdf_HMS.format(dchyXmglClcgShjl.getShsj()));
                        gxchgcClsxDTO.setZxshsj(CalendarUtil.sdf_HMS.format(dchyXmglClcgShjl.getShsj()));
                    }
                    if (StringUtils.isNotBlank(dchyXmglClcgShjl.getShr())) {
                        List<PfOrganVo> pfOrganVoList = exchangeFeignUtil.getOrganListByUser(dchyXmglClcgShjl.getShr());
                        if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
                            gxchgcClsxDTO.setZxshrybm(dchyXmglClcgShjl.getShrmc() + "(" + pfOrganVoList.get(0).getOrganName() + ")");
                        }
                    }
                }
                gxchgcClsxDTOList.add(gxchgcClsxDTO);
            }
        }
        return gxchgcClsxDTOList;
    }

    @Override
    public Map getGxchgcclDownUrl(String babh, String gxywid, UserInfo userInfo) {
        Map resultMap = new HashMap();
        Map paramMap = new HashMap();
        long gxJssj = 0;
        if (userInfo != null && StringUtils.isNotBlank(userInfo.getId())) {
            Map param = new HashMap();
            if (StringUtils.isNotBlank(gxywid)) {
                param.put("gxywId", gxywid);
            }
            List<PfRoleVo> pfRoleVoList = exchangeFeignUtil.getPfRoleVoList(userInfo.getId());
            if (!userInfo.isAdmin()) {
                if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
                    param.put("roleList", pfRoleVoList);
                }
            }
            List<String> cgclmcList = dchyCgglGxywxxMapper.getCgclmcList(param);
            List<Map<String, String>> cgclidList = dchyCgglGxywxxMapper.getCgclidList(param);
            List<String> cgmlmcList = getmlmcList(cgclidList);
            paramMap.put("cgmlmcList", cgmlmcList);
            paramMap.put("cgclmcList", cgclmcList);
            List<DchyCgglGxywxxDO> gxsjList = dchyCgglGxywxxMapper.getGxsjList(param);
            if (gxsjList.isEmpty()) {
                resultMap.put("code", "5003");
                resultMap.put("msg", "没有对应权限，请联系管理员配置");
                return resultMap;
            }
            if (gxsjList.get(0) == null) {
                resultMap.put("code", "5002");
                resultMap.put("msg", "共享业务配置有误");
                return resultMap;
            }
            Date gxjssj = gxsjList.get(0).getGxjssj();
            String sfcqyx = gxsjList.get(0).getSfcqyx();
            long nowDate = new Date().getTime();
            if (gxjssj != null) {
                gxJssj = gxjssj.getTime();
            }
            if (gxJssj < nowDate && !"1".equals(sfcqyx) && !userInfo.isAdmin()) {
                resultMap.put("code", "5001");
                resultMap.put("msg", "您的共享权限已超期，请联系管理员进行配置");
                return resultMap;
            } else {
                resultMap = exchangeFeignUtil.getGxchgcclDownUrl(paramMap, babh);
            }

        }

        if (resultMap != null && resultMap.containsKey("url") && StringUtils.isNotBlank(gxywid)) {
            DchyCgglGxywrzDO dchyCgglGxywrzDO = new DchyCgglGxywrzDO();
            dchyCgglGxywrzDO.setRzid(UUIDGenerator.generate18());
            dchyCgglGxywrzDO.setGxrid(userInfo.getId());
            dchyCgglGxywrzDO.setGxrmc(userInfo.getUsername());
            List<PfOrganVo> pfOrganVoList = exchangeFeignUtil.getOrganListByUser(userInfo.getId());
            if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
                dchyCgglGxywrzDO.setGxbmid(pfOrganVoList.get(0).getOrganId());
                dchyCgglGxywrzDO.setGxbmmc(pfOrganVoList.get(0).getOrganName());
            }
            dchyCgglGxywrzDO.setGxsj(CalendarUtil.getCurHMSDate());
            List<PfRoleVo> pfRoleVoList = exchangeFeignUtil.getPfRoleVoList(userInfo.getId());
            if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
                dchyCgglGxywrzDO.setGxjsid(pfRoleVoList.get(0).getRoleId());
                dchyCgglGxywrzDO.setGxjsmc(pfRoleVoList.get(0).getRoleName());
            }
            DchyCgglGxywxxDO dchyCgglGxywxxDO = entityMapper.selectByPrimaryKey(DchyCgglGxywxxDO.class, gxywid);
            if (dchyCgglGxywxxDO != null) {
                dchyCgglGxywrzDO.setGxywmc(dchyCgglGxywxxDO.getGxywmc());
            }
            entityMapper.saveOrUpdate(dchyCgglGxywrzDO, dchyCgglGxywrzDO.getRzid());
        }
        return resultMap;
    }

    //递归获取文件目录
    public List<String> getmlmcList(List<Map<String, String>> cgclidList) {
        List<String> resultList = Lists.newArrayList();
        for (Map<String, String> map : cgclidList) {
            String clmc = map.get("CLMC");
            String clid = map.get("CLCGPZID");
            getWjmById(clid, resultList);
        }
        return resultList;
    }

    private void getWjmById(String clid, List<String> list) {
        Map map = dchyCgglGxywxxMapper.getWjmById(clid);
        String wjm = map.get("CLMC").toString();
        if (map.get("PCLCGPZID") != null) {
            if (StringUtils.isNotBlank(map.get("PCLCGPZID").toString())) {
                String pzid = map.get("PCLCGPZID").toString();
                list.add(wjm);
                getWjmById(pzid, list);
            }
        } else {
            list.add(wjm);
        }
    }


}
