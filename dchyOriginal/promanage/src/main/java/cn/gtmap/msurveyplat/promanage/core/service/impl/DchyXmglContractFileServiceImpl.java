package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglContractFileMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglContractFileService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.service.impl.DchyXmglChxmClsxServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/3/9 15:37
 * @description
 */
@Service
public class DchyXmglContractFileServiceImpl implements DchyXmglContractFileService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DchyXmglContractFileMapper xmglContractFileMapper;
    @Autowired
    private Repository repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper;

    private static final Log logger = LogFactory.getLog(DchyXmglContractFileServiceImpl.class);


    /**
     * ??????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryInformationToBeRecorded(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        String slbh = CommonUtil.ternaryOperator(data.get("slbh"));
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String wtdw = CommonUtil.ternaryOperator(data.get("wtdw"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        String slr = UserUtil.getCurrentUserId();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("slbh", slbh);
        paramMap.put("gcbh", gcbh);
        paramMap.put("gcmc", gcmc);
        paramMap.put("wtdw", wtdw);
        paramMap.put("chdwmc", chdwmc);
        paramMap.put("slr", slr);

        try {
            return repository.selectPaging("queryInformationToBeRecordedByPage", paramMap, page - 1, pageSize);
        } catch (Exception e) {
            logger.error("????????????{}", e);
        }
        return null;
    }

    /**
     * ????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryRecordList(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        String babh = CommonUtil.ternaryOperator(data.get("babh"));
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String wtdw = CommonUtil.ternaryOperator(data.get("wtdw"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        String xmzt = CommonUtil.ternaryOperator(data.get("xmzt"));

        List<String> xmztList = new ArrayList<>();//????????????

        List<DchyXmglZd> zdList = dchyXmglZdService.getDchyXmglZdListByZdlx(Constants.DCHY_XMGL_CHXM_XMZT);
        if (CollectionUtils.isNotEmpty(zdList)) {
            for (DchyXmglZd xmglZd : zdList) {
                /*??????????????????????????? 1: ?????????*/
                if (!StringUtils.equals(Constants.DCHY_XMGL_CHXM_XMZT_WSL, xmglZd.getDm())) {
                    xmztList.add(xmglZd.getDm());
                }
            }
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("babh", babh);
        paramMap.put("gcbh", gcbh);
        paramMap.put("gcmc", gcmc);
        paramMap.put("wtdw", wtdw);
        paramMap.put("chdwmc", chdwmc);
        paramMap.put("xmzt", xmzt);
        paramMap.put("xmztList", xmztList);

        try {
            Page<Map<String, Object>> resuliList = repository.selectPaging("queryRecordListByPage", paramMap, page - 1, pageSize);
            if (resuliList != null && resuliList.getContent() != null) {
                for (Map<String, Object> resultList : resuliList.getContent()) {
                    String chxmid = MapUtils.getString(resultList, "CHXMID");
                    if (StringUtils.isNotEmpty(chxmid)) {
                        //??????????????????????????????
                        Calendar ca = Calendar.getInstance();
                        ca.add(Calendar.DATE, -1);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String yjjfrq = dateFormat.format(ca.getTime());
                        //???????????????????????????????????????
                        Map<String, Object> paramMaps = Maps.newHashMap();
                        paramMaps.put("yjjfrq", yjjfrq);
                        paramMaps.put("chxmid", chxmid);
                        List<Map<String, Object>> chxmclsxList = dchyXmglChxmClsxMapper.queryClsxByChqx(paramMaps);
                        if (CollectionUtils.isNotEmpty(chxmclsxList)) {
                            resultList.put("SFCQ", "1");
                        } else {
                            resultList.put("SFCQ", "0");
                        }
                    }
                }
            }
            return resuliList;
        } catch (Exception e) {
            logger.error("????????????{}", e);
        }
        return null;
    }

    /**
     * ??????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    @Transactional
    public boolean saveBaxmSfgq(Map<String, Object> data) {
        String sfgq = CommonUtil.ternaryOperator(data.get("sfgq"));
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            if (StringUtils.isNotEmpty(chxmid) && StringUtils.isNotEmpty(sfgq)) {

                //??????????????????????????????
                DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
                dchyXmglChxm.setChxmid(chxmid);
                dchyXmglChxm.setSfgq(sfgq);
                int result1 = entityMapper.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());

                //???????????????????????????????????????
                DchyXmglChxmGqxx dchyXmglChxmGqxx = new DchyXmglChxmGqxx();
                dchyXmglChxmGqxx.setGqxxid(UUIDGenerator.generate18());
                dchyXmglChxmGqxx.setChxmid(chxmid);
                dchyXmglChxmGqxx.setGqzt(sfgq);
                if (StringUtils.equals(sfgq, Constants.DCHY_XMGL_BGQ)) {

                    //?????????????????????
                    Example exampleChxmGqxx = new Example(DchyXmglChxmGqxx.class);
                    exampleChxmGqxx.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("gqzt", Constants.DCHY_XMGL_GQ);
                    exampleChxmGqxx.setOrderByClause("gqsj desc");
                    List<DchyXmglChxmGqxx> dchyXmglChxmGqxxList = entityMapper.selectByExampleNotNull(exampleChxmGqxx);
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmGqxxList)) {
                        Date gqsj = dchyXmglChxmGqxxList.get(0).getGqsj();
                        Date jgsj = CalendarUtil.getCurDate();

                        //??????
                        dchyXmglChxmGqxx.setJgr(UserUtil.getCurrentUserId());
                        dchyXmglChxmGqxx.setJgsj(CalendarUtil.getCurDate());
                        dchyXmglChxmGqxx.setGqr(dchyXmglChxmGqxxList.get(0).getGqr());
                        dchyXmglChxmGqxx.setGqsj(dchyXmglChxmGqxxList.get(0).getGqsj());

                        //??????????????????????????????????????????
                        Example exampleclsx = new Example(DchyXmglChxmClsx.class);
                        exampleclsx.createCriteria().andEqualTo("chxmid", chxmid);
                        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(exampleclsx);
                        if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                            for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                                Date jcrq = dchyXmglChxmClsx.getJcrq();
                                String yjjfrq = dchyXmglChxmClsx.getYjjfrq();
                                if (jcrq != null && StringUtils.isNotEmpty(yjjfrq)) {
                                    int qxts = Integer.parseInt(yjjfrq) - DateUtil.daysBetween(jcrq, gqsj);
                                    int yjjfrqNew = Integer.parseInt(yjjfrq) + qxts;
                                    dchyXmglChxmClsx.setYjjfrq(Integer.toString(yjjfrqNew));
                                    entityMapper.saveOrUpdate(dchyXmglChxmClsx, dchyXmglChxmClsx.getClsxid());
                                }
                            }
                        }
                    }
                } else if (StringUtils.equals(sfgq, Constants.DCHY_XMGL_GQ)) {
                    //??????
                    dchyXmglChxmGqxx.setGqr(UserUtil.getCurrentUserId());
                    dchyXmglChxmGqxx.setGqsj(CalendarUtil.getCurDate());
                }
                int result2 = entityMapper.saveOrUpdate(dchyXmglChxmGqxx, dchyXmglChxmGqxx.getGqxxid());

                if (result1 > 0 && result2 > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
            return false;
        }
        return false;
    }


}
