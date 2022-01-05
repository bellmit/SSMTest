package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcgzCgml;
import cn.gtmap.msurveyplat.common.dto.DchyXmglSjclpzDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.promanage.service.ConfigureSystemService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc ConfigureSystemController: 系统配置相关接口
 */
@RestController
@RequestMapping("/configure")
public class ConfigureSystemController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ConfigureSystemService configureSystemService;

    /**
     * 获取收件材料配置
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findConfigureSystem() {
        ResponseMessage responseMessage;
        try {
            List resultList = configureSystemService.findConfigure();
            responseMessage = ResponseUtil.wrapResponseBodyByObjectList(resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 删除数据
     *
     * @param ids List<String>
     * @return ResponseMessage
     */
    @DeleteMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage dropConfigureSystemByIds(@RequestBody List<String> ids) {
        ResponseMessage responseMessage;
        try {
            if (CollectionUtils.isNotEmpty(ids)) {
                if (configureSystemService.dropConfigureByIds(ids)) {
                    responseMessage = ResponseUtil.wrapSuccessResponse();
                } else {
                    responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.DELETE_FAIL.getMsg(), ResponseMessage.CODE.DELETE_FAIL.getCode());
                }
            } else {
                responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 新增和修改数据
     *
     * @param dchyXmglSjclpzDtoList List<ReceiveConfigDto>
     * @return ResponseMessage
     */
    @PostMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage savaOrUpdate(@Valid @RequestBody List<DchyXmglSjclpzDto> dchyXmglSjclpzDtoList) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            if (CollectionUtils.isNotEmpty(dchyXmglSjclpzDtoList)) {
                if (configureSystemService.savaOrUpdateDtos(dchyXmglSjclpzDtoList)) {
                    responseMessage = ResponseUtil.wrapSuccessResponse();
                } else {
                    responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.SAVE_FAIL.getMsg(), ResponseMessage.CODE.SAVE_FAIL.getCode());
                }
            } else {
                responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
            }

        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 查询测量事项接口
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl/clsx")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findClsxCode() {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> resultList = configureSystemService.queryClsx();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 查询未创建的所属模块
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl/ssmk")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findSsmkCode() {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> resultList = configureSystemService.querySsmk();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 查询目录树接口
     *
     * @return ResponseMessage
     */
    @GetMapping("/cgml/tree")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findAchievementTree() {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> resultList = configureSystemService.findAchievementTree();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 保存规则坚持和成果目录配置
     *
     * @param dto DchyXmglGzjcCgml
     * @return ResponseMessage
     */
    @PostMapping("/cgml/tree/gzjc")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage saveOrUpdateGzjcCgml(@Valid @RequestBody DchyXmglJcgzCgml dto) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            boolean saveTreeSuccess = configureSystemService.saveOrUpdateAchievementTree(dto.getDchyXmglLcgpzs());
            boolean updateClcgjcgzSuccess = configureSystemService.updateClcgjcgz(dto.getDchyXmglJcgzs());
            if (saveTreeSuccess && updateClcgjcgzSuccess) {
                responseMessage = ResponseUtil.wrapSuccessResponse();
            } else {
                if (!saveTreeSuccess && updateClcgjcgzSuccess) {
                    responseMessage.getHead().setMsg("成果目录配置保存失败");
                } else if (saveTreeSuccess && !updateClcgjcgzSuccess) {
                    responseMessage.getHead().setMsg("成果检查规则配置保存失败");
                } else {
                    responseMessage.getHead().setMsg(ResponseMessage.CODE.SAVE_FAIL.getMsg());
                }
                responseMessage.getHead().setCode(ResponseMessage.CODE.SAVE_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/4/21 10:58
     * @description 获取成功检查规则
     */
    @GetMapping("/getCgjcgz")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage getCgjcgz() {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> resultList = configureSystemService.getCgtjjcgzList();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
