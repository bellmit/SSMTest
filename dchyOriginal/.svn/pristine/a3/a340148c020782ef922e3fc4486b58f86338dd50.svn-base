package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.CameraLog;
import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import cn.gtmap.onemap.platform.entity.video.OperationLog;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.service.CameraLoggerService;
import cn.gtmap.onemap.platform.service.InspectRecordService;
import cn.gtmap.onemap.platform.service.OperateLoggerService;
import cn.gtmap.onemap.platform.service.ProjectService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import cn.gtmap.onemap.platform.utils.DateUtils;
import cn.gtmap.onemap.security.SecHelper;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * aop方式输出调用方法信息日志
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/9/16 16:45
 */
@Component
@Aspect
public class MethodLogAspect extends BaseLogger {
    /**
     * 监控设备日志
     */
    @Autowired
    private CameraLoggerService cameraLoggerService;

    /**
     * 系统操作日志
     */
    @Autowired
    private OperateLoggerService operationLoggerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private InspectRecordService inspectRecordService;


    @Pointcut("execution(* cn.gtmap.onemap.platform.service.impl.GeoServiceImpl.*(..))")
    public void pointcut() {
    }

    /**
     * 打印出geoservice接口中的方法名称、参数等信息
     *
     * @param joinPoint
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            logger.debug("-----执行AOP前置输出-------");
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            logger.debug("类：【" + targetName + "】方法名：【" + methodName + "】\n参数:" + JSON.toJSONString(arguments));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }

//    @After("execution(* cn.gtmap.onemap.platform.service.impl.LoggerServiceImpl.search(..))")
    public void getCameraLog(JoinPoint point){
        logger.info("******设备日志切面执行开始******");
        CameraLog log = new CameraLog();
        log.setUserId(SecHelper.getUserId());
        log.setUserName(SecHelper.getUser().getViewName());
        log.setCameraId("j273662683");
        log.setCameraName("测试设备");
        log.setOptContent("查询日志");
//        cameraLoggerService.save(log);
        logger.info("******设备日志切面执行完成******");
    }

//    @After("execution(* cn.gtmap.onemap.platform.controller.VideoController.searchLogs(..))")
    public void getCameraLog1(JoinPoint point){
        logger.info("******设备日志切面执行开始******");
        CameraLog log = new CameraLog();
        log.setUserId(SecHelper.getUserId());
        log.setUserName(SecHelper.getUser().getViewName());
        log.setCameraId("j273662683");
        log.setCameraName("测试设备");
        log.setOptContent("查询日志");
        cameraLoggerService.save(log);
        logger.info("******设备日志切面执行完成******");
    }

    /**
     * 创建项目后日志插入
     * @param point
     */
    @After("execution(* cn.gtmap.onemap.platform.service.impl.ProjectServiceImpl.saveOrUpdate(..))")
    public void insertLoggerAfterProjectCreated(JoinPoint point){
        Project project = (Project)point.getArgs()[0];
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(SecHelper.getUserId());
        operationLog.setUserName(SecHelper.getUser().getViewName());
        operationLog.setProName(project.getProName());
        operationLog.setOptContent("创建项目");
        operationLog.setType(Constant.OptLogType.PROJECTLOG.getType());
        operationLoggerService.save(operationLog);
    }

    /**
     * 删除项目日志记录
     * @param pjp
     */
   @Around("execution(* cn.gtmap.onemap.platform.service.impl.ProjectServiceImpl.deleteByProid(..))")
    public Object insertLoggerAfterProjectDeleted(ProceedingJoinPoint pjp) throws Throwable{
        String projectId = (String)pjp.getArgs()[0];
        Project project = projectService.getByProid(projectId);
        Object result;
        result = pjp.proceed();
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(SecHelper.getUserId());
        operationLog.setUserName(SecHelper.getUser().getViewName());
        operationLog.setProName(project.getProName());
        operationLog.setOptContent("删除项目");
        operationLog.setType(Constant.OptLogType.PROJECTLOG.getType());
        operationLoggerService.save(operationLog);
        return result;
    }

    /**
     * 新增巡查记录日志记录
     * @param joinPoint
     */
    @AfterReturning(value = "execution(* cn.gtmap.onemap.platform.service.impl.InspectRecordServiceImpl.saveOrUpdate(..))",
            argNames = "inspectRecord", returning = "inspectRecord")
    public void insertLoggerAfterInpectRecordCreated(JoinPoint joinPoint, InspectRecord inspectRecord){
        String type = joinPoint.getArgs()[2].toString();
        if("save".equals(type)){
            try {
                OperationLog operationLog = new OperationLog();
                operationLog.setUserId(SecHelper.getUserId());
                operationLog.setUserName(SecHelper.getUser().getViewName());
                operationLog.setOptContent("新增巡查记录【"+ DateUtils.formatDateTime(inspectRecord.getCreateAt(), null) +"】");
                operationLog.setProName(inspectRecord.getProject().getProName());
                operationLog.setType(Constant.OptLogType.INSPECTLOG.getType());
                operationLoggerService.save(operationLog);
            } catch (Exception e) {
                logger.error(getMessage("record.log.add.error", e.getMessage()));
            }
        }
    }


    /***
     * 删除巡查记录日志记录
     * @param pjp
     */
    @Around("execution(* cn.gtmap.onemap.platform.service.impl.InspectRecordServiceImpl.deleteInspectRecordById(..))")
    public Object insertLoggerAfterInpectRecordDelete(ProceedingJoinPoint pjp){
        String inspectId = pjp.getArgs()[0].toString();
        InspectRecord inspectRecord = inspectRecordService.findInspectRecordById(inspectId);
        try {
            Object obj = pjp.proceed();
            OperationLog operationLog = new OperationLog();
            operationLog.setUserId(SecHelper.getUserId());
            operationLog.setUserName(SecHelper.getUser().getViewName());
            operationLog.setOptContent("删除巡查记录【"+ DateUtils.formatDateTime(inspectRecord.getCreateAt(), null) +"】");
            operationLog.setProName(inspectRecord.getProject().getProName());
            operationLog.setType(Constant.OptLogType.INSPECTLOG.getType());
            operationLoggerService.save(operationLog);
            return obj;
        } catch (Throwable throwable) {
            logger.error(getMessage("record.log.delete.error", throwable.getMessage()));
            throw new RuntimeException(throwable);
        }
    }
}
