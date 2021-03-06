package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.JsdwFbxfwService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/11/28
 * @description 线上数据同步线下数据
 */
@Service
public class JsdwFbxfwServiceImpl implements JsdwFbxfwService {

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;


    @Override
    public DchyXmglChxmDto getSingleData(String chxmid) {

        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        //测绘项目数据
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        if (dchyXmglChxm != null) {
            dchyXmglChxm.setWjzxid(Constants.WJZXID_FLAG);
            dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
            //测绘工程数据
            if (StringUtils.isNotBlank(dchyXmglChxm.getChgcid())) {
                Example exampleChgc = new Example(DchyXmglChgc.class);
                exampleChgc.createCriteria().andEqualTo("chgcid", dchyXmglChxm.getChgcid());
                List<DchyXmglChgc> dchyXmglChgcList = entityMapper.selectByExample(exampleChgc);
                if (CollectionUtils.isNotEmpty(dchyXmglChgcList)) {
                    dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgcList.get(0));
                }
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
            }
        }


        //收件信息数据
        Example exampleSjxx = new Example(DchyXmglSjxx.class);
        exampleSjxx.createCriteria().andEqualTo("glsxid", chxmid);
        List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
        if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
            dchyXmglChxmDto.setDchyXmglSjxxList(dchyXmglSjxxList);
            //收件材料数据
            for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                Example exampleSjcl = new Example(DchyXmglSjxx.class);
                exampleSjcl.createCriteria().andEqualTo("glsxid", chxmid).andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjClList = entityMapper.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjClList)) {
                    dchyXmglChxmDto.setDchyXmglSjclList(dchyXmglSjClList);
                }
            }
        }
        return dchyXmglChxmDto;
    }


}
