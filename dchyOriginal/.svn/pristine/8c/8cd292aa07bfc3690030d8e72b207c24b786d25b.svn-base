package cn.gtmap.msurveyplat.serviceol.config;

import cn.gtmap.msurveyplat.common.core.support.mybatis.page.PaginationInterceptor;
import com.alibaba.druid.pool.DruidDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/11/28
 * @description 线上备份数据库
 */
@Configuration
@ImportResource(locations={"classpath:conf/spring/config-xsbfmybatis.xml"})
@MapperScan(basePackages = {"cn.gtmap.msurveyplat.serviceol.core.xsbfmapper"},sqlSessionFactoryRef = "sqlSessionFactoryXSBF")
public class DataBaseConfigXSBF {

    private Logger logger = Logger.getLogger(DataBaseConfig.class);
    static final String MAPPER_LOCATION = "classpath:conf/xsbfmapper/*.xml";
    static final String CONFIG_LOCATION = "classpath:conf/spring/mybatis-configuration.xml";


    @Value("${spring.datasource.dchyxsbf.url}")
    private String dbUrl;

    @Value("${spring.datasource.dchyxsbf.username}")
    private String username;

    @Value("${spring.datasource.dchyxsbf.password}")
    private String password;

    @Value("${spring.datasource.dchyxsbf.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("{spring.datasource.connectionProperties}")
    private String connectionProperties;

    /**
     * 返回data2数据库的数据源
     * @return
     */
    @Bean(name="DataSourceXSBF")
    public DataSource dataSource(){
        logger.info("datasource:"+dbUrl+" user:"+username);
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "druid configuration initialization filter : {0}", e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    /**
     * 返回data2数据库的会话工厂
     * @param ds
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactoryXSBF")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("DataSourceXSBF") DataSource ds,
                                               @Qualifier("mybatisPaginationInterceptor") PaginationInterceptor mybatisPaginationInterceptor,
                                               @Qualifier("mybatisPaginationOrderInterceptor") PaginationInterceptor mybatisPaginationOrderInterceptor) throws Exception{
        Interceptor[] interceptors = new Interceptor[2];
        interceptors[0] = mybatisPaginationInterceptor;
        interceptors[1] = mybatisPaginationOrderInterceptor;
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIG_LOCATION));
        bean.setPlugins(interceptors);
        bean.setDataSource(ds);
        return bean.getObject();
    }

    /**
     * 返回data2数据库的会话模板
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionTemplateXSBF")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryXSBF") SqlSessionFactory sessionFactory) throws  Exception{
        return  new SqlSessionTemplate(sessionFactory);
    }

    /**
     * 返回data2数据库的事务
     * @param ds
     * @return
     */
    @Bean(name = "TransactionManagerXSBF")
    public DataSourceTransactionManager transactionManager(@Qualifier("DataSourceXSBF") DataSource ds){
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "liquibaseXSBF")
    public SpringLiquibase primaryLiquibase(@Qualifier("DataSourceXSBF") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:conf/spring/changelog-master.xml");
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
