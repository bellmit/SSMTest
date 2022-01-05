package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglHtxxMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglHtxxService;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class DchyXmglHtxxServiceImpl implements DchyXmglHtxxService, UploadService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private DchyXmglHtxxMapper dchyXmglHtxxMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(DchyXmglHtxxServiceImpl.class);

    @Override
    public Page<Map<String, Object>> queryMbxxByPage(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String mblx = CommonUtil.formatEmptyValue(data.get("mblx"));
        String mbmc = CommonUtil.formatEmptyValue(data.get("mbmc"));
        int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
        int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("mblx", mblx);
        paramMap.put("mbmc", mbmc);
        Page<Map<String, Object>> mbxxList = repository.selectPaging("queryMbxxByMbmcOrMblxByPage", paramMap, page - 1, pageSize);
        if (CollectionUtils.isNotEmpty(mbxxList.getContent())) {
            for (Map<String, Object> mapTemp : mbxxList.getContent()) {
                if (StringUtils.isNotEmpty(CommonUtil.ternaryOperator(mapTemp.get("MBQYZT")))) {
                    String mbqyzt = CommonUtil.ternaryOperator(mapTemp.get("MBQYZT"));
                    if (StringUtils.equals(mbqyzt, "1")) {
                        mapTemp.put("MBQYZT", "使用中");
                    } else if (StringUtils.equals(mbqyzt, "0")) {
                        mapTemp.put("MBQYZT", "未启用");
                    }
                }
            }
        }
        DataSecurityUtil.decryptMapList(mbxxList.getContent());
        return mbxxList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseMessage saveMbxx(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        String mbid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "mbid"));
        String mblx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "mblx"));
        String mbmc = CommonUtil.formatEmptyValue(MapUtils.getString(map, "mbmc"));
        String mbqyzt = CommonUtil.formatEmptyValue(MapUtils.getString(map, "mbqyzt"));
        String bz = CommonUtil.formatEmptyValue(MapUtils.getString(map, "bz"));

        if (StringUtils.isNotBlank(mbid)) {
            DchyXmglMb dchyXmglMb = entityMapper.selectByPrimaryKey(DchyXmglMb.class, mbid);
            dchyXmglMb.setMbmc(mbmc);
            dchyXmglMb.setMblx(mblx);
            dchyXmglMb.setMbqyzt(mbqyzt);
            dchyXmglMb.setBz(bz);
            dchyXmglMb.setScr(UserUtil.getCurrentUserId());
            dchyXmglMb.setScsj(CalendarUtil.getCurHMSDate());

            //如果没有上传新模板则新增模板没有意义
            if (StringUtils.isNotBlank(dchyXmglMb.getWjzxid())) {
                entityMapper.saveOrUpdate(dchyXmglMb, dchyXmglMb.getMbid());

                //新增模板默认启用状态，添加成功将其同类型其他模板改为禁用
                Example mbxxExample = new Example(DchyXmglZd.class);
                mbxxExample.createCriteria().andEqualTo("zdlx", "MBLX").andEqualTo("dm", mblx);
                List<DchyXmglZd> dchyXmglZd = entityMapper.selectByExampleNotNull(mbxxExample);
                if (CollectionUtils.isNotEmpty(dchyXmglZd)) {
                    for (DchyXmglZd dchyXmglZds : dchyXmglZd) {
                        if (StringUtils.isNotBlank(dchyXmglZds.getQtsx())) {
                            String qtsx = dchyXmglZds.getQtsx();
                            if (StringUtils.equals(qtsx, "1")) {
                                Map<String, Object> mblxMap = new HashMap<>();
                                mblxMap.put("mbid", mbid);
                                mblxMap.put("mblx", mblx);
                                mblxMap.put("mbqyzt", "0");
                                dchyXmglHtxxMapper.updateTmbztByMbid(mblxMap);
                            }
                        }
                    }
                }
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.TEMPLATE_NOT_UPLOAD.getMsg(), ResponseMessage.CODE.TEMPLATE_NOT_UPLOAD.getCode());
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }


        return message;
    }

    @Override
    public Map<String, Object> initMbgl() {
        DchyXmglMb dchyXmglMb = new DchyXmglMb();
        dchyXmglMb.setMbid(UUIDGenerator.generate());
        dchyXmglMb.setMbqyzt("1");
        entityMapper.insertSelective(dchyXmglMb);
        Map<String, Object> result = Maps.newHashMap();
        result.put("mbid", dchyXmglMb.getMbid());
        return result;
    }

    @Override
    @Transactional
    public synchronized Map<String, Object> deleteMbglByMbid(Map<String, Object> paramMap) {
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (null != paramMap && paramMap.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(paramMap, "data");
            String mbid = MapUtils.getString(data, "mbid");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(mbid)) {
                DchyXmglMb dchyXmglMb = entityMapper.selectByPrimaryKey(DchyXmglMb.class, mbid);
                if (null != dchyXmglMb) {
                    if (org.apache.commons.lang3.StringUtils.isNoneBlank(dchyXmglMb.getMbid())) {
                        Example dchyXmglMbExample = new Example(DchyXmglMb.class);
                        dchyXmglMbExample.createCriteria().andEqualTo("mbid", dchyXmglMb.getMbid());
                        entityMapper.deleteByPrimaryKey(DchyXmglMb.class, dchyXmglMb.getMbid());
                    }

                    // 查询收件信息
                    Example dchyXmglSjxxExample = new Example(DchyXmglSjxx.class);
                    dchyXmglSjxxExample.createCriteria().andEqualTo("glsxid", mbid);
                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(dchyXmglSjxxExample);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                        Example dchyXmglSjclExample = new Example(DchyXmglSjcl.class);
                        for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                            // 删除收件材料
                            dchyXmglSjclExample.clear();
                            dchyXmglSjclExample.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                            entityMapper.deleteByExampleNotNull(dchyXmglSjclExample);
                            // 删除收件信息,删除filecenter
                            entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjxx.getSjxxid());
                        }
                    }
                    /*fileCenter数据删除*/
                    int node = platformUtil.creatNode(mbid);
                    platformUtil.deleteNodeById(node);
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                } else {
                    code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                    msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
                }
            } else {
                code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
            }
        } else {
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    public boolean saveMbqyzt(Map<String, Object> map) {
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String mbid = CommonUtil.formatEmptyValue(data.get("mbid"));
            String mbqyzt = CommonUtil.formatEmptyValue(data.get("mbqyzt"));
            String mblx = CommonUtil.formatEmptyValue(data.get("mblx"));
            Map<String, Object> mblxMap = new HashMap<>();
            mblxMap.put("mbid", mbid);
            mblxMap.put("mbqyzt", mbqyzt);
            mblxMap.put("mblx", mblx);

            //新增模板默认启用状态，添加成功将其同类型其他模板改为禁用
            Example mbxxExample = new Example(DchyXmglZd.class);
            mbxxExample.createCriteria().andEqualTo("zdlx", "MBLX").andEqualTo("dm", mblx);
            List<DchyXmglZd> dchyXmglZd = entityMapper.selectByExampleNotNull(mbxxExample);

            if (CollectionUtils.isNotEmpty(dchyXmglZd)) {
                for (DchyXmglZd dchyXmglZds : dchyXmglZd) {
                    String qtsx = dchyXmglZds.getQtsx();
                    //同类型唯一启动项 1
                    if (StringUtils.equals(qtsx, "1")) {
                        if (StringUtils.equals(mbqyzt, "0")) {
                            dchyXmglHtxxMapper.updateMbztByMbid(mblxMap);
                        } else if (StringUtils.equals(mbqyzt, "1")) {
                            dchyXmglHtxxMapper.updateMbztByMbid(mblxMap);
                            mblxMap.clear();
                            mblxMap.put("mbid", mbid);
                            mblxMap.put("mblx", mblx);
                            mblxMap.put("mbqyzt", "0");
                            dchyXmglHtxxMapper.updateTmbztByMbid(mblxMap);
                        }
                    } else {
                        dchyXmglHtxxMapper.updateMbztByMbid(mblxMap);
                    }
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, glsxid);
            Optional<DchyXmglHtxx> htxxOptional = Optional.ofNullable(xmglHtxx);
            if(htxxOptional.isPresent()){
                DchyXmglHtxx htxx = htxxOptional.get();
                htxx.setWjzxid(wjzxid);
                int i = entityMapper.saveOrUpdate(htxx,htxx.getHtxxid());
                if (i > 0) {
                    updated = true;
                }
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return SsmkidEnum.ZXBA.getCode();
    }

}
