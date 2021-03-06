package cn.gtmap.msurveyplat.promanage.feign;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.dto.UploadParamDTO;
import cn.gtmap.msurveyplat.common.dto.UserDto;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.common.vo.SurveyItemVo;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description Exchange服务
 */
public interface ExchangeFeignService {
    /**
     * @param xmid
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据xmid创建文件中心节点
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/creatXmNode/{xmid}")
    Integer creatXmNode(@Param("xmid") String xmid);

    /**
     * @param slbh   受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 触发开始质检接口
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/qc/qualitycheck/{slbh}/{cgsjlx}")
    Object startQualityCheck(@Param("slbh") String slbh, @Param("cgsjlx") String cgsjlx);


    /**
     * @param gzlslid 工作流实例ID
     * @param remark
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新系统remark字段
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/updateRemark/{gzlslid}/{remark}")
    void updateRemark(@Param("gzlslid") String gzlslid, @Param("remark") String remark);

    /**
     * @param slbh 受理编号
     * @param clmc 材料名称
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取上传文件中心参数对象
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getUploadParamDTO/{slbh}/{clmc}")
    UploadParamDTO getUploadParamDTO(@Param("slbh") String slbh, @Param("clmc") String clmc);

    /**
     * @param wiid 工作流实例id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取工作流实例
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getWorkFlowInstanceVo/{wiid}")
    PfWorkFlowInstanceVo getpPfWorkFlowInstanceVo(@Param("wiid") String wiid);

    /**
     * @param slbh slbh
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据受理编号删除文件中心节点
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/deleteXmNode/{slbh}")
    void deleteXmNode(@Param("slbh") String slbh);

    /**
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取审核信息
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/shxx/list")
    List<ShxxVO> getShxxVOList(@RequestBody ShxxParamDTO shxxParamDTO);

    /**
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新审核信息
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/shxx/sign")
    ShxxVO updateShxxVO(@RequestBody ShxxParamDTO shxxParamDTO);


    /**
     * @param shxxid 审核信息ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 删除签名
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("DELETE /rest/v1.0/shxx/sign/{shxxid}")
    ShxxVO deleteSign(@Param("shxxid") String shxxid);

    /**
     * @param slbh   受理编号
     * @param clmc   材料名称
     * @param qtcwid 其他错误id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取上传文件中心参数对象
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getqtcwsccs/{slbh}/{clmc}/{qtcwid}/{userId}")
    UploadParamDTO getQtcwsccs(@Param("slbh") String slbh, @Param("clmc") String clmc, @Param("qtcwid") String qtcwid, @Param("userId") String userId);


    /**
     * @param slbh 受理编号
     * @param clmc 材料名称
     * @param
     * @return
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @description 获取其他错误id号
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getMainID/{slbh}/{clmc}")
    Integer getMainID(@Param("slbh") String slbh, @Param("clmc") String clmc);

    /**
     * @param nodeId 节点id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据节点id删除文件节点
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/deleteNodeById/{nodeId}")
    void deleteNodeById(@Param("nodeId") Integer nodeId);

    /**
     * @param actStProRelDo 流程扩展表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 插入或更新
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/saveorupdatetaskextenddto")
    void saveOrUpdateTaskExtendDto(@RequestBody ActStProRelDo actStProRelDo);

    /**
     * @param slbh 受理编号
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 检查成果包是否存在
     **/
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/qc/check/{slbh}")
    Map<String, Object> check(@Param("slbh") String slbh);

    /**
     * @param slbh   受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 成果数据入库
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/qc/importdatabase/{slbh}/{cgsjlx}")
    Object importDatabase(@Param("slbh") String slbh, @Param("cgsjlx") String cgsjlx);

    /**
     * @param slbh 受理编号
     * @param chjd 测绘阶段
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取项目一棵树材料
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/onemap/material/list/{slbh}/{chjd}")
    List<SurveyItemVo> getMaterialVoList(@Param("slbh") String slbh, @Param("chjd") String chjd);

    /**
     * @param userid 用户ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取角色信息列表
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/role/list/user/{userid}")
    List<PfRoleVo> getPfRoleVoList(@Param("userid") String userid);

    /**
     * @param cgclmcList 材料信息列表
     * @param gcbh       工程编号
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享测绘工程材料下载地址
     */
    @Headers({"Content-Type: application/json", "Accept: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/gxchgcclxx/{gcbh}")
    Map<String, Object> getGxchgcclDownUrl(@RequestBody List<String> cgclmcList, @Param("gcbh") String gcbh);

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/tasklist")
    Map<String, Object> getTaskList(Map<String, Object> map);

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取已办任务
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/taskoverlist")
    Map<String, Object> getTaskOverList(Map<String, Object> map);

    /**
     * @param slbh
     * @return
     * @description 2021/4/13 根据受理编号获取流程信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/getProcessInfoBySlbh/{slbh}")
    List<Map<String, Object>> getProcessInfoBySlbh(@Param("slbh") String slbh);

    /**
     * @param userDto
     * @return
     * @description 2021/5/11 用户名是否重复
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/yhmcf")
    ResponseMessage yhmcf(@RequestBody UserDto userDto);

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 注册新用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/register")
    ResponseMessage register(@RequestBody UserDto userDto);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/delTaskByTaskid/{taskid}")
    ResponseMessage delTaskByTaskid(@Param("taskid") String taskid);

}
