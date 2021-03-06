package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.JsdwFbwtService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2021/02/24
 * @description 线上数据同步线下数据
 */
@Service
public class JsdwFbwtServiceImpl implements JsdwFbwtService {

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Override
    public DchyXmglChxmDto getSingleData(String chxmid) {

        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = Lists.newArrayList();
        //测绘项目数据
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        if (null != dchyXmglChxm && StringUtils.isNotBlank(dchyXmglChxm.getChxmid())) {
            dchyXmglChxm.setWjzxid(Constants.WJZXID_FLAG);
            List<DchyXmglChxm> dchyXmglChxmList = new ArrayList<>();
            dchyXmglChxmList.add(dchyXmglChxm);
            dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
            //测绘工程数据
            if (StringUtils.isNotBlank(dchyXmglChxm.getChgcid())) {
                DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgc);
            }

            //测绘事项数据
            if (StringUtils.isNotBlank(dchyXmglChxm.getChxmid())) {
                Example exampleChsx = new Example(DchyXmglChxmClsx.class);
                exampleChsx.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(exampleChsx);
                dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);
                //测绘事项数据
                List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxListAll = new ArrayList<>();

                if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                    for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                        if (StringUtils.isNotBlank(dchyXmglChxmClsx.getClsxid())) {
                            Example exampleClsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
                            exampleClsxChdwxxGx.createCriteria().andEqualTo("clsxid", dchyXmglChxmClsx.getClsxid());
                            List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList = entityMapper.selectByExample(exampleClsxChdwxxGx);
                            if (CollectionUtils.isNotEmpty(dchyXmglClsxChdwxxGxList)) {
                                dchyXmglClsxChdwxxGxListAll.addAll(dchyXmglClsxChdwxxGxList);
                            }

                        }
                    }
                }
                dchyXmglChxmDto.setDchyXmglClsxChdwxxGxList(dchyXmglClsxChdwxxGxListAll);

                //测绘单位测绘项目
                Example exampleChxmChdwxx = new Example(DchyXmglChxmChdwxx.class);
                exampleChxmChdwxx.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapper.selectByExample(exampleChxmChdwxx);
                dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);
            }

            //收件信息数据
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                dchyXmglChxmDto.setDchyXmglSjxxList(dchyXmglSjxxList);
                //收件材料数据
                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                    Example exampleSjcl = new Example(DchyXmglSjcl.class);
                    exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                    List<DchyXmglSjcl> dchyXmglSjClList = entityMapper.selectByExample(exampleSjcl);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjClList)) {
                        dchyXmglChxmDto.setDchyXmglSjclList(dchyXmglSjClList);
                    }
                }
            }

            /*涉及合同的三张表*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx dchyXmglHtxx : htxxList) {
                    String htxxid = dchyXmglHtxx.getHtxxid();
                    DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
                    dchyXmglHtxxDto.setDchyXmglHtxx(dchyXmglHtxx);

                    /*测绘单位与合同*/
                    Example chdwxxExample = new Example(DchyXmglHtxxChdwxxGx.class);
                    chdwxxExample.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("htxxid", htxxid);
                    List<DchyXmglHtxxChdwxxGx> chdwxxGxList = entityMapper.selectByExample(chdwxxExample);
                    if (CollectionUtils.isNotEmpty(chdwxxGxList)) {
                        dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(chdwxxGxList);
                    }

                    /*测量事项与合同*/
                    Example clsxHtxxExample = new Example(DchyXmglClsxHtxxGx.class);
                    clsxHtxxExample.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("htxxid", htxxid);
                    List<DchyXmglClsxHtxxGx> clsxHtxxGxList = entityMapper.selectByExample(clsxHtxxExample);
                    if (CollectionUtils.isNotEmpty(clsxHtxxGxList)) {
                        dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(clsxHtxxGxList);
                    }

                    //合同收件信息数据
                    Example exampleSjxxHt = new Example(DchyXmglSjxx.class);
                    exampleSjxxHt.createCriteria().andEqualTo("glsxid", chxmid);
                    List<DchyXmglSjxx> dchyXmglSjxxHtList = entityMapper.selectByExample(exampleSjxxHt);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxHtList)) {
                        dchyXmglHtxxDto.setDchyXmglSjxxList(dchyXmglSjxxHtList);
                        //合同收件材料数据
                        for (DchyXmglSjxx dchyXmglSjxxHt : dchyXmglSjxxHtList) {
                            Example exampleSjclHt = new Example(DchyXmglSjcl.class);
                            exampleSjclHt.createCriteria().andEqualTo("sjxxid", dchyXmglSjxxHt.getSjxxid());
                            List<DchyXmglSjcl> dchyXmglSjClHtList = entityMapper.selectByExample(exampleSjclHt);
                            if (CollectionUtils.isNotEmpty(dchyXmglSjClHtList)) {
                                dchyXmglHtxxDto.setDchyXmglSjclList(dchyXmglSjClHtList);
                            }
                        }
                    }

                    dchyXmglHtxxDtoList.add(dchyXmglHtxxDto);
                }
                dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);
            }

            /*申请信息*/
            Example sqxxExample = new Example(DchyXmglSqxx.class);
            sqxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
            if (CollectionUtils.isNotEmpty(sqxxList)) {
                dchyXmglChxmDto.setDchyXmglSqxxList(sqxxList);
            }
        }
        return dchyXmglChxmDto;
    }

    @Override
    public Map<String, Object> getAllData(Map param) {

        Map<String, Object> map = new HashMap<>();

        String chxmidSaveOrUpdate = CommonUtil.formatEmptyValue(param.get("chxmidSaveOrUpdate"));
        DchyXmglChxmDto jsdwFbwtModelDelete = (DchyXmglChxmDto) param.get("chxmidDelete");

        if (StringUtils.isNotBlank(chxmidSaveOrUpdate)) {
            DchyXmglChxmDto jsdwFbwtModelSaveOrUpdate = getSingleData(chxmidSaveOrUpdate);
            map.put("saveOrUpdate", jsdwFbwtModelSaveOrUpdate);
        }

        if (null != jsdwFbwtModelDelete) {
            map.put("delete", jsdwFbwtModelDelete);
        }
        return map;
    }

}
