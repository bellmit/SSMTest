package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglChxmClsxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DchyXmglChxmClsxServiceImpl implements DchyXmglChxmClsxService {

    @Autowired
    DchyXmglZdService dchyXmglZdService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper;
    @Autowired
    PushDataToMqService pushDataToMqService;

    /**
     * 测绘期限超期提醒
     *
     * @return
     */
    @Override
    public boolean queryCqtx() {
        //获取当前时间的前一天
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yjjfrq = dateFormat.format(ca.getTime());
        //获取测绘期限超期的测绘项目
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("yjjfrq", yjjfrq);
        List<Map<String, Object>> chxmclsxList = dchyXmglChxmClsxMapper.queryClsxByChqx(paramMap);
        if (CollectionUtils.isNotEmpty(chxmclsxList)) {
            List<DchyXmglYhxx> dchyXmglYhxxList = Lists.newArrayList();
            for (Map<String, Object> chxmclsx : chxmclsxList) {
                String chxmid = MapUtils.getString(chxmclsx, "CHXMID");
                String wtdw = MapUtils.getString(chxmclsx, "WTDW");
                String clsx = MapUtils.getString(chxmclsx, "CLSX");
                String gcmc = MapUtils.getString(chxmclsx, "GCMC");
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", clsx);
                String xxnr = null;
                String yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_CHQXCQ;
                if (StringUtils.isNotEmpty(yhxxpzid)) {
                    DchyXmglYhxxPz dchyXmglYhxxPz = entityMapper.selectByPrimaryKey(DchyXmglYhxxPz.class, yhxxpzid);
                    if (dchyXmglYhxxPz != null && dchyXmglZd != null) {
                        xxnr = dchyXmglYhxxPz.getXxnr().replaceAll("项目名称", gcmc + "的测绘事项：" + dchyXmglZd.getMc());
                    }
                }

                if (StringUtils.isNotEmpty(wtdw)) {
                    //建设单位组织消息数据
                    DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();
                    dchyXmglYhxx.setYhxxid(UUIDGenerator.generate18());
                    dchyXmglYhxx.setXxnr(xxnr);//测绘期限超期
                    dchyXmglYhxx.setFsyhid(UserUtil.getCurrentUserId());
                    dchyXmglYhxx.setJsyhid(wtdw);
                    dchyXmglYhxx.setFssj(new Date());
                    dchyXmglYhxx.setDqzt(Constants.DCHY_XMGL_XXWD);
                    dchyXmglYhxx.setSftz(Constants.XXTX_SFTZ_BTZ);
                    dchyXmglYhxx.setGlsxid(chxmid);
                    dchyXmglYhxxList.add(dchyXmglYhxx);
                }

                if (StringUtils.isNotEmpty(chxmid)) {
                    //测绘单位组织消息数据
                    Example exampleChdwxx = new Example(DchyXmglChxmChdwxx.class);
                    exampleChdwxx.createCriteria().andEqualTo("chxmid", chxmid);
                    List<DchyXmglChxmChdwxx> chdwxxLitst = entityMapper.selectByExample(exampleChdwxx);
                    if (CollectionUtils.isNotEmpty(chdwxxLitst)) {
                        //当前一个项目对应一个测绘单位
                        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = chdwxxLitst.get(0);
                        if (StringUtils.isNotEmpty(dchyXmglChxmChdwxx.getMlkid())) {
                            //组织消息数据
                            DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();
                            dchyXmglYhxx.setYhxxid(UUIDGenerator.generate18());
                            dchyXmglYhxx.setXxnr(xxnr);//测绘期限超期
                            dchyXmglYhxx.setFsyhid(UserUtil.getCurrentUserId());
                            dchyXmglYhxx.setJsyhid(dchyXmglChxmChdwxx.getMlkid());
                            dchyXmglYhxx.setFssj(new Date());
                            dchyXmglYhxx.setDqzt(Constants.DCHY_XMGL_XXWD);
                            dchyXmglYhxx.setSftz(Constants.XXTX_SFTZ_BTZ);
                            dchyXmglYhxx.setGlsxid(chxmid);
                            dchyXmglYhxxList.add(dchyXmglYhxx);
                        }
                    }
                }
            }
            //entityMapper.batchSaveSelective(dchyXmglYhxxList);
            //推送消息到线上库
            DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
            dchyXmglXxtxDto.setDchyXmglYhxxList(dchyXmglYhxxList);
            pushDataToMqService.pushXxtxResultTo(dchyXmglXxtxDto);
        }

        return false;
    }
}
