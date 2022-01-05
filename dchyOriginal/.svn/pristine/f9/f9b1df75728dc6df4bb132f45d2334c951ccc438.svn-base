package cn.gtmap.msurveyplat.exchange.web.rest;

import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.exchange.service.qc.QualityCheckService;
import cn.gtmap.msurveyplat.exchange.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0,  2020/3/12
 * @description 数据质检接口
 */
@RestController
@Api(tags = "质检接口")
@RequestMapping("/rest/v1.0/qc")
public class QualityCheckController extends BaseController {

    @Autowired
    private QualityCheckService qualityCheckService;

    @ApiOperation(value = "开始质检")
    @ApiImplicitParams({@ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "cgsjlx", value = "成果数据类型", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/qualitycheck/{slbh}/{cgsjlx}")
    @CheckTokenAno
    public Object startQualityCheck(
            @PathVariable("slbh") String slbh,
            @PathVariable("cgsjlx") String cgsjlx) throws IOException {
        LOGGER.info("质检开始:{} {} {}", CalendarUtil.getCurHMSStrDate(), slbh, cgsjlx);
        return qualityCheckService.startQualityCheck(slbh, cgsjlx);
    }

    @ApiOperation(value = "检查成果包是否存在")
    @ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/check/{slbh}")
    @CheckTokenAno
    public Map<String, Object> check(
            @PathVariable("slbh") String slbh) {
        LOGGER.info("检查成果包是否存在:{} {} {}", CalendarUtil.getCurHMSStrDate(), slbh);
        return qualityCheckService.check(slbh);
    }

    @ApiOperation(value = "成果数据入库")
    @ApiImplicitParams({@ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "cgsjlx", value = "成果数据类型", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/importdatabase/{slbh}/{cgsjlx}")
    @CheckTokenAno
    public Object intoDatabase(
            @PathVariable("slbh") String slbh,
            @PathVariable("cgsjlx") String cgsjlx) throws IOException {
        LOGGER.info("成果入库开始:{} {} {}", CalendarUtil.getCurHMSStrDate(), slbh, cgsjlx);
        return qualityCheckService.importDatabase(slbh, cgsjlx);
    }

}
