package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.core.service.UploadService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 16:37
 * @description
 */
@Service
public class DchyXmglMlkServiceImpl implements DchyXmglMlkService, UploadService {

    @Autowired
    private DchyXmglMlkMapper mlkMapper;
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private FileUploadService fileUploadService;


    @Override
    public List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid) {
        return mlkMapper.queryUploadFileBySsmkId(ssmkid);
    }

    @Override
    public List<Map<String, Object>> getSjclXx(String glsxid, String ssmkId) {
        return mlkMapper.getSjclXx(glsxid, ssmkId);
    }

    @Override
    public List<Map<String, Object>> getHtxx(String mlkid) {
        return mlkMapper.getHtxx(mlkid);
    }


    @Override
    public List<String> getHtxxIdByChxmid2(String chxmid) {
        List<String> htxxidList = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid)) {
            /*根据chxmid获取对应htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx xmglHtxx : htxxList) {
                    htxxidList.add(xmglHtxx.getHtxxid());
                }
            }
        }
        return htxxidList;
    }

    @Override
    public List<Map<String, Object>> getSjclXx2(String mlkid, String ssmkid) {
        return mlkMapper.getSjclXx2(mlkid, ssmkid);
    }

    /**
     * 文件上传后记录保存在收件信息和材料信息中
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            /*收件信息*/
            String sjxxId = UUIDGenerator.generate18();
            Date sjsj = new Date();
            String sjr = UserUtil.getCurrentUserId();
            String tjr = "";
            DchyXmglSjxx sjxx = new DchyXmglSjxx();
            sjxx.setSjxxid(sjxxId);
            sjxx.setGlsxid(glsxId);
            sjxx.setSsmkid(ssmkId);
            sjxx.setSjsj(sjsj);
            sjxx.setTjr(tjr);
            sjxx.setSjr(sjr);
            entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());

            for (Map<String, Object> map : mapList) {
                String sjclid = UUIDGenerator.generate18();
                map.put("SJCLID", sjclid);
                map.put("SJXXID", sjxxId);
                int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                String mlmc = (String) map.get("CLMC");
                DchyXmglSjcl sjcl = new DchyXmglSjcl();
                sjcl.setSjclid(sjclid);
                sjcl.setSjxxid(sjxxId);
                sjcl.setClmc(mlmc);
                sjcl.setCllx(CommonUtil.formatEmptyValue(map.get("CLLX")));
                sjcl.setFs(fs);
                sjcl.setClrq(new Date());
                sjcl.setYs(1);
                entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    @Override
    @Transactional
    public ResponseMessage delFile(String sjclId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        int i = 0;
        try {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));

            /*根据sjclid将对应wjzxid置空*/
            DchyXmglSjcl xmglSjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclId);
            if (null != xmglSjcl) {
                xmglSjcl.setWjzxid("");
                i = entityMapper.saveOrUpdate(xmglSjcl, xmglSjcl.getSjclid());
            }
            if (i > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", wjzxId);
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @Override
    @Transactional
    public ResponseMessage delFileHtxx(String htxxId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        int i = 0;
        try {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
            /*根据htxxid将对应wjzxid置空*/
            DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxId);
            if (null != xmglHtxx) {
                xmglHtxx.setWjzxid("");
                i = entityMapper.saveOrUpdate(xmglHtxx, xmglHtxx.getHtxxid());
            }
            if (i > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", wjzxId);
                Map<String, Object> idMaps = Maps.newHashMap();
                idMaps.put("htxxid", htxxId);

                DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
                dchyXmglChxmDto.setCzlx(Constants.DCHY_XMGL_SJTS_CZLX_DEL);
                dchyXmglChxmDto.setIdMaps(idMaps);
                pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglMlk dchyXmglMlk = new DchyXmglMlk();
            dchyXmglMlk.setMlkid(glsxid);
            dchyXmglMlk.setWjzxid(wjzxid);
            int i = entityMapper.updateByPrimaryKeySelective(dchyXmglMlk);
            if (i > 0) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return SsmkidEnum.MLKRZ.getCode();
    }


    @Override
    public DchyXmglChxmClsx getChxmClsxByXmidAndClsx(String chxmid, String clsx) {
        return mlkMapper.getChxmClsxByXmidAndClsx(chxmid, clsx);
    }

    @Override
    public String getHtxxIdByClsxId(String clsxid) {
        return mlkMapper.getHtxxIdByClsxId(clsxid);
    }

    @Override
    public int getClsxCountByHtid(String htxxid) {
        return mlkMapper.getClsxCountByHtid(htxxid);
    }

    @Override
    public String getClsxidByChxmidAndClsx(String chxmid, String clsx) {
        return mlkMapper.getClsxidByChxmidAndClsx(chxmid, clsx);
    }

    @Override
    public DchyXmglClsxHtxxGx getClsxHtGxByChxmidAndClsxid(String chxmid, String clsxid) {
        return mlkMapper.getClsxHtGxByChxmidAndClsxid(chxmid, clsxid);
    }
}
