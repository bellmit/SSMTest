package cn.gtmap.msurveyplat.promanage.web;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglJsdw;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjfxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClgcpzMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.send.SendSlxxToBsdtServiceImpl;
import cn.gtmap.msurveyplat.promanage.core.service.mq.send.SendZxbjToBsdtServiceImpl;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;
import cn.gtmap.msurveyplat.promanage.service.impl.TestService;
import cn.gtmap.msurveyplat.promanage.web.utils.EhcacheUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestController {

    @Autowired
    DchyXmglZdService dchyXmglZdService;

    @Autowired
    SendSlxxToBsdtServiceImpl sendSlxxToBsdtService;

    @Autowired
    SendZxbjToBsdtServiceImpl sendZxbjToBsdtService;

    @Autowired
    TestService testService;

    @Autowired
    DchyXmglClgcpzMapper dchyXmglClgcpzMapper;
    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping("/getjson")
    public Object test() {
        Map map = Maps.newHashMap();
        map.put("1", "2");
//        map.put("list", propsConfig.getListTest());
//        map.put("map", propsConfig.getMapTest());
//        map.put("log.url", Constants.LOG_URL);
//        List<ErrorInfoModel> errorInfoModels = Lists.newArrayList();
//        Map map1 = Maps.newHashMap();
//        DchyXmglClcgpz dchyXmglClcgpz = new DchyXmglClcgpz();
//        dchyXmglClcgpz.setClcgpzid("1");
//        dchyXmglClcgpz.setClmc("立项用地规划许可");
//        return ZipFileUploadUtil.generateDir(dchyXmglClcgpz, map1, errorInfoModels,"gcbh");
        //map.put("blsx", dchyXmglZdService.getDchyZdBlsxList());
//        return ZipFileUploadUtil.generateClml("sdsgfd","gdfsgtrgs");
//        return ZipFileUploadUtil.generateClmlPz("dfbdfsbdfsbdgs","gcbh");


        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();

        String str = "{\"clsx\":[\"地形图补测（扩建、续建等工程）\",\"房产预测绘\",\"规划验线测量\",\"人防竣工测量\",\"地下管线测量\",\"地籍竣工测量\",\"房产实测绘\",\"绿地测量\",\"规划核实测量\",\"1001\",\"1002\"],\"gdwwyqwjList\":[{\"clsx\":\"地形图补测（扩建、续建等工程）\",\"mlmc\":\"XXTXCS0127/工程建设许可/地形图补测（扩建、续建等工程）/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"房产预测绘\",\"mlmc\":\"XXTXCS0127/施工许可/房产预测绘/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"规划验线测量\",\"mlmc\":\"XXTXCS0127/施工许可/规划验线测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"人防竣工测量\",\"mlmc\":\"XXTXCS0127/竣工验收/人防竣工测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"地下管线测量\",\"mlmc\":\"XXTXCS0127/竣工验收/地下管线测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"地籍竣工测量\",\"mlmc\":\"XXTXCS0127/竣工验收/地籍竣工测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"房产实测绘\",\"mlmc\":\"XXTXCS0127/竣工验收/房产实测绘/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"绿地测量\",\"mlmc\":\"XXTXCS0127/竣工验收/绿地测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"规划核实测量\",\"mlmc\":\"XXTXCS0127/竣工验收/规划核实测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"}],\"gscwList\":[],\"sftj\":\"0\",\"wjcfList\":[],\"wjqsList\":[{\"clsx\":\"1001\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/地形图测量/矢量数据/地形图测绘.dwg\",\"msxx\":\"文件缺失\",\"wjmc\":\"地形图测绘.dwg\"},{\"clsx\":\"1001\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/地形图测量/扫描资料/用地现状地形图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"用地现状地形图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/不动产权籍调查报告.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"不动产权籍调查报告.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/建设项目勘测定界报告.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"建设项目勘测定界报告.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/界址点坐标成果表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"界址点坐标成果表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/勘测定界图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"勘测定界图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/平面控制测量成果.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"平面控制测量成果.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/土地分类面积表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地分类面积表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/土地勘测定界表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地勘测定界表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/项目用地地理位置图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"项目用地地理位置图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/矢量数据/土地勘测定界测绘.dwg\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地勘测定界测绘.dwg\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/元数据/metadata.Xml\",\"msxx\":\"文件缺失\",\"wjmc\":\"metadata.Xml\"}]}\n";

        dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
        dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
        dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
        return map;
    }

    @GetMapping("/mqtest/{message}")
    public Object mqtest(@PathVariable(name = "conf/message") String message) {
        Map map = Maps.newHashMap();
//        sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, message+"--服务评价");
//        sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, message+"--项目发布信息");
        try {
//            sendSlxxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY, message + "--受理信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", "success");
        return map;
    }

    @GetMapping("/testMQData")
    public Object testData() {
        Map map = Maps.newHashMap();
        Map param = Maps.newHashMap();
        Map resultMap = Maps.newHashMap();
//        param.put("mrkid","1");
        param.put("sqxxid", "1");
        String msg1 = "受理信息";
        resultMap.put("saveOrUpdate", param);
//        String msg2 = "发布服务";
//        String msg3 = "项目发布";
//        sendSlxxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY, msg1 + "--受理信息");
//        sendZxbjToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_ZXBJ_QUEUE_ROUTINGKEY, msg1 + "--受理信息");
//        sendZxbjToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_ZXBJ_QUEUE_ROUTINGKEY, msg1 + "--受理信息");
//        sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, msg2+"--服务评价");
//        sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, msg3+"--项目发布信息");

//        String pzid = "1";
//        String jsyhid = "4CF91252ANX2P50T";
//        DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
//        DchyXmglYhxxPz dchyXmglYhxxPz = entityMapper.selectByPrimaryKey(DchyXmglYhxxPz.class, pzid);
//        Example example = new Example(DchyXmglYhxx.class);
//        example.createCriteria().andEqualTo("jsyhid", jsyhid);
//        List<DchyXmglYhxx> dchyXmglYhxxList = entityMapper.selectByExample(example);
//
//        dchyXmglXxtxDto.setDchyXmglYhxxList(dchyXmglYhxxList);
//        dchyXmglXxtxDto.setDchyXmglYhxxPz(dchyXmglYhxxPz);
//        resultMap.put("data", dchyXmglXxtxDto);
//        pushDataToMqService.pushXxtxResultTo(JSON.toJSONString(resultMap));
        map.put("status", "error");
        String kssj = "kssj";
        String jssj = "jssj";
        String key = UUIDGenerator.generate18();

        DchyXmglTjfxDto dchyXmglTjfxDto = new DchyXmglTjfxDto();
        dchyXmglTjfxDto.setKey(key);
        dchyXmglTjfxDto.setJssj(jssj);
        dchyXmglTjfxDto.setKssj(kssj);
//        pushDataToMqService.pushTjfxMsgToMq(dchyXmglTjfxDto);
//        return map;

//        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
//        Map mycache = (Map) valueWrapper.get();
//        Map xmslbyqxList = MapUtils.getMap(mycache, "xmslbyqxList");

        DchyXmglJsdwlrDto dchyXmglJsdwlrDto = new DchyXmglJsdwlrDto();
        DchyXmglJsdw dchyXmglJsdw = new DchyXmglJsdw();

        dchyXmglJsdw.setJsdwid(UUIDGenerator.generate18());
        dchyXmglJsdw.setDwmc("ssssss");
        dchyXmglJsdw.setDwbh(UUIDGenerator.generate18());
        dchyXmglJsdw.setTyshxydm(UUIDGenerator.generate18());
        dchyXmglJsdw.setJsdwm(UUIDGenerator.generate18());
        dchyXmglJsdw.setLxr("liuq");
        dchyXmglJsdw.setLxdh("31258755558");

        dchyXmglJsdwlrDto.setDchyXmglJsdw(dchyXmglJsdw);
        pushDataToMqService.pushJsdwlrMsgToMq(dchyXmglJsdwlrDto);

        try {
            Map mycache = queryXxData(key);
            if (StringUtils.equals(MapUtils.getString(mycache, "code"), ResponseMessage.CODE.SUCCESS.getCode())) {
                Map xmslbyqxList = MapUtils.getMap(mycache, "xmslbyqxList");
                return xmslbyqxList;
            } else {
                return map;
            }
        } catch (InterruptedException e) {
            return map;
        }
    }

    @GetMapping("/testCommit")
    public Object testCommit() {
        DchyXmglChxmDto dchyXmglChxmDto = testService.dchyXmglChxmDto();
        dchyXmglChxmDto.getDchyXmglChgc().setGcmc("sdfasdfas");
        dchyXmglChxmDto.getDchyXmglChgc().setLxdh("15051995513");
        dchyXmglChxmDto.getDchyXmglChxmClsxList().get(1).setClsxid("delete_" + dchyXmglChxmDto.getDchyXmglChxmClsxList().get(1).getClsxid());
        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = new DchyXmglChxmChdwxx();
        dchyXmglChxmChdwxx.setChdwmc("测试");
        dchyXmglChxmChdwxx.setChdwxxid(UUIDGenerator.generate18());
        dchyXmglChxmDto.getDchyXmglChxmChdwxxList().add(dchyXmglChxmChdwxx);
//        testService.logTest(dchyXmglChxmDto);
        System.out.println(dchyXmglChxmDto);
        return dchyXmglChxmDto;
    }

    @GetMapping("/testExport")
    public Object testExport(HttpServletResponse response) {
        String path = "D:/testout1.pdf";
        String egovConf = AppConfig.getProperty("egov.conf");
//        PdfUtils.download(path,response);
        return egovConf.substring(6, egovConf.length() - 1);
    }

    @GetMapping("/testMljg")
    public Object testMljg() {
        Map mlMap1 = Maps.newHashMap();
        List<DchyXmglClcgpz> dchyXmglClcgpzList1 = dchyXmglClgcpzMapper.queryclcgpz();
        //一级目录
        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList1)) {
            for (DchyXmglClcgpz dchyXmglClcgpz1 : dchyXmglClcgpzList1) {
                String mlmc1 = dchyXmglClcgpz1.getClmc();
                String clcgpzid1 = dchyXmglClcgpz1.getClcgpzid();
                List<DchyXmglClcgpz> dchyXmglClcgpzList2 = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid1);
                Map mlMap2 = Maps.newHashMap();
                //二级目录
                if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList2)) {
                    for (DchyXmglClcgpz dchyXmglClcgpz2 : dchyXmglClcgpzList2) {
                        String mlmc2 = dchyXmglClcgpz2.getClmc();
                        String clcgpzid2 = dchyXmglClcgpz2.getClcgpzid();

                        List<DchyXmglClcgpz> dchyXmglClcgpzList3 = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid2);
                        Map mlMap3 = Maps.newHashMap();
                        //三级目录
                        if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList3)) {
                            for (DchyXmglClcgpz dchyXmglClcgpz3 : dchyXmglClcgpzList3) {
                                String mlmc3 = dchyXmglClcgpz3.getClmc();
                                String clcgpzid3 = dchyXmglClcgpz3.getClcgpzid();
                                List<DchyXmglClcgpz> dchyXmglClcgpzList4 = dchyXmglClgcpzMapper.queryclcgpzByPclcgpzid(clcgpzid3);
//                                Map mlMap4 = Maps.newHashMap();
                                List<String> mlList = new ArrayList<>();

                                //四级目录
                                if (CollectionUtils.isNotEmpty(dchyXmglClcgpzList4)) {
                                    for (DchyXmglClcgpz dchyXmglClcgpz4 : dchyXmglClcgpzList4) {
                                        String mlmc4 = dchyXmglClcgpz4.getClmc();
//                                        mlMap4.put(mlmc4, mlmc4);
                                        mlList.add(mlmc4);
                                    }
                                }
                                mlMap3.put(mlmc3, mlList);
//                                mlMap3.put(mlmc3, mlMap4);
                            }
                        }
                        mlMap2.put(mlmc2, mlMap3);
                    }
                }
                mlMap1.put(mlmc1, mlMap2);
            }
        }
        return mlMap1;
    }

    @GetMapping("/testErrorInfo")
    public Object testErrorInfo() {
        Map resultMap = Maps.newHashMap();
        ResponseMessage message = new ResponseMessage();
        List<ErrorInfoModel> mapList = new ArrayList<>();
        ErrorInfoModel errorInfoModel1 = new ErrorInfoModel();
        ErrorInfoModel errorInfoModel2 = new ErrorInfoModel();
        ErrorInfoModel errorInfoModel3 = new ErrorInfoModel();
        ErrorInfoModel errorInfoModel4 = new ErrorInfoModel();
        ErrorInfoModel errorInfoModel5 = new ErrorInfoModel();
        ErrorInfoModel errorInfoModel6 = new ErrorInfoModel();

        errorInfoModel1.setWjmc("");
        errorInfoModel1.setClsx("土地勘测定界");
        errorInfoModel1.setMsxx("文件重复");
        mapList.add(errorInfoModel1);

        errorInfoModel2.setWjmc("");
        errorInfoModel2.setClsx("土地勘测定界");
        errorInfoModel2.setMsxx("文件重复");
        mapList.add(errorInfoModel2);

        errorInfoModel3.setWjmc("测量成果表");
        errorInfoModel3.setClsx("地籍竣工测量");
        errorInfoModel3.setMsxx("文件缺失");
        mapList.add(errorInfoModel3);

        errorInfoModel4.setWjmc("竣工总平面图");
        errorInfoModel4.setClsx("地籍竣工测量");
        errorInfoModel4.setMsxx("文件缺失");
        mapList.add(errorInfoModel4);

        errorInfoModel5.setWjmc("房产平面控制点成果");
        errorInfoModel5.setClsx("房产预测绘");
        errorInfoModel1.setMsxx("格式错误");
        mapList.add(errorInfoModel5);

        errorInfoModel6.setWjmc("面积测算成果");
        errorInfoModel6.setClsx("房产预测绘");
        errorInfoModel6.setMsxx("格式错误");
        mapList.add(errorInfoModel6);
        resultMap.put("data", mapList);

        message = ResponseUtil.wrapSuccessResponse();
        message.setData(resultMap);

        return message;
    }

    public static void main(String[] args) {
        String zipname = "XXTXCS0127/竣工验收/地籍竣工测量/矢量数据/地籍测绘.dwg";
        String zipname1 = "XXTXCS0127/竣工验收/地籍竣工测量/矢量数据/";

        String num = "454887";
        System.out.print("请您输入内容:");
        Scanner input = new Scanner(System.in);//创建一个键盘扫描类对象
        String contents = input.next(); //输入字符型

        if (contents.length() > 3) {
            contents = contents.substring(contents.length() - 3, contents.length());
            System.out.println(contents);
        } else {
            System.out.println(contents);
        }


//        System.out.println(zipname.contains(""));
//        System.out.println(zipname.indexOf("/", 2));
//        System.out.println(zipname.substring(0, zipname.indexOf("/", 2)));
//        System.out.println(zipname.substring(0, getCharacterPosition(zipname,"/",3)));
//        System.out.println(zipname1.substring(0, getCharacterPosition(zipname,"/",3)));
//        System.out.println(getCharacterPosition(zipname,"/",3));
    }

    public static int getCharacterPosition(String string, String ch, int index) {
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(ch).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start() + 1;
    }

    //获取缓存里的值,当null时,休眠3s再次请求,最多请求10次
    public Map queryXxData(String key) throws InterruptedException {
        Map resultMap = Maps.newHashMap();
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
        for (int i = 0; i < 10; i++) {
            if (null != valueWrapper) {
                Map mycache = (Map) valueWrapper.get();
                resultMap.put("data", mycache);
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
                break;
            } else {
                resultMap.put("code", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getCode());
                resultMap.put("msg", ResponseMessage.CODE.RESULT_SUBMIT_REQUEST_TIMED_OUT.getMsg());
                Thread.sleep(3000);
                valueWrapper = EhcacheUtil.getDataFromEhcache(key);
            }

        }
        return resultMap;
    }

}