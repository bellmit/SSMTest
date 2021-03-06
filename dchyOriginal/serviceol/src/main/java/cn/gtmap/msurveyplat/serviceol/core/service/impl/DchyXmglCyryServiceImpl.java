package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglCyryService;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/7 14:44
 * @description 从业人员
 */
@Service
public class DchyXmglCyryServiceImpl implements DchyXmglCyryService, UploadService {


    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private PlatformUtil platformUtil;

    protected final Log logger = LogFactory.getLog(getClass());


    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglCyry dchyXmglCyry = new DchyXmglCyry();
            dchyXmglCyry.setCyryid(glsxid);
            dchyXmglCyry.setWjzxid(wjzxid);
            int i = entityMapper.updateByPrimaryKeySelective(dchyXmglCyry);
            if (i > 0) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return SsmkidEnum.CYRY.getCode();
    }

    @Override
    public DchyXmglCyry getCyryXxById(String cyryId) {
        DchyXmglCyry dchyXmglCyry = entityMapper.selectByPrimaryKey(DchyXmglCyry.class, cyryId);
        return null != dchyXmglCyry ? dchyXmglCyry : null;
    }

    @Override
    public DchyXmglCyry initCyry(String mlkId) {
        DchyXmglCyry xmglCyry = new DchyXmglCyry();
        xmglCyry.setCyryid(UUIDGenerator.generate18());
        xmglCyry.setMlkid(mlkId);
        int result = entityMapper.saveOrUpdate(xmglCyry, xmglCyry.getCyryid());
        if (result > 0) {
            return xmglCyry;
        }
        return null;
    }

    @Override
    public DchyXmglMlkDto generateMlkDto4Cyry(Map<String, Object> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
        //获取名录库id
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        String ryxm = CommonUtil.ternaryOperator(data.get("ryxm"));
        String zc = CommonUtil.ternaryOperator(data.get("zc"));
        String zsmc = CommonUtil.ternaryOperator(data.get("zsmc"));
        String zsbh = CommonUtil.ternaryOperator(data.get("zsbh"));
        String cqyx = CommonUtil.ternaryOperator(data.get("cqyx"));
        //证书有效开始日期
        Date zsyxksrq = null;
        //证书有效结束日期
        Date zsyxjsrq = null;
        try {
            zsyxksrq = sdf.parse(CommonUtil.ternaryOperator(data.get("zsyxksrq")));
            /*结束时间不存在*/
            String tempjs = CommonUtil.ternaryOperator(data.get("zsyxjsrq"));
            if ("".equals(tempjs) || null == tempjs) {
                zsyxjsrq = null;
            } else {
                zsyxjsrq = sdf.parse(tempjs);
            }
        } catch (ParseException e) {
            logger.error("错误原因:{}", e);
        }
        DchyXmglCyry xmglCyry = new DchyXmglCyry();
        xmglCyry.setCyryid(cyryId);
        xmglCyry.setRyxm(ryxm);
        xmglCyry.setZc(zc);
        xmglCyry.setZsmc(zsmc);
        xmglCyry.setZsbh(zsbh);
        xmglCyry.setZsyxksrq(zsyxksrq);
        xmglCyry.setZsyxjsrq(zsyxjsrq);
        xmglCyry.setCqyx((Constants.LONG_TERM.equals(cqyx) ? "是" : "否"));
        xmglCyry.setMlkid(mlkId);//名录库id

        DchyXmglMlkDto xmglMlkDto = new DchyXmglMlkDto();
        List<DchyXmglCyry> cyryList = new ArrayList<>();
        cyryList.add(xmglCyry);
        xmglMlkDto.setGlsxid(xmglCyry.getMlkid());
        xmglMlkDto.setDchyXmglCyryList(cyryList);
        return xmglMlkDto;
    }

    @Override
    public DchyXmglMlk isMlkExist(String mlkid) {
        if(StringUtils.isNotBlank(mlkid)){
            return entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglMlkDto.class, czmkCode = ProLog.CZMC_CYRY_ALTER_CODE, czmkMc = ProLog.CZMC_CYRY_ALTER_MC)
    public DchyXmglCyry saveCyry(DchyXmglMlkDto mlkDto, Map<String, Object> data) {
        List<DchyXmglCyry> cyryList = mlkDto.getDchyXmglCyryList();
        if (CollectionUtils.isNotEmpty(cyryList)) {
            for (DchyXmglCyry xmglCyry : cyryList) {
                int result = entityMapper.saveOrUpdate(xmglCyry, xmglCyry.getCyryid());
                if (result > 0) {
                    /*若有文件修改，则更新文件对应信息*/
                    List<Map<String, Object>> uploadList = (List<Map<String, Object>>) data.get("uploadList");
                    if (CollectionUtils.isNotEmpty(uploadList)) {
                        for (Map<String, Object> map : uploadList) {
                            String sjclid = CommonUtil.ternaryOperator(MapUtils.getString(map, "SJCLID"));
                            String fs = CommonUtil.ternaryOperator(MapUtils.getString(map, "FS"));
                            String ys = CommonUtil.ternaryOperator(MapUtils.getString(map, "YS"));
                            DchyXmglSjcl sjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
                            sjcl.setFs(Integer.parseInt(fs));
                            sjcl.setYs(Integer.parseInt(ys));
                            entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                        }
                    }
                    return xmglCyry;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public DchyXmglCyry saveCyryBeforeMlk(Map<String, Object> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
        //获取名录库id
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        String ryxm = CommonUtil.ternaryOperator(data.get("ryxm"));
        String zc = CommonUtil.ternaryOperator(data.get("zc"));
        String zsmc = CommonUtil.ternaryOperator(data.get("zsmc"));
        String zsbh = CommonUtil.ternaryOperator(data.get("zsbh"));
        String cqyx = CommonUtil.ternaryOperator(data.get("cqyx"));
        //证书有效开始日期
        Date zsyxksrq = null;
        //证书有效结束日期
        Date zsyxjsrq = null;
        try {
            zsyxksrq = sdf.parse(CommonUtil.ternaryOperator(data.get("zsyxksrq")));
            /*结束时间不存在*/
            String tempjs = CommonUtil.ternaryOperator(data.get("zsyxjsrq"));
            if ("".equals(tempjs) || null == tempjs) {
                zsyxjsrq = null;
            } else {
                zsyxjsrq = sdf.parse(tempjs);
            }
        } catch (ParseException e) {
            logger.error("错误原因:{}", e);
        }
        DchyXmglCyry xmglCyry = new DchyXmglCyry();
        xmglCyry.setCyryid(cyryId);
        xmglCyry.setRyxm(ryxm);
        xmglCyry.setZc(zc);
        xmglCyry.setZsmc(zsmc);
        xmglCyry.setZsbh(zsbh);
        xmglCyry.setZsyxksrq(zsyxksrq);
        xmglCyry.setZsyxjsrq(zsyxjsrq);
        xmglCyry.setCqyx((Constants.LONG_TERM.equals(cqyx) ? "是" : "否"));
        xmglCyry.setMlkid(mlkId);//名录库id
        int i = entityMapper.saveOrUpdate(xmglCyry, xmglCyry.getCyryid());
        if(i > 0){
            /*若有文件修改，则更新文件对应信息*/
            List<Map<String, Object>> uploadList = (List<Map<String, Object>>) data.get("uploadList");
            if (CollectionUtils.isNotEmpty(uploadList)) {
                for (Map<String, Object> map : uploadList) {
                    String sjclid = CommonUtil.ternaryOperator(MapUtils.getString(map, "SJCLID"));
                    String fs = CommonUtil.ternaryOperator(MapUtils.getString(map, "FS"));
                    String ys = CommonUtil.ternaryOperator(MapUtils.getString(map, "YS"));
                    DchyXmglSjcl sjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
                    sjcl.setFs(Integer.parseInt(fs));
                    sjcl.setYs(Integer.parseInt(ys));
                    entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                }
            }
            return xmglCyry;
        }
        return null;
    }

    @Override
    @Transactional
    public DchyXmglCyry saveCyryXx(Map<String, Object> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
        //获取名录库id
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        String ryxm = CommonUtil.ternaryOperator(data.get("ryxm"));
        String zc = CommonUtil.ternaryOperator(data.get("zc"));
        String zsmc = CommonUtil.ternaryOperator(data.get("zsmc"));
        String zsbh = CommonUtil.ternaryOperator(data.get("zsbh"));
        String cqyx = CommonUtil.ternaryOperator(data.get("cqyx"));
        //证书有效开始日期
        Date zsyxksrq = null;
        //证书有效结束日期
        Date zsyxjsrq = null;
        try {
            zsyxksrq = sdf.parse(CommonUtil.ternaryOperator(data.get("zsyxksrq")));
            /*结束时间不存在*/
            String tempjs = CommonUtil.ternaryOperator(data.get("zsyxjsrq"));
            if ("".equals(tempjs) || null == tempjs) {
                zsyxjsrq = null;
            } else {
                zsyxjsrq = sdf.parse(tempjs);
            }
        } catch (ParseException e) {
            logger.error("错误原因:{}", e);
        }
        DchyXmglCyry xmglCyry = new DchyXmglCyry();
        xmglCyry.setCyryid(cyryId);
        xmglCyry.setRyxm(ryxm);
        xmglCyry.setZc(zc);
        xmglCyry.setZsmc(zsmc);
        xmglCyry.setZsbh(zsbh);
        xmglCyry.setZsyxksrq(zsyxksrq);
        xmglCyry.setZsyxjsrq(zsyxjsrq);
        xmglCyry.setCqyx((Constants.LONG_TERM.equals(cqyx) ? "是" : "否"));
        xmglCyry.setMlkid(mlkId);//名录库id
        int result = entityMapper.saveOrUpdate(xmglCyry, xmglCyry.getCyryid());
        if (result > 0) {
            /*若有文件修改，则更新文件对应信息*/
            List<Map<String, Object>> uploadList = (List<Map<String, Object>>) data.get("uploadList");
            if (CollectionUtils.isNotEmpty(uploadList)) {
                for (Map<String, Object> map : uploadList) {
                    String sjclid = CommonUtil.ternaryOperator(MapUtils.getString(map, "SJCLID"));
                    String fs = CommonUtil.ternaryOperator(MapUtils.getString(map, "FS"));
                    String ys = CommonUtil.ternaryOperator(MapUtils.getString(map, "YS"));
                    DchyXmglSjcl sjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
                    sjcl.setFs(Integer.parseInt(fs));
                    sjcl.setYs(Integer.parseInt(ys));
                    entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                }
            }
            return xmglCyry;
        }
        return null;
    }

    @Override
    public DchyXmglMlkDto generateCyry4Del(Map<String, Object> data) {
        String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
        DchyXmglMlkDto xmglMlkDto = new DchyXmglMlkDto();
        DchyXmglCyry xmglCyry = entityMapper.selectByPrimaryKey(DchyXmglCyry.class, cyryId);
        if (null != xmglCyry) {
            List<DchyXmglCyry> cyryList = new ArrayList<>();
            xmglCyry.setCyryid("delete_" + xmglCyry.getCyryid());
            cyryList.add(xmglCyry);
            xmglMlkDto.setGlsxid(xmglCyry.getMlkid());
            xmglMlkDto.setDchyXmglCyryList(cyryList);
        }
        return xmglMlkDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglMlkDto.class, czmkCode = ProLog.CZMC_CYRY_ALTER_CODE, czmkMc = ProLog.CZMC_CYRY_ALTER_MC)
    public void delCyryById(DchyXmglMlkDto mlkDto, Map<String, Object> data) {
        String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
        DchyXmglCyry xmglCyry = entityMapper.selectByPrimaryKey(DchyXmglCyry.class, cyryId);
        if (null != xmglCyry) {
            /*删除从业人员对应信息*/
            entityMapper.deleteByPrimaryKey(DchyXmglCyry.class, cyryId);
            /*获取材料*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", cyryId);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                for (DchyXmglSjxx sjxx : sjxxList) {
                    if (StringUtils.equals(SsmkidEnum.CYRY.getCode(), sjxx.getSsmkid())) {
                        Example sjclExample = new Example(DchyXmglSjcl.class);
                        sjclExample.createCriteria().andEqualTo("sjxxid", sjxx.getSjxxid());
                        //删除材料
                        entityMapper.deleteByExample(sjclExample);
                        //删除收件信息
                        entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, sjxx.getSjxxid());
                    }
                }
            }
            /*fileCenter数据删除*/
            int node = platformUtil.creatNode(cyryId);
            platformUtil.deleteNodeById(node);
        }
    }
}
