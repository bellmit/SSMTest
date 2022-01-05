package cn.gtmap.onemap.server.monitor.service;

import cn.gtmap.onemap.server.monitor.model.Interface;
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
public interface InterfaceManager {

    List<Interface> getInterfaces(int hostId);

    Interface getInterface(int id);

    Interface getInterfaceByName(int hostId, String name);

    Interface saveInterface(Interface inf);

    void setInterfacesStatus(Collection<Integer> ids, Status status);
    
    void removeInterface(Interface inf);
}
