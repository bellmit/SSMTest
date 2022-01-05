package cn.gtmap.msurveyplat.promanage.scheduler;

import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglContractFileService;
import cn.gtmap.msurveyplat.promanage.core.service.impl.DchyXmglContractFileServiceImpl;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglChxmClsxService;
import cn.gtmap.msurveyplat.promanage.service.FileUploadCheckService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/14
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
@Component
public class SchedulerConfig {

    private final Logger logger = Logger.getLogger(this.getClass());

    //#定时任务设置时间-------- "秒 分 时 ? * ?" [秒] [分] [小时] [日] [月] [周] [年]
    //#   秒 分 时 ? * ?
    //每隔5秒执行一次 */20 * * * * ?

    @Autowired
    private FileUploadCheckService fileUploadCheckService;

    @Autowired
    DchyXmglChxmClsxService dchyXmglChxmClsxService;

    @Scheduled(cron = "${fileUploadTime}")
    private void fileUploadTime() {
        try {
            //fileUploadCheckService.fileUploadCheck();
            logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "------任务执行-------");
        } catch (Exception e) {
            logger.error("错误原因{}", e);
        }
    }

    //测绘期限超期提醒
    @Scheduled(cron = "${overdueReminder}")
    private void overdueReminder() {
        try {
            dchyXmglChxmClsxService.queryCqtx();
            logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "------测绘期限超期检测任务执行-------");
        } catch (Exception e) {
            logger.error("错误原因{}", e);
        }
    }

}
