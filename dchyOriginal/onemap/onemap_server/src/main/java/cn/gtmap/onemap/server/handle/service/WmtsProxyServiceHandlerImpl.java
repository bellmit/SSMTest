package cn.gtmap.onemap.server.handle.service;

import cn.gtmap.onemap.model.Service;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.model.ServiceType;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * wmts 代理服务
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/8/12 10:29
 */
public class WmtsProxyServiceHandlerImpl extends ArcgisServerProxyServiceHandlerImpl {


    /**
     * @param sp
     * @return
     */
    @Override
    public List<Service> getServices(ServiceProvider sp) {
        List<Service> ss = Lists.newArrayListWithCapacity(3);
        Service wmtsService = new Service();
        wmtsService.setServiceType(ServiceType.WMTS);
        wmtsService.setUrl(getServiceUrl(sp));
        ss.add(wmtsService);
        return ss;
    }
}
