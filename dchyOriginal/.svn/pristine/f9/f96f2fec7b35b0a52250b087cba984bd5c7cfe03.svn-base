package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private EntityMapper entityMapper;

//    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglChxmDto.class, czmkCode = ProLog.CZMK_XZSLDJ_CODE, czmkMc = ProLog.CZMK_XZSLDJ_MC)
//    public void logTest(DchyXmglChxmDto dchyXMglChxmDto) {
//
//    }

    public DchyXmglChxmDto dchyXmglChxmDto() {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();

        dchyXmglChxmDto.setGlsxid("3");

        DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, "ss");
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, "3");

        Example example1 = new Example(DchyXmglChxmChdwxx.class);
        example1.createCriteria().andEqualTo("chxmid", "3");
        List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapper.selectByExampleNotNull(example1);

        Example example2 = new Example(DchyXmglClsxChdwxxGx.class);


        Example example3 = new Example(DchyXmglChxmClsx.class);
        example3.createCriteria().andEqualTo("chxmid", "3");
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(example3);


        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = Lists.newArrayList();
        List sjclList = Lists.newArrayList();
        sjclList.add("88");
        sjclList.add("99");
        sjclList.add("100");

        List htxxidList = Lists.newArrayList();
        htxxidList.add("f");
        htxxidList.add("g");
        htxxidList.add("h");

        List cwgList = Lists.newArrayList();
        cwgList.add("9");
        cwgList.add("8");
        cwgList.add("7");

        List chgList = Lists.newArrayList();
        chgList.add("11");
        chgList.add("12");
        chgList.add("13");

        List hwgList = Lists.newArrayList();
        hwgList.add("978");
        hwgList.add("456");
        hwgList.add("321");
        for (int i = 0; i < 3; i++) {
            DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();

            DchyXmglHtxx dchyXmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxidList.get(i));
            DchyXmglSjcl dchyXmglSjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclList.get(i));

            Example example4 = new Example(DchyXmglClsxChdwxxGx.class);
            example4.createCriteria().andIn("gxid", cwgList);
            List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList = entityMapper.selectByExampleNotNull(example4);

            Example example5 = new Example(DchyXmglHtxxChdwxxGx.class);
            example5.createCriteria().andIn("gxid", hwgList);
            List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList = entityMapper.selectByExampleNotNull(example5);

            Example example6 = new Example(DchyXmglClsxHtxxGx.class);
            example6.createCriteria().andIn("gxid", chgList);
            List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(example6);

            dchyXmglHtxxDto.setDchyXmglHtxx(dchyXmglHtxx);
            dchyXmglHtxxDto.setDchyXmglSjcl(dchyXmglSjcl);
            dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(dchyXmglClsxChdwxxGxList);
            dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(dchyXmglClsxHtxxGxList);
            dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(dchyXmglHtxxChdwxxGxList);

            dchyXmglHtxxDtoList.add(dchyXmglHtxxDto);

        }

        Example example5 = new Example(DchyXmglSjcl.class);
        example5.createCriteria().andEqualTo("sjxxid", "44");
        List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExampleNotNull(example3);

        dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgc);
        dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
        dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);
        dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);
        dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);
        dchyXmglChxmDto.setDchyXmglSjclList(dchyXmglSjclList);
        return dchyXmglChxmDto;
    }

}
