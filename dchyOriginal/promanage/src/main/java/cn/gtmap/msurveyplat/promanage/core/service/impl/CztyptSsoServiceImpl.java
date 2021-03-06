package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.dto.UserDto;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.HttpClientUtil;
import cn.gtmap.msurveyplat.promanage.core.service.CztyptSsoService;
import cn.gtmap.msurveyplat.promanage.model.CztyptUserDto;
import cn.gtmap.msurveyplat.promanage.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CztyptSsoServiceImpl implements CztyptSsoService {

    private final Logger logger = LoggerFactory.getLogger(CztyptSsoServiceImpl.class);

    @Autowired
    private ExchangeFeignServiceImpl exchangeFeignService;

    @Value("${cztypt.ask.url}")
    private String fwdz;

    @Value("${cztypt.roleid}")
    private String mrjs;

    /**
     * @param token
     * @return
     * @description 2021/5/12 通过token获取用户的所有信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public CztyptUserDto getTokenInfo(String token) {
        // 常州统一平台网页登录用户信息
        CztyptUserDto cztyptUserDto = new CztyptUserDto();
        if (StringUtils.isNoneBlank(token)) {
            //获取当前用户信息
            String json = cztyptHttpGet(fwdz, token);

            if (StringUtils.isNotBlank(json) && CommonUtil.isJson(json)) {
                JSONObject jsonObject = JSONObject.parseObject(json);
                List<Map> dataList = JSONArray.parseArray((JSON.toJSONString(jsonObject.get("Status"))), Map.class);
                Map data = dataList.get(0);
//                Map<String, Object> data = (Map<String, Object>) jsonObject.get("Status");
                logger.info("***********根据token获取的数据**********" + json);
                if (json.contains("ErrorCode") && !StringUtils.equals("0", CommonUtil.formatEmptyValue(data.get("ErrorCode")))) {
                    // 如果返回的json字符串中包含
                    logger.info("**************常州统一平台网页端用户信息异常(企业用户获取)******************" + jsonObject.get("ErrorMessage"));
                } else {
                    // 登陆成功
                    String personID = CommonUtil.formatEmptyValue(MapUtils.getString(data, "PersonID"));
                    String personName = CommonUtil.formatEmptyValue(MapUtils.getString(data, "PersonName"));
                    String personCode = CommonUtil.formatEmptyValue(MapUtils.getString(data, "PersonCode"));
                    String logonName = CommonUtil.formatEmptyValue(MapUtils.getString(data, "LogonName"));
                    String serialNO = CommonUtil.formatEmptyValue(MapUtils.getString(data, "SerialNO"));
                    String birthday = CommonUtil.formatEmptyValue(MapUtils.getString(data, "Birthday"));
                    String sex = CommonUtil.formatEmptyValue(MapUtils.getString(data, "Sex"));
                    String IDCard = CommonUtil.formatEmptyValue(MapUtils.getString(data, "IDCard"));
                    String symbol = CommonUtil.formatEmptyValue(MapUtils.getString(data, "Symbol"));
                    String telephone = CommonUtil.formatEmptyValue(MapUtils.getString(data, "Telephone"));
                    String mobilePhone = CommonUtil.formatEmptyValue(MapUtils.getString(data, "MobilePhone"));
                    String remark = CommonUtil.formatEmptyValue(MapUtils.getString(data, "Remark"));
                    String departmentID = CommonUtil.formatEmptyValue(MapUtils.getString(data, "DepartmentID"));
                    String departmentName = CommonUtil.formatEmptyValue(MapUtils.getString(data, "DepartmentName"));
                    String depCode = CommonUtil.formatEmptyValue(MapUtils.getString(data, "DepCode"));
                    String parentDepID = CommonUtil.formatEmptyValue(MapUtils.getString(data, "ParentDepID"));
                    String mainDepartmentID = CommonUtil.formatEmptyValue(MapUtils.getString(data, "MainDepartmentID"));

                    cztyptUserDto.setPersonID(personID);
                    cztyptUserDto.setPersonName(personName);
                    cztyptUserDto.setPersonCode(personCode);
                    cztyptUserDto.setLogonName(logonName);
                    cztyptUserDto.setPersonID(personID);
                    cztyptUserDto.setSerialNO(serialNO);
                    cztyptUserDto.setBirthday(birthday);
                    cztyptUserDto.setSex(sex);
                    cztyptUserDto.setIDCard(IDCard);
                    cztyptUserDto.setSymbol(symbol);
                    cztyptUserDto.setTelephone(telephone);
                    cztyptUserDto.setMobilePhone(mobilePhone);
                    cztyptUserDto.setRemark(remark);
                    cztyptUserDto.setDepartmentID(departmentID);
                    cztyptUserDto.setDepartmentName(departmentName);
                    cztyptUserDto.setDepCode(depCode);
                    cztyptUserDto.setParentDepID(parentDepID);
                    cztyptUserDto.setMainDepartmentID(mainDepartmentID);
                }
            }
        }
        return cztyptUserDto;
    }

    /**
     * @param cztyptUserDto
     * @return
     * @description 2021/5/12  使用用户的身份证号作为用户名,查询是否存在
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public void isEsxit(CztyptUserDto cztyptUserDto) {
        if (null != cztyptUserDto && StringUtils.isNotBlank(cztyptUserDto.getIDCard())) {
            boolean usernameIsRepet = exchangeFeignService.usernameIsRepet(cztyptUserDto.getIDCard());
            if (!usernameIsRepet) {
                UserDto userDto = new UserDto();
                List<String> roleIdList = Lists.newArrayList();
                roleIdList.add(mrjs);
                userDto.setUserId(UUIDGenerator.generate18());
                userDto.setLoginName(cztyptUserDto.getIDCard());
                userDto.setLoginPassword(StringUtils.reverse(cztyptUserDto.getIDCard()));
                userDto.setRoleIdList(roleIdList);
                exchangeFeignService.register(userDto);
            }
        }
    }

    private String cztyptHttpGet(String url, String token) {
        System.out.println(url);
        System.out.println(token);
        String json = null;
        if (StringUtils.isNoneBlank(url, token)) {
            url = url.replace("{token}", token);
            url = url.replace("{ResultType}", Constants.DCHY_XMGL_FHLX_JSON);
            //放回json字符串
            String responseContent = HttpClientUtil.httpGet(url, null, null);
//            try {
//                SAXReader reader = new SAXReader(false);
//                InputSource source = new InputSource(new ByteArrayInputStream(responseContent.getBytes(Charsets.UTF_8)));
//                source.setEncoding(Charsets.UTF_8.toString());
//                Document doc = reader.read(source);
////                json = doc.getRootElement().getText();
//            } catch (Exception e) {
//                logger.error("常州统一平台登录信息获取接口异常{}", e);
//            }
            json = responseContent;
        }
        return json;
    }

}
