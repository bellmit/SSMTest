package cn.gtmap.msurveyplat.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 初始化工厂类
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "init.services")
public class InitBeanConfig {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 所有服务
     */
    private List<Class> list;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 初始化服务
     */
    private List<Class> initServices;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 删除服务
     */
    private List<Class> deleteServices;

    public List<Class> getList() {
        return list;
    }

    public void setList(List<Class> list) {
        this.list = list;
    }

    public List<Class> getInitServices() {
        return initServices;
    }

    public void setInitServices(List<Class> initServices) {
        this.initServices = initServices;
    }

    public List<Class> getDeleteServices() {
        return deleteServices;
    }

    public void setDeleteServices(List<Class> deleteServices) {
        this.deleteServices = deleteServices;
    }

}
