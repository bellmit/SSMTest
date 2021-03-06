package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2018/1/26
 * @description 接收来自受理云平台的领导催办信息 发布项目,推送从业人员信息包含名录库
 */

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.service.impl.CommissionFilingServiceImpl;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.utils.SlbhUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description
 * @time 2020/11/27 15:07
 */
@Service
public class ReceiveXsxmxxFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;
    @Autowired
    private PlatformUtil platformUtil;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    CommissionFilingServiceImpl commissionFilingServiceImpl;
    @Autowired
    ReceiveXsxmwjFromBsdtServiceImpl receiveXsxmwjFromBsdtServiceImpl;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_XMFB_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上项目信息文件数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_XMFB_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveOrDeleteXxData(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上项目信息文件数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_XMFB_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            logger.error("消息接收失败", e);
        }
    }

    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("通知服务器移除mq时异常，异常信息：" + e);
        }
    }

    //处理异常，mq重回队列
    private void basicNACK(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            logger.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }

    //处理异常，mq重回队列
    private void basicReject(Message message, Channel channel) {
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrDeleteXxData(String str) {

        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_XMFB_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_XMFB_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmDto dchyXmglChxmDto = JSON.toJavaObject(slxxJson, DchyXmglChxmDto.class);

            DchyXmglChxmDto xsxmxxModelSaveOrUpdate = new DchyXmglChxmDto();
            DchyXmglChxmDto xsxmxxModelDelete = new DchyXmglChxmDto();

            String czlx = dchyXmglChxmDto.getCzlx();
            //根据表示字段判断是新增还是删除
            if (StringUtils.equals(czlx, Constants.DCHY_XMGL_SJTS_CZLX_DEL)) {
                xsxmxxModelDelete = dchyXmglChxmDto;
            } else {
                xsxmxxModelSaveOrUpdate = dchyXmglChxmDto;
            }
//            if (xsxmxxModelSaveOrUpdate != null) {
//                logger.info(xsxmxxModelSaveOrUpdate.toString());
//            }
            //保存的数据
            if (xsxmxxModelSaveOrUpdate != null) {
                DchyXmglChgc chgc = xsxmxxModelSaveOrUpdate.getDchyXmglChgc();
                DchyXmglChxm chxm = xsxmxxModelSaveOrUpdate.getDchyXmglChxm();
                List<DchyXmglSjcl> xmglSjclList = xsxmxxModelSaveOrUpdate.getDchyXmglSjclList();
                List<DchyXmglSjxx> xmglSjxxList = xsxmxxModelSaveOrUpdate.getDchyXmglSjxxList();
                List<DchyXmglClsxChdwxxGx> clsxChdwxxGxList = xsxmxxModelSaveOrUpdate.getDchyXmglClsxChdwxxGxList();
                List<DchyXmglChxmClsx> xmglClsxList = xsxmxxModelSaveOrUpdate.getDchyXmglChxmClsxList();
                List<DchyXmglChxmChdwxx> xmglChxmChdwxxList = xsxmxxModelSaveOrUpdate.getDchyXmglChxmChdwxxList();
                List<DchyXmglSqxx> xmglSqxxList = xsxmxxModelSaveOrUpdate.getDchyXmglSqxxList();
                List<DchyXmglClsxChtl> xmglChtlList = xsxmxxModelSaveOrUpdate.getDchyXmglClsxChtlList();

                if (chxm != null) {
                    /*测绘项目*/
                    String slbh = null;
                    if (StringUtils.isNotEmpty(chxm.getSlbh())) {
                        slbh = chxm.getSlbh();
                    } else {
                        slbh = SlbhUtil.generateSlbh(null, null);
                    }
                    if (slbh != null) {
                        chxm.setSlbh(slbh);
                        logger.info("***************受理编号****************" + slbh);
                    }
                    entityMapper.saveOrUpdate(chxm, chxm.getChxmid());
                    logger.info("************测绘项目入库成功");

                    /*sjcl*/
                    if (CollectionUtils.isNotEmpty(xmglSjclList)) {
                        for (DchyXmglSjcl sjclList : xmglSjclList) {
                            if (StringUtils.isNotEmpty(sjclList.getSjclid())) {
                                entityMapper.saveOrUpdate(sjclList, sjclList.getSjclid());
                                logger.info("************多测合一测绘收件材料入库成功");
                            }
                        }
                    }

                    /*sjxx*/
                    if (CollectionUtils.isNotEmpty(xmglSjxxList)) {
                        for (DchyXmglSjxx sjxx : xmglSjxxList) {
                            if (StringUtils.isNotEmpty(sjxx.getSjxxid())) {
                                entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
                                logger.info("************多测合一测绘收件信息入库成功");
                            }
                        }
                    }

                    /*chgc*/
                    if (chgc != null) {
                        //测绘工程  项目来源为1的才可以更新
                        if (StringUtils.isNoneBlank(chgc.getChgcid())) {
                            if (StringUtils.equals(chgc.getXmly(), Constants.XMLY_XSFB)) {
                                entityMapper.saveOrUpdate(chgc, chgc.getChgcid());
                                logger.info("************测绘工程入库成功");
                            }
                        }
                    }

                    /*clsxChdwxxGx*/
                    if (CollectionUtils.isNotEmpty(clsxChdwxxGxList)) {
                        for (DchyXmglClsxChdwxxGx clsxChdwxxGx : clsxChdwxxGxList) {
                            if (StringUtils.isNoneBlank(clsxChdwxxGx.getGxid())) {
                                entityMapper.saveOrUpdate(clsxChdwxxGx, clsxChdwxxGx.getGxid());
                                logger.info("************多测合一测量事项与测绘单位关系入库成功");
                            }
                        }
                    }

                    /*chxmClsx*/
                    if (CollectionUtils.isNotEmpty(xmglClsxList)) {
                        for (DchyXmglChxmClsx chxmClsx : xmglClsxList) {
                            if (StringUtils.isNoneBlank(chxmClsx.getClsxid())) {
                                entityMapper.saveOrUpdate(chxmClsx, chxmClsx.getClsxid());
                                logger.info("************多测合一测绘项目测量事项入库成功");
                            }
                        }
                    }

                    /*ClsxChtl*/
                    if (CollectionUtils.isNotEmpty(xmglChtlList)) {
                        for (DchyXmglClsxChtl clsxChtl : xmglChtlList) {
                            if (StringUtils.isNoneBlank(clsxChtl.getChtlid())) {
                                entityMapper.saveOrUpdate(clsxChtl, clsxChtl.getChtlid());
                                logger.info("************多测合一测绘体量入库成功");
                            }
                        }
                    }

                    /*chxmChdwxx*/
                    if (CollectionUtils.isNotEmpty(xmglChxmChdwxxList)) {
                        for (DchyXmglChxmChdwxx chxmChdwxx : xmglChxmChdwxxList) {
                            if (StringUtils.isNoneBlank(chxmChdwxx.getChdwxxid())) {
                                entityMapper.saveOrUpdate(chxmChdwxx, chxmChdwxx.getChdwxxid());
                                logger.info("************多测合一测绘项目测绘单位信息入库成功");
                            }
                        }
                    }

                    /*sqxx*/
                    if (CollectionUtils.isNotEmpty(xmglSqxxList)) {
                        for (DchyXmglSqxx xmglSqxx : xmglSqxxList) {
                            if (null != xmglSqxx) {
                                entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
                                logger.info("************申请信息入库成功");
                            }
                        }
                        for (DchyXmglSqxx dchyXmglSqxx : xmglSqxxList) {
                            String chxmid = dchyXmglSqxx.getGlsxid();
                            String sqxxid = dchyXmglSqxx.getSqxxid();
                            String zrryid = dchyXmglSqxx.getSqr();
                            Map paramMap = Maps.newHashMap();
                            paramMap.put("sqxxid", sqxxid);
                            paramMap.put("zrryid", zrryid);
                            paramMap.put("sqzt", Constants.DCHY_XMGL_SQZT_DSH);
                            Map resultMap = this.insertDbrw(paramMap);
                            logger.info("************待办任务信息入库成功");
                        }
                    }
                }

                /*合同信息dto*/
                List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = xsxmxxModelSaveOrUpdate.getDchyXmglHtxxDtoList();
                if (CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                    for (DchyXmglHtxxDto dchyXmglHtxxDto : dchyXmglHtxxDtoList) {
                        /*线上备案时推送到线下的数据及文件信息*/
                        if (null != dchyXmglHtxxDto) {
                            /*合同信息*/
                            List<DchyXmglHtxx> htxxList = dchyXmglHtxxDto.getDchyXmglHtxxList();
                            /*测量事项与测绘单位关系*/
                            List<DchyXmglClsxChdwxxGx> clsxChdwxxGxes = dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList();
                            /*合同与测绘单位关系*/
                            List<DchyXmglHtxxChdwxxGx> htxxChdwxxGxes = dchyXmglHtxxDto.getDchyXmglHtxxChdwxxGxList();
                            /*测量事项与合同关系*/
                            List<DchyXmglClsxHtxxGx> clsxHtxxGxes = dchyXmglHtxxDto.getDchyXmglClsxHtxxGxList();
                            /*收件材料*/
                            List<DchyXmglSjcl> sjclList1 = dchyXmglHtxxDto.getDchyXmglSjclList();
                            /*收件信息*/
                            List<DchyXmglSjxx> sjxxList = dchyXmglHtxxDto.getDchyXmglSjxxList();

                            if (CollectionUtils.isNotEmpty(xmglClsxList)) {
                                for (DchyXmglChxmClsx chxmClsx : xmglClsxList) {
                                    if (StringUtils.isNotEmpty(chxmClsx.getClsxid())) {
                                        entityMapper.saveOrUpdate(chxmClsx, chxmClsx.getClsxid());
                                        logger.info("************测绘项目与测量事项入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(htxxList)) {
                                for (DchyXmglHtxx htxx : htxxList) {
                                    if (StringUtils.isNotEmpty(htxx.getHtxxid())) {
                                        entityMapper.saveOrUpdate(htxx, htxx.getHtxxid());
                                        logger.info("************测绘项目合同信息入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(clsxChdwxxGxes)) {
                                for (DchyXmglClsxChdwxxGx chdwxxGx : clsxChdwxxGxes) {
                                    if (StringUtils.isNotEmpty(chdwxxGx.getGxid())) {
                                        entityMapper.saveOrUpdate(chdwxxGx, chdwxxGx.getGxid());
                                        logger.info("************测绘项目与测绘单位有关系入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(htxxChdwxxGxes)) {
                                for (DchyXmglHtxxChdwxxGx chdwxxGx : htxxChdwxxGxes) {
                                    if (StringUtils.isNotEmpty(chdwxxGx.getGxid())) {
                                        entityMapper.saveOrUpdate(chdwxxGx, chdwxxGx.getGxid());
                                        logger.info("************测绘合同与测绘单位关系入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(clsxHtxxGxes)) {
                                for (DchyXmglClsxHtxxGx htxxGx : clsxHtxxGxes) {
                                    if (StringUtils.isNotEmpty(htxxGx.getGxid())) {
                                        entityMapper.saveOrUpdate(htxxGx, htxxGx.getGxid());
                                        logger.info("************测绘事项与合同关系入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(sjclList1)) {
                                for (DchyXmglSjcl sjcl : sjclList1) {
                                    if (StringUtils.isNotEmpty(sjcl.getSjclid())) {
                                        entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                                        logger.info("************收件材料入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(sjxxList)) {
                                for (DchyXmglSjxx sjxx : sjxxList) {
                                    if (StringUtils.isNotEmpty(sjxx.getSjxxid())) {
                                        entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
                                        logger.info("************收件信息入库成功");
                                    }
                                }
                            }
                        }
                    }
                }

                /*根据需求发布编号判断文件是否先一步保存到数据库*/
                if (StringUtils.isNotEmpty(chxm.getXqfbbh())) {
                    Example example = new Example(DchyXmglMqTswj.class);
                    example.createCriteria().andEqualTo("xqfbbh", chxm.getXqfbbh());
                    List<DchyXmglMqTswj> dchyXmglMqTswjList = entityMapper.selectByExampleNotNull(example);
                    if (CollectionUtils.isNotEmpty(dchyXmglMqTswjList)) {
                        //文件先存到数据库
                        for (DchyXmglMqTswj dchyXmglMqTswjLists : dchyXmglMqTswjList) {
                            String wjnr = new String(dchyXmglMqTswjLists.getWjnr(), "UTF-8");
                            //上传文件
                            receiveXsxmwjFromBsdtServiceImpl.saveOrDeleteWjData(wjnr);
                        }
                    }
                }

                /*自动备案*/
                String autoFiling = AppConfig.getProperty(Constants.DCHY_XMGL_ZDBA_PZ);
                if (StringUtils.isNotEmpty(autoFiling)) {
                    logger.info("************************************自动备案**************************************");
                    if (StringUtils.equals(autoFiling.trim(), "true")) {
                        Map map = Maps.newHashMap();
                        //推送时只包含一个测绘项目
                        if (chxm != null) {
                            map.put("chxmid", chxm.getChxmid());
                        }
                        if (chgc != null) {
                            map.put("gcmc", chgc.getGcmc());
                            map.put("wtdw", chgc.getWtdw());
                        }
                        //通过
                        map.put("sftg", Constants.SHTG);
                        boolean result = commissionFilingServiceImpl.reviewCommission(map);
                    }
                }

            }

            //删除的数据
            if (xsxmxxModelDelete != null) {
                DchyXmglChgc dchyXmglChgcListDelete = xsxmxxModelDelete.getDchyXmglChgc();
                DchyXmglChxm dchyXmglChxmListDelete = xsxmxxModelDelete.getDchyXmglChxm();
                List<DchyXmglSjcl> dchyXmglSjclListDelete = xsxmxxModelDelete.getDchyXmglSjclList();
                List<DchyXmglSjxx> dchyXmglSjxxListDelete = xsxmxxModelDelete.getDchyXmglSjxxList();
                Map<String, String> deletefile = xsxmxxModelDelete.getDeletefile();

                if (dchyXmglChxmListDelete != null) {

                    if (!Constants.DCHY_XMGL_CHXM_XMZT_YBJ.equals(dchyXmglChxmListDelete.getXmzt())) {
                        String chxmid = dchyXmglChxmListDelete.getChxmid();

                        Example example = new Example(DchyXmglChxmClsx.class);
                        example.createCriteria().andEqualTo("chxmid", chxmid);
                        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(example);
                        for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                            entityMapper.deleteByPrimaryKey(DchyXmglChxmClsx.class, dchyXmglChxmClsx.getClsxid());
                        }
                        entityMapper.deleteByPrimaryKey(DchyXmglChxm.class, chxmid);
                    }
                }

                if (dchyXmglChgcListDelete != null) {

                    //测绘工程  项目来源为1的才可以更新
                    if (StringUtils.equals(dchyXmglChgcListDelete.getXmly(), Constants.XMLY_XSFB)) {
                        String chgcbh = dchyXmglChgcListDelete.getGcbh();

                        Example example = new Example(DchyXmglChgc.class);
                        example.createCriteria().andEqualTo("gcbh", chgcbh);
                        List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(example);

                        //如果该测绘工程没有关联测绘项目时才可以删除
                        if (CollectionUtils.isEmpty(dchyXmglChxmList)) {
                            entityMapper.deleteByExample(DchyXmglChgc.class, example);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglSjclListDelete)) {
                    for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclListDelete) {
                        entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, dchyXmglSjcl.getSjclid());
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglSjxxListDelete)) {
                    for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxListDelete) {
                        entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, dchyXmglSjxx.getSjxxid());
                    }
                }

                /*删除的文件数据*/
                if (MapUtils.isNotEmpty(deletefile)) {
                    String ssmkid = MapUtils.getString(deletefile, "ssmkid");
                    /*删除线上推送至线下的(委托，核验的文件信息)*/
                    if (StringUtils.isNotBlank(ssmkid) && (StringUtils.equals("18", ssmkid) || StringUtils.equals("19", ssmkid))) {
                        if (deletefile.containsKey("glsxid")) {//chxmid
                            String glsxid = MapUtils.getString(deletefile, "glsxid");
                            Example sjxxExample = new Example(DchyXmglSjxx.class);
                            sjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
                            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
                            if (CollectionUtils.isEmpty(sjxxList)) {
                                DchyXmglSjxx sjxx = sjxxList.get(0);
                                if (null != sjxx) {
                                    /*删除对应收件信息*/
                                    entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, sjxx.getSjxxid());
                                    /*删除对应收件材料*/
                                    Example sjclExample = new Example(DchyXmglSjcl.class);
                                    sjclExample.createCriteria().andEqualTo("sjxxid", sjxx.getSjxxid());
                                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                                    if (CollectionUtils.isEmpty(sjclList)) {
                                        for (DchyXmglSjcl sjcl : sjclList) {
                                            if (StringUtils.isNotBlank(sjcl.getWjzxid())) {
                                                /*删除对应文件中心*/
                                                platformUtil.deleteNodeById(Integer.parseInt(sjcl.getWjzxid()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            dchyXmglMqCzrz.setSbyy(e.getMessage());

            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            logger.error("接收失败原因:{}", e);
        }

    }


    private void delFile(String sjclid) {
        DchyXmglSjcl dchyXmglSjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
        String wjzxid = dchyXmglSjcl.getWjzxid();
        if (StringUtils.isNotBlank(wjzxid)) {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxid));
        }
        if (StringUtils.isNotBlank(sjclid)) {
            //删除收件材料信息
            int sjclResult = entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclid);
            if (sjclResult > 0) {
                logger.info("************多测合一收件材料删除成功");
            }

            // 删除收件信息
            Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
            dchyXmglSjxx.createCriteria().andEqualTo("glsxid", sjclid);
            int sjxxResult = entityMapper.deleteByExampleNotNull(dchyXmglSjxx);
            if (sjxxResult > 0) {
                logger.info("************多测合一收件信息删除成功");
            }
        }
    }

    public Map insertDbrw(Map<String, Object> paramMap) {
        String zrryid = CommonUtil.formatEmptyValue(paramMap.get("zrryid"));
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("sqxxid"));
        String sqzt = CommonUtil.formatEmptyValue(paramMap.get("sqzt"));
        Map map = Maps.newHashMap();
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        if (StringUtils.isNoneEmpty(zrryid, sqxxid)) {
            Example example = new Example(DchyXmglDbrw.class);
            example.createCriteria().andEqualTo("sqxxid", sqxxid);
            List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglDbrwList)) {
                msg = "该事项已申请";
            } else {
                DchyXmglDbrw dchyXmglDbrw = new DchyXmglDbrw();
                dchyXmglDbrw.setDbrwid(UUIDGenerator.generate18());
                dchyXmglDbrw.setSqxxid(sqxxid);
                dchyXmglDbrw.setZrryid(zrryid);
                dchyXmglDbrw.setZrsj(new Date());
                dchyXmglDbrw.setDqjd(Constants.DQJD_SH);
                entityMapper.insertSelective(dchyXmglDbrw);

                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                dchyXmglSqxx.setSqxxid(sqxxid);
                dchyXmglSqxx.setSqzt(sqzt);

                entityMapper.updateByPrimaryKeySelective(dchyXmglSqxx);
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
            }
        }
        map.put("msg", msg);
        map.put("code", code);
        return map;
    }

    private Map<String, Object> uploadWjcl(List<Map<String, String>> wjList, String mainId, String clmc) throws IOException {
        Map<String, Object> objectMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(wjList)) {
            MultipartFile[] files = new MultipartFile[wjList.size()];
            int i = 0;
            for (Map<String, String> map : wjList) {
                // 文件名--文件内容
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    System.out.println(base64);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    MockMultipartFile file = new MockMultipartFile(key, key, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                    byte[] byte2 = file.getBytes();
                    files[i] = file;
                    i++;
                }
            }
            // 文件,父的name,收件材料id
            objectMap.putAll(FileUploadUtil.uploadFile(files, mainId, clmc));
        }
        return objectMap;
    }
}
