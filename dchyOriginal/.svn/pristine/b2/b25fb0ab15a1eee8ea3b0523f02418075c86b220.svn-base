package cn.gtmap.onemap.platform.entity.job;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description: 任务实体类
 * @date 2018/8/10
 */
@Data
@NoArgsConstructor
public class SchedularJob implements Serializable{

    private static final long serialVersionUID = 8903134104471842308L;

    //任务业务逻辑所在的完全限定类名称
    private String clazz;
    //任务执行的方法
    private String method;
    //任务名称，标示唯一的任务
    private String name;
    //执行任务方法时的参数
    private List args;
    //是否启用任务
    private boolean enable;
    //任务执行时间的Cron表达式
    private String time;
}
