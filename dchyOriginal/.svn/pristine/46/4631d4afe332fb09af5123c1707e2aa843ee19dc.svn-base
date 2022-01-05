package cn.gtmap.msurveyplat.portal;

import cn.gtmap.msurveyplat.common.config.SpringApplicationListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@MapperScan(value="cn.gtmap.msurveyplat.portal.mapper")
@EnableAutoConfiguration
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class PortalApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.bannerMode(Banner.Mode.OFF);
        //监听ApplicationEnvironmentPreparedEvent事件加载egov.home配置
        application.listeners(new SpringApplicationListener());
        application.build();
        return application.sources(PortalApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PortalApplication.class);
        springApplication.addListeners(new SpringApplicationListener());
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }


}
