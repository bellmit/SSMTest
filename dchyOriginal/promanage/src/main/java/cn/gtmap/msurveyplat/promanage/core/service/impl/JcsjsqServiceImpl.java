package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.WfEventDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.HttpClientUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSqxxService;
import cn.gtmap.msurveyplat.promanage.core.service.WorkFlowService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXmlUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/24
 * @description 基础数据申请
 */
@Service("jcsjsqServiceImpl")
public class JcsjsqServiceImpl implements WorkFlowService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private WorkFlowXmlUtil workFlowXmlUtil;
    @Autowired
    private DchyXmglSqxxService dchyXmglSqxxService;
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private DchyXmglJcsjsqService dchyXmglJcsjsqService;

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean lczfcl(WfEventDto wfEventDto) {
        return false;
    }

    @Override
    @Transactional
    public boolean changeSqzt(WfEventDto wfEventDto, String sqzt, String shzt) {
        if (wfEventDto != null && StringUtils.isNoneBlank(wfEventDto.getGzldyid(), wfEventDto.getGzljdid(), wfEventDto.getXmid())) {
            String[] gzldyids = StringUtils.split(wfEventDto.getGzldyid(), ",");
            String[] gzljgids = StringUtils.split(wfEventDto.getGzljdid(), ",");

            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, wfEventDto.getXmid());
            if (dchyXmglSqxx != null) {
                dchyXmglSqxx.setSqzt(sqzt);

                DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, dchyXmglSqxx.getGlsxid());

                if (null == dchyXmglJcsjSqxx) {
                    dchyXmglJcsjSqxx = new DchyXmglJcsjSqxx();
                    dchyXmglJcsjSqxx.setJcsjsqxxid(dchyXmglSqxx.getGlsxid());
                }
                dchyXmglJcsjSqxx.setDqzt(shzt);

                entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                entityMapper.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());

                //初始化进度信息
                DchyXmglJcsjsqDto dchyXmglJcsjsqDto = new DchyXmglJcsjsqDto();
                DchyXmglJcsjJdxx dchyXmglJcsjJdxx = new DchyXmglJcsjJdxx();
                dchyXmglJcsjJdxx.setJdxxid(UUIDGenerator.generate18());
                dchyXmglJcsjJdxx.setJcsjsqxxid(wfEventDto.getXmid());
                dchyXmglJcsjJdxx.setBjry(UserUtil.getCurrentUser().getUsername());
                dchyXmglJcsjJdxx.setBjryid(UserUtil.getCurrentUserId());
                dchyXmglJcsjJdxx.setBjsj(CalendarUtil.getCurHMSDate());
                //制作的时候推送  sqxx,基础数据sqxx,进度信息
                if (StringUtils.equals(shzt, Constants.DCHY_XMGL_JCSJSQ_DQSQZT_DJF)) {
                    dchyXmglJcsjJdxx.setHj(Constants.DCHY_XMGL_JCSJSQ_HJ_ZZ);
                    int flag = entityMapper.saveOrUpdate(dchyXmglJcsjJdxx, dchyXmglJcsjJdxx.getJdxxid());
                    if (flag > 0) {
                        dchyXmglJcsjsqDto.setDchyXmglJcsjJdxx(dchyXmglJcsjJdxx);
                        dchyXmglJcsjsqDto.setDchyXmglJcsjSqxx(dchyXmglJcsjSqxx);
                        dchyXmglJcsjsqDto.setDchyXmglSqxx(dchyXmglSqxx);

                        pushDataToMqService.pushJcsjResultTo(dchyXmglJcsjsqDto);

                    }
                    //交付的时候推送  sqxx,基础数据sqxx,进度信息,交付日志记录信息
                } else if (StringUtils.equals(shzt, Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YJF)) {
                    dchyXmglJcsjJdxx.setHj(Constants.DCHY_XMGL_JCSJSQ_HJ_JF);
                    int flag = entityMapper.saveOrUpdate(dchyXmglJcsjJdxx, dchyXmglJcsjJdxx.getJdxxid());
                    if (flag > 0) {
                        //获取操作日志
                        Example exampleCzrz = new Example(DchyXmglCzrz.class);
                        exampleCzrz.createCriteria().andEqualTo("glsxid", wfEventDto.getXmid());
                        List<DchyXmglCzrz> dchyXmglCzrzList = entityMapper.selectByExample(exampleCzrz);
                        if (CollectionUtils.isNotEmpty(dchyXmglCzrzList)) {
                            dchyXmglJcsjsqDto.setDchyXmglCzrzList(dchyXmglCzrzList);
                        }

                        dchyXmglJcsjsqDto.setDchyXmglJcsjJdxx(dchyXmglJcsjJdxx);
                        dchyXmglJcsjsqDto.setDchyXmglJcsjSqxx(dchyXmglJcsjSqxx);
                        dchyXmglJcsjsqDto.setDchyXmglSqxx(dchyXmglSqxx);
                        pushDataToMqService.pushJcsjResultTo(dchyXmglJcsjsqDto);
                    }
                }
                //数据制作完提醒测绘单位领取基础数据
                if (StringUtils.equals(shzt, Constants.DCHY_XMGL_JCSJSQ_DQSQZT_DJF)) {
                    dchyXmglJcsjsqService.jcsjsqXxtx(wfEventDto.getXmid(), Constants.DCHY_XMGL_ZD_XXNR_JCSJZZWC);
                } else if (StringUtils.equals(shzt, Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YJF)) {
                    dchyXmglJcsjsqService.jcsjsqXxtx(wfEventDto.getXmid(), Constants.DCHY_XMGL_ZD_XXNR_JCSJJFWC);
                }
            }
        }
        return false;
    }

    @Override
    public DchyXmglSqxx initDchySqxx(InitDataParamDTO initDataParamDTO) {
        return dchyXmglSqxxService.initSqxx(initDataParamDTO);
    }

    @Override
    public boolean deleteDchySqxx(String gzldyid, String xmid) {
        logger.info("delXmid-- " + xmid);
        //1.删除申请信息
        //2.删除成果信息
        //3.删除成果提交记录
        //4.删除收件信息
        //5.删除收件材料
        //6.删除文件中心文件信息
        return false;
    }

    @Override
    public boolean qrbzdbj(WfEventDto wfEventDto) throws IOException {
        boolean yxbj = false;
        if (null != wfEventDto && StringUtils.isNotBlank(wfEventDto.getXmid())) {
            DchyXmglJcsjsqDto dchyXmglJcsjsqDto = new DchyXmglJcsjsqDto();
            String xmid = wfEventDto.getXmid();
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);
            dchyXmglJcsjsqDto.setDchyXmglSqxx(dchyXmglSqxx);
            String glsxid = dchyXmglSqxx.getGlsxid();
            DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, glsxid);
            //线下基础申请办结时候,推送数据到线上库
            if (StringUtils.equals(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YJF, dchyXmglJcsjSqxx.getDqzt()) || StringUtils.equals(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YTH, dchyXmglJcsjSqxx.getDqzt())) {
                yxbj = true;
                if (yxbj) {
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowXmlUtil.getWorkFlowIntanceService().getWorkflowInstanceByProId(xmid);
                    List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                    String portal = AppConfig.getProperty("portal.url") + "/index/endTask?wiid=" + pfWorkFlowInstanceVo.getWorkflowIntanceId() + "&taskid=" + pfTaskVoList.get(0).getTaskId();
                    String response = HttpClientUtil.sendHttpClient(portal);
                    logger.info("自动办结返回信息:" + response);
                }

                if (StringUtils.equals(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YTH, dchyXmglJcsjSqxx.getDqzt())) {
                    dchyXmglJcsjsqService.jcsjsqXxtx(xmid, Constants.DCHY_XMGL_ZD_XXNR_JCSJSHBTG);
                }
            }
        }
        return yxbj;
    }

    @Override
    @Transactional
    public boolean completeDchySqxx(WfEventDto wfEventDto) {
        return false;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已提交成果文件
     */
    @Override
    public Map<String, Object> checkYxzf(WfEventDto wfEventDto, String yzfs) {
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.equalsIgnoreCase(yzfs, "checkCgytj")) {
            resultMap = checkCgytj(wfEventDto);
        } else if (StringUtils.equalsIgnoreCase(yzfs, "checkShxxytx")) {
            resultMap = checkCgytj(wfEventDto);
        } else {
            resultMap.put("message", "转发验证事项为空");
            resultMap.put("tabName", "");
        }
        return resultMap;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已填写审核信息
     */
    private Map<String, Object> checkCgytj(WfEventDto wfEventDto) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            if (wfEventDto != null && StringUtils.isNotBlank(wfEventDto.getXmid())) {
                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                String glsxid = dchyXmglSqxx.getGlsxid();
                DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, glsxid);
                if (null == dchyXmglJcsjSqxx) {
                    resultMap.put("message", "基础数据流程尚未申请");
                    resultMap.put("tabName", "基础数据申请");
                }
            } else {
                resultMap.put("message", "基础数据申请流程不存在");
            }
        } catch (Exception e) {
            resultMap.put("message", "转发验证失败");
        }
        return resultMap;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已填写审核信息
     */
    private Map<String, Object> checkShxxytx(WfEventDto wfEventDto) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            if (wfEventDto != null && StringUtils.isNotBlank(wfEventDto.getXmid())) {
                PfActivityVo pfActivityVo = sysTaskService.getActivity(wfEventDto.getAdid());
                Example example = new Example(DchyXmglClcgShjl.class);
                example.createCriteria().andEqualTo("sqxxid", wfEventDto.getXmid()).andEqualTo("shjdmc", pfActivityVo.getActivityName()).andIsNotNull("shsj");
                int num = entityMapper.countByExample(example);
                if (num == 0) {
                    resultMap.put("message", "审核信息未提交");
                    resultMap.put("tabName", "成果审核填写");
                }
            } else {
                resultMap.put("message", "成果提交项目信息为空");
            }
        } catch (Exception e) {
            resultMap.put("message", "转发验证失败");
        }
        return resultMap;
    }

    private Map<String, Object> queryFilesbyWjzxid(String glsxid, int wjzxid) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(glsxid, Base64.getEncoder().encodeToString((byte[]) FileDownoadUtil.downLoadFile(wjzxid).get("wjnr")));
        map.put("wjmc", FileDownoadUtil.downLoadFile(wjzxid).get("wjmc"));
        return map;
    }

}