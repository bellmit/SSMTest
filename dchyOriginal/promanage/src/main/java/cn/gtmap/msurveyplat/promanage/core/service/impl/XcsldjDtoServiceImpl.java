package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChdwMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClsxChdwxxGxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglDyhzdMapper;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadHtxxService;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjDtoService;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
@Service
public class XcsldjDtoServiceImpl implements XcsldjDtoService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    XcsldjService xcsldjService;
    @Autowired
    private FileUploadHtxxService fileUploadHtxxService;
    @Autowired
    private DchyXmglClsxChdwxxGxMapper dchyXmglClsxChdwxxGxMapper;
    @Autowired
    DchyXmglDyhzdMapper dchyXmglDyhzdMapper;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    DchyXmglChdwMapper dchyXmglChdwMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String DELETED = "delete_";
    private final String SEPARATOR = "_SP_";

    @Override
    public boolean saveXcsldjCs(Map<String, Object> paramMap) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();

        Map<String, Object> data = (Map<String, Object>) paramMap.get("data");
        String chgcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Example chgcExample = new Example(DchyXmglChgc.class);
        chgcExample.createCriteria().andEqualTo("gcbh", chgcbh);
        List<DchyXmglChgc> dchyXmglChgcList = entityMapper.selectByExampleNotNull(chgcExample);
        //测绘工程
        DchyXmglChgc dchyXmglChgc;
        if (CollectionUtils.isNotEmpty(dchyXmglChgcList)) {
            dchyXmglChgc = dchyXmglChgcList.get(0);
        } else {
            dchyXmglChgc = new DchyXmglChgc();
            dchyXmglChgc.setChgcid(UUIDGenerator.generate18());
        }
        dchyXmglChgc.setGcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
        dchyXmglChgc.setGcmc(CommonUtil.formatEmptyValue(data.get("gcmc")));
        dchyXmglChgc.setGcdzs(CommonUtil.formatEmptyValue(data.get("gcdzs")));
        dchyXmglChgc.setGcdzss(CommonUtil.formatEmptyValue(data.get("gcdzss")));
        dchyXmglChgc.setGcdzqx(CommonUtil.formatEmptyValue(data.get("gcdzqx")));
        dchyXmglChgc.setGcdzxx(CommonUtil.formatEmptyValue(data.get("gcdzxx")));
        dchyXmglChgc.setXmxz(CommonUtil.formatEmptyValue(data.get("xmxz")));
        dchyXmglChgc.setZdbh(CommonUtil.formatEmptyValue(data.get("zdbh")));
        dchyXmglChgc.setLxr(CommonUtil.formatEmptyValue(data.get("lxr")));
        dchyXmglChgc.setLxdh(CommonUtil.formatEmptyValue(data.get("lxdh")));
        dchyXmglChgc.setWtdw(CommonUtil.formatEmptyValue(data.get("wtdw")));
        dchyXmglChgc.setJsdwm(CommonUtil.formatEmptyValue(data.get("jsdwm")));
        dchyXmglChgc.setXmly(Constants.XMLY_XXFB);//线下发布
        dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgc);

        //测绘项目
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        if (null == dchyXmglChxm) {
            dchyXmglChxm = new DchyXmglChxm();
        }
        dchyXmglChxm.setChxmid(chxmid);
        dchyXmglChxm.setChgcid(dchyXmglChgc.getChgcid());
        dchyXmglChxm.setChgcbh(CommonUtil.formatEmptyValue(data.get("gcbh")));
        if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(data.get("chzsx")))) {
            dchyXmglChxm.setChzsx(Integer.parseInt(CommonUtil.formatEmptyValue(data.get("chzsx"))));
        }
        dchyXmglChxm.setFbr(xcsldjService.getXschxmFbrByXqfbbh(CommonUtil.formatEmptyValue(data.get("xqfbbh"))));
        dchyXmglChxm.setFbsj(new Date());
        dchyXmglChxm.setCgfs(CommonUtil.formatEmptyValue(data.get("cgfs")));
        dchyXmglChxm.setXqfbbh(CommonUtil.formatEmptyValue(data.get("xqfbbh")));
        dchyXmglChxm.setQjfs(CommonUtil.formatEmptyValue(data.get("qjfs")));
        dchyXmglChxm.setQjdd(CommonUtil.formatEmptyValue(data.get("qjdz")));
        dchyXmglChxm.setSjr(CommonUtil.formatEmptyValue(data.get("sjr")));
        dchyXmglChxm.setSjdds(CommonUtil.formatEmptyValue(data.get("sjdds")));
        dchyXmglChxm.setSjddss(CommonUtil.formatEmptyValue(data.get("sjddss")));
        dchyXmglChxm.setSjddqx(CommonUtil.formatEmptyValue(data.get("sjddqx")));
        dchyXmglChxm.setSjddxx(CommonUtil.formatEmptyValue(data.get("sjddxx")));
        dchyXmglChxm.setSjrlxdh(CommonUtil.formatEmptyValue(data.get("sjrlxdh")));
        dchyXmglChxm.setChjglxr(CommonUtil.formatEmptyValue(data.get("chjglxr")));
        dchyXmglChxm.setChjglxdh(CommonUtil.formatEmptyValue(data.get("chjglxdh")));
        dchyXmglChxm.setZdxm(CommonUtil.formatEmptyValue(data.get("zdxm")));
        dchyXmglChxm.setSlbh(CommonUtil.formatEmptyValue(data.get("slbh")));
        dchyXmglChxm.setYyqjsj(CalendarUtil.formatDate(CommonUtil.formatEmptyValue(data.get("yyqjsj"))));
        dchyXmglChxm.setXmzt(Constants.DCHY_XMGL_CHXM_XMZT_WSL);//待备案
        dchyXmglChxm.setWjsftb("1");
        dchyXmglChxm.setPjzt("0");//评价状态
        dchyXmglChxm.setFwxz(CommonUtil.formatEmptyValue(data.get("fwxz")));
        dchyXmglChxm.setSfgq(Constants.NO); //是否挂起
        dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);

        //测绘单位
        List<Map<String, Object>> chdwmcList = (List<Map<String, Object>>) data.get("chdwxx");
        List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = generateChdwxx(chxmid, chdwmcList);
        dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);

        //测量事项保存相关信息
        //String clsx = CommonUtil.formatEmptyValue(data.get("clsx"));
        String jsrq = CommonUtil.formatEmptyValue(data.get("yjjfrq"));
        List<Map<String, Object>> clsxList = (List<Map<String, Object>>) data.get("clsx");
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = generateClsx(jsrq, chxmid, clsxList);
        dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);

        //测量事项测绘体量相关信息
        String fwxz = CommonUtil.formatEmptyValue(data.get("fwxz"));
        List<Map<String, Object>> chtlList = (List<Map<String, Object>>) data.get("chtl");
        List<DchyXmglClsxChtl> dchyXmglClsxChtlList = generateChtl(chxmid, fwxz, chtlList);
        dchyXmglChxmDto.setDchyXmglClsxChtlList(dchyXmglClsxChtlList);

        //合同信息
        List<Map<String, Object>> htxxList = (List<Map<String, Object>>) data.get("htxx");
        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = generateHtxx(chxmid, dchyXmglChxmClsxList, dchyXmglChxmChdwxxList, htxxList);
        dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);

        //收件材料
        List<Map<String, Object>> clxxList = (List<Map<String, Object>>) data.get("clxx");
        List<DchyXmglSjcl> sjclList = generateSjcl(chxmid, clxxList);
        dchyXmglChxmDto.setDchyXmglSjclList(sjclList);

        dchyXmglChxmDto.setGlsxid(chxmid);

        //申请信息
        DchyXmglSqxx dchyXmglSqxx = this.generateSqxx(chxmid);
        dchyXmglChxmDto.setDchyXmglSqxx(dchyXmglSqxx);

        //待办任务
        String sqxxid = dchyXmglSqxx.getSqxxid();
        Map<String, Object> dbrwMap = Maps.newHashMap();
        dbrwMap.put("sqxxid", sqxxid);
        dbrwMap.put("zrryid", UserUtil.getCurrentUserId());
        dbrwMap.put("sqzt", Constants.DCHY_XMGL_SQZT_DSH);
        DchyXmglDbrw dchyXmglDbrw = this.generateDbrw(dbrwMap);
        dchyXmglChxmDto.setDchyXmglDbrw(dchyXmglDbrw);

        boolean result = xcsldjService.saveXcsldj(dchyXmglChxmDto);
        //新增受理信息的时候同步消息
        if (result) {
            if (CollectionUtils.isNotEmpty(sjclList)) {
                DchyXmglSjxx dchyXmglSjxx = entityMapper.selectByPrimaryKey(DchyXmglSjxx.class, sjclList.get(0).getSjxxid());
                dchyXmglChxmDto.setDchyXmglSjxx(dchyXmglSjxx);
            }
        }
        return result;
    }


    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: chxmid
     * @param: chdwmcList
     * @time 2020/12/13 14:18
     * @description 组织测绘单位信息
     */
    private List<DchyXmglChxmChdwxx> generateChdwxx(String chxmid, List<Map<String, Object>> chdwmcList) {
        List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = Lists.newArrayList();
        // 查询原有测绘项目单位信息
        Example dchyXmglChxmChdwxxExample = new Example(DchyXmglChxmChdwxx.class);
        dchyXmglChxmChdwxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmChdwxx> oldDchyXmglChxmChdwxxList = entityMapper.selectByExampleNotNull(dchyXmglChxmChdwxxExample);
        // 选择的测绘单位信息，组织重新入库
        if (CollectionUtils.isNotEmpty(chdwmcList)) {

            Map<String, Map<String, Object>> chdwxx2Chdwid = Maps.newHashMap();
            List chdwidList = Lists.newArrayList();
            List matchedChdwid = Lists.newArrayList();
            for (Map<String, Object> chdwMap : chdwmcList) {
                String chdwid = MapUtils.getString(chdwMap, "chdwid");
                //提取chdwid ，方便做比对
                chdwidList.add(chdwid);
                //chdwid对应测绘单位信息，免去后面的循环比对提取数据
                chdwxx2Chdwid.put(chdwid, chdwMap);
            }
            if (CollectionUtils.isNotEmpty(oldDchyXmglChxmChdwxxList)) {
                for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : oldDchyXmglChxmChdwxxList) {
                    if (chdwidList.contains(dchyXmglChxmChdwxx.getMlkid())) {
                        //重新选择的测绘单位信息在原有的测绘单位信息中
                        matchedChdwid.add(dchyXmglChxmChdwxx.getMlkid());
                    } else {
                        //原有的测绘单位信息未被选中，需要删除
                        dchyXmglChxmChdwxx.setChdwxxid(DELETED + dchyXmglChxmChdwxx.getChdwxxid());
                    }
                    dchyXmglChxmChdwxxList.add(dchyXmglChxmChdwxx);
                }
            }
            // 组织新加入的测绘单位信息，
            List<String> chdwidLfet = (List) CollectionUtils.removeAll(chdwidList, matchedChdwid);
            if (CollectionUtils.isNotEmpty(chdwidLfet)) {
                for (String chdwid : chdwidLfet) {
                    Map<String, Object> chdwMap = chdwxx2Chdwid.get(chdwid);
                    if (MapUtils.isNotEmpty(chdwMap)) {
                        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = new DchyXmglChxmChdwxx();
                        String chdwmc = MapUtils.getString(chdwMap, "chdwmc");
                        String chdwlx = MapUtils.getString(chdwMap, "dwlx");
                        dchyXmglChxmChdwxx.setChdwxxid(UUIDGenerator.generate18());
                        dchyXmglChxmChdwxx.setChxmid(chxmid);
                        dchyXmglChxmChdwxx.setChdwmc(chdwmc);
                        dchyXmglChxmChdwxx.setMlkid(chdwid);
                        dchyXmglChxmChdwxx.setChdwlx(chdwlx);
                        dchyXmglChxmChdwxx.setPjzt("0");
                        dchyXmglChxmChdwxxList.add(dchyXmglChxmChdwxx);
                    }
                }
            }
        } else if (CollectionUtils.isNotEmpty(oldDchyXmglChxmChdwxxList)) {
            // 如果当前没有选择测绘单位信息，则直接删除原测绘单位信息
            for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : oldDchyXmglChxmChdwxxList) {
                dchyXmglChxmChdwxx.setChdwxxid(DELETED + dchyXmglChxmChdwxx.getChdwxxid());
                dchyXmglChxmChdwxxList.add(dchyXmglChxmChdwxx);
            }
        }
        return dchyXmglChxmChdwxxList;

    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: chxmid
     * @param: clsx
     * @time 2020/12/13 14:19
     * @description 组织测量事项
     */
    private List<DchyXmglChxmClsx> generateClsx(String jsrq, String chxmid, List<Map<String, Object>> clsxMap) {
        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = Lists.newArrayList();
        Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
        dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglChxmClsx> oldDchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(dchyXmglChxmClsxExample);
        if (CollectionUtils.isNotEmpty(clsxMap)) {
            //List clsxList = Lists.newArrayList(Arrays.asList(clsx.split(Constants.DCHY_XMGL_CLSX_SEPARATOR)));//根据分号分隔转化为list
            List clsxList = Lists.newArrayList();
            for (Map<String, Object> clsxMaps : clsxMap) {
                String clsx = MapUtils.getString(clsxMaps, "clsxdm");
                clsxList.add(clsx);
            }
            // 匹配的测量事项，即仍需保存的测量事项id
            List matchedClsxid = Lists.newArrayList();
            List<DchyXmglChxmClsx> matchedClsxidMap = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : oldDchyXmglChxmClsxList) {
                    if (clsxList.contains(dchyXmglChxmClsx.getClsx())) {
                        Map<String, Object> clsxidMaps = Maps.newHashMap();
                        clsxidMaps.put("clsxid", dchyXmglChxmClsx.getClsxid());
                        matchedClsxid.add(dchyXmglChxmClsx.getClsx());
                        matchedClsxidMap.add(dchyXmglChxmClsx);

                    } else {
                        dchyXmglChxmClsx.setClsxid(DELETED + dchyXmglChxmClsx.getClsxid());
                    }
                    dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
                }
            }
            // 所有需要保存的测量事项，删除已有的测量事项后，仍需要保存的测量事项信息，直接新增
            List clsxLfet = (List) CollectionUtils.removeAll(clsxList, matchedClsxid);
            if (CollectionUtils.isNotEmpty(clsxLfet)) {
                for (Map<String, Object> clsxMaps : clsxMap) {
                    if (clsxLfet.contains(MapUtils.getString(clsxMaps, "clsxdm"))) {
                        DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                        String clsxs = MapUtils.getString(clsxMaps, "clsxdm");
                        String jcrq = MapUtils.getString(clsxMaps, "jcrq");
                        String yjjfrq = MapUtils.getString(clsxMaps, "yjjfrq");
                        String clsxid = UUIDGenerator.generate18();
                        dchyXmglChxmClsx.setClsxid(clsxid);
                        dchyXmglChxmClsx.setClsx(clsxs);
                        dchyXmglChxmClsx.setJcrq(CalendarUtil.formatDate(jcrq));
                        dchyXmglChxmClsx.setYjjfrq(yjjfrq);
                        dchyXmglChxmClsx.setChxmid(chxmid);
                        dchyXmglChxmClsx.setClzt(Constants.DCHY_XMGL_CLSX_MRCLZT_ZC);
                        dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(matchedClsxidMap)) {
                for (Map<String, Object> clsxMaps : clsxMap) {
                    for (DchyXmglChxmClsx matchedClsxidMaps : matchedClsxidMap) {
                        if (StringUtils.equals(MapUtils.getString(clsxMaps, "clsxdm"), matchedClsxidMaps.getClsx())) {
                            Date jcrq = CalendarUtil.formatDate(CommonUtil.formatEmptyValue(MapUtils.getString(clsxMaps, "jcrq")));
                            String yjjfrq = MapUtils.getString(clsxMaps, "yjjfrq");
                            String clsxid = matchedClsxidMaps.getClsxid();
                            matchedClsxidMaps.setClsxid(clsxid);
                            matchedClsxidMaps.setJcrq(jcrq);
                            matchedClsxidMaps.setYjjfrq(yjjfrq);
                            dchyXmglChxmClsxList.add(matchedClsxidMaps);
                        }
                    }
                }
            }
        } else if (CollectionUtils.isNotEmpty(oldDchyXmglChxmClsxList)) {
            // 要删除的数据进行设置
            for (DchyXmglChxmClsx dchyXmglChxmClsx : oldDchyXmglChxmClsxList) {
                dchyXmglChxmClsx.setClsxid(DELETED + dchyXmglChxmClsx.getClsxid());
                dchyXmglChxmClsxList.add(dchyXmglChxmClsx);
            }
        }
        return dchyXmglChxmClsxList;
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClsxChtl>
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @param: chxmid
     * @param: chtlMap
     * @time 2021/06/21 11:19
     * @description 组织测量事项测绘体量
     */
    private List<DchyXmglClsxChtl> generateChtl(String chxmid, String fwxz, List<Map<String, Object>> chtlMap) {
        List<DchyXmglClsxChtl> dchyXmglClsxChtlList = Lists.newArrayList();
        try {
            //原有的保存记录
            Example dchyXmglClsxChtlExample = new Example(DchyXmglClsxChtl.class);
            dchyXmglClsxChtlExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxChtl> oldDchyXmglClsxChtl = entityMapper.selectByExampleNotNull(dchyXmglClsxChtlExample);
            if (CollectionUtils.isNotEmpty(chtlMap)) {
                for (Map<String, Object> chtl : chtlMap) {
                    DchyXmglClsxChtl dchyXmglClsxChtl = new DchyXmglClsxChtl();
                    String clsx = MapUtils.getString(chtl, "clsx");
                    String sl = MapUtils.getString(chtl, "sl");
                    String jd = MapUtils.getString(chtl, "jd");
                    String dw = MapUtils.getString(chtl, "dw");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String jcrq = formatter.format(new Date());
                    dchyXmglClsxChtl.setChtlid(UUIDGenerator.generate18());
                    dchyXmglClsxChtl.setChxmid(chxmid);
                    dchyXmglClsxChtl.setClsx(clsx);
                    dchyXmglClsxChtl.setSl(sl);
                    if (StringUtils.isNotEmpty(dw)) {
                        if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_M)) {
                            dw = Constants.CLSX_CHTL_DM_M;
                        } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_PFM)) {
                            dw = Constants.CLSX_CHTL_DM_PFM;
                        } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_MC_Z)) {
                            dw = Constants.CLSX_CHTL_DM_Z;
                        }
                        dchyXmglClsxChtl.setDw(dw);
                    }
                    dchyXmglClsxChtl.setJcrq(CalendarUtil.getCurDate());

                    //计算日期
                    if (StringUtils.isNotEmpty(sl) && StringUtils.isNotEmpty(jd)) {
                        String dm = null;
                        if (StringUtils.equals(jd, Constants.CLSX_JD_ZS) && StringUtils.equals(dw, Constants.CLSX_CHTL_DM_PFM)) {
                            //农用阶段,线性工程除外
                            dm = Constants.CLSX_JD_ZS;
                        }

                        if (StringUtils.equals(jd, Constants.CLSX_JD_LX) && StringUtils.equals(dw, Constants.CLSX_CHTL_DM_PFM)) {
                            //立项阶段,线性工程除外
                            dm = Constants.CLSX_JD_LX;
                        }

                        if (StringUtils.equals(jd, Constants.CLSX_JD_GX)) {
                            //规划阶段
                            if (StringUtils.equals(clsx, Constants.CLSX_XXGC)) {
                                dm = Constants.CLSX_JD_GX_XXGC;
                            } else if (StringUtils.equals(clsx, Constants.CLSX_FCYC)) {
                                dm = Constants.CLSX_JD_GX_FCYC;
                            }
                        }

                        if (StringUtils.equals(jd, Constants.CLSX_JD_SG)) {
                            //施工阶段
                            if (StringUtils.equals(clsx, Constants.CLSX_XXFY) && StringUtils.equals(dw, Constants.CLSX_CHTL_DM_M)) {
                                dm = Constants.CLSX_JD_SG_XXFY;
                            } else if (StringUtils.equals(clsx, Constants.CLSX_HXYX)) {
                                dm = Constants.CLSX_JD_SG_HXYX;
                            }
                        }

                        if (StringUtils.equals(jd, Constants.CLSX_JD_JG)) {
                            //竣工阶段
                            if (StringUtils.equals(dw, Constants.CLSX_CHTL_DM_M)) {
                                dm = Constants.CLSX_JD_JG_XXGC;
                            } else if (StringUtils.equals(dw, Constants.CLSX_CHTL_DM_PFM)) {
                                if (StringUtils.isNotEmpty(fwxz) && StringUtils.equals(fwxz, Constants.FWLX_SPF)) {
                                    dm = Constants.CLSX_JD_JG_SPF;
                                } else if (StringUtils.isNotEmpty(fwxz) && StringUtils.equals(fwxz, Constants.FWLX_DWF)) {
                                    dm = Constants.CLSX_JD_JG_DWF;
                                }
                            }
                        }

                        if (StringUtils.isNotEmpty(dm)) {
                            DchyXmglClsxChtlPz dccyXmglClsxChtlPz = entityMapper.selectByPrimaryKey(DchyXmglClsxChtlPz.class, dm);
                            if (dccyXmglClsxChtlPz != null) {
                                String jsgs = dccyXmglClsxChtlPz.getJsgs();
                                if (StringUtils.isNotEmpty(jsgs)) {
                                    ScriptEngineManager manager = new ScriptEngineManager();
                                    ScriptEngine engine = manager.getEngineByName("js");
                                    String str1 = jsgs.substring(0, jsgs.indexOf(">")).replaceAll("sl", sl);
                                    int result = Integer.parseInt(engine.eval(str1).toString());
                                    if (result > 0) {
                                        String str2 = jsgs.substring(jsgs.indexOf("?") + 1, jsgs.lastIndexOf(":")).replaceAll("sl", sl);
                                        double resultCount = Double.parseDouble(engine.eval(str2).toString());
                                        int count = new Double(resultCount).intValue();
                                        dchyXmglClsxChtl.setJfrq(CalendarUtil.formatDate(CalendarUtil.addDay(jcrq, count)));
                                    } else {
                                        String str = jsgs.substring(0, jsgs.indexOf(":"));
                                        String str2 = jsgs.substring(str.length() + 1, jsgs.length()).trim();
                                        int count = Integer.parseInt(str2);
                                        dchyXmglClsxChtl.setJfrq(CalendarUtil.formatDate(CalendarUtil.addDay(jcrq, count)));
                                    }

                                    dchyXmglClsxChtlList.add(dchyXmglClsxChtl);
                                }
                            }
                        }
                        if (StringUtils.isEmpty(dm) && StringUtils.equals(clsx, Constants.CLSX_XXFY) && StringUtils.equals(dw, Constants.CLSX_CHTL_DM_Z)) {
                            //施工阶段,建（构）筑物放样：5个工作日内
                            dchyXmglClsxChtl.setJfrq(CalendarUtil.formatDate(CalendarUtil.addDay(jcrq, 5)));
                            dchyXmglClsxChtlList.add(dchyXmglClsxChtl);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(oldDchyXmglClsxChtl)) {
                //要删除的数据进行设置
                for (DchyXmglClsxChtl dchyXmglClsxChtl : oldDchyXmglClsxChtl) {
                    dchyXmglClsxChtl.setChtlid(DELETED + dchyXmglClsxChtl.getChtlid());
                    dchyXmglClsxChtlList.add(dchyXmglClsxChtl);
                }
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }
        return dchyXmglClsxChtlList;
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: chxmid
     * @param: clxxList
     * @time 2020/12/13 15:45
     * @description 测绘项目收件材料组织
     */
    private List<DchyXmglSjcl> generateSjcl(String chxmid, List<Map<String, Object>> clxxList) {
        List<DchyXmglSjcl> sjclList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clxxList)) {
            Map<String, Map<String, Object>> sjcl2Clid = Maps.newHashMap();
            List sjclidList = Lists.newArrayList();
            for (Map<String, Object> clxxMap : clxxList) {
                String clid = MapUtils.getString(clxxMap, "sjclid");
                if (StringUtils.isNotBlank(clid)) {
                    sjclidList.add(clid);
                    sjcl2Clid.put(clid, clxxMap);
                }
            }
            List<DchyXmglSjcl> oldDchyXmglSjclList = null;
            if (CollectionUtils.isNotEmpty(sjclidList)) {
                Example example = new Example(DchyXmglSjcl.class);
                example.createCriteria().andIn("sjclid", sjclidList);
                oldDchyXmglSjclList = entityMapper.selectByExampleNotNull(example);
            }
            if (CollectionUtils.isNotEmpty(oldDchyXmglSjclList)) {
                for (DchyXmglSjcl dchyXmglSjcl : oldDchyXmglSjclList) {
                    Map<String, Object> clxxMap = sjcl2Clid.get(dchyXmglSjcl.getSjclid());
                    if (MapUtils.isNotEmpty(clxxMap)) {
                        String clmc = MapUtils.getString(clxxMap, "clmc");
                        int fs = MapUtils.getIntValue(clxxMap, "fs");
                        String cllx = MapUtils.getString(clxxMap, "cllx");
                        int ys = MapUtils.getIntValue(clxxMap, "ys");
                        dchyXmglSjcl.setClmc(clmc);
                        dchyXmglSjcl.setCllx(cllx);
                        dchyXmglSjcl.setFs(fs);
                        dchyXmglSjcl.setYs(ys);
                    }
                    sjclList.add(dchyXmglSjcl);
                }
            }
        }
        return sjclList;
    }

    private List<DchyXmglHtxxDto> generateHtxx(String chxmid, List<DchyXmglChxmClsx> dchyXmglChxmClsxList,
                                               List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList, List<Map<String, Object>> htxxList) {
        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(htxxList)) {
            List htxxidList = Lists.newArrayList();
            Map<String, Map<String, Object>> htxxmap2Htxxid = Maps.newHashMap();

            Map<String, List<String>> chdwid2Htxxid = Maps.newHashMap();
            Map<String, List<String>> clsx2Htxxid = Maps.newHashMap();
            LinkedHashSet<String> clsxAndChdwidList = Sets.newLinkedHashSet();
            Map<String, LinkedHashSet<String>> clsxChdw2Htxxid = Maps.newHashMap();

            LinkedHashSet<String> clsxChdw2HtxxidTemp;
            for (Map<String, Object> htxxMap : htxxList) {
                String htxxid = MapUtils.getString(htxxMap, "htxxid");
                String chdwid = MapUtils.getString(htxxMap, "chdwid");
                String clsxht = MapUtils.getString(htxxMap, "clsx");
                List<String> clsxList;
                if (StringUtils.isNotBlank(clsxht)) {
                    clsxList = Arrays.asList(clsxht.split(","));//根据分号分隔转化为list

                } else {
                    clsxList = Lists.newArrayList();
                }
                List<String> chdwList;
                if (StringUtils.isNotBlank(chdwid)) {
                    chdwList = Arrays.asList(chdwid.split(","));
                } else {
                    chdwList = Lists.newArrayList();
                }

                htxxidList.add(htxxid);
                htxxmap2Htxxid.put(htxxid, htxxMap);
                clsx2Htxxid.put(htxxid, Lists.newArrayList(clsxList));
                chdwid2Htxxid.put(htxxid, Lists.newArrayList(chdwList));
                clsxChdw2HtxxidTemp = Sets.newLinkedHashSet();
                if (CollectionUtils.isNotEmpty(clsxList) && CollectionUtils.isNotEmpty(chdwList)) {
                    for (String clsx : clsxList) {
                        for (String chdw : chdwList) {
                            clsxChdw2HtxxidTemp.add(clsx + SEPARATOR + chdw);
                        }
                    }
                }
                clsxAndChdwidList.addAll(clsxChdw2HtxxidTemp);
                clsxChdw2Htxxid.put(htxxid, clsxChdw2HtxxidTemp);
            }

            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglHtxx> oldDchyXmglHtxxList = entityMapper.selectByExampleNotNull(htxxExample);
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andIn("glsxid", htxxidList);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(sjxxExample);
            List sjxxidList = Lists.newArrayList();
            Map<String, DchyXmglSjcl> sjcl2Htxxid = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                Map<String, String> htxxid2Sjxxid = Maps.newHashMap();
                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                    sjxxidList.add(dchyXmglSjxx.getSjxxid());
                    htxxid2Sjxxid.put(dchyXmglSjxx.getSjxxid(), dchyXmglSjxx.getGlsxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                List<DchyXmglSjcl> oldDchyXmglSjclList = entityMapper.selectByExampleNotNull(sjclExample);
                List matched = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(oldDchyXmglSjclList)) {
                    for (DchyXmglSjcl dchyXmglSjcl : oldDchyXmglSjclList) {
                        String sjxxid = dchyXmglSjcl.getSjxxid();
                        String glsxid = htxxid2Sjxxid.get(sjxxid);
                        sjcl2Htxxid.put(glsxid, dchyXmglSjcl);
                        matched.add(dchyXmglSjcl.getSjxxid());
                    }
                }

                List<String> left = (List<String>) CollectionUtils.removeAll(sjxxidList, matched);
                if (CollectionUtils.isNotEmpty(left)) {
                    for (String sjxxid : left) {
                        String glsxid = htxxid2Sjxxid.get(sjxxid);
                        DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
                        dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
                        dchyXmglSjcl.setSjxxid(sjxxid);
                        sjcl2Htxxid.put(glsxid, dchyXmglSjcl);
                    }
                }
            }

            // 信息id转测绘单位id
            Map<String, String> getChdwidByChdwxxid = Maps.newHashMap();
            // 测绘单位id转信息id
            Map<String, String> getChdwxxidByChdwid = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                    getChdwidByChdwxxid.put(dchyXmglChxmChdwxx.getChdwxxid(), dchyXmglChxmChdwxx.getMlkid());
                    getChdwxxidByChdwid.put(dchyXmglChxmChdwxx.getMlkid(), dchyXmglChxmChdwxx.getChdwxxid());
                }
            }

            // 事项id转事项
            Map<String, String> getClsxByClsxid = Maps.newHashMap();
            // 事项转事项id
            Map<String, String> getClsxidByClsx = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                    getClsxidByClsx.put(dchyXmglChxmClsx.getClsx(), dchyXmglChxmClsx.getClsxid());
                    getClsxByClsxid.put(dchyXmglChxmClsx.getClsxid(), dchyXmglChxmClsx.getClsx());
                }
            }

            Example dchyXmglHtxxChdwxxGxExample = new Example(DchyXmglHtxxChdwxxGx.class);
            dchyXmglHtxxChdwxxGxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglHtxxChdwxxGx> oldDchyXmglHtxxChdwxxGxList = entityMapper.selectByExampleNotNull(dchyXmglHtxxChdwxxGxExample);

            Example dchyXmglClsxHtxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
            dchyXmglClsxHtxxGxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglClsxHtxxGx> oldDchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(dchyXmglClsxHtxxGxExample);
            List clsxidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglClsxHtxxGxList)) {
                for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : oldDchyXmglClsxHtxxGxList) {
                    clsxidList.add(dchyXmglClsxHtxxGx.getClsxid());
                }
            } else if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                    clsxidList.add(dchyXmglChxmClsx.getClsxid());
                }
            }

            List chdwxxidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglHtxxChdwxxGxList)) {
                for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : oldDchyXmglHtxxChdwxxGxList) {
                    chdwxxidList.add(dchyXmglHtxxChdwxxGx.getChdwxxid());
                }
            } else if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                    chdwxxidList.add(dchyXmglChxmChdwxx.getChdwxxid());
                }
            }


            // 合同与测绘单位关系
            Map<String, List<DchyXmglHtxxChdwxxGx>> htxxChdwxxGxListMap = generateDchyXmglHtxxChdwxxGx(chxmid, oldDchyXmglHtxxChdwxxGxList, getChdwidByChdwxxid, getChdwxxidByChdwid, chdwid2Htxxid);
            // 合同与测量事项关系
            Map<String, List<DchyXmglClsxHtxxGx>> clsxHtxxGxListMap = generateDchyXmglClsxHtxxGx(chxmid, oldDchyXmglClsxHtxxGxList, getClsxByClsxid, getClsxidByClsx, clsx2Htxxid);
            // 获取测量事项和测绘单位关系列表
            Map<String, List<DchyXmglClsxChdwxxGx>> clsxChdwGxListMap = generateDchyXmglClsxChdwxxGx(chxmid, htxxidList, getClsxByClsxid, getChdwidByChdwxxid, getClsxidByClsx, getChdwxxidByChdwid, clsxAndChdwidList);

            for (DchyXmglHtxx dchyXmglHtxx : oldDchyXmglHtxxList) {
                DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
                // 收件材料处理
                DchyXmglSjcl dchyXmglSjcl = sjcl2Htxxid.get(dchyXmglHtxx.getHtxxid());
                Map<String, Object> htxxMap = htxxmap2Htxxid.get(dchyXmglHtxx.getHtxxid());
                if (null != dchyXmglSjcl && MapUtils.isNotEmpty(htxxMap)) {
                    String clmc = MapUtils.getString(htxxMap, "clmc");
                    int fs = MapUtils.getIntValue(htxxMap, "fs");
                    String cllx = MapUtils.getString(htxxMap, "cllx");
                    int ys = MapUtils.getIntValue(htxxMap, "ys");
                    dchyXmglSjcl.setFs(fs);
                    dchyXmglSjcl.setYs(ys);
                    dchyXmglSjcl.setClmc(clmc);
                    dchyXmglSjcl.setCllx(cllx);
                }
                dchyXmglHtxxDto.setDchyXmglHtxx(dchyXmglHtxx);
                dchyXmglHtxxDto.setDchyXmglSjcl(dchyXmglSjcl);
                if (dchyXmglSjcl != null) {
                    dchyXmglHtxxDto.setDchyXmglSjxx(generateSjxx(dchyXmglSjxxList, dchyXmglSjcl.getSjxxid()));
                }
                dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(htxxChdwxxGxListMap.get(dchyXmglHtxx.getHtxxid()));
                dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(clsxHtxxGxListMap.get(dchyXmglHtxx.getHtxxid()));
                LinkedHashSet<String> clsxAndChdwidListTemp = clsxChdw2Htxxid.get(dchyXmglHtxx.getHtxxid());
                List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxListTemp = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(clsxAndChdwidListTemp)) {
                    for (String temp : clsxAndChdwidListTemp) {
                        if (clsxChdwGxListMap.containsKey(temp)) {
                            dchyXmglClsxChdwxxGxListTemp.addAll(clsxChdwGxListMap.get(temp));
                            clsxChdwGxListMap.remove(temp);
                        }
                    }
                }
                dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(dchyXmglClsxChdwxxGxListTemp);
                dchyXmglHtxxDtoList.add(dchyXmglHtxxDto);
            }

            if (MapUtils.isNotEmpty(clsxChdwGxListMap) && CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                DchyXmglHtxxDto dchyXmglHtxxDto = dchyXmglHtxxDtoList.get(0);
                for (Map.Entry<String, List<DchyXmglClsxChdwxxGx>> entry : clsxChdwGxListMap.entrySet()) {
                    if (CollectionUtils.isNotEmpty(dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList())) {
                        dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList().addAll(entry.getValue());
                    } else {
                        dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(Lists.newArrayList());
                        dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList().addAll(entry.getValue());
                    }
                }
            }


        }
        return dchyXmglHtxxDtoList;
    }

    private Map<String, List<DchyXmglClsxChdwxxGx>> generateDchyXmglClsxChdwxxGx(String chxmid, List htxxidList,
                                                                                 Map<String, String> getClsxByClsxid,
                                                                                 Map<String, String> getChdwidByChdwxxid,
                                                                                 Map<String, String> getClsxidByClsx,
                                                                                 Map<String, String> getChdwxxidByChdwid,
                                                                                 LinkedHashSet<String> clsxAndChdwidList) {

        Map<String, List<DchyXmglClsxChdwxxGx>> clsxChdwGxListMap = Maps.newHashMap();
        List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList;
        if (CollectionUtils.isNotEmpty(htxxidList)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("htxxidList", htxxidList);
            List<DchyXmglClsxChdwxxGx> oldDchyXmglClsxChdwxxGxList = dchyXmglClsxChdwxxGxMapper.getClsxChdwgxByHtxxid(param);

            if (CollectionUtils.isNotEmpty(oldDchyXmglClsxChdwxxGxList)) {
                LinkedHashSet<String> matched = Sets.newLinkedHashSet();
                for (DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx : oldDchyXmglClsxChdwxxGxList) {
                    String clsx = getClsxByClsxid.get(dchyXmglClsxChdwxxGx.getClsxid());
                    String chdwid = getChdwidByChdwxxid.get(dchyXmglClsxChdwxxGx.getChdwxxid());
                    String key = clsx + SEPARATOR + chdwid;
                    if (StringUtils.indexOf(dchyXmglClsxChdwxxGx.getClsxid(), DELETED) >= 0 || StringUtils.indexOf(dchyXmglClsxChdwxxGx.getChdwxxid(), DELETED) >= 0) {
                        dchyXmglClsxChdwxxGx.setGxid(DELETED + dchyXmglClsxChdwxxGx.getGxid());
                    } else if (StringUtils.isNoneBlank(clsx, chdwid) && clsxAndChdwidList.contains(key)) {
                        matched.add(key);
                    } else {
                        dchyXmglClsxChdwxxGx.setGxid(DELETED + dchyXmglClsxChdwxxGx.getGxid());
                    }
                    // 分组数据
                    dchyXmglClsxChdwxxGxList = clsxChdwGxListMap.get(key);
                    if (CollectionUtils.isEmpty(dchyXmglClsxChdwxxGxList)) {
                        dchyXmglClsxChdwxxGxList = Lists.newArrayList();
                    }
                    dchyXmglClsxChdwxxGxList.add(dchyXmglClsxChdwxxGx);
                    clsxChdwGxListMap.put(key, dchyXmglClsxChdwxxGxList);
                }
                Collection<String> left = CollectionUtils.removeAll(clsxAndChdwidList, matched);
                if (CollectionUtils.isNotEmpty(left)) {
                    for (String clsxAndChdwid : left) {
                        String[] clsxs = clsxAndChdwid.split(SEPARATOR);
                        if (null != clsxs && clsxs.length == 2) {
                            String clsxid = getClsxidByClsx.get(clsxs[0]);
                            String chdwxxid = getChdwxxidByChdwid.get(clsxs[1]);
                            if (StringUtils.isNoneBlank(clsxid, chdwxxid) && StringUtils.indexOf(clsxid, DELETED) < 0 && StringUtils.indexOf(chdwxxid, DELETED) < 0) {
                                DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx = new DchyXmglClsxChdwxxGx();
                                dchyXmglClsxChdwxxGx.setGxid(UUIDGenerator.generate18());
                                dchyXmglClsxChdwxxGx.setChxmid(chxmid);
                                dchyXmglClsxChdwxxGx.setChdwxxid(chdwxxid);
                                dchyXmglClsxChdwxxGx.setClsxid(clsxid);
                                // 分组数据
                                dchyXmglClsxChdwxxGxList = clsxChdwGxListMap.get(clsxAndChdwid);
                                if (CollectionUtils.isEmpty(dchyXmglClsxChdwxxGxList)) {
                                    dchyXmglClsxChdwxxGxList = Lists.newArrayList();
                                }
                                dchyXmglClsxChdwxxGxList.add(dchyXmglClsxChdwxxGx);
                                clsxChdwGxListMap.put(clsxAndChdwid, dchyXmglClsxChdwxxGxList);
                            }
                        }
                    }
                }

            } else if (CollectionUtils.isNotEmpty(clsxAndChdwidList)) {
                for (String clsxAndChdwid : clsxAndChdwidList) {
                    String[] clsxs = clsxAndChdwid.split(SEPARATOR);
                    if (null != clsxs && clsxs.length == 2) {
                        String clsxid = getClsxidByClsx.get(clsxs[0]);
                        String chdwxxid = getChdwxxidByChdwid.get(clsxs[1]);
                        if (StringUtils.isNoneBlank(clsxid, chdwxxid)) {
                            DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx = new DchyXmglClsxChdwxxGx();
                            dchyXmglClsxChdwxxGx.setGxid(UUIDGenerator.generate18());
                            dchyXmglClsxChdwxxGx.setChdwxxid(chdwxxid);
                            dchyXmglClsxChdwxxGx.setChxmid(chxmid);
                            dchyXmglClsxChdwxxGx.setClsxid(clsxid);
                            // 分组数据
                            dchyXmglClsxChdwxxGxList = clsxChdwGxListMap.get(clsxAndChdwid);
                            if (CollectionUtils.isEmpty(dchyXmglClsxChdwxxGxList)) {
                                dchyXmglClsxChdwxxGxList = Lists.newArrayList();
                            }
                            dchyXmglClsxChdwxxGxList.add(dchyXmglClsxChdwxxGx);
                            clsxChdwGxListMap.put(clsxAndChdwid, dchyXmglClsxChdwxxGxList);
                        }
                    }
                }
            }
        }
        return clsxChdwGxListMap;
    }

    private Map<String, List<DchyXmglClsxHtxxGx>> generateDchyXmglClsxHtxxGx(String chxmid) {
        Map<String, List<DchyXmglClsxHtxxGx>> clsxHtxxGxListMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(chxmid)) {
            Example htxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
            htxxGxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxHtxxGx> htxxGxList = entityMapper.selectByExample(htxxGxExample);
            if (CollectionUtils.isNotEmpty(htxxGxList)) {
                String htxxid = htxxGxList.get(0).getHtxxid();
                clsxHtxxGxListMap.put(htxxid, htxxGxList);
            }
        }
        return clsxHtxxGxListMap;
    }

    private Map<String, List<DchyXmglClsxHtxxGx>> generateDchyXmglClsxHtxxGx(String chxmid, List<DchyXmglClsxHtxxGx> oldDchyXmglClsxHtxxGxList,
                                                                             Map<String, String> getClsxByClsxid,
                                                                             Map<String, String> getClsxidByClsx,
                                                                             Map<String, List<String>> clsx2Htxxid) {
        List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList;
        Map<String, List<DchyXmglClsxHtxxGx>> clsxHtxxGxListMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(oldDchyXmglClsxHtxxGxList)) {
            Map<String, List<DchyXmglClsxHtxxGx>> htClsxGx2Htxxid = Maps.newHashMap();
            List<DchyXmglClsxHtxxGx> tempGxList;
            for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : oldDchyXmglClsxHtxxGxList) {
                tempGxList = htClsxGx2Htxxid.get(dchyXmglClsxHtxxGx.getHtxxid());
                if (null == tempGxList) {
                    tempGxList = Lists.newArrayList();
                }
                tempGxList.add(dchyXmglClsxHtxxGx);
                htClsxGx2Htxxid.put(dchyXmglClsxHtxxGx.getHtxxid(), tempGxList);
            }
            for (Map.Entry<String, List<String>> entry : clsx2Htxxid.entrySet()) {
                List<String> clsxTemp = entry.getValue();
                List<DchyXmglClsxHtxxGx> oldClsxHtGxList = htClsxGx2Htxxid.get(entry.getKey());
                if (CollectionUtils.isNotEmpty(clsxTemp) && CollectionUtils.isNotEmpty(oldClsxHtGxList)) {
                    List<String> matched = Lists.newArrayList();
                    for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : oldClsxHtGxList) {
                        String clsx = getClsxByClsxid.get(dchyXmglClsxHtxxGx.getClsxid());
                        if (clsxTemp.contains(clsx)) {
                            matched.add(clsx);
                        } else {
                            dchyXmglClsxHtxxGx.setGxid(DELETED + dchyXmglClsxHtxxGx.getGxid());
                        }
                        // 分组数据
                        dchyXmglClsxHtxxGxList = clsxHtxxGxListMap.get(dchyXmglClsxHtxxGx.getHtxxid());
                        if (CollectionUtils.isEmpty(dchyXmglClsxHtxxGxList)) {
                            dchyXmglClsxHtxxGxList = Lists.newArrayList();
                        }
                        dchyXmglClsxHtxxGxList.add(dchyXmglClsxHtxxGx);
                        clsxHtxxGxListMap.put(dchyXmglClsxHtxxGx.getHtxxid(), dchyXmglClsxHtxxGxList);
                    }
                    List<String> left = (List<String>) CollectionUtils.removeAll(clsxTemp, matched);
                    if (CollectionUtils.isNotEmpty(left)) {
                        for (String clsxLeft : left) {
                            String clsxid = getClsxidByClsx.get(clsxLeft);
                            if (StringUtils.isNotBlank(clsxid)) {
                                DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx = new DchyXmglClsxHtxxGx();
                                dchyXmglClsxHtxxGx.setGxid(UUIDGenerator.generate18());
                                dchyXmglClsxHtxxGx.setClsxid(clsxid);
                                dchyXmglClsxHtxxGx.setHtxxid(entry.getKey());
                                dchyXmglClsxHtxxGx.setChxmid(chxmid);
                                // 分组数据
                                dchyXmglClsxHtxxGxList = clsxHtxxGxListMap.get(dchyXmglClsxHtxxGx.getHtxxid());
                                if (CollectionUtils.isEmpty(dchyXmglClsxHtxxGxList)) {
                                    dchyXmglClsxHtxxGxList = Lists.newArrayList();
                                }
                                dchyXmglClsxHtxxGxList.add(dchyXmglClsxHtxxGx);
                                clsxHtxxGxListMap.put(dchyXmglClsxHtxxGx.getHtxxid(), dchyXmglClsxHtxxGxList);
                            }
                        }
                    }
                }
            }
        } else if (MapUtils.isNotEmpty(clsx2Htxxid)) {
            for (Map.Entry<String, List<String>> entry : clsx2Htxxid.entrySet()) {
                List<String> clsxTemp = entry.getValue();
                if (CollectionUtils.isNotEmpty(clsxTemp)) {
                    for (String clsx : clsxTemp) {
                        String clsxid = getClsxidByClsx.get(clsx);
                        if (StringUtils.isNotBlank(clsxid)) {
                            DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx = new DchyXmglClsxHtxxGx();
                            dchyXmglClsxHtxxGx.setGxid(UUIDGenerator.generate18());
                            dchyXmglClsxHtxxGx.setClsxid(clsxid);
                            dchyXmglClsxHtxxGx.setHtxxid(entry.getKey());
                            dchyXmglClsxHtxxGx.setChxmid(chxmid);
                            // 分组数据
                            dchyXmglClsxHtxxGxList = clsxHtxxGxListMap.get(dchyXmglClsxHtxxGx.getHtxxid());
                            if (CollectionUtils.isEmpty(dchyXmglClsxHtxxGxList)) {
                                dchyXmglClsxHtxxGxList = Lists.newArrayList();
                            }
                            dchyXmglClsxHtxxGxList.add(dchyXmglClsxHtxxGx);
                            clsxHtxxGxListMap.put(dchyXmglClsxHtxxGx.getHtxxid(), dchyXmglClsxHtxxGxList);
                        }
                    }
                }
            }
        }
        return clsxHtxxGxListMap;
    }

    private Map<String, List<DchyXmglHtxxChdwxxGx>> generateDchyXmglHtxxChdwxxGx(String chxmid, List<DchyXmglHtxxChdwxxGx> oldDchyXmglHtxxChdwxxGxList,
                                                                                 Map<String, String> getChdwidByChdwxxid,
                                                                                 Map<String, String> getChdwxxidByChdwid,
                                                                                 Map<String, List<String>> chdwid2Htxxid) {
        List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList;
        Map<String, List<DchyXmglHtxxChdwxxGx>> htxxChdwxxGxListMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(oldDchyXmglHtxxChdwxxGxList)) {
            Map<String, List<DchyXmglHtxxChdwxxGx>> htChdwGx2Htxxid = Maps.newHashMap();
            List<DchyXmglHtxxChdwxxGx> temp;
            //先处理
            for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : oldDchyXmglHtxxChdwxxGxList) {
                temp = htChdwGx2Htxxid.get(dchyXmglHtxxChdwxxGx.getHtxxid());
                if (null == temp) {
                    temp = Lists.newArrayList();
                }
                temp.add(dchyXmglHtxxChdwxxGx);
                htChdwGx2Htxxid.put(dchyXmglHtxxChdwxxGx.getHtxxid(), temp);
            }
            for (Map.Entry<String, List<String>> entry : chdwid2Htxxid.entrySet()) {
                List<String> chdwidTemp = entry.getValue();
                List<DchyXmglHtxxChdwxxGx> oldHtChdwGxList = htChdwGx2Htxxid.get(entry.getKey());
                if (CollectionUtils.isNotEmpty(chdwidTemp) && CollectionUtils.isNotEmpty(oldHtChdwGxList)) {
                    List<String> matched = Lists.newArrayList();
                    for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : oldHtChdwGxList) {
                        String dwid = getChdwidByChdwxxid.get(dchyXmglHtxxChdwxxGx.getChdwxxid());
                        if (chdwidTemp.contains(dwid)) {
                            matched.add(dwid);
                        } else {
                            dchyXmglHtxxChdwxxGx.setGxid(DELETED + dchyXmglHtxxChdwxxGx.getGxid());
                        }
                        // 分组数据
                        dchyXmglHtxxChdwxxGxList = htxxChdwxxGxListMap.get(dchyXmglHtxxChdwxxGx.getHtxxid());
                        if (CollectionUtils.isEmpty(dchyXmglHtxxChdwxxGxList)) {
                            dchyXmglHtxxChdwxxGxList = Lists.newArrayList();
                        }
                        dchyXmglHtxxChdwxxGxList.add(dchyXmglHtxxChdwxxGx);
                        htxxChdwxxGxListMap.put(dchyXmglHtxxChdwxxGx.getHtxxid(), dchyXmglHtxxChdwxxGxList);
                    }
                    List<String> left = (List<String>) CollectionUtils.removeAll(chdwidTemp, matched);
                    if (CollectionUtils.isNotEmpty(left)) {
                        for (String chdwidLeft : left) {
                            String chdwxxid = getChdwxxidByChdwid.get(chdwidLeft);
                            if (StringUtils.isNotBlank(chdwxxid)) {
                                DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx = new DchyXmglHtxxChdwxxGx();
                                dchyXmglHtxxChdwxxGx.setGxid(UUIDGenerator.generate18());
                                dchyXmglHtxxChdwxxGx.setChdwxxid(chdwxxid);
                                dchyXmglHtxxChdwxxGx.setHtxxid(entry.getKey());
                                dchyXmglHtxxChdwxxGx.setChxmid(chxmid);
                                // 分组数据
                                dchyXmglHtxxChdwxxGxList = htxxChdwxxGxListMap.get(dchyXmglHtxxChdwxxGx.getHtxxid());
                                if (CollectionUtils.isEmpty(dchyXmglHtxxChdwxxGxList)) {
                                    dchyXmglHtxxChdwxxGxList = Lists.newArrayList();
                                }
                                dchyXmglHtxxChdwxxGxList.add(dchyXmglHtxxChdwxxGx);
                                htxxChdwxxGxListMap.put(dchyXmglHtxxChdwxxGx.getHtxxid(), dchyXmglHtxxChdwxxGxList);
                            }
                        }
                    }
                }
            }
        } else if (MapUtils.isNotEmpty(chdwid2Htxxid)) {
            for (Map.Entry<String, List<String>> entry : chdwid2Htxxid.entrySet()) {
                List<String> chdwidTemp = entry.getValue();
                if (CollectionUtils.isNotEmpty(chdwidTemp)) {
                    for (String dwid : chdwidTemp) {
                        String chdwxxid = getChdwxxidByChdwid.get(dwid);
                        if (StringUtils.isNotBlank(chdwxxid)) {
                            DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx = new DchyXmglHtxxChdwxxGx();
                            dchyXmglHtxxChdwxxGx.setGxid(UUIDGenerator.generate18());
                            dchyXmglHtxxChdwxxGx.setChdwxxid(chdwxxid);
                            dchyXmglHtxxChdwxxGx.setHtxxid(entry.getKey());
                            dchyXmglHtxxChdwxxGx.setChxmid(chxmid);
                            // 分组数据
                            dchyXmglHtxxChdwxxGxList = htxxChdwxxGxListMap.get(dchyXmglHtxxChdwxxGx.getHtxxid());
                            if (CollectionUtils.isEmpty(dchyXmglHtxxChdwxxGxList)) {
                                dchyXmglHtxxChdwxxGxList = Lists.newArrayList();
                            }
                            dchyXmglHtxxChdwxxGxList.add(dchyXmglHtxxChdwxxGx);
                            htxxChdwxxGxListMap.put(dchyXmglHtxxChdwxxGx.getHtxxid(), dchyXmglHtxxChdwxxGxList);
                        }
                    }
                }
            }
        }
        return htxxChdwxxGxListMap;
    }

    @Override
    @Transactional
    public DchyXmglHtxxDto generateHtxxDto(Map<String, Object> htxxMap) {
        DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
        if (MapUtils.isNotEmpty(htxxMap)) {
            //更新测绘项目测量事项,暂时废弃
            //fileUploadHtxxService.saveClsxByChxmid(htxxMap);

            //更新测绘项目测绘单位
            fileUploadHtxxService.saveChdwxxByChxmid(htxxMap);

            String folderId = (String) htxxMap.get("folderId");
            String parentId = (String) htxxMap.get("parentId");

            String chxmid = MapUtils.getString(htxxMap, "chxmid");
            String htxxid = MapUtils.getString(htxxMap, "htxxid");

            List htxxidList = Lists.newArrayList();
            htxxidList.add(htxxid);

            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglHtxx> oldDchyXmglHtxxList = entityMapper.selectByExampleNotNull(htxxExample);

            DchyXmglHtxx dchyXmglHtxx;
            if (CollectionUtils.isNotEmpty(oldDchyXmglHtxxList)) {
                dchyXmglHtxx = oldDchyXmglHtxxList.get(0);
            } else {
                dchyXmglHtxx = new DchyXmglHtxx();
                dchyXmglHtxx.setChxmid(chxmid);
                dchyXmglHtxx.setHtxxid(htxxid);
                oldDchyXmglHtxxList = Lists.newArrayList();
                oldDchyXmglHtxxList.add(dchyXmglHtxx);
            }
            dchyXmglHtxx.setWjzxid(parentId);//设置wjzxid


            String sjxxid;
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andIn("glsxid", htxxidList);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(sjxxExample);
            List sjxxidList = Lists.newArrayList();
            Map<String, DchyXmglSjcl> sjcl2Htxxid = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                Map<String, String> htxxid2Sjxxid = Maps.newHashMap();
                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                    sjxxidList.add(dchyXmglSjxx.getSjxxid());
                    htxxid2Sjxxid.put(dchyXmglSjxx.getSjxxid(), dchyXmglSjxx.getGlsxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                List<DchyXmglSjcl> oldDchyXmglSjclList = entityMapper.selectByExampleNotNull(sjclExample);
                sjxxid = dchyXmglSjxxList.get(0).getSjxxid();
                dchyXmglHtxxDto.setDchyXmglSjxx(dchyXmglSjxxList.get(0));
                if (CollectionUtils.isNotEmpty(oldDchyXmglSjclList)) {
                    for (DchyXmglSjcl dchyXmglSjcl : oldDchyXmglSjclList) {
                        sjxxid = dchyXmglSjcl.getSjxxid();
                        String glsxid = htxxid2Sjxxid.get(sjxxid);
                        sjcl2Htxxid.put(glsxid, dchyXmglSjcl);
                    }
                }
            } else {
                sjxxid = UUIDGenerator.generate18();
                DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
                dchyXmglSjxx.setGlsxid(htxxid);
                dchyXmglSjxx.setSjr(UserUtil.getCurrentUserId());
                dchyXmglSjxx.setSjsj(new Date());
                dchyXmglSjxx.setSjxxid(sjxxid);
                entityMapper.saveOrUpdate(dchyXmglSjxx, sjxxid);
            }

            if (MapUtils.isEmpty(sjcl2Htxxid)) {
                DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
                dchyXmglSjcl.setSjxxid(sjxxid);
                dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
                sjcl2Htxxid.put(htxxid, dchyXmglSjcl);
            }

            LinkedHashSet<String> clsxAndChdwidList = Sets.newLinkedHashSet();

            String chdwid = MapUtils.getString(htxxMap, "chdwid");
            String clsxht = MapUtils.getString(htxxMap, "clsx");
            List<String> clsxList = Arrays.asList(clsxht.split(","));//根据分号分隔转化为list
            List<String> chdwList = Arrays.asList(chdwid.split(","));


            Map<String, List<String>> chdwid2Htxxid = Maps.newHashMap();
            chdwid2Htxxid.put(htxxid, chdwList);

            Map<String, List<String>> clsx2Htxxid = Maps.newHashMap();
            clsx2Htxxid.put(htxxid, clsxList);

            LinkedHashSet<String> clsxChdw2Htxxid = Sets.newLinkedHashSet();
            if (CollectionUtils.isNotEmpty(clsxList) && CollectionUtils.isNotEmpty(chdwList)) {
                for (String clsx : clsxList) {
                    for (String chdw : chdwList) {
                        clsxChdw2Htxxid.add(clsx + SEPARATOR + chdw);
                        clsxAndChdwidList.add(clsx + SEPARATOR + chdw);
                    }
                }
            }


            Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
            dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(dchyXmglChxmClsxExample);

            Example dchyXmglChxmChdwxxExample = new Example(DchyXmglChxmChdwxx.class);
            dchyXmglChxmChdwxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapper.selectByExampleNotNull(dchyXmglChxmChdwxxExample);


            // 信息id转测绘单位id
            Map<String, String> getChdwidByChdwxxid = Maps.newHashMap();
            // 测绘单位id转信息id
            Map<String, String> getChdwxxidByChdwid = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                    getChdwidByChdwxxid.put(dchyXmglChxmChdwxx.getChdwxxid(), dchyXmglChxmChdwxx.getMlkid());
                    getChdwxxidByChdwid.put(dchyXmglChxmChdwxx.getMlkid(), dchyXmglChxmChdwxx.getChdwxxid());
                }
            }

            // 事项id转事项
            Map<String, String> getClsxByClsxid = Maps.newHashMap();
            // 事项转事项id
            Map<String, String> getClsxidByClsx = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                    getClsxidByClsx.put(dchyXmglChxmClsx.getClsx(), dchyXmglChxmClsx.getClsxid());
                    getClsxByClsxid.put(dchyXmglChxmClsx.getClsxid(), dchyXmglChxmClsx.getClsx());
                }
            }

            Example dchyXmglHtxxChdwxxGxExample = new Example(DchyXmglHtxxChdwxxGx.class);
            dchyXmglHtxxChdwxxGxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglHtxxChdwxxGx> oldDchyXmglHtxxChdwxxGxList = entityMapper.selectByExampleNotNull(dchyXmglHtxxChdwxxGxExample);

            Example dchyXmglClsxHtxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
            dchyXmglClsxHtxxGxExample.createCriteria().andIn("htxxid", htxxidList);
            List<DchyXmglClsxHtxxGx> oldDchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(dchyXmglClsxHtxxGxExample);
            List clsxidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglClsxHtxxGxList)) {
                for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : oldDchyXmglClsxHtxxGxList) {
                    clsxidList.add(dchyXmglClsxHtxxGx.getClsxid());
                }
            } else if (CollectionUtils.isNotEmpty(clsxList)) {
                for (String clsx : clsxList) {
                    clsxidList.add(getClsxidByClsx.get(clsx));
                }
            }

            List chdwxxidList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(oldDchyXmglHtxxChdwxxGxList)) {
                for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : oldDchyXmglHtxxChdwxxGxList) {
                    chdwxxidList.add(dchyXmglHtxxChdwxxGx.getChdwxxid());
                }
            } else if (CollectionUtils.isNotEmpty(chdwList)) {
                for (String chdw : chdwList) {
                    chdwxxidList.add(getChdwxxidByChdwid.get(chdw));
                }
            }


            // 合同与测绘单位关系
            Map<String, List<DchyXmglHtxxChdwxxGx>> htxxChdwxxGxListMap = generateDchyXmglHtxxChdwxxGx(chxmid, oldDchyXmglHtxxChdwxxGxList, getChdwidByChdwxxid, getChdwxxidByChdwid, chdwid2Htxxid);
            // 合同与测量事项关系
            Map<String, List<DchyXmglClsxHtxxGx>> clsxHtxxGxListMap = generateDchyXmglClsxHtxxGx(chxmid);
            // 获取测量事项和测绘单位关系列表
            Map<String, List<DchyXmglClsxChdwxxGx>> clsxChdwGxListMap = generateDchyXmglClsxChdwxxGx(chxmid, htxxidList, getClsxByClsxid, getChdwidByChdwxxid, getClsxidByClsx, getChdwxxidByChdwid, clsxAndChdwidList);


            // 收件材料处理
            DchyXmglSjcl dchyXmglSjcl = sjcl2Htxxid.get(dchyXmglHtxx.getHtxxid());
            if (null != dchyXmglSjcl && MapUtils.isNotEmpty(htxxMap)) {
                String clmc = MapUtils.getString(htxxMap, "clmc");
                int fs = MapUtils.getIntValue(htxxMap, "fs");
                String cllx = MapUtils.getString(htxxMap, "cllx");
                int ys = MapUtils.getIntValue(htxxMap, "ys");
                dchyXmglSjcl.setFs(fs);
                dchyXmglSjcl.setYs(ys);
                dchyXmglSjcl.setClmc(clmc);
                dchyXmglSjcl.setCllx(cllx);
                dchyXmglSjcl.setClrq(new Date());
                dchyXmglSjcl.setWjzxid(folderId);
            }
            dchyXmglHtxxDto.setGlsxid(chxmid);
            dchyXmglHtxxDto.setDchyXmglHtxx(dchyXmglHtxx);
            dchyXmglHtxxDto.setDchyXmglSjcl(dchyXmglSjcl);
            dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(htxxChdwxxGxListMap.get(dchyXmglHtxx.getHtxxid()));
            dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(clsxHtxxGxListMap.get(dchyXmglHtxx.getHtxxid()));
            List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxListTemp = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(clsxChdw2Htxxid)) {
                for (Map.Entry<String, List<DchyXmglClsxChdwxxGx>> entry : clsxChdwGxListMap.entrySet()) {
                    dchyXmglClsxChdwxxGxListTemp.addAll(entry.getValue());
                }
            }
            dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(dchyXmglClsxChdwxxGxListTemp);
        }
        return dchyXmglHtxxDto;
    }

    public DchyXmglSjxx generateSjxx(List<DchyXmglSjxx> sjxxes, String sjxxid) {
        if (CollectionUtils.isNotEmpty(sjxxes)) {
            for (DchyXmglSjxx dchyXmglSjxx : sjxxes) {
                if (StringUtils.equals(dchyXmglSjxx.getSjxxid(), sjxxid)) {
                    return dchyXmglSjxx;
                }
            }
        }
        return null;
    }

    public DchyXmglSqxx generateSqxx(String chxmid) {
        Example example = new Example(DchyXmglSqxx.class);
        example.createCriteria().andEqualTo("glsxid", chxmid);
        List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(example);
        DchyXmglSqxx dchyXmglSqxx;
        if (CollectionUtils.isEmpty(dchyXmglSqxxList)) {
            //更新申请信息
            dchyXmglSqxx = new DchyXmglSqxx();
            /*生成sqxxid*/
            String sqxxid = UUIDGenerator.generate18();
            String blsx = Constants.BLSX_FBWT; //办理事项
            String sqr = UserUtil.getCurrentUserId();
            String sqzt = Constants.DCHY_XMGL_SQZT_DSH;//1:未审核
            String sqbh = Calendar.getInstance().getTimeInMillis() + sqxxid;
            Date sqsj = new Date();
            dchyXmglSqxx.setSqxxid(sqxxid);
            //通过用户id去dchy_xmgl_yhdw里查找dwmc保存
            dchyXmglSqxx.setSqjgmc(getDwmc());
            dchyXmglSqxx.setBlsx(blsx);
            dchyXmglSqxx.setSqr(sqr);
            dchyXmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
            dchyXmglSqxx.setSqsj(sqsj);
            dchyXmglSqxx.setSqzt(sqzt);
            dchyXmglSqxx.setGlsxid(chxmid);
            dchyXmglSqxx.setSqbh(sqbh);
            entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        } else {
            dchyXmglSqxx = dchyXmglSqxxList.get(0);
        }
        return dchyXmglSqxx;
    }

    /**
     * 获取当前用户的单位名称
     *
     * @return
     */
    private String getDwmc() {
        String yhId = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", yhId);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwmc = yhdwList.get(0).getDwmc();
            if (StringUtils.isNotBlank(dwmc)) {
                return dwmc;
            }
        }
        return "";
    }

    public DchyXmglDbrw generateDbrw(Map<String, Object> paramMap) {
        String zrryid = CommonUtil.formatEmptyValue(paramMap.get("zrryid"));
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("sqxxid"));
        String sqzt = CommonUtil.formatEmptyValue(paramMap.get("sqzt"));
        DchyXmglDbrw dchyXmglDbrw = new DchyXmglDbrw();
        if (StringUtils.isNoneEmpty(zrryid, sqxxid)) {
            Example example = new Example(DchyXmglDbrw.class);
            example.createCriteria().andEqualTo("sqxxid", sqxxid);
            List<DchyXmglDbrw> dchyXmglDbrwList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isEmpty(dchyXmglDbrwList)) {
                dchyXmglDbrw.setDbrwid(UUIDGenerator.generate18());
                dchyXmglDbrw.setSqxxid(sqxxid);
                dchyXmglDbrw.setZrryid(zrryid);
                dchyXmglDbrw.setBlryid(zrryid);
                dchyXmglDbrw.setZrsj(new Date());
                dchyXmglDbrw.setDqjd(Constants.DQJD_SH);
                entityMapper.insertSelective(dchyXmglDbrw);

                DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                dchyXmglSqxx.setSqxxid(sqxxid);
                dchyXmglSqxx.setSqzt(sqzt);

                entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
            } else {
                dchyXmglDbrw = dchyXmglDbrwList.get(0);
            }
        }
        return dchyXmglDbrw;
    }

}