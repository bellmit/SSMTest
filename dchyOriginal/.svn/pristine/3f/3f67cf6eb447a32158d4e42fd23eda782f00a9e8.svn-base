package cn.gtmap.onemap.server.service.impl;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.model.AuditLog;
import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.server.dao.AuditLogDAO;
import cn.gtmap.onemap.service.AuditService;

import com.mysema.query.types.Predicate;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-17
 */
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditLogDAO auditLogDAO;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void audit(final AuditLog auditLog) {
        if (auditLog.getCreateAt() == null) {
            auditLog.setCreateAt(new Date());
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            auditLog.setIp(RequestUtils.getClientIP(sra.getRequest()));
        }
        cn.gtmap.onemap.security.User user = SecHelper.getUser();
        if (user instanceof User) {
            auditLog.setUser((User) user);
        }
        executor.submit(new Runnable() {
            @Override
            public void run() {
                auditLogDAO.save(auditLog);
            }
        });
    }

    @Override
    @Transactional
    public void audit(String catalog, String content) {
        AuditLog al = new AuditLog();
        al.setCatalog(catalog);
        al.setContent(content);
        audit(al);
    }

    @Override
    public Page<AuditLog> find(Predicate predicate, Pageable request) {
    	PageRequest req = new PageRequest(request.getPageNumber(), request.getPageSize(), Direction.DESC, "id");
        return auditLogDAO.findAll(predicate, req);
    }

    @Override
    @Transactional
    public void clean(Date startTime, Date endTime) {
    	if( startTime == null && endTime == null ){
    		auditLogDAO.deleteAll();
    	}
        auditLogDAO.clean(startTime, endTime);
    }
}
