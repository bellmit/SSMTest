package cn.gtmap.onemap.platform.support.fm;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-11 下午2:51
 */
public final class TplSelector extends BaseLogger implements TemplateMethodModel {

    private final static String DEFAULT_TPL = "YZT";
    private final static String TPL_RESOURCE = "omp-functions:tpl";

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public Object exec(List list) throws TemplateModelException {
        User user = SecHelper.getUser();
        try {
            if (user != null) {
                Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getId(), TPL_RESOURCE);
                for (Privilege privilege : privileges) {
                    for (Operation operation : privilege.getOperations()) {
                        if (Operation.VIEW.equals(operation.getName())) return privilege.getResource();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(getMessage("tpl.om.selector.error", e.getLocalizedMessage()));
        }
        return DEFAULT_TPL;
    }

}
