package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.BeanUtilsEx;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglLcgpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYwyzxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcgz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglLcgpzDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglSjclpzDto;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.promanage.builder.ReceiveConfigViewBuilder;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglSjclpzMapper;
import cn.gtmap.msurveyplat.promanage.service.ConfigureSystemService;
import cn.gtmap.msurveyplat.promanage.utils.BeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
@Service
public class ConfigureSystemServiceImpl implements ConfigureSystemService {

    protected final Log LOGGER = LogFactory.getLog(this.getClass());

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private DchyXmglSjclpzMapper dchyXmglSjclpzMapper;

    @Override
    public String addConfigure(DchyXmglSjclpz receiveConfigDo) {
        String id = "";
        try {
            id = UUIDGenerator.generate18();
            receiveConfigDo.setSjclpzid(id);
            int a = entityMapper.saveOrUpdate(receiveConfigDo, receiveConfigDo.getSjclpzid());
            if (1 > a) {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("????????????{}:", e);
        }
        return id;
    }

    @Override
    public Boolean editConfigure(DchyXmglSjclpz receiveConfigDo) {
        return 1 >= entityMapper.saveOrUpdate(receiveConfigDo, receiveConfigDo.getSjclpzid());
    }

    @Override
    public boolean dropConfigure(String id) {
        int result = entityMapper.deleteByPrimaryKey(DchyXmglSjclpz.class, id);
        return 1 <= result;
    }

    @Override
    public boolean dropConfigureByIds(List<String> ids) {
        ids.forEach(id -> {
            if (null != id && !"".equals(id)) {
                dropConfigure(id);
                dchyXmglSjclpzMapper.dropSsclsx(id);
//                //????????????????????????????????????
//                if (existSjclBySjclpzId(id)) {
//                    editDisabledBySjclpzId(id);
//                } else {
//                    dropConfigure(id);
//                    dchyXmglSjclpzMapper.dropSsclsx(id);
//                }
            }
        });
        return true;
    }

    @Override
    public boolean savaOrUpdateDtos(List<DchyXmglSjclpzDto> dchyXmglSjclpzDtoList) {
        try {
            dchyXmglSjclpzDtoList.forEach(dchyXmglSjclpzDto -> {
                DchyXmglSjclpz dchyXmglSjclpz = ReceiveConfigViewBuilder.dto2Model(dchyXmglSjclpzDto);
                if (null == dchyXmglSjclpz.getSjclpzid() || "".equals(dchyXmglSjclpz.getSjclpzid())) {
                    //save
                    String id = addConfigure(dchyXmglSjclpz);
                    if (null != id) {
                        List<String> ssclsxList = dchyXmglSjclpzDto.getSsclsxList();
                        ssclsxList.forEach(ssclsx -> {
                            dchyXmglSjclpzMapper.saveSsclsx(id, ssclsx);
                        });
                    }
                } else {
                    //update
                    editConfigure(dchyXmglSjclpz);
                    dchyXmglSjclpzMapper.dropSsclsx(dchyXmglSjclpzDto.getSjclpzid());
                    List<String> ssclsxList = dchyXmglSjclpzDto.getSsclsxList();
                    ssclsxList.forEach(ssclsx -> {
                        dchyXmglSjclpzMapper.saveSsclsx(dchyXmglSjclpzDto.getSjclpzid(), ssclsx);
                    });
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateList(List<DchyXmglSjclpz> receiveConfigDoList) {
        try {
            receiveConfigDoList.forEach(receiveConfigDo -> {
                if (null == receiveConfigDo.getSjclpzid() || "".equals(receiveConfigDo.getSjclpzid())) {
                    //save
                    addConfigure(receiveConfigDo);
                } else {
                    //update
                    editConfigure(receiveConfigDo);
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> findConfigure() {
        List<Map<String, Object>> configList = dchyXmglSjclpzMapper.queryResultListMap();
        List<String> ssmkIdList = dchyXmglSjclpzMapper.querySsmkId();
        List<Map<String, Object>> resultList = Lists.newArrayList();
        try {
            for (String ssmkId : ssmkIdList) {
                if (null == ssmkId || "".equals(ssmkId)) {
                    continue;
                }
                Map<String, Object> resultMap = Maps.newHashMap();
                resultMap.put("ssmkid", ssmkId);
                List<Map<String, Object>> receiveConfigModelsListTmp = Lists.newArrayList();
                for (Map<String, Object> receiveConfigModel : configList) {
                    if (null != ssmkId && ssmkId.equals(receiveConfigModel.get("SSMKID"))) {
                        resultMap.put("ssmk", receiveConfigModel.get("SSMK"));
                        receiveConfigModelsListTmp.add(receiveConfigModel);
                    }
                    resultMap.put("list", receiveConfigModelsListTmp);
                }
                resultList.add(resultMap);
            }
            configList.forEach(config -> {
                String sjclpzidStr = config.get("SJCLPZID").toString();
                List<Map<String, Object>> ssclsxIdList = dchyXmglSjclpzMapper.querySsclsxIdList(sjclpzidStr);
                config.put("SSCLSX", ssclsxIdList);
            });
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryClsx() {
        List<Map<String, Object>> clsxList = dchyXmglSjclpzMapper.queryClsxListMap();
        return clsxList;
    }

    @Override
    public List<Map<String, Object>> querySsmk() {
        List<Map<String, Object>> csmkList = dchyXmglSjclpzMapper.queryCsmkListMap();
        return csmkList;
    }

    @Override
    public List<Map<String, Object>> findAchievementTree() {
        List<Map<String, Object>> resultTree = dchyXmglSjclpzMapper.queryRootClsx();
        try {
            //????????????????????????
            resultTree.forEach(root -> {
                List<Map<String, Object>> children = dchyXmglSjclpzMapper.queryChildrenClsx(root.get("DM").toString());
                children = findAchievementChildren(children);
                root.put("children", children);
            });
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return resultTree;
    }

    @Override
    public boolean saveOrUpdateAchievementTree(List<DchyXmglLcgpzDto> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            return saveOrUpdateTree(dtos);
        }
        return false;
    }

    /**
     * @param dchyXmglJcgzList List<DchyXmglJcgz>
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: null
     * @time 2021/4/21 17:08
     * @description ??????????????????????????????
     */
    @Override
    public boolean updateClcgjcgz(List<DchyXmglJcgz> dchyXmglJcgzList) {
        boolean resultBool = false;
        if (CollectionUtils.isNotEmpty(dchyXmglJcgzList)) {
            for (DchyXmglJcgz dchyXmglJcgz : dchyXmglJcgzList) {
                DchyXmglYwyzxx dchyXmglYwyzxx = new DchyXmglYwyzxx();
                BeanUtilsEx.copyProperties(dchyXmglYwyzxx, dchyXmglJcgz);
                resultBool = entityMapper.updateByPrimaryKeySelective(dchyXmglYwyzxx) > 0;
                if (!resultBool) {
                    break;
                }
            }
        }
        return resultBool;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/4/21 11:03
     * @description ????????????????????????
     */
    @Override
    public List<Map<String, Object>> getCgtjjcgzList() {
        List<Map<String, Object>> cgtjjcgzpzList = Lists.newArrayList();
        Example ywyzExample = new Example(DchyXmglYwyzxx.class);
        ywyzExample.createCriteria().andEqualTo("ssmkid", SsmkidEnum.CHTJCGSH.getCode());
        List<DchyXmglYwyzxx> ywyzxxList = entityMapper.selectByExample(ywyzExample);
        if (CollectionUtils.isNotEmpty(ywyzxxList)) {
            for (DchyXmglYwyzxx dchyXmglYwyzxx : ywyzxxList) {
                Map<String, Object> cgtjjcpzMap = Maps.newHashMap();
                cgtjjcpzMap.put("ywyzxxid", dchyXmglYwyzxx.getYwyzxxid());
                cgtjjcpzMap.put("ywms", dchyXmglYwyzxx.getYwms());
                cgtjjcpzMap.put("sfqy", dchyXmglYwyzxx.getSfqy());
                cgtjjcgzpzList.add(cgtjjcpzMap);
            }
        }
        return cgtjjcgzpzList;
    }

    private boolean saveOrUpdateTree(List<DchyXmglLcgpzDto> dtos) {
        boolean resultBool = false;
        //??????dchy_xmgl_lcgpz??????
        dchyXmglSjclpzMapper.dropAllRecount();
        //??????dchy_xmgl_lcgpz??????
        List<DchyXmglLcgpz> dchyXmglLcgpzList = Lists.newArrayList();
        for (int i = 1; i <= dtos.size(); i++) {
            DchyXmglLcgpzDto dto = dtos.get(i - 1);
            DchyXmglLcgpz dchyXmglLcgpz = dto;
            dchyXmglLcgpz.setClcgpzid(i + "");
            dchyXmglLcgpzList.add(dchyXmglLcgpz);
            List<DchyXmglLcgpzDto> children = dto.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                List<DchyXmglLcgpz> childDxl = getChildren(children, dto.getClcgpzid());
                assert childDxl != null;
                dchyXmglLcgpzList.addAll(childDxl);
            }
        }
        if (!CollectionUtils.isEmpty(dchyXmglLcgpzList)) {
            List<DchyXmglLcgpz> dchyXmglLcgpzListTmp = list2Entities(dchyXmglLcgpzList);
            //??????
            entityMapper.insertBatchSelective(dchyXmglLcgpzListTmp);
            resultBool = true;
        }
        return resultBool;
    }

    private List<DchyXmglLcgpz> list2Entities(List<DchyXmglLcgpz> dchyXmglLcgpzList) {
        List<DchyXmglLcgpz> dchyXmglLcgpzs = Lists.newArrayList();
        dchyXmglLcgpzList.forEach(item -> {
            DchyXmglLcgpz dchyXmglLcgpz = new DchyXmglLcgpz();
            BeanUtils.copyProperties(item, dchyXmglLcgpz, "children");
            dchyXmglLcgpzs.add(dchyXmglLcgpz);
        });
        return dchyXmglLcgpzs;
    }

    private List<DchyXmglLcgpz> getChildren(List<DchyXmglLcgpzDto> childrenDto, String fatherId) {
        List<DchyXmglLcgpz> dchyXmglLcgpzList = Lists.newArrayList();
        for (int i = 1; i <= childrenDto.size(); i++) {
            DchyXmglLcgpz dchyXmglLcgpz = childrenDto.get(i - 1);
            dchyXmglLcgpz.setPclcgpzid(fatherId);
            dchyXmglLcgpz.setClcgpzid(fatherId + String.format("%03d", i));
            dchyXmglLcgpzList.add(dchyXmglLcgpz);
            if (!CollectionUtils.isEmpty(childrenDto.get(i - 1).getChildren())) {
                List<DchyXmglLcgpz> child = getChildren(childrenDto.get(i - 1).getChildren(), childrenDto.get(i - 1).getClcgpzid());
                dchyXmglLcgpzList.addAll(child);
            }
        }
        return dchyXmglLcgpzList;
    }

    private List<Map<String, Object>> findAchievementChildren(List<Map<String, Object>> roots) {
        try {
            roots.forEach(root -> {
                List<Map<String, Object>> childrenList = dchyXmglSjclpzMapper.queryChildrenLcgpz(root.get("CLCGPZID").toString());
                if (CollectionUtils.isNotEmpty(childrenList)) {
                    findAchievementChildren(childrenList);
                    root.put("children", childrenList);
                } else {
                    root.put("children", null);
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return roots;
    }

    //    private void editDisabledBySjclpzId(String sjclpzId) {
//        dchyXmglSjclpzMapper.updateDisabledBySjclpzId("0", sjclpzId);
//    }
//
//    /**
//     * ??????????????????????????????????????????
//     *
//     * @param sjclpzId String
//     * @return boolean true ?????? false ?????????
//     */
//    private boolean existSjclBySjclpzId(String sjclpzId) {
//        List<Map<String, Object>> chxmIdXmztList = dchyXmglSjclpzMapper.queryChxmIdAndXmztBySjclpzId(sjclpzId);
//        for (Map<String, Object> chxmIdXmzt : chxmIdXmztList) {
//            if (!"99".equals(chxmIdXmzt.get("XMZT"))) {
//                return true;
//            }
//        }
//        return false;
//    }

}
