package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.FFmpegTaskDao;
import cn.gtmap.onemap.platform.entity.ffmpeg.FFmpegConfig;
import cn.gtmap.onemap.platform.entity.ffmpeg.FFmpegTask;
import cn.gtmap.onemap.platform.service.FFmpegManagerService;
import cn.gtmap.onemap.platform.support.ffmpeg.FFmpegOutputDefaultParser;
import cn.gtmap.onemap.platform.support.ffmpeg.FFmpegOutputThread;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * .ffmpeg 命名管理 service
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
public class FFmpegManagerServiceImpl extends BaseLogger implements FFmpegManagerService {

    private final Runtime runtime = Runtime.getRuntime();

    private final FFmpegTaskDao taskDao;

    private FFmpegConfig config = new FFmpegConfig();


    @Autowired
    public FFmpegManagerServiceImpl(FFmpegTaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void setConfig(Resource path) {
        try {
            Yaml yaml = new Yaml();
            this.config = yaml.loadAs(new FileInputStream(new File(path.getURI())), FFmpegConfig.class);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 开始一个 ffmpeg 命令
     *
     * @param command
     * @return
     */
    @Override
    public String start(String id, String command) {
        if (isNotNull(id) && isNotNull(command)) {
            FFmpegTask task = process(id, command);
            if (task != null) {
                String taskId = taskDao.save(task);
                if (isNotNull(taskId)) {
                    return taskId;
                } else {
                    stop(task.getProcess());
                    stop(task.getOutThread());
                    logger.error("持久化失败，停止任务！");
                }
            }
        }
        return null;
    }

    /**
     * 开启视频流
     *
     * @param cameraCode 镜头id
     * @return rtmp 地址 eg. rtmp://127.0.0.1:1935/live/test
     */
    @Override
    public String startStream(String cameraCode, String domainCode) {
        FFmpegTask task = find(cameraCode);
        if (isNotNull(task)) {
            // 正在运行中 则直接返回 rtmp 地址
            return config.getRtmpUrl().concat("/").concat(task.getId());
        }
        String iServer = AppConfig.getProperty("ivsServer.url");
        if (isNull(iServer)) {
            throw new RuntimeException("ivs server is null.");
        }
        StringBuilder sb = new StringBuilder(iServer);
        sb.append("/api/live");
        sb.append("?cameraCode=".concat(cameraCode));
        sb.append("&domainCode=".concat(domainCode));
        String ivsUrl = sb.toString();
        String rtspUrl = String.valueOf(HttpRequest.get(ivsUrl, StringUtils.EMPTY, null));
        if(StringUtils.isBlank(rtspUrl)){
            logger.error("请求实时视频地址异常，cameracode：【{}】",cameraCode);
            return null;
        }
        logger.info("[{}] rtsp 地址(30s 后失效)为: {}", cameraCode, rtspUrl);
        // 组装 ffmpeg 命令 并执行
        String command = assembleRtmpCommand(cameraCode, rtspUrl);
        String taskId = start(cameraCode, command);
        if (isNotNull(taskId)) {
            return config.getRtmpUrl().concat("/").concat(taskId);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 终止一个 ffmpeg 任务
     *
     * @param id
     * @return
     */
    @Override
    public boolean terminate(String id) {
        if (id != null) {
            if (config.isDebug()) {
                logger.info("正在停止任务[{}].." + id);
            }
            FFmpegTask tasker = taskDao.findOne(id);
            if (stop(tasker.getProcess(), tasker.getOutThread())) {
                taskDao.delete(id);
                return true;
            }
        }
        return false;
    }

    /**
     * 终止所有的 ffmpeg  任务
     *
     * @return
     */
    @PreDestroy
    @Override
    public int terminateAll() {
        Collection<FFmpegTask> list = taskDao.findAll();
        Iterator<FFmpegTask> iter = list.iterator();
        FFmpegTask task;
        int index = 0;
        while (iter.hasNext()) {
            task = iter.next();
            if (stop(task.getProcess(), task.getOutThread())) {
                taskDao.delete(task.getId());
                index++;
            }
        }
        if (config.isDebug()) {
            logger.info("停止了" + index + "个任务！");
        }
        return index;
    }

    /**
     * find by id
     *
     * @param id
     * @return
     */
    @Override
    public FFmpegTask find(String id) {
        return taskDao.findOne(id);
    }

    /**
     * find all
     *
     * @return
     */
    @Override
    public Collection<FFmpegTask> findAll() {
        return taskDao.findAll();
    }

    /**
     * assemble rtmp command
     *
     * @param id
     * @param rtspUrl
     * @return
     */
    private String assembleRtmpCommand(String id, String rtspUrl) {
        StringBuilder ret = new StringBuilder();
        ret.append("ffmpeg ");
        ret.append(" -rtsp_transport ");
        // 输入流的传输协议
        ret.append(config.getTransport());
        ret.append(" -i ");
        ret.append("\"".concat(rtspUrl) + "\"");
        ret.append(config.getRtmpOptions());
        ret.append("\"" + config.getRtmpUrl().concat("/").concat(id) + "\"");
        return ret.toString();
    }

    /**
     * 处理单个 ffmpeg 命令
     *
     * @param command
     * @return
     */
    private FFmpegTask process(String id, String command) {
        Process process = null;
        FFmpegOutputThread outputThread = null;
        FFmpegTask task;
        try {
            if (config.isDebug()) {
                logger.info("即将执行命令: {}", command);
            }
            // 执行本地命令获取任务主进程
            process = runtime.exec(command);

            outputThread = new FFmpegOutputThread(process.getErrorStream(), id,
                    new FFmpegOutputDefaultParser(), config.isDebug());
            outputThread.start();
            task = new FFmpegTask(id, command, process, outputThread);
        } catch (IOException e) {
            logger.error("执行命令失败！正在停止进程和输出线程...");
            stop(outputThread);
            stop(process);
            return null;
        }
        return task;
    }

    /**
     * stop process
     *
     * @param process
     * @return
     */
    private boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    /**
     * stop thread
     *
     * @param outHandler
     * @return
     */
    @SuppressWarnings("deprecation")
    private boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.stop();
            outHandler.destroy();
            return true;
        }
        return false;
    }

    /**
     * stop both
     *
     * @param process
     * @param thread
     * @return
     */
    private boolean stop(Process process, Thread thread) {
        boolean ret;
        ret = stop(thread);
        ret = stop(process);
        return ret;
    }

//    @PreDestroy
//    public void destroy() {
//        terminateAll();
//    }


}
