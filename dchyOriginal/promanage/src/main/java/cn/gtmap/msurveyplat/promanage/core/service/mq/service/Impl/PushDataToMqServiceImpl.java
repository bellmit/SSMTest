package cn.gtmap.msurveyplat.promanage.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.promanage.core.service.mq.send.*;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/13
 * @description 推送数据实现类
 */
@Service
public class PushDataToMqServiceImpl implements PushDataToMqService {

    @Autowired
    private SendCgccResultToBsdtServiceImpl sendCgccResultToBsdtService;

    @Autowired
    private SendCgjcResultToBsdtServiceImpl sendCgjcResultToBsdtService;

    @Autowired
    private SendCgtjResultToBsdtServiceImpl sendCgtjResultToBsdtService;

    @Autowired
    private SendZxbjToBsdtServiceImpl sendZxbjToBsdtService;

    @Autowired
    private SendCgViewToBsdtServerImpl sendCgViewToBsdtServer;

    @Autowired
    private SendSlxxToBsdtServiceImpl sendSlxxToBsdtService;

    @Autowired
    private SendJcsjToBsdtServiceImpl sendJcsjToBsdtService;

    @Autowired
    private SendXxtxToBsdtServiceImpl sendXxtxToBsdtService;

    @Autowired
    private SendTjfxToBsdtServiceImpl sendTjfxToBsdtService;

    @Autowired
    private SendBaxxToBsdtServiceImpl sendBaxxToBsdtService;

    @Autowired
    private SendJsdwlrToBsdtServiceImpl sendJsdwlrToBsdtService;


    @Override
    public void pushSlxxMsgToMq(DchyXmglChxmDto dchyXmglChxmDto) {
        if (null != dchyXmglChxmDto) {
            sendSlxxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY, dchyXmglChxmDto);
        }
    }

    @Override
    public void pushCgjcResultToMq(DchyXmglCgJcDto dchyXmglCgJcDto) {
        if (null != dchyXmglCgJcDto) {
            sendCgjcResultToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_CGJC_QUEUE_ROUTINGKEY, dchyXmglCgJcDto);
        }
    }

    @Override
    public void pushCgtjResultToMq(DchyXmglCgtjDto dchyXmglCgtjDto) {
        if (null != dchyXmglCgtjDto) {
            sendCgtjResultToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_CGTJ_QUEUE_ROUTINGKEY, dchyXmglCgtjDto);
        }
    }

    @Override
    public void pushCgccResultToMq(DchyXmglCgccDto dchyXmglCgJcDto) {
        if (null != dchyXmglCgJcDto) {
            sendCgccResultToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_CGCC_QUEUE_ROUTINGKEY, dchyXmglCgJcDto);
        }
    }

    @Override
    public void pushZxbjResultTo(DchyXmglZxbjDto dchyXmglZxbjDto) {
        if (null != dchyXmglZxbjDto) {
            sendZxbjToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_ZXBJ_QUEUE_ROUTINGKEY, dchyXmglZxbjDto);
        }
    }

    @Override
    public void pushCgViewResultToXs(DchyXmglZxbjDto dchyXmglZxbjDto){
        if(null != dchyXmglZxbjDto){
            sendCgViewToBsdtServer.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE,Constants.XMGL_BSDT_CG_VIEW_QUEUE_ROUTINGKEY,dchyXmglZxbjDto);
        }
    }

    @Override
    public void pushJcsjResultTo(DchyXmglJcsjsqDto dchyXmglJcsjsqDto) {
        if (null != dchyXmglJcsjsqDto) {
            sendJcsjToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_JCSJ_QUEUE_ROUTINGKEY, dchyXmglJcsjsqDto);
        }
    }

    @Override
    public void pushXxtxResultTo(DchyXmglXxtxDto dchyXmglXxtxDto) {
        if (null != dchyXmglXxtxDto) {
            sendXxtxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_XXTX_QUEUE_ROUTINGKEY, dchyXmglXxtxDto);
        }
    }

    @Override
    public void pushTjfxMsgToMq(DchyXmglTjfxDto dchyXmglTjfxDto) {
        if (null != dchyXmglTjfxDto) {
            sendTjfxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_TJFX_QUEUE_ROUTINGKEY, dchyXmglTjfxDto);
        }
    }

    @Override
    public void pushBaxxMsgToMq(DchyXmglChxmListDto dchyXmglChxmListDto) {
        if (null != dchyXmglChxmListDto) {
            sendBaxxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_BAXX_QUEUE_ROUTINGKEY, dchyXmglChxmListDto);
        }
    }

    /**
     * @param dchyXmglJsdwlrDto
     * @return
     * @description 2021/5/10 推送线下的建设单位录入信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public void pushJsdwlrMsgToMq(DchyXmglJsdwlrDto dchyXmglJsdwlrDto) {
        if (null != dchyXmglJsdwlrDto) {
            sendJsdwlrToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_JSDWLR_QUEUE_ROUTINGKEY, dchyXmglJsdwlrDto);
        }
    }
}
