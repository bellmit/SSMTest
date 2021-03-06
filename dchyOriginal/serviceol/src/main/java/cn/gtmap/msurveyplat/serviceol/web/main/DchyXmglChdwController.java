package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglChdwService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 14:52
 * @description 测绘单位
 */
@RestController
@RequestMapping("/chdw")
public class DchyXmglChdwController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DchyXmglChdwService chdwService;


    /**
     * 根据从业人员id获取对应从业人员信息
     *
     * @param param
     * @return
     */
    @CheckInterfaceAuth
    @PostMapping(value = "/getcyrybyid")
    public ResponseMessage getMlkxxByMlkId(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
            /*获取从业人员信息*/
            DchyXmglCyry dchyXmglCyry = chdwService.qeuryCyryByCyryId(cyryId);
            JSONObject json = null;
            if (null != dchyXmglCyry) {
                json = (JSONObject) JSON.toJSON(dchyXmglCyry);
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", json);
            } else {
                message.getHead().setMsg("未查询到数据");
                message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 查看测绘单位评价记录信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getchdwpjxx")
    @CheckInterfaceAuth
    public ResponseMessage getChdwPjxx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            Page<Map<String, Object>> maps = chdwService.queryJsdwPlXx(data);
            DataSecurityUtil.decryptMapList(maps.getContent());
            return ResponseUtil.wrapResponseBodyByPage(maps);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取当前用户信用等级
     *
     * @return
     */
    @PostMapping(value = "getxydj")
    @CheckInterfaceAuth
    public ResponseMessage getXyDj() {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> creditRate = chdwService.getCreditRate();
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().putAll(creditRate);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 查看测绘单位考评记录
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getchdwkpinfo")
    public ResponseMessage getChdwKpInfo(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            Page<Map<String, Object>> maps = chdwService.queryChdwKpInfo(data);
            return ResponseUtil.wrapResponseBodyByPage(maps);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取考评结果字典项
     *
     * @return
     */
    @PostMapping(value = "getkpjgbyzd")
    @CheckInterfaceAuth
    public List<Map<String, Object>> getKpjgByZd() {
        return chdwService.queryFwpjZd();
    }

    /**
     * 非名录库机构查询
     *
     * @param param
     * @return
     */
    @PostMapping(value = "queryUnmlkByPage")
    @CheckInterfaceAuth
    public ResponseMessage getUnmlk(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Page<Map<String, Object>> dchyXmglChdw = chdwService.queryUnmlkByPage(param);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", dchyXmglChdw);

        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }
}
