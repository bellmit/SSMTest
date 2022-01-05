package cn.gtmap.msurveyplat.promanage.config;

import com.gtis.config.AppConfig;
import com.gtis.config.EncryptHelper;
import com.gtis.config.PropertyPlaceholderHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/8
 * @description 应用加载配置文件
 */
@Component
public class AppConfigPlaceholderConfig extends PropertyPlaceholderConfigurer {
    private static final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);

    public AppConfigPlaceholderConfig() {
        String conf = AppConfig.getProperty("egov.conf");
        conf = conf.replace("file:/", "");
        Resource resource = new FileSystemResource(new File(conf + "/promanage/application.properties"));
        this.setLocations(resource);
        this.setFileEncoding("UTF-8");
        this.setIgnoreUnresolvablePlaceholders(true);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        AppConfig.setConfiguration(props);
        super.processProperties(beanFactoryToProcess, props);
    }

    @Override
    protected Properties mergeProperties() throws IOException {
        Properties props = super.mergeProperties();
        Iterator i = props.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String v = (String) entry.getValue();
            if (v != null && v.startsWith("!!")) {
                entry.setValue(EncryptHelper.decrypt(v));
            }
        }

        return props;
    }

    @Override
    protected String parseStringValue(String strVal, final Properties props, Set visitedPlaceholders) throws BeanDefinitionStoreException {
        return helper.replacePlaceholders(strVal, new PropertyPlaceholderHelper.PlaceholderResolver() {
            public String resolvePlaceholder(String placeholderName) {
                return AppConfigPlaceholderConfig.this.resolvePlaceholder(placeholderName, props, 1);
            }
        });
    }
}
