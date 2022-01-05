package cn.gtmap.onemap.platform.support.fm;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import cn.gtmap.onemap.platform.utils.AppPropertyUtils;
import cn.gtmap.onemap.security.*;
import com.gtis.config.AppConfig;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-12 上午8:51
 */
public class EnvContext extends BaseLogger {

    private final static String DEFAULT_TPL = "YZT";
    private final static String BLANK_TPL = "YZT_DEFAULT";
    private final static String TPL_RESOURCE = "omp-functions:tpl";
    private final static String ZT_RESOURCE = "omp-functions:zt";
    private final static String STATISTIC_RESOURCE="omp-functions:statistic";

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private TemplateService templateService;
    /**
     * 分析展示结果模板文件位置
     */
    private static final String ANALYSIS_FTL_DIR = "analysis/template/";

    private static final String TPL_SUFFIX = ".ftl";

    private static final String ANALSYIS_DEFAULT_TPL="default.ftl";


    /**
     * get main tpl
     *
     * @return
     * @throws TemplateModelException
     */
    public Object getMainTpl() throws TemplateModelException {
        if (SecHelper.isAdmin()) return DEFAULT_TPL;
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
        return BLANK_TPL;
    }

    /**
     * get zt authority
     *
     * @param name
     * @return
     */
    public boolean hasZtAuth(String name) {
        if (SecHelper.isAdmin() || SecHelper.isGuest()) return true;
        User user = SecHelper.getUser();
        if (user != null) {
            try {
                Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getId(), ZT_RESOURCE);
                for (Privilege privilege : privileges) {
                    if (!privilege.getResource().equals(name)) continue;
                    for (Operation operation : privilege.getOperations()) {
                        if (Operation.VIEW.equals(operation.getName())) return true;
                    }
                }
            } catch (Exception e) {
                logger.error(getMessage("user.zt.error", e.getLocalizedMessage()));
            }
        }
        return false;
    }
    /**
     * get statictic authority
     * @param name
     * @return
     */
    public boolean hasStatisAuth(String name) {
        if (SecHelper.isAdmin() || SecHelper.isGuest()) return true;
        User user = SecHelper.getUser();
        if (user != null) {
            try {
                Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getId(), STATISTIC_RESOURCE);
                for (Privilege privilege : privileges) {
                    if (!privilege.getTitle().equals(name)) continue;
                    for (Operation operation : privilege.getOperations()) {
                        if (Operation.VIEW.equals(operation.getName())) return true;
                    }
                }
            } catch (Exception e) {
                logger.error(getMessage("user.zt.error", e.getLocalizedMessage()));
            }
        }
        return false;
    }

    /**
     * get app env value
     *
     * @param key
     * @return
     */
    public Object getEnv(String key) {
        return isNull(key)?null:AppPropertyUtils.getAppEnv(key);
    }

    /**
     *   get regionCode of role
     * @return
     */
    public String getRegionCode(){
        if (SecHelper.isAdmin() || SecHelper.isGuest()) return null;
        User user = SecHelper.getUser();
        if(user==null)return null;
        return user.getRegionCode();
    }

    /**
     * abs double value
     * @param value
     * @return
     */
    public double absDouble(double value){
        return Math.abs(value);
    }

    /***
     * sum val of sequence
     * @param list
     * @param key
     * @return
     */
    public Object sumSequence(List<Map> list, String key) {
        assert list != null;
        Object tVal = MapUtils.getObject(list.get(0), key);
        if (tVal instanceof Double) {
            Double sum = 0.0;
            for (Map map : list) {
                sum += MapUtils.getDouble(map, key);
            }
            return sum;

        } else if (tVal instanceof String) {
            String sum = "";
            for (Map map : list) {
                sum += MapUtils.getString(map, key);
            }
            return sum;

        } else if (tVal instanceof Integer) {
            int sum = 0;
            for (Map map : list) {
                sum += MapUtils.getIntValue(map, key);
            }
            return sum;
        } else if (tVal instanceof BigDecimal) {
            BigDecimal sum = new BigDecimal(0.0);
            for (Map map : list) {
                sum = sum.add((BigDecimal) map.get(key));
            }
            return sum.doubleValue();
        } else {
            return null;
        }
    }

    /***
     * 获取配置的分析结果模板
     * @param data
     * @param tplName
     * @return
     */
    public String getAnalysisTpl(Map data, String tplName) {
        try {
            List<String> tpls = templateService.listTplNames(ANALYSIS_FTL_DIR);
            if (!isNull(tpls) && tpls.size() > 0) {
                if (tpls.contains(tplName.concat(TPL_SUFFIX)))
                    return templateService.getTemplate(data, ANALYSIS_FTL_DIR.concat(tplName.concat(TPL_SUFFIX)));
                else if (tpls.contains(ANALSYIS_DEFAULT_TPL))
                    return templateService.getTemplate(data, ANALYSIS_FTL_DIR.concat(ANALSYIS_DEFAULT_TPL));
            }
            return getMessage("template.not.exist", tplName.concat(TPL_SUFFIX));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取所有的sde的配置
     * @return
     */
    public Map getSdes(){
        Map map = new HashMap();
        for(Object key : AppConfig.getProperties().keySet()){
            if(key.toString().startsWith("sde") && key.toString().endsWith("dsname")){
                map.put(key, AppConfig.getProperties().get(key));
            }
        }
        return map;
    }

    public boolean isAdmin() {
        try {
            return SecHelper.isAdmin();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return false;
    }

}
