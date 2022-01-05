package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.common.dto.UploadParamDTO;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglXmMapper;
import cn.gtmap.msurveyplat.server.service.ywxx.SldxxService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import cn.gtmap.msurveyplat.server.web.BaseController;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2020/3/10
 * @description 受理单信息接口
 */
@RestController
@Api(tags = "受理单信息接口")
public class SldxxController extends BaseController {

    @Autowired
    private SldxxService sldxxService;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    Repository repository;

    @Autowired
    DchyCgglXmMapper dchyCgglXmMapper;


    /**
     * @param xmid
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    @ApiOperation(value = "查询受理单信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParam(name = "xmid", value = "项目ID", paramType = "path", dataType = "string")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/rest/v1.0/query/sld/{xmid}")
    public InitDataResultDTO getSldxx(@PathVariable(name = "xmid") String xmid) throws Exception {
        return sldxxService.getSldxx(xmid);
    }


    /**
     * @param initDataResultDTO
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    @ApiOperation(value = "保存项目信息")
    @ApiImplicitParam(name = "initDataResultDTO", value = "业务对象", dataType = "InitDataResultDTO", paramType = "body")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/save/sld")
    public void saveSldxx(@RequestBody InitDataResultDTO initDataResultDTO) {
        sldxxService.saveSldxx(initDataResultDTO);
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取上传附件参数
     */
    @ApiOperation(value = "获取上传附件参数")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmid", value = "项目ID", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "clmc", value = "材料名称", paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/getUploadParam/sld/{slbh}/{xmid}/{sjclid}/{sfpl}")
    public UploadParamDTO getUploadParam(@PathVariable(name = "slbh") String slbh, @PathVariable(name = "xmid") String xmid, @PathVariable(name = "sjclid") String sjclid, @PathVariable(name = "sfpl") boolean sfpl) {
        return sldxxService.getUploadParam(slbh, xmid, sjclid, sfpl);
    }

    /**
     * @param shzt 状态
     * @param wiid 工作流id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 更新审核状态
     */
    @ApiOperation(value = "更新审核状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "shzt", value = "状态", dataType = "string"),
            @ApiImplicitParam(name = "wiid", value = "工作流id", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/rest/v1.0/updateRemark")
    public void updateRemark(String shzt, String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = exchangeFeignUtil.getpPfWorkFlowInstanceVo(wiid);
            String remark = pfWorkFlowInstanceVo.getRemark();
            String newRemark = remark + Constants.SPLIT_STR + shzt;
            exchangeFeignUtil.updateRemark(wiid, newRemark);
        }
    }

    /**
     * @param dchyCgglSjclDO
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 新增收件材料信息
     */
    @ApiOperation(value = "新增收件材料信息")
    @ApiImplicitParam(name = "dchyCgglSjclDO", value = "收件材料对象", dataType = "DchyCgglSjclDO", paramType = "body")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/save/sjcl/{xmid}")
    public String saveSjcl(@RequestBody DchyCgglSjclDO dchyCgglSjclDO, @PathVariable(name = "xmid") String xmid) {
        return sldxxService.saveSjcl(dchyCgglSjclDO, xmid);
    }

    /**
     * @param slclidList 收件材料id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 删除收件材料信息
     */
    @ApiOperation(value = "删除收件材料信息")
    @ApiImplicitParam(name = "slclidList", value = "收件材料id", dataType = "String", paramType = "body")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/delete/sjcl")
    public void deleteSjcl(@RequestBody List<String> slclidList) {
        sldxxService.deleteSjcl(slclidList);
    }


    /**
     * @param slbh   受理编号
     * @param qtcwid 其他错误id
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取其他错误上传参数
     */
    @ApiOperation(value = "获取其他错误上传参数")
    @ApiImplicitParams({@ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "qtcwid", value = "其他错误id", paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/getqtcwsccs/sld/{slbh}/{qtcwid}/{userId}")
    public UploadParamDTO getQtcwsccs(@PathVariable(name = "slbh") String slbh, @PathVariable(name = "qtcwid") String qtcwid, @PathVariable(name = "userId") String userId) {
        String clmc = AppConfig.getProperty("qtcwclmc");
        return exchangeFeignUtil.getQtcwsccs(slbh, clmc, qtcwid, userId);
    }

    /**
     * @param slbh 受理编号
     * @param
     * @return
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @description 获取其他错误文件夹ID号
     */
    @ApiOperation(value = "获取主文件ID号")
    @ApiImplicitParams({@ApiImplicitParam(name = "slbh", value = "受理编号", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/rest/v1.0/getMainId/sld/{slbh}")
    public Integer getMainId(@PathVariable(name = "slbh") String slbh) {
        String clmc = AppConfig.getProperty("qtcwclmc");
        Integer result = exchangeFeignUtil.getMainID(slbh, clmc);
        return result;
    }

    /**
     * @return 项目备案信息
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取项目备案数据
     */
    @ApiOperation(value = "获取项目备案数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "xmhtbh", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "xmmc", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "jsdw", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "chdw", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "pageable", paramType = "path", dataType = "Pageable")})
    @RequestMapping(value = "/rest/v1.0/ObtainXmBanSj/sld")
    public Object ObtainXmBanSj(String xmhtbh,String xmmc,String jsdw,String chdw) {
        Map<String, String> param = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmhtbh)) {
            param.put("xmhtbh", xmhtbh);
        }
        if (StringUtils.isNotBlank(xmmc)) {
            param.put("xmmc", xmmc);
        }
        if (StringUtils.isNotBlank(jsdw)) {
            param.put("jsdw", jsdw);
        }
        if (StringUtils.isNotBlank(chdw)) {
            param.put("chdw", chdw);
        }
        List<Map<String,String>> resultData =  dchyCgglXmMapper.ObtainXmBanSjByPage(param);
         Map<String,Object> resultMap =new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",resultData.size());
        resultMap.put("data",resultData);
            return  resultMap;
    }

    @ApiOperation(value = "获取项目备案数据的联系人和姓名")
    @ApiImplicitParams({@ApiImplicitParam(name = "", paramType = "path", dataType = "string")})
    @RequestMapping(value = "/rest/v1.0/ObtainXmBanSj/sld/lxrphone")
         public   Object  queryLxrPhone(String  mlkid,String chdwlx){
                    Map<String,String> paramMap =new HashMap<>();
                  if(StringUtils.equals(chdwlx,"1")){
                      paramMap.put("tableName","dchy_xmgl_mlk");
                      paramMap.put("chdwlx","1");
                  }else{
                      paramMap.put("tableName","dchy_xmgl_chdw");
                      paramMap.put("chdwlx","2");
                  }
                 paramMap.put("chdwid",mlkid);
              Map<String,String> resultmap =dchyCgglXmMapper.queryNamePhone(paramMap);
               return  resultmap;
         }

    @ApiOperation(value = "查询所有的测绘单位代码和名称前端赋值需求")
    @RequestMapping(value = "/rest/v1.0/queryChdwMc")
    public  Object queryChdwMc(){
         return  dchyCgglXmMapper.queryChdwMc();

    }

    @ApiOperation(value = "获取项目地址 获取的是代码0,1 并非实际值")
    @RequestMapping(value = "/rest/v1.0/queryXmdz")
    public  Object queryXmdz(String gcbh){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("gcbh",gcbh);
        return  dchyCgglXmMapper.queryXmdz(paramMap);

    }


    @ApiOperation(value = "获取项目地址")
    @RequestMapping(value = "/rest/v1.0/queryXmdzmc")
    public  Object queryXmdzmc(@RequestBody List<String> dz){

        return  dchyCgglXmMapper.queryXmdzmc(dz);
    }












}
