package cn.gtmap.msurveyplat.serviceol.log;


import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.AuditLogDto;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlkClsxGx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.serviceol.core.mapper.TableMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.ReflectUtils;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.plat.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ??????????????????
 *
 * @author Liuhongwei
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private final String url = Constants.LOG_URL + "/api/v2/logs";
    //?????????????????????
    int corePoolSize = 20;
    //??????????????????????????????????????????
    int maximumPoolSize = 200;
    //?????????????????????corePoolSize?????????maximumPoolSize????????????corePoolSize???????????????????????????
    long keepActiveTime = 200;
    //????????????????????????
    TimeUnit timeUnit = TimeUnit.SECONDS;
    //?????????????????????????????????????????????FIFO????????????????????????????????????300
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(300);
    //??????ThreadPoolExecutor??????????????????????????????????????????????????????
    ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit, workQueue);

    private final static String ANONYMOUS_USER_ID = "-999";
    private final static String ANONYMOUS_USER_NAME = "????????????";
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private EntityMapper entityMapper;

    public Map<String, List<Map<String, Object>>> tableMap;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Value("${spring.datasource.dchy.username}")
    private String username;

    @Pointcut("@annotation(cn.gtmap.msurveyplat.common.annotion.SystemLog)")
    public void logPointCut() {

    }

    /**
     * ????????????????????????????????????
     *
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void writeLog(JoinPoint joinPoint) {
        if (StringUtils.isNotBlank(Constants.LOG_URL)) {
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Object[] args = joinPoint.getArgs();

            Method method = ms.getMethod();
            SystemLog systemLog = method.getAnnotation(SystemLog.class);
            try {
                saveLog(method, systemLog, args);
            } catch (Exception e) {
                logger.error("??????????????????:??????????????? {}", e);
            }
        } else {
            logger.error("??????????????????:?????????????????? ???????????????????????????");
        }
    }

    private void saveLog(Method method, SystemLog systemLog, final Object[] args) {
        Object[] onbj = method.getDeclaredAnnotations();
        // ??????????????????????????????????????????????????????????????????????????????????????????es????????????????????????????????????????????????
        Parameter[] paramaters = method.getParameters();
        Map<String, String> argMaps = Maps.newHashMap();
        Map<String, String> cznr = Maps.newHashMap();
        if (paramaters != null && args != null) {
            for (int i = 0; i < (paramaters.length < args.length ? paramaters.length : args.length); i++) {
                argMaps.put(paramaters[i].getName(), JSONObject.toJSONString(args[i]));
            }
        }
        cznr.put("args", JSONObject.toJSONString(argMaps));
        // ??????args??????
        // params
        UserInfo userInfo = UserUtil.getCurrentUser();
        AuditLogDto auditLogDto = new AuditLogDto();
        auditLogDto.setCzrid(userInfo != null ? userInfo.getId() : ANONYMOUS_USER_ID);
        auditLogDto.setCzrmc(userInfo != null ? userInfo.getUsername() : ANONYMOUS_USER_NAME);
        auditLogDto.setCzlx(systemLog.czlxCode());
        auditLogDto.setCzlxmc(systemLog.czlxMc());
        auditLogDto.setCzmk(systemLog.czmkCode());
        auditLogDto.setCzmkmc(systemLog.czmkMc());
        auditLogDto.setCznr(cznr);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sendPostRequest(url, JSONObject.parseObject(JSONObject.toJSONString(auditLogDto)));
                } catch (Exception e) {
                    logger.error("errorMsg:", e);
                }

            }
        });
    }

    private void saveLog(final AuditLogDto auditLogDto) {
        //System.out.println(JSONObject.toJSONString(auditLogDto));
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sendPostRequest(url, JSONObject.parseObject(JSONObject.toJSONString(auditLogDto)));
                } catch (Exception e) {
                    logger.error("errorMsg:", e);
                }

            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param url
     * @param obj
     */
    public void sendPostRequest(String url, JSONObject obj) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;

        // ????????????????????????
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ??????????????????????????????????????????
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(obj, headers);

        // ??????HTTP??????????????????????????????????????????
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        Integer status = response.getStatusCodeValue();
        if (200 == status) {
            logger.info("??????????????????:??????????????????");
        } else {
            logger.error("??????????????????:??????????????????" + status);
        }
    }

    @Pointcut("@annotation(cn.gtmap.msurveyplat.common.annotion.AuditLog)")
    public void xglogPointCut() {

    }

    @Before("xglogPointCut()")
    public Object doXgBefore(JoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        String requestMappingValue = method.getDeclaringClass().getName() + "." + method.getName();
        AuditLog log = method.getAnnotation(AuditLog.class);
        try {
            generateLog(method, joinPoint, log, requestMappingValue);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("????????????", e);
        }
        return null;
    }

    private void generateLog(Method method, JoinPoint joinPoint, AuditLog lzlog, String requestMappingValue) throws IllegalAccessException {
        final AuditLogDto auditLogDto = new AuditLogDto();
        auditLogDto.setCzsj(Calendar.getInstance().getTime());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String ip = request.getRemoteAddr();
        auditLogDto.setIp(ip);
        if (UserUtil.getCurrentUser() != null) {
            auditLogDto.setCzrmc(UserUtil.getCurrentUser().getUsername());
        } else {
            auditLogDto.setCzrmc("????????????");
        }
        auditLogDto.setCzmkmc(lzlog.czmkMc());
        auditLogDto.setCzmk(lzlog.czmkCode());
        auditLogDto.setCzlx(lzlog.czlxCode());
        auditLogDto.setCzlxmc(lzlog.czlxMc());
        UserInfo userInfo = UserUtil.getCurrentUser();
        auditLogDto.setCzrid(userInfo != null ? userInfo.getId() : ANONYMOUS_USER_ID);
//        auditLogDto.setCzrid("4F347762EC5F40C786C2C13BCA82E1D1");
        auditLogDto.setCzrmc(userInfo != null ? userInfo.getUsername() : ANONYMOUS_USER_NAME);
        dealArguments(method, joinPoint, auditLogDto, lzlog.clazz());
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
//                        System.out.println("******??????*******");
//                        System.out.println(JSONObject.toJSONString(auditLogDto));
                        saveLog(auditLogDto);
                    } catch (Exception e) {
                        logger.error("errorMsg:", e);
                    }

                }
            });
        } catch (Exception e) {
            logger.error("??????????????????{}", e);
        }
    }

    private void dealArguments(Method method, JoinPoint joinPoint, AuditLogDto auditLogDto, Class clazz) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        //??????????????????  ????????????
        String[] paraNameArr = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        List<Object> arguments = Lists.newArrayList();
        Object arg;
        Map<String, Map<String, List<Object>>> xgnrMap = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            Map<String, String> item = Maps.newHashMap();
            arg = args[i];
            if (arg == null) {
                continue;
            }
            if (args[i] instanceof Model || arg instanceof Pageable || arg instanceof RedirectAttributes || arg instanceof HttpServletResponse) {
                continue;
            } else if (args[i] instanceof HttpServletRequest) {
                item.put("parameterType", HttpServletRequest.class.getName());
                item.put("parameterName", paraNameArr[i]);
                arguments.add(item);
            } else {
                item.put("parameterType", arg.getClass().getName());
                if (arg.getClass() == clazz) {
                    Object glsxid = ReflectUtils.getValueByName(arg, "glsxid");
                    if (null != glsxid && StringUtils.isNotBlank(glsxid.toString())) {
                        auditLogDto.setGlsxid(glsxid.toString());
                    }
                    dbObject(arg, xgnrMap, "");
                } else if (StringUtils.equals(auditLogDto.getCzlxmc(), "??????")) {
//                    saveLogMx(arg, auditLogDto, tableName);
                } else {
                    if (StringUtils.indexOf(arg.toString(), "ryzp") > 0) {
                        JSONObject jsonObject = JSON.parseObject(arg.toString());
                        jsonObject.remove("ryzp");
                        item.put("parameterValue", JSONObject.toJSONString(jsonObject));
                    } else {
                        item.put("parameterValue", JSONObject.toJSONString(arg));

                    }
                    item.put("parameterName", paraNameArr[i]);
                    arguments.add(item);
                }
            }
        }
        Map<String, String> cznr = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(arguments)) {
            cznr.put("czcs", JSONObject.toJSONString(arguments));
        }
        if (MapUtils.isNotEmpty(xgnrMap)) {
            List<Map<String, Object>> czxx = Lists.newArrayList();
            for (Map.Entry<String, Map<String, List<Object>>> entry : xgnrMap.entrySet()) {
                Map<String, Object> newCzxx = Maps.newHashMap();
                List<Map<String, Object>> cznrList = Lists.newArrayList();
                newCzxx.put("czlx", entry.getKey());
                newCzxx.put("cznr", cznrList);
                Map<String, List<Object>> xnggrList = entry.getValue();
                if (MapUtils.isNotEmpty(xnggrList)) {
                    for (Map.Entry<String, List<Object>> entry2 : xnggrList.entrySet()) {
                        Map cznrTemp = Maps.newHashMap();
                        cznrTemp.put("mkmc", entry2.getKey());
                        cznrTemp.put("xgzdxx", entry2.getValue());
                        cznrList.add(cznrTemp);
                    }
                }
                czxx.add(newCzxx);
            }
            cznr.put("xgnr", JSONObject.toJSONString(czxx));
        }
        cznr.put("glsxid", auditLogDto.getGlsxid());
        cznr.put("czrid", auditLogDto.getCzrid());
        auditLogDto.setCznr(cznr);
//        System.out.println(JSONObject.toJSONString(auditLogDto));
    }

    private void dbObject(Object object, Map xgnrMap, String prefix) throws IllegalAccessException {
        if (object instanceof ArrayList) {
            ArrayList list = (ArrayList) object;
            int i = 1;
            for (Object o : list) {
                saveLogNr(o, xgnrMap, prefix);
            }
        } else if (!(object instanceof String) && null != object) {
            saveLogNr(object, xgnrMap, prefix);
        }
    }

    private void saveLogNr(Object object, Map xgnrMap, String prefix) throws IllegalAccessException {
        Table table = object != null ? (Table) object.getClass().getAnnotation(Table.class) : null;
        try {
            if (null != table) {
                String tableName = table.name();
                String pk = (String) ReflectUtils.getPkValue(object);
                String czlx = "";
                if (StringUtils.isNotBlank(pk)) {
                    if (StringUtils.indexOf(pk, "delete_") == 0 && object.getClass() != DchyXmglMlkClsxGx.class) {
                        czlx = "??????";
                        pk = pk.substring(7);
                        Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                        List<Map<String, Object>> compairedMapList = getFields(oldObject, "YZ", new String[]{"dwmc", "tyshxydm", "mlktp"});
                        if (CollectionUtils.isNotEmpty(compairedMapList)) {
                            combine(xgnrMap, compairedMapList, prefix, czlx);
                        }
                    } else {
                        if (object.getClass() == DchyXmglMlkClsxGx.class) {
                            if (pk.indexOf("delete_") != -1) {
                                czlx = "??????";
                                pk = pk.substring(7);
                                Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                                List<Map<String, Object>> compairedMapList = getFields(oldObject, "YZ", new String[]{"dwmc", "tyshxydm", "mlktp"});
                                if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                    combine(xgnrMap, compairedMapList, prefix, czlx);
                                }
                            } else {
                                Example example = new Example(object.getClass());
                                example.createCriteria().andEqualTo("clsx", ((DchyXmglMlkClsxGx) object).getClsx()).andEqualTo("mlkid", ((DchyXmglMlkClsxGx) object).getMlkid());
                                List<DchyXmglMlkClsxGx> mlkClsxGxes = entityMapper.selectByExample(example);
                                //Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                                if (CollectionUtils.isEmpty(mlkClsxGxes)) {
                                    czlx = "??????";
                                    List<Map<String, Object>> compairedMapList = getFieldsByMlkGx(object, "XZ", new String[]{});
                                    if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                        combine(xgnrMap, compairedMapList, prefix, czlx);
                                    }
                                }
                            }
                        } else {
                            Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                            if (null != oldObject) {
                                if (oldObject.getClass() == DchyXmglCyry.class) {
                                    DchyXmglCyry cyry = (DchyXmglCyry) oldObject;
                                    if (StringUtils.isNotBlank(cyry.getRyxm())) {
                                        czlx = "??????";
                                        List<Map<String, Object>> compairedMapList = compareFields(oldObject, object, new String[]{"dwmc", "tyshxydm,wjzxid", "mlktp"});
                                        if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                            combine(xgnrMap, compairedMapList, prefix, czlx);
                                        }
                                    } else {
                                        czlx = "??????";
                                        List<Map<String, Object>> compairedMapList = getFields(object, "XZ", new String[]{"dwmc", "tyshxydm", "mlktp"});
                                        if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                            combine(xgnrMap, compairedMapList, prefix, czlx);
                                        }
                                    }
                                }
                            }
                            if (oldObject == null) {
                                czlx = "??????";
                                List<Map<String, Object>> compairedMapList = getFields(object, "XZ", new String[]{"dwmc", "tyshxydm", "mlktp"});
                                if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                    combine(xgnrMap, compairedMapList, prefix, czlx);
                                }
                            } else if (StringUtils.isBlank(czlx)) {
                                czlx = "??????";
                                List<Map<String, Object>> compairedMapList = compareFields(oldObject, object, new String[]{"dwmc", "tyshxydm,wjzxid", "mlktp"});
                                if (CollectionUtils.isNotEmpty(compairedMapList)) {
                                    combine(xgnrMap, compairedMapList, prefix, czlx);
                                }
                            }
                        }
                    }

                }
            } else if (null != object) {
                Field[] fiels = object.getClass().getDeclaredFields();
                if (null != fiels) {
                    for (int i = 0; i < fiels.length; i++) {
                        DescribeAno describeAno = fiels[i].getAnnotation(DescribeAno.class);
                        fiels[i].setAccessible(true);
                        if (null != describeAno) {
                            String describtion = describeAno.value();
                            dbObject(fiels[i].get(object), xgnrMap, StringUtils.isNoneBlank(prefix, describtion) ? prefix + "-" + describtion : (StringUtils.isNotBlank(prefix) ? prefix : describtion));
                        } else {
                            dbObject(fiels[i].get(object), xgnrMap, prefix);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param obj1      ???????????????????????????
     * @param lx
     * @param ignoreArr ?????????????????????????????????
     * @return ???????????????????????? map
     */
    @SuppressWarnings("rawtypes")
    public List<Map<String, Object>> getFieldsByMlkGx(Object obj1, String lx, String[] ignoreArr) {
        List<Map<String, Object>> compairedList = Lists.newArrayList();
        try {
            List<String> ignoreList = null;
            if (ignoreArr != null && ignoreArr.length > 0) {
                ignoreList = Arrays.asList(ignoreArr);
            }
            Class clazz = obj1.getClass();
            String tableName = obj1.getClass().getAnnotation(Table.class).name();
            List<Map<String, Object>> mapList = getTableMap(tableName);
            //??????object???????????????
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            List<String> msList = Lists.newArrayList();
            Map<String, Object> compairedMap = Maps.newHashMap();
            for (PropertyDescriptor pd : pds) {
                String name = pd.getName();
                String zdmc = "";
                if (ignoreList != null && ignoreList.contains(name)) {
                    continue;
                }
                Method readMethod = pd.getReadMethod();//???????????????get??????
                //???obj1?????????get?????????????????????obj1????????????
                Object o1 = readMethod.invoke(obj1);
                //???obj2?????????get?????????????????????obj2????????????

                if (o1 instanceof String) {
                    if (StringUtils.isBlank(o1.toString())) {
                        o1 = null;
                    }
                }


                String data = this.getZdMc("CLSX", o1.toString());

                if (StringUtils.isNoneBlank(data)) {
                    msList.add(zdmc + " " + data);

                }
                continue;
            }
            compairedMap.put("ZDMC", "????????????");
            compairedMap.put(lx, StringUtils.join(msList, " , "));
            compairedList.add(compairedMap);
            return compairedList;
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("????????????", e);
            return null;
        }
    }

    private String getZdMc(String clsx, String dm) {
        DchyXmglZd clsxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(clsx, dm);
        String data = "";
        if (null != clsxZd) {
            data = clsxZd.getMc();
        }
        return data;
    }

    /**
     * @param obj1      ???????????????????????????
     * @param lx
     * @param ignoreArr ?????????????????????????????????
     * @return ???????????????????????? map
     */
    @SuppressWarnings("rawtypes")
    public List<Map<String, Object>> getFields(Object obj1, String lx, String[] ignoreArr) {
        List<Map<String, Object>> compairedList = Lists.newArrayList();
        try {
            List<String> ignoreList = null;
            if (ignoreArr != null && ignoreArr.length > 0) {
                ignoreList = Arrays.asList(ignoreArr);
            }
            Class clazz = obj1.getClass();
            String tableName = obj1.getClass().getAnnotation(Table.class).name();
            List<Map<String, Object>> mapList = getTableMap(tableName);
            //??????object???????????????
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            List<String> msList = Lists.newArrayList();
            Map<String, Object> compairedMap = Maps.newHashMap();
            for (PropertyDescriptor pd : pds) {
                String name = pd.getName();
                String zdmc = pd.getName();
                if (ignoreList != null && ignoreList.contains(name)) {
                    continue;
                }
                Method readMethod = pd.getReadMethod();//???????????????get??????
                //???obj1?????????get?????????????????????obj1????????????
                Object o1 = readMethod.invoke(obj1);
                //???obj2?????????get?????????????????????obj2????????????

                if (o1 instanceof String) {
                    if (StringUtils.isBlank(o1.toString())) {
                        o1 = null;
                    }
                }

                //????????????????????????
                if (o1 instanceof Timestamp) {
                    o1 = new Date(((Timestamp) o1).getTime());
                }
                //?????????
                if (StringUtils.equals("clsx", name)) {
                    String data = this.getZdMc("CLSX", o1.toString());
                    if (StringUtils.isNotBlank(data)) {
                        msList.add(data);
                    }
                } else if (obj1.getClass() != DchyXmglMlkClsxGx.class) {
                    zdmc = getZdmc(name, mapList);
                    String data = convertMc(name, o1);
                    if (StringUtils.isNoneBlank(data)) {
                        msList.add(zdmc + " " + data);

                    }
                }
                continue;
            }
            compairedMap.put("ZDMC", "????????????");
            compairedMap.put(lx, StringUtils.join(msList, " , "));
            compairedList.add(compairedMap);
            return compairedList;
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("????????????", e);
            return null;
        }
    }

    private void combine(Map<String, Map<String, List<Object>>> map, List tempList, String prefix, String czlx) {
        if (CollectionUtils.isNotEmpty(tempList)) {
            if (map.containsKey(czlx)) {
                Map<String, List<Object>> preFixMap = map.get(czlx);
                if (preFixMap.containsKey(prefix)) {
                    List<Object> list = preFixMap.get(prefix);
                    list.addAll(tempList);
                } else {
                    preFixMap.put(prefix, tempList);
                }
            } else {
                Map<String, List<Object>> preFixMap = Maps.newHashMap();
                preFixMap.put(prefix, tempList);
                map.put(czlx, preFixMap);
            }
        }
    }

    /**
     * @param obj1      ???????????????????????????1
     * @param obj2      ???????????????????????????2
     * @param ignoreArr ?????????????????????????????????
     * @return ???????????????????????? map
     */
    @SuppressWarnings("rawtypes")
    public List<Map<String, Object>> compareFields(Object obj1, Object obj2, String[] ignoreArr) {
        List<Map<String, Object>> compairedList = Lists.newArrayList();
        try {
            List<String> ignoreList = null;
            if (ignoreArr != null && ignoreArr.length > 0) {
                ignoreList = Arrays.asList(ignoreArr);
            }
            if (obj1.getClass() == obj2.getClass()) { //???????????????????????????????????????????????????
                Class clazz = obj1.getClass();
                String tableName = obj1.getClass().getAnnotation(Table.class).name();
                List<Map<String, Object>> mapList = getTableMap(tableName);
                //??????object???????????????
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
                Map compairedMap;
                for (PropertyDescriptor pd : pds) {
                    String name = pd.getName();
                    String zdmc = pd.getName();
                    if (ignoreList != null && ignoreList.contains(name)) {
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();//???????????????get??????
                    //???obj1?????????get?????????????????????obj1????????????
                    Object o1 = readMethod.invoke(obj1);
                    //???obj2?????????get?????????????????????obj2????????????
                    Object o2 = readMethod.invoke(obj2);
                    if (o1 instanceof String) {
                        if (StringUtils.isBlank(o1.toString())) {
                            o1 = null;
                        }
                    }
                    if (o2 instanceof String) {
                        if (StringUtils.isBlank(o2.toString())) {
                            o2 = null;
                        }
                    }
                    //????????????????????????
                    if (o1 instanceof Timestamp) {
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if (o2 instanceof Timestamp) {
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if (o1 == null && o2 == null) {
                        continue;
                    } else if (o1 == null && o2 != null) {
                        //?????????
                        zdmc = getZdmc(name, mapList);
                        if (StringUtils.isNotBlank(zdmc) && (StringUtils.equals("?????????", zdmc) || StringUtils.equals("????????????", zdmc))) {
                            compairedMap = Maps.newHashMap();
                            compairedMap.put("ZDMC", zdmc);
                            compairedMap.put("YZ", SM4Util.decryptData_ECB(o1.toString()));//??????
                            compairedMap.put("XZ", SM4Util.decryptData_ECB(o2.toString()));//??????
                            compairedList.add(compairedMap);
                        } else {
                            compairedMap = Maps.newHashMap();
                            compairedMap.put("ZDMC", zdmc);
                            compairedMap.put("YZ", convertMc(name, o1));//??????
                            compairedMap.put("XZ", convertMc(name, o2));//??????
                            compairedList.add(compairedMap);
                            continue;
                        }
                    }
                    if (!o1.equals(o2)) {
                        zdmc = getZdmc(name, mapList);
                        if (StringUtils.isNotBlank(zdmc) && (StringUtils.equals("?????????", zdmc) || StringUtils.equals("????????????", zdmc))) {
                            compairedMap = Maps.newHashMap();
                            compairedMap.put("ZDMC", zdmc);
                            compairedMap.put("YZ", SM4Util.decryptData_ECB(o1.toString()));//??????
                            compairedMap.put("XZ", SM4Util.decryptData_ECB(o2.toString()));//??????
                            compairedList.add(compairedMap);
                        } else if (StringUtils.isNotBlank(zdmc) && StringUtils.equals("????????????(?????????A.17)", zdmc)) {
                            compairedMap = Maps.newHashMap();
                            compairedMap.put("ZDMC", zdmc.substring(0, 4));
                            compairedMap.put("YZ", this.getZdMc("ZZDJ", o1.toString()));//??????
                            compairedMap.put("XZ", this.getZdMc("ZZDJ", o2.toString()));//??????
                            compairedList.add(compairedMap);
                        } else if (StringUtils.isNotBlank(zdmc) && StringUtils.equals("????????????(?????????A.4)", zdmc)) {
                            compairedMap = Maps.newHashMap();
                            compairedMap.put("ZDMC", zdmc.substring(0, 4));
                            compairedMap.put("YZ", this.getZdMc("DWXZ", o1.toString()));//??????
                            compairedMap.put("XZ", this.getZdMc("DWXZ", o2.toString()));//??????
                            compairedList.add(compairedMap);
                        } else {
                            if (!StringUtils.equals("????????????id", zdmc)) {
                                compairedMap = Maps.newHashMap();
                                compairedMap.put("ZDMC", zdmc);
                                compairedMap.put("YZ", convertMc(name, o1) != null ? convertMc(name, o1) : "");//??????
                                compairedMap.put("XZ", convertMc(name, o2) != null ? convertMc(name, o2) : "");//??????
                                compairedList.add(compairedMap);
                            }
                        }
                    }
                }
            }
            return compairedList;
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("????????????", e);
            return null;
        }
    }

    private String convertMc(String zddm, Object ob) {
        if (ob instanceof Date) {
            return CommonUtil.formatYNRSFM((Date) ob);
        }
        return (String) ob;
    }

    private String getZdmc(String name, List<Map<String, Object>> mapList) {
        String zdmc = "";
        if (CollectionUtils.isNotEmpty(mapList)) {
            for (Map zdMap : mapList) {
                if (StringUtils.equals(StringUtils.lowerCase(zdMap.get("ZD").toString()), name)) {
                    if (zdMap.get("ZDMC") != null) {
                        zdmc = zdMap.get("ZDMC").toString();
                    }
                    break;
                }
            }
        }
        return zdmc;
    }

    private List<Map<String, Object>> getTableMap(String tableName) {
        List<Map<String, Object>> mapList;
        if (MapUtils.isEmpty(tableMap)) {
            tableMap = Maps.newHashMap();
        }
        if (tableMap.containsKey(tableName)) {
            mapList = tableMap.get(tableName);
        } else {
            String owner = username.toUpperCase();
            mapList = tableMapper.queryTableZdmc(tableName, owner);
            if (CollectionUtils.isNotEmpty(mapList)) {
                tableMap.put(tableName, mapList);
            }
        }
        return mapList;
    }
}
