package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglAuthorize;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.serviceol.core.service.AuthorizeService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Autowired
    EntityMapper entityMapper;

    private final String SEPARATOR_STR = "_";

    // 字典列表map
    private final Map<String, DchyXmglAuthorize> dchyXmglAuthorizeMap = Maps.newHashMap();


    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/3/28 12:37
     * @description 重置授权信息
     */
    @PostConstruct
    @SystemLog(czmkMc = ProLog.CZMK_CZAUTH_MC, czmkCode = ProLog.CZMK_CZAUTH_CODE, czlxMc = ProLog.CZLX_RESET_MC, czlxCode = ProLog.CZLX_RESET_CODE)
    public synchronized void reSetAuthorize() {
        Example example = new Example(DchyXmglAuthorize.class);
        example.createCriteria().andEqualTo("sqlx", Constants.DCHY_XMGL_AUTHORIZE_SQLX_YXFW);
        List<DchyXmglAuthorize> dchyXmglAuthorizeList = entityMapper.selectByExample(new Example(DchyXmglAuthorize.class));
        dchyXmglAuthorizeMap.clear();
        if (CollectionUtils.isNotEmpty(dchyXmglAuthorizeList)) {
            List<DchyXmglAuthorize> temp;
            StringBuilder key = new StringBuilder();
            for (DchyXmglAuthorize dchyXmglAuthorize : dchyXmglAuthorizeList) {
                if (StringUtils.isNoneBlank(dchyXmglAuthorize.getRoleid(), dchyXmglAuthorize.getZylx(), dchyXmglAuthorize.getZyuri())) {
                    key.setLength(0);
                    key.append(dchyXmglAuthorize.getSqlx()).append(SEPARATOR_STR).append(dchyXmglAuthorize.getZylx()).append(SEPARATOR_STR).append(dchyXmglAuthorize.getZyuri()).append(SEPARATOR_STR).append(dchyXmglAuthorize.getRoleid());
                    dchyXmglAuthorizeMap.put(key.toString(), dchyXmglAuthorize);
                }
            }
        }
    }

    /**
     * @return java.util.List<cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglAuthorize>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: zylx
     * @param: sqlx
     * @param: zyuri
     * @param: roleId
     * @time 2021/3/28 12:37
     * @description
     */
    @Override
    public boolean checkAuthorized(String zylx, String sqlx, String zyuri, List<String> roleIdList) {
        boolean authorized = false;
//        System.out.println(zyuri);
        if (StringUtils.isNoneBlank(zylx, sqlx, zyuri) && CollectionUtils.isNotEmpty(roleIdList)) {
            StringBuilder key = new StringBuilder();
            for (String roleId : roleIdList) {
                key.setLength(0);
                key.append(sqlx).append(SEPARATOR_STR).append(zylx).append(SEPARATOR_STR).append(zyuri).append(SEPARATOR_STR).append(roleId);
                DchyXmglAuthorize dchyXmglAuthorize = dchyXmglAuthorizeMap.get(key.toString());
                if (null != dchyXmglAuthorize) {
                    authorized = true;
                    break;
                }
            }
        }
        return authorized;
    }
}
