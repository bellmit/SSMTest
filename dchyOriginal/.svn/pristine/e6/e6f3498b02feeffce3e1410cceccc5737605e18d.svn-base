package cn.gtmap.onemap.server.thirdparty.kanq;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.FactoryBean;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-21
 */
public class WsProxyFactoryBean<T> extends JaxWsProxyFactoryBean implements FactoryBean<T> {
    private int ReceiveTimeoutSecond = 10;
    private int connectionTimeoutSecond = 5;

    public void setReceiveTimeoutSecond(int receiveTimeoutSecond) {
        ReceiveTimeoutSecond = receiveTimeoutSecond;
    }

    public void setConnectionTimeoutSecond(int connectionTimeoutSecond) {
        this.connectionTimeoutSecond = connectionTimeoutSecond;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        Object client = create();
        Conduit conduit = (ClientProxy.getClient(client).getConduit());
        HTTPConduit hc = (HTTPConduit) conduit;
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setReceiveTimeout(ReceiveTimeoutSecond * 1000);
        policy.setConnectionTimeout(connectionTimeoutSecond * 1000);
        hc.setClient(policy);
        return (T) client;
    }

    @Override
    public Class<?> getObjectType() {
        return getServiceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
