package cn.gtmap.msurveyplat.portalol.config;


import cn.gtmap.msurveyplat.portalol.interceptor.CheckTokenAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/3/29
 * @desc CheckInterfaceAuthConfig: token验证拦截器
 */
@Configuration
public class CheckTokenAuthConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(this.checkTokenAuthInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public CheckTokenAuthInterceptor checkTokenAuthInterceptor() {
        return new CheckTokenAuthInterceptor();
    }
}
