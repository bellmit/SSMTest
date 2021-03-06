package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 15:43
 * @description 名录库
 */
@RestController
@RequestMapping("/mlk")
public class DchyXmglMlkController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DchyXmglMlkService mlkService;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    private Repo repository;
    @Autowired
    PlatformUtil platformUtil;
    @Autowired
    EntityMapper entityMapper;

    /**
     * 根据名录库id获取对应从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getcyrybymlkid")
    @CheckInterfaceAuth
    public ResponseMessage getCyryByMlkId(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"), "null");
        HashMap<Object, Object> map = Maps.newHashMap();

        map.put("mlkid", mlkId);
        Page<Map<String, Object>> dataPaging = null;
        try {
            int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
            int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
            /*分页查询*/
            dataPaging = repository.selectPaging("queryCyryByMlkIdByPage", map, page - 1, pageSize);
            DataSecurityUtil.decryptMapList(dataPaging.getContent());
            message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 查询登录用户名录库状态
     *
     * @param
     * @return
     */
    @GetMapping(value = "/getmlkzt")
    @CheckInterfaceAuth
    public ResponseMessage getMlkzt() {
        ResponseMessage message = new ResponseMessage();
        try {
            message.getData().putAll(mlkService.queryMlkXxZtByUserid(UserUtil.getCurrentUserId()));
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 根据名录库id获取对应测绘单位信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getchdwbymlkid")
    @CheckInterfaceAuth
    public ResponseMessage getChdwByMlkId(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        List<Map<String, Object>> mlkList = mlkService.getchdwbymlkid(mlkId);
        if (CollectionUtils.isNotEmpty(mlkList)) {
            DchyXmglZd zzdj = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_ZZDJ, (String) mlkList.get(0).get("ZZDJ"));
            DchyXmglZd dwxz = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_DWXZ, (String) mlkList.get(0).get("DWXZ"));
            if (null != zzdj) {
                mlkList.get(0).put("ZZDJMC", zzdj.getMc());
            } else {
                mlkList.get(0).put("ZZDJMC", "");
            }
            if (null != dwxz) {
                mlkList.get(0).put("DWXZMC", dwxz.getMc());
            } else {
                mlkList.get(0).put("DWXZMC", "");
            }
        }
        return ResponseUtil.wrapResponseBodyByList(mlkList);
    }


    /**
     * 初始化申请信息表
     *
     * @param param
     * @return
     */
    @PostMapping(value = "initsqxx")
    @CheckInterfaceAuth
    public ResponseMessage initSqxx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        HashMap<Object, Object> map = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            DchyXmglSqxx dchyXmglSqxx = mlkService.initSqxx(data);
            if (null != dchyXmglSqxx) {
                message = ResponseUtil.wrapSuccessResponse();
                map.put("mlkid", dchyXmglSqxx.getGlsxid());
                map.put("sqxxid", dchyXmglSqxx.getSqxxid());
                map.put("blsx", dchyXmglSqxx.getBlsx());
                message.getData().put("dataList", map);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库入驻
     *
     * @param param
     */
    @PostMapping(value = "/savemlk")
    @CheckInterfaceAuth
    public ResponseMessage saveChdwXx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            boolean initMlk = mlkService.initMlk(data);
            if (initMlk) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库注销前的状态检查(判断是否有在建工程，有则不允许注销)
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/checkbeforelogoutmlk")
    @CheckInterfaceAuth
    public ResponseMessage checkBeforeLogoutMlk(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            boolean b = mlkService.checkBeforeLogoutMlk(data);
            if (b) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.LOGOUT_FAIL.getMsg(), ResponseMessage.CODE.LOGOUT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取名录库代办列表
     *
     * @return
     */
    @PostMapping(value = "getmlkdb")
    public ResponseMessage getMlkDbrw(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> mlkDbrw = mlkService.getMlkDbrw(data);
            message = ResponseUtil.wrapResponseBodyByPage(mlkDbrw);
        } catch (Exception e) {
            logger.error("名录库待办列表错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取名录库已办任务
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getmlkyb")
    public ResponseMessage getMlkYbrw(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> mlkDbrw = mlkService.getMlkYbrw(data);
            return ResponseUtil.wrapResponseBodyByPage(mlkDbrw);
        } catch (Exception e) {
            logger.error("名录库已办列表错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取名录库注销的移除原因
     *
     * @param mlkid
     * @return
     */
    @GetMapping("getycyybymlkid/{mlkid}")
    public ResponseMessage getYcyyByMlkid(@PathVariable("mlkid") String mlkid) {
        ResponseMessage message = new ResponseMessage();
        try {
            String ycyy = mlkService.getYcyyByMlkid(mlkid);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("ycyy", ycyy);
        } catch (Exception e) {
            logger.error("获取名录库移除原因错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库注销审核
     *
     * @param param
     * @return
     */
    @PostMapping("mlkzxsh")
    public ResponseMessage mlkLogoutAudit(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            boolean b = mlkService.mlkLogoutAudit(data);
            if (b) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.MLK_LOGOUT_AUDIT_FAIL.getMsg(), ResponseMessage.CODE.MLK_LOGOUT_AUDIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("名录库注销审核错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库注销
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/logoutmlk")
    public ResponseMessage logoutMlk(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            mlkService.logoutMlk(data);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("名录库注销审核错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 变更名录库信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/altermlkinfo")
    public ResponseMessage alterMlkInfo(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            DchyXmglMlkDto xmglMlkDto = mlkService.alterMlkInfo(data);
            if (null != xmglMlkDto) {
                boolean b = mlkService.saveChangeAfterMlkxx(xmglMlkDto);
                if (b) {
                    message = ResponseUtil.wrapSuccessResponse();
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ALTER_MLK_INFO_FAIL.getMsg(), ResponseMessage.CODE.ALTER_MLK_INFO_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("名录库变更错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库入驻时，向页面返回信息填入
     *
     * @return
     */
    @PostMapping(value = "getinitmlkparam")
    @CheckInterfaceAuth
    public ResponseMessage getInitMlkParam() {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> mlkParam = mlkService.getInitMlkParam();
            if (!mlkParam.isEmpty()) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().putAll(mlkParam);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            logger.error("名录库入驻错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 名录库查看附件
     *
     * @param param
     * @return
     */
    @PostMapping(value = "viewattachments")
    @CheckInterfaceAuth
    public ResponseMessage viewAttachments(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        Map<String, Object> mapList = mlkService.viewattachments(data);
        if (!mapList.isEmpty()) {
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_FAIL.getMsg(), ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/2/23 首页名录库查看台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "mlkck")
    @CheckInterfaceAuth
    public ResponseMessage mlkck(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = MapUtils.getMap(param, "data");
        try {
            Page<Map<String, Object>> mlksList = mlkService.mlkck(data);
            message = ResponseUtil.wrapResponseBodyByPage(mlksList);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 多条件查询测绘单位信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/querychdwsbymultconditions")
    @CheckInterfaceAuth
    public ResponseMessage queryChdwsByMultConditions(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = MapUtils.getMap(param, "data");
        try {
            Page<Map<String, Object>> mlksList = mlkService.queryChdwsByMultConditions(data);
            message = ResponseUtil.wrapResponseBodyByPage(mlksList);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @PostMapping(value = "uploadMlktp")
    public ResponseMessage uploadMlktp(@RequestParam("files") MultipartFile file, HttpServletRequest request) throws IOException {
        ResponseMessage message = new ResponseMessage();
        String mlkid = CommonUtil.ternaryOperator(request.getParameter("mlkid"));
        if (StringUtils.isBlank(mlkid)) {
            mlkid = UUIDGenerator.generate18();
            DchyXmglMlk mlk = new DchyXmglMlk();
            mlk.setMlkid(mlkid);
            entityMapper.saveOrUpdate(mlk, mlkid);
            logger.info("**********mlkid:" + mlkid);
        }

        Map<String, Object> paramMap = Maps.newHashMap();
        byte[] bytes = file.getBytes();

        if (StringUtils.isNotBlank(mlkid) && null != bytes) {
            DchyXmglMlk dchyXmglMlk = new DchyXmglMlk();
            dchyXmglMlk.setMlkid(mlkid);
            dchyXmglMlk.setMlktp(bytes);
            paramMap.put("dchyXmglMlk", dchyXmglMlk);

            try {
                message = mlkService.uploadMlktp(paramMap);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }
}
