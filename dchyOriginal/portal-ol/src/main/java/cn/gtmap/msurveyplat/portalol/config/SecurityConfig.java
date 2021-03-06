package cn.gtmap.msurveyplat.portalol.config;

import cn.gtmap.msurveyplat.portalol.core.service.impl.CustomUserDetailsService;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity //启用web权限
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用方法验证
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CasProperties casProperties;
    @Resource
    private JszwfwProperties jszwfwProperties;

    /**
     * 定义认证用户信息获取来源，密码校验规则等
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(casAuthenticationProvider());
        //inMemoryAuthentication 从内存中获取
        //auth.inMemoryAuthentication().withUser("lidd").password("123456").roles("USER")
        //.and().withUser("admin").password("123456").roles("ADMIN");

        //jdbcAuthentication从数据库中获取，但是默认是以security提供的表结构
        //usersByUsernameQuery 指定查询用户SQL
        //authoritiesByUsernameQuery 指定查询权限SQL
        //auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(query).authoritiesByUsernameQuery(query);

        //注入userDetailsService，需要实现userDetailsService接口
        //auth.userDetailsService(userDetailsService);
    }

    /**
     * 定义安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .and().authorizeRequests()//配置安全策略  /rest/v1.0/platform
                .antMatchers(casProperties.getAppLoginUrl()).permitAll()//定义/请求不需要验证
                .antMatchers("/index/getUserMessage", "/login/**").permitAll()//定义/#/home请求需要验证
                .antMatchers("/index/*", "/home", "/home/*").authenticated()//定义/#/home请求需要验证
                // .anyRequest().authenticated()//其余的所有请求都需要验证
                .and()
                .logout()
                .permitAll()
                .and()
                .headers().frameOptions().disable();

/*                .and()
                .formLogin();//使用form表单登录*/


        http.httpBasic().authenticationEntryPoint(casAuthenticationEntryPoint())
                .and()
                .addFilter(casAuthenticationFilter())
                .addFilterBefore(casLogoutFilter(), LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);

        //http.csrf().disable(); //禁用CSRF
    }

    //
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());
        return successHandler;
    }

    /**
     * 认证的入口
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * 指定service相关信息
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());
        //serviceProperties.setAuthenticateAllArtifacts(true);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    /**
     * CAS认证过滤器
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
        casAuthenticationFilter.setAuthenticationSuccessHandler(new MyUrlAuthenticationSuccessHandler());
        return casAuthenticationFilter;
    }

    /**
     * cas 认证 Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        //casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
        casAuthenticationProvider.setUserDetailsService(customUserDetailsService()); //这里只是接口类型，实现的接口不一样，都可以的。
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     * 用户自定义的AuthenticationUserDetailsService
     */
/*    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService(){
        return new CustomUserDetailsService();
    }*/
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    /**
     * 单点登出过滤器
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getCasServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * 请求单点退出过滤器
     */
    @Bean
    public LogoutFilter casLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
        return logoutFilter;
    }

    @Bean
    public FilterRegistrationBean httpParamsFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new HttpParamsFilter());
        filterRegistrationBean.setOrder(-999);
        //filterRegistrationBean.addUrlPatterns("/");
        return filterRegistrationBean;
    }
}
