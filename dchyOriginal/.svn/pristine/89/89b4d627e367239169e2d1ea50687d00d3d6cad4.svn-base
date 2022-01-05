package cn.gtmap.onemap.platform.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.FileStoreDao;
import cn.gtmap.onemap.platform.dao.InspectRecordJpaDao;
import cn.gtmap.onemap.platform.dao.ProjectCameraRefDao;
import cn.gtmap.onemap.platform.dao.ProjectDao;
import cn.gtmap.onemap.platform.dao.ProjectJpaDao;
import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import cn.gtmap.onemap.platform.entity.video.Preset;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.entity.video.ProjectCameraRef;
import cn.gtmap.onemap.platform.entity.video.Recognition;
import cn.gtmap.onemap.platform.event.FileStoreDeleteEvent;
import cn.gtmap.onemap.platform.service.DBAService;
import cn.gtmap.onemap.platform.service.PresetService;
import cn.gtmap.onemap.platform.service.ProjectService;
import cn.gtmap.onemap.platform.service.QuartzScheduleManager;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import cn.gtmap.onemap.platform.utils.Utils;
import cn.gtmap.onemap.security.SecHelper;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/16 23:53
 */
@Service
public class ProjectServiceImpl extends BaseLogger implements ProjectService {

    @Autowired
    private ProjectJpaDao projectJpaDao;

    @Autowired
    private ProjectDao projectDao;

    @Resource
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FileStoreDao fileStoreDao;

    @Autowired
    private PresetService presetService;

    @Autowired
    private DBAService dbaService;

    @Autowired
    private VideoManager videoManager;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private ProjectCameraRefDao projectCameraRefDao;

    @Autowired
    private InspectRecordJpaDao inspectRecordDao;

    @Autowired
    private TplDao tplDao;

    @Autowired
    private QuartzScheduleManager quartzScheduleManager;

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private enum Tag {
        updatedTime
    }

    @Override
    public Page<Project> getWarningPage(int page, int size, String order,Map condition,int status){
        String where = " PROID IN \n" +
                "\t(SELECT PARENT_ID FROM OMP_FILE_STORE JOIN OMP_CAMERA_RECOG ON OMP_FILE_STORE.ID =OMP_CAMERA_RECOG.ORIGINFILE and OMP_CAMERA_RECOG.STATUS="+status+")";
        String sql="SELECT * FROM omp_project where ";
        if(condition!=null&&condition.size()>0){
            Set<Map.Entry<String, Object>> entries = condition.entrySet();
            int conditionSize = entries.size();
            int i = 0;
            for (Map.Entry<String, Object> entry : entries){
                i++;
                if(i <= conditionSize){
                    where=where+(" and ")+entry.getKey()+" like '%"+entry.getValue()+"%'";
                }
            }
        }
        sql+=where;
        //执行查询
        Pageable pageable = new PageRequest(page, size,new Sort(order));
        return dbaService.searchByPage(sql,pageable,Project.class);
    }

    @Override
    public int getWarningCount(int status){
        String sql = "SELECT * FROM omp_project where PROID IN " +
                        "(SELECT PARENT_ID FROM OMP_FILE_STORE JOIN OMP_CAMERA_RECOG ON OMP_FILE_STORE.ID =OMP_CAMERA_RECOG.ORIGINFILE and OMP_CAMERA_RECOG.STATUS="+status+")";
        List result = dbaService.search(sql);
        return result.size();
    }

    public Page<Recognition> getWarningReco(int page,int size,String proid,int status){
        String sql ="select * from OMP_CAMERA_RECOG A join OMP_FILE_STORE B " +
                "ON A.ID=B.ID AND A.STATUS ="+status+" AND A.PARENT_ID ='"+proid+"'";

        Pageable pageable = new PageRequest(page, size);
        Query query = em.createNativeQuery(sql, Project.class);
        return readPage(query, pageable, sql);
    }

    private Page readPage(Query query, Pageable pageable,String countSql) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        int total = executeCountQuery(countSql);
        List content ;
        if(total>pageable.getOffset()){
            content = query.getResultList();
        }else {
            content = Collections.emptyList();
        }
        return new PageImpl(content, pageable, total);
    }

    private int executeCountQuery(String sql) {
        Query query =em.createNativeQuery(sql, Project.class);
        Assert.notNull(query);
        int total= query.getResultList().size();
        return total;
    }



    /***
     * 保存
     * @param project
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Project saveOrUpdate(Project project, boolean setPreSet) {
        List<String> cameraIds = new ArrayList<String>();
        project.setUpdatedTime(new Date());
        if (isNull(project.getId())) {
            project.setId(UUIDGenerator.generate());
            project.setCreateAt(new Date());
            project.setProId(UUIDGenerator.generate());

            if (isNull(project.getName()))
                project.setName(project.getProName());

            //新增项目 摄像头 预设位关联表 cameraId字段无需再存值（防止字段超长）
            if (isNotNull(project.getCameraId())) {
                cameraIds.addAll(Arrays.asList(project.getCameraId().split(",")));
                //project.setCameraId(null);
            }

            project = projectJpaDao.saveAndFlush(project);

            //添加默认预设位
            boolean hasPreset = AppConfig.getBooleanProperty("project.default.preset", false);
            if (cameraIds.size() > 0 && hasPreset) {
                Preset preset;
                try {
                    for (String cameraId : cameraIds) {
                        preset = presetService.insert(project.getId(), project.getProId(), cameraId);
                        if (setPreSet) {
//                            wsuService.login();
//                            wsuService.setPreset(cameraId,String.valueOf(preset.getPresetNo()),preset.getName());
                        }
                        project.getPresets().add(preset);
                    }
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    logger.error("插入预设位失败",e);
                }
            } else if (cameraIds.size() > 0 && !hasPreset) { //无默认预设位的添加关联关系
                for (String cameraId : cameraIds) {
                    this.insertRefAboutPreset(project.getProId(), cameraId, null);
                }
            }
        } else {
            Project pro = projectJpaDao.findOne(project.getId());
            if (isNotNull(pro)) {
                try {
                    pro = (Project) Utils.copyPropertyForSave(project, pro);
                    pro.setCameraId(null);
                    if(project.getMarkedTime() == null){
                    	pro.setMarkedTime(null);
                    }
                    return projectJpaDao.saveAndFlush(pro);
                } catch (Exception e) {
                    //throw new RuntimeException(e.getLocalizedMessage());
                    logger.error("保存项目失败",e);
                }
            }
        }
        return project;
    }

    /***
     *
     * @param proid
     * @return
     */
    @Override
    public Project getByProid(String proid) {
        return projectJpaDao.findByProId(proid).get(0);
    }

    @Override
    public Project getById(String id){
        return projectJpaDao.findOne(id);
    }

    /**
     * 删除
     *
     * @param proid
     * @return
     */
    @Transactional
    @Override
    public boolean deleteByProid(String proid) {
        try {
            projectDao.delete(proid);
            projectCameraRefDao.deleteByProId(proid);
            ApplicationContextHelper.getContext().publishEvent(new FileStoreDeleteEvent(proid));
        } catch (Exception e) {
            logger.error(getMessage("project.delete.error", e.getMessage()));
            return false;
        }
        return true;
    }

    /***
     * get pros by indexCode
     * @param indexCode
     * @return
     */
    @Override
    public Set<Project> getByCamera(String indexCode) {
        List<ProjectCameraRef> projectCameraRefList = projectCameraRefDao.findByCameraId(indexCode);
        Set<Project> projectSet = new HashSet<Project>();
        //获取监控点关联的项目
        for (ProjectCameraRef projectCameraRef : projectCameraRefList) {
            List<Project> projects = projectJpaDao.findByProId(projectCameraRef.getProId());
            if (isNotNull(projects) && projects.size() > 0) {
                projectSet.add(projects.get(0));
            }
        }
        projectSet.addAll(projectJpaDao.findByCameraId(indexCode));
        for (Project pro : projectSet) {
            if ("panorama0001".equalsIgnoreCase(pro.getProId())) {
                projectSet.remove(pro);
            }
        }
        return projectSet;
    }


    /***
     *  获取分页数据
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Project> getPage(int page, int size, final String ownerId, String order, String orderField, final String xzqdm) {
        boolean flag = false;
        PageRequest pagerequest;
        if (order.equalsIgnoreCase("asc")) {
            pagerequest = new PageRequest(page, size, new Sort(Sort.Direction.ASC, orderField));
        } else {
            pagerequest = new PageRequest(page, size, new Sort(Sort.Direction.DESC, orderField));
        }
        System.out.println("***开始执行查询");
        System.out.println("***xzqdm为"+xzqdm);
        Page<Project> pages = projectJpaDao.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                System.out.println("***ownerId为"+root.<String>get("ownerId"));
                criteriaQuery.where(criteriaBuilder.notEqual(root.<String>get("proId"), "panorama0001"));
                if (!SecHelper.isAdmin()) {
                    if (xzqdm == null) {
                        System.out.println("***代码执行"+"criteriaQuery.where(criteriaBuilder.equal(root.<String>get(\"ownerId\"), ownerId));");
                        criteriaQuery.where(criteriaBuilder.equal(root.<String>get("ownerId"), ownerId));
                    } else {
                        criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.equal(root.<String>get("xzqdm"), xzqdm),
                                criteriaBuilder.gt(criteriaBuilder.length(root.<String>get("xzqdm")), Integer.valueOf(xzqdm.length()))));
                    }
                }
                return null;
            }
        }, pagerequest);
        //项目拍照
        try {
            flag = quartzScheduleManager.isRunning("capture");
        } catch (Exception e) {
            logger.error("获取正在执行的定时调度任务出现异常：{0}", e.getMessage());
        }
        if (flag) {
            for (Project project : pages.getContent()) {
                if (project.getPresets() != null) {
                    for (Preset preset : project.getPresets()) {
                        preset.setEnabled(false);
                    }
                }
            }
        }
        return pages;
    }

    @Override
    public Camera getCameraByProName(String proName){
        List<Project> proList = projectDao.search("PRONAME ='"+proName+"'","PRONAME");
        Project pro =null;
        Camera camera=null;
        if(proList!=null&&proList.size()>0){
            pro = proList.get(0);
            Set<String> cameras = getRefCameras(pro.getProId());
            List <String> strList = new ArrayList<String>(cameras);//B是set型的
            if(cameras.size()>0){
                camera = videoMetadataService.getByIndexCode(strList.get(0));
            }
        }
        return camera;
    }

    /***
     * get location info of all projects
     * @return
     */
    @Override
    public List getLocationInfo(String ownerId) {
        return projectJpaDao.getLocationInfo(ownerId);
    }

    /**
     * @param ownerId
     * @return
     */
    @Override
    public List<Project> getAllProjects(String ownerId, String regionCode) {
        List<Project> list = projectJpaDao.findAll();
        List<Project> ownProjects = new ArrayList<Project>();
        if (SecHelper.isAdmin()) {
            for (Project project : list) {
                if (!project.getProId().equals("panorama0001")) {
                    ownProjects.add(project);
                }
            }
        } else {
            for (Project project : list) {
                if (((!project.getProId().equals("panorama0001")) && project.getOwnerId().equals(ownerId)) || (project.getXzqdm() != null && (project.getXzqdm().length() >= regionCode.length()))) {
                    ownProjects.add(project);
                }
            }
        }
        return ownProjects;
    }

    /**
     * @return
     */
    @Override
    public Workbook getExportExcel(List<Project> projectList) {
        String[] excelHeader = {"项目名称", "关联摄像头名称", "项目类型", "创建时间", "关联地块"};
        int[] excelHeaderWidth = {200, 200, 200, 300};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("项目列表");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // 设置列宽度（像素）
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }
        // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E");
        for (int i = 0; i < projectList.size(); i++) {
            row = sheet.createRow(i + 1);
            Project project = projectList.get(i);
            row.createCell(0).setCellValue(project.getProName() != null ? project.getProName() : "");
            String cameraName = "";
            List<Camera> cameras = videoMetadataService.getAll();
            if (StringUtils.isBlank(project.getCameraId())) {
                List<ProjectCameraRef> projectCameraRefs = projectCameraRefDao.findByProId(project.getProId());
                for (ProjectCameraRef projectCameraRef : projectCameraRefs) {
                    for (Camera camera : cameras) {
                        if (camera.getIndexCode().equals(projectCameraRef.getCameraId())) {
                            cameraName = cameraName.concat(camera.getName()).concat(";");
                        }
                    }
                }
            } else {
                for (Camera camera : cameras) {
                    if (camera.getIndexCode().equals(project.getCameraId())) {
                        cameraName = cameraName.concat(camera.getName()).concat(";");
                    }
                }
            }
            row.createCell(1).setCellValue(cameraName);
            row.createCell(2).setCellValue(project.getProType() != null ? project.getProType() : "");
            row.createCell(3).setCellValue(project.getCreateAt() != null ? format.format(project.getCreateAt()) : "");
            row.createCell(4).setCellValue(project.getAssociateBlockName() != null ? project.getAssociateBlockName() : "");
        }
        return wb;
    }

    /***
     *
     * @param page
     * @param size
     * @param condition
     * @return
     */
    @Override
    public Page<Project> search(int page, int size, Map condition, boolean withXzq,String order,String orderField) {
        String where = createQueryWhere(condition, withXzq);
        if (order.equalsIgnoreCase("asc")) {
            return projectDao.search(where, new PageRequest(page, size, new Sort(Sort.Direction.ASC, orderField)));
        } else {
            return projectDao.search(where, new PageRequest(page, size, new Sort(Sort.Direction.DESC, orderField)));
        }
    }

    @Override
    public List<Project> search(Map condition, boolean withXzq) {
        String where = createQueryWhere(condition, withXzq);
        return projectDao.search(where, "updatedTime desc");
    }

    /***
     * 获取该项目关联的照片记录 并按照年度分组
     * @param proid
     * @param condition
     * @return
     */
    @Override
    public List<Map> getImgRecords(final String proid, final Map condition) {
        try {
            final List<FileStore> fileStores = fileStoreDao.findAll(new Specification<FileStore>() {
                @Override
                public Predicate toPredicate(Root<FileStore> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate predicate = criteriaBuilder.conjunction();
                    List<Expression<Boolean>> expressions = predicate.getExpressions();
                    Date date = null;
                    expressions.add(criteriaBuilder.equal(root.<String>get("parentId"), proid));
                    DateFormat format = new SimpleDateFormat(Constant.DEFAULT_DATE_FORMATE);
                    //包含年份和月份检索
                    if (condition.containsKey("year") && condition.containsKey("month")) {
                        try {
                            date = format.parse(condition.get("year").toString().concat("-").concat(condition.get("month").toString()).concat("-01"));
                        } catch (ParseException e) {
                            logger.error("parse date with error:{}", e.getLocalizedMessage());
                        }
                        if (date != null) {
                            Date maxMonthDate = new DateTime(date).dayOfMonth().withMaximumValue().toDate();
                            expressions.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createTime"), date));
                            expressions.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createTime"), maxMonthDate));
                        }
                    } else {  //默认选中年数据
                        if (condition.containsKey("year")) {
                            try {
                                date = format.parse(condition.get("year").toString().concat("-01-01"));
                            } catch (ParseException e) {
                                logger.error("parse date with error:{}", e.getLocalizedMessage());
                            }
                        } else {
                            date = new Date();
                        }
                        Date minYearDate = new DateTime(date).dayOfYear().withMinimumValue().toDate();    //年度最小天
                        Date maxYearDate = new DateTime(date).dayOfYear().withMaximumValue().toDate();    //年度最大天
                        expressions.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createTime"), minYearDate));
                        expressions.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createTime"), maxYearDate));
                    }
                    return predicate;
                }
            }, new Sort(Sort.Direction.DESC, "createTime"));

            List<Map> result = new ArrayList<Map>();
            Map<String, List> yearMap = new HashMap<String, List>();
            //标记前一个时间串，用来去重
            DateTime preTime = null;

            for (FileStore fileStore : fileStores) {
                Date createTime = fileStore.getCreateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                DateTime dateTime = new DateTime(sdf.format(createTime));
                //如果前后相差天数一样(即为0) 就不存储 避免重复的天数数据出现
                if (preTime != null && Days.daysBetween(dateTime, preTime).getDays() == 0) {
                    preTime = dateTime;
                    continue;
                }
                preTime = dateTime;
                String year = String.valueOf(dateTime.getYear());
                List list = new ArrayList(10);
                if (yearMap.containsKey(year)) {
                    list = yearMap.get(year);
                }
                list.add(fileStore);
                yearMap.put(year, list);
            }
            for (Map.Entry<String, List> entry : yearMap.entrySet()) {
                Map tmp = new HashMap();
                List<FileStore> list = entry.getValue();
                List<Map> newList = new ArrayList<Map>();
                Map<String, List> monthMap = new HashMap<String, List>();
                for (FileStore fileStore : list) {
                    DateTime dateTime = new DateTime(fileStore.getCreateTime());
                    String month = String.valueOf(dateTime.getMonthOfYear());
                    List monthDataList;
                    if (monthMap.containsKey(month)) {
                        monthDataList = monthMap.get(month);
                        monthDataList.add(fileStore);
                        monthMap.put(month, monthDataList);
                    } else {
                        monthDataList = new ArrayList();
                        monthDataList.add(fileStore);
                        monthMap.put(month, monthDataList);
                    }
                }
                for (Map.Entry<String, List> monthEntry : monthMap.entrySet()) {
                    Map tmpMonth = new HashMap();
                    List listMonth = monthEntry.getValue();
                    Collections.sort(list, new Comparator<FileStore>() {
                        @Override
                        public int compare(FileStore o, FileStore o2) {
                            return o.getCreateTime().compareTo(o2.getCreateTime());
                        }
                    });
                    tmpMonth.put("month", monthEntry.getKey());
                    tmpMonth.put("monthData", listMonth);
                    newList.add(tmpMonth);
                }
                Collections.sort(newList, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        return Integer.parseInt(o1.get("month").toString()) - (Integer.parseInt(o2.get("month").toString()));
                    }
                });

                tmp.put("year", entry.getKey());
                tmp.put("yearData", newList);
                result.add(tmp);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     *
     * @param proid
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Map> getRecords(final String proid, final Date startTime, final Date endTime) {
        List<Map> list = new ArrayList<Map>();
        List<InspectRecord> inspectRecords = inspectRecordDao.findAll(new Specification<InspectRecord>() {
            @Override
            public Predicate toPredicate(Root<InspectRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("project").get("proId").as(String.class), proid),
                        cb.between(root.get("createAt").as(Date.class), startTime, endTime));
            }
        }, new Sort(Sort.Direction.DESC, "createAt"));
        List<FileStore> fileStores = fileStoreDao.findAll(new Specification<FileStore>() {
            @Override
            public Predicate toPredicate(Root<FileStore> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("parentId").as(String.class), proid), cb.between(root.get("createTime").as(Date.class), startTime, endTime));
            }
        }, new Sort(Sort.Direction.DESC, "createTime"));

        DateTime startDate = new DateTime(startTime);
        DateTime endDate = new DateTime(endTime);
        int period = Days.daysBetween(startDate, endDate).getDays();
        for (int i = 0; i <= period; i++) {
            final String date = endDate.minusDays(i).toString(Constant.DEFAULT_DATE_FORMATE);
            Map map = new HashMap();
            boolean isToday = DateTime.now().toString(Constant.DEFAULT_DATE_FORMATE).equals(date);
            if (isToday) map.put("isToday", isToday);
            if (i == 0 || isToday)
                map.put("isShow", true);
            else
                map.put("isShow", false);
            map.put("date", date);
            map.put("images", CollectionUtils.select(fileStores, new org.apache.commons.collections.Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    return date.equals(formatter.format(((FileStore) object).getCreateTime()));
                }
            }));
            map.put("inspects", CollectionUtils.select(inspectRecords, new org.apache.commons.collections.Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    return date.equals(formatter.format(((InspectRecord) object).getCreateAt()));
                }
            }));
            list.add(map);
        }
        return list;
    }

    /**
     * 获取一定时间内的所有项目的拍照记录
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    @Override
    public List<FileStore> getImgRecordsByTimeSpan(final Date start, final Date end) {
        // 找出一段时间内的所有 file 记录
        List<FileStore> ret = Lists.newArrayList();
        List<FileStore> fileStores = fileStoreDao.findAll(new Specification<FileStore>() {
            @Override
            public Predicate toPredicate(Root<FileStore> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get("createTime").as(Date.class), start, end);
            }
        }, new Sort(Sort.Direction.DESC, "createTime"));
        for (FileStore fileStore : fileStores) {
            String parentId = fileStore.getParentId();
            List<Project> projects = projectJpaDao.findByProId(parentId);
            if (isNotNull(projects) && projects.size() > 0) {
                ret.add(fileStore);
            }
        }
        return ret;
    }

    /**
     * 获取一定时间内的某个项目的拍照记录
     *
     * @param proId
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<FileStore> getImgRecordsByTimeSpanAndPro(final String proId, final Date start, final Date end) {
        List<FileStore> fileStores = fileStoreDao.findAll(new Specification<FileStore>() {
            @Override
            public Predicate toPredicate(Root<FileStore> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("parentId").as(String.class), proId),
                        cb.between(root.get("createAt").as(Date.class), start, end));
            }
        }, new Sort(Sort.Direction.DESC, "createTime"));
        return fileStores;
    }

    /***
     *
     * @param map
     * @return
     */
    private String createQueryWhere(Map map, boolean withXzq) {
        StringBuilder sb = new StringBuilder("proid <> 'panorama0001' ");
        if (isNotNull(map) && !map.isEmpty()) {
            if (map.containsKey("status")) {
                sb.append(" and status=".concat(String.valueOf(MapUtils.getInteger(map, "status"))));
            } else if (map.containsKey("proType")) {
                sb.append(" and  protype='".concat(MapUtils.getString(map, "proType")) + "'");
            } else if (map.containsKey("exType")) {
                sb.append(" and extype='".concat(MapUtils.getString(map, "exType")) + "'");
            }
            if (map.containsKey("proName")) {
                sb.append(" and proName like '%".concat(MapUtils.getString(map, "proName")).concat("%'"));
            }
            if (map.containsKey("year")) {
                sb.append(" and year like '%".concat(MapUtils.getString(map, "year")).concat("%'"));
            }

            if (withXzq) {
                if (map.containsKey("regionCode")) {
                    String regionCode = MapUtils.getString(map, "regionCode");
                    if (regionCode.equals(MapUtils.getString(map, "xzqdm"))) {
                        sb.append(" and ownerId = '".concat(MapUtils.getString(map, "ownerId")).concat("'"));
                    } else {
                        sb.append(" and xzqdm = '".concat(regionCode).concat("'"));
                    }
                } else if (map.containsKey("xzqdm")) {
                    sb.append("and ( length(xzqdm) > '".concat(String.valueOf(MapUtils.getString(map, "xzqdm").length())).concat("' or ownerId = '").concat(MapUtils.getString(map, "ownerId")).concat("')"));
                }
            } else if (map.containsKey("ownerId") && !SecHelper.isAdmin()) {
                sb.append(" and ownerId = '".concat(MapUtils.getString(map, "ownerId")).concat("'"));
            }
        }
        if (StringUtils.isNotBlank(sb.toString())) {
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public List getRecordsDistinctYear(String proid) {
        return fileStoreDao.findDistinctYear(proid);
    }

    /**
     * 获取项目关联的照片记录
     * 并删除记录及照片文件
     *
     * @param proid
     * @param fileStoreIds
     */
    @Transactional
    @Override
    public void removeProjectRecord(String proid, String[] fileStoreIds) {
        List<FileStore> fileStores = null;
        File file = null;
        try {
            fileStores = fileStoreDao.findByParentIdAndId(proid, Arrays.asList(fileStoreIds));
            if (fileStores.size() > 0) {
                //1.删除项目照片记录，异常事务回滚 2.删除项目的照片文件
                fileStoreDao.delete(fileStores);
                for (FileStore fileStore : fileStores) {
                    file = new File(fileStore.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    file = null;
                }
            }
        } catch (Exception e) {
            logger.error("删除项目关联照片记录存在异常信息{0}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取具有预设位信息的项目
     *
     * @param condition
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map getNormalProject(final Map condition, int page, int size) {
        Map data = new HashedMap();
        List<Map> content = new ArrayList<Map>();
        Page<Project> projects = projectJpaDao.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                root.join(root.getModel().getSet("presets"), JoinType.INNER);
                Predicate predicate = criteriaBuilder.conjunction();
                List<Predicate> list = new ArrayList<Predicate>();
                List<Predicate> list2 = new ArrayList<Predicate>();
                predicate.equals("1 = 1");

                if (condition.containsKey("proName") && isNotNull(condition.get("proName"))) {
                    Predicate predicate1 = criteriaBuilder.like(root.<String>get("proName"), "%" + condition.get("proName").toString() + "%");
                    list.add(predicate1);
                }
                if (condition.containsKey("cameraName") && isNotNull(condition.get("cameraName"))) {
                    List<Camera> devices = videoMetadataService.getByCameraName(condition.get("cameraName").toString());
                    List<String> ids = new ArrayList<String>();
                    for (Camera item : devices) {
                        ids.add(item.getIndexCode());
                    }
                    if (ids.size() == 0) {
                        ids.add("");
                    }
                    list2.add(criteriaBuilder.and(root.<String>get("cameraId").in(ids)));
                    //从关联关系中查找有预设位的项目
                    List<ProjectCameraRef> projectCameraRefs = projectCameraRefDao.findByCameraIdList(ids);
                    if (projectCameraRefs.size() > 0) {
                        List<String> projectIds = new ArrayList<String>();
                        for (ProjectCameraRef projectCameraRef : projectCameraRefs) {
                            if (isNotNull(projectCameraRef.getPresetId())) {
                                projectIds.add(projectCameraRef.getProId());
                            }
                        }
                        list2.add(criteriaBuilder.or(root.<String>get("proId").in(projectIds)));
                    }
                }
                if (list.size() > 0 && list2.size() > 0) {
                    Predicate[] p = new Predicate[list.size()];
                    Predicate[] p2 = new Predicate[list2.size()];
                    Predicate predicate1 = criteriaBuilder.and(list.toArray(p));
                    Predicate predicate2 = criteriaBuilder.or(list2.toArray(p2));
                    criteriaQuery.where(predicate, predicate1, predicate2, criteriaBuilder.notEqual(root.<String>get("proId"), "panorama0001")).distinct(true);
                } else if (list.size() > 0) {
                    Predicate[] p = new Predicate[list.size()];
                    Predicate predicate1 = criteriaBuilder.and(list.toArray(p));
                    criteriaQuery.where(predicate, predicate1, criteriaBuilder.notEqual(root.<String>get("proId"), "panorama0001")).distinct(true);
                } else if (list2.size() > 0) {
                    Predicate[] p2 = new Predicate[list2.size()];
                    Predicate predicate2 = criteriaBuilder.or(list2.toArray(p2));
                    criteriaQuery.where(predicate, predicate2, criteriaBuilder.notEqual(root.<String>get("proId"), "panorama0001")).distinct(true);
                } else {
                    criteriaQuery.where(predicate, criteriaBuilder.notEqual(root.<String>get("proId"), "panorama0001")).distinct(true);
                }
                return criteriaQuery.getRestriction();
            }
        }, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createAt")));

        if (projects != null) {
            data = JSONObject.parseObject(JSONObject.toJSONString(projects), Map.class);
            if (data.containsKey("content")) {
                for (Map item : (List<Map>) data.get("content")) {
                    Map proMap = new HashedMap();
                    List<String> presets = new ArrayList<String>();
                    StringBuffer cameraNames = new StringBuffer();  //探头名称集合
                    StringBuffer cameraIds = new StringBuffer();

                    proMap.put("presets", presets);
                    proMap.put("proId", item.get("proId"));
                    proMap.put("proName", item.get("proName"));

                    Iterator<Map> iterator = ((List<Map>) item.get("presets")).iterator();
                    while (iterator.hasNext()) {
                        Map map = iterator.next();
                        presets.add(map.get("presetNo").toString());
                        if (!item.containsKey("cameraId")) {
                            Camera info = videoMetadataService.getByIndexCode(map.get("indexCode").toString());
                            if (isNotNull(info)) {
                                cameraNames.append(info.getName());
                                cameraIds.append(info.getIndexCode());
                            } else {
                                cameraNames.append("");
                                cameraIds.append("");
                            }

                        }
                        if (iterator.hasNext()) {
                            cameraNames.append(",");
                            cameraIds.append(",");
                        }
                    }
                    if (item.containsKey("cameraId")) {
                        Camera info = videoMetadataService.getByIndexCode(item.get("cameraId").toString());
                        if (isNotNull(info)) {
                            proMap.put("cameraName", info.getName());
                            proMap.put("cameraId", info.getIndexCode());
                        } else {
                            proMap.put("cameraName", "");
                            proMap.put("cameraId", "");
                        }
                    } else {
                        proMap.put("cameraName", cameraNames.toString());
                        proMap.put("cameraId", cameraIds.toString());
                    }

                    content.add(proMap);
                }
            }
        }
        data.put("content", content);
        return data;
    }

    @Override
    public Set<String> getRefCameras(String proId) {
        Set<String> indexCodes = new HashSet<String>();
        List<ProjectCameraRef> projectCameraRefs = projectCameraRefDao.findByProId(proId);
        //获取所有关联的摄像头的indexCode
        for (ProjectCameraRef projectCameraRef : projectCameraRefs) {
            indexCodes.add(projectCameraRef.getCameraId());
        }
        return indexCodes;
    }

    /**
     * 插入关联关系
     *
     * @param proId
     * @param indexCode
     * @param presetId
     * @return
     */
    @Override
    public boolean insertRefAboutPreset(String proId, String indexCode, String presetId) {
        ProjectCameraRef ref;
        try {
            List<ProjectCameraRef> refs = projectCameraRefDao.findByProIdAndCameraIdAndPresetId(proId, indexCode, presetId);
            if (refs.size() == 0) {
                ref = new ProjectCameraRef();
                ref.setName(proId);
                ref.setProId(proId);
                ref.setPresetId(presetId);
                ref.setCameraId(indexCode);
                ref.setCreateAt(new Date());
            } else {
                ref = refs.get(0);
                ref.setPresetId(presetId);
            }

            projectCameraRefDao.save(ref);
        } catch (Exception e) {
            logger.error("insert/update ref error:" + e.toString());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return true;
    }

    /**
     * 删除关联关系
     *
     * @param proId
     * @param indexCode
     * @param presetId
     * @return
     */
    @Override
    public boolean delRefAboutPreset(String proId, String indexCode, String presetId) {
        try {
            projectCameraRefDao.deletePresetRef(proId, indexCode, presetId);
        } catch (Exception e) {
            logger.error("delete ref error:" + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public List<Preset> addProCameras(String id, String proId, String[] cameras) {
        List<Preset> presets = new ArrayList<Preset>();
        //添加默认预设位
        boolean hasPreset = AppConfig.getBooleanProperty("project.default.preset", false);
        if (cameras.length > 0 && hasPreset) {
            Preset preset;
            try {
                for (String indexCode : cameras) {
                    preset = presetService.insert(id, proId, indexCode);
                    presets.add(preset);
                    this.insertRefAboutPreset(proId, indexCode, preset.getId());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (cameras.length > 0 && !hasPreset) { //无默认预设位的添加关联关系
            for (String cameraId : cameras) {
                this.insertRefAboutPreset(proId, cameraId, null);
            }
        }

        return presets;
    }

    /**
     * ps:删除关系之前要删除所有的关联项目的预设位信息
     *
     * @param id
     * @param proId
     * @param indexCode
     * @return
     */
    @Override
    public boolean deleteProCamera(String id, String proId, String indexCode) {
        try {
            for (Preset preset : presetService.find(id, indexCode)) {
                presetService.delete(preset);
            }
            projectCameraRefDao.deleteByProIdAndCameraId(proId, indexCode);
        } catch (Exception e) {
            logger.error(getMessage("project.camera.delete.error", e.toString()));
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<Camera> getCamerasBySpecialProtype(String type){
        return projectJpaDao.getCamerasBySpecialProtype(type);
    }

    /**
     * 获取所有存在的行政区单位信息
     */
    @Override
    public List getOrganList(String xzqdm) {
        try {
            List<Map> organs = tplDao.getOrganConfig();
            if(StringUtils.isBlank(xzqdm)){
            	return organs;
            }
            List<Map> itemList = new ArrayList<Map>();
            List<String> list = projectJpaDao.findXzqdms();
            for (Map organ : organs) {
                Map<String, String> map = new HashMap<String, String>();
                String organRegion = MapUtils.getString(organ, "regionCode");
                if (organRegion != null && list.contains(organRegion)) {
                    if (xzqdm.length() < organRegion.length() || xzqdm.equals(organRegion)) {
                        map.put("organName", MapUtils.getString(organ, "organName"));
                        map.put("regionCode", organRegion);
                        itemList.add(map);
                    }
                }
            }
            return itemList;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return new ArrayList();
    }
}
