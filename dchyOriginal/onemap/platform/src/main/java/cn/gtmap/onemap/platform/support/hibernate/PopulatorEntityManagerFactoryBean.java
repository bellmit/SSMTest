package cn.gtmap.onemap.platform.support.hibernate;

import com.google.common.collect.Sets;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Set;

/**
 * entity manager factory bean with default populator
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/6/13 16:14
 */
public class PopulatorEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

    private DatabasePopulator defaultDbPopulator;

    public static final Set<String> AUTOS = Sets.newHashSet("create", "create-drop");

    @Override
    protected void postProcessEntityManagerFactory(EntityManagerFactory emf, PersistenceUnitInfo pui) {
        String hbm2ddlAuto = (String) getJpaPropertyMap().get(AvailableSettings.HBM2DDL_AUTO);
        if (hbm2ddlAuto != null && AUTOS.contains(hbm2ddlAuto) && defaultDbPopulator != null) {
            logger.warn("App will create Db!");
            DatabasePopulatorUtils.execute(this.defaultDbPopulator, getDataSource());
        }
        super.postProcessEntityManagerFactory(emf, pui);
    }

    public void setDefaultDbPopulator(DatabasePopulator defaultDbPopulator) {
        this.defaultDbPopulator = defaultDbPopulator;
    }
}
