package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglHtService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/3/17 14:43
 * @description
 */
@Service
public class DchyXmglHtServiceImpl implements DchyXmglHtService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public DchyXmglHtxxDto generateHtxxDto(Map<String, Object> map) {
        DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();

        String chxmid = (String) map.get("chxmid");
        String htxxid = (String) map.get("htxxid");

        /*初始化合同信息表*/
        DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
        if(null != xmglHtxx){
            xmglHtxx.setChxmid(chxmid);
            xmglHtxx.setBazt(Constants.WTZT_YBA);//备案状态
            xmglHtxx.setBasj(new Date());//备案时间
            xmglHtxx.setQysj(null);//签约时间
            xmglHtxx.setWjzxid((String) map.get("folderId"));
            xmglHtxx.setHtlx("0");//合同类型
            xmglHtxx.setHtbmid("");//合同模版id
        }
        int result = entityMapper.saveOrUpdate(xmglHtxx, xmglHtxx.getHtxxid());
        if(result > 0){
            List<DchyXmglHtxx> htxxList = new ArrayList<>();
            htxxList.add(xmglHtxx);
            dchyXmglHtxxDto.setDchyXmglHtxxList(htxxList);
        }

        Example clsxExample = new Example(DchyXmglChxmClsx.class);
        clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);

         /*初始化合同和测量事项表*/
        /*通过chxmid获取到测量事项*/
        Example clsxHtxxExample = new Example(DchyXmglClsxHtxxGx.class);
        clsxHtxxExample.createCriteria().andEqualTo("chxmid",chxmid);
        List<DchyXmglClsxHtxxGx> clsxHtxxGxList = entityMapper.selectByExample(clsxHtxxExample);
        if(CollectionUtils.isEmpty(clsxHtxxGxList)){//没有则进行初始化
            /*组织测量事项与测绘项目关系*/
            if (CollectionUtils.isNotEmpty(clsxList)) {
                List<DchyXmglClsxHtxxGx> htxxGxList = new ArrayList<>();
                for (DchyXmglChxmClsx chxmClsx : clsxList) {
                    String clsxid = chxmClsx.getClsxid();
                    DchyXmglClsxHtxxGx htxxGx = new DchyXmglClsxHtxxGx();
                    htxxGx.setGxid(UUIDGenerator.generate18());
                    htxxGx.setHtxxid(htxxid);
                    htxxGx.setClsxid(clsxid);
                    htxxGx.setChxmid(chxmid);
                    int result3 = entityMapper.saveOrUpdate(htxxGx, htxxGx.getGxid());
                    if(result3 > 0){
                        htxxGxList.add(htxxGx);
                    }
                }
                dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(htxxGxList);
            }
        }
        else {
            dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(clsxHtxxGxList);
        }


        /*获取chdwxxid通过chxmid*/
        String chdwxxid = "";
        Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
        chdwExample.createCriteria().andEqualTo("chxmid",chxmid);
        List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExample(chdwExample);
        if(CollectionUtils.isNotEmpty(chdwxxList)){
            chdwxxid = chdwxxList.get(0).getChdwxxid();
        }

        //*初始化合同和测绘单位表*//
        DchyXmglHtxxChdwxxGx chdwxxGx = new DchyXmglHtxxChdwxxGx();
        chdwxxGx.setGxid(UUIDGenerator.generate18());
        chdwxxGx.setChdwxxid(chdwxxid);
        chdwxxGx.setHtxxid(htxxid);
        chdwxxGx.setChxmid(chxmid);
        int result2 = entityMapper.saveOrUpdate(chdwxxGx, chdwxxGx.getGxid());
        if(result2 > 0){
            List<DchyXmglHtxxChdwxxGx> chdwxxGxList = new ArrayList<>();
            chdwxxGxList.add(chdwxxGx);
            dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(chdwxxGxList);
        }

        /*测量事项与测绘单位关系*/
        Example clsxChdwGxExample = new Example(DchyXmglClsxChdwxxGx.class);
        clsxChdwGxExample.createCriteria().andEqualTo("chxmid",chxmid);
        List<DchyXmglClsxChdwxxGx> gxList = entityMapper.selectByExample(clsxChdwGxExample);
        if(CollectionUtils.isNotEmpty(gxList)){
            dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(gxList);
        }
        else {
            if(CollectionUtils.isNotEmpty(clsxList)){
                List<DchyXmglClsxChdwxxGx> chdwxxGxList = new ArrayList<>();
                for (DchyXmglChxmClsx xmglChxmClsx : clsxList) {
                    String clsxid = xmglChxmClsx.getClsxid();
                    DchyXmglClsxChdwxxGx clsxChdwxxGx = new DchyXmglClsxChdwxxGx();
                    clsxChdwxxGx.setGxid(UUIDGenerator.generate18());
                    clsxChdwxxGx.setChxmid(chxmid);
                    clsxChdwxxGx.setClsxid(clsxid);
                    clsxChdwxxGx.setChdwxxid(chdwxxid);
                    int result4 = entityMapper.saveOrUpdate(clsxChdwxxGx,clsxChdwxxGx.getGxid());
                    if(result4 > 0){
                        chdwxxGxList.add(clsxChdwxxGx);
                    }
                }
                dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(chdwxxGxList);
            }
        }
        return dchyXmglHtxxDto;
    }
}
