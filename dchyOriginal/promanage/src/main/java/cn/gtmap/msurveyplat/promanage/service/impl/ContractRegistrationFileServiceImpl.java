package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.ContractRegistrationFileMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClcgMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClgcpzMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClsxHtxxGxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;
import cn.gtmap.msurveyplat.promanage.service.ContractRegistrationFileService;
import cn.gtmap.msurveyplat.promanage.service.ResultsSubmitService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/11
 * @description ??????????????????
 */
@Service
public class ContractRegistrationFileServiceImpl implements ContractRegistrationFileService {

    private static final Log logger = LogFactory.getLog(ContractRegistrationFileServiceImpl.class);

    @Autowired
    private ContractRegistrationFileMapper contractMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    ResultsSubmitService resultsSubmitService;

    @Autowired
    DchyXmglClsxHtxxGxMapper dchyXmglClsxHtxxGxMapper;

    @Autowired
    DchyXmglClcgMapper dchyXmglClcgMapper;

    @Autowired
    private DchyXmglClgcpzMapper dchyXmglClgcpzMapper;

    @Override
    public ResponseMessage getContractRegisterFile(Map<String, Object> data) {
        ResponseMessage message = new ResponseMessage();
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        logger.info("******************page:" + page);
        logger.info("******************pageSize:" + pageSize);
        HashMap<String, Object> map = Maps.newHashMap();
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String ksslsj = CommonUtil.ternaryOperator(data.get("ksslsj"));
        String jsslsj = CommonUtil.ternaryOperator(data.get("jsslsj"));
        String wtdw = CommonUtil.ternaryOperator(data.get("wtdw"));
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        map.put("gcbh", gcbh);
        map.put("gcmc", gcmc);
        map.put("ksslsj", ksslsj);
        map.put("jsslsj", jsslsj);
        map.put("wtdw", wtdw);
        map.put("chdwmc", chdwmc);
        Page<Map<String, Object>> registerFileByPage = repository.selectPaging("getContractRegisterFileByPage", map, page - 1, pageSize);

        logger.info("******************registerFileByPage:" + registerFileByPage.getContent());
        if (null != registerFileByPage) {
            List<Map<String, Object>> rows = combineChdwInfo(registerFileByPage.getContent(), map);
            logger.info("******************rows:" + rows);
            if (CollectionUtils.isNotEmpty(rows)) {
                for (Map<String, Object> dataMap : rows) {
                    if (StringUtils.isNotBlank(MapUtils.getString(dataMap, "XMZT"))) {
                        String xmzt = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XMZT, MapUtils.getString(dataMap, "XMZT")).getMc();
                        if (!StringUtils.equals("?????????", xmzt)) {
                            dataMap.put("XMZTMC", "?????????");
                        } else {
                            dataMap.put("XMZTMC", xmzt);
                        }
                    }
                }
            }
            DataSecurityUtil.decryptMapList(registerFileByPage.getContent());//??????
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", rows);
            message.getData().put("totalPage", registerFileByPage.getTotalPages());
            message.getData().put("totalNum", registerFileByPage.getTotalElements());
        }
        logger.info("******************message:" + message);
        return message;
    }

    private List<Map<String, Object>> combineChdwInfo(List<Map<String, Object>> rows, Map<String, Object> ps) {
        List<String> chxmIdList = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        /*
         * ?????????????????????????????????????????????list??????????????????
         * */
        if (CollectionUtils.isNotEmpty(rows)) {
            String chxmid = "";
            for (Map<String, Object> row : rows) {
                chxmid = CommonUtil.ternaryOperator(row.get("CHXMID"));
                if (StringUtils.isNotBlank(chxmid)) {
                    chxmIdList.add(chxmid);
                }
            }

            map.put("chxmIdList", chxmIdList);
            map.put("chdwmc", MapUtils.getString(ps, "chdwmc"));
            List<Map<String, Object>> mapList = contractMapper.getChdwxxByChxmId(map);
            Iterator<Map<String, Object>> iterator;
            String chdwmc;
            String tempChxmid;
            String blankStr = ",";
            Map<String, Object> next;

            for (Map<String, Object> row : rows) {
                StringBuilder chdmBuilder = new StringBuilder();
                if (CollectionUtils.isNotEmpty(mapList)) {
                    iterator = mapList.iterator();
                    while (iterator.hasNext()) {
                        next = iterator.next();
                        if (!next.isEmpty()) {
                            /*??????????????????*/
                            chdwmc = next.get("CHDWMC") != null ? next.get("CHDWMC").toString() : null;
                            /*??????chxmid*/
                            tempChxmid = next.get("CHXMID") != null ? next.get("CHXMID").toString() : null;

                            if (StringUtils.equals(MapUtils.getString(row, "CHXMID"), tempChxmid)) {
                                chdmBuilder.append(chdwmc).append(blankStr);
                            }
                        }
                    }
                }
                row.put("CHDWMC", chdmBuilder.toString());
            }
        }
        return rows;
    }

    /**
     * ???????????????????????????"?????????"???????????????????????????????????????"??????????????????????????????????????????????????????"
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> checkProjectArchStatus(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        boolean flag = true;
        HashMap<String, Object> map = Maps.newHashMap();
        DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        Example exampleClsx = new Example(DchyXmglChxmClsx.class);
        exampleClsx.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(exampleClsx);
        List<Object> clsxList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
            for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                clsxList.add(dchyXmglChxmClsx.getClsx());
            }
        }
        Map<String, Object> ml = this.generateClml();

        if (null != chxm) {
            String tempXmzt = chxm.getXmzt();
            /*?????????*/
            if (!StringUtils.equals(Constants.DCHY_XMGL_CHXM_XMZT_YBJ, tempXmzt)) {
                Example clcgExample = new Example(DchyXmglClcg.class);
                clcgExample.createCriteria().andEqualTo("chxmid", chxmid).andIn("clsx", clsxList);
                List<DchyXmglClcg> clcgList = entityMapper.selectByExample(clcgExample);

                boolean sftj = this.cgsfqbtj(this.formatClml(ml), this.generateClcgMl(clcgList));
                if (sftj) {
                    map.put("code", "0000");
                    map.put("msg", "??????????????????");
                } else {
                    map.put("code", "0000");
                    map.put("msg", "??????????????????????????????????????????????????????");
                }
            } else {
                map.put("code", "1001");
                map.put("msg", "????????????????????????");
            }
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int projectComplete(Map<String, Object> data) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);

        logger.info("*******************??????????????????*********************");
        if (null != chxm) {
            logger.info("****************************************" + JSONObject.toJSONString(chxm));
            String xmzt = chxm.getXmzt();//??????????????????
            if (StringUtils.isNotBlank(xmzt)) {
                /*?????????????????????????????????*/
                if (StringUtils.equals(Constants.DCHY_XMGL_CHXM_XMZT_YBJ, xmzt)) {
                    /*???????????????????????????*/
                    chxm.setSfsd("0");//0???????????????
                } else {
                    Example chdwxxExample = new Example(DchyXmglChxmChdwxx.class);
                    chdwxxExample.createCriteria().andEqualTo("chxmid", chxm.getChxmid());
                    List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExample(chdwxxExample);
                    String chdwmc = "";
                    if (CollectionUtils.isNotEmpty(chdwxxList)) {
                        DchyXmglChxmChdwxx chdwxx = chdwxxList.get(0);
                        chdwmc = chdwxx.getChdwmc();
                    }
                    /*??????chdwmc???cgcc?????????*/
                    Example cgccExample = new Example(DchyXmglCgcc.class);
                    cgccExample.createCriteria().andEqualTo("chdwmc", chdwmc);
                    List<DchyXmglCgcc> cgccList = entityMapper.selectByExample(cgccExample);
                    /*chdw???cgcc????????????????????????????????????*/
                    if (CollectionUtils.isEmpty(cgccList)) {
                        chxm.setSfsd("1");//????????????????????????
                        chxm.setCcnum(0);//?????????????????????0
                        String chgcid = chxm.getChgcid();
                        if (StringUtils.isNotBlank(chgcid)) {
                            DchyXmglChgc xmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                            if (null != xmglChgc) {
                                /*??????????????????????????????????????????*/
                                DchyXmglCgcc xmglCgcc = new DchyXmglCgcc();
                                xmglCgcc.setCgccid(UUIDGenerator.generate18());
                                xmglCgcc.setCcsj(new Date());//????????????  ????????????
                                xmglCgcc.setBabh(chxm.getSlbh());//????????????
                                xmglCgcc.setGcbh(xmglChgc.getGcbh());//????????????
                                xmglCgcc.setGcmc(xmglChgc.getGcmc());//????????????
                                xmglCgcc.setJsdw(xmglChgc.getWtdw());//????????????
                                xmglCgcc.setChxmid(chxm.getChxmid());//????????????id
                                xmglCgcc.setChdwmc(chdwmc);//??????????????????
                                xmglCgcc.setCgpj("");//????????????
                                xmglCgcc.setPjyj("");//????????????
                                xmglCgcc.setSfsd(Constants.DCHY_XMGL_CGCC_SFSD_NO);//???????????? 1?????????
                                xmglCgcc.setPjzt(Constants.DCHY_XMGL_JCSJSQ_PJZT_DCC);//???????????? 2:?????????
                                xmglCgcc.setCjsj(new Date());//????????????
                                xmglCgcc.setPjsj(null);//????????????
                                xmglCgcc.setCjr(UserUtil.getCurrentUser().getUsername());//?????????
                                xmglCgcc.setCjrid(UserUtil.getCurrentUserId());//?????????id
                                xmglCgcc.setCcr("");//?????????
                                xmglCgcc.setCcrid("");//?????????id
                                xmglCgcc.setPjr("");//?????????
                                xmglCgcc.setPjrid("");//?????????id
                                entityMapper.saveOrUpdate(xmglCgcc, xmglCgcc.getCgccid());

                            }
                        }
                    }
                }
            }
            /*????????????????????????*/
            chxm.setXmzt(Constants.DCHY_XMGL_CHXM_XMZT_YBJ);
            chxm.setBjr(UserUtil.getCurrentUserId());//?????????
            chxm.setBjsj(new Date());//????????????
        }
        int flag = entityMapper.saveOrUpdate(chxm, chxm.getChxmid());
        if (flag > 0) {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            dchyXmglChxmDto.setDchyXmglChxm(entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxm.getChxmid()));

            List<DchyXmglYhxx> yhxxList = new ArrayList<>();
            DchyXmglChgc dchyXmglChgc = null;
            /*????????????---?????????????????????*/
            /*????????????????????????*/
            String slbh = chxm.getSlbh();//????????????
            Date bjsj = chxm.getBjsj();//????????????
            String gcmc = "";//????????????
            String chgcid = chxm.getChgcid();
            try {
                if (StringUtils.isNotBlank(chgcid)) {
                    dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                    if (null != dchyXmglChgc) {
                        gcmc = dchyXmglChgc.getGcmc();
                    }
                }
                /*????????????????????????????????????????????????*/
                Example chdwxxExample = new Example(DchyXmglChxmChdwxx.class);
                chdwxxExample.createCriteria().andEqualTo("chxmid", chxm.getChxmid());
                List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExample(chdwxxExample);
                if (CollectionUtils.isNotEmpty(chdwxxList)) {
                    for (DchyXmglChxmChdwxx chdwxx : chdwxxList) {
                        if (null != chdwxx) {
                            DchyXmglYhxx yhxx = this.organizeUserData(chdwxx.getMlkid(), chxm.getChxmid(), gcmc, slbh, sdf.format(bjsj));
                            yhxxList.add(yhxx);
                        }
                    }
                }
                /*????????????---?????????????????????*/
                DchyXmglYhxx jsdwYhxx = new DchyXmglYhxx();
                jsdwYhxx.setYhxxid(UUIDGenerator.generate18());
                if (null != dchyXmglChgc) {
                    jsdwYhxx.setJsyhid(dchyXmglChgc.getWtdw());//????????????id --- ????????????
                }
                jsdwYhxx.setGlsxid(chxm.getChxmid());
                DchyXmglYhxxPz yhxxPz = this.getYhxxPzByXxlx(Constants.DCHY_XMGL_ZD_XXNR_SLDBJ);
                String xxnr = yhxxPz.getXxnr();
                xxnr = xxnr.replaceAll("????????????", CommonUtil.ternaryOperator(gcmc, StringUtils.EMPTY)).replaceAll("????????????", slbh).replaceAll("????????????", sdf.format(bjsj));
                jsdwYhxx.setXxnr(xxnr);
                jsdwYhxx.setFsyhid(UserUtil.getCurrentUserId());
                jsdwYhxx.setDqzt(Constants.INVALID);//??????
                jsdwYhxx.setFssj(CalendarUtil.getCurHMSDate());
                jsdwYhxx.setDqsj(null);
                jsdwYhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_SLDBJ);//???????????????
                jsdwYhxx.setSftz(yhxxPz.getSftz());
                yhxxList.add(jsdwYhxx);

                dchyXmglXxtxDto.setDchyXmglYhxxList(yhxxList);
                logger.info("*******************************************" + JSONObject.toJSONString(yhxxList));

                //???????????????????????????
                pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
                pushDataToMqService.pushXxtxResultTo(dchyXmglXxtxDto);


            } catch (Exception e) {
                logger.error("????????????:{}", e);
            }
        }
        return flag;
    }

    private DchyXmglYhxxPz getYhxxPzByXxlx(String xxlx) {
        Example yhxxPzExample = new Example(DchyXmglYhxxPz.class);
        yhxxPzExample.createCriteria().andEqualTo("xxlx", xxlx);
        List<DchyXmglYhxxPz> yhxxPzList = entityMapper.selectByExample(yhxxPzExample);
        if (CollectionUtils.isNotEmpty(yhxxPzList)) {
            DchyXmglYhxxPz yhxxPz = yhxxPzList.get(0);
            if (null != yhxxPz) {
                return yhxxPz;
            }
        }
        return null;
    }


    /**
     * ????????????????????????
     *
     * @param jsyhid
     * @param glsxid
     * @param gcmc
     * @param slbh
     * @param bjsj
     * @return
     */
    private DchyXmglYhxx organizeUserData(String jsyhid, String glsxid, String gcmc, String slbh, String bjsj) {
        DchyXmglYhxx yhxx = new DchyXmglYhxx();
        yhxx.setYhxxid(UUIDGenerator.generate18());
        yhxx.setJsyhid(jsyhid);//????????????id --- ????????????mlkid
        yhxx.setGlsxid(glsxid);
        DchyXmglYhxxPz yhxxPz = this.getYhxxPzByXxlx(Constants.DCHY_XMGL_ZD_XXNR_SLDBJ);
        if (null != yhxxPz) {
            String xxnr = yhxxPz.getXxnr();
            xxnr = xxnr.replaceAll("????????????", CommonUtil.ternaryOperator(gcmc, StringUtils.EMPTY)).replaceAll("????????????", slbh).replaceAll("????????????", bjsj);
            yhxx.setXxnr(xxnr);
            yhxx.setFsyhid(UserUtil.getCurrentUserId());
            yhxx.setDqzt(Constants.INVALID);//??????
            yhxx.setFssj(CalendarUtil.getCurHMSDate());
            yhxx.setDqsj(null);
            yhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_SLDBJ);//???????????????
            yhxx.setSftz(yhxxPz.getSftz());
        }

        return yhxx;
    }


    @Override
    public int alterClsxZt(Map<String, Object> data) {
        String chgcbh = CommonUtil.ternaryOperator(data.get("chgcbh"));
        String clsx = CommonUtil.ternaryOperator(data.get("clsx"));
        String cgtjzt = CommonUtil.ternaryOperator(data.get("cgtjzt"));
        Example zdExample = new Example(DchyXmglZd.class);
        zdExample.createCriteria().andEqualTo("zdlx", "CLSX");
        List<DchyXmglZd> dchyXmglZdList = entityMapper.selectByExample(zdExample);
        List clsxList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dchyXmglZdList)) {
            for (DchyXmglZd dchyXmglZd : dchyXmglZdList) {
                if (StringUtils.equals(dchyXmglZd.getDzzd(), clsx)) {
                    clsxList.add(dchyXmglZd.getDm());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(clsxList)) {
            /*??????chgcbh??????chxm??????*/
            Example chxmExample = new Example(DchyXmglChxm.class);
            chxmExample.createCriteria().andEqualTo("chgcbh", chgcbh);
            List<DchyXmglChxm> chxm = entityMapper.selectByExample(chxmExample);
            DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
            if (CollectionUtils.isNotEmpty(chxm)) {
                for (DchyXmglChxm dchyXmglChxm : chxm) {
                    /*??????chxmid??????????????????*/
                    Example clsxExample = new Example(DchyXmglChxmClsx.class);
                    clsxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid()).andIn("clsx", clsxList);
                    List<DchyXmglChxmClsx> chxmClsxes = entityMapper.selectByExample(clsxExample);
                    if (CollectionUtils.isNotEmpty(chxmClsxes)) {
                        for (DchyXmglChxmClsx xmglChxmClsx : chxmClsxes) {
                            xmglChxmClsx.setCgtjzt(cgtjzt);
                        }
                        try {
                            entityMapper.batchSaveSelective(chxmClsxes);
                            //??????????????????????????????
                            dchyXmglChxmDto.setDchyXmglChxmClsxList(chxmClsxes);
                            pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);

                        } catch (Exception e) {
                            logger.error("????????????{}???", e);
                        }
                    }
                }
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Page<Map<String, Object>> getCzsxList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        return repository.selectPaging("queryClsxListByPage", map, page - 1, size);
    }

    @Override
    public Page<Map<String, Object>> getCzrzList(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        return repository.selectPaging("queryCzrzListByPage", map, page - 1, size);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int changeCzzt(Map<String, Object> param) {
        int flag = 1;
        String czzt = MapUtils.getString(param, "czzt");
        String czyy = MapUtils.getString(param, "czyy");
        List<Map<String, String>> clsxidList = (List<Map<String, String>>) param.get("clsxidList");

        if (CollectionUtils.isNotEmpty(clsxidList)) {
            for (Map<String, String> map : clsxidList) {
                String clsxid = MapUtils.getString(map, "clsxid");
                if (StringUtils.isNoneBlank(clsxid)) {
                    DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                    dchyXmglChxmClsx.setClzt(czzt);
                    int num = entityMapper.saveOrUpdate(dchyXmglChxmClsx, clsxid);

                    DchyXmglClsxCzrz dchyXmglClsxCzrz = new DchyXmglClsxCzrz();
                    dchyXmglClsxCzrz.setCzrzid(UUIDGenerator.generate18());
                    dchyXmglClsxCzrz.setCzr(UserUtil.getCurrentUserId());
                    dchyXmglClsxCzrz.setCzsj(CalendarUtil.getCurHMSDate());
                    dchyXmglClsxCzrz.setClsxid(clsxid);
                    dchyXmglClsxCzrz.setCzzt(convertCzzt(czzt));
                    dchyXmglClsxCzrz.setCzyy(czyy);
                    int num1 = entityMapper.insertSelective(dchyXmglClsxCzrz);

                    if (num < 0 || num1 < 0) {
                        flag = 0;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public void getClsxList(List<Map<String, String>> clsxList) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (Map<String, String> map : clsxList) {
                String clsxid = MapUtils.getString(map, "clsxid");
                if (StringUtils.isNoneBlank(clsxid)) {
                    DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                    dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
                }
            }
        }

        //?????????????????????????????????
        dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);
        pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
    }

    @Override
    public Page<Map<String, Object>> getProjectManagerList(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String xmxz = CommonUtil.ternaryOperator(data.get("xmxz"));
        String wtdw = CommonUtil.ternaryOperator(data.get("wtdw"));
        map.put("gcbh", gcbh);
        map.put("gcmc", gcmc);
        map.put("xmxz", xmxz);
        map.put("wtdw", wtdw);
        Page<Map<String, Object>> chgcPage = repository.selectPaging("getProjectManagerListByPage", map, page - 1, pageSize);
        List<Map<String, Object>> content = chgcPage.getContent();
        DataSecurityUtil.decryptMapList(content);
        String tempGcdzs = "";
        if (CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> chgc : content) {
                if (null != chgc) {
                    /*????????????????????????????????????????????????*/
                    String tempXmxz = MapUtils.getString(chgc, "XMXZ");
                    if (StringUtils.isNotBlank(tempXmxz)) {
                        chgc.put("XMXZMC", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XMXZ, tempXmxz).getMc());
                    }
                    String gcdzs = MapUtils.getString(chgc, "GCDZS");
                    StringBuilder builder = new StringBuilder();
                    if (StringUtils.isNotBlank(gcdzs)) {
                        DchyXmglZd xmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzs);
                        if (null != xmglZd) {
                            tempGcdzs = xmglZd.getMc();
                        }
                        if (StringUtils.isNotBlank(tempGcdzs)) {
                            builder.append(tempGcdzs);
                        }
                    }
                    String gcdzss = MapUtils.getString(chgc, "GCDZSS");
                    if (StringUtils.isNotBlank(gcdzss)) {
                        String tempGcdzss = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzss).getMc();
                        if (StringUtils.isNotBlank(tempGcdzss)) {
                            builder.append(tempGcdzss);
                        }
                    }
                    String gcdzqx = MapUtils.getString(chgc, "GCDZQX");
                    if (StringUtils.isNotBlank(gcdzqx)) {
                        String tempGcdzqx = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzqx).getMc();
                        if (StringUtils.isNotBlank(tempGcdzqx)) {
                            builder.append(tempGcdzqx);
                        }
                    }
                    chgc.put("GCDZMC", builder);
                }
            }
        }
        return chgcPage;
    }

    @Override
    public List<Map<String, Object>> getProjectConstruct(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> clcgList = dchyXmglClcgMapper.queryClcgByChxmid(paramMap);

        if (CollectionUtils.isNotEmpty(clcgList)) {
            for (Map<String, Object> clcgMap : clcgList) {
                String clsx = MapUtils.getString(clcgMap, "CLSX");
                String rksj = MapUtils.getString(clcgMap, "RKSJ");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    rksj = format.format(format.parse(rksj));
                } catch (ParseException e) {
                    logger.error("????????????:{}", e);
                }
                List<DchyXmglClcg> cgclList = dchyXmglClcgMapper.queryClcgmcByChxmidAndClsxAndRksj(chxmid, clsx, rksj);
                List clcgmcList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(cgclList)) {
                    for (DchyXmglClcg cgclLists : cgclList) {
                        String cgclmc = cgclLists.getClcgmc();
                        clcgmcList.add(cgclmc);
                    }
                }
                clcgMap.put("CLCGMC", clcgmcList);
                clcgMap.put("CLSXMC", dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, MapUtils.getString(clcgMap, "CLSX")));
            }
        }
        return clcgList;
    }

    @Override
    public List<Map<String, Object>> getProjectClsxInfo(Map<String, Object> data) {
        String chgcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String clsx = CommonUtil.ternaryOperator(data.get("clsx"));

        Map<String, Object> map = new HashMap<>();
        map.put("chgcbh", chgcbh);
        map.put("clsx", clsx);
        List<Map<String, Object>> resultList = dchyXmglClcgMapper.queryShxxByChgcbhAndClsx(map);
        List rolrRel = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> resultLists : resultList) {
                String userid = MapUtils.getString(resultLists, "SHR");
                if (StringUtils.isNotEmpty(userid)) {
                    List<Map<String, Object>> roleList = dchyXmglClcgMapper.queryRoleByUserid(userid);
                    if (CollectionUtils.isNotEmpty(roleList)) {
                        for (Map<String, Object> roleLists : roleList) {
                            String rolename = MapUtils.getString(roleLists, "ROLENAME");
                            rolrRel.add(rolename);
                        }
                        resultLists.put("ROLENAME", rolrRel);
                    }
                }

            }

        }

        return resultList;
    }

    @Override
    public Map<String, Object> chgcclcgByChxm(Map data) {
        String chgcid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chgcid"));
        List<Map<String, Object>> resultList = new LinkedList<>();
        Map<String, Object> chgcClcgMap = Maps.newHashMap();
        Map<String, Object> chgcMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(chgcid)) {
            DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
            Example dchyXmglChxmExample = new Example(DchyXmglChxm.class);
            dchyXmglChxmExample.createCriteria().andEqualTo("chgcid", chgcid);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(dchyXmglChxmExample);
            Map param = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                for (DchyXmglChxm dchyXmglChxm : dchyXmglChxmList) {
                    param.clear();
                    param.put("chxmid", dchyXmglChxm.getChxmid());
                    Map<String, Object> resultMap = viewattachments2(param);
                    if (MapUtils.isNotEmpty(resultMap)) {
                        resultList.add(resultMap);
                    }
                }
            }
            if (null != dchyXmglChgc) {
                chgcMap.put("gcbh", dchyXmglChgc.getGcbh());
            }
        }
        chgcClcgMap.put("chgcxx", chgcMap);
        chgcClcgMap.put("clcgByChxmList", resultList);
        return chgcClcgMap;
    }

    @Override
    public Map<String, Object> viewattachments2(Map data) {

        Map<String, Object> resultMap = new LinkedHashMap<>();
        String xmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "xmid"));
        String chgcid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chgcid"));
        String chxmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chxmid"));
        Map<String, Object> map = resultsSubmitService.queryClsxListByTaskid(data);

        List<String> clsxZdList = (List<String>) map.get("clsxZdList");
        /*???xmid??????????????????chgcid??????xmid*/
        List<DchyXmglClcg> clcgList = null;
        // ??????????????????????????????????????????????????????????????????
        String topBh = null;
        if (StringUtils.isNotBlank(chxmid)) {

            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != dchyXmglChxm) {
                topBh = dchyXmglChxm.getSlbh();
            }
            Map param = Maps.newHashMap();
            param.put("chxmid", chxmid);
            //??????????????????,???????????????????????????
            param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
            clcgList = contractMapper.getClcgByChxmid(param);
        } else if (StringUtils.isNotBlank(chgcid) && StringUtils.isBlank(xmid)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("chgcid", chgcid);
            param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
            clcgList = contractMapper.getClcgByChgcid(param);
        } else if (StringUtils.isNotBlank(xmid)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("sqxxid", xmid);
            param.put("clsxList", clsxZdList);
            clcgList = contractMapper.getClcgBySqxxid(param);
        }
        if (CollectionUtils.isNotEmpty(clcgList)) {

            DchyXmglClcg xmglClcg = clcgList.get(0);
            if (null != xmglClcg) {
                /*???????????????id?????????????????????id*/
                String wjzxid = xmglClcg.getWjzxid();
                if (StringUtils.isNotBlank(wjzxid)) {
                    try {
                        int nodeId = Integer.parseInt(wjzxid);

                        //??????????????????
                        Node node = getNodeService().getNode(nodeId);
                        if (null != node) {
                            /*????????????????????????????????????wjzxid*/
                            if (StringUtils.isBlank(topBh)) {
                                topBh = xmglClcg.getChgcbh();

                            }
                            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                            Integer topMlWjzxid = null;
                            if (null != xmglChxm) {
                                topMlWjzxid = this.getTopMlWjzxid(node.getParentId(), topBh, xmglChxm.getBabh());
                            }
                            Node topNode = getNodeService().getNode(topMlWjzxid);

                            if (null != topNode) {
                                List<String> wjzxidList = getWjzxidList(clcgList, topNode.getId());
                                /*node*/
                                Map<String, Object> nodeMap = new LinkedHashMap<>();
                                nodeMap.put("icon", topNode.getIcon());
                                nodeMap.put("id", topNode.getId());
                                nodeMap.put("name", topNode.getName());
                                nodeMap.put("parentId", topNode.getParentId());
                                nodeMap.put("path", topNode.getPath());
                                nodeMap.put("scope", topNode.getScope());
                                nodeMap.put("type", topNode.getType());
                                nodeMap.put("updateTime", topNode.getUpdateTime());
                                nodeMap.put("chxmid", chxmid);
                                resultMap.put("node", nodeMap);
                                /*items*/
                                List<Map<String, Object>> childNodeList = new LinkedList<>();
                                List<Node> childNodes = getNodeService().getChildNodes(topMlWjzxid);
                                if (CollectionUtils.isNotEmpty(childNodes)) {
                                    for (Node childNode : childNodes) {
                                        Map<String, Object> childNodeMap = new LinkedHashMap<>();
                                        childNodeMap.put("icon", childNode.getIcon());
                                        childNodeMap.put("id", childNode.getId());
                                        childNodeMap.put("name", childNode.getName());
                                        childNodeMap.put("parentId", childNode.getParentId());
                                        childNodeMap.put("path", childNode.getPath());
                                        childNodeMap.put("scope", childNode.getScope());
                                        childNodeMap.put("type", childNode.getType());
                                        childNodeMap.put("updateTime", childNode.getUpdateTime());
                                        List<Node> allChildNodes = getNodeService().getAllChildNodes(childNode.getId());
                                        childNodeMap.put("childCount", getChildCount(childNode.getId(), clcgList, allChildNodes));
                                        if (CollectionUtils.isEmpty(wjzxidList)) {
                                            // ????????????????????????????????????
                                            childNodeList.add(childNodeMap);
                                        } else if (wjzxidList.contains(childNode.getId().toString())) {
                                            //????????????????????????????????????????????????????????????,???????????????????????????????????????,?????????????????????????????????
                                            childNodeList.add(childNodeMap);
                                        }
                                    }
                                }
                                resultMap.put("items", childNodeList);
                                resultMap.put("title", childNodeList.size());
                                resultMap.put("author", xmglClcg.getTjrmc() != null ? xmglClcg.getTjrmc() : "");//?????????
                            }
                        }
                    } catch (Exception e) {
                        logger.error("????????????????????????????????????:{}", e);
                    }
                }
            }
        } else {
            return new LinkedHashMap();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> viewattachments2ByCgsh(Map data) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        Map<String, Object> resultMap = new LinkedHashMap<>();
        String xmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "xmid"));
        String chgcid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chgcid"));
        String chxmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chxmid"));
        Map<String, Object> map = resultsSubmitService.queryClsxListByTaskid(data);

        List<String> clsxZdList = (List<String>) map.get("clsxZdList");
        /*???xmid??????????????????chgcid??????xmid*/
        List<DchyXmglClcg> clcgList = null;
        // ??????????????????????????????????????????????????????????????????
        String topBh = null;
        if (StringUtils.isNotBlank(chxmid)) {

            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != dchyXmglChxm) {
                topBh = dchyXmglChxm.getBabh();
            }
            Map param = Maps.newHashMap();
            param.put("chxmid", chxmid);
//            param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
            clcgList = contractMapper.getClcgByChxmid(param);
        } else if (StringUtils.isNotBlank(chgcid) && StringUtils.isBlank(xmid)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("chgcid", chgcid);
            param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
            clcgList = contractMapper.getClcgByChgcid(param);
        } else if (StringUtils.isNotBlank(xmid)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("sqxxid", xmid);
            param.put("clsxList", clsxZdList);
            clcgList = contractMapper.getClcgBySqxxid(param);
        }
        if (CollectionUtils.isNotEmpty(clcgList)) {

            DchyXmglClcg xmglClcg = clcgList.get(0);
            if (null != xmglClcg) {
                /*???????????????id?????????????????????id*/
                String wjzxid = xmglClcg.getWjzxid();
                if (StringUtils.isNotBlank(wjzxid)) {
                    try {
                        int nodeId = Integer.parseInt(wjzxid);

                        //??????????????????
                        Node node = getNodeService().getNode(nodeId);
                        if (null != node) {
                            /*????????????????????????????????????wjzxid*/
                            if (StringUtils.isBlank(topBh)) {
                                topBh = xmglClcg.getChgcbh();

                            }
                            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, xmglClcg.getChxmid());
                            Integer topMlWjzxid = null;
                            if (null != xmglChxm) {
                                topMlWjzxid = this.getTopMlWjzxid(node.getParentId(), topBh, xmglChxm.getBabh());
                            }
                            Node topNode = getNodeService().getNode(topMlWjzxid);

                            if (null != topNode) {
                                List<String> wjzxidList = getWjzxidList(clcgList, topNode.getId());
                                /*node*/
                                Map<String, Object> nodeMap = new LinkedHashMap<>();
                                nodeMap.put("icon", topNode.getIcon());
                                nodeMap.put("id", topNode.getId());
                                nodeMap.put("name", topNode.getName());
                                nodeMap.put("parentId", topNode.getParentId());
                                nodeMap.put("path", topNode.getPath());
                                nodeMap.put("scope", topNode.getScope());
                                nodeMap.put("type", topNode.getType());
                                nodeMap.put("updateTime", topNode.getUpdateTime());
                                resultMap.put("node", nodeMap);
                                /*items*/
                                List<Map<String, Object>> childNodeList = new LinkedList<>();
                                List<Node> childNodes = getNodeService().getChildNodes(topMlWjzxid);
                                if (CollectionUtils.isNotEmpty(childNodes)) {
                                    for (Node childNode : childNodes) {
                                        Map<String, Object> childNodeMap = new LinkedHashMap<>();
                                        childNodeMap.put("icon", childNode.getIcon());
                                        childNodeMap.put("id", childNode.getId());
                                        childNodeMap.put("name", childNode.getName());
                                        childNodeMap.put("parentId", childNode.getParentId());
                                        childNodeMap.put("path", childNode.getPath());
                                        childNodeMap.put("scope", childNode.getScope());
                                        childNodeMap.put("type", childNode.getType());
                                        childNodeMap.put("updateTime", childNode.getUpdateTime());
                                        List<Node> allChildNodes = getNodeService().getAllChildNodes(childNode.getId());
                                        childNodeMap.put("childCount", getChildCount(childNode.getId(), clcgList, allChildNodes));
                                        if (CollectionUtils.isEmpty(wjzxidList)) {
                                            // ????????????????????????????????????
                                            childNodeList.add(childNodeMap);
                                        } else if (wjzxidList.contains(childNode.getId().toString())) {
                                            //????????????????????????????????????????????????????????????,???????????????????????????????????????,?????????????????????????????????
                                            childNodeList.add(childNodeMap);
                                        }
                                    }
                                }
                                resultMap.put("items", childNodeList);
                                resultMap.put("title", childNodeList.size());
                                resultMap.put("author", xmglClcg.getTjrmc() != null ? xmglClcg.getTjrmc() : "");//?????????
                            }
                            resultList.add(resultMap);
                        }
                    } catch (Exception e) {
                        logger.error("????????????????????????????????????:{}", e);
                    }
                }
            }
        } else {
            return new LinkedHashMap();
        }
        return resultMap;
    }


    private Integer getChildCount(Integer nodeId, List<DchyXmglClcg> dchyXmglClcgList, List<Node> allChildNodes) {
        Integer num = 0;
        if (CollectionUtils.isNotEmpty(dchyXmglClcgList) && CollectionUtils.isNotEmpty(allChildNodes)) {
            List<String> wjzxids = Lists.newArrayList();
            for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                if (StringUtils.isNoneBlank(dchyXmglClcg.getWjzxid())) {
                    wjzxids.add(dchyXmglClcg.getWjzxid());
                }
            }
            List<String> parentNodeList = Lists.newArrayList();
            for (Node node : allChildNodes) {
                if (node.getParentId().equals(nodeId) && node.getType() == 1) {
                    // ???????????????1
                    num++;
                } else if (wjzxids.contains(node.getId().toString()) && parentNodeList.indexOf(node.getParentId().toString()) < 0) {
                    //????????????????????????
                    parentNodeList.add(node.getParentId().toString());

                }
            }
            if (CollectionUtils.isNotEmpty(parentNodeList)) {
                num += getWjzxFolderCount(nodeId, allChildNodes, parentNodeList);
            }
        }
        return num;
    }

    private Integer getWjzxFolderCount(Integer nodeId, List<Node> allChildNodes, List<String> parentNodeList) {
        Integer num = 0;
        if (CollectionUtils.isNotEmpty(parentNodeList)) {
            List<String> parentNodeList2 = Lists.newArrayList();
            for (Node node : allChildNodes) {
                if (node.getParentId().equals(nodeId) && node.getType() == 1) {
                    // ???????????????1
                    num++;
                } else if (parentNodeList.contains(node.getId().toString())) {
                    //????????????????????????
                    if (node.getParentId().equals(nodeId)) {
                        num++;
                    } else {
                        if (parentNodeList2.indexOf(node.getParentId().toString()) < 0) {
                            parentNodeList2.add(node.getParentId().toString());
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(parentNodeList2)) {
                num += getWjzxFolderCount(nodeId, allChildNodes, parentNodeList2);
            }
        }
        return num;
    }


    /**
     * ???????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> onlineBaFilePreview(Map<String, Object> data) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        StringJoiner joiner = new StringJoiner("-");
        String chxmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chxmid"));
        String sjr = "";
        Date sjsj = null;
        if (StringUtils.isNotBlank(chxmid)) {
            /*???????????????????????????*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                DchyXmglSjxx sjxx1 = sjxxList.get(0);
                sjr = sjxx1.getSjr();
                sjsj = sjxx1.getSjsj();
                List<Object> sjxxIdList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxIdList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxIdList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    for (DchyXmglSjcl sjcl : sjclList) {
                        List<Map<String, Object>> uploadClxx = this.getUploadClxx(sjcl.getWjzxid(), joiner);
                        resultList.addAll(uploadClxx);
                    }
                }
            }
            /*????????????*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx xmglHtxx : htxxList) {
                    String htxxid = xmglHtxx.getHtxxid();
                    if (StringUtils.isNotBlank(htxxid)) {
                        /*??????htxxid??????sjxx*/
                        Example htSjxxExample = new Example(DchyXmglSjxx.class);
                        htSjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
                        List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                        if (CollectionUtils.isNotEmpty(htSjxxList)) {
                            for (DchyXmglSjxx htSjxx : htSjxxList) {
                                //String htSjxxid = htSjxxList.get(0).getSjxxid();
                                /*????????????????????????*/
                                Example htSjclExample = new Example(DchyXmglSjcl.class);
                                htSjclExample.createCriteria().andEqualTo("sjxxid", htSjxx.getSjxxid()).andIsNotNull("wjzxid");
                                List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                                if (CollectionUtils.isNotEmpty(htSjclList)) {
                                    for (DchyXmglSjcl htSjcl : htSjclList) {
                                        List<Map<String, Object>> uploadClxx = this.getUploadClxx(htSjcl.getWjzxid(), joiner);
                                        resultList.addAll(uploadClxx);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        returnMap.put("title", "????????????");
        returnMap.put("wjzxid", joiner.toString());
        returnMap.put("type", "0");
        returnMap.put("children", resultList);
        returnMap.put("sjr", UserUtil.getUserNameById(sjr));
        returnMap.put("sjsj", sjsj);
        return returnMap;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @return
     */
    private List<Map<String, Object>> getUploadClxx(String wjzxid, StringJoiner joiner) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        try {
            if (StringUtils.isNotBlank(wjzxid)) {
                List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node node : nodeList) {
                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                        uploadFileMap.put("title", node.getName());
                        uploadFileMap.put("wjzxid", node.getId());
                        uploadFileMap.put("type", node.getType());
                        joiner.add(node.getId() + "");
                        resultList.add(uploadFileMap);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return resultList;
    }


    @Override
    public Map<String, Object> viewChildAttachments(Map data) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        String chgcid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chgcid"));
        String wjzxid = CommonUtil.ternaryOperator(MapUtils.getString(data, "id"));
        String xmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "xmid"));
        String taskId = CommonUtil.ternaryOperator(MapUtils.getString(data, "taskid"));
        String gzlslid = CommonUtil.ternaryOperator(MapUtils.getString(data, "gzlslid"));
        String chxmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chxmid"));
        List<DchyXmglClcg> clcgList = null;
        List<String> wjzxidList = null;

        if (StringUtils.isNotBlank(wjzxid)) {
            try {
                Integer nodeId = Integer.parseInt(wjzxid);
                if (StringUtils.isNoneBlank(taskId, gzlslid, xmid)) {
                    data.put("taskid", taskId);
                    data.put("gzlslid", gzlslid);
                    data.put("xmid", xmid);
                    Map<String, Object> map = resultsSubmitService.queryClsxListByTaskid(data);

                    List<String> clsxZdList = (List<String>) map.get("clsxZdList");
                    /*???xmid??????????????????chgcid??????xmid*/

                    Map<String, Object> param = Maps.newHashMap();
                    param.put("sqxxid", xmid);
                    param.put("clsxList", clsxZdList);
                    param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
                    clcgList = contractMapper.getClcgBySqxxid(param);
                    logger.info("??????: " + param + "  chxmid???????????????????????????: " + clcgList.size() + "  ??????: " + clcgList);
                    wjzxidList = getWjzxidList(clcgList, nodeId);
                } else if (StringUtils.isNotBlank(chxmid)) {
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("chxmid", chxmid);
                    param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
                    clcgList = contractMapper.getClcgByChxmid(param);
                    logger.info("??????: " + param + "  chxmid???????????????????????????: " + clcgList.size() + "  ??????: " + clcgList);
                    wjzxidList = getWjzxidList(clcgList, nodeId);
                    logger.info("wjzxidList: " + wjzxidList);
                } else if (StringUtils.isNotBlank(chgcid)) {
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("chgcid", chgcid);
                    param.put("shzt", Constants.DCHY_XMGL_SHZT_SHTG);
                    clcgList = contractMapper.getClcgByChgcid(param);
                    logger.info("??????: " + param + "  chgcid???????????????????????????: " + clcgList.size() + "  ??????: " + clcgList);
                    wjzxidList = getWjzxidList(clcgList, nodeId);
                }
                Node node = getNodeService().getNode(nodeId);
                if (null != node && CollectionUtils.isNotEmpty(clcgList)) {
                    /*node*/
                    Map<String, Object> nodeMap = new LinkedHashMap<>();
                    nodeMap.put("icon", node.getIcon());
                    nodeMap.put("id", node.getId());
                    nodeMap.put("name", node.getName());
                    nodeMap.put("parentId", node.getParentId());
                    nodeMap.put("path", node.getPath());
                    nodeMap.put("scope", node.getScope());
                    nodeMap.put("type", node.getType());
                    nodeMap.put("updateTime", node.getUpdateTime());
                    resultMap.put("node", nodeMap);
                    /*items*/
                    List<Map<String, Object>> childNodeList = new LinkedList<>();
                    List<Node> childNodes = getNodeService().getChildNodes(node.getId());
                    logger.info("childNodes: " + childNodes);
                    if (CollectionUtils.isNotEmpty(childNodes)) {
                        for (Node childNode : childNodes) {
                            Map<String, Object> childNodeMap = new LinkedHashMap<>();
                            //????????????????????????
                            if (childNode != null && childNode.getParentId().equals(node.getId())) {
                                childNodeMap.put("childCount", childNode.getChildCount());
                                childNodeMap.put("icon", childNode.getIcon());
                                childNodeMap.put("id", childNode.getId());
                                childNodeMap.put("name", childNode.getName());
                                childNodeMap.put("parentId", childNode.getParentId());
                                childNodeMap.put("path", childNode.getPath());
                                childNodeMap.put("scope", childNode.getScope());
                                childNodeMap.put("type", childNode.getType());
                                childNodeMap.put("updateTime", childNode.getUpdateTime());
                                if (CollectionUtils.isEmpty(wjzxidList)) {
                                    // ????????????????????????????????????
                                    childNodeList.add(childNodeMap);
                                    logger.info("wjzxidList??????????????????????????????: " + childNodeMap.get("name"));
                                } else if (wjzxidList.contains(childNode.getId().toString())) {
                                    logger.info("??????id: " + childNode.getId() + "  ?????????: " + childNode.getName());
                                    //????????????????????????????????????????????????????????????,???????????????????????????????????????,?????????????????????????????????
                                    List<Node> allChildNodes = getNodeService().getAllChildNodes(childNode.getId());
                                    logger.info("allChildNodes: " + allChildNodes);
                                    childNodeMap.put("childCount", getChildCount(childNode.getId(), clcgList, allChildNodes));
                                    childNodeList.add(childNodeMap);
                                }
                            }
                        }
                        logger.info("=================================================================================");
                    }
                    resultMap.put("items", childNodeList);
                    logger.info("????????????: " + childNodeList);
                    resultMap.put("title", childNodeList.size());
                }
                resultList.add(resultMap);
            } catch (Exception e) {
                logger.error("????????????:{}", e);
            }
        }
        return resultMap;
    }

    private List<String> getWjzxidList(List<DchyXmglClcg> dchyXmglClcgList, Integer topWjzxid) {
        List<String> wjzxidList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
            for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                if (StringUtils.isBlank(dchyXmglClcg.getWjzxid())) {
                    continue;
                }
                Integer wjzxid = getChildWjzxid(Integer.parseInt(dchyXmglClcg.getWjzxid()), topWjzxid);
                if (wjzxid.compareTo(0) > 0 && !wjzxidList.contains(wjzxid.toString())) {
                    wjzxidList.add(wjzxid.toString());
                }
            }
        }
        return wjzxidList;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param wjzxid
     * @return
     */
    private Integer getChildWjzxid(Integer wjzxid, Integer topWjzxid) {
        try {
            if (!wjzxid.equals(0) && wjzxid.compareTo(topWjzxid) > 0) {
                Node node = getNodeService().getNode(wjzxid);
                if (node.getParentId().equals(topWjzxid)) {
                    return node.getId();
                }
                return this.getChildWjzxid(node.getParentId(), topWjzxid);
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return 0;

    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param wjzxid
     * @return
     */
    private Integer getTopMlWjzxid(Integer wjzxid, String chgcbh, String babh) {
        Node node = getNodeService().getNode(wjzxid);
        String name = node.getName();
        if (StringUtils.equals(chgcbh, name) || StringUtils.equals(babh, name)) {
            return node.getId();
        }
        return this.getTopMlWjzxid(node.getParentId(), chgcbh, babh);
    }


    @Override
    public Map<String, Object> viewattachments(Map<String, Object> data) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        String chgcid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chgcid"));
        if (StringUtils.isNotBlank(chgcid)) {
            /*??????????????????*/
            Map<String, Object> chxmMap = new LinkedHashMap<>();
            chxmMap.put("title", "?????????");
            chxmMap.put("wjzxid", "");
            chxmMap.put("cllx", "0");//1:????????????????????????0????????????????????????
            chxmMap.put("type", 0);
            chxmMap.put("children", this.getPromanageFileView(chgcid));
            /*??????????????????*/
            Map<String, Object> clcgMap = new LinkedHashMap<>();
            clcgMap.put("title", "?????????");
            clcgMap.put("wjzxid", "");
            clcgMap.put("cllx", "1");//1:????????????????????????0????????????????????????
            clcgMap.put("type", 0);
            clcgMap.put("children", this.getClCgFileView(chgcid));
            resultList.add(chxmMap);
            resultList.add(clcgMap);
        }
        returnMap.put("lists", resultList);
        return returnMap;
    }

    @Override
    public Map<String, Object> viewattachmentsByClsx(Map<String, String> map) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        Map<String, Object> mapClsxList = resultsSubmitService.queryClsxListByTaskid(map);
        List<Map<String, Object>> clsxList = (List<Map<String, Object>>) mapClsxList.get("clsxList");
        List<String> clsxMcList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (Map<String, Object> clsxMap : clsxList) {
                clsxMcList.add(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "CLSX")));
            }
        }

        String wjzxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "id"));
        if (StringUtils.isNotBlank(wjzxid)) {
            try {
                Integer nodeId = Integer.parseInt(wjzxid);
                Node node = getNodeService().getNode(nodeId);
                if (null != node) {
                    /*node*/
                    Map<String, Object> nodeMap = new LinkedHashMap<>();
                    nodeMap.put("icon", node.getIcon());
                    nodeMap.put("id", node.getId());
                    nodeMap.put("name", node.getName());
                    nodeMap.put("parentId", node.getParentId());
                    nodeMap.put("path", node.getPath());
                    nodeMap.put("scope", node.getScope());
                    nodeMap.put("type", node.getType());
                    nodeMap.put("updateTime", node.getUpdateTime());
                    resultMap.put("node", nodeMap);
                    /*items*/
                    List<Map<String, Object>> childNodeList = new LinkedList<>();
                    List<Node> childNodes = getNodeService().getChildNodes(node.getId());
                    if (CollectionUtils.isNotEmpty(childNodes)) {
                        for (Node childNode : childNodes) {
                            if (clsxMcList.contains(CommonUtil.formatEmptyValue(childNode.getName()))) {
                                Map<String, Object> childNodeMap = new LinkedHashMap<>();
                                childNodeMap.put("childCount", childNode.getChildCount());
                                childNodeMap.put("icon", childNode.getIcon());
                                childNodeMap.put("id", childNode.getId());
                                childNodeMap.put("name", childNode.getName());
                                childNodeMap.put("parentId", childNode.getParentId());
                                childNodeMap.put("path", childNode.getPath());
                                childNodeMap.put("scope", childNode.getScope());
                                childNodeMap.put("type", childNode.getType());
                                childNodeMap.put("updateTime", childNode.getUpdateTime());
                                childNodeList.add(childNodeMap);
                            }
                        }
                    }
                    resultMap.put("items", childNodeList);
                    resultMap.put("title", childNodeList.size());
                }
                resultList.add(resultMap);
            } catch (Exception e) {
                logger.error("????????????{}", e);

            }
        }
        return resultMap;
    }


    public List<Map<String, Object>> getPromanageFileView(String chgcid) {
        List<String> slrs = new ArrayList<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        Example chxmExample = new Example(DchyXmglChxm.class);
        chxmExample.createCriteria().andEqualTo("chgcid", chgcid);
        List<DchyXmglChxm> chxmList = entityMapper.selectByExample(chxmExample);
        if (CollectionUtils.isNotEmpty(chxmList)) {
            for (DchyXmglChxm chxm : chxmList) {
                if (null != chxm) {
                    String chxmid = chxm.getChxmid();
                    if (StringUtils.isNotBlank(chxmid)) {
                        /*??????????????????*/
                        Map<String, Object> chxmMap = new LinkedHashMap<>();
                        chxmMap.put("title", chxm.getChgcbh());
                        chxmMap.put("wjzxid", "");
                        chxmMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                        chxmMap.put("type", 0);
                        chxmMap.put("children", this.getChxmidByXmid(chxmid));
                        String name = UserUtil.getUserNameById(chxm.getSlr());
                        if (StringUtils.isNotBlank(name)) {
                            slrs.add(name);
                        }
                        chxmMap.put("slrList", slrs);
                        resultList.add(chxmMap);
                    }
                }
            }
        }
        return resultList;
    }

    public List<Map<String, Object>> getClCgFileView(String chgcid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        /*??????????????????*/
        List<DchyXmglClcgpz> clmlList = contractMapper.getClmlFromClcgpz();
        if (CollectionUtils.isNotEmpty(clmlList)) {
            for (DchyXmglClcgpz clcg : clmlList) {
                if (null != clcg) {
                    Map<String, Object> clcgMap = new LinkedHashMap<>();
                    clcgMap.put("title", clcg.getClmc());
                    clcgMap.put("wjzxid", this.getClcgMlWjzxids(clcg.getClcgpzid(), chgcid));
                    clcgMap.put("cllx", "1");//1:????????????????????????0????????????????????????
                    clcgMap.put("cgml", clcg.getClmc());
                    clcgMap.put("type", 0);
                    clcgMap.put("children", this.getClcgTopList(chgcid, clcg.getClcgpzid()));
                    resultList.add(clcgMap);
                }
            }
        }
        return resultList;
    }

    private String getClcgMlWjzxids(String clcgpzid, String chgcid) {
        String wjzxids = "";
        Set<String> set = new HashSet<>();
        if (StringUtils.isNotBlank(chgcid)) {
            Example clcgExample = new Example(DchyXmglClcg.class);
            clcgExample.createCriteria().andEqualTo("chgcid", chgcid);
            List<DchyXmglClcg> clcgList = entityMapper.selectByExample(clcgExample);
            if (CollectionUtils.isNotEmpty(clcgList)) {
                for (DchyXmglClcg xmglClcg : clcgList) {
                    if (null != xmglClcg) {
                        /*?????????wjzxid*/
                        String wjzxid = xmglClcg.getWjzxid();

                        if (StringUtils.isNotBlank(wjzxid)) {
                            //??????????????????
                            String clcgmc = xmglClcg.getClcgmc();
                            int i = clcgmc.indexOf(".");
                            String substring = clcgmc.substring(0, i);
                            Example clpzExample = new Example(DchyXmglClcgpz.class);
                            clpzExample.createCriteria().andLike("clmc", substring);
                            List<DchyXmglClcgpz> clpzList = entityMapper.selectByExample(clpzExample);
                            if (CollectionUtils.isNotEmpty(clpzList)) {
                                DchyXmglClcgpz clcgpz = clpzList.get(0);
                                if (null != clcgpz) {
                                    String fileid = clcgpz.getPclcgpzid();
                                    if (StringUtils.equals(clcgpzid, fileid.substring(0, 1))) {
                                        Node node = getNodeService().getNode(Integer.parseInt(wjzxid));
                                        Integer parentId = node.getParentId();
                                        String clmlDm = contractMapper.getClmlDm(fileid);
                                        set.add(parentId + "," + clmlDm);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (String id : set) {
            wjzxids += "cg" + id + "-";
        }
        return wjzxids;
    }


    /**
     * ?????????????????????
     *
     * @return
     */
    private List<Map<String, Object>> getClcgTopList(String chgcid, String clcgpzid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        Example pzExample = new Example(DchyXmglClcgpz.class);
        pzExample.createCriteria().andEqualTo("pclcgpzid", clcgpzid);
        List<DchyXmglClcgpz> pzList = entityMapper.selectByExample(pzExample);
        if (CollectionUtils.isNotEmpty(pzList)) {
            for (DchyXmglClcgpz clcgpz : pzList) {
                if (null != clcgpz) {
                    Map<String, Object> clcgMap = new LinkedHashMap<>();
                    clcgMap.put("title", clcgpz.getClmc());
                    clcgMap.put("wjzxid", this.getClcgTopWjzxids(chgcid, clcgpz.getClcgpzid()));
                    clcgMap.put("cllx", "1");//1:????????????????????????0????????????????????????
                    clcgMap.put("cgml", clcgpz.getClmc());
                    clcgMap.put("type", 0);
                    clcgMap.put("children", this.getClcgSecList(chgcid, clcgpz.getClcgpzid()));
                    resultList.add(clcgMap);
                }
            }
        }
        return resultList;
    }


    private String getClcgTopWjzxids(String chgcid, String clcgpzid) {
        String wjzxids = "";
        Example pzExample = new Example(DchyXmglClcgpz.class);
        pzExample.createCriteria().andEqualTo("pclcgpzid", clcgpzid);
        List<DchyXmglClcgpz> pzList = entityMapper.selectByExample(pzExample);
        if (CollectionUtils.isNotEmpty(pzList)) {
            for (DchyXmglClcgpz clcgpz : pzList) {
                if (null != clcgpz && StringUtils.isNotBlank(this.getPNodeIdByGcid(chgcid, clcgpz.getClcgpzid()))) {
                    wjzxids += "cg" + this.getPNodeIdByGcid(chgcid, clcgpz.getClcgpzid()) + "-";
                }
            }
        }
        return wjzxids;
    }


    private List<Map<String, Object>> getClcgSecList(String chgcid, String clcgpzid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        Example pzExample = new Example(DchyXmglClcgpz.class);
        pzExample.createCriteria().andEqualTo("pclcgpzid", clcgpzid);
        List<DchyXmglClcgpz> pzList = entityMapper.selectByExample(pzExample);
        if (CollectionUtils.isNotEmpty(pzList)) {
            for (DchyXmglClcgpz clcgpz : pzList) {
                if (null != clcgpz) {
                    Map<String, Object> clcgMap = new LinkedHashMap<>();
                    clcgMap.put("title", clcgpz.getClmc());
                    clcgMap.put("wjzxid", this.getPNodeIdByGcid(chgcid, clcgpz.getClcgpzid()));
                    clcgMap.put("cllx", "1");//1:????????????????????????0????????????????????????
                    clcgMap.put("cgml", clcgpz.getClmc());
                    clcgMap.put("type", 0);
                    clcgMap.put("children", this.getClcgThridList(chgcid, clcgpz.getClcgpzid()));
                    resultList.add(clcgMap);
                }
            }
        }
        return resultList;
    }

    private List<Map<String, Object>> getClcgThridList(String chgcid, String clcgpzid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        if (StringUtils.isNotBlank(chgcid)) {
            Example clcgExample = new Example(DchyXmglClcg.class);
            clcgExample.createCriteria().andEqualTo("chgcid", chgcid);
            List<DchyXmglClcg> clcgList = entityMapper.selectByExample(clcgExample);
            if (CollectionUtils.isNotEmpty(clcgList)) {
                for (DchyXmglClcg xmglClcg : clcgList) {
                    if (null != xmglClcg) {
                        /*?????????wjzxid*/
                        String wjzxid = xmglClcg.getWjzxid();

                        if (StringUtils.isNotBlank(wjzxid)) {
                            //??????????????????
                            String clcgmc = xmglClcg.getClcgmc();
                            int i = clcgmc.indexOf(".");
                            String substring = clcgmc.substring(0, i);
                            Example clpzExample = new Example(DchyXmglClcgpz.class);
                            clpzExample.createCriteria().andLike("clmc", substring);
                            List<DchyXmglClcgpz> clpzList = entityMapper.selectByExample(clpzExample);
                            if (CollectionUtils.isNotEmpty(clpzList)) {
                                DchyXmglClcgpz clcgpz = clpzList.get(0);
                                if (null != clcgpz) {
                                    String tempPclcgpzid = clcgpz.getPclcgpzid();
                                    if (StringUtils.equals(clcgpzid, tempPclcgpzid)) {
                                        Map<String, Object> clcgMap = new LinkedHashMap<>();
                                        clcgMap.put("title", xmglClcg.getClcgmc());
                                        clcgMap.put("wjzxid", xmglClcg.getWjzxid());
                                        clcgMap.put("cllx", "1");//1:????????????????????????0????????????????????????
                                        clcgMap.put("type", 1);
                                        resultList.add(clcgMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }

    private String getPNodeIdByGcid(String chgcid, String clcgpzid) {
        String wjzxids = "";
        if (StringUtils.isNotBlank(chgcid)) {
            Example clcgExample = new Example(DchyXmglClcg.class);
            clcgExample.createCriteria().andEqualTo("chgcid", chgcid);
            List<DchyXmglClcg> clcgList = entityMapper.selectByExample(clcgExample);
            if (CollectionUtils.isNotEmpty(clcgList)) {
                for (DchyXmglClcg xmglClcg : clcgList) {
                    if (null != xmglClcg) {
                        /*?????????wjzxid*/
                        String wjzxid = xmglClcg.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            //??????????????????
                            String clcgmc = xmglClcg.getClcgmc();
                            int i = clcgmc.indexOf(".");
                            String substring = clcgmc.substring(0, i);
                            Example clpzExample = new Example(DchyXmglClcgpz.class);
                            clpzExample.createCriteria().andLike("clmc", substring);
                            List<DchyXmglClcgpz> clpzList = entityMapper.selectByExample(clpzExample);
                            if (CollectionUtils.isNotEmpty(clpzList)) {
                                DchyXmglClcgpz clcgpz = clpzList.get(0);
                                if (null != clcgpz) {
                                    String tempPclcgpzid = clcgpz.getPclcgpzid();
                                    if (StringUtils.equals(clcgpzid, tempPclcgpzid)) {
                                        Node node = getNodeService().getNode(Integer.parseInt(xmglClcg.getWjzxid()));
                                        wjzxids = node.getParentId() + "";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return wjzxids;
    }


    private List<Map<String, Object>> getChxmidByXmid(String chxmid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        Set<String> clsxZd = new HashSet<>();
        /*??????chxmid????????????????????????*/
        Example clsxExample = new Example(DchyXmglChxmClsx.class);
        clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (DchyXmglChxmClsx chxmClsx : clsxList) {
                clsxZd.add(chxmClsx.getClsx());
            }
        }
        List<Map<String, Object>> clsxZdList = contractMapper.getClsxZd(clsxZd);
        if (CollectionUtils.isNotEmpty(clsxZdList)) {
            for (Map<String, Object> clsxMap : clsxZdList) {
                Map<String, Object> returnMap = new LinkedHashMap<>();
                returnMap.put("title", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "MC")));
                returnMap.put("wjzxid", this.getPNodeIdByDm(chxmid, CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "DM"))));//this.getPNodeIdByDm(chxmid, (String) clsxMap.get("DM"))
                returnMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                returnMap.put("fdm", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "DM")));//???????????????
                HashMap<String, Object> param = new HashMap<>();
                param.put("dm", CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "DM")));
                param.put("clsxZd", clsxZd);
                List<DchyXmglZd> concreteClsxZd = contractMapper.getConcreteClsxZd(param);
                String cdm = "";
                if (CollectionUtils.isNotEmpty(concreteClsxZd)) {
                    for (DchyXmglZd xmglZd : concreteClsxZd) {
                        cdm += xmglZd.getDm() + "-";
                    }
                }
                returnMap.put("cdm", cdm);//???????????????
                returnMap.put("type", 0);
                returnMap.put("children", this.getConcreteClsxXx(concreteClsxZd, chxmid));//???????????????????????????????????????
                resultList.add(returnMap);
            }
        }
        return resultList;
    }

    private List<Map<String, Object>> getConcreteClsxXx(List<DchyXmglZd> concreteClsxZd, String chxmid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        try {
            if (CollectionUtils.isNotEmpty(concreteClsxZd)) {
                for (DchyXmglZd xmglZd : concreteClsxZd) {
                    Map<String, Object> clsxMap = new LinkedHashMap<>();
                    clsxMap.put("title", xmglZd.getMc());
                    clsxMap.put("wjzxid", this.getPNodeIdByDm(chxmid, xmglZd.getDm()));
                    clsxMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                    clsxMap.put("dm", xmglZd.getDm());
                    clsxMap.put("type", 0);
                    clsxMap.put("children", this.getClsxFileUpload(chxmid, xmglZd.getDm()));//?????????????????????
                    resultList.add(clsxMap);
                }
            }
            return resultList;
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return null;
    }

    private String getPNodeIdByDm(String chxmid, String dm) {
        String wjzxid = "";
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        String wjzxids = "";
        if (CollectionUtils.isNotEmpty(htxxList)) {
            for (DchyXmglHtxx xmglHtxx : htxxList) {
                String htxxid = xmglHtxx.getHtxxid();
                Example clsxHtxxExample = new Example(DchyXmglClsxHtxxGx.class);
                clsxHtxxExample.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglClsxHtxxGx> clsxHtxxGxes = entityMapper.selectByExample(clsxHtxxExample);
                if (CollectionUtils.isNotEmpty(clsxHtxxGxes)) {
                    for (DchyXmglClsxHtxxGx htxxGx : clsxHtxxGxes) {
                        String clsxid = htxxGx.getClsxid();
                        DchyXmglChxmClsx chxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                        if (null != chxmClsx) {
                            if (dm.length() > 1) {
                                if (StringUtils.equals(dm, chxmClsx.getClsx())) {
                                    wjzxid = xmglHtxx.getWjzxid();
                                    if (StringUtils.isNotBlank(wjzxid)) {
                                        List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                                        if (CollectionUtils.isNotEmpty(nodeList)) {
                                            for (Node node : nodeList) {
                                                wjzxids += "xm" + node.getId() + "-";
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (StringUtils.equals(dm, chxmClsx.getClsx().substring(0, 1))) {
                                    wjzxid = xmglHtxx.getWjzxid();
                                    if (StringUtils.isNotBlank(wjzxid)) {
                                        try {
                                            List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                                            if (CollectionUtils.isNotEmpty(nodeList)) {
                                                for (Node node : nodeList) {
                                                    wjzxids += chxmClsx.getClsx() + node.getId() + "-";
                                                }
                                            }
                                        } catch (Exception e) {
                                            logger.error("????????????:{}", e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return wjzxids;
    }


    private List<Map<String, Object>> getClsxFileUpload(String chxmid, String dm) {
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        String wjzxid = "";
        List<Map<String, Object>> resultList = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(htxxList)) {
            for (DchyXmglHtxx xmglHtxx : htxxList) {

                String htxxid = xmglHtxx.getHtxxid();
                Example clsxHtxxExample = new Example(DchyXmglClsxHtxxGx.class);
                clsxHtxxExample.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglClsxHtxxGx> clsxHtxxGxes = entityMapper.selectByExample(clsxHtxxExample);
                if (CollectionUtils.isNotEmpty(clsxHtxxGxes)) {
                    for (DchyXmglClsxHtxxGx htxxGx : clsxHtxxGxes) {
                        String clsxid = htxxGx.getClsxid();
                        DchyXmglChxmClsx chxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                        if (null != chxmClsx && StringUtils.equals(dm, chxmClsx.getClsx())) {
                            wjzxid = xmglHtxx.getWjzxid();
                            if (StringUtils.isNotBlank(wjzxid)) {
                                List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                                if (CollectionUtils.isNotEmpty(nodeList)) {
                                    for (Node node : nodeList) {
                                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                                        uploadFileMap.put("title", node.getName());
                                        uploadFileMap.put("wjzxid", node.getId());
                                        uploadFileMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                                        uploadFileMap.put("type", node.getType());
                                        uploadFileMap.put("children", this.getUploadFiles(wjzxid, node.getId()));//?????????????????????????????????
                                        resultList.add(uploadFileMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * ????????????????????????
     *
     * @param wjzxid
     * @return
     */
    private List<Map<String, Object>> getUploadFiles(String wjzxid, int parentNodeId) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        int nodeId = Integer.parseInt(wjzxid);
        int num = FileDownoadUtil.getFileNumberByNodeId(parentNodeId);
        try {
            if (1 == num) {
                Node node = getNodeService().getNode(nodeId);
                if (node != null) {
                    if (node.getType() == 1) {//1???????????????  0:??????
                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                        // ??????
                        uploadFileMap.put("title", node.getName());
                        uploadFileMap.put("type", node.getType());
                        uploadFileMap.put("wjzxid", node.getId());
                        uploadFileMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                        resultList.add(uploadFileMap);
                    } else {
                        List<Node> nodeList = getNodeService().getAllChildNodes(nodeId);
                        if (CollectionUtils.isNotEmpty(nodeList)) {
                            for (Node nodeTemp : nodeList) {
                                if (nodeTemp.getType() == 1) {
                                    Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                                    uploadFileMap.put("title", nodeTemp.getName());
                                    uploadFileMap.put("type", nodeTemp.getType());
                                    uploadFileMap.put("wjzxid", nodeTemp.getId());
                                    uploadFileMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                                    resultList.add(uploadFileMap);
                                }
                            }
                        }
                    }
                }
            } else {
                List<Node> nodeList = getNodeService().getAllChildNodes(parentNodeId);
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node nodeTemp : nodeList) {
                        if (nodeTemp.getType() == 1 && (nodeTemp.getParentId().intValue() == parentNodeId)) {
                            Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                            uploadFileMap.put("title", nodeTemp.getName());
                            uploadFileMap.put("type", nodeTemp.getType());
                            uploadFileMap.put("wjzxid", nodeTemp.getId());
                            uploadFileMap.put("cllx", "0");//1:????????????????????????0????????????????????????
                            resultList.add(uploadFileMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return resultList;
    }


    private static String convertCzzt(String czzt) {
        String czztMc = "";
        switch (czzt) {
            case "1":
                czztMc = Constants.DCHY_XMGL_CLSX_CZZT_HF;
                break; //??????
            case "2":
                czztMc = Constants.DCHY_XMGL_CLSX_CZZT_ZT;
                break; //??????
            case "3":
                czztMc = Constants.DCHY_XMGL_CLSX_CZZT_TZ;
                break; //??????
            default:
                break;
        }
        return czztMc;
    }

    /**
     * ??????????????????id????????????????????????
     *
     * @param
     * @return
     */
    private String getChdwmcByChdwxxid(String chdwxxid) {
        if (StringUtils.isNotEmpty(chdwxxid)) {
            DchyXmglChxmChdwxx chdwxx = entityMapper.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
            if (null != chdwxx) {
                return chdwxx.getChdwmc();
            }
        }
        return "";
    }

    /**
     * ????????????clsxid????????????????????????
     *
     * @param
     * @return
     */
    private String getChdwmcByClsxid(String clsx, List chxmidList) {
        if (StringUtils.isNotEmpty(clsx) && CollectionUtils.isNotEmpty(chxmidList)) {
            List<String> chdwmxList = Lists.newArrayList();
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("clsx", clsx);
            paramMap.put("chxmidList", chxmidList);
            List<Map<String, Object>> chdwxxList = dchyXmglClsxHtxxGxMapper.getChdwxxByCLsx(paramMap);
            if (CollectionUtils.isNotEmpty(chdwxxList)) {
                for (Map<String, Object> chdwxxLists : chdwxxList) {
                    String chdwmc = MapUtils.getString(chdwxxLists, "CHDWMC");
                    chdwmxList.add(chdwmc);
                }
                return StringUtils.join(chdwmxList, ",");
            }
        }
        return "";
    }

    /**
     * ??????chxmid????????????????????????
     *
     * @param chxmid
     */
    private Date getRegisterTime(String chxmid) {
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != chxm) {
                return chxm.getFbsj();
            }
        }
        return null;
    }

    /**
     * ??????chgcid??????????????????
     *
     * @param chgcid
     * @return
     */
    private String getSlDhByGcid(String chgcid) {
        if (StringUtils.isNotBlank(chgcid)) {
            DchyXmglChgc xmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
            if (null != xmglChgc) {
                return xmglChgc.getSlbh();
            }
        }
        return "";
    }

    /**
     * ??????????????????????????????
     *
     * @param xmcgzt
     * @return
     */
    private String getXmcgzt(String xmcgzt) {
        if (StringUtils.isNotBlank(xmcgzt)) {
            return dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XMCGZT, xmcgzt).getMc();
        }
        return "?????????";
    }

    private String getClsxParentDm(String clsx) {
        if (StringUtils.isNotBlank(clsx)) {
            return dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, clsx).getFdm();
        }
        return "";
    }

    private Map<String, Object> querySsjdAndClsxByClsxList(List<Map<String, Object>> clsxList) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<String> ssjdMCList = Lists.newArrayList();
        List<String> clsxMCList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (Map<String, Object> map : clsxList) {
                String clsx = MapUtils.getString(map, "CLSX");
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, clsx);
                if (!clsxMCList.contains(dchyXmglZd.getMc())) {
                    clsxMCList.add(dchyXmglZd.getMc());
                    if (!clsxMCList.contains(dchyXmglZd.getDm())) {
                        clsxMCList.add(dchyXmglZd.getMc());
                        clsxMCList.add(dchyXmglZd.getDm());
                    }
                    if (!ssjdMCList.contains(dchyXmglZd.getFdm())) {
                        ssjdMCList.add(dchyXmglZd.getFdm());
                        dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dchyXmglZd.getFdm());
                        ssjdMCList.add(dchyXmglZd.getMc());
                    }
                }
                resultMap.put("clsxMCList", clsxMCList);
                resultMap.put("ssjdMCList", ssjdMCList);
            }
        }
        return resultMap;
    }


    @Override
    public Map<String, Object> generateClml() {
        //???????????????
        List<ErrorInfoModel> fileListPz = Lists.newArrayList();
        Map<String, Object> mlMap = Maps.newHashMap();
        List<DchyXmglClcgpz> dchyXmglClcgpzList1 = dchyXmglClgcpzMapper.queryclcgpz();
        //????????????
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList1)) {
            for (DchyXmglClcgpz dchyXmglClcgpz : dchyXmglClcgpzList1) {
                generateDir(dchyXmglClcgpz, mlMap);
            }
        }

        return mlMap;
    }

    //?????????clcg????????????,??????clsx???????????????
    public static Map<String, Object> formatClml(Map<String, Object> ml) {
        if (MapUtils.isNotEmpty(ml)) {
            for (String key : ml.keySet()) {
                Map<String, Object> yjml = (Map<String, Object>) ml.get(key);
                if (MapUtils.isNotEmpty(yjml)) {
                    for (String key1 : yjml.keySet()) {
                        Map<String, Object> ejml = (Map<String, Object>) yjml.get(key1);
                        if (MapUtils.isNotEmpty(ejml)) {
                            for (String key2 : ejml.keySet()) {
                                List<String> wjmcList = (List<String>) ejml.get(key2);
                                yjml.put(key1, wjmcList);
                            }
                        }
                    }
                }
            }
        }
        return ml;
    }


    @Override
    public Map<String, Object> generateDir(DchyXmglClcgpz dchyXmglClcgpz, Map<String, Object> mlMap) {
        String mlmc = dchyXmglClcgpz.getClmc();
        String clcgpzid = dchyXmglClcgpz.getClcgpzid();
        List<DchyXmglClcgpz> dchyXmglClcgpzList = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid);
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList)) {
            Map<String, Object> mapTemp = Maps.newHashMap();
            List<String> fileList = Lists.newArrayList();
            Map<String, Object> map = Maps.newHashMap();
            for (DchyXmglClcgpz dchyXmglClcgpzTemp : dchyXmglClcgpzList) {
                String mlmcTemp = dchyXmglClcgpzTemp.getClmc();
                fileList.add(mlmcTemp);
                if (mlmcTemp.contains(".") && dchyXmglClcgpzTemp.getClmc().equals(dchyXmglClcgpzList.get(dchyXmglClcgpzList.size() - 1).getClmc())) {
                    map.put(mlmc, fileList);
                } else {
                    map = mapCopy(generateDir(dchyXmglClcgpzTemp, mapTemp));
                }
            }
            mlMap.put(mlmc, map.containsKey(mlmc) ? map.get(mlmc) : map);
        } else {
            mlMap.put(mlmc, "");
        }
        return mlMap;
    }

    /**
     * ??????map??????
     *
     * @param paramsMap ???????????????
     * @return resultMap
     * ??????????????????
     * @explain ???paramsMap??????????????????????????????resultMap??????
     * paramsMap???????????????????????????resultMap???????????????
     */
    public static Map<String, Object> mapCopy(Map<String, Object> paramsMap) {
        Map resultMap = Maps.newHashMap();
        if (MapUtils.isNotEmpty(paramsMap)) {
            Iterator it = paramsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");

            }
            return resultMap;
        } else {
            return null;
        }
    }

    //?????????????????????clcg??????????????????
    public static boolean cgsfqbtj(Map<String, Object> ml, Map<String, Object> clcgMl) {
        boolean flag = false;
        if (MapUtils.isNotEmpty(ml) && MapUtils.isNotEmpty(clcgMl)) {
            for (String key : clcgMl.keySet()) {
                Map<String, Object> ejml = (Map<String, Object>) clcgMl.get(key);
                Map<String, Object> ejmlPz = (Map<String, Object>) ml.get(key);
                if (MapUtils.isNotEmpty(ejml) && MapUtils.isNotEmpty(ejmlPz)) {
                    for (String key1 : ejml.keySet()) {
                        List<String> wjmcList = (List<String>) ejml.get(key1);
                        List<String> wjmcListPz = (List<String>) ejmlPz.get(key1);
                        if (!wjmcList.retainAll(wjmcListPz)) {
                            return false;
                        }
                    }
                }
            }
            flag = true;
        }
        return flag;
    }

    @Override
    public Map<String, Object> generateClcgMl(List<DchyXmglClcg> dchyXmglClcgList) {
        Map<String, Object> clcgMl = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
            for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                //??????clcg?????????????????????????????????????????????????????????
                if (!StringUtils.equals(dchyXmglClcg.getShzt(), Constants.DCHY_XMGL_SHZT_SHTG)) {
                    break;
                }
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dchyXmglClcg.getClsx());
                String fdm = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dchyXmglZd.getFdm());
                String clsxMc = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, dchyXmglZd.getDm());
                String mlmc = dchyXmglClcg.getClcgmc();
                List<String> wjmcList = Lists.newArrayList();
                wjmcList.add(mlmc);

                if (clcgMl.containsKey(fdm)) {
                    Map<String, Object> ejml = (Map<String, Object>) MapUtils.getMap(clcgMl, fdm);

                    if (ejml.containsKey(clsxMc)) {
                        List<String> wjmcListNew = (List<String>) ejml.get(clsxMc);
                        wjmcListNew.addAll(wjmcList);
                        ejml.put(clsxMc, wjmcListNew);
                    } else {
                        ejml.put(clsxMc, wjmcList);
                    }
                } else {
                    Map<String, Object> ejml = Maps.newHashMap();
                    ejml.put(clsxMc, wjmcList);
                    clcgMl.put(fdm, ejml);
                }
            }
        }
        return clcgMl;

    }
}