package cn.gtmap.onemap.platform.service;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeInstance;

import java.util.List;
import java.util.Map;

/**
 * . sde管理
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-1-6 上午11:08
 */
public interface SDEManager {

    /**
     * 获取对应实例
     *
     * @param server
     * @param port
     * @return
     */
    SeInstance getInstance(String server, int port) throws SeException;

    /**
     * 开启sde服务
     *
     * @param server
     * @param port
     * @param database
     * @param password
     */
    void startInstance(String server, int port, String database, String password) throws SeException;

    /**
     * 关闭sde服务
     *
     * @param server
     * @param port
     * @param password
     */
    void stopInstance(String server, int port, String password) throws SeException;

    /**
     * 获取sde配置信息
     *
     * @param server
     * @param port
     * @param password
     * @return
     */
    SeInstance.SeInstanceConfiguration getConfiguration(String server, int port, String password) throws SeException;

    /***
     * 获取app配置的sde
     * @return
     * @throws Exception
     */
    List<Map> getAppSdes() throws Exception;

}
