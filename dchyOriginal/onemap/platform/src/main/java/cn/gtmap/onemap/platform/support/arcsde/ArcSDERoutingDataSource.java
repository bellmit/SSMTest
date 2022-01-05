package cn.gtmap.onemap.platform.support.arcsde;

import cn.gtmap.onemap.platform.support.spring.DynamicDataSource;
import com.esri.sde.sdk.client.SeConnection;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-14 下午5:12
 */
public class ArcSDERoutingDataSource extends DynamicDataSource implements SDEDataSource {

    /**
     * 获取sde的连接
     *
     * @return
     */
    public SeConnection getSeConnection() throws Exception {
        return determineSdeTargetDataSource().getSeConnection();
    }

    /**
     * return seConnection
     *
     * @param connection
     */
    @Override
    public void returnSeConnection(SeConnection connection) {
        determineSdeTargetDataSource().returnSeConnection(connection);
    }

    private SDEDataSource determineSdeTargetDataSource() {
        return (SDEDataSource) super.determineTargetDataSource();
    }

}
