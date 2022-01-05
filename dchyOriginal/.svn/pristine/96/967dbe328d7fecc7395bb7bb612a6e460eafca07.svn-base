package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglOnlineDelegationMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.*;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.EhcacheUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/2/22 8:58
 * @description
 */
@Service
public class DchyXmglOnlineDelegationServiceImpl implements DchyXmglOnlineDelegationService {

    @Autowired
    private DchyXmglOnlineDelegationMapper onlineDelegationMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    PushDataToMqService pushDataToMqService;
    @Autowired
    MessagePushService messagePushService;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    private DchyXmglHtService dchyXmglHtService;
    @Autowired
    private DchyXmglMlkService dchyXmglMlkService;
    @Resource(name = "platformUtil")
    private PlatformUtil platformUtil;

    private final String DELETED = "delete_";

    private static final Log logger = LogFactory.getLog(DchyXmglOnlineDelegationServiceImpl.class);

    //设置核心池大小
    int corePoolSize = 20;
    //设置线程池最大能接受多少线程
    int maximumPoolSize = 200;
    //当前线程数大于corePoolSize、小于maximumPoolSize时，超出corePoolSize的线程数的生命周期
    long keepActiveTime = 200;
    //设置时间单位，秒
    TimeUnit timeUnit = TimeUnit.SECONDS;
    //设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为300
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(300);
    //创建ThreadPoolExecutor线程池对象，并初始化该对象的各种参数
    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit, workQueue);

    /**
     * 多条件查询项目委托信息
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryProjectEntrustMultipleConditionsByPage(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String xmgcbg = CommonUtil.ternaryOperator(data.get("xmgcbg"));//工程编号
        String xmgcmc = CommonUtil.ternaryOperator(data.get("xmgcmc"));//工程名称
        String wtbh = CommonUtil.ternaryOperator(data.get("wtbh"));//委托编号
        String jsdwmc = CommonUtil.ternaryOperator(data.get("jsdwmc"));//建设单位名称
        String status = CommonUtil.ternaryOperator(data.get("status"));//当前状态
        String mlkid = this.getMlkByUserId();//获取当前测绘单位的mlkid
        map.put("xmgcbg", xmgcbg);
        map.put("xmgcmc", xmgcmc);
        map.put("wtbh", wtbh);
        map.put("jsdwmc", jsdwmc);
        map.put("status", status);
        map.put("mlkid", mlkid);
        try {
            /*分页查询在线委托项目信息*/
            Page<Map<String, Object>> onlineEntrustByPage = repository.selectPaging("queryProjectEntrustMultipleConditionsByPage", map, page - 1, pageSize);
            return onlineEntrustByPage;
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return null;
    }


    /**
     * 合同变更前获取合同文件信息
     *
     * @param
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBeforeHtChange4Sjxx(String chxmid, String ssmkid) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid) && StringUtils.isNotBlank(ssmkid)) {
            Example htExample = new Example(DchyXmglHtxx.class);
            htExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExample(htExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx htxx : htxxList) {
                    mapList = dchyXmglMlkService.getSjclXxFromSxbf(htxx.getHtxxid(), ssmkid);
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        String tempHtxxid = (String) mapList.get(0).get("HTXXID");
                        List<String> clsxList = this.generateClsxByHtxxidFromXsbf(tempHtxxid);
                        mapList.get(0).put("CLSXS", clsxList);
                    }
                }
            }
        }
        return mapList;
    }

    /**
     * 测绘单位在线核验
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int verification(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        String wtzt = CommonUtil.ternaryOperator(data.get("wtzt"));//委托状态
        String hyyj = CommonUtil.ternaryOperator(data.get("hyyj"));//核验意见
        String lxr = CommonUtil.ternaryOperator(data.get("lxr"));//联系人
        String lxdh = CommonUtil.ternaryOperator(data.get("lxdh"));//联系电话
        String fwxz = CommonUtil.formatEmptyValue(data.get("fwxz"));//房屋性质
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                if (null != xmglChxm) {
                    /*修改委托状态 3:已接受  4:已拒绝*/
                    if (StringUtils.isNotBlank(wtzt)) {
                        xmglChxm.setWtzt(wtzt);
                        xmglChxm.setChjglxr(lxr);
                        xmglChxm.setChjglxdh(lxdh);
                        xmglChxm.setFwxz(fwxz);
                    }

                    /*更新项目状态*/
                    int result = entityMapper.saveOrUpdate(xmglChxm, xmglChxm.getChxmid());

                    //测量事项测绘体量相关信息
                    List<Map<String, Object>> chtlList = (List<Map<String, Object>>) data.get("chtl");
                    List<DchyXmglClsxChtl> dchyXmglClsxChtlList = generateChtl(chxmid, fwxz, chtlList);
                    entityMapper.batchSaveSelective(dchyXmglClsxChtlList);

                    if (result > 0) {
                        /*通过chxmid获取申请信息*/
                        Example sqxxExample = new Example(DchyXmglSqxx.class);
                        sqxxExample.createCriteria().andEqualTo("glsxid", chxmid).andEqualTo("blsx", "4");
                        List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                        if (null != sqxxList && CollectionUtils.isNotEmpty(sqxxList)) {
                            DchyXmglSqxx sqxx = sqxxList.get(0);
                            if (null != sqxx) {
                                /*通过sqxxid获取待办列表数据*/
                                String sqxxid = sqxx.getSqxxid();
                                if (StringUtils.isNotBlank(sqxxid)) {
                                    /*将待办任务转为已办任务*/
                                    int shth = this.hy(sqxxid, hyyj);
                                    /*用户消息提醒*/
                                    this.messageReminder(chxmid, wtzt);
                                    if (shth > 0) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("lxr", lxr);
                                        map.put("lxdh", lxdh);
                                        String mlkid = this.getMlkByUserId();
                                        if (StringUtils.isNotBlank(mlkid)) {
                                            Example czrzExample = new Example(DchyXmglCzrz.class);
                                            czrzExample.createCriteria().andEqualTo("glsxid", mlkid);
                                            List<DchyXmglCzrz> czrzList = entityMapper.selectByExample(czrzExample);
                                            if (CollectionUtils.isNotEmpty(czrzList)) {
                                                DchyXmglCzrz xmglCzrz = czrzList.get(0);
                                                xmglCzrz.setCzcs(JSONObject.toJSONString(map));
                                                entityMapper.saveOrUpdate(xmglCzrz, xmglCzrz.getCzrzid());
                                            } else {
                                                DchyXmglCzrz xmglCzrz = new DchyXmglCzrz();
                                                xmglCzrz.setCzrzid(UUIDGenerator.generate18());
                                                xmglCzrz.setGlsxid(mlkid);
                                                xmglCzrz.setCzsj(new Date());
                                                xmglCzrz.setCzr(UserUtil.getCurrentUserId());
                                                xmglCzrz.setCzlx("");//操作类型
                                                xmglCzrz.setCzlxmc("");//操作类型名称
                                                xmglCzrz.setCzrid(UserUtil.getCurrentUserId());//操作人id
                                                xmglCzrz.setSsmkid(SsmkidEnum.CHDWHY.getCode());//所属模块id
                                                xmglCzrz.setCzcs(JSONObject.toJSONString(map));//操作参数
                                                entityMapper.saveOrUpdate(xmglCzrz, xmglCzrz.getCzrzid());
                                            }
                                        }
                                    }
                                    return shth;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return 0;
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

    @Override
    public Map<String, Object> beforeVerificationInfo() {
        Map<String, Object> map = new HashMap<>();
        try {
            String mlkid = this.getMlkByUserId();
            if (StringUtils.isNotBlank(mlkid)) {
                Example czrzExample = new Example(DchyXmglCzrz.class);
                czrzExample.createCriteria().andEqualTo("glsxid", mlkid);
                List<DchyXmglCzrz> czrzList = entityMapper.selectByExample(czrzExample);
                if (CollectionUtils.isNotEmpty(czrzList)) {
                    DchyXmglCzrz xmglCzrz = czrzList.get(0);
                    if (null != xmglCzrz) {
                        String czcs = xmglCzrz.getCzcs();
                        if (StringUtils.isNotBlank(czcs)) {
                            JSONObject object = JSONObject.parseObject(czcs);
                            if (null != object) {
                                map.put("lxr", object.get("lxr"));
                                map.put("lxdh", object.get("lxdh"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return map;
    }

    /**
     * 核验，将待办任务转为已办任务
     *
     * @param sqxxid 申请信息id
     * @param hyyj   核验意见
     */
    private int hy(String sqxxid, String hyyj) {
        Example dbrwExample = new Example(DchyXmglDbrw.class);
        /*通过sqxxid获取待办任务信息*/
        dbrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
        try {
            List<DchyXmglDbrw> dbrwList = entityMapper.selectByExample(dbrwExample);
            if (CollectionUtils.isNotEmpty(dbrwList)) {
                DchyXmglDbrw xmglDbrw = dbrwList.get(0);
                if (null != xmglDbrw) {
                    /*新生成一条已办数据*/
                    DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                    dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());//办理人员id
                    dchyXmglYbrw.setBlyj(hyyj);//办理意见  --> 核验意见
                    dchyXmglYbrw.setJssj(new Date());//结束时间
                    dchyXmglYbrw.setSqxxid(xmglDbrw.getSqxxid());//申请信息id
                    dchyXmglYbrw.setBljg(Constants.VALID);//办理结果
                    dchyXmglYbrw.setDqjd(xmglDbrw.getDqjd());//当前节点
                    dchyXmglYbrw.setZrsj(xmglDbrw.getZrsj());//转入时间
                    dchyXmglYbrw.setYbrwid(xmglDbrw.getDbrwid());//已办任务id
                    /*保存已办任务*/
                    entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                    //删除待办任务
                    entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, xmglDbrw.getDbrwid());
                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmglDbrw.getSqxxid());
                    /*修改对应申请信息状态*/
                    if (null != dchyXmglSqxx) {
                        /*98: 审核通过*/
                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_SHTG);
                        return entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return 0;
    }


    /**
     * 在线项目进度查看
     *
     * @param data
     * @return
     */
    @Override
    public List<Map<String, Object>> onlineRecordProcess(Map<String, Object> data) {
        String chxmidXsbf = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> resultList = new LinkedList<>();
            if (StringUtils.isNotBlank(chxmidXsbf)) {
                DchyXmglChxm xmglChxmXsbf = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmidXsbf);
                if (null != xmglChxmXsbf) {
                    Map<String, Object> resultMap = new HashMap<>();
                    String xqfbbh = xmglChxmXsbf.getXqfbbh();//需求发布编号
                    if (StringUtils.isNotBlank(xqfbbh)) {
                        Example chxmExample = new Example(DchyXmglChxm.class);
                        chxmExample.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                        List<DchyXmglChxm> chxmList = entityMapper.selectByExample(chxmExample);
                        if (CollectionUtils.isNotEmpty(chxmList)) {
                            DchyXmglChxm xmglChxm = chxmList.get(0);
                            if (null != xmglChxm) {
                                String chxmid = xmglChxm.getChxmid();
                                Date fbsj = xmglChxm.getFbsj();//委托时间
                                Date hysj = null;//核验时间
                                /*从已办数据中获取接受或拒绝委托时间*/
                                Example sqxxExample = new Example(DchyXmglSqxx.class);
                                sqxxExample.createCriteria().andEqualTo("glsxid", chxmid).andEqualTo("blsx", "4");
                                List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                                if (CollectionUtils.isNotEmpty(sqxxList)) {
                                    DchyXmglSqxx sqxx = sqxxList.get(0);
                                    String sqxxid = sqxx.getSqxxid();
                                    /*通过sqxxid获取已办任务里的核验时间*/
                                    if (StringUtils.isNotBlank(sqxxid)) {
                                        Example ybrwExample = new Example(DchyXmglYbrw.class);
                                        ybrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                                        List<DchyXmglYbrw> ybrwList = entityMapper.selectByExample(ybrwExample);
                                        if (CollectionUtils.isNotEmpty(ybrwList)) {
                                            DchyXmglYbrw ybrw = ybrwList.get(0);
                                            if (null != ybrw) {
                                                hysj = ybrw.getJssj();//核验时间
                                            }
                                        }
                                    }
                                }
                                Map<String, Object> basj = new HashMap<>();
                                basj.put("委托时间", sdf.format(fbsj));
                                if (null != hysj) {
                                    basj.put("核验时间", sdf.format(hysj));
                                }
                                List<Map<String, Object>> dataList = new ArrayList<>();
                                dataList.add(basj);
                                resultMap.put("name", "在线委托");
                                resultMap.put("details", dataList);
                            }
                        }
                    } else {
                        Map<String, Object> basj = new HashMap<>();
                        List<Map<String, Object>> dataList = new ArrayList<>();
                        dataList.add(basj);
                        resultMap.put("name", "在线委托");
                        resultMap.put("details", dataList);
                    }
                    /*项目备案*/
                    DchyXmglChxm chxmSxbf = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmidXsbf);
                    Map<String, Object> resultMap2 = new HashMap<>();
                    resultMap2.put("name", "项目备案");
                    if (null != chxmSxbf) {
                        //String wtzt = chxmSxbf.getWtzt();//5:已备案
                        String xmzt = chxmSxbf.getXmzt();//2:已备案 1:待备案 99:已办结
                        if (StringUtils.isNotBlank(xmzt) && !StringUtils.equals("1", xmzt)) {
                            Date slsj = chxmSxbf.getSlsj();//备案时间
                            if (null != slsj) {
                                Map<String, Object> xmbaSj = new HashMap<>();
                                xmbaSj.put("备案时间", sdf.format(slsj));
                                List<Map<String, Object>> dataList2 = new ArrayList<>();
                                dataList2.add(xmbaSj);
                                resultMap2.put("details", dataList2);
                            } else {
                                resultMap2.put("details", new ArrayList<>());
                            }
                        }
                    }
                    /*成果提交*/
                    Map<String, Object> resultMap4 = new HashMap<>();
                    resultMap4.put("name", "成果提交");
                    List<Map<String, Object>> list = onlineDelegationMapper.getLatelyCgTjjlByChxmid(chxmidXsbf);
                    List<Map<String, Object>> dataList4 = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(list)) {
                        List<String> sqxxidList = new ArrayList<>();
                        list.forEach(cgtj -> {
                            String sqxxid = (String) cgtj.get("SQXXID");
                            if (StringUtils.isNotBlank(sqxxid)) {
                                if (!sqxxidList.contains(sqxxid)) {
                                    sqxxidList.add(sqxxid);
                                    Map<String, Object> clcgSj = new HashMap<>();
                                    Date tjsj = (Date) cgtj.get("TJSJ");
                                    if (null != tjsj) {
                                        clcgSj.put("提交时间", sdf.format(tjsj));
                                        dataList4.add(clcgSj);
                                    }
                                }
                            }
                        });
                        resultMap4.put("details", dataList4);
                    } else {
                        resultMap4.put("details", new ArrayList<>());
                    }

                    /*项目办结*/
                    Map<String, Object> resultMap3 = new HashMap<>();
                    resultMap3.put("name", "项目办结");
                    if (null != chxmSxbf) {
                        String xmzt = chxmSxbf.getXmzt();//已办结:99
                        if (StringUtils.isNotBlank(xmzt) && StringUtils.equals(xmzt, "99")) {
                            Date bjsj = chxmSxbf.getBjsj();//办结时间
                            if (null != bjsj) {
                                Map<String, Object> xmbjSj = new HashMap<>();
                                xmbjSj.put("办结时间", sdf.format(bjsj));
                                List<Map<String, Object>> dataList3 = new ArrayList<>();
                                dataList3.add(xmbjSj);
                                resultMap3.put("details", dataList3);
                            } else {
                                resultMap3.put("details", new ArrayList<>());
                            }
                        }
                    }
                    resultList.add(resultMap);
                    resultList.add(resultMap2);
                    resultList.add(resultMap4);
                    resultList.add(resultMap3);
                    return resultList;
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return new ArrayList<>();
    }

    /**
     * 用户消息提醒
     *
     * @param chxmid
     */
    private void messageReminder(String chxmid, String shth) {
        String chdwmc = "";
        if (StringUtils.isNotBlank(chxmid)) {
            Example dwxxExample = new Example(DchyXmglChxmChdwxx.class);
            dwxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmChdwxx> chdwxxList = entityMapper.selectByExample(dwxxExample);
            if (CollectionUtils.isNotEmpty(chdwxxList)) {
                DchyXmglChxmChdwxx chdwxx = chdwxxList.get(0);
                if (null != chdwxx) {
                    chdwmc = chdwxx.getChdwmc();
                }
            }
        }
        if (StringUtils.equals(shth, "3")) {//核验通过
            /*通知----建设单位核验通过*/
            DchyXmglYhxx jsdwYhxx = new DchyXmglYhxx();
            jsdwYhxx.setYhxxid(UUIDGenerator.generate18());
            String wtdw = this.getWtdwByChxmid(chxmid);
            jsdwYhxx.setJsyhid(wtdw);//接受用户id --- 委托单位
            jsdwYhxx.setGlsxid(chxmid);
            DchyXmglYhxxPz yhxxPz2 = this.getYhxxPzByXxlx(Constants.DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_TG);//委托核验通过(建设单位)
            if (null != yhxxPz2) {
                String xxnr2 = yhxxPz2.getXxnr();
                String gcmc = getGcmcByChxmid(chxmid);
                xxnr2 = xxnr2.replaceAll("建设单位名称", CommonUtil.ternaryOperator(wtdw, StringUtils.EMPTY)).replaceAll("测绘单位名称", CommonUtil.ternaryOperator(chdwmc, StringUtils.EMPTY)).replaceAll("项目名称", CommonUtil.ternaryOperator(gcmc, StringUtils.EMPTY));
                jsdwYhxx.setXxnr(xxnr2);
                jsdwYhxx.setFsyhid(UserUtil.getCurrentUserId());
                jsdwYhxx.setDqzt(Constants.INVALID);//未读
                jsdwYhxx.setFssj(CalendarUtil.getCurHMSDate());
                jsdwYhxx.setDqsj(null);
                jsdwYhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_TG);//委托核验通过(建设单位)
                jsdwYhxx.setSftz(yhxxPz2.getSftz());
                entityMapper.saveOrUpdate(jsdwYhxx, jsdwYhxx.getYhxxid());
            }
        } else {//核验不通过
            /*通知----建设单位核验通过*/
            DchyXmglYhxx jsdwYhxx = new DchyXmglYhxx();
            jsdwYhxx.setYhxxid(UUIDGenerator.generate18());
            String wtdw = this.getWtdwByChxmid(chxmid);
            jsdwYhxx.setJsyhid(wtdw);//接受用户id --- 委托单位
            jsdwYhxx.setGlsxid(chxmid);
            DchyXmglYhxxPz yhxxPz2 = this.getYhxxPzByXxlx(Constants.DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_BTG);//委托核验不通过(建设单位)
            if (null != yhxxPz2) {
                String xxnr2 = yhxxPz2.getXxnr();
                String gcmc = getGcmcByChxmid(chxmid);
                xxnr2 = xxnr2.replaceAll("建设单位名称", CommonUtil.ternaryOperator(wtdw, StringUtils.EMPTY)).replaceAll("测绘单位名称", CommonUtil.ternaryOperator(chdwmc, StringUtils.EMPTY)).replaceAll("项目名称", CommonUtil.ternaryOperator(gcmc, StringUtils.EMPTY));
                jsdwYhxx.setXxnr(xxnr2);
                jsdwYhxx.setFsyhid(UserUtil.getCurrentUserId());
                jsdwYhxx.setDqzt(Constants.INVALID);//未读
                jsdwYhxx.setFssj(CalendarUtil.getCurHMSDate());
                jsdwYhxx.setDqsj(null);
                jsdwYhxx.setXxlx(Constants.DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_BTG);//委托核验不通过(建设单位)
                jsdwYhxx.setSftz(yhxxPz2.getSftz());
                entityMapper.saveOrUpdate(jsdwYhxx, jsdwYhxx.getYhxxid());
            }
        }
    }


    /**
     * 根据测绘项目id获取到委托单位信息
     *
     * @param chxmid
     * @return
     */
    private String getWtdwByChxmid(String chxmid) {
        String wtdw = "";
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != chxm) {
                String chgcid = chxm.getChgcid();
                if (StringUtils.isNotBlank(chgcid)) {
                    DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                    if (null != dchyXmglChgc) {
                        wtdw = dchyXmglChgc.getWtdw();
                        return wtdw;
                    }
                }
            }
        }
        return wtdw;
    }

    /**
     * 根据测绘项目id获取到工程名称
     *
     * @param chxmid
     * @return
     */
    private String getGcmcByChxmid(String chxmid) {
        String gcmc = "";
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != chxm) {
                String chgcid = chxm.getChgcid();
                if (StringUtils.isNotBlank(chgcid)) {
                    DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                    if (null != dchyXmglChgc) {
                        gcmc = dchyXmglChgc.getGcmc();
                        return gcmc;
                    }
                }
            }
        }
        return gcmc;
    }

    private DchyXmglYhxxPz getYhxxPzByXxlx(String xxlx) {
        Example yhxxPzExample = new Example(DchyXmglYhxxPz.class);
        yhxxPzExample.createCriteria().andEqualTo("xxlx", xxlx);
        List<DchyXmglYhxxPz> yhxxPzList = entityMapper.selectByExample(yhxxPzExample);
        if (CollectionUtils.isNotEmpty(yhxxPzList)) {
            DchyXmglYhxxPz yhxxPz = yhxxPzList.get(0);
            if (null != yhxxPz) {
                return yhxxPz;
            }
        }
        return null;
    }


    /**
     * 根据chxmid修改核验状态
     *
     * @param data
     * @return
     */
    @Override
    public int alterHtzyByChxmid(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                if (null != xmglChxm) {
                    xmglChxm.setIshy("1");//1：已核验  0:未核验
                    return entityMapper.saveOrUpdate(xmglChxm, xmglChxm.getChxmid());
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return 0;
    }

    /**
     * 合同删除
     *
     * @param data
     * @return
     */
    @Override
    @Transactional
    public int delHtwjAndSjxx(Map<String, Object> data) {
        List<Object> htxxArray = (List<Object>) data.get("htxxidList");
        try {
            if (CollectionUtils.isNotEmpty(htxxArray)) {
                for (Object htxxid : htxxArray) {
                    /*删除htxx表里的wjzxid*/
                    DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, (String) htxxid);
                    if (null != xmglHtxx) {
                        /*删除文件中心的文件*/
                        if (StringUtils.isNotBlank(xmglHtxx.getWjzxid())) {
                            List<Node> nodeList = platformUtil.getChildNodeListByParentId(Integer.parseInt(xmglHtxx.getWjzxid()));
                            if (CollectionUtils.isNotEmpty(nodeList)) {
                                for (Node node : nodeList) {
                                    platformUtil.deleteNodeById(node.getId());
                                }
                            }
                        }
                        /*wjzxid置null*/
                        onlineDelegationMapper.updateHtxxById(xmglHtxx.getHtxxid());
                    }
                }
                Example sjxxExample = new Example(DchyXmglSjxx.class);
                sjxxExample.createCriteria().andIn("glsxid", htxxArray);
                List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
                List<Object> sjxxidList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(sjxxList)) {
                    sjxxList.forEach(sjxx -> {
                        sjxxidList.add(sjxx.getSjxxid());
                    });
                }
                if (CollectionUtils.isNotEmpty(sjxxidList)) {
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        for (DchyXmglSjcl sjcl : sjclList) {
                            /*sjcl中的wjzxid置null*/
                            onlineDelegationMapper.updateSjclById(sjcl.getSjclid());
                        }
                    }
                }
                return 1;
            }
        } catch (Exception e) {
            logger.error("在线备案合同取消错误原因：{}", e);
        }
        return 0;
    }


    /**
     * 合同变更同步至线下
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int contractChangeSyn(Map<String, Object> data) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        try {
            List<String> htxxids = (List<String>) data.get("htxxid");
            String ssmkid = (String) data.get("ssmkid");
            if (CollectionUtils.isNotEmpty(htxxids)) {
                List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
                for (String htxxid : htxxids) {
                    DchyXmglHtxxDto xmglHtxxDto = new DchyXmglHtxxDto();
                    /*htxx*/
                    DchyXmglHtxx xmglHtxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
                    Optional<DchyXmglHtxx> htxxOptional = Optional.ofNullable(xmglHtxx);
                    List<Object> htxxidList = new ArrayList<>();
                    List<DchyXmglHtxx> htxxList = new ArrayList<>();
                    if (htxxOptional.isPresent()) {
                        htxxidList.add(xmglHtxx.getHtxxid());
                        htxxList.add(xmglHtxx);
                        xmglHtxxDto.setDchyXmglHtxxList(htxxList);
                    }
                    /*sjxx*/
                    if (CollectionUtils.isNotEmpty(htxxidList)) {
                        Example sjxxExample = new Example(DchyXmglSjxx.class);
                        sjxxExample.createCriteria().andIn("glsxid", htxxidList);
                        List<DchyXmglSjxx> sjxxList = entityMapperXSBF.selectByExample(sjxxExample);
                        List<Object> sjxxidList = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(sjxxList)) {
                            sjxxList.forEach(sjxx -> {
                                sjxxidList.add(sjxx.getSjxxid());
                            });
                            xmglHtxxDto.setDchyXmglSjxxList(sjxxList);
                        }
                        /*sjcl*/
                        if (CollectionUtils.isNotEmpty(sjxxidList)) {
                            Example sjclExample = new Example(DchyXmglSjcl.class);
                            sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                            List<DchyXmglSjcl> sjclList = entityMapperXSBF.selectByExample(sjclExample);
                            if (CollectionUtils.isNotEmpty(sjclList)) {
                                xmglHtxxDto.setDchyXmglSjclList(sjclList);

                                List<Map<String, List>> htFileList = new LinkedList<>();
                                List<Map<String, String>> htFile;
                                Map<String, List> listMap = new HashMap<>();
                                for (DchyXmglSjcl xmglSjcl : sjclList) {
                                    String wjzxid = xmglSjcl.getWjzxid();
                                    if (StringUtils.isNotBlank(wjzxid)) {
                                        /*生成文件(委托，核验)*/
                                        htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                        if (htFile != null) {
                                            listMap.put(xmglSjcl.getSjclid(), htFile);
                                            htFileList.add(listMap);
                                        }
                                    }
                                }
                                /*设置上传合同文件*/
                                xmglHtxxDto.setHtFileList(htFileList);
                            }
                        }
                    }
                    htxxDtos.add(xmglHtxxDto);
                    /*记录合同变更日志*/
                    this.saveHtbgCzrz(htxxid, ssmkid, data);
                }
                dchyXmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
                /*同步合同数据至线下*/
                /*统一推送基础数据*/
                pushDataToMqService.pushHtbgMsgToMq(dchyXmglChxmDto);
                return 1;
            }
        } catch (Exception e) {
            logger.error("合同变更错误原因：{}", e);
        }
        return 0;
    }


    /**
     * 记录合同变更操作日志
     */
    @Transactional
    public void saveHtbgCzrz(String htxxid, String ssmkid, Map<String, Object> data) {
        try {
            /*生成新的记录日志*/
            DchyXmglCzrz xmglCzrz = new DchyXmglCzrz();
            xmglCzrz.setCzrzid(UUIDGenerator.generate18());
            xmglCzrz.setCzsj(new Date());
            xmglCzrz.setCzr(UserUtil.getCurrentUser().getUsername());
            xmglCzrz.setCzlx("");
            xmglCzrz.setCzrid(UserUtil.getCurrentUserId());
            xmglCzrz.setSsmkid(ssmkid);
            xmglCzrz.setCzcs(data.toString());
            xmglCzrz.setCzlxmc("合同变更");
            xmglCzrz.setGlsxid(htxxid);
            entityMapper.saveOrUpdate(xmglCzrz, xmglCzrz.getCzrzid());
        } catch (Exception e) {
            logger.error("合同变更记录日志错误：{}", e);
        }
    }


    /**
     * 项目在线备案
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int keepOnRecord(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        String htwjzxid = CommonUtil.ternaryOperator(data.get("wjzxid"));//合同文件中心id
        String chdwxxid = CommonUtil.ternaryOperator(data.get("chdwxxid"));
        try {
            /*修改项目状态为已备案*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != xmglChxm) {
                /*获取配置项*/
                String flag = AppConfig.getProperty(Constants.DCHY_XMGL_ZDBA_PZ);
                /*是否是自动备案*/
                if (StringUtils.isNotBlank(flag) && StringUtils.equals("true", flag)) {

                    xmglChxm.setWtzt(Constants.WTZT_YBA);//6：已备案
                    entityMapper.saveOrUpdate(xmglChxm, xmglChxm.getChxmid());

                    if (htwjzxid.indexOf(",") != -1) {//多个合同
                        /*同步基础数据*/
                        this.pushXsBaseDataToXx(htwjzxid, chxmid, chdwxxid);
                        /*同步文件*/
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                /*组织核验与委托文件*/
                                pushXsFileDataToXx(chxmid);
                            }
                        };
                        thread.start();
                        Thread.sleep(2000);

                    } else {//单个合同
                        /*推送数据*/
                        pushXsDataToXx(chxmid, htwjzxid, chdwxxid);

                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                /*推送文件*/
                                pushFileDataToXx(chxmid, htwjzxid);
                            }
                        };
                        thread.start();
                    }
                } else {
                    xmglChxm.setWtzt(Constants.WTZT_DBA);//6：待备案
                    entityMapper.saveOrUpdate(xmglChxm, xmglChxm.getChxmid());
                    /*组织线上备案涉及的所有数据及文件推送至线下*/
                    this.afterBaPushXsDataToXx(chxmid, htwjzxid, chdwxxid);//53LK2108ELWOQ30T
                }
                return 1;
            }

        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return 0;
    }

    private void pushXsBaseDataToXx(String htwjzxid, String chxmid, String chdwxxid) {
        String[] htwjs = htwjzxid.split(",");
        DchyXmglChxmDto xmglChxmDto = this.generateChxmDtoToXx(chxmid, chdwxxid);
        List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
        /*组织合同数据*/
        /*生成合同信息dto*/
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        if (CollectionUtils.isNotEmpty(htxxList)) {
            for (int i = 0; i < htxxList.size(); i++) {
                DchyXmglHtxxDto dchyXmglHtxxDto = this.generateHtxxDtoxToXx(chxmid, htwjs[i], htxxList.get(i).getHtxxid());
                htxxDtos.add(dchyXmglHtxxDto);
            }
            xmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送基础数据*/
            pushDataToMqService.pushSlxxMsgToMq(xmglChxmDto);
        }
    }

    private void pushXsFileDataToXx(String chxmid) {
        /*组织委托核验材料*/
        DchyXmglChxmDto xmglChxmDto = this.generateWtAndHyFileToXx(chxmid);
        List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
        /*生成合同信息dto*/
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        if (CollectionUtils.isNotEmpty(htxxList)) {
            /*组织合同信息*/
            for (int i = 0; i < htxxList.size(); i++) {
                DchyXmglHtxxDto xmglHtxxDto = this.generateHtFileToXx(chxmid, htxxList.get(i).getHtxxid());
                htxxDtos.add(xmglHtxxDto);
            }
            xmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送*/
            pushDataToMqService.pushOnlineWjFileToxx(xmglChxmDto);
        }
    }

    private DchyXmglHtxxDto generateHtFileToXx(String chxmid, String htxxid) {
        /*生成合同信息dto*/
        String glsxid = htxxid;
        DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
        DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
        List<DchyXmglHtxx> htxxList = new ArrayList<>();
        if (null != xmglHtxx) {
            htxxList.add(xmglHtxx);
        }
        if (CollectionUtils.isNotEmpty(htxxList)) {
            dchyXmglHtxxDto.setDchyXmglHtxxList(htxxList);
        }
        if (StringUtils.isNotBlank(glsxid)) {
            /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
            Example htSjxxExample = new Example(DchyXmglSjxx.class);
            htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
            if (CollectionUtils.isNotEmpty(htSjxxList)) {
                /*设置收件信息*/
                dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                List<Object> sjxxs = new ArrayList<>();
                for (DchyXmglSjxx sjxx : htSjxxList) {
                    sjxxs.add(sjxx.getSjxxid());
                }
                Example htSjclExample = new Example(DchyXmglSjcl.class);
                htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                /*设置收件材料*/
                dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
                List<Map<String, List>> htFileList = new LinkedList<>();
                List<Map<String, String>> htFile;
                Map<String, List> listMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(htSjclList)) {
                    for (DchyXmglSjcl xmglSjcl : htSjclList) {
                        String wjzxid = xmglSjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            if (htFile != null) {
                                listMap.put(xmglSjcl.getSjclid(), htFile);
                                htFileList.add(listMap);
                            }
                        }
                    }
                }
                /*设置上传合同文件*/
                dchyXmglHtxxDto.setHtFileList(htFileList);

                /*合同测量事项关系*/
                List<Map<String, String>> htClsxList = new LinkedList<>();
                Example exampleClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
                exampleClsxHtxxGx.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(exampleClsxHtxxGx);
                if (CollectionUtils.isNotEmpty(dchyXmglClsxHtxxGxList)) {
                    for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : dchyXmglClsxHtxxGxList) {
                        String clsxid = dchyXmglClsxHtxxGx.getClsxid();
                        if (StringUtils.isNotEmpty(clsxid)) {
                            DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                            if (dchyXmglChxmClsx != null) {
                                String clsx = dchyXmglChxmClsx.getClsx();
                                if (StringUtils.isNotEmpty(clsx)) {
                                    Map<String, String> map1 = Maps.newHashMap();
                                    map1.put(htxxid, clsx);
                                    htClsxList.add(map1);
                                }
                            }
                        }
                    }
                }
                dchyXmglHtxxDto.setHtClsxList(htClsxList);
            }
        }
        return dchyXmglHtxxDto;
    }


    private DchyXmglHtxxDto generateHtxxDtoxToXx(String chxmid, String htwjzxid, String htxxid) {
        /*生成合同信息dto*/
        String glsxid = "";
        Map<String, Object> map = new HashMap<>();
        map.put("chxmid", chxmid);
        map.put("htxxid", htxxid);
        map.put("folderId", htwjzxid);//合同文件中心id
        glsxid = htxxid;
        DchyXmglHtxxDto dchyXmglHtxxDto = dchyXmglHtService.generateHtxxDto(map);
        if (StringUtils.isNotBlank(glsxid)) {
            /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
            Example htSjxxExample = new Example(DchyXmglSjxx.class);
            htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
            if (CollectionUtils.isNotEmpty(htSjxxList)) {
                /*设置收件信息*/
                dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                List<Object> sjxxs = new ArrayList<>();
                for (DchyXmglSjxx sjxx : htSjxxList) {
                    sjxxs.add(sjxx.getSjxxid());
                }
                Example htSjclExample = new Example(DchyXmglSjcl.class);
                htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                /*设置收件材料*/
                dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
            }
        }
        return dchyXmglHtxxDto;
    }

    private DchyXmglChxmDto generateChxmDtoToXx(String chxmid, String chdwxxid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        if (StringUtils.isNotBlank(chxmid)) {
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                /*设置测绘工程*/
                DchyXmglChgc chgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, xmglChxm.getChgcid());
                if (null != chgc) {
                    dchyXmglChxmDto.setDchyXmglChgc(chgc);
                }
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*设置测绘单位信息*/
            DchyXmglChxmChdwxx chdwxx = entityMapper.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
            if (null != chdwxx) {
                List<DchyXmglChxmChdwxx> list3 = new ArrayList<>();
                list3.add(chdwxx);
                dchyXmglChxmDto.setDchyXmglChxmChdwxxList(list3);
            }
            /*设置测量事项*/
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            dchyXmglChxmDto.setDchyXmglChxmClsxList(clsxList);
            /*设置测量事项测绘体量*/
            Example chtlExample = new Example(DchyXmglClsxChtl.class);
            chtlExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxChtl> chtlList = entityMapper.selectByExample(chtlExample);
            dchyXmglChxmDto.setDchyXmglClsxChtlList(chtlList);
            /*设置申请信息*/
            Example exampleSqxx = new Example(DchyXmglSqxx.class);
            exampleSqxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(exampleSqxx);
            if (null != dchyXmglSqxxList) {
                dchyXmglChxmDto.setDchyXmglSqxxList(dchyXmglSqxxList);
            }
            /*clsx_chdwxx_gx*/
            Example clsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
            clsxChdwxxGx.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxChdwxxGx> clsxChdwxxGxList = entityMapper.selectByExample(clsxChdwxxGx);
            if (CollectionUtils.isNotEmpty(clsxChdwxxGxList)) {
                dchyXmglChxmDto.setDchyXmglClsxChdwxxGxList(clsxChdwxxGxList);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<DchyXmglSjcl> wtAndHySjclList = new ArrayList<>();
                for (DchyXmglSjxx xmglSjxx : sjxxList) {
                    String sjxxid = xmglSjxx.getSjxxid();
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andEqualTo("sjxxid", sjxxid).andIsNotNull("wjzxid");
                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        /*委托、核验材料*/
                        wtAndHySjclList.addAll(sjclList);
                    }
                }
                dchyXmglChxmDto.setDchyXmglSjclList(wtAndHySjclList);
            }
        }
        return dchyXmglChxmDto;
    }


    private DchyXmglChxmDto generateWtAndHyFileToXx(String chxmid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        if (StringUtils.isNotBlank(chxmid)) {
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<Object> sjxxidList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxidList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    /*委托、核验材料*/
                    List<Map<String, List>> fileList = new ArrayList<>();
                    List<Map<String, String>> file;
                    /*收件材料对应测量事项，用于线下备案文件拆分*/
                    List<Map<String, String>> sjclClsxList = new ArrayList<>();
                    dchyXmglChxmDto.setDchyXmglSjclList(sjclList);
                    Map<String, List> listMap = new HashMap<>();
                    for (DchyXmglSjcl sjcl : sjclList) {
                        String wjzxid = sjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            listMap.put(sjcl.getSjclid(), file);
                            /*生成文件(委托，核验)收件材料对应测量事项*/
                            if (StringUtils.isNotEmpty(sjcl.getClsx()) && StringUtils.isNotEmpty(sjcl.getSjclid())) {
                                Map<String, String> sjclClsxMap = new HashMap<>();
                                sjclClsxMap.put(sjcl.getSjclid(), sjcl.getClsx());
                                sjclClsxList.add(sjclClsxMap);

                                /*List<String> clsxList = Lists.newArrayList();
                                String clsx = sjcl.getClsx();boolean status = clsx.contains(",");
                                if (status) {
                                    clsxList = Arrays.asList(clsx.split(","));//根据分号分隔转化为list
                                    for (String ssclsx : clsxList) {
                                        Map<String, String> sjclClsxMap = new HashMap<>();
                                        sjclClsxMap.put(sjcl.getSjclid(), ssclsx);
                                        sjclClsxList.add(sjclClsxMap);
                                    }
                                } else {
                                    Map<String, String> sjclClsxMap = new HashMap<>();
                                    sjclClsxMap.put( sjcl.getSjclid(),sjcl.getClsx());
                                    sjclClsxList.add(sjclClsxMap);
                                }*/
                            }
                        }
                    }
                    fileList.add(listMap);
                    dchyXmglChxmDto.setFileList(fileList);
                    dchyXmglChxmDto.setSjclClsxList(sjclClsxList);
                }
            }
        }
        return dchyXmglChxmDto;
    }

    /**
     * 推送文件信息(自动备案)
     *
     * @param chxmid
     * @param htwjzxid
     */
    private void pushFileDataToXx(String chxmid, String htwjzxid) {
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<Object> sjxxidList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxidList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    List<Map<String, List>> fileList = new ArrayList<>();
                    List<Map<String, String>> sjclClsxList = new ArrayList<>();
                    List<Map<String, String>> file;
                    /*委托、核验材料*/
                    dchyXmglChxmDto.setDchyXmglSjclList(sjclList);
                    Map<String, List> listMap = new HashMap<>();
                    for (DchyXmglSjcl sjcl : sjclList) {
                        String wjzxid = sjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            listMap.put(sjcl.getSjclid(), file);
                            /*生成文件(委托，核验)收件材料对应测量事项*/
                            if (StringUtils.isNotEmpty(sjcl.getClsx()) && StringUtils.isNotEmpty(sjcl.getSjclid())) {
                                Map<String, String> sjclClsxMap = new HashMap<>();
                                sjclClsxMap.put(sjcl.getSjclid(), sjcl.getClsx());
                                sjclClsxList.add(sjclClsxMap);
                            }
                        }
                    }
                    fileList.add(listMap);
                    dchyXmglChxmDto.setFileList(fileList);
                    dchyXmglChxmDto.setSjclClsxList(sjclClsxList);
                }
            }
            /*组织备案合同时的数据文件*/
            /*生成合同信息dto*/
            String glsxid = "";
            Map<String, Object> map = new HashMap<>();
            map.put("chxmid", chxmid);
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx htxx = htxxList.get(0);
                map.put("htxxid", htxx.getHtxxid());
                glsxid = htxx.getHtxxid();
            }
            map.put("folderId", htwjzxid);//合同文件中心id
            DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
            dchyXmglHtxxDto.setDchyXmglHtxxList(htxxList);
            if (StringUtils.isNotBlank(glsxid)) {
                /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
                Example htSjxxExample = new Example(DchyXmglSjxx.class);
                htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
                List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                if (CollectionUtils.isNotEmpty(htSjxxList)) {
                    /*设置收件信息*/
                    dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                    List<Object> sjxxs = new ArrayList<>();
                    for (DchyXmglSjxx sjxx : htSjxxList) {
                        sjxxs.add(sjxx.getSjxxid());
                    }
                    Example htSjclExample = new Example(DchyXmglSjcl.class);
                    htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                    List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                    /*设置收件材料*/
                    dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
                    List<Map<String, List>> htFileList = new LinkedList<>();
                    List<Map<String, String>> htFile;
                    Map<String, List> listMap = new HashMap<>();
                    if (CollectionUtils.isNotEmpty(htSjclList)) {
                        for (DchyXmglSjcl xmglSjcl : htSjclList) {
                            String wjzxid = xmglSjcl.getWjzxid();
                            if (StringUtils.isNotBlank(wjzxid)) {
                                /*生成合同文件*/
                                htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                if (htFile != null) {
                                    listMap.put(xmglSjcl.getSjclid(), htFile);
                                    htFileList.add(listMap);
                                }
                            }
                        }
                    }
                    /*设置上传合同文件*/
                    dchyXmglHtxxDto.setHtFileList(htFileList);

                    /*合同测量事项关系*/
                    List<Map<String, String>> htClsxList = new LinkedList<>();
                    Example exampleClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
                    exampleClsxHtxxGx.createCriteria().andEqualTo("htxxid", glsxid);
                    List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(exampleClsxHtxxGx);
                    if (CollectionUtils.isNotEmpty(dchyXmglClsxHtxxGxList)) {
                        for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : dchyXmglClsxHtxxGxList) {
                            String clsxid = dchyXmglClsxHtxxGx.getClsxid();
                            if (StringUtils.isNotEmpty(clsxid)) {
                                DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                                if (dchyXmglChxmClsx != null) {
                                    String clsx = dchyXmglChxmClsx.getClsx();
                                    if (StringUtils.isNotEmpty(clsx)) {
                                        Map<String, String> map1 = Maps.newHashMap();
                                        map1.put(glsxid, clsx);
                                        htClsxList.add(map1);
                                    }
                                }
                            }
                        }
                    }
                    dchyXmglHtxxDto.setHtClsxList(htClsxList);
                }
            }
            List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
            htxxDtos.add(dchyXmglHtxxDto);
            dchyXmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送*/
            pushDataToMqService.pushOnlineWjFileToxx(dchyXmglChxmDto);
        }
    }

    /**
     * 推送线上数据到线下(自动备案)
     */
    private void pushXsDataToXx(String chxmid, String htwjzxid, String chdwxxid) {
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                /*设置测绘工程*/
                DchyXmglChgc chgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, xmglChxm.getChgcid());
                if (null != chgc) {
                    dchyXmglChxmDto.setDchyXmglChgc(chgc);
                }
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*设置测绘单位信息*/
            DchyXmglChxmChdwxx chdwxx = entityMapper.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
            if (null != chdwxx) {
                List<DchyXmglChxmChdwxx> list3 = new ArrayList<>();
                list3.add(chdwxx);
                dchyXmglChxmDto.setDchyXmglChxmChdwxxList(list3);
            }
            /*设置测量事项*/
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            dchyXmglChxmDto.setDchyXmglChxmClsxList(clsxList);
            /*设置测量事项测绘体量*/
            Example chtlExample = new Example(DchyXmglClsxChtl.class);
            chtlExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxChtl> chtlList = entityMapper.selectByExample(chtlExample);
            dchyXmglChxmDto.setDchyXmglClsxChtlList(chtlList);
            /*设置申请信息*/
            Example exampleSqxx = new Example(DchyXmglSqxx.class);
            exampleSqxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(exampleSqxx);
            if (null != dchyXmglSqxxList) {
                dchyXmglChxmDto.setDchyXmglSqxxList(dchyXmglSqxxList);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<DchyXmglSjcl> wtAndHySjclList = new ArrayList<>();
                for (DchyXmglSjxx xmglSjxx : sjxxList) {
                    String sjxxid = xmglSjxx.getSjxxid();
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andEqualTo("sjxxid", sjxxid).andIsNotNull("wjzxid");
                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        /*委托、核验材料*/
                        wtAndHySjclList.addAll(sjclList);
                    }
                }
                dchyXmglChxmDto.setDchyXmglSjclList(wtAndHySjclList);
            }
            /*组织备案合同时的数据文件*/
            /*生成合同信息dto*/
            String glsxid = "";
            Map<String, Object> map = new HashMap<>();
            map.put("chxmid", chxmid);
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx htxx = htxxList.get(0);
                map.put("htxxid", htxx.getHtxxid());
                glsxid = htxx.getHtxxid();
            }
            map.put("folderId", htwjzxid);//合同文件中心id
            DchyXmglHtxxDto dchyXmglHtxxDto = dchyXmglHtService.generateHtxxDto(map);
            if (StringUtils.isNotBlank(glsxid)) {
                /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
                Example htSjxxExample = new Example(DchyXmglSjxx.class);
                htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
                List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                if (CollectionUtils.isNotEmpty(htSjxxList)) {
                    /*设置收件信息*/
                    dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                    List<Object> sjxxs = new ArrayList<>();
                    for (DchyXmglSjxx sjxx : htSjxxList) {
                        sjxxs.add(sjxx.getSjxxid());
                    }
                    Example htSjclExample = new Example(DchyXmglSjcl.class);
                    htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                    List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                    /*设置收件材料*/
                    dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
                }
            }
            List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
            htxxDtos.add(dchyXmglHtxxDto);
            dchyXmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送*/
            pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
        }
    }

    /**
     * 备案后推送数据及文件到线下(手动备案)
     *
     * @param chxmid
     */
    private void afterBaPushXsDataToXx(String chxmid, String htwjzxid, String chdwxxid) {
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                /*设置测绘工程*/
                DchyXmglChgc chgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, xmglChxm.getChgcid());
                if (null != chgc) {
                    dchyXmglChxmDto.setDchyXmglChgc(chgc);

                }
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*设置测绘单位信息*/
            DchyXmglChxmChdwxx chdwxx = entityMapper.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
            if (null != chdwxx) {
                List<DchyXmglChxmChdwxx> list3 = new ArrayList<>();
                list3.add(chdwxx);
                dchyXmglChxmDto.setDchyXmglChxmChdwxxList(list3);
            }
            /*设置测量事项*/
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            dchyXmglChxmDto.setDchyXmglChxmClsxList(clsxList);
            /*设置测量事项测绘体量*/
            Example chtlExample = new Example(DchyXmglClsxChtl.class);
            chtlExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglClsxChtl> chtlList = entityMapper.selectByExample(chtlExample);
            dchyXmglChxmDto.setDchyXmglClsxChtlList(chtlList);
            /*设置申请信息*/
            Example exampleSqxx = new Example(DchyXmglSqxx.class);
            exampleSqxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(exampleSqxx);
            if (null != dchyXmglSqxxList) {
                dchyXmglChxmDto.setDchyXmglSqxxList(dchyXmglSqxxList);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<Object> sjxxidList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxidList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    List<Map<String, List>> fileList = new ArrayList<>();
                    List<Map<String, String>> file;
                    /*委托、核验材料*/
                    dchyXmglChxmDto.setDchyXmglSjclList(sjclList);
                    Map<String, List> listMap = new HashMap<>();
                    for (DchyXmglSjcl sjcl : sjclList) {
                        String wjzxid = sjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            listMap.put(sjcl.getSjclid(), file);
                        }
                    }
                    fileList.add(listMap);
                    dchyXmglChxmDto.setFileList(fileList);
                }
            }
            /*组织备案合同时的数据文件*/
            /*生成合同信息dto*/
            String glsxid = "";
            Map<String, Object> map = new HashMap<>();
            map.put("chxmid", chxmid);
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx htxx = htxxList.get(0);
                map.put("htxxid", htxx.getHtxxid());
                glsxid = htxx.getHtxxid();
            }
            map.put("folderId", htwjzxid);//合同文件中心id
            DchyXmglHtxxDto dchyXmglHtxxDto = dchyXmglHtService.generateHtxxDto(map);
            if (StringUtils.isNotBlank(glsxid)) {
                /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
                Example htSjxxExample = new Example(DchyXmglSjxx.class);
                htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
                List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                if (CollectionUtils.isNotEmpty(htSjxxList)) {
                    /*设置收件信息*/
                    dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                    List<Object> sjxxs = new ArrayList<>();
                    for (DchyXmglSjxx sjxx : htSjxxList) {
                        sjxxs.add(sjxx.getSjxxid());
                    }
                    Example htSjclExample = new Example(DchyXmglSjcl.class);
                    htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                    List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                    /*设置收件材料*/
                    dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
                    List<Map<String, String>> htFileList = new LinkedList<>();
                    List<Map<String, String>> htFile;
                    if (CollectionUtils.isNotEmpty(htSjclList)) {
                        for (DchyXmglSjcl xmglSjcl : htSjclList) {
                            String wjzxid = xmglSjcl.getWjzxid();
                            if (StringUtils.isNotBlank(wjzxid)) {
                                /*生成文件(委托，核验)*/
                                htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                htFileList.addAll(htFile);
                            }
                        }
                    }
                    /*设置上传合同文件*/
                    dchyXmglHtxxDto.setSjclList(htFileList);
                }
            }
            List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
            htxxDtos.add(dchyXmglHtxxDto);
            dchyXmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送*/
            pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
        }
    }


    private List<Map<String, String>> generateFileByWjzxid(int nodeId) {
        List<Map<String, String>> sjclList = new ArrayList<>();
        try {
            List<Node> nodeList = getNodeService().getChildNodes(nodeId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                /*单个文件*/
                if (nodeList.size() == 1) {
                    Node node = nodeList.get(0);
                    byte[] body = FileDownoadUtil.downloadWj(node.getId());
                    String fileName = node.getName();
                    String file = Base64.getEncoder().encodeToString(body);
                    Map<String, String> sjcl = new HashMap<>();
                    sjcl.put(fileName, file);
                    sjclList.add(sjcl);
                } else {
                    for (Node node : nodeList) {
                        byte[] body = FileDownoadUtil.downloadWj(node.getId());
                        String fileName = node.getName();
                        String file = Base64.getEncoder().encodeToString(body);
                        Map<String, String> sjcl = new HashMap<>();
                        sjcl.put(fileName, file);
                        sjclList.add(sjcl);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }
        return sjclList;
    }

    @Override
    public int getToBeAcceptedNum() {
        int num = 0;
        try {
            /*获取当前登录用户的mlkid*/
            String mlkid = this.getMlkByUserId();
            /*获取待接受的项目数量*/
            num = onlineDelegationMapper.getToBeAcceptedNum(mlkid);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return num;
    }

    /**
     * 从线上库获取核验意见
     *
     * @param data
     * @return
     */
    @Override
    public String getHyyjByChxmid(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                /*通过chxmid获取申请信息*/
                Example sqxxExample = new Example(DchyXmglSqxx.class);
                sqxxExample.createCriteria().andEqualTo("glsxid", chxmid).andEqualTo("blsx", "4");
                List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                if (CollectionUtils.isNotEmpty(sqxxList)) {
                    DchyXmglSqxx sqxx = sqxxList.get(0);
                    if (null != sqxx) {
                        /*通过sqxxid获取已办列表数据*/
                        String sqxxid = sqxx.getSqxxid();
                        if (StringUtils.isNotBlank(sqxxid)) {
                            /*通过sqxxid获取已办列表*/
                            Example ybrwExample = new Example(DchyXmglYbrw.class);
                            ybrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                            List<DchyXmglYbrw> ybrwList = entityMapper.selectByExample(ybrwExample);
                            if (CollectionUtils.isNotEmpty(ybrwList)) {
                                DchyXmglYbrw xmglYbrw = ybrwList.get(0);
                                if (null != xmglYbrw) {
                                    return xmglYbrw.getBlyj();//返回核验意见
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return "";
    }


    /**
     * 从线上备份库获取委托审核意见
     */
    @Override
    public String getAuditOpinion(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                /*通过chxmid获取申请信息*/
                Example sqxxExample = new Example(DchyXmglSqxx.class);
                sqxxExample.createCriteria().andEqualTo("glsxid", chxmid).andEqualTo("blsx", "4");
                List<DchyXmglSqxx> sqxxList = entityMapperXSBF.selectByExample(sqxxExample);
                if (CollectionUtils.isNotEmpty(sqxxList)) {
                    DchyXmglSqxx sqxx = sqxxList.get(0);
                    if (null != sqxx) {
                        /*通过sqxxid获取已办列表数据*/
                        String sqxxid = sqxx.getSqxxid();
                        if (StringUtils.isNotBlank(sqxxid)) {
                            /*通过sqxxid获取已办列表*/
                            Example ybrwExample = new Example(DchyXmglYbrw.class);
                            ybrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                            List<DchyXmglYbrw> ybrwList = entityMapperXSBF.selectByExample(ybrwExample);
                            if (CollectionUtils.isNotEmpty(ybrwList)) {
                                DchyXmglYbrw xmglYbrw = ybrwList.get(0);
                                if (null != xmglYbrw) {
                                    return xmglYbrw.getBlyj();//返回核验意见
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return "";
    }

    /**
     * 备案上传合同前，初始化htxxid，用于获取上传的材料信息
     *
     * @param data
     * @return
     */
    @Override
    public String initHtxx(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        if (StringUtils.isNotBlank(chxmid)) {
            /*根据chxmid获取对应htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx xmglHtxx = htxxList.get(0);
                if (null != xmglHtxx) {
                    return xmglHtxx.getHtxxid();
                }
            }
        }
        /*htxx为空是，生成一个htxxid返回*/
        return UUIDGenerator.generate18();
    }

    /**
     * 在线备案后文件预览
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> onlineBaFilePreview(Map<String, Object> data) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        List<Map<String, Object>> resultList = new LinkedList<>();
        StringJoiner joiner = new StringJoiner("-");
        String chxmid = CommonUtil.ternaryOperator(MapUtils.getString(data, "chxmid"));
        List<Object> clsxList = (List<Object>) data.get("clsxList");
        String sjr = "";
        Date sjsj = null;
        if (StringUtils.isNotBlank(chxmid)) {
            /*委托，核验收件信息*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                DchyXmglSjxx sjxx1 = sjxxList.get(0);
                sjr = sjxx1.getSjr();
                sjsj = sjxx1.getSjsj();
                List<Object> sjxxIdList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxIdList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxIdList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    for (DchyXmglSjcl sjcl : sjclList) {
                        List<Map<String, Object>> uploadClxx = this.getUploadClxx(sjcl.getWjzxid(), joiner);
                        resultList.addAll(uploadClxx);
                    }
                }
            }
            /*合同文件*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx xmglHtxx : htxxList) {
                    String htxxid = xmglHtxx.getHtxxid();
                    if (StringUtils.isNotBlank(htxxid)) {
                        /*通过htxxid获取sjxx*/
                        Example htSjxxExample = new Example(DchyXmglSjxx.class);
                        htSjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
                        List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                        if (CollectionUtils.isNotEmpty(htSjxxList)) {
                            String htSjxxid = htSjxxList.get(0).getSjxxid();
                            /*获取合同收件材料*/
                            Example htSjclExample = new Example(DchyXmglSjcl.class);
                            htSjclExample.createCriteria().andEqualTo("sjxxid", htSjxxid).andIsNotNull("wjzxid");
                            List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                            if (CollectionUtils.isNotEmpty(htSjclList)) {
                                for (DchyXmglSjcl htSjcl : htSjclList) {
                                    List<Map<String, Object>> uploadClxx = this.getUploadClxx(htSjcl.getWjzxid(), joiner);
                                    resultList.addAll(uploadClxx);
                                }
                            }
                        }
                    }
                }
            }

            if (CollectionUtils.isEmpty(resultList)) {
                DchyXmglChxm dchyXmglChxmNew = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                if (dchyXmglChxmNew != null) {
                    String xqfbbh = dchyXmglChxmNew.getXqfbbh();
                    if (StringUtils.isNotEmpty(xqfbbh) && CollectionUtils.isNotEmpty(clsxList)) {
                        //线上委托到线下备案
                        Example exampleOldChxm = new Example(DchyXmglChxm.class);
                        exampleOldChxm.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                        List<DchyXmglChxm> chxmOld = entityMapper.selectByExample(exampleOldChxm);
                        if (CollectionUtils.isNotEmpty(chxmOld)) {
                            chxmid = chxmOld.get(0).getChxmid();

                            /*委托收件信息*/
                            Example sjxxExampleNew = new Example(DchyXmglSjxx.class);
                            sjxxExampleNew.createCriteria().andEqualTo("glsxid", chxmid);
                            List<DchyXmglSjxx> sjxxNewList = entityMapper.selectByExample(sjxxExampleNew);
                            if (CollectionUtils.isNotEmpty(sjxxNewList)) {
                                DchyXmglSjxx sjxx1 = sjxxNewList.get(0);
                                sjr = sjxx1.getSjr();
                                sjsj = sjxx1.getSjsj();
                                List<Object> sjxxWtIdList = new ArrayList<>();
                                List<Object> sjxxHyIdList = new ArrayList<>();
                                for (DchyXmglSjxx sjxx : sjxxNewList) {
                                    if (StringUtils.equals(sjxx.getSsmkid(), SsmkidEnum.JSDWWT.getCode())) {
                                        sjxxWtIdList.add(sjxx.getSjxxid());
                                    } else {
                                        sjxxHyIdList.add(sjxx.getSjxxid());
                                    }

                                }
                                Example sjclWtExample = new Example(DchyXmglSjcl.class);
                                sjclWtExample.createCriteria().andIn("sjxxid", sjxxWtIdList).andIsNotNull("wjzxid");
                                List<DchyXmglSjcl> sjclWtList = entityMapper.selectByExample(sjclWtExample);
                                if (CollectionUtils.isNotEmpty(sjclWtList)) {
                                    for (DchyXmglSjcl sjcl : sjclWtList) {
                                        List<String> oldClsxList = Lists.newArrayList();
                                        String clsx = sjcl.getClsx();
                                        boolean statuss = clsx.contains(Constants.DCHY_XMGL_CLSX_SEPARATOR2);
                                        if (statuss) {
                                            oldClsxList = Arrays.asList(clsx.split(Constants.DCHY_XMGL_CLSX_SEPARATOR2));//根据分号分隔转化为list
                                        } else {
                                            oldClsxList.add(clsx);
                                        }
                                        //判断材料的所属测量事项是否包含当前阶段的测量事项
                                        boolean isMatched = true;
                                        int countClsx = 0;
                                        for (Object obj : oldClsxList) {
                                            if (clsxList.contains(obj)) {
                                                countClsx = countClsx + 1;
                                            }
                                        }
                                        if (countClsx >= 1) {
                                            List<Map<String, Object>> uploadClxx = this.getUploadClxx(sjcl.getWjzxid(), joiner);
                                            resultList.addAll(uploadClxx);
                                        }
                                    }
                                }
                                Example sjclHyExample = new Example(DchyXmglSjcl.class);
                                sjclHyExample.createCriteria().andIn("sjxxid", sjxxHyIdList).andIsNotNull("wjzxid");
                                List<DchyXmglSjcl> sjclHyList = entityMapper.selectByExample(sjclHyExample);
                                if (CollectionUtils.isNotEmpty(sjclHyList)) {
                                    for (DchyXmglSjcl sjcl : sjclHyList) {
                                        List<Map<String, Object>> uploadClxx = this.getUploadClxx(sjcl.getWjzxid(), joiner);
                                        resultList.addAll(uploadClxx);
                                    }
                                }
                            }
                            /*合同文件*/
                            String clsxid = null;
                            if (CollectionUtils.isNotEmpty(clsxList)) {
                                Example exampleChxmClsx = new Example(DchyXmglChxmClsx.class);
                                exampleChxmClsx.createCriteria().andEqualTo("chxmid", chxmid).andIn("clsx", clsxList);
                                List<DchyXmglChxmClsx> chxmClsxList = entityMapper.selectByExample(exampleChxmClsx);
                                if (CollectionUtils.isNotEmpty(chxmClsxList)) {
                                    clsxid = chxmClsxList.get(0).getClsxid();
                                }
                            }

                            if (StringUtils.isNotEmpty(clsxid)) {
                                Example exampleHtxxClsx = new Example(DchyXmglClsxHtxxGx.class);
                                exampleHtxxClsx.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("clsxid", clsxid);
                                List<DchyXmglClsxHtxxGx> htxxClsxList = entityMapper.selectByExample(exampleHtxxClsx);
                                if (CollectionUtils.isNotEmpty(htxxClsxList)) {
                                    //一个阶段对应一份合同
                                    String htxxid = htxxClsxList.get(0).getHtxxid();
                                    if (StringUtils.isNotBlank(htxxid)) {
                                        /*通过htxxid获取sjxx*/
                                        Example htSjxxExample = new Example(DchyXmglSjxx.class);
                                        htSjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
                                        List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
                                        if (CollectionUtils.isNotEmpty(htSjxxList)) {
                                            String htSjxxid = htSjxxList.get(0).getSjxxid();
                                            /*获取合同收件材料*/
                                            Example htSjclExample = new Example(DchyXmglSjcl.class);
                                            htSjclExample.createCriteria().andEqualTo("sjxxid", htSjxxid).andIsNotNull("wjzxid");
                                            List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                                            if (CollectionUtils.isNotEmpty(htSjclList)) {
                                                for (DchyXmglSjcl htSjcl : htSjclList) {
                                                    List<Map<String, Object>> uploadClxx = this.getUploadClxx(htSjcl.getWjzxid(), joiner);
                                                    resultList.addAll(uploadClxx);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //查询线下备份的数据
                        /*委托，核验收件信息*/
                        if (StringUtils.equals(AppConfig.getProperty(Constants.DCHY_XMGL_ZDBA_PZ), "true")) {
                            Example xxSjxxExample = new Example(DchyXmglSjxx.class);
                            xxSjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
                            List<DchyXmglSjxx> xxSjxxList = entityMapperXSBF.selectByExample(sjxxExample);
                            if (CollectionUtils.isNotEmpty(xxSjxxList)) {
                                DchyXmglSjxx sjxx1 = xxSjxxList.get(0);
                                sjr = sjxx1.getSjr();
                                sjsj = sjxx1.getSjsj();
                                List<Object> sjxxIdList = new ArrayList<>();
                                for (DchyXmglSjxx sjxx : xxSjxxList) {
                                    sjxxIdList.add(sjxx.getSjxxid());
                                }
                                Example xxSjclExample = new Example(DchyXmglSjcl.class);
                                xxSjclExample.createCriteria().andIn("sjxxid", sjxxIdList).andIsNotNull("wjzxid");
                                List<DchyXmglSjcl> xxSjclList = entityMapperXSBF.selectByExample(xxSjclExample);
                                if (CollectionUtils.isNotEmpty(xxSjclList)) {
                                    for (DchyXmglSjcl sjcl : xxSjclList) {
                                        List<Map<String, Object>> uploadClxx = this.getUploadClxx(sjcl.getWjzxid(), joiner);
                                        resultList.addAll(uploadClxx);
                                    }
                                }
                            }
                            /*合同文件*/
                            Example xxHtxxExample = new Example(DchyXmglHtxx.class);
                            xxHtxxExample.createCriteria().andEqualTo("chxmid", chxmid);
                            List<DchyXmglHtxx> xxHtxxList = entityMapperXSBF.selectByExample(xxHtxxExample);
                            if (CollectionUtils.isNotEmpty(xxHtxxList)) {
                                for (DchyXmglHtxx xmglHtxx : xxHtxxList) {
                                    String htxxid = xmglHtxx.getHtxxid();
                                    if (StringUtils.isNotBlank(htxxid)) {
                                        /*通过htxxid获取sjxx*/
                                        Example htSjxxExample = new Example(DchyXmglSjxx.class);
                                        htSjxxExample.createCriteria().andEqualTo("glsxid", htxxid);
                                        List<DchyXmglSjxx> htSjxxList = entityMapperXSBF.selectByExample(htSjxxExample);
                                        if (CollectionUtils.isNotEmpty(htSjxxList)) {
                                            String htSjxxid = htSjxxList.get(0).getSjxxid();
                                            /*获取合同收件材料*/
                                            Example htSjclExample = new Example(DchyXmglSjcl.class);
                                            htSjclExample.createCriteria().andEqualTo("sjxxid", htSjxxid).andIsNotNull("wjzxid");
                                            List<DchyXmglSjcl> htSjclList = entityMapperXSBF.selectByExample(htSjclExample);
                                            if (CollectionUtils.isNotEmpty(htSjclList)) {
                                                for (DchyXmglSjcl htSjcl : htSjclList) {
                                                    List<Map<String, Object>> uploadClxx = this.getUploadClxx(htSjcl.getWjzxid(), joiner);
                                                    resultList.addAll(uploadClxx);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        returnMap.put("title", "附件材料");
        returnMap.put("wjzxid", joiner.toString());
        returnMap.put("type", "0");
        returnMap.put("children", resultList);
        returnMap.put("sjr", UserUtil.getUserNameById(sjr));
        returnMap.put("sjsj", sjsj);
        return returnMap;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getHtbgRecord(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        //TODO
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        if (StringUtils.isNotBlank(chxmid)) {
            Example htExample = new Example(DchyXmglHtxx.class);
            htExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExample(htExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                map.put("htxxid", htxxList.get(0).getHtxxid());
            }
        }
        String czkssj = CommonUtil.ternaryOperator(data.get("czkssj"));//开始时间
        String czjssj = CommonUtil.ternaryOperator(data.get("czjssj"));//结束时间
        map.put("czkssj", czkssj);
        map.put("czjssj", czjssj);
        Page<Map<String, Object>> htBgRecordByPage = repository.selectPaging("getHtbgRecord", map, page - 1, pageSize);
        Optional<Page<Map<String, Object>>> optionalMaps = Optional.ofNullable(htBgRecordByPage);
        if (optionalMaps.isPresent()) {
            Page<Map<String, Object>> maps = optionalMaps.get();
            List<Map<String, Object>> content = maps.getContent();
            if (CollectionUtils.isNotEmpty(content)) {
                for (Map<String, Object> objectMap : content) {
                    String czrid = MapUtils.getString(objectMap, "CZRID");
                    if (StringUtils.isNotBlank(czrid)) {
                        Map<String, String> map1 = this.getChdwmcByUserId(czrid);
                        if (MapUtils.isNotEmpty(map1)) {
                            objectMap.put("DWMC", map1.get("DWMC"));
                            objectMap.put("YHMC", map1.get("YHMC"));
                        }
                    }
                }
            }
        }
        return htBgRecordByPage;
    }

    private Map<String, String> getChdwmcByUserId(String userid) {
        Map<String, String> map = new HashMap<>();
        Example yhdwExample = new Example(DchyXmglYhdw.class);
        yhdwExample.createCriteria().andEqualTo("yhid", userid);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            map.put("DWMC", yhdwList.get(0).getDwmc());
            map.put("YHMC", yhdwList.get(0).getYhmc());
            return map;
        }
        return null;
    }

    /**
     * 获取各个从业人员上传的文件材料目录信息
     *
     * @return
     */
    private List<Map<String, Object>> getUploadClxx(String wjzxid, StringJoiner joiner) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        try {
            if (StringUtils.isNotBlank(wjzxid)) {
                List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node node : nodeList) {
                        if (1 == node.getType()) {
                            Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                            uploadFileMap.put("title", node.getName());
                            uploadFileMap.put("wjzxid", node.getId());
                            uploadFileMap.put("type", node.getType());
                            joiner.add(node.getId() + "");
                            resultList.add(uploadFileMap);
                        } else if (0 == node.getType()) {
                            List<Node> tempNodeList = getNodeService().getChildNodes(node.getId());
                            if (CollectionUtils.isNotEmpty(tempNodeList)) {
                                for (Node node1 : tempNodeList) {
                                    if (1 == node1.getType()) {
                                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                                        uploadFileMap.put("title", node1.getName());
                                        uploadFileMap.put("wjzxid", node1.getId());
                                        uploadFileMap.put("type", node1.getType());
                                        joiner.add(node1.getId() + "");
                                        resultList.add(uploadFileMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultList;
    }

    /**
     * 在线办结
     *
     * @param data
     * @return
     */
    @Override
    public ResponseMessage onlineComplete(Map<String, Object> data) {
        /*pushOnlineCompleteToXx*/
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                /*组织数据推送至线下办结*/
                dchyXmglZxbjDto.setChxmid(chxmid);
                dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ);
                String key = chxmid + "zsbj";
                EhcacheUtil.putDataToEhcache(key, null);
                pushDataToMqService.pushOnlineCompleteToXx(dchyXmglZxbjDto);
                /*获取线下办结后的结果*/
                ResponseMessage responseMessage = this.queryXxData(key);
                return responseMessage;
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return null;
    }

    /**
     * 线办结前的成查状态检查
     *
     * @param data
     * @return
     */
    @Override
    public ResponseMessage onlineCompleteCheck(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        try {
            if (StringUtils.isNotBlank(chxmid)) {
                /*组织数据推送至线下办结*/
                dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ_JC);
                dchyXmglZxbjDto.setChxmid(chxmid);
                String key = chxmid + "zsbjcheck";
                EhcacheUtil.putDataToEhcache(key, null);
                pushDataToMqService.pushOnlineCompleteToXx(dchyXmglZxbjDto);
                /*获取线下办结检查的结果*/
                ResponseMessage responseMessage = this.queryXxData(key);
                return responseMessage;
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return null;
    }


    /**
     * 在线成果下载
     *
     * @param data
     * @return
     */
    @Override
    public ResponseMessage onlineGcPreview(Map<String, Object> data) {
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        String chgcid = CommonUtil.ternaryOperator(data.get("chgcid"));
        ResponseMessage responseMessage = new ResponseMessage();
        /*组织数据*/
        try {
            Map<String, Object> map = new HashMap<>();
//            map.put("model", "onlineGcPreview");
//            map.put("chxmid", chxmid);
//            map.put("chgcid", chgcid);

            dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ_YL);
            dchyXmglZxbjDto.setChxmid(chxmid);
            dchyXmglZxbjDto.setChgcid(chgcid);
            String key = chxmid + "preview";
            EhcacheUtil.putDataToEhcache(key, null);
            pushDataToMqService.pushOnlineGcViewToXx(dchyXmglZxbjDto);
            /*获取线下办结检查的结果*/
            responseMessage = this.queryXxFileData(key);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return responseMessage;
    }


    /**
     * 在线成果预览子结点
     *
     * @param data
     * @return
     */
    @Override
    public ResponseMessage onlineGcPreviewById(Map<String, Object> data) {
        boolean flag = false;
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        String chgcid = CommonUtil.ternaryOperator(data.get("chgcid"));
        String id = CommonUtil.ternaryOperator(data.get("id"));
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ_YLZJD);
            dchyXmglZxbjDto.setId(id);
            dchyXmglZxbjDto.setChgcid(chgcid);
            dchyXmglZxbjDto.setChxmid(chxmid);
            String key = chxmid + "previewById" + id;
            EhcacheUtil.putDataToEhcache(key, null);
            pushDataToMqService.pushOnlineGcViewToXx(dchyXmglZxbjDto);
            /*获取线下办结检查的结果*/
            responseMessage = this.queryXxFileData(key);
            if (MapUtils.isNotEmpty(responseMessage.getData())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataList = (Map<String, Object>) responseMessage.getData().get("dataList");
                if (MapUtils.isNotEmpty(dataList)) {
                    JSONArray jsonArray = (JSONArray) dataList.get("items");
                    if (CollectionUtils.isNotEmpty(jsonArray)) {
                        Iterator<Object> iterator = jsonArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jo = (JSONObject) iterator.next();
                            Integer type = (Integer) jo.get("type");//0:文件夹
                            String name = (String) jo.get("name");
                            //非成果报告移除
                            if (StringUtils.isNotBlank(name) && (StringUtils.equals(name, Constants.DCHY_XMGL_ONLINE_SLTJ) && type.equals(0)) || (StringUtils.equals(name, Constants.DCHY_XMGL_ONLINE_RKCG) && type.equals(0))) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return responseMessage;
    }

    /**
     * 在线成果预览子结点
     *
     * @param data
     * @return
     */
    @Override
    public ResponseMessage onlineGetUploadFileNums(Map<String, Object> data) {
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        String wjzxid = CommonUtil.ternaryOperator(data.get("wjzxid"));
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("model", "onlineGetUploadFileNums");
            map.put("wjzxid", wjzxid);

            dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ_ZXSC);
            dchyXmglZxbjDto.setWjzxid(wjzxid);
            String key = wjzxid + "getFileNums";
            EhcacheUtil.putDataToEhcache(key, null);
            pushDataToMqService.pushOnlineGcViewToXx(dchyXmglZxbjDto);
            /*获取线下办结检查的结果*/
            responseMessage = this.queryXxData(key);
            logger.info("响应信息:" + responseMessage);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return responseMessage;
    }

    /**
     * 线下下载成查包
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseEntity<byte[]> onlineGcDownload(HttpServletRequest request, HttpServletResponse response) {
        DchyXmglZxbjDto dchyXmglZxbjDto = new DchyXmglZxbjDto();
        String wjzxid = request.getParameter("wjzxid");
        try {
            dchyXmglZxbjDto.setModel(Constants.DCHY_XMGL_ZXBJ_ZXXZ);
            dchyXmglZxbjDto.setWjzxid(wjzxid);
            String key = wjzxid + "getGcDownload";
            EhcacheUtil.putDataToEhcache(key, null);
            pushDataToMqService.pushOnlineGcViewToXx(dchyXmglZxbjDto);
            /*获取线下传输过来的文件*/
            ResponseMessage message = this.queryXxFileData(key);
            Map<String, Object> dataList = (Map<String, Object>) message.getData().get("dataList");
            String fileName = (String) dataList.get("fileName");
            byte[] fileBytes = (byte[]) dataList.get("fileBytes");
            return this.generateXxZipToXs(fileName, fileBytes, request, response);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return null;
    }

    /**
     * 组织线下传递的文件byte组成成文件
     *
     * @return
     */
    private ResponseEntity<byte[]> generateXxZipToXs(String fileName, byte[] fileBytes, HttpServletRequest request, HttpServletResponse response) {
        // 构建响应
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // 二进制数据流
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
        response.setContentType("application/octet-stream");
        ZipOutputStream zos = null;
        HttpStatus httpState = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileName = FileDownoadUtil.encodeFileName(request, fileName);
//            response.setHeader("Content-Disposition", "attachment;" + fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; " + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Last-Modified", new Date().toString());
            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
            return org.springframework.http.ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileBytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(fileBytes);
        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            logger.error("错误原因:{}", e);
        } finally {
            // 关闭流
            try {
                if (null != zos) {
                    zos.flush();
                    zos.close();
                }
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("错误原因:{}", e);
            }
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    //获取线上错误列表的返回值,当请求失败时,休眠3s再次请求,最多请求10次
    private ResponseMessage queryXxFileData(String key) throws InterruptedException {
        ResponseMessage resultMap = new ResponseMessage();
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
        for (int i = 0; i < 30; i++) {
            if (null != valueWrapper) {
                if (null != valueWrapper.get()) {
                    resultMap.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
                    resultMap.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
                    resultMap.getData().put("dataList", valueWrapper.get());
                    return resultMap;
                } else {
                    resultMap.getHead().setMsg(ResponseMessage.CODE.ONLINE_CG_PREVIEW_FAIL.getMsg());
                    resultMap.getHead().setCode(ResponseMessage.CODE.ONLINE_CG_PREVIEW_FAIL.getCode());
                    resultMap.getData().put("dataList", new ArrayList<>());
                    Thread.sleep(500);
                    valueWrapper = EhcacheUtil.getDataFromEhcache(key);
                }
            }
        }
        return resultMap;
    }

    /**
     * 获取mlkid
     *
     * @param
     * @return
     */
    private String getMlkByUserId() {
        String userId = UserUtil.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            Example yhdwExample = new Example(DchyXmglYhdw.class);
            yhdwExample.createCriteria().andEqualTo("yhid", userId);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                DchyXmglYhdw yhdw = yhdwList.get(0);
                if (null != yhdw) {
                    String dwbh = yhdw.getDwbh();
                    if (StringUtils.isNotBlank(dwbh)) {
                        /*通过单位编号获取对应mlkid*/
                        Example mlkExample = new Example(DchyXmglMlk.class);
                        mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                        List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
                        if (CollectionUtils.isNotEmpty(mlkList)) {
                            DchyXmglMlk mlk = mlkList.get(0);
                            if (null != mlk) {
                                return mlk.getMlkid();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    //获取线上错误列表的返回值,当请求失败时,休眠3s再次请求,最多请求10次
    private ResponseMessage queryXxData(String key) throws InterruptedException {
        ResponseMessage resultMap = new ResponseMessage();
        Cache.ValueWrapper valueWrapper = EhcacheUtil.getDataFromEhcache(key);
        for (int i = 0; i < 20; i++) {
            if (null != valueWrapper.get()) {
                return (ResponseMessage) valueWrapper.get();
            } else {
                resultMap.getHead().setMsg(ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getMsg());
                resultMap.getHead().setCode(ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getCode());
                Thread.sleep(500);
                valueWrapper = EhcacheUtil.getDataFromEhcache(key);
            }
        }
        return resultMap;
    }

    private List<String> generateClsxByHtxxidFromXsbf(String htxxid) {
        List<String> clsxList = new ArrayList<>();
        Example htxxGxExample = new Example(DchyXmglClsxHtxxGx.class);
        htxxGxExample.createCriteria().andEqualTo("htxxid", htxxid);
        List<DchyXmglClsxHtxxGx> htxxGxListList = entityMapperXSBF.selectByExample(htxxGxExample);
        if (CollectionUtils.isNotEmpty(htxxGxListList)) {
            List<Object> clsxids = new ArrayList<>();
            for (DchyXmglClsxHtxxGx htxxGx : htxxGxListList) {
                clsxids.add(htxxGx.getClsxid());
            }
            if (CollectionUtils.isNotEmpty(clsxids)) {
                Example chxmClsxExample = new Example(DchyXmglChxmClsx.class);
                chxmClsxExample.createCriteria().andIn("clsxid", clsxids);
                List<DchyXmglChxmClsx> chxmClsxList = entityMapperXSBF.selectByExample(chxmClsxExample);
                if (CollectionUtils.isNotEmpty(chxmClsxList)) {
                    for (DchyXmglChxmClsx chxmClsx : chxmClsxList) {
                        clsxList.add(chxmClsx.getClsx());
                    }
                }
            }
        }
        return clsxList;
    }
}
