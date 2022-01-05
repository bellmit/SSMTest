package cn.gtmap.msurveyplat.exchange;

import cn.gtmap.msurveyplat.common.config.SpringApplicationListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = "cn.gtmap.msurveyplat.exchange.mapper")
public class ExchangeApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.bannerMode(Banner.Mode.OFF);
        application.listeners(new SpringApplicationListener());
        application.build();
        return application.sources(ExchangeApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ExchangeApplication.class);
        //监听ApplicationEnvironmentPreparedEvent事件加载egov.home配置
        springApplication.addListeners(new SpringApplicationListener());
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }

}
