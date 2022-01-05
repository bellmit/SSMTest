package cn.gtmap.msurveyplat.server.util;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.dto.UploadParamDTO;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.common.vo.SurveyItemVo;
import cn.gtmap.msurveyplat.server.feign.ExchangeFeignService;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfUserVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description Exchange接口工具类
 */
@Component
public class ExchangeFeignUtil {

    private static ExchangeFeignService exchangeFeignService = null;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 初始化ExchangeFeignService接口
     * */
    static {
        exchangeFeignService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(35000, 35000))
                .retryer(new Retryer.Default(35000, 35000, 3))
                .target(ExchangeFeignService.class, AppConfig.getProperty("exchange.url"));
    }

    /**
     * @param xmid 项目ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据项目ID创建文件中心节点
     */
    public Integer creatXmNode(String xmid) {
        return exchangeFeignService.creatXmNode(xmid);
    }


    /**
     * @param slbh   受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 触发质检操作
     */
    public Object startQualityCheck(String slbh, String cgsjlx) {
        return exchangeFeignService.startQualityCheck(slbh, cgsjlx);
    }

    /**
     * @param gzlslid
     * @param remark
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新remark字段
     */
    public void updateRemark(String gzlslid, String remark) {
        exchangeFeignService.updateRemark(gzlslid, remark);
    }


    /**
     * @param slbh 受理编号
     * @param clmc 材料名称
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取上传文件中心参数对象
     **/
    public UploadParamDTO getUploadParamDTO(String slbh, String clmc) {
        return exchangeFeignService.getUploadParamDTO(slbh, clmc);
    }

    /**
     * @param wiid 工作流实例id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取工作流实例
     **/
    public PfWorkFlowInstanceVo getpPfWorkFlowInstanceVo(String wiid) {
        return exchangeFeignService.getpPfWorkFlowInstanceVo(wiid);
    }

    /**
     * @param slbh slbh
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据受理编号删除文件中心节点
     **/
    public void deleteXmNode(String slbh) {
        exchangeFeignService.deleteXmNode(slbh);
    }

    /**
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取审核信息
     **/
    public List<ShxxVO> getShxxVOList(ShxxParamDTO shxxParamDTO) {
        return exchangeFeignService.getShxxVOList(shxxParamDTO);
    }

    /**
     * @param shxxParamDTO 审核信息参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 更新审核信息
     **/
    public ShxxVO updateShxxVO(ShxxParamDTO shxxParamDTO) {
        return exchangeFeignService.updateShxxVO(shxxParamDTO);
    }


    /**
     * @param shxxid 审核信息ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 删除签名
     **/
    public ShxxVO deleteSign(String shxxid) {
        return exchangeFeignService.deleteSign(shxxid);
    }

    /**
     * @param slbh   受理编号
     * @param clmc   材料名称
     * @param qtcwid 其他错误id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取上传文件中心参数对象
     */
    public UploadParamDTO getQtcwsccs(String slbh, String clmc, String qtcwid, String userId) {
        return exchangeFeignService.getQtcwsccs(slbh, clmc, qtcwid, userId);
    }


    /**
     * @param slbh 受理编号
     * @param clmc 材料名称
     * @param
     * @return
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @description 获取其他错误 文件夹ID号
     */
    public Integer getMainID(String slbh, String clmc) {
        return exchangeFeignService.getMainID(slbh, clmc);
    }


    /**
     * @param nodeId 节点id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据节点id删除文件节点
     */
    public void deleteNodeById(Integer nodeId) {
        exchangeFeignService.deleteNodeById(nodeId);
    }

    /**
     * @param actStProRelDo 流程扩展表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 插入或更新
     */
    public void saveOrUpdateTaskExtendDto(ActStProRelDo actStProRelDo) {
        exchangeFeignService.saveOrUpdateTaskExtendDto(actStProRelDo);
    }

    /**
     * @param slbh 受理编号
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 检查成果包是否存在
     **/
    public Map<String, Object> check(String slbh) {
        return exchangeFeignService.check(slbh);
    }


    /**
     * @param slbh   受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 成果数据入库
     */
    public Object importDatabase(String slbh, String cgsjlx) {
        return exchangeFeignService.importDatabase(slbh, cgsjlx);
    }

    /**
     * @param slbh 受理编号
     * @param chjd 测绘阶段
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取项目一棵树材料
     */
    public List<SurveyItemVo> getMaterialVoList(String slbh, String chjd) {
        return exchangeFeignService.getMaterialVoList(slbh, chjd);
    }


    /**
     * @param userid 用户ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取角色信息列表
     */
    public List<PfRoleVo> getPfRoleVoList(String userid) {
        return exchangeFeignService.getPfRoleVoList(userid);
    }


    /**
     * @param paramMap
     * @param babh     工程编号
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享测绘工程材料下载地址
     */
    public Map getGxchgcclDownUrl(Map paramMap, String babh) {
        return exchangeFeignService.getGxchgcclDownUrl(paramMap, babh);
    }

    /**
     * @param loginName 用户名称
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取用户信息
     */
    public PfUserVo getUserByloginName(String loginName) {
        return exchangeFeignService.getUserByloginName(loginName);
    }

    public List<PfOrganVo> getOrganListByUser(String shr) {
        return exchangeFeignService.getOrganListByUser(shr);
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取部门列表
     */
    public List<PfOrganVo> getPfOrganVoList() {
        return exchangeFeignService.getPfOrganVoList();
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取部门下角色列表
     */
    public List<PfRoleVo> getRoleListByOrganid(String organid) {
        return exchangeFeignService.getRoleListByOrganid(organid);
    }

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    public Map<String,Object> getTaskList(Map<String, Object> map) {
        return exchangeFeignService.getTaskList(map);
    }

    /**
     * @param map
     * @return
     * @description 2021/4/13 获取已办任务
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public Map<String,Object> getTaskOverList(Map<String, Object> map) {
        return exchangeFeignService.getTaskOverList(map);
    }

    /**
     * @param slbh 受理编号
     * @return
     * @description 2021/3/30 通过受理编号获取流程信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public List<Map<String, Object>> getProcessInfoBySlbh(String slbh) {
        return exchangeFeignService.getProcessInfoBySlbh(slbh);
    }

    /**
     * @param taskid
     * @return
     * @description 2021/6/5 删除待办任务
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage delTaskByTaskid(String taskid) {
        return exchangeFeignService.delTaskByTaskid(taskid);
    }

}
