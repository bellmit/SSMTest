package cn.gtmap.onemap.server.monitor.service;

import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.enums.Status;

import java.util.Collection;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-20
 */
public interface HostManager {

    List<Host> getHosts();

    Host getHost(int id);

    Host getHostByName(String name);

    Host saveHost(Host host);

    void setHostsStatus(Collection<Integer> ids, Status status);
    
    void removeHost(Host host);
}
