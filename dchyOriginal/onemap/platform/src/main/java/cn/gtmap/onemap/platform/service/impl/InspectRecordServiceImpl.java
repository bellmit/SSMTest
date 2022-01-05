package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.FileStoreDao;
import cn.gtmap.onemap.platform.dao.InspectRecordJpaDao;
import cn.gtmap.onemap.platform.dao.ProjectJpaDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.service.InspectRecordService;
import cn.gtmap.onemap.platform.utils.DateUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.security.SecHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  巡查记录业务
 * <p/>
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016/3/18
 */
@Service
public class InspectRecordServiceImpl extends BaseLogger implements InspectRecordService {
    @Autowired
    private InspectRecordJpaDao inspectRecordDao;

    @Autowired
    private ProjectJpaDao projectDao;

    @Autowired
    private FileStoreDao fileStoreDao;

    /**
     * 巡查记录查询
     * @param condition
     * @param pageIndex
     * @param size
     * @return
     */
    @Transactional
    @Override
    public Page<InspectRecord> search(final Map condition, int pageIndex, int size) {
        return inspectRecordDao.findAll(new Specification<InspectRecord>() {
            @Override
            public Predicate toPredicate(Root<InspectRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                DateFormat format = new SimpleDateFormat(Constant.DEFAULT_DATETIME_FORMATE);
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                try {
                    expressions.add(cb.greaterThanOrEqualTo(root.<Date>get("createAt"),format.parse(condition.get("start").toString())));
                    expressions.add(cb.lessThanOrEqualTo(root.<Date>get("createAt"), format.parse(condition.get("end").toString())));
                } catch (ParseException e) {
                    logger.error("时间格式化异常"+e.toString());
                }
                expressions.add(cb.equal(root.<Project>get("project"), projectDao.findByProId(condition.get("proId").toString())));
                if(!SecHelper.isAdmin()){
                    expressions.add(cb.equal(root.<String>get("userId"), condition.get("userId")));
                }
                return predicate;
            }
        }, new PageRequest(pageIndex, size, new Sort(Sort.Direction.DESC, "createAt")));
//        return inspectRecordDao.search(createQueryWhere(condition),new PageRequest(pageIndex, size, new Sort(Sort.Direction.DESC, "createAt")));
    }

    /**
     * 添加或更新巡查记录
     * @param data
     * @return
     */
    @Transactional
    @Override
    public InspectRecord saveOrUpdate(String data, String proId, String type,String limitTime) {
        InspectRecord inspectRecord = JSONObject.parseObject(data, InspectRecord.class);
        if(Days.daysBetween(new DateTime(inspectRecord.getCreateAt()), DateTime.now().hourOfDay().withMaximumValue()).getDays() >Integer.parseInt(limitTime)
                && "save".equals(type)){ //校准时间差，不为同日的极为不合法的时间，不予提交
            throw new RuntimeException("不正确的时间，请校准您使用的计算机时间！");
        }
        Map mapInfo = JSONObject.parseObject(data, Map.class);
        Field[] fields = inspectRecord.getClass().getDeclaredFields();
        Field[] pFields = inspectRecord.getClass().getSuperclass().getDeclaredFields();
        if(fields!=null && fields.length>0){
            for(Field field : (Field[])ArrayUtils.addAll(fields, pFields)){
                mapInfo.remove(field.getName());  //移除所有非项目属性配置字段
            }
            mapInfo.remove("proId");

            inspectRecord.setProperty(JSONObject.toJSONString(mapInfo)); //项目属性存储
        }

        if("save".equalsIgnoreCase(type)){
            if (inspectRecord.getCreateAt() == null)
                inspectRecord.setCreateAt(new Date());
            inspectRecord.setEnabled(true);
            inspectRecord.setUserId(SecHelper.getUserId());
            inspectRecord.setProject(projectDao.findByProId(proId).size()>0?projectDao.findByProId(proId).get(0):null);
            return inspectRecordDao.saveAndFlush(inspectRecord);
        }else if("edit".equalsIgnoreCase(type)){
            Map updateInfo = JSONObject.parseObject(data, Map.class);
            InspectRecord target = findInspectRecordById(inspectRecord.getId());
            Project project = target.getProject();
            updateObject(updateInfo, target);
            target.setProperty(JSONObject.toJSONString(inspectRecord.getProperty()));
            target.setProject(project);
            target.setUpdateTime(new Date());
            return inspectRecordDao.saveAndFlush(target);
        }
        return null;
    }


    /**
     * 保存并推送巡查记录
     * @param data
     * @return
     */
    @Transactional
    @Override
    public Map saveAndSend(String data, String proId,String url){
        Project project = projectDao.findByProId(proId).get(0);
        Map map = JSON.parseObject(data);
        map.put("project",JSON.toJSONString(project));
        InspectRecord inspectRecord = new InspectRecord();
        if (inspectRecord.getCreateAt() == null)
            inspectRecord.setCreateAt(new Date());
        inspectRecord.setEnabled(true);
        inspectRecord.setUserId(SecHelper.getUserId());
        inspectRecord.setProject(projectDao.findByProId(proId).size()>0?projectDao.findByProId(proId).get(0):null);
        inspectRecord.setInspectArea(MapUtils.getString(map,"inspectArea"));
        inspectRecord.setInspector(MapUtils.getString(map, "inspector"));
        inspectRecord.setInspectedUnit(MapUtils.getString(map, "inspectedUnit"));
        inspectRecord.setName(MapUtils.getString(map, "inspectedUnit").concat("  ").concat(MapUtils.getString(map,"createAt")));
        inspectRecord.setOrganId(MapUtils.getString(map,"organId",""));
        inspectRecord.setOrganName(MapUtils.getString(map,"organName",""));
        InspectRecord saved = inspectRecordDao.saveAndFlush(inspectRecord);
//        HttpRequest.post(url, map, "JSON");
        Map postMap = new HashMap();

        postMap.put("colName",project.getProName().concat(DateFormatUtils.format(saved.getCreateAt(),"yyyyMMdd")));
        postMap.put("colId",saved.getId());
        postMap.put("colMapDkid",saved.getId());
        postMap.put("colBusinessType","spts");
        postMap.put("colOwner",MapUtils.getString(map, "inspectedUnit",""));
        postMap.put("colDistrictCode", saved.getOrganId());
        postMap.put("colAssignDepartment",saved.getOrganName());
        return postMap;
    }

    private Object updateObject(Map source, Object target){
        Assert.notEmpty(source,"the param source can not null");
        Assert.notNull(target,"the param target can not null");
        Class clazz = null;
        Object value = null;
        for(Object key : source.keySet()){
            try {
                clazz = PropertyUtils.getPropertyType(target, key.toString());
                value = null;
                DateFormat format = new SimpleDateFormat(DateUtils.DEFAULT_TIME_FORMATE);
                if (clazz != null) {
                    if (String.class == clazz) {
                        value = source.get(key).toString();
                    }else if(Date.class == clazz) {
                        value = format.parse(source.get(key).toString());
                    }else if(boolean.class == clazz){
                        value = new Boolean(source.get(key).toString()).booleanValue();
                    }
                    if (value != null)
                        PropertyUtils.setProperty(target, key.toString(), value);
                }
            } catch (IllegalAccessException e) {
                logger.error(getMessage(""), e.getMessage());
            } catch (InvocationTargetException e) {
                logger.error(getMessage(""), e.getMessage());
            } catch (NoSuchMethodException e) {
                logger.error(getMessage(""), e.getMessage());
            } catch (ParseException e) {
                logger.error(getMessage(""), e.getMessage());
            }
        }
        return target;
    }

    /**
     * 获取巡查信息
     * @param id
     * @return
     */
    @Override
    public InspectRecord findInspectRecordById(String id) {
        return inspectRecordDao.findOne(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteInspectRecordById(String id) {
        try {
            inspectRecordDao.delete(id);
        } catch (Exception e) {
            logger.error(getMessage("record.delete.error", e.getMessage()));
            return false;
        }
        return true;
    }

    /***
     *
     * @param proid
     * @return
     */
    @Override
    public boolean deleteByProid(String proid) {
        List<InspectRecord> list=inspectRecordDao.getByproId(proid);
        inspectRecordDao.deleteInBatch(list);
        return false;
    }

    @Override
    public String getInpectRecordTpl() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(AppConfig.getConfHome()+"omp/inspect/record-form.tpl");
            if(resources.length > 0){
                return  IOUtils.toString(resources[0].getURI(), Constant.UTF_8);
            }
        } catch (IOException e) {
            logger.error("加载模板【record-form.tpl】出现异常:{0}", e.toString());
        }
        return "";
    }

    @Override
    public Map sendInspectRecord(String id) {
        Map map = new HashedMap();
        InspectRecord inspectRecord = findInspectRecordById(id);
        List<FileStore> fileStores = new ArrayList<FileStore>();
        List<String> files = new ArrayList<String>();
        Date start, end = null;
        if (inspectRecord != null){
            start = new DateTime(inspectRecord.getCreateAt()).withTimeAtStartOfDay().toDate();
            end = new DateTime(inspectRecord.getCreateAt()).withTimeAtStartOfDay().plusDays(1).toDate();
            fileStores = fileStoreDao.findByParentIdAndTime(inspectRecord.getProject().getProId(), start, end);
        }

        Map properties = inspectRecord.getProperty();
        inspectRecord.setProperty(null);
        inspectRecord.setProject(null);

        map.put("operator", inspectRecord.getInspector());
        map.put("inspectDate", DateFormatUtils.format(inspectRecord.getCreateAt(),"yyyy-MM-dd HH:mm:dd"));
        map.put("recordId", inspectRecord.getId());
        map.put("leasProId", inspectRecord.getLeasProId());
        map.putAll(properties);

        for (FileStore fileStore : fileStores) {
            files.add(AppConfig.getProperty("omp.url").concat("/file/original/".concat(fileStore.getId())));
        }

        map.put("files", files);

        return map;
    }

    private String createQueryWhere(Map conditon){
        StringBuilder sb = new StringBuilder("");
        if (isNotNull(conditon) && !conditon.isEmpty()) {
            if(conditon.containsKey("start")){
                sb.append(" and t.createat>= to_date('" + MapUtils.getString(conditon, "start")+"', 'yyyy-MM-dd HH24:mi:ss')");
            }

            if(conditon.containsKey("end")){
                sb.append(" and t.createat<= to_date('" + MapUtils.getString(conditon, "end")+"', 'yyyy-MM-dd HH24:mi:ss')");
            }

            if(conditon.containsKey("proId")){
                sb.append(" and p.proid='" + MapUtils.getString(conditon, "proId")+"'");
            }

            if(!SecHelper.isAdmin()){
                sb.append(" and t.userid=" + MapUtils.getString(conditon, "userId"));
            }
        }

        return sb.toString();
    }
}
