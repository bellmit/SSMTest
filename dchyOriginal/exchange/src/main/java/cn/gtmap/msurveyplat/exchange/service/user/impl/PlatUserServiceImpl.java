package cn.gtmap.msurveyplat.exchange.service.user.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.dto.RoleDto;
import cn.gtmap.msurveyplat.common.dto.UserDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.vo.*;
import cn.gtmap.msurveyplat.exchange.service.user.PlatUserService;
import cn.gtmap.msurveyplat.exchange.util.Constants;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PlatUserServiceImpl implements PlatUserService {

    private Logger logger = LoggerFactory.getLogger(PlatUserServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private SysUserService sysUserService;

    @Resource(name = "repository")
    Repository repository;

    private String JSDWGLY = AppConfig.getProperty("jsdw.dwgly.roleid");

    private String CHDWGLY = AppConfig.getProperty("chdw.dwgly.roleid");

    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: loginName
     * @time 2021/5/7 16:51
     * @description ????????????????????????
     */
    @Override
    public boolean loginNameExists(String loginName) {
        boolean flag = false;
        Example example = new Example(PfUser.class);
        example.createCriteria().andEqualTo("loginName", loginName);
        List<PfUser> userList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userList)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @param loginName
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: loginName
     * @time 2021/5/13 14:07
     * @description ????????????????????????
     */
    @Override
    public boolean userIsValid(String loginName) {
        boolean flag = false;
        Example example = new Example(PfUser.class);
        example.createCriteria().andEqualTo("loginName", loginName).andEqualTo("isValid", Constants.VALID);
        List<PfUser> userList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userList) && userList.size() == 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: mobilePhone
     * @time 2021/5/7 16:51
     * @description ????????????????????????
     */
    @Override
    public boolean mobilePhoneExists(String mobilePhone) {
        boolean flag = false;
        Example example = new Example(PfUser.class);
        example.createCriteria().andEqualTo("mobilePhone", mobilePhone);
        List<PfUser> phoneList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(phoneList)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/7 16:51
     * @description ???????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map registerNewUser(UserDto userDto) {
        Map resultMap = Maps.newHashMap();
        if (null != userDto && StringUtils.isNoneBlank(userDto.getLoginName(), userDto.getLoginPassword(), userDto.getUserId()) && CollectionUtils.isNotEmpty(userDto.getRoleIdList())) {
            //???????????????roleid   ??????????????????????????????fdm  28224EAEE9A944CDB0ACB247DB9CA6D3
            List<String> roleidList = userDto.getRoleIdList();

            //pf_User????????????
            PfUser pfUser = new PfUser();
            pfUser.setUserId(userDto.getUserId());
            pfUser.setUserName(userDto.getUserName());
            pfUser.setLoginName(userDto.getLoginName());
            pfUser.setMobilePhone(userDto.getMobilePhone());
            //??????spring?????????MD5??????
            pfUser.setLoginPassword(DigestUtils.md5DigestAsHex(userDto.getLoginPassword().getBytes()));
            pfUser.setIsValid(userDto.getIsvalid());
            entityMapper.insertSelective(pfUser);

            //PF-ORGAN????????????
            PfOrgan pfOrgan = new PfOrgan();
            // ??????????????????????????????  ??????organid???organ?????????????????????,
            // ??????????????????organname???????????????,????????????uuid
            Example exampleUtilOrgan = new Example(PfOrgan.class);
            exampleUtilOrgan.createCriteria().andEqualTo("organId", userDto.getOrganId());
            List<PfOrgan> pfOrganList = entityMapper.selectByExample(exampleUtilOrgan);
            if (CollectionUtils.isEmpty(pfOrganList)) {
                pfOrgan.setOrganId(userDto.getOrganId());
                pfOrgan.setOrganName(userDto.getOrganName());
                entityMapper.insertSelective(pfOrgan);
            }
            //pf-user-role-rel????????????
            if (CollectionUtils.isNotEmpty(roleidList)) {
                List<PfUserRoleRel> pfUserRoleRelList = Lists.newArrayList();
                for (String roleid : roleidList) {
                    //pf-user-role-rel????????????
                    PfUserRoleRel pfUserRoleRel = new PfUserRoleRel();
                    pfUserRoleRel.setUser_id(userDto.getUserId());
                    pfUserRoleRel.setUrr_id(UUIDGenerator.generate18());
                    pfUserRoleRel.setRole_id(roleid);
                    pfUserRoleRelList.add(pfUserRoleRel);
                }
                entityMapper.insertBatchSelective(pfUserRoleRelList);
            }

            //pf-user-organ-rel????????????
            PfUserOrganRel pfUserOrganRel = new PfUserOrganRel();
            pfUserOrganRel.setUdr_id(UUIDGenerator.generate18());
            pfUserOrganRel.setUser_id(userDto.getUserId());
            pfUserOrganRel.setOrgan_id(userDto.getOrganId());
            entityMapper.insertSelective(pfUserOrganRel);
            resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
            resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    /**
     * @param userDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userDto
     * @time 2021/5/7 16:53
     * @description ????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map updateUser(UserDto userDto) {
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(userDto.getIsvalid())) {
            if (StringUtils.isNotBlank(userDto.getIsvalid())) {
                PfUser pfUser = new PfUser();
                pfUser.setUserId(userDto.getUserId());
                pfUser.setIsValid(userDto.getIsvalid());
                entityMapper.updateByPrimaryKeySelective(pfUser);
            }

            if (CollectionUtils.isNotEmpty(userDto.getRoleIdList())) {
                Example example = new Example(PfUserRoleRel.class);
                example.createCriteria().andEqualTo("user_id", userDto.getUserId());
                entityMapper.deleteByExampleNotNull(example);

                //pf-user-role-rel????????????
                List<PfUserRoleRel> pfUserRoleRelList = Lists.newArrayList();
                for (String roleid : userDto.getRoleIdList()) {
                    //pf-user-role-rel????????????
                    PfUserRoleRel pfUserRoleRel = new PfUserRoleRel();
                    pfUserRoleRel.setUser_id(userDto.getUserId());
                    pfUserRoleRel.setUrr_id(UUIDGenerator.generate18());
                    pfUserRoleRel.setRole_id(roleid);
                    pfUserRoleRelList.add(pfUserRoleRel);
                }
                entityMapper.insertBatchSelective(pfUserRoleRelList);
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    @Override
    public List<PfUserVo> getUserListByOrgan(UserDto userDto) {
        List<PfUserVo> pfUserVoList = Lists.newArrayList();
        if (null != userDto && StringUtils.isNotBlank(userDto.getOrganId())) {
            pfUserVoList = sysUserService.getUserListByOragn(userDto.getOrganId());
        }
        return pfUserVoList;
    }

    /**
     * @param loginName
     * @return
     * @description 2021/5/12 ???????????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public boolean checkUserIsValidByLoginName(String loginName) {
        boolean flag = false;
        if (StringUtils.isNotBlank(loginName)) {
            Example example = new Example(PfUser.class);
            example.createCriteria().andEqualTo("loginName", loginName).andEqualTo("isValid", "1");
            List<PfUser> userList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(userList)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Page<Map<String, Object>> queryUserByUsername(Map map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int size = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String yhlx = CommonUtil.formatEmptyValue(MapUtils.getString(map, "yhlx"));
        switch (yhlx) {
            //????????????
            case "1":
                map.put("roleid", JSDWGLY);
                break; //??????
            //????????????
            case "2":
                map.put("roleid", CHDWGLY);
                break; //??????
            default: //?????????????????????????????????????????????????????????
                map.put("roleid", "");
                break;
        }
//        map.put("roleid", Constants.DCHY_XMGL_CHDW_DWGLY_ROWID);
        logger.info("***********??????:" + map);
        return repository.selectPaging("queryUserByUsernameByPage", map, page - 1, size);
    }

    @Override
    public Map<String, String> changePassword(String username, String password, String passwordNew) {
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(username, password, passwordNew)) {
            Example example = new Example(PfUser.class);
            example.createCriteria().andEqualTo("loginName", username);
            List<PfUser> userList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(userList)) {
                PfUser pfUser = userList.get(0);
                if (StringUtils.equals(pfUser.getLoginPassword(), DigestUtils.md5DigestAsHex(password.getBytes()))) {
                    pfUser.setLoginPassword(DigestUtils.md5DigestAsHex(passwordNew.getBytes()));
                    entityMapper.saveOrUpdate(pfUser, pfUser.getUserId());
                    resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                } else {
                    resultMap.put("code", ResponseMessage.CODE.PASSWORD_FAIL.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.PASSWORD_FAIL.getMsg());
                }

            } else {
                resultMap.put("code", ResponseMessage.CODE.USERNOTEXIST_FAIL.getCode());
                resultMap.put("msg", ResponseMessage.CODE.USERNOTEXIST_FAIL.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    /**
     * @param sjhm
     * @return
     * @description 2021/5/12 ????????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage phoneNumberIsRegistered(String sjhm) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(sjhm)) {
            Example example = new Example(PfUser.class);
            example.createCriteria().andEqualTo("mobilePhone", sjhm);
            List<PfUser> pfUsers = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(pfUsers)) {
                resultMap.put("isRegistered", "true");
                resultMap.put("data", pfUsers.get(0));
            }
            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
            message.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            message.setData(resultMap);
        } else {
            message.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            message.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return message;
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 ????????????(???????????????????????????)
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage saveUser(UserDto userDto) {
        ResponseMessage message = new ResponseMessage();
        String userId = userDto.getUserId();
        String loginName = userDto.getLoginName();
        String password = userDto.getLoginPassword();

        if (StringUtils.isNoneBlank(userId, loginName, password)) {
            PfUser pfUser = entityMapper.selectByPrimaryKey(PfUser.class, userId);
            pfUser.setLoginName(loginName);
            pfUser.setLoginPassword(password);
            int flag = entityMapper.saveOrUpdate(pfUser, pfUser.getUserId());
            message = ResponseUtil.wrapResponseBodyByCRUD(flag);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    /**
     * @param userDto
     * @return
     * @description 2021/5/12 ??????????????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage getLocalAuthByUsernameAndPwd(UserDto userDto) {
        ResponseMessage message = new ResponseMessage();
        String loginName = userDto.getLoginName();
        String password = userDto.getLoginPassword();

        if (StringUtils.isNoneBlank(loginName, password)) {
            Example example = new Example(PfUser.class);
            String tempPassword = DigestUtils.md5DigestAsHex(CommonUtil.formatEmptyValue(password).getBytes());
            example.createCriteria().andEqualTo("loginName", loginName).andEqualTo("loginPassword", tempPassword);
            List<PfUser> pfUsers = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(pfUsers)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("data", pfUsers.get(0));
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        return message;
    }

    @Override
    public Map<String, String> changeUserState(String userid, String state) {
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(userid, state)) {
            PfUser pfUser = entityMapper.selectByPrimaryKey(PfUser.class, userid);
            if (null != pfUser) {
                pfUser.setIsValid(state);
                entityMapper.saveOrUpdate(pfUser, pfUser.getUserId());
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
            } else {
                resultMap.put("code", ResponseMessage.CODE.USERNOTEXIST_FAIL.getCode());
                resultMap.put("msg", ResponseMessage.CODE.USERNOTEXIST_FAIL.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Map<String, String> distributionRoleAuthority(String userid, String roles) {
        String[] roleList = roles.split(";");
        Map resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(userid) && null != roleList && roleList.length > 0) {
            Example example = new Example(PfUserRoleRel.class);
            example.createCriteria().andEqualTo("user_id", userid);
            entityMapper.deleteByExample(example);

            for (String string : roleList) {
                PfUserRoleRel pfUserRoleRel = new PfUserRoleRel();
                pfUserRoleRel.setUser_id(userid);
                pfUserRoleRel.setRole_id(string);
                pfUserRoleRel.setUrr_id(UUIDGenerator.generate18());
                entityMapper.insertSelective(pfUserRoleRel);
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
            }

        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    @Override
    public ResponseMessage queryAllRole(RoleDto roleDto) {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        List<PfRole> roleList = Lists.newArrayList();
        List<String> roleidList = Lists.newArrayList();
        String jsdwRole = roleDto.getJsdwRole();
        String chdwRole = roleDto.getChdwRole();
        String jsdwRoles = roleDto.getChdwRole();
        String chdwRoles = roleDto.getChdwRoles();

        logger.info("??????????????????id:" + jsdwRole);
        logger.info("??????????????????????????????id:" + jsdwRoles);
        logger.info("??????????????????id:" + chdwRole);
        logger.info("??????????????????????????????id:" + chdwRoles);
        if (StringUtils.isNoneBlank(jsdwRole, jsdwRoles, chdwRole, chdwRoles)) {
            String userid = "";
            Example exampleUser = new Example(PfUserRoleRel.class);
            exampleUser.createCriteria().andEqualTo("user_id", userid);
//
//            UserInfo userInfo = UserUtil.getCurrentUser();

//            if (userInfo.isAdmin()) {
//                roleList = entityMapper.selectByExample(new Example(PfRole.class));
//                resultMap.put("data", roleList);
//                message.setData(resultMap);
//                message = ResponseUtil.wrapSuccessResponse();
//                return message;
//            }
            List<PfUserRoleRel> pfUserRoleRels = entityMapper.selectByExample(exampleUser);
            //????????????,???????????????????????????,?????????????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(pfUserRoleRels)) {
                for (PfUserRoleRel pfUserRoleRel : pfUserRoleRels) {
                    roleidList.add(pfUserRoleRel.getRole_id());
                }
                //????????????????????????????????????????????????
                if (roleidList.contains(jsdwRole)) {
                    roleList = formatRoles(jsdwRoles);
                } else if (roleidList.contains(chdwRole)) {
                    roleList = formatRoles(chdwRoles);
                }

                resultMap.put("data", roleList);
                message = ResponseUtil.wrapSuccessResponse();
                message.setData(resultMap);
            } else {
                message.getHead().setCode(ResponseMessage.CODE.CONFIG_FAIL.getCode());
                message.getHead().setMsg(ResponseMessage.CODE.CONFIG_FAIL.getMsg());
            }

        } else {
            message.getHead().setCode(ResponseMessage.CODE.CONFIG_FAIL.getCode());
            message.getHead().setMsg(ResponseMessage.CODE.CONFIG_FAIL.getMsg());
        }

        return message;
    }

    /**
     * @param dwbh
     * @return
     * @description 2021/5/12 ?????????????????????????????????organ??????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage queryOrganListByDwbh(String dwbh) {
        logger.info("************??????organ?????????dwbh************" + dwbh);
        ResponseMessage message = new ResponseMessage();
        if (StringUtils.isNotBlank(dwbh)) {
            Example exampleUtilOrgan = new Example(PfOrgan.class);
            exampleUtilOrgan.createCriteria().andEqualTo("organId", dwbh);
            List<PfOrgan> pfOrganList = entityMapper.selectByExample(exampleUtilOrgan);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("data", pfOrganList);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getMsg(), ResponseMessage.CODE.PARAMETER_FAIL.getCode());
        }
        logger.info("************??????organ??????????????????************" + JSON.toJSONString(message));
        return message;
    }

    //????????????????????????
    public List<PfRole> formatRoles(String roles) {
        List<PfRole> roleList = Lists.newArrayList();
        if (StringUtils.isNotBlank(roles)) {
            String[] strings = roles.split(";");
            if (null != strings && strings.length > 0) {
                for (String string : strings) {
                    Example example = new Example(PfRole.class);
                    example.createCriteria().andEqualTo("roleId", string);
                    List<PfRole> pfRoles = entityMapper.selectByExample(example);
                    roleList.addAll(pfRoles);
                }
            }
        }
        return roleList;
    }
}
