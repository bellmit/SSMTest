package cn.gtmap.msurveyplat.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/8
 * @description 质检登记小类和成果数据类型关系配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "qc")
public class DjxlCgsjlxConfig {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 登记小类和成果数据类型关系
     */
    private List<Map> djxlCgsjlxMapList;

    public List<Map> getDjxlCgsjlxMapList() {
        return djxlCgsjlxMapList;
    }

    public void setDjxlCgsjlxMapList(List<Map> djxlCgsjlxMapList) {
        this.djxlCgsjlxMapList = djxlCgsjlxMapList;
    }

}
