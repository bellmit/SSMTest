package cn.gtmap.msurveyplat.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/14
 * @description
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            // -- 静态资源
            "/rest/**",
            "/init/**",
            "/view/**",
            "/css/**",
            "/layui/css/**",
            "/js/**",
            "/lib/**",
            "/layui/**",
            "/fonts/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(AUTH_WHITELIST).permitAll();
//                .antMatchers("/**/*").denyAll();

        //可在相同域名页面的frame中展示。
        http.headers().frameOptions().sameOrigin();

        http.csrf().disable();
    }
}
