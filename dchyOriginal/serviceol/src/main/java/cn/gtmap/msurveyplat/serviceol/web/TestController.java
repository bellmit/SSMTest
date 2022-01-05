package cn.gtmap.msurveyplat.serviceol.web;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjfxDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.send.SendXmxgxxToXmglServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.send.SendXsxmxxToXmglServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.send.SendZxbjToXmglServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.Impl.MlkxxServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    SendXmxgxxToXmglServiceImpl sendXmxgxxToXmglService;

    @Autowired
    SendXsxmxxToXmglServiceImpl sendXsxmxxToXmglService;

    @Autowired
    SendZxbjToXmglServiceImpl sendZxbjToXmglService;

    @Autowired
    MlkxxServiceImpl practitionersInfoService;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    PlatformUtil platformUtil;

    @Autowired
    PushDataToMqService pushDataToMqService;


    @GetMapping("/v1.0/testinserttzgg/{sdfsd}")
    @CheckInterfaceAuth(uri = "/v1.0/testinserttzgg")
    public Object testinserttzgg(@PathVariable String sdfsd) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("1", "2");
        return map;
    }

    @GetMapping("/feign/getjson")
//    @SystemLog(event = "重置字典信息")
    public Object test() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("1", "2");
        return map;
    }

    @GetMapping("/setDataToMq")
    public Object testData() {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("mlkid", "1");
        param.put("chxmidSaveOrUpdate", "1");
        param.put("chxmidDelete", "1");
        map.put("status", "success");
        return map;
    }

    @GetMapping("/testMQData")
    public Object testMQData() {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> mapXsData = Maps.newHashMap();
        Map<String, Object> mapMlkData = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        mapXsData.put("mlkid", "1");

        mapMlkData.put("chxmidSaveOrUpdate", "1");
        mapMlkData.put("chxmidDelete", "2");

        String jsyhid = "4CF945202TX2P52F";
        DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
        Example example = new Example(DchyXmglYhxx.class);
        example.createCriteria().andEqualTo("jsyhid", jsyhid);
        List<DchyXmglYhxx> dchyXmglYhxxList = entityMapper.selectByExample(example);

        dchyXmglXxtxDto.setDchyXmglYhxxList(dchyXmglYhxxList);
        resultMap.put("data", dchyXmglXxtxDto);
        //根据kssj,jssj获取返回结果*******************************
        List<Map<String, Object>> xmslbyqxList = Lists.newArrayList();
        Map<String, Object> map2 = Maps.newHashMap();
        map.put("gcbh", "qwernpc");
        map.put("gcmc", "南京市第一测绘单位");
        xmslbyqxList.add(map2);

        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("gcbh", "qwerabc");
        map1.put("gcmc", "南京市第二测绘单位");
        xmslbyqxList.add(map1);
        //根据kssj,jssj获取返回结果*******************************

        DchyXmglTjfxDto dchyXmglTjfxDto = new DchyXmglTjfxDto();
        dchyXmglTjfxDto.setXmslbyqxList(xmslbyqxList);

        DchyXmglJsdwlrDto dchyXmglJsdwlrDto = new DchyXmglJsdwlrDto();
        DchyXmglYhdw dchyXmglYhdw = new DchyXmglYhdw();

        dchyXmglYhdw.setYhdwid(UUIDGenerator.generate18());
        dchyXmglYhdw.setDwmc("ssssss");
        dchyXmglYhdw.setYhlx("1");
        dchyXmglYhdw.setDwbh(UUIDGenerator.generate18());
        dchyXmglYhdw.setTyshxydm(UUIDGenerator.generate18());
        dchyXmglYhdw.setJsdwm(UUIDGenerator.generate18());
        dchyXmglYhdw.setYhid(UUIDGenerator.generate18());

//        dchyXmglJsdwlrDto.setDchyXmglYhdw(dchyXmglYhdw);
        pushDataToMqService.pushJsdwlrMsgToMq(dchyXmglJsdwlrDto);

        map.put("status", "success");
        return map;
    }

    /**
     * 数据加密
     *
     * @return
     */
    @PostMapping(value = "encryptdata")
    @Transactional(rollbackFor = {Exception.class})
    public void testEncryptData() {
        /*DchyXmglYhdw*/
        Example yhdwExample = new Example(DchyXmglYhdw.class);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
        /*加密*/
        DataSecurityUtil.encryptObjectList(yhdwList);
        entityMapper.batchSaveSelective(yhdwList);

        /*DchyXmglChdw*/
        Example chdwExample = new Example(DchyXmglChdw.class);
        List<DchyXmglChdw> chdwList = entityMapper.selectByExample(chdwExample);
        /*加密*/
        DataSecurityUtil.encryptObjectList(chdwList);
        entityMapper.batchSaveSelective(chdwList);

        /*DchyXmglChgc*/
        Example chgcExample = new Example(DchyXmglChgc.class);
        List<DchyXmglChgc> chgcList = entityMapper.selectByExample(chgcExample);
        /*加密*/
        DataSecurityUtil.encryptObjectList(chgcList);
        entityMapper.batchSaveSelective(chgcList);

        /*DchyXmglMlk*/
        Example mlkExample = new Example(DchyXmglMlk.class);
        List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
        /*加密*/
        DataSecurityUtil.encryptObjectList(mlkList);
        entityMapper.batchSaveSelective(mlkList);
    }


}
