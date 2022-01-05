package cn.gtmap.onemap.platform.entity.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * . ffmpeg 命令任务
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
@Data
@AllArgsConstructor
public class FFmpegTask implements Serializable {
    private static final long serialVersionUID = -7239641847244460524L;

    /**
     * 任务 id
     */
    private String id;

    /**
     * 任务 命令
     */
    private String command;

    /**
     * 任务命令主进程
     */
    private Process process;

    /**
     * 命令处理输出进程
     */
    private Thread outThread;

}
