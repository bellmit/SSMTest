package cn.gtmap.msurveyplat.portalol.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglJsdw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.common.vo.PfOrgan;
import cn.gtmap.msurveyplat.common.vo.PfUser;
import cn.gtmap.msurveyplat.portalol.core.service.impl.DchyXmglZdServiceImpl;
import cn.gtmap.msurveyplat.portalol.core.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.portalol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.portalol.utils.*;
import cn.gtmap.msurveyplat.portalol.utils.token.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DchyXmglYhdwServiceImpl implements DchyXmglYhdwService {

    private static final Logger logger = LoggerFactory.getLogger(DchyXmglYhdwServiceImpl.class);

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Autowired
    private DchyXmglZdServiceImpl dchyXmglZdService;

    @Autowired
    private ServiceOlFeignUtil serviceOlFeignUtil;

    @Autowired
    private ExchangeFeignServiceImpl exchangeFeignService;

    @Value("${jsdw.dwgly.roleid}")
    public String JSDWGLY;

    @Value("${chdw.dwgly.roleid}")
    public String CHDWGLY;

    @Override
    public DchyXmglYhdw getDchyXmglYhdwByUserId(String userid) {
        if (StringUtils.isNotBlank(userid)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhid", userid);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
                DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                return dchyXmglYhdw;
            }
        }
        return null;
    }

    /**
     * @param yhzjhm
     * @return
     * @description 2020/12/30 ??????yhzjhm??????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public DchyXmglYhdw getDchyXmglYhdwByZjhm(String yhzjhm) {
        if (StringUtils.isNotBlank(yhzjhm)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("yhzjhm", SM4Util.encryptData_ECB(yhzjhm));
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
                DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                return dchyXmglYhdw;
            }
        }
        return null;
    }

    /**
     * @param tyshxydm
     * @return
     * @description 2020/12/30 ??????tyshxydm??????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public DchyXmglYhdw getValidDchyXmglYhdwByTyshxydm(String tyshxydm) {
        if (StringUtils.isNotBlank(tyshxydm)) {
            Example example = new Example(DchyXmglYhdw.class);
            example.createCriteria().andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm)).andEqualTo("isvalid", Constants.VALID);
            List<DchyXmglYhdw> dchyXmglYhdwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdwList)) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwList.get(0);
                DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
                return dchyXmglYhdw;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> resetPassword(String sjhm, String password, String yzm) {
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(sjhm, yzm, password)) {
            resultMap = yzmIsVaild(sjhm, yzm);
            if (StringUtils.equals(ResponseMessage.CODE.SUCCESS.getCode(), CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "code")))) {
                ResponseMessage message = exchangeFeignService.phoneNumberIsRegistered(sjhm);
                if (StringUtils.equals(message.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
                    Map<String, Object> data = message.getData();
                    if (MapUtils.isNotEmpty(data) && StringUtils.equals("true", CommonUtil.formatEmptyValue(MapUtils.getString(data, "isRegistered")))) {
                        PfUser pfUser = (PfUser) data.get("data");
                        UserDto userDto = new UserDto();
                        userDto.setUserId(pfUser.getUserId());
                        userDto.setLoginName(pfUser.getLoginName());
                        userDto.setLoginPassword(pfUser.getLoginPassword());
                        ResponseHead head = exchangeFeignService.saveUser(userDto).getHead();
                        resultMap.put("code", head.getCode());
                        resultMap.put("msg", head.getMsg());
                    }
                } else {
                    resultMap.put("code", message.getHead().getCode());
                    resultMap.put("msg", message.getHead().getMsg());
                }
            } else {
                resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
                resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> yzmIsVaild(String sjhm, String yzm) {
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isNoneBlank(sjhm, yzm)) {
            boolean flag = phoneIsRepet(sjhm);
            if (flag) {
                String key = sjhm + "_" + yzm;
                String token = TokenUtil.getTokenByCode(key);
                //??????token??????
                if (TokenUtil.tokenVaild(token)) {
                    resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                } else {
                    resultMap.put("code", ResponseMessage.CODE.TOKEN_EXPIRE.getCode());
                    resultMap.put("msg", ResponseMessage.CODE.TOKEN_EXPIRE.getMsg());
                }
            } else {
                resultMap.put("code", ResponseMessage.CODE.PHONENOTREPEAT_FAIL.getCode());
                resultMap.put("msg", ResponseMessage.CODE.PHONENOTREPEAT_FAIL.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            resultMap.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return resultMap;
    }

    @Override
    public PfUser getLocalAuthByUsernameAndPwd(String username, String password) {
        UserDto userDto = new UserDto();
        userDto.setLoginName(username);
        userDto.setLoginPassword(password);
        ResponseMessage message = exchangeFeignService.getLocalAuthByUsernameAndPwd(userDto);
        if (StringUtils.equals(message.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
            Map<String, Object> data = message.getData();
            logger.info("************???????????????????????????????????????" + JSON.toJSONString(message));
            if (MapUtils.isNotEmpty(data)) {
                return JSON.parseObject(JSON.toJSONString(data.get("data")), PfUser.class);
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, String> registeredYhdw(Map<String, Object> data, MultipartFile[] files) {
        Map resultMap = Maps.newHashMap();
        if (null != files) {
            ResponseMessage message = fileUpload(data, files);
            String wjzxid = CommonUtil.formatEmptyValue(message.getData().get("wjzxid"));
            if (StringUtils.isNotBlank(wjzxid)) {
                data.put("wjzxid", wjzxid);
            } else {
                resultMap.put("code", ResponseMessage.CODE.FILE_NOT_UPLOAD.getCode());
                resultMap.put("msg", ResponseMessage.CODE.FILE_NOT_UPLOAD.getMsg());
                return resultMap;
            }
        }
        String sjhm = CommonUtil.formatEmptyValue(data.get("sjhm"));
        String yhlx = CommonUtil.formatEmptyValue(data.get("yhlx"));

        boolean nongldw = StringUtils.equals(CommonUtil.formatEmptyValue(data.get("nondwgly")), Boolean.TRUE.toString());
        boolean isvalid = StringUtils.equals(Constants.VALID, CommonUtil.formatEmptyValue(data.get("isvalid"), Constants.VALID));

        UserDto userDto = new UserDto();
        List<String> roleIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(yhlx)) {
            //???????????????roleid   ??????????????????????????????fdm  28224EAEE9A944CDB0ACB247DB9CA6D3
            String roleid = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_YHDW, yhlx).getFdm();
            data.put("roleid", roleid);
            roleIdList.add(roleid);
        }

        String tyshxydm = CommonUtil.formatEmptyValue(data.get("tyshxydm"));
        String dwmc = CommonUtil.formatEmptyValue(data.get("dwmc"));
        String yhid = CommonUtil.formatEmptyValue(data.get("glsxid"));
        String jsdwm = CommonUtil.formatEmptyValue(data.get("jsdwm"));
        String loginname = CommonUtil.formatEmptyValue(data.get("loginname"));
        String username = CommonUtil.formatEmptyValue(data.get("username"));
        String lxr = CommonUtil.formatEmptyValue(data.get("lxr"));
        String lxdh = CommonUtil.formatEmptyValue(data.get("lxdh"));
        String password = CommonUtil.formatEmptyValue(data.get("password"));
        String yhdwid = CommonUtil.formatEmptyValue(data.get("yhdwid"), UUIDGenerator.generate18());

        String dwbh = "";
        if (StringUtils.isNotBlank(tyshxydm)) {
            Example example = new Example(DchyXmglYhdw.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm)).andEqualTo("isvalid", Constants.VALID);
            List<DchyXmglYhdw> dchyXmglYhdws = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyXmglYhdws)) {
                dwbh = dchyXmglYhdws.get(0).getDwbh();
                dwmc = dchyXmglYhdws.get(0).getDwmc();
                yhlx = dchyXmglYhdws.get(0).getYhlx();
                criteria.andEqualTo("isdwgly", Constants.VALID);
                dchyXmglYhdws = entityMapper.selectByExample(example);
                if (CollectionUtils.isEmpty(dchyXmglYhdws)) {
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (!nongldw) {
                        switch (yhlx) {
                            //????????????
                            case Constants.DCHY_XMGL_YHLX_JSDW:
                                roleIdList.add(JSDWGLY);
                                break; //??????
                            //????????????
                            case Constants.DCHY_XMGL_YHLX_CHDW:
                                roleIdList.add(CHDWGLY);
                                break; //??????
                            default: //??????
                                break;
                        }
                    }
                }
            } else {
                //?????????????????????????????????????????????,?????????????????????????????????
                dwbh = UUIDGenerator.generate18();
                if (!nongldw) {
                    switch (yhlx) {
                        //????????????
                        case Constants.DCHY_XMGL_YHLX_JSDW:
                            roleIdList.add(JSDWGLY);
                            break; //??????
                        //????????????
                        case Constants.DCHY_XMGL_YHLX_CHDW:
                            roleIdList.add(CHDWGLY);
                            break; //??????
                        default: //??????
                            break;
                    }
                }
            }
        }

        DchyXmglYhdw dchyXmglYhdw = null;
        if (StringUtils.isNotBlank(yhdwid)) {
            dchyXmglYhdw = entityMapper.selectByPrimaryKey(DchyXmglYhdw.class, yhdwid);
            DataSecurityUtil.decryptSingleObject(dchyXmglYhdw);
        }
        if (null == dchyXmglYhdw) {
            // dchyXmglYhdw?????? json??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            dchyXmglYhdw = JSONObject.parseObject(JSONObject.toJSONString(data), DchyXmglYhdw.class);
            dchyXmglYhdw.setYhdwid(yhdwid);
            dchyXmglYhdw.setYhid(yhid);
        }
        dchyXmglYhdw.setJsdwm(jsdwm);
        dchyXmglYhdw.setDwmc(dwmc);
        dchyXmglYhdw.setTyshxydm(tyshxydm);
        dchyXmglYhdw.setDwbh(dwbh);
        dchyXmglYhdw.setIsvalid(CommonUtil.formatEmptyValue(data.get("isvalid"), Constants.VALID));
        dchyXmglYhdw.setYhlx(yhlx);

        DataSecurityUtil.encryptSingleObject(dchyXmglYhdw);
        entityMapper.saveOrUpdate(dchyXmglYhdw, dchyXmglYhdw.getYhdwid());

        resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
        resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());

        if (isvalid) {
            userDto.setUserId(yhid);
            userDto.setIsvalid(Constants.DCHY_XMGL_YHZT_YX);
            userDto.setUserName(username);
            userDto.setLoginName(loginname);
            userDto.setMobilePhone(sjhm);
            userDto.setLoginPassword(password);
            userDto.setRoleIdList(roleIdList);
            userDto.setOrganId(dwbh);
            userDto.setOrganName(dwmc);
            ResponseMessage responseMessage = exchangeFeignService.register(userDto);
            if (null != responseMessage && !responseMessage.issuccess()) {
                resultMap.put("code", responseMessage.getHead().getCode());
                resultMap.put("msg", responseMessage.getHead().getMsg());
            } else {
                //??????????????????????????????????????????
                if (StringUtils.equals(dchyXmglYhdw.getYhlx(), Constants.DCHY_XMGL_YHLX_JSDW)) {
                    DchyXmglJsdwlrDto dchyXmglJsdwlrDto = new DchyXmglJsdwlrDto();
                    DchyXmglJsdw dchyXmglJsdw;
                    Example example = new Example(DchyXmglJsdw.class);
                    example.createCriteria().andEqualTo("tyshxydm", dchyXmglYhdw.getTyshxydm()).andEqualTo("dwmc", dwmc);
                    List<DchyXmglJsdw> dchyXmglJsdwList = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(dchyXmglJsdwList)) {
                        dchyXmglJsdw = dchyXmglJsdwList.get(0);
                        if (StringUtils.isNotBlank(lxr)) {
                            dchyXmglJsdw.setLxr(SM4Util.encryptData_ECB(lxr));
                        }
                        if (StringUtils.isNotBlank(lxdh)) {
                            dchyXmglJsdw.setLxdh(SM4Util.encryptData_ECB(lxdh));
                        }
                        dchyXmglJsdw.setJsdwm(CommonUtil.ternaryOperator(dchyXmglYhdw.getJsdwm(), jsdwm));
                    } else {
                        dchyXmglJsdw = new DchyXmglJsdw();
                        dchyXmglJsdw.setJsdwid(UUIDGenerator.generate18());
                        dchyXmglJsdw.setDwmc(dchyXmglYhdw.getDwmc());
                        dchyXmglJsdw.setDwbh(dchyXmglYhdw.getDwbh());
                        dchyXmglJsdw.setLxr(SM4Util.encryptData_ECB(lxr));
                        dchyXmglJsdw.setLxdh(SM4Util.encryptData_ECB(lxdh));
                        dchyXmglJsdw.setTyshxydm(dchyXmglYhdw.getTyshxydm());
                        dchyXmglJsdw.setJsdwm(CommonUtil.ternaryOperator(dchyXmglYhdw.getJsdwm(), jsdwm));
                        dchyXmglJsdw.setLrr(dchyXmglYhdw.getYhmc());
                        dchyXmglJsdw.setLrsj(CalendarUtil.getCurHMSDate());
                    }
                    int flag = entityMapper.saveOrUpdate(dchyXmglJsdw, dchyXmglJsdw.getJsdwid());
                    if (flag > 0) {
                        dchyXmglJsdwlrDto.setDchyXmglJsdw(dchyXmglJsdw);
                        serviceOlFeignUtil.pushJsdwlrDataMsg(dchyXmglJsdwlrDto);
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public ResponseMessage fileUpload(Map<String, Object> paramMap, MultipartFile[] files) {
        String ssmkid = CommonUtil.formatEmptyValue(paramMap.get("ssmkid"));
        String glsxid = CommonUtil.formatEmptyValue(paramMap.get("glsxid"));// ?????????id
        String cllx = CommonUtil.formatEmptyValue(paramMap.get("cllx"));
        String fs = CommonUtil.formatEmptyValue(paramMap.get("fs"));
        String ys = CommonUtil.formatEmptyValue(paramMap.get("ys"));
        String sjclid = CommonUtil.formatEmptyValue(paramMap.get("sjclid"));
        String clmc = CommonUtil.formatEmptyValue(paramMap.get("clmc"));
        String sjxxid = CommonUtil.formatEmptyValue(paramMap.get("sjxxid"));
        String xh = CommonUtil.formatEmptyValue(paramMap.get("xh"));
        /*????????????*/
        Map<String, Object> map = FileUploadUtil.uploadFile(files, glsxid, clmc);
        map.put("ssmkid", ssmkid);
        map.put("glsxid", glsxid);
        map.put("cllx", cllx);
        map.put("fs", fs);
        map.put("ys", ys);
        map.put("sjclid", sjclid);
        map.put("ys", ys);
        map.put("xh", xh);
        map.put("clmc", clmc);
        map.put("sjxxid", sjxxid);
        /*??????????????????????????????*/
        return FileUploadUtil.updateSjxxAndClxx(map);

    }

    @Override
    public boolean phoneIsRepet(String phone) {
        boolean flag = false;
        if (StringUtils.isNotBlank(phone)) {
            UserDto userDto = new UserDto();
            userDto.setMobilePhone(phone);
            ResponseMessage message = exchangeFeignService.sjhcf(userDto);
            if (StringUtils.equals(message.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
                Map<String, Object> data = message.getData();
                if (MapUtils.isNotEmpty(data) && StringUtils.equals("true", CommonUtil.formatEmptyValue(MapUtils.getString(data, "isRegistered")))) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * @param username
     * @return
     * @description 2020/12/15 ?????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public boolean usernameIsRepet(String username) {
        boolean flag = false;
        UserDto userDto = new UserDto();
        userDto.setLoginName(username);
        ResponseMessage message = exchangeFeignService.yhmcf(userDto);
        if (StringUtils.equals(message.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
            Map<String, Object> data = message.getData();
            if (MapUtils.isNotEmpty(data) && StringUtils.equals("true", CommonUtil.formatEmptyValue(MapUtils.getString(data, "yhmcf")))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param username
     * @return
     * @description 2020/12/15 ??????????????????????????????
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     */
    @Override
    public boolean usernameAvaliable(String username) {
        boolean flag = false;
        UserDto userDto = new UserDto();
        userDto.setLoginName(username);
        ResponseMessage message = exchangeFeignService.checkUserIsValidByLoginName(userDto);
        if (StringUtils.equals(message.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
            Map<String, Object> data = message.getData();
            if (MapUtils.isNotEmpty(data) && StringUtils.equals("true", CommonUtil.formatEmptyValue(MapUtils.getString(data, "data")))) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public DchyXmglSjclpz getSjclpzByClmc(String clmc, String ssmkid) {
        Example sjclpzExample = new Example(DchyXmglSjclpz.class);
        Example.Criteria criteria = sjclpzExample.createCriteria();
        if (StringUtils.isNotBlank(clmc)) {
            criteria.andEqualTo("clmc", clmc);
        }
        if (StringUtils.isNotBlank(ssmkid)) {
            criteria.andEqualTo("ssmkid", ssmkid);
        }
        List<DchyXmglSjclpz> dchyXmglSjclpzList = entityMapper.selectByExampleNotNull(sjclpzExample);
        DchyXmglSjclpz dchyXmglSjclpz = new DchyXmglSjclpz();
        if (CollectionUtils.isNotEmpty(dchyXmglSjclpzList)) {
            dchyXmglSjclpz = dchyXmglSjclpzList.get(0);
        }
        return dchyXmglSjclpz;
    }

    /**
     * @param tyshxydm
     * @return
     * @description 2021/5/10 ??????????????????????????????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public DchyXmglJsdw queryJsdwByTyshxydm(String tyshxydm) {
        if (StringUtils.isNotBlank(tyshxydm)) {
            Example example = new Example(DchyXmglJsdw.class);
            example.createCriteria().andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm));
            List<DchyXmglJsdw> dchyXmglJsdwList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyXmglJsdwList)) {
                DchyXmglJsdw dchyXmglJsdw = dchyXmglJsdwList.get(0);
                dchyXmglJsdw.setTyshxydm(SM4Util.decryptData_ECB(dchyXmglJsdw.getTyshxydm()));
                dchyXmglJsdw.setLxr(SM4Util.decryptData_ECB(dchyXmglJsdw.getLxr()));
                dchyXmglJsdw.setLxdh(SM4Util.decryptData_ECB(dchyXmglJsdw.getLxdh()));
                return dchyXmglJsdw;
            }
        }
        return null;
    }

}
