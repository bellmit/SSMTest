package cn.gtmap.msurveyplat.server.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtils  implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static String getActiveProfile() {
        String[] profiles = context.getEnvironment().getActiveProfiles();
        if (profiles.length != 0) {
            return profiles[0];
        }
        return "";
    }
}
