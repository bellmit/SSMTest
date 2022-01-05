package cn.gtmap.onemap.platform.support.arcsde.impl;

import cn.gtmap.onemap.platform.event.GISDaoException;
import cn.gtmap.onemap.platform.support.arcsde.ArcSDEPoolableConnectionFactory;
import cn.gtmap.onemap.platform.support.arcsde.SDEDataSource;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-14 下午4:33
 */
public class ArcSDEDataSource extends BasicDataSource implements SDEDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String server;
    private String instance;

    private ArcSDEPoolableConnectionFactory factory;

    private volatile GenericObjectPool pool;

    public ArcSDEDataSource() {
        setMinEvictableIdleTimeMillis(60000);//一分钟内不使用的空闲连接,则释放
        setTimeBetweenEvictionRunsMillis(30000);//30秒做一次空闲连接检查
        setRemoveAbandoned(true);//启用清理removeAbandonedTimeout秒没有使用的活动连接,清理后并没有放回连接池
        setRemoveAbandonedTimeout(120);//活动连接的最大空闲时间
        setLogAbandoned(true);//连接池收回空闲的活动连接时打印消息
    }

    /**
     * 创建sde连接源
     */
    @PostConstruct
    private void createSdeDataSource() {
        createSdePoolFactory();
        createSeConnectionPool();
        try {
            for (int i = 0; i < initialSize; i++) {
                pool.addObject();
            }
        } catch (SeException ex) {
            logger.error("ArcSDE连接异常【{}】", GISDaoException.formateSeError(ex.getSeError()));
        } catch (Exception e) {
            logger.error("创建ArcSDE数据源异常【{}】", e.getLocalizedMessage());
        }
    }

    private void createSdePoolFactory() {
        factory = new ArcSDEPoolableConnectionFactory(this);
    }

    /**
     * 获取sde的连接
     *
     * @return
     */
    public SeConnection getSeConnection() throws Exception {
        SeConnection seConnection = null;
        try {
            logger.info(" pool : num active [{}], num idle [{}]", pool.getNumActive(), pool.getNumIdle());
            seConnection = (SeConnection) pool.borrowObject();
            return seConnection;
        } catch (SeException e) {
            throw new RuntimeException(GISDaoException.formateSeError(e.getSeError()));
        } catch (Exception e) {
            Assert.notNull(seConnection, "未正常获取ArcSDE连接！".concat(toString()));
            if (seConnection != null) pool.invalidateObject(seConnection);
            logger.error("获取ArcSDE数据连接出现异常【{}】", e.getLocalizedMessage());
        }
        throw new RuntimeException("ArcSDE DataSource get SeConnection error");
    }

    /**
     * return seConnection
     *
     * @param connection
     */
    @Override
    public void returnSeConnection(SeConnection connection) {
        if (connection != null)
            try {
                pool.returnObject(connection);
            } catch (Exception e) {
                logger.error(" return SeConnection error :[{}]", e.getLocalizedMessage());
            }
    }

    /**
     *
     */
    private void createSeConnectionPool() {
        GenericObjectPool gop = null;
        gop = new GenericObjectPool(factory);
        gop.setLifo(true);
        gop.setMaxActive(maxActive);
        gop.setMaxIdle(maxIdle);
        gop.setMinIdle(minIdle);
        gop.setMaxWait(maxWait);
        gop.setTestOnBorrow(testOnBorrow || true);
        gop.setTestOnReturn(false);
        gop.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis > 0 ? timeBetweenEvictionRunsMillis : 30 * 1000);
        gop.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        gop.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis > 0 ? minEvictableIdleTimeMillis : 5 * 60 * 1000);
        gop.setTestWhileIdle(false);
//        if (maxWait > 0) gop.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
//        else gop.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_FAIL);
        gop.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
        pool = gop;

    }

    @Override
    public synchronized void close() throws SQLException {
        try {
            pool.clear();
            pool.close();
        } catch (Exception e) {
            logger.error("关闭连接池异常【{}】", e.getLocalizedMessage());
        }
        super.close();
    }

    @Override
    public String toString() {
        return "ArcSDE " + getServer().concat(":").concat(String.valueOf(getInstance())).concat("/" + getUsername());
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    /**
     * Return the parent Logger of all the Loggers used by this data source. This
     * should be the Logger farthest from the root Logger that is
     * still an ancestor of all of the Loggers used by this data source. Configuring
     * this Logger will affect all of the log messages generated by the data source.
     * In the worst case, this may be the root Logger.
     *
     * @return the parent Logger for this data source
     * @throws java.sql.SQLFeatureNotSupportedException if the data source does not use <code>java.util.logging<code>.
     * @since 1.7
     */
//    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
