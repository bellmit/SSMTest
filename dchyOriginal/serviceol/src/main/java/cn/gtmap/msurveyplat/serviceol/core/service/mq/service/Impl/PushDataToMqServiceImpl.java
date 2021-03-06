package cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.send.*;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.JsdwPjService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.KpxxService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/13
 * @description 数据推送实现类
 */
@Service
public class PushDataToMqServiceImpl implements PushDataToMqService {

    @Autowired
    private SendXsxmxxToXmglServiceImpl sendXsxmxxToXmglService;

    @Autowired
    private SendHtbgToXmglServiceImpl sendHtbgToXmglService;

    @Autowired
    private SendXmxgxxToXmglServiceImpl sendXmxgxxToXmglService;

    @Autowired
    private SendCgjcQqToXmglServiceImpl sendCgjcQqToXmglService;

    @Autowired
    private SendCgtjQqToXmglServiceImpl sendCgtjQqToXmglService;

    @Autowired
    private JsdwPjService jsdwPjService;

    @Autowired
    private SendZxbjToXmglServiceImpl sendZxbjToXmglService;

    @Autowired
    private SendCgViewToXmglServiceImpl sendCgViewToXmglService;

    @Autowired
    private SendXsxmwjToXmglServiceImpl sendXsxmwjToXmglService;

    @Autowired
    private KpxxService kpxxService;

    @Autowired
    private MlkxxServiceImpl mlkxxService;

    @Autowired
    private JsdwFbxfwServiceImpl jsdwFbxfwService;

    @Autowired
    private SendJcsjToXmglServiceImpl sendJcsjToXmglService;

    @Autowired
    private SendXxtxToXmglServiceImpl sendXxtxToXmglService;

    @Autowired
    private SendTjfxToXmglServiceImpl sendTjfxToXmglService;

    @Autowired
    private SendJsdwlrToXmglServiceImpl sendJsdwlrToXmglService;

    @Override
    public void pushMlkMsgToMq(String mlkid, String sqxxid) {
        Map<String, String> param = Maps.newHashMap();
        if (StringUtils.isNoneBlank(mlkid)) {
            param.put("mlkid", mlkid);
        }
        if (StringUtils.isNoneBlank(sqxxid)) {
            param.put("sqxxid", sqxxid);
        }
        //名录库入驻数据   线上-->线下
        sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, mlkxxService.getAllData(param));
    }

    @Override
    public void pushKpMsgToMq(String mlkid) {
        if (StringUtils.isNoneBlank(mlkid)) {
            //管理单位考评数据   线上-->线下
            sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, kpxxService.getAllData(mlkid));
        }
    }

    @Override
    public void pushMlkXxToMq(String mlkid) {
        if (StringUtils.isNoneBlank(mlkid)) {
            //管理单位考评数据   线上-->线下
            sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, mlkxxService.getSingleData(mlkid));
        }
    }

    @Override
    public void pushXmpjMsgToMq(String chxmid) {
        if (StringUtils.isNoneBlank(chxmid)) {
            //建设单位评价测绘单位数据  线上备份-->线下
            sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_ZXBJ_QUEUE_ROUTINGKEY, jsdwPjService.getAllData(chxmid));
        }
    }

    @Override
    public void pushJsdwFbfwMsgToMqSave(String chxmid) {
        if (StringUtils.isNotBlank(chxmid)) {
            //建设单位发布新服务  线上备份-->线下
            sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, jsdwFbxfwService.getSingleData(chxmid));
        }
    }

    @Override
    public void pushJsdwFbfwMsgToMqDel(DchyXmglChxmDto jsdwFbxfwModel) {
        if (null != jsdwFbxfwModel) {
            //建设单位发布新服务(删除)  线上备份-->线下
            jsdwFbxfwModel.setCzlx(Constants.DCHY_XMGL_SJTS_CZLX_DEL);
            sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, jsdwFbxfwModel);

        }
    }

    @Override
    public void pushSfdjMlkMsgToMq(String mlkid) {
        if (StringUtils.isNotBlank(mlkid)) {
            //名录库冻结数据   线上-->线下
            sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, mlkxxService.getSingleData(mlkid));
        }
    }

    @Override
    public void pushMlkDtoToMq(DchyXmglMlkDto dchyXmglMlkDto) {
        DchyXmglMlkDto mlkxxModelSaveOrUpdate = new DchyXmglMlkDto();
        mlkxxModelSaveOrUpdate.setDchyXmglMlkList(dchyXmglMlkDto.getDchyXmglMlkList());
        if (CollectionUtils.isNotEmpty(dchyXmglMlkDto.getDchyXmglMlkClsxGxList())) {
            mlkxxModelSaveOrUpdate.setDchyXmglMlkClsxGxList(dchyXmglMlkDto.getDchyXmglMlkClsxGxList());
        }
        if (CollectionUtils.isNotEmpty(dchyXmglMlkDto.getDchyXmglCyryList())) {
            mlkxxModelSaveOrUpdate.setDchyXmglCyryList(dchyXmglMlkDto.getDchyXmglCyryList());
        }
        List<DchyXmglMlk> mlkList = dchyXmglMlkDto.getDchyXmglMlkList();
        if (CollectionUtils.isNotEmpty(mlkList)) {
            DchyXmglMlk mlk = mlkList.get(0);
            mlkxxModelSaveOrUpdate.setMlkTp(new String(mlk.getMlktp()));
            mlk.setMlktp(null);
        }
        //名录库信息修改   线上-->线下
        sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, mlkxxModelSaveOrUpdate);
    }

    @Override
    public void pushCgjcMsgToMq(DchyXmglCgJcDto dchyXmglCgJcDto) {
        if (null != dchyXmglCgJcDto) {
            //测量成果包  线上到线下
            sendCgjcQqToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_CGJC_QUEUE_ROUTINGKEY, dchyXmglCgJcDto);
        }
    }

    @Override
    public void pushCgtjMsgToMq(DchyXmglCgtjDto dchyXmglCgtjDto) {
        if (null != dchyXmglCgtjDto) {
            //测量成果包  线上到线下
            sendCgtjQqToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_CGTJ_QUEUE_ROUTINGKEY, dchyXmglCgtjDto);
        }
    }

    @Override
    public void pushSlxxMsgToMq(DchyXmglChxmDto dchyXmglChxmDto) {
        if (null != dchyXmglChxmDto) {
            //合同信息  线上到线下
            sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, dchyXmglChxmDto);
        }
    }

    @Override
    public void pushHtbgMsgToMq(DchyXmglChxmDto dchyXmglChxmDto) {
        if(null != dchyXmglChxmDto){
            //合同信息变更  线上到线下
            sendHtbgToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_HTBG_QUEUE_ROUTINGKEY, dchyXmglChxmDto);
        }
    }

    /**
     * 线上办结推线下处理
     *
     * @param dchyXmglZxbjDto
     */
    @Override
    public void pushOnlineCompleteToXx(DchyXmglZxbjDto dchyXmglZxbjDto) {
        if (null != dchyXmglZxbjDto) {
            sendZxbjToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_ZXBJ_QUEUE_ROUTINGKEY, dchyXmglZxbjDto);
        }
    }

    @Override
    public void pushOnlineGcViewToXx(DchyXmglZxbjDto dchyXmglZxbjDto) {
        if (null != dchyXmglZxbjDto) {
            sendCgViewToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_CG_VIEW_QUEUE_ROUTINGKEY, dchyXmglZxbjDto);
        }
    }

    @Override
    public void pushOnlineWjFileToxx(DchyXmglChxmDto dchyXmglChxmDto) {
        if (null != dchyXmglChxmDto) {
            sendXsxmwjToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_TSWJ_QUEUE_ROUTINGKEY, dchyXmglChxmDto);
        }
    }

    @Override
    public void pushJcsjMsgToMq(DchyXmglJcsjsqDto dchyXmglJcsjsqDto) {
        if (null != dchyXmglJcsjsqDto) {
            sendJcsjToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_JCSJ_QUEUE_ROUTINGKEY, dchyXmglJcsjsqDto);
        }
    }

    @Override
    public void pushXxtxMsgToMq(DchyXmglXxtxDto dchyXmglXxtxDto) {
        if (null != dchyXmglXxtxDto) {
            sendXxtxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XXTX_QUEUE_ROUTINGKEY, dchyXmglXxtxDto);
        }
    }

    @Override
    public void pushTjfxMsgToMq(DchyXmglTjfxDto dchyXmglTjfxDto) {
        if (null != dchyXmglTjfxDto) {
            sendTjfxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_TJFX_QUEUE_ROUTINGKEY, dchyXmglTjfxDto);
        }
    }

    /**
     * @param dchyXmglJsdwlrDto
     * @return
     * @description 2021/5/10 推送线上注册的建设单位信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public void pushJsdwlrMsgToMq(DchyXmglJsdwlrDto dchyXmglJsdwlrDto) {
        if (null != dchyXmglJsdwlrDto) {
            sendJsdwlrToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_JSDWLR_QUEUE_ROUTINGKEY, dchyXmglJsdwlrDto);
        }
    }
}
