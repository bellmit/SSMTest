package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjfxDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjgxFyDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.mapper.DchyTjfxFromXmglMapper;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.gtis.common.util.UUIDGenerator;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 接收来自线下的统计分析数据的参数
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveTjfxFromXmglServiceImpl implements ChannelAwareMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveTjfxFromXmglServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Autowired
    private DchyTjfxFromXmglMapper dchyTjfxFromXmglMapper;

    @Autowired
    private Repository repository;

    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_TJFX_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            LOGGER.info("**********线下统计分析参数数据--消息接收成功**********");
            LOGGER.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_TJFX_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveTjfx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            LOGGER.info("**********线下统计分析参数数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_TJFX_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            LOGGER.error("消息接收失败", e);
        }
    }

    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            LOGGER.error("通知服务器移除mq时异常，异常信息：", e);
        }
    }

    //处理异常，mq重回队列
    private void basicNACK(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            LOGGER.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }

    //处理异常，mq重回队列
    private void basicReject(Message message, Channel channel) {
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            LOGGER.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }


    //统计分析数据推送
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTjfx(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_TJFX_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_TJFX_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglTjfxDto dchyXmglTjfxDto = JSON.toJavaObject(slxxJson, DchyXmglTjfxDto.class);
            String key = dchyXmglTjfxDto.getKey();
            String kssj = dchyXmglTjfxDto.getKssj();
            String jssj = dchyXmglTjfxDto.getJssj();
            String wtkssj = dchyXmglTjfxDto.getWtkssj();
            String wtjssj = dchyXmglTjfxDto.getWtjssj();
            String year = dchyXmglTjfxDto.getYear();
            String month = dchyXmglTjfxDto.getMonth();
            String quarter = dchyXmglTjfxDto.getQuarter();//季度
            String jsdwmc = dchyXmglTjfxDto.getJsdwmc();
            String chdwmc = dchyXmglTjfxDto.getChdwmc();
            String wtzt = dchyXmglTjfxDto.getWtzt();
            String exportflag = dchyXmglTjfxDto.getExportflag();
            String pageflag = dchyXmglTjfxDto.getPageflag();
            DchyXmglTjgxFyDto dchyXmglTjgxFyDto = dchyXmglTjfxDto.getDchyXmglTjgxFyDto();
            Map<String, Object> paramMap = new HashMap<>();
            Map<String, Object> exportParamMap = new HashMap<>();
            if (StringUtils.isNotBlank(kssj)) {
                paramMap.put("kssj", kssj);
            }
            if (StringUtils.isNotBlank(jssj)) {
                paramMap.put("jssj", jssj);
            }
            if (StringUtils.isNotBlank(wtkssj)) {
                paramMap.put("wtkssj", wtkssj);
            }
            if (StringUtils.isNotBlank(wtjssj)) {
                paramMap.put("wtjssj", wtjssj);
            }
            if (StringUtils.isNotBlank(year)) {
                paramMap.put("year", year);
                exportParamMap.put("year", year);
            }
            if (StringUtils.isNotBlank(jsdwmc)) {
                paramMap.put("jsdwmc", jsdwmc);
                exportParamMap.put("jsdwmc", jsdwmc);
            }
            if (StringUtils.isNotBlank(chdwmc)) {
                paramMap.put("chdwmc", chdwmc);
                exportParamMap.put("chdwmc", chdwmc);
            }
            if (StringUtils.isNotBlank(wtzt)) {
                paramMap.put("wtzt", wtzt);
            }
            paramMap.put("month",month);//月份
            String ksjd = "";//开始季度
            String jsjd = "";//结束季度
            if(StringUtils.isNotBlank(quarter)){//季度
                if(StringUtils.equals("1",quarter)){
                    ksjd = "01";
                    jsjd = "03";
                }
                else if(StringUtils.equals("2",quarter)){
                    ksjd = "04";
                    jsjd = "06";
                }
                else if(StringUtils.equals("3",quarter)){
                    ksjd = "07";
                    jsjd = "09";
                }
                else if(StringUtils.equals("4",quarter)){
                    ksjd = "10";
                    jsjd = "12";
                }
            }
            if(StringUtils.isNotBlank(ksjd) && StringUtils.isNotBlank(jsjd)){
                paramMap.put("ksjd",ksjd);
                paramMap.put("jsjd",jsjd);
            }

            List<Map<String, Object>> xmslbyqxList = dchyTjfxFromXmglMapper.getXmslByqxList(paramMap);
            List<Map<String, Object>> xmslbyqxlysList = dchyTjfxFromXmglMapper.getXmslByqxlysList(paramMap);
            dchyXmglTjfxDto.setXmslbylysList(xmslbyqxlysList);
            dchyXmglTjfxDto.setXmslbyqxList(xmslbyqxList);
            if (StringUtils.isBlank(exportflag) && "false".equals(pageflag)) {
                if (dchyXmglTjgxFyDto != null) {
                    int page = dchyXmglTjgxFyDto.getPage();
                    int pageSize = dchyXmglTjgxFyDto.getPageSize();
                    Page<Map<String, Object>> wtjlbypageList = repository.selectPaging("getWtjlByPage", paramMap, page - 1, pageSize);
                    dchyXmglTjgxFyDto.setRows(wtjlbypageList.getContent());
                    dchyXmglTjgxFyDto.setTotalPages(wtjlbypageList.getTotalPages());
                    dchyXmglTjgxFyDto.setTotalElements(wtjlbypageList.getTotalElements());
                    dchyXmglTjfxDto.setDchyXmglTjgxFyDto(dchyXmglTjgxFyDto);
                }
                //根据kssj,jssj获取返回结果*******************************
                List<Map<String, Object>> xmslbyyearList = dchyTjfxFromXmglMapper.getXmslByYear(paramMap);
                List<Map<String, Object>> xmslbymouthList = dchyTjfxFromXmglMapper.getXmslByMouth(paramMap);
                List<Map<String, Object>> xmslbywtztList = dchyTjfxFromXmglMapper.getXmslByWtzt(paramMap);
                List<Map<String, Object>> xmslbyqxmcandchjdList = dchyTjfxFromXmglMapper.getXmslByQxmcAndChjd(paramMap);
                List<Map<String, Object>> xmslbychjdList = dchyTjfxFromXmglMapper.getXmslByChjd(paramMap);
                List<Map<String, Object>> jsdwList = dchyTjfxFromXmglMapper.getJsdw();
                List<Map<String, Object>> chdwList = dchyTjfxFromXmglMapper.getChdw();
                dchyXmglTjfxDto.setXmslbyyearList(xmslbyyearList);
                dchyXmglTjfxDto.setXmslbymouthList(xmslbymouthList);
                dchyXmglTjfxDto.setXmslbywtztList(xmslbywtztList);
                dchyXmglTjfxDto.setXmslbyqxmcandchjdList(xmslbyqxmcandchjdList);
                dchyXmglTjfxDto.setXmslbychjdList(xmslbychjdList);
                dchyXmglTjfxDto.setJsdwList(jsdwList);
                dchyXmglTjfxDto.setChdwList(chdwList);
            } else if (StringUtils.isNotBlank(exportflag)) {
                List<Map<String, Object>> exportDataList = dchyTjfxFromXmglMapper.getWtjlByPage(exportParamMap);
                dchyXmglTjfxDto.setExportwtjlList(exportDataList);
            } else if ("true".equals(pageflag)) {
                if (dchyXmglTjgxFyDto != null) {
                    int page = dchyXmglTjgxFyDto.getPage();
                    int pageSize = dchyXmglTjgxFyDto.getPageSize();
                    Page<Map<String, Object>> wtjlbypageList = repository.selectPaging("getWtjlByPage", paramMap, page - 1, pageSize);
                    dchyXmglTjgxFyDto.setRows(wtjlbypageList.getContent());
                    dchyXmglTjgxFyDto.setTotalPages(wtjlbypageList.getTotalPages());
                    dchyXmglTjgxFyDto.setTotalElements(wtjlbypageList.getTotalElements());
                    dchyXmglTjfxDto.setDchyXmglTjgxFyDto(dchyXmglTjgxFyDto);
                }
            }
            pushDataToMqService.pushTjfxMsgToMq(dchyXmglTjfxDto);
        } catch (Exception e) {
            LOGGER.error("数据库入库失败:", e);
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            throw new Exception();
        }
    }


}
