package cn.gtmap.msurveyplat.exchange.web.rest;

import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.Assignment;
import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.exchange.core.entity.BdcModuleDTO;
import cn.gtmap.msurveyplat.exchange.core.entity.ModuleDto;
import cn.gtmap.msurveyplat.exchange.mapper.PlatFormMapper;
import cn.gtmap.msurveyplat.exchange.service.process.ProcessService;
import cn.gtmap.msurveyplat.exchange.service.share.GxchgcxxService;
import cn.gtmap.msurveyplat.exchange.service.user.PlatUserService;
import cn.gtmap.msurveyplat.exchange.util.PlatformUtil;
import cn.gtmap.msurveyplat.exchange.web.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.*;
import com.gtis.plat.vo.UserInfo;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/6
 * @description 平台接口
 */
@RestController
@Api(tags = "平台接口")
@RequestMapping("/rest/v1.0/platform")
public class PlatformController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private SysActivityService sysActivityService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    Repository repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private ProcessService processService;
    @Autowired
    private PlatFormMapper platformMapper;
    @Autowired
    private GxchgcxxService gxchgcxxService;
    @Autowired
    private SysAuthorService sysAuthorService;
    @Autowired
    private PlatUserService platUserService;

    private final Logger logger = LoggerFactory.getLogger(PlatformController.class);

    /**
     * @param useId
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取平台资源
     */
    @CheckTokenAno
    @RequestMapping(value = "/getSysMenu/{useId}/{systemId}", method = RequestMethod.POST)
    public Object getSysMenu(
            @PathVariable("useId") String useId,
            @PathVariable("systemId") String systemId) {
        UserInfo userInfo = BuildUserInfo(sysUserService, useId);
        // 系统资源是否弹窗
        String popup = AppConfig.getProperty("data.xt.popup");
        logger.info("***********配置项:系统资源是否弹窗data.xt.popup:" + popup);
        JSONObject jo = JSON.parseObject(popup);
        logger.info("***********获取的系统菜单的入参" + JSON.toJSONString(userInfo));
        List<PfMenuVo> pfMenuVoList = sysMenuService.getRootMenuListByRoles(userInfo, systemId);
        logger.info("***********获取的系统菜单" + JSON.toJSONString(pfMenuVoList));
        List<BdcModuleDTO> bdcModuleDTOList = new ArrayList<>();
        BdcModuleDTO bdcModuleDTO;
        ModuleDto moduleDto;
        ModuleDto moduleChildDto;
        for (PfMenuVo pfMenuVo : pfMenuVoList) {
            // 如果没有单独的菜单过滤配置，显示全部
            moduleDto = new ModuleDto();
            moduleDto.setName(pfMenuVo.getMenuName());
            moduleDto.setId(pfMenuVo.getMenuId());
            moduleDto.setCode(pfMenuVo.getMenuCode());
            PfResourceVo pfResourceVo = pfMenuVo.getResourceVo();
            moduleDto.setUrl(pfResourceVo.getResourceUrl());
            moduleDto.setSequenceNumber(0);
            if (pfResourceVo != null && jo != null && StringUtils.isNotBlank(pfResourceVo.getResourceUrl()) && pfMenuVo != null && jo.containsKey(pfMenuVo.getMenuName())) {
                boolean flag = (boolean) jo.get(pfMenuVo.getMenuName());
                if (flag) {
                    moduleDto.setPopupWindow(true);
                }
            }
            bdcModuleDTO = new BdcModuleDTO(moduleDto);
            String pid = pfMenuVo.getMenuPId();
            if (pid != null) {
                List<BdcModuleDTO> bdcModuleChildDTOList = new ArrayList<>();
                List<PfMenuVo> pfChild = sysMenuService.getRootMenuChildsByRoles(userInfo, pfMenuVo.getMenuId());
                for (PfMenuVo pfMenuChildVo : pfChild) {
                    if (!StringUtils.equals(pfMenuChildVo.getMenuPId(), pfMenuVo.getMenuId())) {
                        continue;
                    }
                    moduleChildDto = new ModuleDto();
                    moduleChildDto.setName(pfMenuChildVo.getMenuName());
                    moduleChildDto.setId(pfMenuChildVo.getMenuId());
                    moduleChildDto.setCode(pfMenuChildVo.getMenuCode());
                    PfResourceVo pfResourceChildVo = pfMenuChildVo.getResourceVo();
                    String url = pfResourceChildVo.getResourceUrl();
                    if (StringUtils.isNotBlank(url)) {
                        if (url.indexOf("${portal.url}") != -1) {
                            url = url.replace("${portal.url}", AppConfig.getProperty("portal.url"));
                        } else if (url.indexOf("${server.url}") > -1) {
                            url = url.replace("${server.url}", AppConfig.getProperty("server.url"));
                        } else if (url.indexOf("${portalol.url}") != -1) {
                            url = url.replace("${portalol.url}", AppConfig.getProperty("portalol.url"));
                        } else if (url.indexOf("${serviceol.url}") > -1) {
                            url = url.replace("${serviceol.url}", AppConfig.getProperty("serviceol.url"));
                        } else if (url.indexOf("${promanage.url}") > -1) {
                            url = url.replace("${promanage.url}", AppConfig.getProperty("promanage.url"));
                        }
                        if (url.indexOf("?") > 0) {
                            url = url + "&moduleCode=" + pfResourceChildVo.getResourceId();
                        } else {
                            url = url + "?moduleCode=" + pfResourceChildVo.getResourceId();
                        }
                    }
                    moduleChildDto.setUrl(url);
                    moduleChildDto.setSequenceNumber(0);
                    if (StringUtils.isNotBlank(url) && jo != null && pfMenuChildVo != null && jo.containsKey(pfMenuChildVo.getMenuName())) {
                        boolean flag = (boolean) jo.get(pfMenuChildVo.getMenuName());
                        if (flag) {
                            moduleChildDto.setPopupWindow(true);
                        }
                    }
                    BdcModuleDTO bdcModuleChildDTO = new BdcModuleDTO(moduleChildDto);
                    bdcModuleChildDTOList.add(bdcModuleChildDTO);
                }
                bdcModuleDTO.setChildTree(bdcModuleChildDTOList);
                bdcModuleDTOList.add(bdcModuleDTO);
            }
        }
        return bdcModuleDTOList;
    }

    /**
     * @param useId
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取平台资源
     */
    @CheckTokenAno
    @RequestMapping(value = "/getSysMenu/{useId}", method = RequestMethod.POST)
    public Object getSysMenu(
            @PathVariable("useId") String useId) {
        UserInfo userInfo = BuildUserInfo(sysUserService, useId);
        // 系统资源是否弹窗
        String popup = AppConfig.getProperty("data.xt.popup");
        JSONObject jo = JSON.parseObject(popup);
        List<PfMenuVo> pfMenuVoList = sysMenuService.getSysMenuByUserInfo(userInfo);
        List<BdcModuleDTO> bdcModuleDTOList = new ArrayList<>();
        BdcModuleDTO bdcModuleDTO;
        ModuleDto moduleDto;
        ModuleDto moduleChildDto;
        for (PfMenuVo pfMenuVo : pfMenuVoList) {
            // 如果没有单独的菜单过滤配置，显示全部
            moduleDto = new ModuleDto();
            moduleDto.setName(pfMenuVo.getMenuName());
            moduleDto.setId(pfMenuVo.getMenuId());
            moduleDto.setCode(pfMenuVo.getMenuCode());
            PfResourceVo pfResourceVo = pfMenuVo.getResourceVo();
            moduleDto.setUrl(pfResourceVo.getResourceUrl());
            moduleDto.setSequenceNumber(0);
            if (pfResourceVo != null && jo != null && StringUtils.isNotBlank(pfResourceVo.getResourceUrl()) && pfMenuVo != null && jo.containsKey(pfMenuVo.getMenuName())) {
                boolean flag = (boolean) jo.get(pfMenuVo.getMenuName());
                if (flag) {
                    moduleDto.setPopupWindow(true);
                }
            }
            bdcModuleDTO = new BdcModuleDTO(moduleDto);
            String pid = pfMenuVo.getMenuPId();
            if (pid != null) {
                List<BdcModuleDTO> bdcModuleChildDTOList = new ArrayList<>();
                List<PfMenuVo> pfChild = sysMenuService.getRootMenuChildsByRoles(userInfo, pfMenuVo.getMenuId());
                for (PfMenuVo pfMenuChildVo : pfChild) {
                    if (!StringUtils.equals(pfMenuChildVo.getMenuPId(), pfMenuVo.getMenuId())) {
                        continue;
                    }
                    moduleChildDto = new ModuleDto();
                    moduleChildDto.setName(pfMenuChildVo.getMenuName());
                    moduleChildDto.setId(pfMenuChildVo.getMenuId());
                    moduleChildDto.setCode(pfMenuChildVo.getMenuCode());
                    PfResourceVo pfResourceChildVo = pfMenuChildVo.getResourceVo();
                    String url = pfResourceChildVo.getResourceUrl();
                    if (StringUtils.isNotBlank(url)) {
                        if (url.indexOf("${portal.url}") != -1) {
                            url = url.replace("${portal.url}", AppConfig.getProperty("portal.url"));
                        } else if (url.indexOf("${server.url}") > -1) {
                            url = url.replace("${server.url}", AppConfig.getProperty("server.url"));
                        } else if (url.indexOf("${portalol.url}") != -1) {
                            url = url.replace("${portalol.url}", AppConfig.getProperty("portalol.url"));
                        } else if (url.indexOf("${serviceol.url}") > -1) {
                            url = url.replace("${serviceol.url}", AppConfig.getProperty("serviceol.url"));
                        } else if (url.indexOf("${promanage.url}") > -1) {
                            url = url.replace("${promanage.url}", AppConfig.getProperty("promanage.url"));
                        }
                        if (url.indexOf("?") > 0) {
                            url = url + "&moduleCode=" + pfResourceChildVo.getResourceId();
                        } else {
                            url = url + "?moduleCode=" + pfResourceChildVo.getResourceId();
                        }
                    }
                    moduleChildDto.setUrl(url);
                    moduleChildDto.setSequenceNumber(0);
                    if (StringUtils.isNotBlank(url) && jo != null && pfMenuChildVo != null && jo.containsKey(pfMenuChildVo.getMenuName())) {
                        boolean flag = (boolean) jo.get(pfMenuChildVo.getMenuName());
                        if (flag) {
                            moduleChildDto.setPopupWindow(true);
                        }
                    }
                    BdcModuleDTO bdcModuleChildDTO = new BdcModuleDTO(moduleChildDto);
                    bdcModuleChildDTOList.add(bdcModuleChildDTO);
                }
                bdcModuleDTO.setChildTree(bdcModuleChildDTOList);
                bdcModuleDTOList.add(bdcModuleDTO);
            }
        }
        return bdcModuleDTOList;
    }

    public List<String> Menus(String menstr) {
        List<String> resultlist = new ArrayList<>();
        if (StringUtils.isNotBlank(menstr)) {
            String[] strArr = menstr.split(",");//注意分隔符是需要转译
            for (int i = 0; i < strArr.length; i++) {
                resultlist.add(strArr[i]);
            }
        }
        return resultlist;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取资源
     */
    @CheckTokenAno
    @RequestMapping(value = "/getResource/{resourceId}", method = RequestMethod.POST)
    public PfResourceVo getResource(
            @PathVariable("resourceId") String resourceId) {
        PfResourceVo pfResourceVo = sysMenuService.getResource(resourceId);
        if (pfResourceVo != null) {
            return sysMenuService.getResourceByCode(pfResourceVo.getResourceCode());
        }
        return null;
    }

    @ApiOperation(value = "获取资源授权信息")
    @CheckTokenAno
    @PostMapping(value = "/getResourceAuthorty/{useId}/{resourceId}")
    public Map<String, String> getResourceAuthorty(
            @PathVariable("useId") String useId,
            @PathVariable("resourceId") String resourceId) {
        Map<String, String> authorities = Maps.newHashMap();
        UserInfo userInfo = BuildUserInfo(sysUserService, useId);
        if (!userInfo.isAdmin() && StringUtils.isNotBlank(userInfo.getId())) {
            // 完全控制列表
            List<PfPartitionInfoVo> systemResrouceFunAuthorList = sysAuthorService.getSystemResrouceFunAuthorList(userInfo.getRoleIds(), resourceId);
            // 只读、不可见列表
            List<PfPartitionInfoVo> systemResrouceAuthorList = sysAuthorService.getSystemResrouceAuthorList(resourceId, userInfo.getRoleIds());
            if (CollectionUtils.isNotEmpty(systemResrouceFunAuthorList)) {
                for (PfPartitionInfoVo partitionInfoVo : systemResrouceFunAuthorList) {
                    authorities.put(partitionInfoVo.getElementId(), "available");
                }
            }
            if (CollectionUtils.isNotEmpty(systemResrouceAuthorList)) {
                for (PfPartitionInfoVo partitionInfoVo : systemResrouceAuthorList) {
                    authorities.put(partitionInfoVo.getElementId(), partitionInfoVo.getOperateType() == 1 ? "readonly" : "hidden");
                }
            }
        }
        return authorities;
    }

    public static UserInfo BuildUserInfo(SysUserService userService, String useId) {
        PfUserVo pfUserVo = userService.getUserVo(useId);
        UserInfo userInfo = new UserInfo();
        if (pfUserVo != null) {
            userInfo.setId(pfUserVo.getUserId());
            userInfo.setUsername(pfUserVo.getUserName());
            if (!"0".equals(pfUserVo.getUserId())) {
                List<PfRoleVo> pfRoleVoList = userService.getRoleListByUser(pfUserVo.getUserId());
                userInfo.setUsersIdAll(userService.getUserAgentList(pfUserVo.getUserId()));
                userInfo.setLstRole(pfRoleVoList);
                BuildUserRoles(userService, userInfo);
            } else {
                userInfo.setAdmin(true);
                userInfo.setUsersIdAll(null);
                userInfo.setRoleIds(null);
            }
        }
        return userInfo;
    }

    public static void BuildUserRoles(SysUserService userService, UserInfo userInfo) {
        if (userInfo != null) {
            List<String> lstAgentRoles = userService.getRoleListByAgentUser(userInfo.getId());
            List<PfRoleVo> lstRoles = userInfo.getLstRole();
            List<String> tmpList = new ArrayList();
            StringBuffer strRoles = new StringBuffer("");
            if (CollectionUtils.isNotEmpty(lstRoles)) {
                for (PfRoleVo roleVo : lstRoles) {
                    strRoles.append("'" + roleVo.getRoleId() + "',");
                    tmpList.add(roleVo.getRoleId());
                }
            }
            if (CollectionUtils.isNotEmpty(lstAgentRoles)) {
                for (String roleStr : lstAgentRoles) {
                    if (!tmpList.contains(roleStr)) {
                        strRoles.append("'" + roleStr + "',");
                    }
                }
            }
            if (strRoles.length() > 1) {
                userInfo.setRoleIds(strRoles.toString().substring(0, strRoles.toString().length() - 1));
            } else {
                userInfo.setRoleIds("");
            }
        }
    }

    /**
     * @param slbh
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据项目ID创建文件中心节点
     */
    @CheckTokenAno
    @RequestMapping(value = "/creatXmNode/{slbh}", method = RequestMethod.POST)
    public Integer creatXmNode(
            @PathVariable("slbh") String slbh) {
        return platformUtil.creatXmNode(slbh);
    }

    /**
     * @param wiid 流程实例id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取节点名称
     */
    @CheckTokenAno
    @RequestMapping(value = "getActivityName/{wiid}")
    public List<HashMap> getActivityName(
            @PathVariable("wiid") String wiid) {
        return sysActivityService.queryActivityDetailInfoById(wiid);

    }

    @CheckTokenAno
    @RequestMapping(value = "getWorkFlowInstanceVo/{wiid}")
    public PfWorkFlowInstanceVo getWorkFlowInstanceVo(
            @PathVariable("wiid") String wiid) {
        return sysWorkFlowInstanceService.getWorkflowInstance(wiid);
    }

    /**
     * @param gzlslid
     * @param remark
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据gzlslid和remark更新工作流实例的remark字段信息
     */
    @CheckTokenAno
    @RequestMapping(value = "/updateRemark/{gzlslid}/{remark}", method = RequestMethod.POST)
    public void updateWorkFlowInstanceRemark(
            @PathVariable("gzlslid") String gzlslid,
            @PathVariable("remark") String remark) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
        if (StringUtils.isNotBlank(gzlslid)) {
            pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(gzlslid);
        }
        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(remark)) {
            pfWorkFlowInstanceVo.setRemark(remark);
            sysWorkFlowInstanceService.updateWorkFlowInstanceRemark(pfWorkFlowInstanceVo);
        }
    }

    /**
     * @param slbh
     * @param clmc
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取上传文件中心参数对象
     */
    @CheckTokenAno
    @RequestMapping(value = "/getUploadParamDTO/{slbh}/{clmc}", method = RequestMethod.POST)
    public UploadParamDTO getUploadParamDTO(
            @PathVariable("slbh") String slbh,
            @PathVariable("clmc") String clmc) {
        UploadParamDTO uploadParamDTO = null;
        if (StringUtils.isNotBlank(slbh)) {
            uploadParamDTO = new UploadParamDTO();
            String fileCenterUrl = "fcm";
            if (super.fileCenterUrl != null && super.fileCenterUrl.length() > 4) {
                fileCenterUrl += super.fileCenterUrl.substring(4);
            }
            uploadParamDTO.setFileCenterUrl(fileCenterUrl);

            Node parentNode = platformUtil.getNodeBySlbh(slbh);
            if (parentNode != null && parentNode.getId() != null) {
                if (StringUtils.isBlank(clmc) || StringUtils.equals("undefined", clmc)) {
                    uploadParamDTO.setNodeId(String.valueOf(parentNode.getId()));
                } else {
                    uploadParamDTO.setNodeId(String.valueOf(platformUtil.createChildNodeByClmc(parentNode, clmc)));
                }
            }
        }
        return uploadParamDTO;

    }

    /**
     * @param slbh slbh
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据项目ID删除文件中心节点
     */
    @CheckTokenAno
    @RequestMapping(value = "/deleteXmNode/{slbh}", method = RequestMethod.POST)
    public void deleteXmNode(
            @PathVariable("slbh") String slbh) {
        platformUtil.deleteNode(slbh);
    }

    /**
     * @param roles 角色id
     * @param wdid  wdid
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取项目菜单
     */
    @CheckTokenAno
    @RequestMapping(value = "/getProjectMenu/{roles}/{wdid}")
    public List<PfResourceVo> getProjectMenu(
            @PathVariable("roles") String roles,
            @PathVariable("wdid") String wdid) {
        if (StringUtils.equals("roles", roles)) {
            roles = "";
        }
        return sysMenuService.getProjectMenu(roles, wdid);
    }

    /**
     * @param slbh   受理编号
     * @param clmc   材料名称
     * @param qtcwid 其他错误id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取上传文件中心参数对象
     */
    @CheckTokenAno
    @RequestMapping(value = "/getqtcwsccs/{slbh}/{clmc}/{qtcwid}/{userId}", method = RequestMethod.POST)
    public UploadParamDTO getQtcwsccs(
            @PathVariable("slbh") String slbh,
            @PathVariable("clmc") String
                    clmc, @PathVariable("qtcwid") String qtcwid, @PathVariable("userId") String userId) {
        UploadParamDTO uploadParamDTO = null;
        if (StringUtils.isNotBlank(slbh)) {
            uploadParamDTO = new UploadParamDTO();
            String fileCenterUrl = "fcm";
            if (super.fileCenterUrl != null && super.fileCenterUrl.length() > 4) {
                fileCenterUrl += super.fileCenterUrl.substring(4);
            }
            uploadParamDTO.setFileCenterUrl(fileCenterUrl);
            PfUserVo pfUserVo = sysUserService.getUserVo(userId);
            Node parentNode = platformUtil.getNodeBySlbh(slbh);
            if (parentNode != null && parentNode.getId() != null) {
                Integer secondId = platformUtil.createChildNodeByClmc(parentNode, clmc);
                uploadParamDTO.setNodeId(String.valueOf(platformUtil.createFileFolderByClmc(secondId, qtcwid, pfUserVo.getUserName())));
            }
        }
        return uploadParamDTO;

    }


    /**
     * @param slbh 受理编号
     * @param clmc 材料名称
     * @param
     * @return
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @description 获取其他错误文件夹ID号
     */
    @CheckTokenAno
    @RequestMapping(value = "/getMainID/{slbh}/{clmc}", method = RequestMethod.POST)
    public Integer getmainID(
            @PathVariable("slbh") String slbh,
            @PathVariable("clmc") String clmc) {
        Integer secondId = null;
        Node parentNode = platformUtil.getNodeBySlbh(slbh);
        if (parentNode != null && parentNode.getId() != null) {
            secondId = platformUtil.createChildNodeByClmc(parentNode, clmc);
        }
        return secondId;
    }

    /**
     * @param nodeId 节点id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据节点id删除文件节点
     */
    @CheckTokenAno
    @RequestMapping(value = "/deleteNodeById/{nodeId}", method = RequestMethod.POST)
    public void deleteNodeById(
            @PathVariable("nodeId") Integer nodeId) {
        platformUtil.deleteNodeById(nodeId);
    }

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    @CheckTokenAno
    @RequestMapping(value = "/tasklist", method = RequestMethod.POST)
    public Object getTaskList(
            @RequestBody Map<String, Object> map) {
        Map<String, Integer> pageable = (Map<String, Integer>) map.get("pageable");
        Map<String, String> param = (Map<String, String>) map.get("param");
        int pageNum = pageable.get("pageNumber") - 1;
        int pageSize = pageable.get("pageSize");
        return repository.selectPaging("getTaskListByPage", param, pageNum, pageSize);
    }

    /**
     * @param map 参数
     * @return 已办任务
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取已办任务
     */
    @RequestMapping(value = "/taskoverlist", method = RequestMethod.POST)
    public Object getTaskOverList(
            @RequestBody Map<String, Object> map) {
        Map<String, Integer> pageable = (Map<String, Integer>) map.get("pageable");
        Map<String, String> param = (Map<String, String>) map.get("param");
        int pageNum = pageable.get("pageNumber") - 1;
        int pageSize = pageable.get("pageSize");
        return repository.selectPaging("getTaskOverListByPage", param, pageNum, pageSize);
    }

    /**
     * @param map 参数
     * @return 项目列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 项目列表
     */
    @CheckTokenAno
    @RequestMapping(value = "/projectlist", method = RequestMethod.POST)
    public Object getProjectList(
            @RequestBody Map<String, Object> map) {
        Map<String, Integer> pageable = (Map<String, Integer>) map.get("pageable");
        Map<String, String> param = (Map<String, String>) map.get("param");
        int pageNum = pageable.get("pageNumber") - 1;
        int pageSize = pageable.get("pageSize");
        return repository.selectPaging("getProjectListByPage", param, pageNum, pageSize);
    }

    /**
     * @param actStProRelDo 流程扩展表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 插入或更新
     */
    @CheckTokenAno
    @RequestMapping(value = "/saveorupdatetaskextenddto", method = RequestMethod.POST)
    public void saveOrUpdateTaskExtendDto(
            @RequestBody ActStProRelDo actStProRelDo) {
        Example example = new Example(ActStProRelDo.class);
        example.createCriteria().andEqualTo("procInsId", actStProRelDo.getProcInsId());
        List<ActStProRelDo> actStProRelDoList = entityMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(actStProRelDoList)) {
            actStProRelDo.setId(UUIDGenerator.generate());
            entityMapper.saveOrUpdate(actStProRelDo, actStProRelDo.getId());
        } else {
            for (ActStProRelDo actStProRel : actStProRelDoList) {
                actStProRelDo.setId(actStProRel.getId());
                entityMapper.saveOrUpdate(actStProRelDo, actStProRelDo.getId());
            }
        }
    }

    /**
     * @param wiid 流程是咧id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据流程实例id删除流程扩展表
     */
    @CheckTokenAno
    @RequestMapping(value = "/deleteTaskExtendDto/{wiid}", method = RequestMethod.POST)
    public void deleteTaskExtendDto(
            @PathVariable("wiid") String wiid) {
        Example example = new Example(ActStProRelDo.class);
        example.createCriteria().andEqualTo("procInsId", wiid);
        entityMapper.deleteByExample(example);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取工作流信息列表
     */
    @ApiOperation(value = "获取工作流信息列表")
    @GetMapping(value = "/process/list")
    @ResponseStatus(code = HttpStatus.OK)
    @CheckTokenAno
    public List<ProcessDefData> getWorkflowDefinitionList() {
        return processService.getWorkflowDefinitionList();
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取节点信息列表
     */
    @ApiOperation(value = "获取节点信息列表")
    @PostMapping(value = "/process/{gzldyid}")
    @ApiImplicitParams({@ApiImplicitParam(name = "gzldyid", value = "工作流定义ID", required = true, paramType = "path", dataType = "string")})
    @ResponseStatus(code = HttpStatus.OK)
    @CheckTokenAno
    public List<UserTaskDto> getUserTaskDtoListByGzldyid(
            @PathVariable("gzldyid") String gzldyid) {
        return processService.getUserTaskDtoListByGzldyid(gzldyid);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取部门信息列表
     */
    @ApiOperation(value = "获取部门信息列表")
    @PostMapping(value = "/department/list")
    @CheckTokenAno
    public List<PfOrganVo> getPfOrganVoList() {
        return sysUserService.getOrganList();
    }

    /**
     * @param organid 部门ID
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取部门中的所有角色信息列表
     */
    @ApiOperation(value = "获取部门中的所有角色信息列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/role/list/organ/{organid}")
    @CheckTokenAno
    public List<PfRoleVo> getRoleListByOrganid(
            @PathVariable("organid") String organid) {
        Map param = new HashMap();
        param.put("organId", organid);
        return platformMapper.getRoleList(param);
    }

    @ApiOperation(value = "获取用户所有角色信息列表")
    @GetMapping(value = "/role/list/user/{userid}")
    @CheckTokenAno
    public List<PfRoleVo> getRoleListByUserid(
            @PathVariable("userid") String userid) {
        return sysUserService.getRoleListByUser(userid);
    }

    @ApiOperation(value = "获取共享测绘工程材料下载地址")
    @PostMapping(value = "/gxchgcclxx/{babh}")
    @CheckTokenAno
    public Map getGxchgcclDownUrl(
            @RequestBody Map paramMap,
            @PathVariable("babh") String babh) {
        return gxchgcxxService.getGxchgcclDownUrl(paramMap, babh);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/user/{loginName}")
    @CheckTokenAno
    public PfUserVo getUserByloginName(@PathVariable("loginName") String loginName) {
        return sysUserService.getUserByloginName(loginName);
    }

    @ApiOperation(value = "获取部门信息")
    @GetMapping(value = "/organ/{shr}")
    @CheckTokenAno
    public List<PfOrganVo> getOrganListByUser(
            @PathVariable("shr") String shr) {
        return sysUserService.getOrganListByUser(shr);
    }

    @CheckTokenAno
    @ApiOperation(value = "测试")
    @GetMapping(value = "/demo/get")
    public String testget() {
        return "hello get";
    }

    @CheckTokenAno
    @ApiOperation(value = "测试")
    @PostMapping(value = "/demo/post")
    public String testpost() {
        return "hello psot";
    }

    @ApiOperation(value = "获取流程信息")
    @GetMapping(value = "/getProcessInfoBySlbh/{slbh}")
    @CheckTokenAno
    public List<Map> getProcessInfoBySlbh(
            @PathVariable("slbh") String slbh) {
        Map paramMap = Maps.newHashMap();
        paramMap.put("slbh", slbh);
        return platformMapper.getProcessInfoBySlbh(paramMap);
    }

    //endregion

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 注册新用户
     */
    @CheckTokenAno
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseMessage register(
            @RequestBody UserDto userDto) {
        Map resultMap = new HashMap<>();

        if (null != userDto && StringUtils.isNoneBlank(userDto.getUserId(), userDto.getLoginPassword(), userDto.getLoginName())) {
            try {
                //验证用户是否存在
                boolean usernameFlag = platUserService.loginNameExists(userDto.getLoginName());
                boolean phoneFlag = false;
                if (usernameFlag) {
                    resultMap.put("code", ResponseMessage.CODE.USERREPEAT_FAIL.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.USERREPEAT_FAIL.getMsg());
                } else if (StringUtils.isNotEmpty(userDto.getMobilePhone())) {
                    //验证手机号是否注册过
                    phoneFlag = platUserService.mobilePhoneExists(userDto.getMobilePhone());
                    if (phoneFlag) {
                        resultMap.put("code", ResponseMessage.CODE.PHONEREPEAT_FAIL.getCode());
                        resultMap.put("msg", ResponseMessage.CODE.PHONEREPEAT_FAIL.getMsg());
                    }
                }
                if (!usernameFlag && !phoneFlag) {
                    // 用户名和手机都未注册，才能进行注册
                    logger.info("***************注册参数**************" + JSON.toJSONString(userDto));
                    resultMap.putAll(platUserService.registerNewUser(userDto));
                    logger.info("***************注册返回结果**************" + resultMap.toString());
                }
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 注册新用户
     */
    @CheckTokenAno
    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public ResponseMessage updateuser(
            @RequestBody UserDto userDto) {
        Map resultMap = new HashMap<>();
        if (null != userDto && StringUtils.isNotEmpty(userDto.getUserId())) {
            // 验证用户是否存在
            try {
                resultMap.putAll(platUserService.updateUser(userDto));
                logger.info("***************更新用户信息**************" + resultMap.toString());
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                resultMap.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 注册新用户
     */
    @CheckTokenAno
    @RequestMapping(value = "/userroles", method = RequestMethod.POST)
    public ResponseMessage userRoleList(
            @RequestBody UserAuthParamDto userAuthParamDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = new HashMap<>();
        if (null != userAuthParamDto && CollectionUtils.isNotEmpty(userAuthParamDto.getUseridList())) {
            // 验证用户是否存在
            try {
                for (String userid : userAuthParamDto.getUseridList()) {
                    List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(userid);
                    resultMap.put(userid, pfRoleVoList);
                }
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 注册手机号重复
     */
    @CheckTokenAno
    @RequestMapping(value = "/sjhcf", method = RequestMethod.POST)
    public ResponseMessage sjhcf(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getMobilePhone())) {
            // 验证用户是否存在
            try {
                resultMap.put("sjhcf", platUserService.mobilePhoneExists(userDto.getMobilePhone()));
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 注册新用户
     */
    @CheckTokenAno
    @RequestMapping(value = "/yhmcf", method = RequestMethod.POST)
    public ResponseMessage yhmcf(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getLoginName())) {
            // 验证用户是否存在
            try {
                resultMap.put("yhmcf", platUserService.loginNameExists(userDto.getLoginName()));
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 用户可用
     */
    @CheckTokenAno
    @RequestMapping(value = "/uservalid", method = RequestMethod.POST)
    public ResponseMessage uservalid(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getLoginName())) {
            // 验证用户是否可用
            try {
                resultMap.put("uservalid", platUserService.userIsValid(userDto.getLoginName()));
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }


    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 获取部门用户信息
     */
    @CheckTokenAno
    @RequestMapping(value = "/organ/user/list", method = RequestMethod.POST)
    public ResponseMessage getUserListByOrgan(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getOrganId())) {
            // 验证用户是否存在
            try {
                List<PfUserVo> list = platUserService.getUserListByOrgan(userDto);
                resultMap.put("userList", list);
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/6 16:55
     * @description 获取部门用户信息
     */
    @CheckTokenAno
    @RequestMapping(value = "/organ/user/isvalid", method = RequestMethod.POST)
    public ResponseMessage checkUserIsValidByOrgan(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getOrganId())) {
            // 验证用户是否存在
            try {
                List<PfUserVo> list = platUserService.getUserListByOrgan(userDto);
                resultMap.put("userList", list);
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 通过用户名判断该用户是否有效
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @RequestMapping(value = "/user/isvalid", method = RequestMethod.POST)
    public ResponseMessage checkUserIsValidByLoginName(
            @RequestBody UserDto userDto) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNotBlank(userDto.getLoginName())) {
            // 验证用户是否存在
            try {
                boolean isValid = platUserService.checkUserIsValidByLoginName(userDto.getLoginName());
                resultMap.put("data", isValid + "");
                responseMessage.setData(resultMap);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                responseMessage.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            }
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    /**
     * @param map
     * @return
     * @description 2021/1/6 用户管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/queryUser")
    public ResponseMessage queryUser(@RequestBody Map<String, String> map) {
        return ResponseUtil.wrapResponseBodyByPage(platUserService.queryUserByUsername(map));
    }

    /**
     * @param username
     * @param password
     * @return
     * @description 2021/1/5 修改密码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/changePassword/{username}/{password}/{passwordNew}")
    public ResponseMessage changePassword(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("passwordNew") String passwordNew) {
        return ResponseUtil.wrapResponseBodyByCodeMap(platUserService.changePassword(username, password, passwordNew));
    }

    /**
     * @param userid
     * @param state
     * @return
     * @description 2021/1/5 启用或停用用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/changeUserState/{userid}/{state}")
    public ResponseMessage changeUserState(@PathVariable("userid") String userid, @PathVariable("state") String state) {
        return ResponseUtil.wrapResponseBodyByCodeMap(platUserService.changeUserState(userid, state));
    }

    /**
     * @param userid
     * @param roles
     * @return
     * @description 2021/1/5 用户分配权限
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/distributionRoleAuthority/{userid}/{roles}")
    public ResponseMessage distributionRoleAuthority(@PathVariable("userid") String userid, @PathVariable("roles") String roles) {
        return ResponseUtil.wrapResponseBodyByCodeMap(platUserService.distributionRoleAuthority(userid, roles));
    }

    /**
     * @param
     * @return
     * @description 2021/1/6 获取所有角色
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @GetMapping(value = "/user/queryAllRole")
    public ResponseMessage queryAllRole(@RequestBody RoleDto roleDto) {
        ResponseMessage message = new ResponseMessage();
        try {
            message = platUserService.queryAllRole(roleDto);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param sjhm
     * @return
     * @description 2021/1/5 通过手机号判断该手机号是否注册过
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/phoneNumberIsRegistered/{sjhm}")
    public ResponseMessage phoneNumberIsRegistered(@PathVariable("sjhm") String sjhm) {
        return platUserService.phoneNumberIsRegistered(sjhm);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 验证完手机验证码之后保存用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/saveUser")
    public ResponseMessage saveUser(@RequestBody UserDto userDto) {
        return platUserService.saveUser(userDto);
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 通过用户名和密码获取用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/getLocalAuthByUsernameAndPwd")
    public ResponseMessage getLocalAuthByUsernameAndPwd(@RequestBody UserDto userDto) {
        return platUserService.getLocalAuthByUsernameAndPwd(userDto);
    }

    /**
     * @param dwbh
     * @return
     * @description 2021/5/12  通过单位名称获取对用的organ实体
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @CheckTokenAno
    @PostMapping(value = "/user/queryOrganListByDwbh/{dwbh}")
    public ResponseMessage queryOrganListByDwbh(@PathVariable("dwbh") String dwbh) {
        return platUserService.queryOrganListByDwbh(dwbh);
    }

    @CheckTokenAno
    @PostMapping(value = "/deleteOtherAssignment/{taskid}/{userid}")
    public ResponseMessage deleteOtherAssignment(@PathVariable(name = "taskid") String taskid, @PathVariable(name = "userid") String userid) {
        ResponseMessage message;
        if (StringUtils.isNoneBlank(taskid, userid)) {
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("taskid", taskid);
            Assignment assignment = entityMapper.selectByPrimaryKey(Assignment.class, taskid);
            String activityId = assignment.getActivityId();

            try {
                Example exampleIsExist = new Example(Assignment.class);
                Example example = new Example(Assignment.class);
                exampleIsExist.createCriteria().andEqualTo("activityId", activityId).andEqualTo("userId", userid);
                example.createCriteria().andEqualTo("activityId", activityId).andNotEqualTo("userId", userid);
                List<Assignment> assignmentList = entityMapper.selectByExample(exampleIsExist);
                if (CollectionUtils.isNotEmpty(assignmentList)) {
                    entityMapper.deleteByExample(example);
                    message = ResponseUtil.wrapSuccessResponse();
                } else {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PROCESSHAVINGDONE_FAIL.getMsg(), ResponseMessage.CODE.PROCESSHAVINGDONE_FAIL.getCode());
                }
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                LOGGER.error("错误原因:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }


    @CheckTokenAno
    @GetMapping(value = "/delTaskByTaskid/{taskid}")
    public ResponseMessage delTaskByTaskid(@PathVariable("taskid") String taskid) {
        ResponseMessage message;
        if (StringUtils.isNotBlank(taskid)) {
            int flag = entityMapper.deleteByPrimaryKey(Assignment.class, taskid);
            if (flag > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapExceptionResponse();
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

}
