package cn.gtmap.msurveyplat.server;

import cn.gtmap.msurveyplat.common.config.SpringApplicationListener;
import cn.gtmap.msurveyplat.server.util.SpringContextUtil;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.WebApplicationContextUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


@SpringBootApplication
@ComponentScan(basePackages = "cn.gtmap.msurveyplat.server")
@EnableSwagger2
public class ServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.bannerMode(Banner.Mode.OFF);
        //监听ApplicationEnvironmentPreparedEvent事件加载egov.home配置
        application.listeners(new SpringApplicationListener());
        application.build();
        return application.sources(ServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ServerApplication.class);
        springApplication.addListeners(new SpringApplicationListener());
        springApplication.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = springApplication.run(args);
        SpringContextUtil.setApplicationContext(context);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        SpringContextUtil.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
    }

}
