package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.service.SDEManager;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-1-6 上午11:09
 */
@Service
public class SDEManagerImpl implements SDEManager {
    /**
     * 获取对应实例
     *
     * @param server
     * @param port
     * @return
     */
    public SeInstance getInstance(String server, int port) throws SeException {
        return new SeInstance(server, port);
    }

    /**
     * 开启sde服务
     *
     * @param server
     * @param port
     * @param database
     * @param password
     */
    public void startInstance(String server, int port, String database, String password) throws SeException {
        SeInstance instance = getInstance(server, port);
        if (instance != null) {
            instance.start(database, password);
        }
    }

    /**
     * 关闭sde服务
     *
     * @param server
     * @param port
     * @param password
     */
    public void stopInstance(String server, int port, String password) throws SeException {
        SeInstance instance = getInstance(server, port);
        if (instance != null) {
            instance.shutdown(password);
        }
    }

    /**
     * 获取sde配置信息
     *
     * @param server
     * @param port
     * @param password
     * @return
     */
    public SeInstance.SeInstanceConfiguration getConfiguration(String server, int port, String password) throws SeException {
        SeInstance instance = getInstance(server, port);
        if (instance != null) {
            return instance.getConfiguration();
        }
        return null;
    }

    /**
     * 获取app配置的sde
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> getAppSdes() throws Exception {
        Map proMap = AppConfig.getProperties();
        List<Map> sdeList = Lists.newArrayList((Map) Maps.newHashMap(), (Map) Maps.newHashMap());
        Map<String, Object> sdeMap = Maps.newHashMap();
        Pattern pattern = Pattern.compile("(sde\\d*)\\.(db).*");
        for (Object obj : proMap.keySet()) {
            String key = String.valueOf(obj);
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                sdeMap.put(key, proMap.get(key));
            }
        }
        if (!sdeMap.isEmpty()) {
            for (String k : sdeMap.keySet()) {
                //确定是第几个sde
                String[] vals = k.split("\\.");
                int index = StringUtils.isBlank(vals[0].substring(3)) ? 0 : Integer.valueOf(vals[0].substring(3));
                String sdeKey = vals[2];
                if (sdeList.size() > index) {
                    Map map = sdeList.get(index);
                    map.put(sdeKey, sdeMap.get(k));
                } else {
                    Map map = Maps.newHashMap();
                    map.put(sdeKey, sdeMap.get(k));
                    sdeList.add(map);
                }
            }
        }
        List<Map> result=Lists.newArrayList();
        for (Map map : sdeList) {
            if (!map.isEmpty()) result.add(map);
        }
        return result;
    }


}
