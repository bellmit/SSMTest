package cn.gtmap.onemap.server.log.audit;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import cn.gtmap.onemap.core.event.EntityEvent;
import cn.gtmap.onemap.core.template.TemplateRenderer;
import cn.gtmap.onemap.service.AuditService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-17
 */
public class HibernateEntityAuditor implements ApplicationListener<EntityEvent> {
    @Autowired
    private AuditService auditService;
    private Map<Class, EntityConfig> configs;
    private TemplateRenderer templateRenderer;

    public void setConfigs(Map<Class, EntityConfig> configs) {
        this.configs = configs;
    }

    public void setTemplateRenderer(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public void onApplicationEvent(EntityEvent event) {
        Object entity = event.getSource();
        EntityConfig ec = configs.get(entity.getClass());
        if (ec != null) {
            String tpl = null;
            switch (event.getType()) {
                case INSERT:
                    tpl = ec.getInsertTpl();
                    break;
                case UPDATE:
                    tpl = ec.getUpdateTpl();
                    break;
                case DELETE:
                    tpl = ec.getDeleteTpl();
                    break;
            }
            auditService.audit(ec.getCatalog(), tpl == null ? entity.toString() : templateRenderer.renderFragment(tpl, Collections.singletonMap("e", entity)));
        }
    }
}
