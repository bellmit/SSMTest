package cn.gtmap.onemap.platform.support.ffmpeg;

import cn.gtmap.onemap.platform.service.impl.BaseLogger;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
public class FFmpegOutputDefaultParser extends BaseLogger implements FFmpegOutputParser {
    /**
     * 解析消息
     *
     * @param id
     * @param msg
     */
    @Override
    public void parse(String id, String msg) {
        // 过滤消息
        if (msg.indexOf("[rtsp") != -1) {
            logger.warn("任务 [{}] 发生网络异常丢包，消息体：[{}]", id, msg);
        } else if (msg.indexOf("frame=") != -1) {
            logger.debug(id + ":" + msg);
        } else {
            logger.debug(msg);
        }

    }
}
