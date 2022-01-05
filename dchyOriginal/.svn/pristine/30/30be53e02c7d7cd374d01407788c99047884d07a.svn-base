package cn.gtmap.onemap.server.monitor.service.impl;

import static cn.gtmap.onemap.server.monitor.model.QInterface.interface$;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.gtmap.onemap.core.support.hibernate.Hibernates;
import cn.gtmap.onemap.core.support.hibernate.Repository;
import cn.gtmap.onemap.server.monitor.model.Interface;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import cn.gtmap.onemap.server.monitor.service.InterfaceManager;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-10
 */
@Transactional(readOnly = true)
public class InterfaceManagerImpl implements InterfaceManager {
    private Repository<Interface, Integer> interfaceDAO;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        interfaceDAO = Hibernates.repo(Interface.class, sessionFactory);
    }

    @Override
    public List<Interface> getInterfaces(int hostId) {
        return interfaceDAO.list(interface$.host.id.eq(hostId));
    }

    @Override
    public Interface getInterface(int id) {
        return interfaceDAO.load(id);
    }

    @Override
    public Interface getInterfaceByName(int hostId, String name) {
        return interfaceDAO.get(interface$.host.id.eq(hostId).and(interface$.name.eq(name)));
    }

    @Override
    @Transactional
    public Interface saveInterface(Interface inf) {
        return interfaceDAO.save(inf);
    }

    @Override
    @Transactional
    public void setInterfacesStatus(Collection<Integer> ids, Status status) {
        for (Integer id : ids) {
            Interface inf = getInterface(id);
            switch (status) {
                case ENABLED:
                    if (!inf.isEnabled()) {
                        inf.setEnabled(true);
                        interfaceDAO.save(inf);
                    }
                    break;
                case DISABLED:
                    if (inf.isEnabled()) {
                        inf.setEnabled(false);
                        interfaceDAO.save(inf);
                    }
                    break;
                case DELETED:
                    interfaceDAO.delete(inf);
                    break;
            }
        }
    }

	@Override
	@Transactional
	public void removeInterface(Interface inf) {
		interfaceDAO.delete(inf);
	}
}
