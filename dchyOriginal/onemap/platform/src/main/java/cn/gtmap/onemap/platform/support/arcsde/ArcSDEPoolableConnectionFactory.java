package cn.gtmap.onemap.platform.support.arcsde;

import cn.gtmap.onemap.platform.support.arcsde.impl.ArcSDEDataSource;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import org.apache.commons.pool.PoolableObjectFactory;
import org.springframework.util.Assert;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-13 下午5:32
 */
public class ArcSDEPoolableConnectionFactory implements PoolableObjectFactory {

    private static final long TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS = 5;

    private final String NONE = "none";

    private ArcSDEDataSource sdeDataSource;


    public ArcSDEPoolableConnectionFactory(ArcSDEDataSource sdeDataSource) {
        this.sdeDataSource = sdeDataSource;
    }

    /**
     * Creates an instance that can be served by the pool.
     * Instances returned from this method should be in the
     * same state as if they had been
     * {@link #activateObject activated}. They will not be
     * activated before being served by the pool.
     *
     * @return an instance that can be served by the pool.
     * @throws Exception if there is a problem creating a new instance,
     *                   this will be propagated to the code requesting an object.
     */
    public Object makeObject() throws Exception {
        Assert.notNull(sdeDataSource, "数据源不可为空");
        return new SeConnection(sdeDataSource.getServer(), sdeDataSource.getInstance(), NONE,
                sdeDataSource.getUsername(), sdeDataSource.getPassword());
    }

    /**
     * Destroys an instance no longer needed by the pool.
     * <p>
     * It is important for implementations of this method to be aware
     * that there is no guarantee about what state <code>obj</code>
     * will be in and the implementation should be prepared to handle
     * unexpected errors.
     * </p>
     * <p>
     * Also, an implementation must take in to consideration that
     * instances lost to the garbage collector may never be destroyed.
     * </p>
     *
     * @param obj the instance to be destroyed
     * @throws Exception should be avoided as it may be swallowed by
     *                   the pool implementation.
     * @see #validateObject
     * @see org.apache.commons.pool.ObjectPool#invalidateObject
     */
    public void destroyObject(Object obj) throws Exception {
        Assert.notNull(obj, "销毁对象不可为空！");
        if (obj instanceof SeConnection) {
            ((SeConnection) obj).close();
        }
    }

    /**
     * Ensures that the instance is safe to be returned by the pool.
     * Returns <code>false</code> if <code>obj</code> should be destroyed.
     *
     * @param obj the instance to be validated
     * @return <code>false</code> if <code>obj</code> is not valid and should
     *         be dropped from the pool, <code>true</code> otherwise.
     */
    public boolean validateObject(Object obj) {
        Assert.notNull(obj, "验证对象不可为空！");
        if (obj instanceof SeConnection && !((SeConnection) obj).isClosed()) {
            try {
                /*((SeConnection) obj).getServerTime();*/
                if (TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS < ((SeConnection) obj).getTimeSinceLastRT())
                    ((SeConnection) obj).testServer(TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS);
                return true;
            } catch (SeException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Reinitialize an instance to be returned by the pool.
     *
     * @param obj the instance to be activated
     * @throws Exception if there is a problem activating <code>obj</code>,
     *                   this exception may be swallowed by the pool.
     * @see #destroyObject
     */
    public void activateObject(Object obj) throws Exception {
        SeConnection connection = (SeConnection) obj;
    }

    /**
     * Uninitialize an instance to be returned to the idle object pool.
     *
     * @param obj the instance to be passivated
     * @throws Exception if there is a problem passivating <code>obj</code>,
     *                   this exception may be swallowed by the pool.
     * @see #destroyObject
     */
    public void passivateObject(Object obj) throws Exception {
        SeConnection connection = (SeConnection) obj;
        try {
            ((SeConnection) obj).close();
        } catch (SeException e) {
            e.printStackTrace();
        }
    }
}
