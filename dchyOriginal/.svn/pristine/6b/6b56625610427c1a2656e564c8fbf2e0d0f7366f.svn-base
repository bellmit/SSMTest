package cn.gtmap.msurveyplat.portalol.web;


import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.vo.PfUser;
import cn.gtmap.msurveyplat.portalol.service.impl.DchyXmglYhdwServiceImpl;
import cn.gtmap.msurveyplat.portalol.utils.ServiceOlFeignUtil;
import cn.gtmap.msurveyplat.portalol.utils.token.TokenUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Resource(name = "entityMapper")
    EntityMapper entityMapper;

    @Resource(name = "entityMapperPLAT")
    EntityMapper entityMapperPLAT;

    @Autowired
    ServiceOlFeignUtil serviceOlFeignUtil;


    @Autowired
    DchyXmglYhdwServiceImpl dchyXmglYhdwService;

    @GetMapping("/getjson")
    public Object test() {
        String password = "71ecd394b5add46f158e74e3a819c2a6";
        Map map = Maps.newHashMap();
        map.put("1", "2");
        map.put("password","");
        return map;
    }

    @GetMapping("/fegin/getjson2")
    public Object test2() {
        Map map = Maps.newHashMap();
        Map param = Maps.newHashMap();
        Map paramPlat = Maps.newHashMap();
        String roleId = "28224EAEE9A944CDB0ACB247DB9CA6D3";
        map = serviceOlFeignUtil.queryGldwTzgggl("1");
        return map;
    }

    @PostMapping("/testToken")
    public Object testToken(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String yzm = CommonUtil.formatEmptyValue(data.get("yzm"));
        String phone = CommonUtil.formatEmptyValue(data.get("phone"));
        Map map = Maps.newHashMap();
        String code = phone + "_" + yzm;
        map.put(code, TokenUtil.getToken(code));
        return map;
    }

    @PostMapping("/testTokenIsValid")
    public Object testTokenIsValid(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String yzm = CommonUtil.formatEmptyValue(data.get("yzm"));
        String phone = CommonUtil.formatEmptyValue(data.get("phone"));
        Map map = Maps.newHashMap();
        String code = phone + "_" + yzm;
        String token = TokenUtil.getTokenByCode(code);
        map.put("isValid", TokenUtil.tokenVaild(token));
        return map;
    }

    @GetMapping("/testFeign")
    public Object testFeign() {
        return serviceOlFeignUtil.queryGldwTzgggl("1");
    }

//    @GetMapping("/testBsznPage")
//    public Object testBsznPage() {
//        return serviceOlFeignUtil.getTzggByBszn("1", "10");
//    }

//    @GetMapping("/testOtherTzggPage")
//    public Object testOtherTzggPage() {
//        return serviceOlFeignUtil.getOtherTzgg("1", "10");
//    }

    @PostMapping("/testUpload")
    public Object testUpload(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> param = new HashMap<>();

        param.put("ssmkid", "4");
        param.put("glsxid", "2562617BFF754DFE89AD29C06D9E760D");
        param.put("cllx", "1");
        param.put("sjclid", "geragaegregqaer");
        param.put("clmc", "建设单位营业执照");
        param.put("sjxxid", "vadfgvdfgagerqafgera");
        return dchyXmglYhdwService.fileUpload(param, files);
    }

    @GetMapping("/testEncryt")
    public Object testEncryt() {
        Map<String, Object> resultMap = new HashMap<>();

        List<Object> dchyXmglMlkList = new ArrayList<>();
        List<Object> dchyXmglMlkList1 = new ArrayList<>();

        DchyXmglMlk dchyXmglMlk = new DchyXmglMlk();
        DchyXmglMlk dchyXmglMlk1 = new DchyXmglMlk();
        DchyXmglMlk dchyXmglMlk2 = new DchyXmglMlk();
        DchyXmglMlk dchyXmglMlk3 = new DchyXmglMlk();

//        dchyXmglMlk.setMlkid("fdavvdsgvfsd");
//        dchyXmglMlk.setLxr("刘强");
//        dchyXmglMlk.setLxdh("18994142515");
//
//        dchyXmglMlk1.setMlkid("gsdfgdfsgdf");
//        dchyXmglMlk1.setLxr("李俊");
//        dchyXmglMlk1.setLxdh("13207132414");

        dchyXmglMlk2.setMlkid("fdavvdsgvfsd");
        dchyXmglMlk2.setLxr("9lmlIwQJy5gTTcGuCQU0Sg==");
        dchyXmglMlk2.setLxdh("movGT0LRqj0CG9J3rVJyjg==");

        dchyXmglMlk3.setMlkid("gsdfgdfsgdf");
        dchyXmglMlk3.setLxr("EhDzqCltlWjwsD3aeMS3PQ==");
        dchyXmglMlk3.setLxdh("bX9EnKSz72mRKjZ/cZoH8A==");

//        dchyXmglMlkList.add(dchyXmglMlk);
//        dchyXmglMlkList.add(dchyXmglMlk1);

        dchyXmglMlkList1.add(dchyXmglMlk2);
        dchyXmglMlkList1.add(dchyXmglMlk3);

//        DataSecurityUtil.encryptSingleObject(dchyXmglMlk);
//        DataSecurityUtil.encryptSingleObject(dchyXmglMlk1);
//
//        DataSecurityUtil.encryptObjectList(dchyXmglMlkList);
//
//
//        DataSecurityUtil.decryptSingleObject(dchyXmglMlk2);
//        DataSecurityUtil.decryptSingleObject(dchyXmglMlk3);
//
//        DataSecurityUtil.decryptObjectList(dchyXmglMlkList1);

//        resultMap.put("加密Single:", dchyXmglMlk);
//        resultMap.put("加密Single:", dchyXmglMlk1);
//        resultMap.put("加密:", dchyXmglMlkList);
//        resultMap.put("解密Single:", dchyXmglMlk2);
//        resultMap.put("解密Single:", dchyXmglMlk3);
        resultMap.put("解密:", dchyXmglMlkList1);

        return resultMap;
    }

    @GetMapping("/testUSer")
    public Object testUSer(){
        String username = "zhaox";
        String password = "c4ca4238a0b923820dcc509a6f75849b";
        Example example = new Example(PfUser.class);
        example.createCriteria().andEqualTo("loginName",username).andEqualTo("loginPassword",password);
        List<PfUser> pfUserList = entityMapperPLAT.selectByExample(example);
        return pfUserList.get(0);
    }

}
