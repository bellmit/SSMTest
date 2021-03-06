package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglJsdwService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/9 8:51
 * @description 建设单位
 */
@RestController
@RequestMapping(value = "jsdw")
public class DchyXmglJsdwController {

    protected final Log logger = LogFactory.getLog(DchyXmglJsdwController.class);

    @Autowired
    private DchyXmglJsdwService jsdwService;

    @Autowired
    private DchyXmglMlkMapper dchyXmglMlkMapper;

    @Autowired
    private DchyXmglMlkService mlkService;

    @Autowired
    private EntityMapper entityMapper;

    /**
     * 获取项目评价列表
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getxmpjlist")
    @CheckInterfaceAuth
    public ResponseMessage getProjectEvalList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> objects = jsdwService.queryXmPjStatusByPage(data);
            DataSecurityUtil.decryptMapList(objects.getContent());
            return ResponseUtil.wrapResponseBodyByPage(objects);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 根据chxxid查看或新增评价详情信息
     */
    @PostMapping(value = "getpjxxbyid")
    @CheckInterfaceAuth
    @Transactional
    public ResponseMessage getEvalInfoByProjectId(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> evalInfoList = jsdwService.getEvalInfoByProjectId(data);
            //评价完成后更新mlk的评价等级字段

            List<String> mlkidList = dchyXmglMlkMapper.queryMlkidBychxmid(data);
            if (CollectionUtils.isNotEmpty(mlkidList)) {
                String mlkid = mlkidList.get(0);
                /*评价分数= (所有的考评结果平均 + 所有的测绘单位服务评价平均) / 2*/
                int pjfs = mlkService.getKpResultByMlkId(mlkid);
                DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
                if (null != dchyXmglMlk) {
                    dchyXmglMlk.setPjdj(pjfs + "");
                    entityMapper.saveOrUpdate(dchyXmglMlk, mlkid);
                }
            }

            if (CollectionUtils.isNotEmpty(evalInfoList)) {
                Map<String, Object> map = evalInfoList.get(0);
                if (!map.isEmpty()) {
                    String msg = MapUtils.getString(map, "msg");
                    if (null != msg) {
                        message.getHead().setMsg(msg);
                        message.getHead().setCode(ResponseMessage.CODE.SAVE_FAIL.getCode());
                        message.getData().put("dataList", evalInfoList);
                        return message;
                    }
                }
            }
            return ResponseUtil.wrapResponseBodyByList(evalInfoList);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 项目评价前判断项目是否已办结
     *
     * @param param
     * @return
     */
    @PostMapping(value = "checkchxmisfinish")
    @CheckInterfaceAuth
    public ResponseMessage checkChxmIsFinish(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, Object> chxmIsFinish = jsdwService.checkChxmIsFinish(data);
            if (null != chxmIsFinish) {
                String msg = MapUtils.getString(chxmIsFinish, "msg");
                if (StringUtils.equals(msg, "fail")) {
                    message.getHead().setMsg("该项目尚未办结，暂不支持评价");
                    message.getHead().setCode(ResponseMessage.CODE.SAVE_FAIL.getCode());
                } else {
                    message = ResponseUtil.wrapSuccessResponse();
                }
                message.getData().put("msg", msg);
            }
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

}
