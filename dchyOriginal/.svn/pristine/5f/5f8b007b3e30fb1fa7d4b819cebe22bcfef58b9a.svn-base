package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywsqDO;
import cn.gtmap.msurveyplat.common.dto.DchyCgglGxywsqDto;
import cn.gtmap.msurveyplat.common.dto.GxywsqFycxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.UserInfo;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.server.service.share.GxywsqService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import cn.gtmap.msurveyplat.server.web.BaseController;
import com.gtis.plat.vo.PfOrganVo;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/12
 * @description 共享业务日志信息
 */
@RestController
@Api(tags = "共享业务申请接口")
public class GxywsqController extends BaseController {

    @Autowired
    private GxywsqService gxywsqService;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;

    /**
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务日志分页信息
     */
    @ApiOperation(value = "获取申请工作流定义id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/gzldyid")
    public String getGzldyid() {
        return Constants.GXYWSQ_GZLDYID;
    }

    /**
     * @param dchyCgglGxywsqDto 共享业务申请信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享业务日志分页信息
     */
    @ApiOperation(value = "共享申请业务信息初始化")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglGxywsqDto", value = "共享业务日志参数对象", required = true, dataType = "DchyCgglGxywsqDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/initgxywsq")
    public ResponseMessage initgxywsq(HttpServletRequest request, @RequestBody DchyCgglGxywsqDto dchyCgglGxywsqDto) {
        ResponseMessage responseMessage;
        try {
            UserInfo userInfo = getCurrentUser(request);
            if (dchyCgglGxywsqDto != null && CollectionUtils.isNotEmpty(dchyCgglGxywsqDto.getDchyCgglGxywsqDOList()) && null != userInfo) {
                String organId = null;
                String organName = null;
                try {
                    List<PfOrganVo> pfOrganVoList = exchangeFeignUtil.getOrganListByUser(userInfo.getId());

                    if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
                        organId = pfOrganVoList.get(0).getOrganId();
                        organName = pfOrganVoList.get(0).getOrganName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (DchyCgglGxywsqDO dchyCgglGxywsqDO : dchyCgglGxywsqDto.getDchyCgglGxywsqDOList()) {
                    dchyCgglGxywsqDO.setSqrid(userInfo.getId());
                    dchyCgglGxywsqDO.setSqrmc(userInfo.getUsername());
                    dchyCgglGxywsqDO.setSqbmid(organId);
                    dchyCgglGxywsqDO.setSqbmmc(organName);
                }
            }
            Map resultMap = gxywsqService.initGxywsq(dchyCgglGxywsqDto);
            responseMessage = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    @ApiOperation(value = "成果共享申请申请信息审核")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglGxywsqDO", value = "成果共享申请信息参数对象", required = true, dataType = "DchyCgglGxywsqDO")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/gxywsqsh")
    public ResponseMessage gxywsqsh(HttpServletRequest request, @RequestBody DchyCgglGxywsqDO dchyCgglGxywsqDO) {
        ResponseMessage responseMessage;
        try {
            UserInfo userInfo = getCurrentUser(request);
            if (dchyCgglGxywsqDO != null) {
                dchyCgglGxywsqDO.setShrid(userInfo.getId());
                dchyCgglGxywsqDO.setShrmc(userInfo.getUsername());
            }
            Map resultMap = gxywsqService.gxywsqSh(dchyCgglGxywsqDO);
            responseMessage = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @ApiOperation(value = "成果共享申请审核待办台账")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywsqFycxDto", value = "成果共享申请信息参数对象", required = true, dataType = "GxywsqFycxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/gxywsqshdb")
    public ResponseMessage cggxsqShjlDb(HttpServletRequest request, @RequestBody GxywsqFycxDto gxywsqFycxDto) {
        ResponseMessage responseMessage;
        try {
            UserInfo userInfo = getCurrentUser(request);
            if (userInfo != null && gxywsqFycxDto != null && !userInfo.isAdmin()) {
                gxywsqFycxDto.setUserid(userInfo.getId());
            }
            Page<Map<String, Object>> resultMap = gxywsqService.gxywsqDbSh(gxywsqFycxDto);
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    @ApiOperation(value = "成果共享申请审核已办台账")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywsqFycxDto", value = "成果共享申请信息参数对象", required = true, dataType = "GxywsqFycxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/gxywsqshyb")
    public ResponseMessage cggxsqShjlYb(HttpServletRequest request, @RequestBody GxywsqFycxDto gxywsqFycxDto) {
        ResponseMessage responseMessage;
        try {
            UserInfo userInfo = getCurrentUser(request);
            if (userInfo != null && gxywsqFycxDto != null && !userInfo.isAdmin()) {
                gxywsqFycxDto.setUserid(userInfo.getId());
            }
            Page<Map<String, Object>> resultMap = gxywsqService.gxywsqYbSh(gxywsqFycxDto);
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    @ApiOperation(value = "成果共享申请已办台账")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "gxywsqFycxDto", value = "成果共享申请信息参数对象", required = true, dataType = "GxywsqFycxDto")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/rest/v1.0/gxsq/gxywsqyb")
    public ResponseMessage cggxsqYb(HttpServletRequest request, @RequestBody GxywsqFycxDto gxywsqFycxDto) {
        ResponseMessage responseMessage;
        try {
            UserInfo userInfo = getCurrentUser(request);
            if (userInfo != null && gxywsqFycxDto != null && !userInfo.isAdmin()) {
                gxywsqFycxDto.setUserid(userInfo.getId());
            }
            Page<Map<String, Object>> resultMap = gxywsqService.gxywsqYbSq(gxywsqFycxDto);
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
