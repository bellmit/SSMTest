package cn.gtmap.msurveyplat.serviceol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.common.vo.SurveyItemVo;
import cn.gtmap.msurveyplat.serviceol.feign.ExchangeFeignService;
import cn.gtmap.msurveyplat.serviceol.utils.CommonUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@Service
public class ExchangeFeignServiceImpl {

    private static final Logger LOGGER = Logger.getLogger(ExchangeFeignServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

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
     * @param cgclmcList 材料名称
     * @param gcbh       工程编号
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取共享测绘工程材料下载地址
     */
    public Map<String, Object> getGxchgcclDownUrl(List<String> cgclmcList, String gcbh) {
        return exchangeFeignService.getGxchgcclDownUrl(cgclmcList, gcbh);
    }

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    public Map<String, Object> getTaskList(Map<String, Object> map) {
        return exchangeFeignService.getTaskList(map);
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
     * @param userDto 材料名称
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 新用户注册
     */
    public ResponseMessage register(UserDto userDto) {
        return exchangeFeignService.register(userDto);
    }

    /**
     * @param userDto
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 用户授权
     */
    public ResponseMessage updateuser(UserDto userDto) {
        return exchangeFeignService.updateuser(userDto);
    }

    /**
     * @param userAuthParamDto
     * @return
     * @description 2021/3/30 用户角色
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage userroles(UserAuthParamDto userAuthParamDto) {
        return exchangeFeignService.userroles(userAuthParamDto);
    }

    /**
     * @param userDto
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 用户名重复检查
     */
    public ResponseMessage yhmcf(UserDto userDto) {
        return exchangeFeignService.yhmcf(userDto);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/3/30 注册手机号重复检查
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage sjhcf(UserDto userDto) {
        return exchangeFeignService.sjhcf(userDto);
    }

    public ResponseMessage queryUser(Map<String, Object> paramData) {
        Map<String, String> param = Maps.newHashMap();
        String username = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "username"));
        String lxr = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "lxr"));
        String yhzjhm = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "yhzjhm"));
        String isvalid = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "isvalid"));
        String page = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "page"));
        String size = CommonUtil.formatEmptyValue(MapUtils.getString(paramData, "size"));

        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", UserUtil.getCurrentUserId());
        List<DchyXmglYhdw> dchyXmglYhdws = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(dchyXmglYhdws)) {
            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdws.get(0);
            param.put("dwbh", dchyXmglYhdw.getDwbh());
            param.put("yhlx", dchyXmglYhdw.getYhlx());
        }
        if (StringUtils.isNotBlank(page)) {
            param.put("page", page);
        }
        if (StringUtils.isNotBlank(size)) {
            param.put("size", size);
        }
        if (StringUtils.isNotBlank(username)) {
            param.put("username", username);
        }
        if (StringUtils.isNotBlank(lxr)) {
            param.put("lxr", lxr);
        }
        if (StringUtils.isNotBlank(isvalid)) {
            param.put("isvalid", isvalid);
        }
        if (StringUtils.isNotBlank(yhzjhm)) {
            param.put("yhzjhm", yhzjhm);
        }
        return exchangeFeignService.queryUser(param);
    }

    public ResponseMessage changePassword(String username, String password, String passwordNew) {
        return exchangeFeignService.changePassword(username, password, passwordNew);
    }

    public ResponseMessage changeUserState(String userid, String state) {
        return exchangeFeignService.changeUserState(userid, state);
    }

    public ResponseMessage distributionRoleAuthority(String userid, String roles) {
        return exchangeFeignService.distributionRoleAuthority(userid, roles);
    }

    public ResponseMessage queryAllRole() {
        String jsdwRole = AppConfig.getProperty("jsdwRole");
        String chdwRole = AppConfig.getProperty("chdwRole");
        String jsdwRoles = AppConfig.getProperty("jsdw.roles");
        String chdwRoles = AppConfig.getProperty("chdw.roles");

        RoleDto roleDto = new RoleDto();
        roleDto.setJsdwRole(jsdwRole);
        roleDto.setJsdwRoles(jsdwRoles);
        roleDto.setChdwRole(chdwRole);
        roleDto.setChdwRoles(chdwRoles);

        return exchangeFeignService.queryAllRole(roleDto);
    }
}
