package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglHtxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/6/1 18:14
 * @description
 */
@Service
public class ReceiveHtbgFromBsdtServiceImpl implements ChannelAwareMessageListener {


    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;
    private Logger logger = LoggerFactory.getLogger(ReceiveHtbgFromBsdtServiceImpl.class);


    @Override
    @RabbitListener(queues = Constants.BSTD_XMGL_HTBG_QUEUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            logger.info("**********线上合同变更--消息接收成功**********");
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            logger.info("**********接收来自线上合同变更查看数据的请求数据:**********" + str);
            doHtbg(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);
            logger.error("线上成果文件查看错误原因：{}", e);
        }
    }


    public void doHtbg(String str){
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try{
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSTD_XMGL_HTBG_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_HTBG_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmDto chxmDto = JSON.toJavaObject(slxxJson, DchyXmglChxmDto.class);
            Optional<DchyXmglChxmDto> chxmDtoOptional = Optional.ofNullable(chxmDto);
            if(chxmDtoOptional.isPresent()){
                /*合同信息dto*/
                DchyXmglChxmDto dchyXmglChxmDto = chxmDtoOptional.get();
                List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = dchyXmglChxmDto.getDchyXmglHtxxDtoList();
                if(CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)){
                    for (DchyXmglHtxxDto htxxDto : dchyXmglHtxxDtoList) {
                        List<DchyXmglHtxx> htxxList = htxxDto.getDchyXmglHtxxList();
                        List<DchyXmglSjxx> sjxxList = htxxDto.getDchyXmglSjxxList();
                        List<DchyXmglSjcl> sjclList = htxxDto.getDchyXmglSjclList();
                        List<Map<String, List>> htFileList = htxxDto.getHtFileList();

                        /*sjxx*/
                        if(CollectionUtils.isNotEmpty(sjxxList)){
                            sjxxList.forEach(sjxx -> {
                                entityMapper.saveOrUpdate(sjxx,sjxx.getSjxxid());
                            });
                        }

                        /*htxx*/
                        if(CollectionUtils.isNotEmpty(htxxList)){
                            for (DchyXmglHtxx htxx : htxxList) {
                                /*sjcl*/
                                if(CollectionUtils.isNotEmpty(sjclList) && CollectionUtils.isNotEmpty(htFileList)){
                                    for (DchyXmglSjcl sjcl : sjclList) {
                                        /*合同文件上传*/
                                        for (Map<String, List> htFile : htFileList) {
                                            Map<String, Object> map = this.uploadWjcl(htFile.get(sjcl.getSjclid()), htxx.getHtxxid(), sjcl.getClmc());
                                            if(MapUtils.isNotEmpty(map)){
                                                String wjzxid = (String) map.get("folderId");//新wjzxid
                                                htxx.setWjzxid(wjzxid);
                                                entityMapper.saveOrUpdate(htxx,htxx.getHtxxid());
                                                sjcl.setWjzxid(wjzxid);
                                                entityMapper.saveOrUpdate(sjcl,sjcl.getSjclid());
                                            }
                                        }
                                    }
                                }
                            }
                            logger.info("==================线上合同变更同步线下成功=========================");
                        }
                    }
                }
            }
        }
        catch (Exception e){
            dchyXmglMqCzrz.setSbyy(e.getMessage());

            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            logger.error("接收失败原因:{}", e);
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

    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("通知服务器移除mq时异常，异常信息：", e);
        }
    }

    private Map<String, Object> uploadWjcl(List<Map<String, String>> wjList, String mainId, String clmc) throws
            IOException {
        Map<String, Object> objectMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(wjList)) {
            MultipartFile[] files = new MultipartFile[wjList.size()];
            int i = 0;
            for (Map<String, String> map : wjList) {
                // 文件名--文件内容
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    //System.out.println(base64);
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
