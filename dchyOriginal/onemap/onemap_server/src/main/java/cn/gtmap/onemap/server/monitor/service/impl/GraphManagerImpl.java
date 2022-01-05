package cn.gtmap.onemap.server.monitor.service.impl;

import cn.gtmap.onemap.core.support.hibernate.Hibernates;
import cn.gtmap.onemap.core.support.hibernate.Repository;
import cn.gtmap.onemap.server.monitor.model.Graph;
import cn.gtmap.onemap.server.monitor.service.GraphManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static cn.gtmap.onemap.server.monitor.model.QGraph.graph;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-5
 */
public class GraphManagerImpl implements GraphManager {
    private Repository<Graph, Integer> graphDAO;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        graphDAO = Hibernates.repo(Graph.class, sessionFactory);
    }

    @Override
    public List<Graph> getGraphs(int hostId) {
        return graphDAO.list(graph.host.id.eq(hostId));
    }

    @Override
    public Graph getGraph(int id) {
        return graphDAO.load(id);
    }

    @Override
    @Transactional
    public Graph saveGraph(Graph graph) {
        return graphDAO.save(graph);
    }

    @Override
    @Transactional
    public void removeGraphs(Collection<Integer> ids) {
        graphDAO.deleteByPK(ids);
    }
}
