package cn.gtmap.onemap.platform.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import cn.gtmap.onemap.platform.entity.video.OperationLog;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.InspectRecordService;
import cn.gtmap.onemap.platform.service.OperateLoggerService;
import cn.gtmap.onemap.platform.service.ProjectService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.DateUtils;
import cn.gtmap.onemap.platform.utils.HttpRequestUtils;
import cn.gtmap.onemap.security.SecHelper;

import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;

/**
 * 项目模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/11/24 11:13
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private InspectRecordService inspectRecordService;

    @Autowired
    private OperateLoggerService operateLoggerService;

    @Autowired
    private SysUserService sysUserService;


    /***
     * 获取所有项目的proid以及项目位置信息
     * @return
     */
    @RequestMapping(value = "/loc/all")
    @ResponseBody
    public List getAllLocInfo(@RequestParam(value = "ownerId") String ownerId) {
        try {
            return projectService.getLocationInfo(ownerId);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/getWarnProCount")
    @ResponseBody
    public int getWarnProCount() {
        try {
            return projectService.getWarningCount(1);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * 根据indexcode 找出关联的项目信息
     * @param indexCode
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Set<Project> getList(@RequestParam(value = "indexCode") String indexCode) {
        try {
            return projectService.getByCamera(indexCode);
        } catch (Exception e) {
            //throw new RuntimeException(e.getLocalizedMessage());
            return new HashSet<>();
        }
    }

    /***
     * 获取单个项目信息
     * @param proid
     * @return
     */
    @RequestMapping(value = "/get/{proid}")
    @ResponseBody
    public Project get(@PathVariable(value = "proid") String proid) {
        try {
            return projectService.getByProid(proid);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
    /***
     * 获取分页数据
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/page")
    @ResponseBody
    public Map getPage(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam String ownerId,
                       @RequestParam(defaultValue = "desc") String order,
                       @RequestParam(defaultValue = "updatedTime") String orderField,
                       @RequestParam(required = false) String showSubordinate) {//下级
        Map result = new HashMap();
        try {
            Page pages;
            pages = projectService.getPage(page, size, ownerId, order, orderField,
                    StringUtils.isNotBlank(showSubordinate) ? sysUserService.getRegionCodeByUserId(ownerId) : null);
            result.put("pageCount", pages.getTotalPages());
            result.put("total", pages.getTotalElements());
            result.put("content", pages.getContent());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }



    @RequestMapping(value = "/warningPage")
    @ResponseBody
    public Map getWarningPage(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "desc") String order,
                       @RequestParam(defaultValue = "0") int status
                      )
    {

        Map result = new HashMap();
        try {
            Page pages;
            Map conditions = new HashMap();
            pages = projectService.getWarningPage(page,size,order,conditions,status);
            result.put("pageCount", pages.getTotalPages());
            result.put("total", pages.getTotalElements());
            result.put("content", pages.getContent());

        } catch (Exception e) {
             throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /***
     * 保存项目信息
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Project save(Project project,
                        @RequestParam String ownerId,
                        @RequestParam(required = false) boolean xzqdm,
                        @RequestParam(required = false) String xzqdmSelect,
                        @RequestParam(required = false) String autoCreate) {
    	boolean showXzq = AppConfig.getBooleanProperty("project.create.showxzq", false);
    	if(showXzq){
    		project.setXzqdm(xzqdmSelect);
    	}else{
    		if (xzqdm) {
    			try {
    				String userXzqdm = sysUserService.getRegionCodeByUserId(ownerId);
    				project.setXzqdm(userXzqdm);
    			} catch (Exception e) {
    				logger.error(e.getLocalizedMessage());
    			}
    		}
    	}
    	project.setOwnerId(ownerId);
    	
        return projectService.saveOrUpdate(project, StringUtils.isNotBlank(autoCreate));
    }

    /**
     * 删除项目
     *
     * @param proid
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String proid) {
        return result(projectService.deleteByProid(proid));
    }
    
    /**
     * 描述：是否标记重点项目
     * @author 卜祥东
     * 2019年3月6日 下午3:48:32
     * @param proid
     * @param isMarked 是否标记
     * @return
     */
    @RequestMapping(value = "/marked")
    @ResponseBody
    public Object marked(String proid,boolean isMarked) {
    	try {
        	Project projectTemp = projectService.getByProid(proid);
        	if(projectTemp != null){
        		if(isMarked){
        			projectTemp.setMarkedTime(new Date());
        		}else{
        			projectTemp.setMarkedTime(null);
        		}
        		projectService.saveOrUpdate(projectTemp, false);
        		return result(true);
        	}
        } catch (Exception e) {
             throw new RuntimeException(e.getLocalizedMessage());
        }
    	return result(false);
    }
    
    

    /**
     * 对一个存在的项目，可以添加缓冲的新监控点
     *
     * @param id
     * @param proId
     * @param indexCodes
     * @return
     */
    @RequestMapping("/cameras/add")
    @ResponseBody
    public Object addCameras(@RequestParam(defaultValue = "") String id,
                             @RequestParam(defaultValue = "") String proId,
                             @RequestParam(defaultValue = "") String indexCodes) {
        return projectService.addProCameras(id, proId, indexCodes.split(","));
    }



    /**
     * 删除一个项目存在的摄像头
     *
     * @param id
     * @param proId
     * @param indexCode
     * @return
     */
    @RequestMapping("/camera/delete")
    @ResponseBody
    public Object deleteCamera(@RequestParam(defaultValue = "") String id,
                               @RequestParam(defaultValue = "") String proId,
                               @RequestParam(defaultValue = "") String indexCode) {
        try {
            return result(projectService.deleteProCamera(id, proId, indexCode));
        } catch (Exception e) {
            return new JSONMessageException(e.toString());
        }
    }


    /**
     * 获取指定项目类型下所有摄像头
     * @param protype
     * @return
     */
    @RequestMapping("/getCamsBySpecPro")
    @ResponseBody
    public Object getCamerasBySpecialProtype(@RequestParam String protype){
        return projectService.getCamerasBySpecialProtype(protype);
    }


    /***
     * 分页查询
     *
     * @param page
     * @param size
     * @param proType
     * @param status
     * @param exType
     * @return
     */
    @RequestMapping(value = "/search")
    @ResponseBody
    public Map search(@RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false) String proType,
                      @RequestParam(required = false) String proName,
                      @RequestParam(required = false) String year,
                      @RequestParam(required = false) String status,
                      @RequestParam(required = false) String exType,
                      @RequestParam(required = false) String ownerId,
                      @RequestParam(required = false) String regionCode,
                      @RequestParam(required = false) String withXzq,
                      @RequestParam(defaultValue = "desc") String order,
                      @RequestParam(defaultValue = "markedtime desc nulls last,updatedTime") String orderField) {
        Map result = new HashMap();
        try {
            Map condition = new HashMap();
            if (StringUtils.isNotBlank(proType))
                condition.put("proType", proType);
            if (StringUtils.isNotBlank(status))
                condition.put("status", Integer.valueOf(status));
            if (StringUtils.isNotBlank(exType))
                condition.put("exType", exType);
            if (StringUtils.isNotBlank(proName))
                condition.put("proName", proName);
            if (StringUtils.isNotBlank(year)) {
                condition.put("year", year);
            }
//            if (StringUtils.isNotBlank(ownerId)) {
//                condition.put("ownerId", ownerId);
//                try {
//                    String xzqdm = sysUserService.getRegionCodeByUserId(ownerId);
//                    if (StringUtils.isNotBlank(xzqdm)) {
//                        condition.put("xzqdm", xzqdm);
//                    }
//                } catch (Exception e) {
//                    logger.warn(e.getLocalizedMessage());
//                }
//            }
            if (StringUtils.isNotBlank(regionCode))
                condition.put("regionCode", regionCode);
            Page pages = projectService.search(page, size, condition, StringUtils.isNotBlank(withXzq),order,orderField);
            result.putAll(condition);
            result.put("pageCount", pages.getTotalPages());
            result.put("total", pages.getTotalElements());
            result.put("content", pages.getContent());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 查询所有符合条件的项目
     *
     * @param proType
     * @param proName
     * @param year
     * @param status
     * @param exType
     * @param ownerId
     * @param regionCode
     * @param withXzq
     * @return
     */
    @RequestMapping(value = "/searchAll")
    @ResponseBody
    public List searchAll(@RequestParam(required = false) String proType,
                          @RequestParam(required = false) String proName,
                          @RequestParam(required = false) String year,
                          @RequestParam(required = false) String status,
                          @RequestParam(required = false) String exType,
                          @RequestParam(required = false) String ownerId,
                          @RequestParam(required = false) String regionCode,
                          @RequestParam(required = false) String withXzq) {
        try {
            Map condition = new HashMap();
            if (StringUtils.isNotBlank(proType))
                condition.put("proType", proType);
            if (StringUtils.isNotBlank(status))
                condition.put("status", Integer.valueOf(status));
            if (StringUtils.isNotBlank(exType))
                condition.put("exType", exType);
            if (StringUtils.isNotBlank(proName))
                condition.put("proName", proName);
            if (StringUtils.isNotBlank(year)) {
                condition.put("year", year);
            }
            if (StringUtils.isNotBlank(ownerId)) {
                condition.put("ownerId", ownerId);
                try {
                    String xzqdm = sysUserService.getRegionCodeByUserId(ownerId);
                    if (StringUtils.isNotBlank(xzqdm)) {
                        condition.put("xzqdm", xzqdm);
                    }
                } catch (Exception e) {
                    logger.warn(e.getLocalizedMessage());
                }
            }
            if (StringUtils.isNotBlank(regionCode))
                condition.put("regionCode", regionCode);
            return projectService.search(condition, StringUtils.isNotBlank(withXzq));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * 获取某个项目的所有照片记录 并按年度分组
     *
     * @param proid
     * @return
     */
    @RequestMapping(value = "/records/all")
    @ResponseBody
    public Map records(@RequestParam String proid,
                       @RequestParam(defaultValue = "{}") String condition) {
        Map result = new HashMap();
        try {
            Project project = projectService.getByProid(proid);
            List<Map> records = new ArrayList<Map>();
            if (isNotNull(project)) {
                records = projectService.getImgRecords(proid, JSONObject.parseObject(condition, Map.class));
                result.put("allYear", projectService.getRecordsDistinctYear(proid));
            }
            result.put("project", project);
            result.put("records", records);
        } catch (Exception e) {
            logger.error("获取项目记录异常: {}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /***
     * 获取某段时间内某个项目的相关记录(照片以及巡查记录)
     *
     * @param proid     项目id
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return
     */
    @RequestMapping(value = "/records/{proid}")
    @ResponseBody
    public List<Map> getRecords(@PathVariable String proid,
                                @RequestParam(value = "startTime") Date startTime,
                                @RequestParam(value = "endTime") Date endTime) {
        try {
            return projectService.getRecords(proid, startTime, endTime);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取一定时间内某个项目的 照片记录
     * @param proid
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/records/files")
    @ResponseBody
    public List<FileStore> getFiles(@RequestParam String proid,
                                    @RequestParam(value = "startTime") Date startTime,
                                    @RequestParam(value = "endTime") Date endTime) {
        try {
            return projectService.getImgRecordsByTimeSpanAndPro(proid, startTime, endTime);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 删除项目的照片记录
     *
     * @param proid
     * @param fileIds
     * @return
     */
    @RequestMapping("/record/remove")
    @ResponseBody
    public Map recordRemove(@RequestParam(required = true, defaultValue = "") String proid,
                            @RequestParam(required = true, defaultValue = "") String fileIds) {
        try {
            projectService.removeProjectRecord(proid, fileIds.split(","));
        } catch (Exception e) {
            return result(false);
        }

        return result(true);
    }


    /***
     * 手动上传图片记录
     *
     * @param proid
     * @param model
     * @return
     */
    @RequestMapping(value = "/upload/manual")
    public String upload(@RequestParam(required = true) String proid, @RequestParam(required = false) String picid, Model model) {
        model.addAttribute("proid", proid);
        model.addAttribute("picid", picid);
        return "geo/fileUpload";
    }

    /**
     * 获取项目巡查记录
     *
     * @return
     */
    @RequestMapping(value = "/inspect/record/{proId}")
    @ResponseBody
    public Map getCurrentMonthRecords(@PathVariable String proId, @RequestParam int page,
                                      @RequestParam int size, HttpServletRequest request) {
        Map info = new HashedMap();
        Map condtion = HttpRequestUtils.getRequestValues(request);
        condtion.put("start", DateUtils.formatDateTime(DateUtils.getFistDayCurrentMonth(), "yyyy-MM-dd HH:mm:ss"));
        condtion.put("end", DateUtils.formatDateTime(DateUtils.getLastDayCurrentMonth(), "yyyy-MM-dd HH:mm:ss"));
        condtion.put("proId", proId);
        condtion.put("userId", SecHelper.getUserId());
        logger.info("proId:" + proId + " userId:" + SecHelper.getUserId() + " page:" + page + " size:" + size);

        try {
            Page<InspectRecord> records = inspectRecordService.search(condtion, page - 1, size);
            if (records.getTotalElements() > 0) {
                if (!DateUtils.isLessOrEqualToday(records.getContent().get(0).getCreateAt())) {
                    info.put("showBtn", false);
                } else {
                    info.put("showBtn", true);
                }
            }

            if (SecHelper.getSession() != null) {
                info.put("userName", SecHelper.getSession().getUser().getViewName());
            } else {
                info.put("showBtn", false);
                info.put("userName", "");
            }

            info.put("result", records);
            info.put("page", page);
            info.put("size", size);
            info.put("success", true);
        } catch (Exception e) {
            info.put("success", false);
            logger.error(getMessage("record.query.error", e.getMessage()));
        }
        return info;
    }

    /**
     * 保存巡查记录
     *
     * @param data
     * @param proId
     * @param type
     * @return
     */
    @RequestMapping("/inspect/record/save")
    @ResponseBody
    public Object saveInspectRecord(@RequestParam(value = "data", required = true, defaultValue = "") String data,
                                    @RequestParam(required = true, defaultValue = "") String proId,
                                    @RequestParam(required = true, defaultValue = "save") String type,
                                    @RequestParam(required = true, defaultValue = "1") String limitTime) {
        if (StringUtils.isBlank(data)) return result(false);
        try {
            return inspectRecordService.saveOrUpdate(data, proId, type, limitTime);
        } catch (Exception e) {
            return new JSONMessageException(getMessage("record.save.error", e.getMessage()));
        }
    }

    /**
     * 保存并推送巡查记录
     *
     * @param data
     * @param proId
     * @return
     */
    @RequestMapping("/inspect/record/saveAndSend")
    @ResponseBody
    public Object saveAndSendInspectRecord(@RequestParam(value = "data", required = true, defaultValue = "") String data,
                                           @RequestParam(required = true, defaultValue = "") String proId,
                                           @RequestParam(required = true, defaultValue = "") String url) {
        if (StringUtils.isBlank(data)) return result(false);
        try {
            return inspectRecordService.saveAndSend(data, proId, url);
        } catch (Exception e) {
            return result(new JSONMessageException(getMessage("record.save.error", e.getMessage())));
        }
    }

    @RequestMapping("/inspect/record/tpl")
    @ResponseBody
    public Object getInpectRecordTpl() {
        return result(inspectRecordService.getInpectRecordTpl());
    }

    /**
     * 获取单个巡查记录信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/inspect/record/get/{id}")
    @ResponseBody
    public Object getInspectRecordInfo(@PathVariable(value = "id") String id) {
        try {
            Map map = new HashedMap();
            InspectRecord inspectRecord = inspectRecordService.findInspectRecordById(id);
            map.put("proId", inspectRecord.getProject().getProId());
            Map properties = inspectRecord.getProperty();
            inspectRecord.setProperty(null);
            inspectRecord.setProject(null);
            map.putAll(BeanUtils.describe(inspectRecord));
            map.putAll(properties);
            map.put("createAt", DateFormatUtils.format(inspectRecord.getCreateAt(), "yyyy-MM-dd HH:mm:dd"));

            return result(map);
        } catch (Exception e) {
            return new JSONMessageException(getMessage("record.findOne.error", e.getMessage()));
        }
    }

    /**
     * 获取巡查记录以及附件的信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/inspect/record/send/{id}")
    @ResponseBody
    public Object sendInspectRecord(@PathVariable(value = "id") String id) {
        try {
            return result(inspectRecordService.sendInspectRecord(id));
        } catch (Exception e) {
            return new JSONMessageException(getMessage("record.send.error", e.getMessage()));
        }
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping("/inspect/record/del/{id}")
    @ResponseBody
    public Object deleteInspectRecordInfo(@PathVariable("id") String id) {
        try {
            inspectRecordService.deleteInspectRecordById(id);
            return result(true);
        } catch (Exception e) {
            return new JSONMessageException(getMessage("record.delete.error", e.getMessage()));
        }
    }

    /**
     * 对外接口（执法监察）
     *
     * @return
     */
    @RequestMapping("/external/list")
    @ResponseBody
    public Object showProList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "{}") String condition, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            return projectService.getNormalProject(JSONObject.parseObject(condition, Map.class), page - 1, size);
        } catch (Exception e) {
            return new JSONMessageException(getMessage("project.external.list.error", e.getMessage()));
        }
    }

    /**
     * 获取项目关联的摄像头信息
     *
     * @param proId
     * @return
     */
    @RequestMapping("/ref/cameras")
    @ResponseBody
    public Object getRefCameras(@RequestParam String proId) {
        return projectService.getRefCameras(proId);
    }

    /**
     * 查看项目操作日志
     *
     * @return
     */
    @RequestMapping("/log")
    public String OperationLog() {
        return "project/log";
    }

    /**
     * 查询项目操作日志
     *
     * @param page
     * @param size
     * @param condition
     * @return
     */
    @RequestMapping("/log/search")
    @ResponseBody
    public Map searchOperationLogs(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "{}") String condition) {
        Map conditions = JSONObject.parseObject(condition, Map.class);
        Page<OperationLog> operationLogs;
        try {
            operationLogs = operateLoggerService.search(conditions, page - 1, size);
            if(operationLogs!=null&&operationLogs.getContent().size()>0){
                for(int i=0;i<operationLogs.getContent().size();i++){
                    OperationLog item = operationLogs.getContent().get(i);
                    Camera camera= projectService.getCameraByProName(item.getProName());
                    if(camera!=null){
                        item.setRegionName(camera.getRegionName());
                    }
                }
            }

        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
        return result(operationLogs);
    }

    @RequestMapping("/log/export")
    @ResponseBody
    public void exportOperationLogs(@RequestParam(defaultValue = "{}") String condition,
                                    HttpServletResponse response) {
        Map conditions = JSONObject.parseObject(condition, Map.class);
        try {
            List<OperationLog> operationLogs = operateLoggerService.export(conditions);
            Workbook workbook = operateLoggerService.getExportExcel(operationLogs);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("项目操作日志信息.xls", Constant.UTF_8));
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    @RequestMapping("/export")
    @ResponseBody
    public void exportProjects(@RequestParam(defaultValue = "") String ownerId,
                               HttpServletResponse response) {
        try {
            String regionCode = sysUserService.getRegionCodeByUserId(ownerId);
            if (regionCode == null)
                return;
            List<Project> projectList = projectService.getAllProjects(ownerId, regionCode);
            Workbook workbook = projectService.getExportExcel(projectList);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("项目列表.xls", Constant.UTF_8));
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 获取行政区信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("/department")
    @ResponseBody
    public List getAllDept(@RequestParam(required = true) String userId) {
    	//显示全部行政区
    	boolean showXzq = AppConfig.getBooleanProperty("project.create.showxzq", false);
    	if(showXzq){
    		return projectService.getOrganList(null);
    	}else{
    		if (SecHelper.isGuest())
    			return new ArrayList();
    		else
    			return projectService.getOrganList(sysUserService.getRegionCodeByUserId(userId));
    	}
    }

    /**
     * 获取部门信息
     *
     * @return
     */
    @RequestMapping("/organ/list")
    @ResponseBody
    public List getOrganList() {
        return sysUserService.getOrganList();
    }

}
