package cn.gtmap.msurveyplat.common.config;

import com.gtis.config.EgovConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @param
 * @return
 * @description 监听ApplicationEnvironmentPreparedEvent事件加载egov.home配置
 */
public final class SpringApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationListener.class);

    public SpringApplicationListener() {
        //do
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        try {
            EgovConfigLoader.load(new String[0]);
            LOGGER.info("Spring applicationContext create success");
        } catch (RuntimeException var3) {
            LOGGER.error("Spring applicationContext create fail", var3);
        }
    }
}

