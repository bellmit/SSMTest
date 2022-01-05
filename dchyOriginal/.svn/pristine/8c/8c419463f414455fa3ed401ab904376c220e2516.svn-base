package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.MlkxxService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/11/29
 * @description 这个是从业人员信息的
 */
@Service
public class MlkxxServiceImpl implements MlkxxService {

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;
    @Autowired
    private DchyXmglMlkMapper mlkMapper;

    @Override
    public DchyXmglMlkDto getAllData(Map<String, String> param) {
        String glxxid = "";
        String sqxxid = CommonUtil.formatEmptyValue(param.get("sqxxid"));
        String mlkid = CommonUtil.formatEmptyValue(param.get("mlkid"));

        if (StringUtils.isNotBlank(sqxxid)) {
            glxxid = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid).getGlsxid();
        }
        mlkid = StringUtils.isNotBlank(mlkid) ? mlkid : glxxid;

        DchyXmglMlkDto mlkxxModelSaveOrUpdate = new DchyXmglMlkDto();
        DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
        dchyXmglMlk.setWjzxid(Constants.WJZXID_FLAG);
        dchyXmglMlk.setSfyx(Constants.VALID);
        dchyXmglMlk.setMlktp(null);
        dchyXmglMlk.setSfdj(Constants.DCHY_XMGL_MLK_DJ);//0:不冻结
        dchyXmglMlk.setWjzxid(Constants.WJZXID_FLAG);

        //名录库数据
        List<DchyXmglMlk> dchyXmglMlkList = new ArrayList<>();
        dchyXmglMlkList.add(dchyXmglMlk);
        mlkxxModelSaveOrUpdate.setDchyXmglMlkList(dchyXmglMlkList);

        //从业人员数据
        Example exampleCyry = new Example(DchyXmglCyry.class);
        exampleCyry.createCriteria().andEqualTo("mlkid", mlkid);
        List<DchyXmglCyry> dchyXmglCyryList = entityMapper.selectByExample(exampleCyry);

        mlkxxModelSaveOrUpdate.setDchyXmglCyryList(dchyXmglCyryList);

        //收件信息数据
        Example exampleSjxx = new Example(DchyXmglSjxx.class);
        exampleSjxx.createCriteria().andEqualTo("glsxid", mlkid);
        List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);

        mlkxxModelSaveOrUpdate.setDchyXmglSjxxList(dchyXmglSjxxList);

        //收件材料数据
        if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
            for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {

                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjClList = entityMapper.selectByExample(exampleSjcl);

                mlkxxModelSaveOrUpdate.setDchyXmglSjclList(dchyXmglSjClList);
            }

            //名录库和测量事项关系表
            Example exampleMlkClsxGx = new Example(DchyXmglMlkClsxGx.class);
            exampleMlkClsxGx.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxList = entityMapper.selectByExample(exampleMlkClsxGx);
            mlkxxModelSaveOrUpdate.setDchyXmglMlkClsxGxList(dchyXmglMlkClsxGxList);
        }
        return mlkxxModelSaveOrUpdate;
    }

    @Override
    public DchyXmglMlkDto getSingleData(String mlkid) {
        DchyXmglMlkDto mlkxxModelSaveOrUpdate = new DchyXmglMlkDto();
        if (StringUtils.isNotBlank(mlkid)) {
            List<DchyXmglMlk> mlkList = new ArrayList<>();
            DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != mlk) {
                mlk.setMlktp(null);
                mlkList.add(mlk);
            }
            mlkxxModelSaveOrUpdate.setDchyXmglMlkList(mlkList);
        }
        return mlkxxModelSaveOrUpdate;
    }
}
