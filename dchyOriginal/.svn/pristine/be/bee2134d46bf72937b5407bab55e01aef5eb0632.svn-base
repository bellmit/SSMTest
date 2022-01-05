package cn.gtmap.msurveyplat.server.config;

import cn.gtmap.msurveyplat.common.vo.ProjectDetailVo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/27
 * @description 一张图项目详情配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "onemap")
public class ProjectDetailConfig {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 项目详情
     */
    List<ProjectDetailVo> projectDetailVoList;

    public List<ProjectDetailVo> getProjectDetailVoList() {
        return projectDetailVoList;
    }

    public void setProjectDetailVoList(List<ProjectDetailVo> projectDetailVoList) {
        this.projectDetailVoList = projectDetailVoList;
    }
}
