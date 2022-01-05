package cn.gtmap.msurveyplat.exchange.web.rest;

import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.exchange.service.sign.SignService;
import cn.gtmap.msurveyplat.exchange.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0,  2020/3/12
 * @description 审核信息接口
 */
@RestController
@Api(tags = "审核信息接口")
@RequestMapping("/rest/v1.0/shxx")
public class ShxxController extends BaseController {
    @Autowired
    private SignService signService;

    /**
     * @param shxxid
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据shxxid删除签名信息
     **/
    @ApiOperation("删除签名信息")
    @DeleteMapping(value = "/sign/{shxxid}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiImplicitParams(
            @ApiImplicitParam(name = "shxxid", value = "审核信息 ID", dataType = "string", paramType = "path"))
    @ResponseBody
    @CheckTokenAno
    public void deleteShxxSign(
            @PathVariable("shxxid") String shxxid) {
        signService.deleteShxxSign(shxxid);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取签名意见
     **/
    @ApiOperation("获取签名意见")
    @PostMapping(value = "/list")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shxxParamDTO", value = "审核信息参数对象", required = true, dataType = "ShxxParamDTO")})
    @ResponseBody
    @CheckTokenAno
    public List<ShxxVO> listShxx(
            @RequestBody ShxxParamDTO shxxParamDTO) {
        return signService.getShxxVOList(shxxParamDTO);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取签名意见
     **/
    @ApiOperation("更新签名意见")
    @PostMapping(value = "/sign")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shxxParamDTO", value = "审核信息参数对象", required = true, dataType = "ShxxParamDTO")})
    @ResponseBody
    @CheckTokenAno
    public ShxxVO updateShxx(
            @RequestBody ShxxParamDTO shxxParamDTO) {
        return signService.updateShxx(shxxParamDTO);
    }

}
