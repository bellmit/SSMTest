package cn.gtmap.msurveyplat.server.web.rest;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglZjqtcwDO;
import cn.gtmap.msurveyplat.server.service.ywxx.QualityCheckService;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import cn.gtmap.msurveyplat.server.web.BaseController;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 质检信息接口
 */
@RestController
@Api(tags = "质检接口")
@RequestMapping("/v1.0/qualitycheck")
public class QualityCheckController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(QualityCheckController.class);

    @Autowired
    private QualityCheckService qualityCheckService;
    @Autowired
    Repository repository;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  cwList 其他错误
     * @return
     * @description 新增其他错误
     * */
    @ApiOperation(value ="新增其他错误")
    @ApiImplicitParams({@ApiImplicitParam(name = "dchyCgglZjqtcwDO", value = "新增其他错误", required = true, dataType = "DchyCgglZjqtcwDO")})
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("xzqtcw")
    public Map<String, String> xzqtcw(@RequestBody List<DchyCgglZjqtcwDO> cwList) {
        Map<String, String> result = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(cwList)) {
            try {
                for (DchyCgglZjqtcwDO dchyCgglZjqtcwDO : cwList) {
                    if (StringUtils.isBlank(dchyCgglZjqtcwDO.getCwid())) {
                        dchyCgglZjqtcwDO.setCwid(UUIDGenerator.generate());
                    }
                    dchyCgglZjqtcwDO.setTjsj(new Date());
                }
                qualityCheckService.xzqtcw(cwList);
                result.put("code","success");
            } catch (Exception e) {
                result.put("code","error");
                result.put("msg","新增错误");
                logger.error(e.getMessage());
            }
        } else {
            result.put("code","error");
            result.put("msg","新增内容不能为空");
        }
        return  result;
    }

    /**
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @param cwid 错误id
     * @return
     * @description
     * */
    @RequestMapping("getqtcwxx")
    @ResponseBody
    public Map<String,Object> getqtcwxx(@RequestBody(required = false) String cwid){
     Map<String,String> cwidmap = new HashMap<>();
        cwidmap.put("CWID",cwid);
        Map<String,Object> resultmap = qualityCheckService.getqtcwxx(cwidmap);
        return resultmap;
    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param cwidList 错误id
     * @return
     * @description  删除其他错误
     * */
    @RequestMapping("delQtcw")
    public Map<String, String> delQtcw(@RequestBody  List<String> cwidList) {
        Map<String, String> result = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(cwidList)) {
            try {
                qualityCheckService.delQtcw(cwidList);
                result.put("code","success");
            } catch (Exception e) {
                result.put("code","error");
                result.put("msg","删除失败");
                logger.error(e.getMessage());
            }
        } else {
            result.put("code","error");
            result.put("msg","删除失败，id不能为空");
        }
        return result;
    }


    /**
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @return
     * @description  删除其他错误
     * */
    @RequestMapping("deleteMainCw")
    public  void countCwxx( String xmid ,  String Mainid){
        Map<String,String> xmidMap = new HashMap<>();
        xmidMap.put("xmid",xmid);
        Integer sumcwxx=qualityCheckService.countcwxx(xmidMap);//查询数据是否全部删除
        //找出其他错误的id号
        if(sumcwxx== 0 && StringUtils.isNotBlank(Mainid)){
            exchangeFeignUtil.deleteNodeById(Integer.valueOf(Mainid));
        }

        //判断是否全部清空并且其他错误id存在的情况下 删除其他错误文件夹


    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param  xmid 项目id
     * @return
     * @description 获取其他错误
     * */
    @RequestMapping("getQtcw")
    public Page<DchyCgglZjqtcwDO> getQtcw(Pageable pageable, String xmid) {
        Map<String, String> param = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmid)) {
            param.put("xmid",xmid);
        }
        int pageNum = pageable.getPageNumber()-1;
        return repository.selectPaging("getQtcwListByPage",param, pageNum, pageable.getPageSize());
    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @return 检查类型
     * @description 获取检查类型
     * */
    @RequestMapping("getJclx")
    public List<Map<String, Object>> getJclx(String xmbh) {
        return qualityCheckService.getJclx(xmbh);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param pageable
     * @param dm  代码
     * @param xmbh 项目编号
     * @return 检查结果
     * @description 获取检查结果
     * */
    @RequestMapping("getJcjg")
    public Page getJcjg(Pageable pageable, String xmbh, String dm) {
        Map<String, String> param = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmbh)) {
            param.put("xmbh", xmbh);
        }
        if (StringUtils.isNotBlank(dm)) {
            param.put("dm", dm);
        }
        int pageNum = pageable.getPageNumber()-1;
        return repository.selectPaging("getJcjgByPage",param, pageNum, pageable.getPageSize());
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 更新其他错误
     * */
    @RequestMapping("updateQtcw")
    public Map<String, String> updateQtcw(@RequestBody DchyCgglZjqtcwDO dchyCgglZjqtcwDO) {
        Map<String, String> param = Maps.newHashMap();
        try {
            qualityCheckService.updateQtcw(dchyCgglZjqtcwDO);
            param.put("code","success");
        } catch (Exception e) {
            param.put("code","error");
            param.put("msg",e.getMessage());
            logger.error(e.getMessage());
        }
        return param;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 获取错误级别
     * */
    @RequestMapping("getCwjb")
    public Map<String, Object> getCwjb() {
        Map<String, Object> map = Maps.newHashMap();
        try {
            List<Map<String, String>> result = qualityCheckService.getCwjb();
            map.put("code","success");
            map.put("data",result);
        } catch (Exception e) {
            map.put("code","error");
            map.put("msg",e.getMessage());
            logger.error(e.getMessage());;
        }
        return map;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh  项目编号
     * @return 检查内容
     * @description 获取检查内容
     * */
    @ApiOperation(value ="获取检查内容")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmbh", value = "项目编号", required = true, dataType = "String")})
    @PostMapping("getJcnr")
    public Map<String, Object> getJcnr(String xmbh) {
        Map<String, Object> map = Maps.newHashMap();
        try {
            Map<String, String> result = qualityCheckService.getJcnr(xmbh);
            map.put("code","success");
            map.put("data",result);
        } catch (Exception e) {
            map.put("code","error");
            map.put("msg",e.getMessage());
            logger.error(e.getMessage());;
        }
        return map;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @return 检查类型
     * @description 获取检查类型
     * */
    @RequestMapping("getJclxTotal")
    public List<Map<String, Object>> getJclxTotal(String xmbh) {
        return qualityCheckService.getJclxTotal(xmbh);
    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmbh 项目编号
     * @return
     * @description 统计检查结果数量
     * */
    @RequestMapping("countJcjgTotal")
    public Map<String, Object> countJcjgTotal(String xmbh){
        return qualityCheckService.countJcjgTotal(xmbh);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param
     * @return
     * @description 附件上传时先生成其他错误id
     * */
    @RequestMapping("scqtcwId")
    public String scqtcwId() {
        return UUIDGenerator.generate();
    }



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param slbh 受理编号
     * @return
     * @description 开始质检
     * */
    @ApiOperation(value ="开始质检")
    @ApiResponses(value = {@ApiResponse(code = 200,message = "请求获取成功"),@ApiResponse(code = 500,message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目ID", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/qc/qualitycheck/{xmid}/{slbh}")
    public Object startQualityCheck (@PathVariable(name = "xmid") String xmid, @PathVariable(name = "slbh") String slbh) throws Exception{
        return qualityCheckService.startQualityCheck(xmid,slbh);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @param slbh 受理编号
     * @return
     * @description 导出质检报告
     * */
    @ApiOperation(value = "导出质检报告")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "slbh", value = "受理编号", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/export/zjbg/{xmid}/{slbh}/{type}")
    public void export(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "xmid")String xmid, @PathVariable(name = "slbh")String slbh, @PathVariable("type") String type) throws Exception{
        qualityCheckService.export(request, response, xmid, slbh,type);
    }

    @ApiOperation(value = "检查成果包是否存在")
    @ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/check/{slbh}")
    public Map<String, Object> check(@PathVariable("slbh") String slbh){
        return qualityCheckService.check(slbh);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @param slbh 受理编号
     * @return
     * @description 获取文件信息
     * */
    @ApiOperation(value = "获取文件信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "slbh", value = "受理编号", paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/wjxx/zjbg/{xmid}/{slbh}")
    public List<Map<String, String>> getFileInfo(@PathVariable("xmid") String xmid ,@PathVariable("slbh") String slbh){
        return qualityCheckService.getFileInfo(xmid,slbh);
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @param slbh 受理编号
     * @return
     * @description 成果数据入库
     * */
    @ApiOperation(value ="成果数据入库")
    @ApiResponses(value = {@ApiResponse(code = 200,message = "请求获取成功"),@ApiResponse(code = 500,message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目ID", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "chgcbh", value = "测绘工程编号", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "gzldyid", value = "工作流定义ID", required = true, paramType = "path", dataType = "string")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/qc/importdatabase/{xmid}/{slbh}/{chgcbh}/{gzldyid}")
    public Object importDatabase(@PathVariable(name = "xmid") String xmid, @PathVariable(name = "slbh") String slbh,@PathVariable("chgcbh") String chgcbh,@PathVariable("gzldyid") String gzldyid) throws Exception{
        return qualityCheckService.importDatabase(xmid,slbh,chgcbh,gzldyid);
    }

}
