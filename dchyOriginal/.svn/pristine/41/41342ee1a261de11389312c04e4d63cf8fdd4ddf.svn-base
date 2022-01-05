package cn.gtmap.onemap.server.monitor.service.impl;

import cn.gtmap.onemap.core.support.hibernate.Hibernates;
import cn.gtmap.onemap.core.support.hibernate.Repository;
import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import cn.gtmap.onemap.server.monitor.service.HostManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static cn.gtmap.onemap.server.monitor.model.QHost.host;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-5
 */
@Transactional(readOnly = true)
public class HostManagerImpl implements HostManager {
    private Repository<Host, Integer> hostDAO;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        hostDAO = Hibernates.repo(Host.class, sessionFactory);
    }

    @Override
    public List<Host> getHosts() {
        return hostDAO.list();
    }

    @Override
    public Host getHost(int id) {
        return hostDAO.load(id);
    }

    @Override
    public Host getHostByName(String name) {
        return hostDAO.get(host.name.eq(name));
    }

    @Override
    @Transactional
    public Host saveHost(Host host) {
        return hostDAO.save(host);
    }

    @Override
    @Transactional
    public void setHostsStatus(Collection<Integer> ids, Status status) {
        for (Integer id : ids) {
            Host host = getHost(id);
            switch (status) {
                case ENABLED:
                    if (!host.isEnabled()) {
                        host.setEnabled(true);
                        hostDAO.save(host);
                    }
                    break;
                case DISABLED:
                    if (host.isEnabled()) {
                        host.setEnabled(false);
                        hostDAO.save(host);
                    }
                    break;
                case DELETED:
                    hostDAO.delete(host);
                    break;
            }
        }
    }

	@Override
	@Transactional
	public void removeHost(Host host) {
		hostDAO.delete(host);
	}
}
