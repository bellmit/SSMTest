package cn.gtmap.msurveyplat.promanage.aop;


import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.TableMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.model.UserInfo;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.ReflectUtils;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private EntityMapper entityMapper;

    public Map<String, List<Map>> tableMap;

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
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        Method method = ms.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        UserInfo userInfo = UserUtil.getCurrentUser();

        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        AuditLogDto auditLogDto = generateLog(userInfo, method, systemLog, args);
                        saveTableLog(auditLogDto);
                    } catch (Exception e) {
                        logger.error("errorMsg:", e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("??????????????????{}", e);
        }
    }

    private AuditLogDto generateLog(UserInfo userInfo, Method method, SystemLog systemLog, final Object[] args) {
        Object[] obj = method.getDeclaredAnnotations();
        // ??????????????????????????????????????????????????????????????????????????????????????????es????????????????????????????????????????????????
        Parameter[] paramaters = method.getParameters();
        Map<String, String> argMaps = Maps.newHashMap();
        Map<String, String> cznr = Maps.newHashMap();
        if (paramaters != null && args != null) {
            for (int i = 0; i < (paramaters.length < args.length ? paramaters.length : args.length); i++) {
                try {
                    argMaps.put(paramaters[i].getName(), JSONObject.toJSONString(args[i]));
                } catch (Exception e) {
                    if (null != args[i]) {
                        argMaps.put(paramaters[i].getName(), args[i].toString());
                    }
                }
            }
        }
        cznr.put("args", JSONObject.toJSONString(argMaps));
        // ??????args??????
        // params

        AuditLogDto auditLogDto = new AuditLogDto();
        auditLogDto.setCzrid(userInfo != null ? userInfo.getId() : ANONYMOUS_USER_ID);
        auditLogDto.setCzrmc(userInfo != null ? userInfo.getUsername() : ANONYMOUS_USER_NAME);
        auditLogDto.setCzlx(systemLog.czlxCode());
        auditLogDto.setCzlxmc(systemLog.czlxMc());
        auditLogDto.setCzmk(systemLog.czmkCode());
        auditLogDto.setCzmkmc(systemLog.czmkMc());
        auditLogDto.setCznr(cznr);
        auditLogDto.setCzsj(CalendarUtil.getCurHMSDate());
        return auditLogDto;
    }

    private void saveLog(final AuditLogDto auditLogDto) {
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

    //?????????????????????
    private void saveTableLog(final AuditLogDto auditLogDto) {
        DchyXmglCzrz dchyXmglCzrz = new DchyXmglCzrz();
        dchyXmglCzrz.setCzrzid(UUIDGenerator.generate18());
        dchyXmglCzrz.setCzrid(auditLogDto.getCzrid());
        dchyXmglCzrz.setCzr(auditLogDto.getCzrmc());
        dchyXmglCzrz.setCzsj(auditLogDto.getCzsj());
        dchyXmglCzrz.setSsmkid(auditLogDto.getSsmkid());
        dchyXmglCzrz.setCzlx(auditLogDto.getCzlx());
        dchyXmglCzrz.setCzlxmc(auditLogDto.getCzlxmc());
        dchyXmglCzrz.setCzmk(auditLogDto.getCzmk());
        dchyXmglCzrz.setCzmkmc(auditLogDto.getCzmkmc());
        dchyXmglCzrz.setCzcs(CommonUtil.formatEmptyValue(auditLogDto.getCznr().get("args")));
        entityMapper.saveOrUpdate(dchyXmglCzrz, dchyXmglCzrz.getCzrzid());
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
//        System.out.println(url);
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
        UserInfo userInfo = UserUtil.getCurrentUser();
        try {
            generateLog(userInfo,method, joinPoint, log, requestMappingValue);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("????????????", e);
        }
        return null;
    }

    private void generateLog(UserInfo userInfo,Method method, JoinPoint joinPoint, AuditLog lzlog, String requestMappingValue) throws IllegalAccessException {
        final AuditLogDto auditLogDto = new AuditLogDto();
        auditLogDto.setCzsj(Calendar.getInstance().getTime());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String ip = request.getRemoteAddr();
        auditLogDto.setIp(ip);
        if (userInfo != null) {
            auditLogDto.setCzrmc(userInfo.getUsername());
        } else {
            auditLogDto.setCzrmc("????????????");
        }
        auditLogDto.setCzmkmc(lzlog.czmkMc());
        auditLogDto.setCzmk(lzlog.czmkCode());
        auditLogDto.setCzlx(lzlog.czlxCode());
        auditLogDto.setCzlxmc(lzlog.czlxMc());
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
        } else if (!(object instanceof String)) {
            saveLogNr(object, xgnrMap, prefix);
        }
    }

    private void saveLogNr(Object object, Map xgnrMap, String prefix) throws IllegalAccessException {
        Table table = object != null ? (Table) object.getClass().getAnnotation(Table.class) : null;

        if (null != table) {
            String tableName = table.name();
            String pk = (String) ReflectUtils.getPkValue(object);
            String czlx = "";
            if (StringUtils.isNotBlank(pk)) {
                if (StringUtils.indexOf(pk, "delete_") == 0) {
                    czlx = "??????";
                    pk = pk.substring(7);
                    Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                    List<Map<String, Object>> compairedMapList = getFields(oldObject, "YZ", new String[]{"chxmid", "mlkid", "gxid"});
                    if (CollectionUtils.isNotEmpty(compairedMapList)) {
                        combine(xgnrMap, compairedMapList, prefix, czlx);
                    }
                } else {
                    Object oldObject = entityMapper.selectByPrimaryKey(object.getClass(), pk);
                    if (oldObject == null) {
                        czlx = "??????";
                        List<Map<String, Object>> compairedMapList = getFields(object, "XZ", new String[]{"chxmid", "mlkid", "gxid"});
                        if (CollectionUtils.isNotEmpty(compairedMapList)) {
                            combine(xgnrMap, compairedMapList, prefix, czlx);
                        }
                    } else if (StringUtils.isBlank(czlx)) {
                        czlx = "??????";
                        List<Map<String, Object>> compairedMapList = compareFields(oldObject, object, new String[]{"chxmid", "mlkid", "gxid"});
                        if (CollectionUtils.isNotEmpty(compairedMapList)) {
                            combine(xgnrMap, compairedMapList, prefix, czlx);
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

//    private void combine(Map<String, List<Object>> map, Map<String, List<Object>> tempMap) {
//        if (MapUtils.isNotEmpty(tempMap)) {
//            if (MapUtils.isEmpty(map)) {
//                map.putAll(tempMap);
//            } else {
//                for (Map.Entry<String, List<Object>> entry : tempMap.entrySet()) {
//                    List<Object> list = entry.getValue();
//                    if (map.containsValue(entry.getKey())) {
//                        map.get(entry.getKey()).addAll(entry.getValue());
//                    } else {
//                        map.put(entry.getKey(), entry.getValue());
//                    }
//                }
//            }
//        }
//    }

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
                List<Map> mapList = getTableMap(tableName);
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
                        compairedMap = Maps.newHashMap();
                        compairedMap.put("ZDMC", zdmc);
                        compairedMap.put("YZ", convertMc(name, o1));
                        compairedMap.put("XZ", convertMc(name, o2));
                        compairedList.add(compairedMap);
                        continue;
                    }
                    if (!o1.equals(o2)) {
                        zdmc = getZdmc(name, mapList);
                        compairedMap = Maps.newHashMap();
                        compairedMap.put("ZDMC", zdmc);
                        compairedMap.put("YZ", convertMc(name, o1));
                        compairedMap.put("XZ", convertMc(name, o2));
                        compairedList.add(compairedMap);
                    }
                }
            }
            return compairedList;
        } catch (Exception e) {
            logger.error("????????????", e);
            return null;
        }
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
            List<Map> mapList = getTableMap(tableName);
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
                zdmc = getZdmc(name, mapList);
                String data = convertMc(name, o1);
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
            logger.error("????????????", e);
            return null;
        }
    }

    private String getZdmc(String name, List<Map> mapList) {
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

    private List<Map> getTableMap(String tableName) {
        List<Map> mapList;
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

    private String convertMc(String zddm, Object ob) {
        if (ob instanceof Date) {
            //return CommonUtil.formatYNRSFM((Date) ob);
        }
        return convertMc(zddm, CommonUtil.formatEmptyValue(ob));
    }

    private String convertMc(String name, String value) {
        if (StringUtils.isNoneBlank(name, value)) {
            Map<String, String> zdlxNameMap = Maps.newHashMap();
            zdlxNameMap.put("cllx", "CLLX");
            zdlxNameMap.put("clsx", "CLSX");
            zdlxNameMap.put("xmxz", "XMXZ");
            zdlxNameMap.put("qjfs", "QJFS");
            zdlxNameMap.put("chdwlx", "CHDWLX");
            zdlxNameMap.put("CLLX", "CLLX");
            zdlxNameMap.put("CLSX", "CLSX");
            zdlxNameMap.put("XMXZ", "XMXZ");
            zdlxNameMap.put("QJFS", "QJFS");
            zdlxNameMap.put("CHDWLX", "CHDWLX");

            Map<String, Class> tableMap = Maps.newHashMap();
            tableMap.put("clsxid", DchyXmglChxmClsx.class);
            tableMap.put("chdwxxid", DchyXmglChxmChdwxx.class);
            tableMap.put("CLSXID", DchyXmglChxmClsx.class);
            tableMap.put("CHDWXXID", DchyXmglChxmChdwxx.class);

            Map<String, List<Class>> tableListMap = Maps.newHashMap();
//            List<Class> list1 = Lists.newArrayList();
//            list1.add(DchyXmglMlk.class);
//            list1.add(DchyXmglChdw.class);
//            tableListMap.put("chdwid", list1);
//            tableListMap.put("CHDWID", list1);

            if (StringUtils.isNotBlank(name) && tableMap.containsKey(name)) {
                Object obj = entityMapper.selectByPrimaryKey(tableMap.get(name), value);
                if (obj instanceof DchyXmglChxmClsx) {
                    name = "clsx";
                    value = ((DchyXmglChxmClsx) obj).getClsx();
                } else if (obj instanceof DchyXmglChxmChdwxx) {
                    value = ((DchyXmglChxmChdwxx) obj).getChdwmc();
                }
            } else if (StringUtils.isNotBlank(name) && tableListMap.containsKey(name)) {
                List<Class> classList = tableListMap.get(name);
                for (Class clazz : classList) {
                    Object obj = entityMapper.selectByPrimaryKey(clazz, value);
                    if (obj instanceof DchyXmglMlk) {
                        value = ((DchyXmglMlk) obj).getDwmc();
                        break;
                    } else if (obj instanceof DchyXmglChdw) {
                        value = ((DchyXmglChdw) obj).getChdwmc();
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(name) && zdlxNameMap.containsKey(name)) {
                DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(zdlxNameMap.get(name), value);
                if (null != dchyXmglZd) {
                    return dchyXmglZd.getMc();
                }
            }
        }
        return value;
    }
}
