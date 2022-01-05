package cn.gtmap.onemap.platform.support.ffmpeg;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * . ffpmeg output thread
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
public class FFmpegOutputThread extends Thread {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 控制状态
     */
    private volatile boolean desstatus = true;

    /**
     * 读取输出流
     */
    private BufferedReader br = null;

    /**
     * 任务 id
     */
    private String taskId = null;

    /**
     * 消息解析器
     */
    private FFmpegOutputParser parser;

    private boolean debug = true;


    public FFmpegOutputThread(InputStream is, String id, FFmpegOutputParser parser, boolean debug) {
        br = new BufferedReader(new InputStreamReader(is));
        this.taskId = id;
        this.parser = parser;
        this.debug = debug;
    }

    /**
     * 重写线程销毁方法，安全的关闭线程
     */
    @Override
    public void destroy() {
        desstatus = false;
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg;
        try {
            if (debug) {
                while (desstatus && (msg = br.readLine()) != null) {
                    parser.parse(taskId, msg);
                }
            } else {
                Thread.yield();
            }
        } catch (IOException e) {
            logger.error("任务[{}] 发生内部异常错误，自动关闭[{}]线程", this.taskId, this.getId());
            destroy();
        } finally {
            if (this.isAlive()) {
                destroy();
            }
        }
    }

}
