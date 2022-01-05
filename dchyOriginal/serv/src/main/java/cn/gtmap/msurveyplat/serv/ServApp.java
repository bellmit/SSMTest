package cn.gtmap.msurveyplat.serv;

import cn.gtmap.msurveyplat.common.config.SpringApplicationListener;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2018/10/30
 * @description
 */
@EnableTransactionManagement
//开启定时
@EnableScheduling
@EnableCaching
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@ServletComponentScan
public class ServApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.bannerMode(Banner.Mode.OFF);
        //监听ApplicationEnvironmentPreparedEvent事件加载egov.home配置
        application.listeners(new SpringApplicationListener());

        application.build();
        return application.sources(ServApp.class);
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ServApp.class);
        springApplication.addListeners(new SpringApplicationListener());
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
